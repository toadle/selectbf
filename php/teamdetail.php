<?
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

//read the base parameters
$start = "";
@$start = $_REQUEST["start"];

$orderby = "";
@$orderby = $_REQUEST["orderby"];

$direction = "";
@$direction = $_REQUEST["direction"];

$startgames = "";
@$startgames = $_REQUEST["startgames"];

//added by Gary
$clanname = "";
@$clanname = $_REQUEST["clanname"];
if($clanname == "")
{
	die(SQL_error("Team Detail is available only in Clan Ranking.","There isn't Clan Tag at connect!"));
}

if($start == "")
{
	$start = "0";
}

if($orderby == "")
{
	$orderby = getRankingOrderByColumn();
}
if($direction == "")
{
	$direction = "desc";
}
if($startgames == "")
{
	$startgames = 0;
}

//now start setting the variables for the Template
if(!checkTemplateConsistency($TEMPLATE_DIR,"teamdetail.html"))
{
		die(Template_error("This page is not templated yet, plz create a 'teamdetail.html' for the '$TEMPLATE_DIR'-Template!"));
}
$tmpl = new vlibTemplate("templates/$TEMPLATE_DIR/teamdetail.html");

//set basic Template-Variables
$tmpl->setVar("TITLE",getActiveTitlePrefix()." - Player Ranking");
$tmpl->setVar("CSS","templates/$TEMPLATE_DIR/include/$TMPL_CFG_CSS");
$tmpl->setVar("IMAGES_DIR","templates/$TEMPLATE_DIR/images/");
$tmpl->setVar("ADMINMODE_LINK","admin/index.php");
$tmpl->setLoop("NAVBAR",getNavBar());

//Added by Gary Chiu
//Looking for Clan Tags in Database only
$resultset = SQL_query("select clanname from selectbf_clan_ranking where clanname='".clear_special_char($clanname,false)."'");
if(mysql_num_rows($resultset) > 0){
	if(stristr($_ENV["TERM"],"linux"))
		$clanname = str_replace("\\","",$clanname);
}else{
	$clanname = "";
	die(SQL_error("Team Detail is available only in Clan Ranking.","There isn't Clan Tag at connect!"));	
}

$contextbar = array();
$contextbar = addContextItem($contextbar,getActiveTitlePrefix());
$contextbar = addLinkedContextItem($contextbar,"clans.php","Clans");
$contextbar = addContextItem($contextbar,clearUpText("$clanname Clan Ranking","TEAM"));
$tmpl->setLoop("CONTEXTBAR",$contextbar);


//now set Index-specific Variables

//first the linking-information for the different collumns
$tmpl->setVar("points_link",getLinkForNeededColumn("points","teamdetail.php",$clanname,$orderby,$direction));
$tmpl->setVar("avgscore_link",getLinkForNeededColumn("avgscore","teamdetail.php",$clanname,$orderby,$direction));
$tmpl->setVar("score_link",getLinkForNeededColumn("score","teamdetail.php",$clanname,$orderby,$direction));
$tmpl->setVar("kills_link",getLinkForNeededColumn("kills","teamdetail.php",$clanname,$orderby,$direction));
$tmpl->setVar("deaths_link",getLinkForNeededColumn("deaths","teamdetail.php",$clanname,$orderby,$direction));
$tmpl->setVar("kdrate_link",getLinkForNeededColumn("kdrate","teamdetail.php",$clanname,$orderby,$direction));
$tmpl->setVar("first_link",getLinkForNeededColumn("first","teamdetail.php",$clanname,$orderby,$direction));
$tmpl->setVar("second_link",getLinkForNeededColumn("second","teamdetail.php",$clanname,$orderby,$direction));
$tmpl->setVar("third_link",getLinkForNeededColumn("third","teamdetail.php",$clanname,$orderby,$direction));
$tmpl->setVar("tks_link",getLinkForNeededColumn("tks","teamdetail.php",$clanname,$orderby,$direction));
$tmpl->setVar("attacks_link",getLinkForNeededColumn("attacks","teamdetail.php",$clanname,$orderby,$direction));
$tmpl->setVar("captures_link",getLinkForNeededColumn("captures","teamdetail.php",$clanname,$orderby,$direction));
$tmpl->setVar("objectives_link",getLinkForNeededColumn("objectives","teamdetail.php",$clanname,$orderby,$direction));
$tmpl->setVar("rounds_link",getLinkForNeededColumn("rounds","teamdetail.php",$clanname,$orderby,$direction));
$tmpl->setVar("heals_link",getLinkForNeededColumn("heals","teamdetail.php",$clanname,$orderby,$direction));
$tmpl->setVar("repairs_link",getLinkForNeededColumn("otherrepairs","teamdetail.php",$clanname,$orderby,$direction));

if($direction=="desc") $direction_bool = true; else $direction_bool = false;
if($orderby=="points") $points_bool = true; else $points_bool = false;
if($orderby=="avgscore") $avgscore_bool = true; else $avgscore_bool = false;
if($orderby=="score") $score_bool = true; else $score_bool = false;
if($orderby=="kills") $kills_bool = true; else $kills_bool = false;
if($orderby=="deaths") $deaths_bool = true; else $deaths_bool = false;
if($orderby=="kdrate") $kdrate_bool = true; else $kdrate_bool = false;
if($orderby=="first") $first_bool = true; else $first_bool = false;
if($orderby=="second") $second_bool = true; else $second_bool = false;
if($orderby=="third") $third_bool = true; else $third_bool = false;
if($orderby=="tks") $tks_bool = true; else $tks_bool = false;
if($orderby=="attacks") $attacks_bool = true; else $attacks_bool = false;
if($orderby=="captures") $captures_bool = true; else $captures_bool = false;
if($orderby=="objectives") $objectives_bool = true; else $objectives_bool = false;
if($orderby=="rounds") $rounds_bool = true; else $rounds_bool = false;
if($orderby=="heals") $heals_bool = true; else $heals_bool = false;
if($orderby=="otherrepairs") $repairs_bool = true; else $repairs_bool = false;

$tmpl->setVar("desc", $direction_bool);
$tmpl->setVar("points_order", $points_bool);
$tmpl->setVar("avgscore_order", $avgscore_bool);
$tmpl->setVar("score_order", $score_bool);
$tmpl->setVar("kills_order", $kills_bool);
$tmpl->setVar("deaths_order", $deaths_bool);
$tmpl->setVar("kdrate_order", $kdrate_bool);
$tmpl->setVar("first_order", $first_bool);
$tmpl->setVar("second_order", $second_bool);
$tmpl->setVar("third_order", $third_bool);
$tmpl->setVar("tks_order", $tks_bool);
$tmpl->setVar("attacks_order", $attacks_bool);
$tmpl->setVar("captures_order", $captures_bool);
$tmpl->setVar("objectives_order", $objectives_bool);
$tmpl->setVar("rounds_order", $rounds_bool);
$tmpl->setVar("heals_order", $heals_bool);
$tmpl->setVar("repairs_order", $repairs_bool);

//Added by Gary Chiu
//Defind Clan Tag
$tmpl->setVar("clanname", $clanname);
//Team Ranking information
$res = getTeamRanking($orderby,$direction,$start,$clanname);
$tmpl->setLoop("ranking",$res);

//Serash
$Res = SQL_query("select DISTINCT modid from selectbf_games");
$mods = array();
while($Erg = SQL_fetchArray($Res))
{
  	array_push($mods,array("name"=>$Erg["modid"]));
}	
  
$tmpl->setVar("search_action","search.php");
  
$tmpl->setVar("search_type_games","games");
$tmpl->setVar("search_type_players","players");
   
$tmpl->setVar("search_param_searchtype","search");
$tmpl->setVar("search_param_servername","servername");
$tmpl->setVar("search_param_day","day");
$tmpl->setVar("search_param_month","month");
$tmpl->setVar("search_param_year","year");
$tmpl->setVar("search_param_mod","mod");
$tmpl->setVar("search_param_playername","playername");
$tmpl->setLoop("search_mods",$mods);

//now finish the processtime timer
$totaltime = timer()- $starttime;
$tmpl->setVar("PROCESSTIME",sprintf ("%01.2f seconds",$totaltime));

@$tmpl->pparse();
?>
