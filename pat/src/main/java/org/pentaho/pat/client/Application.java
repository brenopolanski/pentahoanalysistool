package org.pentaho.pat.client;

import java.util.HashMap;
import java.util.Map;

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
import org.pentaho.pat.client.ui.panels.MainMenu;
import org.pentaho.pat.client.ui.panels.OlapPanel;
import org.pentaho.pat.client.ui.panels.ToolBarPanel;
import org.pentaho.pat.client.ui.widgets.DataWidget;
import org.pentaho.pat.client.ui.widgets.WelcomePanel;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;

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

public class Application extends Viewport implements ConnectionListener {
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
	 * The wrapper around the content.
	 */
	public static LayoutPanel contentWrapper;
	
	/**
	 * The base style name.
	 */
	public static final String DEFAULT_STYLE_NAME = "Application"; //$NON-NLS-1$

	/**
	 * The panel that holds the main links.
	 */
	private HorizontalPanel linksPanel;




	/**
	 * The panel that contains the title widget and links.
	 */
	private FlexTable topPanel;


	/*
	 * The tool bar
	 */
	private ToolBarPanel toolBarPanel;

	private static LayoutPanel layoutPanel;

	private static LayoutPanel bottomPanel;

	
	
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
		//createMainMenu();
		
		toolBarPanel.addConnectionListener(Application.this);
		final CaptionLayoutPanel westPanel = new CaptionLayoutPanel();
		
		// toolBarPanel.addConnectionListener(dimensionPanel);

		// westPanel.getHeader().add(Showcase.IMAGES.showcaseDemos().createImage());
		final ImageButton collapseBtn = new ImageButton(Caption.IMAGES.toolCollapseLeft());
		westPanel.getHeader().add(collapseBtn, CaptionRegion.RIGHT);

		collapseBtn.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				bottomPanel.setCollapsed(westPanel, !layoutPanel.isCollapsed(westPanel));
				bottomPanel.layout();
			}
		});
		bottomPanel.setCollapsed(westPanel, true);

		// bottomPanel.addCollapsedListener(cubeExplorerPanel, this);

		bottomPanel.add(westPanel, new BorderLayoutData(Region.WEST, 200, 10, 250));
		// bottomPanel.setCollapsed(westPanel, true);

		// Add the content wrapper
		contentWrapper = new LayoutPanel(new FillLayout());
		contentWrapper.addStyleName(DEFAULT_STYLE_NAME + "-content-wrapper"); //$NON-NLS-1$
		
		bottomPanel.add(contentWrapper);
		MainMenu mainPanel = new MainMenu();
		
		westPanel.add(mainPanel);
		
		setContent(null);
	
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mosaic.ui.client.layout.HasLayoutManager#getPreferredSize()
	 */
	public int[] getPreferredSize() {
		return getWidget().getPreferredSize();
	}
	
	@Override
	protected LayoutPanel getWidget() {
		return super.getWidget();
	}
	/**
	 * Add a link to the top of the page.
	 * 
	 * @param link
	 *            the widget to add to the mainLinks
	 */
	public void addLink(Widget link) {
		if (linksPanel.getWidgetCount() > 0) {
			linksPanel.add(new HTML("&nbsp;|&nbsp;")); //$NON-NLS-1$
		}
		linksPanel.add(link);
	}

	public static LayoutPanel getPanel() {
		return bottomPanel;
	}
	
	/**
	 * Set the {@link Widget} to display in the content area.
	 * 
	 * @param content
	 *            the content widget
	 */
	public static void setContent(Widget content) {
		Application.contentWrapper.clear();
		if (content != null) {
			Application.contentWrapper.add(content);
		}
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
		topPanel.setStyleName(DEFAULT_STYLE_NAME + "-top"); //$NON-NLS-1$
		FlexCellFormatter formatter = topPanel.getFlexCellFormatter();

		// Setup the toolbar
		toolBarPanel = new ToolBarPanel();

		topPanel.setWidget(0, 0, toolBarPanel);
		formatter.setStyleName(0, 0, DEFAULT_STYLE_NAME + "-menu"); //$NON-NLS-1$

		formatter.setColSpan(0, 0, 2);

		// Setup the title cell
		setTitleWidget(null);
		formatter.setStyleName(1, 0, DEFAULT_STYLE_NAME + "-title"); //$NON-NLS-1$

		// Setup the options cell
		setOptionsWidget(null);
		formatter.setStyleName(1, 1, DEFAULT_STYLE_NAME + "-options"); //$NON-NLS-1$
		if (isRTL) {
			formatter.setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT);
		} else {
			formatter.setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		}

		// Align the content to the top
		topPanel.getRowFormatter().setVerticalAlign(0, HasVerticalAlignment.ALIGN_TOP);
		topPanel.getRowFormatter().setVerticalAlign(1, HasVerticalAlignment.ALIGN_TOP);
	}

	private void destroyCubeMenu() {
		ServiceFactory.getSessionInstance().getQueries(Pat.getSessionID(), new AsyncCallback<String[]>() {

			public void onFailure(Throwable arg0) {
				// TODO Auto-generated method stub

			}

			public void onSuccess(String[] arg0) {
				// TODO Auto-generated method stub
				String queryID = ""; //$NON-NLS-1$
				ServiceFactory.getSessionInstance().deleteQuery(Pat.getSessionID(), queryID, new AsyncCallback() {

					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub

					}

					public void onSuccess(Object arg0) {
						// TODO Auto-generated method stub

					}

				});
			}

		});
	}

	
	
	
	

	/**
	 * @return the {@link Widget} used as the title
	 */
	public Widget getTitleWidget() {
		return topPanel.getWidget(0, 0);
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
		destroyCubeMenu();
	}

	public void onConnectionMade(Widget sender) {
	//	setupCubeMenu();
	}

}