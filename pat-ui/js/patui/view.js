/*
 * This is the view for PATui. This will handle the drawing of the UI.
 */

var view = {
    /*
     * This is where you're going to put your initial UI drawing
     */
    drawUI: function() {
	
        var $tabs;
        var outer_layout, tabs_container_layout;

        function resize_tab_panel_layout () {
            var tabIndex = $tabs.tabs("option", "selected")
            , $tab_panel = $("#query"+ (tabIndex + 1)).show()
            , tab_layout;
            if ($tab_panel.data("layoutContainer")) {
                tab_layout = $tab_panel.layout();
                tab_layout.resizeAll();
            } else {
                tab_layout = $tab_panel.layout(tab_layout_options);
            }
            return;
        }

        var tab_layout_options = {
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
                resizeWithWindowDelay:	250,
                resizable:		false,
                slidable:		false,
                closable:		false,
                north__paneSelector:	"#header",
                north__closable:        false,
                north__resizable:       false,
                north__spacing_open:    0,
                north__spacing_closed:  0,
                center__paneSelector:	"#wrapper"
            });
            tabs_container_layout = $("#wrapper").layout({
                name:                   "tabs_container_layout",
                resizable:              false,
                slidable:               false,
                closable:               false,
                north__size:            30,
                north__paneSelector:	"#tab_list",
                center__paneSelector:	"#tab_content",
                spacing_open:		0,
                fxName:                 "none",
                center__onresize:       resize_tab_panel_layout
            });
            
            window.tabs_loading = true;

            $tabs = $("#tab_list");
            $tabs.tabs({
                show: function (evt, ui) {
                    if (tabs_loading) {
                        tabs_loading = false;
                        tabs_container_layout.resizeAll();
                    } else {
                        resize_tab_panel_layout(ui);
                    }
                }
            });

            // When a tab is requested to be closed
            $('#tab_list ul li a img').click(function() {
                alert('Are you sure you want to do that?');
                var index = $('li',$tabs).index($(this).parent());
                $tabs.tabs('remove', index);
                return false;
            });

            // Add event handler to toolbar buttons
            $("#toolbar a").click(function() {
                controller.click_handler($(this));
            });

            // Convert dimension and measure <ul/> to trees
            $(".dimension_tree, .measure_tree").jstree({
                "core" : {
                    "animation" : 0
                },
                "themes" : {
                    "theme" : false
                },
                "plugins" : [ "themes", "html_data" ]
            });
        });
    },
	
    processing: function(message) {
        // Block the UI
        // TODO - handle the case that the UI is already blocked
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
        
        view.free();
    }
};

view.drawUI();
