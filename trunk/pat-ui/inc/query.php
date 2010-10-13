<?php

session_start();

if (isset($_SESSION['username'])) {

    $connectionid = $_POST['connectionid'];
    $schemaname = $_POST['schemaname'];
    $cubename = $_POST['cubename'];

    $fields_string = "sessionid=" . $_SESSION['sessionid'] . "&connectionid=" . $connectionid;

    $url = "http://demo.analytical-labs.com/rest/" . $_SESSION['username'] . "/query/" . $schemaname . "/" . $cubename . "/newquery";
    //$url = "http://localhost/pat/json/query.json";
    $ch = curl_init();

    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_PROXY, "http://sensis-proxy-vs.sensis.com.au");
    curl_setopt($ch, CURLOPT_PROXYPORT, 8080);
    curl_setopt($ch, CURLOPT_HTTPHEADER, array('Accept: application/json'));
    curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
    curl_setopt($ch, CURLOPT_USERPWD, $_SESSION['username'] . ":" . $_SESSION['password']);
    curl_setopt($ch, CURLOPT_POST, 1);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $fields_string);

    $output = curl_exec($ch);
    echo $output;
}
?>