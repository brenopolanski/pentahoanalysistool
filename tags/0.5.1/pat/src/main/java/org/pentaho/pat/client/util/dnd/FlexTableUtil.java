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
package org.pentaho.pat.client.util.dnd;

import java.util.ArrayList;

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.panels.LogoPanel;
import org.pentaho.pat.client.ui.widgets.DimensionFlexTable;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.IAxis;
import org.pentaho.pat.rpc.dto.StringTree;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Flex Table DnD utils
 * 
 * @author tom(at)wamonline.org.uk
 */
public class FlexTableUtil {

    private final static String TABLE_CSS_SPACER = "spacer-label"; //$NON-NLS-1$

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
    public static void copyRow(final DimensionFlexTable sourceTable, final DimensionFlexTable targetTable,
            final int sourceRow, final int targetRow) {
        targetTable.insertRow(targetRow);
        final HTML html = new HTML();
        for (int col = 0; col < sourceTable.getCellCount(sourceRow); col++) {
            html.setHTML(sourceTable.getHTML(sourceRow, col));
            targetTable.setWidget(targetRow, col, html);
        }
        copyRowStyle(sourceTable, targetTable, sourceRow, targetRow);
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
    public static void moveRow(final DimensionFlexTable sourceTable, final DimensionFlexTable targetTable,
            final int sourceRow, final int targetRow, final int sourceCol, final int targetCol, final IAxis targetAxis) {
        // targetRow = targetTable.getRowCount();
        LogoPanel.spinWheel(true);
        final int sRow = sourceRow;
        final int sCol = sourceCol;
        if (!sourceTable.equals(targetTable)) {
            if (sourceTable.getHorizontal()) {
                // if (sourceTable.equals(targetTable) && sCol >= targetCol)
                // sCol++;
                targetTable.insertCell(0, targetTable.getCellCount(0));
            } else {
                // if (sourceTable.equals(targetTable) && sRow >= targetRow)
                // sRow++;
                targetTable.insertRow(targetTable.getRowCount());
            }
            final Widget w = sourceTable.getWidget(sRow, sCol);
            ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), Pat.getCurrQuery(), targetAxis,
                    w.getElement().getInnerText().trim(), new AsyncCallback<Object>() {

                        public void onFailure(final Throwable arg0) {
                            MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                    .failedDimensionSet(arg0.getLocalizedMessage()));
                        }

                        public void onSuccess(final Object arg0) {
                            ServiceFactory.getDiscoveryInstance().getMembers(Pat.getSessionID(), Pat.getCurrQuery(),
                                    w.getElement().getInnerText().trim(), new AsyncCallback<StringTree>() {

                                        public void onFailure(final Throwable arg0) {
                                            MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory
                                                    .getInstance().failedMemberFetch(arg0.getLocalizedMessage()));
                                        }

                                        public void onSuccess(final StringTree arg0) {
                                            ServiceFactory.getDiscoveryInstance().getMembers(Pat.getSessionID(),
                                                    Pat.getCurrQuery(), w.getElement().getInnerText().trim(),
                                                    new AsyncCallback<StringTree>() {

                                                        public void onFailure(final Throwable arg0) {
                                                            MessageBox.error(ConstantFactory.getInstance().error(),
                                                                    MessageFactory.getInstance().failedMemberFetch(
                                                                            arg0.getLocalizedMessage()));

                                                        }

                                                        public void onSuccess(final StringTree memberTree) {

                                                            final ArrayList<String> names = new ArrayList<String>();
                                                            names.add(memberTree.getChildren().get(0).getValue());
                                                            ServiceFactory.getQueryInstance().createSelection(
                                                                    Pat.getSessionID(), Pat.getCurrQuery(),
                                                                    w.getElement().getInnerText().trim(), names,
                                                                    "MEMBER", new AsyncCallback<Object>() { //$NON-NLS-1$

                                                                        public void onFailure(final Throwable arg0) {
                                                                            MessageBox
                                                                                    .error(
                                                                                            ConstantFactory
                                                                                                    .getInstance()
                                                                                                    .error(),
                                                                                            MessageFactory
                                                                                                    .getInstance()
                                                                                                    .noSelectionSet(
                                                                                                            arg0
                                                                                                                    .getLocalizedMessage()));
                                                                        }

                                                                        public void onSuccess(final Object arg0) {

                                                                            if (targetTable.getHorizontal()) {

                                                                                final Label spacerLabel = new Label(""); //$NON-NLS-1$
                                                                                spacerLabel
                                                                                        .setStylePrimaryName(TABLE_CSS_SPACER);
                                                                                targetTable
                                                                                        .getCellFormatter()
                                                                                        .setHorizontalAlignment(
                                                                                                0,
                                                                                                targetTable
                                                                                                        .getCellCount(0),
                                                                                                HasHorizontalAlignment.ALIGN_LEFT);
                                                                                targetTable
                                                                                        .getCellFormatter()
                                                                                        .setVerticalAlignment(
                                                                                                0,
                                                                                                targetTable
                                                                                                        .getCellCount(0),
                                                                                                HasVerticalAlignment.ALIGN_TOP);
                                                                                targetTable.setWidget(0, targetTable
                                                                                        .getCellCount(0), spacerLabel);

                                                                            } else {
                                                                                final Label spacerLabel = new Label(""); //$NON-NLS-1$
                                                                                spacerLabel
                                                                                        .setStylePrimaryName(TABLE_CSS_SPACER);
                                                                                targetTable
                                                                                        .getCellFormatter()
                                                                                        .setVerticalAlignment(
                                                                                                targetTable
                                                                                                        .getRowCount(),
                                                                                                0,
                                                                                                HasVerticalAlignment.ALIGN_TOP);
                                                                                targetTable.setWidget(targetTable
                                                                                        .getRowCount(), 0, spacerLabel);
                                                                            }
                                                                            GlobalConnectionFactory.getQueryInstance()
                                                                                    .getQueryListeners()
                                                                                    .fireQueryChanged(w);

                                                                            copyRowStyle(sourceTable, targetTable,
                                                                                    sourceRow, targetTable
                                                                                            .getRowCount());
                                                                            sourceTable.removeRow(sourceRow);
                                                                            LogoPanel.spinWheel(false);
                                                                        }

                                                                    });

                                                        }

                                                    });

                                        }

                                    });
                        }

                    });

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
    private static void copyRowStyle(final DimensionFlexTable sourceTable, final DimensionFlexTable targetTable,
            final int sourceRow, final int targetRow) {
        final String rowStyle = sourceTable.getRowFormatter().getStyleName(sourceRow);
        targetTable.getRowFormatter().setStyleName(targetRow, rowStyle);
    }

}