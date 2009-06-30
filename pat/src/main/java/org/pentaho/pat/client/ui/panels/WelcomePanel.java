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

package org.pentaho.pat.client.ui.panels;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.WidgetWrapper;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Alignment;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.util.ButtonHelper;
import org.gwt.mosaic.ui.client.util.ButtonHelper.ButtonLabelType;
import org.pentaho.pat.client.Application;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.ui.ConnectionWindow;
import org.pentaho.pat.client.ui.widgets.DataWidget;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Basic Welcome Panel that currently opens the PAT Homepage.
 *
 * @author tom(at)wamonline.org.uk
 */
public class WelcomePanel extends DataWidget  implements ConnectionListener {

	/** Name. */
	private transient String name;

	/** WindowPanel for WIKI */
	private WindowPanel sized;
	
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
	 *TODO JAVADOC
	 *
	 */
	public WelcomePanel() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.ui.widgets.DataWidget#getDescription()
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
	 * @see org.pentaho.pat.client.ui.widgets.DataWidget#getName()
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
	 * @see org.pentaho.pat.client.ui.widgets.DataWidget#onInitialize()
	 */
	/**
	 * Initialization routine.
	 * @return layoutPanel;
	 */
	@Override
	public final Widget onInitialize() {
		
	    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL, Alignment.CENTER));
	    
	    final String pageTitle = "<h1>" + ConstantFactory.getInstance().mainTitle() + "</h1>";
	    final LayoutPanel buttonBar = new LayoutPanel(new BoxLayout());
	    buttonBar.setWidgetSpacing(20);
	    conButton = new ToolButton(ButtonHelper.createButtonLabel(Pat.IMAGES.database(),
	    		ConstantFactory.getInstance().connect(),
	        ButtonLabelType.TEXT_ON_BOTTOM),new ClickHandler() {
				public void onClick(ClickEvent arg0) {
					if (!WelcomePanel.connectionEstablished) {
						if (Application.getConnectionWindow() == null) {
							Application.setConnectionWindow(new ConnectionWindow());
						}
						Application.getConnectionWindow().emptyForms();
						Application.getConnectionWindow().showModal(true);
					} else {
						ServiceFactory.getSessionInstance().disconnect(Pat.getSessionID(), new AsyncCallback<Object>() {
							public void onFailure(final Throwable arg0) {
								MessageBox.error(ConstantFactory.getInstance().error(), arg0.getLocalizedMessage());
							}

							public void onSuccess(final Object o) {
								setConnectionEstablished(false);
								GlobalConnectionFactory.getInstance().getConnectionListeners().fireConnectionBroken(WelcomePanel.this);
							}
						});
					}
				}   	
	        });
	    ToolButton patwikiBtn = new ToolButton(ButtonHelper.createButtonLabel(
	        Pat.IMAGES.help_index(), ConstantFactory.getInstance().wiki(),
	        ButtonLabelType.TEXT_ON_BOTTOM),new ClickHandler() {
				public void onClick(ClickEvent arg0) {
					//createSizedWindowPanel();
					//sized.showModal();
					Window.open("http://code.google.com/p/pentahoanalysistool/wiki/StartPage?tm=6", "_blank", "");
				}
	    });

	    buttonBar.add(conButton);
	    buttonBar.add(patwikiBtn);

	    layoutPanel.add(new WidgetWrapper(new HTML(pageTitle)), new BoxLayoutData(FillStyle.BOTH));
	    layoutPanel.add(buttonBar, new BoxLayoutData(FillStyle.VERTICAL));
	    
	    return layoutPanel;
	}

	@SuppressWarnings("unused")
	private void createSizedWindowPanel() {
	    sized = new WindowPanel("Sized");
	    sized.setAnimationEnabled(true);
	    sized.setSize("812px", "484px");
	    final Frame frame = new Frame("http://code.google.com/p/pentahoanalysistool/wiki/StartPage?tm=6");
	    DOM.setStyleAttribute(frame.getElement(), "border", "none");
	    sized.setWidget(frame);

	    final ImageButton refreshBtn = new ImageButton(Caption.IMAGES.toolRefresh());
	    refreshBtn.addClickHandler(new ClickHandler() {
	      public void onClick(ClickEvent event) {
	        frame.setUrl(frame.getUrl());
	      }
	    });
	    sized.getHeader().add(refreshBtn, CaptionRegion.RIGHT);

	    sized.addWindowClosingHandler(new Window.ClosingHandler() {
	     public void onWindowClosing(ClosingEvent arg0) {	
	    }
	    });
	  }

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionBroken(com.google.gwt.user.client.ui.Widget)
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
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionMade(com.google.gwt.user.client.ui.Widget)
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