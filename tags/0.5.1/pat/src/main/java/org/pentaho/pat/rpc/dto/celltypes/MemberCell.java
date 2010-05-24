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
package org.pentaho.pat.rpc.dto.celltypes;

import java.io.Serializable;
import java.util.List;

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.widgets.CellLabelPanel;
import org.pentaho.pat.client.ui.windows.DimensionBrowserWindow;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.DrillType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class MemberCell extends AbstractBaseCell implements Serializable, IsSerializable {
    private static final long serialVersionUID = 1L;

    private boolean lastRow = false;

    private boolean expanded = false;

    private String parentDimension = null;

    private String parentMember = null;

    private MemberCell rightOf = null;

    private String uniqueName;

    private int childMemberCount;

    private String rightOfDimension;

    private List<String> memberPath;

    private final static String CELLBUTTON = "cellButton"; //$NON-NLS-1$

    /**
     * 
     * Blank Constructor for Serializable niceness, don't use it.
     * 
     */
    public MemberCell() {
    }

    /**
     * 
     * Creates a member cell.
     * 
     * @param b
     * @param c
     */
    public MemberCell(final boolean b, final boolean c) {
        super();
        this.right = b;
        this.sameAsPrev = c;
    }

    /**
     * Returns true if this is the bottom row of the column headers(supposedly).
     * 
     * @return the lastRow
     */
    public boolean isLastRow() {
        return lastRow;
    }

    /**
     * 
     * Set true if this is the bottom row of the column headers.
     * 
     * @param lastRow
     *            the lastRow to set
     */
    public void setLastRow(final boolean lastRow) {
        this.lastRow = lastRow;
    }

    public void setParentDimension(final String parDim) {
        parentDimension = parDim;
    }

    public String getParentDimension() {
        return parentDimension;
    }

    /**
     *TODO JAVADOC
     * 
     * @return the expanded
     */
    public boolean isExpanded() {
        return expanded;
    }

    /**
     * 
     *TODO JAVADOC
     * 
     * @param expanded
     *            the expanded to set
     */
    public void setExpanded(final boolean expanded) {
        this.expanded = expanded;
    }

    /**
     *TODO JAVADOC
     * 
     * @param parentMember
     */
    public void setParentMember(final String parentMember) {

        this.parentMember = parentMember;

    }

    public String getParentMember() {
        return parentMember;
    }

    /**
     *TODO JAVADOC
     * 
     * @param uniqueName
     */
    public void setUniquename(final String uniqueName) {

        this.uniqueName = uniqueName;

    }

    public String getUniqueName() {
        return uniqueName;
    }

    /**
     *TODO JAVADOC
     * 
     * @param childMemberCount
     */
    public void setChildMemberCount(final int childMemberCount) {
        this.childMemberCount = childMemberCount;
    }

    public int getChildMemberCount() {
        return childMemberCount;
    }

    /**
     *TODO JAVADOC
     * 
     * @param memberCell
     */
    public void setRightOf(MemberCell memberCell) {
        this.rightOf = memberCell;

    }

    public MemberCell getRightOf() {
        return rightOf;
    }

    @Override
    public HorizontalPanel getLabel() {
        final CellLabelPanel cellPanel = new CellLabelPanel(MemberCell.this);
        /**
         * NON IMAGE BUNDLE IMAGES TO GET AROUND AN IE BUG
         */
        if (this.getRawValue() != null) {

            final Image cellButton = new Image() {
                public void onBrowserEvent(Event event) {
                    if (DOM.eventGetType(event) == Event.ONCLICK) {
                        DimensionBrowserWindow.displayDimension(Pat.getCurrQuery(), getParentDimension());
                    }
                }
            };

            cellButton.setUrl(GWT.getModuleBaseURL() + "dimbrowser.png");

            Image drillButton = null;
            if ((MemberCell.this).getChildMemberCount() > 0) {
                drillButton = new Image() {

                    public void onBrowserEvent(Event event) {
                        if (DOM.eventGetType(event) == Event.ONCLICK) {
                            ServiceFactory.getQueryInstance().drillPosition(Pat.getSessionID(), Pat.getCurrQuery(),
                                    DrillType.POSITION, MemberCell.this, new AsyncCallback<Object>() {

                                        public void onFailure(Throwable arg0) {
                                            MessageBox.alert(ConstantFactory.getInstance().error(), MessageFactory
                                                    .getInstance().failedDrill(arg0.getLocalizedMessage()));
                                        }

                                        public void onSuccess(Object arg0) {
                                            ServiceFactory.getQueryInstance().executeQuery(Pat.getSessionID(),
                                                    Pat.getCurrQuery(), new AsyncCallback<CellDataSet>() {

                                                        public void onFailure(Throwable arg0) {

                                                            MessageBox.alert(ConstantFactory.getInstance().error(),
                                                                    MessageFactory.getInstance().failedQuery(
                                                                            arg0.getLocalizedMessage()));

                                                        }

                                                        public void onSuccess(CellDataSet arg0) {
                                                            GlobalConnectionFactory.getQueryInstance()
                                                                    .getQueryListeners().fireQueryExecuted(cellPanel,
                                                                            Pat.getCurrQuery(), arg0);

                                                        }

                                                    });
                                        }

                                    });
                        }
                    }
                };

                if (isExpanded()) {
                    drillButton.setUrl(GWT.getModuleBaseURL() + "closeButton.png");
                } else {
                    drillButton.setUrl(GWT.getModuleBaseURL() + "drill.png");
                }

            }
            final Label cellLabel = new Label(getFormattedValue());

            cellLabel.addMouseOverHandler(new MouseOverHandler() {

                public void onMouseOver(MouseOverEvent arg0) {
                    if (!getRawValue().equals("")) { //$NON-NLS-1$
                        cellButton.setVisible(true);
                    }

                }
            });

            final Timer dimbrowserTimer = new Timer() {
                @Override
                public void run() {
                    cellButton.setVisible(false);
                }
            };

            cellLabel.addMouseOutHandler(new MouseOutHandler() {

                public void onMouseOut(MouseOutEvent arg0) {
                    if (!getRawValue().equals("")) { //$NON-NLS-1$
                        dimbrowserTimer.schedule(800);
                    }

                }
            });

            if (drillButton != null) {
                cellPanel.add(drillButton);
            }
            cellPanel.add(cellLabel);

            cellPanel.setWidth("100%"); //$NON-NLS-1$

            cellButton.addStyleName(CELLBUTTON);

            if (!getRawValue().equals("")) { //$NON-NLS-1$
                cellPanel.add(cellButton);
                cellButton.setVisible(false);
            }
        }
        return cellPanel;
    }

    /**
     *TODO JAVADOC
     * 
     * @param name
     */
    public void setRightOfDimension(String name) {

        this.rightOfDimension = name;

    }

    public String getRightOfDimension() {
        return this.rightOfDimension;
    }

    public void setMemberPath(List<String> memberPath) {
        this.memberPath = memberPath;

    }

    public List<String> getMemberPath() {
        return memberPath;
    }

}