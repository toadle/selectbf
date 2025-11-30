<?php
//require_once("include/jpcache/jpcache.php");
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
if(!checkTemplateConsistency($TEMPLATE_DIR,"player.html"))
{
		die(Template_error("This page is not templated yet, plz create a 'player.html' for the '$TEMPLATE_DIR'-Template!"));
}
$tmpl = new vlibTemplate("templates/$TEMPLATE_DIR/player.html");

//set basic Template-Variables
$tmpl->setVar("TITLE",getActiveTitlePrefix()." - Player Details");
$tmpl->setVar("CSS","templates/$TEMPLATE_DIR/include/$TMPL_CFG_CSS");
$tmpl->setVar("IMAGES_DIR","templates/$TEMPLATE_DIR/images/");
$tmpl->setVar("ADMINMODE_LINK","admin/index.php");
$tmpl->setLoop("NAVBAR",getNavBar());


//read the base parameters
$id = "";
@$id = $_REQUEST["id"];

$infos = getPlayerInfo($id);
$tmpl->setVar($infos);
$tmpl->setVar("id",$id);

//set the Context-Bar here to have the Player-Infos available
$contextbar = array();
$contextbar = addContextItem($contextbar,getActiveTitlePrefix());
$contextbar = addLinkedContextItem($contextbar,"index.php","Players");
$contextbar = addContextItem($contextbar,$infos["name"]);
$tmpl->setLoop("CONTEXTBAR",$contextbar);


$toprepair = getTopEngineerId();
$topheal = getTopMedicId();
$topscore = getTopScorerId();
$starnumber = getMaxStarNumber();
$topattack = getTopAttackerId();

$awards = getAwards($id,$infos["first"],$infos["second"],$infos["third"],$toprepair,$topheal,$topscore,$starnumber,$topattack);
$tmpl->setVar("awards",$awards["awards_withstars"]);
$tmpl->setVar("awards_withoutstars",$awards["awards_withoutstars"]);

$tmpl->setLoop("charactertypes",getCharacterTypeUsage($id));
$tmpl->setLoop("weaponusage",getWeaponUsage($id));
$tmpl->setLoop("topvictims",getTopVictims($id));
$tmpl->setLoop("topassasins",getTopAssasins($id));
$tmpl->setLoop("favvehicles",getFavVehicles($id));
$tmpl->setLoop("mapperformance",getMapPerformance($id));
$tmpl->setLoop("nicknames",getNickNames($id));

$healtimes = getHealTimes($id);
$tmpl->setVar("otherheals_time",$healtimes["otherheals"]["time"]);
$tmpl->setVar("otherheals_amount",$healtimes["otherheals"]["amount"]);
$tmpl->setVar("selfheals_time",$healtimes["selfheals"]["time"]);
$tmpl->setVar("selfheals_amount",$healtimes["selfheals"]["amount"]);

$repairtimes = getRepairTimes($id);
$tmpl->setVar("repairs_time",$repairtimes["repairs"]["time"]);
$tmpl->setVar("repairs_amount",$repairtimes["repairs"]["amount"]);

$tmpl->setLoop("games",getLastGamesPlayer($id));


//now finish the processtime timer
$totaltime = timer()- $starttime;
$tmpl->setVar("PROCESSTIME",sprintf ("%01.2f seconds",$totaltime));

$tmpl->pparse();
?>



