<?php

    // jpcache configuration file
    //
    // Some settings are specific for the type of cache you are running, like
    // file- or database-based.
    // 

    /**
     * Define which jpcache-type you want to use (storage and/or system).
     *
     * This allows for system-specific patches & handling, as sometimes
     * 'platform independent' is behaving quite differently.
     *
     * Uncomment only the one you want to use in the following lines! 
     */
     
    $JPCACHE_TYPE = "file";
    //$JPCACHE_TYPE = "mysql";
    //$JPCACHE_TYPE = "dbm"; -- Not yet

    // DOH! Strip out this check for performance if you are sure you did set it.
    if (!isset($JPCACHE_TYPE))
    {
        exit("[jpcache-config.php] No JPCACHE_TYPE has been set!");
    }
    
    /**
     * General configuration options. 
     */
    $JPCACHE_TIME         =   900; // Default number of seconds to cache a page
    $JPCACHE_DEBUG        =   0;   // Turn debugging on/off
    $JPCACHE_IGNORE_DOMAIN=   1;   // Ignore domain name in request(single site)
    $JPCACHE_ON           =   1;   // Turn caching on/off
    $JPCACHE_USE_GZIP     =   1;   // Whether or not to use GZIP
    $JPCACHE_POST         =   0;   // Should POST's be cached (default OFF)
    $JPCACHE_GC           =   1;   // Probability % of garbage collection
    $JPCACHE_GZIP_LEVEL   =   9;   // GZIPcompressionlevel to use (1=low,9=high)
    $JPCACHE_CLEANKEYS    =   0;   // Set to 1 to avoid hashing storage-key:
                                   // you can easily see cachefile-origin.



    /**
     * File based caching setting.
     */
    $JPCACHE_DIR          = "<some writable dir>"; 
								   // Directory where jpcache must store 
                                   // generated files. Please use a dedicated
                                   // directory, and make it writable
    $JPCACHE_FILEPREFIX   = "jpc-";// Prefix used in the filename. This enables
                                   // us to (more accuratly) recognize jpcache-
                                   // files.

    /**
     * DB based caching settings. 
     */
    $JPCACHE_DB_HOST      = "localhost"; // Database Server
    $JPCACHE_DB_DATABASE  = "jpcache";   // Database-name to use
    $JPCACHE_DB_USERNAME  = "sqluser";   // Username 
    $JPCACHE_DB_PASSWORD  = "passwd";    // Password
    $JPCACHE_DB_TABLE     = "CACHEDATA"; // Table that holds the data
    $JPCACHE_OPTIMIZE     = 1;           // If 'OPTIMIZE TABLE' after garbage
                                         // collection is executed. Please check 
                                         // first if this works on your mySQL!
?>