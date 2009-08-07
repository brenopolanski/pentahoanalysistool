package org.pentaho.pat.client.ui.panels;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.Application.ApplicationImages;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CubeConnection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class CubeMenu extends LayoutComposite {

    /** The main menu. */
    private Tree cubeTree;

    public CubeMenu() {
        super();
        this.sinkEvents(Event.BUTTON_LEFT | Event.BUTTON_RIGHT | Event.ONCONTEXTMENU);
        final LayoutPanel baseLayoutPanel = getLayoutPanel();


        final ApplicationImages treeImages = GWT.create(ApplicationImages.class);
        cubeTree = new Tree(treeImages);
        cubeTree.setAnimationEnabled(true);
        cubeTree.addStyleName(Pat.DEF_STYLE_NAME + "-menu"); //$NON-NLS-1$
        cubeTree.setSize("100%", "100%"); //$NON-NLS-1$ //$NON-NLS-2$

        baseLayoutPanel.add(cubeTree);
        loadCubes();


    }



    /**
     * Generates a cube list for the Cube Menu.
     */
    private final void refreshCubeMenu(CubeConnection[] connections) {
        cubeTree.clear();
        for(int i =0; i< connections.length;i++) {
            final TreeItem cubesList = cubeTree.addItem(connections[i].getName());
            ServiceFactory.getDiscoveryInstance().getCubes(Pat.getSessionID(), connections[i].getId(), new AsyncCallback<String[]>() {
                public void onFailure(final Throwable arg0) {
                    MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedCubeList(arg0.getLocalizedMessage()));
                }

                public void onSuccess(final String[] o) {

                    for (final String element2 : o) {
                        cubesList.addItem(element2);
                        //setupMainMenuOption(cubesList, new QueryPanel(element2), Pat.IMAGES.cube());
                    }
                    cubesList.setState(true);
                }
            });
        }

    }

    public final void loadCubes() {
        ServiceFactory.getSessionInstance().getActiveConnections(Pat.getSessionID(),  new AsyncCallback<CubeConnection[]>() {

            public void onFailure(Throwable arg0) {
                MessageBox.error(ConstantFactory.getInstance().error(),"Error loading connections");
            }

            public void onSuccess(CubeConnection[] connections) {
                refreshCubeMenu(connections);
            }
        });
    }

    public final void loadCubes(String connectionId) {
        ServiceFactory.getSessionInstance().getConnection(Pat.getSessionID(), connectionId, new AsyncCallback<CubeConnection>() {

            public void onFailure(Throwable arg0) {
                MessageBox.error(ConstantFactory.getInstance().error(),"Error loading connections");
            }

            public void onSuccess(CubeConnection connection) {
                CubeConnection[] connections = new CubeConnection[] { connection };
                refreshCubeMenu(connections);
            }
        });
    }


//    /**
//     * Fires on browser clicks.
//     * @param event the event
//     */
//    @Override
//    public void onBrowserEvent(final Event event) {
//        super.onBrowserEvent(event);
//        final TreeItem item = cubeTree.getSelectedItem();
//        if (listener != null && item != null && !item.getText().equals("connection 1") && !item.getText().equals("connection 2")) { //$NON-NLS-1$ //$NON-NLS-2$
//
//        }
//
//        cubeTree.setSelectedItem(item, false);
//        cubeTree.ensureSelectedItemVisible();
//
//
//    }





}
