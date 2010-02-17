/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.util;

import org.pentaho.pat.client.ui.widgets.MeasureGrid;
import org.pentaho.pat.client.ui.widgets.MeasureLabel;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

/**
 *TODO JAVADOC
 * 
 * @author bugg
 * 
 */
public class TableUtil {

    private final static String TABLE_CSS_SPACER = "spacer-label"; //$NON-NLS-1$
    
    public static FlexTable insertSpacer(FlexTable flexTable) {
        flexTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
        flexTable.setWidget(0, 0, new Label("")); //$NON-NLS-1$
        flexTable.getCellFormatter().addStyleName(0, 0, TABLE_CSS_SPACER);
        return flexTable;
    }
    
    public static FlexTable removeSpacer(FlexTable flexTable){
        if(flexTable.getCellFormatter().getStyleName(0, 0).equals(TABLE_CSS_SPACER)){
            flexTable.getCellFormatter().removeStyleName(0, 0, TABLE_CSS_SPACER);
            flexTable.removeRow(0);
            return flexTable;
        }
        else
            return flexTable;
    }
    
    public static FlexTable clearTableRows(FlexTable flexTable){
        int count = flexTable.getRowCount();
        while (count > 0) {
            flexTable.removeRow(0);
            count--;
        }
        return flexTable;
    }
    
    public static FlexTable clearUnusedTableRows(FlexTable flexTable){
        int count = flexTable.getRowCount();
        while (count > 0) {
            if(flexTable.getWidget(count, 0)==null){
            flexTable.removeRow(0);
            }
            count--;
        }
        return flexTable;
    }
    
    
    public static MeasureLabel cloneMeasureLabel(MeasureLabel sender){
        return new MeasureLabel(sender.getText(), sender.getType(), sender.getDragController(), true);
    }
    
    public static MeasureGrid cloneMeasureGrid(MeasureGrid sender){
        MeasureGrid oldMeasureGrid = ((MeasureGrid)sender);
        MeasureGrid mg = new MeasureGrid(oldMeasureGrid.getQuery(), oldMeasureGrid.getCurrentAxis(), oldMeasureGrid.getHorizontal());
        mg.setDragController(((MeasureGrid)sender).getDragController());
        for(int i=0; i<oldMeasureGrid.getRows().getRowCount(); i++){
            MeasureLabel oldMl = (MeasureLabel) oldMeasureGrid.getRows().getWidget(i, 0);
            mg.addRow(cloneMeasureLabel(oldMl), i);
        }
        return mg;
    }
    public static MeasureGrid cloneMeasureGrid(MeasureGrid sender, Boolean overrideHorizontal){
        MeasureGrid oldMeasureGrid = ((MeasureGrid)sender);
        MeasureGrid mg = new MeasureGrid(oldMeasureGrid.getQuery(), oldMeasureGrid.getCurrentAxis(), overrideHorizontal);
        mg.setDragController(((MeasureGrid)sender).getDragController());
        if(oldMeasureGrid.getHorizontal()){
            for(int i=0; i<oldMeasureGrid.getRows().getCellCount(0); i++){
            MeasureLabel oldMl = (MeasureLabel) oldMeasureGrid.getRows().getWidget(0,i);
            mg.addRow(cloneMeasureLabel(oldMl), i);
            }
        }
        else{
        for(int i=0; i<oldMeasureGrid.getRows().getRowCount(); i++){
            MeasureLabel oldMl = (MeasureLabel) oldMeasureGrid.getRows().getWidget(i, 0);
            mg.addRow(cloneMeasureLabel(oldMl), i);
        }
        }
        return mg;
    }
}
