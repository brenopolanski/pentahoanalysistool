/*
 * Copyright (C) 2009 Paul Stoellberger
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA *
 *
 * @created Apr 23, 2009 
 * @author Paul Stoellberger
 */
package org.pentaho.pat.client.ui.windows;

import org.gwt.mosaic.ui.client.TabLayoutPanel;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.ui.panels.ConnectMondrianPanel;
import org.pentaho.pat.client.ui.panels.ConnectXmlaPanel;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class ConnectionWindow.
 * 
 * @author pstoellberger
 */
public class ConnectionWindow extends WindowPanel implements ConnectionListener {

	/** The Window Title. */
	private static final String TITLE = ConstantFactory.getInstance().registerNewConnection();

	/** Mondrian Panel. */
	private transient final ConnectMondrianPanel connectMondrian;

	/** Xmla Panel. */
	private transient final ConnectXmlaPanel connectXmla;

	/** Connection Established indicator. */
	private boolean connectionEstablished = false;


	/** A Tab Layout Panel. */
	private transient final TabLayoutPanel tabPanel = new TabLayoutPanel();

	/**
	 * Connection Window Constructor.
	 */
	public ConnectionWindow() {
		super(TITLE);
		
		connectMondrian = new ConnectMondrianPanel();
		connectXmla = new ConnectXmlaPanel();
		tabPanel.setPadding(5);
		tabPanel.add(connectMondrian, ConstantFactory.getInstance().mondrian());
		tabPanel.add(connectXmla, ConstantFactory.getInstance().xmla());
		GlobalConnectionFactory.getInstance().addConnectionListener(ConnectionWindow.this);
		this.setWidget(tabPanel);
	}

	public static void display() {
		 final ConnectionWindow connectionWindow = new ConnectionWindow();
		 int preferredWidth = Window.getClientWidth();
		 preferredWidth = Math.max(preferredWidth / 3, 256);

		 connectionWindow.setWidth(preferredWidth + "px"); //$NON-NLS-1$
		 
		 if (connectionWindow.getOffsetWidth() < preferredWidth) {
		      connectionWindow.setWidth(preferredWidth + "px"); //$NON-NLS-1$
		    }
		 connectionWindow.showModal();

	}

	/**
	 * Check connection.
	 * 
	 * @return true, if checks if is connection established
	 */
	public boolean isConnectionEstablished() {
		return connectionEstablished;
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionBroken(com.google.gwt.user.client.ui.Widget)
	 */
	/**
	 * Fire when database connection is broken.
	 * @param sender the sender
	 */
	public void onConnectionBroken(final Widget sender) {
		setConnectionEstablished(false);
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionMade(com.google.gwt.user.client.ui.Widget)
	 */
	/**
	 * Fire when database connection is made.
	 * @param sender the sender
	 */
	public void onConnectionMade(final Widget sender) {
		setConnectionEstablished(true);
		if(this.isVisible()) {
			if (ConnectionWindow.this.isAttached())
				ConnectionWindow.this.hide();
		}
	}

	
	/* (non-Javadoc)
	 * @see org.gwt.mosaic.ui.client.WindowPanel#onLoad()
	 */
	/**
	 * Fire on window load.
	 */
	@Override
	protected void onLoad() {
		tabPanel.selectTab(0);
		super.onLoad();
	}

	/**
	 * Set the connectionEstablished status.
	 * 
	 * @param connectionEstablished the connection established
	 */
	public void setConnectionEstablished(final boolean connectionEstablished) {
		this.connectionEstablished = connectionEstablished;
	}

}
