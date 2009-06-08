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
package org.pentaho.pat.client.ui.panels;

import java.util.HashMap;
import java.util.Map;

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Application;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.Application.ApplicationImages;
import org.pentaho.pat.client.Application.ApplicationListener;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.listeners.QueryListener;
import org.pentaho.pat.client.ui.widgets.DataWidget;
import org.pentaho.pat.client.ui.widgets.DimensionDropWidget;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Main Menu that contains the cube list and dimension list.
 *
 * @author tom(at)wamonline.org.uk
 */
public class MainMenu extends StackPanel implements ConnectionListener, QueryListener{ // NOPMD by bugg on 21/04/09 05:42

	/**
  * Set the content to the {@link DataWidget}.
  *
  * @param content the {@link DataWidget} to display
  */
	static Integer counter = 0;
	public static void displayContentWidget(final DataWidget content) {
		if (content != null) {
			if (!content.isInitialized()) {
				content.initialize();
			}
			DataWidget contentdupe = null;
			contentdupe = copyMatrix(content, contentdupe, counter);
			counter++;
			Application.addContent(contentdupe, content.getName());
		}
	}

	  protected static DataWidget copyMatrix( DataWidget source, DataWidget destination, Integer counter)
	  {
		
		  if(source != null){
			  
			  if (source instanceof WelcomePanel){
			    destination = new WelcomePanel();
			  String name = source.getName()+Integer.toString(counter);
			  ((WelcomePanel) destination).setName(name);
			  }
			  
			  else if (source instanceof OlapPanel){
			      	
				 destination = new OlapPanel();
				  ((OlapPanel) destination).setName(((OlapPanel) source).getName()+Integer.toString(counter));
				  ((OlapPanel) destination).setCube(((OlapPanel) source).getCube());
				  ((OlapPanel) destination).setQuery(((OlapPanel) source).getQuery());
			  }
		  }
		  return destination;
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
		GlobalConnectionFactory.getInstance().addConnectionListener(MainMenu.this);
		GlobalConnectionFactory.getQueryInstance().addQueryListener(MainMenu.this);
		createMainMenu();
		setupMainMenu();
		mainMenuTree.setSize("100%", "100%"); //$NON-NLS-1$ //$NON-NLS-2$
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
		mainMenuTree.addSelectionHandler(new SelectionHandler<TreeItem>() {
			public void onSelection(SelectionEvent<TreeItem> selectionEvent) {
				TreeItem item = selectionEvent.getSelectedItem();
				if (listener != null) {
					final DataWidget widget = (DataWidget) ITEMWIDGETS.get(item);
					
					if (item.getParentItem().getText().equals(ConstantFactory.getInstance().availablecubes())) {
						((OlapPanel) widget).setCube(item.getText().trim());
						ServiceFactory.getSessionInstance().setCurrentCube(Pat.getSessionID(), item.getText().trim(), new AsyncCallback<String[]>() {

							public void onFailure(final Throwable arg0) {
								MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedDimensionList(arg0.getLocalizedMessage()));
							}

							public void onSuccess(final String[] arg0) {
								
								ServiceFactory.getQueryInstance().createNewQuery(Pat.getSessionID(), new AsyncCallback<String>() {

									public void onFailure(final Throwable arg0) {
										MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedQueryCreate(arg0.getLocalizedMessage()));
									}

									public void onSuccess(final String arg0) {
										((OlapPanel) widget).setQuery(arg0);
										ServiceFactory.getQueryInstance().setCurrentQuery(Pat.getSessionID(), arg0, new AsyncCallback<Object>() {

											public void onFailure(final Throwable arg0) {

												MessageBox.error(ConstantFactory.getInstance().error() , MessageFactory.getInstance().noqueryset(arg0.getLocalizedMessage()));
											}

											public void onSuccess(final Object arg0) {
												dimensionPanel.createDimensionList();
												displayContentWidget(widget);
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
					
					Application.getContentWrapper().layout();

				}
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
	private final void setupCubeMenu() {
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

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionBroken(com.google.gwt.user.client.ui.Widget)
	 */
	/**
	 * Fires when a db connection is broken.
	 * @param sender the sender.
	 */
	public void onConnectionBroken(final Widget sender) {
		this.showStack(0);
		mainMenuTree.clear();
		setupMainMenu();
		final TreeItem firstItem = getMainMenu().getItem(0).getChild(0);
		getMainMenu().setSelectedItem(firstItem, false);
		getMainMenu().ensureSelectedItemVisible();
		displayContentWidget(ITEMWIDGETS.get(firstItem));
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionMade(com.google.gwt.user.client.ui.Widget)
	 */
	/**
	 * Fires when db connection is made.
	 * @param sender the sender.
	 */
	public void onConnectionMade(final Widget sender) {
	setupCubeMenu();	
	}

	public void onQueryChange(Widget sender) {
	    // TODO Auto-generated method stub
	    dimensionPanel.createDimensionList();
	}
}