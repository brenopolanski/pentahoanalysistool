package org.pentaho.pat.client.util;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.BoundaryDropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

// TODO: Auto-generated Javadoc
/**
 * TODO JAVADOC.
 * 
 * @author bugg
 */
public class FlexTableRowDragController extends PickupDragController {

	/** TODO JAVADOC. */
	private static final String CSS_DEMO_FLEX_TABLE_ROW_EXAMPLE_TABLE_PROXY = "demo-FlexTableRowExample-table-proxy"; //$NON-NLS-1$

	/** TODO JAVADOC. */
	private FlexTable draggableTable;

	/** TODO JAVADOC. */
	private int dragRow;

	/**
	 * TODO JAVADOC.
	 * 
	 * @param boundaryPanel the boundary panel
	 */
	public FlexTableRowDragController(final AbsolutePanel boundaryPanel) {
		super(boundaryPanel, false);
		setBehaviorDragProxy(true);
		setBehaviorMultipleSelection(false);
	}

	/* (non-Javadoc)
	 * @see com.allen_sauer.gwt.dnd.client.PickupDragController#dragEnd()
	 */
	@Override
	public void dragEnd() {
		super.dragEnd();

		// cleanup
		draggableTable = null;
	}

	/**
	 * TODO JAVADOC.
	 * 
	 * @return the draggable table
	 */
	FlexTable getDraggableTable() {
		return draggableTable;
	}

	/**
	 * TODO JAVADOC.
	 * 
	 * @return the drag row
	 */
	int getDragRow() {
		return dragRow;
	}

	/**
	 * TODO JAVADOC.
	 * 
	 * @param widget the widget
	 * @param table the table
	 * 
	 * @return the widget row
	 */
	private int getWidgetRow(final Widget widget, final FlexTable table) {
		for (int row = 0; row < table.getRowCount(); row++) {
			for (int col = 0; col < table.getCellCount(row); col++) {
				final Widget w = table.getWidget(row, col);
				if (w == widget) {
					return row;
				}
			}
		}
		throw new RuntimeException("Unable to determine widget row"); //$NON-NLS-1$ // NOPMD by bugg on 20/04/09 20:17
	}

	/* (non-Javadoc)
	 * @see com.allen_sauer.gwt.dnd.client.PickupDragController#newBoundaryDropController(com.google.gwt.user.client.ui.AbsolutePanel, boolean)
	 */
	@Override
	protected BoundaryDropController newBoundaryDropController(final AbsolutePanel boundaryPanel, final boolean allowDroppingOnBoundaryPanel) {
		if (allowDroppingOnBoundaryPanel) {
			throw new IllegalArgumentException();
		}
		return super.newBoundaryDropController(boundaryPanel, allowDroppingOnBoundaryPanel);
	}

	/* (non-Javadoc)
	 * @see com.allen_sauer.gwt.dnd.client.PickupDragController#newDragProxy(com.allen_sauer.gwt.dnd.client.DragContext)
	 */
	@Override
	protected Widget newDragProxy(final DragContext context) {
		FlexTable proxy;
		proxy = new FlexTable();
		proxy.addStyleName(CSS_DEMO_FLEX_TABLE_ROW_EXAMPLE_TABLE_PROXY);
		draggableTable = (FlexTable) context.draggable.getParent();
		dragRow = getWidgetRow(context.draggable, draggableTable);
		FlexTableUtil.copyRow(draggableTable, proxy, dragRow, 0);
		return proxy;
	}

	/* (non-Javadoc)
	 * @see com.allen_sauer.gwt.dnd.client.PickupDragController#setBehaviorDragProxy(boolean)
	 */
	@Override
	public void setBehaviorDragProxy(final boolean dragProxyEnabled) {
		if (!dragProxyEnabled) {
			// TODO implement drag proxy behavior
			throw new IllegalArgumentException();
		}
		super.setBehaviorDragProxy(dragProxyEnabled);
	}

}
