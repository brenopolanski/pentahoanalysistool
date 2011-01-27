/**
 * jQuery.fixer - Scrolling Table With Fixed Rows And Columns
 *
 * Copyright (c) 2011, Anand Inumpudi
 * Licensed under the MIT license http://creativecommons.org/licenses/MIT/
 * Version: 1.0 Date: 2011/01/21
 */
(function($) {
    //
    // plugin definition
    //
    $.fn.fixer = function(options) {
        var opts = $.extend({}, $.fn.fixer.defaults, options);
        return $(this)
        .each(
            function() {
                // only process tables
                if ($(this).get(0).tagName != "TABLE")
                    return;
                // load options
                var fixedrows = Math.round(opts.fixedrows);
                var fixedcols = Math.round(opts.fixedcols);
                var height = Math.round(opts.height);
                var width = Math.round(opts.width);
                var hidden = opts.hidden;
                // get the table into a local variable
                var fixertable = $(this);
                // create a shell to use for the subtables
                fixershell = $(this.cloneNode(false));
                fixershell.removeAttr("id");
                // get height of table and width of parent
                var tableheight = fixertable.height();
                var parentwidth = fixertable.parent().width() - 40;
                if (hidden) {
                    fixertable.evenIfHidden(function(element) {
                        tableheight = element.height();
                    });
                    fixertable.parent().evenIfHidden(
                        function(element) {
                            parentwidth = element.width() - 40;
                        });
                }
                // see if the user wants to deliberately set width
                var setwidth = false;
                if (width > 0)
                    setwidth = true;
                else
                    width = parentwidth;
                // get the row and column count
                var rows = $('tr', fixertable);
                var cols = $('th,td', rows[0]);
                var rowcount = rows.length;
                var colcount = cols.length;
                // if table has no content, nothing to do
                if (rowcount == 0 || colcount == 0)
                    return;
                // flags to indicate if rows are to be fixed,
                // columns are to be fixed, or both
                var fixrows = false;
                var fixcols = false;
                if (fixedrows > 0 && fixedrows < rowcount)
                    fixrows = true;
                else
                    fixedrows = 0;
                if (fixedcols > 0 && fixedcols < colcount)
                    fixcols = true;
                else
                    fixedcols = 0;
                // get the heights of first column cells and widths
                // of first row cells
                var heights = new Array();
                var widths = new Array();
                rows.each(function() {
                    if (hidden)
                        $(this).evenIfHidden(function(element) {
                            heights.push(element.height())
                        });
                    else
                        heights.push($(this).height());
                });
                cols.each(function() {
                    if (hidden)
                        $(this).evenIfHidden(function(element) {
                            widths.push(element.width() + 1)
                        });
                    else
                        widths.push($(this).width() + 1)
                });
                // calculate the height of the fixed rows and width
                // of the fixed columns
                var fixedheight = 0;
                var fixedwidth = 0;
                if (fixrows)
                    for ( var i = 0; i < fixedrows; i++)
                        fixedheight = fixedheight + heights[i] + 3;
                if (fixcols)
                    for ( var i = 0; i < fixedcols; i++)
                        fixedwidth = fixedwidth + widths[i] + 3;
                // no point fixing something if the fixed table
                // doesn't have
                // enough viewing area
                if (fixedheight > height) {
                    fixrows = false;
                    fixedrows = 0;
                }
                if (fixedwidth > width) {
                    fixcols = false;
                    fixedcols = 0;
                }
                if (!fixrows && !fixcols)
                    return;
                // remove table from DOM to speed up operations
                var placeholder = $('<table></table>');
                fixertable.replaceWith(placeholder);
                // set explicit heights and widths so that
                // everything remains aligned
                // when the table is broken up
                if (fixcols)
                    rows.each(function(i, e) {
                        $(this).height(heights[i]);
                    });
                if (fixrows) {
                    cols.each(function(i, e) {
                        div = $("<div></div>").append(
                            $(this).html()).css("width",
                            widths[i]);
                        $(this).empty();
                        $(this).append(div);
                    });
                    var secondcols = $('th,td', rows[fixedrows]);
                    secondcols.each(function(i, e) {
                        div = $("<div></div>").append(
                            $(this).html()).css("width",
                            widths[i]);
                        $(this).empty();
                        $(this).append(div);
                    });
                }
                // create the four tables
                var toplefttable;
                var toprighttable;
                var bottomlefttable;
                var bottomrighttable = fixertable;
                if (fixrows) {
                    toprighttable = fixershell.clone();
                    toprighttable.append(rows.slice(0, fixedrows));
                }
                if (fixcols) {
                    if (fixrows) {
                        toplefttable = fixershell.clone();
                        for (i = 0; i < fixedrows; i++) {
                            row = $(rows[i].cloneNode(false));
                            row.append($('td,th', rows[i]).slice(0,
                                fixedcols));
                            toplefttable.append(row);
                        }
                    }
                    bottomlefttable = fixershell.clone();
                    for (i = fixedrows; i < rowcount; i++) {
                        row = $(rows[i].cloneNode(false));
                        row.append($('td,th', rows[i]).slice(0,
                            fixedcols));
                        bottomlefttable.append(row);
                    }
                }
                // pack the tables into a wrapper
                var wrapper = $('<div class="table_wrapper"></div>');
                if (fixrows && fixcols) {
                    var topleft = $('<div class="table_nulls"><br></div>');
                    topleft.css("float", "left").css("overflow",
                        "hidden");
                    topleft.append(toplefttable);
                    wrapper.append(topleft);
                }
                if (fixrows) {
                    var topright = $('<div class="table_rows"><br/></div>');
                    topright.css("overflow-x", "hidden");
                    if ($.browser.msie)
                        topright.css("float", "left");
                    topright.append(toprighttable);
                    wrapper.append(topright);
                    var cleardiv1 = $("<div></div>");
                    cleardiv1.css("clear", "both");
                    wrapper.append(cleardiv1);
                }
                if (fixcols) {
                    var bottomleft = $('<div class="table_cols"></div>');
                    bottomleft.css("float", "left").css(
                        "overflow-y", "hidden").css(
                        "overflow-x", "scroll");
                    bottomleft.append(bottomlefttable);
                    wrapper.append(bottomleft);
                }
                var bottomright = $('<div class="table_data"></div>');
                bottomright.css("overflow-x", "scroll");
                if ($.browser.msie)
                    bottomright.css("float", "left");
                bottomright.append(bottomrighttable);
                wrapper.append(bottomright);
                if (fixcols) {
                    var cleardiv2 = $("<div></div>");
                    cleardiv2.css("clear", "both");
                    wrapper.append(cleardiv2);
                }
                if (tableheight > height) {
                    if (fixcols)
                        bottomleft.css("height", height
                            - fixedheight);
                    bottomright.css("height", height - fixedheight);
                    if (fixrows)
                        topright.css("overflow-y", "scroll");
                    bottomright.css("overflow-y", "scroll");
                }

                if (setwidth || $.browser.msie) {
                    // for ie, a fixed width wrapper div is
                    // necessary
                    // otherwise the divs won't be inline when the
                    // browser
                    // is resized to be too narrow
                    // base the width of the right side tables on
                    // this width
                    // while accounting for the fixed columns
                    wrapper.css("width", width);
                    bottomright
                    .css("width", width - fixedwidth - 1);
                    if (fixrows)
                        topright.css("width", width - fixedwidth
                            - 1);
                }
                // prevent browser resize from covering up mobile
                // columns
                if (fixcols)
                    wrapper.css("minWidth", fixedwidth + 50);
                // synchronize scrolling
                if (fixrows && fixcols)
                    bottomright.scroll(function() {
                        topright.scrollLeft(bottomright
                            .scrollLeft());
                        bottomleft.scrollTop(bottomright
                            .scrollTop());
                    });
                else if (fixcols)
                    bottomright.scroll(function() {
                        bottomleft.scrollTop(bottomright
                            .scrollTop());
                    });
                else if (fixrows)
                    bottomright.scroll(function() {
                        topright.scrollLeft(bottomright
                            .scrollLeft());
                    });
                // finally, add the fixed tables to the DOM
                placeholder.replaceWith(wrapper);
            });
    };

    $.fn.fixer.defaults = {
        width : 0,
        height : 500,
        fixedrows : 1,
        fixedcols : 1,
        hidden : false
    };
})(jQuery);