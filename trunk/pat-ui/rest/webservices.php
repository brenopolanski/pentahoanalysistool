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
	 */
	private function request($method, $url, $data) {
		$headers = array(
        'Accept: application/json',
        'Content-Type: application/json',
    	);
    	
	    $ch = curl_init();
	    curl_setopt($ch, CURLOPT_URL, $url);
	    curl_setopt($ch, CURLOPT_HTTPHEADER, array('Accept: application/json')); //  Make sure the header is set to accept JSON
	    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
	    curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
	    
	    // Perform HTTP Basic Authentication if credentials available
	    if ($this->username != '' && $this->password != '')
	    	curl_setopt($ch, CURLOPT_USERPWD, $this->username . ":" . $this->password);
	    
	    // Post for POST (== cURL is weird)
	    if ($method = 'POST')
	    	curl_setopt($ch, CURLOPT_POST, 1);
	    	
	    // TODO - not sure how to do PUT or DELETE with cURL yet
	    	
	    curl_setopt($ch, CURLOPT_POSTFIELDS, ""); //  Had to set this for PHP's cURL library to play nice
	    
	    $response = array();
	    $response['string'] = curl_exec($ch);
	    $response['data'] = json_decode($response['string']);
	    
	    if (curl_errno($ch) != 0) {
	    	$response['data'] = array();
	    }
	    
	    return $response;
	}
	
	/*
	 * GET request
	 */
	public function get($url, $data=array()) {
		return $this->request('GET', $url, $data);
	}
	
	/*
	 * POST request
	 */
	public function post($url, $data=array()) {
		return $this->request('POST', $url, $data);
	}
	
	/*
	 * TODO - PUT request
	 */
	public function put($url, $data=array()) {
		
	}
	
	/*
	 * TODO - DELETE request
	 */
	public function delete($url) {
		
	}
	
}

function JSONresponse($HTTP_code, $message) {
	// Lookup appropriate HTTP response
	$status_codes = array(
		200=>"OK",	
		401=>"Unauthorized",
		404=>"Not Found",
		500=>"Internal Server Error"		
	);
	
	// Override inputs with Internal Server Error if invalid code used
	if (! isset($status_codes[$HTTP_code])) {
		$HTTP_code = 500;
		$message = "Internal server error.";
	}	
	
	// Send appropriate headers
	if ($HTTP_code == 401)
		header('WWW-Authenticate: Basic realm="PAT"');
	header("HTTP/1.0 $HTTP_code {$status_codes['$HTTP_code']}");
	
	// Send the JSON response to the browser
	$output = array( 'message'=>$message );
	echo json_encode($output);
	exit(0);
}