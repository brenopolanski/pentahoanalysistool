package org.pentaho.pat.client.ui.panels;

import java.util.HashMap;
import java.util.Map;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.pentaho.pat.client.Application;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.Application.ApplicationImages;
import org.pentaho.pat.client.Application.ApplicationListener;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.ui.panels.MainMenu.MenuItem;
import org.pentaho.pat.client.ui.popups.QueryModePopup;
import org.pentaho.pat.client.ui.widgets.DataWidget;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

public class CubeMenu extends LayoutComposite implements ConnectionListener {
	/** A mapping of history tokens to their associated menu items. */
	public static final Map<String, TreeItem> ITEMTOKENS = new HashMap<String, TreeItem>();

	/** A mapping of menu items to the widget display when the item is selected. */
	public static final Map<TreeItem, DataWidget> ITEMWIDGETS = new HashMap<TreeItem, DataWidget>();

	/** The main menu. */
	private Tree cubeTree;

	/** The {@link ApplicationListener}. */
	private transient ApplicationListener listener = null;

	public CubeMenu() {
		super();
		this.sinkEvents(Event.BUTTON_LEFT | Event.BUTTON_RIGHT | Event.ONCONTEXTMENU);
		GlobalConnectionFactory.getInstance().addConnectionListener(CubeMenu.this);
		final LayoutPanel baseLayoutPanel = getLayoutPanel();
		
		
		final ApplicationImages treeImages = GWT.create(ApplicationImages.class);
		cubeTree = new Tree(treeImages);
		cubeTree.setAnimationEnabled(true);
		cubeTree.addStyleName(Pat.DEF_STYLE_NAME + "-menu"); //$NON-NLS-1$
		cubeTree.setSize("100%", "100%"); //$NON-NLS-1$ //$NON-NLS-2$

		baseLayoutPanel.add(cubeTree);

		this.listener = new ApplicationListener() {
			public void onMenuItemSelected(final TreeItem item) {
				ITEMWIDGETS.get(item);
			}
		};

	}
	
	@Override
	protected void onUnload(){
		
	}
	
	@Override
	protected void onLoad(){
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
	private static void setupMainMenuOption(final TreeItem parent, final DataWidget content, final AbstractImagePrototype image) {
		// Create the TreeItem
		final TreeItem option = parent.addItem(image.getHTML() + " " //$NON-NLS-1$
				+ content.getName());

		// Map the item to its history token and content widget
		ITEMWIDGETS.put(option, content);
		ITEMTOKENS.put(getContentWidgetToken(content), option);
	}

	/**
	 * Get the token for a given content widget.
	 * 
	 * @param content
	 *            the content
	 * 
	 * @return the content widget token.
	 */
	public static String getContentWidgetToken(final DataWidget content) {
		String className = content.getClass().getName();
		className = className.substring(className.lastIndexOf('.') + 1);
		return className;
	}

	/**
	 * Generates a cube list for the Cube Menu.
	 */
	private final void setupCubeMenu() {
		// TODO remove when showcase not needed
		cubeTree.addItem("connection 1"); //$NON-NLS-1$
		ServiceFactory.getDiscoveryInstance().getCubes(Pat.getSessionID(), new AsyncCallback<String[]>() {
			public void onFailure(final Throwable arg0) {
				MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedCubeList(arg0.getLocalizedMessage()));
			}

			public void onSuccess(final String[] o) {
				// TODO implement new treeitem correctly
				final TreeItem cubesList = cubeTree.addItem("connection 2"); //$NON-NLS-1$

				for (final String element2 : o) {
					setupMainMenuOption(cubesList, new QueryPanel(element2), Pat.IMAGES.cube());
				}
				cubesList.setState(true);

			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pentaho.pat.client.listeners.ConnectionListener#onConnectionBroken
	 * (com.google.gwt.user.client.ui.Widget)
	 */
	/**
	 * Fires when a db connection is broken.
	 * 
	 * @param sender
	 *            the sender.
	 */
	public void onConnectionBroken(final Widget sender) {
		MainMenu.showNamedMenu(MenuItem.Connections);
		MainMenu.getStackPanel().layout();
		cubeTree.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pentaho.pat.client.listeners.ConnectionListener#onConnectionMade(
	 * com.google.gwt.user.client.ui.Widget)
	 */
	/**
	 * Fires when db connection is made.
	 * 
	 * @param sender
	 *            the sender.
	 */
	public void onConnectionMade(final Widget sender) {
		setupCubeMenu();
	}
	
	/**
	 * Fires on browser clicks.
	 * @param event the event
	 */
	@Override
	public void onBrowserEvent(final Event event) {
		super.onBrowserEvent(event);
		final TreeItem item = cubeTree.getSelectedItem();
		if (listener != null && item != null && !item.getText().equals("connection 1") && !item.getText().equals("connection 2")) { //$NON-NLS-1$ //$NON-NLS-2$
			final DataWidget widget = ITEMWIDGETS.get(item);
			final QueryModePopup qModePopup = new QueryModePopup();
			qModePopup.setPopupPositionAndShow(new PositionCallback() {
				public void setPosition(final int offsetWidth, final int offsetHeight) {
					qModePopup.setPopupPosition(event.getClientX(), event.getClientY());
				}
			});

			qModePopup.showContextMenu(event,item, widget);

		}

		cubeTree.setSelectedItem(item, false);
		cubeTree.ensureSelectedItemVisible();

		// Show the associated ContentWidget

		Application.getMainTabPanel().layout();


	}





}