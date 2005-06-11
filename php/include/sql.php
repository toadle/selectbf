<?php
require("sql_setting.php");

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
     Please report this error in the forums at <a href="http://www.selectbf.org">www.selectbf.org</a> or send mail to <a href="mailto:selectbf@s-h-i-n-y.com">selectbf@s-h-i-n-y.com</a>! <b>Also</b> provide the following information and the <b>page of the stats</b> you were trying to access.<p>
     <u><b>MySQL-Error-Message:</b></u>
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
	$con = mysql_connect($SQL_host,$SQL_user,$SQL_password) or die(SQL_error(mysql_error(),"at connect"));
	mysql_select_db($SQL_datenbank);
	return $con;
}


function SQL_query($sql)
{
	GLOBAL $DBVerbindung;
	$res = mysql_query($sql,$DBVerbindung) or die(SQL_error(mysql_error(),$sql));
	
	return $res;
}

function SQL_oneRowQuery($sql)
{
	GLOBAL $DBVerbindung;
	$res = mysql_query($sql,$DBVerbindung) or die(SQL_error(mysql_error(),$sql));
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