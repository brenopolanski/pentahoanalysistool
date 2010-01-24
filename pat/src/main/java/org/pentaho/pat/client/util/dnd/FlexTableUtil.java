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
import java.util.List;

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.QueryListenerCollection;
import org.pentaho.pat.client.ui.panels.LogoPanel;
import org.pentaho.pat.client.ui.widgets.DimensionFlexTable;
import org.pentaho.pat.client.ui.widgets.MeasureGrid;
import org.pentaho.pat.client.ui.widgets.MeasureLabel;
import org.pentaho.pat.client.util.TableUtil;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.IAxis;
import org.pentaho.pat.rpc.dto.StringTree;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Flex Table DnD utils
 * 
 * @author tom(at)wamonline.org.uk
 */
public class FlexTableUtil {

    private final static String SPACER_LABEL = "spacer-label"; //$NON-NLS-1$

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
        LogoPanel.spinWheel(true);
        final int sRow = sourceRow;
        final int sCol = sourceCol;
        if (sourceTable.getAxis() == null || !sourceTable.getAxis().equals(targetTable.getAxis())) {
            final Widget w = sourceTable.getWidget(sRow, sCol);
            if (w instanceof MeasureLabel) {
                if (((MeasureLabel) w).getType() == MeasureLabel.labelType.DIMENSION)
                    moveDimension(w, sourceRow, sourceTable, targetTable, targetAxis);
                else if (((MeasureLabel) w).getType() == MeasureLabel.labelType.MEASURE)
                    moveMeasure(w, sourceRow, sourceTable, targetTable, targetAxis);
                else {
                    // Throw Error
                }
            } else if (w instanceof MeasureGrid)
                moveMeasureGrid(w, sourceRow, sourceTable, targetTable, targetAxis);
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

    private static void moveDimension(final Widget w, final int sourceRow, final DimensionFlexTable sourceTable,
            final DimensionFlexTable targetTable, final IAxis targetAxis) {
        ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), Pat.getCurrQuery(), targetAxis,
                ((MeasureLabel) w).getText().trim(), new AsyncCallback<Object>() {

                    public void onFailure(final Throwable arg0) {
                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                .failedDimensionSet(arg0.getLocalizedMessage()));

                    }

                    public void onSuccess(final Object arg0) {
                        ServiceFactory.getDiscoveryInstance().getMembers(Pat.getSessionID(), Pat.getCurrQuery(),
                                ((MeasureLabel) w).getText().trim(), new AsyncCallback<StringTree>() {

                                    public void onFailure(final Throwable arg0) {
                                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory
                                                .getInstance().failedMemberFetch(arg0.getLocalizedMessage()));

                                    }

                                    public void onSuccess(final StringTree memberTree) {
                                        final ArrayList<String> names = new ArrayList<String>();
                                        names.add(memberTree.getChildren().get(0).getValue());
                                        // TODO Create interface to createselection with default member w/o
                                        // getting them all first.
                                        ServiceFactory.getQueryInstance().createSelection(Pat.getSessionID(),
                                                Pat.getCurrQuery(), ((MeasureLabel) w).getText().trim(), names,
                                                "MEMBER", new AsyncCallback<Object>() { //$NON-NLS-1$

                                                    public void onFailure(final Throwable arg0) {
                                                        MessageBox.error(ConstantFactory.getInstance().error(),
                                                                MessageFactory.getInstance().noSelectionSet(
                                                                        arg0.getLocalizedMessage()));
                                                    }

                                                    public void onSuccess(final Object arg0) {
                                                        ((QueryListenerCollection) GlobalConnectionFactory.getQueryInstance().getQueryListeners()
                                                                .clone()).fireQueryChanged(w, sourceRow, sourceTable.getAxis(),
                                                                        targetTable.getAxis());

                                                        LogoPanel.spinWheel(false);

                                                    }
                                                });
                                    }
                                });

                    }

                });

    }

    private static void moveDimensionCreateSelection(final IAxis targetAxis, final Widget w,
            final DimensionFlexTable sourceTable, final int sourceRow, final DimensionFlexTable targetTable) {
        ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), Pat.getCurrQuery(), targetAxis,
                "Measures", new AsyncCallback<Object>() { //$NON-NLS-1$

                    public void onFailure(final Throwable arg0) {
                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                .failedDimensionSet(arg0.getLocalizedMessage()));
                    }

                    public void onSuccess(final Object arg0) {
                        final ArrayList<String> memberList = new ArrayList<String>();
                        memberList.add(((MeasureLabel) w).getText().trim());
                        ServiceFactory.getQueryInstance().createSelection(Pat.getSessionID(), Pat.getCurrQuery(),
                                "Measures", memberList, "MEMBER", //$NON-NLS-1$ //$NON-NLS-2$
                                new AsyncCallback<Object>() {

                                    public void onFailure(final Throwable arg0) {
                                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedDimensionSet(arg0.getLocalizedMessage()));
                                        LogoPanel.spinWheel(false);

                                    }

                                    public void onSuccess(final Object arg0) {

                                        sourceTable.removeRow(sourceRow);
                                        ((QueryListenerCollection) GlobalConnectionFactory.getQueryInstance().getQueryListeners()
                                                .clone()).fireQueryChanged(w, sourceRow, sourceTable.getAxis(),
                                                        targetTable.getAxis());
                                        Pat.setMeasuresDimension(targetAxis);
                                        LogoPanel.spinWheel(false);
                                    }
                                });
                    }
                });

    }

    private static void measureClearSelection(final ArrayList<String> memberList, final DimensionFlexTable sourceTable,
            final int sourceRow, final DimensionFlexTable targetTable, final IAxis targetAxis, final Widget w) {
        ServiceFactory.getQueryInstance().clearSelection(Pat.getSessionID(), Pat.getCurrQuery(),
                "Measures", memberList, new AsyncCallback<Object>() { //$NON-NLS-1$

                    public void onFailure(final Throwable arg0) {
                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedDimensionSet(arg0.getLocalizedMessage()));
                        LogoPanel.spinWheel(false);
                        
                    }

                    public void onSuccess(final Object arg0) {
                        ((QueryListenerCollection) GlobalConnectionFactory.getQueryInstance().getQueryListeners().clone()).fireQueryChanged(w, sourceRow,
                                sourceTable.getAxis(), targetTable.getAxis());
                        LogoPanel.spinWheel(false);

                    }

                });

    }

    private static void moveMeasure(final Widget w, final int sourceRow, final DimensionFlexTable sourceTable,
            final DimensionFlexTable targetTable, final IAxis targetAxis) {

        // If Measures is in unused Move Dimension to axis and create selection 
        //or if the source dimension only has one measure left
        if ((Pat.getMeasuresDimension().equals(IAxis.UNUSED) && !Pat.getMeasuresDimension().equals(targetAxis))
                || (targetAxis.equals(IAxis.UNUSED) && sourceTable.getRowCount() == 1)) {
            moveDimensionCreateSelection(targetAxis, w, sourceTable, sourceRow, targetTable);
        } else if (targetAxis.equals(IAxis.UNUSED)) {
            final ArrayList<String> memberList = new ArrayList<String>();
            memberList.add(((MeasureLabel) w).getText().trim());
            if (sourceTable.getRowCount() > 1) {
                measureClearSelection(memberList, sourceTable, sourceRow, targetTable, targetAxis, w);
            }

        }

        // Else Just adjust measures selection
        else if (!Pat.getMeasuresDimension().equals(IAxis.UNUSED) && Pat.getMeasuresDimension().equals(targetAxis)) {
            final ArrayList<String> memberList = new ArrayList<String>();
            memberList.add(((MeasureLabel) w).getText().trim());
            measureCreateSelection(memberList, sourceTable, sourceRow, targetTable, w);
        } else
            //Throw Error.
            LogoPanel.spinWheel(false);

    }

    private static void measureCreateSelection(final ArrayList<String> memberList,
            final DimensionFlexTable sourceTable, final int sourceRow, final DimensionFlexTable targetTable,
            final Widget w) {
        ServiceFactory.getQueryInstance().createSelection(Pat.getSessionID(), Pat.getCurrQuery(),
                "Measures", memberList, "MEMBER", new AsyncCallback<Object>() { //$NON-NLS-1$//$NON-NLS-2$

                    public void onFailure(final Throwable arg0) {
                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedDimensionSet(arg0.getLocalizedMessage()));
                        LogoPanel.spinWheel(false);
                    }

                    public void onSuccess(final Object arg0) {
                        ((QueryListenerCollection) GlobalConnectionFactory.getQueryInstance().getQueryListeners().clone()).fireQueryChanged(w, sourceRow,
                                sourceTable.getAxis(), targetTable.getAxis());
                        LogoPanel.spinWheel(false);
                    }
                });
    }

    private static void moveMeasureGrid(final Widget w, final int sourceRow, final DimensionFlexTable sourceTable,
            final DimensionFlexTable targetTable, final IAxis targetAxis) {
        if (Pat.getMeasuresDimension() == IAxis.UNUSED || targetAxis.equals(IAxis.UNUSED))
            ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), Pat.getCurrQuery(), targetAxis,
                    "Measures", new AsyncCallback<Object>() { //$NON-NLS-1$

                        public void onFailure(final Throwable arg0) {
                            MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedDimensionSet(arg0.getLocalizedMessage()));
                            LogoPanel.spinWheel(false);
                        }

                        public void onSuccess(final Object arg0) {
                            
                            if (sourceTable.getAxis().equals(IAxis.UNUSED) && w instanceof MeasureGrid) {
                                final Widget wid = TableUtil.cloneMeasureGrid((MeasureGrid) w);
                                final List<String> memberNames = new ArrayList<String>();
                                for (int i = 0; i < ((MeasureGrid) wid).getRows().getRowCount(); i++) {
                                    memberNames.clear();
                                    final MeasureLabel measureLab = (MeasureLabel) ((MeasureGrid) wid).getRows()
                                            .getWidget(i, 0);
                                    memberNames.add(measureLab.getText().trim());
                                    ServiceFactory.getQueryInstance().createSelection(Pat.getSessionID(),
                                            Pat.getCurrQuery(),
                                            "Measures", memberNames, "MEMBER", new AsyncCallback<Object>() { //$NON-NLS-1$//$NON-NLS-2$

                                                public void onFailure(final Throwable arg0) {
                                                    MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedDimensionSet(arg0.getLocalizedMessage()));
                                                    LogoPanel.spinWheel(false);

                                                }

                                                public void onSuccess(final Object arg0) {
                                                    ((QueryListenerCollection) GlobalConnectionFactory.getQueryInstance().getQueryListeners().clone()).fireQueryChanged(wid,
                                                            sourceRow, sourceTable.getAxis(), targetTable.getAxis());
                                                    
                                                    
                                                }

                                            });

                                }


                            }

                            else if (targetTable.getAxis().equals(IAxis.UNUSED) && w instanceof MeasureGrid) {
                                
                                ((QueryListenerCollection) GlobalConnectionFactory.getQueryInstance().getQueryListeners().clone()).fireQueryChanged(w,
                                        sourceRow, sourceTable.getAxis(), targetTable.getAxis());
                                
                                
                            } else
                                ((QueryListenerCollection) GlobalConnectionFactory.getQueryInstance().getQueryListeners().clone()).fireQueryChanged(w,
                                        sourceRow, sourceTable.getAxis(), targetTable.getAxis());

                            Pat.setMeasuresDimension(targetAxis);

                            LogoPanel.spinWheel(false);
                        }

                    });
        else if (Pat.getMeasuresDimension().equals(targetAxis)) {
            final ArrayList<String> memberList = new ArrayList<String>();
            for (int i = 0; i < ((MeasureGrid) w).getMeasureLabels().size(); i++) {
                memberList.clear();
                memberList.add(((MeasureLabel) ((MeasureGrid) w).getMeasureLabels().get(i)).getText().trim());
                ServiceFactory.getQueryInstance().createSelection(Pat.getSessionID(), Pat.getCurrQuery(),
                        "Measures", memberList, "MEMBER", new AsyncCallback<Object>() { //$NON-NLS-1$//$NON-NLS-2$

                            public void onFailure(final Throwable arg0) {
                                MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedDimensionSet(arg0.getLocalizedMessage()));
                                LogoPanel.spinWheel(false);
                            }

                            public void onSuccess(final Object arg0) {
                                ((QueryListenerCollection) GlobalConnectionFactory.getQueryInstance().getQueryListeners().clone()).fireQueryChanged(w, sourceRow,
                                        sourceTable.getAxis(), targetTable.getAxis());
                                LogoPanel.spinWheel(false);
                            }
                        });
            }
            
         
        }
        else {
            // TODO Throw error.
        }
    }
}
