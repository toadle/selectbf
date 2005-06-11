<?php
/*
This file will show you how to create a loop structure in vlibTemplate.
This style loop is more intuitive than vlibTemplate's other looping method
and is a lot easier to learn.

It happens in 3 stages. 1st you define the loop name, then you add all of
the rows that you wish, then you add the loop...
*/

if ($showsource == 1) {
    show_source ($SCRIPT_FILENAME);
    exit;
}
elseif ($showsource == 2) {
    show_source (dirname(__FILE__).'/templates/vlibTemplate_basicloop.html');
    exit;
}

include_once('../../vlibTemplate.php');

$tmpl = new vlibTemplate('templates/vlibTemplate_basicloop.html');

// first we set the title
$tmpl->setVar('title', 'This is the vlibTemplate Loops "BASIC" example...');



/*-------------------------------------------------------------------------------------*/



// this is the array we will use for data, mimicking a mysql_fetch_array() style call
$rows = array();
$rows[] = array('id' => 1, 'username' => 'pj', 'password' => 'pjpass');
$rows[] = array('id' => 2, 'username' => 'graham', 'password' => '52ddd');
$rows[] = array('id' => 3, 'username' => 'smithy', 'password' => 'mypass');
$rows[] = array('id' => 4, 'username' => 'dj', 'password' => 'vinyl');
$rows[] = array('id' => 5, 'username' => 'footymad', 'password' => 'worldcup');
$rows[] = array('id' => 6, 'username' => 'tennismad', 'password' => 'wmbldn');


// the 1st loop will show you how to turn an associative array into a vlibTemplate loop
// you can use this method as you would with the results of a mysql_fetch_array() call.
// To see a mysql example, scroll to the end of this source page.

$tmpl->newLoop('loop1'); // set the name which will be used in the template

foreach ($rows as $row) {
    $tmpl->addRow($row); // add a row to the loop
}

$tmpl->addLoop(); // completes the process by actually setting the loop in the template.

// et voila, there is the 3 stage loop method.



/*-------------------------------------------------------------------------------------*/


// if you had the above $rows but it wasn't associative, you can set the loop in the same way,
// but in your template you use variable name like _0, _1, ...etc for the names. here's an example:

$rows = array();
$rows[] = array(1, 'pj', 'pjpass');
$rows[] = array(2, 'graham', '52ddd');
$rows[] = array(3, 'smithy', 'mypass');
$rows[] = array(4, 'dj', 'vinyl');
$rows[] = array(5, 'footymad', 'worldcup');
$rows[] = array(6, 'tennismad', 'wmbldn');


// the 2nd loop will show you how to turn a non-associative array into a vlibTemplate loop.

$tmpl->newLoop('loop2'); // set the name which will be used in the template

foreach ($rows as $row) {
    $tmpl->addRow($row); // add a row to the loop
}

$tmpl->addLoop(); // completes the process by actually setting the loop in the template.

//and there is the 3 stage loop method for a non-associative array.




/*-------------------------------------------------------------------------------------*/



// The 3rd example will show you what to do with a very basic array.
// let's say that you have a array like this...
$fruits = array('apples', 'oranges', 'pears', 'strawberries', 'bananas');

// to put this in a loop, do the following

$tmpl->newLoop('loop3'); // set the name which will be used in the template

foreach ($fruits as $fruit) {
    $tmpl->addRow(array('fruit' => $fruit)); // add a row to the loop
}

$tmpl->addLoop(); // completes the process by actually setting the loop in the template.



/*-------------------------------------------------------------------------------------*/

// parse and print the template
$tmpl->pparse();


/*-------------------------------------------------------------------------------------*/
// below is an example in comments of what you would do to put a mysql result into a loop,
// it's extremely simple, see for yourself:


/* MYSQL Example
<?php

    // make a db connection
    $cnx = mysql_connect('localhost', 'user', 'pass');
    mysql_select_db('my_db', $cnx);

    $q = "select id, username, password, firstname, lastname from users";
    $r = mysql_query($q, $cnx);

    include_once "../vlibTemplate.php";

    $tmpl = new vlibTemplate('./templates/mysql_template.html');

    $tmpl->newLoop('myloop'); // define loop name

    while($row = mysql_fetch_array($r, MYSQL_ASSOC)) {
        $tmpl->addRow($row); // add row
    }
    $tmpl->addLoop(); // add loop

    $tmpl->pparse();
?>

And the template could look something like this:

<html>
    <head>
        <title>MySQL example</title>
    </head>

    <body>
    <tmpl_if name="myloop"><!-- check if the loop was filled with rows -->
        <table>
            <tr>
                <th> ID </th>
                <th> Username </th>
                <th> Password </th>
                <th> First Name </th>
                <th> Last Name </th>
            </tr>
        <tmpl_loop name="myloop"> <!-- do the loop -->
            <tr>
                <th> <tmpl_var name="id"> </th>
                <th> <tmpl_var name="username"> </th>
                <th> <tmpl_var name="password"> </th>
                <th> <tmpl_var name="firstname"> </th>
                <th> <tmpl_var name="lastname"> </th>
            </tr>
        </tmpl_loop>
        </table>
    <tmpl_else>
        Sorry, no results were found in the Database.:(
    </tmpl_if>
    </body>
</html>

And there you have it, give it a go.
*/
?>
