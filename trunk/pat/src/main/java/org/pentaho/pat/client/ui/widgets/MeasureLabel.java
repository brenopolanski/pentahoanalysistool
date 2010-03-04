package org.pentaho.pat.client.ui.widgets;

import org.pentaho.pat.client.util.dnd.FlexTableRowDragController;
import org.pentaho.pat.client.util.dnd.impl.FlexTableRowDragControllerImpl;

import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;

public class MeasureLabel extends FocusPanel {

    public enum LabelType {
        DIMENSION, MEASURE
    }

    private final static String TABLE_DRAG_WIDGET = "dragDimension"; //$NON-NLS-1$

    private FlexTableRowDragController dragController;

    private Label text = new Label();

    private LabelType type;

    public MeasureLabel(final String string, final LabelType lType) {
        super();
        text.setText(string);
        this.add(text);
        setStylePrimaryName(TABLE_DRAG_WIDGET);
        this.setType(lType);

    }

    /**
     *TODO JAVADOC
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
     *TODO JAVADOC
     * 
     * @return the dragController
     */
    public FlexTableRowDragController getDragController() {
        return dragController;
    }

    public String getText() {
        return text.getText();
    }

    public LabelType getType() {
        return type;
    }

    public void makeDraggable() {
        dragController.makeDraggable(MeasureLabel.this);
    }

    public void makeNotDraggable() {
        dragController.makeNotDraggable(MeasureLabel.this);
    }

    /**
     * 
     *TODO JAVADOC
     * 
     * @param dragController
     *            the dragController to set
     */
    public void setDragController(final FlexTableRowDragControllerImpl dragController) {
        this.dragController = dragController;
    }

    public void setType(final LabelType type) {
        this.type = type;
    }
}