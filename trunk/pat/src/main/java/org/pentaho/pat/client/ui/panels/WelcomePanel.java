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

import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.ui.widgets.DataWidget;

import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;

/**
 * Basic Welcome Panel that currently opens the PAT Homepage.
 *
 * @author tom(at)wamonline.org.uk
 */
public class WelcomePanel extends DataWidget {

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
	}

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
		// Not Permanent, but better than a big white space.
		 final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(
			        Orientation.VERTICAL));
			    layoutPanel.setPadding(0);
			    layoutPanel.setWidgetSpacing(20);
		layoutPanel.add(new Frame("http://code.google.com/p/pentahoanalysistool/wiki/StartPage?tm=6"), new BoxLayoutData(FillStyle.BOTH)); //$NON-NLS-1$
		return layoutPanel;
	}

} 