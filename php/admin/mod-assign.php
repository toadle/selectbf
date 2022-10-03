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
$tmpl = new vlibTemplate("../templates/original/admin/mod-assign.html");
//evaluate Messages and Errors!
require_once("messages.php");

//set basic Template-Variables
$tmpl->setVar("TITLE","select(bf) - Admin Panel");
$tmpl->setVar("CSS","../templates/original/include/$TMPL_CFG_CSS");
$tmpl->setVar("IMAGES_DIR","../templates/original/images/");   

$tmpl->setVar("form_action","r_mod-assign.php");
$tmpl->setVar("param_todo","todo");
$tmpl->setVar("todo_value_kit","kit");
$tmpl->setVar("todo_value_weapon","weapon");
$tmpl->setVar("param_item","item");
$tmpl->setVar("param_mod","mod");

$tmpl->setLoop("mods",getMods());

$tmpl->setLoop("unassigned_kits",getUnAssignedKits());
$tmpl->setLoop("kits",getAssignments("KIT"));

$tmpl->setLoop("unassigned_weapons",getUnAssignedWeapons());
$tmpl->setLoop("weapons",getAssignments("WEAPON"));

//now finish the processtime timer
$totaltime = timer()- $starttime;
$tmpl->setVar("PROCESSTIME",sprintf ("%01.2f seconds",$totaltime));

@$tmpl->pparse();
}
?>
