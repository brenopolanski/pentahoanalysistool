package org.pentaho.pat.client.ui.panels;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.Application.ApplicationImages;
import org.pentaho.pat.client.ui.widgets.CubeTreeItem;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CubeConnection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class CubeMenu extends LayoutComposite {

    /** The main menu. */
    private Tree cubeTree;

    private final ToolButton qmQueryButton = new ToolButton("New Query");

    public CubeMenu() {
        super();
        this.sinkEvents(Event.BUTTON_LEFT | Event.BUTTON_RIGHT | Event.ONCONTEXTMENU);
        final LayoutPanel baseLayoutPanel = getLayoutPanel();
        baseLayoutPanel.setLayout(new BoxLayout(Orientation.HORIZONTAL));

        final ApplicationImages treeImages = GWT.create(ApplicationImages.class);
        cubeTree = new Tree(treeImages);
        cubeTree.setAnimationEnabled(true);
        cubeTree.addStyleName(Pat.DEF_STYLE_NAME + "-cubemenu"); //$NON-NLS-1$

        baseLayoutPanel.add(cubeTree, new BoxLayoutData(FillStyle.BOTH));
        loadCubes();

        cubeTree.addSelectionHandler(new SelectionHandler<TreeItem>() {

            public void onSelection(SelectionEvent<TreeItem> arg0) {
                cubeTree.ensureSelectedItemVisible();
                qmQueryButton.setEnabled(true);
                // CubeTreeItem selected = (CubeTreeItem)cubeTree.getSelectedItem().getWidget();
                // if (selected.getType() == CubeTreeItem.ItemType.CONNECTION) {
                // MessageBox.info("Selected Item", "Connection: " + selected.getConnectionId());
                // }
                // if (selected.getType() == CubeTreeItem.ItemType.CUBE) {
                // MessageBox.info("Selected Item", "Connection: " + selected.getConnectionId() + " Cube: " +
                // selected.getCube());
                // }

            }

        });

        LayoutPanel newQueryButtonPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));

        qmQueryButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent arg0) {
                if (cubeTree.getSelectedItem() != null) {
                    final CubeTreeItem selected = (CubeTreeItem) cubeTree.getSelectedItem().getWidget();
                    if (selected.getType() == CubeTreeItem.ItemType.CONNECTION) {
                        MessageBox.info("Selected Item", "Connection: " + selected.getConnectionId());
                    }
                    if (selected.getType() == CubeTreeItem.ItemType.CUBE) {
                        MessageBox.info("Selected Item", "Connection: " + selected.getConnectionId() + " Cube: "
                                + selected.getCube());
                        ServiceFactory.getQueryInstance().createNewQuery(Pat.getSessionID(),
                                selected.getConnectionId(), selected.getCube(), new AsyncCallback<String>() {

                                    public void onFailure(Throwable arg0) {
                                        // TODO Warn
                                        MessageBox.alert("Warning", "Query Not Created");
                                    }

                                    public void onSuccess(String arg0) {
                                        // TODO Open Query Tab
                                        OlapPanel olappanel = new OlapPanel(selected.getCube());
                                        MainTabPanel.displayContentWidget(olappanel);
                                    }

                                });
                    }
                }
            }
        });
        qmQueryButton.setEnabled(false);
        newQueryButtonPanel.add(qmQueryButton);
        baseLayoutPanel.add(newQueryButtonPanel, new BoxLayoutData(FillStyle.VERTICAL));

    }

    /**
     * Generates a cube list for the Cube Menu.
     */
    private final void refreshCubeMenu(final CubeConnection[] connections) {
        cubeTree.clear();
        for (int i = 0; i < connections.length; i++) {
            final TreeItem cubesList = cubeTree.addItem(new CubeTreeItem(connections[i], null));
            final CubeConnection connection = connections[i];

            ServiceFactory.getDiscoveryInstance().getCubes(Pat.getSessionID(), connections[i].getId(),
                    new AsyncCallback<String[]>() {
                        public void onFailure(final Throwable arg0) {
                            MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                    .failedCubeList(arg0.getLocalizedMessage()));
                        }

                        public void onSuccess(final String[] o) {

                            for (final String element2 : o) {
                                cubesList.addItem(new CubeTreeItem(connection, element2));
                            }
                            cubesList.setState(true);
                        }
                    });
        }

    }

    public final void loadCubes() {
        ServiceFactory.getSessionInstance().getActiveConnections(Pat.getSessionID(),
                new AsyncCallback<CubeConnection[]>() {

                    public void onFailure(Throwable arg0) {
                        MessageBox.error(ConstantFactory.getInstance().error(), "Error loading connections");
                    }

                    public void onSuccess(CubeConnection[] connections) {
                        refreshCubeMenu(connections);
                    }
                });
    }

    public final void loadCubes(String connectionId) {
        ServiceFactory.getSessionInstance().getConnection(Pat.getSessionID(), connectionId,
                new AsyncCallback<CubeConnection>() {

                    public void onFailure(Throwable arg0) {
                        MessageBox.error(ConstantFactory.getInstance().error(), "Error loading connections");
                    }

                    public void onSuccess(CubeConnection connection) {
                        CubeConnection[] connections = new CubeConnection[] {connection};
                        refreshCubeMenu(connections);
                    }
                });
    }

}
