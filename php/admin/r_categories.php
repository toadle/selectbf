<?
require("../include/sql.php");
require("admin_func.php");

//read the needed vars
@$category_name = $_REQUEST["cat_name"];
@$collect_data = $_REQUEST["col_data"];
@$datasource_name = $_REQUEST["datas_name"];
$todo = $_REQUEST["todo"];
@$id = $_REQUEST["id"];
@$member = $_REQUEST["member"];

@session_start();

if(isAdmin())
{
	switch($todo)
	{
		case "new_cat":			if($category_name != "" && $datasource_name!="")
								{
									createCategory($category_name,$collect_data,$datasource_name,"WEAPON");
									msg("New Category <b>".$category_name."</b> added<br>");
								}
								else
								{
									error("Please enter a value for every text-field!");
								}
    							break;
		case "new_member":		addMember($id,$member);
								msg("New Member <b>".$member."</b> added<br>");
								break;
	 	case "delete_member":	deleteMember($id,$member);
	 							msg("Member <b>".$member."</b> deleted from Category<br>");
	 							break;
	 	case "delete_category":	deleteCategory($id);
								msg("Category <b>deleted</b>!");
								break;
		case "change_collect":	changeCollectData($collect_data,$id);
								msg("<b>Collect-Data</b>-value changed!<br>");
								break;
	}	

	Header("Location: categories.php");
}
else
{
	Header("Location: login.php");
}
?>