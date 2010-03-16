/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.ui.widgets;

import java.util.ArrayList;
import java.util.List;

import org.gwt.mosaic.ui.client.util.WidgetHelper;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.IQueryListener;
import org.pentaho.pat.client.util.TableUtil;
import org.pentaho.pat.client.util.dnd.FlexTableRowDragController;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.IAxis;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Measure Grid, contains measure widgets for query creation.
 * 
 * @author tom(at)wamonline.org.uk
 * 
 */
public class MeasureGrid extends FocusPanel implements IQueryListener {

    private FlexTableRowDragController dragController;

    private final DimensionFlexTable grid;

    private Boolean empty = true;

    private IAxis currentAxis;

    private String query;

    private final static String MEASURE_GRID = "pat-MeasureGrid"; //$NON-NLS-1$

    private Boolean horizontal;

    /**
     * Creates the measure grid.
     * @param query
     * @param currentAxis
     * @param horizontal
     */
    public MeasureGrid(final String query, final IAxis currentAxis, final Boolean horizontal) {
        super();
        grid = new DimensionFlexTable(horizontal, currentAxis);
        this.currentAxis = currentAxis;
        this.horizontal = horizontal;
        final CaptionPanel panel = new CaptionPanel(ConstantFactory.getInstance().measures());
        this.setStyleName(MEASURE_GRID);
        panel.add(grid);
        this.add(panel);
        this.query = query;
    }

    /**
     * Add a row.
     * @param mLabel
     */
    public void addRow(final MeasureLabel mLabel) {
        if (horizontal) {
            if (empty && grid.getRowCount()>0 && grid.getCellCount(0) > 0)
                TableUtil.removeSpacer(grid);
            if(grid.getRowCount()>0){
            grid.setWidget(0, grid.getCellCount(0), mLabel);
            }
            else{
                grid.setWidget(0, 0, mLabel);    
            }
        } else {
            if (empty && grid.getRowCount() > 0)
                TableUtil.removeSpacer(grid);
            grid.setWidget(grid.getRowCount(), 0, mLabel);
        }
    }

    /**
     * Add a row.
     * @param mLabel
     * @param row
     */
    public void addRow(final MeasureLabel mLabel, final int row) {
        if (horizontal) {
            if (empty && grid.getRowCount() > 0) {
                TableUtil.removeSpacer(grid);

            }
            empty = false;
            grid.setWidget(0, row, mLabel);

        } else {
            if (empty && grid.getRowCount() > 0) {
                TableUtil.removeSpacer(grid);
                empty = false;
            }
            grid.setWidget(row, 0, mLabel);
        }

    }

    /**
     * return the current axis.
     * 
     * @return the currentAxis
     */
    public IAxis getCurrentAxis() {
        return currentAxis;
    }

    /**
     * return the dragController.
     * 
     * @return the dragController
     */
    public FlexTableRowDragController getDragController() {
        return dragController;
    }

    /**
     * Return true if the container is empty.
     * 
     * @return the empty
     */
    public Boolean getEmpty() {
        return empty;
    }

    /**
     * Return a list of Measure Labels.
     * @return
     */
    public List<MeasureLabel> getMeasureLabels() {
        final List<MeasureLabel> measureLabels = new ArrayList<MeasureLabel>();
        if (horizontal) {
            for (int i = 0; i < grid.getCellCount(0); i++)
                if (grid.getWidget(0, i) instanceof MeasureLabel)
                    measureLabels.add((MeasureLabel) grid.getWidget(0, i));
        } else {
            for (int i = 0; i < grid.getRowCount(); i++)
                if (grid.getWidget(i, 0) instanceof MeasureLabel)
                    measureLabels.add((MeasureLabel) grid.getWidget(i, 0));
        }
        return measureLabels;
    }

    /**
     * Return the query id.
     * 
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    /**
     * Return the flextable.
     * @return
     */
    public DimensionFlexTable getRows() {
        return grid;
    }

    /**
     * Make this widget draggable.
     */
    public void makeDraggable() {
        dragController.makeDraggable(MeasureGrid.this);
    }

    /**
     * Make this widget non draggable.
     */
    public void makeNotDraggable() {
        dragController.makeNotDraggable(MeasureGrid.this);
    }

    @Override
    public void onLoad() {
        GlobalConnectionFactory.getQueryInstance().addQueryListener(MeasureGrid.this);
    }

    @Override
    public void onUnload() {
        GlobalConnectionFactory.getQueryInstance().removeQueryListener(MeasureGrid.this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.listeners.IQueryListener#onQueryChange(com.google.gwt.user.client.ui.Widget, int,
     * org.pentaho.pat.rpc.dto.IAxis, org.pentaho.pat.rpc.dto.IAxis)
     */
    public void onQueryChange(final Widget sender, final int sourceRow, final boolean isSourceRow, final IAxis sourceAxis,
            final IAxis targetAxis) {

        DeferredCommand.addCommand(new Command() {
            public void execute() {

                if (isAttached() && isVisible() && Pat.getCurrQuery().equals(query) && currentAxis.equals(sourceAxis)
                        && sender instanceof MeasureLabel
                        && ((MeasureLabel) sender).getType().equals(MeasureLabel.LabelType.MEASURE)) {
                    if (horizontal) {
                        removeRow(sourceRow);
                    } else {
                        grid.removeRow(sourceRow);

                        int rowcount = 0;
                        for (int i = 0; i < grid.getRowCount(); i++) {
                            if (grid.getWidget(i, 0) != null) {
                                rowcount++;
                                break;
                            }
                        }
                        if (rowcount == 0) {
                            MeasureGrid.this.removeFromParent();
                        }
                    }
                    WidgetHelper.revalidate(MeasureGrid.this);
                }

                else if (isAttached() && isVisible() && Pat.getCurrQuery().equals(query)
                        && currentAxis.equals(targetAxis) && sender instanceof MeasureLabel
                        && ((MeasureLabel) sender).getType().equals(MeasureLabel.LabelType.MEASURE) && Pat.getMeasuresAxis().equals(getCurrentAxis())) {
                    MeasureLabel measureLabel = TableUtil.cloneMeasureLabel((MeasureLabel) sender);
                    addRow(measureLabel);

                    WidgetHelper.revalidate(MeasureGrid.this);
                } else if (isAttached() && isVisible() && Pat.getCurrQuery().equals(query)
                        && currentAxis.equals(targetAxis) && sender instanceof MeasureGrid) {
                    MeasureGrid mGrid = TableUtil.cloneMeasureGrid((MeasureGrid) sender);
                    if(mGrid.getHorizontal()){
                        for (int i = 0; i < mGrid.getRows().getCellCount(0); i++) {
                            MeasureLabel measureLabel = ((MeasureLabel) mGrid.getRows().getWidget(0, i));
                            addRow(measureLabel);

                        }
                    	
                    }
                    else{
                    for (int i = 0; i < mGrid.getRows().getRowCount(); i++) {
                        MeasureLabel measureLabel = ((MeasureLabel) mGrid.getRows().getWidget(i, 0));
                        addRow(measureLabel);

                    }
                    }
                    WidgetHelper.revalidate(MeasureGrid.this);
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.listeners.IQueryListener#onQueryExecuted(java.lang.String,
     * org.pentaho.pat.rpc.dto.CellDataSet)
     */
    public void onQueryExecuted(final String queryId, final CellDataSet matrix) {
        // TODO Auto-generated method stub

    }

    /**
     * Remove a row.
     * @param row
     */
    public void removeRow(final int row) {
        if (horizontal) {
            if (grid.getCellCount(0) > 1) {
                grid.removeCell(0, row);
            }

            else {
                MeasureGrid.this.removeFromParent();
            }
        } else {
            if (grid.getRowCount() > 1) {
                grid.removeRow(row);
            }

            else {
                MeasureGrid.this.removeFromParent();
            }
        }
    }

    /**
     * 
     * Set the widgets current axis.
     * 
     * @param currentAxis
     *            the currentAxis to set
     */
    public void setCurrentAxis(final IAxis currentAxis) {
        this.currentAxis = currentAxis;
        grid.setAxis(currentAxis);
    }

    /**
     * 
     * Set the drag controller for this widget.
     * 
     * @param dragController
     *            the dragController to set
     */
    public void setDragController(final FlexTableRowDragController dragController) {
        this.dragController = dragController;
    }

    /**
     * 
     * Set the widgets empty status.
     * 
     * @param empty
     *            the empty to set
     */
    public void setEmpty(final Boolean empty) {
        this.empty = empty;
    }

    /**
     * 
     * Set the query id.
     * 
     * @param query
     *            the query to set
     */
    public void setQuery(final String query) {
        this.query = query;
    }

    /**
     * 
     * Set the orientation of the widget.
     * 
     * @param horizontal
     *            the horizontal to set
     */
    public void setHorizontal(Boolean horizontal) {
        this.horizontal = horizontal;
    }

    /**
     * Return the orientation of this widget.
     * 
     * @return the horizontal
     */
    public Boolean getHorizontal() {
        return horizontal;
    }

	public void onQueryPivoted(String queryId) {
		// TODO Auto-generated method stub
		
	}
}
