<?
require("../include/sql.php");
require("admin_func.php");

//read the needed vars
$password = $_REQUEST["password"];
$retype = $_REQUEST["retype"];


if(isAdmin())
{
	if($password == $retype)
	{
		if(!(strlen($password)<3))
		{
			changePassword($password);
			msg("<i>Password</i> changed<br>");
		}
		else
		{
			error("<b>Password</b> was too short! <i>(at least 4 letters)</i><br>");
		}
	}
	else
	{
		error("<b>Password</b> and <b>Retype</b> did not match!<br>");
	}
    Header("Location: password.php");
}
else
{
	Header("Location: login.php");
}
?>