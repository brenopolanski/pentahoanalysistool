package org.pentaho.pat.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gwt.mosaic.ui.client.CollapsedListener;
import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.pentaho.pat.client.Application.ApplicationListener;
import org.pentaho.pat.client.events.SourcesConnectionEvents;
import org.pentaho.pat.client.images.PatImages;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.ui.panels.ToolBarPanel;
import org.pentaho.pat.client.ui.widgets.DataWidget;
import org.pentaho.pat.client.ui.widgets.OlapPanel;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ClickListener;
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * 
 * @author tom(at)wamonline.org.uk
 * 
 */

public class Pat implements EntryPoint, ConnectionListener {
	

	
	/**
	 * Get the token for a given content widget.
	 * 
	 * @return the content widget token.
	 */
	public static String getContentWidgetToken(DataWidget content) {
		String className = content.getClass().getName();
		className = className.substring(className.lastIndexOf('.') + 1);
		return className;
	}


	/**
	 * PatImages ImageBundle
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
	public static final String DEFAULT_STYLE_NAME = "Pat";

	/**
	 * A mapping of history tokens to their associated menu items.
	 */
	public static Map<String, TreeItem> itemTokens = new HashMap<String, TreeItem>();

	/**
	 * A mapping of menu items to the widget display when the item is selected.
	 */
	public static Map<TreeItem, DataWidget> itemWidgets = new HashMap<TreeItem, DataWidget>();

	/**
	 * The {@link Application}.
	 */
	private static Application app = new Application();

	
	/**
	 * Global Session ID
	 */
	private static String SESSION_ID;

	
	/**
	 * Pat Constructor
	 */
	public Pat() {
		super();
		
	}

	/**
	 * Set the content to the {@link DataWidget}.
	 * 
	 * @param content
	 *            the {@link DataWidget} to display
	 */
	private void displayContentWidget(final DataWidget content) {
		if (content != null) {
			if (!content.isInitialized()) {
				content.initialize();
			}
			app.setContent(content);
		}
	}

	/**
	 * Get the style name of the reference element defined in the current GWT
	 * theme style sheet.
	 * 
	 * @param prefix
	 *            the prefix of the reference style name
	 * @return the style name
	 */
	private static String getCurrentReferenceStyleName(String prefix) {
		String gwtRef = prefix + "-Reference-" + CUR_THEME;
		if (LocaleInfo.getCurrentLocale().isRTL()) {
			gwtRef += "-rtl";
		}
		return gwtRef;
	}



	
	/* (non-Javadoc)
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	public void onModuleLoad() {

		setSessionID();
		// Swap out the style sheets for the RTL versions if needed
		updateStyleSheets();

		// Create the application
		setupTitlePanel();
		// setupOptionsPanel();
		

		// Setup a history listener to reselect the associate menu item
		final HistoryListener historyListener = new HistoryListener() {
			public void onHistoryChanged(String historyToken) {
				TreeItem item = itemTokens.get(historyToken);
				if (item == null) {
					item = app.getMainMenu().getItem(0).getChild(0);
				}

				// Select the associated TreeItem
				app.getMainMenu().setSelectedItem(item, false);
				app.getMainMenu().ensureSelectedItemVisible();

				// Show the associated ContentWidget
				displayContentWidget(itemWidgets.get(item));
			}
		};

		History.addHistoryListener(historyListener);
		// Add an listener that sets the content widget when a menu item is
		// selected
		app.setListener(new ApplicationListener() {
			public void onMenuItemSelected(TreeItem item) {
				DataWidget content = itemWidgets.get(item);
				if (content != null && !content.equals(app.getContent())) {
					History.newItem(getContentWidgetToken(content));
				}
			}
		});

		// Show the initial example
		String initToken = History.getToken();
		if (initToken.length() > 0) {
			historyListener.onHistoryChanged(initToken);
		} else { // Use the first token available
			TreeItem firstItem = app.getMainMenu().getItem(0).getChild(0);
			app.getMainMenu().setSelectedItem(firstItem, false);
			app.getMainMenu().ensureSelectedItemVisible();
			displayContentWidget(itemWidgets.get(firstItem));
		}

		com.google.gwt.user.client.DOM.getElementById("splash").getStyle().setProperty("display", "none");
		
	}
	
	/**
	 * Returns the SESSION_ID
	 * @return SESSION_ID
	 */
	public static String getSessionID(){
		return SESSION_ID;
	}
	
	
	/**
	 * Sets the SESSION_ID 
	 */
	private static void setSessionID(){
		ServiceFactory.getSessionInstance().createSession(new AsyncCallback(){

			public void onFailure(Throwable arg0) {
			    Button errorBtn = new Button("Error");
			    errorBtn.addClickListener(new ClickListener() {
			      public void onClick(Widget sender) {
			        MessageBox.error("Error", "Failed to get Session ID(Thats not great)!");
			      }
			    });
			}

			public void onSuccess(Object arg0) {
				SESSION_ID = (String) arg0;
			}
			
		});
		
	}

	/**
	 * Create the title bar at the top of the Application.
	 * 
	 * @param constants
	 *            the constant values to use
	 */
	private void setupTitlePanel() {
		// Get the title from the internationalized constants
		String pageTitle = "<h1>" + ConstantFactory.getInstance().mainTitle()
				+ "</h1><h2>" + ConstantFactory.getInstance().mainSubTitle()
				+ "</h2>";

		// Add the title and some images to the title bar
		HorizontalPanel titlePanel = new HorizontalPanel();
		titlePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		// titlePanel.add(IMAGES.gwtLogo().createImage());
		titlePanel.add(new HTML(pageTitle));
		app.setTitleWidget(titlePanel);
	}

	/**
	 * Update the style sheets to reflect the current theme and direction.
	 * 
	 */
	public static void updateStyleSheets() {
		// Generate the names of the style sheets to include
		String gwtStyleSheet = "gwt/" + CUR_THEME + "/" + CUR_THEME + ".css";
		String gwtMosaicStyleSheet = "gwt/" + CUR_THEME + "/Mosaic.css";
		String showcaseStyleSheet = CUR_THEME + "/Showcase.css";
		String widgetStyleSheet = "/widgets.css";
		if (LocaleInfo.getCurrentLocale().isRTL()) {
			gwtStyleSheet = gwtStyleSheet.replace(".css", "_rtl.css");
			gwtMosaicStyleSheet = gwtMosaicStyleSheet.replace(".css",
					"_rtl.css");
			showcaseStyleSheet = showcaseStyleSheet.replace(".css", "_rtl.css");
		}

		// Find existing style sheets that need to be removed
		boolean styleSheetsFound = false;
		final HeadElement headElem = StyleSheetLoader.getHeadElement();
		final List<Element> toRemove = new ArrayList<Element>();
		NodeList<Node> children = headElem.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.getItem(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element elem = Element.as(node);
				if (elem.getTagName().equalsIgnoreCase("link")
						&& elem.getPropertyString("rel").equalsIgnoreCase(
								"stylesheet")) {
					styleSheetsFound = true;
					String href = elem.getPropertyString("href");
					// If the correct style sheets are already loaded, then we
					// should have
					// nothing to remove.
					if (!href.contains(gwtStyleSheet)
							&& !href.contains(gwtMosaicStyleSheet)
							&& !href.contains(showcaseStyleSheet)) {
						toRemove.add(elem);
					}
				}
			}
		}

		// Return if we already have the correct style sheets
		if (styleSheetsFound && toRemove.size() == 0) {
			return;
		}

		// Detach the app while we manipulate the styles to avoid rendering
		// issues
		RootPanel.get().remove(app);

		// Remove the old style sheets
		for (Element elem : toRemove) {
			headElem.removeChild(elem);
		}

		// Load the GWT theme style sheet
		String modulePath = GWT.getModuleBaseURL();
		Command callback = new Command() {
			public void execute() {
				// Different themes use different background colors for the body
				// element, but IE only changes the background of the visible
				// content
				// on the page instead of changing the background color of the
				// entire
				// page. By changing the display style on the body element, we
				// force
				// IE to redraw the background correctly.
				RootPanel.getBodyElement().getStyle().setProperty("display",
						"none");
				RootPanel.getBodyElement().getStyle()
						.setProperty("display", "");
				RootPanel.get().add(app);
			}
		};

		StyleSheetLoader.loadStyleSheet(modulePath + gwtStyleSheet,
				getCurrentReferenceStyleName("gwt"), null);
		StyleSheetLoader.loadStyleSheet(modulePath + gwtMosaicStyleSheet,
				getCurrentReferenceStyleName("mosaic"), null);
		// Load the showcase specific style sheet after the GWT & Mosaic theme
		// style
		// sheet so that custom styles supercede the theme styles.
		StyleSheetLoader.loadStyleSheet(modulePath + showcaseStyleSheet,
				getCurrentReferenceStyleName("Application"), callback);
		StyleSheetLoader.loadStyleSheet(modulePath + widgetStyleSheet, 
				getCurrentReferenceStyleName("widgets"), null);
		
	}
	
	
	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionBroken(com.google.gwt.user.client.ui.Widget)
	 */
	public void onConnectionBroken(Widget sender) {
		// TODO Auto-generated method stub
		
	}
	
	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionMade(com.google.gwt.user.client.ui.Widget)
	 */
	public void onConnectionMade(Widget sender) {
//		setupCubeMenu();
		
	}
	
	
	
}
