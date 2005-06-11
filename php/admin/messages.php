<?
@session_start();
if(isset($_SESSION["msg"]))
{
	$msg = $_SESSION["msg"];
	$tmpl->setVar("message",$msg);
	$_SESSION["msg"] = null;
	unset($_SESSION["msg"]);
}


if(isset($_SESSION["error"]))
{
	$error = $_SESSION["error"];
	$tmpl->setVar("error",$error);
	$_SESSION["error"] = null;
	unset($_SESSION["error"]);
}
?>