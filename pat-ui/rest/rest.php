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
	 * This function will route any GET calls.
	 * Reflection will be used to load the appropriate method
	 * based on URI.
	 */
	public function GET($matches) {								
		$this->request('GET', $matches);
	}
	
	/*
	 * This function will route any POST calls.
	 * Reflection will be used to load the appropriate method
	 * based on URI.
	 */
	public function POST($matches) {
		$this->request('POST', $matches);
	}
	
	/*
	 * For DRY's sake, we'll handle all requests through here, using a single $method parameter
	 */
	private function request($method, $matches) {
		$this->get_segments($matches);
		$this->start_session();
		
		// Call handler dynamically by handle_ + url_segments
		// Ex.
		$handler = "handle_" . implode("_", $this->url_segments);
		if (! method_exists($this, $handler)) {
			// FIXME (see index.php:25ish)
	    	header("HTTP/1.0 404 Not Found");
	    	$output = array( 'error'=>'The resource you requested was not found.' );
			die(json_encode($output)); // I've always loved the sweet justice of this function
		}
		
		call_user_func(array($this, $handler));
	}
	
	/*
	 * This method logs url segments
	 */
	private function get_segments($matches) {
		if (isset($matches['route1']))
			$this->url_segments[0] = $matches['route1'];
		if (isset($matches['route2']))
			$this->url_segments[1] = $matches['route2'];
		if (isset($matches['route3']))
			$this->url_segments[2] = $matches['route3'];
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
		
		// FIXME - somewhere in here the session_id needs to be saved to the PHP session
		// Right now we're creating a new session_id for each call to the server
		
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
				header('WWW-Authenticate: Basic realm="PATui"');
				header("HTTP/1.0 401 Unauthorized");
				$output = array( 'error'=>'Invalid credentials supplied. Please try again.' );
				die(json_encode($output)); // I've always loved the sweet justice of this function
			}
			
		// If unsuccessful, return Permission Denied 
		} else {
			// Not authenticated at all
			session_destroy();
			header('WWW-Authenticate: Basic realm="PATui"');
			header("HTTP/1.0 401 Unauthorized");
			$output = array( 'error'=>'No credentials supplied. Session may be expired.' );
			die(json_encode($output)); // I've always loved the sweet justice of this function
		}	

		// FIXME - check to see if session is already created and use that
		return true;
	}
	
	/*
	 * TODO - Creates a session from a username/password combination
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
		
		// FIXME - add this stuff to PHP session if possible
		// FIXME - check if username and password are set in session, and add if not
		
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
}