<?php
/*
This script simply displays a couple of dates, and gives you an idea into
date manipulation with this class.
*/

if ($showsource) {
    show_source ($SCRIPT_FILENAME);
    exit;
}

    include_once("../../vlibDate.php");

    $date = new vlibDate();
    $today = $date->now(); // set $today to today's timestamp
    $nextweek = $date->addInterval($today, '1 WEEK'); // set $nextweek to 1 week from $today
?>
<html>
<body>
Hello customer,
<br><br>
Your subscription to our website has elapsed today (<b>

<?php
    echo $date->formatDate($today, '%A %e%s %B, %Y');
?>

</b>).<br>
If you do not contact us by this time next week (<b>

<?php
    echo $date->formatDate($nextweek, '%A %e%s %B, %Y');
?>

</b>) your account will be deleted.
<br><br>
Kind regards,<br><br>
The Boss<br><br>
    <a href="./example1.php?showsource=1">show source</a>
</body>
</html>

