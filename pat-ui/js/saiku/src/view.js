/**
 * @fileOverview    This represents the view for Saiku UI.
 * @description     This will handle the drawing of the UI.
 * @version         1.0.0
 */ 

/**
 * View class.
 * @class
 */
var view = {
    /** Display the login form when the view is initialised. */
    init : function() {

        // Append a dialog <div/> to the body.
        $('<div id="dialog" class="dialog hide" />').appendTo('body');
        
        // Load the view into the dialog <div/> and disable caching.
        $.ajax({
            url : BASE_URL + 'views/session/',
            cache : false,
            dataType : "html",
            success : function(data) {
                $('#dialog').html(data).modal({
                    opacity : 100,
                    // onShow : function (dialog) {}
                    onClose : function (dialog) {
                        // Get the username and password from the form.
                        model.username = $('#username').val();
                        model.password = $('#password').val();

                        // Remove all simple modal objects.
                        dialog.data.remove();
                        dialog.container.remove();
                        dialog.overlay.remove();
                        $.modal.close();
                        
                        // Remove the #dialog which we appended to the body.
                        $('#dialog').remove();

                        // Show pre loading message
                        // $('<div class="dialog pre_waiting"><div class="dialog_inner"><div class="dialog_body_waiting">Loading Saiku. Please wait...</div></div></div>').appendTo('body');

                        view.show_processing('Loading Saiku User Interface. Please wait...');

                        // Create the session and log in.
                        model.get_session();
                    }
                });
            }
        });
    },

    /** Tabs container. */
    tabs : new TabContainer($("#tabs"), $('#tab_panel')),
    
    /**
     * Resize layout to fit window height
     */
    resize_height: function(tab_index) {
        
        // What tab is being viewed
        $active_tab = view.tabs.tabs[tab_index].content;
        var window_height, workspace_header;

        // Work out the browser windows height and make sure that it is set to
        // 600px when the user reduces the height below 600px.
        if ($(window).height() <= 600) {
            window_height = 600;
        }else{
            window_height = $(window).height();
        }

        // Work out the header height and add 1px for the border-bottom on the
        // header.
        var header_height = $('#header').outerHeight() + 1; 
        
        // Work out the sidebar height, which is the header minus the window
        // height.
        var sidebar_height = window_height - header_height;

        // Work out the height of the workspace toolbar.
        var workspace_toolbar = $active_tab.find('.workspace_toolbar').outerHeight(true);
        
        // Work out the height of the fields area
        var workspace_fields = $active_tab.find('.workspace_fields').outerHeight(true);

        // Calculate workspace_header area taking into account that fields can
        // be hidden or shown

        if($active_tab.find('.workspace_fields').is(':visible')) {
            workspace_header = workspace_toolbar + workspace_fields;
        }else{
            workspace_header = workspace_toolbar;
        }
        
        // Set sidebar heights
        $active_tab.find('.sidebar, .sidebar_separator, .workspace_inner')
        .css('height', sidebar_height);

        $active_tab.find('.workspace_results')
        // Add 30 for padding on height
        .css('height', (sidebar_height - workspace_header) - 30);

    },
    
    /** 
     * Toggle (hide/show) the sidebar. 
     */
    toggle_sidebar: function($sidebar_separator) {
        // Find the tab
        $tab = $sidebar_separator.closest('.tab');
    	
        // Get the width of the sidebar.
        var sidebar_width = $tab.find('.sidebar').width();
       
        if (sidebar_width == 260) {
            // If the sidebar is not hidden.
            $tab.find('.sidebar').css('width', 0);
            $tab.find('.workspace_inner').css('margin-left', 5);
        } else {
            // If the sidebar is hidden.
            $tab.find('.sidebar').css('width', 260);
            $tab.find('.workspace_inner').css('margin-left', 265);
        }
    },

    /** Initialise the user interface. */
    draw_ui : function () {

        // Patch for webkit browsers to stop the text cursor appearing
        // when dragging items.
        document.onselectstart = function () {
            return false;
        };

        /** Show all UI elements. */
        $('#header, #tab_panel').show();

        /** Add an event handler to all toolbar buttons. */
        $("#toolbar ul li a").click(function() {
            controller.toolbar_click_handler($(this));
            return false;
        });

        /** Bind resize_height() to the resize event. */
        $(window).bind('resize', function() {
            view.resize_height(0);
        });

        /** Bind toggle_sidebar() to click event on the sidebar_separator. */
        $('.sidebar_separator').live('click', function() {
            view.toggle_sidebar($(this));
        });

        /** Add click handler on tabs. */
        view.tabs.tab_container.find("a").live('mousedown', function(event) {
            if (event.which == 1) {
                view.tabs.select_tab(view.tabs.index_from_tab($(this).parent()));
            } else if (event.which == 2) {
                view.tabs.remove_tab(view.tabs.index_from_tab($(this).parent()));
            }
            event.stopImmediatePropagation();
            event.cancelBubble = true;
            return false;
        });

        /** Add click handler on tabs. */
        view.tabs.tab_container.find("span").live('click', function() {
            view.tabs.remove_tab(view.tabs.index_from_tab($(this).parent()));
            return false;
        });
        
        // Activate language selector
        $("#language-selector").val(locale)
        .change(function() {
            locale = $("#language-selector").val();
            $.ajax({
                url: '/i18n/' + locale + ".json",
                type: 'GET',
                dataType: 'json',
                success: function(data) {
                    if (data) {
                        po_file = data;
                        $('.i18n_translated').un_i18n();
                        $('.i18n').i18n(po_file);
                    }
                }
            });
        });
    },

    /** Destroy the user interface. */
    destroy_ui : function () {
        $('#header, #tab_panel').hide();
    },

    /**
     * Populate a select box with available schemas and cubes.
     * @param tab_index {Integer} Index of the selected tab.
     */
    load_cubes : function(tab_index) {
        $tab = view.tabs.tabs[tab_index].content;
        $cubes = $tab.find('.cubes');
        $cubes.append('<option>Select a cube</option>');

        view.tabs.tabs[tab_index].data['navigation'] = new Array();
        storage_id = 0;

        /** Loop through available connections and populate the select box. */
        $.each(model.connections, function(i,connection){
            $.each(connection.catalogs[0].schemas, function(i,schema){
                
                $cubes.append('<optgroup label="'+schema['name']+'">');
                $.each(schema.cubes, function(i,cube){
                    $("<option />")
                    .attr({
                        'value': storage_id
                    })
                    .text(cube['name'])
                    .appendTo($cubes);
                    view.tabs.tabs[tab_index].data['navigation'][storage_id] = {
                        'connectionName': connection['name'],
                        'catalogName': connection.catalogs[0]['name'],
                        'schema': schema['name'],
                        'cube': cube['name']
                    };
                    storage_id++;
                //});
                });
                $cubes.append('</optgroup>');
            });
        });

        $cubes.change(function() {
            model.new_query(tab_index);
        });
    },

    /**
     * Load queries into the sidebar of the open query dialog
     * @param tab_index {Integer} Index of a queries.
     */
    load_queries: function(tab_index, data) {
        // Pointer for sidebar
        $query_list = view.tabs.tabs[tab_index].content.find('.sidebar_inner ul');
        
        // Load the list of queries
        $.each(data, function(query_iterator, query) {
            $list_element = $('<li />').appendTo($query_list);
            $("<a />").text(query.name)
            .data('object', query)
            .attr('href', '#')
            // Show information about the query when its name is clicked
            .click(function() {
                var $query = $(this).data('object');
                $results = view.tabs.tabs[tab_index].content.find(".workspace_results");
                $results.html('<h2>' + $query.name + '</h2>');
                $properties = $('<ul />').appendTo($results);
                // Iterate through properties and show a key=>value set in the information pane
                for (property in $query) {
                    if ($query.hasOwnProperty(property)) {
                        $properties.append($('<li />').text(property + ": " + $query[property]));
                    }
                }
                    
                // Add open query button
                $('<a />').text('Open query')
                .attr('href', '#')
                .addClass('i18n')
                .i18n(po_file)
                .click(function() {
                    model.open_query($query.name, view.tabs.add_tab());
                    return false;
                })
                .appendTo($results);
                    
                return false;
            })
            .appendTo($list_element);
        });
    },

    /**
     * Populate the dimension tree for the selected tab.
     * @param $tab {Object} Selected tab content.
     * @param data {Object} Data object which contains the available dimension
     *                      members.
     */
    load_dimensions : function(tab_index, data) {
        // Remove any instances of a tree.
        $tab = view.tabs.tabs[tab_index].content;
        $tab.find('.dimension_tree ul').remove();
        
        // Add a new dimension tree.
        $dimension_tree = $('<ul />').appendTo($tab.find('.dimension_tree'));
        
        // Populate the tree with first level dimensions.
        dimension_id = 0;
        delete view.tabs.tabs[tab_index].data['dimensions'];
        view.tabs.tabs[tab_index].data['dimensions'] = new Array();
        $.each(data, function(dimension_iterator, dimension) {
            if (this['name'] != 'Measures') {
                // Make sure the first level has a unique rel attribute.
                $first_level = $('<li><span class="root collapsed"><a href="#" rel="d' + dimension_iterator + '" class="folder_collapsed">' + this['name'] + '</a></span></li>')
                .appendTo($dimension_tree);
                $second_level = $('<ul />').appendTo($first_level);
                $.each(dimension.hierarchies, function(hierarchy_iterator, hierarchy) {
                    
                    // Add the hierarchy name.
                    $('<li class="hierarchy" />').html('<a href="#">' + hierarchy.caption + '</a>').appendTo($second_level);
                    // Loop through each hierarchy.
                    $.each(hierarchy.levels, function(level_iterator, level){
                        dimension_id++;
                        $li = $('<li />').mousedown(function() {
                            return false;
                        })
                        .attr('title', dimension_id)
                        .appendTo($second_level);
                        view.tabs.tabs[tab_index].data['dimensions'][dimension_id] = {
                            'dimension': dimension.name,
                            'hierarchy': hierarchy.uniqueName, //hierarchy.hierarchy
                            'level': level.uniqueName // level.level
                        };                        
                        // Check if the dimension level is (All) if so display the All dimension_name instead.
                        if (level['caption'] === '(All)') {
                            // Create a parent-child relationship with the rel attribute.
                            $second_level_link = $('<a href="#" class="dimension" rel="d' + dimension_iterator + '_' + hierarchy_iterator + '_' + level_iterator + '" title="' + level['uniqueName'] + '"> All ' + hierarchy.caption + '</a>')
                            .appendTo($li);
                        }else{
                            // Create a parent-child relationship with the rel attribute.
                            $second_level_link = $('<a href="#" class="dimension" rel="d' + dimension_iterator + '_' + hierarchy_iterator + '_' + level_iterator + '" title="' + level['uniqueName'] + '">' + level['caption'] + '</a>')
                            .appendTo($li);
                        }
                    });
                });
                /** After each loop of the dimension make sure that is more than one hierarchy, if not remove the hiearchy. */
                if ($first_level.find('.hierarchy').length == 1) {
                    $first_level.find('.hierarchy').remove();
                }
            }
        });
    },

    /**
     * Populate the measure tree for the selected tab.
     * @param $tab {Object} Selected tab content.
     * @param data {Object} Data object which contains the available measure
     *                      members.
     */
    load_measures : function(tab_index, data, url) {
        /** We need to fetch the measures separetely. */
        $tab = view.tabs.tabs[tab_index].content;

        // Remove any instances of a tree.
        $tab.find('.measure_tree ul').remove();
        // Create a new measures tree.
        $measure_tree = $('<ul />').appendTo($tab.find('.measure_tree'));
        // Add the first static measures folder.
        $measures = $('<li><span class="root expand"><a href="#" title="Measures" rel="m0" class="folder_expand">Measures</a></span></li>')
        .appendTo($measure_tree);
        // Add a child list to the measures list.
        $measures_ul = $('<ul />').appendTo($measures);
        
        // Prep measures metadata
        measure_id = 0;
        delete view.tabs.tabs[tab_index].data['measures'];
        view.tabs.tabs[tab_index].data['measures'] = new Array();
        
        // Populate the tree with the children of MeasureLevel
        $.each(data, function(i, member) {
            measure_id++;
        	
            $('<li title="' + measure_id + '"><a href="#" class="measure" rel="m0_' + i + '"  title="'+this['uniqueName']+'">'+this['caption']+'</a></li>')
            .mousedown(function() {
                return false;
            }).appendTo($measures_ul);
            
            view.tabs.tabs[tab_index].data['measures'][measure_id] = {
                'measure': member.uniqueName // member.member
            };
        });
        /** Prepare the workspace. */
        view.prepare_workspace($tab);
        
        // Add a new measure tree.
        $measure_tree = $('<ul />').appendTo($tab.find('.measure_tree')).addClass('mtree');
        // Populate the tree with first level measures.
        $.each(data, function(i, dimension) {
            if (this['name'] === 'Measures') {
                $measures = $('<li><span class="root expand"><a href="#" title="Measures" rel="m' + i + '" class="folder_expand">Measures</a></span></li>')
                .appendTo($tab.find('.measure_tree ul'));
                $measures_ul = $('<ul />').appendTo($measures);
                $.each(dimension.hierarchies[0].levels, function(j, level){
                    $('<li><a href="#" class="measure" rel="m' + i + '_' + j + '"  title="'+this['level']+'">'+this['caption']+'</a></li>')
                    .mousedown(function() {
                        return false;
                    }).appendTo($measures_ul);
                });
            }
        });
    },

    /**
     * Prepare the new query trees and workspace.
     * @param $tab {Object} Selected tab content.
     */
    prepare_workspace: function($tab) {

        /** Initisalise trees */
        init_trees($tab);

        /** Tree selectors. */
        $dimension_tree = $tab.find('.dimension_tree');
        $measure_tree = $tab.find('.measure_tree');
        $both_trees = $tab.find('.measure_tree, .dimension_tree');
        $both_tree_items = $tab.find('.measure_tree ul li ul li, .dimension_tree ul li ul li');

        /** Dropzone selectors. */
        $both_dropzones = $tab.find('.rows ul, .columns ul');
        $column_dropzone = $tab.find('.columns ul');
        $row_dropzone = $tab.find('.rows ul');
        $sidebar_dropzone = $tab.find('.sidebar');
        $connectable = $tab.find('.columns > ul, .rows > ul');
        $sidebar_accept = $tab.find('.rows li, .columns li');

        /** Disable selection. */
        $both_dropzones.find('.placeholder').disableSelection();
        $both_trees.find('ul > li').disableSelection();

        // If the user selects a new query within the same tab.
        /** Remove all dropped items. */
        $both_dropzones.find('li').remove();
        /** Remove the table. */
        $tab.find('.workspace_results table').remove();

        /** Reset all sortable items. */
        $both_dropzones.sortable('reset');

        /** Check the toolbar. */
        view.check_toolbar();

        //        /** Double click instead of drag and drop. */
        //        $both_tree_items.dblclick(function(e){
        //
        //
        //
        //            // Prevent default browser action from occuring.
        //            e.preventDefault();
        //            /* Is the user double clicking on a dimension or measure. */
        //            var is_dimension = $(this).find('a').hasClass('dimension'), is_measure = $(this).find('a').hasClass('measure');
        //            /** Only continue if the item is active. */
        //
        //            if ($(this).hasClass('ui-draggable')) {
        //                /** If a measure. */
        //                if (is_measure) {
        //                    /** If the first measure in the dropzone. */
        //                    if ($both_dropzones.find('.d_measure').length == 0) {
        //                        /** By default add the measure to the column dropzone. */
        //                        $(this).clone().appendTo($column_dropzone).addClass('d_measure');
        //                        /** Continue adding the measure. */
        //                        add_measure($tab, $(this).find('a').attr('rel'));
        //                    }else{
        //                        /** Append the measure to the last measure available. */
        //                        $(this).clone().insertAfter($both_dropzones.find('.d_measure').last()).addClass('d_measure');
        //                        /** Continue adding the measure. */
        //                        add_measure($tab, $(this).find('a').attr('rel'));
        //                    }
        //                    /** When stopped dropping or sorting set the selection. */
        //                    model.dropped_item($(this), true);
        //                }else if(is_dimension) {
        //                    /** Add the dimension to the row dropzone manually. */
        //                    $(this).clone().appendTo($row_dropzone).addClass('d_dimension');
        //                    /** Continue adding the dimension. */
        //                    add_dimension($tab, $(this).find('a').attr('rel'));
        //                    /** When stopped dropping or sorting set the selection. */
        //                    model.dropped_item($(this), true);
        //                }
        //                /** Refresh the sortables. */
        //                $both_dropzones.sortable('refresh');
        //
        //            } else if ($(this).hasClass('not-draggable')) {
        //                if (is_measure) {
        //                    /** Remove the measure manually. */
        //                    $both_dropzones.find('[rel=' + $(this).find('a').attr('rel') +']').parent().remove();
        //                    /** Continue removing the measure. */
        //                    remove_measure($tab, $(this).find('a').attr('rel'));
        //                } else if(is_dimension) {
        //                    /** Remove the dimension manually. */
        //                    $both_dropzones.find('[rel=' + $(this).find('a').attr('rel') +']').parent().remove();
        //                    /** Continue removing the measure. */
        //                    remove_dimension($tab, $(this).find('a').attr('rel'));
        //                }
        //                /** Refresh the sortables. */
        //                $both_dropzones.sortable('refresh');
        //                /** When dimension or measure is removed, set the selection. */
        //                model.removed_item($(this), true);
        //
        //                // If automatic query execution is enabled, rerun the query after making change
        //                if (view.tabs.tabs[tab_index].data['options']['automatic_execution']) {
        //                    if($row_dropzone.find('li.d_measure, li.d_dimension').length > 0 && $column_dropzone.find('li.d_measure, li.d_dimension').length > 0) {
        //                        model.run_query(tab_index);
        //                    }
        //                }
        //
        //                view.check_toolbar();
        //
        //            }
        //        });


        /** Activate all items for selection. */
        $('.rows ul li a, .columns ul li a').live('dblclick', function() {
            var member_clicked = $(this).text();
            model.show_selections(member_clicked);
        });

        /** Make the dropzones sortable. */
        $both_dropzones.sortable({
            connectWith: $tab.find('.connectable'),
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
                    if ($('.'+sort_to).find('.d_measure').length == 1 && $('.'+sort_from).find('.d_measure').length > 0 && is_measure) {
                        /** Move all measures from rows to columns. */
                        $('.'+sort_to).find('.d_measure').last().after($('.'+sort_from).find('.d_measure'));
                        /** Set sorting between lists to true. */
                        between_lists = true;
                    }
                    /** Sorting a dimension or measure. */
                    var is_dimension = ui.item.find('a').hasClass('dimension'), is_measure = ui.item.find('a').hasClass('measure');
                    /** What is on the left and right of the placeholder. */
                    var left_item = $(this).find('.placeholder').prev().prev(), right_item = $(this).find('.placeholder').next();
                    /** Sorting a dimension. */
                    if (is_dimension){
                        /** If the placeholder is in between measures. */
                        if(left_item.hasClass('d_measure') && right_item.hasClass('d_measure')) {
                            /** Find the last item and append it to the end of the list. */
                            $(this).find('li').last().append().after(ui.item.css('display', '').addClass('d_dimension'));
                        }else{
                            /** Act as normal. */
                            ui.item.css('display', '').addClass('d_dimension');
                        }
                        /** Continue adding the dimension. */
                        add_dimension($tab, ui.item.find('a').attr('rel'));
                    }else if (!(between_lists)) {
                        /** If sorting a measure and is not between lists. */
                        /** If this is the first measure. */
                        if ($both_dropzones.find('.d_measure').length == 0) {
                            /** Act as normal. */
                            ui.item.css('display','').addClass('d_measure');
                        }else{ /** Is there a measure on the left or right or ( measure on left and right ). */
                            if ((left_item.hasClass('d_measure') || right_item.hasClass('d_measure') || (left_item.hasClass('d_measure') && right_item.hasClass('d_measure')))) {
                                /** Act as normal. */
                                ui.item.css('display','').addClass('d_measure');
                            }else{
                                /** If not then find all other measures insert them after the measure being sorted. */
                                $both_dropzones.find('.d_measure').insertAfter($('.placeholder'));
                                ui.item.css('display','').addClass('d_measure');
                            }
                        }
                        /** Continue adding the measure. */
                        add_measure($tab, ui.item.find('a').attr('rel'));
                    }
                }
            },
            
            stop: function(event, ui) {
                /** Is the item being removed. */
                if(!(ui.item.hasClass('dropped'))) {
                    /** When stopped dropping or sorting set the selection. */
                    model.dropped_item(ui.item, false);
                }
            }
            
        }).disableSelection();

        /** Make the measure and dimension tree draggable. */
        $both_tree_items.draggable({
            cancel: '.not-draggable, .hierarchy',
            connectToSortable: $connectable,
            helper: 'clone',
            opacity: 0.60,
            tolerance: 'pointer',
            cursorAt: {
                top: 10,
                left: 35
            }
        });

        /** Make the sidebar droppable. */
        $sidebar_dropzone.droppable({
            accept: '.d_measure, .d_dimension',
            drop: function(event, ui) {
                /** Add the drop class so that the sortable functions. */
                ui.draggable.addClass('dropped');
                /** Is the item being removed is a measure or is all the measures. */
                if (ui.draggable.find('a').hasClass('measure')) {
                    /** Remove the measure. */
                    remove_measure($tab, ui.draggable.find('a').attr('rel'), true);
                }else{
                    /** Remove the dimension. */
                    remove_dimension($tab, ui.draggable.find('a').attr('rel'));
                }
                
                // Remove item from query
                model.removed_item(ui.draggable);
                
                /** Remove the draggable measure. */
                ui.draggable.remove();
                // Patch needed for IE to work.
                setTimeout(function() {
                    /** Remove the draggable measure. */
                    ui.draggable.remove();
                },1);
                /** When dimension or measure is removed, set the selection. */

                // If automatic query execution is enabled, rerun the query after making change
                if (view.tabs.tabs[tab_index].data['options']['automatic_execution']) {
                    if($row_dropzone.find('li.d_measure, li.d_dimension').length > 0 && $column_dropzone.find('li.d_measure, li.d_dimension').length > 0) {
                        model.run_query(tab_index);
                    }
                }

                view.check_toolbar();
            }
        });

        /**
         * Active dimension and measure trees.
         */
        function init_trees($tab) {
            /** Activate hide and show on trees. */
            $tab.find('.dimension_tree').find('ul li ul').hide();
            /** When the root item is clicked show it's children. */
            $tab.find('.root').click(function(e) {
                e.preventDefault();
                $(this).parent().find('ul').toggle();
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
        }

        /**
         * Add a dimension.
         * @param id {String} The rel attribute of the link being clicked which
         * identifies the dimension.
         */
        function add_dimension($tab, id) {

            /* Make sure we are referencing the tab being used */
            $dimension_tree = $tab.find('.dimension_tree');

            /** Find the parent dimension id. */
            var parent_id = id.split('_')[0];
            /** Disable all of the dimension's siblings and highlight the dimension being used. */
            $dimension_tree.find('[rel=' + id + ']').parent().addClass('used')
            .parent().children().removeClass('ui-draggable').addClass('not-draggable');
            /** Highlight the dimension's parent being used. */
            $dimension_tree.find('[rel=' + parent_id + ']').parent().addClass('used');
            /** Collapse the dimension parent if it is exapnded. */
            if ($dimension_tree.find('[rel=' + parent_id + ']').parent().hasClass('expand')) {
                /** Toggle the children of the dimension's parent. */
                $dimension_tree.find('[rel=' + parent_id + ']').parent().parent().find('ul').toggle();
                /** Style the parent dimension. */
                $dimension_tree.find('[rel=' + parent_id + ']').parent()
                .removeClass('expand').addClass('collapsed')
                .find('a.folder_expand').removeClass('folder_expand').addClass('folder_collapsed');
            }
            view.check_toolbar();
        }
         
        /**
         * Remove a dimension.
         * @param id {String} The rel attribute of the dimension being removed.
         */
        function remove_dimension($tab, id) {

            /* Make sure we are referencing the tab being used */
            $dimension_tree = $tab.find('.dimension_tree');

            /** Find the parent dimension id. */
            var parent_id = id.split('_')[0];
            /** Enable all of the dimension's siblings and unhighlight the dimension being used. */
            $dimension_tree.find('[rel=' + parent_id + ']').parent().removeClass('used').parent().find('li')
            .removeClass('not-draggable').addClass('ui-draggable');
            /** Remove the dimension's highlighted parent. */
            $dimension_tree.find('[rel=' + id + ']').parent().removeClass('used');
            /** Collapse the dimension parent if it is exapnded. */
            if ($dimension_tree.find('[rel=' + parent_id + ']').parent().hasClass('expand')) {
                /** Toggle the children of the dimension's parent. */
                $dimension_tree.find('[rel=' + parent_id + ']').parent().parent().find('ul').toggle();
                /** Style the parent dimension. */
                $dimension_tree.find('[rel=' + parent_id + ']').parent()
                .removeClass('expand').addClass('collapsed')
                .find('a.folder_expand').removeClass('folder_expand').addClass('folder_collapsed');
            }
            view.check_toolbar();
        }

        /**
         * Add a measure.
         * @param id {String} The rel attribute of the link being clicked which
         * identifies the measure.
         */
        function add_measure($tab, id) {

            /* Make sure we are referencing the tab being used */
            $measure_tree = $tab.find('.measure_tree');

            /** Disable and highlight the measure. */
            $measure_tree.find('[rel=' + id + ']').parent()
            .removeClass('ui-draggable').addClass('used not-draggable');
            $measure_tree.find('.root').addClass('used');
            view.check_toolbar();
        }

        /**
         * Remove a measure.
         * @param id {String} The rel attribute of the measure being removed.
         * @param is_drop {Boolean} If the measure is being dropped.
         */
        function remove_measure($tab, id, is_drop) {

            /* Make sure we are referencing the tab being used */
            $measure_tree = $tab.find('.measure_tree');

            /** Disable and highlight the measure. */
            $measure_tree.find('[rel=' + id + ']').parent()
            .removeClass('used not-draggable').addClass('ui-draggable');
            if ($both_dropzones.find('.d_measure').length == 0) {
                $measure_tree.find('.root').removeClass('used');
            }else if ($both_dropzones.find('.d_measure').length == 1 && is_drop) {
                $measure_tree.find('.root').removeClass('used');
            }
            view.check_toolbar();
        }

    },

    /**
     * Displays a waiting message and blocks the user from performing any actions.
     * @param msg {String} Message to be displayed to the user.
     * @param block_div {Boolean} If blocking a specific tab or the whole viewport.
     * @param tab_index {Integer} Index of the active tab.
     */
    show_processing : function (msg, block_div, tab_index) {
        if(block_div) {
            $active_tab = view.tabs.tabs[tab_index].content;
            $active_tab.unblock();
            $active_tab.block({
                message: '<div class="processing"><div class="processing_inner"><span class="processing_image">&nbsp;</span>' + msg + '</div></div>',
                overlayCSS:  {
                    backgroundColor: '#FFF',
                    opacity:         0.5
                }
            });
        }else{
            $.unblockUI();
            $.blockUI({
                message: '<div class="processing"><div class="processing_inner"><span class="processing_image">&nbsp;</span>' + msg + '</div></div>',
                overlayCSS:  {
                    backgroundColor: '#FFF',
                    opacity:         0.5
                }
            });
        }
    },

    /**
     * Hides the waiting message.
     * @param block_div {Boolean} If blocking a specific tab or the whole viewport.
     * @param tab_index {Integer} Index of the active tab.
     */
    hide_processing : function(block_div, tab_index) {
        if (block_div) {
            view.tabs.tabs[tab_index].content.unblock();
        }else{
            $.unblockUI();
        }
    },

    /**
     * Load views into a dialog template
     * @param url {String} The url where the view is located.
     */
    show_view : function(url, callback) {
        // Append a dialog <div/> to the body.
        $('<div id="dialog" class="dialog hide" />').appendTo('body');
        // Load the view into the dialog <div/> and disable caching.
        $.ajax({
            url : BASE_URL + url,
            cache : false,
            dataType : "html",
            success : function(data) {
                $('#dialog').html(data).modal({
                    opacity : 100,
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
                
                if (callback)
                    callback();
            }
        });
    },

    /**
     * Loads a pop up dialog box for alerting.
     * @param title {String} Title to be displayed in the dialog box.
     * @param message {String} Message to be displayed in the dialog box.
     */
    show_dialog : function (title, message, type) {

        // Check if there is already a dialog box
        if($('#dialog').length > 0) {
        // Do nothing
        }else {
            // Append a dialog <div/> to the body.
            $('<div id="dialog" class="dialog hide">').appendTo('body');
            // Add the structure of the dialog.
            $('#dialog').append('<div class="dialog_inner">' +
                '<div class="dialog_header">' +
                '<h3>' + title + '</h3>' +
                '<a href="#" title="Close" class="close_dialog close">Close</a>' +
                '<div class="clear"></div>' +
                '</div>' +
                '<div class="dialog_body_' + type + '">' + message + '</div>' +
                '<div class="dialog_footer calign"><input type="button" class="close" value="&nbsp;OK&nbsp;" />' +
                '</div>' +
                '</div>').modal({
                opacity : 100,
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
    },


    /**
     * Check if the toolbar can be enabled or disabled.
     */
    check_toolbar: function() {

        /* Make sure we are referencing the tab being used */
        $tab = $('#tab_panel').find('.tab:visible');
        $column_dropzone = $tab.find('.columns ul');
        $row_dropzone = $tab.find('.rows ul');

        if($row_dropzone.find('li.d_measure, li.d_dimension').length > 0 && $column_dropzone.find('li.d_measure, li.d_dimension').length > 0) {
            $tab.find('.workspace_toolbar').removeClass('disabled_toolbar');
        }else{
            $tab.find('.workspace_toolbar').addClass('disabled_toolbar');
            // Lets clear the .workspace result as well
            $tab.find('.workspace_results').html('');
        }
    }

};

view.init();
