<?php
/*
This script shows the current date, yesterdays date, and tomorrow date in
all supported languages.
*/

if ($showsource) {
    show_source ($SCRIPT_FILENAME);
    exit;
}

    include_once("../../vlibDate.php");

    $date = new vlibDate();
    $today = $date->now(); // set $today to today's timestamp
    $yesterday = $date->prevDay($today); // get yesterdays date
    $tomorrow  = $date->nextDay($today); // get tomorrows date
?>
<html>
<body>
<table border=1>
    <tr>
    <th> Language </th>
    <th> Yesterday </th>
    <th> Today </th>
    <th> Tomorrow </th>
    </tr>
<?php
    foreach ($date->accepted_langs as $lang) {
        $date->setLang ($lang);
        echo ("<td>$lang</td>\n");
        echo ('<td>'.htmlentities($date->formatDate($yesterday, '%A %d%s %B %Y'))."</td>\n");
        echo ('<td>'.htmlentities($date->formatDate($today,     '%A %d%s %B %Y'))."</td>\n");
        echo ('<td>'.htmlentities($date->formatDate($tomorrow,  '%A %d%s %B %Y'))."</td>\n");
        echo ("</tr>\n");
    }
?>
</table>
    <a href="./example2.php?showsource=1">show source</a>

</body>
</html>
