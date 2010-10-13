$(document).ready(function () {

    // BlockUI
    $.blockUI.defaults.overlayCSS = {};
    $.blockUI({
        message: '<div class="blockOverlay-inner">Loading available data, please wait...</div>'
    });

    // jQuery UI Layout
    $('body').layout({
        north__closable:            false
        ,
        north__resizable:         false
        ,
        north__spacing_open:      4
        ,
        north__spacing_closed:    4
        ,
        north__size:              30
        ,
        west__spacing_open:       4
        ,
        west__spacing_closed:     4
        ,
        west__size:               250
        ,
        west__resizable:          true
    });


    // Load data
    load_data();

    // Create a new query
    $('#data-list').change(function(){
        if($(this).val() === "none"){
            $('#dimension-list').html('<li class="placeholder quiet"><em>No dimensions available</em></li>');
            $('#measure-list').html('<li class="placeholder quiet"><em>No measures available</em></li>');
            return false;
        }
        $.blockUI({
            message: '<div class="blockOverlay-inner">Loading dimensions and measures, please wait...</div>'
        });
        new_query($(this).val());
    });

    $('#column-drop ol li .remove').live('click', function(){
        $(this).parent().remove();
        if($('#column-drop ol li').length == 0) {
            $('#column-drop ol').append('<li class="quiet placeholder"><em>Drop dimensions and measures for columns here</em></li>');
        }
    });
    $('#row-drop ol li .remove').live('click', function(){
        $(this).parent().remove();
        if($('#row-drop ol li').length == 0) {
            $('#row-drop ol').append('<li class="quiet placeholder"><em>Drop dimensions and measures for rows here</em></li>');
        }
    });
    $(".trigger").live('click', function() {
        var child_id = 'child_'+$(this).attr('id');
        $('#'+child_id).toggle();
    });
});

function load_data() {
    $.getJSON('inc/info.php', function(data) {
        if(data == 0){
            alert('An error has occured!');
            $.unblockUI();
            return false;
        }else{
            $.each(data.connections.connection, function(i,connection){
                $.each(connection.schemas, function(i,schema){
                    $('#data-list').append('<optgroup label="'+schema['@schemaname']+'">');
                    $.each(schema.cubes, function(i,cube){
                        $('#data-list').append('<option value="'+connection['@connectionid']+'|'+schema['@schemaname']+'|'+cube['@cubename']+'">'+cube['@cubename']+'</option>');
                    });
                    $('#data-list').append('</optgroup>');
                    $.unblockUI();
                });
            });
        }
    });
}

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
            $('#dimension-list, #measure-list').html('');
            var obj = $.parseJSON(data);
            $.each(obj.axes, function(i,axis){
                $.each(axis.dimensions.dimension, function(i,dimension){
                    if(this['@dimensionname'] != "Measures"){
                        $('#dimension-list').append('<li id="'+this['@dimensionname']+'"><span id="'+this['@dimensionname']+'" class="trigger">'+this['@dimensionname']+'</span><span class="dimension-hidden">['+this['@dimensionname']+']</span></li>');
                        $('#'+this['@dimensionname']).append('<ul id="child_'+this['@dimensionname']+'"></ul>');
                        var dimension_name = this['@dimensionname'];
                        $.each(dimension.levels.level, function(i,level){
                            $('#dimension-list #child_'+dimension_name).append('<li><span>'+this['@levelcaption']+'</span><span class="dimension-hidden">'+this['@levelname']+'</span></li>');
                        });
                    }else{
                        $('#measure-list').append('<li id="'+this['@dimensionname']+'"><span id="'+this['@dimensionname']+'" class="trigger">'+this['@dimensionname']+'</span></li>');
                        $('#'+this['@dimensionname']).append('<ul id="child_'+this['@dimensionname']+'"></ul>');
                        var dimension_name = this['@dimensionname'];
                        $.each(dimension.levels.level.members.member, function(i,member){
                            $('#measure-list #child_'+dimension_name).append('<li id="'+this['@membercaption']+'"><span>'+this['@membercaption']+'</span><span class="dimension-hidden">'+this['@membername']+'</span></li>');
                        });
                    }
                });
                $("#dimension-list ul, #measure-list ul").hide();
            });
            $("#dimension-list li, #measure-list li").draggable({
                cancel: '.not-draggable',
                helper: function(){
                    return $(this).clone().appendTo('body').css('zIndex',5).show();
                },
                cursor: 'move'
            });
            $('#column-drop ol, #row-drop ol').droppable({
                accept:         '#dimension-list li, #measure-list li',
                activeClass:    "",
                hoverClass:     "",
                accept:         ":not(.ui-sortable-helper)",
                drop:           function(event, ui) {
                    var dropped_member = ui.draggable.children(':nth-child(2)').text();
                    if($('#column-drop ol span:contains("'+dropped_member+'"), #row-drop ol span:contains("'+dropped_member+'")').length > 0) {
                        alert('That already exists!');
                        return false;
                    }
                    $(this).find(".placeholder").remove();
                    $("<li></li>").text(ui.draggable.children(':first-child').text()).appendTo(this).append(' <a href="#" class="remove small">remove</a>').append('<span style="display:none">'+ui.draggable.children(':nth-child(2)').text()+'</span>');
                }
            }).sortable({
                items: "li:not(.placeholder)",
                forceHelperSize: true,
                helper: '.ui-draggable-dragging',
                placeholder: 'placeholder-sort',
                sort: function() {}
            });

            $.unblockUI();
        }
    });
}