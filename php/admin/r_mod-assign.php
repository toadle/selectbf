<?
require("../include/sql.php");
require("admin_func.php");

//read the needed vars
@$item = $_REQUEST["item"];
@$mod = $_REQUEST["mod"];
$todo = $_REQUEST["todo"];
@$id = $_REQUEST["id"];

@session_start();

if(isAdmin())
{
	switch($todo)
	{
		case "kit":		addAssignment($item,$mod,"KIT");
						msg("New Mod-Assignment for Kit <b>".$item."</b> saved<br>");
						break;
		case "weapon":	addAssignment($item,$mod,"WEAPON");
						msg("New Mod-Assignment for Weapon <b>".$item."</b> saved<br>");
						break;
		case "delete":	deleteAssignment($id); 
						msg("Assignment <b>deleted</b><br>"); 
						break;
	}	
	
	if(isset($_SESSION["AssignmentKIT"])) unset($_SESSION["AssignmentKIT"]);
	if(isset($_SESSION["AssignmentWEAPON"])) unset($_SESSION["AssignmentWEAPON"]);
	
	Header("Location: mod-assign.php");
}
else
{
	Header("Location: login.php");
}
?>