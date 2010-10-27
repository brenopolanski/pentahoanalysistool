<?php

/*
 * This class will allow us to use web services in PHP,
 * abstracting out the cURL calls for us. This version
 * assumes JSON at the moment.
 */

class WebServices {
	
	private $username = '';
	
	private $password = '';
	
	public function WebServices($username='', $password='') {
		if ($username != '') {
			$this->username = $username;
		}
		
		if ($password != '') {
			$this->password = $password;
		}
	}
	
	/*
	 * This hides the ugliness that is cURL in PHP
	 * FIXME - handle PUT and DELETE requests
	 */
	public function request($method, $url, $data=array()) {
		$headers = array(
        'Accept: application/json',
        'Content-Type: application/json',
    	);
    	
    	if (! function_exists('curl_init')) {
    		JSONresponse(500, "The cURL module for PHP is not installed on this server.");
    	}
    	
	    $ch = curl_init();
	    curl_setopt($ch, CURLOPT_URL, $url);
	    curl_setopt($ch, CURLOPT_HTTPHEADER, array('Accept: application/json')); //  Make sure the header is set to accept JSON
	    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
	    curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
	    
	    
	    // FIXME - seeing if this hack will help PAT 404s
	    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, 1);
	    // FIXME - set reasonable timeout
	    
	    // Perform HTTP Basic Authentication if credentials available
	    if ($this->username != '' && $this->password != '')
	    	curl_setopt($ch, CURLOPT_USERPWD, $this->username . ":" . $this->password);
	    
	    // Set for POST (== cURL is weird)
	    if ($method == 'POST')
	    	curl_setopt($ch, CURLOPT_POST, 1);
	    	curl_setopt($ch, CURLOPT_POSTFIELDS, ""); //  Had to set this for PHP's cURL library to play nice
	    	
	    // Set for PUT (== cURL is weird)
	    if ($method == 'PUT')
	    	curl_setopt($ch, CURLOPT_PUT, 1);
	    	
	    // Set for DELETE (== cURL is weird)
	    if ($method == 'DELETE')
	    	curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "DELETE");
	    	
	    // TODO - not sure how to do DELETE with cURL yet
	    
	    $response = array();
	    $response['response_string'] = curl_exec($ch);
	    $response['response_data'] = json_decode($response['response_string']);
	    
	    // Merge in cURL request data
	    $header  = curl_getinfo( $ch );
	    $response = array_merge($response, $header);
	    
	    if (curl_errno($ch) != 0) {
	    	$response['data'] = array();
	    }
	    
	    return $response;
	}	
}

function JSONresponse($HTTP_code, $message) {
	// Lookup appropriate HTTP response
	$status_codes = array(
		200=>"OK",	
		401=>"Unauthorized",
		403=>"Forbidden",
		404=>"Not Found",
		405=>"Method Not Allowed",
		500=>"Internal Server Error"		
	);
	
	// Override inputs with Internal Server Error if invalid code used
	if (! in_array($HTTP_code, array_keys($status_codes))) {
		$HTTP_code = 500;
		$message = "Internal server error (Unkown status code: {$HTTP_code}).";
	}

	// TODO - if message == '' then message = default message for status codes
	
	// Send appropriate headers
	if ($HTTP_code == 401)
		header('WWW-Authenticate: Basic realm="PAT"');
	header("HTTP/1.0 $HTTP_code {$status_codes[$HTTP_code]}");
	
	// Send the JSON response to the browser
	$output = array( 'message'=>$message );
	echo json_encode($output);
	exit(0);
}