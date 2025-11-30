<?php
require("../include/sql.php");
require("admin_func.php");

//read the needed vars
$minrounds = $_REQUEST["minrounds"];
$starnumber = $_REQUEST["starnumber"];
$orderbycolumn = $_REQUEST["orderbycolumn"];
//Added by Gary Chiu
$clanparserpath = $_REQUEST["clanparserpath"];
$minclanmembers = $_REQUEST["minclanmembers"];
$minclanrounds = $_REQUEST["minclanrounds"];

if(isAdmin())
{
	if(is_numeric($minrounds))
	{
		setMinRounds($minrounds);
		msg("<i>Minimum Rounds</i> value saved<br>");
	}
	else
	{
		error("Minimum Rounds-value is not a number!<br>");
	}
	
	if(is_numeric($starnumber))
	{
		setStarNumber($starnumber);
		msg("<i>Number of stars</i> value saved<br>");
	}
	else
	{
		error("The value for <i>number of stars</i> is not a number!<br>");
	}

	//Added By Gary Chiu
	if(is_numeric($minclanmembers))
	{
		setMinClanMembers($minclanmembers);
		msg("<i>Minimum Clan's Members</i> value saved<br>");
	}
	else
	{
		error("Minimum Clan's Members-value is not a number!<br>");
	}
	if(is_numeric($minclanrounds))
	{
		setMinClanRounds($minclanrounds);
		msg("<i>Minimum Clan's Rounds</i> value saved<br>");
	}
	else
	{
		error("Minimum Clan's Rounds-value is not a number!<br>");
	}
	//
	setRankOrderByColumn($orderbycolumn);
	msg("<i>Startup Order Column</i> saved<br>");
    Header("Location: display.php");
}
else
{
	Header("Location: login.php");
}
?>