<?php

    /* Take a wild guess... */
    function jpcache_debug($s) 
    {
        static $jpcache_debugline;

        if ($GLOBALS["JPCACHE_DEBUG"]) 
        {
            $jpcache_debugline++;
            header("X-CacheDebug-$jpcache_debugline: $s");
        }
    }
    
    /* jpcache_key()
     *
     * Returns a hashvalue for the current. Maybe md5 is too heavy, 
     * so you can implement your own hashing-function. 
     */
    function jpcache_key()
    {
            if ($GLOBALS["JPCACHE_CLEANKEYS"])
            {
                $key = eregi_replace("[^A-Z,0-9,=]", "_", jpcache_scriptkey());
                $key .= ".".eregi_replace("[^A-Z,0-9,=]", "_", jpcache_varkey());
                if (strlen($key) > 255)
                {
                    // Too large, fallback to md5!
                    $key = md5(jpcache_scriptkey().jpcache_varkey());
                }
            }
            else
            {
                $key = md5(jpcache_scriptkey().jpcache_varkey());
            }
            jpcache_debug("Cachekey is set to $key");
            return $key;
    }

    /* jpcache_varkey()
     * 
     * Returns a serialized version of POST & GET vars
     * If you want to take cookies into account in the varkey too, 
     * add them inhere.
     */
    function jpcache_varkey() 
    {
        $varkey = "";
        if ($GLOBALS["JPCACHE_POST"])
        {
            $varkey = "POST=".serialize($_POST); 
        }
        $varkey .= "GET=".serialize($_GET);
        jpcache_debug("Cache varkey is set to $varkey");
        return $varkey;
    }

    /* jpcache_scriptkey()
     *
     * Returns the script-identifier for the request
     */
    function jpcache_scriptkey()
    {
        // These should be available, unless running commandline
        if ($GLOBALS["JPCACHE_IGNORE_DOMAIN"])
        {
            $name=$_SERVER["PHP_SELF"];
        } 
        else 
        {
            $name=$_SERVER["SCRIPT_URI"];
        }

        // Commandline mode will also fail this one, I'm afraid, as there is no
        // way to determine the scriptname
        if ($name=="")
        {
            $name="http://".$_SERVER["SERVER_NAME"].$_SERVER["SCRIPT_NAME"];
        }
        
        jpcache_debug("Cache scriptkey is set to $name");        
        return $name;
    }


    /* jpcache_check()
     *
     */
    function jpcache_check() 
    {
        if (!$GLOBALS["JPCACHE_ON"]) 
        {
            jpcache_debug("Cache has been disabled!");
            return false;
        }
        
        // We need to set this global, as ob_start only calls the given method
        // with no parameters.
        $GLOBALS["jpcache_key"] = jpcache_key();
        
        // Can we read the cached data for this key ?
        if (jpcache_restore())
        {
            jpcache_debug("Cachedata for ".$GLOBALS["jpcache_key"]." found, data restored");    
            return true;
        } 
        else 
        {
            // No cache data (yet) or unable to read
            jpcache_debug("No (valid) cachedata for ".$GLOBALS["jpcache_key"]);
            return false;
        }
    }
    
    /* jpcache_encoding()
     *
     * Are we capable of receiving gzipped data ?
     * Returns the encoding that is accepted. Maybe additional check for Mac ?
     */
    function jpcache_encoding()
    { 
        if (headers_sent() || connection_aborted())
        { 
            return false; 
        } 
        if (strpos($_SERVER["HTTP_ACCEPT_ENCODING"],'x-gzip') !== false)
        {
            return "x-gzip";
        }
        if (strpos($_SERVER["HTTP_ACCEPT_ENCODING"],'gzip') !== false)
        {
            return "gzip";
        }
        return false; 
    }

    /* jpcache_init()
     *
     * Checks some global variables and might decide to disable caching
     */
    function jpcache_init()
    {
        // Override default JPCACHE_TIME ?
        if (isset($GLOBALS["cachetimeout"]))
        {
            $GLOBALS["JPCACHE_TIME"]=$GLOBALS["cachetimeout"];
        }
        
        // Force gzip off if gzcompress does not exist
        if (!function_exists('gzcompress')) 
        {
        	$GLOBALS["JPCACHE_USE_GZIP"]  = 0;
        }

        // Force cache off when POST occured when you don't want it cached
        if (!$GLOBALS["JPCACHE_POST"] && (count($_POST) > 0)) 
        {
            $GLOBALS["JPCACHE_ON"] = 0;
            $GLOBALS["JPCACHE_TIME"] = -1;
        }
        
        // A cachetimeout of -1 disables writing, only ETag and content encoding
        if ($GLOBALS["JPCACHE_TIME"] == -1)
        {
            $GLOBALS["JPCACHE_ON"] = 0;
        }
        
        // Output header to recognize version
        header("X-Cache: jpcache v".$GLOBALS["JPCACHE_VERSION"].
                " - ".$GLOBALS["JPCACHE_TYPE"]);
    }

    /* jpcache_gc()
     *
     * Checks if garbagecollection is needed.
     */
    function jpcache_gc()
    {
        // Should we garbage collect ?
        if ($GLOBALS["JPCACHE_GC"]>0) 
        {
            mt_srand(time(NULL));
            $precision=100000;
            // Garbagecollection probability
            if (((mt_rand()%$precision)/$precision) <=
                ($GLOBALS["JPCACHE_GC"]/100)) 
            {
                jpcache_debug("GarbageCollection hit!");
                jpcache_do_gc();
            }
        }
    }

    /* jpcache_start()
     *
     * Sets the handler for callback
     */
    function jpcache_start()
    {
        // Initialize cache
        jpcache_init();

        // Handle type-specific additional code if required
        jpcache_do_start();
  
        // Check cache
        if (jpcache_check())
        {
            // Cache is valid and restored: flush it!
            print jpcache_flush($GLOBALS["jpcachedata_gzdata"], 
                                $GLOBALS["jpcachedata_datasize"], 
                                $GLOBALS["jpcachedata_datacrc"]);
            // Handle type-specific additional code if required
            jpcache_do_end();
            exit;
        }
        else
        {
            // if we came here, cache is invalid: go generate page 
            // and wait for jpCacheEnd() which will be called automagically
            
            // Check garbagecollection
            jpcache_gc();
            
            // Go generate page and wait for callback
            ob_start("jpcache_end");
            ob_implicit_flush(0);
        }
    }

    /* jpcache_end()
     *
     * This one is called by the callback-funtion of the ob_start. 
     */
    function jpcache_end($contents)
    {
        jpcache_debug("Callback happened");
        
        $datasize = strlen($contents);
        $datacrc = crc32($contents);
        
        if ($GLOBALS["JPCACHE_USE_GZIP"]) 
        {
            $gzdata = gzcompress($contents, $GLOBALS["JPCACHE_GZIP_LEVEL"]);
        } 
        else 
        {
            $gzdata = $contents;
        }
        
        // If the connection was aborted, do not write the cache.
        // We don't know if the data we have is valid, as the user
        // has interupted the generation of the page.
        // Also check if jpcache is not disabled
        if ((!connection_aborted()) && 
             $GLOBALS["JPCACHE_ON"] && 
            ($GLOBALS["JPCACHE_TIME"] >= 0))
        {
            jpcache_debug("Writing cached data to storage");
            // write the cache with the current data
            jpcache_write($gzdata, $datasize, $datacrc);
        }
        
        // Handle type-specific additional code if required
        jpcache_do_end();

        // Return flushed data
        return jpcache_flush($gzdata, $datasize, $datacrc);
    }

    /* jpcache_flush()
     *
     * Responsible for final flushing everything.
     * Sets ETag-headers and returns "Not modified" when possible
     *
     * When ETag doesn't match (or is invalid), it is tried to send
     * the gzipped data. If that is also not possible, we sadly have to
     * uncompress (assuming JPCACHE_USE_GZIP is on)
     */
    function jpcache_flush($gzdata, $datasize, $datacrc)
    {       
        // First check if we can send last-modified
        $myETag = "\"jpd-$datacrc.$datasize\"";
        header("ETag: $myETag");
        $foundETag = isset($_SERVER["HTTP_IF_NONE_MATCH"]) ? stripslashes($_SERVER["HTTP_IF_NONE_MATCH"]) : "";
        $ret = NULL;
        
        if (strstr($foundETag, $myETag))
        {
            // Not modified!
            if(stristr($_SERVER["SERVER_SOFTWARE"], "microsoft"))
    	    {
    	        // IIS has already sent a HTTP/1.1 200 by this stage for
    	        // some strange reason
                header("Status: 304 Not Modified");  
            } 
            else 
            {
                header("HTTP/1.0 304");
            }
        }
        else
        {
            // Are we gzipping ?
            if ($GLOBALS["JPCACHE_USE_GZIP"]) 
            {
                $ENCODING = jpcache_encoding(); 
                if ($ENCODING) 
                { 
                    // compressed output: set header. Need to modify, as
                    // in some versions, the gzipped content is not what
                    // your browser expects.
                    header("Content-Encoding: $ENCODING");
                    $ret =  "\x1f\x8b\x08\x00\x00\x00\x00\x00";
                    $ret .= substr($gzdata, 0, strlen($gzdata) - 4);
                    $ret .= pack('V',$datacrc);
                    $ret .= pack('V',$datasize);
                } 
                else 
                {
                    // Darn, we need to uncompress :(
                    $ret = gzuncompress($gzdata);
                }
            } 
            else 
            {
                // So content isn't gzipped either
                $ret=$gzdata;
            }
        }
        return $ret;
    }
    
?>