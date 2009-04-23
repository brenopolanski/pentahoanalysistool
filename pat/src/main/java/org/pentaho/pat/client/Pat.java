/*
 * Copyright 2009 Thomas Barber.  All rights reserved. 
 * This software was developed by Thomas Barber and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * Analysis Tool.  The Initial Developer is Thomas Barber.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 *
 *
 * @created Apr 23, 2009
 * @author Tom Barber
 */


package org.pentaho.pat.client;

import java.util.ArrayList;
import java.util.List;

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.images.PatImages;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import org.pentaho.pat.client.util.GlobalConnectionListeners;
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 *
 * @author tom(at)wamonline.org.uk
 */

public class Pat implements EntryPoint { // NOPMD by bugg on
    // 21/04/09 05:30

	/**
     * PatImages ImageBundle.
     */
    public static final PatImages IMAGES = (PatImages) GWT
	    .create(PatImages.class);

    /**
     * The current style theme.
     */
    
    public static String CUR_THEME = ConstantFactory.getInstance().STYLE_THEMES[0];

    /**
     * The base style name.
     */
    public static final String DEF_STYLE_NAME = "Pat"; //$NON-NLS-1$

    /**
     * The {@link Application}.
     */
    private static Application app = new Application();

    /**
     * Global Session ID.
     */
    private static String sessionid;

    /**
     * Get the style name of the reference element defined in the current GWT
     * theme style sheet.
     *
     * @param prefix
     *            the prefix of the reference style name
     * @return the style name
     */
    private static String getCurrentReferenceStyleName(final String prefix) {
	String gwtRef = prefix + "-Reference-" + CUR_THEME; //$NON-NLS-1$
	if (LocaleInfo.getCurrentLocale().isRTL()) {
	    gwtRef += "-rtl"; //$NON-NLS-1$
	}
	return gwtRef;
    }

    /**
     * Returns the SESSION_ID.
     *
     * @return SESSION_ID
     */
    public static String getSessionID() {
	return sessionid;
    }

    /**
     * Sets the SESSION_ID.
     */
    private static void setSessionID() {
	ServiceFactory.getSessionInstance().createSession(
		new AsyncCallback<String>() {

		    public void onFailure(final Throwable arg0) {
			MessageBox.error(ConstantFactory.getInstance().error(),
				MessageFactory.getInstance().failedSessionID(
					arg0.getLocalizedMessage()));
		    }

		    public void onSuccess(final String arg0) {
			sessionid = arg0;
		    }

		});

    }

    /**
     * Update the style sheets to reflect the current theme and direction.
     */
    public static void updateStyleSheets() {
	// Generate the names of the style sheets to include
	String gwtStyleSheet = "gwt/" + CUR_THEME + "/" + CUR_THEME + ".css"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	String gwtMosStyleSheet = "gwt/" + CUR_THEME + "/Mosaic.css"; //$NON-NLS-1$ //$NON-NLS-2$
	String scStyleSheet = CUR_THEME + "/Showcase.css"; //$NON-NLS-1$
	final String widgetStyleSheet = "/widgets.css"; //$NON-NLS-1$ // NOPMD by bugg on 21/04/09 05:35
	final String halogenStyleSheet = "/halogen.css"; //$NON-NLS-1$ // NOPMD by bugg on 21/04/09 05:35
	if (LocaleInfo.getCurrentLocale().isRTL()) {
	    gwtStyleSheet = gwtStyleSheet.replace(".css", "_rtl.css"); //$NON-NLS-1$ //$NON-NLS-2$
	    gwtMosStyleSheet = gwtMosStyleSheet.replace(".css", //$NON-NLS-1$
		    "_rtl.css"); //$NON-NLS-1$
	    scStyleSheet = scStyleSheet.replace(".css", "_rtl.css"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	// Find existing style sheets that need to be removed
	boolean styleSheetsFound = false;
	final HeadElement headElem = StyleSheetLoader.getHeadElement();
	final List<Element> toRemove = new ArrayList<Element>();
	final NodeList<Node> children = headElem.getChildNodes();
	for (int i = 0; i < children.getLength(); i++) {
	    final Node node = children.getItem(i);
	    if (node.getNodeType() == Node.ELEMENT_NODE) {
		final Element elem = Element.as(node);
		if (elem.getTagName().equalsIgnoreCase("link") //$NON-NLS-1$
			&& elem.getPropertyString("rel").equalsIgnoreCase(//$NON-NLS-1$
				"stylesheet")) { //$NON-NLS-1$
		    styleSheetsFound = true;
		    final String href = elem.getPropertyString("href"); //$NON-NLS-1$
		    // If the correct style sheets are already loaded, then we
		    // should have
		    // nothing to remove.
		    if (!href.contains(gwtStyleSheet)
			    && !href.contains(gwtMosStyleSheet)
			    && !href.contains(scStyleSheet)) {
			toRemove.add(elem);
		    }
		}
	    }
	}

	// Return if we already have the correct style sheets
	if (styleSheetsFound && toRemove.isEmpty()) {
	    return;
	}

	// Detach the app while we manipulate the styles to avoid rendering
	// issues
	RootPanel.get().remove(app);

	// Remove the old style sheets
	for (final Element elem : toRemove) {
	    headElem.removeChild(elem);
	}

	// Load the GWT theme style sheet
	final String modulePath = GWT.getModuleBaseURL();
	final Command callback = new Command() {
	    public void execute() {
		// Different themes use different background colors for the body
		// element, but IE only changes the background of the visible
		// content
		// on the page instead of changing the background color of the
		// entire
		// page. By changing the display style on the body element, we
		// force
		// IE to redraw the background correctly.
		RootPanel.getBodyElement().getStyle().setProperty("display", //$NON-NLS-1$
			"none"); //$NON-NLS-1$
		RootPanel.getBodyElement().getStyle()
			.setProperty("display", ""); //$NON-NLS-1$ //$NON-NLS-2$
		RootPanel.get().add(app);
	    }
	};

	StyleSheetLoader.loadStyleSheet(modulePath + gwtStyleSheet,
		getCurrentReferenceStyleName("gwt"), null); //$NON-NLS-1$
	StyleSheetLoader.loadStyleSheet(modulePath + gwtMosStyleSheet,
		getCurrentReferenceStyleName("mosaic"), null); //$NON-NLS-1$
	// Load the showcase specific style sheet after the GWT & Mosaic theme
	// style
	// sheet so that custom styles supercede the theme styles.
	StyleSheetLoader.loadStyleSheet(modulePath + scStyleSheet,
		getCurrentReferenceStyleName("Application"), callback); //$NON-NLS-1$
	StyleSheetLoader.loadStyleSheet(modulePath + widgetStyleSheet,
		getCurrentReferenceStyleName("widgets"), null); //$NON-NLS-1$
	StyleSheetLoader.loadStyleSheet(modulePath + halogenStyleSheet,
			getCurrentReferenceStyleName("halogen"), null); //$NON-NLS-1$

    }
    
    /*
     * (non-Javadoc)
     *
     * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
     */
    /**
	 *
	 */
    public final void onModuleLoad() {
	// Create a Pat unique session ID
	setSessionID();

	// Swap out the style sheets for the RTL versions if needed
	updateStyleSheets();

	// Create the application
	setupTitlePanel();
	// setupOptionsPanel();

	// hide splash
	com.google.gwt.user.client.DOM
		.getElementById("splash").getStyle().setProperty("display", "none"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    }

    /**
     * Create the title bar at the top of the Application.
     */
    private void setupTitlePanel() {
	// Get the title from the internationalized constants
	final String pageTitle = "<h1>" + ConstantFactory.getInstance().mainTitle() //$NON-NLS-1$
		+ "</h1><h2>" + ConstantFactory.getInstance().mainSubTitle() //$NON-NLS-1$
		+ "</h2>"; //$NON-NLS-1$

	// Add the title and some images to the title bar
	final HorizontalPanel titlePanel = new HorizontalPanel();
	titlePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	// titlePanel.add(IMAGES.gwtLogo().createImage());
	titlePanel.add(new HTML(pageTitle));
	app.setTitleWidget(titlePanel);
    }

}
