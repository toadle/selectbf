<?php
require("../include/sql.php");
require("admin_func.php");

//read the needed vars
@$todo = $_REQUEST["todo"];


if(isAdmin())
{
	if($todo == "delete")
	{
		$id = $_REQUEST["id"];	
		SQL_query("DELETE FROM selectbf_chatlog where id = $id");
		SQL_query("DELETE FROM selectbf_drives where player_id = $id");
		SQL_query("DELETE FROM selectbf_heals where player_id = $id or healed_player_id = $id");
		SQL_query("DELETE FROM selectbf_kills_player where player_id = $id or victim_id = $id");
		SQL_query("DELETE FROM selectbf_kills_weapon where player_id = $id");
		SQL_query("DELETE FROM selectbf_kits where player_id = $id");
		SQL_query("DELETE FROM selectbf_nicknames where player_id = $id");
		SQL_query("DELETE FROM selectbf_playerstats where player_id = $id");
		SQL_query("DELETE FROM selectbf_playtimes where player_id = $id");
		SQL_query("DELETE FROM selectbf_repairs where player_id = $id");
		SQL_query("DELETE FROM selectbf_selfkills where player_id = $id");
		SQL_query("DELETE FROM selectbf_tks where player_id = $id or victim_id = $id");
		SQL_query("DELETE FROM selectbf_cache_ranking where player_id = $id");
		SQL_query("DELETE FROM selectbf_players where id = $id");
		msg("Player $id <b>deleted</b>!<br>");			
	}

    Header("Location: rem_players.php");
}
else
{
	Header("Location: login.php");
}
?>