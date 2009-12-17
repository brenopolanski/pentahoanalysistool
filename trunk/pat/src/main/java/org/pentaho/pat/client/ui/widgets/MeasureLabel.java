package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.core.client.DOM;

import com.google.gwt.user.client.ui.Label;

public class MeasureLabel extends Label {

    private final static String TABLE_DRAG_WIDGET = "dragDimension"; //$NON-NLS-1$

    public enum labelType {
	DIMENSION,
	MEASURE
    }
    private labelType type;
    public MeasureLabel(String string, labelType lt){
	super(string);
	setStylePrimaryName(TABLE_DRAG_WIDGET);
	this.setType(lt);
	if (lt == labelType.DIMENSION){
	    
	}
	else if(lt == labelType.MEASURE){
	    DOM.setStyleAttribute(this.getElement(), "background", "red");
	}
    }
    public void setType(labelType type) {
	this.type = type;
    }
    public labelType getType() {
	return type;
    }
    
    
}
