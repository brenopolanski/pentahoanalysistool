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

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.util.ButtonHelper;
import org.gwt.mosaic.ui.client.util.ButtonHelper.ButtonLabelType;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.panels.windows.ConnectionManagerPanel;
import org.pentaho.pat.client.ui.widgets.CubeTreeItem;
import org.pentaho.pat.client.ui.windows.ConnectionManagerWindow;
import org.pentaho.pat.client.ui.windows.CubeBrowserWindow;
import org.pentaho.pat.client.ui.windows.LoadWindow;
import org.pentaho.pat.client.ui.windows.SaveWindow;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.rpc.dto.CubeItem;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MenuItemSeparator;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Menu Bar
 * 
 * @created Jul 29, 2009
 * @since 0.5.0
 * @author Paul Stoellberger
 * 
 */
public class MainMenuBar extends LayoutComposite {

    private final LayoutPanel rootPanel = getLayoutPanel();

    private final static ToolButton SAVEBUTTON = new ToolButton(ButtonHelper.createButtonLabel(Pat.IMAGES.cube(),
            ConstantFactory.getInstance().save(), ButtonLabelType.TEXT_ON_BOTTOM));

    private final static ToolButton SAVECDABUTTON = new ToolButton(ButtonHelper.createButtonLabel(Pat.IMAGES.cube(),
            ConstantFactory.getInstance().save() + " as CDA", ButtonLabelType.TEXT_ON_BOTTOM));
    // TODO replace with proper icon set; connections icon(create a button widget that can be duplicated across all
    // cases)
    final static ToolButton CONNECTIONBUTTON = new ToolButton(ButtonHelper.createButtonLabel(Pat.IMAGES.databases(),
            ConstantFactory.getInstance().connections(), ButtonLabelType.TEXT_ON_BOTTOM));

    final static ToolButton CUBEBUTTON = new ToolButton(ButtonHelper.createButtonLabel(Pat.IMAGES.cube(), ConstantFactory
            .getInstance().cubes(), ButtonLabelType.TEXT_ON_BOTTOM));

    final static ToolButton LOADBUTTON = new ToolButton(ButtonHelper.createButtonLabel(Pat.IMAGES.cube(), ConstantFactory
            .getInstance().load(), ButtonLabelType.TEXT_ON_BOTTOM));

    final static ToolButton LOGOUTBUTTON = new ToolButton(ConstantFactory.getInstance().logout());

    private static MenuBar menu = null;


    /**
     * Initializes the Menu Bar contents
     */
    public MainMenuBar() {
        super();
        final LogoPanel logoPanel = new LogoPanel();
        rootPanel.setLayout(new BoxLayout(Orientation.HORIZONTAL));
        rootPanel.setWidth("100%"); //$NON-NLS-1$
        //        addConnectionsButton();
        //        addCubesButton();
        //        addSaveButton();
        //        if (Pat.isPlugin()) {
        //            addSaveCdaButton();
        //        }
        //        addLoadButton();

        //        addLogoutButton();
        addMenu();
        rootPanel.add(logoPanel, new BoxLayoutData(FillStyle.HORIZONTAL));

        rootPanel.addStyleName("pat-menuBar"); //$NON-NLS-1$
    }

    /**
     * 
     * Adds a menu to the menubar
     */
    private void addMenu() {
        menu = createMenuBar();
        rootPanel.add(menu, new BorderLayoutData(Region.SOUTH));
    }
    /**
     * 
     * Adds a Connections button which controls the connection window.
     * 
     */
    private void addConnectionsButton() {
        CONNECTIONBUTTON.addStyleName("pat-toolButton"); //$NON-NLS-1$
        // FIXME remove that and use style
        DOM.setStyleAttribute(CONNECTIONBUTTON.getElement(), "background", "white"); //$NON-NLS-1$ //$NON-NLS-2$

        CONNECTIONBUTTON.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                ConnectionManagerWindow.display();
            }
        });
        CONNECTIONBUTTON.setEnabled(false);
        rootPanel.add(CONNECTIONBUTTON, new BoxLayoutData(FillStyle.VERTICAL));
    }

    /**
     * 
     * Adds a button which generates the cube browser.
     * 
     */
    private void addCubesButton() {

        CUBEBUTTON.addStyleName("pat-toolButton"); //$NON-NLS-1$
        // FIXME remove that and use style
        DOM.setStyleAttribute(CUBEBUTTON.getElement(), "background", "white"); //$NON-NLS-1$ //$NON-NLS-2$

        CUBEBUTTON.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                CubeBrowserWindow.display();
            }
        });
        CUBEBUTTON.setEnabled(false);
        rootPanel.add(CUBEBUTTON, new BoxLayoutData(FillStyle.VERTICAL));
    }

    /**
     * 
     * Adds a currently unusable save button.
     * 
     */
    private void addSaveButton() {
        // TODO replace with proper icon set; connections icon(create a button widget that can be duplicated across all
        // cases)
        SAVEBUTTON.addStyleName("pat-toolButton"); //$NON-NLS-1$
        // FIXME remove that and use style
        DOM.setStyleAttribute(SAVEBUTTON.getElement(), "background", "white"); //$NON-NLS-1$ //$NON-NLS-2$
        SAVEBUTTON.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                SaveWindow.display();
            }
        });
        SAVEBUTTON.setEnabled(false);
        rootPanel.add(SAVEBUTTON, new BoxLayoutData(FillStyle.VERTICAL));
    }

    private void addSaveCdaButton() {
        // TODO replace with proper icon set; connections icon(create a button widget that can be duplicated across all
        // cases)
        SAVECDABUTTON.addStyleName("pat-toolButton"); //$NON-NLS-1$
        // FIXME remove that and use style
        DOM.setStyleAttribute(SAVECDABUTTON.getElement(), "background", "white"); //$NON-NLS-1$ //$NON-NLS-2$
        SAVECDABUTTON.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                SaveWindow.displayCDA();
            }
        });
        SAVECDABUTTON.setEnabled(false);
        rootPanel.add(SAVECDABUTTON, new BoxLayoutData(FillStyle.VERTICAL));
    }


    private void addLoadButton() {
        LOADBUTTON.addStyleName("pat-toolButton"); //$NON-NLS-1$
        // FIXME remove that and use style
        DOM.setStyleAttribute(LOADBUTTON.getElement(), "background", "white"); //$NON-NLS-1$ //$NON-NLS-2$
        LOADBUTTON.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                LoadWindow.display();
            }
        });
        LOADBUTTON.setEnabled(false);
        rootPanel.add(LOADBUTTON, new BoxLayoutData(FillStyle.VERTICAL));
    }

    private void addLogoutButton() {
        LOGOUTBUTTON.addStyleName("pat-toolButton"); //$NON-NLS-1$
        // FIXME remove that and use style
        DOM.setStyleAttribute(LOGOUTBUTTON.getElement(), "background", "white"); //$NON-NLS-1$ //$NON-NLS-2$
        LOGOUTBUTTON.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                if (!Pat.isPlugin()) {
                    Window.Location.assign(Pat.getBaseUrl()+"logout");
                }

            }
        });
        if (!Pat.isPlugin()) {
            rootPanel.add(LOGOUTBUTTON, new BoxLayoutData(FillStyle.VERTICAL));
        }

    }

    public static void enableConnect(final Boolean enabled) {
        CONNECTIONBUTTON.setEnabled(enabled);
    }
    public static void enableSave(final Boolean enabled) {
        SAVEBUTTON.setEnabled(enabled);
        SAVECDABUTTON.setEnabled(enabled);
    }
    public static void enableLoad(final Boolean enabled) {
        LOADBUTTON.setEnabled(enabled);
    }

    public static void enableCube(final Boolean enabled) {
        CUBEBUTTON.setEnabled(enabled);
    }

    private MenuBar createMenuBar() {

        MenuBar menu = new MenuBar();
        menu.clearItems();

        // Create a command that will execute on menu item selection
        Command menuCommand = new Command() {
            public void execute() {
                MessageBox.alert(Window.getTitle(), "exec 1");
            }
        };

        // Create a menu bar


        menu.setAnimationEnabled(false);

        // Create a sub menu of recent documents
        MenuBar recentDocsMenu = new MenuBar(true);
        String[] recentDocs = { "recent1 ", "recent2" };
        for (int i = 0; i < recentDocs.length; i++) {
            recentDocsMenu.addItem(recentDocs[i], menuCommand);
        }

        // Create the file menu
        MenuBar fileMenu = new MenuBar(true);
        fileMenu.setAnimationEnabled(false);
        menu.addItem(new MenuItem("filecat1", fileMenu));
        String[] fileOptions = { "file option 1", "file option2" };
        for (int i = 0; i < fileOptions.length; i++) {
            if (i == 1) {
                fileMenu.addSeparator();
                fileMenu.addItem(fileOptions[i], recentDocsMenu);
                fileMenu.addSeparator();
            } else {
                fileMenu.addItem(fileOptions[i], menuCommand);
            }
        }

        final MenuBar conMenu = createConnectionMenu();
        final MenuItem conItem = new MenuItem("Connections", conMenu);
        menu.addItem(conItem);

        final MenuBar cubeMenu = createCubesMenu();
        final MenuItem cubeItem = new MenuItem("Cubes", cubeMenu);
        menu.addItem(cubeItem);

        
        // Create the help menu
        MenuBar helpMenu = new MenuBar(true);
        menu.addSeparator();
        menu.addItem(new MenuItem("helpcat", helpMenu));
        String[] helpOptions = { "About" };
        for (int i = 0; i < helpOptions.length; i++) {
            helpMenu.addItem(helpOptions[i], menuCommand);
        }

        return menu;
    }



    public final MenuBar createConnectionMenu() {
        final ConnectionsMenu connectionMenu = new ConnectionsMenu(true);
        connectionMenu.setAnimationEnabled(true);
        return connectionMenu;

    }

    public final MenuBar createCubesMenu() {
        final CubesMenu cubesMenu = new CubesMenu(true);
        cubesMenu.setAnimationEnabled(true);
        return cubesMenu;

    }
    
    private class ConnectionsMenu extends MenuBar {

        ConnectionsMenu(Boolean isVertical) {
            super(isVertical);
        }

        @Override
        protected void onAttach() {
            // TODO Auto-generated method stub
            super.onAttach();
            refresh();
        }

        public void refresh() {
            ConnectionsMenu.this.clearItems();
            MenuItemSeparator mis = new MenuItemSeparator();
            mis.getElement().setInnerText("Available Connections");
            ConnectionsMenu.this.addSeparator(mis);

            ServiceFactory.getSessionInstance().getConnections(Pat.getSessionID(),
                    new AsyncCallback<CubeConnection[]>() {

                        public void onFailure(final Throwable arg0) {
                            MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                    .failedLoadConnection(arg0.getLocalizedMessage()));
                        }

                        public void onSuccess(final CubeConnection[] connections) {
                            for (final CubeConnection con : connections) {
                                final Command cmd = new Command() {

                                    public void execute() {
                                        ConnectionManagerPanel.connectEvent(con.getId(), con.isConnected(),false); 
                                    }

                                };
                                HTML widget = new HTML((con.isConnected() == true ? Pat.IMAGES.connect().getHTML() : Pat.IMAGES.disconnect().getHTML())
                                        + " " + con.getName() );
                                MenuItem item = new MenuItem(widget.getHTML(),true, cmd);

                                ConnectionsMenu.this.addItem(item);
                            }

                            if (connections.length < 1) {
                                MenuItem item = new MenuItem("(No Connections)",new Command() {
                                    public void execute() {};
                                });
                                ConnectionsMenu.this.addItem(item);
                            }

                            ConnectionsMenu.this.addSeparator();
                            ConnectionsMenu.this.addItem("Manage Connections", new Command() {

                                public void execute() {
                                    ConnectionManagerWindow.display();
                                }

                            });

                        }
                    });
        }
    }

    private class CubesMenu extends MenuBar {

        CubesMenu(Boolean isVertical) {
            super(isVertical);
        }

        @Override
        protected void onAttach() {
            // TODO Auto-generated method stub
            super.onAttach();
            refresh();
        }

        public void refresh() {
            CubesMenu.this.clearItems();
            ServiceFactory.getSessionInstance().getActiveConnections(Pat.getSessionID(),
                    new AsyncCallback<CubeConnection[]>() {

                        public void onFailure(final Throwable arg0) {
                            MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                    .failedLoadConnection(arg0.getLocalizedMessage()));
                        }

                        public void onSuccess(final CubeConnection[] connections) {

                            for (final CubeConnection connection : connections) {

                                ServiceFactory.getDiscoveryInstance().getCubes(Pat.getSessionID(), connection.getId(),
                                        new AsyncCallback<CubeItem[]>() {
                                            public void onFailure(final Throwable arg0) {
                                                MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                                        .failedCubeList(arg0.getLocalizedMessage()));
                                            }

                                            public void onSuccess(final CubeItem[] cubeItm) {
                                                for (final CubeItem element2 : cubeItm) {
                                                    HTML widget = new HTML((new CubeTreeItem(connection, element2)).getHTML());
                                                    MenuItem item = new MenuItem(widget.getHTML(),true, new Command() {

                                                        public void execute() {
                                                            LogoPanel.spinWheel(true);
                                                            final OlapPanel olappanel = new OlapPanel(element2, connection);
                                                            MainTabPanel.displayContentWidget(olappanel);

                                                        }

                                                    });
                                                    CubesMenu.this.addItem(item);
                                                }

                                                if (cubeItm.length < 1) {
                                                    MenuItem item = new MenuItem("(No Cubes)",new Command() {
                                                        public void execute() {};
                                                    });
                                                    CubesMenu.this.addItem(item);
                                                }
                                                
                                                CubesMenu.this.addSeparator();
                                                CubesMenu.this.addItem("Cube Window", new Command() {

                                                    public void execute() {
                                                        CubeBrowserWindow.display();
                                                    }

                                                });


                                            }
                                        });
                            }



                        }
                    });
        }
    }




}
