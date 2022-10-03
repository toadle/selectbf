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
$tmpl = new vlibTemplate("../templates/original/admin/templates.html");
//evaluate Messages and Errors!
require_once("messages.php");

//set basic Template-Variables
$tmpl->setVar("TITLE","select(bf) - Admin Panel");
$tmpl->setVar("CSS","../templates/original/include/$TMPL_CFG_CSS");
$tmpl->setVar("IMAGES_DIR","../templates/original/images/");   

$dir=opendir("../templates");

$activetemplate = getActiveTemplate();

$templates = array();
while ($file = readdir($dir))
{
   if(!($file == "." || $file == "..") && is_dir("../templates/".$file))
   {
   		if($activetemplate == $file)
   		{
			array_push($templates,array("name"=>$file,"selected"=>true));
   		}
   		else
   		{
   			array_push($templates,array("name"=>$file));
   		}
   }
}
$tmpl->setLoop("templates",$templates);
closedir($dir);

$tmpl->setVar("activeTemplate",$activetemplate);
$tmpl->setVar("form_action","r_templates.php");
$tmpl->setVar("param_template","template");

//now finish the processtime timer
$totaltime = timer()- $starttime;
$tmpl->setVar("PROCESSTIME",sprintf ("%01.2f seconds",$totaltime));

@$tmpl->pparse();
}
?>
