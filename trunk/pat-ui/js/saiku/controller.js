/*
 * This is the controller for PATui. It will lazy load and then initialize the model and the view.
 */
    
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
    //if (console != 'undefined') {
     //   console.log(msg);
    //}
}

var controller = {
    errors: 0,
	
    server_error: function() {
        // Restart the process over after a delay
        view.logout();
        view.processing("Could not connect to server, trying again...");
        if (controller.errors > 5) {
            view.free();
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
    lazy_load("js/saiku/view.js");
    lazy_load("js/saiku/model.js");
});