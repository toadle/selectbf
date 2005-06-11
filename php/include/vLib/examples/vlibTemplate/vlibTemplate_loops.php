<?php
/*
This file will show you how to create a loop structure in vlibTemplate.
It will also show you how to do inner loops and the exact loop structure.
*/

if ($showsource == 1) {
    show_source ($SCRIPT_FILENAME);
    exit;
}
elseif ($showsource == 2) {
    show_source (dirname(__FILE__).'/templates/vlibTemplate_loops.html');
    exit;
}

include_once('../../vlibTemplate.php');

$tmpl = new vlibTemplate('templates/vlibTemplate_loops.html');

// first we set the title
$tmpl->setVar('title', 'This is the vlibTemplate Loops example...');

// this is the array we will use for data
$people = array(
                'David'  => array('age' => 32,
                                  'sex' => 'Male',
                                  'height' => 175,
                                  'hobby' => 'football',
                                  'children' => 2),
                'Bob'    => array('age' => 65,
                                  'sex' => 'Male',
                                  'height' => 150,
                                  'hobby' => 'rugby',
                                  'children' => 0),
                'Gloria' => array('age' => 28,
                                  'sex' => 'Female',
                                  'height' => 160,
                                  'hobbies' => 'swimming, TV',
                                  'children' => 1)
                );


// the following will be a simple loop, after we'll show you an inner loop.

$peoplearr_basic = array(); // we need to rebuild the array into the correct format (see docs)

foreach ($people as $name => $details)
{

    array_push($peoplearr_basic, array(
                        'name' => $name,
                        'age' => $details['age'],
                        'sex' => $details['sex'],
                        'height' => $details['height']
                ));

}

$tmpl->setLoop('basic_loop', $peoplearr_basic); // sets the loop in the vlibTemplate


// now here's the more complicated loop

$peoplearr_adv = array(); // we need to rebuild the array into the correct format (see docs)

foreach ($people as $name => $details)
{

    $detailsarr = array();
    // this is the inner loop
    foreach ($details as $detail => $value)
    {
        // set a couple of variables
        array_push($detailsarr, array(
                            'detail' => $detail,
                            'value' => $value
                    ));

    } // << inner loop

    array_push($peoplearr_adv, array(
                        'name' => $name,
                        'details' => $detailsarr  // this is where we set the inner loop
                ));

}

$tmpl->setLoop('advanced_loop', $peoplearr_adv); // set the loop


/*
NB: I know this can be daunting at first, but what you must remember is that each loop in the array is a complete
    loop in it's own right. It must therefore have exactly the same structure as the simple loops.

The above array that we would parse would look like this (comments r on the 1st loop 2 show U what is happening):

    Array
    (
        [0] => Array // outer array row 0
            (
                [name] => David // variable
                [details] => Array // loop name because it's an array
                    (
                        [0] => Array // inner array row 0
                            (
                                [detail] => age // variable
                                [value] => 45 // variable
                            )

                        [1] => Array // inner array row 1
                            (
                                [detail] => sex // variable
                                [value] => Male // variable
                            )

                        [2] => Array // inner array row 2
                            (
                                [detail] => height // variable
                                [value] => 175 // variable
                            )

                    )

            )

        [1] => Array
            (
                [name] => Bob
                [details] => Array
                    (
                        [0] => Array
                            (
                                [detail] => age
                                [value] => 65
                            )

                        [1] => Array
                            (
                                [detail] => sex
                                [value] => Male
                            )

                        [2] => Array
                            (
                                [detail] => height
                                [value] => 150
                            )

                    )

            )

        [2] => Array
            (
                [name] => Gloria
                [details] => Array
                    (
                        [0] => Array
                            (
                                [detail] => age
                                [value] => 28
                            )

                        [1] => Array
                            (
                                [detail] => sex
                                [value] => Female
                            )

                        [2] => Array
                            (
                                [detail] => height
                                [value] => 160
                            )

                    )

            )

    )


You can have as many inner loops as you please.
*/

// parse and print the template
$tmpl->pparse();
?>
