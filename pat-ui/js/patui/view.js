/*
 * This is the view for PATui. This will handle the drawing of the UI.
 */

var view = {
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
    
    logout: function() {
        $("#header").hide();
        $("#wrapper").hide();
    },
	
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
	
    free: function() {
        // Unblock UI
        $.unblockUI();
    },
    
    generate_navigation: function($connections) {
        view.processing("Loading schema...");
    	
        // Iterate over connections and populate navigation
        $.each(model.connections.connections.connection, function(i,connection){
            $.each(connection.schemas, function(i,schema){
                $('.data_list').append('<optgroup label="'+schema['@schemaname']+'">');
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
                        .appendTo($('.data_list'));
                    });
                });
                $('.data_list').append('</optgroup>');
            });
        });
        
        $('.data_list').change(function() {
            model.new_query($(this).find("option:selected"));
        });
        
        $("#header").show();
        $("#wrapper").show();
        
        view.free();
    }
};

view.drawUI();
