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
package org.pentaho.pat.client.ui;

import org.gwt.mosaic.ui.client.TabLayoutPanel;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.listeners.ConnectionListenerCollection;
import org.pentaho.pat.client.ui.widgets.ConnectMondrianPanel;
import org.pentaho.pat.client.ui.widgets.ConnectXmlaPanel;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;

import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class ConnectionWindow.
 * 
 * @author pstoellberger
 */
public class ConnectionWindow extends WindowPanel implements ConnectionListener {

	/** The Window Height. */
	private static final String HEIGHT = "330px"; //$NON-NLS-1$

	/** The Window Width. */
	private static final String WIDTH = "660px"; //$NON-NLS-1$

	/** The Window Title. */
	private static final String TITLE = ConstantFactory.getInstance().registernewconnection();

	/** Mondrian Panel. */
	private transient final ConnectMondrianPanel connectMondrian;

	/** Xmla Panel. */
	private transient final ConnectXmlaPanel connectXmla;

	/** Connection Established indicator. */
	private boolean connectionEstablished = false;

	/** Connection Listeners. */
	private transient ConnectionListenerCollection connectionListeners;

	/** A Tab Layout Panel. */
	private transient final TabLayoutPanel tabPanel = new TabLayoutPanel();

	/**
	 * Connection Window Constructor.
	 */
	public ConnectionWindow() {
		super(TITLE);
		this.setHeight(HEIGHT);
		this.setWidth(WIDTH);
		
		connectMondrian = new ConnectMondrianPanel();
		connectXmla = new ConnectXmlaPanel();
		this.add(onInitialize());
	}

	/**
	 * Empties the form contents.
	 */
	public void emptyForms() {
		connectMondrian.emptyForm();
		connectXmla.emptyForm();
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
		//connectionListeners.fireConnectionBroken(ConnectionWindow.this);
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
		//connectionListeners.fireConnectionMade(ConnectionWindow.this);
		ConnectionWindow.this.hide();
	}

	/**
	 * Initialize the window.
	 * 
	 * @return the layout panel
	 */
	protected LayoutPanel onInitialize() {
		final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));

		tabPanel.setPadding(5);
		tabPanel.add(connectMondrian, ConstantFactory.getInstance().mondrian());
		tabPanel.add(connectXmla, ConstantFactory.getInstance().xmla());
		GlobalConnectionFactory.getInstance().addConnectionListener(ConnectionWindow.this);

		tabPanel.addTabListener(new TabListener() {
			public  boolean onBeforeTabSelected(final SourcesTabEvents sender, final int tabIndex) {
				return true;
			}

			public  void onTabSelected(final SourcesTabEvents sender, final int tabIndex) {
				// FormLayout.getPreferredSize() needs to be improved so that
				// pack()
				// works like expected. But you can try it.
				// pack();
			}
		});
		layoutPanel.add(tabPanel, new BoxLayoutData(FillStyle.BOTH));
		return layoutPanel;
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
