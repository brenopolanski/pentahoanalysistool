/*
 * This is the model for the PATui. It will handle all calls to the REST API stored on the server.
 */

var model = {
    /*
     * FIXME - The username to be used with HTTP basic auth
     */
    username: "",
	
    /*
     * FIXME - The password to be used with HTTP basic auth
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
	        jPrompt("Please enter your username: ", "admin", "PAT", function(input) {
	        	model.username = input;
	        	jPrompt("Please enter your password: ", "admin", "PAT", function(input) {
	        		model.password = input;
	        		
	        		// Obtain a session_id
	                model.get_session();
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
     *  When a new query button is clicked create a new tab and load with
     *  a new query template.
     */
    new_tab: function() {
        
        // TODO - Need to load partial view using ajax method i.e. _new_query.html
        // See if I can reformat the markup to be liked by jQuery UI tabs ? Worth it ?

        var new_tab_counter, tab_counter;

        // Find all new query tabs, designating the tab markup with a 'rel="new"
        // makes it a new query tab, this can be used for open/saved queries i.e.
        // 'rel="save"'
        new_tab_counter = $('#tab_list li a[rel="new"]:not(a[rel="welcome"])').length + 1;
        
        // Find the total number of enabled tabs
        tab_counter = $('#tab_list li:not(.welcome)').length + 1;

        // We can not use jQuery UI tab methods as the markup of our tabs do not
        // follow the jQuery UI tabs standard templates, the only way around this
        // is to manually do the methods (it is not as neater but does the job)
        // http://stackoverflow.com/questions/3593713/using-jquery-ui-tabs-with-a-custom-html-layout
        // ^ mentions that they will not change the way, so I will try and make the tabs
        // follow jQuery UI's style - if not this method should suffice.

        // Add a new <li/> element to the #tab_list ul (tab)
        $('#tab_list ul').append('<li class="ui-state-default ui-corner-top">' +
            '<a href="#query' + tab_counter + '" rel="new" class="closable">' +
            'New Query (' + new_tab_counter + ')' +
            '<img alt="Close tab" src="images/tab/close.png" />' +
            '</a>' +
            '</li>');
                             
        // Add a new <div/> element to the #tab_content <div/> (tab panel)
        $('#tab_content').append('<div id="query' + tab_counter +'" class="tab ui-tabs-panel ui-widget-content ui-corner-bottom ui-tabs-hide"><div class="tab_body"></div><div class="sidebar"></div></div>');
        
        // load content
        // init layout
        // enable tabs
        // show tab


        /* Other method trying to use jQuery Tabs UI methods */
        /*
        $tabs.tabs({
            add: function(event, ui){
                $("#tab_content").append('<div id="query3"><div class="tab_body ui-layout-pane ui-layout-pane-center">Idas</div><div class="sidebar ui-layout-pane ui-layout-pane-west">Idsa</div></div>');
                $tabs.tabs('select', '#' + ui.panel.id);
            }
        });

        $tabs.tabs({
            tabTemplate: '<li><a href="#{href}">#{label} <img alt="Close tab" src="images/tab/close.png" /></a></li>'
        });

        $tabs.tabs("add", "#query"+tab_counter, "New Query ("+new_tab_counter+")");
        */




    },
     
    /*
         *  Populates dimensions_tree and measures_tree with available items
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
                // TODO - method to get current tab, hardcoded #query1

                // Remove all instances of previous trees
                // incase user reselects cube
                $('#query1 .dimensions_tree').html('')
                .html('<ul id="query1_dimensions" />');
                $('#query1 .measures_tree').html('')
                .html('<ul id="query1_measures" />');

                $.each(data.axis.dimensions, function(i,dimension) {

                    // Populate dimension tree first
                    if(this['@dimensionname'] != 'Measures') {

                        // Clean up the dimension name
                        var dimension_name = this['@dimensionname'].replace(' ', '_');
                        // Append <li/> to the dimensions_tree <ul/>
                        $('#query1_dimensions').append('<li id="'+dimension_name+'"><a href="#">'+this['@dimensionname']+'</a></li>');

                        // TODO - Check if there is a secondary level
                        // Append <ul/> to the dimensions_tree <li/>
                        $('#'+dimension_name).append('<ul id="c_'+dimension_name+'"/>');
                        
                        // If there is a seconday level loop through and add to the <ul/>
                        $.each(dimension.levels, function(i,level){
                            // Populate the second <ul/>
                            $('#c_'+dimension_name).append('<li><a href="#">'+level['@levelcaption']+'</a></li>');
                        });
                        
                    } else {
                        // Create a 'dummy' measures <ul/>
                        // Append <li/> to the dimensions_tree <ul/>
                        $('#query1_measures').append('<li id="measures"><a href="#">Measures</a></li>');

                        // Append <ul/> to the dimensions_tree <li/>
                        $('#measures').append('<ul id="c_measures"/>');
                            
                        $.each(dimension.levels.members, function(i,member){
                            // Populate the second <ul/>
                            $('#c_measures').append('<li id="'+this['@membercaption']+'"><a href="#">'+this['@membercaption']+'</a></li>');
                        });
                        
                    }
                });
                
                // jsTree
                // TODO - Must reference active tab only
                $("#query1 .dimensions_tree, #query1 .measures_tree").jstree({
                    "core" : {
                        "animation" : 0
                    },
                    "themes" : {
                        "theme" : false
                    },
                    "plugins" : [ "themes", "html_data" ]
                });

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