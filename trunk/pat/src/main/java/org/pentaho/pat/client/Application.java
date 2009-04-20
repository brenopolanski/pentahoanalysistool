package org.pentaho.pat.client;

import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.ImageButton;
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
import org.pentaho.pat.client.ui.panels.MainMenu;
import org.pentaho.pat.client.ui.panels.ToolBarPanel;
import org.pentaho.pat.client.util.factory.ServiceFactory;

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.TreeImages;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

/**
 * <p>
 * The Application wrapper that includes a menu bar, title and content.
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
	private static LayoutPanel contentWrapper;

	/**
	 * The base style name.
	 */
	public static final String DEFAULT_STYLE_NAME = "Application"; //$NON-NLS-1$

	/**
	 * The Application Main Panel.
	 */
	private MainMenu mainPanel;

	/**
	 * The panel that contains the title widget and links.
	 */
	private FlexTable topPanel;

	/**
	 * The Application's Toolbar Panel.
	 */
	private ToolBarPanel toolBarPanel;

	/**
	 * Application LayoutPanel.
	 */
	private LayoutPanel layoutPanel;

	/**
	 * The bottom Panel for the Application, contains the main panels.
	 */
	private static LayoutPanel bottomPanel;

	/**
	 * @return the bottom panel
	 */
	public static LayoutPanel getBottomPanel() {
		return bottomPanel;
	}

	/**
	 * @param bottomPanel
	 *            Returns the bottom Panel(dimension and data container)
	 */
	private static void setBottomPanel(final LayoutPanel bottomPanel) {
		Application.bottomPanel = bottomPanel;
	}

	/**
	 * Set the {@link Widget} to display in the content area.
	 *
	 * @param content
	 *            the content widget
	 */
	public static void setContent(final Widget content) {
		Application.contentWrapper.clear();
		if (content != null) {
			Application.contentWrapper.add(content);
		}
	}

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

		setBottomPanel(new LayoutPanel(new BorderLayout()));
		layoutPanel.add(bottomPanel, new BoxLayoutData(FillStyle.BOTH));

		// Add the main menu
		toolBarPanel.addConnectionListener(Application.this);
		final CaptionLayoutPanel westPanel = new CaptionLayoutPanel();

		final ImageButton collapseBtn = new ImageButton(Caption.IMAGES
				.toolCollapseLeft());
		westPanel.getHeader().add(collapseBtn, CaptionRegion.RIGHT);

		collapseBtn.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				bottomPanel.setCollapsed(westPanel, !layoutPanel
						.isCollapsed(westPanel));
				bottomPanel.layout();
			}
		});
		bottomPanel.setCollapsed(westPanel, true);
		bottomPanel.add(westPanel, new BorderLayoutData(Region.WEST, 200, 10,
				250));

		// Add the content wrapper
		setContentWrapper(new LayoutPanel(new FillLayout()));
		contentWrapper.addStyleName(DEFAULT_STYLE_NAME + "-content-wrapper"); //$NON-NLS-1$

		bottomPanel.add(contentWrapper);
		mainPanel = new MainMenu();

		westPanel.add(mainPanel);

		setContent(null);

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
	 * Destroy the cube menu when an onConnectionBroken is called.
	 */
	private void destroyCubeMenu() {
		ServiceFactory.getSessionInstance().getQueries(Pat.getSessionID(),
				new AsyncCallback<String[]>() {

					public void onFailure(final Throwable arg0) {
						// TODO Auto-generated method stub

					}

					public void onSuccess(final String[] arg0) {
						String queryID = ""; //$NON-NLS-1$
						ServiceFactory.getSessionInstance().deleteQuery(
								Pat.getSessionID(), queryID,
								new AsyncCallback() {

									public void onFailure(final Throwable arg0) {
										// TODO Auto-generated method stub

									}

									public void onSuccess(final Object arg0) {
										// TODO Auto-generated method stub

									}

								});
					}

				});
	}

	/**
	 * Returns the Applications Content Wrapper.
	 *
	 * @return the Content Wrapper
	 */
	public static LayoutPanel getContentWrapper() {
		return contentWrapper;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.mosaic.ui.client.layout.HasLayoutManager#getPreferredSize()
	 */
	/**
	 * @return the widgets preferred size
	 */
	public final int[] getPreferredSize() {
		return getWidget().getPreferredSize();
	}

	/**
	 * @return the {@link Widget} used as the title
	 */
	public final Widget getTitleWidget() {
		return topPanel.getWidget(0, 0);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gwt.mosaic.ui.client.Viewport#getWidget()
	 */
	/**
	 * @return the widget.
	 */
	@Override
	protected final LayoutPanel getWidget() {
		return super.getWidget();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.pentaho.pat.client.listeners.ConnectionListener#onConnectionBroken
	 * (com.google.gwt.user.client.ui.Widget)
	 */
	/**
	 * Runs when a database disconnect is called.
	 *
	 * @param sender
	 *            sender widget
	 */
	public final void onConnectionBroken(final Widget sender) {
		destroyCubeMenu();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.pentaho.pat.client.listeners.ConnectionListener#onConnectionMade(
	 * com.google.gwt.user.client.ui.Widget)
	 */
	/**
	 * Runs when a database connection is made.
	 *
	 * @param sender
	 *            sender widget
	 */
	public final void onConnectionMade(final Widget sender) {
		mainPanel.setupCubeMenu();
	}

	/**
	 * Sets the Applications Content Wrapper.
	 *
	 * @param contentWrapper
	 *            The Application Content Wrapper
	 */
	public static void setContentWrapper(final LayoutPanel contentWrapper) {
		Application.contentWrapper = contentWrapper;
	}

	/**
	 * Set the {@link Widget} to use as options, which appear to the right of
	 * the title bar.
	 *
	 * @param options
	 *            the options widget
	 */
	public final void setOptionsWidget(final Widget options) {
		topPanel.setWidget(1, 1, options);
	}

	/**
	 * Set the {@link Widget} to use as the title bar.
	 *
	 * @param title
	 *            the title widget
	 */
	public final void setTitleWidget(final Widget title) {
		topPanel.setWidget(1, 0, title);
	}

}

