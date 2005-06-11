<?php

    // Template for other types of cache 
    // You'll need to implement these 5 functions, add
    // additional functions inhere. 
    //
    // Add variables you use to jpcache-config.php
    //
    // When you've implemented a new storage-system, and think that the world
    // could/should use it too, please submit it to me (jp@jpcache.com),
    // and I'll include it in a next release (with full credits, ofcourse).
    
    
    /* jpcache_restore()
     *
     * Will (try to) restore the cachedata.
     */
    function jpcache_restore()
    {
        global $JPCACHE_TIME, $cache_key, $cachedata_gzdata, $cachedata_datasize, $cachedata_datacrc;
        
        // Implement restoring of cached data
        // 
        // Use $cache_key to lookup data, use $JPACHE_TIME to check if
        // data is still valid.
        //
        // If data-retrieval was succesfull, you'll need to set
        // $cachedata_gzdata, $cachedata_datasize and $cachedata_datacrc
        // and return true, if unsuccesfull, return false.
        
        return false;
    }

    /* jpcache_write()
     *
     * Will (try to) write out the cachedata to the db
     */
    function jpcache_write($gzcontents, $size, $crc32) 
    {
        global $JPCACHE_TIME, $JPCACHE_ON, $cache_key;
        
        // Implement writing of the data as given. 
        // Store on $cache_key and add a 'field' for $JPCACHE_TIME
        // Store the 3 parameters seperatly
    }
    
    /* jpcache_do_gc()
     *
     * Performs the actual garbagecollection
     */
    function jpcache_do_gc()
    {
        // Implement garbage-collection
    }


    /* jpcache_do_start()
     *
     * Additional code that is executed before real jpcache-code kicks
     */
    function jpcache_do_start()
    {  
        // Add additional code you might require
    }

    /* jpcache_do_end()
     *
     * Additional code that us executed after caching has been performed,
     * but just before output is returned. No new output can be added.
     */
    function jpcache_do_end()
    {
        // Add additional code you might require
    }

    // Make sure no additional lines/characters are after the closing-tag!
?>