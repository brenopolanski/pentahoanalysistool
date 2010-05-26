/**
 * 
 */
package org.pentaho.pat.client.ui.widgets;

import java.util.List;

import org.pentaho.pat.client.listeners.ILabelListener;
import org.pentaho.pat.client.ui.popups.MeasureLabelSelectionModeMenu;
import org.pentaho.pat.client.util.dnd.impl.SimplePanelDragControllerImpl;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.rpc.dto.IAxis;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.gen2.complexpanel.client.FastTreeItem;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

/**
 * The measure label widget.
 * @author tom(at)wamonline.org.uk
 *
 */
public class MeasureLabel extends FocusPanel implements ILabelListener{

    public enum LabelType {
        DIMENSION, MEASURE, ALLMEMBER, HIERARCHY, LEVEL, MEMBER
    }

    private boolean isUniqueName;
    private final static String TABLE_DRAG_WIDGET = "dragDimension"; //$NON-NLS-1$

    private SimplePanelDragControllerImpl dragController;

    private Label text = new Label();
    
    private List<String> value;

    private LabelType type;

	private String actualname;

	private String caption;
	
	private IAxis axis;

	private boolean draggable;

	private List<String> currentSelection;

	private String selectionType;

	private FastTreeItem parentNode;

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
    
    public MeasureLabel(final List<String> parents, final String name, 
    		final String caption, final LabelType lType, FastTreeItem parentNode, boolean isuniquename) {
        super();
        this.setParentNode(parentNode);
        if(isuniquename){
        	text.setText(name);
        }else{
        	text.setText(caption);
        }
        this.setIsUniqueName(isuniquename);
        this.add(text);
        this.setActualName(name);
        this.setCaption(caption);
        setStylePrimaryName(TABLE_DRAG_WIDGET);
        this.setType(lType);
        this.setValue(parents);
        GlobalConnectionFactory.getLabelInstance().addLabelListener(MeasureLabel.this);
    }
    public void setIsUniqueName(boolean isuniquename) {
		this.isUniqueName= isuniquename;
		
	}
    
    public boolean isUniqueName(){
    	return isUniqueName;
    }

    public void setActualName(String name) {
		actualname = name;
		
	}
    
    public String getActualName(){
    	return actualname;
    }

    public void setCaption(String name) {
		caption = name;
		
	}
    
    public String getCaption(){
    	return caption;
    }

	/**
     * Create a measure label.
     * 
     * @param text2
     * @param type2
     * @param dragController2
     */
    /*public MeasureLabel(final List<String> string, final String caption, final LabelType lType, final SimplePanelDragControllerImpl dragController2,
            boolean draggable) {
        this(string, null, caption, lType);
        this.dragController = dragController2;

        if (draggable == true) {
            this.makeDraggable();
        }
    }*/

    public void enableSinkEvents(){
    	this.sinkEvents(NativeEvent.BUTTON_LEFT | NativeEvent.BUTTON_RIGHT | Event.ONCONTEXTMENU);
    }
    
    public void disableSinkEvents(){
    	this.unsinkEvents(NativeEvent.BUTTON_LEFT | NativeEvent.BUTTON_RIGHT | Event.ONCONTEXTMENU);
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
    	if(draggable==false){
    	this.draggable=true;
    	
        dragController.makeDraggable(MeasureLabel.this);
    	}
    }

    /**
     * Make the widget non draggable.
     */
    public void makeNotDraggable() {
    	if(draggable==true){
    	this.draggable=false;
        dragController.makeNotDraggable(MeasureLabel.this);
    	}
    }

    public boolean isDraggable(){
    	return draggable;
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

	public void setValue(List<String> value) {
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
        	final MeasureLabelSelectionModeMenu selectionMenu = new MeasureLabelSelectionModeMenu(this.getType());
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

	public void setAxis(IAxis axis) {
		this.axis=axis;
		
	}
	
	public IAxis getAxis(){
		return axis;
	}

	public void setCurrentSelection(List<String> arg0) {
		this.currentSelection=arg0;
		
	}

	public void setSelectionType(String string) {
		this.selectionType=string;
		
	}
	
	public List<String> getCurrentSelection(){
		return this.currentSelection;
	}
	
	public String getSelectionType(){
		return this.selectionType;
	}

	public void setParentNode(FastTreeItem parentNode) {
		this.parentNode = parentNode;
	}

	public FastTreeItem getParentNode() {
		return parentNode;
	}

	public void onUniqueNameChange(Widget sender, boolean uniqueName) {
		if(uniqueName){
			text.setText(actualname);
		}
		else{
			text.setText(caption);
		}
		this.setIsUniqueName(uniqueName);
		
	}
}
