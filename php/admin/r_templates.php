<?php
require("../include/sql.php");
require("admin_func.php");

//read the needed vars
$template= $_REQUEST["template"];

if(isAdmin())
{
	changeActiveTemplate($template);
	msg("Template changed! You'll have to <b>empty your cache-dir</b> in order to see the change, or simply wait until the cache becomes outdated. ");
	Header("Location: templates.php");
}
else
{
	Header("Location: login.php");
}
?>