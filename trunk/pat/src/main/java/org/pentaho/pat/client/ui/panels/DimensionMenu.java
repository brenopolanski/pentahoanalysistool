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

import java.util.ArrayList;
import java.util.List;

import org.gwt.mosaic.ui.client.ComboBox;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.ListBox;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.ListBox.CellRenderer;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.list.DefaultComboBoxModel;
import org.gwt.mosaic.ui.client.list.DefaultListModel;
import org.gwt.mosaic.ui.client.list.Filter;
import org.gwt.mosaic.ui.client.list.FilterProxyListModel;
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
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
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

    private final DefaultListModel<MemberSelectionLabel> memberListBoxModel = new DefaultListModel<MemberSelectionLabel>();
    
    private final  ListBox<MemberSelectionLabel> memberListBox = new ListBox<MemberSelectionLabel>(new String[]  {ConstantFactory.getInstance().member(),ConstantFactory.getInstance().path()});
    
    private FilterProxyListModel<MemberSelectionLabel, String> filterModel;
    
    private final TextBox filterbox = new TextBox();


    private Timer filterTimer = new Timer() {
        @Override
        public void run() {
          filterModel.filter(filterbox.getText());
        }
    };

    final DefaultComboBoxModel<String> model2 = (DefaultComboBoxModel<String>) hierarchyComboBox.getModel();
    /**
     * 
     * DimensionMenu Constructor.
     *
     */
    public DimensionMenu() {
        super();
        final LayoutPanel baseLayoutPanel = getLayoutPanel();
        baseLayoutPanel.setLayout(new BoxLayout(Orientation.VERTICAL));

        final ApplicationImages treeImages = GWT.create(ApplicationImages.class);
        dimensionTree = new Tree(treeImages);
        dimensionTree.setAnimationEnabled(false);
        dimensionTree.setWidth("300px"); //$NON-NLS-1$
        memberListBox.setColumnTruncatable(0, false);
        memberListBox.setColumnTruncatable(1, false);
        memberListBox.setWidth("500px"); //$NON-NLS-1$
        
        memberListBox.setCellRenderer(new CellRenderer<MemberSelectionLabel>() {
            public void renderCell(ListBox<MemberSelectionLabel> listBox, int row, int column,
            		MemberSelectionLabel item) {
                
              switch (column) {
                case 0:
                  listBox.setWidget(row, column, item);
                  break;
                case 1:
                	String path = ""; //$NON-NLS-1$
                	for (int i=0;i<item.getFullPath().length;i++) {
                		path+=item.getFullPath()[i];
                		if((i+1)<item.getFullPath().length) {
                			path+="->"; //$NON-NLS-1$
                		}
                	}
                  listBox.setText(row, column, path);
                  break;
                default:
                  throw new RuntimeException(MessageFactory.getInstance().unexpectedError());
              }
            }
          });

        
        //dimensionTree.addStyleName(Pat.DEF_STYLE_NAME + "-cubemenu"); //$NON-NLS-1$
        final LayoutPanel filterPanel = new LayoutPanel(new BoxLayout(Orientation.HORIZONTAL));
        filterbox.addKeyPressHandler(new KeyPressHandler() {
            public void onKeyPress(KeyPressEvent event) {
                filterTimer.schedule(300);
              }
            });

        final Label filterText= new Label(ConstantFactory.getInstance().filter()+":"); //$NON-NLS-1$
        filterPanel.add(filterText, new BoxLayoutData(FillStyle.VERTICAL));
        filterPanel.add(filterbox, new BoxLayoutData(FillStyle.BOTH));
        
        sortModeModel.add(ConstantFactory.getInstance().sortAscending());
        sortModeModel.add(ConstantFactory.getInstance().sortDescending());
        sortModeModel.add(ConstantFactory.getInstance().sortBreakAscending());
        sortModeModel.add(ConstantFactory.getInstance().sortBreakDescending());

        sortComboBox.addChangeHandler(new ChangeHandler() {
            public void onChange(final ChangeEvent arg0) {
                String scb = new String();
                switch (sortComboBox.getSelectedIndex()) {
                case 0:
                    scb = "ASC"; //$NON-NLS-1$
                    break;
                case 1:
                    scb = "DESC"; //$NON-NLS-1$
                    break;
                case 2:
                    scb = "BASC"; //$NON-NLS-1$
                    break;
                case 3:
                    scb = "BDESC"; //$NON-NLS-1$
                    break;
                default:
                    throw new RuntimeException(MessageFactory.getInstance().unexpectedError());
                }
                ServiceFactory.getQueryInstance().setSortOrder(Pat.getSessionID(), Pat.getCurrQuery(),
                        dimensionLabel.getText(), scb, new AsyncCallback<Object>() {

                            public void onFailure(final Throwable arg0) {
                                MessageBox.error(ConstantFactory.getInstance().error(), ConstantFactory.getInstance().sortFailed());
                            }

                            public void onSuccess(final Object arg0) {
                            }
                        });
            }
        });


        hierarchyModeModel.add(ConstantFactory.getInstance().pre());
        hierarchyModeModel.add(ConstantFactory.getInstance().post());

        hierarchyComboBox.addChangeHandler(new ChangeHandler() {
            public void onChange(final ChangeEvent arg0) {
                String hcb = new String();
                switch (hierarchyComboBox.getSelectedIndex()){
                case 0:
                    hcb = "PRE"; //$NON-NLS-1$
                    break;
                case 1:
                    hcb = "POST"; //$NON-NLS-1$
                    break;
                default:
                    throw new RuntimeException(MessageFactory.getInstance().unexpectedError());
                }
                ServiceFactory.getQueryInstance().setHierarchizeMode(Pat.getSessionID(), Pat.getCurrQuery(),
                        dimensionLabel.getText(), hcb, new AsyncCallback<Object>() {

                            public void onFailure(final Throwable arg0) {
                                MessageBox.error(ConstantFactory.getInstance().error(), ConstantFactory.getInstance().hierarchizeFailed());
                            }

                            public void onSuccess(final Object arg0) {

                            }

                        });

            }

        });

        baseLayoutPanel.add(filterPanel, new BoxLayoutData(FillStyle.HORIZONTAL));
        baseLayoutPanel.add(sortComboBox, new BoxLayoutData(FillStyle.HORIZONTAL));
        baseLayoutPanel.add(hierarchyComboBox, new BoxLayoutData(FillStyle.HORIZONTAL));
        
        
        ScrollLayoutPanel dimTreeScrollPanel = new ScrollLayoutPanel(new BoxLayout(Orientation.HORIZONTAL));
        dimTreeScrollPanel.setAnimationEnabled(true);
        
        // TODO this needs a proper fix, it just means that the scrollpanel and the dimensiontree will 
        // have the same background (if you expand it will look weird otherwise)
        dimTreeScrollPanel.setStyleName(dimensionTree.getStyleName());
        
        dimTreeScrollPanel.add(dimensionTree, new BoxLayoutData(FillStyle.BOTH));
        
        dimensionTree.addSelectionHandler(new SelectionHandler<TreeItem>() {

            public void onSelection(final SelectionEvent<TreeItem> arg0) {
                dimensionTree.ensureSelectedItemVisible();
            }

        });
        
        LayoutPanel groupPanel = new LayoutPanel(new BoxLayout(Orientation.HORIZONTAL));
        groupPanel.add(dimTreeScrollPanel,new BoxLayoutData(FillStyle.BOTH, true));
        groupPanel.add(memberListBox,new BoxLayoutData(FillStyle.BOTH));
        baseLayoutPanel.add(groupPanel, new BoxLayoutData(FillStyle.BOTH));

    }

    /** 
     * Empties filter textbox when being attached
     */
    @Override
    protected void onAttach() {
        super.onAttach();
        filterbox.setText(""); //$NON-NLS-1$
    };
    
    public Tree getDimensionTree() {
        return dimensionTree;
    }

    /**
     * Loads all Members of a given dimension and query
     * @param queryId - Query to use to discover dimension members
     * @param dimensionId - Dimension of interest
     */
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
                                                                MessageFactory.getInstance().failedGetSort());

                                                    }

                                                    public void onSuccess(final String arg0) {
                                                        for (int i = 0; i < sortComboBox.getItemCount(); i++)
                                                            //if (sortComboBox.getModel().getElementAt(i).equals(arg0)){
                                                                //DimensionMenu.this.sortComboBox.setSelectedIndex(i);
                                                                if(arg0.equals("ASC")){
                                                                    DimensionMenu.this.sortModeModel.setSelectedItem(ConstantFactory.getInstance().sortAscending()); //$NON-NLS-1$
                                                                    break;
                                                                }
                                                                else if(arg0.equals("DESC")){
                                                                    DimensionMenu.this.sortModeModel.setSelectedItem(ConstantFactory.getInstance().sortDescending()); //$NON-NLS-1$
                                                                    break;
                                                                }
                                                                else if(arg0.equals("BASC")){
                                                                    DimensionMenu.this.sortModeModel.setSelectedItem(ConstantFactory.getInstance().sortBreakAscending()); //$NON-NLS-1$
                                                                    break;
                                                                }
                                                                else if(arg0.equals("BDESC")){
                                                                    DimensionMenu.this.sortModeModel.setSelectedItem(ConstantFactory.getInstance().sortBreakDescending()); //$NON-NLS-1$
                                                                    break;
                                                                }
                                                                else{
                                                                    DimensionMenu.this.sortModeModel.setSelectedItem(null);
                                                                }
                                                                    
                                                                //DimensionMenu.this.sortModeModel.setSelectedItem(arg0);
                                                                DimensionMenu.this.sortModeModel.getSelectedItem();
                                                                
                                                            //}
                                                            //else
                                                             //   DimensionMenu.this.sortModeModel.setSelectedItem(null);
                                        
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
                                                                        
                                                                        memberListBoxModel.clear();
                                                                        addDimensionTreeItem(labels, parent,
                                                                                selectionlist,dimensionLabel.getText());
                                                                        // TODO why do i have to do it here and not in the constructor?
                                                                        filterModel = new FilterProxyListModel<MemberSelectionLabel, String>(memberListBoxModel);
                                                                        filterModel.setModelFilter(new Filter<MemberSelectionLabel, String>() {
                                                                            public boolean select(MemberSelectionLabel element, String filter) {
                                                                              if (filter == null || filter.length() == 0) {
                                                                                return true;
                                                                              }
                                                                              return element.getText().toUpperCase().contains(filter.toUpperCase());
                                                                            }
                                                                          });
                                                                        memberListBox.setModel(filterModel);
                                                                        
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
     * Adds children {@link StringTree} to a given parent in the Tree 
     * @param childStringTree - Children StringTree
     * @param parent - Parent in the Tree
     * @param selectionlist - List of selected members
     * @param dimension - Currently processed dimension 
     */
    private final void addDimensionTreeItem(final StringTree childStringTree, final TreeItem parent,
            final String[][] selectionlist, final String dimension) {
        final List<StringTree> child = childStringTree.getChildren();

        for (int i = 0; i < child.size(); i++) {
            // Need a copy of the memberLabel because of GWT's lack of clone support
            MemberSelectionLabel memberLabel = new MemberSelectionLabel(child.get(i).getValue());
            MemberSelectionLabel memberLabelcopy = new MemberSelectionLabel(child.get(i).getValue());
            memberLabel.setDimension(dimension);
            memberLabelcopy.setDimension(dimension);

            
            for (final String[] element2 : selectionlist)
                if (memberLabel.getText().equals(element2[0])) {
                    memberLabel.setSelectionMode(element2[1]);
                    memberLabelcopy.setSelectionMode(element2[1]);
                }
            
            TreeItem newParent = parent.addItem(memberLabel);
            memberLabel.setFullPath(getFullPath(newParent));
            memberLabelcopy.setFullPath(getFullPath(newParent));
            memberListBoxModel.add(memberLabelcopy);
            addDimensionTreeItem(child.get(i), newParent, selectionlist, dimension);
        }
    }

    /**
     * Returns the full path of the member in the dimension tree
     * @param currentTreeItem - TreeItem of interest
     * @return String[] path
     */
    public final String[] getFullPath(TreeItem currentTreeItem) {
        final List<String> pathList = new ArrayList<String>();
        pathList.add(currentTreeItem.getText());
        while (currentTreeItem.getParentItem() != null
                && currentTreeItem.getParentItem().getWidget() instanceof MemberSelectionLabel) {
            currentTreeItem = currentTreeItem.getParentItem();
            pathList.add(0, ((MemberSelectionLabel) currentTreeItem.getWidget()).getText());
        }
        final String[] values = new String[pathList.size()];
        return pathList.toArray(values);
    }
}
