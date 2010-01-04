/*
 * Copyright (C) 2009 Tom Barber
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *
 */
package org.pentaho.pat.client.ui.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.util.WidgetHelper;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.IQueryListener;
import org.pentaho.pat.client.util.dnd.FlexTableRowDragController;
import org.pentaho.pat.client.util.dnd.FlexTableRowDropController;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.IAxis;
import org.pentaho.pat.rpc.dto.StringTree;
import org.pentaho.pat.rpc.dto.IAxis.Standard;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 *TODO JAVADOC
 * 
 * @created Aug 8, 2009
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public class DimensionDropWidget extends LayoutComposite implements IQueryListener {

    private final Standard dimAxis;

    private final LayoutPanel baseLayoutPanel;

    private DimensionFlexTable dimensionTable;

    private CaptionLayoutPanel captLayoutPanel;

    private Boolean horizontal = false;

    private FlexTableRowDropController fTblRowDropCont;

    private final String query;

    private final Label spacerLabel = new Label(""); //$NON-NLS-1$

    private final FlexTableRowDragController tblRowDragCont;

    private Boolean measures = false;

    private final static String TABLE_DROP_ROWENDCELL = "dropRowEndCell";

    private final static String TABLE_CSS_SPACER = "spacer-label"; //$NON-NLS-1$


    private final static String TABLE_DRAG_CELL = "dragDimensionCell"; //$NON-NLS-1$

    private final static String TABLE_DROP_ENDCELL = "dropEndCell"; //$NON-NLS-1$

    private boolean empty = true;
    
    /**
     *TODO JAVADOC
     * 
     * @param tblRowDragCont
     * 
     * @param unused
     * @param string
     * 
     */
    public DimensionDropWidget(final String labelText, final Standard targetAxis,
            final FlexTableRowDragController tblRowDragCont) {
        super();
        this.tblRowDragCont = tblRowDragCont;
        this.dimAxis = targetAxis;
        query = Pat.getCurrQuery();
        baseLayoutPanel = getLayoutPanel();
        init(labelText, dimAxis);
        baseLayoutPanel.add(captLayoutPanel);
        GlobalConnectionFactory.getQueryInstance().addQueryListener(DimensionDropWidget.this);

    }
    
    public DimensionDropWidget(final String labelText, final Standard targetAxis,
            final FlexTableRowDragController tblRowDragCont, Boolean measures) {
        super();
        this.tblRowDragCont = tblRowDragCont;
        this.dimAxis = targetAxis;
        query = Pat.getCurrQuery();
        baseLayoutPanel = getLayoutPanel();
        init(labelText, dimAxis);
        baseLayoutPanel.add(captLayoutPanel);
        GlobalConnectionFactory.getQueryInstance().addQueryListener(DimensionDropWidget.this);
        this.setMeasures(measures);
    }

    public DimensionDropWidget(final String labelText, final Standard targetAxis, final Boolean orientation,
            final FlexTableRowDragController tblRowDragCont) {
        super();
        horizontal = orientation;
        this.dimAxis = targetAxis;
        query = Pat.getCurrQuery();
        baseLayoutPanel = getLayoutPanel();
        init(labelText, dimAxis);
        baseLayoutPanel.add(captLayoutPanel);
        GlobalConnectionFactory.getQueryInstance().addQueryListener(DimensionDropWidget.this);

        this.tblRowDragCont = tblRowDragCont;
    }

  

    /**
     *TODO JAVADOC
     * 
     * @return the horizontal
     */
    public Boolean getHorizontal() {
        return horizontal;
    }

    /**
     * Initialization.
     * 
     * @param labelText
     *            the label text
     * @param targetAxis
     *            the target axis
     */
    public final void init(final String labelText, final IAxis targetAxis) {

        captLayoutPanel = new CaptionLayoutPanel(labelText);
        captLayoutPanel.setLayout(new BoxLayout());
        dimensionTable = new DimensionFlexTable(horizontal, dimAxis);
       // dimensionTable.addStyleName("FlexTable"); //$NON-NLS-1$
        
        captLayoutPanel.add(dimensionTable, new BoxLayoutData(FillStyle.BOTH, true));

        dimensionTable.clear();
        populateDimensionTable(dimAxis);

    }

    @Override
    public void onLoad() {
        fTblRowDropCont = new FlexTableRowDropController(dimensionTable, dimAxis);
        DimensionDropWidget.this.tblRowDragCont.registerDropController(fTblRowDropCont);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.listeners.QueryListener#onQueryChange(com.google.gwt.user.client.ui.Widget)
     */
    public void onQueryChange(final Widget sender, int sourceRow, final IAxis sourceAxis, final IAxis targetAxis) {
        // TODO Auto-generated method stub
        if (isAttached() && isVisible() && Pat.getCurrQuery().equals(query) && dimAxis == targetAxis) {
            flexTableAddRecord(sender, targetAxis);
            
        } else if(isAttached() && isVisible() && Pat.getCurrQuery().equals(query) && dimAxis == sourceAxis){
            flexTableRemoveRecord(sourceRow);
        }
    }

    private void flexTableRemoveRecord(int sourceRow) {
	dimensionTable.removeRow(sourceRow);
	
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.listeners.QueryListener#onQueryExecuted(java.lang.String,
     * org.pentaho.pat.rpc.dto.CellDataSet)
     */
    public void onQueryExecuted(final String queryId, final CellDataSet matrix) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUnload() {
        tblRowDragCont.unregisterDropController(fTblRowDropCont);
    }

    private void populateDimensionTable(final IAxis targetAxis) {

        ServiceFactory.getDiscoveryInstance().getDimensions(Pat.getSessionID(), Pat.getCurrQuery(), targetAxis,
                new AsyncCallback<String[]>() {

                    

		    public void onFailure(final Throwable arg0) {
                        MessageBox.error(ConstantFactory.getInstance().error(), ConstantFactory.getInstance()
                                .dimensionFetchFail());
                    }

                    public void onSuccess(final String[] arg0) {
                	dimensionTable.clear();
                        //TODO GWT2.0 REmove All Rows
                        int count = dimensionTable.getRowCount();
                        while (count > 0) {
                           dimensionTable.removeRow(0);
                           count--;
                        } 

                        if(arg0.length==0){
                            dimensionTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
                            dimensionTable.setWidget(0, 0, spacerLabel);
                            dimensionTable.getCellFormatter().addStyleName(0, 0, TABLE_CSS_SPACER);
                            }
                       
                        
                        
                        
                        ServiceFactory.getDiscoveryInstance().getMembers(Pat.getSessionID(), Pat.getCurrQuery(), 
                    		"Measures", new AsyncCallback<StringTree>(){

				    public void onFailure(Throwable arg0) {
					// TODO Auto-generated method stub
					
				    }

				    public void onSuccess(StringTree measuresTree) {
					if(!measures){
					    int index = Arrays.binarySearch(arg0, "Measures");
					    
					    List<String> dimensionList = Arrays.asList(arg0);
					    ArrayList<String> anotherList = new ArrayList<String>(dimensionList);
					    
					    if(index>-1){
					    anotherList.remove(index);
						int recordInsert = index;
					    for(int i=0; i< measuresTree.getChildren().size();i++){
						String measureName = measuresTree.getChildren().get(i).getValue();
						anotherList.add(recordInsert, measureName);
						recordInsert++;
					    }
					    }
					    
					    
					    
					for (int row = 0; row < anotherList.size(); row++) {
					    MeasureLabel handle;
					    if(row >= index && row <=index+measuresTree.getChildren().size()-1){
						handle = new MeasureLabel(anotherList.get(row), MeasureLabel.labelType.MEASURE);
					    }
					    else{
						handle = new MeasureLabel(anotherList.get(row), MeasureLabel.labelType.DIMENSION);
					    }
					    empty = false;
        	                            	tblRowDragCont.makeDraggable(handle);
        	                            dimensionTable.setWidget(row, 0, handle);
        	                        dimensionTable.getCellFormatter().addStyleName(row, 0 ,"FlexTable-Cell");
	                                dimensionTable.getCellFormatter().setStylePrimaryName(row, 0, TABLE_DRAG_CELL);
	                                dimensionTable.getCellFormatter().setVerticalAlignment(row, 0,
	                                        HasVerticalAlignment.ALIGN_TOP);
	                                if (row == anotherList.size() - 1 /*&& anotherList.size() - 1 > 0*/) {
	                                    dimensionTable.getCellFormatter().addStyleName(row, 0, TABLE_DROP_ROWENDCELL);
	                                }
					}
					refreshTable();
					}
					
				    }
                        });
                        
                       
                        refreshTable();     
                    }
                });
    }

    private void refreshTable() {

        WidgetHelper.revalidate(dimensionTable);

    }

   

    public void setMeasures(Boolean measures) {
	this.measures = measures;
    }

    public Boolean getMeasures() {
	return measures;
    }

    public void flexTableAddRecord(Widget sender, IAxis axis){
	MeasureLabel newLabel = new MeasureLabel(((MeasureLabel) sender).getText(), ((MeasureLabel) sender).getType());
	tblRowDragCont.makeDraggable(newLabel);
	if(empty){
	    dimensionTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
	    dimensionTable.setWidget(0, 0 , newLabel);
	    empty = false;
	}else{
	 dimensionTable.getCellFormatter().setVerticalAlignment(dimensionTable.getRowCount(), 0, HasVerticalAlignment.ALIGN_TOP);
	 dimensionTable.setWidget(dimensionTable.getRowCount(), 0, newLabel);
	}
	WidgetHelper.revalidate(dimensionTable);
    }
    

}