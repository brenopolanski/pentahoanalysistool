package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.core.client.DOM;
import org.pentaho.pat.client.util.dnd.FlexTableRowDragController;

import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;

public class MeasureLabel extends FocusPanel {

    private final static String TABLE_DRAG_WIDGET = "dragDimension"; //$NON-NLS-1$

    FlexTableRowDragController dragController;
    Label text = new Label();
    public enum labelType {
	DIMENSION,
	MEASURE
    }
    private labelType type;
    public MeasureLabel(String string, labelType lt){
	super();
	text.setText(string);
	this.add(text);
	setStylePrimaryName(TABLE_DRAG_WIDGET);
	this.setType(lt);
	if (lt == labelType.DIMENSION){
	    
	}
	else if(lt == labelType.MEASURE){
	    DOM.setStyleAttribute(this.getElement(), "background", "red");
	}
    }
    /**
     *TODO JAVADOC
     *
     * @param text2
     * @param type2
     * @param dragController2
     */
    public MeasureLabel(String string, labelType lt, FlexTableRowDragController dragController2, boolean draggable) {
        super();
        text.setText(string);
        this.add(text);
        setStylePrimaryName(TABLE_DRAG_WIDGET);
        this.setType(lt);
        if (lt == labelType.DIMENSION){
            
        }
        else if(lt == labelType.MEASURE){
            DOM.setStyleAttribute(this.getElement(), "background", "red");
        }
        this.dragController=dragController2;
        
        if (draggable=true){
            this.makeDraggable();
        }
    }
    public void setType(labelType type) {
	this.type = type;
    }
    public labelType getType() {
	return type;
    }
    

    /**
     *TODO JAVADOC
     * @return the dragController
     */
    public FlexTableRowDragController getDragController() {
        return dragController;
    }

    /**
     *
     *TODO JAVADOC
     * @param dragController the dragController to set
     */
    public void setDragController(FlexTableRowDragController dragController) {
        this.dragController = dragController;
    }
    
    public void makeDraggable(){
        dragController.makeDraggable(MeasureLabel.this);
    }
    
    public void makeNotDraggable(){
        dragController.makeNotDraggable(MeasureLabel.this);
    }
    
    public String getText(){
        return text.getText();
    }
}
