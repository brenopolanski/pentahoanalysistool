/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.ui.widgets;

import com.google.gwt.user.client.ui.FocusPanel;

/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class MeasureGrid extends FocusPanel{

    
    DimensionFlexTable grid;
    public MeasureGrid(){
        super();
        grid = new DimensionFlexTable();
        this.add(grid);
    }
    
    public void addRow(MeasureLabel ml, int row){
        grid.setWidget(row, 0, ml);
    }
    
    public DimensionFlexTable getRows(){
        return grid;
    }
    
    public void removeRow(int row){
        grid.removeRow(row);
    }
}
