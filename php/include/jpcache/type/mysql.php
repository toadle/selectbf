<?php

    /* jpcache_db_connect()
     *
     * Makes connection to the database
     */
    function jpcache_db_connect()
    {
        $GLOBALS["sql_link"] = @mysqli_connect($GLOBALS["JPCACHE_DB_HOST"], 
                                              $GLOBALS["JPCACHE_DB_USERNAME"],
                                              $GLOBALS["JPCACHE_DB_PASSWORD"]);
    }
    
    /* jpcache_db_disconnect()
     *
     * Closes connection to the database
     */
    function jpcache_db_disconnect()
    {
        if($GLOBALS["sql_link"] !== null) {
        	mysqli_close($GLOBALS["sql_link"]);
	}
    }
    
    /* jpcache_db_query($query)
     *
     * Executes a given query
     */    
    function jpcache_db_query($query)
    {
	jpcache_db_connect();
        // jpcache_debug("Executing SQL-query $query");
        @mysqli_select_db($GLOBALS["sql_link"], $GLOBALS["JPCACHE_DB_DATABASE"]);
        $ret = @mysqli_query($GLOBALS["sql_link"], $query);
        return $ret;
    }
    
    /* jpcache_restore()
     *
     * Will try to restore the cachedata from the db.
     */
    function jpcache_restore()
    {
        $res = jpcache_db_query("select GZDATA, DATASIZE, DATACRC from ".
                                    $GLOBALS["JPCACHE_DB_TABLE"].
                                " where CACHEKEY='".
                                    addslashes($GLOBALS["jpcache_key"]).
                                "' and (CACHEEXPIRATION>".
                                    time().
                                " or CACHEEXPIRATION=0)"
                               );
                                
        if ($res && mysqli_num_rows($res))
        {
            if ($row = mysqli_fetch_array($res))
            {
                // restore data into global scope from found row
                $GLOBALS["jpcachedata_gzdata"]   = $row["GZDATA"];
                $GLOBALS["jpcachedata_datasize"] = $row["DATASIZE"];
                $GLOBALS["jpcachedata_datacrc"]  = $row["DATACRC"];
                return true;
            }
        }
        return false;
    }

    /* jpcache_write()
     *
     * Will (try to) write out the cachedata to the db
     */
    function jpcache_write($gzdata, $datasize, $datacrc) 
    {
        $dbtable = $GLOBALS["JPCACHE_DB_TABLE"];
        
        // XXX: Later on, maybe implement locking mechanism inhere.
        
        // Check if it already exists
        $res = jpcache_db_query("select CACHEEXPIRATION from $dbtable".
                                " where CACHEKEY='".
                                    addslashes($GLOBALS["jpcache_key"]).
                                "'"
                               );
        
        
        if (!$res || mysqli_num_rows($res) < 1) 
        {
            // Key not found, so insert
            $res = jpcache_db_query("insert into $dbtable".
                                    " (CACHEKEY, CACHEEXPIRATION, GZDATA,".
                                    " DATASIZE, DATACRC) values ('".
                                        addslashes($GLOBALS["jpcache_key"]).
                                    "',".
                                        (($GLOBALS["JPCACHE_TIME"] != 0) ? 
                                        (time()+$GLOBALS["JPCACHE_TIME"]) : 0).
                                    ",'".
                                        addslashes($gzdata).
                                    "', $datasize, $datacrc)"
                                   );
            // This fails with unique-key violation when another thread has just
            // inserted the same key. Just continue, as the result is (almost) 
            // the same.
        }
        else
        {
            // Key found, so update
            $res = jpcache_db_query("update $dbtable set CACHEEXPIRATION=".
                                        (($GLOBALS["JPCACHE_TIME"] != 0) ?
                                        (time()+$GLOBALS["JPCACHE_TIME"]) : 0).
                                    ", GZDATA='".
                                        addslashes($gzdata).
                                    "', DATASIZE=$datasize, DATACRC=$datacrc where".
                                    " CACHEKEY='".
                                        addslashes($GLOBALS["jpcache_key"]).
                                    "'"
                                   );
            // This might be an update too much, but it shouldn't matter
        }
    }
   
    /* jpcache_do_gc()
     *
     * Performs the actual garbagecollection
     */
    function jpcache_do_gc()
    {
        jpcache_db_query("delete from ".
                            $GLOBALS["JPCACHE_DB_TABLE"].
                         " where CACHEEXPIRATION<=".
                            time().
                         " and CACHEEXPIRATION!=0"
                        );
                        
        // Are we allowed to do an optimize table-call?
        // As noted, first check if this works on your mysql-installation!
        if ($GLOBALS["JPCACHE_OPTIMIZE"])
        {
            jpcache_db_query("OPTIMIZE TABLE ".$GLOBALS["JPCACHE_DB_TABLE"]);                       
        }
    }

    
    /* jpcache_do_start()
     *
     * Additional code that is executed before main jpcache-code kicks in.
     */
    function jpcache_do_start()
    {  
        // Connect to db
        jpcache_db_connect();
    }

    /* jpcache_do_end()
     *
     * Additional code that is executed after caching has been performed,
     * but just before output is returned. No new output can be added!
     */
    function jpcache_do_end()
    {
        // Disconnect from db
        jpcache_db_disconnect();
    }
    
?>
