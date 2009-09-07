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
package org.pentaho.pat.client.deprecated.ui.panels;

import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Alignment;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.deprecated.listeners.QueryListener;
import org.pentaho.pat.client.deprecated.ui.panels.MainMenu.MenuItem;
import org.pentaho.pat.client.deprecated.ui.widgets.DataWidget;
import org.pentaho.pat.client.deprecated.ui.widgets.OlapTable;
import org.pentaho.pat.client.deprecated.util.factory.ConstantFactory;
import org.pentaho.pat.client.deprecated.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.deprecated.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;


/**
 * Creates the main dataview for Olap tables and charts.
 * 
 * @author tom(at)wamonline.org.uk
 */
public class QueryPanel extends DataWidget implements QueryListener {

	/** The olap table. */
	private OlapTable olapTable = new OlapTable();
	/** Panel Name. */
	private transient String name;

	private transient String query;

	private transient String cube;
	
	public enum QueryMode {
		MDX, QUERY_MODEL;
	}

	private static QueryMode selectedQueryMode;
	
    private final LayoutPanel layoutPanel = new ScrollLayoutPanel(new BoxLayout());
    
    private QueryModelSelectionPanel qmSelectionPanel = new QueryModelSelectionPanel(); 

    private static WindowPanel wp = null;
	
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
	 * @see org.pentaho.pat.client.deprecated.ui.widgets.DataWidget#getDescription()
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
	 * @see org.pentaho.pat.client.deprecated.ui.widgets.DataWidget#getName()
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
	 * @see org.pentaho.pat.client.deprecated.ui.widgets.DataWidget#onInitialize()
	 */
	/**
	 * Run when constructed.
	 */
	@Override
	protected void onLoad() {
		super.onLoad();
		if (Pat.getApplicationState().getMode().isAllowQmQuery() && selectedQueryMode == QueryMode.QUERY_MODEL) {
//			Application.getMenuPanel();
			MainMenu.showNamedMenu(MenuItem.Dimensions);
		}
		if (Pat.getApplicationState().getMode().isAllowMdxQuery() && selectedQueryMode == QueryMode.MDX) {
//			Application.getMenuPanel();
			MainMenu.showNamedMenu(MenuItem.Cubes);
		}
	}
	@Override
	public Widget onInitialize() {
		final LayoutPanel basePanel = new LayoutPanel(new BorderLayout());

		((BoxLayout)layoutPanel.getLayout()).setAlignment(Alignment.CENTER);
			    if (Pat.getApplicationState().getMode().isAllowQmQuery() && selectedQueryMode == QueryMode.QUERY_MODEL) {
			    	layoutPanel.add(qmSelectionPanel, new BoxLayoutData(0.30,1));
			    }
			    if (Pat.getApplicationState().getMode().isAllowMdxQuery() && selectedQueryMode == QueryMode.MDX) {
			    	// TODO this is just for demonstration at the moment.
			    	final String fQuery = this.query;
			    	ToolButton editMDX = new ToolButton("MDX Query Editor"); //$NON-NLS-1$
			    	editMDX.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent arg0) {
							if (wp == null) {
								wp = CreateMdxWindowPanel(fQuery);
							}
							wp.center();
							
						}
					});
			    	layoutPanel.add(editMDX);
			    	
			    }
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
	
	public OlapTable getTable(){
		return olapTable;
	}

	public void setSelectedQueryMode(QueryMode selectedQueryMode) {
		QueryPanel.selectedQueryMode = selectedQueryMode;
	}

	public QueryMode getSelectedQueryMode() {
		return selectedQueryMode;
	}

	public void onQueryChange(Widget sender) {
		// TODO Auto-generated method stub
		
	}

	public void onQueryExecuted(String queryId, CellDataSet olapData) {
		if (this.query != null && queryId == this.query && this.isAttached()) {
			// TODO why is this called twice? why two instances of the same object?
			olapTable.setData(olapData);
		}
	}
	
	private WindowPanel CreateMdxWindowPanel(final String fQuery) {
		final WindowPanel wp = new WindowPanel(ConstantFactory.getInstance().editMdx());
		LayoutPanel wpLayoutPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
		wpLayoutPanel.setSize("300px", "200px"); //$NON-NLS-1$ //$NON-NLS-2$
		final TextArea mdxArea = new TextArea();
		
		mdxArea.setText("select NON EMPTY { } ON COLUMNS," +
							" NON EMPTY { } ON ROWS " +
							" from [] ");

		wpLayoutPanel.add(mdxArea, new BoxLayoutData(1,0.9));
		ToolButton closeBtn = new ToolButton(ConstantFactory.getInstance().executeMdx());
		closeBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
//				ServiceFactory.getQueryInstance().executeMdxQuery(Pat.getSessionID(), mdxArea.getText(), new AsyncCallback<CellDataSet>() {
//
//					public void onFailure(Throwable arg0) {
//						MessageBox.error(ConstantFactory.getInstance().error(), arg0.getLocalizedMessage());
//					}
//
//					public void onSuccess(CellDataSet matrix) {
//						GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryExecuted(QueryPanel.this, fQuery, matrix);
//						
//					}
//					
//				});
//						
				wp.hide();
				
			}
		});
		wpLayoutPanel.add(closeBtn);
		wpLayoutPanel.layout();
		wp.add(wpLayoutPanel);
		wp.layout();
		wp.pack();
		wp.setSize("400px", "320px"); //$NON-NLS-1$ //$NON-NLS-2$
		return wp;
	}

    /* (non-Javadoc)
     * @see org.pentaho.pat.client.deprecated.listeners.QueryListener#onMemberMove(com.google.gwt.user.client.ui.Widget)
     */
    public void onMemberMove(Widget sender) {
        // TODO Auto-generated method stub
        
    }
}
