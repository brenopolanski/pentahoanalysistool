/*
 * Copyright (C) 2009 Paul Stoellberger
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
package org.pentaho.pat.client.ui.panels;

import java.util.List;

import org.gwt.mosaic.ui.client.ComboBox;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.list.DefaultComboBoxModel;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.Application.ApplicationImages;
import org.pentaho.pat.client.ui.widgets.MemberSelectionLabel;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.StringTree;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Tree list of connections and cubes.
 * 
 * @created Aug 18, 2009
 * @since 0.5.0
 * @author Paul Stoellberger
 * 
 */
public class DimensionMenu extends LayoutComposite {

    /** The main menu. */
    private final Tree dimensionTree;

    private Label dimensionLabel;

    private final ComboBox<String> sortComboBox = new ComboBox<String>();

    private final DefaultComboBoxModel<String> sortModeModel = (DefaultComboBoxModel<String>) sortComboBox.getModel();
    
    private final ComboBox<String> hierarchyComboBox = new ComboBox<String>();

    private final DefaultComboBoxModel<String> hierarchyModeModel = (DefaultComboBoxModel<String>) hierarchyComboBox.getModel();
    
    /**
     * 
     * DimensionMenu Constructor.
     *
     */
    public DimensionMenu() {
        super();
        // this.sinkEvents(NativeEvent.BUTTON_LEFT | NativeEvent.BUTTON_RIGHT | Event.ONCONTEXTMENU);
        final LayoutPanel baseLayoutPanel = getLayoutPanel();
        baseLayoutPanel.setLayout(new BoxLayout(Orientation.VERTICAL));

        final ApplicationImages treeImages = GWT.create(ApplicationImages.class);
        dimensionTree = new Tree(treeImages);
        dimensionTree.setAnimationEnabled(true);
        //dimensionTree.addStyleName(Pat.DEF_STYLE_NAME + "-cubemenu"); //$NON-NLS-1$
        final LayoutPanel filterPanel = new LayoutPanel(new BoxLayout(Orientation.HORIZONTAL));

        final TextBox filterbox = new TextBox();
//        filterbox.setEnabled(false);
//        filterbox.addKeyUpHandler(new KeyUpHandler() {
//
//            public void onKeyUp(final KeyUpEvent arg0) {
//
//                final String filter = filterbox.getText();
//                for (int i = 0; i < dimensionTree.getItemCount(); i++)
//                    if (!dimensionTree.getItem(i).getText().startsWith(filter))
//                        dimensionTree.getItem(i).remove();
//
//            }
//
//        });
        final Button filterButton = new Button(ConstantFactory.getInstance().filter());
//        filterButton.setEnabled(false);
        filterButton.addClickHandler(new ClickHandler() {
            
            public void onClick(ClickEvent arg0) {
                if (filterbox.getText() != null && filterbox.getText().length() > 0) {
                    findItems(filterbox.getText());
                }
                
            }
        });

        filterPanel.add(filterbox, new BoxLayoutData(FillStyle.BOTH));
        filterPanel.add(filterButton, new BoxLayoutData(FillStyle.VERTICAL));

        
        sortModeModel.add("ASC");
        sortModeModel.add("DESC");
        sortModeModel.add("BASC");
        sortModeModel.add("BDESC");

        sortComboBox.addChangeHandler(new ChangeHandler() {

            public void onChange(final ChangeEvent arg0) {

                ServiceFactory.getQueryInstance().setSortOrder(Pat.getSessionID(), Pat.getCurrQuery(),
                        dimensionLabel.getText(), sortComboBox.getText(), new AsyncCallback<Object>() {

                            public void onFailure(final Throwable arg0) {
                                MessageBox.error(ConstantFactory.getInstance().error(), "Failed");

                            }

                            public void onSuccess(final Object arg0) {

                            }

                        });

            }

        });


        hierarchyModeModel.add("PRE");
        hierarchyModeModel.add("POST");

        hierarchyComboBox.addChangeHandler(new ChangeHandler() {

            public void onChange(final ChangeEvent arg0) {
                ServiceFactory.getQueryInstance().setHierarchizeMode(Pat.getSessionID(), Pat.getCurrQuery(),
                        dimensionLabel.getText(), hierarchyComboBox.getText(), new AsyncCallback<Object>() {

                            public void onFailure(final Throwable arg0) {
                                MessageBox.error(ConstantFactory.getInstance().error(), "Failed");
                            }

                            public void onSuccess(final Object arg0) {

                            }

                        });

            }

        });

        baseLayoutPanel.add(filterPanel, new BoxLayoutData(FillStyle.HORIZONTAL));
        baseLayoutPanel.add(sortComboBox, new BoxLayoutData(FillStyle.HORIZONTAL));
        baseLayoutPanel.add(hierarchyComboBox, new BoxLayoutData(FillStyle.HORIZONTAL));
        baseLayoutPanel.add(dimensionTree, new BoxLayoutData(FillStyle.BOTH));

        dimensionTree.addSelectionHandler(new SelectionHandler<TreeItem>() {

            public void onSelection(final SelectionEvent<TreeItem> arg0) {
                dimensionTree.ensureSelectedItemVisible();

                // TODO uncomment when implemented
                // DimensionTreeItem selected = (DimensionTreeItem)dimensionTree.getSelectedItem().getWidget();
                // selected.showButton();

            }

        });

    }

    public Tree getDimensionTree() {
        return dimensionTree;
    }

    public final void loadMembers(final String queryId, final String dimensionId) {
        ServiceFactory.getDiscoveryInstance().getMembers(Pat.getSessionID(), queryId, dimensionId,
                new AsyncCallback<StringTree>() {

                    public void onFailure(final Throwable arg0) {
                        dimensionTree.clear();
                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                .failedMemberFetch(arg0.getLocalizedMessage()));
                    }

                    public void onSuccess(final StringTree labels) {
                        dimensionTree.clear();
                        dimensionLabel = new Label(labels.getValue());

                        final TreeItem parent = dimensionTree.addItem(dimensionLabel);

                        ServiceFactory.getQueryInstance().getSelection(Pat.getSessionID(), Pat.getCurrQuery(),
                                dimensionLabel.getText(), new AsyncCallback<String[][]>() {

                                    public void onFailure(final Throwable arg0) {
                                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory
                                                .getInstance().failedMemberFetch(arg0.getLocalizedMessage()));

                                    }

                                    public void onSuccess(final String[][] selectionlist) {

                                        ServiceFactory.getQueryInstance().getSortOrder(Pat.getSessionID(),
                                                Pat.getCurrQuery(), dimensionId, new AsyncCallback<String>() {

                                                    public void onFailure(final Throwable arg0) {
                                                        MessageBox.error(ConstantFactory.getInstance().error(),
                                                                "failed to get sort");

                                                    }

                                                    public void onSuccess(final String arg0) {
                                                        for (int i = 0; i < sortComboBox.getItemCount(); i++)
                                                            if (sortComboBox.getModel().getElementAt(i).equals(arg0)){
                                                                //DimensionMenu.this.sortComboBox.setSelectedIndex(i);
                                                                DimensionMenu.this.sortModeModel.setSelectedItem(arg0);
                                                                DimensionMenu.this.sortModeModel.getSelectedItem();
                                                                break;
                                                            }
                                                            else
                                                                DimensionMenu.this.sortModeModel.setSelectedItem(null);
                                                        
                                                        ServiceFactory.getQueryInstance().getHierarchizeMode(
                                                                Pat.getSessionID(), Pat.getCurrQuery(), dimensionId,
                                                                new AsyncCallback<String>() {

                                                                    public void onFailure(final Throwable arg0) {
                                                                        // TODO Auto-generated method stub

                                                                    }

                                                                    public void onSuccess(final String arg0) {
                                                                        for (int i = 0; i < hierarchyComboBox
                                                                                .getItemCount(); i++)
                                                                            if (hierarchyComboBox.getModel().getElementAt(i)
                                                                                    .equals(arg0)){
                                                                                DimensionMenu.this.hierarchyModeModel.setSelectedItem(arg0);
                                                                        DimensionMenu.this.hierarchyModeModel.getSelectedItem();
                                                                        break;
                                                                    }
                                                                    else
                                                                        DimensionMenu.this.hierarchyModeModel.setSelectedItem(null);
                                                                            
                                                                        addDimensionTreeItem(labels, parent,
                                                                                selectionlist);
                                                                    }

                                                                });

                                                    }

                                                });

                                    }

                                });
                    }

                });
    }

    /**
     * Generates the Member Tree of a Dimension.
     * 
     * @param arg0
     */
    private final void addDimensionTreeItem(final StringTree childStringTree, final TreeItem parent,
            final String[][] selectionlist) {
        final List<StringTree> child = childStringTree.getChildren();

        for (int i = 0; i < child.size(); i++) {
            final MemberSelectionLabel memberLabel = new MemberSelectionLabel(child.get(i).getValue());
            for (final String[] element2 : selectionlist)
                if (memberLabel.getText().equals(element2[0]))
                    memberLabel.setSelectionMode(element2[1]);

            final TreeItem newParent = parent.addItem(memberLabel);
            memberLabel.setTreeItem(newParent);
            addDimensionTreeItem(child.get(i), newParent, selectionlist);
        }
    }
    
    private final void findItems(final String searchText) {
        for(int i = 0; i< dimensionTree.getItemCount();i++) {
            searchTreeItems(dimensionTree.getItem(i),searchText);
        }
    }

    private final void searchTreeItems(final TreeItem item,final String searchText) {
        if (item.getText().toUpperCase().contains(searchText.toUpperCase())) {
            item.setSelected(true);
            if (item.getParentItem() != null) {
                openParent(item.getParentItem());
            }
        }
        for (int i = 0; i < item.getChildCount();i++) {
            searchTreeItems(item.getChild(i),searchText);
        }
    }
    
    private final void openParent(final TreeItem item) {
        item.setState(true);
        if (item.getParentItem() != null) {
            openParent(item.getParentItem());
        }
    }
}
