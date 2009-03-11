<?
/************************************************
* Config file for select(bf)-Template Set       *
*                                               *
* Template-Name: "Default"                      *
* Author:        tadler                      *
* Last Modified: 22.10.2003                     *
* Version:       0.4                            *
*                                               *
*************************************************/

//PLEASE NOTE: If there are values in here that you don't want to set
//e.g. if you don't need a stats-bar in your template and so don't
//want to specify a graphic, DO NOT delete the whole line,
//simply set the value to "".

//Define the name of your primary CSS-File
//start path from "include"-subdir
$TMPL_CFG_CSS = "style.css";

//Define the the grachics that will be used to draw graph-bars
$TMPL_CFG_BAR['left'] = "stats_bar_left.gif";
$TMPL_CFG_BAR['middle'] = "stats_bar_middle.gif";
$TMPL_CFG_BAR['right'] = "stats_bar_right.gif";
$TMPL_CFG_BAR['height'] = "10";
$TMPL_CFG_BAR['maxwidth'] = "250";

//please specify graphics for statsbars for the specific teams
$TMPL_CFG_BAR_TEAMS['height'] = "10";
$TMPL_CFG_BAR_TEAMS['maxwidth'] = "250";
$TMPL_CFG_BAR_TEAMS['axis']['left'] = "stats_bar_left.gif";
$TMPL_CFG_BAR_TEAMS['axis']['middle'] = "stats_bar_middle_axis.gif";
$TMPL_CFG_BAR_TEAMS['axis']['right'] = "stats_bar_right.gif";
$TMPL_CFG_BAR_TEAMS['allies']['left'] = "stats_bar_left.gif";
$TMPL_CFG_BAR_TEAMS['allies']['middle'] = "stats_bar_middle_allies.gif";
$TMPL_CFG_BAR_TEAMS['allies']['right'] = "stats_bar_right.gif";

//Define the graphics for the little player-icons
$TMPL_CFG_PLAYER_IMG[0] = "soldier_icon.gif";

//Define the graphics for the different awards
//start paths from "images"-subdir
$TMPL_CFG_BRONZE_STAR_IMG = "star_bronze.gif";
$TMPL_CFG_SILVER_STAR_IMG = "star_silver.gif";
$TMPL_CFG_GOLD_STAR_IMG = "star_gold.gif";
$TMPL_CFG_TOP_HEAL_IMG = "top-heal.gif";
$TMPL_CFG_TOP_POINT_IMG = "top-point.gif";
$TMPL_CFG_TOP_REPAIR_IMG = "top-repair.gif";
$TMPL_CFG_TOP_SCORE_IMG = "top-score.gif";
$TMPL_CFG_TOP_ATTACK_IMG = "top-attack.gif";

//Define the graphics for the different MODs
//please mention the Modname that also appears in the log-files
//start paths from "images"-subdir
$TMPL_CFG_MOD_IMG["BF1942"] = "mod_BF1942.gif";
$TMPL_CFG_MOD_IMG["DesertCombat"] = "mod_DC.gif";
$TMPL_CFG_MOD_IMG["XPack1"] = "mod_Xpack1.gif";
$TMPL_CFG_MOD_IMG["XPack2"] = "mod_Xpack2.gif";

//Define the graphics for Axis and Allies
//start paths from "images"-subdir
$TMPL_CFG_AXIS_IMG = "axis_flag.gif";
$TMPL_CFG_ALLIED_IMG = "allied_flag.gif"; 

//now finally speficy the screenshots for the maps that you want to use
//please ALWAYS use the in-log mapname to identify the map, otherweise select(bf)
//will not the able to understand what you mean
//start paths from "images"-subdir
$TMPL_CFG_MAP_IMG["dc_oil_fields"] = "mapscreens/oil-fields.jpg";
$TMPL_CFG_MAP_IMG["guadalcanal"] = "mapscreens/guadalcanal.jpg";
$TMPL_CFG_MAP_IMG["kursk"] = "mapscreens/kursk.jpg";
$TMPL_CFG_MAP_IMG["iwo_jima"] = "mapscreens/iwo-jima.jpg";
$TMPL_CFG_MAP_IMG["dc_lostvillage"] = "mapscreens/lost-village.jpg";
$TMPL_CFG_MAP_IMG["bocage"] = "mapscreens/bocage.jpg";
$TMPL_CFG_MAP_IMG["berlin"] = "mapscreens/berlin.jpg";
$TMPL_CFG_MAP_IMG["battleaxe"] = "mapscreens/battleaxe.jpg";
$TMPL_CFG_MAP_IMG["battle_of_the_bulge"] = "mapscreens/battle-of-the-bulge.jpg";
$TMPL_CFG_MAP_IMG["el_alamein"] = "mapscreens/el-alamein.jpg";
$TMPL_CFG_MAP_IMG["Stalingrad"] = "mapscreens/stalingrad.jpg";
$TMPL_CFG_MAP_IMG["stalingrad"] = "mapscreens/stalingrad.jpg";
$TMPL_CFG_MAP_IMG["gazala"] = "mapscreens/gazala.jpg";
$TMPL_CFG_MAP_IMG["midway"] = "mapscreens/midway.jpg";
$TMPL_CFG_MAP_IMG["omaha_beach"] = "mapscreens/omaha-beach.jpg";
$TMPL_CFG_MAP_IMG["tobruk"] = "mapscreens/tobruk.jpg";
$TMPL_CFG_MAP_IMG["kharkov"] = "mapscreens/kharkov.jpg";
$TMPL_CFG_MAP_IMG["wake"] = "mapscreens/wake.jpg";
$TMPL_CFG_MAP_IMG["aberdeen"] = "mapscreens/aberdeen.jpg";
$TMPL_CFG_MAP_IMG["market_garden"] = "mapscreens/market-garden.jpg";
$TMPL_CFG_MAP_IMG["battle_of_britain"] = "mapscreens/battle-of-britain.jpg";
$TMPL_CFG_MAP_IMG["dc_al_khafji_docks"] = "mapscreens/al-khafji-docs.jpg";
$TMPL_CFG_MAP_IMG["dc_battle_of_73_easting"] = "mapscreens/battle-of-73-easting.jpg";
$TMPL_CFG_MAP_IMG["dc_medina_ridge"] = "mapscreens/medina-ridge.jpg";
$TMPL_CFG_MAP_IMG["dc_weapon_bunkers"] = "mapscreens/weapon-bunkers.jpg";
$TMPL_CFG_MAP_IMG["inshallah_valley"] = "mapscreens/inshallah-valley.jpg";
$TMPL_CFG_MAP_IMG["road_to_basra"] = "mapscreens/basrahs-edge.jpg";
$TMPL_CFG_MAP_IMG["anzio"] = "mapscreens/anzio.jpg";
$TMPL_CFG_MAP_IMG["baytown"] = "mapscreens/baytown.jpg";
$TMPL_CFG_MAP_IMG["cassino"] = "mapscreens/cassino.jpg";
$TMPL_CFG_MAP_IMG["husky"] = "mapscreens/husky.jpg";
$TMPL_CFG_MAP_IMG["salerno"] = "mapscreens/salerno.jpg";
$TMPL_CFG_MAP_IMG["santo_croce"] = "mapscreens/santa-croce.jpg";
// Added Secret Weapon maps (images were already in place)
// jrivett 2009Feb26
$TMPL_CFG_MAP_IMG["eagles_nest"] = "mapscreens/eagles-nest.jpg";
$TMPL_CFG_MAP_IMG["essen"] = "mapscreens/essen.jpg";
$TMPL_CFG_MAP_IMG["gothic_line"] = "mapscreens/gothic-line.jpg";
$TMPL_CFG_MAP_IMG["hellendoorn"] = "mapscreens/hellendoorn.jpg";
$TMPL_CFG_MAP_IMG["kbely_airfield"] = "mapscreens/kbelly-airfield.jpg";
$TMPL_CFG_MAP_IMG["mimoyecques"] = "mapscreens/mimoyecques.jpg";
$TMPL_CFG_MAP_IMG["peenemunde"] = "mapscreens/peenemunde.jpg";
$TMPL_CFG_MAP_IMG["telemark"] = "mapscreens/telemark.jpg";

?>