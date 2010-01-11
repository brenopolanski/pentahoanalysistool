/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.ui.widgets;

import org.pentaho.pat.client.util.TableUtil;
import org.pentaho.pat.client.util.dnd.FlexTableRowDragController;
import org.pentaho.pat.rpc.dto.IAxis;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FocusPanel;

/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class MeasureGrid extends FocusPanel{
    
    private FlexTableRowDragController dragController;
    
    private DimensionFlexTable grid;
    
    private Boolean empty = true;
    
    private IAxis currentAxis;
    
    public MeasureGrid(){
        super();
        grid = new DimensionFlexTable();
        final CaptionPanel panel = new CaptionPanel("Measures");
        DOM.setStyleAttribute(this.getElement(), "background", "yellow");
        panel.add(grid);
        this.add(panel);
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

}
