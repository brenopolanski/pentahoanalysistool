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
package org.pentaho.pat.client.ui.popups;

import java.util.ArrayList;
import java.util.List;

import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.PopupMenu;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.widgets.DimensionSimplePanel;
import org.pentaho.pat.client.ui.widgets.MeasureLabel;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.MemberLabelItem;
import org.pentaho.pat.rpc.dto.StringTree;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

/**
 *TODO JAVADOC
 * 
 * @author bugg
 * 
 */
public class MeasureLabelSelectionModeMenu extends PopupMenu {

    public final static String SELECTION_MODE_MENU = "pat-SelectionModeMenu"; //$NON-NLS-1$
    public class SelectionModeClearCommand implements Command {

        /*
         * (non-Javadoc)
         * 
         * @see com.google.gwt.user.client.Command#execute()
         */
        /**
         * Code to execute on click.
         */
        public final void execute() {

            final MeasureLabel targetLabel = (MeasureLabel) getSource();
            final String dimName = targetLabel.getText();
            final List<String> dimSelections = (targetLabel.getValue());

            ServiceFactory.getQueryInstance().clearSelection(Pat.getSessionID(), Pat.getCurrQuery(), dimName,
                    dimSelections, new AsyncCallback<Object>() {
                        public void onFailure(final Throwable caught) {
                            MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                    .noSelectionCleared(caught.getLocalizedMessage()));

                        }

                        public void onSuccess(final Object result) {
                            //targetLabel.setSelectionMode(CLEAR);
                            //DimensionBrowserWindow.getDimensionMenuPanel().syncTreeAndList(targetLabel, CLEAR);

                        }
                    });
            MeasureLabelSelectionModeMenu.this.hide();
        }
    }

    public class SelectionModeCommand implements Command {

        /** The selection mode. */
        private int selectionMode = -1;

        /**
         * The Constructor.
         * 
         * @param selectionMode
         *            the selection mode
         */
        public SelectionModeCommand(final int selectionMode) {
            this.selectionMode = selectionMode;
        }

        private void addNewChild(StringTree tree, FlexTable ft, int[] parentcoords){
        	final List<StringTree> child = tree.getChildren();
            for (int i = 0; i < child.size(); i++) {
                // Need a copy of the memberLabel because of GWT's lack of clone support
                final Label memberLabel = new Label(child.get(i).getCaption());
               
                ft.insertRow(parentcoords[0]+1);
            	
            	ft.setWidget(parentcoords[0]+1, parentcoords[1], memberLabel);
                
               addNewChild(child.get(i), ft, parentcoords);
            }

        }
        
        /*
         * (non-Javadoc)
         * 
         * @see com.google.gwt.user.client.Command#execute()
         */
        /**
         * The Command executed on click.
         */
        public final void execute() {
            final MeasureLabel targetLabel = (MeasureLabel) getSource();
            final String dimName = targetLabel.getText();
            
            final String selection = setSelectionMode(selectionMode);
            if(targetLabel.getType() == MeasureLabel.LabelType.DIMENSION){
            ServiceFactory.getQueryInstance().createSelection(Pat.getSessionID(), Pat.getCurrQuery(), dimName,
                    null, "dimension", selection, new AsyncCallback<StringTree>() {

                        public void onFailure(final Throwable arg0) {
                            MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                    .noSelectionSet(arg0.getLocalizedMessage()));

                        }

                        public void onSuccess(final StringTree labels) {
                        	FlexTable flexTable = ((FlexTable)targetLabel.getParent().getParent());
                        	DimensionSimplePanel dimPanel = ((DimensionSimplePanel)targetLabel.getParent());
                        
                        	int[] parentcoords = dimPanel.getCoord();
                        	final List<StringTree> child = labels.getChildren();
                        	flexTable.insertRow(parentcoords[0]+1);
                        	final Label parentLabel = new Label(labels.getCaption());
                        	removeRowsFromFlexTable(flexTable, parentcoords);
                         	flexTable.setWidget(parentcoords[0]+1, parentcoords[1], parentLabel);
                            for (int i = 0; i < child.size(); i++) {
                                
                                final Label memberLabel = new Label(child.get(i).getCaption());
                                
                                flexTable.insertRow(parentcoords[0]+1);
                            	
                            	flexTable.setWidget(parentcoords[0]+1, parentcoords[1], memberLabel);
                                
                            	addNewChild(child.get(i), flexTable, parentcoords);
                               
                            }

                        	
                        }

                    });
            }
            else if(targetLabel.getType() == MeasureLabel.LabelType.HIERARCHY){
            	final List<String> hierarchySelections = new ArrayList<String>();
            	String dimName1 = targetLabel.getValue().get(0);
            	hierarchySelections.add(targetLabel.getValue().get(0));
            	hierarchySelections.add(targetLabel.getActualName());	
            	ServiceFactory.getQueryInstance().createSelection(Pat.getSessionID(), Pat.getCurrQuery(), dimName1,
                        hierarchySelections, "hierarchy", selection, new AsyncCallback<StringTree>() {

                            public void onFailure(final Throwable arg0) {
                                MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                        .noSelectionSet(arg0.getLocalizedMessage()));

                            }

                            public void onSuccess(final StringTree labels) {
                            	FlexTable flexTable = ((FlexTable)targetLabel.getParent().getParent());
                            	DimensionSimplePanel dimPanel = ((DimensionSimplePanel)targetLabel.getParent());
                            
                            	int[] parentcoords = dimPanel.getCoord();
                            	final List<StringTree> child = labels.getChildren();
                            	removeRowsFromFlexTable(flexTable, parentcoords);
                            	
                            	flexTable.insertRow(parentcoords[0]+1);
                            	final Label parentLabel = new Label(labels.getCaption());         	
                             	flexTable.setWidget(parentcoords[0]+1, parentcoords[1], parentLabel);
                                for (int i = 0; i < child.size(); i++) {
                                    
                                    final Label memberLabel = new Label(child.get(i).getCaption());
                                   
                                    flexTable.insertRow(parentcoords[0]+1);
                                	
                                	flexTable.setWidget(parentcoords[0]+1, parentcoords[1], memberLabel);
                                    
                                	addNewChild(child.get(i), flexTable, parentcoords);                                   
                                }

                            }

                        });            	
            }
            else if(targetLabel.getType() == MeasureLabel.LabelType.LEVEL){
            	
     
            	final List<String> levelSelections = new ArrayList<String>();
            	String dimName1 = targetLabel.getValue().get(0);
            	levelSelections.add(targetLabel.getValue().get(0));
            	levelSelections.add(targetLabel.getValue().get(1));
            	levelSelections.add(targetLabel.getActualName());
            	
            	ServiceFactory.getQueryInstance().createSelection(Pat.getSessionID(), Pat.getCurrQuery(), dimName1,
                        levelSelections, "level", selection, new AsyncCallback<StringTree>() {

                            public void onFailure(final Throwable arg0) {
                                MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                        .noSelectionSet(arg0.getLocalizedMessage()));

                            }

                            public void onSuccess(final StringTree labels) {
                            	FlexTable flexTable = ((FlexTable)targetLabel.getParent().getParent());
                            	DimensionSimplePanel dimPanel = ((DimensionSimplePanel)targetLabel.getParent());
                            
                            	int[] parentcoords = dimPanel.getCoord();
                            	final List<StringTree> child = labels.getChildren();

                            	removeRowsFromFlexTable(flexTable, parentcoords);
                            	
                            	flexTable.insertRow(parentcoords[0]+1);
                            	final Label parentLabel = new Label(labels.getCaption());         	
                             	flexTable.setWidget(parentcoords[0]+1, parentcoords[1], parentLabel);
                            	for (int i = 0; i < child.size(); i++) {
                                    
                                    final Label memberLabel = new Label(child.get(i).getCaption());
                                   
                                    flexTable.insertRow(parentcoords[0]+1);
                                	
                                	flexTable.setWidget(parentcoords[0]+1, parentcoords[1], memberLabel);
                                    
                                	addNewChild(child.get(i), flexTable, parentcoords);                                   
                                }
                            }

                        });            	
            }

            MeasureLabelSelectionModeMenu.this.hide();
        }
    }

    
    private void removeRowsFromFlexTable(FlexTable flexTable, int[] coords){
    	while(flexTable.isCellPresent(coords[0]+1, coords[1])==true 
    			&& flexTable.getWidget(coords[0]+1, coords[1]) instanceof Label){
    	
    		flexTable.removeRow(coords[0]+1);
    	
    	}
    	
    }
    /** The Constant MEMBER. */
    public static final int MEMBER = 0;

    /** The Constant CHILDREN. */
    public static final int CHILDREN = 1;

    /** The Constant INCLUDE_CHILDREN. */
    public static final int INCLUDE_CHILDREN = 2;

    /** The Constant SIBLINGS. */
    public static final int SIBLINGS = 3;

    public static final int DESCENDANTS = 4;

    public static final int ANCESTORS = 5;

    /** The Constant CLEAR. */
    public static final int CLEAR = -1;

    /** The source. */
    private static Widget source;

    public static Widget getSource() {
        return source;
    }

    public static void setSource(final Widget source2) {
        source = source2;
    }


	public MeasureLabelSelectionModeMenu() {
        super();
        init();
        this.setStyleName(SELECTION_MODE_MENU);
    }

    public MeasureLabelSelectionModeMenu(DimensionSimplePanel dimensionSimplePanel, MeasureLabel label) {
    	super();
        init();
        this.setStyleName(SELECTION_MODE_MENU);
        
	}

	private void init() {
        this.setAutoOpen(true);
        this.addItem(new MenuItem(ConstantFactory.getInstance().member(), new SelectionModeCommand(MEMBER)));
        this.addItem(new MenuItem(ConstantFactory.getInstance().children(), new SelectionModeCommand(CHILDREN)));
        this.addItem(new MenuItem(ConstantFactory.getInstance().includeChildren(), new SelectionModeCommand(
                INCLUDE_CHILDREN)));
        this.addItem(new MenuItem(ConstantFactory.getInstance().siblings(), new SelectionModeCommand(SIBLINGS)));
        this.addItem(new MenuItem(ConstantFactory.getInstance().descendants(), new SelectionModeCommand(DESCENDANTS)));
        this.addItem(new MenuItem(ConstantFactory.getInstance().ancestors(), new SelectionModeCommand(ANCESTORS)));
        this.addItem(new MenuItem(ConstantFactory.getInstance().clearSelections(), new SelectionModeClearCommand()));
    }

    public final String setSelectionMode(final int selectionMode) {
        String selection = ""; //$NON-NLS-1$
        switch (selectionMode) {
        case 0:
            selection = "MEMBER"; //$NON-NLS-1$
            break;
        case 1:
            selection = "CHILDREN"; //$NON-NLS-1$
            break;
        case 2:
            selection = "INCLUDE_CHILDREN"; //$NON-NLS-1$
            break;
        case 3:
            selection = "SIBLINGS"; //$NON-NLS-1$
            break;
        case 4:
            selection = "DESCENDANTS"; //$NON-NLS-1$
            break;
        case 5:
            selection = "ANCESTORS"; //$NON-NLS-1$

        default:
            break;
        }
        return selection;

    }

    /**
     * Show the context menu.
     * 
     * @param event
     *            the event
     * @param selectedItem
     *            the selected item
     */
    public final void showContextMenu(final Event event, final MeasureLabel selectedItem) {

        setSource(selectedItem);

    }

    public final void showContextMenu(final MeasureLabel selectedItem) {

        setSource(selectedItem);

    }

    protected final String getDimensionName(final MeasureLabel targetLabel) {
        final Tree tree = (Tree) targetLabel.getParent();
        final TreeItem rootItem = tree.getItem(0);
        final Label widget = (Label) rootItem.getWidget();

        return widget.getText();
    }

    
}
