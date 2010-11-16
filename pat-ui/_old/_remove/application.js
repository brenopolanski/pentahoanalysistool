$(document).ready(function () {
    //  Clear cookies
    $.cookie('queryid', null);
    $.cookie('sessionid', null);
    






    //  Load available schemas and cubes
    load_data();

    //  Remove links

    //  Remove links on column axis items
    $('#column-drop ul li .remove').live('click', function(){
        $(this).parent().remove();
        if($('#column-drop ul li').length == 0) {
            $('#column-drop ul').append('<li class="quiet placeholder">Drop column axis items here</li>');
            run_query();
        }
        run_query();
    });
    //  Remove links on rows axis items
    $('#row-drop ul li .remove').live('click', function(){
        $(this).parent().remove();
        if($('#row-drop ul li').length == 0) {
            $('#row-drop ul').append('<li class="quiet placeholder">Drop row axis items here</li>');
            run_query();
        }
        run_query();
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
        view.new_query($(this).val());
    });
});



/*
 *  run_query()
 *  Runs a query by using a POST method to PAT's server
 */

function run_query() {
    
    var data_string = $('#data-list').val();
    var split_string = data_string.split("|");
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
                //  Convert output to a JSON object
                var obj = $.parseJSON(data);
                //  Set the queryid into a cookie
                $.cookie('queryid', obj['@queryid']);
            }
        }
    });
    
    //  Before printing JSON count how many measures and dimensions
    //  are on both the column and row axis
    var col_meas_count = $('#column-axis li span:contains("[Measures]")').length;
    var row_meas_count = $('#row-axis li span:contains("[Measures]")').length;
    var col_dims_count = $('#column-axis li:not(.Measures, .placeholder)').length;
    var row_dims_count = $('#row-axis li:not(.Measures, .placeholder)').length;
    var output;

    if(col_meas_count == 0 && row_dims_count == 0 && row_meas_count == 0 && col_dims_count == 0){
        $('#output .json').html('');
        $('#result').html('');
        return false;
    }
    if(col_meas_count == 0 && row_dims_count == 0 || row_meas_count > 0 && col_dims_count > 0) {
        $('#output .json').html('Incompatible items!');
        $('#result').html('');
        return false;
    } else if(col_meas_count > 0 && row_dims_count == 0 || col_meas_count == 0 && row_dims_count > 0) {
        $('#output .json').html('You will need to drag one more dimension or measure!');
        $('#result').html('');
        return false;
    } else {
        $.blockUI({
            message: '<div class="blockOverlay-inner"><strong>PAT<em>ui</em> Demo</strong><br/>Running query...</span></div>'
        });
        //  root
        //  queryid
        //output  =    'curl -XPUT --basic -u admin:admin -HContent-type:application/json --data-binary \'';
        output  =   '{ "@queryid" : "'+$.cookie('queryid')+'", ';

        //  columns
        output  +=  '"axis" : [{ "@location" : "ROWS", "@nonempty" : "true", "dimensions" :  {';

        $('#row-axis li:not(.Measures)').each(function(index,item){
            //  @levelcaption
            var levelcaption = $(item).find(".levelcaption").text().trim();
            //  @levelname
            var levelname = $(item).find(".levelname").text().trim();
            var arr = levelname.split(".");
            //  @dimensioname
            var dimensionname = arr[0].replace('[','').replace(']','').trim();
            if(levelcaption === ""){
            // Do nothing
            }else {
                if(index > 0) {
                    output  +=  ', "@dimensionname" : "'+dimensionname+'",';
                }else{
                    output  +=  '"@dimensionname" : "'+dimensionname+'",';
                }
                output  +=  ' "levels" : [ {';
                output  +=  ' "@levelcaption" : "'+levelcaption+'",';
                output  +=  ' "@levelname" : "'+levelname+'" } ] ';
                if(index == row_dims_count-1) {
                    output += '} ';
                }
            }
        });
        //  Eof loop

        //  rows
        output += '},{ "@location" : "COLUMNS", "@nonempty" : "true", "dimensions" :  {';

        //  handle measures first

        //  loop through all measures in the column list
        $('#column-axis li.Measures').each(function(index,item){
            //  @levelcaption
            var levelcaption = $(item).find(".levelcaption").text().trim();
            //  @levelname
            var levelname = $(item).find(".levelname").text().trim();
            var arr = levelname.split(".");
            //  @dimensioname
            var dimensionname = arr[0].replace('[','').replace(']','').trim();

            //  If we drag only one measure onto the column axis
            if(col_meas_count == 1) {
                output +=  '"@dimensionname" : "'+dimensionname+'",';
                output +=  ' "levels": { "@levelcaption" : "MeasuresLevel", "@levelname" : "[Measures].[MeasuresLevel]",';
                output +=  '"members" : [';
                output += ' { "@membercaption" : "'+levelcaption+'", '
                output += ' "@membername" : "'+levelname+'",';
                output += ' "@status" : "INCLUSION", ';
                output += ' "@type" : "MEMBER" ';
                output += ' } ] ';
            }else{
                if(index == 0) {
                    output  +=  '"@dimensionname" : "'+dimensionname+'",';
                    output  +=  ' "levels": { "@levelcaption" : "MeasuresLevel", "@levelname" : "[Measures].[MeasuresLevel]",';
                    output  +=  '"members" : [';
                }
                if(index == 0) {
                    output += ' { "@membercaption" : "'+levelcaption+'", '
                }else{
                    output += ', { "@membercaption" : "'+levelcaption+'", '
                }
                output += ' "@membername" : "'+levelname+'",';
                output += ' "@status" : "INCLUSION", ';
                output += ' "@type" : "MEMBER" }';
                if(index == col_meas_count-1) {
                    output += ']';
                }
            }

        });
        //  Eof loop

        if(col_meas_count > 0){
            output += ' } }  ';
        }

        output += ' } ] }';

        //output += '\' http://demo.analytical-labs.com/rest/admin/query/SteelWheels/SteelWheelsSales/newquery/run?'+$.cookie('sessionid');
        $('#output .json').html(output);

        var data_string = $('#data-list').val();
        var split_string = data_string.split("|");
        var connectionid = split_string[0];
        var schemaname = split_string[1];
        var cubename = split_string[2];
        $.ajax({
            type:       'POST',
            url:        'inc/query.php',
            data:       'method=POST_RUN&connectionid='+connectionid+'&schemaname='+schemaname+'&cubename='+cubename+'&query='+output,
            datatype:   'json',
            success:    function(data){
                var obj = $.parseJSON(data);
                create_table(obj);
            }
        });
    }
}

/*
 *  create_table()
 */

function create_table(data){
    $('#result').html('');
    $('#result').append('<table id="result-table">');
    $('#result-table').append('<tr id="hd"></tr>');
    
    $('#row-axis li:not(.Measures)').each(function(index,item){
        var levelcaption = $(item).find(".levelcaption").text().trim();
        $('#hd').append('<th>'+levelcaption+'</th>')
    });
    $('#column-axis li.Measures').each(function(index,item){
        var levelcaption = $(item).find(".levelcaption").text().trim();
        $('#hd').append('<th>'+levelcaption+'</th>')
    });
    $(data.result).each(function(index,item){
        $('#result-table').append('<tr id="'+index+'"></tr>');
        $('#'+index).append('<td>'+item.ROW[0]+'</td>')
        $('#column-axis li.Measures').each(function(index2,measure){
            $('#'+index).append('<td>'+item.ROW[index2+1]+'</td>')
        });
    });
    $('#result-table').append('</table>');
    $.unblockUI();
}