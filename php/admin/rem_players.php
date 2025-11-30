<?php
require_once("../include/vLib/vlibTemplate.php");
use clausvb\vlib\vlibTemplate;
require("../include/sql.php");
require("admin_func.php");
require_once("../templates/original/config.php");

if(!isAdmin())
{
	Header("Location: login.php");
}
else
{
//start the processtime-timer
$starttime=timer();	
	
//now start setting the variables for the Template
$tmpl = new vlibTemplate("../templates/original/admin/rem_players.html");


//set basic Template-Variables
$tmpl->setVar("TITLE","select(bf) - Admin Panel");
$tmpl->setVar("CSS","../templates/original/include/$TMPL_CFG_CSS");
$tmpl->setVar("IMAGES_DIR","../templates/original/images/");   

$tmpl->setVar("form_action","rem_players.php");

$tmpl->setVar("param_search_name","search_name");


if(isset($_REQUEST["search_name"]))
{
	$searchresults = array();
	$playername = $_REQUEST["search_name"];
	$playername = mysqli_real_escape_string(SQL_getconnection(), $playername); 
	
	$found = false;
	$res = SQL_query("select id, name, CONCAT(TIME_FORMAT(inserttime,'%H:%i:%S '),DATE_FORMAT(inserttime,'%d|%m|%Y')) time from selectbf_players WHERE name LIKE '%$playername%' ORDER BY name ASC");
	while($cols = SQL_fetchArray($res))
	{
		$found = true;
		$cols["deletelink"] = "r_rem_players.php?todo=delete&id=".$cols["id"];
		array_push($searchresults,$cols);
	}
	
	if($found)
	{
		$tmpl->setLoop("searchresults",$searchresults);
	}
	else
	{
		msg("There was no Player matching your search!");
	}
}

//evaluate Messages and Errors!
require_once("messages.php");

//now finish the processtime timer
$totaltime = timer()- $starttime;
$tmpl->setVar("PROCESSTIME",sprintf ("%01.2f seconds",$totaltime));

@$tmpl->pparse();
}
?>
