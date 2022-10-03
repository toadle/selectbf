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
$tmpl = new vlibTemplate("../templates/original/admin/clear-text.html");
//evaluate Messages and Errors!
require_once("messages.php");

//set basic Template-Variables
$tmpl->setVar("TITLE","select(bf) - Admin Panel");
$tmpl->setVar("CSS","../templates/original/include/$TMPL_CFG_CSS");
$tmpl->setVar("IMAGES_DIR","../templates/original/images/");   

//Parameter-Names
$tmpl->setVar("param_todo","todo");
$tmpl->setVar("todo_value_kit","kit");
$tmpl->setVar("todo_value_map","map");
$tmpl->setVar("todo_value_vehicle","vehicle");
$tmpl->setVar("todo_value_weapon","weapon");
$tmpl->setVar("todo_value_gamemode","gamemode");
$tmpl->setVar("param_uncleared","uncleared");
$tmpl->setVar("param_cleared","cleared");

//Weapon Data
$tmpl->setLoop("ClearedGameModes",getClearedGameModes());
$tmpl->setLoop("UnclearedGameModes",getUnClearedGameModes());

//Weapon Data
$tmpl->setLoop("ClearedWeapons",getClearedWeapons());
$tmpl->setLoop("UnclearedWeapons",getUnClearedWeapons());

//Vehicle Data
$tmpl->setLoop("ClearedVehicles",getClearedVehicles());
$tmpl->setLoop("UnclearedVehicles",getUnClearedVehicles());

//Map Data
$tmpl->setLoop("ClearedMaps",getClearedMaps());
$tmpl->setLoop("UnclearedMaps",getUnClearedMaps());

//Kit Data
$tmpl->setLoop("ClearedKits",getClearedKits());
$tmpl->setLoop("UnclearedKits",getUnClearedKits());

$tmpl->setVar("form_action","r_clear-text.php");

//now finish the processtime timer
$totaltime = timer()- $starttime;
$tmpl->setVar("PROCESSTIME",sprintf ("%01.2f seconds",$totaltime));

@$tmpl->pparse();
}
?>
