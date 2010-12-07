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

        view.start_waiting('Preparing workspace...');

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
                $tab.find('.dimension_tree .root').children('ul').children('li').hide();
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

                /** jQuery objects for trees.  */
                $dimension_tree = $tab.find('.dimension_tree');
                $dimension_tree_items = $dimension_tree.find('li ul li');
                $measure_tree = $tab.find('.measure_tree');
                $measure_tree_items = $measure_tree.find('li ul li');
                $both_trees = $tab.find('.measure_tree, .dimension_tree');
                $both_trees_items = $tab.find('.measure_tree li ul li, .dimension_tree li ul li');

                /** jQuery objects for dropzones. */
                $row_dropzone = view.tabs.tabs[tab_index].content.find('.rows ul');
                $column_dropzone = view.tabs.tabs[tab_index].content.find('.columns ul');
                $both_dropzones = view.tabs.tabs[tab_index].content.find('.rows ul, .columns ul');
                $measures_dropzone = view.tabs.tabs[tab_index].content.find('.rows .measures_group, .columns .measures_group');
                $sidebar_dropzone = view.tabs.tabs[tab_index].content.find('.sidebar');
                $connectable = view.tabs.tabs[tab_index].content.find('.columns > ul, .rows > ul');
                $sidebar_accept = view.tabs.tabs[tab_index].content.find('.rows li, .columns li, .measures_group li');

                $both_dropzones.find('.placeholder').disableSelection();
                $both_trees.find('ul > li').disableSelection();

                /** Make the dropzones sortable. */
                $both_dropzones.sortable({
                    connectWith : '.connectable',
                    items : '> li',
                    placeholder : 'placeholder',
                    forcePlaceholderSize: true,
                    opacity : 0.60,
                    handler : 'a',
                    //cursor : 'move',
                    //tolerance: 'pointer',
                    cursorAt : {
                        top : 10,
                        left : 10
                    },
                    start : function(event, ui) {
                        ui.placeholder.text(ui.helper.text());
                    },
                    beforeStop : function(event, ui) {
                        /** If the user is trying to not remove the item. */
                        if (!(ui.item.hasClass('dropped'))) {
                            /** If the user is dropping or sorting a dimension. */
                            if (ui.item.find('a').hasClass('dimension')) {
                                /** What is the dimension_id. */
                                var dimension_id = ui.item.find('a').attr('rel').split('_')[0];
                                /** Make sure none of the dimensions in the same level can not be dragged or sorted. */
                                $dimension_tree.parent().parent().parent()
                                .find('[rel=' + dimension_id + ']').parent().children().children()
                                .removeClass('ui-draggable').addClass('not-draggable');
                                /** Style the newly sorted / dropped item. */
                                ui.item.css('display', '').addClass('dropped_dimension');
                            }else{ /** If the user is dropping or sorting a measure. */
                                /** Check if this is a new measure (no other measures exist. */
                                if($both_dropzones.find('.measures_group li').length == 0 && ui.item.find('a').hasClass('measure')) {
                                    /** Get the measure id. */
                                    var measure_id = ui.item.find('a').attr('rel');
                                    /** Find out where the user is dropping the first measure. */
                                    $(this).find('.placeholder')
                                    .after('<li class="all_measures"><span class="small">All</span><ul class="measures_group"></ul></li>');
                                    // Set a pointer to the above list.
                                    $measures_group = $both_dropzones.find('.measures_group');
                                    /** Style the first measure and add it to the above ul. */
                                    ui.item.css('display', '').addClass('dropped_measure').appendTo($measures_group);
                                    /** Style the measure which has just been dropped (first measure). */
                                    $measure_tree.find('[rel=' + measure_id + ']').parent()
                                    .removeClass('ui-draggable').addClass('not-draggable');
                                    /** Make the measures group sortable. */
                                    $measures_group.sortable({
                                        placeholder : 'placeholder',
                                        items : 'li',
                                        opacity : 0.60,
                                        //cursor : 'move',
                                        cursorAt : {
                                            top : 10,
                                            left : 20
                                        },
                                        start : function(event, ui) {
                                            if (!(ui.item.hasClass('dropped'))) {
                                                /** Expand the measures group for easy drag and drop. */
                                                var set_width = $('.measures_group').width() + ui.helper.width() + 10;
                                                $('.measures_group').css('width', set_width+'px');
                                                /** Style the placeholder within the measures group. */
                                                ui.placeholder.text(ui.helper.text());
                                            }
                                        },
                                        stop : function(event, ui) {
                                            if (!(ui.item.hasClass('dropped'))) {
                                                /** Get the measure id. */
                                                var measure_id = ui.item.find('a').attr('rel');
                                                /** Style the measure which was just added. */
                                                ui.item.css('display', '').addClass('dropped_measure').appendTo($measures_group);
                                                /** Disable the measure in the measure tree. */
                                                $measure_tree.find('[rel=' + measure_id + ']').parent()
                                                .removeClass('ui-draggable').addClass('not-draggable');
                                            }
                                            /** Reset the width. */
                                            $('.measures_group').css('width', 'auto');
                                        }
                                    });
                                    
                                    /** Destroy and create the measures tree so that it points only to the measures group. */
                                    $measure_tree_items.draggable('destroy').draggable({
                                        cancel : '.not-draggable',
                                        connectToSortable : $measures_group,
                                        helper : 'clone',
                                        opacity : 0.60,
                                        ///cursor: 'move',
                                        cursorAt : {
                                            top : 10,
                                            left : 20
                                        }
                                    });
                                }else{
                                    /** If we are sorting from one axis to another axis. */
                                    var sorting_to = ui.helper.parent().parent().attr('class').split(' ')[1],
                                    sorting_from = $(this).parent().attr('class').split(' ')[1];
                                    if (sorting_to === 'rows' && sorting_from === 'columns'
                                        || sorting_to === 'columns' && sorting_from === 'rows') {
                                    /** Lets do nothing, this works for some reason. */
                                    }else{
                                        /** The user is adding an existing measure. */
                                        /** Get the measure id. */
                                        var measure_id = ui.item.find('a').attr('rel');
                                        if (!measure_id) {
                                            /** Style the measure which was just added. */
                                            ui.item.css('display', '').addClass('dropped_measure');
                                            /** Disable the measure in the measure tree. */
                                            $measure_tree.find('[rel=' + measure_id + ']').parent()
                                            .removeClass('ui-draggable').addClass('not-draggable');
                                        }
                                    }
                                }
                            }
                        }
                    },
                    stop : function (event, ui) {
                        $both_dropzones.sortable("refresh");
                    }                    
                }).disableSelection();

                /** Make the dimension tree draggable. */
                $dimension_tree_items.draggable({
                    cancel : '.not-draggable',
                    connectToSortable : $connectable,
                    helper : 'clone',
                    opacity : 0.60,
                    //cursor: 'move',
                    cursorAt : {
                        top : 10,
                        left : 20
                    }
                });
                /** Make the measure tree draggable. */
                $measure_tree_items.draggable({
                    cancel : '.not-draggable',
                    connectToSortable : $connectable,
                    helper : 'clone',
                    opacity : 0.60,
                    //cursor: 'move',
                    cursorAt : {
                        top : 10,
                        left : 20
                    }
                });
                
                /** Make the sidebar droppable. */
                $sidebar_dropzone.droppable({
                    accept : '.dropped_measure, .dropped_dimension, .all_measures',
                    drop : function(event, ui) {
                        /** Add the drop class so that the sortable functions. */
                        ui.draggable.addClass('dropped');

                        /** Is the item being removed is a measure or is all the measures. */
                        if (ui.draggable.find('a').hasClass('measure') || ui.draggable.hasClass('all_measures')) {
                            /** Is this the last measure being removed or is all the masures being removed. */
                            if ($measures_group.find('.measure').length == 1 || ui.draggable.hasClass('all_measures')) {
                                /** Remove the measures group.*/
                                $('.all_measures').remove();
                                /** Destroy the measure_tree_items draggable instance and recreate it. */
                                $measure_tree_items.attr('class', '').draggable('destroy').draggable({
                                    cancel : '.not-draggable',
                                    connectToSortable : $connectable,
                                    helper : 'clone',
                                    cursor: 'move',
                                    opacity : 0.60,
                                    cursorAt : {
                                        top : 10,
                                        left : 20
                                    }
                                });
                            }else{
                                /** We can assume it is only one measures being removed. */
                                var measure_id = ui.draggable.find('a').attr('rel');
                                /** Enable the measure in the measures tree. */
                                $measure_tree.find('[rel=' + measure_id + ']').parent()
                                .removeClass('not-draggable').addClass('ui-draggable');
                            }
                        }else{
                            /** We can assume it is a dimension. */
                            /** Get the dimension id. */
                            var dimension_id = ui.draggable.find('a').attr('rel').split('_')[0];
                            /** Enable the dimenson and sibilings in the dimension tree. */
                            $dimension_tree.parent().parent().parent()
                            .find('[rel=' + dimension_id + ']').parent().children().children()
                            .removeClass('not-draggable').addClass('ui-draggable');
                        }
                        /** Remove the draggable measure. */
                        ui.draggable.remove();
                        // Patch needed for IE to work.
                        setTimeout(function() {
                            ui.draggable.remove();
                        },1);
                    }
                });

                view.stop_waiting();
            },
            error: function() {
                view.stop_waiting();
                view.show_dialog("Error", "Couldn't create a new query. Please try again.", "error");
                $('.cubes').find('option:first').attr('selected', 'selected');
            }
        });
    }
    
}
