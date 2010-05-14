/*
 * Copyright (C) 2010 Thomas Barber
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

import java.util.ArrayList;
import java.util.List;

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
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
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author tom(at)wamonline.org.uk
 *
 */
public class DimensionMovementsImpl {

	/**
	 * Move a Dimension Widget from axis x to axis y.
	 * @param w
	 * @param sourceRow
	 * @param isSourceRow
	 * @param sourceTable
	 * @param targetTable
	 * @param targetAxis
	 */
	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.util.dnd.impl.DimensionMovement#moveDimension(com.google.gwt.user.client.ui.Widget, int, boolean, org.pentaho.pat.client.ui.widgets.DimensionFlexTable, org.pentaho.pat.client.ui.widgets.DimensionFlexTable, org.pentaho.pat.rpc.dto.IAxis)
	 */
	public void moveDimension(final Widget w, final int sourceRow, final boolean isSourceRow,
            final DimensionFlexTable sourceTable, final DimensionFlexTable targetTable, final IAxis targetAxis) {
        ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), Pat.getCurrQuery(), targetAxis,
                ((MeasureLabel) w).getText().trim(), new AsyncCallback<Object>() {

                    public void onFailure(final Throwable arg0) {
                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                .failedDimensionSet(arg0.getLocalizedMessage()));

                    }

                    public void onSuccess(final Object arg0) {
                        ServiceFactory.getQueryInstance().createSelection(Pat.getSessionID(), Pat.getCurrQuery(),
                                ((MeasureLabel) w).getText().trim(), "MEMBER", new AsyncCallback<Object>() { //$NON-NLS-1$

                                    public void onFailure(final Throwable arg0) {
                                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory
                                                .getInstance().noSelectionSet(arg0.getLocalizedMessage()));
                                        LogoPanel.spinWheel(false);
                                    }

                                    public void onSuccess(final Object arg0) {
                                        GlobalConnectionFactory.getQueryInstance().getQueryListeners()
                                                .fireQueryChanged(w, sourceRow, isSourceRow, sourceTable.getAxis(),
                                                        targetTable.getAxis());

                                        LogoPanel.spinWheel(false);

                                    }
                                });
                    }
                });

    }

	/**
	 * Move the measures dimension from axis x to axis y and create a selection.
	 * @param targetAxis
	 * @param w
	 * @param sourceTable
	 * @param sourceRow
	 * @param isSourceRow
	 * @param targetTable
	 */
    /* (non-Javadoc)
	 * @see org.pentaho.pat.client.util.dnd.impl.DimensionMovement#moveDimensionCreateSelection(org.pentaho.pat.rpc.dto.IAxis, com.google.gwt.user.client.ui.Widget, org.pentaho.pat.client.ui.widgets.DimensionFlexTable, int, boolean, org.pentaho.pat.client.ui.widgets.DimensionFlexTable)
	 */
    public void moveDimensionCreateSelection(final IAxis targetAxis, final Widget w,
            final DimensionFlexTable sourceTable, final int sourceRow, final boolean isSourceRow,
            final DimensionFlexTable targetTable) {
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
                                new AsyncCallback<StringTree>() {

                                    public void onFailure(final Throwable arg0) {
                                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory
                                                .getInstance().failedDimensionSet(arg0.getLocalizedMessage()));
                                        LogoPanel.spinWheel(false);

                                    }

                                    public void onSuccess(final StringTree arg0) {

                                        GlobalConnectionFactory.getQueryInstance().getQueryListeners()
                                                .fireQueryChanged(w, sourceRow, isSourceRow, sourceTable.getAxis(),
                                                        targetTable.getAxis());
                                        Pat.setMeasuresAxis(targetAxis);
                                        LogoPanel.spinWheel(false);
                                    }
                                });
                    }
                });

    }

    /**
     * Clear Measure Selection.
     * @param memberList
     * @param sourceTable
     * @param sourceRow
     * @param isSourceRow
     * @param targetTable
     * @param targetAxis
     * @param w
     */
    /* (non-Javadoc)
	 * @see org.pentaho.pat.client.util.dnd.impl.DimensionMovement#measureClearSelection(java.util.ArrayList, org.pentaho.pat.client.ui.widgets.DimensionFlexTable, int, boolean, org.pentaho.pat.client.ui.widgets.DimensionFlexTable, org.pentaho.pat.rpc.dto.IAxis, com.google.gwt.user.client.ui.Widget)
	 */
    public void measureClearSelection(final ArrayList<String> memberList, final DimensionFlexTable sourceTable,
            final int sourceRow, final boolean isSourceRow, final DimensionFlexTable targetTable, final IAxis targetAxis,
            final Widget w) {
        ServiceFactory.getQueryInstance().clearSelection(Pat.getSessionID(), Pat.getCurrQuery(),
                "Measures", memberList, new AsyncCallback<Object>() { //$NON-NLS-1$

                    public void onFailure(final Throwable arg0) {
                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                .failedDimensionSet(arg0.getLocalizedMessage()));
                        LogoPanel.spinWheel(false);

                    }

                    public void onSuccess(final Object arg0) {
                        GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryChanged(w, sourceRow,
                                isSourceRow, sourceTable.getAxis(), targetTable.getAxis());
                        LogoPanel.spinWheel(false);

                    }

                });

    }

    /**
     * Move a measure from axis x to axis y.
     * @param w
     * @param sourceRow
     * @param isSourceRow
     * @param sourceTable
     * @param targetTable
     * @param targetAxis
     */
    /* (non-Javadoc)
	 * @see org.pentaho.pat.client.util.dnd.impl.DimensionMovement#moveMeasure(com.google.gwt.user.client.ui.Widget, int, boolean, org.pentaho.pat.client.ui.widgets.DimensionFlexTable, org.pentaho.pat.client.ui.widgets.DimensionFlexTable, org.pentaho.pat.rpc.dto.IAxis)
	 */
    public void moveMeasure(final Widget w, final int sourceRow, final boolean isSourceRow,
            final DimensionFlexTable sourceTable, final DimensionFlexTable targetTable, final IAxis targetAxis) {

        // If Measures is in unused Move Dimension to axis and create selection
        // or if the source dimension only has one measure left
        if ((Pat.getMeasuresAxis().equals(IAxis.UNUSED) && !Pat.getMeasuresAxis().equals(targetAxis))
                || (targetAxis.equals(IAxis.UNUSED) && sourceTable.getRowCount() == 1 && sourceTable.getCellCount(0) == 1)) {
            moveDimensionCreateSelection(targetAxis, w, sourceTable, sourceRow, isSourceRow, targetTable);
        } else if (targetAxis.equals(IAxis.UNUSED)) {
            final ArrayList<String> memberList = new ArrayList<String>();
            memberList.add(((MeasureLabel) w).getText().trim());
            /* if (sourceTable.getRowCount() > 1) { */
            measureClearSelection(memberList, sourceTable, sourceRow, isSourceRow, targetTable, targetAxis, w);
            // }

        }

        // Else Just adjust measures selection
        else if (!Pat.getMeasuresAxis().equals(IAxis.UNUSED) && Pat.getMeasuresAxis().equals(targetAxis)) {
            final ArrayList<String> memberList = new ArrayList<String>();
            memberList.add(((MeasureLabel) w).getText().trim());
            measureCreateSelection(memberList, sourceTable, sourceRow, isSourceRow, targetTable, w);
        } else
            // Throw Error.
            LogoPanel.spinWheel(false);

    }

    /**
     * Move Measure Dimension Widget and Create a Selection.
     * @param memberList
     * @param sourceTable
     * @param sourceRow
     * @param isSourceRow
     * @param targetTable
     * @param w
     */
    /* (non-Javadoc)
	 * @see org.pentaho.pat.client.util.dnd.impl.DimensionMovement#measureCreateSelection(java.util.ArrayList, org.pentaho.pat.client.ui.widgets.DimensionFlexTable, int, boolean, org.pentaho.pat.client.ui.widgets.DimensionFlexTable, com.google.gwt.user.client.ui.Widget)
	 */
    public void measureCreateSelection(final ArrayList<String> memberList,
            final DimensionFlexTable sourceTable, final int sourceRow, final boolean isSourceRow,
            final DimensionFlexTable targetTable, final Widget w) {
        ServiceFactory.getQueryInstance().createSelection(Pat.getSessionID(), Pat.getCurrQuery(),
                "Measures", memberList, "MEMBER", new AsyncCallback<StringTree>() { //$NON-NLS-1$//$NON-NLS-2$

                    public void onFailure(final Throwable arg0) {
                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                .failedDimensionSet(arg0.getLocalizedMessage()));
                        LogoPanel.spinWheel(false);
                    }

                    public void onSuccess(final StringTree arg0) {
                        GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryChanged(w, sourceRow,
                                isSourceRow, sourceTable.getAxis(), targetTable.getAxis());
                        LogoPanel.spinWheel(false);
                    }
                });
    }

    /**
     * Move a measureGrid from axis x to axis y.
     * @param w
     * @param sourceRow
     * @param isSourceRow
     * @param sourceTable
     * @param targetTable
     * @param targetAxis
     */
    /* (non-Javadoc)
	 * @see org.pentaho.pat.client.util.dnd.impl.DimensionMovement#moveMeasureGrid(com.google.gwt.user.client.ui.Widget, int, boolean, org.pentaho.pat.client.ui.widgets.DimensionFlexTable, org.pentaho.pat.client.ui.widgets.DimensionFlexTable, org.pentaho.pat.rpc.dto.IAxis)
	 */
    public void moveMeasureGrid(final Widget w, final int sourceRow, final boolean isSourceRow,
            final DimensionFlexTable sourceTable, final DimensionFlexTable targetTable, final IAxis targetAxis) {

        // If Current Measures Axis or the target axis is unused
        if (Pat.getMeasuresAxis() == IAxis.UNUSED || targetAxis.equals(IAxis.UNUSED))
            ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), Pat.getCurrQuery(), targetAxis,
                    "Measures", new AsyncCallback<Object>() { //$NON-NLS-1$

                        public void onFailure(final Throwable arg0) {
                            MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                    .failedDimensionSet(arg0.getLocalizedMessage()));
                            LogoPanel.spinWheel(false);
                        }

                        public void onSuccess(final Object arg0) {

                            if (sourceTable.getAxis().equals(IAxis.UNUSED)) {
                                final Widget wid = TableUtil.cloneMeasureGrid((MeasureGrid) w, ((MeasureGrid)w).getHorizontal());
                                final List<String> memberNames = new ArrayList<String>();
                                for (int i = 0; i < ((MeasureGrid) wid).getRows().getRowCount(); i++) {
                                    memberNames.clear();
                                    final MeasureLabel measureLab = (MeasureLabel) ((MeasureGrid) wid).getRows()
                                            .getWidget(i, 0);
                                    memberNames.add(measureLab.getText().trim());
                                    ServiceFactory.getQueryInstance().createSelection(Pat.getSessionID(),
                                            Pat.getCurrQuery(),
                                            "Measures", memberNames, "MEMBER", new AsyncCallback<StringTree>() { //$NON-NLS-1$//$NON-NLS-2$

                                                public void onFailure(final Throwable arg0) {
                                                    MessageBox.error(ConstantFactory.getInstance().error(),
                                                            MessageFactory.getInstance().failedDimensionSet(
                                                                    arg0.getLocalizedMessage()));
                                                    LogoPanel.spinWheel(false);

                                                }

                                                public void onSuccess(final StringTree arg0) {

                                                }

                                            });

                                }
                                GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryChanged(wid,
                                        sourceRow, isSourceRow, sourceTable.getAxis(), targetTable.getAxis());

                            }

                            else if (targetTable.getAxis().equals(IAxis.UNUSED)) {
                                final Widget wid = TableUtil.cloneMeasureGrid((MeasureGrid) w, ((MeasureGrid)w).getHorizontal());
                                final List<String> memberNames = new ArrayList<String>();
                                for (int i = 0; i < ((MeasureGrid) wid).getRows().getRowCount(); i++) {
                                    ServiceFactory.getQueryInstance().clearSelection(Pat.getSessionID(),
                                            Pat.getCurrQuery(), "Measures", memberNames, new AsyncCallback<Object>() { //$NON-NLS-1$

                                                public void onFailure(Throwable arg0) {
                                                    // TODO Auto-generated method stub

                                                }

                                                public void onSuccess(Object arg0) {
                                                    // TODO Auto-generated method stub

                                                }

                                            });

                                }
                                GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryChanged(w,
                                        sourceRow, isSourceRow, sourceTable.getAxis(), targetTable.getAxis());

                            } else
                                GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryChanged(w,
                                        sourceRow, isSourceRow, sourceTable.getAxis(), targetTable.getAxis());

                            Pat.setMeasuresAxis(targetAxis);

                            LogoPanel.spinWheel(false);
                        }

                    });
        else if (Pat.getMeasuresAxis().equals(targetAxis)) {
            final ArrayList<String> memberList = new ArrayList<String>();
            for (int i = 0; i < ((MeasureGrid) w).getMeasureLabels().size(); i++) {
                memberList.clear();
                memberList.add(((MeasureLabel) ((MeasureGrid) w).getMeasureLabels().get(i)).getText().trim());
                ServiceFactory.getQueryInstance().createSelection(Pat.getSessionID(), Pat.getCurrQuery(),
                        "Measures", memberList, "MEMBER", new AsyncCallback<StringTree>() { //$NON-NLS-1$//$NON-NLS-2$

                            public void onFailure(final Throwable arg0) {
                                MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                        .failedDimensionSet(arg0.getLocalizedMessage()));
                                LogoPanel.spinWheel(false);
                            }

                            public void onSuccess(final StringTree arg0) {
                            }
                        });
            }
            GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryChanged(w, sourceRow, isSourceRow,
                    sourceTable.getAxis(), targetTable.getAxis());
            LogoPanel.spinWheel(false);

        } else {
            ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), Pat.getCurrQuery(), targetAxis,
                    "Measures", new AsyncCallback<Object>() {

                        public void onFailure(Throwable arg0) {
                            MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                    .failedDimensionSet(arg0.getLocalizedMessage()));
                            LogoPanel.spinWheel(false);

                        }

                        public void onSuccess(Object arg0) {
                            GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryChanged(w,
                                    sourceRow, isSourceRow, sourceTable.getAxis(), targetTable.getAxis());

                            Pat.setMeasuresAxis(targetAxis);

                            LogoPanel.spinWheel(false);

                        }

                    });
        }
    }

    
}
