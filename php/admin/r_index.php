<?
require("../include/sql.php");
require("admin_func.php");

if(isAdmin())
{
	//read the needed vars
	$tigerwings= $_REQUEST["Tiger-Wings"];
	$healrepairs= $_REQUEST["Heal-Repairs"];
	$starnumber= $_REQUEST["star_number"];
	$lastgames= $_REQUEST["Last-Games"];
	$search= $_REQUEST["Search"];
	$total= $_REQUEST["Total"];
	$ordercolumn = $_REQUEST["ordercolumn"];
	
	setValueForParameter($tigerwings,"TIGER-WINGS");
	setValueForParameter($healrepairs,"HEAL-REPAIRS");
	setValueForParameter($starnumber,"STARS");
	setValueForParameter($lastgames,"LAST-GAMES");
	setValueForParameter($search,"SEARCH");
	setValueForParameter($total,"TOTAL");
	setValueForParameter($ordercolumn,"ORDER_COLUMN");
	
	Header("Location: index.php");	
}
else
{
	Header("Location: login.php");
}
?>