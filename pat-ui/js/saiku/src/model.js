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
    
    /** BASE_URL of saiku server. */
    BASE_URL : "pat-ui",

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
            // FIXME - should be rest/ instead of fixtures/rest/
            url: BASE_URL + "fixtures/rest/" + parameters.url,
            dataType: 'json',
            username: model.username,
            password: model.password,
            success: parameters.success,
            error: parameters.error
        });
    },

    /** Handle all errors which occur with the server. */
    server_error : function() {
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
        $.modal.close();
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
                /** Load dimensions into a tree. */
                view.load_dimensions($tab, data.axis.dimensions);
                /** Load measures into a tree. */
                view.load_measures($tab, data.axis.dimensions);

                /** Activate hide and show on trees. */
                $tab.find('.root').parent().children('ul').children('li').hide();
                /** When the root item is clicked show it's children. */
                $tab.find('.root').click(function() {
                    $(this).parent().children("ul").children('li').toggle();
                    if ($(this).parent().hasClass('expand')) {
                        $(this).parent().removeClass('expand').addClass('collapsed')
                        .find('a.folder_expand')
                        .removeClass('folder_expand')
                        .addClass('folder_collapsed');
                    }else{
                        $(this).parent().removeClass('collapsed').addClass('expand')
                        .find('a.folder_collapsed')
                        .removeClass('folder_collapsed')
                        .addClass('folder_expand');
                    }
                    return false;
                });

                /** Lookups. */
                $both_trees = $tab.find('.dimension_tree, .measure_tree');
                $both_dropzones = view.tabs.tabs[tab_index].content.find('.rows ul, .columns ul');
                $row_dropzone = view.tabs.tabs[tab_index].content.find('.rows ul');
                $column_dropzone = view.tabs.tabs[tab_index].content.find('.columns ul');
                $measure_tree = $tab.find('.measure_tree li ul li');
                $dimension_tree = $tab.find('.dimension_tree li ul li');
                $dimensions_dropzone = view.tabs.tabs[tab_index].content.find('.dimensions_group');
                $measures_dropzone = view.tabs.tabs[tab_index].content.find('.measures_group');

                /** Dimensions. */

                /** Enable dragging on the dimension tree. */
                $dimension_tree.draggable({
                    cancel : ".not-draggable",
                    helper : "clone",
                    opacity : 0.60,
                    /** Connect to the dimension dropzone. */
                    connectToSortable : $dimensions_dropzone,
                    cursorAt : {
                        top : 8,
                        right : 40
                    }
                }).disableSelection();


                /** Make the dimension lists sortable and connectable. */
                $dimensions_dropzone.sortable({
                    cancel : 'placeholder_dimension',
                    placeholder : 'placeholder_dimension_sort',
                    items : 'li:not(.placeholder_dimension)',
                    connectWith : $dimensions_dropzone,
                    opacity: 0.60,
                    distance: 30,
                    cursorAt : {
                        top : 8,
                        right : 40
                    },

                /**
                 * This event is triggered when sorting starts (belongs to jQuery UI).
                 * @param event {Object} jQuery UI object.
                 * @param ui {Object} jQuery UI object.
                 **/
                    start : function(event, ui) {
                        /** When the sorting starts make sure the placeholder resembles the sorted item. */
                        ui.placeholder.text(ui.helper.text());

                    },

                /**
                 * This event is triggered when a sortable item has been dragged out
                 * from the list and into another (belongs to jQuery UI).
                 * @param event {Object} jQuery UI object.
                 * @param ui {Object} jQuery UI object.
                 */
                    remove : function(event, ui) {
                        /** Id of the item being sorted. */
                        var dimension_id = ui.item.find('a').attr('rel').split('_')[0];
                        /** Remove the not-draggable class and add ui-draggable to all the sorted items sibilings. */
                        $dimension_tree.parent().parent().parent().find('[rel=' + dimension_id + ']').parent().children().children()
                        .removeClass('not-draggable').addClass('ui-draggable');
                    },

                /**
                 * This event is triggered when sorting stops, but when the placeholder/helper
                 * is still available (belongs to jQuery UI).
                 * @param event {Object} jQuery UI object.
                 * @param ui {Object} jQuery UI object.
                 */

                    beforeStop : function(event, ui) {
                        /** If the item is not being 'dropped/removed' onto the .sidebar. */
                        if(!(ui.item.hasClass('dropped'))) {
                            /** Id of the removed item. */
                            var dimension_id = ui.item.find('a').attr('rel').split('_')[0];
                            /** Add the not-draggable class and add ui-draggable to all the sorted items sibilings. */
                            $dimension_tree.parent().parent().parent().find('[rel=' + dimension_id + ']').parent().children().children()
                            .removeClass('ui-draggable').addClass('not-draggable');
                            ui.item.addClass('dropped_dimension');
                        }
                    },
                    
                    /**
                     * This event is triggered when sorting has stopped (belongs to jQuery UI).
                     * @param event {Object} jQuery UI object.
                     * @param ui {Object} jQuery UI object.
                     */
                    stop : function(event, ui) {
                        /** Check if the placeholder should be visible. */
                        if ($row_dropzone.find('.dropped_dimension').length == 0) {
                            $row_dropzone.find('.placeholder_dimension').show();
                        }else{
                            $row_dropzone.find('.placeholder_dimension').hide();
                        }
                        if ($column_dropzone.find('.dropped_dimension').length == 0){
                            $column_dropzone.find('.placeholder_dimension').show();
                        }else{
                            $column_dropzone.find('.placeholder_dimension').hide();
                        }
                    }
                }).disableSelection();

                /** Measures. */
                
                /** Enable dragging on the dimension tree. */
                $measure_tree.draggable({
                    cancel : ".not-draggable",
                    helper : "clone",
                    opacity : 0.60,
                    tolerance : "pointer",
                    /** Connect to the dimension dropzone. */
                    connectToSortable : $measures_dropzone,
                    cursorAt : {
                        top : 7,
                        left : 5
                    }
                }).disableSelection();


                /** Make the dimension lists sortable and connectable. */
                $measures_dropzone.sortable({
                    cancel : '.placeholder_measure',
                    placeholder : 'placeholder_measure_sort',
                    items : 'li:not(.placeholder_measure)',
                    connectWith : $measures_dropzone,
                    tolerance : "pointer",
                    opacity: 0.60,
                    cursorAt : {
                        top : 7,
                        left : 5
                    },

                /**
                 * This event is triggered when sorting starts (belongs to jQuery UI).
                 * @param event {Object} jQuery UI object.
                 * @param ui {Object} jQuery UI object.
                 **/
                    start : function(event, ui) {
                        /** When the sorting starts make sure the placeholder resembles the sorted item. */
                        ui.placeholder.text(ui.helper.text());
                    },

                    /**
                 * This event is triggered when a sortable item has been dragged out
                 * from the list and into another (belongs to jQuery UI).
                 * @param event {Object} jQuery UI object.
                 * @param ui {Object} jQuery UI object.
                 */
                    remove : function(event, ui) {
                        /** Id of the item being sorted. */
                        var measure_id = ui.item.find('a').attr('rel');
                        /** Remove the not-draggable class and add ui-draggable to the measure item. */
                        $measure_tree.parent().parent().parent().find('[rel=' + measure_id + ']').parent()
                        .removeClass('not-draggable').addClass('ui-draggable');
                    },

                    /**
                 * This event is triggered when sorting stops, but when the placeholder/helper
                 * is still available (belongs to jQuery UI).
                 * @param event {Object} jQuery UI object.
                 * @param ui {Object} jQuery UI object.
                 */

                    beforeStop : function(event, ui) {
                        /** If the item is not being 'dropped/removed' onto the .sidebar. */
                        if(!(ui.item.hasClass('dropped'))) {
                            /** Id of the measure item. */
                            var measure_id = ui.item.find('a').attr('rel');
                            /** If this is the first measure being dropped. */
                            if ($measures_dropzone.find('.dropped_measure').length == 0) {
                                /** Add the not-draggable class and remove ui-draggable to the measure item in the measures tree. */
                                $measure_tree.parent().parent().parent().find('[rel=' + measure_id + ']').parent()
                                .removeClass('ui-draggable').addClass('not-draggable');
                                // Style the measure item.
                                ui.item.addClass('dropped_measure');
                            }else{
                                /** Setup sorting from pointers. */
                                var drag_to = ui.helper.parent().parent().attr('class').split(' ')[1],
                                drag_from = $(this).parent().attr('class').split(' ')[1];
                                /** If sorting between lists only. */
                                if (drag_to === 'rows' && drag_from === 'columns' || drag_to === 'columns' && drag_from === 'rows') {
                                    /** If sorting to an axis which has no measures then bring along all other measures. */
                                    if ($('.'+drag_to).find('.dropped_measure').length - 1 == 0) {
                                        /** Find all other measures and append them to the new axis. */
                                        $measures_dropzone.find('.dropped_measure').appendTo($('.'+drag_to+' .measures_group'));
                                    }
                                }else{
                                    /** If sorting to a list which contains no measures. */
                                    if ($('.'+drag_from).find('.dropped_measure').length == 0) {
                                        /** Add the not-draggable class and remove ui-draggable to the measure item in the measures tree. */
                                        $measure_tree.parent().parent().parent().find('[rel=' + measure_id + ']').parent()
                                        .removeClass('ui-draggable').addClass('not-draggable');
                                        /** Find all other measures and append them to the new axis. */
                                        $measures_dropzone.find('.dropped_measure').appendTo($('.'+drag_from+' .measures_group'));
                                        // Style the measure item.
                                        ui.item.addClass('dropped_measure');
                                    }else{
                                        /** If sorting from the tree to a list which already contains measures. */
                                        $measure_tree.parent().parent().parent().find('[rel=' + measure_id + ']')
                                        .parent().removeClass('ui-draggable').addClass('not-draggable');
                                        // Style the measure item.
                                        ui.item.addClass('dropped_measure');
                                    }
                                }
                            }
                        }
                    },

                    /**
                     * This event is triggered when sorting has stopped (belongs to jQuery UI).
                     * @param event {Object} jQuery UI object.
                     * @param ui {Object} jQuery UI object.
                     */
                    stop : function(event, ui) {
                        /** Check if the placeholder should be visible. */
                        if ($row_dropzone.find('.dropped_measure').length == 0) {
                            $row_dropzone.find('.placeholder_measure').show();
                        }else{
                            $row_dropzone.find('.placeholder_measure').hide();
                        }
                        if ($column_dropzone.find('.dropped_measure').length == 0){
                            $column_dropzone.find('.placeholder_measure').show();
                        }else{
                            $column_dropzone.find('.placeholder_measure').hide();
                        }
                    }
                }).disableSelection();

                /**
                 * Make the sidebar a droppable area for items.
                 */
                $('.sidebar').droppable({
                    accept : ".rows li, .columns li",

                    /**
                     * This event is triggered when an accepted draggable is dropped 'over'
                     * (within the tolerance of) this droppable (this belongs to jQuery UI).
                     * @param event {Object} jQuery UI object.
                     * @param ui {Object} jQuery UI object.
                     */
                    drop : function(event, ui) {
                        /** Add a class to the to be dropped item so that the sortable beforeStop event can detect the item being removed. */
                        ui.draggable.addClass('dropped');
                        /** Remove the draggable item. */
                        ui.draggable.remove();
                        /** Using setTimeout due to IE7+ bug with jQuery UI. */
                        setTimeout(function() {
                            ui.draggable.remove();
                        } , 1);
                    }
                });
            },
            error: function() {
                view.show_dialog("Error", "Couldn't create a new query. Please try again.", "error");
            }
        });
    }
    
}