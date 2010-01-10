/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.util;

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

    private final static Label spacerLabel = new Label(""); //$NON-NLS-1$
    private final static String TABLE_CSS_SPACER = "spacer-label"; //$NON-NLS-1$
    
    public static FlexTable insertSpacer(FlexTable flexTable) {
        flexTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
        flexTable.setWidget(0, 0, spacerLabel);
        flexTable.getCellFormatter().addStyleName(0, 0, TABLE_CSS_SPACER);
        return flexTable;
    }
}
