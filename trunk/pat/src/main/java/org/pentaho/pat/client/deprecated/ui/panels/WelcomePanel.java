/*
 * Copyright (C) 2009 Paul Stoellberger
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA *
 *
 * @created Jun 26, 2009 
 * @author Paul Stoellberger
 */

package org.pentaho.pat.client.deprecated.ui.panels;

import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.WidgetWrapper;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Alignment;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.util.ButtonHelper;
import org.gwt.mosaic.ui.client.util.ButtonHelper.ButtonLabelType;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.deprecated.demo.DemoPanel;
import org.pentaho.pat.client.deprecated.listeners.ConnectionListener;
import org.pentaho.pat.client.deprecated.ui.widgets.DataWidget;
import org.pentaho.pat.client.deprecated.ui.windows.ConnectionWindow;
import org.pentaho.pat.client.deprecated.util.factory.ConstantFactory;
import org.pentaho.pat.client.deprecated.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.deprecated.util.factory.ServiceFactory;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Basic Welcome Panel that currently opens the PAT Homepage.
 *
 * @author tom(at)wamonline.org.uk
 */
public class WelcomePanel extends DataWidget  implements ConnectionListener {

	/** Name. */
	private transient String name;

	/**
	 * Constructor pass panel Name.
	 *
	 * @param name the name
	 */
	public WelcomePanel(final String name) {
		super();
		this.name = name;
		WelcomePanel.connectionEstablished = false;
		GlobalConnectionFactory.getInstance().addConnectionListener(WelcomePanel.this);
	}
	
	private static ToolButton conButton;

	/** The Connection Dialog. */
	//private ConnectionWindow connectWindow;
	
	/** Connection Established. */
	private static boolean connectionEstablished;
	
	/**
	 * The PAT Welcome Panel. Currently allows connection editing and
	 * opening of the PAT Wiki.
	 *
	 */
	public WelcomePanel() {

	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.deprecated.ui.widgets.DataWidget#getDescription()
	 */
	/**
	 * Get the panel description.
	 * @return null
	 */
	@Override
	public final String getDescription() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.deprecated.ui.widgets.DataWidget#getName()
	 */
	/**
	 * get the Panel Name.
	 * @return name;
	 */
	@Override
	public final String getName() {
		return name;
	}

	public void setName(String name){
		this.name = name;	
	}
	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.deprecated.ui.widgets.DataWidget#onInitialize()
	 */
	/**
	 * Initialization routine.
	 * @return layoutPanel;
	 */
	@Override
	public final Widget onInitialize() {
		
	    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL, Alignment.CENTER));
	    
	    final String pageTitle = "<h1>" + ConstantFactory.getInstance().mainTitle() + "</h1>"; //$NON-NLS-1$ //$NON-NLS-2$
	    final LayoutPanel buttonBar = new LayoutPanel(new BoxLayout());
	    buttonBar.setWidgetSpacing(20);
	    conButton = new ToolButton(ButtonHelper.createButtonLabel(Pat.IMAGES.database(),
	    		ConstantFactory.getInstance().connect(),
	        ButtonLabelType.TEXT_ON_BOTTOM),new ClickHandler() {
				public void onClick(ClickEvent arg0) {
					if (!WelcomePanel.connectionEstablished) {
						ConnectionWindow.display();
					} else {
//						ServiceFactory.getSessionInstance().disconnect(Pat.getSessionID(), new AsyncCallback<Object>() {
//							public void onFailure(final Throwable arg0) {
//								MessageBox.error(ConstantFactory.getInstance().error(), arg0.getLocalizedMessage());
//							}
//
//							public void onSuccess(final Object o) {
//								setConnectionEstablished(false);
//								GlobalConnectionFactory.getInstance().getConnectionListeners().fireConnectionBroken(WelcomePanel.this);
//							}
//						});
					}
				}   	
	        });
	    ToolButton patwikiBtn = new ToolButton(ButtonHelper.createButtonLabel(
	        Pat.IMAGES.help_index(), ConstantFactory.getInstance().wiki(),
	        ButtonLabelType.TEXT_ON_BOTTOM),new ClickHandler() {
				public void onClick(ClickEvent arg0) {
					Window.open("http://code.google.com/p/pentahoanalysistool/wiki/StartPage?tm=6", "_blank", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
	    });

	    if (Pat.getApplicationState().getMode().isManageConnections()) {
	    	buttonBar.add(conButton);
	    }
	    buttonBar.add(patwikiBtn);

	    layoutPanel.add(new WidgetWrapper(new HTML(pageTitle)), new BoxLayoutData(FillStyle.BOTH));
	    layoutPanel.add(buttonBar);
	    layoutPanel.add(new DemoPanel());
	    
	    return layoutPanel;
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.deprecated.listeners.ConnectionListener#onConnectionBroken(com.google.gwt.user.client.ui.Widget)
	 */
	/**
	 * Fires when the database connection is broken.
	 * @param sender the sender
	 */
	public void onConnectionBroken(final Widget sender) {
		setConnectionEstablished(false);
		// Alter menu
		conButton.setHTML(ButtonHelper.createButtonLabel(Pat.IMAGES.database(), ConstantFactory.getInstance().connect(),ButtonLabelType.TEXT_ON_BOTTOM));
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.deprecated.listeners.ConnectionListener#onConnectionMade(com.google.gwt.user.client.ui.Widget)
	 */
	/**
	 * Fires when a database connection is established.
	 * @param sender the sender
	 */
	public void onConnectionMade(final Widget sender) {
		setConnectionEstablished(true);
		conButton.setHTML(ButtonHelper.createButtonLabel(Pat.IMAGES.database(), ConstantFactory.getInstance().disconnect(),ButtonLabelType.TEXT_ON_BOTTOM));

	}

	/**
	 * Sets the connection status
	 * 
	 * @param connectionEstablished the connection established
	 */
	private final void setConnectionEstablished(final boolean connectionEstablished) {
		WelcomePanel.connectionEstablished = connectionEstablished;
	}
} 