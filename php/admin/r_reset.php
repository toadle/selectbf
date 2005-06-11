<?
require("../include/sql.php");
require("admin_func.php");

if(isAdmin())
{
	if(isset($_REQUEST["sure"]) && isset($_REQUEST["reallysure"]))
	{
		SQL_query("TRUNCATE `selectbf_cache_chartypeusage`");
		SQL_query("TRUNCATE `selectbf_cache_mapstats`");
		SQL_query("TRUNCATE `selectbf_cache_ranking");
		SQL_query("TRUNCATE `selectbf_cache_vehicletime`");
		SQL_query("TRUNCATE `selectbf_cache_weaponkills`");		
		SQL_query("TRUNCATE `selectbf_drives`");
		SQL_query("TRUNCATE `selectbf_games`");
		SQL_query("TRUNCATE `selectbf_heals`");
		SQL_query("TRUNCATE `selectbf_kills_player`");
		SQL_query("TRUNCATE `selectbf_kills_weapon`");		
		SQL_query("TRUNCATE `selectbf_kits`");
		SQL_query("TRUNCATE `selectbf_players`");
		SQL_query("TRUNCATE `selectbf_playtimes`");
		SQL_query("TRUNCATE `selectbf_playerstats`");
		SQL_query("TRUNCATE `selectbf_repairs`");
		SQL_query("TRUNCATE `selectbf_rounds`");
		SQL_query("TRUNCATE `selectbf_selfkills`");
		SQL_query("TRUNCATE `selectbf_tks`");
		SQL_query("TRUNCATE `selectbf_nicknames`");
		SQL_query("TRUNCATE `selectbf_chatlog`");
		SQL_query("TRUNCATE `selectbf_clan_ranking`");
		msg("<i>Stats</i> reseted<br>");
	}
	else
	{
		error("I were not <b>sure</b> and <b>really sure</b>!<br>");
	}
    Header("Location: reset.php");
}
else
{
	Header("Location: login.php");
}
?>