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
	$("<ul />").appendTo(tab_container);
	this.tab_container = tab_container.find("ul");
	this.content_container = content_container;
	this.tabs = new Array();
	this.selected = 0;
	
	/*
	 * Counts active tabs
	 */
	this.active_tabs = function() {
		active_tabs = 0;
		for (i = 0; i < this.tabs.length; i++) {
			if (typeof this.tabs[i] != "undefined") {
				active_tabs++;
			}
		}
		
		return active_tabs;
	}
	
	/*
	 * Remove a tab and reclaim memory
	 */
	this.remove_tab = function(index) {
		if (typeof this.tabs[index] != "undefined") {
			this.tabs[index].tab.remove();
			this.tabs[index].content.remove();
			delete this.tabs[index].data;
			delete this.tabs[index];
		}
		
		// Find the next tab and select it
		for (next_tab = index; next_tab < this.tabs.length; next_tab++) {
			if (typeof this.tabs[next_tab] != "undefined") {
				this.select_tab(next_tab);
				return;
			}
		}
		
		// Check and make sure there are any tabs at all
		if (this.active_tabs() == 0) {
			// Create one if not
			this.add_tab();
			return;
		}
		
		// If the last tab was removed, select the next to last tab
		this.select_tab(this.index_from_tab(this.tab_container.find("li:last")));
	};
	
	/*
	 * Change the selected tab
	 */
	this.select_tab = function(index) {
		if (typeof this.tabs[index] != "undefined") {
			this.tab_container.find("li.ui-tabs-selected").removeClass("ui-tabs-selected");
			this.content_container.find(".tab").hide();
			this.tabs[index].tab.addClass("ui-tabs-selected");
			this.tabs[index].content.show();
			this.selected = index;
		}
	};
	
	/*
	 * Add a tab
	 */
	this.add_tab = function() {
		// Create the tab itself
		var new_index = this.tabs.length;
		var $new_tab = $("<li />").addClass("closable");
		var $new_tab_link = $("<a />")
			.html("Unsaved query (" + (new_index + 1) + ")")
			.appendTo($new_tab);
		var $new_tab_closer = $("<span />")
			.appendTo($new_tab);
		$new_tab.appendTo(this.tab_container);
		
		// Create the content portion of the tab
		$new_tab_content = $('<div />')
			.addClass("tab")
			// FIXME - this is preventing caching atm
			.load("views/_new_query.html", function() {
				view.generate_navigation(new_index);
			});
		$new_tab_content.appendTo(this.content_container);
		
		// Register the new tab with the TabContainer
		this.tabs.push(new Tab($new_tab, $new_tab_content));
		this.select_tab(new_index);
	};
	
	/*
	 * Completely empty the tab container
	 * (Used for logout)
	 */
	this.clear_tabs = function() {
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
	
	/*
	 * Get a tab_index from a tab instance
	 */
	this.index_from_tab = function($tab) {
		for (i = 0; i < this.tabs.length; i++) {
			if (typeof this.tabs[i] != "undefined" && $tab[0] == this.tabs[i].tab[0]) {
				return i;
			}
		}
		
		return -1;
	};
	
	/*
	 * Get a tab_index from a tab instance
	 */
	this.index_from_content = function($content) {
		for (i = 0; i < this.tabs.length; i++) {
			if (typeof this.tabs[i] != "undefined" && $content[0] == this.tabs[i].content[0]) {
				return i;
			}
		}
		
		return -1;
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
			return false;
		});
		
		// Add click handler on tabs
		view.tabs.tab_container.find("a").live('click', function() {
			view.tabs.select_tab(view.tabs.index_from_tab($(this).parent()));
			return false;
		});
		
		view.tabs.tab_container.find("a").live('mousedown', function(event) {
			if (event.which == 2) {
				view.tabs.remove_tab(view.tabs.index_from_tab($(this).parent()));
			}
			return false;
		});
		
		// Add click handler on tabs
		view.tabs.tab_container.find("span").live('click', function() {
			view.tabs.remove_tab(view.tabs.index_from_tab($(this).parent()));
			return false;
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
		this.tabs.clear_tabs();
    	
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
	generate_navigation: function(tab_index) {
		$tab = view.tabs.tabs[tab_index].content;
		$data_list = $tab.find('.data_list');
		view.tabs.tabs[tab_index].data['navigation'] = new Array();
		storage_id = 0;
    	
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
							'value': storage_id
						})
						.text(item['@cubename'])
						.appendTo($data_list);
						
						view.tabs.tabs[tab_index].data['navigation'][storage_id] = {
							'connection_id': connection['@connectionid'],
							'schema': schema['@schemaname'],
							'cube': item['@cubename']
						};
						storage_id++;
						
					});
				});
				$data_list.append('</optgroup>');
			});
		});
        
		$data_list.change(function() {
			model.new_query(tab_index);
		});
	},
    
	new_tab: function() {
		this.tabs.add_tab();
	}
};

view.drawUI();
