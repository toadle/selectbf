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
$tmpl = new vlibTemplate("../templates/original/admin/clantag.html");


//set basic Template-Variables
$tmpl->setVar("TITLE","select(bf) - Admin Panel");
$tmpl->setVar("CSS","../templates/original/include/$TMPL_CFG_CSS");
$tmpl->setVar("IMAGES_DIR","../templates/original/images/");   

//Clan DataTable is not setup if "CLAN-TABLE-SETUP" is 0
if(getClanTableSetup()){
	$clantablesetup = true;
}else{
	$clantablesetup = false;
	$tmpl->setVar("clantablesetup","CLAN Data Table isn't created, please run setup again!");
}
//Clan Access Right
//realpath function is works on PHP4 only
$path = convertPath(realpath("../admin/update_clans.php"));
if(checkFilePathConsistency($path, "update_clans.php") && $clantablesetup){
	$tmpl->setVar("form_action","r_clantag.php");
	$tmpl->setVar("addclantag", true);
	$tmpl->setVar("new_clan_tag","clan_tag");
	$res = SQL_query("select clan_tag from selectbf_clan_tags");
	if(mysqli_num_rows($res) > 0)
	{
		$addedresults = array();
		while($cols = SQL_fetchArray($res))
		{
			$cols["deletelink"] = "r_clantag.php?todo=delete&id=" . $cols["clan_tag"];
			array_push($addedresults,$cols);
		}
		$tmpl->setLoop("addedresults",$addedresults);
		$tmpl->setVar("update_clans", "update_clans.php?html=1");
	}
	$tmpl->setVar("clanaccessright","Clan Tags Management is: Enabled");
}else{
	$tmpl->setVar("clanaccessright","Clan Tags Management is: Disabled");
	$tmpl->setVar("clanpatherror","Update_Clans.php Parser file is not found on \"admin\" directory!");
}

//evaluate Messages and Errors!
require_once("messages.php");

//now finish the processtime timer
$totaltime = timer()- $starttime;
$tmpl->setVar("PROCESSTIME",sprintf ("%01.2f seconds",$totaltime));

@$tmpl->pparse();
}
?>
