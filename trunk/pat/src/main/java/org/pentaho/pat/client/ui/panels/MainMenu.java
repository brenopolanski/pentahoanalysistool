/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.ui.panels;

import java.util.HashMap;
import java.util.Map;

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Application;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.Application.ApplicationImages;
import org.pentaho.pat.client.Application.ApplicationListener;
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

// TODO: Auto-generated Javadoc
/**
 * The Main Menu that contains the cube list and dimension list.
 *
 * @author bugg
 */
public class MainMenu extends StackPanel { // NOPMD by bugg on 21/04/09 05:42

	/**
  * Set the content to the {@link DataWidget}.
  *
  * @param content the {@link DataWidget} to display
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
	 * Get the token for a given content widget.
	 *
	 * @param content the content
	 *
	 * @return the content widget token.
	 */
	public static String getContentWidgetToken(final DataWidget content) {
		String className = content.getClass().getName();
		className = className.substring(className.lastIndexOf('.') + 1);
		return className;
	}

	/**
	 * Setup all of the options in the main menu.
	 */
	private static void setupMainMenu() {
		final Tree mainMenu = getMainMenu();

		final TreeItem homeMenu = mainMenu.addItem(ConstantFactory.getInstance().home());
		setupMainMenuOption(homeMenu, new WelcomePanel(ConstantFactory.getInstance().welcome()), Pat.IMAGES.cube());

	}

	/**
	 * Add an option to the main menu.
	 * 
	 * @param parent the {@link TreeItem} that is the option
	 * @param content the {@link DataWidget} to display when selected
	 * @param image the icon to display next to the {@link TreeItem}
	 */
	private static void setupMainMenuOption(final TreeItem parent, final DataWidget content, final AbstractImagePrototype image) {
		// Create the TreeItem
		final TreeItem option = parent.addItem(image.getHTML() + " " //$NON-NLS-1$
				+ content.getName());

		// Map the item to its history token and content widget
		ITEMWIDGETS.put(option, content);
		ITEMTOKENS.put(getContentWidgetToken(content), option);
	}

	/** The dimension panel, a scroll panel containing a dimension drop widget. */
	private final transient DimensionPanel dimensionPanel;

	/** A mapping of history tokens to their associated menu items. */
	public static final Map<String, TreeItem> ITEMTOKENS = new HashMap<String, TreeItem>();

	/** A mapping of menu items to the widget display when the item is selected. */
	public static final Map<TreeItem, DataWidget> ITEMWIDGETS = new HashMap<TreeItem, DataWidget>();

	/** The main menu. */
	private static Tree mainMenuTree;

	/**
	 * Gets the main menu.
	 * 
	 * @return the main menu.
	 */
	public static Tree getMainMenu() {
		return mainMenuTree;
	}

	/** The {@link ApplicationListener}. */
	private transient ApplicationListener listener = null;



	/**
	 * Constructor.
	 */
	public MainMenu() {
		super();

		createMainMenu();
		setupMainMenu();

		this.add(new ScrollPanel(mainMenuTree), ConstantFactory.getInstance().cubes());

		dimensionPanel = new DimensionPanel();
		this.add(dimensionPanel, ConstantFactory.getInstance().dimensions());

		this.showStack(0);


		setListener(new ApplicationListener() {
			public void onMenuItemSelected(final TreeItem item) {

				ITEMWIDGETS.get(item);

			}
		});

		final TreeItem firstItem = getMainMenu().getItem(0).getChild(0);
		getMainMenu().setSelectedItem(firstItem, false);
		getMainMenu().ensureSelectedItemVisible();
		displayContentWidget(ITEMWIDGETS.get(firstItem));
	}


	/**
	 * Create the main menu.
	 */
	protected final void createMainMenu() { // NOPMD by bugg on 21/04/09 05:43
		// Setup the main menu
		final ApplicationImages treeImages = GWT.create(ApplicationImages.class);
		mainMenuTree = new Tree(treeImages);
		mainMenuTree.setAnimationEnabled(true);
		mainMenuTree.addStyleName(Pat.DEF_STYLE_NAME + "-menu"); //$NON-NLS-1$
		mainMenuTree.addTreeListener(new TreeListener() {
			public void onTreeItemSelected(final TreeItem item) {
				if (listener != null) {
					if (item.getParentItem().getText().equals(ConstantFactory.getInstance().availablecubes())) {

						ServiceFactory.getSessionInstance().setCurrentCube(Pat.getSessionID(), item.getText().trim(), new AsyncCallback<String[]>() {

							public void onFailure(final Throwable arg0) {
								MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedDimensionList(arg0.getLocalizedMessage()));
							}

							public void onSuccess(final String[] arg0) {
								ServiceFactory.getSessionInstance().createNewQuery(Pat.getSessionID(), new AsyncCallback<String>() {

									public void onFailure(final Throwable arg0) {
										MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedQueryCreate(arg0.getLocalizedMessage()));
									}

									public void onSuccess(final String arg0) {

										ServiceFactory.getSessionInstance().setCurrentQuery(Pat.getSessionID(), arg0, new AsyncCallback<Object>() {

											public void onFailure(final Throwable arg0) {

												MessageBox.error(ConstantFactory.getInstance().error() , MessageFactory.getInstance().noqueryset(arg0.getLocalizedMessage()));
											}

											public void onSuccess(final Object arg0) {
												dimensionPanel.createDimensionList();

												MainMenu.this.showStack(1);
												//MainMenu.this.layout(true);
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
					displayContentWidget(ITEMWIDGETS.get(item));
					Application.getContentWrapper().layout(true);

				}
			}


			public void onTreeItemStateChanged(final TreeItem item) {
			}
		});
	}





	/**
	 * Gets the content.
	 * 
	 * @return the {@link Widget} in the content area
	 */
	public final Widget getContent() {
		return Application.getContentWrapper().getWidget(0);
	}

	/**
	 * Set the {@link ApplicationListener}.
	 * 
	 * @param listener the listener
	 */
	public final void setListener(final ApplicationListener listener) {
		this.listener = listener;
	}
	
	/**
	 * Generates a cube list for the Cube Menu.
	 */
	public final void setupCubeMenu() {
		ServiceFactory.getDiscoveryInstance().getCubes(Pat.getSessionID(), new AsyncCallback<String[]>() {
			public void onFailure(final Throwable arg0) {
				MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedCubeList(arg0.getLocalizedMessage()));
			}

			public void onSuccess(final String[] o) {

				final Tree mainMenu = getMainMenu();
				final TreeItem cubeMenu = mainMenu.addItem(ConstantFactory.getInstance().availablecubes());

				for (final String element2 : o) {
					setupMainMenuOption(cubeMenu, new OlapPanel(element2), Pat.IMAGES.cube());
				}

			}
		});

	}
}