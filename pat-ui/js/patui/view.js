/*
 * Object for storing tab metadata
 */
var Tab = function(tab_selector, content_selector) {
	this.tab = tab_selector;
	this.content = content_selector;
	this.data = {};
};

/*
 * Container object used for tracking tabs
 */
var TabContainer = function(tab_container, content_container) {
	this.tab_container = tab_container;
	this.content_container = content_container;
	this.tabs = new Array();
	this.selected = 0;
	
	/*
	 * Add a tab
	 */
	this.add = function() {
		// Create the tab itself
		var new_index = this.tabs.length;
		var $new_tab = $("<li />").addClass("closable ui-state-default ui-corner-top ui-tabs-selected ui-state-active");
		var $new_tab_link = $("<a />")
			.html("Unsaved query (" + new_index + ")")
			.click(function() {
				this.select(new_index);
				})
			.appendTo($new_tab);
		var $new_tab_closer = $("<span />")
			.click(function() {
				this.remove(new_index);
				})
			.appendTo($new_tab);
		$new_tab.appendTo(this.tab_container);
		
		// Create the content portion of the tab
		$new_tab_content = $('<div />')
			.addClass("tab ui-tabs-panel ui-widget-content ui-corner-bottom tabs-ui-hide")
			// FIXME - this is preventing caching atm
			.load("views/_new_query.html", {"_": Math.floor(Math.random()*1000000)}, function() {
				view.generate_navigation($new_tab);
			});
		$new_tab_content.appendTo(this.content_container);
		
		// Register the new tab with the TabContainer
		this.tabs.push(new Tab($new_tab, $new_tab_content));
	};
	
	/*
	 * Change the selected tab
	 */
	this.select = function(index) {
		this.content_container.find("div").hide();
		this.tabs[index].content.show();
	};
	
	/*
	 * Remove a tab and reclaim memory
	 */
	this.remove = function(index) {
		this.tabs[index].tab.remove();
		this.tabs[index].container.remove();
		delete this.tabs[index];
	};
	
	/*
	 * Completely empty the tab container
	 * (Used for logout)
	 */
	this.clear = function() {
		for (i = 0; i < this.tabs.length; i++) {
			if (typeof this.tabs[i] != 'undefined') {
				this.remove[i];
			}
		}
	};
	
	/*
	 * Determine whether or not the TabContainer is empty
	 */
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
};


/*
 * This is the view for PATui. This will handle the drawing of the UI.
 */

var view = {	
	/*
	 * Tabs container
	 */
	tabs: new TabContainer($("#tabs"), $('#tab_panel')),
	
	/*
     * This is where you're going to put your initial UI drawing
     */
	drawUI: function() {
		// Add event handler to toolbar buttons
		$("#toolbar a").click(function() {
			controller.click_handler($(this));
		});

		// Blank everything out until the user logs in
		view.logout();
	},
	
	/*
     * Prepare the screen for user input
     */
	login: function() {
		// Hide the layout
		$("#header").show();
		$("#tab_panel").show();
	},
    
	/*
     * Clear the screen
     */
	logout: function() {
		// Remove all tabs
		this.tabs.clear();
    	
		// Hide the layout
		$("#header").hide();
		$("#tab_panel").hide();
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
     * FIXME - store data in tab object somehow
     */
	generate_navigation: function($tab) {
		$data_list = $tab.find('.data_list');
		
		// TODO - Cache HTML itself
    	
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
		$("#tab_panel").show();
        
		$data_list.change(function() {
			model.new_query($tab, $data_list.find("option:selected"));
		});
	},
    
	new_tab: function() {
		this.tabs.add();
	}
};

view.drawUI();
