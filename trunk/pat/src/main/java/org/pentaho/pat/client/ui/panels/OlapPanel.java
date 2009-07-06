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

import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.StackLayoutPanel;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Alignment;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.widgets.DataWidget;
import org.pentaho.pat.client.ui.widgets.DimensionDropWidget;
import org.pentaho.pat.client.ui.widgets.OlapTable;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.Axis;
import org.pentaho.pat.rpc.dto.OlapData;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;


/**
 * Creates the main dataview for Olap tables and charts.
 * 
 * @author tom(at)wamonline.org.uk
 */
public class OlapPanel extends DataWidget {

	/** The olap table. */
	private transient OlapTable olapTable;

	/** Panel Name. */
	private transient String name;

	private transient String query;

	private transient String cube;
	
	private Button executeButton;
	
	final LayoutPanel layoutPanel = new ScrollLayoutPanel(new BoxLayout());
	
	DimensionDropWidget rowDimDrop = new DimensionDropWidget(ConstantFactory.getInstance().rows(), Axis.ROWS);

	DimensionDropWidget colDimDrop = new DimensionDropWidget(ConstantFactory.getInstance().columns(), Axis.COLUMNS);
	
	DimensionDropWidget filterDimDrop = new DimensionDropWidget(ConstantFactory.getInstance().filter(), Axis.FILTER);
	/**
	 *TODO JAVADOC
	 *
	 */
	public OlapPanel() {
		super();
		init();
	}

	/**
	 * Constructor.
	 * 
	 * @param name the name
	 */
	public OlapPanel(final String name) {
		super();
		this.name = name;
		init();
	}

	@Override
	protected void onUnload() {
		ServiceFactory.getQueryInstance().deleteQuery(Pat.getSessionID(),this.query, new AsyncCallback<Object>() {
			public void onSuccess(Object arg0) {
			}
			public void onFailure(Throwable arg0) {
				// TODO add failure routine
			}
		});
		super.onUnload();
		
	}

	/**
	 * Create the Tab Bar Text.
	 * 
	 * @param image the image
	 * @param text the text
	 * 
	 * @return the string
	 */
	private String createTabBarCaption(final AbstractImagePrototype image, final String text) {
		final StringBuffer sb = new StringBuffer();
		sb.append("<table cellspacing='0px' cellpadding='0px' border='0px'><thead><tr>"); //$NON-NLS-1$
		sb.append("<td valign='middle'>"); //$NON-NLS-1$
		sb.append(image.getHTML());
		sb.append("</td><td valign='middle' style='white-space: nowrap;'>&nbsp;"); //$NON-NLS-1$
		sb.append(text);
		sb.append("</td></tr></thead></table>"); //$NON-NLS-1$
		return sb.toString();
	}

	/**
	 * Do execute query model.
	 */
	public void doExecuteQueryModel() {
		ServiceFactory.getQueryInstance().executeQuery(Pat.getSessionID(), new AsyncCallback<OlapData>() {

			public void onFailure(final Throwable caught) {
				Window.alert(MessageFactory.getInstance().noServerData(caught.toString()));
			}

			public void onSuccess(final OlapData result1) {
				olapTable.setData(result1);
				//doCreateChart();
			}

		});

	}

	public String getCube(){
		return cube;
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.ui.widgets.DataWidget#getDescription()
	 */
	/**
	 * Get the Panel Description
	 * @return null
	 */
	@Override
	public String getDescription() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.ui.widgets.DataWidget#getName()
	 */
	/**
	 * Get the Panel Name
	 * @return name
	 */
	@Override
	public String getName() {
		return name;
	}

	public String getQuery(){
		return query;
	}

	/**
	 * Initialization routine.
	 */
	private void init() {
		olapTable = new OlapTable(MessageFactory.getInstance());

	}
	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.ui.widgets.DataWidget#onInitialize()
	 */
	/**
	 * Run when constructed.
	 */
	@Override
	public Widget onInitialize() {
		final LayoutPanel basePanel = new LayoutPanel(new BorderLayout());

		
		final StackLayoutPanel stackPanel = new StackLayoutPanel();


		executeButton = new Button(ConstantFactory.getInstance().executeQuery());
		executeButton.addClickHandler(new ClickHandler() {
			public void onClick(final ClickEvent event) {
				doExecuteQueryModel();
			
			}

		});
			  
			    ((BoxLayout)layoutPanel.getLayout()).setAlignment(Alignment.CENTER);
			    
			    layoutPanel.add(executeButton, new BoxLayoutData(-1.0, 0.75));	    
			    		    
			    final LayoutPanel dropLayoutPanel = new ScrollLayoutPanel();
			    ((BoxLayout)layoutPanel.getLayout()).setAlignment(Alignment.CENTER);
			    
				dropLayoutPanel.add(rowDimDrop, new BoxLayoutData(1.0, 0.33));
				dropLayoutPanel.add(colDimDrop, new BoxLayoutData(1.0, 0.33));
				dropLayoutPanel.add(filterDimDrop, new BoxLayoutData(1.0, 0.33));
				
				layoutPanel.add(dropLayoutPanel, new BoxLayoutData(0.20,1));
				layoutPanel.add(olapTable,new BoxLayoutData(FillStyle.BOTH));
				
		stackPanel.add(layoutPanel, createTabBarCaption(Pat.IMAGES.cube(), ConstantFactory.getInstance().data() + " (" + getName() + ")"), //$NON-NLS-1$ //$NON-NLS-2$
				true);



		    
		final LayoutPanel panel2 = new LayoutPanel();
		final HTML sourceWidget = new HTML("GIVE  ME A FUNCTION"); //$NON-NLS-1$
		sourceWidget.setStyleName(DEFAULT_STYLE_NAME + "-source"); //$NON-NLS-1$
		panel2.add(sourceWidget);
		stackPanel.add(panel2, createTabBarCaption(Pat.IMAGES.chart(), ConstantFactory.getInstance().chart() + " (" + getName() + ")"), true); //$NON-NLS-1$ //$NON-NLS-2$

		stackPanel.showStack(0);

		basePanel.add(stackPanel);
	

		return basePanel;
	}


	public void setCube(final String cube){
		this.cube = cube;
	}

	/**
	 *TODO JAVADOC
	 *
	 * @param string
	 */
	public void setName(final String string) {
		this.name = string;
	}

	/**
	 *TODO JAVADOC
	 *
	 * @param arg0
	 */
	public void setQuery(final String query) {
		this.query = query;
	}
	
	public OlapTable getTable(){
		return olapTable;
	}
}
