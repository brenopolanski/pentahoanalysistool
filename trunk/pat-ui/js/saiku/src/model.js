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

                /** jQuery objects for trees.  */
                $dimension_tree = $tab.find('.dimension_tree');
                $dimension_tree_items = $dimension_tree.find('li ul li');
                $measure_tree = $tab.find('.measure_tree');
                $measure_tree_items = $measure_tree.find('li ul li');
                $both_trees = $tab.find('.measure_tree, .dimension_tree');

                /** jQuery objects for dropzones. */
                $row_dropzone = view.tabs.tabs[tab_index].content.find('.rows ul');
                $column_dropzone = view.tabs.tabs[tab_index].content.find('.columns ul');
                $both_dropzones = view.tabs.tabs[tab_index].content.find('.rows ul, .columns ul');
                $measures_dropzone = view.tabs.tabs[tab_index].content.find('.rows .measures_group, .columns .measures_group');
                $sidebar_dropzone = view.tabs.tabs[tab_index].content.find('.sidebar');
                $connectable = view.tabs.tabs[tab_index].content.find('.columns > ul, .rows > ul');
                $sidebar_accept = view.tabs.tabs[tab_index].content.find('.rows li, .columns li, .measures_group li');
                
                /** Make the dropzones sortable. */
                $both_dropzones.sortable({
                    connectWith : '.connectable',
                    forcePlaceholderSize : true,
                    items : '> li',
                    placeholder : 'placeholder',
                    opacity : 0.60,
                    cursorAt : {
                        top : 10,
                        left : 40
                    },
                    start : function(event, ui) {
                        ui.placeholder.text(ui.helper.text());
                    },
                    remove : function(event, ui) {
                        if(ui.item.find('a').hasClass('dimension')){
                            var dimension_id = ui.item.find('a').attr('rel').split('_')[0];
                            $dimension_tree.parent().parent().parent()
                            .find('[rel=' + dimension_id + ']').parent().children().children()
                            .removeClass('not-draggable').addClass('ui-draggable');
                        }else{
                            var measure_id = ui.item.find('a').attr('rel');
                            $measure_tree.find('[rel=' + measure_id + ']').parent()
                            .removeClass('not-draggable').addClass('ui-draggable');
                        }
                        if($(this).find('.dropped_dimension, .dropped_measure').length == 0) {
                            $(this).find('.empty_placeholder').show();
                        }
                    },
                    beforeStop : function(event, ui) {
                        if(!(ui.item.hasClass('dropped'))) {
                            if($both_dropzones.find('.measures_group').find('li').length == 0 && ui.item.find('a').hasClass('measure')) {
                                $(this).find('.placeholder').after('<li class="all_measures"><span>Drag all</span><ul class="measures_group"/></li>');
                                $measures_group = $both_dropzones.find('.measures_group');
                            }
                        }
                    },
                    stop : function (event, ui) {
                        if(!(ui.item.hasClass('dropped'))) {
                            if (ui.item.find('a').hasClass('dimension')) {
                                var dimension_id = ui.item.find('a').attr('rel').split('_')[0];
                                $dimension_tree.parent().parent().parent()
                                .find('[rel=' + dimension_id + ']').parent().children().children()
                                .removeClass('ui-draggable').addClass('not-draggable');
                                ui.item.css('display', '').addClass('dropped_dimension');
                            }else{
                                var measure_id = ui.item.find('a').attr('rel');
                                if($both_dropzones.find('.measures_group').find('li').length == 0) {
                                    ui.item.css('display', '').addClass('dropped_measure').appendTo($measures_group);
                                    $measures_group.sortable({
                                        forcePlaceholderSize : true,
                                        placeholder : 'placeholder',
                                        items : 'li',
                                        opacity : 0.60,
                                        cursorAt : {
                                            top : 10,
                                            left : 40
                                        },
                                        start : function(event, ui) {
                                            ui.placeholder.text(ui.helper.text());
                                        },
                                        stop : function(event, ui) {
                                            var measure_id = ui.item.find('a').attr('rel');
                                            ui.item.css('display', '').addClass('dropped_measure').appendTo($measures_group);
                                            $measure_tree.find('[rel=' + measure_id + ']').parent()
                                            .removeClass('ui-draggable').addClass('not-draggable');
                                        }
                                    });
                                    $measure_tree.find('li ul li').draggable('destroy').draggable({
                                        cancel : '.not-draggable',
                                        connectToSortable : '.measures_group',
                                        helper : 'clone',
                                        revert : 'invalid',
                                        opacity : 0.60,
                                        cursorAt : {
                                            top : 10,
                                            left : 40
                                        }
                                    });
                                    $measure_tree.find('[rel=' + measure_id + ']').parent()
                                    .removeClass('ui-draggable').addClass('not-draggable');
                                }else{
                                    $both_dropzones.sortable("refresh");
                                }
                            }
                            if($(this).find('.dropped_dimension, .dropped_measure').length == 1) {
                                $(this).find('.empty_placeholder').hide();
                            }
                        }                       
                    }
                }).disableSelection();

                /** Make the dimension tree draggable. */
                $dimension_tree_items.draggable({
                    cancel : '.not-draggable',
                    connectToSortable : $connectable,
                    helper : 'clone',
                    revert : 'invalid',
                    opacity : 0.60,
                    cursorAt : {
                        top : 10,
                        left : 40
                    },
                    over : function() {
                        console.log('O M G');
                    }
                });

                /** Make the measure tree draggable. */
                $measure_tree_items.draggable({
                    cancel : '.not-draggable',
                    connectToSortable : $connectable,
                    helper : 'clone',
                    revert : 'invalid',
                    opacity : 0.60,
                    cursorAt : {
                        top : 10,
                        left : 40
                    }
                });

                /** Make the sidebar droppable. */
                $sidebar_dropzone.droppable({
                    accept : '.dropped_measure, .dropped_dimension, .all_measures',
                    drop : function(event, ui) {
                        ui.draggable.addClass('dropped');
                        if(ui.draggable.find('a').hasClass('measure')) {
                            if($measures_group.find('.measure').length == 1 || ui.draggable.hasClass('all_measures')) {
                                $('.all_measures').remove();
                                $measure_tree_items.draggable('destroy').draggable({
                                    cancel : '.not-draggable',
                                    connectToSortable : $connectable,
                                    helper : 'clone',
                                    revert : 'invalid',
                                    opacity : 0.60,
                                    cursorAt : {
                                        top : 8,
                                        right : 40
                                    }
                                });
                                $both_dropzones.sortable("refresh");
                            }else{
                                ui.draggable.remove();
                                setTimeout(function() {
                                    ui.draggable.remove();
                                },1);
                            }
                        }else{
                            ui.draggable.remove();
                            setTimeout(function() {
                                ui.draggable.remove();
                            },1);
                        }
                        if($row_dropzone.find('.dropped_dimension, .dropped_measure').length == 0) {
                            $row_dropzone.find('.empty_placeholder').show();
                        }
                        if($column_dropzone.find('.dropped_dimension, .dropped_measure').length == 0) {
                            $column_dropzone.find('.empty_placeholder').show();
                        }
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