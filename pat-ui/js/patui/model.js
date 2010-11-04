/*
 * This is the model for the PATui. It will handle all calls to the REST API stored on the server.
 */

var model = {
    /*
     * The username to be used with HTTP basic auth
     */
    username: "",
	
    /*
     * The password to be used with HTTP basic auth
     */
    password: "",
	
    /*
     * The session_id used to make calls to the server
     */
    session_id: "",
	
    /*
     * Connection information for this PAT server
     */
    connections: {},
		
    /*
     * This is the constructor of sorts, ensuring that the session ID is valid
     */
    init: function (username) {
        // Let's be friends!
        model.server_errors = 0;
        
        // Really ghetto way to get credentials. Better than the browser prompting, I guess...
        // FIXME - ask for credentials using a pretty form that doesn't block the browser (and hides password)
        if (model.username == "" || model.password == "") {
            jPrompt("Please enter your username: ", "admin", "PAT - Login", function(input) { //FIXME - auth hardcoded
                model.username = input;
                jPrompt("Please enter your password: ", "admin", "PAT - Login", function(input) { //FIXME - auth hardcoded
                    model.password = input;
	        		
                    // Obtain a session_id
                    model.get_session();
                    view.login();
                });
            });
        }
    },
    
    /*
     * Make an ajax request
     */
    request: function(parameters) {
        if (typeof parameters.method == "undefined")
            parameters.method = "GET";
    	
        if (typeof parameters.data == "undefined")
            parameters.data = {};
    	
        if (typeof parameters.success == "undefined")
            parameters.success = function() {};
    	
        if (typeof parameters.error == "undefined")
            parameters.error = controller.server_error;
    	
        $.ajax({
            type: parameters.method,
            url: "rest/" + parameters.url,
            dataType: 'json',
            username: model.username,
            password: model.password,
            success: parameters.success,
            error: parameters.error
        });
    },
	
    /*
     * Obtain a session and load connections
     */
    get_session: function() {
        model.request({
            method: "POST",
            url: model.username + "/session",
            success: function(data, textStatus, XMLHttpRequest) {
                model.session_id = data['@sessionid'];
                model.connections = data;
                view.new_tab();
                view.login();
            }
        });
    },

    /*
     *  When a new query button is clicked create a new tab and load with
     *  a new query template.
     */
    new_tab: function() {
    	
        /*
    	 * Create the actual tab and tab index
    	 */
        view.new_tab();
    },
     
    /*
         *  Populates dimensions_tree and measures_tree with available items
         *  and enables draggable, droppable and sortable lists.
         *  
         *  FIXME - this needs some cleanup
         */
    new_query: function(tab_index) {
        // Find the selected cube
        $cube = view.tabs.tabs[tab_index].content.find(".data_list option:selected");
    	
        data = view.tabs.tabs[tab_index].data['navigation'][$cube.attr('value')];
        if (typeof data == "undefined") {
            debug("Broken tab: " + tab_index + ", cube value: " + $cube.attr('value'));
            //view.tabs.tabs[tab_index].content.html("Something broke. Please close this tab and try again on another one.");
            jAlert("Something broke. Please close this tab and try again on another one.");
            return;
        }

        view.processing("Creating new query on " + data['cube']);
        $tab = view.tabs.tabs[tab_index].content;
    	
        model.request({
            method: "POST",
            url: model.username + "/query/" + data['schema'] + "/" + data['cube'] + "/newquery",
            success: function(data, textStatus, XMLHttpRequest) {
                // Remove all instances of previous trees
                // in case user reselects cube
                $dimension_tree = $('<ul />').addClass("tree").appendTo($tab.find('.dimensions_tree'));
                $measures_tree = $('<ul />').addClass("tree").appendTo($tab.find('.measures_tree'));
                
                $both_trees = $tab.find('.dimensions_tree, .measures_tree');

                $.each(data.axis.dimensions, function(i,dimension) {

                    // Populate dimension tree first
                    if(this['@dimensionname'] != 'Measures') {                        
                        // Append <li/> to the dimensions_tree <ul/>
                        $first_level = $('<li><a href="#">'+this['@dimensionname']+'</a></li>')
                        .addClass("dimension folder")
                        .click(function() {
                            $(this).find("ul li").toggle();
                            return false;
                        })
                        .appendTo($dimension_tree);

                        if (dimension.levels.length > 1) {
                            // Append <ul/> to the dimensions_tree <li/>
                            $second_level = $('<ul />').appendTo($first_level);
	                        
                            // If there is a secondary level loop through and add to the <ul/>
                            $.each(dimension.levels, function(i,level){
                                // Populate the second <ul/>
                                $li = $('<li />')
                                .mousedown(function() { return false; })
                                .appendTo($second_level);
                                
                                $("<a>"+level['@levelcaption']+"</a>")
                                .click(function(event) { return false; })
                                .appendTo($li);
                            });
                        }
                        
                    } else {
                        // Create a 'dummy' measures <ul/>
                        // Append <li/> to the dimensions_tree <ul/>
                        $measures = $('<li>Measures</li>')
                        .addClass("measures folder")
                        .appendTo($tab.find('.measures_tree ul'));

                        // Append <ul/> to the dimensions_tree <li/>
                        $measures_ul = $('<ul />').appendTo($measures);
                            
                        $.each(dimension.levels.members, function(i,member){
                            // Populate the second <ul/>
                            $('<li id="'+this['@membercaption']+'"><a>'+this['@membercaption']+'</a></li>')
                            	.mousedown(function() { return false; })
                            	.appendTo($measures_ul);
                        });
                        
                    }
                });
                
                /* DND Rules:
                 * Rules:
                 * Unique items only the row and column axis
                 * You can only drag one item from the hierarchy onto 
                 * 		the row and column axis i.e. Only Region from Markets
                 * Once the above occurs you can no longer any other items 
                 * 		from the hierarchy onto the row, column, filters axis
                 * Measures must be grouped together and can only belong 
                 * 		on one axis as a group
                 * Measures can not be place on the filters axis
                 */

                //  Draggable
                $both_trees.find("li ul li a").draggable({
                    cancel: ".not-draggable",
                    
                    helper: "clone",
                    opacity: 0.90
                });

                $drop_zones = view.tabs.tabs[tab_index].content.find('.row_axis_drop ul, .col_axis_drop ul, .filter_axis_drop ul');

                //  Droppable
                $drop_zones.droppable({
                    accept: '.ui-draggable',
                    activeClass: "ui-state-hover",
                    hoverClass: "ui-state-active",
                    drop: function(event, ui) {
                        $(this).append('<li><span>'+ui.draggable.text()+'</span></li>');
                    	},
                	deactivate: function() {
                    	tab_index = view.tabs.index_from_content($(this).parent().parent().parent().parent().parent("div.tab"));
                    	view.check_query(tab_index);
                    	}
                }).sortable({
                    connectWith: $drop_zones,
                    tolerance: 'pointer',
                    placeholder: '.placeholder_sort'
                    
                });
                
                $('li', $drop_zones).draggable({
                    cancel: ".not-draggable",
                    revert: "invalid",
                    opacity: 0.90
                });

                $('.sidebar').droppable({
                    accept: '.row_axis_drop ul li, .col_axis_drop ul li, .filter_axis_drop ul li',
                    drop: function(event, ui) {
                        ui.draggable.remove();
                    }
                })
                
                //.selectable();
                // can use selectable to select and then delete using the "delete or backspace key"

                // Remove blockUI
                view.free();
            },
            error: function() {
                jAlert("Couldn't create a new query. Please try again.", "Error");
                view.free();
            }
        });
    },
    
    open_query: function() {}, //TODO - open query
    save_query: function() {}, //TODO - save query
    delete_query: function() {}, //TODO - delete query
    
    /*
     * Kill credentials and server-side session
     */
    logout: function() {
        // Kill server-side session
        model.request({
            url: ""
        });
    	
        // Clear credentials
        model.username = "";
        model.password = "";
        model.session_id = "";
        model.connections = {};
    	
        // Hide everything
        view.logout();
    	
        // Start over
        model.init();
    },
    
    /*
         * Display information about PAT
         */
    about: function() {
        jAlert('PATui Version 1.0', 'About');
    }
};

model.init();