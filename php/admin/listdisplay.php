<?
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
$tmpl = new vlibTemplate("../templates/original/admin/listdisplay.html");
//evaluate Messages and Errors!
require_once("messages.php");

//set basic Template-Variables
$tmpl->setVar("TITLE","select(bf) - Admin Panel");
$tmpl->setVar("CSS","../templates/original/include/$TMPL_CFG_CSS");
$tmpl->setVar("IMAGES_DIR","../templates/original/images/");   
$tmpl->setVar("form_action","r_listdisplay.php");

$tmpl->setVar("param_ranking_player","ranking_player");
$tmpl->setVar("param_ranking_games","ranking_games");
$tmpl->setVar("param_clan_ranking","clan_ranking");
$tmpl->setVar("param_character_type","character_type");
$tmpl->setVar("param_character_repairs","character_repairs");
$tmpl->setVar("param_character_heals","character_heals");
$tmpl->setVar("param_map_rounds","map_rounds");
$tmpl->setVar("param_map_killers","map_killers");
$tmpl->setVar("param_map_attacks","map_attacks");
$tmpl->setVar("param_map_deaths","map_deaths");
$tmpl->setVar("param_map_tks","map_tks");
$tmpl->setVar("param_weapons_list","weapons_list");
$tmpl->setVar("param_vehicles_list","vehicles_list");
$tmpl->setVar("param_player_nicknames","player_nicknames");
$tmpl->setVar("param_player_characters","player_characters");
$tmpl->setVar("param_player_weapons","player_weapons");
$tmpl->setVar("param_player_victims","player_victims");
$tmpl->setVar("param_player_vehicles","player_vehicles");
$tmpl->setVar("param_player_assasins","player_assasins");
$tmpl->setVar("param_player_maps","player_maps");
$tmpl->setVar("param_player_games","player_games");
$tmpl->setVar("param_clan_members","clan_members");
$tmpl->setVar("ranking_player",getParamValue("LIST-RANKING-PLAYER"));
$tmpl->setVar("ranking_games",getParamValue("LIST-RANKING-GAMES"));
$tmpl->setVar("clan_ranking",getParamValue("LIST-CLAN-RANKING"));
$tmpl->setVar("character_type",getParamValue("LIST-CHARACTER-TYPE"));
$tmpl->setVar("character_repairs",getParamValue("LIST-CHARACTER-REPAIRS"));
$tmpl->setVar("character_heals",getParamValue("LIST-CHARACTER-HEALS"));
$tmpl->setVar("map_rounds",getParamValue("LIST-MAP-ROUNDS"));
$tmpl->setVar("map_killers",getParamValue("LIST-MAP-KILLERS"));
$tmpl->setVar("map_attacks",getParamValue("LIST-MAP-ATTACKS"));
$tmpl->setVar("map_deaths",getParamValue("LIST-MAP-DEATHS"));
$tmpl->setVar("map_tks",getParamValue("LIST-MAP-TKS"));
$tmpl->setVar("weapons_list",getParamValue("LIST-WEAPONS-LIST"));
$tmpl->setVar("vehicles_list",getParamValue("LIST-VEHICLES-LIST"));
$tmpl->setVar("player_nicknames",getParamValue("LIST-PLAYER-NICKNAMES"));
$tmpl->setVar("player_characters",getParamValue("LIST-PLAYER-CHARACTERS"));
$tmpl->setVar("player_weapons",getParamValue("LIST-PLAYER-WEAPONS"));
$tmpl->setVar("player_victims",getParamValue("LIST-PLAYER-VICTIMS"));
$tmpl->setVar("player_assasins",getParamValue("LIST-PLAYER-ASSASINS"));
$tmpl->setVar("player_vehicles",getParamValue("LIST-PLAYER-VEHICLES"));
$tmpl->setVar("player_maps",getParamValue("LIST-PLAYER-MAPS"));
$tmpl->setVar("player_games",getParamValue("LIST-PLAYER-GAMES"));
$tmpl->setVar("clan_members",getParamValue("LIST-CLAN-MEMBERS"));

//now finish the processtime timer
$totaltime = timer()- $starttime;
$tmpl->setVar("PROCESSTIME",sprintf ("%01.2f seconds",$totaltime));

@$tmpl->pparse();
}
?>