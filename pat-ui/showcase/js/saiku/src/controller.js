/**
 * @fileOverview    This represents the controller for Saiku UI.
 * @description     This will lazy load and init the model and view.
 * @version         1.0.0
 */

/**
 * Lazy load JavaScript files.
 * @param script_filename {String} The name of the file to lazy load.
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
 * Handle log messages.
 * @param message {String} The message to output.
 */
function debug(message) {
    if(enable_debug == true && console != undefined) {
        console.log(message);
    }
}

/**
 * Controller class
 * @class
 */
var controller = {
    
    /**
     * Handle all clicks events on the toolbar.
     * @param $button {Object} The clicked toolbar button object.
     */
    toolbar_click_handler : function($button) {
        var handler = $button.attr("id");
        var success = false;
        for (method in controller) {
            if (method == handler) {
                eval("controller." + method + "();");
                success = true;
            }
        }
        if (success == false)
            debug("This button handler isn't implemented yet.");
    },

    /** Handle click when the new query button is clicked. */
    new_query : function () {},

    /** Handle click when the open query button is clicked. */
    open_query : function () {},

    /** Handle click when the save query button is clicked. */
    save_query : function () {},

    /** Handle click when the delete query button is clicked. */
    delete_query : function () {},

    /** Handle click when the save logout button is clicked. */
    logout : function () {
        model.username = "";
        model.password = "";
        view.destroy_ui();
        location.reload(true);
    },

    /** Handle click when the about button is clicked. */
    about : function() {
        view.show_dialog('../views/info/');
    }
    
}

/** Lazy load the view and model. */
$(document).ready(function() {
    lazy_load("js/saiku/src/view.js");
    lazy_load("js/saiku/src/model.js");
});