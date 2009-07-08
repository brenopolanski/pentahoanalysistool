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

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.StackLayoutPanel;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.pentaho.pat.client.Application;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.listeners.QueryListener;
import org.pentaho.pat.client.ui.widgets.DataWidget;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.rpc.dto.OlapData;

import com.google.gwt.user.client.ui.Widget;

/**
 * The Main Menu that contains the cube list and dimension list.
 * 
 * @author tom(at)wamonline.org.uk
 */
public class MainMenu extends LayoutComposite implements ConnectionListener, QueryListener {

	private final static StackLayoutPanel stackPanel = new StackLayoutPanel();
	
	private final CubeMenu cubeMenu = new CubeMenu();
	
	/** The dimension panel, a scroll panel containing a dimension drop widget. */
	private static final transient DimensionPanel dimensionPanel = new DimensionPanel();

	/** The connections panel */
	private final transient ConnectionManagerPanel connectionsPanel = new ConnectionManagerPanel();

	private boolean initialized = false;

	/**
	 * Set the content to the {@link DataWidget}.
	 * 
	 * @param content
	 *            the {@link DataWidget} to display
	 */
	static Integer counter = 0;

	
	// TODO add welcome panel and other widgets shouldnt be happening here i think
	protected static DataWidget copyMatrix(final DataWidget source, DataWidget destination) {

		if (source != null) {

			if (source instanceof WelcomePanel) {
				destination = new WelcomePanel();
				final String name = source.getName();
				((WelcomePanel) destination).setName(name);
			}

			else if (source instanceof QueryPanel) {

				destination = new QueryPanel();
				((QueryPanel) destination).setName(((QueryPanel) source).getName());
				((QueryPanel) destination).setCube(((QueryPanel) source).getCube());
				((QueryPanel) destination).setQuery(((QueryPanel) source).getQuery());
			}
		}
		return destination;
	}

	public static void displayContentWidget(final DataWidget content) {
		if (content != null) {
			if (!content.isInitialized()) {
				content.initialize();
			}
			DataWidget contentdupe = null;
			contentdupe = copyMatrix(content, contentdupe);
			counter++;
			Application.getMainTabPanel().addContent(contentdupe, content.getName());
		}
	}
	/**
	 * Constructor.
	 */
	public MainMenu() {
		super();

		if (initialized) {
			return;
		}
		initialized = true;



		final LayoutPanel baseLayoutPanel = getLayoutPanel();
		baseLayoutPanel.add(stackPanel);
		GlobalConnectionFactory.getInstance().addConnectionListener(MainMenu.this);
		GlobalConnectionFactory.getQueryInstance().addQueryListener(MainMenu.this);
		
		stackPanel.add(connectionsPanel, ConstantFactory.getInstance().connections());
		stackPanel.add(cubeMenu, ConstantFactory.getInstance().cubes());
		stackPanel.add(dimensionPanel, ConstantFactory.getInstance().dimensions());
		stackPanel.showStack(0);

		
		displayContentWidget(new WelcomePanel(ConstantFactory.getInstance().welcome()));
	}
	/**
	 * Create the main menu.
	 */
	
	

	/**
	 * Checks if is initialized.
	 *
	 * @return true, if is initialized
	 */
	public final boolean isInitialized() {
		return initialized;
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
		stackPanel.showStack(0);
		stackPanel.layout();
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
	public void onConnectionMade(Widget sender) {
		// TODO Auto-generated method stub
		
	}

	public void onQueryChange(final Widget sender) {
		dimensionPanel.createDimensionList();
	}
	
	public void onQueryExecuted(String queryId, OlapData olapData) {
		// TODO Auto-generated method stub
		
	}

	
	public void showMenu(final int number){
		stackPanel.showStack(number);
	}

	public static StackLayoutPanel getStackPanel() {
		return stackPanel;
	}

	public static DimensionPanel getDimensionPanel() {
		return dimensionPanel;
	}
	
}