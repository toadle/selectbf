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


//now start setting the variables for the Template
if(!checkTemplateConsistency($TEMPLATE_DIR,"maps.html"))
{
		die(Template_error("This page is not templated yet, plz create a 'maps.html' for the '$TEMPLATE_DIR'-Template!"));
}

$tmpl = new vlibTemplate("templates/$TEMPLATE_DIR/maps.html");

//set basic Template-Variables
$tmpl->setVar("TITLE",getActiveTitlePrefix()." - Maps Overview");
$tmpl->setVar("CSS","templates/$TEMPLATE_DIR/include/$TMPL_CFG_CSS");
$tmpl->setVar("IMAGES_DIR","templates/$TEMPLATE_DIR/images/");
$tmpl->setVar("ADMINMODE_LINK","admin/index.php");
$tmpl->setLoop("NAVBAR",getNavBar());

$contextbar = array();
$contextbar = addContextItem($contextbar,getActiveTitlePrefix());
$contextbar = addContextItem($contextbar,"Maps");
$tmpl->setLoop("CONTEXTBAR",$contextbar);

//page specific data-collection

$tmpl->setLoop("mapstats",getMapStats());

//now finish the processtime timer
$totaltime = timer()- $starttime;
$tmpl->setVar("PROCESSTIME",sprintf ("%01.2f seconds",$totaltime));

@$tmpl->pparse();
?>



