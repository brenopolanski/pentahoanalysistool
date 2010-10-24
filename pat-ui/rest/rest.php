<?php 

/*
 * This is the (static) class that will handle the sessions and REST calls
 */
class Rest {
	
	/*
	 * Any global settings should be stored here
	 */
	private $settings = array(
		'base_url'=>'http://demo.analytical-labs.com/rest'
	);
	
	/*
	 * Username to be used with HTTP basic auth
	 */
	private $username = '';
	
	/*
	 * Password to be used with HTTP basic auth
	 */
	private $password = '';
	
	/*
	 * Web services wrapper to make requests
	 */
	private $client;
	
	/*
	 * Session ID to be used for REST calls
	 */
	private $session_id = '';
	
	/*
	 * Url segments from requested url
	 */
	private $url_segments = array();
	
	/*
	 * Connection data returned by session creation
	 */
	private $connections;
	
	/*
	 * Constructor - handles sessions and authentication before proxying API calls
	 */
	public function Rest() {
		$this->start_session();
	}
	
	/*
	 * For DRY's sake, we'll handle all requests through here, using a single $method parameter
	 */
	public function request($method, $url) {
		// Make sure people don't try TRACE requests or crap like that
		$valid_methods = array("GET", "POST", "PUT", "DELETE");
		if (! in_array($method, $valid_methods)) {
			JSONresponse(405, "Invalid HTTP method.");
			return false;			
		}
		
		// Call handler dynamically by handle_ + url_segments
		$handler = "handle_" . implode("_", $this->url_segments);
		
		// FIXME - replace all of this with a proxy object
		// FIXME - a switch statement would be ideal here
		// FIXME - fix that trailing slash issue
		
		// /rest destroys session
		if ($url == "/" || $url == "") {
			$this->destroy_session();
			
		// Creating PAT session
		} else if ($url == "/admin/session/" || $url == "/admin/session") {
			$this->handle_admin_session();
			
		// If no matching url handlers are found, throw 404
		} else if (! method_exists($this, $handler)) {
			JSONresponse(404, 'The resource you requested was not found.');
			
		// Call appropriate url handler
		} else {
			call_user_func(array($this, $handler));
		}		
	}
	
	/*
	 * This start the session at the beginning of every request.
	 * Make sure there's no whitespace at the end of index.php
	 * if sessions don't work. Also, libraries should NOT have
	 * closing tags.
	 * 
	 * This is the closest thing we have to a constructor in this
	 * static class.
	 */
	private function start_session() {
		// Start PHP session
		session_start();
		
		// Set class variables for username and password from session
		if (isset($_SESSION['username']) && isset($_SESSION['password'])) {
			$this->username = $_SESSION['username'];
			$this->password = $_SESSION['password'];
			$this->session_id = $_SESSION['pat_session_id'];
			$this->connections = $_SESSION['connections'];
			
			// Create a new client
			$this->client = new WebServices($this->username, $this->password);
		
		// If there is no session, see if a new session is trying to be created
		} else if (isset($_SERVER['PHP_AUTH_USER']) && isset($_SERVER['PHP_AUTH_PW'])) {
			// See if session is trying to be created
		    $this->username = $_SERVER['PHP_AUTH_USER'];
    		$this->password = $_SERVER['PHP_AUTH_PW'];
    		
			// Create a new client
			$this->client = new WebServices($this->username, $this->password);
    		
			// Create a new PAT session
    		$status = $this->create_session();
			
			if ($status) {
				// Credentials good, create PHP session to store PAT session
				$_SESSION['username'] = $this->username;
				$_SESSION['password'] = $this->password;
				$_SESSION['pat_session_id'] = $this->session_id;
				$_SESSION['connections'] = $this->connections;
			} else {
				// Bad credentials, try again
				JSONresponse(401, 'Invalid credentials supplied. Please try again.');
			}
			
		// If unsuccessful, return Permission Denied 
		} else {
			// Not authenticated at all
			session_destroy();
			JSONresponse(401, 'No credentials supplied. Session may be expired.');
		}	

		return true;
	}
	
	/*
	 * Creates a session from a username/password combination
	 */
	private function create_session() {
		$credentials = array(
			'username'=>$this->username,
			'password'=>$this->password
		);
		
		$response = $this->client->post($this->settings['base_url'] . "/admin/session", $credentials);
		
		// No data returned, something got screwed up
		if ($response['string'] == '' || empty($response['data']->{'@sessionid'})) {
			return false;
		}
		
		$this->session_id = $response['data']->{'@sessionid'};
		$this->connections = $response['data'];
		
		return true;
	}
	
	/****************************************************************************
	 * TODO - Build proxy object here. All calls except session calls are forwarded
	 * directly to the server.
	 */
	
	/*
	 * url: /admin/session
	 * description: return session information
	 */
	private function handle_admin_session() {
		header("HTTP/1.0 200 OK");
		die(json_encode($this->connections));
	}
	
	/*
	 * url: /
	 * description: destroys the session in the event that things got b0rked on the UI somehow
	 * 				i.e. bad PAT session
	 */
	private function destroy_session() {
		session_destroy();
		JSONresponse(200, 'Your session has been purged.');
	}
	
	/*
	 * url: all others
	 * description: proxy function for PAT REST API calls
	 */
	private function call_api() { // TODO
		// If cached returned cached output
		// Call API
		// Cache output
		// Send back response, including headers
		// use JSONresponse()
	}
}