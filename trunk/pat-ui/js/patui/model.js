/*
 * This is the model for the PATui. It will handle all calls to the REST API stored on the server.
 */

var model = {
	/*
	 * FIXME - The username to be used with HTTP basic auth
	 */
	username: "admin",
	
	/*
	 * FIXME - The password to be used with HTTP basic auth
	 */
	password: "admin",
	
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
        /* if (model.username == "" || model.password == "") {
	        jPrompt("Please enter your username: ", "", "PAT", function(input) {
	        	model.username = input;
	        	jPrompt("Please enter your password: ", "", "PAT", function(input) {
	        		model.password = input;
	        		
	        		// Obtain a session_id
	                model.get_session();
	        	});
	        });
        }*/
        
        // FIXME - hard-coded credentials to ease development
        
		// Obtain a session_id
        model.get_session();
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
	            view.generate_navigation();
    		}
    	});
    },
	
    /*
     * Clears the current query to prepare for a new one
     */
    clear_query: function() {

        // TODO - Add a new tab
        // TODO - Render content for new tab
        return false;
    },

    /*
	 *  Populates dimension-list and measure-list with available items
	 *  and enables draggable, droppable and sortable lists.
	 */
    new_query: function($cube) {
    	data = $cube.data();
    	view.processing("Creating new query on " + data['cube']);
    	
    	model.clear_query();
    	
    	model.request({
    		method: "POST",  
    		url: model.username + "/query/" + data['schema'] + "/" + data['cube'] + "/newquery", 
    		success: function(data, textStatus, XMLHttpRequest) {
    			$('.dimension_tree, .measure_tree').html('');
    			// TODO - fill in dimensions and measures
                $.each(data.axis.dimensions, function(i,dimension){
                    //  If not a Measure dimension
                    if(this['@dimensionname'] != 'Measures')
                    {   
                        //  Store and rename the dimension name (remove spaces and replace with _)
                        var dimension_name = this['@dimensionname'].replace(' ', '_');
                        var dimension_name_old = this['@dimensionname'];
                        //  Add dimension names as a <li></li> element
                        $('.dimension_tree').append('<li id="'+dimension_name+'"><a href="#" class="not-draggable">'+this['@dimensionname']+'<span class="hide">'+this['@dimensionname']+'</span></a></li>');
                        //  Add a secondary list to the dimension name
                        $('#'+dimension_name).append('<ul id="child_'+dimension_name+'"></ul>');
                        //  Add levels to the above <ul></ul> element
                        $.each(dimension.levels, function(i,level){
                            $('.dimension_tree #child_'+dimension_name).append('<li><a href="#">'+level['@levelcaption']+'<span class="hide">'+level['@levelname']+'</span></a></li>');
                        });
                    } else {
                        //  If a measure, display them all without a category
                        //  they are seen as members
                        $.each(dimension.levels.members, function(i,member){
                            //  Update blockUI
                            $('#blockOverlay-update').html('measures...');
                            //  Add members to the measure-list <ul></ul>
                            $('.measure_tree').append('<li id="'+this['@membercaption']+'"><a href="#">'+this['@membercaption']+'<span class="hide">'+this['@membername']+'</span></a></li>');
                        });

                    }
                });
                

    			
    			view.free();
    		},
    		error: function() {
    			jAlert("Couldn't create a new query. Please try again.");
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
    	model.request("GET", "", function() {}, {});
    	
    	// Clear credentials
    	model.username = "";
    	model.password = "";
    	
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
    },
	
    new_query_old: function (data_string) {
    /*
	                //  Eof populating dimensions and measures
	                //  Activate the jstree plugin on the above lists
	                $("#dimensions, #measures").jstree({
	                    "core"      : {
	                        "animation" : 1
	                    },
	                    "plugins" : [ "themes", "html_data" ]
	                });
	                //  Draggable
	                $("#dimensions ul a, #measures ul a").draggable({
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
	                $('#row-axis, #column-axis').droppable({
	                    accept:         '#dimensions ul a, #measures ul a',
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
	                //  Sortable
	                }).sortable({
	                    //connectWith: '#row-axis, #column-axis',
	                    items: "li:not(.placeholder)",
	                    placeholder: 'placeholder-sort',
	                    stop: function() {
	                        run_query();
	                    }

	                });
	                //  Remove blockUI
	                $.unblockUI();
	            }
	        }
	    }); */
    }
};

model.init();