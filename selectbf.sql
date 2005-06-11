# MySQL-Front 3.0


# Host: select(bf).2    Database: selectbf2
# ------------------------------------------------------
# Server version 4.0.13-nt

#
# Table structure for table selectbf_admin
#

CREATE TABLE `selectbf_admin` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `name` text,
  `value` text,
  `inserttime` datetime default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`)
) TYPE=MyISAM;


#
# Table structure for table selectbf_cache_chartypeusage
#

CREATE TABLE `selectbf_cache_chartypeusage` (
  `kit` varchar(35) NOT NULL default '',
  `percentage` float default NULL,
  `times_used` int(10) unsigned default NULL,
  PRIMARY KEY  (`kit`)
) TYPE=MyISAM;


#
# Table structure for table selectbf_cache_mapstats
#

CREATE TABLE `selectbf_cache_mapstats` (
  `map` varchar(100) NOT NULL default '0',
  `wins_team1` int(10) unsigned default NULL,
  `wins_team2` int(10) unsigned default NULL,
  `win_team1_tickets_team1` float default NULL,
  `win_team1_tickets_team2` float default NULL,
  `win_team2_tickets_team1` float default NULL,
  `win_team2_tickets_team2` float default NULL,
  `score_team1` int(10) unsigned default NULL,
  `score_team2` int(10) unsigned default NULL,
  `kills_team1` int(10) unsigned default NULL,
  `kills_team2` int(10) unsigned default NULL,
  `deaths_team1` int(10) unsigned default NULL,
  `deaths_team2` int(10) unsigned default NULL,
  `attacks_team1` int(10) unsigned default NULL,
  `attacks_team2` int(10) unsigned default NULL,
  `captures_team1` int(10) unsigned default NULL,
  `captures_team2` int(10) unsigned default NULL,
  PRIMARY KEY  (`map`),
  KEY `map` (`map`)
) TYPE=MyISAM;


#
# Table structure for table selectbf_cache_ranking
#

CREATE TABLE `selectbf_cache_ranking` (
  `rank` int(10) unsigned default NULL,
  `player_id` int(10) unsigned NOT NULL default '0',
  `playername` varchar(100) default NULL,
  `score` int(10) unsigned default NULL,
  `kills` int(10) unsigned default NULL,
  `deaths` int(10) unsigned default NULL,
  `kdrate` double default NULL,
  `score_per_minute` double default NULL,
  `tks` int(10) unsigned default NULL,
  `captures` int(10) unsigned default NULL,
  `attacks` int(10) unsigned default NULL,
  `defences` int(10) unsigned default NULL,
  `objectives` int(10) unsigned default NULL,
  `objectivetks` int(10) unsigned default NULL,
  `heals` int(10) unsigned default NULL,
  `selfheals` int(10) unsigned default NULL,
  `repairs` tinyint(3) unsigned default NULL,
  `otherrepairs` tinyint(3) unsigned default NULL,
  `first` tinyint(3) unsigned default NULL,
  `second` tinyint(3) unsigned default NULL,
  `third` tinyint(3) unsigned default NULL,
  `playtime` double default NULL,
  `rounds_played` int(10) unsigned default NULL,
  `last_visit` datetime default NULL,
  PRIMARY KEY  (`player_id`)
) TYPE=MyISAM;


#
# Table structure for table selectbf_cache_vehicletime
#

CREATE TABLE `selectbf_cache_vehicletime` (
  `vehicle` varchar(100) NOT NULL default '',
  `time` float default NULL,
  `percentage_time` float default NULL,
  `times_used` int(10) unsigned default NULL,
  `percentage_usage` float default NULL,
  PRIMARY KEY  (`vehicle`),
  KEY `vehicle` (`vehicle`)
) TYPE=MyISAM;


#
# Table structure for table selectbf_cache_weaponkills
#

CREATE TABLE `selectbf_cache_weaponkills` (
  `weapon` varchar(50) NOT NULL default '',
  `kills` int(10) unsigned default NULL,
  `percentage` float default NULL,
  PRIMARY KEY  (`weapon`),
  KEY `weapon` (`weapon`)
) TYPE=MyISAM;


#
# Table structure for table selectbf_category
#

CREATE TABLE `selectbf_category` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(50) default NULL,
  `collect_data` int(10) unsigned default NULL,
  `datasource_name` varchar(50) default NULL,
  `type` varchar(50) default NULL,
  `inserttime` datetime default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`)
) TYPE=MyISAM;


#
# Table structure for table selectbf_categorymember
#

CREATE TABLE `selectbf_categorymember` (
  `member` varchar(50) default NULL,
  `category` int(10) unsigned default NULL
) TYPE=MyISAM;


#
# Table structure for table selectbf_chatlog
#

CREATE TABLE `selectbf_chatlog` (
  `Id` int(10) unsigned NOT NULL auto_increment,
  `text` varchar(50) NOT NULL default '',
  `player_id` smallint(10) NOT NULL default '0',
  `round_id` int(10) default NULL,
  `inserttime` datetime default NULL,
  PRIMARY KEY  (`Id`)
) TYPE=MyISAM;


#
# Table structure for table selectbf_clan_ranking
#

CREATE TABLE `selectbf_clan_ranking` (
  `ranks` int(10) unsigned default NULL,
  `score` float unsigned default NULL,
  `clanname` varchar(100) default NULL,
  `members` int(10) default NULL,
  `kills` float default NULL,
  `deaths` float default NULL,
  `kdrate` float default NULL,
  `tks` float default NULL,
  `captures` float default NULL,
  `attacks` float default NULL,
  `defences` float default NULL,
  `objectives` float default NULL,
  `objectivetks` float default NULL,
  `heals` float default NULL,
  `selfheals` float default NULL,
  `repairs` float default NULL,
  `otherrepairs` float default NULL,
  `rounds_played` float default NULL
) TYPE=MyISAM;


#
# Table structure for table selectbf_clan_tags
#

CREATE TABLE `selectbf_clan_tags` (
  `clan_tag` varchar(100) default NULL
) TYPE=MyISAM;


#
# Table structure for table selectbf_cleartext
#

CREATE TABLE `selectbf_cleartext` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `original` varchar(50) default NULL,
  `custom` varchar(100) default NULL,
  `type` varchar(50) default NULL,
  `inserttime` datetime default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`)
) TYPE=MyISAM;


#
# Table structure for table selectbf_drives
#

CREATE TABLE `selectbf_drives` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `player_id` int(10) unsigned default NULL,
  `vehicle` tinytext,
  `drivetime` float default '0',
  `times_used` int(10) unsigned default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`,`player_id`)
) TYPE=MyISAM;


#
# Table structure for table selectbf_games
#

CREATE TABLE `selectbf_games` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `servername` tinytext,
  `modid` tinytext,
  `mapid` tinytext,
  `map` tinytext,
  `game_mode` tinytext,
  `gametime` int(10) unsigned default NULL,
  `maxplayers` int(10) unsigned default NULL,
  `scorelimit` int(10) unsigned default NULL,
  `spawntime` int(10) unsigned default NULL,
  `soldierff` int(10) unsigned default NULL,
  `vehicleff` int(10) unsigned default NULL,
  `tkpunish` tinyint(3) unsigned default NULL,
  `deathcamtype` tinyint(3) unsigned default NULL,
  `starttime` datetime default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`)
) TYPE=MyISAM;


#
# Table structure for table selectbf_heals
#

CREATE TABLE `selectbf_heals` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `player_id` int(10) unsigned default NULL,
  `healed_player_id` int(10) unsigned default NULL,
  `amount` int(10) unsigned default NULL,
  `healtime` float default NULL,
  `times_healed` int(10) unsigned default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`,`player_id`,`healed_player_id`)
) TYPE=MyISAM;


#
# Table structure for table selectbf_kills_player
#

CREATE TABLE `selectbf_kills_player` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `player_id` int(10) unsigned default NULL,
  `victim_id` int(10) unsigned default NULL,
  `times_killed` int(10) unsigned default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`,`player_id`,`victim_id`)
) TYPE=MyISAM;


#
# Table structure for table selectbf_kills_weapon
#

CREATE TABLE `selectbf_kills_weapon` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `player_id` int(10) unsigned default NULL,
  `weapon` varchar(50) default '0',
  `times_used` int(10) unsigned default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`,`player_id`,`weapon`)
) TYPE=MyISAM;


#
# Table structure for table selectbf_kits
#

CREATE TABLE `selectbf_kits` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `player_id` int(10) unsigned default NULL,
  `kit` text,
  `times_used` int(10) unsigned default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`,`player_id`)
) TYPE=MyISAM;


#
# Table structure for table selectbf_modassignment
#

CREATE TABLE `selectbf_modassignment` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `item` varchar(50) default NULL,
  `mod` varchar(50) default NULL,
  `type` varchar(50) default NULL,
  `inserttime` datetime default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`)
) TYPE=MyISAM;


#
# Table structure for table selectbf_nicknames
#

CREATE TABLE `selectbf_nicknames` (
  `nickname` varchar(150) default NULL,
  `times_used` int(10) unsigned default NULL,
  `player_id` int(10) unsigned default NULL,
  KEY `player_id` (`player_id`)
) TYPE=MyISAM;


#
# Table structure for table selectbf_params
#

CREATE TABLE `selectbf_params` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(50) default NULL,
  `value` varchar(255) default NULL,
  `inserttime` datetime default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`)
) TYPE=MyISAM;


#
# Table structure for table selectbf_players
#

CREATE TABLE `selectbf_players` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(150) default NULL,
  `keyhash` varchar(32) NOT NULL default '',
  `inserttime` datetime default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`)
) TYPE=MyISAM;


#
# Table structure for table selectbf_playerstats
#

CREATE TABLE `selectbf_playerstats` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `player_id` int(10) unsigned default NULL,
  `team` tinyint(3) unsigned default NULL,
  `score` int(10) unsigned default NULL,
  `kills` int(10) unsigned default NULL,
  `deaths` int(10) unsigned default NULL,
  `tks` int(10) unsigned default NULL,
  `captures` int(10) unsigned default NULL,
  `attacks` int(10) unsigned default NULL,
  `defences` int(10) unsigned default NULL,
  `objectives` int(10) unsigned default NULL,
  `objectivetks` int(10) unsigned default NULL,
  `heals` int(10) unsigned default NULL,
  `selfheals` int(10) unsigned default NULL,
  `repairs` tinyint(3) unsigned default NULL,
  `otherrepairs` tinyint(3) unsigned default NULL,
  `round_id` int(10) unsigned default NULL,
  `first` tinyint(3) unsigned default NULL,
  `second` tinyint(3) unsigned default NULL,
  `third` tinyint(3) unsigned default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`,`player_id`,`round_id`)
) TYPE=MyISAM;


#
# Table structure for table selectbf_playtimes
#

CREATE TABLE `selectbf_playtimes` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `player_id` int(10) unsigned default NULL,
  `last_seen` datetime default NULL,
  `playtime` float default '0',
  `slots_used` int(10) unsigned default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`,`player_id`)
) TYPE=MyISAM;


#
# Table structure for table selectbf_repairs
#

CREATE TABLE `selectbf_repairs` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `player_id` int(10) unsigned default NULL,
  `vehicle` varchar(150) default NULL,
  `amount` int(10) unsigned default NULL,
  `repairtime` float default NULL,
  `times_repaired` int(10) unsigned default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`,`player_id`,`vehicle`)
) TYPE=MyISAM;


#
# Table structure for table selectbf_rounds
#

CREATE TABLE `selectbf_rounds` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `start_tickets_team1` int(10) unsigned default NULL,
  `start_tickets_team2` int(10) unsigned default NULL,
  `starttime` datetime default NULL,
  `end_tickets_team1` int(10) unsigned default NULL,
  `end_tickets_team2` int(10) unsigned default NULL,
  `endtime` datetime default NULL,
  `endtype` tinytext,
  `winning_team` tinyint(3) unsigned default '0',
  `game_id` int(10) unsigned default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`,`game_id`)
) TYPE=MyISAM;


#
# Table structure for table selectbf_selfkills
#

CREATE TABLE `selectbf_selfkills` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `player_id` int(10) unsigned default NULL,
  `times_killed` int(10) unsigned default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`,`player_id`)
) TYPE=MyISAM;


#
# Table structure for table selectbf_tks
#

CREATE TABLE `selectbf_tks` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `player_id` int(10) unsigned default NULL,
  `victim_id` int(10) unsigned default NULL,
  `times_killed` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`,`player_id`,`victim_id`)
) TYPE=MyISAM;


