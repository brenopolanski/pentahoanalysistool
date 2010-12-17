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
            parameters.error = function() {
        		view.show_dialog('Error', 
        				'Could not connect to the server, please check your internet connection.' +
        				'If this problem persists, please refresh the page.', 'error');
        	};
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
     * Delete old query and create new.
     * @param tab_index {Integer} Index of the selected tab.
     */
    new_query_id: function(tab_index) {
        if (typeof view.tabs.tabs[tab_index].data['query_name'] != "undefined") {
            model.request({
                method: "DELETE",
                url: model.username + "/query/" + view.tabs.tabs[tab_index].data['query_name'] + "/"
            });
        }
    	
        view.tabs.tabs[tab_index].data['query_name'] = 
        	'xxxxxxxx-xxxx-xxxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
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
    	
        // Generate the temporary query name
        model.new_query_id(tab_index);

        // Find the selected cube.
        $cube = view.tabs.tabs[tab_index].content.find(".cubes option:selected");

        // Check if the cube is valid if so then display an error.
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
                // Load dimensions into a tree.
                view.load_dimensions(tab_index, data.axes[0].dimensions);
                // Load measures into a tree.
                view.load_measures(tab_index, data.axes[0].dimensions, 
                		"/query/" + view.tabs.tabs[tab_index].data['query_name'] + 
                		"/axis/UNUSED/dimension/Measures/hierarchy/Measures/MeasuresLevel");
            },
            
            error: function() {
            	// Could not retrieve dimensions and measures from server
                view.stop_waiting();
                view.show_dialog("Error", "Couldn't create a new query. Please try again.", "error");
                $('.cubes').find('option:first').attr('selected', 'selected');
            }
        });        
    },

    /**
     * When a dimension or measure is dropped or sorted set it as a selection.
     * @param $item {Object} 
     * @param is_click {Boolean} If the dimension or measure is being added via a double click.
     */
    dropped_item: function($item, is_click) {
        tab_index = view.tabs.index_from_content($item.closest('.tab'));
        
        if (is_click) {
            if ($item.find('a').hasClass('dimension')) {
                axis = 'ROWS';
            }else if ($item.find('a').hasClass('measure')){
                axis = 'COLUMNS';
            }
        }else{
            axis = $item.closest('.fields_list').attr('title');
        }

        if ($item.find('a').hasClass('dimension')) {
            // This is a dimension
            item_data = view.tabs.tabs[tab_index].data['dimensions'][$item.attr('title')];
            url = model.username + "/query/" + view.tabs.tabs[tab_index].data['query_name'] + "/axis/" + axis + "/dimension/" + item_data.dimension
            + "/hierarchy/" + item_data.hierarchy + "/" + item_data.level;
        } else if ($item.find('a').hasClass('measure')) {
            // This is a measure
            item_data = view.tabs.tabs[tab_index].data['measures'][$item.attr('title')];
            url = model.username + "/query/" + view.tabs.tabs[tab_index].data['query_name'] + "/axis/" + axis + "/dimension/Measures/member/" + item_data.measure;
        }
        
        // Notify server of change
        model.request({
            method: "POST",
            url: url
        });
    },

    /**
     * When a dimension or measure is removed, remove it from the selection.
     * @param $item {Object}
     * @param is_click {Boolean} If the dimension or measure is being removed via a double click.
     */
    removed_item: function($item, is_click) {
        tab_index = view.tabs.index_from_content($item.closest('.tab'));
        axis = $item.closest('.fields_list').attr('title');
        
        if ($item.find('a').hasClass('dimension')) {
            // This is a dimension
            item_data = view.tabs.tabs[tab_index].data['dimensions'][$item.attr('title')];
            url = model.username + "/query/" + view.tabs.tabs[tab_index].data['query_name'] + "/axis/" + axis + "/dimension/" + item_data.dimension
            + "/hierarchy/" + item_data.hierarchy + "/" + item_data.level;
        } else if ($item.find('a').hasClass('measure')) {
            // This is a measure
            item_data = view.tabs.tabs[tab_index].data['measures'][$item.attr('title')];
            url = model.username + "/query/" + view.tabs.tabs[tab_index].data['query_name'] + "/axis/" + axis + "/dimension/Measures/member/" + item_data.measure;
        }
        
        // Notify server of change
        model.request({
            method: "DELETE",
            url: url
        });
    },
    
    /**
     * Run the query (triggered by drag events, double click events, and button
     * @param tab_index {Integer} the id of the tab
     */
    run_query: function(tab_index) {
    	// Make sure that a cube has been selected on this tab
        if (! view.tabs.tabs[tab_index].data['query_name']) {
            view.show_dialog("Run query", "Please select a cube first.", "info");
            return false;
        }
        
        // Let the users know this might take a while
        view.start_waiting('Getting results...');
        
        // Fetch the resultset from the server
        model.request({
            method: "GET",
            url: model.username + "/query/" + view.tabs.tabs[tab_index].data['query_name'] + "/result/",
            success: function(data, textStatus, XMLHttpRequest) {
                // Set up a pointer to the result area of the active tab.
                $workspace_result = view.tabs.tabs[tab_index].content.find('.workspace_results');
                
                // Create the table visualisation structure.
                $workspace_result.html('<table><thead></thead><tbody></tbody></table>');
                
                // Setup a pointer to the table.
                $table_vis = $workspace_result.find('table');

                // Loop through the resultset.
                $.each(data, function(i, cells) {

                	// Fill in headers
                    $table_vis.find('thead').append('<tr id="' + i + '" />');
                    $.each(cells, function(j, header) {
                        if (header['type'] === 'COLUMN_HEADER' && header['value'] === "null") {
                            $table_vis.find('thead tr#' + i).append('<th class="null"></th>');
                        }else if(header['type'] === 'COLUMN_HEADER') {
                            $table_vis.find('thead tr#' + i).append('<th class="col_hd">' + header['value'] + '</th>');
                        }
                    });
                    
                    // Fill in data
                    $table_vis.find('tbody').append('<tr id="' + i + '" />');
                    $.each(cells, function(k, body) {
                        if (body['type'] === 'ROW_HEADER' && body['value'] === "null") {
                            $table_vis.find('tbody tr#' + i).append('<th class="row_hd"></th>');
                        }else if(body['type'] === 'ROW_HEADER') {
                            $table_vis.find('tbody tr#' + i).append('<th class="row_hd">' + body['value'] + '</th>');
                        }else if(body['type'] === 'DATA_CELL') {
                            $table_vis.find('tbody tr#' + i).append('<td class="data_cell">' + body['value'] + '</td>');
                        }
                    });
                });

                // Clear the wait message
                view.stop_waiting();
            },
            
            error: function() {
            	// Let the user know that their query was not successful
                view.show_dialog("Result set", "There was an error getting the result set <br/>for that query.", "error");
                view.stop_waiting();
            }
        });
    }
};