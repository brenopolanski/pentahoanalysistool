/*
 * This is the view for PATui. This will handle the drawing of the UI.
 */

var view = {
    /*
     * This is where you're going to put your initial UI drawing
     */
    drawUI: function() {
	
        var $tabs, $inner_tabs;
        var outer_layout, tabs_container_layout;

        function resize_tab_panel_layout () {
            var tabIndex = $tabs.tabs("option", "selected")
            // Make sure is 'visible'
            , $tab_panel = $("#query"+ (tabIndex + 1)).show()
            , tab_layout;
            // If tab_layout exists - get instance and call resizeAll
            if ($tab_panel.data("layoutContainer")) {
                // Resize the layout-panes - if required
                tab_layout = $tab_panel.layout();
                tab_layout.resizeAll();
            }
            // Else if tab_layout does not exist yet, create it now
            else {
                // if (ui.index > 0) // panel #0 layout is initialized in document.ready
                tab_layout = $tab_panel.layout(tab_layout_options);
            }
            return;
        };


        var tab_layout_options = {
            resizeWithWindow:         false,
            north__resizable:         false,
            north__closable:          false,
            spacing_open:             4,
            spacing_closed:           4,
            center__paneSelector:     "#tab_body",
            west__paneSelector:       "#sidebar",
            west__size:               "250",
            contentSelector:          ".tab_scroll_content"
        };

        $(document).ready(function(){

            outer_layout = $("body").layout({
                // delay calling resizeAll when window is *still* resizing
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

            tabs_container_layout = $("#wrapper").layout( {
                resizable:              false,
                slidable:               false,
                closable:               false,
                north__closable:        false,
                north__resizable:       true,
                north__size:            30,
                north__paneSelector:	"#tab_list",
                center__paneSelector:	"#tab_content",
                spacing_open:		0,
                fxName:                 "slide",
                fxSpeed:                0.00001,
                center__onresize:       resize_tab_panel_layout
            });

            window.tabs_loading = true;
            // Set object BEFORE initializing tabs because is used during init

            $tabs = $("#wrapper");
            $tabs.tabs({
                show: function (evt, ui) {
                    // Need to resize layout after tabs init,
                    // but before creating inner tabPanelLayout
                    if (tabs_loading) {
                        tabs_loading = false;
                        tabs_container_layout.resizeAll();
                    // ResizeAll will trigger center.onresize = resize_tab_panel_layout()
                    }
                    else
                        // Resize the INNER-layout each time it becomes 'visible'
                        resize_tab_panel_layout(ui);
                }
            });

            // If close button is clicked
            $('#querylist_container li a img').click(function() {
                alert('Are you sure you want to do that?');
                var index = $('li',$tabs).index($(this).parent());
                $tabs.tabs('remove', index);
            });

            // Query toolbar events
            // TODO - This should be part of the newQuery method
            $('#col_axis, #row_axis, #filter_axis').hide();
            $('#toggle_axis').click(function(){
                var showAxisText = 'Show Axis';
                var hideAxisText = 'Hide Axis';

                if ($(this).text() === showAxisText) {
                    $('#col_axis, #row_axis').show();
                    $(this).text(hideAxisText);
                    return false;
                } else {
                    $('#col_axis, #row_axis').hide();
                    $(this).text(showAxisText);
                    return false;
                }
            });

            $('#toggle_filter').click(function(){
                var showFilterText = 'Show Filters';
                var hideFilterText = 'Hide Filters';

                if ($(this).text() === showFilterText) {
                    $('#filter_axis').show();
                    $(this).text(hideFilterText);
                    return false;
                } else {
                    $('#filter_axis').hide()
                    $(this).text(showFilterText);
                    return false;
                }
            });
            
            // Add event handler to toolbar buttons
            $("#toolbar a").click(function() {
            	controller.click_handler($(this));
            });
                
        });
    },
	
    processing: function(message) {
    	// Block the UI
    	// TODO - handle the case that the UI is already blocked
    	$.blockUI({
            message: '<div class="blockOverlay-inner"><p>' + message + '</p></div>'
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
                $('#data-list').append('<optgroup label="'+schema['@schemaname']+'">');
                $('#blockOverlay-update').html('schema '+schema['@schemaname']);
                $.each(schema.cubes, function(i,cube){
                    $('#blockOverlay-update').html('cube '+cube['@cubename']);
                    if(cube.length == undefined) { 
                        $('#data-list').append('<option value="'+connection['@connectionid']+'|'+schema['@schemaname']+'|'+cube['@cubename']+'">'+cube['@cubename']+'</option>');
                    }else{
                        $.each(cube, function(i,item){
                            $('#data-list').append('<option value="'+connection['@connectionid']+'|'+schema['@schemaname']+'|'+item['@cubename']+'">'+item['@cubename']+'</option>');
                        });
                    }
                });
                $('#data-list').append('</optgroup>');
            });
        });
        
        view.free();
    }
};

view.drawUI();