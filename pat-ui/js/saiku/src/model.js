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
                    'Could not connect to the server, please check your internet connection. ' +
                    'If this problem persists, please refresh the page.', 'error');
            };
        if (typeof parameters.dataType == "undefined")
            parameters.dataType = 'json';
        
        $.ajax({
            type: parameters.method,
            cache: false,
            /* This is used for local development. */
            url: BASE_URL + TOMCAT_WEBAPP + REST_MOUNT_POINT + encodeURI(parameters.url),
            //url: BASE_URL + TOMCAT_WEBAPP + REST_MOUNT_POINT + parameters.url,
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
            url: model.username + "/datasources",
            success: function(data, textStatus, XMLHttpRequest) {
                model.connections = data;
                view.draw_ui();
                controller.add_tab();
                view.hide_processing();
            }
        });
    /* }
        }); */
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

        view.show_processing('Preparing workspace. Please wait...', true, tab_index);

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
            
            success: function(responsedata, textStatus, XMLHttpRequest) {
                // Load dimensions into a tree.
                view.load_dimensions(tab_index, responsedata.axes[0].dimensions);
                // Load measures into a tree.
                view.load_measures(tab_index, responsedata.axes[0].dimensions, 
                    "/datasources/" + data['connectionName'] + "/" + data['catalogName'] + "/" +
                    data['schema'] + "/" + data['cube'] + "/measures"); 
                view.hide_processing(true, tab_index);
            },
            
            error: function() {
                // Could not retrieve dimensions and measures from server
                view.hide_processing(true, tab_index);
                view.show_dialog("Error", "Couldn't create a new query. Please try again.", "error");
                $('.cubes').find('option:first').attr('selected', 'selected');
            }
        });        
    },

    /**
     * When a dimension or measure is dropped or sorted set it as a selection.
     * @param $item {Object} 
     * @param is_click {Boolean} If the dimension or measure is being added via a double click.
     * @param position {Integer} The position of the dimension or measure being dropped.
     */
    dropped_item: function($item, is_click, pos) {
        tab_index = view.tabs.index_from_content($item.closest('.tab'));

        /** Has the dimension or measure been double clicked on. */
        if (is_click) {
            if ($item.find('a').hasClass('dimension')) {
                axis = 'ROWS';
            }else if ($item.find('a').hasClass('measure')){
                axis = 'COLUMNS';
            }
        }else{
            axis = $item.closest('.fields_list').attr('title');
        }

        /** Sorting a dimension or measure. */
        var is_dimension = $item.find('a').hasClass('dimension'),
        is_measure = $item.find('a').hasClass('measure');

        /** If sorting on a ROW axis and is a dimension. */
        if (axis === 'ROWS' && is_dimension) {
            var position = $row_dropzone.find('li').index($item), memberposition = -1;
        /** If sorting on a COLUMN axis and is a dimension. */
        } else if (axis === 'COLUMNS' && is_dimension) {
            var position = $column_dropzone.find('li').index($item), memberposition = -1;
        /** If sorting on a ROW axis and is a measure. */
        } else if (axis === 'ROWS' && is_measure) {
            var memberposition = $both_dropzones.find('li.d_measure').index($item),
            position = $row_dropzone.find('li').index($both_dropzones.find('li.d_measure:first'));
        /** If sorting on a COLUMN axis and is a measure. */
        } else if (axis === 'COLUMNS' && is_measure) {
            var memberposition = $both_dropzones.find('li.d_measure').index($item),
            position = $column_dropzone.find('li').index($both_dropzones.find('li.d_measure:first'));
        }

        if (is_dimension) {
            // This is a dimension
            item_data = view.tabs.tabs[tab_index].data['dimensions'][$item.attr('title')];
            url = model.username + "/query/" + view.tabs.tabs[tab_index].data['query_name'] + "/axis/" + axis + "/dimension/" + item_data.dimension
            + "/hierarchy/" + item_data.hierarchy + "/" + item_data.level;
        } else if (is_measure) {
            // This is a measure
            item_data = view.tabs.tabs[tab_index].data['measures'][$item.attr('title')];
            url = model.username + "/query/" + view.tabs.tabs[tab_index].data['query_name'] + "/axis/" + axis + "/dimension/Measures/member/" + item_data.measure;
        }
        
        // Notify server of change
        model.request({
            method: "POST",
            url: url,
            data: {
                'position' : position,
                'memberposition' : memberposition
            }
        });
        
        // If automatic query execution is enabled, rerun the query after making change
        if (view.tabs.tabs[tab_index].data['options']['automatic_execution']) {
            model.run_query(tab_index);
        }
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
        
        // If automatic query execution is enabled, rerun the query after making change
        if (view.tabs.tabs[tab_index].data['options']['automatic_execution']) {
            model.run_query(tab_index);
        }
    },
    
    /**
     * Run the query (triggered by drag events, double click events, and button
     * @param tab_index {Integer} the id of the tab
     */
    run_query: function(tab_index) {

        // Notify the user...
        view.show_processing('Executing query. Please wait...', true, tab_index);

        // Make sure that a cube has been selected on this tab
        if (! view.tabs.tabs[tab_index].data['query_name']) {
            view.show_dialog("Run query", "Please select a cube first.", "info");
            return false;
        }
        // Set up a pointer to the result area of the active tab.
        $workspace_result = view.tabs.tabs[tab_index].content.find('.workspace_results');

        var col_counter = view.tabs.tabs[tab_index].content.find('.columns ul li').length;
        var row_counter = view.tabs.tabs[tab_index].content.find('.rows ul li').length;

        // Fetch the resultset from the server
        model.request({
            method: "GET",
            url: model.username + "/query/" + view.tabs.tabs[tab_index].data['query_name'] + "/result/",
            success: function(data, textStatus, XMLHttpRequest) {
                
                // Create a variable to store the table
                var table_vis = '<table>';

                // Start looping through the result set
                $.each(data, function(i, cells) {

                    // Add a new row.
                    table_vis = table_vis + '<tr>';

                    // Look through the contents of the row
                    $.each(cells, function(j, header) {

                        // If the cell is a column header and is null (top left of table)
                        if(header['type'] === "COLUMN_HEADER"
                            && header['value'] === "null") {
                            table_vis = table_vis + '<th class="all_null"><div>&nbsp;</div></th>';
                        } // If the cell is a column header and isn't null (column header of table)
                        else if(header['type'] === "COLUMN_HEADER") {
                            table_vis = table_vis + '<th class="col"><div>'+header['value']+'</div></th>';
                        } // If the cell is a row header and is null (grouped row header)
                        else if(header['type'] === "ROW_HEADER"
                            && header['value'] === "null") {
                            table_vis = table_vis + '<th class="row_null"><div>&nbsp;</div></th>';
                        } // If the cell is a row header and isn't null (last row header)
                        else if(header['type'] === "ROW_HEADER") {
                            table_vis = table_vis + '<th class="row"><div>'+header['value']+'</div></th>';
                        } // If the cell is a normal data cell
                        else if(header['type'] === "DATA_CELL") {
                            table_vis = table_vis + '<td class="data"><div>'+header['value']+'</div></td>';
                        }
                        
                    });

                    // Close of the new row
                    table_vis = table_vis + '</tr>';

                });

                // Resize the workspace
                view.resize_height();

                // Close the table
                table_vis = table_vis + '</table>';
              
                // Insert the table to the DOM
                $workspace_result.html(table_vis);

                // Loop through all headers and find the largest one.
                var max_width = 0;
                $.each($workspace_result.find('table th'), function(i, j){

                    var width = $(this).width();
                    if(width > max_width) {
                        max_width = width;
                    }

                });

                // Make sure all <div/>'s are all equal.
                $workspace_result.find('table div').css('width', max_width);

                // Enable highlighting on rows.
                $workspace_result.find('table tr').hover(function(){
                    $(this).children().css('background', '#eff4fc');
                },function(){
                    $(this).children().css('background', '');
                });

                // Clear the wait message
                view.hide_processing(true, tab_index);
            },
            
            error: function() {
                // Let the user know that their query was not successful
                view.hide_processing(true, tab_index);
                view.show_dialog("Result Set", "There was an error getting the result set for that query.", "info");
            }
        });
    },

    /**
     * Display the MDX for the active tab query.
     * @param tab_index {Integer} The active tab index.
     */
    show_mdx: function(tab_index) {

        // Fetch the MDX from the server
        model.request({
            method: "GET",
            dataType: 'html',
            url: model.username + "/query/" + view.tabs.tabs[tab_index].data['query_name'] + "/mdx",
            success: function(data, textStatus, XMLHttpRequest) {
                // Let the user know that their query was not successful
                view.show_dialog("MDX", data, "mdx");
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                // Let the user know that their query was not successful
                view.show_dialog("MDX", "There was an error getting the MDX for that query.", "info");
            }
        });

    },
    
    /**
     * Enable or disable NON EMPTY
     * @param tab_index {Integer} The active tab index
     */
    non_empty: function(tab_index) {

        view.show_processing('Setting Non-empty. Please wait...', true, tab_index);

        $button = view.tabs.tabs[tab_index].content.find('a[title="Non-empty"]');
        if (view.tabs.tabs[tab_index].data['options']['nonempty']) {
            view.tabs.tabs[tab_index].data['options']['nonempty'] = false;
            $button.removeClass('button_toggle_on').addClass('button_toggle_off');
        } else {
            view.tabs.tabs[tab_index].data['options']['nonempty'] = true;
            $button.addClass('button_toggle_on').removeClass('button_toggle_off');
        }
    	
        url = model.username + "/query/" + view.tabs.tabs[tab_index].data['query_name'] + "/properties/saiku.olap.query.nonempty";
    	
        // Notify server of change
        model.request({
            method: "POST",
            url: url,
            data: {
                'propertyValue' : view.tabs.tabs[tab_index].data['options']['nonempty']
            }
        });
        
        // If automatic query execution is enabled, rerun the query when this option is changed
        if (view.tabs.tabs[tab_index].data['options']['automatic_execution']) {
            model.run_query(tab_index);
        }

        view.hide_processing(true, tab_index);
    },
    
    /**
     * Enable or disable automatic query execution
     * @param tab_index {Integer} The active tab index
     */
    automatic_execution: function(tab_index) {

        view.show_processing('Setting automatic execution. Please wait...', true, tab_index);

        $button = view.tabs.tabs[tab_index].content.find('a[title="Automatic execution"]');
        if (view.tabs.tabs[tab_index].data['options']['automatic_execution']) {
            view.tabs.tabs[tab_index].data['options']['automatic_execution'] = false;
            $button.removeClass('button_toggle_on').addClass('button_toggle_off');
        } else {
            view.tabs.tabs[tab_index].data['options']['automatic_execution'] = true;
            $button.addClass('button_toggle_on').removeClass('button_toggle_off');
        }

        view.hide_processing(true, tab_index);
    },
    
    /**
     * Swap axis
     * @param tab_index {Integer} The active tab index
     */
    swap_axis: function(tab_index) {

        view.show_processing('Swapping axis. Please wait...', true, tab_index);

        // Swap the actual selections
        $rows = view.tabs.tabs[tab_index].content.find('.rows li');
        $columns = view.tabs.tabs[tab_index].content.find('.columns li');
    	
        $rows.detach().appendTo(view.tabs.tabs[tab_index].content.find('.columns ul'));
        $columns.detach().appendTo(view.tabs.tabs[tab_index].content.find('.rows ul'));
    	
        url = model.username + "/query/" + view.tabs.tabs[tab_index].data['query_name'] + "/properties/saiku.olap.query.swap.axis";
    	
        // Notify server of change
        model.request({
            method: "POST",
            url: url,
            data: {
                'propertyValue' : true
            }
        });
        
        // If automatic query execution is enabled, rerun the query when this option is changed
        if (view.tabs.tabs[tab_index].data['options']['automatic_execution']) {
            model.run_query(tab_index);
        }

        view.hide_processing(true, tab_index);
    },

    /**
     * Export data as Excel XML
     * @param tab_index {Integer} The active tab index
     */
    export_data: function(tab_index) {
        view.show_view('views/queries/export.html', function() {
            types = ['csv', 'xls'];
            $.each(types, function(index, type) {
                $('<a />')
                .attr({
                    'href': BASE_URL + TOMCAT_WEBAPP + REST_MOUNT_POINT + model.username + "/query/"
                    + view.tabs.tabs[tab_index].data['query_name'] + "/export/" + type
                })
                .text(type.toUpperCase() + " format")
                .appendTo($('.export_data'));
                $('<br />').appendTo($('.export_data'));
            });
        });
    },

    /**
     * Save query
     * @param tab_index {Integer} The active tab index
     * @param query_name {string} The query name
     */
    save_query: function(tab_index, query_name) {

    // Save with the new query name
    // If success
    //  Change the title of tab
    //  Confirm successful save

    }
};