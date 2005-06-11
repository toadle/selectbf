<?php
/*
This file demonstrates a simple template script.
It adds a couple of variables then parses and prints the template.
*/

if ($showsource == 1) {
    show_source ($SCRIPT_FILENAME);
    exit;
}
elseif ($showsource == 2) {
    show_source (dirname(__FILE__).'/templates/vlibTemplate_basic.html');
    exit;
}

include_once('../../vlibTemplate.php');

$tmpl = new vlibTemplate('templates/vlibTemplate_basic.html');

$tmpl->setVar('title', 'This is the vlibTemplate Basic example...');
$tmpl->setVar('msg', 'This is the message set using setVar()');

$tmpl->pparse();
?>
