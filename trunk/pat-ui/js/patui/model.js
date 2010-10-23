/*
 * This is the model for the PATui. It will handle all calls to the REST API stored on the server.
 */

var model = {
	/*
	 * The session_id used to make calls to the server
	 */ 
	session_id: "",
	
	/*
	 * Connection information for this PAT server
	 */
	connections: {},
	
	/*
	 * We're going to try to log server errors gracefully. However, if it reaches
	 * a certain threshold, we're going to suggest to the user that they just
	 * refresh the page.
	 */
	server_errors: 0,
		
	/*
	 * This is the constructor of sorts, ensuring that the session ID is valid
	 */
	init: function (username) {
		// TODO - Obtain a session_id
		model.get_session();
	
		// Load schemas
		//while (! this.load_schemas() && this.server_errors < 10) 
		//	{ } // Cross our fingers there's no infinite loop
		
		// Let the controller know if connection was successful
		if (model.server_errors >= 10) {
			return false;
		} else {
			return true;
		}
	},
	
	/*
	 * Obtain a session and load connections
	 */
	get_session: function() {
		$.getJSON(BASE_URL + "/rest/admin/session", function(data, textStatus, XMLHttpRequest) {
			//alert(XMLHttpRequest.responseText);
			model.session_id = data['@sessionid'];
			model.connections = data;
		});
	},
	
	/*
	 * Load the schemas into the model's schema property
	 * TODO - move view logic to view
	 */
	load_schemas: function() {
		/* Jackedupedness
		success = true;
	    $.getJSON('inc/query.php', function(data) {
	        if(data === null) {
	        	// Let the controller know that we couldn't get the schemas
	            success = false;
	            this.server_errors++;
	        } else {
	        	// TODO - this is all jacked up at the moment
		        // Load the session_id into a cookie for use later
	        	// TODO - this logic should go in model.init() 
		        $.cookie('sessionid', data['@sessionid']);
		        
		        // TODO - all this connection data and stuff should be loaded into class variables and then iterated
		        $.each(data.connections.connection, function(i,connection){
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
		                $.unblockUI(); // TODO - this is view logic
		            });
		        });
	        } 
	    });
	    
	    return success; */
	},
	
	/*
	 *  new_query()
	 *  Populates dimension-list and measure-list with available items
	 *  and enables draggable, droppable and sortable lists.
	 *  @data_string    :   A concatenated string of connectionid, schemaname and
	 *                      cubename.
	 *                      
	 *  TODO - this needs to be cleaned up considerably
	 */
	new_query: function (data_string) {
	/*
	    var split_string = data_string.split("|"); // TODO - what the hell!? why don't we have json?
	    var connectionid = split_string[0];
	    var schemaname = split_string[1];
	    var cubename = split_string[2];
	    $.ajax({
	        type:       'POST',
	        url:        'inc/query.php',
	        data:       'method=POST_NEW&connectionid='+connectionid+'&schemaname='+schemaname+'&cubename='+cubename,
	        datatype:   'json',
	        success:    function(data){
	            if(data === "false" || data === null) {
	                alert('An error has occured when contacting PAT\'s server. Check your console log for more info.');
	                $.unblockUI();
	            }else{
	                // Remove placeholders
	                $('#dimension-list, #measure-list').html('');
	                //  Convert output to a JSON object
	                var obj = $.parseJSON(data);
	                //  Set the queryid into a cookie
	                $.cookie('queryid', obj['@queryid']);
	                // For each dimension
	                $.each(obj.axis.dimensions, function(i,dimension){
	                    //  If not a Measure dimension
	                    if(this['@dimensionname'] != 'Measures')
	                    {   //  Update blockUI
	                        $('#blockOverlay-update').html('dimensions...');
	                        //  Store and rename the dimension name (remove spaces and replace with _)
	                        var dimension_name = this['@dimensionname'].replace(' ', '_');
	                        var dimension_name_old = this['@dimensionname'];
	                        //  Add dimension names as a <li></li> element
	                        $('#dimension-list').append('<li id="'+dimension_name+'"><a href="#" class="not-draggable">'+this['@dimensionname']+'<span class="hide">'+this['@dimensionname']+'</span></a></li>');
	                        //  Add a secondary list to the dimension name
	                        $('#'+dimension_name).append('<ul id="child_'+dimension_name+'"></ul>');
	                        //  Add levels to the above <ul></ul> element
	                        $.each(dimension.levels, function(i,level){
	                            $('#dimension-list #child_'+dimension_name).append('<li><a href="#">'+level['@levelcaption']+'<span class="hide">'+level['@levelname']+'</span></a></li>');
	                        });
	                    } else {
	                        //  If a measure, display them all without a category
	                        //  they are seen as members
	                        $.each(dimension.levels.members, function(i,member){
	                            //  Update blockUI
	                            $('#blockOverlay-update').html('measures...');
	                            //  Add members to the measure-list <ul></ul>
	                            $('#measure-list').append('<li id="'+this['@membercaption']+'"><a href="#">'+this['@membercaption']+'<span class="hide">'+this['@membername']+'</span></a></li>');
	                        });

	                    }
	                });
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

if (! model.init())
	controller.server_error();