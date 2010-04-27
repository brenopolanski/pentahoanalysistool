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

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.panels.windows.ConnectionManagerPanel;
import org.pentaho.pat.client.ui.widgets.CubeTreeItem;
import org.pentaho.pat.client.ui.windows.ConnectionManagerWindow;
import org.pentaho.pat.client.ui.windows.CubeBrowserWindow;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.rpc.dto.CubeItem;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MenuItemSeparator;

/**
 * Menu for PAT - MenuBar Style
 * @created Apr 27, 2010 
 * @since 0.7
 * @author Paul Stoellberger
 * 
 */
public class TopMenu extends MenuBar {

    /**
     * 
     */
    public TopMenu() {
        setup();
    }
    
    

    /**
     * @param vertical
     */
    public TopMenu(boolean vertical) {
        super(vertical);
        setup();
    }

    /**
     * @param images
     */
    public TopMenu(MenuBarImages images) {
        super(images);
        setup();
    }

    /**
     * @param vertical
     * @param images
     */
    public TopMenu(boolean vertical, MenuBarImages images) {
        super(vertical, images);
        setup();
    }
    
    public void setup() {

        this.clearItems();

        // Create a command that will execute on menu item selection
        Command menuCommand = new Command() {
            public void execute() {
                MessageBox.alert(Window.getTitle(), "exec 1");
            }
        };

        // Create a menu bar
        this.setAnimationEnabled(false);

        // Create a sub menu of recent documents
        MenuBar recentDocsMenu = new MenuBar(true);
        String[] recentDocs = { "recent1 ", "recent2" };
        for (int i = 0; i < recentDocs.length; i++) {
            recentDocsMenu.addItem(recentDocs[i], menuCommand);
        }

        // Create the file menu
        MenuBar fileMenu = new MenuBar(true);
        fileMenu.setAnimationEnabled(false);
        this.addItem(new MenuItem("File", fileMenu));
        String[] fileOptions = { "Open Query", "Save Query", "Save Query As ..." };
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
        this.addItem(conItem);

        final MenuBar cubeMenu = createCubesMenu();
        final MenuItem cubeItem = new MenuItem("Cubes", cubeMenu);
        this.addItem(cubeItem);

        
        // Create the help menu
        MenuBar helpMenu = new MenuBar(true);
        helpMenu.clearItems();
        this.addItem(new MenuItem("Help", helpMenu));
        
        
            helpMenu.addItem("About", new Command() {

                public void execute() {
                    // TODO Auto-generated method stub
                    
                }
                
            });
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
            MenuItem item = new MenuItem("(Loading ...)",new Command() {
                public void execute() {};
            });
            ConnectionsMenu.this.addItem(item);
            
            ServiceFactory.getSessionInstance().getConnections(Pat.getSessionID(),
                    new AsyncCallback<CubeConnection[]>() {

                        public void onFailure(final Throwable arg0) {
                            MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                    .failedLoadConnection(arg0.getLocalizedMessage()));
                        }

                        public void onSuccess(final CubeConnection[] connections) {
                            ConnectionsMenu.this.clearItems();
                            MenuItemSeparator mis = new MenuItemSeparator();
                            mis.getElement().setInnerText("Available Connections");
                            ConnectionsMenu.this.addSeparator(mis);

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
            MenuItem item = new MenuItem("(Loading ...)",new Command() {
                public void execute() {};
            });
            CubesMenu.this.addItem(item);
            
            ServiceFactory.getSessionInstance().getActiveConnections(Pat.getSessionID(),
                    new AsyncCallback<CubeConnection[]>() {

                        public void onFailure(final Throwable arg0) {
                            CubesMenu.this.clearItems();
                            MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                    .failedLoadConnection(arg0.getLocalizedMessage()));
                        }

                        public void onSuccess(final CubeConnection[] connections) {
                            CubesMenu.this.clearItems();
                            for (final CubeConnection connection : connections) {

                                ServiceFactory.getDiscoveryInstance().getCubes(Pat.getSessionID(), connection.getId(),
                                        new AsyncCallback<CubeItem[]>() {
                                            public void onFailure(final Throwable arg0) {
                                                MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                                        .failedCubeList(arg0.getLocalizedMessage()));
                                            }

                                            public void onSuccess(final CubeItem[] cubeItm) {
                                                CubesMenu.this.clearItems();
                                                for (final CubeItem cubeItem : cubeItm) {
                                                    HTML widget = new HTML((new CubeTreeItem(connection, cubeItem)).getHTML());
                                                     
                                                    Command cmd = new Command() {

                                                        public void execute() {
                                                            LogoPanel.spinWheel(true);
                                                            final OlapPanel olappanel = new OlapPanel(cubeItem, connection);
                                                            MainTabPanel.displayContentWidget(olappanel);

                                                        };
                                                    };
                                                    
                                                    MenuBar newQuery = new MenuBar(true);
                                                    newQuery.addItem(new MenuItem(ConstantFactory.getInstance().newQuery(), cmd));
                                                    newQuery.addItem(new MenuItem(ConstantFactory.getInstance().newMdxQuery(), new Command() {

                                                        public void execute() {
                                                            LogoPanel.spinWheel(true);
                                                            final MdxPanel mdxPanel = new MdxPanel(cubeItem, connection.getId());
                                                            MainTabPanel.displayContentWidget(mdxPanel);
                                                            
                                                        }

                                                    }));
                                                    
                                                    MenuItem item = new MenuItem(widget.getHTML(),true,newQuery);
                                                    item.setCommand(cmd);
                                                    
                                                    CubesMenu.this.addItem(item);
                                                }

                                                if (cubeItm.length < 1 || CubesMenu.this.getItems().size() == 0) {
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
