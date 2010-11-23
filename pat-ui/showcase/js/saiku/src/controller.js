/**
 * @fileOverview    This represents the controller for Saiku UI.
 * @description     It will lazy load and init the model and view.
 * @version         1.0.0
 */

/**
 * Lazy load JavaScript files
 * @param script_filename {String} The name of the file to lazy load
 */
function lazy_load(script_filename) {
    var html_doc = document.getElementsByTagName('head').item(0);
    var js = document.createElement('script');

    js.setAttribute('type', 'text/javascript');
    js.setAttribute('src', script_filename);
    html_doc.appendChild(js);
    
    return false;
}

/**
 * Handle log messages
 * @param message {String} The message to output
 */
function debug(message) {
    /*...*/
}

/**
 * Controller class
 * @class
 */
var controller = {

    click_handler : function($button) {

        

    }

}