<?php

session_start();

// make sure the username is set
if (isset($_SESSION['username'])) {

    // create a new session with the username
    $url = "http://demo.analytical-labs.com/rest/" . $_SESSION['username'] . "/session";

    $ch = curl_init();

    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    //curl_setopt($ch, CURLOPT_PROXY, "http://sensis-proxy-vs.sensis.com.au");
    //curl_setopt($ch, CURLOPT_PROXYPORT, 8080);
    curl_setopt($ch, CURLOPT_HTTPHEADER, array('Accept: application/json'));
    curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
    curl_setopt($ch, CURLOPT_USERPWD, $_SESSION['username'] . ":" . $_SESSION['password']);
    curl_setopt($ch, CURLOPT_POST, 1);
    curl_setopt($ch, CURLOPT_POSTFIELDS, "");

    $output = curl_exec($ch);

    // store the output into a json object
    $obj = json_decode($output);
    $sessionid = $obj->{'@sessionid'};

    // if launch.php is relaunched the getData() function
    // will try and create a new session everytime to get
    // the schemas and cubes, this way we are always using
    // first sessionid we created orginally
    // * this is not ideal
    if (!isset($_SESSION['sessionid'])) {
        $_SESSION['sessionid'] = $sessionid;
    }

    echo $output;
}
?>