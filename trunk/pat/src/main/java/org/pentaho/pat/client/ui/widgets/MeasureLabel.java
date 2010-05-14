/**
 * 
 */
package org.pentaho.pat.client.ui.widgets;

import java.util.List;

import org.pentaho.pat.client.ui.popups.MeasureLabelSelectionModeMenu;
import org.pentaho.pat.client.ui.popups.SelectionModeMenu;
import org.pentaho.pat.client.util.dnd.impl.SimplePanelDragControllerImpl;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

/**
 * The measure label widget.
 * @author tom(at)wamonline.org.uk
 *
 */
public class MeasureLabel extends FocusPanel {

    public enum LabelType {
        DIMENSION, MEASURE, ALLMEMBER, HIERARCHY, LEVEL, MEMBER
    }

    private final static String TABLE_DRAG_WIDGET = "dragDimension"; //$NON-NLS-1$

    private SimplePanelDragControllerImpl dragController;

    private Label text = new Label();
    
    private List value;

    private LabelType type;

	private String actualname;

    /**
     * Create a measure label (with no predefined caption).
     * @param valure
     * @param lType
     */
    public MeasureLabel(final List<String> value, final LabelType lType) {
        super();
        text.setText(value.get(0));
        this.add(text);
        setStylePrimaryName(TABLE_DRAG_WIDGET);
        this.setType(lType);
        this.setValue(value);
    }
    
    /**
     * Create a measure label with a separate caption.
     * @param value
     * @param caption
     * @param lType
     */
    public MeasureLabel(final List value, final String caption, final LabelType lType) {
        super();
        
        text.setText(caption);
        this.add(text);
        setStylePrimaryName(TABLE_DRAG_WIDGET);
        this.setType(lType);
        this.setValue(value);
    }

    public MeasureLabel(final List parents, final String name, final String caption, final LabelType lType) {
        super();
        text.setText(caption);
        this.add(text);
        this.setActualName(name);
        setStylePrimaryName(TABLE_DRAG_WIDGET);
        this.setType(lType);
        this.setValue(parents);
    }
    public void setActualName(String name) {
		actualname = name;
		
	}
    
    public String getActualName(){
    	return actualname;
    }

	/**
     * Create a measure label.
     * 
     * @param text2
     * @param type2
     * @param dragController2
     */
    public MeasureLabel(final List string, final String caption, final LabelType lType, final SimplePanelDragControllerImpl dragController2,
            boolean draggable) {
        this(string, caption, lType);
        this.dragController = dragController2;

        if (draggable == true) {
            this.makeDraggable();
        }
    }

    public void enableSinkEvents(){
    	this.sinkEvents(NativeEvent.BUTTON_LEFT | NativeEvent.BUTTON_RIGHT | Event.ONCONTEXTMENU);
    }
    
    public void disableSinkEvents(){
    	this.unsinkEvents(NativeEvent.BUTTON_LEFT | NativeEvent.BUTTON_RIGHT | Event.ONCONTEXTMENU);
    }
    
    public MeasureLabel() {
		// TODO Auto-generated constructor stub
	}

	/**
     * Return the drag controller.
     * 
     * @return the dragController
     */
    public SimplePanelDragControllerImpl getDragController() {
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
    public void setDragController(final SimplePanelDragControllerImpl dragController) {
        this.dragController = dragController;
    }

    /**
     * Set the type.
     * @param type
     */
    public void setType(final LabelType type) {
        this.type = type;
    }

	public void setValue(List value) {
		this.value = value;
	}

	public List<String> getValue() {
		return value;
	}
	
	
	@Override
    public void onBrowserEvent(final Event event) {
        super.onBrowserEvent(event);
        switch (DOM.eventGetType(event)) {
        case Event.ONCONTEXTMENU:
        	final MeasureLabelSelectionModeMenu selectionMenu = new MeasureLabelSelectionModeMenu();
            // test.showContextMenu(event, getSelectedItem().getText(), getSelectedItem().getTree());
            selectionMenu.showContextMenu(event, this);
            selectionMenu.setPopupPositionAndShow(new PositionCallback() {
                public void setPosition(final int offsetWidth, final int offsetHeight) {
                    selectionMenu.setPopupPosition(event.getClientX(), event.getClientY());
                }
            });	
        case Event.ONCLICK:
        	break;
        default:
            break;
        }
    }
}
