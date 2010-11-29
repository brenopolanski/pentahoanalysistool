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
        
        $.ajax({
            type: parameters.method,
            url: "/pat-ui/fixtures/rest/" + parameters.url,
            //url: "rest/" + parameters.url, CHANGE
            dataType: 'json',
            username: model.username,
            password: model.password,
            success: parameters.success,
            error: parameters.error
        });
    },

    /** Handle all errors which occur with the server. */
    server_error: function() {
        $('<div id="dialog" class="dialog hide">').appendTo('body');
        $('#dialog').append('<div class="dialog_inner">' +
            '<div class="dialog_header">' +
            '<h3>Error</h3>' +
            '<a href="#" title="Close" class="close_dialog close">Close</a>' +
            '<div class="clear"></div>' +
            '</div>' +
            '<div class="dialog_body_error">Could not connect to the server.</div>' +
            '<div class="dialog_footer calign"><input type="button" class="close" value="&nbsp;OK&nbsp;" />' +
            '</div>' +
            '</div>');
        $('#dialog').modal({
            opacity : 100,
            overlayCss : {
                background : 'white'
            },
            onClose : function () {
                $.modal.close();
                $('#dialog').remove();
                controller.logout();
            }
        });
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
            method: "POST",
            url: model.username + "/session",
            success: function(data, textStatus, XMLHttpRequest) {
                model.session_id = data['@sessionid'];
                model.connections = data;
                view.draw_ui();
                controller.add_tab();
            }
        });
    },

    /**
     * Populate the dimension and measure tree and initialise draggable,
     * droppable and sortable items.
     * @param tab_index {Integer} Index of the selected tab.
     */
    new_query : function(tab_index) {

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

        // Get a list of available dimensions and measures.
        model.request({
            method : "POST",
            url : model.username + "/query/" + data['schema'] + "/" + data['cube'] + "/newquery",
            success : function(data, textStatus, XMLHttpRequest) {
                view.start_waiting('Loading...');
                
                view.load_dimensions($tab, data.axis.dimensions);
                view.load_measures($tab, data.axis.dimensions);

                /** Activate hide and show on trees. */
                // Hide all sub items from the populated tree.
                $tab.find('.root').children('ul').children('li').hide();
                /** When the root item is clicked show it's children. */
                $tab.find('.root').click(function() {
                    $(this).children("ul").children('li').toggle();
                    if($(this).hasClass('expand')) {
                        $(this).removeClass('expand').addClass('collapsed')
                        .find('a.folder_expand')
                        .removeClass('folder_expand')
                        .addClass('folder_collapsed');
                    }else{
                        $(this).removeClass('collapsed').addClass('expand')
                        .find('a.folder_collapsed')
                        .removeClass('folder_collapsed')
                        .addClass('folder_expand');
                    }
                    return false;
                });

                /** Enable dragging, dropping and sorting. */
                // Enable dragging
                $both_trees = $tab.find('.dimension_tree, .measure_tree');
                $both_trees.find("li ul li a").draggable({
                    cancel : ".not-draggable",
                    helper : "clone",
                    opacity : 0.70,
                    start : function(event, ui) {
                        if(ui.helper.hasClass('dimension')) {
                            ui.helper.addClass('dimension_dragging');
                        }else if(ui.helper.hasClass('measure')) {
                            ui.helper.addClass('measure_dragging');
                        }
                    }
                });
                
                // Enable droppable
                $both_dropzones = view.tabs.tabs[tab_index].content.find('.rows ul, .columns ul');
                $row_dropzone = view.tabs.tabs[tab_index].content.find('.rows ul');
                $column_dropzone = view.tabs.tabs[tab_index].content.find('.columns ul');

                $both_dropzones.droppable({
                    accept : '.ui-draggable',
                    activeClass : "notice",
                    hoverClass : "success",
                    drop : function(event, ui) {
                        // Find the dropped item's parent_id.
                        var parent_id = ui.helper.attr('rel').split('_')[0];
                        /**
                         * If the dropped item's parent id already exists in the row or column axis
                         * then prevent the drop from occuring.
                         */
                        if($both_dropzones.find('[rel=' + parent_id + ']').length > 0) {
                            view.show_dialog('Incompatible Items', 'You can only have one unique member within the same hierarchy on the bolumn and row axis at once.', 'error');
                        }else{
                            // Hide the placeholder.
                            $(this).find('.placeholder').hide();
                            // If a dimension has been dropped.
                            if(ui.helper.hasClass('dimension')){
                                $(this).append('<li class="dimension_dropped"><a href="#" title="'+ui.helper.attr('title')+'" rel="' + parent_id + '">'+ui.draggable.text()+'</a></li>');
                            }else{
                                // If a measure has been dropped.
                                $(this).append('<li class="measure_dropped"><a href="#" title="'+ui.helper.attr('title')+'">'+ui.draggable.text()+'</a></li>');
                            }
                        }
                        tab_index = view.tabs.index_from_content($(this).parent().parent().parent().parent().parent("div.tab"));
                    }
                // Enable sortable
                }).sortable({
                    cancel : 'placeholder',
                    connectWith : $both_dropzones,
                    placeholder : 'empty_placeholder',
                    forcePlaceholderSize : true,
                    stop : function() {
                        // Show placeholders
                        if($row_dropzone.find('.dimension_dropped, .measure_dropped').length == 0) {
                            $row_dropzone.find('.placeholder').show();
                        }
                        if($column_dropzone.find('.dimension_dropped, .measure_dropped').length == 0) {
                            $column_dropzone.find('.placeholder').show();
                        }
                    }
                });

                // Enable droppable
                $('.sidebar').droppable({
                    accept : ".rows li, .columns li",
                    drop : function(event, ui) {
                        ui.draggable.remove();
                        // Using setTimeout due to IE7+ bug with jQuery UI
                        setTimeout(function() {
                            ui.draggable.remove();
                        } , 1);
                        // Show placeholders
                        if($row_dropzone.find('.dimension_dropped, .measure_dropped').length == 0) {
                            $row_dropzone.find('.placeholder').show();
                        }
                        if($column_dropzone.find('.dimension_dropped, .measure_dropped').length == 0) {
                            $column_dropzone.find('.placeholder').show();
                        }
                    }
                });
                
                view.stop_waiting();
            },
            error: function() {
                view.show_dialog("Error", "Couldn't create a new query. Please try again.");
            }
        });
    },

    /**
     * Check if the dropped item (dimnesion or measure) follows the rules.
     * Drag and Drop rules
     *   - Unique items on all axis
     *   - You can only drag one item from a level onto an axis
     *   - Measures must be grouped
     * @param axis {Object} Axis where the object has been dropped.
     */
    is_valid_drop : function() {}
}