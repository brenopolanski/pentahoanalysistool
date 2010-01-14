/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.ui.widgets;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.IQueryListener;
import org.pentaho.pat.client.util.TableUtil;
import org.pentaho.pat.client.util.dnd.FlexTableRowDragController;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.IAxis;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class MeasureGrid extends FocusPanel implements IQueryListener{
    
    private FlexTableRowDragController dragController;
    
    private DimensionFlexTable grid;
    
    private Boolean empty = true;
    
    private IAxis currentAxis;

    private String query;
    
    public MeasureGrid(String query){
        super();
        grid = new DimensionFlexTable();
        final CaptionPanel panel = new CaptionPanel("Measures");
        DOM.setStyleAttribute(this.getElement(), "background", "yellow");
        panel.add(grid);
        this.add(panel);
        this.query = query;
    }

    public void addRow(MeasureLabel ml, int row){
        if(empty && grid.getRowCount()>0){
        TableUtil.removeSpacer(grid);
        }
        grid.setWidget(row, 0, ml);
    }
    
    public void addRow(MeasureLabel ml){
        if(empty && grid.getRowCount()>0){
            TableUtil.removeSpacer(grid);
            }
        grid.setWidget(grid.getRowCount(), 0, ml);
    }
    public DimensionFlexTable getRows(){
        return grid;
    }
    
    public void removeRow(int row){
        if (!currentAxis.equals(IAxis.UNUSED) && grid.getRowCount()>1){
        grid.removeRow(row);
        }
        else{
            MeasureGrid.this.removeFromParent();
        }
    }

    /**
     *TODO JAVADOC
     * @return the dragController
     */
    public FlexTableRowDragController getDragController() {
        return dragController;
    }

    /**
     *
     *TODO JAVADOC
     * @param dragController the dragController to set
     */
    public void setDragController(FlexTableRowDragController dragController) {
        this.dragController = dragController;
    }
    
    public void makeDraggable(){
        dragController.makeDraggable(MeasureGrid.this);
    }
    
    public void makeNotDraggable(){
        dragController.makeNotDraggable(MeasureGrid.this);
    }

    /**
     *TODO JAVADOC
     * @return the empty
     */
    public Boolean getEmpty() {
        return empty;
    }

    /**
     *
     *TODO JAVADOC
     * @param empty the empty to set
     */
    public void setEmpty(Boolean empty) {
        this.empty = empty;
    }

    /**
     *
     *TODO JAVADOC
     * @param currentAxis the currentAxis to set
     */
    public void setCurrentAxis(IAxis currentAxis) {
        this.currentAxis = currentAxis;
    }

    /**
     *TODO JAVADOC
     * @return the currentAxis
     */
    public IAxis getCurrentAxis() {
        return currentAxis;
    }
 

    /* (non-Javadoc)
     * @see org.pentaho.pat.client.listeners.IQueryListener#onQueryChange(com.google.gwt.user.client.ui.Widget, int, org.pentaho.pat.rpc.dto.IAxis, org.pentaho.pat.rpc.dto.IAxis)
     */
    public void onQueryChange(Widget sender, int sourceRow, IAxis sourceAxis, IAxis targetAxis) {
        if (isAttached() && isVisible() && Pat.getCurrQuery().equals(query) && currentAxis == targetAxis) {
            int rowcount = 0;
            for (int i = 0; i<grid.getRowCount(); i++){
                if (grid.getWidget(i, 0) != null)
                    rowcount++;
            }
            
            if (rowcount==0){
                this.removeFromParent();
            }
                 
        }
        
    }

    @Override
    public void onLoad(){
    //    GlobalConnectionFactory.getQueryInstance().addQueryListener(MeasureGrid.this);
    }
    /* (non-Javadoc)
     * @see org.pentaho.pat.client.listeners.IQueryListener#onQueryExecuted(java.lang.String, org.pentaho.pat.rpc.dto.CellDataSet)
     */
    public void onQueryExecuted(String queryId, CellDataSet matrix) {
        // TODO Auto-generated method stub
        
    }

    /**
     *TODO JAVADOC
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    /**
     *
     *TODO JAVADOC
     * @param query the query to set
     */
    public void setQuery(String query) {
        this.query = query;
    }

    public List getMeasureLabels(){
        List measureLabels = new ArrayList();
        for(int i=0; i<grid.getRowCount(); i++){
            if(grid.getWidget(i, 0) instanceof MeasureLabel){
                measureLabels.add(grid.getWidget(i, 0));
            }
        }
        return measureLabels;
    }
}
