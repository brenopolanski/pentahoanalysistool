/**
 * PATui
 * This is only a temporary implementation of jQuery to demonstrate
 * UI interaface and interactions
 */

var myLayout;
$(document).ready(function () {
    
    /*
     * jQuery UI Layout
     */

    $('body').layout({
        fxName:                 "slide",
        fxSpeed:                0.00001,
        north__closable:        false,
        north__resizable:       false,
        north__spacing_open:    0,
        north__spacing_closed:  0,
        west__spacing_open:     4,
        west__spacing_closed:   4,
        west__resizable:        true,
        west__closable:         true,
        west__size:             280
    });

    /*
     * Jquery UI Tabs
     */
    // tabs init with a custom tab template and an "add" callback filling in the content
    var $tabs = $('#queries').tabs({
        
    });

    // close icon: removing the tab on click
    // note: closable tabs gonna be an option in the future - see http://dev.jqueryui.com/ticket/3924
    $('#querylist li img').live('click', function() {
        var index = $('li',$tabs).index($(this).parent());
        $tabs.tabs('remove', index);
    });

});
