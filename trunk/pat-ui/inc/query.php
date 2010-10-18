<?php

/*
 *  Document    :   rest.php
 *  Author      :   Prashant Raju (http://www.prashantraju.com/)
 *  Description :   This file handles all REST calls to PAT's demo server.
 *
 *  @url        :   URL for the PAT server
 *  @method     :   Method being used to interact with REST service
 */

//  Start session
session_start();

if (isset($_SESSION['username'])) {
    if ($_SERVER['REQUEST_METHOD'] == 'GET') {
        $method = $_SERVER['REQUEST_METHOD'];
    } else {
        $method = $_POST['method'];
    }

//  Headers and data, change this if you would like to work with XML
//  instead of JSON
    $headers = array(
        'Accept: application/json',
        'Content-Type: application/json',
    );

//  For each method...
    switch ($method) {
        case 'GET':
            echo $_SESSION['connections'];
            break;

        case 'POST_NEW':
            //  Get the connectionid, schemaname and cubename
            $connectionid = $_POST['connectionid'];
            $schemaname = $_POST['schemaname'];
            $cubename = $_POST['cubename'];

            //  Create a URL parameter string for PATs REST web service
            $fields_string = "sessionid=" . $_SESSION['sessionid'] . "&connectionid=" . $connectionid;

            //  URL for PAT's REST web service
            $url = "http://demo.analytical-labs.com/rest/" . $_SESSION['username'] . "/query/" . $schemaname . "/" . $cubename . "/newquery";

            $ch = curl_init();

            curl_setopt($ch, CURLOPT_URL, $url);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
            //curl_setopt($ch, CURLOPT_PROXY, "http://sensis-proxy-vs.sensis.com.au");
            //curl_setopt($ch, CURLOPT_PROXYPORT, 8080);
            //  Make sure the header is set to accept JSON
            curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
            curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
            curl_setopt($ch, CURLOPT_USERPWD, $_SESSION['username'] . ":" . $_SESSION['password']);
            curl_setopt($ch, CURLOPT_POST, 1);
            curl_setopt($ch, CURLOPT_POSTFIELDS, $fields_string);
            $output = curl_exec($ch);

            //  Convert the output to a JSON object
            $obj = json_decode($output);
            //  Retrieve the queryid from the JSON object
            $queryid = $obj->{'@queryid'};

            //  HTTP status of response
            $status = curl_getinfo($ch, CURLINFO_HTTP_CODE);

            //  If the cURL error code is 0 (success)
            if ($status == 200 && $queryid != null) {
                //  Set the queryid as a SESSION variable
                $_SESSION['queryid'] = $queryid;
                //  Output schemas and cubes as JSON
                echo $output;
            } else {
                //  Else output what the error is
                echo "false";
            }
            break;

        case 'POST_RUN':
            //  Get the connectionid, schemaname and cubename
            $schemaname = $_POST['schemaname'];
            $cubename = $_POST['cubename'];
            $query = $_POST['query'];

            $ch = curl_init();

            //  URL for PAT's REST web service
            $url = "http://demo.analytical-labs.com/rest/" . $_SESSION['username'] . "/query/" . $schemaname . "/" . $cubename . "/newquery/run?sessionid=" . $_SESSION['sessionid'];

            $putString = $query;
            $putData = tmpfile();
            fwrite($putData, $putString);
            fseek($putData, 0);

            curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
            curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
            curl_setopt($ch, CURLOPT_USERPWD, $_SESSION['username'] . ":" . $_SESSION['password']);
            curl_setopt($ch, CURLOPT_BINARYTRANSFER, true);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
            curl_setopt($ch, CURLOPT_URL, $url);
            curl_setopt($ch, CURLOPT_PUT, true);
            curl_setopt($ch, CURLOPT_INFILE, $putData);
            curl_setopt($ch, CURLOPT_INFILESIZE, strlen($putString));

            $output = curl_exec($ch);

            fclose($putData);
            curl_close($ch);

            //  HTTP status of response
            // $status = curl_getinfo($ch, CURLINFO_HTTP_CODE);
            //  If the cURL error code is 0 (success)
            if ($output) {
                //  Output schemas and cubes as JSON
                echo $output;
            } else {
                //  Else output what the error is
                echo "false";
            }
            break;

        case 'DELETE':
            break;
    }
} else {
    //  If username is not set logout
    header('Location: logout.php');
}
?>