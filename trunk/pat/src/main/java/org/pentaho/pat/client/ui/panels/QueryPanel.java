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

import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Alignment;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.QueryListener;
import org.pentaho.pat.client.ui.widgets.DataWidget;
import org.pentaho.pat.client.ui.widgets.OlapTableDev;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.Matrix;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;


/**
 * Creates the main dataview for Olap tables and charts.
 * 
 * @author tom(at)wamonline.org.uk
 */
public class QueryPanel extends DataWidget implements QueryListener {

	/** The olap table. */
	//private transient OlapTable olapTable = new OlapTable(MessageFactory.getInstance());
	private transient OlapTableDev olapTable = new OlapTableDev();
	/** Panel Name. */
	private transient String name;

	private transient String query;

	private transient String cube;
	
    private final LayoutPanel layoutPanel = new ScrollLayoutPanel(new BoxLayout());
    
    private QueryModelSelectionPanel qmSelectionPanel = new QueryModelSelectionPanel(); 

	
	/**
	 *TODO JAVADOC
	 *
	 */
	public QueryPanel() {
		super();
		init();
	}

	/**
	 * Constructor.
	 * 
	 * @param name the name
	 */
	public QueryPanel(final String name) {
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
		GlobalConnectionFactory.getQueryInstance().addQueryListener(QueryPanel.this);

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

		((BoxLayout)layoutPanel.getLayout()).setAlignment(Alignment.CENTER);
			    
				layoutPanel.add(qmSelectionPanel, new BoxLayoutData(0.30,1));
				layoutPanel.add(olapTable,new BoxLayoutData(FillStyle.BOTH));
				

		basePanel.add(layoutPanel);
	

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
		qmSelectionPanel.setQueryId(query);
	}
	
	public OlapTableDev getTable(){
		return olapTable;
	}

	public void onQueryChange(Widget sender) {
		// TODO Auto-generated method stub
		
	}

	public void onQueryExecuted(String queryId, Matrix olapData) {
		if (this.query != null && queryId == this.query) {
			// TODO why is this called twice? why two instances of the same object?
			olapTable.setData(olapData);
		}
	}
}
