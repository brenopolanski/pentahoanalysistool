package org.pentaho.pat.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gwt.beansbinding.core.client.util.GWTBeansBinding;
import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.CollapsedListener;
import org.gwt.mosaic.ui.client.DecoratedTabLayoutPanel;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.pentaho.pat.client.Application.ApplicationListener;
import org.pentaho.pat.client.images.PatImages;
import org.pentaho.pat.client.panels.CubeExplorerPanel;
import org.pentaho.pat.client.panels.NorthPanel;
import org.pentaho.pat.client.panels.SouthPanel;
import org.pentaho.pat.client.util.ConstantFactory;
import org.pentaho.pat.client.widgets.ContentWidget;
import org.pentaho.pat.client.widgets.OlapPanel;

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
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * 
 * @author Tom Barber
 * 
 */
public class Pat implements CollapsedListener, EntryPoint {
	/**
	 * The type passed into the
	 * {@link org.gwt.mosaic.showcase.generator.ShowcaseGenerator}.
	 */
	private static final class GeneratorInfo {
	}
													static {
		GWTBeansBinding.init();
	}
	/**
	 * Get the token for a given content widget.
	 * 
	 * @return the content widget token.
	 */
	private static String getContentWidgetToken(ContentWidget content) {
		String className = content.getClass().getName();
		className = className.substring(className.lastIndexOf('.') + 1);
		return className;
	}
	/**
	 * Add an option to the main menu.
	 * 
	 * @param parent
	 *            the {@link TreeItem} that is the option
	 * @param content
	 *            the {@link ContentWidget} to display when selected
	 * @param image
	 *            the icon to display next to the {@link TreeItem}
	 */
	private static void setupMainMenuOption(TreeItem parent,
			ContentWidget content, AbstractImagePrototype image) {
		// Create the TreeItem
		TreeItem option = parent.addItem(image.getHTML() + " "
				+ content.getName());

		// Map the item to its history token and content widget
		itemWidgets.put(option, content);
		itemTokens.put(getContentWidgetToken(content), option);
	}
	private CubeExplorerPanel cubeExplorerPanel; // Dimension GUI for defining
	// the
	// query model
	private OlapPanel olapPanel; // Main Window
	private NorthPanel northPanel; // Contains the MDX and Filter GUI
	private SouthPanel drillPanel; // Contains the drill down information

	public static LayoutPanel borderLayoutPanel;

	private LayoutPanel boxPanel;

	private DecoratedTabLayoutPanel tabPanel;

	public static final PatImages IMAGES = (PatImages) GWT
			.create(PatImages.class);

	/**
	 * The current style theme.
	 */
	public static String CUR_THEME = PatConstants.STYLE_THEMES[0];

	/**
	 * The base style name.
	 */
	public static final String DEFAULT_STYLE_NAME = "Mosaic";

	/**
	 * A mapping of history tokens to their associated menu items.
	 */
	private static Map<String, TreeItem> itemTokens = new HashMap<String, TreeItem>();

	/**
	 * A mapping of menu items to the widget display when the item is selected.
	 */
	private static Map<TreeItem, ContentWidget> itemWidgets = new HashMap<TreeItem, ContentWidget>();

	/**
	 * The {@link Application}.
	 */
	private Application app = new Application();

	public Pat() {
		super();

		// init();
	}

	/**
	 * Set the content to the {@link ContentWidget}.
	 * 
	 * @param content
	 *            the {@link ContentWidget} to display
	 */
	private void displayContentWidget(final ContentWidget content) {
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
	private String getCurrentReferenceStyleName(String prefix) {
		String gwtRef = prefix + "-Reference-" + CUR_THEME;
		if (LocaleInfo.getCurrentLocale().isRTL()) {
			gwtRef += "-rtl";
		}
		return gwtRef;
	}

	public void onCollapsedChange(Widget sender) {
		// TODO Auto-generated method stub

	}

	public void onModuleLoad() {

		// Swap out the style sheets for the RTL versions if needed
		updateStyleSheets();

		// Create the application
		setupTitlePanel();
		setupMainLinks();
		// setupOptionsPanel();
		setupMainMenu();

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
				ContentWidget content = itemWidgets.get(item);
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
	 * Create the main links at the top of the application.
	 * 
	 * @param constants
	 *            the constants with text
	 */
	private void setupMainLinks() {
		// Link to PAT Homepage
		app.addLink(new HTML("<a href=\""
				+ ConstantFactory.getInstance().pat_homepage() + "\">"
				+ ConstantFactory.getInstance().mainLinkPat() + "</a>"));

		// Link to Pentaho Homepage
		app.addLink(new HTML("<a href=\""
				+ ConstantFactory.getInstance().pentaho_homepage() + "\">"
				+ ConstantFactory.getInstance().mainLinkHomepage() + "</a>"));

	}

	/**
	 * Setup all of the options in the main menu.
	 * 
	 * @param constants
	 *            the constant values to use
	 */
	private void setupMainMenu() {
		Tree mainMenu = app.getMainMenu();

		TreeItem homeMenu = mainMenu.addItem("Home");
		setupMainMenuOption(homeMenu, new OlapPanel("Blah"), IMAGES.cube());
		setupMainMenuOption(homeMenu, new OlapPanel("Blah2"), IMAGES.cube());
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
	 */
	private void updateStyleSheets() {
		// Generate the names of the style sheets to include
		String gwtStyleSheet = "gwt/" + CUR_THEME + "/" + CUR_THEME + ".css";
		String gwtMosaicStyleSheet = "gwt/" + CUR_THEME + "/Mosaic.css";
		String showcaseStyleSheet = CUR_THEME + "/Showcase.css";
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
	}
}
