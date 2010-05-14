package org.pentaho.pat.client.ui.widgets;

import java.util.List;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.pentaho.pat.rpc.dto.IAxis;
import org.pentaho.pat.rpc.dto.StringTree;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class QueryDesignTable extends LayoutComposite {
	
	FlexTable flex = new FlexTable();
	
	public QueryDesignTable(){
		ScrollLayoutPanel slp = new ScrollLayoutPanel();
		
		slp.add(flex);
		//this.flex.setSize("100%", "100%");
		this.flex.setBorderWidth(10);
		DOM.setStyleAttribute(flex.getElement(), "background", "red");
		this.setSize("100%", "100%");
		this.getLayoutPanel().add(slp);
	}

	public void alterSelectionDisplay(MeasureLabel targetLabel, StringTree labels){
    	if(targetLabel.getAxis().equals(IAxis.ROWS)){
        	
        	DimensionSimplePanel dimPanel = ((DimensionSimplePanel)targetLabel.getParent());
        
        	int[] parentcoords = dimPanel.getCoord();
        	final List<StringTree> child = labels.getChildren();
        	removeRowsFromFlexTable(flex, parentcoords);
        	if(!flex.isCellPresent(parentcoords[0]+1, 0)){
        	flex.insertRow(parentcoords[0]+1);
        	}
        	final Label parentLabel = new Label(labels.getCaption());
        	
         	flex.setWidget(parentcoords[0]+1, parentcoords[1], parentLabel);
            for (int i = 2; i < 6; i++) {
                if(i<child.size()){
                final Label memberLabel = new Label(child.get(i).getCaption());
                if(!flex.isCellPresent(parentcoords[1]+i, 0)){
                flex.insertRow(parentcoords[0]+i);
                }
            	
            	flex.setWidget(parentcoords[0]+i, parentcoords[1], memberLabel);
                }
            	//addNewChild(child.get(i), this, parentcoords, IAxis.ROWS);
               
            }

        	}
    	else if(targetLabel.getAxis().equals(IAxis.COLUMNS)){
        	
        		
            	DimensionSimplePanel dimPanel = ((DimensionSimplePanel)targetLabel.getParent());
            
            	int[] parentcoords = dimPanel.getCoord();
            	final List<StringTree> child = labels.getChildren();
            	removeRowsFromFlexTable(flex, parentcoords);
            	flex.insertRow(parentcoords[0]+1);
            	final Label parentLabel = new Label(labels.getCaption());
            	
             	flex.setWidget(parentcoords[0], parentcoords[1]+1, parentLabel);
                for (int i = 2; i < 6; i++) {
                	if(i<child.size()){
                    final Label memberLabel = new Label(child.get(i).getCaption());
                    
                    flex.setWidget(parentcoords[0], parentcoords[1]+i, memberLabel);
                	}
                	//addNewChild(child.get(i), flexTable, parentcoords, IAxis.COLUMNS);
                   
                }
        	}
        }

    


	private void addNewChild(StringTree tree, FlexTable ft, int[] parentcoords, IAxis axis){
    	if(axis.equals(IAxis.ROWS)){
    	final List<StringTree> child = tree.getChildren();
        for (int i = 0; i < child.size(); i++) {
            // Need a copy of the memberLabel because of GWT's lack of clone support
            final Label memberLabel = new Label(child.get(i).getCaption());
           
            ft.insertRow(parentcoords[0]+1);
        	
        	ft.setWidget(parentcoords[0]+1, parentcoords[1], memberLabel);
            
           addNewChild(child.get(i), ft, parentcoords, IAxis.ROWS);
        }
    	}
    	else if(axis.equals(IAxis.COLUMNS)){
    		final List<StringTree> child = tree.getChildren();
            for (int i = 0; i < child.size(); i++) {
                // Need a copy of the memberLabel because of GWT's lack of clone support
                final Label memberLabel = new Label(child.get(i).getCaption());
               
                
            	
            	ft.setWidget(parentcoords[0], parentcoords[1]+i, memberLabel);
                
               addNewChild(child.get(i), ft, parentcoords, IAxis.COLUMNS);
            }	
    	}

    }

    private void removeRowsFromFlexTable(FlexTable flexTable, int[] coords){
    	while(flexTable.isCellPresent(coords[0]+1, coords[1])==true 
    			&& flexTable.getWidget(coords[0]+1, coords[1]) instanceof Label){
    	
    		flexTable.removeRow(coords[0]+1);
    	
    	}
    	
    }

	public void setWidget(int i, int j,
			Widget w) {
		this.flex.setWidget(i, j, w);
		
	}
	
	public Widget getWidget(int i, int j){
		return this.flex.getWidget(i, j);
	}

}
