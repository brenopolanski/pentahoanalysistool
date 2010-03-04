/*
 * Copyright (C) 2009 Tom Barber
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA *
 *
 * @created Apr 23, 2009 
 * @author Tom Barber
 */

package org.pentaho.pat.client;

import java.util.ArrayList;
import java.util.List;

import org.gwt.mosaic.ui.client.MessageBox;
import org.gwtwidgets.client.util.Location;
import org.gwtwidgets.client.util.WindowUtils;
import org.pentaho.pat.client.i18n.PatConstants;
import org.pentaho.pat.client.images.PatImages;
import org.pentaho.pat.client.util.State;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
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

	public static String CUR_THEME = PatConstants.STYLE_THEMES[0];

	/**
	 * The base style name.
	 */
	public static final String DEF_STYLE_NAME = "Pat"; //$NON-NLS-1$

	/**
	 * The {@link Application}.
	 */
	private static Application app;

	/**
	 * Global Session ID.
	 */

	private static State initialState = new State();
	
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
		return initialState.getSession();
	}

	/**
	 * Sets the SESSION_ID.
	 */
	private void assignSessionID(String session) {
	    if (session == null)
	    {
		    ServiceFactory.getSessionInstance().createSession(
				new AsyncCallback<String>() {
		
				    public void onFailure(final Throwable arg0) {
					MessageBox.error(ConstantFactory.getInstance().error(),
						MessageFactory.getInstance().failedSessionID(
							arg0.getLocalizedMessage()));
				    }
		
				    public void onSuccess(final String arg0) {
					initialState.setSession(arg0);
				    }
				});
		    }
		else {
			initialState.setSession(session);
		}
	}

	/**
	 * Update the style sheets to reflect the current theme and direction.
	 */
	public static void updateStyleSheets() {
		// Generate the names of the style sheets to include
		String gwtStyleSheet = "gwt/" + CUR_THEME + "/" + CUR_THEME + ".css"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String gwtMosStyleSheet = "gwt/" + CUR_THEME + "/mosaic.css"; //$NON-NLS-1$ //$NON-NLS-2$
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
		//app.removeFromParent();
		
		// Remove the old style sheets
		for (final Element elem : toRemove) {
			headElem.removeChild(elem);
		}
		
		// Load the GWT theme style sheet
		final String modulePath = GWT.getModuleBaseURL();
		final Command callback = new Command() {
			private int numStyleSheetsLoaded = 0;
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
				//app.attach();
			}
		};

		StyleSheetLoader.loadStyleSheet(modulePath + gwtStyleSheet,
				getCurrentReferenceStyleName("gwt"), callback); //$NON-NLS-1$
		StyleSheetLoader.loadStyleSheet(modulePath + gwtMosStyleSheet,
				getCurrentReferenceStyleName("mosaic"), callback); //$NON-NLS-1$
		// Load the showcase specific style sheet after the GWT & Mosaic theme
		// style
		// sheet so that custom styles supercede the theme styles.
		StyleSheetLoader.loadStyleSheet(modulePath + scStyleSheet,
				getCurrentReferenceStyleName("Application"), callback); //$NON-NLS-1$
		StyleSheetLoader.loadStyleSheet(modulePath + widgetStyleSheet,
				getCurrentReferenceStyleName("widgets"), callback); //$NON-NLS-1$
		StyleSheetLoader.loadStyleSheet(modulePath + halogenStyleSheet,
				getCurrentReferenceStyleName("halogen"), callback); //$NON-NLS-1$

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

		// parse possible parameters
		parseInitialStateFromParameter();
		// Create a Pat unique session ID
		
		app = new Application();
		// Swap out the style sheets for the RTL versions if needed
		updateStyleSheets();

		// Create the application
		setupTitlePanel();
		
		if (initialState.isConnected())
		{
			GlobalConnectionFactory.getInstance().getConnectionListeners().fireConnectionMade(new Widget());
		}
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
		
		if (initialState.getMode().isShowToolbar()) {
			app.setTitleWidget(titlePanel);
		}
		
	}

	private void parseInitialStateFromParameter() {
		Location loadURL = WindowUtils.getLocation();
		State.Mode mode = State.Mode.getModeByParameter(loadURL.getParameter("MODE"));
		if (mode == null)
			initialState.setMode(State.Mode.STANDALONE);
		else
			initialState.setMode(mode);
		String _sessionParam = loadURL.getParameter("SESSION");
		assignSessionID(_sessionParam);
		if (_sessionParam != null)
			initialState.setConnected(true);

	}

	public static State getInitialState() {
		return initialState;
	}

}