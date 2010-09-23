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
package org.pentaho.pat.client.ui.popups;

import java.util.ArrayList;

import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.PopupMenu;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.widgets.CellLabelPanel;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

/**
 *TODO JAVADOC
 * 
 * @author bugg
 * 
 */
public class CellModeMenu extends PopupMenu {

    public final static String CELL_MODE_MENU = "pat-CellModeMenu"; //$NON-NLS-1$
    public class ClearExcludeCommand implements Command {

        /*
         * (non-Javadoc)
         * 
         * @see com.google.gwt.user.client.Command#execute()
         */
        /**
         * The Command executed on click.
         */
        public final void execute() {
            final CellLabelPanel targetLabel = (CellLabelPanel) getSource();

            ServiceFactory.getQueryInstance().clearExclusion(Pat.getSessionID(), Pat.getCurrQuery(),
                    targetLabel.getMc().getParentDimension(), new AsyncCallback<Object>() {

                        public void onFailure(final Throwable arg0) {
                            // TODO Auto-generated method stub

                        }

                        public void onSuccess(final Object arg0) {
                            Pat.executeQuery(targetLabel, Pat.getCurrQuery());
                        }

                    });

            CellModeMenu.this.hide();
        }
    }

    public class ExcludeCommand implements Command {

        /*
         * (non-Javadoc)
         * 
         * @see com.google.gwt.user.client.Command#execute()
         */
        /**
         * The Command executed on click.
         */
        public final void execute() {
            final CellLabelPanel targetLabel = (CellLabelPanel) getSource();
            final ArrayList<String> memberList = new ArrayList<String>();
            memberList.addAll(targetLabel.getMc().getMemberPath());

            ServiceFactory.getQueryInstance().createExclusion(Pat.getSessionID(), Pat.getCurrQuery(),
                    targetLabel.getMc().getParentDimension(), memberList, new AsyncCallback<Object>() {

                        public void onFailure(final Throwable arg0) {
                            // TODO Auto-generated method stub

                        }

                        public void onSuccess(final Object arg0) {
                            Pat.executeQuery(targetLabel, Pat.getCurrQuery());
                        }

                    });

            CellModeMenu.this.hide();
        }
    }

    public class SortOrderCommand implements Command {

        /** The selection mode. */
        private final String sortOrder;

        /**
         * The Constructor.
         * 
         * @param selectionMode
         *            the selection mode
         */
        public SortOrderCommand(final String sortOrder) {
            this.sortOrder = sortOrder;
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.google.gwt.user.client.Command#execute()
         */
        /**
         * The Command executed on click.
         */
        public final void execute() {
            final CellLabelPanel targetLabel = (CellLabelPanel) getSource();

            ServiceFactory.getQueryInstance().setSortOrder(Pat.getSessionID(), Pat.getCurrQuery(),
                    targetLabel.getMc().getParentDimension(), sortOrder, new AsyncCallback<Object>() {

                        public void onFailure(final Throwable arg0) {

                            MessageBox.error(Pat.CONSTANTS.error(), MessageFactory.getInstance()
                                    .failedSetSortOrder(arg0.getLocalizedMessage()));
                        }

                        public void onSuccess(final Object arg0) {
                            Pat.executeQuery(targetLabel,Pat.getCurrQuery());
                        }

                    });
            CellModeMenu.this.hide();
        }
    }

    /** The source. */
    private static Widget source;

    public static Widget getSource() {
        return source;
    }

    public static void setSource(final Widget source2) {
        source = source2;
    }

    public CellModeMenu() {
        super();
        init();
        this.setStyleName(CELL_MODE_MENU);
    }

    private void init() {
        this.setAutoOpen(true);
//        this.addItem(new MenuItem(Pat.CONSTANTS.sortAZ(), new SortOrderCommand("ASC"))); //$NON-NLS-1$
//        this.addItem(new MenuItem(Pat.CONSTANTS.sortZA(), new SortOrderCommand("DESC"))); //$NON-NLS-1$
        this.addItem(new MenuItem(Pat.CONSTANTS.exclude(), new ExcludeCommand()));
        this.addItem(new MenuItem(Pat.CONSTANTS.clearExclusions(), new ClearExcludeCommand()));
    }

    /**
     * Show the context menu.
     * 
     * @param event
     *            the event
     * @param selectedItem
     *            the selected item
     */
    public final void showContextMenu(final Event event, final CellLabelPanel selectedItem) {

        setSource(selectedItem);

    }

    protected final String getDimensionName(final CellLabelPanel targetLabel) {
        final Tree tree = (Tree) targetLabel.getParent();
        final TreeItem rootItem = tree.getItem(0);
        final Label widget = (Label) rootItem.getWidget();

        return widget.getText();
    }
}
