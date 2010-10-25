/*
 * This is the controller for PATui. It will lazy load and then initialize the model and the view.
 */

/*
 * The extent of our globalization at this point, I'm guessing
 */
var PAT_TITLE       =   "<strong>PAT<em>ui</em> Demo</strong>";
var LOADING_DATA    =   "";
var NO_DIMENSIONS   =   "No cube selected";
var NO_MEASURES     =   "No cube selected";
var BASE_URL		=	""; // Same domain
    
/*
 * Lazy load javascript files
 */
function lazy_load(script_filename) {
    var html_doc = document.getElementsByTagName('head').item(0);
    var js = document.createElement('script');
    js.setAttribute('language', 'javascript');
    js.setAttribute('type', 'text/javascript');
    js.setAttribute('src', script_filename);
    html_doc.appendChild(js);
    return false;
}

/*
 * Debug 
 * TODO - I would remove this in the production code
 */
function debug(msg) {
    //if (debug_enabled && console != 'undefined') {
    if (console != 'undefined') {
        console.log(msg);
    }
}

var controller = {
	errors: 0,
	
    server_error: function() {
        // Rstart the process over after a delay
        view.processing("Could not connect to server. Retrying in 10 seconds...");
        if (controller.errors > 5) {
        	view.processing("Could not connect to server. Giving up.");
        } else {
	        setTimeout(function() {
	        	controller.errors++;
	        	view.free();
	        	model.init();
	        }, 10000);
        }
    },
    
    click_handler: function($button) {
    	var handler = $button.attr("id");
    	var success = false;
    	for (method in model) {
    		if (method == handler) {
    			eval("model." + method + "();");
    			success = true;
    		}
    	}
    	
    	if (success == false)
    		debug("This button handler isn't implemented yet.");
    }
};

/*
 * Lazy load view and model and initialize session
 */
$(document).ready(function() {
	lazy_load("/js/patui/view.js");
	lazy_load("/js/patui/model.js");
});