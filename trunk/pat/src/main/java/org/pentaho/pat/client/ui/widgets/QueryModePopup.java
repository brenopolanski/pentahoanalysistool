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

import org.pentaho.pat.client.util.factory.ConstantFactory;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
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

		/** The selection mode. */
		private transient int queryMode = -1;

		/**
		 * The Constructor.
		 *
		 * @param selectionMode the selection mode
		 */
		public QueryModeCommand(final int queryMode) {
			this.queryMode = queryMode;
			if ( queryMode == QUERY_MODEL) {
				
			}
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
			
			QueryModePopup.this.hide();
		}
	}

	/**
	 * Gets the source.
	 *
	 * @return the source
	 */
	public static Widget getSource() {
		return source;
	}

	/**
	 * Sets the source.
	 *
	 * @param source2 the new source
	 */
	public static void setSource(final Widget source2) {
		source = source2;
	}

	/**
	 * Show the context menu.
	 *
	 * @param event the event
	 * @param selectedItem the selected item
	 */
	public final void showContextMenu(final Event event, final TreeItem selectedItem) {
		init();
		setSource(selectedItem.getWidget());

	}
}