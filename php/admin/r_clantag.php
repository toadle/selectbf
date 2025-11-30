<?php
require("../include/sql.php");
require("admin_func.php");

//read the needed vars
@$todo = $_REQUEST["todo"];
$clan_tag = $_REQUEST["clan_tag"];

if(isAdmin())
{
	if(isset($clan_tag)){
		setClanTag($clan_tag);
		msg("<i>Clan Tag</i> value saved<br>");
	}
	if($todo == "delete")
	{
		$id = $_REQUEST["id"];
		SQL_query("DELETE FROM selectbf_clan_tags where clan_tag = '".clear_special_char($id,false)."'");
		SQL_query("DELETE FROM selectbf_clan_ranking where clanname = '".clear_special_char($id,false)."'");
		msg("Clan Tag: $id <b>deleted</b>!<br>");			
	}
    Header("Location: clantag.php");
}
else
{
	Header("Location: login.php");
}
?>