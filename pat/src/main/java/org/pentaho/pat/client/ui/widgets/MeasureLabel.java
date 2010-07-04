/*
 * Copyright (C) 2009 Tom Barber
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *
 */
package org.pentaho.pat.client.ui.widgets;

import java.util.List;

import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.ILabelListener;
import org.pentaho.pat.client.ui.popups.MeasureLabelSelectionModeMenu;
import org.pentaho.pat.client.util.dnd.impl.SimplePanelDragControllerImpl;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.rpc.dto.IAxis;
import org.pentaho.pat.rpc.dto.enums.ObjectType;
import org.pentaho.pat.rpc.dto.enums.SelectionType;

import com.google.gwt.gen2.complexpanel.client.FastTreeItem;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

/**
 * The measure label widget.
 * @author tom(at)wamonline.org.uk
 *
 */
@SuppressWarnings("deprecation")
public class MeasureLabel extends FocusPanel implements ILabelListener{

    private boolean isUniqueName;
    private final static String TABLE_DRAG_WIDGET = "dragDimension"; //$NON-NLS-1$

    private SimplePanelDragControllerImpl dragController;

    private Label text = new Label();

    private ObjectType type;

	private String actualname;

	private String caption;
	
	private IAxis axis;

	private boolean draggable;

	private List<String> currentSelection;

	private SelectionType selectionType;

	private FastTreeItem parentNode;

	private Image image;
    public MeasureLabel(final String uniquename, 
    		final String caption, final ObjectType lType, FastTreeItem parentNode, boolean isuniquename) {
        super();
        this.setParentNode(parentNode);
        if(isuniquename){
        	text.setText(uniquename);
        }else{
        	text.setText(caption);
        }
        this.setIsUniqueName(isuniquename);
        HorizontalPanel container = new HorizontalPanel();
        container.add(text);
        final MeasureLabelSelectionModeMenu selectionMenu = new MeasureLabelSelectionModeMenu(this.getType());

         image = Pat.IMAGES.downbutton().createImage();
         image.addClickListener(new ClickListener() {
			
			public void onClick(Widget sender) {
				selectionMenu.showContextMenu(MeasureLabel.this);
				 final int left = sender.getAbsoluteLeft() + 10;
			        final int top = sender.getAbsoluteTop() + 10;
			        
			        selectionMenu.setPopupPositionAndShow(new PositionCallback() {
		                public void setPosition(final int offsetWidth, final int offsetHeight) {
		                     selectionMenu.setPopupPosition(left, top);
		                }
		            });
			}
		});
         
         image.setVisible(false);
        container.add(image);
        this.add(container);
        this.setActualName(uniquename);
        this.setCaption(caption);
        setStylePrimaryName(TABLE_DRAG_WIDGET);
        this.setType(lType);
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
    
    public String getDimensionName(){
    	String name=this.getActualName();
		
		String[] split = name.split("[*.?]");
		
			split[0] = split[0].substring(1);
//			split[0] = split[0].replaceAll("[", "");
			split[0] = split[0].replaceAll("]", "");
		return split[0];
    }

    public void setCaption(String name) {
		caption = name;
		
	}
    
    public String getCaption(){
    	return caption;
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
    public ObjectType getType() {
        return type;
    }

    /**
     * Make the widget draggable.
     */
    public void makeDraggable() {
    	if(draggable==false){
    	this.draggable=true;
    	
        dragController.makeDraggable(text);
    	}
    }

    /**
     * Make the widget non draggable.
     */
    public void makeNotDraggable() {
    	if(draggable==true){
    	this.draggable=false;
        dragController.makeNotDraggable(text);
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
    public void setType(final ObjectType type) {
        this.type = type;
    }
    
	public void setDownButtonVisible(boolean isVisible){
		if(!type.equals(ObjectType.MEASURE) && !type.equals(ObjectType.MEMBER)){
			this.image.setVisible(isVisible);
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

	public void setSelectionType(SelectionType string) {
		this.selectionType=string;
		
	}
	
	public List<String> getCurrentSelection(){
		return this.currentSelection;
	}
	
	public SelectionType getSelectionType(){
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
