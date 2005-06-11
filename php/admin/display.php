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
$tmpl = new vlibTemplate("../templates/original/admin/display.html");
//evaluate Messages and Errors!
require_once("messages.php");

//set basic Template-Variables
$tmpl->setVar("TITLE","select(bf) - Admin Panel");
$tmpl->setVar("CSS","../templates/original/include/$TMPL_CFG_CSS");
$tmpl->setVar("IMAGES_DIR","../templates/original/images/");   

$tmpl->setVar("form_action","r_display.php");

$tmpl->setVar("minrounds",getMinRounds());
$tmpl->setVar("param_minrounds","minrounds");

$tmpl->setVar("starnumber",getStarNumber());
$tmpl->setVar("param_starnumber","starnumber");

$orderbycolumn = getRankOrderbyColumn();
$tmpl->setVar("orderbycolumn",$orderbycolumn);

//Added By Gary Chiu
//Create Available(value) if there aren't Available(value) in the dbTable("selectbf_params").
//Setup $dataArray format. (What Available(value) you want to create.)
$dataArray = array(
	array("name" => "CLAN-TABLE-SETUP",  "value" => 0, "inserttime" => 'now()'),
	array("name" => "MIN-CLAN-MEMBERS",  "value" => 3, "inserttime" => 'now()'),
	array("name" => "MIN-CLAN-ROUNDS",   "value" => 1, "inserttime" => 'now()'),
);
//Function "createData(dbTable, field-check, $dataArray)" would do nothing if that "field-check" have created.
createData("selectbf_params", "name", $dataArray);
//Minimum Clan's Members
$tmpl->setVar("minclanmembers",getMinClanMembers());
$tmpl->setVar("param_minclanmembers","minclanmembers");
//Minimum Clan's Rounds
$tmpl->setVar("minclanrounds",getMinClanRounds());
$tmpl->setVar("param_minclanrounds","minclanrounds");
//

$orderbycolumns = array();
if($orderbycolumn == "points") array_push($orderbycolumns,array("name"=>"points","selected"=>true));
else array_push($orderbycolumns,array("name"=>"points","selected"=>false));
if($orderbycolumn == "avgscore") array_push($orderbycolumns,array("name"=>"avgscore","selected"=>true));   
else array_push($orderbycolumns,array("name"=>"avgscore","selected"=>false));   
if($orderbycolumn == "score") array_push($orderbycolumns,array("name"=>"score","selected"=>true));
else array_push($orderbycolumns,array("name"=>"score","selected"=>false));
if($orderbycolumn == "kills") array_push($orderbycolumns,array("name"=>"kills","selected"=>true));
else array_push($orderbycolumns,array("name"=>"kills","selected"=>false));
if($orderbycolumn == "deaths") array_push($orderbycolumns,array("name"=>"deaths","selected"=>true));
else array_push($orderbycolumns,array("name"=>"deaths","selected"=>false));
if($orderbycolumn == "kdrate") array_push($orderbycolumns,array("name"=>"kdrate","selected"=>true));
else array_push($orderbycolumns,array("name"=>"kdrate","selected"=>false));
if($orderbycolumn == "first") array_push($orderbycolumns,array("name"=>"first","selected"=>true));
else array_push($orderbycolumns,array("name"=>"first","selected"=>false));
if($orderbycolumn == "second") array_push($orderbycolumns,array("name"=>"second","selected"=>true));
else array_push($orderbycolumns,array("name"=>"second","selected"=>false));
if($orderbycolumn == "third") array_push($orderbycolumns,array("name"=>"third","selected"=>true));
else array_push($orderbycolumns,array("name"=>"third","selected"=>false));
if($orderbycolumn == "tks") array_push($orderbycolumns,array("name"=>"tks","selected"=>true));
else array_push($orderbycolumns,array("name"=>"tks","selected"=>false));
if($orderbycolumn == "attacks") array_push($orderbycolumns,array("name"=>"attacks","selected"=>true));
else array_push($orderbycolumns,array("name"=>"attacks","selected"=>false));
if($orderbycolumn == "captures") array_push($orderbycolumns,array("name"=>"captures","selected"=>true));
else array_push($orderbycolumns,array("name"=>"captures","selected"=>false));
if($orderbycolumn == "objectives") array_push($orderbycolumns,array("name"=>"objectives","selected"=>true));
else array_push($orderbycolumns,array("name"=>"objectives","selected"=>false));
if($orderbycolumn == "rounds") array_push($orderbycolumns,array("name"=>"rounds","selected"=>true));
else array_push($orderbycolumns,array("name"=>"rounds","selected"=>false));
if($orderbycolumn == "heals") array_push($orderbycolumns,array("name"=>"heals","selected"=>true));
else array_push($orderbycolumns,array("name"=>"heals","selected"=>false));
if($orderbycolumn == "repairs") array_push($orderbycolumns,array("name"=>"repairs","selected"=>true));
else array_push($orderbycolumns,array("name"=>"repairs","selected"=>false));

$tmpl->setLoop("orderbycolumns",$orderbycolumns);
$tmpl->setVar("param_orderbycolumn","orderbycolumn");



//now finish the processtime timer
$totaltime = timer()- $starttime;
$tmpl->setVar("PROCESSTIME",sprintf ("%01.2f seconds",$totaltime));

@$tmpl->pparse();
}
?>
