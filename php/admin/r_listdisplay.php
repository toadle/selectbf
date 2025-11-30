<?php
require("../include/sql.php");
require("admin_func.php");


if(isAdmin())
{
	$ranking_player = $_REQUEST["ranking_player"];
	$ranking_games = $_REQUEST["ranking_games"];
	$clan_ranking = $_REQUEST["clan_ranking"];
	$character_type = $_REQUEST["character_type"];
	$character_repairs = $_REQUEST["character_repairs"];
	$character_heals = $_REQUEST["character_heals"];
	$map_rounds = $_REQUEST["map_rounds"];
	$map_killers = $_REQUEST["map_killers"];
	$map_attacks = $_REQUEST["map_attacks"];
	$map_deaths = $_REQUEST["map_deaths"];
	$map_tks = $_REQUEST["map_tks"];
	$weapons_list = $_REQUEST["weapons_list"];
	$vehicles_list = $_REQUEST["vehicles_list"];
	$player_nicknames = $_REQUEST["player_nicknames"];
	$player_characters = $_REQUEST["player_characters"];
	$player_weapons = $_REQUEST["player_weapons"];
	$player_victims = $_REQUEST["player_victims"];
	$player_assasins = $_REQUEST["player_assasins"];
	$player_vehicles = $_REQUEST["player_vehicles"];
	$player_maps = $_REQUEST["player_maps"];
	$player_games = $_REQUEST["player_games"];
	$clan_members = $_REQUEST["clan_members"];

	if(is_numeric($ranking_player))
	{
		setParamValues($ranking_player,"LIST-RANKING-PLAYER");
		msg("<i>Ranking Player</i> value saved<br>");
	}
	else
	{
		error("Ranking Player-value is not a number!<br>");
	}
	if(is_numeric($ranking_games))
	{
		setParamValues($ranking_games,"LIST-RANKING-GAMES");
		msg("<i>Ranking Games</i> value saved<br>");
	}
	else
	{
		error("Ranking Games-value is not a number!<br>");
	}
	if(is_numeric($clan_ranking))
	{
		setParamValues($clan_ranking,"LIST-CLAN-RANKING");
		msg("<i>Clan Ranking</i> value saved<br>");
	}
	else
	{
		error("Clan Ranking-value is not a number!<br>");
	}
	if(is_numeric($character_type))
	{
		setParamValues($character_type,"LIST-CHARACTER-TYPE");
		msg("<i>Character Type</i> value saved<br>");
	}
	else
	{
		error("Character Type-value is not a number!<br>");
	}
	if(is_numeric($character_repairs))
	{
		setParamValues($character_repairs,"LIST-CHARACTER-REPAIRS");
		msg("<i>Character Repairs</i> value saved<br>");
	}
	else
	{
		error("Character Repairs-value is not a number!<br>");
	}
	if(is_numeric($character_heals))
	{
		setParamValues($character_heals,"LIST-CHARACTER-HEALS");
		msg("<i>Character Heals</i> value saved<br>");
	}
	else
	{
		error("Character Heals-value is not a number!<br>");
	}
	if(is_numeric($map_rounds))
	{
		setParamValues($map_rounds,"LIST-MAP-ROUNDS");
		msg("<i>Map Rounds</i> value saved<br>");
	}
	else
	{
		error("Map Rounds-value is not a number!<br>");
	}
	if(is_numeric($map_killers))
	{
		setParamValues($map_killers,"LIST-MAP-KILLERS");
		msg("<i>Map Killers</i> value saved<br>");
	}
	else
	{
		error("Map Killers-value is not a number!<br>");
	}
	if(is_numeric($map_attacks))
	{
		setParamValues($map_attacks,"LIST-MAP-ATTACKS");
		msg("<i>Map Attacks</i> value saved<br>");
	}
	else
	{
		error("Map Attacks-value is not a number!<br>");
	}
	if(is_numeric($map_deaths))
	{
		setParamValues($map_deaths,"LIST-MAP-DEATHS");
		msg("<i>Map Deaths</i> value saved<br>");
	}
	else
	{
		error("Map Deaths-value is not a number!<br>");
	}
	if(is_numeric($map_tks))
	{
		setParamValues($map_tks,"LIST-MAP-TKS");
		msg("<i>Map TKS</i> value saved<br>");
	}
	else
	{
		error("Map TKS-value is not a number!<br>");
	}
	if(is_numeric($weapons_list))
	{
		setParamValues($weapons_list,"LIST-WEAPONS-LIST");
		msg("<i>Weapons List</i> value saved<br>");
	}
	else
	{
		error("Weapons List-value is not a number!<br>");
	}
	if(is_numeric($vehicles_list))
	{
		setParamValues($vehicles_list,"LIST-VEHICLES-LIST");
		msg("<i>Vehicles List</i> value saved<br>");
	}
	else
	{
		error("Vehicles List-value is not a number!<br>");
	}
	if(is_numeric($player_nicknames))
	{
		setParamValues($player_nicknames,"LIST-PLAYER-NICKNAMES");
		msg("<i>Player Nicknames</i> value saved<br>");
	}
	else
	{
		error("Player Nicknames-value is not a number!<br>");
	}
	if(is_numeric($player_characters))
	{
		setParamValues($player_characters,"LIST-PLAYER-CHARACTERS");
		msg("<i>Player Characters</i> value saved<br>");
	}
	else
	{
		error("Player Characters-value is not a number!<br>");
	}
	if(is_numeric($player_weapons))
	{
		setParamValues($player_weapons,"LIST-PLAYER-WEAPONS");
		msg("<i>Player Weapons</i> value saved<br>");
	}
	else
	{
		error("Player Weapons-value is not a number!<br>");
	}
	if(is_numeric($player_victims))
	{
		setParamValues($player_victims,"LIST-PLAYER-VICTIMS");
		msg("<i>Player Victims</i> value saved<br>");
	}
	else
	{
		error("Player Victims-value is not a number!<br>");
	}
	if(is_numeric($player_assasins))
	{
		setParamValues($player_assasins,"LIST-PLAYER-ASSASINS");
		msg("<i>Player Assasins</i> value saved<br>");
	}
	else
	{
		error("Player Assasins-value is not a number!<br>");
	}
	if(is_numeric($player_vehicles))
	{
		setParamValues($player_vehicles,"LIST-PLAYER-VEHICLES");
		msg("<i>Player Vehicles</i> value saved<br>");
	}
	else
	{
		error("Player Vechicles-value is not a number!<br>");
	}
	if(is_numeric($player_maps))
	{
		setParamValues($player_maps,"LIST-PLAYER-MAPS");
		msg("<i>Player Maps</i> value saved<br>");
	}
	else
	{
		error("Player Maps-value is not a number!<br>");
	}
	if(is_numeric($player_games))
	{
		setParamValues($player_games,"LIST-PLAYER-GAMES");
		msg("<i>Player Games</i> value saved<br>");
	}
	else
	{
		error("Player Games-value is not a number!<br>");
	}
	if(is_numeric($clan_members))
	{
		setParamValues($clan_members,"LIST-CLAN-MEMBERS");
		msg("<i>Clan Members</i> value saved<br>");
	}
	else
	{
		error("Clan Members-value is not a number!<br>");
	}

    Header("Location: listdisplay.php");
}
else
{
	Header("Location: login.php");
}
?>