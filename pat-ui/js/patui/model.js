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
                        		})
                        	.appendTo($dimension_tree);

                        if (dimension.levels.length > 1) {
	                        // Append <ul/> to the dimensions_tree <li/>
	                        $second_level = $('<ul />').appendTo($first_level);
	                        
	                        // If there is a secondary level loop through and add to the <ul/>
	                        $.each(dimension.levels, function(i,level){
	                            // Populate the second <ul/>
	                            $('<li><a href="#">'+level['@levelcaption']+'</a></li>')
	                            .click(function() { return false; })
	                            .appendTo($second_level);
	                        });
                        }
                        
                    } else {
                        // Create a 'dummy' measures <ul/>
                        // Append <li/> to the dimensions_tree <ul/>
                    	$measures = $('<li>Measures</li>')
                    		.addClass("measures folder")
                    		.appendTo($tab.find('.measures_tree ul'));

                        // Append <ul/> to the dimensions_tree <li/>
                        $measures.append('<ul />');
                            
                        $.each(dimension.levels.members, function(i,member){
                            // Populate the second <ul/>
                        	$measures.find('ul').append('<li id="'+this['@membercaption']+'"><a href="#">'+this['@membercaption']+'</a></li>');
                        });
                        
                    }
                });
                
                // jsTree
                /*
                
                //  Draggable
                $both_trees.find("li ul li").draggable({
                    cancel:         '.not-draggable',
                    opacity:        0.90,
                    drag:       function(){
                        $('.ui-draggable-dragging ins').remove();
                    },
                    helper:         function(){
                        return $(this).clone().appendTo('body').css('zIndex',5).show();
                    }
                });
                
                //  Droppable
                $both_trees.find("li ul li").droppable({
                    accept:         '#query1 .dimensions_tree, #query1 .measures_tree',
                    accept:         ":not(.ui-sortable-helper)",
                    drop:           function(event, ui) {
                        var member_syntax = ui.draggable.children(':nth-child(2)').html()
                        var member = ui.draggable.text().replace(member_syntax, '');
                        if($('#column-drop ul span:contains("'+member_syntax+'"), #row-drop ul span:contains("'+member_syntax+'")').length > 0) {
                            return false;
                        }
                        var arr = member_syntax.split('.');
                        var str = arr[0].replace('[','').replace(']','');
                        
                        $(this).find(".placeholder").remove();
                        $("<li class="+str+"></li>").text(member).appendTo(this).append('<span class="remove"></span>').append('<span class="hide levelname">'+member_syntax+'</span><span class="hide levelcaption">'+member+'</span>');
                        run_query();
                    }
                })
                
                //  Sortable
                .sortable({
                    //connectWith: '#row-axis, #column-axis',
                    items: "li:not(.placeholder)",
                    placeholder: 'placeholder-sort',
                    stop: function() {
                        run_query();
                    }

                });
                */
                
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
        model.request({ url: "" });
    	
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