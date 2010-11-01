$(document).ready(function () {

	var page_layout, tab_layout;

	tab_layout = {
		name: 'tab_layout',
		resizeWithWindow: false,
		center__paneSelector: ".workspace",
		west__paneSelector: ".sidebar",
		west__size: "250",
		south__paneSelector: ".status_bar",
		contentSelector: ".content"
	};

	page_layout = $("body").layout({
		name: 'page_layout',
		resizeWithWindowDelay: 250,
		north__paneSelector: "#header",
		center__paneSelector: "#tab_panel",
		center__onresize: resize_tab_panel_layout
	});
	
	window.tabs_loading = true;

	var $tabs;
	var tab_counter = 3;
	var $tabs = $('#tabs');

	$tabs.tabs({
		tabTemplate: '<li><a href="#{href}">#{label}</a><span class="ui-icon ui-icon-close">Remove Tab</span></li>',
		add: function(event, ui) {
			var index = $('#tabs ul li').length - 1;
			$('#tabs-'+tab_counter).appendTo('#tab_panel');
			$tabs.tabs('select', index);
		},
		show: function (event, ui) {
			if (tabs_loading) {
				tabs_loading = false;
				page_layout.resizeAll();
			} else {
				resize_tab_panel_layout(ui);
			}
		}
	});

	/*
     * add_tab
     * belongs to jquery ui tabs
     * add tab when the link is clicked
     */
    
	$('#add_tab').click(function(){
		var num_tabs = $('#tabs ul li').length;
		tab_counter++;
		$('#tab_panel').append('<div id="tabs-'+tab_counter+'" class="tab ui-tabs-panel ui-widget-content ui-corner-bottom tabs-ui-hide"><div class="sidebar"><div class="content">I\'m tab '+tab_counter+' sidebar</div></div><div class="workspace"><div class="content">I\'m tab '+tab_counter+' workspace</div></div><div class="status_bar"><div class="content">I\'m tab '+tab_counter+' status_bar</div></div></div>');
		$tabs.tabs('add', '#tabs-'+tab_counter, 'New Query ('+tab_counter+')');
	});

	/*
     * remove_tab
     * belongs to jquery ui tabs
     * remove the tab when icon is clicked
     */
     
	$('#tabs span.ui-icon-close').live('click', function() {
		var index = $('li',$tabs).index($(this).parent());
		var tab_list = $(this).parent();
		var tab_panel = $(this).prev().attr('href');
		var num_tabs = $('#tabs ul li').length;

		if( num_tabs == 1 ) {
			tab_counter++;
			$('#tab_panel').append('<div id="tabs-'+tab_counter+'" class="tab ui-tabs-panel ui-widget-content ui-corner-bottom tabs-ui-hide"><div class="sidebar"><div class="content">I\'m tab '+tab_counter+' sidebar</div></div><div class="workspace"><div class="content">I\'m tab '+tab_counter+' workspace</div></div><div class="status_bar"><div class="content">I\'m tab '+tab_counter+' status_bar</div></div></div>');
			$tabs.tabs('add', '#tabs-'+tab_counter, 'New Query ('+tab_counter+')');
		}

		tab_list.remove();
		$(tab_panel).remove();

		if ( num_tabs == 0 ) {
		} else if ( num_tabs == 1 ) {
			var link = $('#tabs ul li a:first').attr('href');
			$tabs.tabs('select', link );
		} else if ( num_tabs > 1 ) {
			if (index == 0) {
				var link = $('#tabs ul li a:first').attr('href');
				$tabs.tabs('select', link );
			}else{
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
		var index = $('#tabs ul li').index($('.ui-tabs-selected'));
		var tab_panel = $('#tabs ul li:eq('+index+') a').attr('href');

		if ($(tab_panel).data("layoutContainer")) {
			$(tab_panel).layout().resizeAll();
		} else {
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