/**
 * Object for containing tab metadata.
 * @class
 */
var Tab = function(tab_selector, content_selector) {
    this.tab = tab_selector;
    this.content = content_selector;
    this.data = {};
};

/**
 * Container object used for tracking tabs.
 * @class
 */
var TabContainer = function(tab_container, content_container) {
    $("<ul />").appendTo(tab_container);
    this.tab_container = tab_container.find("ul");
    this.content_container = content_container;
    this.tabs = new Array();
    this.selected = 0;

    /** Counts active tabs. */
    this.active_tabs = function() {
        active_tabs = 0;
        for (i = 0; i < this.tabs.length; i++) {
            if (typeof this.tabs[i] != "undefined") {
                active_tabs++;
            }
        }
        return active_tabs;
    }

    /** Remove a tab and reclaim memory. */
    this.remove_tab = function(index) {
    	model.new_query_id(index);
    	
        if (typeof this.tabs[index] != "undefined") {
            this.tabs[index].tab.remove();
            this.tabs[index].content.remove();
            delete this.tabs[index].data;
            delete this.tabs[index];
        }
        // Find the next tab and select it.
        for (next_tab = index; next_tab < this.tabs.length; next_tab++) {
            if (typeof this.tabs[next_tab] != "undefined") {
                this.select_tab(next_tab);
                return;
            }
        }
        // Check and make sure there are any tabs at all.
        if (this.active_tabs() == 0 && model.session_id != "") {
            // Create one if not.
            this.add_tab();
            return;
        }
        // If the last tab was removed, select the next to last tab.
        this.select_tab(this.index_from_tab(this.tab_container.find("li:last")));
    };

    /** Change the selected tab. */
    this.select_tab = function(index) {
        if (typeof this.tabs[index] != "undefined") {
            this.tab_container.find("li.selected").removeClass("selected");
            this.content_container.find(".tab").hide();
            this.tabs[index].tab.addClass("selected");
            resize_height();
            this.tabs[index].content.show();
            this.selected = index;
        }
    };

    /** Add a tab. */
    this.add_tab = function() {
        // Create the tab itself
        var new_index = this.tabs.length;
        var $new_tab = $("<li />");
        var $new_tab_link = $("<a />")
        .html("Unsaved query (" + (new_index + 1) + ")")
        .appendTo($new_tab);
        var $new_tab_closer = $("<span>Close Tab</span>")
        .addClass("close_tab")
        .appendTo($new_tab);
        $new_tab.appendTo(this.tab_container);

        // Create the content portion of the tab.
        $new_tab_content = $('<div />')
        .addClass("tab")
        .load(BASE_URL + "views/queries/", function() {
            view.load_cubes(new_index);
            resize_height();
        });
        $new_tab_content.appendTo(this.content_container);

        // Register the new tab with the TabContainer.
        this.tabs.push(new Tab($new_tab, $new_tab_content));
        this.select_tab(new_index);

    };

    /** Empty the tab container (used for logout) */
    this.clear_tabs = function() {
        for (i = 0; i < this.tabs.length; i++) {
            if (typeof this.tabs[i] != 'undefined') {
                this.remove_tab(i);
            }
        }
    };

    /** Determine whether or not the TabContainer is empty .*/
    this.is_empty = function() {
        // If the array is uninitialized, obviously return true
        if (this.tabs.length == 0)
            return true;
        // Check to see if there are any active tabs
        for (i = 0; i < this.tabs.length; i++) {
            if (typeof this.tabs[i] != 'undefined') {
                // An active tab still exists, return false
                return false;
            }
        }
        // If not, return true
        return true;
    };

    /** Get a tab_index from a tab instance. */
    this.index_from_tab = function($tab) {
        for (i = 0; i < this.tabs.length; i++) {
            if (typeof this.tabs[i] != "undefined" && $tab[0] == this.tabs[i].tab[0]) {
                return i;
            }
        }
        return -1;
    };

    /** Get a tab_index from a tab content. */
    this.index_from_content = function($content) {
        for (i = 0; i < this.tabs.length; i++) {
            if (typeof this.tabs[i] != "undefined" && $content[0] == this.tabs[i].content[0]) {
                return i;
            }
        }
        return -1;
    };
};