<?
require_once("include/vLib/vlibTemplate.php");
use clausvb\vlib\vlibTemplate;
require_once("include/sql.php");

function createAllTables()
{
		SQL_query("CREATE TABLE `selectbf_admin` (  `id` int(10) signed NOT NULL auto_increment,  `name` text,  `value` text,  `inserttime` datetime default NULL,  PRIMARY KEY  (`id`),  UNIQUE KEY `id` (`id`),  KEY `id_2` (`id`)) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_cache_chartypeusage` (  `kit` varchar(255) NOT NULL default '',  `percentage` float default NULL,  `times_used` int(10) signed default NULL,  PRIMARY KEY  (`kit`)) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_cache_mapstats` (  `map` varchar(100) NOT NULL default '0',  `wins_team1` int(10) signed default NULL,  `wins_team2` int(10) signed default NULL,  `win_team1_tickets_team1` float default NULL,  `win_team1_tickets_team2` float default NULL,  `win_team2_tickets_team1` float default NULL,  `win_team2_tickets_team2` float default NULL,  `score_team1` int(10) signed default NULL,  `score_team2` int(10) signed default NULL,  `kills_team1` int(10) signed default NULL,  `kills_team2` int(10) signed default NULL,  `deaths_team1` int(10) signed default NULL,  `deaths_team2` int(10) signed default NULL,  `attacks_team1` int(10) signed default NULL,  `attacks_team2` int(10) signed default NULL,  `captures_team1` int(10) signed default NULL,  `captures_team2` int(10) signed default NULL,  PRIMARY KEY  (`map`),  KEY `map` (`map`)) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_cache_ranking` (  `rank` int(10) signed default NULL,  `player_id` int(10) signed NOT NULL default '0',  `playername` varchar(100) default NULL,  `score` int(10) signed default NULL,  `kills` int(10) signed default NULL,  `deaths` int(10) signed default NULL,  `kdrate` double default NULL,  `score_per_minute` double default NULL,  `tks` int(10) signed default NULL,  `captures` int(10) signed default NULL,  `attacks` int(10) signed default NULL,  `defences` int(10) signed default NULL,  `objectives` int(10) signed default NULL,  `objectivetks` int(10) signed default NULL,  `heals` int(10) signed default NULL,  `selfheals` int(10) signed default NULL,  `repairs` int(10) signed default NULL,  `otherrepairs` int(10) signed default NULL,  `first` int(10) signed default NULL,  `second` int(10) signed default NULL,  `third` int(10) signed default NULL,  `playtime` double default NULL,  `rounds_played` int(10) signed default NULL,  `last_visit` datetime default NULL,  PRIMARY KEY  (`player_id`)) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_cache_vehicletime` (  `vehicle` varchar(100) NOT NULL default '',  `time` float default NULL,  `percentage_time` float default NULL,  `times_used` int(10) signed default NULL,  `percentage_usage` float default NULL,  PRIMARY KEY  (`vehicle`),  KEY `vehicle` (`vehicle`)) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_cache_weaponkills` (  `weapon` varchar(50) NOT NULL default '',  `kills` int(10) signed default NULL,  `percentage` float default NULL,  PRIMARY KEY  (`weapon`),  KEY `weapon` (`weapon`)) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_category` (  `id` int(10) signed NOT NULL auto_increment,  `name` varchar(50) default NULL,  `collect_data` int(10) signed default NULL,  `datasource_name` varchar(50) default NULL,  `type` varchar(50) default NULL,  `inserttime` datetime default NULL,  PRIMARY KEY  (`id`),  UNIQUE KEY `id` (`id`),  KEY `id_2` (`id`)) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_categorymember` (  `member` varchar(50) default NULL,  `category` int(10) signed default NULL) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_chatlog` (  `Id` int(10) signed NOT NULL auto_increment,  `text` text NOT NULL,  `player_id` smallint(10) NOT NULL default '0',  `round_id` int(10) default NULL,  `inserttime` datetime default NULL,  PRIMARY KEY  (`Id`)) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_clan_ranking` (  `ranks` int(10) signed default NULL,  `score` float signed default NULL,  `clanname` varchar(100) default NULL,  `members` int(10) default NULL,  `kills` float default NULL,  `deaths` float default NULL,  `kdrate` float default NULL,  `tks` float default NULL,  `captures` float default NULL,  `attacks` float default NULL,  `defences` float default NULL,  `objectives` float default NULL,  `objectivetks` float default NULL,  `heals` float default NULL,  `selfheals` float default NULL,  `repairs` float default NULL,  `otherrepairs` float default NULL,  `rounds_played` float default NULL, first int(11) default NULL, second int(11) default NULL, third int(11) default NULL) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_clan_tags` (  `clan_tag` varchar(100) default NULL) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_cleartext` (  `id` int(10) signed NOT NULL auto_increment,  `original` varchar(50) default NULL,  `custom` varchar(100) default NULL,  `type` varchar(50) default NULL,  `inserttime` datetime default NULL,  PRIMARY KEY  (`id`),  UNIQUE KEY `id` (`id`),  KEY `id_2` (`id`)) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_drives` (  `id` int(10) signed NOT NULL auto_increment,  `player_id` int(10) signed default NULL,  `vehicle` tinytext,  `drivetime` float default '0',  `times_used` int(10) signed default '0',  PRIMARY KEY  (`id`),  UNIQUE KEY `id` (`id`),  KEY `id_2` (`id`,`player_id`)) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_games` (  `id` int(10) signed NOT NULL auto_increment,  `servername` tinytext,  `modid` tinytext,  `mapid` tinytext,  `map` tinytext,  `game_mode` tinytext,  `gametime` int(10) signed default NULL,  `maxplayers` int(10) signed default NULL,  `scorelimit` int(10) signed default NULL,  `spawntime` int(10) signed default NULL,  `soldierff` int(10) signed default NULL,  `vehicleff` int(10) signed default NULL,  `tkpunish` int(10) signed default NULL,  `deathcamtype` int(10) signed default NULL,  `starttime` datetime default NULL,  PRIMARY KEY  (`id`),  UNIQUE KEY `id` (`id`),  KEY `id_2` (`id`)) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_heals` (  `id` int(10) signed NOT NULL auto_increment,  `player_id` int(10) signed default NULL,  `healed_player_id` int(10) signed default NULL,  `amount` int(10) signed default NULL,  `healtime` float default NULL,  `times_healed` int(10) signed default NULL,  PRIMARY KEY  (`id`),  UNIQUE KEY `id` (`id`),  KEY `id_2` (`id`,`player_id`,`healed_player_id`)) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_kills_player` (  `id` int(10) signed NOT NULL auto_increment,  `player_id` int(10) signed default NULL,  `victim_id` int(10) signed default NULL,  `times_killed` int(10) signed default '0',  PRIMARY KEY  (`id`),  UNIQUE KEY `id` (`id`),  KEY `id_2` (`id`,`player_id`,`victim_id`)) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_kills_weapon` (  `id` int(10) signed NOT NULL auto_increment,  `player_id` int(10) signed default NULL,  `weapon` varchar(50) default '0',  `times_used` int(10) signed default '0',  PRIMARY KEY  (`id`),  UNIQUE KEY `id` (`id`),  KEY `id_2` (`id`,`player_id`,`weapon`)) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_kits` (  `id` int(10) signed NOT NULL auto_increment,  `player_id` int(10) signed default NULL,  `kit` text,  `times_used` int(10) signed default '0',  PRIMARY KEY  (`id`),  UNIQUE KEY `id` (`id`),  KEY `id_2` (`id`,`player_id`)) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_modassignment` (  `id` int(10) signed NOT NULL auto_increment,  `item` varchar(50) default NULL,  `mod` varchar(50) default NULL,  `type` varchar(50) default NULL,  `inserttime` datetime default NULL,  PRIMARY KEY  (`id`),  UNIQUE KEY `id` (`id`),  KEY `id_2` (`id`)) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_nicknames` (  `nickname` varchar(150) default NULL,  `times_used` int(10) signed default NULL,  `player_id` int(10) signed default NULL,  KEY `player_id` (`player_id`)) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_params` (  `id` int(10) signed NOT NULL auto_increment,  `name` varchar(50) default NULL,  `value` varchar(255) default NULL,  `inserttime` datetime default NULL,  PRIMARY KEY  (`id`),  UNIQUE KEY `id` (`id`),  KEY `id_2` (`id`)) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_players` (  `id` int(10) signed NOT NULL auto_increment,  `name` varchar(150) default NULL,  `keyhash` varchar(32) NOT NULL default '',  `inserttime` datetime default NULL,  PRIMARY KEY  (`id`),  UNIQUE KEY `id` (`id`),  KEY `id_2` (`id`)) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_playerstats` (  `id` int(10) signed NOT NULL auto_increment,  `player_id` int(10) signed default NULL,  `team` int(10) signed default NULL,  `score` int(10) signed default NULL,  `kills` int(10) signed default NULL,  `deaths` int(10) signed default NULL,  `tks` int(10) signed default NULL,  `captures` int(10) signed default NULL,  `attacks` int(10) signed default NULL,  `defences` int(10) signed default NULL,  `objectives` int(10) signed default NULL,  `objectivetks` int(10) signed default NULL,  `heals` int(10) signed default NULL,  `selfheals` int(10) signed default NULL,  `repairs` int(10) signed default NULL,  `otherrepairs` int(10) signed default NULL,  `round_id` int(10) signed default NULL,  `first` int(10) signed default NULL,  `second` int(10) signed default NULL,  `third` int(10) signed default NULL,  PRIMARY KEY  (`id`),  UNIQUE KEY `id` (`id`),  KEY `id_2` (`id`,`player_id`,`round_id`)) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_playtimes` (  `id` int(10) signed NOT NULL auto_increment,  `player_id` int(10) signed default NULL,  `last_seen` datetime default NULL,  `playtime` float default '0',  `slots_used` int(10) signed default '0',  PRIMARY KEY  (`id`),  UNIQUE KEY `id` (`id`),  KEY `id_2` (`id`,`player_id`)) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_repairs` (  `id` int(10) signed NOT NULL auto_increment,  `player_id` int(10) signed default NULL,  `vehicle` varchar(150) default NULL,  `amount` int(10) signed default NULL,  `repairtime` float default NULL,  `times_repaired` int(10) signed default '0',  PRIMARY KEY  (`id`),  UNIQUE KEY `id` (`id`),  KEY `id_2` (`id`,`player_id`,`vehicle`)) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_rounds` (  `id` int(10) signed NOT NULL auto_increment,  `start_tickets_team1` int(10) signed default NULL,  `start_tickets_team2` int(10) signed default NULL,  `starttime` datetime default NULL,  `end_tickets_team1` int(10) signed default NULL,  `end_tickets_team2` int(10) signed default NULL,  `endtime` datetime default NULL,  `endtype` tinytext,  `winning_team` int(10) signed default '0',  `game_id` int(10) signed default NULL,  PRIMARY KEY  (`id`),  UNIQUE KEY `id` (`id`),  KEY `id_2` (`id`,`game_id`)) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_selfkills` (  `id` int(10) signed NOT NULL auto_increment,  `player_id` int(10) signed default NULL,  `times_killed` int(10) signed default NULL,  PRIMARY KEY  (`id`),  UNIQUE KEY `id` (`id`),  KEY `id_2` (`id`,`player_id`)) ENGINE=MyISAM;");
		SQL_query("CREATE TABLE `selectbf_tks` (  `id` int(10) signed NOT NULL auto_increment,  `player_id` int(10) signed default NULL,  `victim_id` int(10) signed default NULL,  `times_killed` int(10) signed NOT NULL default '0',  PRIMARY KEY  (`id`),  UNIQUE KEY `id` (`id`),  KEY `id_2` (`id`,`player_id`,`victim_id`)) ENGINE=MyISAM;");

		//Speed-up index hacks
		SQL_query("ALTER Table `selectbf_chatlog` ADD INDEX ( `player_id` );");
		SQL_query("ALTER Table `selectbf_chatlog` ADD INDEX ( `round_id` );");
		SQL_query("ALTER Table `selectbf_drives` ADD INDEX ( `player_id` );");
		SQL_query("ALTER Table `selectbf_heals` ADD INDEX ( `player_id` );");
		SQL_query("ALTER Table `selectbf_heals` ADD INDEX ( `healed_player_id` );");
		SQL_query("ALTER Table `selectbf_kills_player` ADD INDEX ( `player_id` );");
		SQL_query("ALTER Table `selectbf_kills_player` ADD INDEX ( `victim_id` );");
		SQL_query("ALTER Table `selectbf_kills_weapon` ADD INDEX ( `player_id` );");
		SQL_query("ALTER Table `selectbf_kits` ADD INDEX ( `player_id` );");
		SQL_query("ALTER Table `selectbf_nicknames` ADD INDEX ( `player_id` );");
		SQL_query("ALTER Table `selectbf_playerstats` ADD INDEX ( `player_id` );");
		SQL_query("ALTER Table `selectbf_playerstats` ADD INDEX ( `round_id` );");
		SQL_query("ALTER Table `selectbf_playtimes` ADD INDEX ( `player_id` );");
		SQL_query("ALTER Table `selectbf_repairs` ADD INDEX ( `player_id` );");
		SQL_query("ALTER Table `selectbf_rounds` ADD INDEX ( `game_id` );");
		SQL_query("ALTER Table `selectbf_selfkills` ADD INDEX ( `player_id` );");
		SQL_query("ALTER Table `selectbf_tks` ADD INDEX ( `player_id` );");
		SQL_query("ALTER Table `selectbf_tks` ADD INDEX ( `victim_id` );");
}

function addNeededValues($password)
{
		SQL_query("INSERT INTO selectbf_admin (name, value, inserttime) VALUES('ADMIN_PSW', '$password', now());");
        SQL_query("INSERT INTO selectbf_admin (name, value, inserttime) VALUES('VERSION', '0.3', now());");
		SQL_query("INSERT INTO `selectbf_params` VALUES (1,'TEMPLATE','original','2003-10-19 21:50:21');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (2,'DEBUG-LEVEL','0','2003-10-19 21:50:21');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (3,'TITLE-PREFIX','select(bf)','2003-10-19 21:50:21');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (4,'MIN-ROUNDS','0','2004-02-21 23:14:17');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (5,'STAR-NUMBER','20','2004-02-21 23:14:17');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (6,'RANK-ORDERBY','points','2004-02-21 23:14:17');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (7,'RANK-FORMULA','(0/0)*0','2004-02-29 12:31:29');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (8,'CLAN-TABLE-SETUP','1','2004-02-21 11:37:45');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (9,'CLAN-ACCESS-RIGHT','1','2004-02-21 23:14:17');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (10,'CLAN-PARSER-PATH','<CONFIGURE THIS>','2004-02-21 23:14:17');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (11,'MIN-CLAN-MEMBERS','1','2004-02-21 23:14:17');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (12,'MIN-CLAN-ROUNDS','1','2004-02-21 23:14:17');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (13,'LIST-RANKING-PLAYER','50','2004-02-29 02:24:20');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (14,'LIST-RANKING-GAMES','15','2004-02-29 02:24:20');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (15,'LIST-CLAN-RANKING','25','2004-02-29 02:24:20');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (16,'LIST-CHARACTER-TYPE','15','2004-02-29 02:24:20');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (17,'LIST-CHARACTER-REPAIRS','15','2004-02-29 02:24:20');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (18,'LIST-CHARACTER-HEALS','15','2004-02-29 02:24:20');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (19,'LIST-MAP-ROUNDS','1','2004-02-29 02:24:20');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (20,'LIST-MAP-KILLERS','15','2004-02-29 02:24:20');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (21,'LIST-MAP-ATTACKS','15','2004-02-29 02:24:20');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (22,'LIST-MAP-DEATHS','15','2004-02-29 02:24:20');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (23,'LIST-MAP-TKS','15','2004-02-29 02:24:20');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (24,'LIST-WEAPONS-LIST','25','2004-02-29 02:24:20');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (25,'LIST-VEHICLES-LIST','25','2004-02-29 02:24:20');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (26,'LIST-PLAYER-NICKNAMES','10','2004-02-29 02:24:20');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (27,'LIST-PLAYER-CHARACTERS','10','2004-02-29 02:24:20');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (28,'LIST-PLAYER-WEAPONS','10','2004-02-29 02:24:20');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (29,'LIST-PLAYER-VICTIMS','10','2004-02-29 02:24:20');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (30,'LIST-PLAYER-ASSASINS','10','2004-02-29 02:24:20');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (31,'LIST-PLAYER-VEHICLES','10','2004-02-29 02:24:20');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (32,'LIST-PLAYER-MAPS','10','2004-02-29 02:24:20');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (33,'LIST-PLAYER-GAMES','10','2004-02-29 02:24:20');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (34,'LIST-CLAN-MEMBERS','50','2004-02-29 02:24:20');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (35,'RANK-ROUND','other','2004-02-29 12:31:29');");
		SQL_query("INSERT INTO `selectbf_params` VALUES (36,'RANK-ROUND-NUMBER','2','2004-02-29 12:31:21');");
}

function dropAllTables()
{
		SQL_query("DROP TABLE `selectbf_admin`;");
		SQL_query("DROP TABLE `selectbf_cache_chartypeusage`;");
		SQL_query("DROP TABLE `selectbf_cache_mapstats` ");
		SQL_query("DROP TABLE `selectbf_cache_ranking` ");
		SQL_query("DROP TABLE `selectbf_cache_vehicletime`;");
		SQL_query("DROP TABLE `selectbf_cache_weaponkills`;");
		SQL_query("DROP TABLE `selectbf_category` ");
		SQL_query("DROP TABLE `selectbf_categorymember`;");
		SQL_query("DROP TABLE `selectbf_chatlog` ");
		SQL_query("DROP TABLE `selectbf_clan_ranking`;");
		SQL_query("DROP TABLE `selectbf_clan_tags`;");
		SQL_query("DROP TABLE `selectbf_cleartext`;");
		SQL_query("DROP TABLE `selectbf_drives`;");
		SQL_query("DROP TABLE `selectbf_games`;");
		SQL_query("DROP TABLE `selectbf_heals`;");
		SQL_query("DROP TABLE `selectbf_kills_player` ");
		SQL_query("DROP TABLE `selectbf_kills_weapon`;");
		SQL_query("DROP TABLE `selectbf_kits`;");
		SQL_query("DROP TABLE `selectbf_modassignment`;");
		SQL_query("DROP TABLE `selectbf_nicknames` ");
		SQL_query("DROP TABLE `selectbf_params` ");
		SQL_query("DROP TABLE `selectbf_players`;");
		SQL_query("DROP TABLE `selectbf_playerstats`;");
		SQL_query("DROP TABLE `selectbf_playtimes`;");
		SQL_query("DROP TABLE `selectbf_repairs`;");
		SQL_query("DROP TABLE `selectbf_rounds`;");
		SQL_query("DROP TABLE `selectbf_selfkills`;");
		SQL_query("DROP TABLE `selectbf_tks` ");
}

function updateTables()
{
		SQL_query("ALTER TABLE `selectbf_cache_mapstats` CHANGE `score_team1` `score_team1` INT( 10 ) NULL DEFAULT NULL , CHANGE `score_team2` `score_team2` INT( 10 ) NULL DEFAULT NULL;");
		SQL_query("ALTER TABLE `selectbf_cache_ranking` CHANGE `score` `score` INT( 10 ) NULL DEFAULT NULL , CHANGE `repairs` `repairs` INT( 10 ) SIGNED NULL DEFAULT NULL , CHANGE `otherrepairs` `otherrepairs` INT( 10 ) SIGNED NULL DEFAULT NULL , CHANGE `first` `first` INT( 10 ) SIGNED NULL DEFAULT NULL , CHANGE `second` `second` INT( 10 ) SIGNED NULL DEFAULT NULL , CHANGE `third` `third` INT( 10 ) SIGNED NULL DEFAULT NULL;");
		SQL_query("ALTER TABLE `selectbf_playerstats` CHANGE `score` `score` INT( 10 ) NULL DEFAULT NULL , CHANGE `repairs` `repairs` INT( 10 ) SIGNED NULL DEFAULT NULL , CHANGE `otherrepairs` `otherrepairs` INT( 10 ) SIGNED NULL DEFAULT NULL , CHANGE `first` `first` INT( 10 ) SIGNED NULL DEFAULT NULL , CHANGE `second` `second` INT( 10 ) SIGNED NULL DEFAULT NULL , CHANGE `third` `third` INT( 10 ) SIGNED NULL DEFAULT NULL;");
}

//now start setting the variables for the Template
$tmpl = new vlibTemplate("templates/original/_setup.html");

@$todo = $_REQUEST["todo"];

if(isset($todo))
{
	switch($todo)
	{
		case "scratch":		@$password = $_REQUEST["password"];
							@$confirm = $_REQUEST["confirm"];
							
							if($password == $confirm)
							{
								$password = md5($password);
								createAllTables();
								addNeededValues($password);
								$tmpl->setVar("msg","<b>All Tables created!</b><br><b>Default-Parameter successfully written!</b><br>");
								$tmpl->setVar("done",true);								
							} 	
							else
							{
								$tmpl->setVar("error","<b>Password</b> and <b>Confirm</b> didin't match!");
							}
							break;

		case "reset":		if(isset($_REQUEST["sure"])&& isset($_REQUEST["reallysure"]))
							{
								dropAllTables();
								$tmpl->setVar("msg","<b>All Tables dropped!</b>");
								$tmpl->setVar("deleted",true);
							}
							else
							{
								$tmpl->setVar("error","Please first be <b>sure</b> and <b>really sure</b> before you push buttons!");
							}
							break;
							
		case "update":		updateTables();
							$tmpl->setVar("msg","<b>All Tables updated!</b>");
							$tmpl->setVar("done",true);

							break;								
	}
}


@$tmpl->pparse();


?>



