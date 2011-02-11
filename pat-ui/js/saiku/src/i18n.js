(function( $ ){
	$.fn.i18n = function(po_file) {
		// If no PO file is provided, then don't translate anything
		if (! po_file)
			return this;
		
		// If key is not found, return original language
		var translate = function(key, po_file) {
			if (typeof po_file[key] == "undefined") {
				return false;
			} else {
				return po_file[key];
			}
		};		

		// Iterate over UI elements that need to be translated
		return $.each(this, function() {
			element = $(this);
			
			// Translate text
			if (element.text && po_file) {
				translated_text = translate( element.text(), po_file );
				if (translated_text)
					element.text( );
			}
			
			// Translate title
			if (element.attr && po_file)
				translated_text = translate( element.attr('title'), po_file );
				if (translated_text) 
					element.attr({ 'title': translated_text });
			
			// Remove class so this element isn't repeatedly translated
			if (element.removeClass)
				element.removeClass('.i18n');
		});
	};
})( jQuery );

// Have the server look up the user-set language and 
// attempt to automatically translate the interface

// TODO - if po is not available, prompt user to provide translation
// Fetch PO file and store for later use
var po_file;
var locale;

$.ajax({
	url: '/i18n/',
	type: 'GET',
	dataType: 'text',
	success: function(data) {
		locale = data.substring(0,2);
		$.ajax({
			url: '/i18n/' + locale + ".json",
			type: 'GET',
			dataType: 'json',
			success: function(data) {
				po_file = data;
			},
			error: function() {
				// Prompt user if they would like to contribute a translation
			}
		});
	}
});