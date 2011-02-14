/**
 * The PO file for the currently selected language
 */
var po_file;

/**
 * The user's current locale
 */
var locale;

(function( $ ){
	/**
	 * Internationalize selected elements with the provided PO file
	 */
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
			if (element.text()) {
				translated_text = translate( element.text(), po_file );
				if (translated_text) {
					element.data('original', element.text());
					element.text(translated_text);
				}
			}
			
			// Translate title
			if (element.attr('title'))
				translated_text = translate( element.attr('title'), po_file );
				if (translated_text) {
					element.data('original', element.attr('title'));
					element.attr({ 'title': translated_text });
				}
			
			// Remove class so this element isn't repeatedly translated
			element.removeClass('i18n');
			element.addClass('i18n_translated');
		});
	};
	
	$.fn.un_i18n = function() {
		// Iterate over UI elements to replace the original text
		return $.each(this, function() {
			element = $(this);
			
			if (element.text())
				element.text(element.data('original'));
			
			if (element.attr('title'))
				element.attr({ 'title': element.data('original') });
			
			element.addClass('i18n');
			element.removeClass('i18n_translated');
		});
	};
})( jQuery );

/**
 * Automatically internationalize the UI based on the user's Accept-Language header
 */
automatic_i18n = function() {
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
				}
			});
		}
	});
}();