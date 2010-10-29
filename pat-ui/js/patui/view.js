/*
 * This is the view for PATui. This will handle the drawing of the UI.
 */

var view = {	
	/*
	 * The number of tabs that have been rendered, for naming new tabs
	 */
	rendered_tabs: 0,
	
    /*
     * This is where you're going to put your initial UI drawing
     */
    drawUI: function() {

        var $tabs, outer_layout, tab_layout_options;

        // Resizes the tab panel when 'show' event is triggered
        function resize_tab_panel_layout (ui) {
            var tabIndex = $tabs.tabs("option", "selected"),
            $tab_panel = $( "#query"+ (tabIndex + 1) );
            if ($tab_panel.data("layoutContainer")) {
                $tab_panel.layout().resizeAll();
            } else {
                $tab_panel.layout(tab_layout_options);
            }
            return;
        }

        // Layout options for the contents inside the tab
        tab_layout_options = {
            name:                       'tab_layout_options',
            resizeWithWindow:           false,
            north__resizable:           false,
            north__closable:            false,
            spacing_open:               4,
            spacing_closed:             4,
            center__paneSelector:       ".tab_body",
            west__paneSelector:         ".sidebar",
            west__size:                 "250",
            contentSelector:            ".tab_scroll_content",
            west__resizable:            true,
            west__closable:             true
        };

        $(document).ready(function(){

            outer_layout = $("body").layout({
                name:                   'outer_layout',
                resizeWithWindowDelay:	250,
                closable:		false,
                resizable:		false,
                spacing_open:    	0,
                spacing_closed: 	0,
                center__paneSelector:	"#tab_content",
                north__paneSelector:	"#header",
                center__onresize:       resize_tab_panel_layout
            });
            
            window.tabs_loading = true;

            $tabs = $("#tab_list");
            $tabs.tabs({
                show: function (evt, ui) {
                    if (tabs_loading) {
                        tabs_loading = false;
                        outer_layout.resizeAll();
                    }
                    resize_tab_panel_layout(ui);
                }
            });

            // Add event handler to toolbar buttons
            $("#toolbar a").click(function() {
                controller.click_handler($(this));
            });

            view.logout();
        });
    },
    
    /*
     * Clear credentials and clear the screen
     */
    logout: function() {
    	// Remove all tabs
    	$('#tab_list ul li').remove();
    	$('#tab_content div').remove();
    	
    	// Hide the layout
        $("#header").hide();
        $("#tab_content").hide();
    },
	
    /*
     * Block the UI and display a status message
     */
    processing: function(message) {
        // Block the UI
        $.blockUI({
            message: '<div class="loading_wrapper">'
            + '<div class="loading_inner">'
            + '<div class="loading_body">'
            + '<img src="images/global/loading.gif" alt="Loading..." /><br />'
            + '<span>' + message + '</span>'
            + '</div>'
            + '</div>'
            + '</div>'
        });
    },
	
    /*
     * Free the UI for interaction again
     */
    free: function() {
        // Unblock UI
        $.unblockUI();
    },
    
    /*
     * Load data list with schema and cubes from the PAT server
     */
    generate_navigation: function($tab) {
        $data_list = $tab.find('.data_list');
    	
    	// Iterate over connections and populate navigation
        $.each(model.connections.connections.connection, function(i,connection){
            $.each(connection.schemas, function(i,schema){
            	$data_list.append('<optgroup label="'+schema['@schemaname']+'">');
                $.each(schema.cubes, function(i,cube){
                    if(cube.length == undefined)
                        cube = [cube];
                    $.each(cube, function(i,item){
                        $("<option />")
                        .attr({
                            'value': connection['@connectionid']
                        })
                        .data({
                            'schema': schema['@schemaname'],
                            'cube': item['@cubename']
                        })
                        .text(item['@cubename'])
                        .appendTo($data_list);
                    });
                });
                $data_list.append('</optgroup>');
            });
        });
        
        // Show the window (hidden first time)
        $("#header").show();
        $("#tab_content").show();
        


        /* NEW - Start */

        var $tabs, outer_layout, tab_layout_options;

        // Resizes the tab panel when 'show' event is triggered
        function resize_tab_panel_layout (ui) {
            var tabIndex = $tabs.tabs("option", "selected"),
            $tab_panel = $( "#query"+ (tabIndex + 1) );
            if ($tab_panel.data("layoutContainer")) {
                $tab_panel.layout().resizeAll();
            } else {
                $tab_panel.layout(tab_layout_options);
            }
            return;
        }

        // Layout options for the contents inside the tab
        tab_layout_options = {
            name:                       'tab_layout_options',
            resizeWithWindow:           false,
            north__resizable:           false,
            north__closable:            false,
            spacing_open:               4,
            spacing_closed:             4,
            center__paneSelector:       ".tab_body",
            west__paneSelector:         ".sidebar",
            west__size:                 "250",
            contentSelector:            ".tab_scroll_content",
            west__resizable:            true,
            west__closable:             true
        };


            outer_layout = $("body").layout({
                name:                   'outer_layout',
                resizeWithWindowDelay:	250,
                closable:		false,
                resizable:		false,
                spacing_open:    	0,
                spacing_closed: 	0,
                center__paneSelector:	"#tab_content",
                north__paneSelector:	"#header",
                center__onresize:       resize_tab_panel_layout
            });

            window.tabs_loading = true;

            $tabs = $("#tab_list");
            $tabs.tabs({
                show: function (evt, ui) {
                    if (tabs_loading) {
                        tabs_loading = false;
                        outer_layout.resizeAll();
                    }
                    resize_tab_panel_layout(ui);
                }
            });
        
        //$tabs.tabs( "enable");
        $('#tab_list').tabs( "select", $('#tab_list ul li').length - 1 );

        /* NEW - End */
        
        $data_list.change(function() {
            model.new_query($tab, $data_list.find("option:selected"));
        });
    },
    
    new_tab: function() {
    	// Increment the number rendered tabs
        view.rendered_tabs++;

    	var tab_id = 'query' + view.rendered_tabs;
    	
        // Add a new <li/> element to the #tab_list ul (tab)
        $new_tab_index = $('<li />').addClass('ui-state-default ui-corner-top');
        $new_tab_link = $('<a />').attr({
        		'href': '#' + tab_id,
        		'rel': 'new'})
        	.addClass('closable')
        	.text('Unsaved Query')
        	.appendTo($new_tab_index);
        $new_tab_close_button = $('<img />').attr({ 'alt': 'Close tab', 'src': 'images/tab/close.png' })
        	.click(function() {
                    /* NEW - Start */
                        var index = $('#tab_list').tabs('option', 'selected');
        		view.delete_tab(index);
                    /* NEW - End */
        	})
        	.appendTo($new_tab_link);
        $new_tab_index.appendTo($('#tab_list ul'));
                             
        // Add a new <div/> element to the #tab_content <div/> (tab panel)
        $new_tab = $('<div />').attr({ 'id': tab_id })
        	.addClass('tab ui-tabs-panel ui-widget-content ui-corner-bottom ui-tabs-hide')
        	.appendTo($('#tab_content'));
        
    	$.get('views/_new_query.html', function(data) {
    		$new_tab.html(data); //.show();
    		view.generate_navigation($new_tab)
    	});
    },
    
    delete_tab: function(index) {
        /* NEW - Start */
    	$('#tab_list').tabs("remove", index);
        //$('#tab_list ul').append('<li></li>');
        /* NEW - End */
    }
};

view.drawUI();
