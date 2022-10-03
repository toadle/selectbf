<?php
//set error-level
error_reporting(E_ERROR);


//for random picture generation
$start = (int) mktime(date("h"),date("i"),date("s"),date("d"),date("m"),date("Y"));
mt_srand($start);

@session_start();
// Set session variables
if(!isset($_SESSION['sessionset']))
{
	setsessionvariables();
}


function setsessionvariables()
{
	$res = SQL_Query("SELECT name, value FROM selectbf_params"); 
   	while($cols = SQL_fetchArray($res))
	{
		$paramname = $cols["name"];
		$paramvalue = $cols["value"];
		$_SESSION["$paramname"] = $paramvalue;
	}
	$_SESSION["sessionset"] = 1;
}

//if Debug-Messages should be displayed or not
$DEBUG = getActiveDebugLevel();
if($DEBUG) echo "<b><u>DEBUG</b>-Informations:</u><br>";

function getNavBar()
{
	$navbar = array();
	array_push($navbar,array("link"=>"index.php","name"=>"Players"));
	array_push($navbar,array("link"=>"clans.php","name"=>"Clans")); 
	array_push($navbar,array("link"=>"character.php","name"=>"Character-Types"));
	array_push($navbar,array("link"=>"weapon.php","name"=>"Weapons"));
	array_push($navbar,array("link"=>"vehicle.php","name"=>"Vehicles"));
	array_push($navbar,array("link"=>"maps.php","name"=>"Maps"));
	return $navbar;
}

function addLinkedContextItem($array,$link,$text)
{
	array_push($array,array("link"=>$link,"name"=>$text));
	return $array;
}

function addContextItem($array,$text)
{
	array_push($array,array("link"=>false,"name"=>$text));
	return $array;
}

function getMapImage($str)
{
	GLOBAL $TMPL_CFG_MAP_IMG,$TEMPLATE_DIR;
	
	if(isset($TMPL_CFG_MAP_IMG[$str]))
	{
		return "templates/$TEMPLATE_DIR/images/".$TMPL_CFG_MAP_IMG[$str];
	}
	else
	{
		return "templates/$TEMPLATE_DIR/images/blank.gif";
	}
}

function clearUpText($text,$type)
{
	@session_start();
	
	if(isset($_SESSION["ClearText".$type]))
	{
		$lookup = $_SESSION["ClearText".$type];
		
		if(isset($lookup[$text]))
		{
			return $lookup[$text];
		}
		else
		{
			return $text;
		}
	}
	else
	{
		$res = SQL_query("select original, custom from selectbf_cleartext where type='$type' order by original ");
		$lookup = array();
		while($cols = SQL_fetchArray($res))
		{
			$lookup[$cols["original"]] = $cols["custom"]; 
		}
		$_SESSION["ClearText".$type] = $lookup;
		
		if(isset($lookup[$text]))
		{
			return $lookup[$text];
		}
		else
		{
			return $text;
		}		
	}
}

function getModFor($text,$type)
{
	@session_start();
	
	if(isset($_SESSION["Assignment".$type]))
	{
		$lookup = $_SESSION["Assignment".$type];
		
		if(isset($lookup[$text]))
		{
			return $lookup[$text];
		}
		else
		{
			return null;
		}
	}
	else
	{
		$res = SQL_query("select item, 'mod' from selectbf_modassignment where type='$type' order by item ");
		$lookup = array();
		while($cols = SQL_fetchArray($res))
		{
			$lookup[$cols["item"]] = $cols["mod"]; 
		}
		$_SESSION["Assignment".$type] = $lookup;
		
		if(isset($lookup[$text]))
		{
			return $lookup[$text];
		}
		else
		{
			return null;
		}		
	}
}

function createWhereClause($arraymember,$colname)
{
	$str = "( $colname = ";
	
	for($i = 0; $i<count($arraymember); $i++)
	{	
		$member = $arraymember[$i]["member"];
		if($i == 0)
		{
			$str = $str." '$member'";
		}
		else
		{
			$str = "$str OR $colname = '$member'";
		}
		
	}
	
	$str = "$str )";
	
	return $str;
}


function getMapTKs($map)
{
	GLOBAL $DEBUG;
	$starttime=timer();
	
	$usage = $_SESSION["LIST-MAP-TKS"];
	$res = SQL_query("select p.name,p.id, sum(k.tks) count from selectbf_playerstats k, selectbf_players p, selectbf_rounds r, selectbf_games g where k.player_id = p.id and k.round_id = r.id and r.game_id = g.id and g.map = '$map' group by p.name order by count DESC LIMIT 0,$usage");
	$max = getMaxForField($res,"count");
	
	$maptks = array();
	while($cols = SQL_fetchArray($res))
	{
		$cols['bar'] = statsbar($cols['count'],$max);
		$cols['playerdetaillink'] = "player.php?id=".$cols['id'];
		$cols["playerimg"] = randomPlayerImg();
		array_push($maptks, $cols);	
	}
	$totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getMapTKs</i></b> took $totaltime secs<br>");
	
	return $maptks;
}

function getMapDeaths($map)
{
	GLOBAL $DEBUG;
	$starttime=timer();
	
	$usage = $_SESSION["LIST-MAP-DEATHS"];
	$res = SQL_query("select p.name,p.id, sum(k.deaths) count from selectbf_playerstats k, selectbf_players p, selectbf_rounds r, selectbf_games g where k.player_id = p.id and k.round_id = r.id and r.game_id = g.id and g.map = '$map' group by p.name order by count DESC LIMIT 0,$usage");
	$max = getMaxForField($res,"count");
	
	$mapdeaths = array();
	while($cols = SQL_fetchArray($res))
	{
		$cols['playerdetaillink'] = "player.php?id=".$cols['id'];
		$cols['bar'] = statsbar($cols['count'],$max);
		$cols["playerimg"] = randomPlayerImg();
		array_push($mapdeaths, $cols);	
	}
	$totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getMapDeaths</i></b> took $totaltime secs<br>");
	
	return $mapdeaths;
}


function getMapAttacks($map)
{
	GLOBAL $DEBUG;
	$starttime=timer();
	
	$usage = $_SESSION["LIST-MAP-ATTACKS"];
	$res = SQL_query("select p.name,p.id, sum(k.attacks) count from selectbf_playerstats k, selectbf_players p, selectbf_rounds r, selectbf_games g where k.player_id = p.id and k.round_id = r.id and r.game_id = g.id and g.map = '$map' group by p.name order by count DESC LIMIT 0,$usage");
	$max = getMaxForField($res,"count");
	
	$mapattacks = array();
	while($cols = SQL_fetchArray($res))
	{
		$cols['playerdetaillink'] = "player.php?id=".$cols['id'];
		$cols['bar'] = statsbar($cols['count'],$max);
		$cols["playerimg"] = randomPlayerImg();
		array_push($mapattacks, $cols);	
	}
	$totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getMapAttacks</i></b> took $totaltime secs<br>");
	
	return $mapattacks;
}


function getMapKills($map)
{
	GLOBAL $DEBUG;
	$starttime=timer();
	
	$usage = $_SESSION["LIST-MAP-KILLERS"];
	$sql = "select p.name,p.id, sum(k.kills) count from selectbf_playerstats k, selectbf_players p, selectbf_rounds r, selectbf_games g where k.player_id = p.id and k.round_id = r.id and r.game_id = g.id and g.map = '$map' group by p.name order by count DESC LIMIT 0,$usage";
	$res = SQL_query($sql);
	$max = getMaxForField($res,"count");
	
	
	$mapkills = array();
	while($cols = SQL_fetchArray($res))
	{
		$cols['playerdetaillink'] = "player.php?id=".$cols['id'];
		$cols['bar'] = statsbar($cols['count'],$max);
		$cols["playerimg"] = randomPlayerImg();
		array_push($mapkills, $cols);	
	}
	
	$totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getMapKills</i></b> took $totaltime secs<br>");
	
	return $mapkills;
}

function getMapStats()
{
	GLOBAL $DEBUG,$TMPL_CFG_BAR_TEAMS,$TEMPLATE_DIR;
	$starttime=timer();
	
	
	$MAXWIDTH = $TMPL_CFG_BAR_TEAMS['maxwidth'];
	$HEIGHT = $TMPL_CFG_BAR_TEAMS['height'];
	$left = $TMPL_CFG_BAR_TEAMS['axis']['left'];
	$axis = $TMPL_CFG_BAR_TEAMS['axis']['middle'];
	$allies = $TMPL_CFG_BAR_TEAMS['allies']['middle'];
	$right = $TMPL_CFG_BAR_TEAMS['axis']['right'];
		
	$mapstats = array();
	
	$res = SQL_query("SELECT map, wins_team1, wins_team2,win_team1_tickets_team1 win_team1_end_tickets_team1,win_team1_tickets_team2 win_team1_end_tickets_team2,win_team2_tickets_team1 win_team2_end_tickets_team1,win_team2_tickets_team2 win_team2_end_tickets_team2,score_team1,score_team2,kills_team1,kills_team2,deaths_team1,deaths_team2,attacks_team1,attacks_team2,captures_team1,captures_team2 from selectbf_cache_mapstats order by map ASC");
	while($cols = SQL_fetchArray($res))
	{
		$cols["mapdetaillink"] = "map.php?map=".urlencode($cols["map"]);
    	$cols["screen"] = getMapImage($cols["map"]);
		$cols["map"] = clearUpText($cols["map"],"MAP");
				
		$sum = 	$cols["wins_team1"]+$cols["wins_team2"];
    		
    	if($sum == 0)
    	{
    		$pixelAxis = $MAXWIDTH/2;
    		$pixelAllies = $MAXWIDTH/2;
    	}
    	else
    	{	
    		$pixelAxis = ($cols["wins_team1"]/$sum)*$MAXWIDTH;
    		$pixelAllies = ($cols["wins_team2"]/$sum)*$MAXWIDTH;
    	}
    	
    	$img = "<img src=templates/$TEMPLATE_DIR/images/$left>";
    	$img = $img."<img src=templates/$TEMPLATE_DIR/images/$axis width=\"$pixelAxis\" height=\"$HEIGHT\">";
    	$img = $img."<img src=templates/$TEMPLATE_DIR/images/$allies width=\"$pixelAllies\" height=\"$HEIGHT\">";
    	$img = $img."<img src=templates/$TEMPLATE_DIR/images/$right>";
    	
    	$cols["bar"] = $img;
		
		array_push($mapstats,$cols);
	}
	
	$totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getMapStats</i></b> took $totaltime secs<br>");
	
	return $mapstats;	
}

function getVehicleUsage()
{
	GLOBAL $DEBUG,$TMPL_CFG_BAR,$TEMPLATE_DIR;
	$starttime=timer();	
	
	$usage = $_SESSION["LIST-VEHICLES-LIST"];
	$res = SQL_query("select vehicle, time, percentage_time, times_used, percentage_usage from selectbf_cache_vehicletime order by time DESC LIMIT 0,$usage");
	$max = getMaxForField($res,"time");
	
	$vehusage = array();
	while($cols = SQL_fetchArray($res))
	{
		$vehicle = $cols["vehicle"];
		$res2 = SQL_query("select kills, percentage from selectbf_cache_weaponkills where weapon='$vehicle'");
		if($cols2 = SQL_fetchArray($res2))
		{
			$cols["kills"] = $cols2["kills"];
			$cols["percentage_kills"] = $cols2["percentage"];
		}
		else
		{
			$cols["kills"] = 0;
			$cols["percentage_kills"] = 0;
		}
		$cols['vehicle'] = clearUpText($cols['vehicle'],"VEHICLE");
		$cols['bar'] = statsbar($cols['time'],$max);
		$cols['time'] = sec2time($cols['time']);

		$cols['percentage_time'] = sprintf("%01.2f",$cols['percentage_time']);
		$cols['percentage_usage'] = sprintf("%01.2f",$cols['percentage_usage']);
		$cols['percentage_kills'] = sprintf("%01.2f",$cols['percentage_kills']);
		
		array_push($vehusage,$cols);
	}
	
	$totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getVehicleUsage</i></b> took $totaltime secs<br>");
	
	return $vehusage;
}

function getDataForWeaponCategory($categoryid)
{
	GLOBAL $DEBUG;
	$starttime=timer();	
	
	$usage = $_SESSION["LIST-WEAPONS-LIST"];
	$res = SQL_query("select member from selectbf_categorymember where category = $categoryid");
	$members = array();
	$content = false;
	while($cols = SQL_fetchArray($res))
	{
		array_push($members,$cols);
		$content = true;
	}
   	
   	$data = array();
   	if($content)
   	{	
    	$whereclause = createWhereClause($members,"k.weapon");
    	$sql = "select p.name,p.id,sum(times_used) count from selectbf_kills_weapon k, selectbf_players p where k.player_id = p.id and $whereclause group by p.name,p.id order by count DESC LIMIT 0,$usage";
    	$res = SQL_query($sql);
       	$max = getMaxForField($res,"count");
       	
    
       	while($cols = SQL_fetchArray($res))
        {
        	$cols['bar'] = statsbar($cols['count'],$max);
    		$cols["playerimg"] = randomPlayerImg();
    		$id = $cols['id'];
    		$cols["playerdetaillink"] = "player.php?id=$id";    	
        	array_push($data,$cols);
        }
   	}
   	else
   	{
   		$data = null;
   	}
    $totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getDataForWeaponCategory</i></b> took $totaltime secs<br>");
    
    return $data;
}
//Added by Gary Chiu 
function getActiveMinClanRoundsValue() 
{ 
        $cols = $_SESSION["MIN-CLAN-ROUNDS"];
        return $cols; 
} 
 
function getActiveMinClanMembersValue() 
{ 
        $cols = $_SESSION["MIN-CLAN-MEMBERS"]; 
        return $cols; 
} 
// 

function getActiveMinRoundsValue()
{
	$cols = $_SESSION["MIN-ROUNDS"];
	return $cols;
}

function getActiveTemplate()
{
	$cols = $_SESSION["TEMPLATE"]; 
	return $cols;
}

function checkTemplateConsistency($template,$check)
{
	$dir=opendir("templates/$template/");
	
	$found = false;
	while (($file = readdir($dir)) && !$found)
	{
   		if($file == $check)
   		{
			$found = true;
  		}
	}
	return $found;
}

function Template_error($msg)
{
	?>
   <html>
   <head>
   <title>select(bf) Error</title>
   <link rel="stylesheet" href="templates/default/include/style.css" type="text/css">
   </head>
   <body>
   <center>
   <table class=navbar width=450>
   <tr class=axis>
    <th><img src="templates/default/images/icon_error.gif" align=absmiddle hspace=2> Template Error</th>
   </tr>
   <tr>
    <td class=admin>
     <b>There is a problem with the configured Template.</b><br>
     Please access the <a href="admin/index.php">Admin Mode</a> and change back to a working one.<br>
	 <b>OR</b> rework the template to meet the specifications.<p>
     <u><b>Problem:</b></u>
     <i><?echo $msg;?></i><br>
     <p>
    </td>
   </tr>
  </table>
  </center>
  </html>
	<?
}

function getActiveDebugLevel()
{
	$cols = $_SESSION["DEBUG-LEVEL"];
	return $cols;
}

function getActiveTitlePrefix()
{
	$cols = $_SESSION["TITLE-PREFIX"];
	return $cols;
}

function randomPlayerImg()
{
	GLOBAL $TMPL_CFG_PLAYER_IMG,$TEMPLATE_DIR;
	
	$i = (int) mt_rand(0,sizeof($TMPL_CFG_PLAYER_IMG)-1);
	
	return "templates/$TEMPLATE_DIR/images/$TMPL_CFG_PLAYER_IMG[$i]";
}

function getMaxForField($ResultSet,$Fieldname)
{
	$erg = 0;
	while($Ergebnisse = SQL_fetchArray($ResultSet))
	{
		if($erg < $Ergebnisse[$Fieldname]) $erg = $Ergebnisse[$Fieldname];
	}
	SQL_resetResult($ResultSet);
	return $erg;
}

function getSumForField($ResultSet,$Fieldname)
{
	$erg = 0;
	while($Ergebnisse = SQL_fetchArray($ResultSet))
	{
		$erg += $Ergebnisse[$Fieldname];
	}
	SQL_resetResult($ResultSet);
	return $erg;
}

function pixelValue($value,$max,$maxpixel)
{
	if($max==0)
	{
		return 0;
	}
	else
	{
		return ($value*$maxpixel)/$max;
	}
}

function statsbar($value,$max)
{
	GLOBAL $TMPL_CFG_BAR,$TEMPLATE_DIR;
	
	$width = pixelValue($value,$max,$TMPL_CFG_BAR['maxwidth']);
	$left = $TMPL_CFG_BAR['left'];
	$middle = $TMPL_CFG_BAR['middle'];
	$right = $TMPL_CFG_BAR['right'];
	$height = $TMPL_CFG_BAR['height'];
	return "<img src=templates/$TEMPLATE_DIR/images/$left><img src=templates/$TEMPLATE_DIR/images/$middle width=\"$width\" height=\"$height\"><img src=templates/$TEMPLATE_DIR/images/$right>";
}

function getModSymbol($str,$type)
{
	GLOBAL $TEMPLATE_DIR,$TMPL_CFG_MOD_IMG;
	
	$mod = getModFor($str,$type);

	if($mod == null)
	{
		$img = "templates/$TEMPLATE_DIR/images/blank.gif";
	}
	else
	{
		if(isset($TMPL_CFG_MOD_IMG[$mod]))
		{
			$img = "templates/$TEMPLATE_DIR/images/".$TMPL_CFG_MOD_IMG[$mod];
		}
		else
		{
			$img = "templates/$TEMPLATE_DIR/images/blank.gif";	
		}
	}	

	return $img;
}

function getModSymbolForName($str)
{
	GLOBAL $TMPL_CFG_MOD_IMG,$TEMPLATE_DIR;
	
	$img = "templates/$TEMPLATE_DIR/images/blank.gif";
	
	if(isset($TMPL_CFG_MOD_IMG[$str]))
	{
		$img = "templates/$TEMPLATE_DIR/images/".$TMPL_CFG_MOD_IMG[$str];
	}

	return $img;
}

function getRankingOrderByColumn()
{
	$cols = $_SESSION["RANK-ORDERBY"];
	return $cols;
}

function getRankingFormula()
{
	$Ergebnis = $_SESSION["RANK-FORMULA"];
	return $Ergebnis;
}

function getRankingRounding()
{
	$Ergebnis = $_SESSION["RANK-ROUND"];
	return $Ergebnis;
}

function getRankingRoundingNumber()
{
	$Ergebnis = $_SESSION["RANK-ROUND-NUMBER"];
	return $Ergebnis;
}

function sec2time($str)
{
	$min = (int) ($str/60);
	
	$sec = $str - ($min*60); 

	$h = (int) ($min/60);
	$min = $min -($h*60);
	
	$d = (int) ($h/24);
	$h = $h - ($d*24);
	
	if($min == 0 && $h == 0 && $d == 0)
	{
		return sprintf("%01.2fsec",$sec);	
	} else
	if($h == 0 && $d == 0)
	{
		return sprintf("%dmin %01.2fsec",$min,$sec);	
	} else
	if($d == 0)
	{	
		return sprintf("%dh %dmin %01.2fsec",$h,$min,$sec);
	} else
	{
		return sprintf("%dd %dh %dmin %01.2fsec",$d,$h,$min,$sec);
	}
		
}

function timer() 
{
	list($low, $high) = preg_split("/ /", microtime());
    	$t = $high + $low;
	return $t;
}

function getMaxStarNumber()
{
	$Erg = $_SESSION["STAR-NUMBER"];
	return $Erg;
}

function getTopScorerId()
{
	$minrounds = getActiveMinRoundsValue();
	$Erg = SQL_oneRowQuery("select ps.player_id id, sum(ps.score) score from selectbf_playerstats ps, selectbf_cache_ranking cr where ps.player_id = cr.player_id and cr.rounds_played >= $minrounds group by ps.player_id order by score DESC LIMIT 0,1");
	return $Erg["id"];	
}

function getTopPointerId()
{
	$Erg = SQL_oneRowQuery("select ps.player_id id, ((sum(kills)/sum(deaths))+(sum(attacks)*2/count(*))-(sum(tks)/count(*))+(sum(captures)*10/count(*)))*100 points from selectbf_playerstats ps group by ps.player_id order by points DESC LIMIT 0,1");
	return $Erg["id"];	
}

function getTopMedicId()
{
	$minrounds = getActiveMinRoundsValue();
	$Erg = SQL_oneRowQuery("select p.id,p.name, sum(amount) amount, sum(healtime) healtime from selectbf_heals h, selectbf_players p, selectbf_cache_ranking cr where h.player_id!=healed_player_id and h.player_id = p.id and cr.player_id = h.player_id and cr.rounds_played >= $minrounds group by h.player_id order by amount DESC,healtime DESC LIMIT 0,1");
	return $Erg["id"];	
}

function getTopEngineerId()
{
	$minrounds = getActiveMinRoundsValue();
	$Erg = SQL_oneRowQuery("select p.id,p.name, sum(amount) amount, sum(repairtime) repairtime from selectbf_repairs r, selectbf_players p, selectbf_cache_ranking cr where r.player_id = p.id and r.player_id = cr.player_id and cr.rounds_played >= $minrounds group by r.player_id order by amount DESC,repairtime DESC LIMIT 0,1");
	return $Erg["id"];	
}

function getTopAttackerId()
{
	$minrounds = getActiveMinRoundsValue();
	$Erg = SQL_oneRowQuery("select ps.player_id id, sum(ps.attacks) score from selectbf_playerstats ps, selectbf_cache_ranking cr where ps.player_id = cr.player_id and cr.rounds_played >= $minrounds group by ps.player_id order by score DESC LIMIT 0,1");
	return $Erg["id"];	
}

function getTopRounds($map)
{
	$numrounds = $_SESSION["LIST-MAP-ROUNDS"];
	$res= SQL_Query("select player_id, p.name, score, kills, deaths, attacks, tks, g.id, g.servername,CONCAT(DATE_FORMAT(r.starttime,'%Y-%m-%d '),TIME_FORMAT(r.starttime,'%H:%i:%S')) time from selectbf_playerstats ps, selectbf_players p, selectbf_rounds r, selectbf_games g where ps.player_id = p.id and ps.round_id = r.id and r.game_id = g.id and g.map = '$map' order by score DESC LIMIT 0,$numrounds");
	$rounds = array();
	while($cols = SQL_fetchArray($res))
	{
		$cols["playerimg"] = randomPlayerImg();	
		$cols["playerdetaillink"] = "player.php?id=".$cols['player_id'];		
		$cols["gamedetaillink"] = "game.php?id=".$cols['id'];		
		array_push($rounds,$cols);
	}
	return $rounds;
}

function getAwards($player_id,$first,$second,$third,$toprepair,$topheal,$topscore,$starnumber,$topattack)
{
	GLOBAL $TMPL_CFG_BRONZE_STAR_IMG,$TMPL_CFG_SILVER_STAR_IMG,$TMPL_CFG_GOLD_STAR_IMG,$TMPL_CFG_TOP_HEAL_IMG,$TMPL_CFG_TOP_POINT_IMG,$TMPL_CFG_TOP_REPAIR_IMG,$TMPL_CFG_TOP_SCORE_IMG,$TEMPLATE_DIR,$TMPL_CFG_TOP_ATTACK_IMG;
	
	$awards = array();
	
	$awards_withoutstars = "";
	$awards_withstars = "";
	
	if($first>$starnumber)
	{
		$awards_withstars = $awards_withstars."<img src=\"templates/$TEMPLATE_DIR/images/$TMPL_CFG_GOLD_STAR_IMG\" align=absmiddle alt=\"Rank 1: $first Times\">x$first";
	}
	else
	{
		for($i = 0; $i<$first; $i++)
		{
			$awards_withstars = $awards_withstars."<img src=\"templates/$TEMPLATE_DIR/images/$TMPL_CFG_GOLD_STAR_IMG\" align=absmiddle alt=\"Rank 1: $first Times\">";
		}
	}
	if($second>$starnumber)
	{
		$awards_withstars = $awards_withstars."<img src=\"templates/$TEMPLATE_DIR/images/$TMPL_CFG_SILVER_STAR_IMG\" align=absmiddle alt=\"Rank 2: $second Times\">x$second";
	}
	else
	{
		for($i = 0; $i<$second; $i++)
		{
			$awards_withstars = $awards_withstars."<img src=\"templates/$TEMPLATE_DIR/images/$TMPL_CFG_SILVER_STAR_IMG\" align=absmiddle alt=\"Rank 2: $second Times\">";
		}
	}
	if($third>$starnumber)
	{

		$awards_withstars = $awards_withstars."<img src=\"templates/$TEMPLATE_DIR/images/$TMPL_CFG_BRONZE_STAR_IMG\" align=absmiddle alt=\"Rank 3: $third Times\">x$third";
	}
	else
	{
		for($i = 0; $i<$third; $i++)
		{
			$awards_withstars = $awards_withstars."<img src=\"templates/$TEMPLATE_DIR/images/$TMPL_CFG_BRONZE_STAR_IMG\" align=absmiddle alt=\"Rank 3: $third Times\">";
		}
	}
	
	if($topheal==$player_id)
	{
		$awards_withstars = $awards_withstars."<img src=\"templates/$TEMPLATE_DIR/images/$TMPL_CFG_TOP_HEAL_IMG\" align=absmiddle alt=\"Top-Heals\" title=\"Top Heals\">";
		$awards_withoutstars = $awards_withoutstars."<img src=\"templates/$TEMPLATE_DIR/images/$TMPL_CFG_TOP_HEAL_IMG\" align=absmiddle alt=\"Top-Heals\" title=\"Top Heals\">";
	}
	if($toprepair==$player_id)
	{
		$awards_withstars = $awards_withstars."<img src=\"templates/$TEMPLATE_DIR/images/$TMPL_CFG_TOP_REPAIR_IMG\" align=absmiddle alt=\"Top-Repairs\" title=\"Top Repairs\">";
		$awards_withoutstars = $awards_withoutstars."<img src=\"templates/$TEMPLATE_DIR/images/$TMPL_CFG_TOP_REPAIR_IMG\" align=absmiddle alt=\"Top-Repairs\" title=\"Top Repairs\">";
	}
	if($topscore==$player_id)
	{
		$awards_withstars = $awards_withstars."<img src=\"templates/$TEMPLATE_DIR/images/$TMPL_CFG_TOP_SCORE_IMG\" align=absmiddle alt=\"Top-Scorer\" title=\"Top Scorer\">";
		$awards_withoutstars = $awards_withoutstars."<img src=\"templates/$TEMPLATE_DIR/images/$TMPL_CFG_TOP_SCORE_IMG\" align=absmiddle alt=\"Top-Scorer\" title=\"Top Scorer\">";
	}		
	if($topattack==$player_id)
	{
		$awards_withstars = $awards_withstars."<img src=\"templates/$TEMPLATE_DIR/images/$TMPL_CFG_TOP_ATTACK_IMG\" align=absmiddle alt=\"Top-Conquests\" title=\"Top Conquests\">";
		$awards_withoutstars = $awards_withoutstars."<img src=\"templates/$TEMPLATE_DIR/images/$TMPL_CFG_TOP_ATTACK_IMG\" align=absmiddle alt=\"Top-Conquests\" title=\"Top Conquests\">";
	}		

	$awards["awards_withstars"] =  $awards_withstars;
	$awards["awards_withoutstars"] =  $awards_withoutstars;

	return $awards;
}

function getRanking($orderby,$direction,$start)
{
	GLOBAL $DEBUG;
	$starttime=timer();
	
	$numranking = $_SESSION["LIST-RANKING-PLAYER"];
	$minrounds = getActiveMinRoundsValue();
	$formula = getRankingFormula();
	$res = SQL_query("select playername name,player_id id, score, tks, kills, deaths, captures, attacks, objectives, heals, selfheals, repairs, otherrepairs, rounds_played rounds, kdrate, first, second, third, playtime, score_per_minute, (score/rounds_played) avgscore, ($formula) points from selectbf_cache_ranking having rounds >= $minrounds order by $orderby $direction LIMIT $start,$numranking");
	$rounding = getRankingRounding();
	$roundingnumber = getRankingRoundingNumber();
	
	$toprepair = getTopEngineerId();
	$topheal = getTopMedicId();
	$toppoint = getTopPointerId();
	$topscore = getTopScorerId();
	$starnumber = getMaxStarNumber();
	$topattack = getTopAttackerId();

	$ranking = array();
	$i = $start+1;
	while($cols = SQL_fetchArray($res))
	{
		if($rounding =="whole")
		{
			$cols["points"] = round($cols["points"]);
		}
		elseif($rounding =="other")
		{
			$cols["points"] = number_format($cols["points"],$roundingnumber);
		}
		else
		{
		    $cols["points"] = $cols["points"];
		}
		$cols["rank"] = $i;
		$cols["playerimg"] = randomPlayerImg();
		$id = $cols['id'];
		$cols["playerdetaillink"] = "player.php?id=$id";
		$awards = getAwards($id,$cols["first"],$cols["second"],$cols["third"],$toprepair,$topheal,$topscore,$starnumber,$topattack);
		$cols["awards"] = $awards["awards_withstars"];
		$cols["awards_withoutstars"] = $awards["awards_withoutstars"];
		array_push($ranking,$cols);
		$i++;
	}

	$totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getRanking</i></b> took $totaltime secs<br>");
	
	return $ranking;
}

//Function added by Gary Chiu
function clear_special_char($clan_name,$update_clan){
	if(stristr($_ENV["TERM"],"linux")){
		if($update_clan){
			return addslashes($clan_name);
		}else{
			return $clan_name;
		}
	}else{
		$search = array ("/\'/si","/\"/si");
		$replace = array ("\\\'","\\\"");
		$clear_format = preg_replace($search, $replace, $clan_name);
		return $clear_format;
	}
}

//Function added by Gary Chiu
function getTeamRanking($orderby,$direction,$start,$clanname)
{
	GLOBAL $DEBUG;
	//
	$starttime=timer();
	$rounding = getRankingRounding();
	$roundingnumber = getRankingRoundingNumber();
	$formula = getRankingFormula();

	$minrounds = getActiveMinClanRoundsValue();
	$res = SQL_query("select playername name,player_id id, score, tks, kills, deaths, captures, attacks, objectives, heals, selfheals, repairs, otherrepairs, rounds_played rounds, kdrate, first, second, third, playtime, score_per_minute, $formula points, (score/rounds_played) avgscore from selectbf_cache_ranking where playername like '%".clear_special_char($clanname,true)."%' having rounds >= $minrounds order by $orderby $direction LIMIT $start,50");

	$topattack = getTopAttackerId();
	$toprepair = getTopEngineerId();
	$topheal = getTopMedicId();
	$toppoint = getTopPointerId();
	$topscore = getTopScorerId();
	$starnumber = getMaxStarNumber();

	$ranking = array();
	$i = $start+1;
	while($cols = SQL_fetchArray($res))
	{
		if($rounding =="whole")
		{
			$cols["points"] = round($cols["points"]);
		}
		elseif($rounding =="other")
		{
			$cols["points"] = number_format($cols["points"],$roundingnumber);
		}
		else
		{
		    $cols["points"] = $cols["points"];
		}
		$cols["avgscore"] = number_format($cols["avgscore"],'2');
		$cols["rank"] = $i;
		$cols["playerimg"] = randomPlayerImg();
		$id = $cols['id'];
		$cols["playerdetaillink"] = "player.php?id=$id&clanname=$clanname";
		$awards = getAwards($id,$cols["first"],$cols["second"],$cols["third"],$toprepair,$topheal,$topscore,$starnumber,$topattack);
		$cols["awards"] = $awards["awards_withstars"];
		$cols["awards_withoutstars"] = $awards["awards_withoutstars"];
		array_push($ranking,$cols);
		$i++;
	}

	$totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getRanking</i></b> took $totaltime secs<br>");

	return $ranking;
}

//Function added by Gary Chiu
function getClansRanking($orderby,$direction,$start)
{
	GLOBAL $DEBUG;
	//
	$starttime=timer();
	$formula = getRankingFormula();

	$minrounds = getActiveMinClanRoundsValue();
	$res = SQL_query("select clanname name, members, score, tks, kills, deaths, captures, attacks, objectives, heals, selfheals, repairs, otherrepairs, rounds_played rounds, kdrate, $formula points, (score/rounds_played) avgscore from selectbf_clan_ranking having rounds >= $minrounds order by $orderby $direction LIMIT $start,50");
	$rounding = getRankingRounding();
	$roundingnumber = getRankingRoundingNumber();

	$ranking = array();
	$i = $start+1;
	while($cols = SQL_fetchArray($res))
	{
		if($rounding =="whole")
		{
			$cols["points"] = round($cols["points"]);
		}
		elseif($rounding =="other")
		{
			$cols["points"] = number_format($cols["points"],$roundingnumber);
		}
		else
		{
		    $cols["points"] = $cols["points"];
		}
		$cols["rank"] = $i;
		$cols["avgscore"] = number_format($cols["avgscore"],'2');
		$cols["playerimg"] = randomPlayerImg();
		$clanname = $cols["name"];
		$cols["teamdetaillink"] = "teamdetail.php?clanname=$clanname";
		array_push($ranking,$cols);
		$i++;
	}

	$totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getRanking</i></b> took $totaltime secs<br>");

	return $ranking;
}

function getServerUsage()
{
	GLOBAL $DEBUG,$gametypeLookupArray;
	$starttime=timer();
		
	$res = SQL_query("select TIME_FORMAT(last_seen,'%H') time, count(*) count, sum(playtime) playtime from selectbf_playtimes group by time");
	
	$max = getMaxForField($res,"count");
	$sum = getSumForField($res,"count");

	
	$cols = SQL_fetchArray($res);
	
	$serverusage = array();	
	for($i = 0; $i<24; $i++)
	{
		if($i==$cols['time']) 
		{
			$cols["playtime"] = sec2time($cols["playtime"]);
			$cols['bar'] = statsbar($cols["count"],$max);
			$cols['percentage'] = sprintf("%01.2f",($cols["count"]/$sum)*100);
			array_push($serverusage,$cols);
			$cols = SQL_fetchArray($res);
		}
		else
		{
			$buf = array();
			$buf["time"] = $i;
			$buf["count"] = 0;
			$buf["percentage"] = 0.00;
			$buf["playtime"] = sec2time(0.0);
			$buf["bar"] = statsbar(0,$max);
			array_push($serverusage,$buf);
		}
	}
	
	$totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getServerUsage</i></b> took $totaltime secs<br>");
	
	return $serverusage;
}


function getLastGames($start)
{
	GLOBAL $DEBUG;
	$starttime=timer();
	$numgames = $_SESSION["LIST-RANKING-GAMES"];
	$res = SQL_query("select servername,modid 'mod',game_mode,map,g.id,CONCAT(DATE_FORMAT(g.starttime,'%Y-%m-%d '),TIME_FORMAT(g.starttime,'%H:%i:%S')) date, count(*) rounds from selectbf_games g, selectbf_rounds r where g.id = r.game_id group by g.id order by g.starttime desc LIMIT $start,$numgames");
	$games = array();
	while($cols = SQL_fetchArray($res))
	{
		$id = $cols['id'];
		$cols["gamedetaillink"] = "game.php?id=$id";
		$cols["map"] = clearUpText($cols['map'],"MAP");
		$cols['game_mode'] = clearUpText($cols['game_mode'],"GAME-MODE");
		$cols['modimg'] = getModSymbolForName($cols['mod']);
		array_push($games,$cols);		
	}
	
	$totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getLastGames</i></b> took $totaltime secs<br>");	
	return $games;
}

function getAllGamesPlayer($start,$id)
{
	GLOBAL $DEBUG;
	$starttime=timer();
	
	$res = SQL_query("select servername,modid 'mod',game_mode,map,g.id,CONCAT(DATE_FORMAT(g.starttime,'%Y-%m-%d '),TIME_FORMAT(g.starttime,'%H:%i:%S')) date, count(*) rounds from selectbf_games g, selectbf_rounds r, selectbf_playerstats p where g.id = r.game_id and p.player_id = $id and r.id = p.round_id group by g.id order by g.starttime desc LIMIT $start,15");
	$games = array();
	while($cols = SQL_fetchArray($res))
	{
		$id = $cols['id'];
		$cols["gamedetaillink"] = "game.php?id=$id";
		$cols["map"] = clearUpText($cols['map'],"MAP");
		$cols['game_mode'] = clearUpText($cols['game_mode'],"GAME-MODE");
		$cols['modimg'] = getModSymbolForName($cols['mod']);
		array_push($games,$cols);		
	}
	
	$totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getAllGamesPlayer</i></b> took $totaltime secs<br>");	
	return $games;
}

function getLastGamesPlayer($id)
{
	GLOBAL $DEBUG;
	$starttime=timer();
	
	$usage = $_SESSION["LIST-PLAYER-GAMES"];
	$res = SQL_query("select servername,modid 'mod',game_mode,map,g.id,CONCAT(DATE_FORMAT(g.starttime,'%Y-%m-%d '),TIME_FORMAT(g.starttime,'%H:%i:%S')) date, count(*) rounds, score, kills, deaths, tks, attacks from selectbf_games g, selectbf_rounds r, selectbf_playerstats p where g.id = r.game_id and r.id = p.round_id and p.player_id = $id group by g.id order by g.starttime desc LIMIT 0,$usage");
	$games = array();
	while($cols = SQL_fetchArray($res))
	{
		$id = $cols['id'];
		$cols["gamedetaillink"] = "game.php?id=$id";
		$cols["map"] = clearUpText($cols['map'],"MAP");
		$cols['game_mode'] = clearUpText($cols['game_mode'],"GAME-MODE");
		$cols['modimg'] = getModSymbolForName($cols['mod']);
		array_push($games,$cols);		
	}
	
	$totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getLastGamesPlayer</i></b> took $totaltime secs<br>");	
	return $games;
}

function getTigerAsses()
{
	GLOBAL $DEBUG;
	$starttime=timer();	
	
	$res = SQL_query("select p.name,p.id,count(*) count from selectbf_kills k, selectbf_players p where k.player_id = p.id and k.weapon='Tiger' group by p.name,p.id order by count DESC LIMIT 0,15");
   	$max = getMaxForField($res,"count");
   	
   	$tigerasses = array();
   	while($cols = SQL_fetchArray($res))
    {
    	$cols['bar'] = statsbar($cols['count'],$max);
		$cols["playerimg"] = randomPlayerImg();
		$id = $cols['id'];
		$cols["playerdetaillink"] = "player.php?id=$id";    	
    	array_push($tigerasses,$cols);
    }
    $totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getTigerAsses</i></b> took $totaltime secs<br>");
    
    return $tigerasses;
}

function getRepairs()
{
   	GLOBAL $DEBUG;
   	$starttime=timer();	
	
	$usage = $_SESSION["LIST-CHARACTER-REPAIRS"];
	$res = SQL_query("select p.id,p.name, sum(amount) amount, sum(repairtime) repairtime from selectbf_repairs r, selectbf_players p where r.player_id = p.id group by player_id order by amount DESC,repairtime DESC LIMIT 0,$usage");
	$max = getMaxForField($res,"amount");
   	
   	$repairs = array();
   	while($cols = SQL_fetchArray($res))
    {
    	$cols['bar'] = statsbar($cols['amount'],$max);
		$cols["playerimg"] = randomPlayerImg();
		$cols['time'] = sec2time($cols['repairtime']);
		$id = $cols['id'];
		$cols["playerdetaillink"] = "player.php?id=$id";    	
    	array_push($repairs,$cols);
    }
    $totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getRepairs</i></b> took $totaltime secs<br>");
    
    return $repairs;
}

function getHeals()
{
   	GLOBAL $DEBUG;
   	$starttime=timer();	
	
	$usage = $_SESSION["LIST-CHARACTER-HEALS"];
	$res = SQL_query("select p.id,p.name, sum(amount) amount, sum(healtime) healtime from selectbf_heals h, selectbf_players p where player_id!=healed_player_id and h.player_id = p.id group by player_id order by amount DESC,healtime DESC LIMIT 0,$usage");
	$max = getMaxForField($res,"amount");
   	
   	$heals = array();
   	while($cols = SQL_fetchArray($res))
    {
    	$cols['bar'] = statsbar($cols['amount'],$max);
		$cols["playerimg"] = randomPlayerImg();
		$cols['time'] = sec2time($cols['healtime']);
		$id = $cols['id'];
		$cols["playerdetaillink"] = "player.php?id=$id";    	
    	array_push($heals,$cols);
    }
    $totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getHeals</i></b> took $totaltime secs<br>");
    
    return $heals;
}

function getWeaponUsage($id)
{
   	GLOBAL $DEBUG;
   	$starttime=timer();	
	
	$usage = $_SESSION["LIST-PLAYER-WEAPONS"];
    $res = SQL_oneRowQuery("select sum(times_killed) kills from selectbf_kills_player WHERE player_id = $id");
    $totalkills = $res["kills"];
 
    $res = SQL_query("select weapon, times_used count from selectbf_kills_weapon WHERE player_id = $id ORDER BY times_used DESC LIMIT 0,$usage");
    $max = getMaxForField($res,"count"); 
   	
   	$types = array();
   	while($cols = SQL_fetchArray($res))
    {
    	$cols['weapon'] = clearUpText($cols['weapon'],"WEAPON");
    	$cols['bar'] = statsbar($cols['count'],$max);
		$cols['percentage'] = sprintf("%01.2f",(($cols['count']/$totalkills)*100));
    	array_push($types,$cols);
    }
    $totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getWeaponUsage</i></b> took $totaltime secs<br>");
    
    return $types;	
}


function getCharacterTypeUsage($id)
{
   	GLOBAL $DEBUG;
   	$starttime=timer();	
   	
	$listsize = $_SESSION["LIST-PLAYER-CHARACTERS"];
    $res = SQL_query("select kit,times_used count from selectbf_kits where player_id=$id group by kit,times_used order by count desc limit 0,$listsize");
    $max = getMaxForField($res,"count");
   	
   	$types = array();
   	while($cols = SQL_fetchArray($res))
    {
		$cols['modimg'] = getModSymbol($cols['kit'],"KIT");
    	$cols['kit'] = clearUpText($cols['kit'],"KIT");
    	$cols['bar'] = statsbar($cols['count'],$max);
    	array_push($types,$cols);
    }
    $totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getCharacterTypeUsage</i></b> took $totaltime secs<br>");
    
    return $types;	
}

function getCharacterTypes()
{
   	GLOBAL $DEBUG;
   	$starttime=timer();	
	
	$usage = $_SESSION["LIST-CHARACTER-TYPE"];
    $res = SQL_query("select kit, times_used count, percentage from selectbf_cache_chartypeusage order by times_used desc Limit 0,$usage");
    $max = getMaxForField($res,"count");
   	
   	$types = array();
   	while($cols = SQL_fetchArray($res))
    {
		$cols['modimg'] = getModSymbol($cols['kit'],"KIT");
    	$cols['kit'] = clearUpText($cols['kit'],"KIT");
    	$cols['bar'] = statsbar($cols['count'],$max);
    	$cols['percentage'] = sprintf("%01.2f",$cols['percentage']);
    	array_push($types,$cols);
    }
    $totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getCharacterTypes</i></b> took $totaltime secs<br>");
    
    return $types;	
}


function getTopVictims($id)
{
   	GLOBAL $DEBUG;
   	$starttime=timer();	
	
	$usage = $_SESSION["LIST-PLAYER-VICTIMS"];
    $Ergebnisse = SQL_oneRowQuery("select sum(times_killed) kills from selectbf_kills_player WHERE player_id = $id");
    $totalkills = $Ergebnisse["kills"];
          
          
    $res = SQL_query("select victim_id id,p.name,times_killed count from selectbf_kills_player k, selectbf_players p where player_id=$id and k.victim_id = p.id order by times_killed desc Limit 0,$usage");    
    $max = getMaxForField($res,"count");
   	
   	$victims = array();
   	while($cols = SQL_fetchArray($res))
    {
    	$cols['bar'] = statsbar($cols['count'],$max);
		$cols['percentage'] = sprintf("%01.2f",(($cols['count']/$totalkills)*100));
		$id = $cols['id'];
		$cols["playerdetaillink"] = "player.php?id=$id";   
		$cols["playerimg"] = randomPlayerImg();
    	array_push($victims,$cols);
    }
    $totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getTopVictims</i></b> took $totaltime secs<br>");
    
    return $victims;	
}

function getNickNames($id)
{
   	GLOBAL $DEBUG;
   	$starttime=timer();	
	
 	$usage = $_SESSION["LIST-PLAYER-NICKNAMES"];
    $Ergebnisse = SQL_oneRowQuery("select sum(times_used) count from selectbf_nicknames WHERE player_id = $id");
    $totaluses = $Ergebnisse["count"];
          
          
    $res = SQL_query("select nickname,times_used count from selectbf_nicknames WHERE player_id = $id order by times_used desc Limit 0,$usage");    
    $max = getMaxForField($res,"count");
   	
   	$nicknames = array();
   	while($cols = SQL_fetchArray($res))
    {
    	$cols['bar'] = statsbar($cols['count'],$max);
		$cols['percentage'] = sprintf("%01.2f",(($cols['count']/$totaluses)*100));
    	array_push($nicknames,$cols);
    }
    $totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getNickNames</i></b> took $totaltime secs<br>");
    
    return $nicknames;	
}

function getTopAssasins($id)
{
   	GLOBAL $DEBUG;
   	$starttime=timer();	
	
	$usage = $_SESSION["LIST-PLAYER-ASSASINS"];
    $Ergebnisse = SQL_oneRowQuery("select sum(times_killed) kills from selectbf_kills_player where victim_id=$id");
    $totalkills = $Ergebnisse["kills"];
          
          
    $res = SQL_query("select victim_id id,p.name,times_killed count,player_id from selectbf_kills_player k, selectbf_players p where victim_id=$id and k.player_id = p.id order by times_killed desc Limit 0,$usage");
    $max = getMaxForField($res,"count");
   	
   	$assasins = array();
   	while($cols = SQL_fetchArray($res))
    {
    	$cols['bar'] = statsbar($cols['count'],$max);
		$cols['percentage'] = sprintf("%01.2f",(($cols['count']/$totalkills)*100));
		$id = $cols['id'];
		$player_id = $cols['player_id'];
		$cols["playerdetaillink"] = "player.php?id=$player_id";   
		$cols["playerimg"] = randomPlayerImg();
    	array_push($assasins,$cols);
    }
    $totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getTopAssasins</i></b> took $totaltime secs<br>");
    
    return $assasins;	
}

function getFavVehicles($id)
{
   	GLOBAL $DEBUG;
   	$starttime=timer();	
	
	$usage = $_SESSION["LIST-PLAYER-VEHICLES"];
    $Ergebnisse = SQL_oneRowQuery("select sum(drivetime) drivetime from selectbf_drives where player_id=$id");
    $totaltime = $Ergebnisse["drivetime"];
          
    $res = SQL_query("select vehicle,sum(drivetime) drivetime from selectbf_drives where player_id = $id group by vehicle order by drivetime DESC Limit 0,$usage");
    $max = getMaxForField($res,"drivetime");
   	
   	$vehicles = array();
   	while($cols = SQL_fetchArray($res))
    {
    	$cols['vehicle'] = clearUpText($cols['vehicle'],"VEHICLE");
		$cols['time'] =  sec2time($cols["drivetime"]);
        $cols['percentage'] = sprintf("%01.2f",(($cols["drivetime"]/$totaltime)*100));
        $cols['bar'] = statsbar($cols["drivetime"],$max,100);
    	array_push($vehicles,$cols);
    }
    $totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getFavVehicles</i></b> took $totaltime secs<br>");
    
    return $vehicles;	
}

function getMapPerformance($id)
{
   	GLOBAL $DEBUG;
   	$starttime=timer();	
	
	$usage = $_SESSION["LIST-PLAYER-MAPS"];
    $Ergebnisse = SQL_oneRowQuery("select sum(score) score from selectbf_playerstats where player_id = $id");
    $totalscore = $Ergebnisse["score"];
 
    $res = SQL_query("select sum(ps.score) score, g.map from selectbf_playerstats ps, selectbf_rounds r, selectbf_games g where player_id=$id and ps.round_id = r.id and r.game_id = g.id group by g.map having score != 0 order by score desc Limit 0,$usage");
    $max = getMaxForField($res,"score");
   	
   	$maps = array();
   	while($cols = SQL_fetchArray($res))
    {
    	$cols['mapdetaillink'] = "map.php?map=".$cols['map'];
    	$cols['map'] = clearUpText($cols['map'],"MAP");		
        $cols['bar'] = statsbar($cols['score'],$max,100);
        $cols['percentage'] = sprintf("%01.2f",(($cols['score']/$totalscore)*100));
    	array_push($maps,$cols);
    }
    $totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getMapPerformance</i></b> took $totaltime secs<br>");
    
    return $maps;	
}

function getHealTimes($id)
{
	GLOBAL $DEBUG;
   	$starttime=timer();	
	
	$healtimes = array();
	
	$res = SQL_query("select sum(amount) amount, sum(healtime) healtime from selectbf_heals where player_id!=healed_player_id and player_id=$id");
    if($Ergebnisse = SQL_fetchArray($res))
    {
    	$amount = $Ergebnisse["amount"];
    	$time = $Ergebnisse["healtime"];
    	
    	$healtimes["otherheals"] = array("amount"=>$amount,"time"=>sec2time($time));
    }
    else
    {
    	$amount = 0;
    	$time = 0;
    	
    	$healtimes["otherheals"] = array("amount"=>$amount,"time"=>sec2time($time));         	
    }
    
    $res = SQL_query("select sum(amount) amount, sum(healtime) healtime from selectbf_heals where player_id=healed_player_id and player_id=$id");
    if($Ergebnisse = SQL_fetchArray($res))
    {
    	$amount = $Ergebnisse["amount"];
    	$time = $Ergebnisse["healtime"];
    	
    	$healtimes["selfheals"] = array("amount"=>$amount,"time"=>sec2time($time));
    }
    else
    {
    	$amount = 0;
    	$time = 0;         	
    	
    	$healtimes["selfheals"] = array("amount"=>$amount,"time"=>sec2time($time));
    }
    
    $totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getHealTimes</i></b> took $totaltime secs<br>");
	
	return $healtimes;     
}

function getRepairTimes($id)
{
	GLOBAL $DEBUG;
   	$starttime=timer();	
	
	$repairtimes = array();
	
    $res = SQL_query("select sum(amount) amount, sum(repairtime) repairtime from selectbf_repairs where player_id=$id");
    if($Ergebnisse = SQL_fetchArray($res))
    {
    	$amount = $Ergebnisse["amount"];
    	$time = $Ergebnisse["repairtime"];
    	
    	$repairtimes["repairs"] = array("amount"=>$amount,"time"=>sec2time($time));
    }
    else
    {
    	$amount = 0;
    	$time = 0;      
    	
    	$repairtimes["repairs"] = array("amount"=>$amount,"time"=>sec2time($time));  	
    }
     
    $totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getRepairTimes</i></b> took $totaltime secs<br>");

 	return $repairtimes;
     	     
}

function getGameInfo($id)
{	
	GLOBAL $DEBUG,$gametypeLookupArray;
   	$starttime=timer();	
	
	$cols = SQL_oneRowQuery("select servername,modid 'mod',mapid,map,game_mode,gametime,maxplayers,scorelimit,spawntime,soldierff,vehicleff,tkpunish,deathcamtype,CONCAT(DATE_FORMAT(starttime,'%Y-%m-%d '),TIME_FORMAT(starttime,'%H:%i:%S')) date from selectbf_games where id=$id");
	$cols["map"] = clearUpText($cols['map'],"MAP");
	$cols['game_mode'] = clearUpText($cols['game_mode'],"GAME-MODE");
	$cols['modimg'] = getModSymbolForName($cols['mod']);
	
    $totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getGameInfo</i></b> took $totaltime secs<br>"); 	
	
	return $cols;
}

function getChatLogForRound($id)
{
	GLOBAL $DEBUG;
   	$starttime=timer();
   		
	$res = SQL_query("select c.player_id,p.name,c.text, CONCAT(DATE_FORMAT(c.inserttime,'%Y-%m-%d '),TIME_FORMAT(c.inserttime,'%H:%i:%S')) inserttime from selectbf_chatlog c, selectbf_players p, selectbf_rounds r where c.player_id = p.id and c.round_id = r.id and r.game_id = $id order by inserttime ASC");
	$content = false;
	
	$chats = array();
	while($cols = SQL_fetchArray($res))
	{
		$content = true;
		$cols["playerdetaillink"] = "player.php?id=".$cols['player_id']; 
		$cols["playerimg"] = randomPlayerImg();
		array_push($chats,$cols);
	}	
	
	$totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getChatLogForRound</i></b> took $totaltime secs<br>");
	
	if($content)
	{
		return $chats;
	}
	else
	{
		return null;
	}
}

function getChatLogCount($id)
{
	GLOBAL $DEBUG;
   	$starttime=timer();

	$res = SQL_query("select c.player_id,p.name,c.text, CONCAT(DATE_FORMAT(c.inserttime,'%Y-%m-%d '),TIME_FORMAT(c.inserttime,'%H:%i:%S')) inserttime from selectbf_chatlog c, selectbf_players p, selectbf_rounds r where c.player_id = p.id and c.round_id = r.id and r.game_id = $id order by inserttime ASC");
	$content = false;
	
	while($cols = SQL_fetchArray($res))
	{
		$content = true;
		$chats = true;
	}	

	if($content)
	{
		return $chats;
	}
	else
	{
		return null;
	}
	$totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getChatLogCount</i></b> took $totaltime secs<br>");

}

function getRoundsForGame($id)
{
	GLOBAL $DEBUG;
   	$starttime=timer();
   		
	$res = SQL_query("select id,start_tickets_team1,start_tickets_team2,CONCAT(DATE_FORMAT(starttime,'%Y-%m-%d '),TIME_FORMAT(starttime,'%H:%i:%S')) starttime,end_tickets_team1,end_tickets_team2,CONCAT(DATE_FORMAT(endtime,'%Y-%m-%d '),TIME_FORMAT(endtime,'%H:%i:%S')) endtime,endtype,winning_team from selectbf_rounds where game_id=$id order by selectbf_rounds.starttime ASC");
	
	$rounds = array();
	while($cols = SQL_fetchArray($res))
	{
		if($cols['endtype']=="REGULAR")
		{
			$cols['topthree'] = getTopThreeForRound($cols['id']);
			$cols['axis'] = getTeamStatsForRoundAndTeam($cols['id'],"1");
			$cols['allied'] = getTeamStatsForRoundAndTeam($cols['id'],"2");
			$cols['isRegular'] = true;
		}
		else
		if($cols['endtype']=="RESTART")
		{
			$cols['isRestart'] = true;
		}
		else
		if($cols['endtype']=="FORCED")
		{
			$cols['isForced'] = true;
		}
		if($cols["winning_team"]==1)
		{
			$cols["axis_won"] = true;
		}else
		if($cols["winning_team"]==2)
		{
			$cols["allies_won"] = true;
		}else
		{
			$cols["nobody_won"] = true;
		}
		
		
		array_push($rounds,$cols);
	}	
	
	$totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getRoundsForGame</i></b> took $totaltime secs<br>");
	
	return $rounds;
}

function getTopThreeForRound($id)
{
	GLOBAL $DEBUG,$TMPL_CFG_GOLD_STAR_IMG,$TMPL_CFG_SILVER_STAR_IMG,$TMPL_CFG_BRONZE_STAR_IMG,$TEMPLATE_DIR,$TMPL_CFG_AXIS_IMG,$TMPL_CFG_ALLIED_IMG;
   	$starttime=timer();
   	
   	$res = SQL_query("select p.name, p.id, team, score, kills, deaths, tks, captures, attacks, defences, objectives, objectivetks, heals,selfheals,repairs,otherrepairs,first, second, third from selectbf_playerstats ps, selectbf_players p where ps.player_id = p.id and ps.round_id=$id order by score DESC LIMIT 0,3");
   	
   	$topthree = array();
   	
   	$i = 0;
   	while($cols = SQL_fetchArray($res))
   	{
   		$id = $cols['id'];
   		$cols["playerdetaillink"] = "player.php?id=$id";
   		$cols["playerimg"] = randomPlayerImg();
   		
   		if($cols['team'] == 1) $cols['TeamFlag'] = "templates/".$TEMPLATE_DIR."/images/".$TMPL_CFG_AXIS_IMG;
   		else $cols['TeamFlag'] = "templates/".$TEMPLATE_DIR."/images/".$TMPL_CFG_ALLIED_IMG;
   		
   		if($i == 0) $cols['starimg'] = "templates/".$TEMPLATE_DIR."/images/".$TMPL_CFG_GOLD_STAR_IMG;
   		else if($i == 1) $cols['starimg'] = "templates/".$TEMPLATE_DIR."/images/".$TMPL_CFG_SILVER_STAR_IMG;
   		else if($i == 2) $cols['starimg'] = "templates/".$TEMPLATE_DIR."/images/".$TMPL_CFG_BRONZE_STAR_IMG;
   		array_push($topthree,$cols);
   		$i++;
   	}
 	
 	if($i==0) $topthree = null;
 	
   	$totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getTopThreeForRound</i></b> took $totaltime secs<br>");
	
	return $topthree; 
}

function getTeamStatsForRoundAndTeam($id,$team)
{
	GLOBAL $DEBUG,$TMPL_CFG_GOLD_STAR_IMG,$TMPL_CFG_SILVER_STAR_IMG,$TMPL_CFG_BRONZE_STAR_IMG,$TEMPLATE_DIR,$TMPL_CFG_AXIS_IMG,$TMPL_CFG_ALLIED_IMG;
   	$starttime=timer();
   	
   	$res = SQL_query("select p.name, p.id, team, score, kills, deaths, tks, captures, attacks, defences, objectives, objectivetks, heals,selfheals,repairs,otherrepairs,first, second, third from selectbf_playerstats ps, selectbf_players p where ps.player_id = p.id and ps.round_id=$id and team=$team order by score DESC");
   	
   	$topthree = array();
   	
   	$i = 0;
   	while($cols = SQL_fetchArray($res))
   	{
   		$id = $cols['id'];
   		$cols["playerdetaillink"] = "player.php?id=$id";
   		$cols["playerimg"] = randomPlayerImg();
   		
   		if($cols['team'] == 1) $cols['TeamFlag'] = "templates/".$TEMPLATE_DIR."/images/".$TMPL_CFG_AXIS_IMG;
   		else $cols['TeamFlag'] = "templates/".$TEMPLATE_DIR."/images/".$TMPL_CFG_ALLIED_IMG;
   		
   		if($cols['first'] == "1") $cols['starimg'] = "templates/".$TEMPLATE_DIR."/images/".$TMPL_CFG_GOLD_STAR_IMG;
   		else if($cols['second'] == "1") $cols['starimg'] = "templates/".$TEMPLATE_DIR."/images/".$TMPL_CFG_SILVER_STAR_IMG;
   		else if($cols['third'] == "1") $cols['starimg'] = $cols['starimg'] = "templates/".$TEMPLATE_DIR."/images/".$TMPL_CFG_BRONZE_STAR_IMG;
   		array_push($topthree,$cols);
   		
   		$i++;
   	}
   	
   	if($i==0) $topthree= null;
 
   	$totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getTeamStatsForRoundAndTeam</i></b> took $totaltime secs<br>");
	
	return $topthree; 
}

function getWingsOfFury()
{
   	GLOBAL $DEBUG;
   	$starttime=timer();	
	
   	$res = SQL_query("select p.name,p.id,count(*) count from selectbf_kills k, selectbf_players p where k.player_id = p.id and k.weapon in ('BF109','Chi-ha','AichiVal','SBD','Corsair','Zero','Spitfire','Ju88A','B17','Stuka','Yak9') group by p.name,p.id order by count DESC LIMIT 0,15");
   	$max = getMaxForField($res,"count");
   	
   	$wings = array();
   	while($cols = SQL_fetchArray($res))
    {
    	$cols['bar'] = statsbar($cols['count'],$max);
		$cols["playerimg"] = randomPlayerImg();
		$id = $cols['id'];
		$cols["playerdetaillink"] = "player.php?id=$id";    	
    	array_push($wings,$cols);
    }
    $totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getWingsOfFury</i></b> took $totaltime secs<br>");
    
    return $wings;
}

function getLinkForIndexColumn($column,$orderby,$direction)
{
	GLOBAL $DEBUG;
	$starttime=timer();
	
	$str = "index.php?orderby=$column";
	
	if($column==$orderby)
	{
		if($direction == "asc")
		{
			$str="$str&direction=desc";
		} 
		else
		{
			$str="$str&direction=asc";
		}
	}
	else
	{
		$str="$str&direction=desc";
	}
	
	$totaltime = timer()- $starttime;
	if($DEBUG>1) echo("<b><i>getLinkForIndexColumn($column)</i></b> took $totaltime secs<br>");	

	return $str;
}

//Added By Gary Chiu
function getLinkForNeededColumn($column,$neededpage,$neededvalue,$orderby,$direction)
{
	GLOBAL $DEBUG;
	$starttime=timer();
	
	if($neededvalue == ""){
		$str = "$neededpage?orderby=$column";
	}else{
		$str = "$neededpage?orderby=$column&clanname=$neededvalue";
	}

	if($column==$orderby)
	{
		if($direction == "asc")
		{
			$str="$str&direction=desc";
		} 
		else
		{
			$str="$str&direction=asc";
		}
	}
	else
	{
		$str="$str&direction=desc";
	}
	
	$totaltime = timer()- $starttime;
	if($DEBUG>1) echo("<b><i>getLinkForIndexColumn($column)</i></b> took $totaltime secs<br>");	

	return $str;
}

function getLastGamesNavBar($totalgamecount,$start)
{
	GLOBAL $DEBUG;
	$starttime=timer();
	$numgames = $_SESSION["LIST-RANKING-GAMES"];	
	$step = $start/$numgames;
	$totalsteps = $totalgamecount/$numgames;
	
	$navbarinfo = array();

    for($i = 0; $i<=$totalsteps; $i++)
	{
		$text = "";
		$isLinked = false;
		$link = "";
			
		if($i==$step)
	 	{
			$text = "$i";
			$isLinked = false;
			$link = "";
	   	}
	   	else
	   	{
	    		if($totalsteps>25)
	    		{
	     			if($i==$step-10) 
	     			{
	     				$text = "...";
	     				$isLinked = 0;
	     				$link = "";
	     			}
	     			if($i>=$step-10 && $i<=$step+10)
	     			{
	     				$text = "$i";
	     				$buf = $i*$numgames;
	     				$isLinked = 1;
	     				$link = "index.php?startgames=$buf";
	     			}	   
	     			if($i==$step+10)
	     			{
	     				$text = "...";
	     				$isLinked = 0;
	     				$link = "";
	     			}
	    		}
	    		else
	    		{
	     				$text = "$i";
	     				$buf = $i*$numgames;
	     				$isLinked = 1;
	     				$link = "index.php?startgames=$buf";
	    		}
	   	}
	   	if($text!="")
	   	{
	   		array_push($navbarinfo,array("text"=>$text,"isLinked"=>$isLinked,"link"=>$link));
	   	}
	  }
	
	  
	$totaltime = timer()- $starttime;
	if($DEBUG>1) echo("<b><i>getLastGamesNavBar</i></b> took $totaltime secs<br>");		  
	
	return $navbarinfo;
}

function getRankingNavBar($totalplayercount,$start,$orderby,$direction)
{
	GLOBAL $DEBUG;
	$starttime=timer();
	$numranking = $_SESSION["LIST-RANKING-PLAYER"];	
	$step = $start/$numranking;
	$totalsteps = $totalplayercount/$numranking;
	
	$navbarinfo = array();

    for($i = 0; $i<=$totalsteps; $i++)
	{
		$text = "";
		$isLinked = false;
		$link = "";
			
		if($i==$step)
	 	{
			$text = "$i";
			$isLinked = false;
			$link = "";
	   	}
	   	else
	   	{
	    		if($totalsteps>25)
	    		{
	     			if($i==$step-$numranking) 
	     			{
	     				$text = "...";
	     				$isLinked = 0;
	     				$link = "";
	     			}
	     			if($i>=$step-10 && $i<=$step+10)
	     			{
	     				$text = "$i";
	     				$buf = $i*$numranking;
	     				$isLinked = 1;
	     				$link = "index.php?orderby=$orderby&start=$buf&direction=$direction";
	     			}	   
	     			if($i==$step+10)
	     			{
	     				$text = "...";
	     				$isLinked = 0;
	     				$link = "";
	     			}
	    		}
	    		else
	    		{
	     				$text = "$i";
	     				$buf = $i*$numranking;
	     				$isLinked = 1;
	     				$link = "index.php?orderby=$orderby&start=$buf&direction=$direction";
	    		}
	   	}
	   	if($text!="")
	   	{
	   		array_push($navbarinfo,array("text"=>$text,"isLinked"=>$isLinked,"link"=>$link));
	   	}
	  }
	  
	$totaltime = timer()- $starttime;
	if($DEBUG>1) echo("<b><i>getRankingNavBar</i></b> took $totaltime secs<br>");		  
	
	return $navbarinfo;
}

function getPlayerInfo($id)
{
	GLOBAL $DEBUG;
	$starttime=timer();
	
	
	$infos = array();
	
	$aResult = SQL_oneRowQuery("select name,keyhash,CONCAT(DATE_FORMAT(inserttime,'%Y-%m-%d '),TIME_FORMAT(inserttime,'%H:%i:%S')) date, CONCAT(DATE_FORMAT(last_seen,'%Y-%m-%d '),TIME_FORMAT(last_seen,'%H:%i:%S')) last_seen from selectbf_players p, selectbf_playtimes pt where pt.id=p.id and p.id=$id");
	$infos["name"]=$aResult["name"];
	$infos["keyhash"]=$aResult["keyhash"];	
	$infos["date"]=$aResult["date"];
	$infos["last_seen"]=$aResult["last_seen"];	
	
	$Ergebnisse = SQL_oneRowQuery("select count(*)count from selectbf_playerstats where player_id=$id");
	$infos["rounds"]=$Ergebnisse["count"];
	
	$Ergebnisse = SQL_oneRowQuery("select p.name,ps.player_id id, sum(score) score ,sum(kills) kills ,sum(deaths) deaths ,sum(tks) tks ,sum(captures) captures ,sum(attacks) attacks ,sum(defences) defences ,sum(objectives) objetives,sum(objectivetks) objetivetks,sum(heals) heals,sum(selfheals) selfheals ,sum(repairs) repairs ,sum(otherrepairs) otherrepairs,count(*) rounds, sum(first) first, sum(second) second, sum(third) third from selectbf_playerstats ps, selectbf_players p where ps.player_id = p.id and p.id = $id group by p.name,ps.player_id ");
	$infos["score"]=$Ergebnisse["score"];
	$infos["kills"]=$Ergebnisse["kills"];
	$infos["deaths"]=$Ergebnisse["deaths"];
	$infos["tks"]=$Ergebnisse["tks"];
	$infos["first"]=$Ergebnisse["first"];
	$infos["second"]=$Ergebnisse["second"];
	$infos["third"]=$Ergebnisse["third"];
	

    $totaltime = timer()- $starttime;
	if($DEBUG>0) echo("<b><i>getPlayerInfo</i></b> took $totaltime secs<br>");	
	
	return $infos;
}
?>
