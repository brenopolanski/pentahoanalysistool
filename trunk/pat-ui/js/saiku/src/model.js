/**
 * @fileOverview    This represents the model for Saiku UI.
 * @description     This will handle all interaction to Saiku's REST API.
 * @version         1.0.0
 */

/**
 * Model class
 * @class
 */
var model = {

    /** Username to be used with BASIC_AUTH. */
    username : "",

    /** Password to be used with BASIC_AUTH. */
    password : "",

    /** session_id used to make calls to the server. */
    session_id : "",

    /** Connection information for this Saiku server. */
    connections : {},

    /**
     * Handle all AJAX requests.
     * @param paramters {Object} Parameters for AJAX requests.
     */
    request : function(parameters) {
        if (typeof parameters.method == "undefined")
            parameters.method = "GET";
        if (typeof parameters.data == "undefined")
            parameters.data = {};
        if (typeof parameters.success == "undefined")
            parameters.success = function() {};
        if (typeof parameters.error == "undefined")
            parameters.error = model.server_error;
        if (typeof parameters.dataType == "undefined")
            parameters.dataType = 'json';
        
        $.ajax({
            type: parameters.method,
            url: BASE_URL + TOMCAT_WEBAPP + REST_MOUNT_POINT + parameters.url,
            dataType: parameters.dataType,
            username: model.username,
            password: model.password,
            success: parameters.success,
            error: parameters.error,
            data: parameters.data
        });
    },

    /** Handle all errors which occur with the server. */
    server_error : function() {
        view.show_dialog('Error', 'Could not connect to the server, please refresh the page.', 'error');
    /*
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
         */
    },

    /** Get the sessionid and based on the username and unhide the UI. */
    get_session : function() {
        model.request({
            method: "GET",
            url: "session",
            dataType: "html",
            success: function(data, textStatus, XMLHttpRequest) {
                model.session_id = data;
                model.request({
                    method: "GET",
                    url: model.username + "/datasources",
                    success: function(data, textStatus, XMLHttpRequest) {
                        model.connections = data;
                        $('.pre_waiting').remove();
                        view.draw_ui();
                        controller.add_tab();
                    }
                });
            }
        });
    },
    
    /**
     * Delete old query and create new
     */
    new_query_id: function(tab_index) {
    	if (typeof view.tabs.tabs[tab_index].data['query_name'] != "undefined") {
    		model.request({
    			method: "DELETE",
    			url: model.username + "/query/" + view.tabs.tabs[tab_index].data['query_name'] + "/"
    		});
    	}
    	
    	view.tabs.tabs[tab_index].data['query_name'] = 'xxxxxxxx-xxxx-xxxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
    	    var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
    	    return v.toString(16);
    	}).toUpperCase();
    },

    /**
     * Populate the dimension and measure tree and initialise draggable,
     * droppable and sortable items.
     * @param tab_index {Integer} Index of the selected tab.
     */
    new_query : function(tab_index) {
    	
    	/** Generate the temporary query name */
    	model.new_query_id(tab_index);

        /** Find the selected cube. */
        $cube = view.tabs.tabs[tab_index].content.find(".cubes option:selected");

        /** Check if the cube is valid if so then display an error. */
        data = view.tabs.tabs[tab_index].data['navigation'][$cube.attr('value')];
        if (typeof data == "undefined") {
            view.show_dialog('Error', 'There was an error loading that cube.<br/>Please close the tab and try again.', 'error')
            return;
        }

        // Reference for the selected tabs content.
        $tab = view.tabs.tabs[tab_index].content;

        view.start_waiting('Preparing workspace...');

        // Get a list of available dimensions and measures.
        model.request({
            method : "POST",
            url : model.username + "/query/" + view.tabs.tabs[tab_index].data['query_name'] + "/",
            data: {
                'connection': data['connectionName'],
                'cube': data['cube'],
                'catalog': data['catalogName'],
                'schema': data['schema']
            },
            success: function(data, textStatus, XMLHttpRequest) {
                /** Load dimensions into a tree. */
                view.load_dimensions($tab, data.axes[0].dimensions);
                /** Load measures into a tree. */
                view.load_measures($tab, data.axes[0].dimensions, "/query/" + view.tabs.tabs[tab_index].data['query_name'] + "/axis/UNUSED/dimension/Measures/hierarchy/Measures/MeasuresLevel");
            },
            error: function() {
                view.stop_waiting();
                view.show_dialog("Error", "Couldn't create a new query. Please try again.", "error");
                $('.cubes').find('option:first').attr('selected', 'selected');
            }
        });        
    }
};