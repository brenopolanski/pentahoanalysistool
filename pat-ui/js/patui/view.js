/*
 * This is the view for PATui. This will handle the drawing of the UI.
 */

var view = {
    /*
	 * This is where you're going to put your initial UI drawing
	 */
    drawUI: function() {
	
        // Ensure that document has lazy loaded whole view file
        $(document).ready(function() {
			
            //  Initialise BlockUI for loading available schemas and cubes
            // $.blockUI({
            //     message: '<div class="blockOverlay-inner">'+PAT_TITLE+'<br/>Loading <span id="blockOverlay-update">schemas and cubes...</span></div>'
            // });
		    
            // Intialise jQuery UI Layout
            $('body').layout({
                fxName:                 "slide",
                fxSpeed:                0.00001,
                north__closable:        false,
                north__resizable:       false,
                north__spacing_open:    0,
                north__spacing_closed:  0,
                west__spacing_open:     4,
                west__spacing_closed:   4,
                west__resizable:        true,
                west__closable:         true,
                west__size:             250
            });

            // jQuery UI Tabs
            var $tabs = $('#querylist_container').tabs();

            // If close button is clicked
            $('#querylist_container li a img').click(function() {
                alert('Are you sure you want to do that?');
                var index = $('li',$tabs).index($(this).parent());
                $tabs.tabs('remove', index);
            });
            
        });

    // New query method controller , newQueryButtonClick
    },
	
    processing: function(message) {
    // Block the UI
    // TODO - handle the case that the UI is already blocked
    },
	
    free: function() {
    // Unblock UI
    }
};

view.drawUI();