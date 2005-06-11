<?
/************************************************
* Config file for select(bf)-Template Set       *
*                                               *
* Template-Name: "Original"                     *
* Author:        tadler                         *
* Last Modified: 16.05.2004                     *
* Version:       0.4                            *
*                                               *
*************************************************/

//Define the name of your primare CSS-File
//start path from "include"-subdir
$TMPL_CFG_CSS = "selectbf.css";

//Define the the grachic that will be used to draw graph-bars
$TMPL_CFG_BAR['left'] = "stats_bar_left.gif";
$TMPL_CFG_BAR['middle'] = "stats_bar_middle.gif";
$TMPL_CFG_BAR['right'] = "stats_bar_right.gif";
$TMPL_CFG_BAR['height'] = "10";
$TMPL_CFG_BAR['maxwidth'] = "100"; 

//Define the graphics for the little player-icons
$TMPL_CFG_PLAYER_IMG[0] = "symbols/soldier_us.gif";
$TMPL_CFG_PLAYER_IMG[1] = "symbols/soldier_ger.gif";
$TMPL_CFG_PLAYER_IMG[2] = "symbols/soldier_brit.gif";
$TMPL_CFG_PLAYER_IMG[3] = "symbols/soldier_jap.gif";
$TMPL_CFG_PLAYER_IMG[4] = "symbols/soldier_rus.gif";

//Define the graphics for the different awards
//start paths from "images"-subdir
$TMPL_CFG_BRONZE_STAR_IMG = "symbols/star_bronze.gif";
$TMPL_CFG_SILVER_STAR_IMG = "symbols/star_silver.gif";
$TMPL_CFG_GOLD_STAR_IMG = "symbols/star_gold.gif";
$TMPL_CFG_TOP_HEAL_IMG = "symbols/top-heal.gif";
$TMPL_CFG_TOP_POINT_IMG = "symbols/top-point.gif";
$TMPL_CFG_TOP_REPAIR_IMG = "symbols/top-repair.gif";
$TMPL_CFG_TOP_SCORE_IMG = "symbols/top-score.gif";
$TMPL_CFG_TOP_ATTACK_IMG = "symbols/top-attack.gif";

//Define the graphics for the different MODs
//please mention the Modname that also appears in the log-files
//start paths from "images"-subdir
$TMPL_CFG_MOD_IMG["bf1942"] = "symbols/mod_BF1942.gif";
$TMPL_CFG_MOD_IMG["DesertCombat"] = "symbols/mod_DC.gif";
$TMPL_CFG_MOD_IMG["xpack1"] = "symbols/mod_Xpack1.gif";
$TMPL_CFG_MOD_IMG["xpack2"] = "symbols/mod_Xpack2.gif";

//Define the graphics for Axis and Allies
//start paths from "images"-subdir
$TMPL_CFG_AXIS_IMG = "symbols/axis_flag.gif";
$TMPL_CFG_ALLIED_IMG = "symbols/allied_flag.gif"; 

//please specify graphics for statsbars for the specific teams
$TMPL_CFG_BAR_TEAMS['height'] = "10";
$TMPL_CFG_BAR_TEAMS['maxwidth'] = "250";
$TMPL_CFG_BAR_TEAMS['axis']['left'] = "stats_bar_left.gif";
$TMPL_CFG_BAR_TEAMS['axis']['middle'] = "stats_bar_middle_axis.gif";
$TMPL_CFG_BAR_TEAMS['axis']['right'] = "stats_bar_right.gif";
$TMPL_CFG_BAR_TEAMS['allies']['left'] = "stats_bar_left.gif";
$TMPL_CFG_BAR_TEAMS['allies']['middle'] = "stats_bar_middle_allies.gif";
$TMPL_CFG_BAR_TEAMS['allies']['right'] = "stats_bar_right.gif";

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
$TMPL_CFG_MAP_IMG["Kharkov"] = "mapscreens/kharkov.jpg";
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
$TMPL_CFG_MAP_IMG["raid_on_agheila"] = "mapscreens/agheila.jpg";
$TMPL_CFG_MAP_IMG["liberation_of_caen"] = "mapscreens/caen.jpg";
$TMPL_CFG_MAP_IMG["hue"] = "mapscreens/hue1968.jpg";
$TMPL_CFG_MAP_IMG["ho_chi_minh_trail"] = "mapscreens/hochiminhtrail-b.jpg";
$TMPL_CFG_MAP_IMG["ia_drang"] = "mapscreens/iadrangvalley-d.jpg";
$TMPL_CFG_MAP_IMG["khe_sahn"] = "mapscreens/siegeofkhesahn-a.jpg";
$TMPL_CFG_MAP_IMG["landing_zone_albany"] = "mapscreens/landingzonealbany-c.jpg";
$TMPL_CFG_MAP_IMG["lang_vei"] = "mapscreens/falloflangvei-d.jpg";
$TMPL_CFG_MAP_IMG["operation_flaming_dart"] = "mapscreens/operationflamingdart-a.jpg";
$TMPL_CFG_MAP_IMG["Operation_Game_Warden"] = "mapscreens/operationgamewarden-a.jpg";
$TMPL_CFG_MAP_IMG["operation_hastings"] = "mapscreens/operationhastings-d.jpg";
$TMPL_CFG_MAP_IMG["quang_tri"] = "mapscreens/quangtri1972-b.jpg";
?>