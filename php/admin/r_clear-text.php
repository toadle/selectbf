<?
require("../include/sql.php");
require("admin_func.php");

//read the needed vars
@$cleared = $_REQUEST["cleared"];
@$uncleared = $_REQUEST["uncleared"];
$todo = $_REQUEST["todo"];
@$id = $_REQUEST["id"];


if(isAdmin())
{
	switch($todo)
	{
		case "vehicle":		if(strlen($cleared)==0)
						{	
							error("Please enter a value for <b>Cleared-Text</b>!");
						}
						else
						{
							addClearedText($uncleared,$cleared,"VEHICLE"); 
							msg("New Clear-Text for Vehicle <b>".$uncleared."</b> saved<br>");
						} 
						break;
		case "kit":		if(strlen($cleared)==0)
						{	
							error("Please enter a value for <b>Cleared-Text</b>!");
						}
						else
						{
							addClearedText($uncleared,$cleared,"KIT"); 
							msg("New Clear-Text for Kit <b>".$uncleared."</b> saved<br>");
						} 
						break;
		case "weapon":	if(strlen($cleared)==0)
						{	
							error("Please enter a value for <b>Cleared-Text</b>!");
						}
						else
						{
							addClearedText($uncleared,$cleared,"WEAPON"); 
							msg("New Clear-Text for Weapon <b>".$uncleared."</b> saved<br>");
						} 
						break;
		case "gamemode":if(strlen($cleared)==0)
						{	
							error("Please enter a value for <b>Cleared-Text</b>!");
						}
						else
						{
							addClearedText($uncleared,$cleared,"GAME-MODE"); 
							msg("New Clear-Text for Game-Mode <b>".$uncleared."</b> saved<br>");
						} 
						break;
		case "map":		if(strlen($cleared)==0)
						{	
							error("Please enter a value for <b>Cleared-Text</b>!");
						}
						else
						{
							addClearedText($uncleared,$cleared,"MAP"); 
							msg("New Clear-Text for Map <b>".$uncleared."</b> saved<br>");
						} 
						break; 						 
		case "delete":	deleteClearText($id); 
						msg("Clear-Text <b>deleted</b><br>"); 
						break;
	}	
	
	if(isset($_SESSION["ClearTextKIT"])) unset($_SESSION["ClearTextKIT"]);
	if(isset($_SESSION["ClearTextVEHICLE"])) unset($_SESSION["ClearTextVEHICLE"]);
	if(isset($_SESSION["ClearTextWEAPON"])) unset($_SESSION["ClearTextWEAPON"]);
	if(isset($_SESSION["ClearTextGAME-MODE"])) unset($_SESSION["ClearTextGAME-MODE"]);
	if(isset($_SESSION["ClearTextMAP"])) unset($_SESSION["ClearTextMAP"]);
	
	Header("Location: clear-text.php");
}
else
{
	Header("Location: login.php");
}
?>