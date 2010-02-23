package org.pentaho.pat.client.util.dnd;

import org.pentaho.pat.client.ui.widgets.DimensionFlexTable;

import com.google.gwt.user.client.ui.Widget;

public interface FlexTableRowDragController {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.allen_sauer.gwt.dnd.client.PickupDragController#dragEnd()
	 */
	/**
	 * Fire on drag end.
	 */
	public abstract void dragEnd();

	/**
	 * get the source table.
	 * 
	 * @return the draggable table
	 */
	public abstract DimensionFlexTable getDraggableTable();

	/**
	 * Get the source row.
	 * 
	 * @return the drag row
	 */
	public abstract int getDragRow();

	/**
	 * Get the source column.
	 * 
	 * @return the drag row
	 */
	public abstract int getDragCol();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.allen_sauer.gwt.dnd.client.PickupDragController#setBehaviorDragProxy(boolean)
	 */
	/**
	 * The proxy drag behaviour
	 * 
	 * @param dragProxyEnabled
	 *            boolean indicator
	 */
	public abstract void setBehaviorDragProxy(final boolean dragProxyEnabled);

	public abstract void makeDraggable(Widget widget);

	public abstract void makeNotDraggable(Widget widget);

}