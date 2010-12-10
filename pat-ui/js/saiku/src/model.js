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
            url: BASE_URL + "saiku/rest/saiku/" + parameters.url,
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
                        view.draw_ui();
                        controller.add_tab();
                    }
                });
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
            url : model.username + "/query/new_query",
            data: {
                'connection': data['connectionName'],
                'cube': data['cube'],
                'catalog': data['catalogName'],
                'schema': data['schema'],
                'queryname': 'new_query'
            },
            success : function(data, textStatus, XMLHttpRequest) {
                //view.stop_waiting();
                //view.start_waiting("Loading dimensions and levels...");
        		
                /** Load dimensions into a tree. */
                view.load_dimensions($tab, data.axes[0].dimensions);
                /** Load measures into a tree. */
                view.load_measures($tab, data.axes[0].dimensions);

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

                /** Tree selectors. */
                $dimension_tree = $tab.find('.dimension_tree');
                $measure_tree = $tab.find('.measure_tree');
                $both_trees = $tab.find('.measure_tree, .dimension_tree');
                $both_tree_items = $tab.find('.measure_tree li ul li, .dimension_tree li ul li');

                /** Dropzone selectors. */
                $both_dropzones = $tab.find('.rows ul, .columns ul');
                $sidebar_dropzone = $tab.find('.sidebar');
                $connectable = $tab.find('.columns > ul, .rows > ul');
                $sidebar_accept = $tab.find('.rows li, .columns li');

                /** Disable selection. */
                $both_dropzones.find('.placeholder').disableSelection();
                $both_trees.find('ul > li').disableSelection();

                /** Remove all dropped items. */
                $both_dropzones.find('li').remove();

                /** Make the dropzones sortable. */
                $both_dropzones.sortable({
                    connectWith: '.connectable',
                    cursor: 'move',
                    cursorAt: {
                        top: 10,
                        left: 35
                    },
                    forcePlaceholderSize: true,
                    items: '> li',
                    opacity: 0.60,
                    placeholder: 'placeholder',
                    tolerance: 'pointer',
                    start: function(event, ui) {
                        /** Replace the placeholder text. */
                        ui.placeholder.text(ui.helper.text());
                    },
                    beforeStop: function(event, ui) {
                        /** Is the item being removed. */
                        if(!(ui.item.hasClass('dropped'))) {
                            /** Determine the sorting to and from axis. */
                            if($(this).parent().hasClass('rows')) {
                                var sort_to = 'columns', sort_from = 'rows', is_measure = ui.item.hasClass('d_measure');
                            }else{
                                var sort_to = 'rows', sort_from = 'columns', is_measure = ui.item.hasClass('d_measure');
                            }

                            /** Set sorting between lists to false. */
                            var between_lists = false;

                            /** Check if sorting a measure, does the axis accepting the sort have a measure already. */
                            if ($('.'+sort_to).find('.d_measure').length == 1
                                && $('.'+sort_from).find('.d_measure').length > 0
                                && is_measure) {
                                /** Move all measures from rows to columns. */
                                $('.'+sort_to).find('.d_measure').last()
                                .after($('.'+sort_from).find('.d_measure'));
                                /** Set sorting between lists to true. */
                                between_lists = true;
                            }

                            /** Sorting a dimension or measure. */
                            var is_dimension = ui.item.find('a').hasClass('dimension'),
                            is_measure = ui.item.find('a').hasClass('measure');
                            /** What is on the left and right of the placeholder. */
                            var left_item = $(this).find('.placeholder').prev().prev(),
                            right_item = $(this).find('.placeholder').next();

                            /** Sorting a dimension. */
                            if (is_dimension){
                                /** dimension id. */
                                var dimension_id = ui.item.find('a').attr('rel').split('_')[0];
                                /** If the placeholder is in between measures. */
                                if(left_item.hasClass('d_measure') && right_item.hasClass('d_measure')) {
                                    /** Find the last item and append it to the end of the list. */
                                    $(this).find('li').last().append()
                                    .after(ui.item.css('display', '').addClass('d_dimension'));
                                }else{
                                    /** Act as normal. */
                                    ui.item.css('display', '').addClass('d_dimension');
                                }
                                /** Disable all siblings of the dimension. */
                                $dimension_tree.parent().parent().parent()
                                .find('[rel=' + dimension_id + ']').parent().children().children()
                                .removeClass('ui-draggable').addClass('not-draggable');

                            }else if (!(between_lists)) {
                                /** If sorting between lists and is a measure. */
                                /** If this is the first measure. */
                                if ($both_dropzones.find('.d_measure').length == 0) {
                                    /** Act as normal. */
                                    ui.item.css('display','').addClass('d_measure');
                                }else{ /** Is there a measure on the left or right or ( measure on left and right ). */
                                    if ((left_item.hasClass('d_measure') || right_item.hasClass('d_measure')
                                        || (left_item.hasClass('d_measure') && right_item.hasClass('d_measure')))) {
                                        /** Act as normal. */
                                        ui.item.css('display','').addClass('d_measure');
                                    }else{
                                        /** If not then find all other measures insert them after the measure being sorted. */
                                        $both_dropzones.find('.d_measure').insertAfter($('.placeholder'));
                                        ui.item.css('display','').addClass('d_measure');
                                    }
                                }

                                /** measure id. */
                                var measure_id = ui.item.find('a').attr('rel');
                                /** Disable the measure. */
                                $measure_tree.find('[rel=' + measure_id + ']').parent()
                                .removeClass('ui-draggable').addClass('not-draggable');
                            }
                        }
                    },
                    receive: function(event, ui) {
                        if( ui.item.hasClass('d_measure') || ui.item.hasClass('d_dimension')
                            || ui.item.find('a').hasClass('measure') || ui.helper === null) {
                        // Do nothing.
                        }else{
                            /** Get the dimension_id. */
                            var dimension_id = ui.item.find('a').attr('rel').split('_')[0];
                            /** Toggle (Hide/Show) the children of the dimension. */
                            $dimension_tree.parent().parent().parent()
                            .find('[rel=' + dimension_id + ']').parent().children().children().toggle();
                            /** Style the parent dimension. */
                            $dimension_tree.parent().parent().parent()
                            .find('[rel=' + dimension_id + ']').parent().removeClass('expand').addClass('collapsed')
                            .find('a.folder_expand').removeClass('folder_expand').addClass('folder_collapsed');
                        }
                    }
                }).disableSelection();

                /** Make the measure and dimension tree draggable. */
                $both_tree_items.draggable({
                    cancel: '.not-draggable',
                    connectToSortable: $connectable,
                    helper: 'clone',
                    opacity: 0.60,
                    cursor: 'move',
                    tolerance: 'pointer',
                    cursorAt: {
                        top: 10,
                        left: 35
                    }
                });

                //** Make the sidebar droppable. */
                $sidebar_dropzone.droppable({
                    accept: '.d_measure, .d_dimension',
                    drop: function(event, ui) {
                        /** Add the drop class so that the sortable functions. */
                        ui.draggable.addClass('dropped');
                        /** Is the item being removed is a measure or is all the measures. */
                        if (ui.draggable.find('a').hasClass('measure')) {
                            /** We can assume it is only one measures being removed. */
                            var measure_id = ui.draggable.find('a').attr('rel');
                            /** Enable the measure in the measures tree. */
                            $measure_tree.find('[rel=' + measure_id + ']').parent()
                            .removeClass('not-draggable').addClass('ui-draggable');
                        }else{
                            /** dimension id. */
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
                            /** Remove the draggable measure. */
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