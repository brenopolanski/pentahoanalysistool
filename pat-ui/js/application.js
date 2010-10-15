$(document).ready(function () {

    //  Messages
    var PAT_TITLE       =   "<strong>PAT<em>ui</em> Demo</strong>";
    var LOADING_DATA    =   "";
    var NO_DIMENSIONS   =   "No cube selected";
    var NO_MEASURES     =   "No cube selected";

    //  Initialise BlockUI for loading available schemas and cubes
    $.blockUI({
        message: '<div class="blockOverlay-inner">'+PAT_TITLE+'<br/>Loading <span id="blockOverlay-update">schemas and cubes...</span></div>'
    });

    //  Intialise jQuery UI Layout
    $('body').layout({
        north__closable:          false,
        north__resizable:         false,
        north__spacing_open:      4,
        north__spacing_closed:    4,
        north__size:              30,
        west__spacing_open:       4,
        west__spacing_closed:     4,
        west__size:               250,
        west__resizable:          true
    });

    //  Load available schemas and cubes
    load_data();

    //  Remove links

    //  Remove links on column axis items
    $('#column-drop ul li .remove').live('click', function(){
        $(this).parent().remove();
        if($('#column-drop ul li').length == 0) {
            $('#column-drop ul').append('<li class="quiet placeholder">Drop column axis items here</li>');
        }
        createOutput();
    });
    //  Remove links on rows axis items
    $('#row-drop ul li .remove').live('click', function(){
        $(this).parent().remove();
        if($('#row-drop ul li').length == 0) {
            $('#row-drop ul').append('<li class="quiet placeholder">Drop row axis items here</li>');
        }
        createOutput();
    });
    //  When hovering over ther remove links
    $(".remove").live('hover',
        function(event) {
            if (event.type == 'mouseover') {
                $(this).addClass('remove-hover');
            } else {
                $(this).removeClass('remove-hover');
            }
        });

    //  When user creates a new query
    $('#data-list').change(function(){
        //  If Select a cube option is selected
        if($(this).val() === "none"){
            $('#dimensions, #measures').removeClass('jstree jstree-0 jstree-default');
            $('#dimensions').html('<ul id="dimension-list"><li class="placeholder quiet">'+NO_DIMENSIONS+'</li></ul>');
            $('#measures').html('<ul id="measure-list"><li class="placeholder quiet">'+NO_MEASURES+'</li></ul>');
            return false;
        }

        //  Display a BlockUI when loading dimensions and measures
        $.blockUI({
            message: '<div class="blockOverlay-inner">'+PAT_TITLE+'<br/>Loading <span id="blockOverlay-update">dimensions and measures...</span></div>'
        });

        //  Populate list of dimensions and measures
        new_query($(this).val());
    });
});

/*
 *  load_data()
 *  Retrieves a list of schemas and cubes and populates the
 *  #data_list selectbox.
 */

function load_data() {
    $.getJSON('inc/info.php', function(data) {
        if(data == 0){
            alert('Error!');
            $.unblockUI();
            return false;
        }else{
            $.each(data.connections.connection, function(i,connection){
                $.each(connection.schemas, function(i,schema){
                    $('#data-list').append('<optgroup label="'+schema['@schemaname']+'">');
                    $('#blockOverlay-update').html('schema '+schema['@schemaname']);
                    $.each(schema.cubes, function(i,cube){
                        $('#blockOverlay-update').html('cube '+cube['@cubename']);
                        if(cube.length == undefined) { /* hack */
                            $('#data-list').append('<option value="'+connection['@connectionid']+'|'+schema['@schemaname']+'|'+cube['@cubename']+'">'+cube['@cubename']+'</option>');
                        }else{
                            $.each(cube, function(i,item){
                                $('#data-list').append('<option value="'+connection['@connectionid']+'|'+schema['@schemaname']+'|'+item['@cubename']+'">'+item['@cubename']+'</option>');
                            });
                        }
                    });
                    $('#data-list').append('</optgroup>');
                    $.unblockUI();
                });
            });
        }
    });
}

/*
 *  new_query()
 *  Populates dimension-list and measure-list with available items
 *  and enables draggable, droppable and sortable lists.
 *  @data_string    :   A concatenated string of connectionid, schemaname and
 *                      cubename.
 */

function new_query(data_string) {
    var split_string = data_string.split("|");
    var connectionid = split_string[0];
    var schemaname = split_string[1];
    var cubename = split_string[2];
    $.ajax({
        type:       'POST',
        url:        'inc/query.php',
        data:       'connectionid='+connectionid+'&schemaname='+schemaname+'&cubename='+cubename,
        datatype:   'json',
        success:    function(data){
            if(data === "false") {
                return false;
            }else{
                $('#dimension-list, #measure-list').html('');
                var obj = $.parseJSON(data);
                
                $.each(obj.axes, function(i,axis){
                    $.each(axis.dimensions.dimension, function(i,dimension){
                        if(this['@dimensionname'] != "Measures"){
                            $('#blockOverlay-update').html('dimensions...');
                            $('#dimension-list').append('<li id="'+this['@dimensionname'].replace(' ', '_')+'"><a href="#" id="'+this['@dimensionname']+'" class="trigger">'+this['@dimensionname']+'<span class="hide">['+this['@dimensionname']+']</span></a></li>');
                            $('#'+this['@dimensionname'].replace(' ', '_')).append('<ul id="child_'+this['@dimensionname'].replace(' ', '_')+'"></ul>');
                            var dimension_name = this['@dimensionname'].replace(' ', '_');
                            $.each(dimension.levels.level, function(i,level){
                                $('#dimension-list #child_'+dimension_name).append('<li><a href="#">'+this['@levelcaption']+'<span class="hide">'+this['@levelname']+'</span></a></li>');
                            });
                        }else{
                            $.each(dimension.levels.level.members.member, function(i,member){
                                $('#blockOverlay-update').html('measures...');
                                $('#measure-list').append('<li id="'+this['@membercaption']+'"><a href="#">'+this['@membercaption']+'<span class="hide">'+this['@membername']+'</span></a></li>');
                            });
                        }
                    });
                    $("#dimension-list ul, #measure-list ul").hide();
                    $("#dimensions, #measures").jstree({
                        "core"      : {
                            "animation" : 1
                        },
                        "plugins" : [ "themes", "html_data" ]
                    });
                });
            
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

                $('#row-axis, #column-axis').droppable({
                    accept:         '#dimensions ul a, #measures ul a',
                    accept:         ":not(.ui-sortable-helper)",
                    drop:           function(event, ui) {
                        var member_syntax = ui.draggable.children(':nth-child(2)').html()
                        var member = ui.draggable.text().replace(member_syntax, '');
                        if($('#column-drop ul span:contains("'+member_syntax+'"), #row-drop ul span:contains("'+member_syntax+'")').length > 0) {
                            return false;
                        }
                        $(this).find(".placeholder").remove();
                        $("<li></li>").text(member).appendTo(this).append('<span class="remove"></span>').append('<span class="hide">'+member_syntax+'</span>');
                        createOutput();
                    }
                }).sortable({
                    connectWith: '#row-axis, #column-axis',
                    items: "li:not(.placeholder)",
                    placeholder: 'placeholder-sort',
                    sort: function() {
                        createOutput();
                    }

                });

                $.unblockUI();
            }
        }
    });
}

function createOutput() {
    $('#rows, #columns').html('')
    $('#column-axis').find('.hide').each(function(index) {
        $('#columns').append(' '+$(this).text());
    });
    $('#row-axis').find('.hide').each(function(index) {
        $('#rows').append(' '+$(this).text());
    });
}