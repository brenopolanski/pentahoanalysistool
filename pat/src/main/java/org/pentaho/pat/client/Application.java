package org.pentaho.pat.client;


import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.StackLayoutPanel;
import org.gwt.mosaic.ui.client.Viewport;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.FillLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.ui.panels.DimensionPanel;
import org.pentaho.pat.client.ui.panels.ToolBarPanel;
import org.pentaho.pat.client.ui.widgets.DataWidget;
import org.pentaho.pat.client.ui.widgets.OlapPanel;
import org.pentaho.pat.client.ui.widgets.WelcomePanel;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.beans.Axis;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeImages;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

/**
 * <p>
 * The Application wrapper that includes a menu bar, title and content 
 * </p>
 * <h3>CSS Style Rules</h3> <ul class="css"> <li>.Application { Applied to the
 * entire Application }</li> <li>.Application-top { The top portion of the
 * Application }</li> <li>.Application-title { The title widget }</li> <li>
 * .Application-links { The main external links }</li> <li>.Application-options
 * { The options widget }</li> <li>.Application-menu { The main menu }</li> <li>
 * .Application-content-wrapper { The element around the content }</li> </ul>
 * 
 * @author tom(at)wamonline.org.uk
 */

public class Application extends Viewport implements ConnectionListener{
	/**
	 * Images used in the {@link Application}.
	 */
	public interface ApplicationImages extends TreeImages {
		/**
		 * An image indicating a leaf.
		 * 
		 * @return a prototype of this image
		 */
		@Resource("noimage.png")
		AbstractImagePrototype treeLeaf();
	}

	/**
	 * A listener to handle events from the Application.
	 */
	public interface ApplicationListener {
		/**
		 * Fired when a menu item is selected.
		 * 
		 * @param item
		 *            the item that was selected
		 */
		void onMenuItemSelected(com.google.gwt.user.client.ui.TreeItem item);
	}

	/**
	 * The base style name.
	 */
	public static final String DEFAULT_STYLE_NAME = "Application";

	/**
	 * The wrapper around the content.
	 */
	private LayoutPanel contentWrapper;

	/**
	 * The panel that holds the main links.
	 */
	private HorizontalPanel linksPanel;

	/**
	 * The {@link ApplicationListener}.
	 */
	private ApplicationListener listener = null;

	/**
	 * The main menu.
	 */
	private Tree mainMenu;

	/**
	 * The panel that contains the title widget and links.
	 */
	private FlexTable topPanel;

	/**
	 * Create StackPanel
	 */
	private StackLayoutPanel stackPanel;
	
	/*
	 * The tool bar
	 */
	private ToolBarPanel toolBarPanel;
	
	private static LayoutPanel layoutPanel;
	
	private static LayoutPanel bottomPanel;
	
	private DimensionPanel dimensionPanel;
	/**
	 * Constructor.
	 */
	public Application() {
		super();
		// Setup the main layout widget
		layoutPanel = getWidget();
		layoutPanel.setLayout(new BoxLayout(Orientation.VERTICAL));

		// Setup the top panel with the title and links
		createTopPanel();
		layoutPanel.add(topPanel, new BoxLayoutData(FillStyle.HORIZONTAL));

		
		
		
		bottomPanel = new LayoutPanel(new BorderLayout());
		layoutPanel.add(bottomPanel, new BoxLayoutData(FillStyle.BOTH));

		// Add the main menu
		createMainMenu();

		toolBarPanel.addConnectionListener(Application.this);
		final CaptionLayoutPanel westPanel = new CaptionLayoutPanel();
		stackPanel = new StackLayoutPanel();
		westPanel.add(stackPanel);
		stackPanel.add(new ScrollPanel(mainMenu), ConstantFactory.getInstance()
				.cubes());

		dimensionPanel = new DimensionPanel();
		stackPanel.add(dimensionPanel, ConstantFactory.getInstance().dimensions());
		stackPanel.showStack(0);
		//toolBarPanel.addConnectionListener(dimensionPanel);
		
		// westPanel.getHeader().add(Showcase.IMAGES.showcaseDemos().createImage());
		final ImageButton collapseBtn = new ImageButton(Caption.IMAGES
				.toolCollapseLeft());
		westPanel.getHeader().add(collapseBtn, CaptionRegion.RIGHT);

		collapseBtn.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				bottomPanel.setCollapsed(westPanel, !layoutPanel
						.isCollapsed(westPanel));
				bottomPanel.layout();
			}
		});
		bottomPanel.setCollapsed(westPanel, true);

		// bottomPanel.addCollapsedListener(cubeExplorerPanel, this);

		bottomPanel.add(westPanel, new BorderLayoutData(Region.WEST, 200, 10,
				250));
		// bottomPanel.setCollapsed(westPanel, true);

		// Add the content wrapper
		contentWrapper = new LayoutPanel(new FillLayout());
		contentWrapper.addStyleName(DEFAULT_STYLE_NAME + "-content-wrapper");
		bottomPanel.add(contentWrapper);
		setupMainMenu();
		setContent(null);
	}

	/**
	 * Add a link to the top of the page.
	 * 
	 * @param link
	 *            the widget to add to the mainLinks
	 */
	public void addLink(Widget link) {
		if (linksPanel.getWidgetCount() > 0) {
			linksPanel.add(new HTML("&nbsp;|&nbsp;"));
		}
		linksPanel.add(link);
	}

	public static LayoutPanel getPanel(){
		return bottomPanel;
	}
	/**
	 * Create the main menu.
	 */
	private void createMainMenu() {
		// Setup the main menu
		ApplicationImages treeImages = GWT.create(ApplicationImages.class);
		mainMenu = new Tree(treeImages);
		mainMenu.setAnimationEnabled(true);
		mainMenu.addStyleName(DEFAULT_STYLE_NAME + "-menu");
		mainMenu.addTreeListener(new TreeListener() {
			public void onTreeItemSelected(TreeItem item) {
				if (listener != null) {
					if (item.getParentItem().getText().equals(ConstantFactory.getInstance().available_cubes())){
						
						
					ServiceFactory.getSessionInstance().setCurrentCube(Pat.getSessionID(), item.getText().trim(), new AsyncCallback<String[]>() {

						public void onFailure(Throwable arg0) {
							MessageBox.error(ConstantFactory.getInstance().error(), ConstantFactory.getInstance().failedDimensionList());
						}

						public void onSuccess(String[] arg0) {
							 ServiceFactory.getSessionInstance().createNewQuery(Pat.getSessionID(), new AsyncCallback<String>(){

									public void onFailure(Throwable arg0) {
										// TODO Auto-generated method stub
										MessageBox.error("Query", "Failed");
									}
					
									public void onSuccess(String arg0) {
										
										ServiceFactory.getSessionInstance().setCurrentQuery(Pat.getSessionID(), arg0, new AsyncCallback() {

											public void onFailure(Throwable arg0) {
												// TODO Auto-generated method stub
												
											}

											public void onSuccess(Object arg0) {
												// TODO Auto-generated method stub
												dimensionPanel.createDimensionList();
												
												stackPanel.showStack(1);
												stackPanel.layout(true);
											}
											
										});
																				
									
									}
									
								});
							
						}
					});
					
					
					

					
					
					listener.onMenuItemSelected(item);
					contentWrapper.layout(true);
					
					
					}
				}
			}

			public void onTreeItemStateChanged(TreeItem item) {
			}
		});
	}

	/**
	 * Create the panel at the top of the page that contains the title and
	 * links.
	 */
	private void createTopPanel() {
		boolean isRTL = LocaleInfo.getCurrentLocale().isRTL();
		topPanel = new FlexTable();
		topPanel.setCellPadding(0);
		topPanel.setCellSpacing(0);
		topPanel.setStyleName(DEFAULT_STYLE_NAME + "-top");
		FlexCellFormatter formatter = topPanel.getFlexCellFormatter();

		//Setup the toolbar
		toolBarPanel = new ToolBarPanel();
		
		topPanel.setWidget(0, 0, toolBarPanel);
		formatter.setStyleName(0, 0, DEFAULT_STYLE_NAME + "-menu");

		formatter.setColSpan(0, 0, 2);

		// Setup the title cell
		setTitleWidget(null);
		formatter.setStyleName(1, 0, DEFAULT_STYLE_NAME + "-title");

		// Setup the options cell
		setOptionsWidget(null);
		formatter.setStyleName(1, 1, DEFAULT_STYLE_NAME + "-options");
		if (isRTL) {
			formatter.setHorizontalAlignment(1, 1,
					HasHorizontalAlignment.ALIGN_LEFT);
		} else {
			formatter.setHorizontalAlignment(1, 1,
					HasHorizontalAlignment.ALIGN_RIGHT);
		}

		// Align the content to the top
		topPanel.getRowFormatter().setVerticalAlign(0,
				HasVerticalAlignment.ALIGN_TOP);
		topPanel.getRowFormatter().setVerticalAlign(1,
				HasVerticalAlignment.ALIGN_TOP);
	}

	/**
	 * Generates a cube list for the Cube Menu 
	 */
	private void setupCubeMenu(){	
		ServiceFactory.getDiscoveryInstance().getCubes(Pat.getSessionID(), new AsyncCallback<String[]>() {
			public void onSuccess(String[] o) {
				
				Tree mainMenu = getMainMenu();
				TreeItem cubeMenu = mainMenu.addItem(ConstantFactory.getInstance().available_cubes());
				
				
				for (int i=0; i<o.length; i++){
					setupMainMenuOption(cubeMenu, new OlapPanel(o[i]), Pat.IMAGES.cube());
				}
				
			}
			public void onFailure(Throwable arg0) {			  
			       MessageBox.error(ConstantFactory.getInstance().error(), ConstantFactory.getInstance().failedCubeList());
			}	
		});
		
		
	}

	/**
	 * Setup all of the options in the main menu.
	 */
	private void setupMainMenu() {
		Tree mainMenu = getMainMenu();

		TreeItem homeMenu = mainMenu.addItem(ConstantFactory.getInstance().home());
		setupMainMenuOption(homeMenu, new WelcomePanel(ConstantFactory.getInstance().welcome()), Pat.IMAGES.cube());
		
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
	private static void setupMainMenuOption(TreeItem parent,
			DataWidget content, AbstractImagePrototype image) {
		// Create the TreeItem
		TreeItem option = parent.addItem(image.getHTML() + " "
				+ content.getName());
		
		// Map the item to its history token and content widget
		Pat.itemWidgets.put(option, content);
		Pat.itemTokens.put(Pat.getContentWidgetToken(content), option);
	}

	/**
	 * @return the {@link Widget} in the content area
	 */
	public Widget getContent() {
		return contentWrapper.getWidget(0);
	}

	/**
	 * @return the main menu.
	 */
	public Tree getMainMenu() {
		return mainMenu;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mosaic.ui.client.layout.HasLayoutManager#getPreferredSize()
	 */
	public int[] getPreferredSize() {
		return getWidget().getPreferredSize();
	}

	/**
	 * @return the {@link Widget} used as the title
	 */
	public Widget getTitleWidget() {
		return topPanel.getWidget(0, 0);
	}

	@Override
	protected LayoutPanel getWidget() {
		return super.getWidget();
	}

	/**
	 * Set the {@link Widget} to display in the content area.
	 * 
	 * @param content
	 *            the content widget
	 */
	public void setContent(Widget content) {
		contentWrapper.clear();
		if (content != null) {
			contentWrapper.add(content);
		}
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
	 * Set the {@link Widget} to use as options, which appear to the right of
	 * the title bar.
	 * 
	 * @param options
	 *            the options widget
	 */
	public void setOptionsWidget(Widget options) {
		topPanel.setWidget(1, 1, options);
	}

	/**
	 * Set the {@link Widget} to use as the title bar.
	 * 
	 * @param title
	 *            the title widget
	 */
	public void setTitleWidget(Widget title) {
		topPanel.setWidget(1, 0, title);
	}

	public void onConnectionBroken(Widget sender) {
		// TODO Auto-generated method stub
		
	}

	public void onConnectionMade(Widget sender) {
		setupCubeMenu();
	}

}