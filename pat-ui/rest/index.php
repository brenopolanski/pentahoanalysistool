<?php
	/*
	 * This is the router for the rest API
	 */

	// Necessary bits
	// Coming soon: require_once('cache.php'); // Wrapper for APC cache, used to cache API calls
    require_once('webservices.php'); // Wrapper for cURL
    require_once('rest.php'); // Proxy for PAT REST API
    
    // Where are we?
    define('REST_URL', '/rest');
    define('REQUEST_URI', strtolower($_SERVER['REQUEST_URI']));
    $subdir = stripos(REQUEST_URI, REST_URL);
    define('BASE_URL', substr(REQUEST_URI, 0, $subdir) . REST_URL);
    define('API_URL', substr(REQUEST_URI, $subdir + strlen(REST_URL), strlen(REQUEST_URI) - $subdir - strlen(REST_URL)));
    
    // Determine request method
    if ($_SERVER['REQUEST_METHOD'] == 'GET') {
        $request_method = strtoupper($_SERVER['REQUEST_METHOD']);
    } else {
        $request_method = strtoupper($_POST['method']);
    }
    
    // Get data
    // TODO - clean data?
    if ($request_method == "GET") {
		$data = $_GET;    	
    } else if ($request_method == "POST") {
    	$data = $_POST;
    }
    

	// Removed glue.php
	// This wasn't necessary if we're just acting as a proxy. Now we're *really* OO
	$rest = new Rest();
	echo $rest->request($request_method, API_URL, $data);
?>