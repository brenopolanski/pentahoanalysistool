/**
 * @fileOverview    This represents the model for Saiku UI.
 * @description     This will handle all interaction to Saiku's REST API.
 * @version         1.0.0
 */

/**
 * Model class
 * @class
 */
var model = {

    /** Username to be used with BASIC_AUTH. */
    username : "",

    /** Password to be used with BASIC_AUTH. */
    password : "",
    
    /**
     * Handle all AJAX requests.
     * @param paramters {Object} Parameters for AJAX requests.
     */
    request : function(parameters) {
        if (typeof parameters.method == "undefined")
            parameters.method = "GET";
        if (typeof parameters.data == "undefined")
            parameters.data = {};
        if (typeof parameters.success == "undefined")
            parameters.success = function() {};
        if (typeof parameters.error == "undefined")
            parameters.error = model.server_error;
        
        $.ajax({
            type: parameters.method,
            url: "/pat-ui/fixtures/rest/" + parameters.url,
            //url: "rest/" + parameters.url, CHANGE
            dataType: 'json',
            username: model.username,
            password: model.password,
            success: parameters.success,
            error: parameters.error
        });
    },
    
    /** Get the sessionid and based on the username and unhide the UI. */
    get_session : function() {
        model.request({
            method: "POST",
            url: model.username + "/session",
            success: function(data, textStatus, XMLHttpRequest) {
                model.session_id = data['@sessionid'];
                model.connections = data;
                view.draw_ui();
                controller.add_tab();
            }
        });
    },

    /**
     * Populate the dimension and measure tree and initialise draggable,
     * droppable and sortable items.
     * @param tab_index {Integer} Index of the selected tab.
     */
    new_query : function(tab_index) {

        /** Find the selected cube. */
        $cube = view.tabs.tabs[tab_index].content.find(".cubes option:selected");

        /** Check if the cube is valid if so then display an error. */
        data = view.tabs.tabs[tab_index].data['navigation'][$cube.attr('value')];
        if (typeof data == "undefined") {
            view.show_dialog('Error', 'There was an error loading that cube.<br/>Please close the tab and try again.')
            return;
        }

        // Reference for the selected tabs content.
        $tab = view.tabs.tabs[tab_index].content;

        // Get a list of available dimensions and measures.
        model.request({
            method: "POST",
            url: model.username + "/query/" + data['schema'] + "/" + data['cube'] + "/newquery",
            success: function(data, textStatus, XMLHttpRequest) {
                view.start_waiting('Loading');

                view.load_dimensions($tab, data.axis.dimensions);
                view.load_measures($tab, data.axis.dimensions);

                /** Activate hide and show on trees. */
                // Hide all sub items from the populated tree.
                $tab.find('.root').children('ul').children('li').hide();
                /** When the root item is clicked show it's children. */
                $tab.find('.root').click(function() {
                    $(this).children("ul").children('li').toggle();
                    if($(this).hasClass('expand')) {
                        $(this).removeClass('expand').addClass('collapsed')
                        .find('a.folder_expand')
                        .removeClass('folder_expand')
                        .addClass('folder_collapsed');
                    }else{
                        $(this).removeClass('collapsed').addClass('expand')
                        .find('a.folder_collapsed')
                        .removeClass('folder_collapsed')
                        .addClass('folder_expand');
                    }
                    return false;
                });

                /**
                 * Enable dragging, dropping and sorting.
                 * Drag and Drop rules
                 *   - Unique items on all axis
                 *   - You can only drag one item from a level onto an axis
                 *   - Measures must be grouped
                 */
                // Enable dragging
                $both_trees = $tab.find('.dimension_tree, .measure_tree');
                $both_trees.find("li ul li a").draggable({
                    cancel : ".not-draggable",
                    helper : "clone",
                    opacity : 0.70,
                    start : function(event, ui) {
                        if(ui.helper.hasClass('dimension')) {
                            ui.helper.addClass('dimension_dragging');
                        }else if(ui.helper.hasClass('measure')) {
                            ui.helper.addClass('measure_dragging');
                        }
                    }
                });
                // Enable droppable
                $both_dropzones = view.tabs.tabs[tab_index].content.find('field_list_body .rows li, .field_list_body .columns li');
                $both_dropzones.droppable({
                    accept: '.ui-draggable',
                    activeClass: "notice",
                    hoverClass: "success",
                    drop: function(event, ui) {
                        $(this).parent().find('.placeholder').remove();
                        if(ui.helper.hasClass('dimension')) {
                            $(this).parent().append('<li><span class="dimension_dropped">'+ui.draggable.text()+'</span></li>');
                        }else if(ui.helper.hasClass('measure')) {
                            $(this).append('<li class="measure_dropped">'+ui.draggable.text()+'</li>');
                        }
                        tab_index = view.tabs.index_from_content($(this).parent().parent().parent().parent().parent("div.tab"));
                        
                        // Enable draggable on dropped items
                        $('span', $both_dropzones).draggable({
                            cancel: ".not-draggable",
                            revert: "invalid",
                            opacity: 0.70
                        });

                    //view.check_query(tab_index);
                    }
                });

                

                // Enable droppable
                $('.sidebar').droppable({
                    accept: ".field_list_body .rows, .field_list_body .columns",
                    drop: function(event, ui) {
                        // Using setTimeout due to IE7+ bug with jQuery UI
                        setTimeout(function() {
                            ui.draggable.remove();
                        } , 1);
                    }
                })

                
                view.stop_waiting();
            },
            error: function() {
                view.show_dialog("Error", "Couldn't create a new query. Please try again.");
            }
        });
    }
    
}