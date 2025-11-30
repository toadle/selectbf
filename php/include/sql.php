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
	<?php
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


function SQL_query($sql, $params = [])
{
	GLOBAL $DBVerbindung;
    if (empty($params)) {
	    $res = mysqli_query($DBVerbindung, $sql) or die(SQL_error(mysqli_error($DBVerbindung),$sql));
	    return $res;
    }
    
    $stmt = mysqli_prepare($DBVerbindung, $sql);
    if (!$stmt) {
        die(SQL_error(mysqli_error($DBVerbindung), $sql));
    }

    $types = "";
    $values = [];
    foreach ($params as $param) {
        if (is_int($param)) {
            $types .= "i";
        } elseif (is_double($param)) {
            $types .= "d";
        } else {
            $types .= "s";
        }
        $values[] = $param;
    }
    
    if (!empty($values)) {
        mysqli_stmt_bind_param($stmt, $types, ...$values);
    }

    mysqli_stmt_execute($stmt) or die(SQL_error(mysqli_stmt_error($stmt), $sql));
    $res = mysqli_stmt_get_result($stmt);
    mysqli_stmt_close($stmt);
    
	return $res;
}

function SQL_oneRowQuery($sql, $params = [])
{
	$res = SQL_query($sql, $params);
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
