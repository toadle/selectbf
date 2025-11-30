<?php
require("../include/sql.php");
require("admin_func.php");

//read the needed vars
$formula = $_REQUEST["formula"];
$rounding = $_REQUEST["rounding"];
$roundingnumber = $_REQUEST["roundingnumber"];

if(isAdmin())
{
	$backup_formula = $formula;
	
	//first see if the input is valid
	$formula = str_replace("*"," ",$formula);
	$formula = str_replace("/"," ",$formula);
	$formula = str_replace("+"," ",$formula);
	$formula = str_replace("-"," ",$formula);
	$formula = str_replace("("," ",$formula);
	$formula = str_replace(")"," ",$formula);
	
	$valid = true;
	$error_subject = "";
	$variables = explode(" ",$formula);
	
	for($i = 0; $i < sizeof($variables) && $valid; $i++)
	{
		if($variables[$i]==" "
		   || $variables[$i]==""
		   || $variables[$i]=="score"
		   || $variables[$i]=="tks"
		   || $variables[$i]=="kills"
		   || $variables[$i]=="deaths"
		   || $variables[$i]=="captures"
		   || $variables[$i]=="attacks"
		   || $variables[$i]=="objectives"
           || $variables[$i]=="heals"
           || $variables[$i]=="selfheals"
           || $variables[$i]=="repairs"
           || $variables[$i]=="rounds_played"
           || $variables[$i]=="kdrate"
           || $variables[$i]=="first"
           || $variables[$i]=="second"
           || $variables[$i]=="third"
           || $variables[$i]=="otherrepairs"
           || is_numeric($variables[$i]))
       {
       	  //that's ok
       }
       else
       {
       	  $valid = false;
       	  $error_subject = $variables[$i];
       }
	}
	
	if($valid)
	{
		setRankingFormula($backup_formula);
		setRankingRounding($rounding);
		setRankingRoundingNumber($roundingnumber);
		msg("<i>Ranking formula</i> saved<br>");
	}
	else
	{
		error("The ranking-formula has problems.<br><b>".$error_subject."</b> is not a valid variable or numeric value");
		$_SESSION["input_formula"] = $backup_formula;		
	}

    Header("Location: ranking.php");
}
else
{
	Header("Location: login.php");
}
?>