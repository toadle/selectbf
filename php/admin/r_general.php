<?
require("../include/sql.php");
require("admin_func.php");

//read the needed vars
$debuglevel= $_REQUEST["debuglvl"];
$titleprefix = $_REQUEST["titleprefix"];

if(isAdmin())
{
	changeDebugLevel($debuglevel);
	setTitlePrefix($titleprefix);
	msg("Settings saved!");	
	Header("Location: general.php");
}
else
{
	Header("Location: login.php");
}
?>