<?php
/*
This file demonstrates a simple template include.
You must look at the html file in templates to see the tag.
*/

if ($showsource == 1) {
    show_source ($SCRIPT_FILENAME);
    exit;
}
elseif ($showsource == 2) {
    show_source (dirname(__FILE__).'/templates/vlibTemplate_include.html');
    exit;
}
elseif ($showsource == 3) {
    show_source (dirname(__FILE__).'/templates/vlibTemplate_include2.html');
    exit;
}

include_once('../../vlibTemplate.php');

$tmpl = new vlibTemplate('templates/vlibTemplate_include.html');

$tmpl->setVar('title', 'This demonstrates the include functionality.');

$tmpl->pparse();
?>
