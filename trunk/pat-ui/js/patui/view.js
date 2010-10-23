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
		    $.blockUI({
		        message: '<div class="blockOverlay-inner">'+PAT_TITLE+'<br/>Loading <span id="blockOverlay-update">schemas and cubes...</span></div>'
		    });
		    
		    //  Intialise jQuery UI Layout
		    $('body').layout({
		        north__closable:          false,
		        north__resizable:         false,
		        north__spacing_open:      4,
		        north__spacing_closed:    4,
		        north__size:              30,
		        west__spacing_open:       4,
		        west__spacing_closed:     4,
		        west__size:               250,
		        west__resizable:          true
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