<?php
/*
Send a message with html text and 2 attachment.
We will also set the priority and request a receipt.
*/

if ($showsource) {
    show_source ($SCRIPT_FILENAME);
    exit;
}

    include_once("../../vlibMimeMail.php");

    $mail= new vlibMimeMail; // create the mail
    $mail->to('to_example@example.com', 'To Example');
    $mail->from('me@example.com', 'From Me');
    $mail->subject("this is a test subject");
    $mail->priority(1); // sets the priority to highest
    $mail->receipt(); // requests a receipt

    $htmlbody = "<html>\n<body>\nHello<br>\n\nThis is an html body</body>\n</html>";
    $mail->htmlbody($htmlbody);    // set the body

    // attach a file to the mail, setting the disposition and mime-type
    $mail->attach('./example1.php','attachment','text/plain');
    // attach another file to the mail
    $mail->attach('./example2.php');

    /* send the mail (this is commented out as it's purely documentational) */
    //$mail->send();
    echo "<pre>", $mail->get(), "</pre>"; // and echo the entire body of the mail to see what it looks like
?>
    <a href="./example3.php?showsource=1">show source</a>

