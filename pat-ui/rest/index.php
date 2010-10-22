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
    // Trailing slashes limits the urls that hit
    $urls = array(
    	BASE_URL . '' => 'Rest',
        BASE_URL . '/(?P<route1>[-\w]+)' => 'Rest',
        BASE_URL . '/(?P<route1>[-\w]+)/(?P<route2>[-\w]+)' => 'Rest',
        BASE_URL . '/(?P<route1>[-\w]+)/(?P<route2>[-\w]+)/(?P<route3>[-\w]+)' => 'Rest'
    );
	
    try {
    	glue::stick($urls);
    } catch (BadMethodCallException $e) {
    	header("HTTP/1.0 500 Internal Server Error");
    	$output = array( 'error'=>'There was an error with your request.' );
		die(json_encode($output)); // I've always loved the sweet justice of this function
    } catch (Exception $e) {
    	header("HTTP/1.0 404 Not Found");
    	$output = array( 'error'=>'The page you requested was not found.' );
		die(json_encode($output)); // I've always loved the sweet justice of this function
    }
?>