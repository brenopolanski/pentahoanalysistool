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
import org.gwt.mosaic.ui.client.StackLayoutPanel;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
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
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
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

	/**
	 *TODO JAVADOC
	 *
	 */
	public OlapPanel() {
		super();
		init();
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
		final StringBuffer sb = new StringBuffer(); // NOPMD by bugg on 21/04/09 05:48
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
				Window.alert(MessageFactory.getInstance().noserverdata(caught.toString()));
			}

			public void onSuccess(final OlapData result1) {
			   // olapTable.layout(true);
			    olapTable.setData((OlapData)result1);
				//doCreateChart();
			}

		});

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
		final LayoutPanel layoutPanel = new LayoutPanel(new BorderLayout());

		// MDX(north) panel
		final NorthPanel northPanel = new NorthPanel("MDX Panel"); //$NON-NLS-1$
		final ImageButton collapseBtn1 = new ImageButton(Caption.IMAGES.toolCollapseUp());
		northPanel.getHeader().add(collapseBtn1, CaptionRegion.RIGHT);

		collapseBtn1.addClickHandler(new ClickHandler() {
			public void onClick(final ClickEvent event) {
				layoutPanel.setCollapsed(northPanel, !layoutPanel.isCollapsed(northPanel));
				layoutPanel.layout();
			}
		});

		layoutPanel.add(northPanel, new BorderLayoutData(Region.NORTH, 100, 10, 250));
		layoutPanel.setCollapsed(northPanel, true);

		// Drill(south) panel
		final SouthPanel drillPanel = new SouthPanel("Drill Data"); //$NON-NLS-1$

		final ImageButton collapseBtn2 = new ImageButton(Caption.IMAGES.toolCollapseDown());
		drillPanel.getHeader().add(collapseBtn2, CaptionRegion.RIGHT);

		collapseBtn2.addClickHandler(new ClickHandler() {
			public void onClick(final ClickEvent event) {
				layoutPanel.setCollapsed(drillPanel, !layoutPanel.isCollapsed(drillPanel));
				layoutPanel.layout();
			}
		});

		layoutPanel.add(drillPanel, new BorderLayoutData(Region.SOUTH, 100, 10, 250));
		layoutPanel.setCollapsed(drillPanel, true);

		final StackLayoutPanel stackPanel = new StackLayoutPanel();
		layoutPanel.add(stackPanel);

		// Create the container for the main example
		final ScrollPanel panel1 = new ScrollPanel();
		// panel1.setPadding(0);
		// panel1.setWidgetSpacing(0);
		final Button executeButton = new Button(ConstantFactory.getInstance().executequery());
		executeButton.addClickHandler(new ClickHandler() {
			public void onClick(final ClickEvent event) {
				doExecuteQueryModel();
			}

		});
		final FlexTable grid = new FlexTable();
		grid.setWidget(0, 0, executeButton);
		DimensionDropWidget rowDimDrop = new DimensionDropWidget(ConstantFactory.getInstance().rows(), Axis.ROWS);
		rowDimDrop.setWidth("100%"); //$NON-NLS-1$
		grid.setWidget(1, 0, rowDimDrop);
		
		DimensionDropWidget colDimDrop = new DimensionDropWidget(ConstantFactory.getInstance().columns(), Axis.COLUMNS);
		colDimDrop.setWidth("100%"); //$NON-NLS-1$
		grid.setWidget(2, 0, colDimDrop);
		
		DimensionDropWidget filterDimDrop = new DimensionDropWidget(ConstantFactory.getInstance().filter(), Axis.FILTER);
		filterDimDrop.setWidth("100%"); //$NON-NLS-1$
		grid.setWidget(3, 0, filterDimDrop);
//		olapTable.setWidth("100%");
//		olapTable.setHeight("100%");
		grid.setWidget(0, 1, olapTable);
		grid.getFlexCellFormatter().setRowSpan(0, 1, 4);
		grid.getFlexCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
		panel1.add(grid);
		stackPanel.add(panel1, createTabBarCaption(Pat.IMAGES.cube(), ConstantFactory.getInstance().data() + " (" + getName() + ")"), //$NON-NLS-1$ //$NON-NLS-2$
				true);

		final LayoutPanel panel2 = new LayoutPanel();
		final HTML sourceWidget = new HTML();
		sourceWidget.setStyleName(DEFAULT_STYLE_NAME + "-source"); //$NON-NLS-1$
		panel2.add(sourceWidget);
		stackPanel.add(panel2, createTabBarCaption(Pat.IMAGES.chart(), ConstantFactory.getInstance().chart() + " (" + getName() + ")"), true); //$NON-NLS-1$ //$NON-NLS-2$

		stackPanel.showStack(0);

		return layoutPanel;

	}

	public String getQuery(){
		return query;
	}
	/**
	 *TODO JAVADOC
	 *
	 * @param arg0
	 */
	public void setQuery(String query) {
		// TODO Auto-generated method stub
		this.query = query;
	}


	public void setCube(String cube){
		this.cube = cube;
	}
	
	public String getCube(){
		return cube;
	}

	/**
	 *TODO JAVADOC
	 *
	 * @param string
	 */
	public void setName(String string) {
		this.name = string;
	}
}
