<?
require("../include/sql.php");
require("admin_func.php");

//read the needed vars
@$psw= $_REQUEST["password"];

if(checkAdminPsw($psw))
{
	registerAdminLogin($psw);
	Header("Location: index.php");
}
else
{
	error("<b>Wrong</b> password!");
	Header("Location: login.php");
}
?>