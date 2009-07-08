/*
 * Copyright (C) 2009 Paul Stoellberger
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA *
 *
 * @created Jul 6, 2009 
 * @author Paul Stoellberger
 */

package org.pentaho.pat.client.ui.panels;

import java.util.Arrays;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.QueryListener;
import org.pentaho.pat.client.ui.widgets.DimensionDropWidget;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.Axis;
import org.pentaho.pat.rpc.dto.Matrix;
import org.pentaho.pat.rpc.dto.OlapData;
import org.pentaho.pat.rpc.dto.celltypes.BaseCell;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

public class QueryModelSelectionPanel extends LayoutComposite implements QueryListener {

	private Button executeButton;
	
	private final LayoutPanel scrollLayoutPanel = new ScrollLayoutPanel(new BoxLayout(Orientation.VERTICAL));
	

	DimensionDropWidget rowDimDrop = new DimensionDropWidget(ConstantFactory.getInstance().rows(), Axis.ROWS);

	DimensionDropWidget colDimDrop = new DimensionDropWidget(ConstantFactory.getInstance().columns(), Axis.COLUMNS);
	
	DimensionDropWidget filterDimDrop = new DimensionDropWidget(ConstantFactory.getInstance().filter(), Axis.FILTER);
	
	private String queryId = null;

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public QueryModelSelectionPanel() {
		super();
		final LayoutPanel baseLayoutPanel= getLayoutPanel();
		GlobalConnectionFactory.getQueryInstance().addQueryListener(QueryModelSelectionPanel.this);
		
		executeButton = new Button(ConstantFactory.getInstance().executeQuery());
		executeButton.addClickHandler(new ClickHandler() {
			public void onClick(final ClickEvent event) {
				doExecuteQueryModel();
			}
		});
		scrollLayoutPanel.add(executeButton);
		scrollLayoutPanel.add(rowDimDrop, new BoxLayoutData(1.0, 0.30));
		scrollLayoutPanel.add(colDimDrop, new BoxLayoutData(1.0, 0.30));
		scrollLayoutPanel.add(filterDimDrop, new BoxLayoutData(1.0, 0.30));
		
	    //((BoxLayout)baseLayoutPanel.getLayout()).setAlignment(Alignment.CENTER);
	    baseLayoutPanel.add(scrollLayoutPanel);
		
		
		

	}
	
	/**
	 * Do execute query model.
	 */
	public void doExecuteQueryModel() {
	    	ServiceFactory.getQueryInstance().executeQuery2(Pat.getSessionID(), new AsyncCallback<Matrix>() {

			public void onFailure(final Throwable caught) {
				Window.alert(MessageFactory.getInstance().noServerData(caught.toString()));
			}

			public void onSuccess(Matrix matrix) {
			    GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryExecuted(QueryModelSelectionPanel.this, queryId, matrix);

			    char[] spaces = new char[1000];
			        Arrays.fill(spaces, ' ');
			        char[] equals = new char[1000];
			        Arrays.fill(equals, '=');
			        char[] dashes = new char[3000];
			        Arrays.fill(dashes, '-');
			    
			    
			    for (int y = 0; y < matrix.getMatrixHeight(); y++) {
		                for (int x = 0; x < matrix.getMatrixWidth(); x++) {
		                    final BaseCell cell = matrix.get(x, y);
		                    if (cell != null) {
		                        if (cell.sameAsPrev) {
		                            System.out.print("  ");
		                        } else {
		                            System.out.print("| ");
		                            if (cell.right) {
		                                
		                                System.out.print(spaces);
		                                System.out.print(cell.formattedValue);
		                                System.out.print(' ');
		                                continue;
		                            }
		                            
		                            System.out.print(cell.formattedValue);
		                                                        
		                        }
		                    } else {
		                        System.out.print("| ");
		                    }
		                    
		                    System.out.print(spaces);
		                }
		                System.out.println('|');
		        }
			}

		});
	}

	public void onQueryChange(Widget sender) {
		// TODO Auto-generated method stub
		
	}

	public void onQueryExecuted(String queryId, Matrix olapData) {
		// TODO Auto-generated method stub
		
	}


	
}
