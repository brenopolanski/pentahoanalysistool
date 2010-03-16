/**
 * 
 */
package org.pentaho.pat.client.ui.widgets;

import org.pentaho.pat.client.util.dnd.FlexTableRowDragController;
import org.pentaho.pat.client.util.dnd.impl.FlexTableRowDragControllerImpl;

import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * The measure label widget.
 * @author tom(at)wamonline.org.uk
 *
 */
public class MeasureLabel extends FocusPanel {

    public enum LabelType {
        DIMENSION, MEASURE
    }

    private final static String TABLE_DRAG_WIDGET = "dragDimension"; //$NON-NLS-1$

    private FlexTableRowDragController dragController;

    private Label text = new Label();

    private LabelType type;

    /**
     * Create a measure label.
     * @param string
     * @param lType
     */
    public MeasureLabel(final String string, final LabelType lType) {
        super();
        text.setText(string);
        this.add(text);
        setStylePrimaryName(TABLE_DRAG_WIDGET);
        this.setType(lType);

    }

    /**
     * Create a measure label.
     * 
     * @param text2
     * @param type2
     * @param dragController2
     */
    public MeasureLabel(final String string, final LabelType lType, final FlexTableRowDragController dragController2,
            boolean draggable) {
        this(string, lType);
        this.dragController = dragController2;

        if (draggable == true) {
            this.makeDraggable();
        }
    }

    /**
     * Return the drag controller.
     * 
     * @return the dragController
     */
    public FlexTableRowDragController getDragController() {
        return dragController;
    }

    /**
     * Return the labels text.
     * @return
     */
    public String getText() {
        return text.getText();
    }

    /**
     * Return the labels type.
     * @return
     */
    public LabelType getType() {
        return type;
    }

    /**
     * Make the widget draggable.
     */
    public void makeDraggable() {
        dragController.makeDraggable(MeasureLabel.this);
    }

    /**
     * Make the widget non draggable.
     */
    public void makeNotDraggable() {
        dragController.makeNotDraggable(MeasureLabel.this);
    }

    /**
     * 
     * Set the drag contoller.
     * 
     * @param dragController
     *            the dragController to set
     */
    public void setDragController(final FlexTableRowDragControllerImpl dragController) {
        this.dragController = dragController;
    }

    /**
     * Set the type.
     * @param type
     */
    public void setType(final LabelType type) {
        this.type = type;
    }
}
