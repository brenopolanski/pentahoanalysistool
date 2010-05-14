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
package org.pentaho.pat.client.util.dnd.impl;

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.ui.panels.LogoPanel;
import org.pentaho.pat.client.ui.widgets.DimensionFlexTable;
import org.pentaho.pat.client.ui.widgets.MeasureGrid;
import org.pentaho.pat.client.ui.widgets.MeasureLabel;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.rpc.dto.IAxis;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Flex Table DnD utils
 * 
 * @author tom(at)wamonline.org.uk
 */
public class FlexTableUtilImpl {

    /**
     * Copy an entire FlexTable from one FlexTable to another. Each element is copied by creating a new {@link HTML}
     * widget by calling {@link FlexTable#getHTML(int, int)} on the source table.
     * 
     * @param sourceTable
     *            the FlexTable to copy a row from
     * @param targetTable
     *            the FlexTable to copy a row to
     * @param sourceRow
     *            the index of the source row
     * @param targetRow
     *            the index before which to insert the copied row
     */
    protected static void copyRow(final MeasureLabel sourceTable, final MeasureLabel targetTable) {
        
    	targetTable.setValue(sourceTable.getValue());
    	targetTable.setType(sourceTable.getType());
        copyRowStyle(sourceTable, targetTable);
        
    }

    /**
     * Move an entire FlexTable from one FlexTable to another. Elements are moved by attempting to call
     * {@link FlexTable#getWidget(int, int)} on the source table. If no widget is found (because <code>null</code> is
     * returned), a new {@link HTML} is created instead by calling {@link FlexTable#getHTML(int, int)} on the source
     * table.
     * 
     * @param sourceTable
     *            the FlexTable to move a row from
     * @param targetTable
     *            the FlexTable to move a row to
     * @param sourceRow
     *            the index of the source row
     * @param targetRow
     *            the index before which to insert the moved row
     * @param targetAxis
     *            the target axis
     */
    protected static void moveRow(final DimensionFlexTable sourceTable, final DimensionFlexTable targetTable,
            final int sourceRoworCol, final boolean isSourceRow, final int targetRow, final int targetCol, final IAxis targetAxis) {
        DimensionMovementsImpl dmovements = new DimensionMovementsImpl();
    	LogoPanel.spinWheel(true);
        final int sRoworCol = sourceRoworCol;
        if (sourceTable.getAxis() == null || !sourceTable.getAxis().equals(targetTable.getAxis())) {
            final Widget w;
            if(isSourceRow){
                w = sourceTable.getWidget(sRoworCol, 0);
            }
            else{
                w = sourceTable.getWidget(0, sRoworCol);
            }
            
            if (w instanceof MeasureLabel) {
                if (((MeasureLabel) w).getType() == MeasureLabel.LabelType.DIMENSION)
                    dmovements.moveDimension(w, sourceRoworCol, isSourceRow, sourceTable, targetTable, targetAxis);
                else if (((MeasureLabel) w).getType() == MeasureLabel.LabelType.MEASURE)
                    dmovements.moveMeasure(w, sourceRoworCol, isSourceRow, sourceTable, targetTable, targetAxis);
                else {
                    MessageBox.error(ConstantFactory.getInstance().error(), "Forgot to add to the list");
                }
            } else if (w instanceof MeasureGrid)
                dmovements.moveMeasureGrid(w, sourceRoworCol, isSourceRow, sourceTable, targetTable, targetAxis);
        }
    }

    /**
     * Copies the CSS style of a source row to a target row.
     * 
     * @param sourceTable
     *            the source table
     * @param targetTable
     *            the target table
     * @param sourceRow
     *            the source row
     * @param targetRow
     *            the target row
     */
    private static void copyRowStyle(final MeasureLabel sourceTable, final MeasureLabel targetTable) {
        final String rowStyle = sourceTable.getStyleName();
        targetTable.setStyleName(rowStyle);
    }

	public static void moveRowNew(MeasureLabel draggableTable,
			DimensionFlexTable flexTable) {
		
		flexTable.setWidget(0,0, draggableTable);
		
	}

    }
