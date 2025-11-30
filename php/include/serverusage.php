<?php
header("Content-Type: image/png");

require_once("sql.php");

function getActiveTemplate()
{
	$cols = SQL_oneRowQuery("SELECT value FROM selectbf_params WHERE name='TEMPLATE'");
	return $cols['value'];
}

$TEMPLATE_DIR = getActiveTemplate();
require_once("../templates/$TEMPLATE_DIR/config.php");

$width=$TMPL_CFG_SERVERSTAT_WIDTH;
$height=$TMPL_CFG_SERVERSTAT_HEIGHT;

$img = imagecreate($width, $height);

$background_color= imagecolorallocate($img,$TMPL_CFG_SERVERSTAT_BACKGROUND["R"],$TMPL_CFG_SERVERSTAT_BACKGROUND["B"],$TMPL_CFG_SERVERSTAT_BACKGROUND["G"]);
$bars_color= imagecolorallocate($img,$TMPL_CFG_SERVERSTAT_BARS["R"],$TMPL_CFG_SERVERSTAT_BARS["B"],$TMPL_CFG_SERVERSTAT_BARS["G"]);

imagefilledrectangle($img,0,0,100,100,$background_color);

imagefilledrectangle($img,0,0,100,100,$bars_color);



imagePNG($img);
?>