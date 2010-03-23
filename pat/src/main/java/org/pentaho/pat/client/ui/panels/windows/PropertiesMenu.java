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
package org.pentaho.pat.client.ui.panels.windows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gwt.mosaic.ui.client.ComboBox;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.ListBox;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.TabLayoutPanel;
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
import org.pentaho.pat.rpc.dto.IAxis;
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
public class PropertiesMenu extends LayoutComposite {


    private final static String DIMENSION_MENU = "pat-DimensionMenu"; //$NON-NLS-1$


    // final DefaultComboBoxModel<String> model2 = (DefaultComboBoxModel<String>) hierarchyComboBox.getModel();
    /**
     * 
     * DimensionMenu Constructor.
     * 
     */
    public PropertiesMenu() {
        super();
        this.setStyleName(DIMENSION_MENU);
        final LayoutPanel baseLayoutPanel = getLayoutPanel();
        baseLayoutPanel.setLayout(new BoxLayout(Orientation.VERTICAL));
        
        }


    public void loadAxisProperties(final String queryId, IAxis targetAxis){
        ServiceFactory.getDiscoveryInstance().getDimensions(Pat.getSessionID(), Pat.getCurrQuery(), targetAxis,
                new AsyncCallback<String[]>() {

                    public void onFailure(Throwable arg0) {
                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                .failedMemberFetch(arg0.getLocalizedMessage()));

                    }

                    public void onSuccess(String[] dimensionList) {
                        createListBox(queryId, dimensionList[0]);
                    }
                });
    }
    
    public void createListBox(String queryId, String dimensionId){
        final LayoutPanel vBox = new LayoutPanel(
                new BoxLayout(Orientation.VERTICAL));
            vBox.setPadding(0);
            vBox.setWidgetSpacing(0);

            final ListBox<String> listBox = new ListBox<String>();
//            listBox.setContextMenu(createContextMenu());

            final DefaultListModel<String> model = (DefaultListModel<String>) listBox.getModel();
            
            
            ServiceFactory.getDiscoveryInstance().getAllLevelProperties(Pat.getSessionID(), queryId, dimensionId,
                    new AsyncCallback<StringTree>() {

                public void onFailure(final Throwable arg0) {
                    //dimensionTree.clear();
                    MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                            .failedMemberFetch(arg0.getLocalizedMessage()));
                }

                public void onSuccess(final StringTree labels) {
                    
                    model.add(labels.getValue());
                    
                }
            });
    }
            
            

    }