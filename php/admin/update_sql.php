<?php
require("../include/sql_setting.php");
$DBVerbindung = SQL_connect();

function SQL_connect()
{
	GLOBAL $SQL_host,$SQL_user,$SQL_datenbank,$SQL_password;
	$con = mysql_connect($SQL_host,$SQL_user,$SQL_password) or die(mysql_error());
	mysql_select_db($SQL_datenbank);
	return $con;
}


function SQL_query($sql)
{
	GLOBAL $DBVerbindung;
	$res = mysql_query($sql,$DBVerbindung) or die(mysql_error());
	
	return $res;
}

function SQL_oneRowQuery($sql)
{
	GLOBAL $DBVerbindung;
	$res = mysql_query($sql,$DBVerbindung) or die(mysql_error());
	$Zeile = SQL_fetchArray($res);
	return $Zeile;
}

function SQL_fetchArray($ResultSet)
{
        $row = mysql_fetch_array($ResultSet,MYSQL_ASSOC);
        return $row;
}

function SQL_resetResult($ResultSet)
{
	@mysql_data_seek($ResultSet,0); 
}

function SQL_containsRows($ResultSet)
{
	if($row = mysql_fetch_array($ResultSet,MYSQL_ASSOC))
	{
		@mysql_data_seek($ResultSet,0); 
		return true;
	}
	else
	{
		return false;
	}
}
?>