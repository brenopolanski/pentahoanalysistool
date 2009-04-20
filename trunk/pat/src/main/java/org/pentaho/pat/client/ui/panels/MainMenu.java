/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.ui.panels;

import java.util.HashMap;
import java.util.Map;

import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.StackLayoutPanel;
import org.gwt.mosaic.ui.client.Viewport;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.pentaho.pat.client.Application;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.Application.ApplicationImages;
import org.pentaho.pat.client.Application.ApplicationListener;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.ui.widgets.DataWidget;
import org.pentaho.pat.client.ui.widgets.WelcomePanel;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.Widget;

/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class MainMenu extends StackPanel {

	DimensionPanel dimensionPanel;
	
	/**
	 * Create StackPanel
	 */
	StackLayoutPanel stackPanel;


	/**
	 * A mapping of history tokens to their associated menu items.
	 */
	public static Map<String, TreeItem> itemTokens = new HashMap<String, TreeItem>();
	
	/**
	 * A mapping of menu items to the widget display when the item is selected.
	 */
	public static Map<TreeItem, DataWidget> itemWidgets = new HashMap<TreeItem, DataWidget>();

	/**
	 * @return the main menu.
	 */
	public static Tree getMainMenu() {
		return mainMenu;
	}

	/**
	 * The main menu.
	 */
	static Tree mainMenu;

	

	/**
	 * The {@link ApplicationListener}.
	 */
	ApplicationListener listener = null;

	/**
	 *TODO JAVADOC
	 *
	 */
	public MainMenu() {
		super();
		
		//stackPanel = new StackLayoutPanel();
		createMainMenu();
		setupMainMenu();
		
		this.add(new ScrollPanel(mainMenu), ConstantFactory.getInstance().cubes());

		
		
		dimensionPanel = new DimensionPanel();
		this.add(dimensionPanel, ConstantFactory.getInstance().dimensions());
		
		this.showStack(0);

		
		setListener(new ApplicationListener() {
			 public void onMenuItemSelected(TreeItem item) {

				   DataWidget content = itemWidgets.get(item);
                  if (content != null && !content.equals(getContent())) {
                   //       History.newItem(getContentWidgetToken(content));
                  }
          } 
  });

		TreeItem firstItem = getMainMenu().getItem(0).getChild(0);
        getMainMenu().setSelectedItem(firstItem, false);
        getMainMenu().ensureSelectedItemVisible();
        displayContentWidget(itemWidgets.get(firstItem));
	}

	/**
	 * @return the {@link Widget} in the content area
	 */
	public Widget getContent() {
		return Application.contentWrapper.getWidget(0);
	}

	/**
	 * Create the main menu.
	 */
	protected void createMainMenu() {
		// Setup the main menu
		ApplicationImages treeImages = GWT.create(ApplicationImages.class);
		mainMenu = new Tree(treeImages);
		mainMenu.setAnimationEnabled(true);
		mainMenu.addStyleName(Pat.DEFAULT_STYLE_NAME + "-menu"); //$NON-NLS-1$
		mainMenu.addTreeListener(new TreeListener() {
			public void onTreeItemSelected(TreeItem item) {
				if (listener != null) {
					if (item.getParentItem().getText().equals(ConstantFactory.getInstance().available_cubes())) {
	
						ServiceFactory.getSessionInstance().setCurrentCube(Pat.getSessionID(), item.getText().trim(), new AsyncCallback<String[]>() {
	
							public void onFailure(Throwable arg0) {
								MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedDimensionList(arg0.getLocalizedMessage()));
							}
	
							public void onSuccess(String[] arg0) {
								ServiceFactory.getSessionInstance().createNewQuery(Pat.getSessionID(), new AsyncCallback<String>() {
	
									public void onFailure(Throwable arg0) {
										MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedQueryCreate(arg0.getLocalizedMessage()));
									}
	
									public void onSuccess(String arg0) {
	
										ServiceFactory.getSessionInstance().setCurrentQuery(Pat.getSessionID(), arg0, new AsyncCallback<Object>() {
	
											public void onFailure(Throwable arg0) {
	
												MessageBox.error(ConstantFactory.getInstance().error() , MessageFactory.getInstance().no_query_set(arg0.getLocalizedMessage()));
											}
	
											public void onSuccess(Object arg0) {
												dimensionPanel.createDimensionList();
	
												stackPanel.showStack(1);
												stackPanel.layout(true);
											}
	
										});
	
									}
	
								});
	
							}
						});
	
					}
	
					getMainMenu().setSelectedItem(item, false);
					getMainMenu().ensureSelectedItemVisible();
	
					// Show the associated ContentWidget
					displayContentWidget(itemWidgets.get(item));
					Application.contentWrapper.layout(true);
	
				}
			}
	
			public void onTreeItemStateChanged(TreeItem item) {
			}
		});
	}

	/**
	 * Set the content to the {@link DataWidget}.
	 * 
	 * @param content
	 *            the {@link DataWidget} to display
	 */
	public static void displayContentWidget(final DataWidget content) {
		if (content != null) {
			if (!content.isInitialized()) {
				content.initialize();
			}
			Application.setContent(content);
		}
	}

	

	/**
	 * Setup all of the options in the main menu.
	 */
	public static void setupMainMenu() {
		Tree mainMenu = getMainMenu();

		TreeItem homeMenu = mainMenu.addItem(ConstantFactory.getInstance().home());
		setupMainMenuOption(homeMenu, new WelcomePanel(ConstantFactory.getInstance().welcome()), Pat.IMAGES.cube());

	}


	/**
	 * Generates a cube list for the Cube Menu
	 */
	private void setupCubeMenu() {
		ServiceFactory.getDiscoveryInstance().getCubes(Pat.getSessionID(), new AsyncCallback<String[]>() {
			public void onSuccess(String[] o) {

				Tree mainMenu = getMainMenu();
				TreeItem cubeMenu = mainMenu.addItem(ConstantFactory.getInstance().available_cubes());

				for (int i = 0; i < o.length; i++) {
					setupMainMenuOption(cubeMenu, new OlapPanel(o[i]), Pat.IMAGES.cube());
				}

			}

			public void onFailure(Throwable arg0) {
				MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedCubeList(arg0.getLocalizedMessage()));
			}
		});

	}


	
	

	/**
	 * Set the {@link ApplicationListener}.
	 * 
	 * @param listener
	 *            the listener
	 */
	public void setListener(ApplicationListener listener) {
		this.listener = listener;
	}

	/**
	 * Add an option to the main menu.
	 * 
	 * @param parent
	 *            the {@link TreeItem} that is the option
	 * @param content
	 *            the {@link DataWidget} to display when selected
	 * @param image
	 *            the icon to display next to the {@link TreeItem}
	 */
	private static void setupMainMenuOption(TreeItem parent, DataWidget content, AbstractImagePrototype image) {
		// Create the TreeItem
		TreeItem option = parent.addItem(image.getHTML() + " " //$NON-NLS-1$
				+ content.getName());

		// Map the item to its history token and content widget
		itemWidgets.put(option, content);
		itemTokens.put(getContentWidgetToken(content), option);
	}
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
}