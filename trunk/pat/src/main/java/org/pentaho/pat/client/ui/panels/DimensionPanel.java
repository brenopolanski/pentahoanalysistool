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
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.pentaho.pat.client.Application;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.widgets.DimensionDropWidget;
import org.pentaho.pat.client.util.dnd.FlexTableRowDragController;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.IAxis;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;

/**
 * The dimension panel creates the axis dimension lists and facilitates the drag and drop of those widgets
 * 
 * @created Aug 8, 2009
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public class DimensionPanel extends LayoutComposite {

    private transient final DimensionDropWidget dimDropUnused;

    private transient final DimensionDropWidget dimDropRow;

    private transient final DimensionDropWidget dimDropCol;

    private transient final DimensionDropWidget dimDropFilter;

    private transient final FlexTableRowDragController tRDragController = Application.tableRowDragController;

    private final static String ROOT_STYLE_NAME = "pat-DimensionPanel"; //$NON-NLS-1$

    /**
     * Returns the drag controller.
     * 
     * @return tableRowDragController
     */
    public FlexTableRowDragController getTableRowDragController() {
        return tRDragController;
    }

    /**
     * Creates a dimension panel this lists all the available dimensions on the various available axis.
     * 
     */
    public DimensionPanel() {
	super();
        //this.tableRowDragController = new FlexTableRowDragController(Application.getMainPanel());

        final LayoutPanel rootPanel = getLayoutPanel();

        final ScrollLayoutPanel mainPanel = new ScrollLayoutPanel();

        mainPanel.setLayout(new BoxLayout(Orientation.VERTICAL));
        mainPanel.addStyleName(ROOT_STYLE_NAME);

        dimDropUnused = new DimensionDropWidget(ConstantFactory.getInstance().unused(), IAxis.UNUSED, tRDragController);
        dimDropRow = new DimensionDropWidget(ConstantFactory.getInstance().rows(), IAxis.ROWS, tRDragController);
        dimDropCol = new DimensionDropWidget(ConstantFactory.getInstance().columns(), IAxis.COLUMNS, tRDragController);
        dimDropFilter = new DimensionDropWidget(ConstantFactory.getInstance().filter(), IAxis.FILTER, tRDragController);

        mainPanel.add(dimDropUnused, new BoxLayoutData(1, -1));
        mainPanel.add(dimDropRow, new BoxLayoutData(1, -1));
        mainPanel.add(dimDropCol, new BoxLayoutData(1, -1));
        mainPanel.add(dimDropFilter, new BoxLayoutData(1, -1));

        final Button executeButton = new Button(ConstantFactory.getInstance().executeQuery());
        executeButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                ServiceFactory.getQueryInstance().executeQuery(Pat.getSessionID(), Pat.getCurrQuery(),
                        new AsyncCallback<CellDataSet>() {

                    public void onFailure(final Throwable arg0) {
                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                .failedQuery(arg0.getLocalizedMessage()));
                    }

                    public void onSuccess(final CellDataSet result1) {
                        GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryExecuted(
                                DimensionPanel.this, Pat.getCurrQuery(), result1);
                    }

                });

            }

        });
        mainPanel.add(executeButton);
        rootPanel.add(mainPanel);
        

    }

}
