/**
 * @fileOverview    This represents the controller for Saiku UI.
 * @description     This will lazy load and init the model and view.
 * @version         1.0.0
 */

/**
 * Global variables that control where REST API calls are sent
 */

	// The mount point for Tomcat (when using mod_jk)
	BASE_URL = "/";
	
	// The name of the Saiku server webapp in Tomcat
	TOMCAT_WEBAPP = "saiku/";
	
	// The preferred REST mountpoint for Enunciate
	REST_MOUNT_POINT = "rest/saiku/";

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
        method_name = $button.attr("id");
        controller.call_method(method_name, -1);
    },
    
    /**
     * Handle all click events on workspace toolbar
     * @param $target {Object} button which was pressed
     */
    workspace_toolbar_click_handler: function($button) {
    	tab_index = view.tabs.index_from_content($button.closest('.tab'));
    	method_name = $button.text().replace(" ", "_").toLowerCase();
    	controller.call_method(method_name, tab_index);
    },
    
    /** Allows the use of reflection to call methods */
    call_method: function(method_name, tab_index) {
        for (method in controller) {
            if (method == method_name) {
            	if (tab_index == -1) {
            		eval("controller." + method + "();");
            	} else {
            		eval("controller." + method + "(" + tab_index + ");");
            	}
            }
        }
    },
    
    /** Run query **/
    run_query: function(tab_index) {
    	model.run_query(tab_index);
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
        location.reload(true);
    },

    /** Handle click when the about button is clicked. */
    about : function() {
        view.show_view('views/info/');
    }
    
};

/** Lazy load the rest of the javascript. */
$(document).ready(function() {
	$.getScript("js/saiku/src/tabs.js", function() {
		$.getScript("js/saiku/src/view.js");
	});
	$.getScript("js/saiku/src/model.js");
});