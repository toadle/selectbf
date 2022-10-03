<?php
require("admin_func.php");
require("update_sql.php");
require_once("../include/vLib/vlibTemplate.php");
use clausvb\vlib\vlibTemplate;
//read the needed vars
$html = $_REQUEST["html"];
if(!isset($html)){
	$numargs = $_SERVER['argc'];
	if($numargs == 3){
		$fullpath = $_SERVER['argv'][0];
		$argv[0] = $_SERVER['argv'][1];
		$argv[1] = $_SERVER['argv'][2];
		for($i=0;$i<count($argv);$i++){
			if(preg_match("/html=/i", $argv[$i])){
				$html = str_replace("html=", "", $argv[$i]);
			}
			if(preg_match("/admin_pass=/i", $argv[$i])){
				$admin_pass = str_replace("admin_pass=", "", $argv[$i]);
			}
		}
	}else{
		echo "Input error!!";
	}
}

function update_clan($is_html){
	if($is_html){
		GLOBAL $tmpl;
		$updatemethod = array();
	}
	$clanname = array();
	$res = SQL_query("select * from selectbf_clan_tags");

	while($cols = SQL_fetchArray($res))
	{
		array_push($clanname, $cols);
	}
	//Define how many member counded "Clan Ranking" in.
	$res = SQL_oneRowQuery("select value from selectbf_params where name='MIN-CLAN-MEMBERS'");
	$minmember = $res["value"];
	//Define min rounds.
	$res = SQL_oneRowQuery("select value from selectbf_params where name='MIN-CLAN-ROUNDS'");
	$minround = $res["value"];
	
	//tadler: On PHPs with high warning level the vars need to be preinitialized
	$score = 0 ;
	$kills = 0 ;
	$deaths = 0 ;
	$kdrate = 0 ;
	$tks = 0 ;
	$captures = 0 ;
	$attacks = 0 ;
	$defences = 0 ;
	$objectives = 0 ;
	$objectivetks = 0 ;
	$heals = 0 ;
	$selfheals = 0 ;
	$repairs = 0 ;
	$otherrepairs = 0 ;
	$rounds_played = 0 ;
	$first = 0 ;
	$second = 0 ;
	$third = 0 ;

	for ($i=0 ; $i < count($clanname) ; $i++){
		$clantag = $clanname[$i]["clan_tag"];
		if(stristr($_ENV["TERM"],"linux"))
			$clantag = str_replace("\\","",$clantag);
		//At least 1(Default Value) round played for each member
		$res = SQL_query("SELECT * from selectbf_cache_ranking where playername like '%".clear_special_char($clantag,true)."%' and rounds_played >= $minround");
		$members = mysqli_num_rows($res);
		$clantag = str_replace("%","",$clanname[$i]["clan_tag"]);
		//At least 3(Default Value) members for each team (clan)
		if($members >= $minmember){
			while($clancols = SQL_fetchArray($res)){
				$score        += $clancols['score'];
				$kills         += $clancols['kills'];
				$deaths        += $clancols['deaths'];
				$tks           += $clancols['tks'];
				$captures      += $clancols['captures'];
				$attacks       += $clancols['attacks'];
				$defences      += $clancols['defences'];
				$objectives    += $clancols['objectives'];
				$objectivetks  += $clancols['objectivetks'];
				$heals         += $clancols['heals'];
				$selfheals     += $clancols['selfheals'];
				$repairs       += $clancols['repairs'];
				$otherrepairs  += $clancols['otherrepairs'];
				$rounds_played += $clancols['rounds_played'];
				$first		   += $clancols['first'];
				$second		   += $clancols['second'];
				$third		   += $clancols['third'];
			}
			$kdrate = round($kills / $deaths, 2);
			$clanres = SQL_query("SELECT * from selectbf_clan_ranking where clanname = '".clear_special_char($clantag,true)."'");
			if(mysqli_num_rows($clanres) > 0){
				SQL_query("update selectbf_clan_ranking SET members='$members', score='$score', kills='$kills', deaths='$deaths', kdrate='$kdrate', tks='$tks', captures='$captures', attacks='$attacks', defences='$defences', objectives='$objectives', objectivetks='$objectivetks', heals='$heals', selfheals='$selfheals', repairs='$repairs', otherrepairs='$otherrepairs', rounds_played='$rounds_played', first='$first', second='$second', third='$third' where clanname = '".clear_special_char($clantag,true)."'");
				$update_method = "Updated ";
			}else{
				SQL_query("INSERT INTO selectbf_clan_ranking (ranks, clanname, members, score, kills, deaths, kdrate, tks, captures, attacks, defences, objectives, objectivetks, heals, selfheals, repairs, otherrepairs, rounds_played, first, second, third) VALUES(NULL, '".clear_special_char($clantag,true)."', '$members', '$score', '$kills', '$deaths', '$kdrate', '$tks', '$captures', '$attacks', '$defences', '$objectives', '$objectivetks', '$heals', '$selfheals', '$repairs', '$otherrepairs', '$rounds_played', '$first', '$second', '$third')");
				$update_method = "Inserted ";
			}
			//
			if($is_html){
				$update_out = $update_method . "Total " . ($i+1) . " out of " . count($clanname) . " \"Clan Ranking\"" . " Counted " . $clantag . " in.";
				array_push($updatemethod, array("update_event"=>$update_out));
				$tmpl->setLoop("update_method",$updatemethod);
			}else{
				echo $update_method . "Total " . ($i+1) . " out of " . count($clanname) . " \"Clan Ranking\"" . " Counted " . $clantag . " in.\n";
			}
		}else{
			$clanres = SQL_query("SELECT * from selectbf_clan_ranking where clanname = '".clear_special_char($clantag,true)."'");
			if(mysqli_num_rows($clanres) > 0){
				SQL_query("DELETE FROM selectbf_clan_ranking where clanname = '".clear_special_char($clantag,true)."'");
				$update_method = " is deleted caused by there is no enough Member or Round Played!";
			}else{
				$update_method = " is not added caused by there is no enough Member or Round Played!";
			}
			//
			if($is_html){
				$update_out = $clantag . $update_method;
				array_push($updatemethod, array("update_event"=>$update_out));
				$tmpl->setLoop("update_method",$updatemethod);
			}else{
				echo $clantag . $update_method . "\n";
			}
		}
		//reset to zero
		$score         = 0;
		$kills         = 0;
		$deaths        = 0;
		$kdrate        = 0;
		$tks           = 0;
		$captures      = 0;
		$attacks       = 0;
		$defences      = 0;
		$objectives    = 0;
		$objectivetks  = 0;
		$heals         = 0;
		$selfheals     = 0;
		$repairs       = 0;
		$otherrepairs  = 0;
		$snipers       = 0;
		$rounds_played = 0;
		$first = 0 ;
		$second = 0 ;
		$third = 0 ;
	}
}
//Main
if(is_numeric($html) && $html == 1){
	require("../include/sql_setting.php");

	require_once("../templates/original/config.php");
	if(!isAdmin()){
		Header("Location: login.php");
	}else{
		//start the processtime-timer
		$starttime=timer();	
		//now start setting the variables for the Template
		$tmpl = new vlibTemplate("../templates/original/admin/update_clans.html");
		//evaluate Messages and Errors!
		require_once("messages.php");
		
		//set basic Template-Variables
		$tmpl->setVar("TITLE","select(bf) - Admin Panel");
		$tmpl->setVar("CSS","../templates/original/include/$TMPL_CFG_CSS");
		$tmpl->setVar("IMAGES_DIR","../templates/original/images/");   

		//To execute update clan method
		update_clan($html);

		//now finish the processtime timer
		$totaltime = timer()- $starttime;
		$tmpl->setVar("PROCESSTIME",sprintf ("%01.2f seconds",$totaltime));
		@$tmpl->pparse();
	}//Is Html
}elseif(is_numeric($html) && $html == 0 && isset($admin_pass)){
	//Remark:
	//You can put this "/admin/update_clans.php" program to your Schedule program for processing in non html mode.
	//How to execute this file in non html mode by using as below command:
	//php /home/selectbf/www/bfstats/admin/update_clans.php html=0 admin_pass=(your_admin_password)
	$path = convertPath($fullpath);
	require("$path../include/sql_setting.php");
	//
	//start the processtime-timer
	$starttime=timer();
	if(checkAdminPsw($admin_pass)){
		//To execute update clan method
		update_clan($html);
	}//Is not Html
	//now finish the processtime timer
	$totaltime = timer()- $starttime;
	printf("\nGenerated in %01.2f seconds\n",$totaltime);
}
?>