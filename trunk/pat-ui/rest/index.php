<?php
	/*
	 * This is the router for the rest API, which makes use of the GluePHP microframework
	 */

    require_once('glue.php');
    require_once('webservices.php');
    require_once('rest.php');
    
    // TODO - there has to be a better way of determining this for non-root deployments
    define('BASE_URL', "/patui/rest");

    // Define routes here - See http://gluephp.com/documentation.html
    $urls = array(
    	BASE_URL . '/' => 'Rest',
        BASE_URL . '/(?P<route1>[-\w]+)/' => 'Rest',
        BASE_URL . '/(?P<route1>[-\w]+)/(?P<route2>[-\w]+)/' => 'Rest',
        BASE_URL . '/(?P<route1>[-\w]+)/(?P<route2>[-\w]+)/(?P<route3>[-\w]+)/' => 'Rest'
    );
	
    try {
    	glue::stick($urls);
    } catch (Exception $e) {
    	header("HTTP/1.0 404 Not Found");
    	die("The page you requested was not found.");
    }
?>