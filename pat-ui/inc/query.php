<?php

/*
 *  Document        :   query.php
 *  Author          :   Prashant Raju (http://www.prashantraju.com/)
 *  Description     :   This file is used to retrieve dimensions and measures
 *                      with for a specific cube.
 *  @sessionid      :   sessionid stored in the session
 *  @username       :   username stored in the session
 *  @connectionid   :   connectionid used for the new query
 *  @schemaname     :   schemname used for the new query
 *  @cubename       :   cubename used for the new query
 * 
 */

//  Start session
session_start();

//  If the username if set we can continue
if (isset($_SESSION['username'])) {

    //  Get the connectionid, schemaname and cubename
    $connectionid = $_POST['connectionid'];
    $schemaname = $_POST['schemaname'];
    $cubename = $_POST['cubename'];

    //  Create a URL parameter string for PATs REST web service
    $fields_string = "sessionid=" . $_SESSION['sessionid'] . "&connectionid=" . $connectionid;

    //  URL for PAT's REST web service
    $url = "http://demo.analytical-labs.com/rest/" . $_SESSION['username'] . "/query/" . $schemaname . "/" . $cubename . "/newquery";
    //$url = "http://localhost/pat-ui/json/query.json";

    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    //curl_setopt($ch, CURLOPT_PROXY, "http://sensis-proxy-vs.sensis.com.au");
    //curl_setopt($ch, CURLOPT_PROXYPORT, 8080);
    //  Make sure the header is set to accept JSON
    curl_setopt($ch, CURLOPT_HTTPHEADER, array('Accept: application/json'));
    curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
    curl_setopt($ch, CURLOPT_USERPWD, $_SESSION['username'] . ":" . $_SESSION['password']);
    curl_setopt($ch, CURLOPT_POST, 1);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $fields_string);
    $output = curl_exec($ch);

    //  Convert the output to a JSON object
    $obj = json_decode($output);
    //  Retrieve the sessionid from the JSON object
    $queryid = $obj->{'@queryid'};

    //  If the cURL error code is 0 (success)
    if (curl_errno($ch) == 0) {
        //  Set the queryid as a SESSION variable
        $_SESSION['queryid'] = $queryid;
        //  Output schemas and cubes as JSON
        echo $output;
    } else {
        //  Else output what the error is
        echo "false";
    }
} else {
    //  Logout
    header('Location: logout.php');
}

?>