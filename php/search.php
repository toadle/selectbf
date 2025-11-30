<?php
require_once("include/jpcache/jpcache.php");
require_once("include/vLib/vlibTemplate.php");
use clausvb\vlib\vlibTemplate;
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

//now start setting the variables for the Template
if(!checkTemplateConsistency($TEMPLATE_DIR,"search.html"))
{
		die(Template_error("This page is not templated yet, plz create a 'search.html' for the '$TEMPLATE_DIR'-Template!"));
}
$tmpl = new vlibTemplate("templates/$TEMPLATE_DIR/search.html");

//set basic Template-Variables
$tmpl->setVar("TITLE",getActiveTitlePrefix()." - Search Results");
$tmpl->setVar("CSS","templates/$TEMPLATE_DIR/include/$TMPL_CFG_CSS");
$tmpl->setVar("IMAGES_DIR","templates/$TEMPLATE_DIR/images/");
$tmpl->setVar("ADMINMODE_LINK","admin/index.php");
$tmpl->setLoop("NAVBAR",getNavBar());

$contextbar = array();
$contextbar = addContextItem($contextbar,getActiveTitlePrefix());
$contextbar = addLinkedContextItem($contextbar,"index.php","Players");
$tmpl->setLoop("CONTEXTBAR",$contextbar);


$searchstart=timer();

//read the needed vars
$search = $_REQUEST["search"];

$searchresults = array();
if($search=="players")
{
	$tmpl->setVar("isPlayerSearch",true);
	$playername = $_REQUEST["playername"];
	$res = SQL_query("select id, name, CONCAT(DATE_FORMAT(inserttime,'%Y-%m-%d '),TIME_FORMAT(inserttime,'%H:%i:%S')) time from selectbf_players WHERE name LIKE ? ORDER BY name ASC", array('%'.$playername.'%'));
	while($cols = SQL_fetchArray($res))
	{
		$cols["playerdetaillink"] = "player.php?id=".$cols["id"];
		$cols["playerimg"] = randomPlayerImg();
		array_push($searchresults,$cols);
	}
} 
else
if($search=="games")
{
	$tmpl->setVar("isGameSearch",true);
	$servername = $_REQUEST["servername"];
	$day = $_REQUEST["day"];
	$month = $_REQUEST["month"];
	$year = $_REQUEST["year"];
	$mod = $_REQUEST["mod"];
	
	$res = null;
	if($year=="nothing" || $month=="nothing" || $day=="nothing")
    {
    	$res = SQL_query("select id, servername,modid,map,game_mode, CONCAT(TIME_FORMAT(starttime,'%H:%i:%S '),DATE_FORMAT(starttime,'%d|%m|%Y')) time from selectbf_games WHERE servername LIKE ? AND modid=? ORDER BY starttime ASC LIMIT 100", array('%'.$servername.'%', $mod));
    } 
    else
    {
    	$res = SQL_query("select id, servername,modid,map,game_mode, CONCAT(TIME_FORMAT(starttime,'%H:%i:%S '),DATE_FORMAT(starttime,'%d|%m|%Y')) time from selectbf_games WHERE servername LIKE ? AND starttime BETWEEN ? and ? and modid=? ORDER BY starttime ASC", array('%'.$servername.'%', "$year-$month-$day 00:00:00", "$year-$month-$day 23:59:59", $mod));
    }
    while($cols = SQL_fetchArray($res))
	{
		$cols["map"] = clearUpText($cols["map"],"MAP");
		$cols["game_mode"] = clearUpText($cols["game_mode"],"GAME-MODE");
		$cols["gamedetaillink"] = "game.php?id=".$cols["id"];
		array_push($searchresults,$cols);
	}
}

$tmpl->setLoop("searchresults",$searchresults);

$totaltime = timer()- $searchstart;
if($DEBUG>0) echo("<b><i>gathering Search results</i></b> took $totaltime secs<br>");

//now finish the processtime timer
$totaltime = timer()- $starttime;
$tmpl->setVar("PROCESSTIME",sprintf ("%01.2f seconds",$totaltime));

@$tmpl->pparse();
?>

