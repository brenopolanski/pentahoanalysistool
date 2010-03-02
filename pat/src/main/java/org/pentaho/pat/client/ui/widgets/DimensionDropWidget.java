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
package org.pentaho.pat.client.ui.widgets;

import java.util.Arrays;
import java.util.List;

import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.util.WidgetHelper;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.IQueryListener;
import org.pentaho.pat.client.ui.windows.DimensionBrowserWindow;
import org.pentaho.pat.client.util.TableUtil;
import org.pentaho.pat.client.util.dnd.impl.FlexTableRowDragControllerImpl;
import org.pentaho.pat.client.util.dnd.impl.FlexTableRowDropControllerImpl;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.IAxis;
import org.pentaho.pat.rpc.dto.StringTree;
import org.pentaho.pat.rpc.dto.IAxis.Standard;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Widget;

/**
 * Creates a dimension drop container widget for Dimension and Measure widgets.
 * 
 * @created Aug 8, 2009
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public class DimensionDropWidget extends LayoutComposite implements IQueryListener {

    private IAxis dimAxis;

    private DimensionFlexTable dimensionTable;

    private Boolean horizontal = false;

    private FlexTableRowDropControllerImpl fTblRowDropCont;

    private String query;

    private FlexTableRowDragControllerImpl tblRowDragCont;

    private final static String TABLE_DROP_ROWENDCELL = "pat-dropRowEndCell"; //$NON-NLS-1$

    private final static String TABLE_DRAG_CELL = "pat-dragDimensionCell"; //$NON-NLS-1$

    private final static String DIMENSION_DROP_WIDGET = "pat-DimensionDropWidget"; //$NON-NLS-1$

    private boolean empty = true;

    /**
     *TODO JAVADOC
     * 
     * @param tblRowDragCont
     * 
     * @param unused
     * @param string
     * 
     */
    public DimensionDropWidget(final String labelText, final Standard targetAxis,
            final FlexTableRowDragControllerImpl tblRowDragCont) {
        super();

        init(labelText, targetAxis, tblRowDragCont);

    }

    public DimensionDropWidget(final String labelText, final Standard targetAxis, final Boolean orientation,
            final FlexTableRowDragControllerImpl tblRowDragCont) {
        super();
        horizontal = orientation;
        init(labelText, targetAxis, tblRowDragCont);

    }

    /**
     *TODO JAVADOC
     * 
     * @return the horizontal
     */
    public Boolean getHorizontal() {
        return horizontal;
    }

    /**
     * Initialization.
     * 
     * @param labelText
     *            the label text
     * @param targetAxis
     *            the target axis
     */
    public void init(final String labelText, final IAxis targetAxis, final FlexTableRowDragControllerImpl tRDC) {
        this.setStyleName(DIMENSION_DROP_WIDGET);
        query = Pat.getCurrQuery();
        dimAxis = targetAxis;
        tblRowDragCont = tRDC;
        CaptionLayoutPanel captLayoutPanel = new CaptionLayoutPanel(labelText);
        captLayoutPanel.setLayout(new BoxLayout());
        ImageButton axisButton = new ImageButton(Pat.IMAGES.dimbrowser());
        axisButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent arg0) {
                DimensionBrowserWindow.displayAxis(query, targetAxis);
            }

        });

        captLayoutPanel.getHeader().add(axisButton, CaptionRegion.RIGHT);
        captLayoutPanel.getHeader().layout();

        dimensionTable = new DimensionFlexTable(horizontal, dimAxis);
        dimensionTable = (DimensionFlexTable) TableUtil.insertSpacer(dimensionTable);
        captLayoutPanel.add(dimensionTable, new BoxLayoutData(FillStyle.BOTH, true));

        populateDimensionTable(dimAxis);

        getLayoutPanel().add(captLayoutPanel);
    }

    @Override
    public void onLoad() {
        fTblRowDropCont = new FlexTableRowDropControllerImpl(dimensionTable, dimAxis);
        DimensionDropWidget.this.tblRowDragCont.registerDropController(fTblRowDropCont);
        GlobalConnectionFactory.getQueryInstance().addQueryListener(DimensionDropWidget.this);
    }

    private void doMeasureStuff(Widget sender) {
        if (sender instanceof MeasureLabel && ((MeasureLabel) sender).getType().equals(MeasureLabel.LabelType.MEASURE)) {
            int location = -1;
            // If the dimension drop widget contains a measure grid, add it to the measure grid.
            if(horizontal){
            	for (int i = 0; i < dimensionTable.getCellCount(0); i++) {
                    if (dimensionTable.getWidget(0, i) instanceof MeasureGrid) {
                        location = i;
                        break;
                    }
                }	
            }
            else{
            for (int i = 0; i < dimensionTable.getRowCount(); i++) {
                if (dimensionTable.getWidget(i, 0) instanceof MeasureGrid) {
                    location = i;
                    break;
                }
            }
            }

            if (location == -1) {
                final MeasureGrid mGrid = new MeasureGrid(query, dimAxis, horizontal);
                mGrid.setDragController(tblRowDragCont);
                mGrid.setCurrentAxis(dimAxis);
                mGrid.makeDraggable();
                mGrid.addRow((MeasureLabel) sender);
                flexTableAddRecord(mGrid);
            }

        } else {
            flexTableAddRecord(sender);
        }
    }

    private void doMeasureStuff(MeasureGrid sender) {
        int location = -1;
        // If the dimension drop widget contains a measure grid, add it to the measure grid.
        for (int i = 0; i < dimensionTable.getRowCount(); i++) {
            if (dimensionTable.getWidget(i, 0) instanceof MeasureGrid) {
                location = i;
                break;
            }
        }

        if (location == -1) {

            final MeasureGrid mGrid = TableUtil.cloneMeasureGrid(sender, horizontal);
            mGrid.setDragController(tblRowDragCont);
            mGrid.setCurrentAxis(dimAxis);
            mGrid.makeDraggable();
            flexTableAddRecord(mGrid);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.listeners.QueryListener#onQueryChange(com.google.gwt.user.client.ui.Widget)
     */
    public void onQueryChange(final Widget sender, final int sourceRow, final boolean isSourceRow, final IAxis sourceAxis,
            final IAxis targetAxis) {
        // If the drop axis equals the target axis add the widget.

        DeferredCommand.addCommand(new Command() {
            public void execute() {

                if (isAttached() && isVisible() && Pat.getCurrQuery().equals(query) && dimAxis.equals(targetAxis)) {
                    if (sender instanceof MeasureLabel) {
                        doMeasureStuff((MeasureLabel) sender);
                    } else if (sender instanceof MeasureGrid) {
                        doMeasureStuff((MeasureGrid) sender);
                    }

                    else {
                        flexTableAddRecord(sender);
                    }
                }
                // If the drop axis equals the source axis, remove the widget.
                else if (isAttached() && isVisible()
                        && (Pat.getCurrQuery().equals(query) && (dimAxis.equals(sourceAxis)))) {
                    if (sender instanceof MeasureLabel
                            && ((MeasureLabel) sender).getType().equals(MeasureLabel.LabelType.MEASURE)) {

                    } else {
                            flexTableRemoveRecord(sourceRow);
                        
                    }
                }

            }
        });
    }

    private void flexTableRemoveRecord(final int sourceRow) {
        if (horizontal) {
            dimensionTable.removeCell(0, sourceRow);
            if (dimensionTable.getCellCount(0) == 0) {
                dimensionTable = (DimensionFlexTable) TableUtil.insertSpacer(dimensionTable);
                empty = true;
            }
        } else {
            dimensionTable.removeRow(sourceRow);
            if (dimensionTable.getRowCount() == 0) {
                dimensionTable = (DimensionFlexTable) TableUtil.insertSpacer(dimensionTable);
                empty = true;
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.listeners.QueryListener#onQueryExecuted(java.lang.String,
     * org.pentaho.pat.rpc.dto.CellDataSet)
     */
    public void onQueryExecuted(final String queryId, final CellDataSet matrix) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUnload() {
        tblRowDragCont.unregisterDropController(fTblRowDropCont);
        GlobalConnectionFactory.getQueryInstance().removeQueryListener(DimensionDropWidget.this);
    }

    private void populateDimensionTable(final IAxis targetAxis) {

        ServiceFactory.getDiscoveryInstance().getDimensions(Pat.getSessionID(), Pat.getCurrQuery(), targetAxis,
                new AsyncCallback<String[]>() {

                    public void onFailure(final Throwable arg0) {
                        MessageBox.error(ConstantFactory.getInstance().error(), ConstantFactory.getInstance()
                                .dimensionFetchFail());
                    }

                    public void onSuccess(final String[] arg0) {
                        dimensionTable.clear();
                        // TODO GWT2.0 REmove All Rows
                        dimensionTable = (DimensionFlexTable) TableUtil.clearTableRows(dimensionTable);

                        if (arg0.length == 0) {
                            dimensionTable = (DimensionFlexTable) TableUtil.insertSpacer(dimensionTable);

                        } else {
                            ServiceFactory.getDiscoveryInstance().getMembers(Pat.getSessionID(), Pat.getCurrQuery(),
                                    "Measures", new AsyncCallback<StringTree>() { //$NON-NLS-1$

                                        public void onFailure(final Throwable arg0) {
                                        	// TODO Auto-generated method stub

                                        }

                                        public void onSuccess(final StringTree measuresTree) {
                                        	Arrays.sort(arg0);
                                            final int index = Arrays.binarySearch(arg0, "Measures"); //$NON-NLS-1$

                                            final List<String> dimensionList = Arrays.asList(arg0);

                                            MeasureGrid measureDropWidget = null;

                                            if (index > -1) {
                                                measureDropWidget = new MeasureGrid(query, dimAxis, horizontal);
                                                Pat.setMeasuresAxis(dimAxis);
                                                tblRowDragCont.makeDraggable(measureDropWidget);
                                                flexTableAddRecord(measureDropWidget, index);

                                                // Insert Measures
                                                for (int i = 0; i < measuresTree.getChildren().size(); i++) {
                                                    final MeasureLabel handle = new MeasureLabel(measuresTree
                                                            .getChildren().get(i).getValue(),
                                                            MeasureLabel.LabelType.MEASURE);
                                                    measureDropWidget.addRow(handle, i);
                                                    handle.setDragController(tblRowDragCont);
                                                    handle.makeDraggable();
                                                }
                                                measureDropWidget.setDragController(tblRowDragCont);
                                            }

                                            for (int row = 0; row < dimensionList.size(); row++) {
                                                if (row != index) {
                                                    MeasureLabel handle = new MeasureLabel(dimensionList.get(row),
                                                            MeasureLabel.LabelType.DIMENSION);

                                                    flexTableAddRecord(handle, row);

                                                    handle.setDragController(tblRowDragCont);
                                                    handle.makeDraggable();
                                                    if (row == dimensionList.size() - 1 /* && anotherList.size() - 1 > 0 */) {
                                                        dimensionTable.getCellFormatter().addStyleName(row, 0,
                                                                TABLE_DROP_ROWENDCELL);
                                                    }

                                                }
                                            }
                                            refreshTable();

                                        }
                                    });
                        }
                    }
                });
    }

    private void refreshTable() {

        WidgetHelper.revalidate(dimensionTable);

    }

    public void flexTableAddRecord(final Widget sender) {
        Widget wid = null;
        if (sender instanceof MeasureLabel) {
            wid = TableUtil.cloneMeasureLabel((MeasureLabel) sender);
        } else if (sender instanceof MeasureGrid) {
            wid = TableUtil.cloneMeasureGrid((MeasureGrid) sender, this.getHorizontal());
            ((MeasureGrid) wid).makeDraggable();
            ((MeasureGrid) wid).setCurrentAxis(dimensionTable.getAxis());
        } else {
            MessageBox.error(ConstantFactory.getInstance().error(), "Error Message");
        }

        if (empty) {
            dimensionTable = (DimensionFlexTable) TableUtil.clearTableRows(dimensionTable);
            dimensionTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
            flexTableAddRecord(wid, 0, horizontal);
            empty = false;
        } else {
            if (horizontal) {
                flexTableAddRecord(wid, dimensionTable.getCellCount(0), horizontal);
            } else {
                flexTableAddRecord(wid, dimensionTable.getRowCount(), horizontal);
            }
        }

        WidgetHelper.revalidate(dimensionTable);
    }

    private void flexTableAddRecord(Widget sender, int row) {
        // uncomment when widgets should be added horizontal instead of vertical
        // if (horizontal) {
        // dimensionTable.setWidget(0, row, sender);
        // }
        // else {
        dimensionTable.setWidget(row, 0, sender);

        dimensionTable.getCellFormatter().addStyleName(row, 0, "FlexTable-Cell"); //$NON-NLS-1$
        dimensionTable.getCellFormatter().setStyleName(row, 0, TABLE_DRAG_CELL);
        dimensionTable.getCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
        empty = false;
    }

    private void flexTableAddRecord(Widget sender, int row, boolean horizontal) {
        if (horizontal) {
            dimensionTable.setWidget(0, row, sender);
            dimensionTable.getCellFormatter().addStyleName(0, row, "FlexTable-Cell"); //$NON-NLS-1$
            dimensionTable.getCellFormatter().setStyleName(0, row, TABLE_DRAG_CELL);
            dimensionTable.getCellFormatter().setVerticalAlignment(0, row, HasVerticalAlignment.ALIGN_TOP);
        } else {
            dimensionTable.setWidget(row, 0, sender);
            dimensionTable.getCellFormatter().addStyleName(row, 0, "FlexTable-Cell"); //$NON-NLS-1$
            dimensionTable.getCellFormatter().setStyleName(row, 0, TABLE_DRAG_CELL);
            dimensionTable.getCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
        }

        empty = false;
    }

	public void onQueryPivoted(String queryId) {

		if (isAttached() && isVisible() && Pat.getCurrQuery().equals(query) && (dimAxis.equals(IAxis.COLUMNS) || dimAxis.equals(IAxis.ROWS))){
		populateDimensionTable(dimAxis);
	}
		
	}

}
