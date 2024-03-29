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

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.Application.ApplicationImages;
import org.pentaho.pat.client.ui.widgets.CubeTreeItem;
import org.pentaho.pat.client.ui.windows.CubeBrowserWindow;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.rpc.dto.CubeItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Tree list of connections and cubes.
 * 
 * @created Jul 2, 2009
 * @since 0.4.0
 * @author Paul Stoellberger
 * 
 */
public class CubeMenu extends LayoutComposite {

    /** The main menu. */
    private final Tree cubeTree;

    /**
     * CubeMenu Constructor.
     * 
     */
    public CubeMenu() {
        super();
        this.sinkEvents(NativeEvent.BUTTON_LEFT | NativeEvent.BUTTON_RIGHT | Event.ONCONTEXTMENU);
        final LayoutPanel baseLayoutPanel = getLayoutPanel();
        baseLayoutPanel.setLayout(new BoxLayout(Orientation.HORIZONTAL));

        final ApplicationImages treeImages = GWT.create(ApplicationImages.class);
        cubeTree = new Tree(treeImages);
        cubeTree.setAnimationEnabled(true);
        cubeTree.addStyleName("pat-Tree"); //$NON-NLS-1$

        ScrollLayoutPanel sp = new ScrollLayoutPanel(new BoxLayout());
        sp.setAnimationEnabled(true);
        sp.add(cubeTree, new BoxLayoutData(FillStyle.BOTH));
        baseLayoutPanel.add(sp, new BoxLayoutData(FillStyle.BOTH));

        cubeTree.addSelectionHandler(new SelectionHandler<TreeItem>() {

            public void onSelection(final SelectionEvent<TreeItem> arg0) {
                cubeTree.ensureSelectedItemVisible();
                if (((CubeTreeItem)arg0.getSelectedItem()).getCubeItem() != null) {
                    CubeBrowserWindow.enableQmQuery(true);
                    CubeBrowserWindow.enableMdxQuery(true);
                }
                else {
                    CubeBrowserWindow.enableQmQuery(false);
                    CubeBrowserWindow.enableMdxQuery(false);
                }
                
                
            }

        });
        baseLayoutPanel.layout();
    }

    public Tree getCubeTree() {
        return cubeTree;
    }

    /**
     * Loads all cubes of all active connections
     */
    public final void loadCubes() {
        ServiceFactory.getSessionInstance().getActiveConnections(Pat.getSessionID(),
                new AsyncCallback<CubeConnection[]>() {

                    public void onFailure(final Throwable arg0) {
                        cubeTree.clear();
                        MessageBox.error(Pat.CONSTANTS.error(), MessageFactory.getInstance()
                                .failedActiveConnection(arg0.getLocalizedMessage()));
                    }

                    public void onSuccess(final CubeConnection[] connections) {
                        refreshCubeMenu(connections);
                    }
                });
    }

    /**
     * Loads all cubes of the given connection
     * 
     * @param connectionId
     */
    public final void loadCubes(final String connectionId) {
        ServiceFactory.getSessionInstance().getConnection(Pat.getSessionID(), connectionId,
                new AsyncCallback<CubeConnection>() {

                    public void onFailure(final Throwable arg0) {
                        cubeTree.clear();
                        MessageBox.error(Pat.CONSTANTS.error(), MessageFactory.getInstance()
                                .failedCubeList(arg0.getLocalizedMessage()));
                    }

                    public void onSuccess(final CubeConnection connection) {
                        final CubeConnection[] connections = new CubeConnection[] {connection};
                        refreshCubeMenu(connections);
                    }
                });
    }

    /**
     * Generates a cube list for the Cube Menu.
     */
    private final void refreshCubeMenu(final CubeConnection[] connections) {
        cubeTree.clear();
        for (final CubeConnection connection : connections) {
            final TreeItem cubesList = new CubeTreeItem(connection, null);
            cubeTree.addItem(cubesList);
            ServiceFactory.getDiscoveryInstance().getCubes(Pat.getSessionID(), connection.getId(),
                    new AsyncCallback<CubeItem[]>() {
                        public void onFailure(final Throwable arg0) {
                            MessageBox.error(Pat.CONSTANTS.error(), MessageFactory.getInstance()
                                    .failedCubeList(arg0.getLocalizedMessage()));
                        }

                        public void onSuccess(final CubeItem[] cubeItm) {
                            for (final CubeItem element2 : cubeItm) {
                                cubesList.addItem(new CubeTreeItem(connection, element2));
                            }
                            cubesList.setState(true);
                        }
                    });
        }

    }

}
