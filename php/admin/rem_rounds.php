<?
require_once("../include/vLib/vlibTemplate.php");
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
$tmpl = new vlibTemplate("../templates/original/admin/rem_rounds.html");


//set basic Template-Variables
$tmpl->setVar("TITLE","select(bf) - Admin Panel");
$tmpl->setVar("CSS","../templates/original/include/$TMPL_CFG_CSS");
$tmpl->setVar("IMAGES_DIR","../templates/original/images/");   

$tmpl->setVar("form_action","rem_rounds.php");

$tmpl->setVar("param_gameid","id");


if(isset($_REQUEST["id"]))
{
	$searchresults = array();
	$id = $_REQUEST["id"];

    @$cols = SQL_oneRowQuery("select id,servername,modid 'mod',mapid,map,game_mode,gametime,maxplayers,scorelimit,spawntime,soldierff,vehicleff,tkpunish,deathcamtype,CONCAT(TIME_FORMAT(starttime,'%H:%i:%S '),DATE_FORMAT(starttime,'%d|%m|%Y')) date from selectbf_games where id=$id");
    if($cols)
    {
    	$cols["map"] = clearUpText($cols['map'],"MAP");
    	$cols['game_mode'] = clearUpText($cols['game_mode'],"GAME-MODE");
    	$tmpl->setVar($cols);
		$tmpl->setVar("deletegamelink","r_rem_rounds.php?todo=game&id=".$id);
    }
	
	$found = false;
	$res = SQL_query("select id,start_tickets_team1,start_tickets_team2,CONCAT(TIME_FORMAT(starttime,'%H:%i:%S '),DATE_FORMAT(starttime,'%d|%m|%Y')) starttime,end_tickets_team1,end_tickets_team2,CONCAT(TIME_FORMAT(endtime,'%H:%i:%S '),DATE_FORMAT(endtime,'%d|%m|%Y')) endtime,endtype,winning_team from selectbf_rounds where game_id=$id order by starttime ASC");
	while($cols = SQL_fetchArray($res))
	{
		$found = true;
		$cols["deletelink"] = "r_rem_rounds.php?&gameid=".$id."&todo=round&id=".$cols["id"];
		array_push($searchresults,$cols);
	}
	
	if($found)
	{
		$tmpl->setLoop("searchresults",$searchresults);
	}
	else
	{
		msg("There was <b>no Game</b> for ID $id!</br>");
	}
}

//evaluate Messages and Errors!
require_once("messages.php");

//now finish the processtime timer
$totaltime = timer()- $starttime;
$tmpl->setVar("PROCESSTIME",sprintf ("%01.2f seconds",$totaltime));

@$tmpl->pparse();
}
?>
