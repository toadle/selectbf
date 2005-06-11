<?
require("../include/sql.php");
require("admin_func.php");

//read the needed vars
@$todo = $_REQUEST["todo"];


if(isAdmin())
{
	if($todo == "game")
	{
		$id = $_REQUEST["id"];	
		deleteGame($id);
		msg("Game $id <b>deleted</b>!<br>");			
	} else
	if($todo == "round")
	{
		deleteRound($id);
		msg("Round $id <b>deleted</b>!<br>");		
	}
	
	
	if(isset($_REQUEST["gameid"]))
	{
		Header("Location: rem_rounds.php?id=".$gameid);
	}
	else
	{
		Header("Location: rem_rounds.php");
	}
}
else
{
	Header("Location: login.php");
}
?>