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

    /** Connection information for this Saiku server. */
    connections : {},

    /**
     * Handle all AJAX requests.
     * @param paramters {Object} Parameters for AJAX requests.
     */
    request : function(parameters) {
        // Overwrite defaults with incoming parameters
        settings = $.extend({
            method: "GET",
            data: {},
            success: function() {},
            error: function() {
                view.show_dialog('Error',
                    'Could not connect to the server, please check your internet connection. ' +
                    'If this problem persists, please refresh the page.', 'error');
            },
            dataType: "json"
        }, parameters);
        
        // Make ajax request
        $.ajax({
            type: settings.method,
            cache: false,
            url: TOMCAT_WEBAPP + REST_MOUNT_POINT + encodeURI(parameters.url),
            dataType: settings.dataType,
            username: model.username,
            password: model.password,
            success: settings.success,
            error: settings.error,
            data: settings.data
        });
    },

    /** 
     * Create a new session and unhide the UI. 
     */
    get_session : function() {
        model.request({
            method: "GET",
            url: model.username + "/discover",
            success: function(data, textStatus, XMLHttpRequest) {
                model.connections = data;
                view.draw_ui();
                controller.add_tab();
                view.hide_processing();
            }
        });
    },
    
    /**
     * Delete a query
     * @param tab_index The tab containing the query
     */
    delete_query: function(tab_index) {
        if (view.tabs.tabs[tab_index].data['query_name'] !== undefined) {
            model.request({
                method: "DELETE",
                url: model.username + "/query/" + view.tabs.tabs[tab_index].data['query_name'] + "/"
            });
        }
    },
    
    /**
     * Generate a new query_id
     * @param tab_index {Integer} Index of the selected tab.
     * @return A new unique query_id
     */
    generate_query_id: function(tab_index) {
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
    new_query: function(tab_index, xml, callback) {
        // If query already exists, delete it
        if (typeof view.tabs.tabs[tab_index].data['query_name'] != "undefined") {
            model.delete_query(tab_index);
        }
    	
        // Generate the temporary query name
        model.generate_query_id(tab_index);

        // Reference for the selected tabs content.
        var $tab = view.tabs.tabs[tab_index].content;

        view.show_processing('Preparing workspace. Please wait...', true, tab_index);
        
        if (xml) {
            // Open existing query
            var post_data = {
                'xml': xml
            };
            
            // FIXME - get connection data from opened query
        } else {
            // Create new query
            // Find the selected cube.
            var $cube = view.tabs.tabs[tab_index].content.find(".cubes option:selected");

            // Check if the cube is valid if so then display an error.
            var cube_data = view.tabs.tabs[tab_index].data['navigation'][$cube.attr('value')];
            if (typeof cube_data == "undefined") {
                view.show_dialog('Error', 'There was an error loading that cube.<br/>Please close the tab and try again.', 'error');
                return;
            }
            
            var post_data = {
                'connection': cube_data['connectionName'],
                'cube': cube_data['cube'],
                'catalog': cube_data['catalogName'],
                'schema': cube_data['schema']
            };
            
            view.tabs.tabs[tab_index].data['connection'] = post_data;
        }

        // Get a list of available dimensions and measures.
        model.request({
            method : "POST",
            url : model.username + "/query/" + view.tabs.tabs[tab_index].data['query_name'] + "/",
            
            data: post_data,
            
            success: function(data, textStatus, XMLHttpRequest) {
                // Get cube data
                var cube = data.cube;
                
                // Load dimensions into a tree.
                model.request({
                    method : "GET",
                    url : model.username + "/discover/" + cube.connectionName + "/" +
                    cube.catalogName + "/" + cube.schemaName + "/" + cube.name + "/dimensions",
                    success: function(data, textStatus, XMLHttpRequest) {
                        view.load_dimensions(tab_index, data);
                		
                        // Load measures into a tree.
                        model.request({
                            method : "GET",
                            url : model.username + "/discover/" + cube.connectionName + "/" +
                            cube.catalogName + "/" + cube.schemaName + "/" + cube.name + "/measures",
                            success: function(data, textStatus, XMLHttpRequest) {
                                view.load_measures(tab_index, data);
                                if (callback) {
                                    callback(cube);
                                }
                            },
                            error: function() {
                                view.hide_processing(true, tab_index);
                                view.show_dialog("Error", "Couldn't fetch dimensions. Please try again.", "error");
                                $tab.find('.cubes').find('option:first').attr('selected', 'selected');
                            }
                        });
                    },
                    error: function() {
                        view.hide_processing(true, tab_index);
                        view.show_dialog("Error", "Couldn't fetch dimensions. Please try again.", "error");
                        $tab.find('.cubes').find('option:first').attr('selected', 'selected');
                    }
                });
                
                view.hide_processing(true, tab_index);
            },
            
            error: function() {
                // Could not retrieve dimensions and measures from server
                view.hide_processing(true, tab_index);
                view.show_dialog("Error", "Couldn't create a new query. Please try again.", "error");
                $tab.find('.cubes').find('option:first').attr('selected', 'selected');
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
        var $tab = $item.closest('.tab');
        var tab_index = view.tabs.index_from_content($tab);
        var $column_dropzone = $tab.find('.columns ul');
        var $row_dropzone = $tab.find('.rows ul');
        var $both_dropzones = $tab.find('.rows ul, .columns ul');

        /** Has the dimension or measure been double clicked on. */
        if (is_click) {
            if ($item.find('a').hasClass('dimension')) {
                var axis = 'ROWS';
            }else if ($item.find('a').hasClass('measure')){
                var axis = 'COLUMNS';
            }
        }else{
            var axis = $item.closest('.fields_list').attr('title');
        }

        /** Sorting a dimension or measure. */
        var is_dimension = $item.find('a').hasClass('dimension');
        var is_measure = $item.find('a').hasClass('measure');

        /** If sorting on a ROW axis and is a dimension. */
        if (axis === 'ROWS' && is_dimension) {
            var position = $row_dropzone.find('li').index($item), memberposition = -1;
        /** If sorting on a COLUMN axis and is a dimension. */
        } else if (axis === 'COLUMNS' && is_dimension) {
            var position = $column_dropzone.find('li').index($item), memberposition = -1;
        /** If sorting on a ROW axis and is a measure. */
        } else if (axis === 'ROWS' && is_measure) {
            var memberposition = $both_dropzones.find('li.d_measure').index($item);
            var position = $row_dropzone.find('li').index($both_dropzones.find('li.d_measure:first'));
        /** If sorting on a COLUMN axis and is a measure. */
        } else if (axis === 'COLUMNS' && is_measure) {
            var memberposition = $both_dropzones.find('li.d_measure').index($item);
            var position = $column_dropzone.find('li').index($both_dropzones.find('li.d_measure:first'));
        }

        if (is_dimension) {
            // This is a dimension
            var item_data = view.tabs.tabs[tab_index].data['dimensions'][$item.attr('title')];
            var url = model.username + "/query/" + view.tabs.tabs[tab_index].data['query_name'] + "/axis/" + axis + "/dimension/" + item_data.dimension
            + "/hierarchy/" + item_data.hierarchy + "/" + item_data.level;
        } else if (is_measure) {
            // This is a measure
            var item_data = view.tabs.tabs[tab_index].data['measures'][$item.attr('title')];
            var url = model.username + "/query/" + view.tabs.tabs[tab_index].data['query_name'] + "/axis/" + axis + "/dimension/Measures/member/" + item_data.measure;
        }
        
        // Notify server of change
        model.request({
            method: "POST",
            url: url,
            data: {
                'position' : position,
                'memberposition' : memberposition
            },
            success: function(data, textStatus, XMLHttpRequest) {
                // If automatic query execution is enabled, rerun the query when this option is changed
                if (view.tabs.tabs[tab_index].data['options']['automatic_execution']) {
                    model.run_query(tab_index);
                }
            }

        });
        
    },

    /**
     * When a dimension or measure is removed, remove it from the selection.
     * @param $item {Object}
     * @param is_click {Boolean} If the dimension or measure is being removed via a double click.
     */
    removed_item: function($item, is_click) {
        var tab_index = view.tabs.index_from_content($item.closest('.tab'));
        var axis = $item.closest('.fields_list').attr('title');
        
        if ($item.find('a').hasClass('dimension')) {
            // This is a dimension
            var item_data = view.tabs.tabs[tab_index].data['dimensions'][$item.attr('title')];
            var url = model.username + "/query/" + view.tabs.tabs[tab_index].data['query_name'] + "/axis/" + axis + "/dimension/" + item_data.dimension
            + "/hierarchy/" + item_data.hierarchy + "/" + item_data.level;
        } else if ($item.find('a').hasClass('measure')) {
            // This is a measure
            var item_data = view.tabs.tabs[tab_index].data['measures'][$item.attr('title')];
            var url = model.username + "/query/" + view.tabs.tabs[tab_index].data['query_name'] + "/axis/" + axis + "/dimension/Measures/member/" + item_data.measure;
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

        var col_counter = view.tabs.tabs[tab_index].content.find('.columns ul li').length;
        var row_counter = view.tabs.tabs[tab_index].content.find('.rows ul li').length;
        
        // Abort if one axis or the other is empty
        if (col_counter == 0 || row_counter == 0)
            return;
        
        // Notify the user...
        view.show_processing('Executing query. Please wait...', true, tab_index);

        // Set up a pointer to the result area of the active tab.
        var $workspace_result = view.tabs.tabs[tab_index].content.find('.workspace_results');
        
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

                // Close the table
                table_vis = table_vis + '</table>';
              
                // Insert the table to the DOM
                $workspace_result.html(table_vis);

                // Enable highlighting on rows.
                $workspace_result.find('table tr').hover(function(){
                    $(this).children().css('background', '#eff4fc');
                },function(){
                    $(this).children().css('background', '');
                });
                
                // Resize the workspace
                view.resize_height(tab_index);
                
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

        var $button = view.tabs.tabs[tab_index].content.find('a[title="Non-empty"]');
        if (view.tabs.tabs[tab_index].data['options']['nonempty']) {
            view.tabs.tabs[tab_index].data['options']['nonempty'] = false;
            $button.removeClass('on');
        } else {
            view.tabs.tabs[tab_index].data['options']['nonempty'] = true;
            $button.addClass('on');
        }
    	
        url = model.username + "/query/" + view.tabs.tabs[tab_index].data['query_name'] + "/properties/saiku.olap.query.nonempty";
    	
        // Notify server of change
        model.request({
            method: "POST",
            url: url,
            data: {
                'propertyValue' : view.tabs.tabs[tab_index].data['options']['nonempty']
            },
            success: function() {
                // If automatic query execution is enabled, rerun the query when this option is changed
                if (view.tabs.tabs[tab_index].data['options']['automatic_execution']) {
                    model.run_query(tab_index);
                }
            }
        });

        view.hide_processing(true, tab_index);
    },
    
    /**
     * Enable or disable automatic query execution
     * @param tab_index {Integer} The active tab index
     */
    automatic_execution: function(tab_index) {

        view.show_processing('Setting automatic execution. Please wait...', true, tab_index);

        var $button = view.tabs.tabs[tab_index].content.find('a[title="Automatic execution"]');
        if (view.tabs.tabs[tab_index].data['options']['automatic_execution']) {
            view.tabs.tabs[tab_index].data['options']['automatic_execution'] = false;
            $button.removeClass('on');
        } else {
            view.tabs.tabs[tab_index].data['options']['automatic_execution'] = true;
            $button.addClass('on');
        }

        view.hide_processing(true, tab_index);
    },

    /**
     * Show or hide the fields list
     * @param tab_index {Integer} The active tab index
     */
    toggle_fields: function(tax_index) {

        var $button = view.tabs.tabs[tab_index].content.find('a[title="Toggle fields"]');
        if (view.tabs.tabs[tab_index].data['options']['toggle_fields']) {
            view.tabs.tabs[tab_index].data['options']['toggle_fields'] = false;
            $button.removeClass('on');
            view.tabs.tabs[tab_index].content.find('.workspace_fields').show();
            view.resize_height(tab_index);
        } else {
            view.tabs.tabs[tab_index].data['options']['toggle_fields'] = true;
            $button.addClass('on');
            view.tabs.tabs[tab_index].content.find('.workspace_fields').hide();
            view.resize_height(tab_index);
        }
    },

    /**
     * Swap axis
     * @param tab_index {Integer} The active tab index
     */
    swap_axis: function(tab_index) {

        view.show_processing('Swapping axis. Please wait...', true, tab_index);

        // Swap the actual selections
        var $rows = view.tabs.tabs[tab_index].content.find('.rows li');
        var $columns = view.tabs.tabs[tab_index].content.find('.columns li');
    	
        $rows.detach().appendTo(view.tabs.tabs[tab_index].content.find('.columns ul'));
        $columns.detach().appendTo(view.tabs.tabs[tab_index].content.find('.rows ul'));
    	
        var url = model.username + "/query/" + view.tabs.tabs[tab_index].data['query_name'] + "/properties/saiku.olap.query.swap.axis";
    	
        // Notify server of change
        model.request({
            method: "POST",
            url: url,
            data: {
                'propertyValue' : true
            },
            success: function() {
                // If automatic query execution is enabled, rerun the query when this option is changed
                if (view.tabs.tabs[tab_index].data['options']['automatic_execution']) {
                    model.run_query(tab_index);
                }
            }
        });

        view.hide_processing(true, tab_index);
    },

    /**
     * Export data as Excel XML
     * @param tab_index {Integer} The active tab index
     */
    export_xls: function(tab_index) {
        
        window.location = TOMCAT_WEBAPP + REST_MOUNT_POINT + model.username + "/query/"
        + view.tabs.tabs[tab_index].data['query_name'] + "/export/xls";
            
    },

    /**
     * Export data as CSV
     * @param tab_index {Integer} The active tab index
     */
    export_csv: function(tab_index) {

        window.location = TOMCAT_WEBAPP + REST_MOUNT_POINT + model.username + "/query/"
        + view.tabs.tabs[tab_index].data['query_name'] + "/export/csv";

    },

    /**
     * Save the query
     * @param tab_index {Integer} The active tab index
     */
    save_query: function(tab_index) {
        // Append a dialog <div/> to the body.
        $('<div id="dialog" class="dialog hide" />').appendTo('body');
        // Load the view into the dialog <div/> and disable caching.
        $.ajax({
            url : BASE_URL + 'views/queries/save.html',
            cache : false,
            dataType : "html",
            success : function(data) {
                $('#dialog').html(data).modal({
                    opacity : 100,
                    onShow : function(dialog) {
                        dialog.data.find('#query_name').val($('#header').find('.selected').find('a').html());
                        dialog.data.find('#save_query').click(function() {
                            if(dialog.data.find('#query_name').val().length == 0) {
                                dialog.data.find('.error_msg').html('You need to specify a name for your query.');
                            }else{
                                var query_name = dialog.data.find('#query_name').val();

                                var url = model.username + "/repository/" + view.tabs.tabs[tab_index].data['query_name'];

                                model.request({
                                    method: "POST",
                                    url: url,
                                    data: {
                                        'newname' : encodeURI(query_name)
                                    },
                                    success : function(){
                                        // Change the tab title
                                        $('#header').find('.selected').find('a').html(query_name);
                                        // Change the dialog message
                                        $('#dialog').find('.dialog_body_save').text('').text('Query saved succesfully.');
                                        // Remove the dialog save button
                                        $('#save_query').remove();
                                        // Rename the cancel button
                                        $('#dialog').find('.close').val('Ok');
                                    }
                                });
                            }
                        });
                    },
                    onClose : function (dialog) {
                        // Remove all simple modal objects.
                        dialog.data.remove();
                        dialog.container.remove();
                        dialog.overlay.remove();
                        $.modal.close();
                        // Remove the #dialog which we appended to the body.
                        $('#dialog').remove();
                    }
                });
            }
        });
    },
    
    /**
     * Open a query
     * @param query_name The name of the query
     * @param tab_index The tab to load it into
     */
    open_query: function(query_name, tab_index) {
        // Change tab title
        view.tabs.tabs[tab_index].tab.find('a').text(query_name);
        
        //TODO - request selections and adjust UI accordingly
        model.request({
            url: model.username + "/repository/" + query_name,
            dataType: 'xml',
            success: function(data, textStatus, jqXHR) {
                // Create a new query in the workspace
                model.new_query(tab_index, jqXHR.responseText, function(cube) {
                    // Select cube in menu
                    var selected_cube = cube.name;
                    $cubes = view.tabs.tabs[tab_index].content.find('.cubes');
                    $cubes.val($cubes.find('option:[text="' + selected_cube + '"]').val()); 
                    
                // TODO - Move selections to axes
                //$.each(data.saikuAxes, function(selection_iterator, selection) {
                //
                //});
                    
                // TODO - Retrieve properties for this query
                });
            }
        });
    },
    
    /**
     * Delete a query from the repository
     */
    delete_query_from_repository: function(query_name, tab_index) {
        if (confirm("Are you sure?")) {
            model.request({
                method: "DELETE",
                url: model.username + "/repository/" + query_name + "/",
                success: function(data, textStatus, XMLHttpRequest) {
                    view.tabs.tabs[tab_index].content.find(".workspace_results").empty();
                    model.get_queries(tab_index);
                }
            });
        }
    },

    /**
     * Get a list of queries
     * @param tab_index {Integer} The active tab index
     */
    get_queries: function(tab_index) {
        model.request({
            method: "GET",
            url: model.username + "/repository/",
            success: function(data, textStatus, XMLHttpRequest) {
                view.load_queries(tab_index, data);
            }
        });
    },

    /**
     * Show and populate the selection dialog
     * @param tab_index {Integer} The active tab index
     */
    show_selections: function(member_clicked, $tab) {
        var tab_index = view.tabs.index_from_content($tab);
        var member_data = view.tabs.tabs[tab_index].data['dimensions'][member_clicked.parent().attr('title')];
        var tab_data = view.tabs.tabs[tab_index].data['connection'];
        
        // Append a dialog <div/> to the body.
        $('<div id="dialog" class="dialog hide" />').appendTo('body');
        
        // Load the view into the dialog <div/> and disable caching.
        $.ajax({
            url : BASE_URL + 'views/selections/',
            cache : false,
            dataType : "html",
            success : function(data) {
                $('#dialog').html(data).modal({
                    onShow: function() {
                        $('.selection_tree').html('Insert tree here...');

                        /**
                         * Step 1.
                         * Populate the thin version of the tree.
                         */

                        
                        url = model.username + '/discover/' + tab_data.connection + "/" + tab_data.catalog + "/" + tab_data.schema + "/" + tab_data.cube + "/dimensions/" + member_data.dimension;
                        model.request({
                            method: "GET",
                            url: url,
                            success: function(data, textStatus, jqXHR) {
                                // Loop through data
                                //$.each(data,function(index, item) {
                                    // Append to a var the html string
                                //});
                                
                                // Append the var to the DOM
                                $('.selection_tree').html(jqXHR.responseText);
                            }
                        });                          

                         /*
                          * Step 2.
                          * Activate a lazy load method over the thin tree.
                          */

                         /*
                          * The method will be most likely in the view so you can
                          * call it by:
                          *
                          * view.lazy_selection_tree();
                          *
                          * lazy_selection_tree: function(tab_index) {
                          *
                          *     // Add a listener to clicking on root lists and call
                          *     // the REST api to send back the correct list element
                          *     // and loop through results like above.
                          *
                          *     $('.selection_tree li.root').click(function() {
                          *
                          *         model.request({
                          *             url: model.username + '/discover/' + url
                          *             success: function(data) {
                          *                 $.each(data, function(index, item) {
                          *                     // Append the new list
                          *                 });
                          *             }
                          *         })
                          *
                          *     });
                          *
                          * }
                          *
                          */
                    }
                });

            },
            error: function(data) {

            }
        });
    }
};