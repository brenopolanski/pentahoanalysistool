/*
 * Copyright 2008 Google Inc.
 * 
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.Widget;

/**
 * A widget used to show the examples in the content panel. It includes a tab
 * bar with options to view the example, view the source, or view the css style
 * rules.
 * <p>
 * This widget uses a lazy initialization mechanism so that the content is not
 * rendered until the onInitialize method is called, which happens the first
 * time the widget is added to the page. The data in the source and css tabs are
 * loaded using RPC call to the server.
 * 
 * <h3>CSS Style Rules</h3>
 * <ul class="css">
 * <li>.mosaic-sc-Page { Applied to the entire widget }</li>
 * <li>.sc-ContentWidget-tabBar { Applied to the TabBar }</li>
 * <li>.sc-ContentWidget-deckPanel { Applied to the DeckPanel }</li>
 * <li>.sc-ContentWidget-name { Applied to the name }</li>
 * <li>.sc-ContentWidget-description { Applied to the description }</li> </ul>
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public abstract class DataWidget extends LayoutPanel {

	/**
	 * The default style name.
	 */
	protected static final String DEFAULT_STYLE_NAME = "Pat-sc-ContentWidget";

	/**
	 * A boolean indicating whether or not this widget has been initialized.
	 */
	private boolean initialized = false;

	/**
	 * Constructor.
	 * 
	 * @param constants
	 *            the constants
	 */
	public DataWidget() {
		
		setStyleName(DEFAULT_STYLE_NAME);
	}

	

	/**
	 * Get the description of this example.
	 * 
	 * @return a description for this example
	 */
	public abstract String getDescription();

	public final String getId() {
		final String s = this.getClass().getName();
		return s.substring(s.lastIndexOf('.') + 1, s.length());
	}

	/**
	 * Get the name of this example to use as a title.
	 * 
	 * @return a name for this example
	 */
	public abstract String getName();

	/**
	 * Initialize this widget by creating the elements that should be added to
	 * the page.
	 */
	public final void initialize() {
		if (initialized) {
			return;
		}
		initialized = true;

		
		
		


		
	
		// Initialize the widget and add it to the page
		final Widget widget = onInitialize();
		if (widget != null) {
			add(widget, new BoxLayoutData(FillStyle.BOTH));
		}
		onInitializeComplete();
	}

	public final boolean isInitialized() {
		return initialized;
	}

	public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
		return true;
	}

	/**
	 * When the widget is first initialized, this method is called. If it
	 * returns a Widget, the widget will be added as the first tab. Return
	 * <code>null</code> to disable the first tab.
	 * 
	 * @return
	 */
	protected abstract Widget onInitialize();

	/**
	 * Called when initialization has completed and the widget has been added to
	 * the page.
	 */
	protected void onInitializeComplete() {
		// Nothing to do here!
	}

	@Override
	protected void onLoad() {
		// Initialize this widget if we haven't already
		initialize();
	}

}
