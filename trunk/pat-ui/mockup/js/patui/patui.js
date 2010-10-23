/**
 * PATui
 * This is only a temporary implementation of jQuery to demonstrate
 * UI interaface and interactions
 */

var myLayout;
$(document).ready(function () {

    // jQuery UI Layout
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

    // jQuery UI Tabs
    var $tabs = $('#queries_container').tabs();

    // If close button is clicked
    $('#queries li a img').click(function() {
        alert('Are you sure you want to do that?');
        var index = $('li',$tabs).index($(this).parent());
        $tabs.tabs('remove', index);
    });

});
