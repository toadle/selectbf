# select(bf) A Battlefield 1942 XML Log Parsing Tool and Statistics generator

## Foreword / Disclaimer

**This project is from 2005. Full scale development has ceased for about 10 years now.**  Use at you own discretion. Know 

In 2022 PHP8-compatibility was [added](https://github.com/toadle/selectbf/pull/1) by [htn](https://github.com/htn1491) through of [HelloClan](https://www.helloclan.eu). He also fixed a lot of SQL-injection problems. They still run BF Vietnam servers and select(bf) based statistics.
So if YOU want to run this code on PHP 8. You can clone this repo and use the PHP-code provided here.

That time I had to set up a Battlefield server for a LAN-Party and I wanted to have some Stats for the players to see. Unfortunately I found nothing that came close to what I was searching for. So I decided to create a tool of my own. Having features that people are used to from watching CounterStrike-statistic pages. The Battlefield Logs at the time anyhow contained some more information, so that it is possible to even keeptrack of specific games and round infos. But see for yourself.

## For your consideration

Please note: The Battlefield XML Log Format itself does not seem to be very bugfree. I had
lots of files during the testing that showed either flaws in the XML-structure or contained
errors on the `<bf:event>`-level so that an event is not always written the same.
I tried to make this little tool as tolerant as possible but still it sometimes rejects files
because of flaws in the XML-Log itself.

## Thanks

First of all Thanks to Andy Abshagen who joined this little project here in the meantime and
provided an openly accessible CVS for everybody. Also he contributed lots of idea and codes to
enhance our little tool here. Also to Gary Chiu, who contributed his Clan Stats !!

Also I'd like to thank all the guys that joined to select(bf) forums and gave their
cheers for this project. That's what makes the OpenSource-World go round :)!

Also thx to the people (actually 2 up til now _g_) who donated a little amount of money
to my Paypal-Account. Especially to that guy, who's donation was worth more than two
completely new Battlefields :)!

Thanks to Andreas Fredriksson of DICE of the Battlefield development team who wrote the XML
Logging system and supplied some valuable answers on this one.

Thanks to everybody else that submitted errors and suggestions at http://www.selectbf.org.
Especially to the guys who modded s(bf) to fit their needs and some that wrote little code-bits
to help others making good use of this little thing.

Thanks to the creators of the three freesoftware-projects I used to create select(bf). Without
them making their software freely available this could never have been done.

## INSTALL/USE INSTRUCTIONS

### About Updating and Changes in 0.5 beta

Since the last release of select(bf) times have changed and even MySQL has gotten better ;)
There it is more strict regarding values that are written into typed columns. Since the
older releases of s(bf) had very strict column-settings, which were not very tightly matched
by the data in the log-files, those settings have been losened in this release. So you
either have to recreate your database using the \_setup.php OR update your datamodell using the
\_setup.php. This method is undestructive, but your should always have a
backup handy :)!

Due to the popular demand I have added or updated a few things to select(bf)
in this little release. First of all: All MySQL-errors should be fixed. Means that all
of the "Unexpected End of input-stream" and 'mod'-issues are hopefully gone. If not,
let me know, we'll track down those stupid little glitches.
Perhaps even some FTP-issues are gone now, cause I added a new FTP-library version. I cannot
test that, so plz give me feedback how the outcome is for you guys!
Additionally I have added PHP-output caching to select(bf). For all of you guys out
there, who have CPU-trouble during database-requests. Sadly this means A LITTLE(!) more
configuration work for you to do, but believe me, I tried that today, the outcome is really
good (also, there are defaults in place, which should work from the start). The default
caching time of the stats is 15 minutes. Therefore new data will NOT pop up immidiately.
But you can disable caching completely through the config. See below.

### What you need to run this thing

**PARSER**

- Java Runtime Environment 1.4 or higher installed it is available freely at http://java.sun.com
  Be aware to download their offline-installations. The online-installations are somewhat creepy.
  You can also install the Java Software Development Kit 1.4 or higher it comes with the JRE.

**WEBSERVER**

- PHP 4.3.2 http://www.php.net
  NOTICE: This the version that select(bf) has been tested with. It will probably work with earlier
  versions also.

**DATABASE**

- MySQL 4.0.13 http://www.mysql.com
  NOTICE: This the version that select(bf) has been tested with. It will probably work with earlier
  versions also.

### How you run this thing

#### Step One

First uncompress the contents of the archive you downloaded to a desired directory.

Archive structure:
`bin` - Contains the binaries of select(bf), the config-file and batch-files to run it.
`lib` - Contains the "connector/j", "jdom" and "commons-net" libraries needed to run the selectbf-parser.
`php` - Contains all the files that you later shoot into your webserver
`src` - Contains the source-files of the select(bf)-parser
`doc` - Contains the documentation and readme-files, but you already know that

#### Step Two

Create a database for the data that select(bf) collects, or gather choose one that you
already have. You'll need the authentication and connect information.

Adjusting the parser-config (whatever you do in here. Don't mess up the config-files XML structure
or the parser will not be able to read it)

- Open the `config.xml` file in the `bin`-directory.

- Now you'll have to set the options you want in this file (its the config for the parser)

  - Set all database connection information to those of the database you want the parser to use.

  - Set die `after-parsing` to one of the following values:
    `remain` - for leaving the files where they are after parse
    `rename` - to add `.parsed` to the end of every parsed file
    `delete` - to delete the file after parsing
    `archive` - to have the files moved to a different directory

  - Set the `archive-folder` to where you want the files moved to

  - Set die `after-download` to one of the following values:
    `remain` - for leaving the files where they are after download
    `rename` - to add `.downloaded` to the end of every downloaded file
    `delete` - to delete the file after downloading  
     This determines what happens to log files, after they have been download by the select(bf)-parser
    through FTP. Please take care that the certain rights for this actions are allowed on the FTP-Server.

  - Set the `delete-decompressed-xml-files` to one of the following values:
    `true` - for deleting files that were decompressed zxmls after processing
    `false` - for leaving them where they are after processing

  - Set the `rename-at-error` to one of the following values:
    `true` - for renaming files that showed errors to `\*.error`
    `false` - for don't doing that

  - Set the `log-bots` to one of the following values:
    `true` - for accounting bots to your stats
    BE AWARE: select(bf) will only log bots if they have `createPlayer` events
    in the log files, which is not the case at the moment (at least in
    vanilla BF1942). This little restriction came with the tracking down
    of changing Player-IDs during a round.
    `false` - for leaving them out

  - Set the `trim-database` values
    You can set the value between the <trim-database>-tags to `true` if you want the datbase to be
    trimmed at the end of parsing. By default, only outdated games will be removed
    `days` - you should set the <trim-database>-tag attribute to the number of days you want to keep in the database
    `keep-players` - this value can either be `true` or `false`
    You should set it to `true` if you want to keep players even if they haven't
    played on the server within the given time. Otherwise they are also removed.
    BE AWARE: If you plan to keep you database trimmed to a certain amount of days, I URGENTLY
    advise you to reset your stats first. Also choose a very small interval of adding new logs to
    your database. You have to know: deleting events from the database can be as time-consuming
    as selecting data from it. So if you have a large amount of games in there that are outdated
    it will take a certain amount of time to trim them down. If you REALLY have a HUGE amount
    of outdated games, this trimming procedure will take a HUGE amount of time!!

  - Set the `skip-empty-rounds` to one of the following values:
    `true` - to not import empty rounds from the logs
    `false` - to import all rounds from the logs

  - Set the `lan-mode` to one of the following values:
    `true` - if player should be tracked by their nickname
    `false` - if player should be tracked by their CD-KEY
    BE AWARE: Don't mix both modes. That can get itchy!

  - Set the `consistency-check` to one of the following values:
    `true` - for enabling the consistency checker
    `false` - for disabling
    BE AWARE: The consistency checker might change your XML-files. It checks the files for
    probable inconsistency in the XML-structure. Corrections/Changes made to the log file
    are marked with a comment so that you can find them in case something goes wrong.
    Also the consistency-check takes a bit more time than simple parsing.

  - Set the `memory-safer` to one of the following values:
    `true` - for enabling the memory safer
    `false` - for disabling
    BE AWARE: The memory safer clears up unused memory after every file parsed. Therefore
    the complete parsing process might take longer.

  - Adjust the `<log>` section of the XML-file

    - **Configuring directories**
      You can open a `<dir>` tag for every directory that contains log files and that you want the parser to go
      through. The examples should lead you the way. Also you can set the special attribute `live` of the
      dirs to one of the following values:
      `true` - for considering this dir as a live-server dir (this will always leave out
      the most actual file in the dir because this is probably the file under
      BF1942 server-access)
      BE AWARE: Don't place any newer files in the dir cause in that case this file
      will be considered most actual. There is no better solution for this because
      under Windows the log under server-access is not locked in any way
      `false` - for not doing the above described
      Between the `<dir>` and `</dir>` tag, please write the name of the directory that holds the log files.
      NOTICE: Only use ABSOLUTE dir names. In any other case select(bf) will not be able to find them.

    - **Configuring FTP**
      You can also add a `<ftp>` tag for every FTP-location you want to gather logs from. Please take care
      that you also set the following values for the `<ftp>`-tag:
      `host` - containg either the DNS-name of the FTP-server that you want to get the files from
      or a valid IP-address
      `port` - the port that the FTP-server is on
      `user` - the FTP-user you'd like to use, to access the logs
      `password` - the password for this FTP-user
      `live` - please see the <dir>-part for the explanation of this value
      Between the <ftp> and </ftp> tag, please write the directory on the FTP-server that holds the log-files.
      Please consider, that you'll have to use a directory-path that originates from the directory that the
      user you configured starts in. For example if you use a user who starts in `c:\program files\battlefield server\`
      and your logs files reside in `c:\program files\battlefield server\mods\bf1942\logs` the tags would look like this
      <ftp ...>mods/bf1942/logs</ftp.
      NOTICE: If the user you use already starts in the right directory, simply write nothing between the tags (<ftp ...></ftp>)

  **PLEASE NOTE:** The <dir> and <ftp> tags are not mandatory. If there is something that
  you don't need, then simply delete it!

Now you need to adjust one php-file so that the viewing pages can find the database

- Go to the `php/include`-directory and open `sql_setting.php`
  And adjust ONLY the following values:
  `$SQL_host = <host's address>`;
  `$SQL_user = <user name>`;
  `$SQL_datenbank = <database name>`;
  `$SQL_password = <database password>";
- [NEW in 0.5] Configure the caching:
  The location of the configuration of the PHP-cache is set to sensible defaults from the begining, and should work
  instantly. BUT IF you should receive completely white pages after install or see PHP-errors in the page mentioning
  jpcache. Got about an do the following.

  Go to the `php/include/jpcache`-directory and open `jpcache.php`
  Adjust the value for
  `$includedir = "<jpcache-path>";`
  to the path, where the JPCACHE-files are.

  After that you need to open the `jpcache-config.php`-file and adjust
  the settings there. This is the configuration for the Cache that select(bf)
  uses. You may simply read the `jpcache/readme`-file in order to get detailed
  information about what you can to with the cache. I myself used the `file`-caching
  where you only need to uncomment the line with
  `$JPCACHE_TYPE = "file"`;
  and put some value in
  `$JPCACHE_DIR = "<some valid directory, you created>";`
  But if I understand the thing correctly, you may also use MySQL-based caching.

  After all, you can completely disable caching in the `jpcache-config.php`!

#### Step Three

Shoot the PHP-Files to the desired dir of your webserver.

After that open your browser and goto http://your-selectbf-website/\_setup.php!

Now choose a desired password for the Admin-Mode.
Now push the "Create Tables" to create all the tables needed by select(bf).

The "Drop Tables" Button is for deleting all select(bf) tables from your database.
NOTICE: During public access better remove the \_setup.php from your webserver!

#### Step Four

Run the parser. Therefore you can either use

`run-selectbf.bat`: For full select(bf)-output to the console
`run-selectbf-slient.bat`: For running select(bf) silent and write the output to "selectbf.out"

For our Unix friends there are similar files. Simply look in the bin :)!

#### Step Five

Watch your stats at http://your-selectbf-website

## Parser behaviour

### File-extensions

The parser will determine the files to parser from their extensions. Meaning that he will try to parser
EVERY .xml in a log-dir for a simple BF1942-log and tries to decompress every .zxml-file first.
So please take care that you have every file properly named. This is the same for FTP. The parser will
only download files that are either named .xml or .zxml

### Filenames

Please note that the Parser somehow depends on the original BF1942-filenames for you log-files. Meaning
that you better not change them, or you risk a non-conclusive outcome. The parser uses the original
filename to determine the start of the game that this log was written for. If this is not possible, meaning
that the filename is not BF-conform anymore, then the parser will take the Last-Modified Date of the
file as the game-starttime to at least get as close as possible to the original starttime.

## For our UNIX-Friends

Even though select(bf) is developed under a pure Windows environment, all the technics used are
platform independed. Meaning that the Java-Parser is working without any new compilation for Linux
also. You'll just have to use the other set of batch-files.

`selectbf.sh` For full select(bf)-output to the console
`selectbfsilent.sh` For running select(bf) silent and write the output to "selectbf.out"

There should be no problems and/or differences in the outcome comparing to the Windows-usage. But still
you'll have to download a Linux-JRE for running Java, but you guessed that right ;-)?

## Troubles

There are sometimes troubles with the data-structure or logic of some log-files. This is because
Battlefield is not very consistent with it's log writing. There are sometimes heavy XML-errors
or missing informations (missing informations should only appear in very small files). I can't
do something about that because the errors are made on BF-side and s(bf) skips those files to
keep the data consistent.
If you experience anything that doesn't look like it should be or you have any suggestions,
feel free to post them at http://forum.selectbf.org

## License

select(bf) is published under the conditions of the General Public License (GPL)
Therefore this program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

select(bf) makes use of some other projects that are believed to be be freely available.
They are either packed with select(bf) as a library or integrated with the code.
There to mention:

Connector/J available under GPL (http://www.mysql.com/)

JDOM available under Apache-style open source license (http://www.jdom.org/)

JZLIB available under GPL (http://www.jcraft.com/jzlib/)

Jakarta Commons/Net available under Apache Software License, Version 1.3 (http://jakarta.apache.org)
Jakarta Commons/ORO available under Apache Software License, Version 2.0.8 (http://jakarta.apache.org)

vLib Template (http://vlib.activefish.com [Dunno where that page has gone])
jpcache v2 for PHP available under GPL (http://www.jpcache.com/)

Please see further below for license informations regarding the above products!

---

**NOTICE: No license harm is intended with select(bf) so. Please contact us if you do not aggree how select(bf) does things.**
