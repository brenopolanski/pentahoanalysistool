<?php

// start the session
session_start();

// get the username and password from the login form
$username = $_POST['username'];
$password = $_POST['password'];

// url to create a new session
$url = "http://demo.analytical-labs.com/rest/admin/session";

$ch = curl_init();

curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_HTTPHEADER, array('Accept: application/json'));
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
//curl_setopt($ch, CURLOPT_PROXY, "http://sensis-proxy-vs.sensis.com.au");
//curl_setopt($ch, CURLOPT_PROXYPORT, 8080);
curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
curl_setopt($ch, CURLOPT_USERPWD, $username . ":" . $password);
curl_setopt($ch, CURLOPT_POST, 1);
curl_setopt($ch, CURLOPT_POSTFIELDS, "");


$output = curl_exec($ch);

// convert the output into a json object
$obj = json_decode($output);
// get the sessionid
$sessionid = $obj->{'@sessionid'};

// if you were able to get the sessionid then echo true else false

if (!isset($sessionid)) {
    echo "false";
} else {
    // store the username as a session variable
    $_SESSION['username'] = $username;
    $_SESSION['password'] = $password;
    echo "true";
}
?>