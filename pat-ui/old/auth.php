<?php

/*
 *  Document    :   auth.php
 *  Author      :   Prashant Raju (http://www.prashantraju.com/)
 *  Description :   This file is used to authenticate the user and retrieve
 *                  information about available schemas and cubes.
 *  @username   :   username used for authentication and for PAT's REST web service
 *  @password   :   password used for authentication
 *
 */

//  Start session
session_start();

/*
 *  Check if the username has been stored as a session variable,
 *  if it has we can assume that the user is wanting to retrieve
 *  schema and cubes, if not they are trying to authenticate.
 */
if (!isset($_SESSION['username'])) {

    //  Get the username/password from the login form
    $username = $_POST['username'];
    $password = $_POST['password'];

    //  URL for PAT's REST web service
    $url = "http://demo.analytical-labs.com/rest/admin/session";
    //$url = "http://localhost/pat-ui/json/info.json";

    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    //  Make sure the header is set to accept JSON
    curl_setopt($ch, CURLOPT_HTTPHEADER, array('Accept: application/json'));
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    //curl_setopt($ch, CURLOPT_PROXY, "http://sensis-proxy-vs.sensis.com.au");
    //curl_setopt($ch, CURLOPT_PROXYPORT, 8080);
    curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
    curl_setopt($ch, CURLOPT_USERPWD, $username . ":" . $password);
    curl_setopt($ch, CURLOPT_POST, 1);
    //  Had to set this for PHP's cURL library to play nice
    curl_setopt($ch, CURLOPT_POSTFIELDS, "");
    $output = curl_exec($ch);

    //  Convert the output to a JSON object
    $obj = json_decode($output);
    //  Retrieve the sessionid from the JSON object
    $sessionid = $obj->{'@sessionid'};

    //  If the cURL error code is 0 (success) and the sessionid is set
    //  then authentication was successful
    if (curl_errno($ch) == 0 && isset($sessionid)) {
        //  Store the username/password as session variables
        $_SESSION['username'] = $username;
        $_SESSION['password'] = $password;
        //  Store the connections and sessionid as session variable
        $_SESSION['connections'] = $output;
        $_SESSION['sessionid'] = $sessionid;
        //  Had to echo as 'return' was not working
        echo curl_errno($ch);
    } else {
        //  Else output what the error is
        echo curl_error($ch);
    }
} else {
    //  If the username is not set then logout
    header('Location: logout.php');
}
?>