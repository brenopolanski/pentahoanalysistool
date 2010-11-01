debug_mode = true;

$(document).ready(function () {
	
	debug('Debug mode is '+debug_mode);

	debug('On document ready');

	debug('   Setup layout');

	var page_layout, tab_layout;

	debug('   Setup tab area layout options');
	tab_layout = {
		name: 'tab_layout',
		resizeWithWindow: false,
		center__paneSelector: ".workspace",
		west__paneSelector: ".sidebar",
		west__size: "250",
		south__paneSelector: ".status_bar",
		contentSelector: ".content"
	};

	debug('   Setup page layout options');
	page_layout = $("body").layout({
		name: 'page_layout',
		resizeWithWindowDelay: 250,
		north__paneSelector: "#header",
		center__paneSelector: "#tab_panel",
		center__onresize: resize_tab_panel_layout
	});
	
	debug('   It is true that we are loading this tab layout for the first time');
	window.tabs_loading = true;

	debug('   Setup tabs');
	var $tabs;

	debug('   Intialise the tab_counter at 3');
	var tab_counter = 3;

	debug('   Create $tabs variable for reuse');
	var $tabs = $('#tabs');

	debug('   Initialise tabs');
	$tabs.tabs({
		tabTemplate: '<li><a href="#{href}">#{label}</a><span class="ui-icon ui-icon-close">Remove Tab</span></li>',
		add: function(event, ui) {

			debug('   "add" callback called within $tabs.tabs({})');
			var index = $('#tabs ul li').length - 1;

			debug('   Move the new tab area from the ul#tabs and appendTo #tab_panel');
			$('#tabs-'+tab_counter).appendTo('#tab_panel');

			

			debug('   Select the new tab with an index of '+index);
			$tabs.tabs('select', index);

			debug('   Resize the new tab which has just been added');

		},
		show: function (event, ui) {
			debug('Show tab');
			debug('   "show" callback called within $tabs.tabs({})');

			debug('   tabs_loading is '+tabs_loading);
			if (tabs_loading) {

				tabs_loading = false;
				
				debug('   On the first document load resize all layouts');
				page_layout.resizeAll();

			} else {
			
				debug('   Resize the tab layout if switching between tabs after document load');
				resize_tab_panel_layout(ui);

			}

			debug('Eof show tab');
			
		}
	});

	debug('Eof on document ready');

	/*
     * add_tab
     * belongs to jquery ui tabs
     * add tab when the link is clicked
     */
    
	$('#add_tab').click(function(){
		
		debug('Add a new tab');

		var num_tabs = $('#tabs ul li').length;
		debug('   There are ' + num_tabs + ' tabs available');

		tab_counter++;
		debug('   Increment the tab_counter to '+tab_counter);

		debug('   Append the tab area to the #tab_panel');
		$('#tab_panel').append('<div id="tabs-'+tab_counter+'" class="tab ui-tabs-panel ui-widget-content ui-corner-bottom tabs-ui-hide"><div class="sidebar"><div class="content">I\'m tab '+tab_counter+' sidebar</div></div><div class="workspace"><div class="content">I\'m tab '+tab_counter+' workspace</div></div><div class="status_bar"><div class="content">I\'m tab '+tab_counter+' status_bar</div></div></div>');


		debug('   Add the tab using the "add" method');
		$tabs.tabs('add', '#tabs-'+tab_counter, 'New Query ('+tab_counter+')');

		debug('Eof add a new tab');
		
	});

	/*
     * remove_tab
     * belongs to jquery ui tabs
     * remove the tab when icon is clicked
     */
     
	$('#tabs span.ui-icon-close').live('click', function() {

		debug('Remove a tab');

		var index = $('li',$tabs).index($(this).parent());
		var tab_list = $(this).parent();
		var tab_panel = $(this).prev().attr('href');

		var num_tabs = $('#tabs ul li').length;
		debug('   There are '+num_tabs+' tabs left');

		debug('   Before we remove anything, if it is the last tab then we must add a new tab');

		if( num_tabs == 1 ) {
			debug('Add a new tab');

			tab_counter++;
			debug('   Increment the tab_counter to '+tab_counter);

			debug('   Append the tab area to the #tab_panel');
			$('#tab_panel').append('<div id="tabs-'+tab_counter+'" class="tab ui-tabs-panel ui-widget-content ui-corner-bottom tabs-ui-hide"><div class="sidebar"><div class="content">I\'m tab '+tab_counter+' sidebar</div></div><div class="workspace"><div class="content">I\'m tab '+tab_counter+' workspace</div></div><div class="status_bar"><div class="content">I\'m tab '+tab_counter+' status_bar</div></div></div>');

			debug('   Add the tab using the "add" method');
			$tabs.tabs('add', '#tabs-'+tab_counter, 'New Query ('+tab_counter+')');

			debug('Eof add a new tab');
		}

		debug('   Remove tab list <li/> with an index of '+index);
		tab_list.remove();

		debug('   Remove tab area <div/> with an id of '+tab_panel);
		$(tab_panel).remove();

		debug('   Select the next tab available');

		// if 0 then - end
		if ( num_tabs == 0 ) {

			debug('   No tabs left, this should NOT happen!');

		} else if ( num_tabs == 1 ) {

			debug('   A tab with an index of 0 has been removed and there is only 1 tab left');
			debug('   Find the first <li/> <a/> href');
			var link = $('#tabs ul li a:first').attr('href');

			$tabs.tabs('select', link );

		} else if ( num_tabs > 1 ) {

			debug('   2 or more tabs left')

			if (index == 0) {

				debug('   A tab with an index of 0 has been removed');
				debug('   Find the first <li/> <a/> href');
				var link = $('#tabs ul li a:first').attr('href');

				$tabs.tabs('select', link );

			}else{
				
				debug('   Find the last <li/> <a/> href');
				var link = $('#tabs ul li a:last').attr('href');

				$tabs.tabs('select', link );

			}
		}

		debug('Eof remove a tab');

	});


	/*
     * resize_tab_layout
     * belongs to jquery ui layout
     * resizes the tab layout when "shown"
     */

	function resize_tab_panel_layout(ui) {

		debug('  "resize_tab_panel_layout" method called');

		var index = $('#tabs ul li').index($('.ui-tabs-selected'));

		var tab_panel = $('#tabs ul li:eq('+index+') a').attr('href');
		
		debug('   Resizing a tab with an index of '+ index +' and panel of '+tab_panel);

		if ($(tab_panel).data("layoutContainer")) {
			
			debug('   The tab area already exists, so call resizeAll()');
			$(tab_panel).layout().resizeAll();

		} else {

			debug('   The tab does not exists, so intialise it now');
			$(tab_panel).layout(tab_layout);
				
		}

		return;
			
	}
});

/*
 * debug
 * debug(message);
 */

function debug(message) {
	if(debug_mode) {
		console.log(message);
	}
}