<?php
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
$tmpl = new vlibTemplate("../templates/original/admin/ranking.html");
//evaluate Messages and Errors!
require_once("messages.php");

//set basic Template-Variables
$tmpl->setVar("TITLE","select(bf) - Admin Panel");
$tmpl->setVar("CSS","../templates/original/include/$TMPL_CFG_CSS");
$tmpl->setVar("IMAGES_DIR","../templates/original/images/");   

$tmpl->setVar("form_action","r_ranking.php");
$tmpl->setVar("param_formula","formula");
$tmpl->setVar("input_round1","whole");
$tmpl->setVar("input_round2","none");
$tmpl->setVar("input_round3","other");
$tmpl->setVar("param_round","rounding");
$tmpl->setVar("param_roundnumber","roundingnumber");

//Added By Gary Chiu
//Create Available(value) if there aren't Available(value) in the dbTable("selectbf_params").
//Setup $dataArray format. (What Available(value) you want to create.)
$dataArray = array(
	array("name" => "RANK-FORMULA",  "value" => 0, "inserttime" => 'now()'),
	array("name" => "RANK-ROUND", "value" => 0, "inserttime" => 'now()'),
	array("name" => "RANK-ROUND-NUMBER",  "value" => 0, "inserttime" => 'now()'),
);
//Function "createData(dbTable, field-check, $dataArray)" would do nothing if that "field-check" have created.
createData("selectbf_params", "name", $dataArray);

$formula = getRankingFormula();
$rounding = getRankingRounding();
$roundingnumber = getRankingRoundingNumber();
if(isset($_SESSION["input_formula"]))
{
	$tmpl->setVar("input_formula",$_SESSION["input_formula"]);
	$_SESSION["input_formula"] = null;	
}
else
{
	$tmpl->setVar("input_formula",$formula);
}
$tmpl->setVar("formula",$formula);
$tmpl->setVar("rounding",$rounding);
$tmpl->setVar("roundingnumber",$roundingnumber);
$tmpl->setVar("input_round1a"," ");
$tmpl->setVar("input_round2a"," ");
$tmpl->setVar("input_round3a"," ");

if($rounding=="whole")
{
	$tmpl->setVar("input_round1a"," checked ");
}
elseif($rounding=="none")
{
	$tmpl->setVar("input_round2a"," checked ");
}
else
{
	$tmpl->setVar("input_round3a"," checked ");
}

//now finish the processtime timer
$totaltime = timer()- $starttime;
$tmpl->setVar("PROCESSTIME",sprintf ("%01.2f seconds",$totaltime));

@$tmpl->pparse();
}
?>
