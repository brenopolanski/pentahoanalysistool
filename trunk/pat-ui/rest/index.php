<?php
	/*
	 * This is the router for the rest API, which makes use of the GluePHP microframework
	 */

    require_once('glue.php');
    require_once('webservices.php');
    require_once('rest.php');
    
    // FIXME - there has to be a better way of determining this for non-root deployments
    define('BASE_URL', "/rest");

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
    	JSONresponse(500, 'There was an error with your request.');
    } catch (Exception $e) {
    	JSONresponse(404, 'The url you requested could not be found.');
    }
?>