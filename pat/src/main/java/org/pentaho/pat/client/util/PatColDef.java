/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.util;

import org.gwt.mosaic.ui.client.table.DefaultColumnDefinition;

import com.google.gwt.gen2.table.client.AbstractColumnDefinition;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;

/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class PatColDef<RowType, ColType> extends
        AbstractColumnDefinition<RowType, ColType> implements HasHorizontalAlignment {
    
    /**
     * Construct a new {@link DefaultColumnDefinition}.
     * 
     * @param header the name of the column.
     */
    public PatColDef(Widget header) {
        setHeader(0, header);
    }

    @Override
    public void setCellValue(RowType rowValue, ColType cellValue) {
        // Ignore
    }

    @Override
    public ColType getCellValue(RowType rowValue) {
        // TODO Auto-generated method stub
        return null;
    }

    public HorizontalAlignmentConstant getHorizontalAlignment() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
        // TODO Auto-generated method stub
        
    }
}