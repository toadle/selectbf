<?
//set error-level
error_reporting(E_ERROR);

function checkAdminPsw($psw)
{
	$Ergebnis = SQL_oneRowQuery("SELECT value from selectbf_admin where name='ADMIN_PSW'");	
	if($Ergebnis["value"] == md5($psw))
	{
		return true;
	} else
	{
		return false;
	}
}

//Added by Gary Chiu
function createData($dbTable, $check, $dataArray){
	if(!is_countable($dataArray)){
		$dataArray = array();
	}
	for($i=0; $i < count($dataArray); $i++){
		$dataValue = $dataArray[$i][$check];
		$res = SQL_query("SELECT * from $dbTable where $check = '$dataValue'");
		if(mysqli_num_rows($res) > 0){}else{
			foreach($dataArray[$i] as $key=>$value){
				$dataValue = $dataArray[$i][$check];
				$res = SQL_query("SELECT * from $dbTable where $check = '$dataValue'");
				if(mysqli_num_rows($res) > 0){
					if($value == 'now()'){
						SQL_query("UPDATE $dbTable SET $key=$value where $check = '$dataValue'");
					}else{
						SQL_query("UPDATE $dbTable SET $key='$value' where $check = '$dataValue'");
					}
				}else{
					SQL_query("INSERT INTO $dbTable ($check) VALUES('$dataValue')");
				}
			}
		}
	}
}

function convertPath($fullpath){
	if(strstr($fullpath,"/")){//Is Not Window Path Format
		$filename = substr($fullpath,strrpos($fullpath,"/")+1);
		$path = str_replace($filename,"",$fullpath);
	}else{//Is Window Path Format
		$filename = substr($fullpath,strrpos($fullpath,"\\")+1);
		$path = str_replace($filename,"",$fullpath);
		$path = str_replace("\\","/",$path);
	}
	return $path;
}

function checkFilePathConsistency($filepath,$check)
{
	$dir=opendir("$filepath");
	
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

function deleteGame($id)
{
	//fetch all rounds that happened during that game
	$res = SQL_query("SELECT id FROM selectbf_rounds WHERE game_id=$id");
	
	while($Ergebnis = SQL_fetchArray($res))
	{
		deleteRound($Ergebnis["id"]);
	}
}

function deleteRound($id)
{
	//first check if this is the last round of the game to not leave any lonely games in the DB
	$Ergebnis = SQL_oneRowQuery("SELECT game_id from selectbf_rounds where id=$id");	
	$gameid = $Ergebnis["game_id"];
	
	$Ergebnis = SQL_oneRowQuery("SELECT count(*) count from selectbf_rounds where game_id=$gameid");
	$count = $Ergebnis["count"];
	
	//terminate lonely games from DB
	if($count=="1")
	{
		SQL_query("DELETE FROM selectbf_games WHERE id=$gameid");
	}

	//delete depending events
	SQL_query("DELETE FROM selectbf_chatlog WHERE round_id=$id");
	SQL_query("DELETE FROM selectbf_playerstats WHERE round_id=$id");
	
	//then delete the round
	SQL_query("DELETE FROM selectbf_rounds WHERE id=$id");
}

function changePassword($str)
{
	SQL_query("UPDATE selectbf_admin SET value='".md5($str)."' where name='ADMIN_PSW'");
}

function addClearedText($uncleared,$cleared,$type)
{
	$uncleared = addslashes($uncleared);
	$cleared = addslashes($cleared);
	SQL_query("INSERT INTO selectbf_cleartext (original,custom,type,inserttime) VALUES ('$uncleared','$cleared','$type',now())");
}

function deleteClearText($id)
{
	SQL_query("DELETE FROM selectbf_cleartext where id=$id");
}

function addMember($id,$member)
{
	SQL_query("INSERT INTO selectbf_categorymember (member,category) VALUES ('$member',$id)");
}

function deleteCategory($id)
{
	SQL_query("DELETE FROM selectbf_category where id = $id");
	SQL_query("DELETE FROM selectbf_categorymember where category = $id");
}

function changeCollectData($collect_data,$id)
{
	SQL_query("UPDATE selectbf_category SET collect_data=$collect_data where id=$id");
}

function deleteMember($id,$member)
{
	SQL_query("DELETE FROM selectbf_categorymember WHERE member='$member' AND category=$id");
}

function addUnCategorized($categories)
{
	$weapons = array();
	$res = SQL_query("select distinct weapon from selectbf_kills_weapon order by weapon ASC");
	while($cols = SQL_fetchArray($res))
	{
		array_push($weapons,$cols);
	}
	
	if(!is_countable($categories)){
		$categories = array();
	}
	for($i = 0; $i<count($categories); $i++)
	{
		$members = $categories[$i]["members"];
		
		$uncategorized = array();
		
		if(!is_countable($weapons)){
			$weapons = array();
		}
		for($j = 0; $j<count($weapons); $j++)
		{
			$found = false;
			if(!is_countable($members)){
				$members = array();
			}
			for($k = 0; $k<count($members) && $found==false; $k++)
			{
				if($weapons[$j]["weapon"]==$members[$k]["member"])
				{
					$found = true;
				}
			}
			if($found == false) 
			{
				array_push($uncategorized,array("weapon"=>$weapons[$j]["weapon"]));
			}
		}
		
		$categories[$i]["uncategorized"] = $uncategorized;
	}
	
	return $categories;
}

function getCategories()
{
	$res = SQL_query("select id,name,collect_data,datasource_name from selectbf_category WHERE type='WEAPON' order by name asc");
	
	$categories = array();
	while($cols = SQL_fetchArray($res))
	{
		$id = $cols["id"];
		$name = $cols["name"];
		
		$collect_data = array();
		if($cols["collect_data"]=="1")
		{
			array_push($collect_data,array("option"=>"yes","selected"=>true,"value"=>"1"));
			array_push($collect_data,array("option"=>"no","selected"=>false,"value"=>"0"));
		}
		else
		{
			array_push($collect_data,array("option"=>"yes","selected"=>false,"value"=>"1"));
			array_push($collect_data,array("option"=>"no","selected"=>true,"value"=>"0"));			
		}
		$datasource = $cols["datasource_name"];
		$deletelink = "r_categories.php?todo=delete_category&id=".$id;
		
		$member = array();
		$resultset = SQL_query("select member from selectbf_categorymember where category = $id");
		$i = 0;
		while($columns = SQL_fetchArray($resultset))
		{
			$columns["deletelink"] = "r_categories.php?todo=delete_member&member=".$columns["member"]."&id=".$id;
			array_push($member,$columns);
			$i++;
		}
		if($i == 0) $member = false;
		
		$category = array("id"=>$id,"name"=>$name,"collect_data"=>$collect_data,"datasource_name"=>$datasource,"deletelink"=>$deletelink,"members"=>$member);
		
		array_push($categories,$category);
	}
	
	return $categories;
}

function setRankingFormula($formula)
{
	setValueForParameter($formula,"RANK-FORMULA");
}

function getRankingFormula()
{
	return getValueForParameter("RANK-FORMULA");
}
function setRankingRounding($rounding)   
 {   
         setValueForParameter($rounding,"RANK-ROUND");   
 }   
    
function setRankingRoundingNumber($roundingnumber)   
{   
        SQL_query("UPDATE selectbf_params set value='$roundingnumber' WHERE name='RANK-ROUND-NUMBER'");   
}   

function setTopRoundsValue($toprounds)   
{   
        SQL_query("UPDATE selectbf_params set value='$toprounds' WHERE name='TOP-ROUNDS'");   
}   
   
function getRankingRounding()   
{   
        return getValueForParameter("RANK-ROUND");   
}   

function getTopRoundsValue()
{
        return getValueForParameter("TOP-ROUNDS");   
}   
   
function getRankingRoundingNumber()   
{   
        $cols = SQL_oneRowQuery("SELECT value FROM selectbf_params WHERE name='RANK-ROUND-NUMBER'");   
        return $cols["value"];   
} 

function createCategory($name,$collectdata,$datasourcename,$type)
{
	$name = addslashes($name);
	$datasourcename = addslashes($datasourcename);
	if($collectdata!="1" && $collectdata!="0")
	{
		$collectdata = "0";
	}
	SQL_query("INSERT INTO selectbf_category (name,collect_data,datasource_name,type,inserttime) VALUES ('$name',$collectdata,'$datasourcename','$type',now())");
}

function getMods()
{
	$res = SQL_query("Select distinct modid 'mod' from selectbf_games order by 'mod' ASC");
	$mods = array();
	while($cols = SQL_fetchArray($res))
	{
		array_push($mods,$cols);
	}
	
	return $mods;
}

function addAssignment($item,$mod,$type)
{
	$item = addslashes($item);
	$mod = addslashes($mod);
	SQL_query("INSERT INTO selectbf_modassignment (item,mod,type,inserttime) VALUES ('$item','$mod','$type',now())");
}

function deleteAssignment($id)
{
	SQL_query("DELETE FROM selectbf_modassignment where id=$id");
}

function getUnAssignedWeapons()
{
	$res = SQL_query("select distinct weapon from selectbf_kills_weapon order by weapon asc");
	$total_weapons = array();
	while($cols = SQL_fetchArray($res))
	{
		array_push($total_weapons,array("weapon"=>$cols["weapon"]));
	}
	
	$res = SQL_query("select item weapon from selectbf_modassignment where type='WEAPON' order by weapon ASC");
	$cleared_weapons = array();
	while($cols = SQL_fetchArray($res))
	{
		array_push($cleared_weapons,array("weapon"=>$cols["weapon"]));		
	}
	
	$weapons = array();
	if(!is_countable($total_weapons)){
		$total_weapons = array();
	}
	for($i=0;$i<count($total_weapons);$i++)
	{
		$found = false;
		if(!is_countable($cleared_weapons)){
			$cleared_weapons = array();
		}
		for($j=0;$j<count($cleared_weapons) && $found!=true;$j++)
		{
			if($total_weapons[$i]["weapon"] == $cleared_weapons[$j]["weapon"])
			{
				$found = true;
			}
		}
		
		if($found!=true)
		{
			array_push($weapons,array("weapon"=>$total_weapons[$i]["weapon"],"cleartext"=>clearUpText($total_weapons[$i]["weapon"],"WEAPON")));
		}
	}
	
	return $weapons;
}

function getUnAssignedKits()
{
	$res = SQL_query("select distinct kit from selectbf_kits order by kit asc");
	$total_kits = array();
	while($cols = SQL_fetchArray($res))
	{
		array_push($total_kits,array("kit"=>$cols["kit"]));
	}
	
	$res = SQL_query("select item kit from selectbf_modassignment where type='KIT' order by kit ASC");
	$cleared_kits = array();
	while($cols = SQL_fetchArray($res))
	{
		array_push($cleared_kits,array("kit"=>$cols["kit"]));		
	}
	
	$kits = array();
	if(!is_countable($total_kits)){
		$total_kits = array();
	}
	for($i=0;$i<count($total_kits);$i++)
	{
		$found = false;
		if(!is_countable($cleared_kits)){
			$cleared_kits = array();
		}
		for($j=0;$j<count($cleared_kits) && $found!=true;$j++)
		{
			if($total_kits[$i]["kit"] == $cleared_kits[$j]["kit"])
			{
				$found = true;
			}
		}
		
		if($found!=true)
		{
			array_push($kits,array("kit"=>$total_kits[$i]["kit"],"cleartext"=>clearUpText($total_kits[$i]["kit"],"KIT")));
		}
	}
	
	return $kits;
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

function getAssignments($type)
{
	$res = SQL_query("select id,item, 'mod' from selectbf_modassignment where type='$type'");
	
	$assignments = array();
	while($cols = SQL_fetchArray($res))
	{
		$cols["item"] = clearUpText($cols["item"],$type);
		$cols["deletelink"] = "r_mod-assign.php?todo=delete&id=".$cols["id"];
		array_push($assignments, $cols);
	}	
	
	return $assignments;
}

function getClearedGameModes()
{
	$res = SQL_query("select id, original, custom from selectbf_cleartext where type='GAME-MODE' order by original ");
	$cleared_gamemodes = array();
	while($cols = SQL_fetchArray($res))
	{
		$cols["deletelink"] = "r_clear-text.php?todo=delete&id=".$cols["id"];
		array_push($cleared_gamemodes,$cols);		
	}
	
	return $cleared_gamemodes;
}

function getUnClearedGameModes()
{
	$res = SQL_query("select distinct game_mode gamemode from selectbf_games order by gamemode asc");
	$total_gamemodes = array();
	while($cols = SQL_fetchArray($res))
	{
		array_push($total_gamemodes,array("gamemode"=>$cols["gamemode"]));
	}
	
	$res = SQL_query("select original gamemode from selectbf_cleartext where type='GAME-MODE' order by gamemode ASC");
	$cleared_gamemodes = array();
	while($cols = SQL_fetchArray($res))
	{
		array_push($cleared_gamemodes,array("gamemode"=>$cols["gamemode"]));		
	}
	
	$gamemodes = array();
	if(!is_countable($total_gamemodes)){
		$total_gamemodes = array();
	}
	for($i=0;$i<count($total_gamemodes);$i++)
	{
		$found = false;
		if(!is_countable($cleared_gamemodes)){
			$cleared_gamemodes = array();
		}
		for($j=0;$j<count($cleared_gamemodes) && $found!=true;$j++)
		{
			if($total_gamemodes[$i]["gamemode"] == $cleared_gamemodes[$j]["gamemode"])
			{
				$found = true;
			}
		}
		
		if($found!=true)
		{
			array_push($gamemodes,array("gamemode"=>$total_gamemodes[$i]["gamemode"]));
		}
	}
	
	return $gamemodes;
}

function getClearedWeapons()
{
	$res = SQL_query("select id, original, custom from selectbf_cleartext where type='WEAPON' order by original ");
	$cleared_weapons = array();
	while($cols = SQL_fetchArray($res))
	{
		$cols["deletelink"] = "r_clear-text.php?todo=delete&id=".$cols["id"];
		array_push($cleared_weapons,$cols);		
	}
	
	return $cleared_weapons;
}

function getUnClearedWeapons()
{
	$res = SQL_query("select distinct weapon from selectbf_kills_weapon order by weapon asc");
	$total_weapons = array();
	while($cols = SQL_fetchArray($res))
	{
		array_push($total_weapons,array("weapon"=>$cols["weapon"]));
	}
	
	$res = SQL_query("select original weapon from selectbf_cleartext where type='WEAPON' order by weapon ASC");
	$cleared_weapons = array();
	while($cols = SQL_fetchArray($res))
	{
		array_push($cleared_weapons,array("weapon"=>$cols["weapon"]));		
	}
	
	$weapons = array();
	if(!is_countable($total_weapons)){
		$total_weapons = array();
	}
	for($i=0;$i<count($total_weapons);$i++)
	{
		$found = false;
		if(!is_countable($cleared_weapons)){
			$cleared_weapons = array();
		}
		for($j=0;$j<count($cleared_weapons) && $found!=true;$j++)
		{
			if($total_weapons[$i]["weapon"] == $cleared_weapons[$j]["weapon"])
			{
				$found = true;
			}
		}
		
		if($found!=true)
		{
			array_push($weapons,array("weapon"=>$total_weapons[$i]["weapon"]));
		}
	}
	
	return $weapons;
}

function getClearedVehicles()
{
	$res = SQL_query("select id, original, custom from selectbf_cleartext where type='VEHICLE' order by original ");
	$cleared_vehicles = array();
	while($cols = SQL_fetchArray($res))
	{
		$cols["deletelink"] = "r_clear-text.php?todo=delete&id=".$cols["id"];
		array_push($cleared_vehicles,$cols);		
	}
	
	return $cleared_vehicles;
}

function getUnClearedVehicles()
{
	$res = SQL_query("select distinct vehicle from selectbf_drives order by vehicle asc");
	$total_vehicles = array();
	while($cols = SQL_fetchArray($res))
	{
		array_push($total_vehicles,array("vehicle"=>$cols["vehicle"]));
	}
	
	$res = SQL_query("select original vehicle from selectbf_cleartext where type='VEHICLE' order by vehicle ASC");
	$cleared_vehicles = array();
	while($cols = SQL_fetchArray($res))
	{
		array_push($cleared_vehicles,array("vehicle"=>$cols["vehicle"]));		
	}
	
	$vehicles = array();
	if(!is_countable($total_vehicles)){
		$total_vehicles = array();
	}
	for($i=0;$i<count($total_vehicles);$i++)
	{
		$found = false;
		if(!is_countable($cleared_vehicles)){
			$cleared_vehicles = array();
		}
		for($j=0;$j<count($cleared_vehicles) && $found!=true;$j++)
		{
			if($total_vehicles[$i]["vehicle"] == $cleared_vehicles[$j]["vehicle"])
			{
				$found = true;
			}
		}
		
		if($found!=true)
		{
			array_push($vehicles,array("vehicle"=>$total_vehicles[$i]["vehicle"]));
		}
	}
	
	return $vehicles;
}

function getClearedMaps()
{
	$res = SQL_query("select id, original, custom from selectbf_cleartext where type='MAP' order by original ");
	$cleared_maps = array();
	while($cols = SQL_fetchArray($res))
	{
		$cols["deletelink"] = "r_clear-text.php?todo=delete&id=".$cols["id"];
		array_push($cleared_maps,$cols);		
	}
	
	return $cleared_maps;
}

function getUnClearedMaps()
{
	$res = SQL_query("select distinct map from selectbf_games order by map asc");
	$total_maps = array();
	while($cols = SQL_fetchArray($res))
	{
		array_push($total_maps,array("map"=>$cols["map"]));
	}
	
	$res = SQL_query("select original map from selectbf_cleartext where type='MAP' order by map ASC");
	$cleared_maps = array();
	while($cols = SQL_fetchArray($res))
	{
		array_push($cleared_maps,array("map"=>$cols["map"]));		
	}
	
	$maps = array();
	if(!is_countable($total_maps)){
		$total_maps = array();
	}
	for($i=0;$i<count($total_maps);$i++)
	{
		$found = false;
		if(!is_countable($cleared_maps)){
			$cleared_maps = array();
		}
		for($j=0;$j<count($cleared_maps) && $found!=true;$j++)
		{
			if($total_maps[$i]["map"] == $cleared_maps[$j]["map"])
			{
				$found = true;
			}
		}
		
		if($found!=true)
		{
			array_push($maps,array("map"=>$total_maps[$i]["map"]));
		}
	}
	
	return $maps;
}

function getClearedKits()
{
	$res = SQL_query("select id, original, custom from selectbf_cleartext where type='KIT' order by original ");
	$cleared_kits = array();
	while($cols = SQL_fetchArray($res))
	{
		$cols["deletelink"] = "r_clear-text.php?todo=delete&id=".$cols["id"];
		array_push($cleared_kits,$cols);		
	}
	
	return $cleared_kits;
}

function getUnClearedKits()
{
	$res = SQL_query("select distinct kit from selectbf_kits order by kit asc");
	$total_kits = array();
	while($cols = SQL_fetchArray($res))
	{
		array_push($total_kits,array("kit"=>$cols["kit"]));
	}
	
	$res = SQL_query("select original kit from selectbf_cleartext where type='KIT' order by kit ASC");
	$cleared_kits = array();
	while($cols = SQL_fetchArray($res))
	{
		array_push($cleared_kits,array("kit"=>$cols["kit"]));		
	}
	
	$kits = array();
	if(!is_countable($total_kits)){
		$total_kits = array();
	}
	for($i=0;$i<count($total_kits);$i++)
	{
		$found = false;
		if(!is_countable($cleared_kits)){
			$cleared_kits = array();
		}
		for($j=0;$j<count($cleared_kits) && $found!=true;$j++)
		{
			if($total_kits[$i]["kit"] == $cleared_kits[$j]["kit"])
			{
				$found = true;
			}
		}
		
		if($found!=true)
		{
			array_push($kits,array("kit"=>$total_kits[$i]["kit"]));
		}
	}
	
	return $kits;
}

function registerAdminLogin($psw)
{
        @session_start();
	$_SESSION["session_psw"] = $psw;
}

function killmysession()
{
	$session_psw = $_SESSION["session_psw"];
	session_destroy();
	session_start();
	$_SESSION["session_psw"] = $session_psw;
}

function msg($str)
{
	@session_start();
	if(isset($_SESSION["msg"]))
	{
		$_SESSION["msg"] = $_SESSION["msg"].$str;
	}
	else
	{
		$_SESSION["msg"] = $str;
	}
}

function error($str)
{
	@session_start();
	if(isset($_SESSION["error"]))
	{
		$_SESSION["error"] = $_SESSION["error"].$str;
	}
	else
	{
		$_SESSION["error"] = $str;
	}
}

function getValueForParameter($str)
{
	$Ergebnis = SQL_oneRowQuery("select value from selectbf_params where name='$str'");
	return $Ergebnis["value"];
}

function setValueForParameter($value, $parameter)
{
	$value = mysqli_real_escape_string(SQL_getconnection(), $value);
	SQL_query("update selectbf_params SET value='$value',inserttime=now() WHERE name='$parameter'");
	$_SESSION["$parameter"] = $value;
}

function getTimeForParameter($str)
{
	$Ergebnis = SQL_oneRowQuery("select inserttime from selectbf_admin where name='$str'");
	return $Ergebnis["inserttime"];
}

function getRankOrderByColumn()
{
	return getValueForParameter("RANK-ORDERBY");
}

function setRankOrderByColumn($str)
{
	setValueForParameter($str,"RANK-ORDERBY");
}

function getStarNumber()
{
	return getValueForParameter("STAR-NUMBER");
}

function setStarNumber($str)
{
	setValueForParameter($str,"STAR-NUMBER");
}

function getMinRounds()
{
	return getValueForParameter("MIN-ROUNDS");
}

//Added By Gary Chiu
function getClanTableSetup()
{
	return getValueForParameter("CLAN-TABLE-SETUP");
}

function getClanAccessRight()
{
	return getValueForParameter("CLAN-ACCESS-RIGHT");
}

function getClanParserPath()
{
	return stripslashes(getValueForParameter("CLAN-PARSER-PATH"));
}

function getMinClanMembers()
{
	return getValueForParameter("MIN-CLAN-MEMBERS");
}

function getMinClanRounds()
{
	return getValueForParameter("MIN-CLAN-ROUNDS");
}

function setClanAccessRight($str)
{
	return setValueForParameter($str, "CLAN-ACCESS-RIGHT");
}

function setClanParserPath($str)
{
	return setValueForParameter($str, "CLAN-PARSER-PATH");
}

function setMinClanMembers($str)
{
	return setValueForParameter($str, "MIN-CLAN-MEMBERS");
}

function setMinClanRounds($str)
{
	return setValueForParameter($str, "MIN-CLAN-ROUNDS");
}

function setClanTag($str)
{
	$value = clear_special_char($str,false);
	$res = SQL_query("select * from selectbf_clan_tags where clan_tag = '$value'");
	if(mysqli_num_rows($res) > 0){}else{
		SQL_query("INSERT INTO selectbf_clan_tags (clan_tag) VALUES('$value')");
	}
}
//

function setMinRounds($str)
{
	setValueForParameter($str, "MIN-ROUNDS");
}

function getTitlePrefix()
{
	return getValueForParameter("TITLE-PREFIX");
}

function setTitlePrefix($str)
{
	setValueForParameter($str, "TITLE-PREFIX");
}

function getParameterInfo($str)
{
	$Ergebnis = SQL_oneRowQuery("select name, value, inserttime from selectbf_admin where name='$str'");
	return $Ergebnis;
}

function printCheckedIf($value,$const)
{
	if($value == $const)
	{
		echo "checked";
	}
}

function printSelectedIf($value,$const)
{
	if($value == $const)
	{
		echo "selected";
	}
}

function isAdmin()
{
	@session_start();

	$session_psw = "";
	if(isset($_SESSION["session_psw"]))
	{
		$session_psw = $_SESSION["session_psw"];
	}
	
	if($session_psw!="")
	{
		return true;
	}
	else
	{
		return false;
	}
}

function logoutAdmin()
{
	@session_start();
	session_destroy();
}


function timer() 
{
	list($low, $high) = preg_split("/ /", microtime());
    	$t = $high + $low;
	return $t;
}

function changeActiveTemplate($str)
{
	setParamValues($str,'TEMPLATE');
}

function getActiveTemplate()
{
	$cols = SQL_oneRowQuery("SELECT value FROM selectbf_params WHERE name='TEMPLATE'");
	return $cols["value"]; 
}

function getDebugLevel()
{
	$cols = SQL_oneRowQuery("SELECT value FROM selectbf_params WHERE name='DEBUG-LEVEL'");
	return $cols["value"]; 	
}

function changeDebugLevel($str)
{
	SQL_query("UPDATE selectbf_params set value='$str' WHERE name='DEBUG-LEVEL'");
}

function setParamValues($paramvalue,$param)
{
	SQL_query("update selectbf_params set value='$paramvalue' where name='$param'");
	$_SESSION["$param"] = $paramvalue;
}
function getParamValue($param)
{
	$cols = SQL_oneRowQuery("select value from selectbf_params where name ='$param'");
	return $cols["value"];
}
function clear_special_char($clan_name,$update_clan){
	if(stristr($_ENV["TERM"],"linux")){
		if($update_clan){
			return addslashes($clan_name);
		}else{
			return $clan_name;
		}
	}else{
		$search = array ("/\'/si",
						 "/\"/si");
		$replace = array ("\\\'",
						  "\\\"");
		$clear_format = preg_replace($search, $replace, $clan_name);
		return $clear_format;
	}
}
?>
