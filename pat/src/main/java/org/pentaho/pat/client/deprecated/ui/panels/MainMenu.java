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
package org.pentaho.pat.client.deprecated.ui.panels;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.StackLayoutPanel;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.deprecated.listeners.ConnectionListener;
import org.pentaho.pat.client.deprecated.listeners.QueryListener;
import org.pentaho.pat.client.deprecated.util.factory.ConstantFactory;
import org.pentaho.pat.client.deprecated.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.ui.panels.ConnectionManagerPanel;
import org.pentaho.pat.client.ui.panels.CubeMenu;
import org.pentaho.pat.rpc.dto.CellDataSet;

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

	private static int menuCounter = 0;

	public enum MenuItem {
		Connections, Cubes, Dimensions;
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

		if (Pat.getApplicationState().getMode().isShowMenu()) {

			final LayoutPanel baseLayoutPanel = getLayoutPanel();
			baseLayoutPanel.add(stackPanel);
			GlobalConnectionFactory.getInstance().addConnectionListener(MainMenu.this);
			GlobalConnectionFactory.getQueryInstance().addQueryListener(MainMenu.this);

			if (Pat.getApplicationState().getMode().isShowConnections()) {
				addMenuItem(connectionsPanel, ConstantFactory.getInstance().connections());
			}

			if (Pat.getApplicationState().getMode().isShowCubeMenu()) {
				addMenuItem(cubeMenu, ConstantFactory.getInstance().cubes());
			}

			if (Pat.getApplicationState().getMode().isShowDimensionMenu()) {
				addMenuItem(dimensionPanel, ConstantFactory.getInstance().dimensions());
			}

			showNamedMenu(MenuItem.Connections);

		}
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
	 * org.pentaho.pat.client.deprecated.listeners.ConnectionListener#onConnectionBroken
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
	 * org.pentaho.pat.client.deprecated.listeners.ConnectionListener#onConnectionMade(
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

	public void onQueryExecuted(String queryId, CellDataSet olapData) {
		// TODO Auto-generated method stub

	}

	public static StackLayoutPanel getStackPanel() {
		return stackPanel;
	}

	public static DimensionPanel getDimensionPanel() {
		return dimensionPanel;
	}

	public static boolean showNamedMenu(MenuItem menuItem) {
		if (stackPanel != null) {
			for (int i = 0; i < menuCounter; i++) {
				String stackname = null;
				if (menuItem.equals(MenuItem.Connections))
					stackname = ConstantFactory.getInstance().connections();
				if (menuItem.equals(MenuItem.Cubes))
					stackname = ConstantFactory.getInstance().cubes();
				if (menuItem.equals(MenuItem.Dimensions))
					stackname = ConstantFactory.getInstance().dimensions();

				if (stackPanel.getCaption(i).getText().equals(stackname)) {
					stackPanel.showStack(i);
					stackPanel.layout();
					return true;
				}
			}
		}
		stackPanel.layout();
		stackPanel.showStack(0);
		return false;
	}

	public static void addMenuItem(Widget widget, String stackText) {
		stackPanel.add(widget, stackText);
		menuCounter++;
	}
}