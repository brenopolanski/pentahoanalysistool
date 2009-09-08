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

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.Application.ApplicationImages;
import org.pentaho.pat.client.ui.widgets.MemberSelectionLabel;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.StringTree;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Tree list of connections and cubes
 * 
 * @created Aug 18, 2009
 * @since 0.5.0
 * @author Paul Stoellberger
 * 
 */
public class DimensionMenu extends LayoutComposite {

    /** The main menu. */
    private final Tree dimensionTree;

    public DimensionMenu() {
        super();
       // this.sinkEvents(NativeEvent.BUTTON_LEFT | NativeEvent.BUTTON_RIGHT | Event.ONCONTEXTMENU);
        final LayoutPanel baseLayoutPanel = getLayoutPanel();
        baseLayoutPanel.setLayout(new BoxLayout(Orientation.HORIZONTAL));

        final ApplicationImages treeImages = GWT.create(ApplicationImages.class);
        dimensionTree = new Tree(treeImages);
        dimensionTree.setAnimationEnabled(true);
        //dimensionTree.addStyleName(Pat.DEF_STYLE_NAME + "-cubemenu"); //$NON-NLS-1$

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
                        MessageBox.error(ConstantFactory.getInstance().error(), "Error loading connections");
                    }

                    public void onSuccess(final StringTree arg0) {
                        dimensionTree.clear();
                        MemberSelectionLabel memberLabel = new MemberSelectionLabel(arg0.getValue());
                        

                        final TreeItem parent = dimensionTree.addItem(memberLabel);
                        memberLabel.setTreeItem(parent);
                        addDimensionTreeItem(arg0, parent);
                    }

                });
    }

    /**
     * Generates the Member Tree of a Dimension.
     */
    private final void addDimensionTreeItem(final StringTree childStringTree, final TreeItem parent) {
        final List<StringTree> child = childStringTree.getChildren();
        for (int i = 0; i < child.size(); i++) {
            final TreeItem newParent = parent.addItem(child.get(i).getValue());
            addDimensionTreeItem(child.get(i), newParent);
        }
    }

}
