/**
 * @fileOverview    This represents the view for Saiku UI.
 * @description     This will handle the drawing of the UI.
 * @version         1.0.0
 */

// Enable debugging
enable_debug = true;

/** Resize the height of the layout to keep consistant columns. */
function resize_height() {
    // Get the browser's current height
    var window_height = $(window).height();
    // When the height of the browser is less than 600px set a height of 600px.
    if(window_height <= 600) {
        window_height = 600;
    }
    var workspace_offset = $('.workspace_inner').outerHeight(true) - $('.workspace_inner').height(),
    // Add 1px to tabs height for tab_panel border-top: 1px solid #CCC
    sidebar_offset = ($('#toolbar').outerHeight(true) + ($('#tabs').outerHeight(true) + 1)),
    sidebar_height = window_height - sidebar_offset,
    workspace_height = sidebar_height - workspace_offset;

    $('.sidebar, .sidebar_separator').css('height', sidebar_height);
    $('.workspace_inner').css('height', workspace_height);
}

/** Toggle (hide/show) the sidebar. */
function toggle_sidebar() {
    // Get the width of the sidebar.
    var sidebar_width = $('.sidebar').width();
    // If the sidebar is not hidden.
    if(sidebar_width == 260) {
        $('.sidebar').css('width', 0);
        $('.workspace_inner').css('margin-left', 5);
    } else {
        // If the sidebar is hidden.
        $('.sidebar').css('width', 260);
        $('.workspace_inner').css('margin-left', 265);
    }
}

/**
 * View class.
 * @class
 */
var view = {

    /** Display the login form when the view is initialised. */
    init : function() {
        // Append a dialog <div/> to the body.
        $('<div id="dialog" class="dialog hide" />').appendTo('body');
        // Load the view into the dialog <div/> and disable caching.
        $.ajax({
            url : '../views/session',
            cache : false,
            dataType : "html",
            success : function(data) {
                $('#dialog').html(data).modal({
                    overlayCss: {
                        background: 'white',
                        opacity: 'none'
                    }
                });
                $('#dialog').find('#login').click(function(){
                    model.username = $('#username').val();
                    model.password = $('#password').val();
                    model.get_session();
                });
            }
        });
    },

    /** Initialise the user interface. */
    draw_ui : function () {
        // Show waiting message.
        view.show_waiting('Drawing UI, please wait...');
        
        /** Show all UI elements. */
        $('#toolbar, #tabs, #tab_panel').show();

        /** Add an event handler to all toolbar buttons. */
        $("#toolbar ul li a").click(function() {
            controller.toolbar_click_handler($(this));
            return false;
        });

        /** Bind resize_height() to the resize event. */
        $(window).bind('resize', function() {
            resize_height();
        });

        /** Bind toggle_sidebar() to click event on the sidebar_separator. */
        $('.sidebar_separator').bind('click', function() {
            toggle_sidebar();
        });

        /** Initialise resize_height() on first page load. */
        resize_height();

        // Remove waiting message.
        view.stop_waiting();
        
    },

    /** Destroy the user interface. */
    destroy_ui : function () {
        $('#toolbar, #tabs, #tab_panel').hide();
    },

    /**
     * Displays a waiting dialog box.
     * @param message {String} Waiting message to be displayed.
     */
    show_waiting : function (message) {
        // Append the waiting <div/> to the body.
        $('<div id="waiting" class="waiting hide" />').appendTo('body');
        // Load the contents and message into the waiting <div/>.
        $('#waiting').append('<div class="waiting_inner"><div class="waiting_body">' + message + '</div></div>').modal();
    },
    
    /** Removes a waiting dialog box. */
    stop_waiting : function () {
        $.modal.close();
        $('#waiting').remove();
    },

    /**
     * Load views into a dialog template
     * @param url {String} The url where the view is located.
     */
    show_dialog : function(url) {
        // Append a dialog <div/> to the body.
        $('<div id="dialog" class="dialog hide" />').appendTo('body');
        // Load the view into the dialog <div/> and disable caching.
        $.ajax({
            url : url,
            cache : false,
            dataType : "html",
            success : function(data) {
                $('#dialog').html(data).modal();
            }
        });
    }

}

/** Initialise the user interface. */
view.init();