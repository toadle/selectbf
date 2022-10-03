<?
require_once("../include/vLib/vlibTemplate.php");
use clausvb\vlib\vlibTemplate;
require("../include/sql.php");
require("admin_func.php");
require_once("../templates/original/config.php");

if(!isAdmin())
{
	Header("Location: login.php");
}
else
{
//start the processtime-timer
$starttime=timer();	
	
//now start setting the variables for the Template
$tmpl = new vlibTemplate("../templates/original/admin/general.html");
//evaluate Messages and Errors!
require_once("messages.php");

//set basic Template-Variables
$tmpl->setVar("TITLE","select(bf) - Admin Panel");
$tmpl->setVar("CSS","../templates/original/include/$TMPL_CFG_CSS");
$tmpl->setVar("IMAGES_DIR","../templates/original/images/");   

$tmpl->setVar("form_action","r_general.php");

$debuglevel = getDebugLevel();
$debuglevels = array();

for($i=0;$i<4;$i++)
{
	if($i==$debuglevel)
	{
		array_push($debuglevels,array("level"=>$i,"selected"=>true));
	}
	else
	{
		array_push($debuglevels,array("level"=>$i,"selected"=>false));
	}
}
$tmpl->setLoop("debuglevels",$debuglevels);
$tmpl->setVar("debuglevel",$debuglevel);
$tmpl->setVar("param_debuglevel","debuglvl");

$tmpl->setVar("titleprefix",getTitlePrefix());
$tmpl->setVar("param_titleprefix","titleprefix");

//now finish the processtime timer
$totaltime = timer()- $starttime;
$tmpl->setVar("PROCESSTIME",sprintf ("%01.2f seconds",$totaltime));

@$tmpl->pparse();
}
?>
