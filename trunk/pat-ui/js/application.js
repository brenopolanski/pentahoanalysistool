$(document).ready(function () {

    //  Messages
    var LOADING_DATA    =   "Loading data, please wait...";
    var NO_DIMENSIONS   =   "No dimensions available";
    var NO_MEASURES     =   "No measures available";
    var NEW_QUERY       =   "Loading dimensions and measures, please wait..."

    //  BlockUI CSS defaults
    $.blockUI();

    //  On page load
    $.blockUI({
        message: '<div class="blockOverlay-inner">'+LOADING_DATA+'</div>'
    });

    //  jQuery UI Layout
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

    
    //  Handle clicks on the remove links
    $('#column-drop ul li .remove').live('click', function(){
        $(this).parent().remove();
        if($('#column-drop ul li').length == 0) {
            $('#column-drop ul').append('<li class="quiet placeholder"><em>Drop dimensions and measures for columns here</em></li>');
        }
        createOutput();
    });
    $('#row-drop ul li .remove').live('click', function(){
        $(this).parent().remove();
        if($('#row-drop ul li').length == 0) {
            $('#row-drop ul').append('<li class="quiet placeholder"><em>Drop dimensions and measures for rows here</em></li>');
        }
        createOutput();
    });

    //  Handle dimension-list and measure-list
    $(".trigger").live('click', function() {
        var child_id = 'child_'+$(this).attr('id');
        $('#'+child_id).toggle();
    });


    // Handle changes on #data-list AKA create a new query
    $('#data-list').change(function(){
        if($(this).val() === "none"){
            $('#dimension-list').html('<li class="placeholder quiet"><em>'+NO_DIMENSIONS+'</em></li>');
            $('#measure-list').html('<li class="placeholder quiet"><em>'+NO_MEASURES+'</em></li>');
            return false;
        }
        $.blockUI({
            message: '<div class="blockOverlay-inner">'+NEW_QUERY+'</div>'
        });
        new_query($(this).val());
    });
});

/*
 *  load_data()
 *  This populates available schemas and cubes into #data-list
 */

function load_data() {
    $.getJSON('inc/info.php', function(data) {
        if(data == 0){
            alert('Error!');
            $.unblockUI();
            return false;
        }else{
            console.log(data);
            $.each(data.connections.connection, function(i,connection){
                $.each(connection.schemas, function(i,schema){
                    $('#data-list').append('<optgroup label="'+schema['@schemaname']+'">');
                    $.each(schema.cubes, function(i,cube){
                        console.log(cube.length);
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
                            $('#dimension-list').append('<li id="'+this['@dimensionname']+'"><span id="'+this['@dimensionname']+'" class="trigger">'+this['@dimensionname']+'<span class="hide">['+this['@dimensionname']+']</span></span></li>');
                            $('#'+this['@dimensionname']).append('<ul id="child_'+this['@dimensionname']+'"></ul>');
                            var dimension_name = this['@dimensionname'];
                            $.each(dimension.levels.level, function(i,level){
                                $('#dimension-list #child_'+dimension_name).append('<li><span>'+this['@levelcaption']+'<span class="hide">'+this['@levelname']+'</span></span></li>');
                            });
                        }else{
                            $('#measure-list').append('<li id="'+this['@dimensionname']+'"><span id="'+this['@dimensionname']+'" class="trigger">'+this['@dimensionname']+'</span></span></li>');
                            $('#'+this['@dimensionname']).append('<ul id="child_'+this['@dimensionname']+'"></ul>');
                            var dimension_name = this['@dimensionname'];
                            $.each(dimension.levels.level.members.member, function(i,member){
                                $('#measure-list #child_'+dimension_name).append('<li id="'+this['@membercaption']+'"><span>'+this['@membercaption']+'<span class="hide">'+this['@membername']+'</span></span></li>');
                            });
                        }
                    });
                    $("#dimension-list ul, #measure-list ul").hide();
                });
            
                //  Draggable
                $("#dimension-list span, #measure-list span").draggable({
                    cancel:         '.not-draggable',
                    helper:         function(){
                        return $(this).clone().appendTo('body').css('zIndex',5).show();
                    }
                });

                //  Droppable and sortable
                $('#row-axis, #column-axis').droppable({
                    accept:         '#dimension-list span, #measure-list span',
                    activeClass:    "",
                    hoverClass:     "",
                    accept:         ":not(.ui-sortable-helper)",
                    drop:           function(event, ui) {
                        var dropped_member = ui.draggable.children(':nth-child(1)').text();
                        var member = ui.draggable.text().replace(dropped_member, '');
                        if($('#column-drop ul span:contains("'+dropped_member+'"), #row-drop ul span:contains("'+dropped_member+'")').length > 0) {
                            return false;
                        }
                        $(this).find(".placeholder").remove();
                        $("<li></li>").text(member).appendTo(this).append(' <a href="#" class="remove small">x</a>').append('<span class="hide">'+ui.draggable.children(':nth-child(1)').text()+'</span>');
                        createOutput();
                    }
                }).sortable({
                    items: "li:not(.placeholder)",
                    forceHelperSize: true,
                    helper: '.ui-draggable-dragging',
                    placeholder: 'placeholder-sort',
                    update: function() {
                        createOutput();
                    }
                });
                $.unblockUI();
            }
        }
    });
}

/*
 * createOutput
 * Create output for seeing whats on rows and columns
 */

function createOutput () {
    $('#rows, #columns').html('')
    $('#column-axis').find('.hide').each(function(index) {
        $('#columns').append(' '+$(this).text());
    });
    $('#row-axis').find('.hide').each(function(index) {
        $('#rows').append(' '+$(this).text());
    });
}
