/**
 * @fileOverview    This represents the controller for Saiku UI.
 * @description     This will lazy load and init the model and view.
 * @version         1.0.0
 */

/**
 * Handle log messages.
 * @param message {String} The message to output.
 */
function debug(message) {
    if (enable_debug == true && console != 'undefined') {
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
            view.show_dialog("Warning", "This button handler isn't implemented yet.", "error");
    },

    /** Handle click when the new query button is clicked. */
    add_tab : function () {
        view.tabs.add_tab();
    },

    /** Handle click when the open query button is clicked. */
    open_query : function () {},

    /** Handle click when the save query button is clicked. */
    save_query : function () {},

    /** Handle click when the delete query button is clicked. */
    delete_query : function () {},

    /** Handle click when the save logout button is clicked. */
    logout : function () {
        view.destroy_ui();
        model.username = "";
        model.password = "";
        model.session_id = "";
        model.connections = "";
        location.reload(true);
    },

    /** Handle click when the about button is clicked. */
    about : function() {
        view.show_view('views/info/');
    }
    
};

/** Lazy load the view and model. */
$(document).ready(function() {
	// Set base url for all REST calls
	BASE_URL = "/";
	TOMCAT_WEBAPP = "saiku/";
	REST_MOUNT_POINT = "rest/saiku/";

	$.getScript("js/saiku/src/tabs.js", function() {
		$.getScript("js/saiku/src/view.js");
	});
	$.getScript("js/saiku/src/model.js");
});