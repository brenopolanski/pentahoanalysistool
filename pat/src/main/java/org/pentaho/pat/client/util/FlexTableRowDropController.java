package org.pentaho.pat.client.util;

import org.pentaho.pat.rpc.beans.Axis;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbstractPositioningDropController;
import com.allen_sauer.gwt.dnd.client.util.CoordinateLocation;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.Location;
import com.allen_sauer.gwt.dnd.client.util.LocationWidgetComparator;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class FlexTableRowDropController extends AbstractPositioningDropController {

	/**
	 *TODO JAVADOC
	 */
	private static final String CSS_DEMO_TABLE_POSITIONER = "demo-table-positioner"; //$NON-NLS-1$

	/**
	 *TODO JAVADOC
	 */
	private FlexTable flexTable;

	/**
	 *TODO JAVADOC
	 */
	private IndexedPanel flexTableRowsAsIndexPanel = new IndexedPanel() {

		public Widget getWidget(int index) {
			return flexTable.getWidget(index, 0);
		}

		public int getWidgetCount() {
			return flexTable.getRowCount();
		}

		public int getWidgetIndex(Widget child) {
			throw new UnsupportedOperationException();
		}

		public boolean remove(int index) {
			throw new UnsupportedOperationException();
		}
	};

	/**
	 *TODO JAVADOC
	 */
	private Widget positioner = null;

	/**
	 *TODO JAVADOC
	 */
	private int targetRow;

	/**
	 *TODO JAVADOC
	 */
	private org.pentaho.pat.rpc.beans.Axis targetAxis;

	/**
	 *TODO JAVADOC
	 *
	 * @param flexTable
	 */
	public FlexTableRowDropController(FlexTable flexTable) {
		super(flexTable);
		this.flexTable = flexTable;
	}

	/**
	 *TODO JAVADOC
	 *
	 * @param flexTable
	 * @param rows
	 */
	public FlexTableRowDropController(FlexTable flexTable, Axis rows) {
		super(flexTable);
		this.flexTable = flexTable;
		targetAxis = rows;
	}

	/* (non-Javadoc)
	 * @see com.allen_sauer.gwt.dnd.client.drop.AbstractDropController#onDrop(com.allen_sauer.gwt.dnd.client.DragContext)
	 */
	@Override
	public void onDrop(DragContext context) {
		FlexTableRowDragController trDragController = (FlexTableRowDragController) context.dragController;
		FlexTableUtil.moveRow(trDragController.getDraggableTable(), flexTable, trDragController.getDragRow(), targetRow + 1, targetAxis);
		super.onDrop(context);
	}

	/* (non-Javadoc)
	 * @see com.allen_sauer.gwt.dnd.client.drop.AbstractDropController#onEnter(com.allen_sauer.gwt.dnd.client.DragContext)
	 */
	@Override
	public void onEnter(DragContext context) {
		super.onEnter(context);
		positioner = newPositioner(context);
	}

	/* (non-Javadoc)
	 * @see com.allen_sauer.gwt.dnd.client.drop.AbstractDropController#onLeave(com.allen_sauer.gwt.dnd.client.DragContext)
	 */
	@Override
	public void onLeave(DragContext context) {
		positioner.removeFromParent();
		positioner = null;
		super.onLeave(context);
	}

	/* (non-Javadoc)
	 * @see com.allen_sauer.gwt.dnd.client.drop.AbstractDropController#onMove(com.allen_sauer.gwt.dnd.client.DragContext)
	 */
	@Override
	public void onMove(DragContext context) {
		super.onMove(context);
		targetRow = DOMUtil.findIntersect(flexTableRowsAsIndexPanel, new CoordinateLocation(context.mouseX, context.mouseY),
				LocationWidgetComparator.BOTTOM_HALF_COMPARATOR) - 1;

		Widget w = flexTable.getWidget(targetRow == -1 ? 0 : targetRow, 0);
		Location widgetLocation = new WidgetLocation(w, context.boundaryPanel);
		Location tableLocation = new WidgetLocation(flexTable, context.boundaryPanel);
		context.boundaryPanel.add(positioner, tableLocation.getLeft(), widgetLocation.getTop() + (targetRow == -1 ? 0 : w.getOffsetHeight()));
	}

	/**
	 *TODO JAVADOC
	 *
	 * @param context
	 * @return
	 */
	Widget newPositioner(DragContext context) {
		Widget p = new SimplePanel();
		p.addStyleName(CSS_DEMO_TABLE_POSITIONER);
		p.setPixelSize(flexTable.getOffsetWidth(), 1);
		return p;
	}

}
