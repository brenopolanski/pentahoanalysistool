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
                $tab.find('.root').children('ul').children('li').hide();
                /** When the root item is clicked show it's children. */
                $tab.find('.root').click(function() {
                    $(this).children("ul").children('li').toggle();
                    if ($(this).hasClass('expand')) {
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

                /** Set the getters. */
                $both_trees = $tab.find('.dimension_tree, .measure_tree');
                $both_dropzones = view.tabs.tabs[tab_index].content.find('.rows ul, .columns ul');
                $row_dropzone = view.tabs.tabs[tab_index].content.find('.rows ul');
                $column_dropzone = view.tabs.tabs[tab_index].content.find('.columns ul');

                /** Enable dragging. */
                $both_trees.find("li ul li").draggable({
                    cancel : ".not-draggable_",
                    helper : "clone",
                    opacity : 0.60,
                    connectToSortable : $both_dropzones
                });

                /** Disable selection. */
                $both_trees.find("li ul li").disableSelection();
                

                /**
             * Make both the dropzones sortable and connectable.
             **/
                $both_dropzones.sortable({
                    cancel : 'placeholder, dropped_measure',
                    placeholder : 'placeholder',
                    items : 'li:not(.placeholder)',
                    connectWith : $both_dropzones,
                    opacity: 0.60,
                    distance: 30,

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
                        // If the item being removed is a dimension.
                        if (ui.item.find('a').hasClass('dimension')) {
                            /** Id of the item being sorted. */
                            var dimension_id = ui.item.find('a').attr('rel').split('_')[0];
                            /** Remove the not-draggable class and add ui-draggable to all the sorted items sibilings. */
                            $both_trees.find('[rel=' + dimension_id + ']').parent().children().children()
                            .removeClass('not-draggable').addClass('ui-draggable');
                        }else{
                            // If the item being removed is a measure.
                            /** Id of the item being sorted. */
                            var measure_id = ui.item.find('a').attr('rel');
                            /** Remove the not-draggable class and add ui-draggable to the measure item. */
                            $both_trees.find('[rel=' + measure_id + ']').parent()
                            .removeClass('not-draggable').addClass('ui-draggable');
                        }
                    },

                    /**
                 * This event is triggered when sorting stops, but when the placeholder/helper
                 * is still available (belongs to jQuery UI).
                 * @param event {Object} jQuery UI object.
                 * @param ui {Object} jQuery UI object.
                 */

                    beforeStop : function(event, ui) {
                        if(!(ui.item.hasClass('dropped'))) {
                            /** If the item is not being 'dropped/removed' onto the .sidebar. */
                            /** Check if the item being sorted is invalid by finding if it has a not-draggable class. */
                            if (ui.item.hasClass('not-draggable')) {
                                // Display generic error.
                                view.show_dialog('Incompatible Items', 'That combination of items is not valid with this version of Saiku Server.', 'error');
                                // Remove the ui.item object.
                                ui.item.remove();
                            }else{
                                /** If the item being added/sorted is a dimension. */
                                if (ui.item.find('a').hasClass('dimension')) {
                                    /** Id of the removed item. */
                                    var dimension_id = ui.item.find('a').attr('rel').split('_')[0];
                                    /** Add the not-draggable class and add ui-draggable to all the sorted items sibilings. */
                                    $both_trees.find('[rel=' + dimension_id + ']').parent().children().children()
                                    .removeClass('ui-draggable').addClass('not-draggable');
                                    ui.item.addClass('dropped_dimension');
                                }else{
                                    /** If the item being added/sorted is a measure. */
                                    if ($both_dropzones.find('.dropped_measure').length == 0) {
                                        /** Id of the removed item. */
                                        var measure_id = ui.item.find('a').attr('rel');
                                        /** Add the not-draggable class and add ui-draggable to the measure item. */
                                        $both_trees.find('[rel=' + measure_id + ']').parent()
                                        .removeClass('ui-draggable').addClass('not-draggable');
                                        ui.item.addClass('dropped_measure');
                                    }else{
                                        var drag_to = ui.helper.parent().parent().attr('class').split(' ')[1];
                                        var drag_from   = $(this).parent().attr('class').split(' ')[1];

                                        debug('Dragging to '+drag_to);
                                        debug('Dragging from '+drag_from);

                                        if (drag_to === 'rows' && drag_from === 'columns' || drag_to === 'columns' && drag_from === 'rows')  {
                                        
                                            debug('Dragging b/w lists');
                                            debug('Dragging to a list which has '+$('.' + drag_to).find('.dropped_measure').length);
                                            if($('.'+drag_to).find('.dropped_measure').length - 1 == 0) {
                                                $both_dropzones.find('.dropped_measure').appendTo($('.'+drag_to+' ul'));
                                                $both_trees.find('[rel=' + measure_id + ']').parent()
                                                .removeClass('ui-draggable').addClass('not-draggable');
                                                ui.item.addClass('dropped_measure');
                                            }else{
                                                debug('Say hello to measure, measure.');
                                                $both_trees.find('[rel=' + measure_id + ']').parent()
                                                .removeClass('ui-draggable').addClass('not-draggable');
                                                ui.item.addClass('dropped_measure');
                                            }


                                        }else{
                                            debug('Say hello to measure, measure.');
                                            $both_trees.find('[rel=' + measure_id + ']').parent()
                                            .removeClass('ui-draggable').addClass('not-draggable');
                                            ui.item.addClass('dropped_measure');
                                        }

                                    //                                    if($(this).find('.dropped_measure').length > 0) {
                                    //                                        debug('Say hello to measure, measure.');
                                    //                                        $both_trees.find('[rel=' + measure_id + ']').parent()
                                    //                                        .removeClass('ui-draggable').addClass('not-draggable');
                                    //                                        ui.item.addClass('dropped_measure');
                                    //                                    }else{
                                    //                                        debug('No other measures here, bring your friends!');
                                    //                                        $both_dropzones.find('.dropped_measure').appendTo($(this));
                                    //                                        $both_trees.find('[rel=' + measure_id + ']').parent()
                                    //                                        .removeClass('ui-draggable').addClass('not-draggable');
                                    //                                        ui.item.addClass('dropped_measure');
                                    //                                    }

                                    
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
                        if ($row_dropzone.find('.dropped_dimension, .dropped_measure').length == 0) {
                            $row_dropzone.find('.placeholder').show();
                        }else{
                            $row_dropzone.find('.placeholder').hide();
                        }
                        if ($column_dropzone.find('.dropped_dimension, .dropped_measure').length == 0){
                            $column_dropzone.find('.placeholder').show();
                        }else{
                            $column_dropzone.find('.placeholder').hide();
                        }
                    }
                    

                //                        // If the item being sorted is a dimension.
                //                        if (ui.item.find('a').hasClass('dimension')) {
                //                            debug('~~~');
                //                            debug('Sorting a dimension');
                //                            debug('There are '+$both_dropzones.find('[rel=' + parent_id + ']').length+' other dimensions with the same parent_id');
                //                            if($both_dropzones.find('[rel=' + parent_id + ']').length > 0) {
                //                                debug('You can not drop that dimension as either it already exists or a member from the same level already exists on the row or column axis.');
                //                                debug('Remove the ui.item instance');
                //                                ui.item.remove();
                //                                debug('~~~');
                //                            }else{
                //                                debug('Add the dimension and change the rel attribute to ' + parent_id + '.');
                //                                ui.item.find('a').attr('rel', parent_id);
                //                                debug('~~~');
                //                            }
                //                            // If the item being sorted is a measure.
                //                        }else{
                //                            debug('~~~');
                //                            debug('Sorting a measure');
                //                            debug('There are '+$both_dropzones.find('[rel=' + child_id + ']').length+' other measures with the same child_id');
                //                            if($both_dropzones.find('[rel=' + child_id + ']').length > 0) {
                //                                debug('You can not drop that measure as it already exists!');
                //                                debug('Remove the ui.item instance');
                //                                ui.item.remove();
                //                                debug('~~~');
                //                            }else if ($both_dropzones.find('.measure').length > 0){
                ////                                if ($dropped_axis) {
                ////                                    debug('~~~');
                ////                                    debug('Check if the measure is on the correct axis');
                ////                                    debug('You can not drop the measure on this axis!');
                ////                                    debug('Remove the ui.item instance');
                ////                                    ui.item.remove();
                ////                                    debug('~~~');
                ////                                }else{
                //                                    debug('Add the measure and change the rel attribute to ' + child_id + '.');
                //                                    ui.item.find('a').attr('rel', child_id);
                //                                    debug('~~~');
                //                                }
                //                            }
                //                        }
                //                    over : function(event, ui) {
                //                    //ui.placeholder.text(ui.helper.find('a').text());
                //                    },
                //                    stop : function(event, ui) {
                //                        // Get the parent_id and child_id of the sorted item.
                //                        var parent_id = ui.item.find('a').attr('rel').split('_')[0],
                //                        child_id = ui.item.find('a').attr('rel').split('_')[1];
                //                        // Check if the sort is valid or not.
                //                        if(view.valid_sort(ui, $(this), parent_id, child_id)) {
                //                            // Remove the placeholder.
                //                            $(this).find('.placeholder').hide();
                //                            // If valid and the item sortted is a dimension.
                //                            if(ui.item.find('a').hasClass('dimension')) {
                //                                /** Add the dimension_dropped class and replace the rel attribute with only the parent_id. */
                //                                ui.item.addClass('dimension_dropped').find('a').attr('rel', parent_id);
                //                                /** Show and hide the placeholder when necessary. */
                //                                if ($row_dropzone.find('.dimension_dropped, .measure_dropped').length == 0) {
                //                                    $(this).find('.placeholder').show();
                //                                }
                //                            }else{
                //                                /** Add the measure_dropped class and replace the rel attribute with only the child_id. */
                //                                ui.item.addClass('measure_dropped').find('a').attr('rel', child_id);
                //                                /** Show and hide the placeholder when necessary. */
                //                                if ($column_dropzone.find('.dimension_dropped, .measure_dropped').length == 0) {
                //                                    $(this).find('.placeholder').show();
                //                                }
                //                            }
                //                        }else{
                //                            // If the drop wasn't valid then remove the sortted item.
                //                            ui.item.remove();
                //                        }
                //                    }
                // Make the sortable area also droppable.
                });

                $both_dropzones.find("li").disableSelection();
                
                $('.sidebar').droppable({
                    accept : ".rows li, .columns li",
                    drop : function(event, ui) {
                        ui.draggable.addClass('dropped');
                        // Remove the draggable item.
                        ui.draggable.remove();
                        // Using setTimeout due to IE7+ bug with jQuery UI.
                        setTimeout(function() {
                            ui.draggable.remove();
                        } , 1);
                    }
                });

                view.stop_waiting();
            },
            error: function() {
                view.show_dialog("Error", "Couldn't create a new query. Please try again.", "error");
            }
        });
    }
   
}