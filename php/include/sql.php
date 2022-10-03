<?php
require("sql_setting.php");

$DBVerbindung = SQL_connect();

function SQL_error($msg,$sql)
{
	?>
   <html>
   <head>
   <title>select(bf) Error</title>
   <link rel="stylesheet" href="templates/default/include/style.css" type="text/css">
   </head>
   <body>
   <center>
   <table class=navbar width=450>
   <tr class=axis>
    <th><img src="templates/default/images/icon_error.gif" align=absmiddle hspace=2> Database Error</th>
   </tr>
   <tr>
    <td class=admin>
     <b>There was an database-error.</b><br>
     <u><b>mysql-Error-Message:</b></u>
     <i><?echo $msg;?></i><br>
     <p>
     <u><b>SQL-Command:</b></u>
     <i><?echo $sql;?></i>
     <p>
    </td>
   </tr>
  </table>
  </center>
  </html>
	<?
}

function SQL_connect()
{
	GLOBAL $SQL_host,$SQL_user,$SQL_datenbank,$SQL_password;
	$con = mysqli_connect($SQL_host,$SQL_user,$SQL_password) or die(SQL_error(mysqli_error(),"at connect"));
	mysqli_select_db($con, $SQL_datenbank);
	return $con;
}

function SQL_getconnection() {
	GLOBAL $DBVerbindung;
	return $DBVerbindung;
}


function SQL_query($sql)
{
	GLOBAL $DBVerbindung;
	$res = mysqli_query($DBVerbindung, $sql) or die(SQL_error(mysqli_error($DBVerbindung),$sql));
	
	return $res;
}

function SQL_oneRowQuery($sql)
{
	GLOBAL $DBVerbindung;
	$res = mysqli_query($DBVerbindung, $sql) or die(SQL_error(mysqli_error($DBVerbindung),$sql));
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
