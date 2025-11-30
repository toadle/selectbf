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
if(!checkTemplateConsistency($TEMPLATE_DIR,"chatlog.html"))
{
		die(Template_error("This page is not templated yet, plz create a 'chatlog.html' for the '$TEMPLATE_DIR'-Template!"));
}
$tmpl = new vlibTemplate("templates/$TEMPLATE_DIR/chatlog.html");

//set basic Template-Variables
$tmpl->setVar("TITLE",getActiveTitlePrefix()." - Chat Log Details");
$tmpl->setVar("CSS","templates/$TEMPLATE_DIR/include/$TMPL_CFG_CSS");
$tmpl->setVar("IMAGES_DIR","templates/$TEMPLATE_DIR/images/");
$tmpl->setVar("ADMINMODE_LINK","admin/index.php");
$tmpl->setLoop("NAVBAR",getNavBar());


//read the base parameters
$id = "";
@$id = $_REQUEST["id"];

$gameinfos = getGameInfo($id);
$tmpl->setVar($gameinfos);

//set Context-Bar at the end to have gameinfos
$contextbar = array();
$contextbar = addContextItem($contextbar,getActiveTitlePrefix());
$contextbar = addLinkedContextItem($contextbar,"index.php","Ranking");
$contextbar = addLinkedContextItem($contextbar,"game.php?id=$id",$gameinfos["servername"]." - ".$gameinfos["map"]." - ".$gameinfos["date"]);
$contextbar = addContextItem($contextbar,"Chatlog");
$tmpl->setLoop("CONTEXTBAR",$contextbar);

$tmpl->setVar("id",$id);
$tmpl->setLoop("chatlog",getChatLogForRound($id));

//now finish the processtime timer
$totaltime = timer()- $starttime;
$tmpl->setVar("PROCESSTIME",sprintf ("%01.2f seconds",$totaltime));

@$tmpl->pparse();
?>



