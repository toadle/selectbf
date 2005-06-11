<?
require_once("include/jpcache/jpcache.php");
require_once("include/vLib/vlibTemplate.php");
require_once("include/sql.php");
require_once("include/func.php");

//start the processtime-timer
$starttime=timer();


//add the specified template's config
$TEMPLATE_DIR = getActiveTemplate();
if(checkTemplateConsistency($TEMPLATE_DIR,"config.php"))
{
	require_once("templates/$TEMPLATE_DIR/config.php");
}
else
{
	die(Template_error("The config.php is missing!"));
	
}

//read the need variables
@$map = $_REQUEST["map"];

//now start setting the variables for the Template
if(!checkTemplateConsistency($TEMPLATE_DIR,"map.html"))
{
		die(Template_error("This page is not templated yet, plz create a 'map.html' for the '$TEMPLATE_DIR'-Template!"));
}
$tmpl = new vlibTemplate("templates/$TEMPLATE_DIR/map.html");

//set basic Template-Variables
$tmpl->setVar("TITLE",getActiveTitlePrefix()." - Map Details");
$tmpl->setVar("CSS","templates/$TEMPLATE_DIR/include/$TMPL_CFG_CSS");
$tmpl->setVar("IMAGES_DIR","templates/$TEMPLATE_DIR/images/");
$tmpl->setVar("ADMINMODE_LINK","admin/index.php");
$tmpl->setLoop("NAVBAR",getNavBar());

$contextbar = array();
$contextbar = addContextItem($contextbar,getActiveTitlePrefix()."-statistics");
$contextbar = addLinkedContextItem($contextbar,"index.php","Ranking");
$contextbar = addLinkedContextItem($contextbar,"maps.php","Maps");
$contextbar = addContextItem($contextbar,clearUpText($map,"MAP"));
$tmpl->setLoop("CONTEXTBAR",$contextbar);

// $cols = SQL_oneRowQuery("select player_id, p.name, score, kills, deaths, attacks, tks, g.id, g.servername,CONCAT(TIME_FORMAT(r.starttime,'%H:%i:%S '),DATE_FORMAT(r.starttime,'%d|%m|%Y')) time from selectbf_playerstats ps, selectbf_players p, selectbf_rounds r, selectbf_games g where ps.player_id = p.id and ps.round_id = r.id and r.game_id = g.id and g.map = '$map' order by score DESC LIMIT 0,1");

$tmpl->setLoop("BestRounds",getTopRounds($map));
$TopRoundsValue = $_SESSION["LIST-MAP-ROUNDS"];
$tmpl->setVar("TopRoundsValue",$TopRoundsValue);
if($TopRoundsValue > 1)
{
$tmpl->setVar("TopRoundsS","s");
}
else
{
$tmpl->setVar("TopRoundsS","");
}

$cols = SQL_oneRowQuery("SELECT map, wins_team1, wins_team2,win_team1_tickets_team1 win_team1_end_tickets_team1,win_team1_tickets_team2 win_team1_end_tickets_team2,win_team2_tickets_team1 win_team2_end_tickets_team1,win_team2_tickets_team2 win_team2_end_tickets_team2,score_team1,score_team2,kills_team1,kills_team2,deaths_team1,deaths_team2,attacks_team1,attacks_team2,captures_team1,captures_team2 from selectbf_cache_mapstats WHERE map='$map'");
$cols['mapname'] = clearUpText($cols['map'],"MAP");

$tmpl->setVar($cols);
$tmpl->setVar("screen",getMapImage($map));
$tmpl->setLoop("kills",getMapKills($map));
$tmpl->setLoop("attacks",getMapAttacks($map));
$tmpl->setLoop("deaths",getMapDeaths($map));
$tmpl->setLoop("tks",getMapTKs($map));

//now finish the processtime timer
$totaltime = timer()- $starttime;
$tmpl->setVar("PROCESSTIME",sprintf ("%01.2f seconds",$totaltime));

@$tmpl->pparse();
?>



