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

package org.pentaho.pat.client.ui.panels;

import org.gwt.mosaic.ui.client.ComboBox;
import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.PopupMenu;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.ToolButton.ToolButtonStyle;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.list.DefaultComboBoxModel;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextArea;

/**
 * Creates a properties panel, the properties panel controls things like drill method and pivot.
 * 
 * @created Aug 8, 2009
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public class PropertiesPanel extends LayoutComposite {

    /**
     * PropertiesPanel Constructor.
     * 
     */
    public PropertiesPanel() {
        final LayoutPanel rootPanel = getLayoutPanel();

        final LayoutPanel mainPanel = new LayoutPanel();
        mainPanel.addStyleName("pat-propertiesPanel"); //$NON-NLS-1$
        mainPanel.setLayout(new BoxLayout(Orientation.VERTICAL));
        final ToolButton mdxButton = new ToolButton(ConstantFactory.getInstance().showMDX());
        mdxButton.addClickHandler( new ClickHandler(){

            public void onClick(ClickEvent arg0) {
                ServiceFactory.getQueryInstance().getMdxForQuery(Pat.getSessionID(), Pat.getCurrQuery(), new AsyncCallback<String>(){

                    public void onFailure(Throwable arg0) {
                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedPivot(arg0.getLocalizedMessage()));
                    }

                    public void onSuccess(String mdx) {
                        // TODO localize + externalize strings + extract show mdx window
                        final WindowPanel wp = new WindowPanel(ConstantFactory.getInstance().mdx());
                        LayoutPanel wpLayoutPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
                        wpLayoutPanel.setSize("450px", "200px"); //$NON-NLS-1$ //$NON-NLS-2$
                        final TextArea mdxArea = new TextArea();

                        mdxArea.setText(mdx);

                        wpLayoutPanel.add(mdxArea, new BoxLayoutData(1,0.9));
                        ToolButton closeBtn = new ToolButton(ConstantFactory.getInstance().close());
                        closeBtn.addClickHandler(new ClickHandler() {
                            public void onClick(ClickEvent arg0) {
                                wp.hide();
                            }

                        });
                        ToolButton mdxBtn = new ToolButton(ConstantFactory.getInstance().executeQuery());
                        mdxBtn.addClickHandler(new ClickHandler() {
                            public void onClick(ClickEvent arg0) {
                                ServiceFactory.getQueryInstance().executeMdxQuery(Pat.getSessionID(), Pat.getCurrConnection(), mdxArea.getText(), new AsyncCallback<CellDataSet>() {

                                    public void onFailure(Throwable arg0) {
                                        MessageBox.error(ConstantFactory.getInstance().error(), arg0.getLocalizedMessage());
                                    }

                                    public void onSuccess(CellDataSet matrix) {
                                        GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryExecuted(new OlapPanel(), Pat.getCurrQuery(), matrix);

                                    }

                                });

                                wp.hide();

                            }
                        });
                        LayoutPanel wpButtonPanel = new LayoutPanel(new BoxLayout(Orientation.HORIZONTAL));

                        wpButtonPanel.add(mdxBtn);
                        wpButtonPanel.add(closeBtn);
                        wpLayoutPanel.add(wpButtonPanel);
                        wpLayoutPanel.layout();
                        wp.add(wpLayoutPanel);
                        wp.layout();
                        wp.pack();
                        wp.setSize("500px", "320px"); //$NON-NLS-1$ //$NON-NLS-2$
                        wp.center();



                    }

                });

            }});
        mdxButton.setEnabled(true);


        final ToolButton checkButton1 = new ToolButton(ConstantFactory.getInstance().showParent());
        checkButton1.setStyle(ToolButtonStyle.CHECKBOX);
        checkButton1.setEnabled(false);

        final ToolButton checkButton2 = new ToolButton(ConstantFactory.getInstance().showFilters());
        checkButton2.setStyle(ToolButtonStyle.CHECKBOX);
        checkButton2.setEnabled(false);

        final ToolButton checkButton3 = new ToolButton(ConstantFactory.getInstance().showProperties());
        checkButton3.setStyle(ToolButtonStyle.CHECKBOX);
        checkButton3.setEnabled(false);

        final ToolButton checkButton4 = new ToolButton(ConstantFactory.getInstance().hideBlankCells());
        checkButton4.setStyle(ToolButtonStyle.CHECKBOX);
        checkButton4.setEnabled(false);

        final ToolButton checkButton5 = new ToolButton(ConstantFactory.getInstance().pivot());
        checkButton5.setStyle(ToolButtonStyle.CHECKBOX);
        checkButton5.addClickHandler( new ClickHandler(){

            public void onClick(ClickEvent arg0) {
                ServiceFactory.getQueryInstance().swapAxis(Pat.getSessionID(), Pat.getCurrQuery(), new AsyncCallback<CellDataSet>(){

                    public void onFailure(Throwable arg0) {

                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedPivot(arg0.getLocalizedMessage()));
                    }

                    public void onSuccess(CellDataSet arg0) {

                        GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryExecuted(
                                PropertiesPanel.this, Pat.getCurrQuery(), arg0);


                    }

                });

            }});
        checkButton5.setEnabled(true);

        ToolButton menuButton = new ToolButton("Layout");
        menuButton.setStyle(ToolButtonStyle.MENU);

        Command cmd1 = new Command() {
            public void execute() {
              InfoPanel.show("Menu Button", "You selected a menu item!");
            }
          };

        PopupMenu menuBtnMenu = new PopupMenu();
        menuBtnMenu.addItem(ConstantFactory.getInstance().grid(), cmd1);
        menuBtnMenu.addItem(ConstantFactory.getInstance().chart(), cmd1);
        menuBtnMenu.addItem(ConstantFactory.getInstance().top(), cmd1);
        menuBtnMenu.addItem(ConstantFactory.getInstance().bottom(), cmd1);
        menuBtnMenu.addItem(ConstantFactory.getInstance().left(), cmd1);
        menuBtnMenu.addItem(ConstantFactory.getInstance().right(), cmd1);
        

        menuButton.setMenu(menuBtnMenu);

        
        final ToolButton checkButton6 = new ToolButton(ConstantFactory.getInstance().drillThrough());
        checkButton6.setStyle(ToolButtonStyle.CHECKBOX);
        checkButton6.setEnabled(false);

        mainPanel.add(mdxButton);
        mainPanel.add(checkButton1);
        mainPanel.add(checkButton2);
        mainPanel.add(checkButton3);
        mainPanel.add(checkButton4);
        mainPanel.add(checkButton5);
        mainPanel.add(menuButton);

        rootPanel.add(mainPanel);
    }

}
