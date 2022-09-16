<?php
require("../include/sql_setting.php");
$DBVerbindung = SQL_connect();

function SQL_connect()
{
	GLOBAL $SQL_host,$SQL_admin_user,$SQL_datenbank,$SQL_admin_password;
	$con = mysqli_connect($SQL_host,$SQL_admin_user,$SQL_admin_password) or die(mysqli_connect_error());
	mysqli_select_db($con, $SQL_datenbank);
	return $con;
}


function SQL_query($sql)
{
	GLOBAL $DBVerbindung;
	$res = mysqli_query($DBVerbindung, $sql) or die(mysqli_error($DBVerbindung));
	
	return $res;
}

function SQL_oneRowQuery($sql)
{
	GLOBAL $DBVerbindung;
	$res = mysqli_query($DBVerbindung, $sql) or die(mysqli_error($DBVerbindung));
	$Zeile = SQL_fetchArray($res);
	return $Zeile;
}

function SQL_fetchArray($ResultSet)
{
        $row = mysqli_fetch_array($ResultSet,MYSQLI_ASSOC);
        return $row;
}

function SQL_resetResult($ResultSet)
{
	@mysqli_data_seek($ResultSet,0); 
}

function SQL_containsRows($ResultSet)
{
	if($row = mysqli_fetch_array($ResultSet,MYSQLI_ASSOC))
	{
		@mysqli_data_seek($ResultSet,0); 
		return true;
	}
	else
	{
		return false;
	}
}
?>
