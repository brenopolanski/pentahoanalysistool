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
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.ToolButton.ToolButtonStyle;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.widgets.MDXRichTextArea;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.DrillType;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Creates a properties panel, the properties panel controls things like drill method and pivot.
 * 
 * @created Aug 8, 2009
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public class PropertiesPanel extends LayoutComposite {

    private class LayoutCommand implements Command {

        private final transient Region region;

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
    
    private class DrillCommand implements Command {
	
	private final transient DrillType drillType;

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
    
    private final transient DataPanel dataPanel;

    /**
     * PropertiesPanel Constructor.
     * 
     * @param dPanel
     * 
     */
    public PropertiesPanel(final DataPanel dPanel) {
	super();
        this.dataPanel = dPanel;
        final LayoutPanel rootPanel = getLayoutPanel();

        final LayoutPanel mainPanel = new LayoutPanel();
        mainPanel.addStyleName("pat-propertiesPanel"); //$NON-NLS-1$
        mainPanel.setLayout(new BoxLayout(Orientation.VERTICAL));
        final ToolButton mdxButton = new ToolButton(ConstantFactory.getInstance().showMDX());
        mdxButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                ServiceFactory.getQueryInstance().getMdxForQuery(Pat.getSessionID(), Pat.getCurrQuery(),
                        new AsyncCallback<String>() {

                            public void onFailure(final Throwable arg0) {
                                MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                        .failedPivot(arg0.getLocalizedMessage()));
                            }

                            public void onSuccess(final String mdx) {
                                // TODO localize + externalize strings + extract show mdx window
                                final WindowPanel wp = new WindowPanel(ConstantFactory.getInstance().mdx());
                                final LayoutPanel wpLayoutPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
                                wpLayoutPanel.setSize("450px", "200px"); //$NON-NLS-1$ //$NON-NLS-2$
                                final MDXRichTextArea mdxArea = new MDXRichTextArea();

                                mdxArea.setText(mdx);

                                wpLayoutPanel.add(mdxArea, new BoxLayoutData(1, 0.9));
                                final ToolButton closeBtn = new ToolButton(ConstantFactory.getInstance().close());
                                closeBtn.addClickHandler(new ClickHandler() {
                                    public void onClick(final ClickEvent arg0) {
                                        wp.hide();
                                    }

                                });
                                final ToolButton mdxBtn = new ToolButton(ConstantFactory.getInstance().newMdxQuery());
                                mdxBtn.addClickHandler(new ClickHandler() {
                                    public void onClick(final ClickEvent arg0) {

                                        final Widget widget = MainTabPanel.getSelectedWidget();
                                        if (widget != null && widget instanceof OlapPanel) {
                                            ((OlapPanel) widget).getCubeItem();
                                            final MdxPanel mdxpanel = new MdxPanel(((OlapPanel) widget).getCubeItem(),
                                                    Pat.getCurrConnection(), mdxArea.getText());
                                            MainTabPanel.displayContentWidget(mdxpanel);
                                        }

                                        wp.hide();

                                    }
                                });
                                final LayoutPanel wpButtonPanel = new LayoutPanel(new BoxLayout(Orientation.HORIZONTAL));

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

            }
        });
        mdxButton.setEnabled(true);

        final ToolButton showParentButton = new ToolButton(ConstantFactory.getInstance().showParent());
        showParentButton.setStyle(ToolButtonStyle.CHECKBOX);
        showParentButton.setEnabled(false);
        showParentButton.setChecked(true);

        final ToolButton showFiltersButton = new ToolButton(ConstantFactory.getInstance().showFilters());
        showFiltersButton.setStyle(ToolButtonStyle.CHECKBOX);
        showFiltersButton.setEnabled(false);

        final ToolButton showPropsButton = new ToolButton(ConstantFactory.getInstance().showProperties());
        showPropsButton.setStyle(ToolButtonStyle.CHECKBOX);
        showPropsButton.setEnabled(false);

        final ToolButton hideBlanksButton = new ToolButton(ConstantFactory.getInstance().hideBlankCells());
        hideBlanksButton.setStyle(ToolButtonStyle.CHECKBOX);
        hideBlanksButton.addClickHandler(new ClickHandler(){

	    public void onClick(final ClickEvent arg0) {
		
		ServiceFactory.getQueryInstance().setNonEmpty(Pat.getSessionID(), Pat.getCurrQuery(), hideBlanksButton.isChecked(), new AsyncCallback<CellDataSet>(){

		    public void onFailure(final Throwable arg0) {
			
			MessageBox.error(ConstantFactory.getInstance().error(), "Failed to set non empty");
			
		    }

		    public void onSuccess(final CellDataSet arg0) {
			
			if(hideBlanksButton.isChecked()){
			    hideBlanksButton.setText("Show Blank Cells");
			}
			else{
			    hideBlanksButton.setText(ConstantFactory.getInstance().hideBlankCells());
			}
			 GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryExecuted(
                                 PropertiesPanel.this, Pat.getCurrQuery(), arg0);
		    }
		    
		});
		
	    }
            
        });

        final ToolButton pivotButton = new ToolButton(ConstantFactory.getInstance().pivot());
        pivotButton.setStyle(ToolButtonStyle.CHECKBOX);
        pivotButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                ServiceFactory.getQueryInstance().swapAxis(Pat.getSessionID(), Pat.getCurrQuery(),
                        new AsyncCallback<CellDataSet>() {

                            public void onFailure(final Throwable arg0) {

                                MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                        .failedPivot(arg0.getLocalizedMessage()));
                            }

                            public void onSuccess(final CellDataSet arg0) {

                                GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryExecuted(
                                        PropertiesPanel.this, Pat.getCurrQuery(), arg0);

                            }

                        });

            }
        });
        pivotButton.setEnabled(true);

        final ToolButton layoutMenuButton = new ToolButton(ConstantFactory.getInstance().layout());
        layoutMenuButton.setStyle(ToolButtonStyle.MENU);

        final PopupMenu layoutMenuBtnMenu = new PopupMenu();
        layoutMenuBtnMenu.addItem(ConstantFactory.getInstance().grid(), new LayoutCommand(null));
        layoutMenuBtnMenu.addItem(ConstantFactory.getInstance().chart(), new LayoutCommand(Region.CENTER));
        layoutMenuBtnMenu.addItem(ConstantFactory.getInstance().top(), new LayoutCommand(Region.NORTH));
        layoutMenuBtnMenu.addItem(ConstantFactory.getInstance().bottom(), new LayoutCommand(Region.SOUTH));
        layoutMenuBtnMenu.addItem(ConstantFactory.getInstance().left(), new LayoutCommand(Region.WEST));
        layoutMenuBtnMenu.addItem(ConstantFactory.getInstance().right(), new LayoutCommand(Region.EAST));

        layoutMenuButton.setMenu(layoutMenuBtnMenu);

        final ToolButton drillMenuButton = new ToolButton("Drill");
        drillMenuButton.setStyle(ToolButtonStyle.MENU);

        final PopupMenu drillMenuBtnMenu = new PopupMenu();
        drillMenuBtnMenu.addItem(ConstantFactory.getInstance().drillPosition(), new DrillCommand(DrillType.POSITION));
        drillMenuBtnMenu.addItem(ConstantFactory.getInstance().drillReplace(), new DrillCommand(DrillType.REPLACE));

        final ToolButton drillThruButton = new ToolButton(ConstantFactory.getInstance().drillThrough());
        drillThruButton.setStyle(ToolButtonStyle.CHECKBOX);
        drillThruButton.setEnabled(false);

        mainPanel.add(layoutMenuButton);
        mainPanel.add(drillMenuButton);
        mainPanel.add(mdxButton);
        mainPanel.add(showParentButton);
        mainPanel.add(showFiltersButton);
        mainPanel.add(showPropsButton);
        mainPanel.add(hideBlanksButton);
        mainPanel.add(pivotButton);

        rootPanel.add(mainPanel);

    }
}
