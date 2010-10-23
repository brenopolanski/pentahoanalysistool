/*
 * This is the controller for PATui. It will lazy load and then initialize the model and the view.
 */

/*
 * The extent of our globalization at this point, I'm guessing
 */
var PAT_TITLE       =   "<strong>PAT<em>ui</em> Demo</strong>";
var LOADING_DATA    =   "";
var NO_DIMENSIONS   =   "No cube selected";
var NO_MEASURES     =   "No cube selected";
var BASE_URL		=	""; // Same domain
    
/*
 * Lazy load javascript files
 */
function lazy_load(script_filename) {
    var html_doc = document.getElementsByTagName('head').item(0);
    var js = document.createElement('script');
    js.setAttribute('language', 'javascript');
    js.setAttribute('type', 'text/javascript');
    js.setAttribute('src', script_filename);
    html_doc.appendChild(js);
    return false;
}

// model.NewQuery
// if success then call a method of view to create new query

/*
 * Load view and draw UI, load model and initialize session
 */
$(document).ready( function() {
	lazy_load("js/patui/view.js");
	view.drawUI();
	lazy_load("js/patui/model.js");
	model.init();
});