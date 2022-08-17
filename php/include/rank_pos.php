<?
$Erg = SQL_oneRowQuery("select p.id,p.name, sum(amount) amount, sum(repairtime) repairtime from selectbf_repairs r, selectbf_players p where player_id!=repair_player_id and r.player_id = p.id group by player_id order by amount DESC,repairtime DESC LIMIT 0,1");
$toprepair = $Erg["id"];

$Erg = SQL_oneRowQuery("select p.id,p.name, sum(amount) amount, sum(healtime) healtime from selectbf_heals h, selectbf_players p where player_id!=healed_player_id and h.player_id = p.id group by player_id order by amount DESC,healtime DESC LIMIT 0,1");
$topheal = $Erg["id"];


$Erg = SQL_oneRowQuery("select ps.player_id id, ((sum(kills)/sum(deaths))+(sum(attacks)*2/count(*))-(sum(tks)/count(*))+(sum(captures)*10/count(*)))*100 points from selectbf_playerstats ps group by ps.player_id order by points DESC LIMIT 0,1");
$toppoint = $Erg["id"];

$Erg = SQL_oneRowQuery("select ps.player_id id, sum(score) score from selectbf_playerstats ps group by ps.player_id order by score DESC LIMIT 0,1");
$topscore = $Erg["id"];

$Erg = SQL_oneRowQuery("select value from selectbf_admin where name='STARS'");
$starnumber = $Erg["value"];
}

function getAwards($player_id,$first,$second,$third)
{
	GLOBAL $toprepair,$topheal,$toppoint,$topscore,$starnumber;
	$str = "";

	
	if($first>$starnumber)
	{
		$str = "$str<img src=images/symbols/star_gold.gif alt=\"Rank 1: $first times\">x$first&nbsp;";
	}
	else
	{
		for($i = 0; $i<$first; $i++)
		{
			$str = "$str<img src=images/symbols/star_gold.gif alt=\"Rank 1: $first times\">";
		}
	}
	if($second>$starnumber)
	{
		$str = "$str<img src=images/symbols/star_silver.gif alt=\"Rank 2: $second times\">x$second&nbsp;";
	}
	else
	{
		for($i = 0; $i<$second; $i++)
		{
			$str = "$str<img src=images/symbols/star_silver.gif alt=\"Rank 2: $second times\">";
		}
	}
	if($third>$starnumber)
	{
		$str = "$str<img src=images/symbols/star_bronze.gif alt=\"Rank 3: $third times\">x$third&nbsp;";
	}
	else
	{
		for($i = 0; $i<$third; $i++)
		{
			$str = "$str<img src=images/symbols/star_bronze.gif alt=\"Rank 3: $third times\">";
		}
	}	
	if($topheal==$player_id)
	{
		$str = "$str<img src=images/symbols/top-heal.gif alt=\"Top-Heals\" title=\"Top Heals\">";
	}
	if($toprepair==$player_id)
	{
		$str = "$str <img src=images/symbols/top-repair.gif alt=\"Top-Repairs\" title=\"Top Repairs\">";
	}
	if($toppoint==$player_id)
	{
		$str = "$str <img src=images/symbols/top-point.gif alt=\"Top-Points\" title=\"Top Points\">";
	}
	if($topscore==$player_id)
	{
		$str = "$str <img src=images/symbols/top-score.gif alt=\"Top-Score\" title=\"Top Points\">";
	}		
	
	echo $str;	
}

?>
