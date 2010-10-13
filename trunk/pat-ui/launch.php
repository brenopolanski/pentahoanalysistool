<?php
// start the session
session_start();

// check if the username is set, if not logout
if (!isset($_SESSION['username'])) {
    header('Location: logout.php');
}
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>PATui - Analysis View</title>
        <!-- Blueprint -->
        <link rel="stylesheet" href="css/blueprint/screen.css" type="text/css" media="screen, projection">
        <link rel="stylesheet" href="css/blueprint/print.css" type="text/css" media="print">
        <!--[if lt IE 8]><link rel="stylesheet" href="css/blueprint/ie.css" type="text/css" media="screen, projection"><![endif]-->
        <link rel="stylesheet" href="css/styles.css" type="text/css" media="screen, projection">
        <!-- jQuery -->
        <script type="text/javascript" src="js/jquery.js"></script>
        <script type="text/javascript" src="js/jquery.ui.js"></script>
        <script type="text/javascript" src="js/jquery.layout.js"></script>
        <script type="text/javascript" src="js/jquery.blockui.js"></script>
        <script type="text/javascript" src="js/jquery.quicktree.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                
                // BlockUI
                $.blockUI.defaults.overlayCSS = {};
                $.blockUI({ message: '<div class="blockOverlay-inner">Loading available data, please wait...</div>' });

                // jQuery UI Layout
                $('body').layout({
                    north__closable:            false
                    , north__resizable:         false
                    , north__spacing_open:      4
                    , north__spacing_closed:    4
                    , north__size:              30
                    , west__spacing_open:       4
                    , west__spacing_closed:     4
                    , west__size:               250
                    , west__resizable:          true
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
                    $.blockUI({ message: '<div class="blockOverlay-inner">Loading dimensions and measures, please wait...</div>' });
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
                $.getJSON('inc/get_data.php', function(data) {
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
        </script>
    </head>
    <body id="launch">
        <!-- North / Toolbar -->
        <div id="toolbar" class="ui-layout-north">
            <ul id="menu-list">
                <li><a class="home" href="launch.php" title="Home">&nbsp;</a></li>
                <li><a class="logout" href="logout.php" title="Logout">&nbsp;</a></li>
            </ul>
        </div>
        <!-- Eof North / Toolbar -->
        <!-- West / Sidebar -->
        <div id="sidebar" class="ui-layout-west">
            <h3 class="bottom first">Data</h3>
            <div class="sidebar-inner">
                <select id="data-list">
                    <option value="none">Select a cube</option>
                </select>
            </div>
            <h3 class="bottom">Dimensions</h3>
            <div class="sidebar-inner">
                <ul id="dimension-list">
                    <li class="placeholder quiet"><em>No dimensions available</em></li>
                </ul>
            </div>
            <h3 class="bottom">Measures</h3>
            <div class="sidebar-inner">
                <ul id="measure-list">
                    <li class="placeholder quiet"><em>No measures available</em></li>
                </ul>
            </div>
        </div>
        <!-- Eof West / Sidebar -->
        <!-- Content / Center -->
        <div class="ui-layout-center">
            <div id="content">
                <div id="column-drop">
                    <div class="column-drop-title">
                        <strong>Columns</strong>
                    </div>
                    <div class="column-drop-area">
                        <ol class="top bottom">
                            <li class="placeholder quiet"><em>Drop dimensions and measures for columns here</em></li>
                        </ol>
                    </div>
                    <div class="clear"></div>
                </div>
                <div id="row-drop">
                    <div class="row-drop-title">
                        <strong>Rows</strong>
                    </div>
                    <div class="row-drop-area">
                        <ol class="top bottom">
                            <li class="placeholder quiet"><em>Drop dimensions and measures for rows here</em></li>
                        </ol>
                    </div>
                    <div class="clear"></div>
                    <hr class="space" />
                    <hr />
                    * Dimension names are not draggable yet i.e. Markets.<br/>
                    * Quadrant Analysis cube does not work due to URI issue.
                </div>
            </div>
        </div>
        <!--Eof Content / Center-->
    </body>
</html>