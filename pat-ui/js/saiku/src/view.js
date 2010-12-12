/**
 * @fileOverview    This represents the view for Saiku UI.
 * @description     This will handle the drawing of the UI.
 * @version         1.0.0
 */

// Enable debugging
enable_debug = true;

/** Resize the height of the layout to keep consistant columns. */
function resize_height() {
    // Get the browser's current height
    var window_height = $(window).height();
    // When the height of the browser is less than 600px set a height of 600px.
    if (window_height <= 600) {
        window_height = 600;
    }
    // Add 1px to tabs height for tab_panel border-top: 1px solid #CCC
    var sidebar_offset = ( $('#toolbar').outerHeight(true) + ($('#tabs').outerHeight(true) + 4) ),
    sidebar_height = window_height - sidebar_offset,
    workspace_height = sidebar_height - 20;

    $('.sidebar, .sidebar_separator').css('height', sidebar_height);
    $('.workspace_inner').css('height', workspace_height);
}

/** Toggle (hide/show) the sidebar. */
function toggle_sidebar() {
    // TODO - Has to be for selected tab only.
    // Get the width of the sidebar.
    var sidebar_width = $('.sidebar').width();
    // If the sidebar is not hidden.
    if (sidebar_width == 260) {
        $('.sidebar').css('width', 0);
        $('.workspace_inner').css('margin-left', 5);
    } else {
        // If the sidebar is hidden.
        $('.sidebar').css('width', 260);
        $('.workspace_inner').css('margin-left', 265);
    }
}

/**
 * Object for containing tab metadata.
 * @class
 */
var Tab = function(tab_selector, content_selector) {
    this.tab = tab_selector;
    this.content = content_selector;
    this.data = {};
}

/**
 * Container object used for tracking tabs.
 * @class
 */
var TabContainer = function(tab_container, content_container) {
    $("<ul />").appendTo(tab_container);
    this.tab_container = tab_container.find("ul");
    this.content_container = content_container;
    this.tabs = new Array();
    this.selected = 0;

    /** Counts active tabs. */
    this.active_tabs = function() {
        active_tabs = 0;
        for (i = 0; i < this.tabs.length; i++) {
            if (typeof this.tabs[i] != "undefined") {
                active_tabs++;
            }
        }
        return active_tabs;
    }

    /** Remove a tab and reclaim memory. */
    this.remove_tab = function(index) {
        if (typeof this.tabs[index] != "undefined") {
            this.tabs[index].tab.remove();
            this.tabs[index].content.remove();
            delete this.tabs[index].data;
            delete this.tabs[index];
        }
        // Find the next tab and select it.
        for (next_tab = index; next_tab < this.tabs.length; next_tab++) {
            if (typeof this.tabs[next_tab] != "undefined") {
                this.select_tab(next_tab);
                return;
            }
        }
        // Check and make sure there are any tabs at all.
        if (this.active_tabs() == 0 && model.session_id != "") {
            // Create one if not.
            this.add_tab();
            return;
        }
        // If the last tab was removed, select the next to last tab.
        this.select_tab(this.index_from_tab(this.tab_container.find("li:last")));
    };

    /** Change the selected tab. */
    this.select_tab = function(index) {
        if (typeof this.tabs[index] != "undefined") {
            this.tab_container.find("li.selected").removeClass("selected");
            this.content_container.find(".tab").hide();
            this.tabs[index].tab.addClass("selected");
            resize_height();
            this.tabs[index].content.show();
            this.selected = index;
        }
    };

    /** Add a tab. */
    this.add_tab = function() {
        // Create the tab itself
        var new_index = this.tabs.length;
        var $new_tab = $("<li />");
        var $new_tab_link = $("<a />")
        .html("Unsaved query (" + (new_index + 1) + ")")
        .appendTo($new_tab);
        var $new_tab_closer = $("<span>Close Tab</span>")
        .addClass("close_tab")
        .appendTo($new_tab);
        $new_tab.appendTo(this.tab_container);

        // Create the content portion of the tab.
        $new_tab_content = $('<div />')
        .addClass("tab")
        .load(BASE_URL + "views/queries/", function() {
            view.load_cubes(new_index);
            resize_height();
        });
        $new_tab_content.appendTo(this.content_container);

        // Register the new tab with the TabContainer.
        this.tabs.push(new Tab($new_tab, $new_tab_content));
        this.select_tab(new_index);

    };

    /** Empty the tab container (used for logout) */
    this.clear_tabs = function() {
        for (i = 0; i < this.tabs.length; i++) {
            if (typeof this.tabs[i] != 'undefined') {
                this.remove_tab(i);
            }
        }
    };

    /** Determine whether or not the TabContainer is empty .*/
    this.is_empty = function() {
        // If the array is uninitialized, obviously return true
        if (this.tabs.length == 0)
            return true;
        // Check to see if there are any active tabs
        for (i = 0; i < this.tabs.length; i++) {
            if (typeof this.tabs[i] != 'undefined') {
                // An active tab still exists, return false
                return false;
            }
        }
        // If not, return true
        return true;
    };

    /** Get a tab_index from a tab instance. */
    this.index_from_tab = function($tab) {
        for (i = 0; i < this.tabs.length; i++) {
            if (typeof this.tabs[i] != "undefined" && $tab[0] == this.tabs[i].tab[0]) {
                return i;
            }
        }
        return -1;
    };

    /** Get a tab_index from a tab content. */
    this.index_from_content = function($content) {
        for (i = 0; i < this.tabs.length; i++) {
            if (typeof this.tabs[i] != "undefined" && $content[0] == this.tabs[i].content[0]) {
                return i;
            }
        }
        return -1;
    };
};

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
            url : BASE_URL + 'views/session',
            cache : false,
            dataType : "html",
            success : function(data) {
                $('#dialog').html(data).modal({
                    opacity : 100,
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

                        // Create the session and log in.
                        model.get_session();
                    }
                });
            }
        });
    },

    /** Tabs container. */
    tabs : new TabContainer($("#tabs"), $('#tab_panel')),

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
            resize_height();
        });

        /** Bind toggle_sidebar() to click event on the sidebar_separator. */
        $('.sidebar_separator').live('click', function() {
            toggle_sidebar();
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

    /** Initialise resize_height() on first page load. */
    //resize_height();

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
        view.start_waiting('Loading available schemas and cubes');
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
                    //if (cube.length == undefined)
                    //    cube = [cube];
                    //$.each(cube, function(i,item){
                    $("<option />")
                    .attr({
                        'value': storage_id
                    })
                    .text(cube['cubeName'])
                    .appendTo($cubes);
                    view.tabs.tabs[tab_index].data['navigation'][storage_id] = {
                        'connectionName': connection['name'],
                        'catalogName': connection.catalogs[0]['name'],
                        'schema': schema['name'],
                        'cube': cube['cubeName']
                    };
                    storage_id++;
                //});
                });
                $cubes.append('</optgroup>');
            });
        });
        view.stop_waiting();

        $cubes.change(function() {
            model.new_query(tab_index);
        });
    },

    /**
     * Populate the dimension tree for the selected tab.
     * @param $tab {Object} Selected tab content.
     * @param data {Object} Data object which contains the available dimension
     *                      members.
     */
    load_dimensions : function($tab, data) {
        // Remove any instances of a tree.
        $tab.find('.dimension_tree ul').remove();
        // Add a new dimension tree.
        $dimension_tree = $('<ul />').appendTo($tab.find('.dimension_tree')).addClass('dtree');
        // Populate the tree with first level dimensions.
        $.each(data, function(i, dimension) {
            if (this['name'] != 'Measures') {
                // Make sure the first level has a unique rel attribute.
                $first_level = $('<li><span class="root collapsed"><a href="#" rel="d' + i + '" class="folder_collapsed">' + this['name'] + '</a></span></li>')
                .appendTo($dimension_tree);
                // Store the dimension name for the (All) level
                var dimension_name = this['name'];
                if (dimension.hierarchies[0].levels.length > 1) {
                    $second_level = $('<ul />').appendTo($first_level);
                    $.each(dimension.hierarchies[0].levels, function(j, level){
                        $li = $('<li />').mousedown(function() {
                            return false;
                        }).appendTo($second_level);
                        // Check if the dimension level is (All) if so display the All dimension_name instead.
                        if (level['caption'] === '(All)') {
                            // Create a parent-child relationship with the rel attribute.
                            $second_level_link = $('<a href="#" class="dimension" rel="d' + i + '_' + j + '" title="' + level['uniqueName'] + '"> All ' + dimension_name + '</a>')
                            .appendTo($li);
                        }else{
                            // Create a parent-child relationship with the rel attribute.
                            $second_level_link = $('<a href="#" class="dimension" rel="d' + i + '_' + j + '" title="' + level['uniqueName'] + '">' + level['caption'] + '</a>')
                            .appendTo($li);
                        }
                    });
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
    load_measures : function($tab, data) {
        // Remove any instances of a tree.
        $tab.find('.measure_tree ul').remove();
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
     * Displays a waiting dialog box.
     * @param message {String} Waiting message to be displayed.
     */
    start_waiting : function (message) {
        $('.waiting_message').html(message);
        $('.waiting').show();
    },
    
    /** Removes a waiting dialog box. */
    stop_waiting : function () {
        $('.waiting_message').html('');
        $('.waiting').hide();
    },

    /**
     * Load views into a dialog template
     * @param url {String} The url where the view is located.
     */
    show_view : function(url) {
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
            }
        });
    },

    /**
     * Loads a pop up dialog box for alerting.
     * @param title {String} Title to be displayed in the dialog box.
     * @param message {String} Message to be displayed in the dialog box.
     */
    show_dialog : function (title, message, type) {
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
    
}

/** Initialise the user interface. */
view.init();