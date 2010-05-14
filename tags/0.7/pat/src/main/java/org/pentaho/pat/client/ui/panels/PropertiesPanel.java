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

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.PopupMenu;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.ToolButton.ToolButtonStyle;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.IQueryListener;
import org.pentaho.pat.client.ui.widgets.MDXRichTextArea;
import org.pentaho.pat.client.util.Operation;
import org.pentaho.pat.client.util.PanelUtil;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.rpc.dto.IAxis;
import org.pentaho.pat.rpc.dto.enums.DrillType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Widget;

/**
 * Creates a properties panel, the properties panel controls things like drill method and pivot.
 * 
 * @created Aug 8, 2009
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public class PropertiesPanel extends LayoutComposite implements IQueryListener {

	private final ToolButton exportButton;

	private final ToolButton mdxButton;
	
	private final ToolButton hideBlanksButton;
	
	private final ToolButton pivotButton;

	private final ToolButton layoutMenuButton;
	
	private final ToolButton drillReplaceButton;
	
	private final ToolButton drillPositionButton;
	
	private final ToolButton drillThroughButton;
	
	private final String queryId;
	
    private class LayoutCommand implements Command {

        private final Region region;

        
        public LayoutCommand(final Region region) {
            this.region = region;
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.google.gwt.user.client.Command#execute()
         */
        public void execute() {
            dataPanel.chartPosition(region);

        }

    }

    private static class DrillCommand implements Command {

        private final DrillType drillType;

        public DrillCommand(final DrillType drillType) {
            this.drillType = drillType;
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.google.gwt.user.client.Command#execute()
         */
        public void execute() {
            Pat.setDrillType(drillType);

        }

    }

    private final DataPanel dataPanel;

    /** Form element name of the file component. */
    private static final String FORM_NAME_QUERY = "query"; //$NON-NLS-1$

    /** Submit method of the Connection form. */
    private static final String FORM_METHOD = "POST"; //$NON-NLS-1$

    /** Defines the action of the form. */
    private static String FORM_ACTION = "export"; //$NON-NLS-1$
    static {
        if (GWT.getModuleBaseURL().indexOf("content/pat")> -1) {
            String url = GWT.getModuleBaseURL().substring(0, GWT.getModuleBaseURL().indexOf("content/pat")+11) + "/export";
            FORM_ACTION = url;
        }
    }


    /**
     * PropertiesPanel Constructor.
     * 
     * @param dPanel
     * 
     */
    public PropertiesPanel(final DataPanel dPanel, PanelUtil.PanelType pType) {
        super();
        this.dataPanel = dPanel;
        this.queryId = Pat.getCurrQuery();
        
        GlobalConnectionFactory.getQueryInstance().addQueryListener(this);
        
        final LayoutPanel rootPanel = getLayoutPanel();

        final ScrollLayoutPanel mainPanel = new ScrollLayoutPanel();
        mainPanel.addStyleName("pat-propertiesPanel"); //$NON-NLS-1$
        mainPanel.setLayout(new BoxLayout(Orientation.HORIZONTAL));

        final FormPanel formPanel = new FormPanel();
        formPanel.setAction(FORM_ACTION);
        formPanel.setMethod(FORM_METHOD);
        //formPanel.setEncoding(FORM_ENCODING);

        Hidden curQuery = new Hidden(FORM_NAME_QUERY);
        curQuery.setName(FORM_NAME_QUERY);
        curQuery.setValue(queryId);
        formPanel.add(curQuery);
        
        final Button executeButton = new Button(ConstantFactory.getInstance().executeQuery());
        executeButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                Pat.executeQuery(PropertiesPanel.this, queryId);
            }

        });
        

        exportButton = new ToolButton(ConstantFactory.getInstance().export());


        exportButton.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent arg0) {
                formPanel.submit();
            }
        });

        exportButton.setEnabled(false);
        
        mdxButton = new ToolButton(ConstantFactory.getInstance().showMDX());
        mdxButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                ServiceFactory.getQueryInstance().getMdxForQuery(Pat.getSessionID(), queryId,
                        new AsyncCallback<String>() {

                    public void onFailure(final Throwable arg0) {
                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                .failedPivot(arg0.getLocalizedMessage()));
                    }

                    public void onSuccess(final String mdx) {
                        final WindowPanel winPanel = new WindowPanel(ConstantFactory.getInstance().mdx());
                        final LayoutPanel wpLayoutPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
                        wpLayoutPanel.setSize("450px", "200px"); //$NON-NLS-1$ //$NON-NLS-2$
                        final MDXRichTextArea mdxArea = new MDXRichTextArea();

                        mdxArea.setText(mdx);

                        wpLayoutPanel.add(mdxArea, new BoxLayoutData(1, 0.9));
                        final ToolButton closeBtn = new ToolButton(ConstantFactory.getInstance().close());
                        closeBtn.addClickHandler(new ClickHandler() {
                            public void onClick(final ClickEvent arg0) {
                                winPanel.hide();
                            }

                        });
                        final ToolButton mdxBtn = new ToolButton(ConstantFactory.getInstance().newMdxQuery());
                        mdxBtn.addClickHandler(new ClickHandler() {
                            public void onClick(final ClickEvent arg0) {

                                final Widget widget = MainTabPanel.getSelectedWidget();
                                if (widget instanceof OlapPanel) {
                                    ((OlapPanel) widget).getCubeItem();
                                    final MdxPanel mdxpanel = new MdxPanel(((OlapPanel) widget).getCubeItem(),
                                            Pat.getCurrConnection(), mdxArea.getText());
                                    MainTabPanel.displayContentWidget(mdxpanel);
                                }

                                winPanel.hide();

                            }
                        });
                        final LayoutPanel wpButtonPanel = new LayoutPanel(new BoxLayout(Orientation.HORIZONTAL));

                        wpButtonPanel.add(mdxBtn);
                        wpButtonPanel.add(closeBtn);
                        wpLayoutPanel.add(wpButtonPanel);
                        wpLayoutPanel.layout();
                        winPanel.add(wpLayoutPanel);
                        winPanel.layout();
                        winPanel.pack();
                        winPanel.setSize("500px", "320px"); //$NON-NLS-1$ //$NON-NLS-2$
                        winPanel.center();

                    }

                });

            }
        });
        //mdxButton.setEnabled(false);

        hideBlanksButton = new ToolButton(ConstantFactory.getInstance().showBlankCells());
        hideBlanksButton.setStyle(ToolButtonStyle.CHECKBOX);
        hideBlanksButton.setChecked(true);
        hideBlanksButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {

                ServiceFactory.getQueryInstance().setNonEmpty(Pat.getSessionID(), queryId,
                        hideBlanksButton.isChecked(), new AsyncCallback<CellDataSet>() {

                    public void onFailure(final Throwable arg0) {

                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedNonEmpty());

                    }

                    public void onSuccess(final CellDataSet arg0) {

                        if (hideBlanksButton.isChecked()) {
                            hideBlanksButton.setText(ConstantFactory.getInstance().showBlankCells());
                        } else {
                            hideBlanksButton.setText(ConstantFactory.getInstance().hideBlankCells());
                        }
                        GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryExecuted(
                                PropertiesPanel.this, queryId, arg0);
                    }

                });

            }

        });

        hideBlanksButton.setEnabled(false);
        
        final ToolButton createScenarioButton = new ToolButton("Create Scenario");
        createScenarioButton.setStyle(ToolButtonStyle.CHECKBOX);
        createScenarioButton.setEnabled(false);
        createScenarioButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {

                ServiceFactory.getQueryInstance().alterCell(queryId, Pat.getSessionID(), Pat.getCurrScenario(), Pat.getCurrConnectionId(), "123", new AsyncCallback<CellDataSet>(){

                    public void onFailure(Throwable arg0) {
                        // TODO Auto-generated method stub

                    }

                    public void onSuccess(CellDataSet arg0) {
                        Pat.executeQuery(PropertiesPanel.this, queryId);
                    }

                });

                /*ServiceFactory.getSessionInstance().createNewScenario(Pat.getSessionID(), Pat.getCurrConnection(), new AsyncCallback<String>(){
                    public void onFailure(final Throwable arg0){
                	MessageBox.error(ConstantFactory.getInstance().error(), "Failed to set scenario");
                    }

                    public void onSuccess(String scenario){
                	createScenarioButton.setText(scenario);
                	Pat.setCurrScenario(scenario);
                    }
                });*/
            }
        });
        
        pivotButton = new ToolButton(ConstantFactory.getInstance().pivot());
        pivotButton.setStyle(ToolButtonStyle.CHECKBOX);
        pivotButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                ServiceFactory.getQueryInstance().swapAxis(Pat.getSessionID(), queryId,
                        new AsyncCallback<CellDataSet>() {

                    public void onFailure(final Throwable arg0) {

                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                .failedPivot(arg0.getLocalizedMessage()));
                    }

                    public void onSuccess(final CellDataSet arg0) {

                        GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryExecuted(
                                PropertiesPanel.this, queryId, arg0);
                        
                        GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryPivoted(PropertiesPanel.this, queryId);

                    }

                });

            }
        });        
        pivotButton.setEnabled(false);

        layoutMenuButton = new ToolButton(ConstantFactory.getInstance().chart());
        layoutMenuButton.setStyle(ToolButtonStyle.MENU);
        layoutMenuButton.setEnabled(false);
        
        final PopupMenu layoutMenuBtnMenu = new PopupMenu();
        layoutMenuBtnMenu.addItem(ConstantFactory.getInstance().grid(), new LayoutCommand(null));
        // layoutMenuBtnMenu.addItem(ConstantFactory.getInstance().chart(), new LayoutCommand(Region.CENTER));
        layoutMenuBtnMenu.addItem(ConstantFactory.getInstance().top(), new LayoutCommand(Region.NORTH));
        layoutMenuBtnMenu.addItem(ConstantFactory.getInstance().bottom(), new LayoutCommand(Region.SOUTH));
        layoutMenuBtnMenu.addItem(ConstantFactory.getInstance().left(), new LayoutCommand(Region.WEST));
        layoutMenuBtnMenu.addItem(ConstantFactory.getInstance().right(), new LayoutCommand(Region.EAST));

        layoutMenuButton.setMenu(layoutMenuBtnMenu);

        
        drillPositionButton = new ToolButton(ConstantFactory.getInstance().drillPosition());
        drillPositionButton.setStyle(ToolButtonStyle.RADIO);
        drillPositionButton.setEnabled(false);
        drillPositionButton.setChecked(true);
        drillPositionButton.addClickHandler(new ClickHandler() {
            
            public void onClick(ClickEvent arg0) {
                drillReplaceButton.setChecked(false);
                drillPositionButton.setChecked(true);

                (new DrillCommand(DrillType.POSITION)).execute();
            }
        });
        
        drillReplaceButton = new ToolButton(ConstantFactory.getInstance().drillReplace());
        drillReplaceButton.setStyle(ToolButtonStyle.RADIO);
        drillReplaceButton.setEnabled(false);
        drillReplaceButton.addClickHandler(new ClickHandler() {
            
            public void onClick(ClickEvent arg0) {
                drillPositionButton.setChecked(false);
                drillReplaceButton.setChecked(true);

                (new DrillCommand(DrillType.REPLACE)).execute();
            }
        });
        

//        drillPositionButton = new RadioButton(ConstantFactory.getInstance().drillPosition(),ConstantFactory.getInstance().drillPosition());
//        drillPositionButton.addClickHandler(new ClickHandler() {
//            
//            public void onClick(ClickEvent arg0) {
//                drillPositionButton.setValue(true);
//                drillReplaceButton.setValue(false);
//                
//                (new DrillCommand(DrillType.POSITION)).execute();
//            }
//        });
//        drillPositionButton.setName("DRILLSTYLE");
//        
//        drillReplaceButton = new RadioButton(ConstantFactory.getInstance().drillReplace(),ConstantFactory.getInstance().drillReplace());
//        drillReplaceButton.addClickHandler(new ClickHandler() {
//            
//            public void onClick(ClickEvent arg0) {
//                drillReplaceButton.setValue(true);
//                drillPositionButton.setValue(false);
//                
//                (new DrillCommand(DrillType.REPLACE)).execute();
//            }
//        });
//        drillReplaceButton.setName("DRILLSTYLE");

        drillThroughButton = new ToolButton(ConstantFactory.getInstance().drillThrough());
        drillThroughButton.setEnabled(false);
        drillThroughButton.setStyle(ToolButtonStyle.CHECKBOX);
        
        drillThroughButton.addClickHandler(new ClickHandler() {
            
            public void onClick(ClickEvent arg0) {
                if (drillThroughButton.isChecked()) {
                    GlobalConnectionFactory.getOperationInstance().getTableListeners().fireOperationExecuted(PropertiesPanel.this, queryId,  Operation.ENABLE_DRILLTHROUGH);
                }
                else
                {
                    GlobalConnectionFactory.getOperationInstance().getTableListeners().fireOperationExecuted(PropertiesPanel.this, queryId, Operation.DISABLE_DRILLTHROUGH);
                }
                
            }
        });
        
        if (pType == PanelUtil.PanelType.MDX) {
            mainPanel.add(exportButton, new BoxLayoutData(FillStyle.HORIZONTAL));        
            mainPanel.add(layoutMenuButton, new BoxLayoutData(FillStyle.HORIZONTAL));    
        }
        if (pType == PanelUtil.PanelType.QM) {
            mainPanel.add(executeButton, new BoxLayoutData(FillStyle.HORIZONTAL));
            mainPanel.add(exportButton, new BoxLayoutData(FillStyle.HORIZONTAL));        
            mainPanel.add(layoutMenuButton, new BoxLayoutData(FillStyle.HORIZONTAL));
            mainPanel.add(drillPositionButton, new BoxLayoutData(FillStyle.HORIZONTAL));
            mainPanel.add(drillReplaceButton, new BoxLayoutData(FillStyle.HORIZONTAL));
            mainPanel.add(mdxButton, new BoxLayoutData(FillStyle.HORIZONTAL));
            mainPanel.add(hideBlanksButton, new BoxLayoutData(FillStyle.HORIZONTAL));
            mainPanel.add(pivotButton, new BoxLayoutData(FillStyle.HORIZONTAL));
            mainPanel.add(drillThroughButton, new BoxLayoutData(FillStyle.HORIZONTAL));
//            mainPanel.add(createScenarioButton, new BoxLayoutData(FillStyle.HORIZONTAL));
        }
        mainPanel.add(formPanel, new BoxLayoutData(FillStyle.HORIZONTAL));
        rootPanel.add(mainPanel);

    }

    @Override
    protected void onLoad() {
        super.onLoad();
        if (Pat.getCurrDrillType() != null ) {
        if (Pat.getCurrDrillType().equals(DrillType.POSITION)) {
            drillReplaceButton.setChecked(false);
            drillPositionButton.setChecked(true);
        }
        if (Pat.getCurrDrillType().equals(DrillType.REPLACE)) {
            drillPositionButton.setChecked(false);
            drillReplaceButton.setChecked(true);
        }
        }
        else {
            drillReplaceButton.setChecked(false);
            drillPositionButton.setChecked(true);
        }
    }
    
    public void onQueryChange(Widget sender, int sourceRow, boolean isSourceRow, IAxis sourceAxis, IAxis targetAxis) {
        // TODO Auto-generated method stub
        
    }

    public void onQueryExecuted(String queryId, CellDataSet matrix) {

        if(queryId == this.queryId) {
            exportButton.setEnabled(true);
            mdxButton.setEnabled(true);
            hideBlanksButton.setEnabled(true);
            pivotButton.setEnabled(true);
            layoutMenuButton.setEnabled(true);
            drillPositionButton.setEnabled(true);
            drillReplaceButton.setEnabled(true);
            if (Pat.getCurrConnection().getConnectionType().equals(CubeConnection.ConnectionType.Mondrian)) {
                drillThroughButton.setEnabled(true);
            }
            drillThroughButton.setChecked(false);
            // TODO Enable the listener again, dunno why it breaks other stuff
//            GlobalConnectionFactory.getOperationInstance().getTableListeners().fireOperationExecuted(this, Operation.DISABLE_DRILLTHROUGH);
        }

    }

    public void onQueryPivoted(String queryId) {
        // TODO Auto-generated method stub
        
    }


    public void onQueryStartExecution(String queryId) {
        // TODO Auto-generated method stub
        
    }
}