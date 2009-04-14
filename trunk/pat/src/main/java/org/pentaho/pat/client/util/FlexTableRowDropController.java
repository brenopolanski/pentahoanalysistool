package org.pentaho.pat.client.util;

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
import org.pentaho.pat.rpc.beans.Axis;

public class FlexTableRowDropController extends AbstractPositioningDropController {

	  private static final String CSS_DEMO_TABLE_POSITIONER = "demo-table-positioner";

	  private FlexTable flexTable;

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

	  private Widget positioner = null;

	  private int targetRow;

	  private org.pentaho.pat.rpc.beans.Axis targetAxis;
	  
	  public FlexTableRowDropController(FlexTable flexTable) {
	    super(flexTable);
	    this.flexTable = flexTable;
	  }

	  public FlexTableRowDropController(FlexTable flexTable, Axis rows) {
		  super(flexTable);
		    this.flexTable = flexTable;
		targetAxis = rows;    
	}

	@Override
	  public void onDrop(DragContext context) {
	    FlexTableRowDragController trDragController = (FlexTableRowDragController) context.dragController;
	    FlexTableUtil.moveRow(trDragController.getDraggableTable(), flexTable,
	        trDragController.getDragRow(), targetRow + 1, targetAxis);
	    super.onDrop(context);
	  }

	  @Override
	  public void onEnter(DragContext context) {
	    super.onEnter(context);
	    positioner = newPositioner(context);
	  }

	  @Override
	  public void onLeave(DragContext context) {
	    positioner.removeFromParent();
	    positioner = null;
	    super.onLeave(context);
	  }

	  @Override
	  public void onMove(DragContext context) {
	    super.onMove(context);
	    targetRow = DOMUtil.findIntersect(flexTableRowsAsIndexPanel, new CoordinateLocation(
	        context.mouseX, context.mouseY), LocationWidgetComparator.BOTTOM_HALF_COMPARATOR) - 1;

	    Widget w = flexTable.getWidget(targetRow == -1 ? 0 : targetRow, 0);
	    Location widgetLocation = new WidgetLocation(w, context.boundaryPanel);
	    Location tableLocation = new WidgetLocation(flexTable, context.boundaryPanel);
	    context.boundaryPanel.add(positioner, tableLocation.getLeft(), widgetLocation.getTop()
	        + (targetRow == -1 ? 0 : w.getOffsetHeight()));
	  }

	  Widget newPositioner(DragContext context) {
	    Widget p = new SimplePanel();
	    p.addStyleName(CSS_DEMO_TABLE_POSITIONER);
	    p.setPixelSize(flexTable.getOffsetWidth(), 1);
	    return p;
	  }

}
