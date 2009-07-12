/*
 * Copyright (C) 2009 Paul Stoellberger
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA *
 *
 * @created Jul 11, 2009
 * @author Paul Stoellberger
 */

package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.panels.MainMenu;
import org.pentaho.pat.client.ui.panels.MainTabPanel;
import org.pentaho.pat.client.ui.panels.QueryPanel;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class SelectionModePopup.
 */
public class QueryModePopup extends PopupPanel {
	

	/** The Constant QUERY_MODEL. */
	public static final int QUERY_MODEL = 0;

	/** The Constant MDX. */
	public static final int MDX = 1;

	/** The Constant CLEAR. */
	public static final int CANCEL = -1;

	/** The source. */
	private static Widget source;

	/** The source. */
	private static TreeItem item;

	/** The menu bar. */
	private transient MenuBar menuBar;
	
	/**
	 * Instantiates a new query mode popup.
	 */
	public QueryModePopup() {
		super(false, true);
		init();
	}
	
	/**
	 * Initialization of the QueryMode Popup
	 */
	protected final void init() {
		menuBar = new MenuBar(true);
		menuBar.setAutoOpen(true);
		menuBar.addItem(new MenuItem("Query Wizard", new QueryModeCommand(QUERY_MODEL)));
		menuBar.addItem(new MenuItem("MDX Query", new QueryModeCommand(MDX)));
		menuBar.addItem(new MenuItem(ConstantFactory.getInstance().clearSelections(), new QueryModeCancelCommand()));

		this.setWidget(menuBar);
	}
	
	public class QueryModeCancelCommand implements Command {

	    	/**
	    	 * Code to execute on click.
	    	 */
		public final void execute() {
			QueryModePopup.this.hide();
		}
	}

	
	public class QueryModeCommand implements Command {

		/** The query mode. */
		private transient int queryMode = -1;

		/**
		 * The Constructor.
		 *
		 * @param queryMode the selection mode
		 */
		public QueryModeCommand(final int queryMode) {
			this.queryMode = queryMode;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see com.google.gwt.user.client.Command#execute()
		 */
		/**
		 * The Command executed on click.
		 */
		public final void execute() {

				final Integer qmode = this.queryMode;
				final DataWidget widget = (DataWidget)source;
				if (!item.getText().equals(ConstantFactory.getInstance().availableCubes())) {
					((QueryPanel) widget).setCube(item.getText().trim());
					ServiceFactory.getSessionInstance().setCurrentCube(Pat.getSessionID(), item.getText().trim(), new AsyncCallback<String[]>() {

						public void onFailure(final Throwable arg0) {
							MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedDimensionList(
									arg0.getLocalizedMessage()));
						}

						public void onSuccess(final String[] arg0) {

							ServiceFactory.getQueryInstance().createNewQuery(Pat.getSessionID(), new AsyncCallback<String>() {

								public void onFailure(final Throwable arg0) {
									MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedQueryCreate(
											arg0.getLocalizedMessage()));
								}

								public void onSuccess(final String arg0) {
									((QueryPanel) widget).setQuery(arg0);
									ServiceFactory.getQueryInstance().setCurrentQuery(Pat.getSessionID(), arg0, new AsyncCallback<Object>() {

										public void onFailure(final Throwable arg0) {

											MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().noQuerySet(
													arg0.getLocalizedMessage()));
										}

										public void onSuccess(final Object arg0) {
											if (qmode != null) {
											
											if (qmode.equals(QUERY_MODEL)) {
												MainMenu.getDimensionPanel().createDimensionList();
												MainMenu.getDimensionPanel().layout();
												MainMenu.showNamedMenu(MainMenu.MenuItem.Dimensions);
												MainMenu.getStackPanel().layout();
												((QueryPanel) widget).setSelectedQueryMode(QueryPanel.QueryMode.QUERY_MODEL);
											}
											if (qmode.equals(MDX)) {
												((QueryPanel) widget).setSelectedQueryMode(QueryPanel.QueryMode.MDX);	
											}
											
											}
											MainTabPanel.displayContentWidget(widget);
											
											
										}
									});
								}
							});
						}

					});
				}
				QueryModePopup.this.hide();
			}
		
	}
	
	/**
	 * Gets the source.
	 *
	 * @return the source
	 */
	public Widget getSource() {
		return source;
	}

	/**
	 * Sets the source.
	 *
	 * @param source2 the new source
	 */
	public void setSource(final Widget source2) {
		source = source2;
	}

	public static void setItem(TreeItem item) {
		QueryModePopup.item = item;
	}

	/**
	 * Show the context menu.
	 *
	 * @param event the event
	 * @param selectedItem the selected item
	 */
	public final void showContextMenu(final Event event, final TreeItem selectedItem, final DataWidget sourceWidget) {
		setSource(sourceWidget);
		setItem(selectedItem);
		if (DOM.eventGetType(event) == Event.ONCONTEXTMENU) {
			init();
		}
		if (DOM.eventGetType(event) == Event.ONCLICK) {
			new QueryModeCommand(QUERY_MODEL).execute();
		}


	}
}