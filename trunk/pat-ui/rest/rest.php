<?php 

/*
 * This is the (static) class that will handle the sessions and REST calls
 */
class Rest {
	
	/*
	 * Any global settings should be stored here
	 */
	private $settings = array(
		'url'=>'http://demo.analytical-labs.com/rest/'
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
	private $session_id;
	
	/*
	 * Url segments from requested url
	 */
	private $url_segments = array();
	
	/*
	 * Connection data returned by session creation
	 */
	private $connections;
	
	/*
	 * TODO - This function will route any GET calls.
	 * Reflection will be used to load the appropriate method
	 * based on URI.
	 */
	public function GET($matches) {								
		// TODO - call 
		$this->start_session();
	}
	
	/*
	 * TODO - This function will route any POST calls.
	 * Reflection will be used to load the appropriate method
	 * based on URI.
	 */
	public function POST($matches) {
		// TODO - call 
		$this->start_session();
	}
	
	/*
	 * This method logs url segments
	 */
	private function get_segments() {
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
		
		// Get url segments
		$this->get_segments();
		
		// Set class variables for username and password from session
		if (isset($_SESSION['username']) && isset($_SESSION['password'])) {
			$this->username = $_SESSION['username'];
			$this->password = $_SESSION['password'];
		
		// If there is no session, see if a new session is trying to be created
		} else if (isset($_POST['username']) && isset($_POST['password'])) {
			// See if session is trying to be created
		    $this->username = $_POST['username'];
    		$this->password = $_POST['password'];
			
		// If unsuccessful, return Permission Denied 
		} else {
			header("HTTP/1.0 403 Forbidden");
			$output = array( 'error'=>'No credentials supplied. Session may be expired.' );
			die(json_encode($output)); // I've always loved the sweet justice of this function
		}
		
		// Create a new client
		$this->client = new WebServices($this->username, $this->password);		
		
		// Create a session on the PAT server
		$this->create_session();
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
		
		$response = $this->client->post("/", $credentials);
		
		// No data returned, something got screwed up
		if (! isset($reponse['data']) || empty($reponse['data'])) {
			return false;
		}
		
		$this->session_id = $reponse['data']->{'@sessionid'};
		$this->connections = $reponse['data'];
		
		return true;
	}
	
}