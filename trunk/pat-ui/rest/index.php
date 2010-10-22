<?php
	/*
	 * This is the router for the rest API, which makes use of the GluePHP microframework
	 */

    require_once('glue.php');
    require_once('webservices.php');
    require_once('rest.php');

    // TODO - define routes here
    // See http://gluephp.com/documentation.html
    $urls = array(
        '/(?P<route1>/[-\w]+)' => 'Rest'
    );

    glue::stick($urls);
?>