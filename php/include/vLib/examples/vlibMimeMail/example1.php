<?php
/*
Send a simple message with plain text and no attachments.
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
    $mail->body("Hello\n\nThis is a plain text body for the mail!");    // set the body

    /* send the mail (this is commented out as it's purely documentational) */
    //$mail->send();
    echo "<pre>", $mail->get(), "</pre>"; // and echo the entire body of the mail to see what it looks like
?>
    <a href="./example1.php?showsource=1">show source</a>

