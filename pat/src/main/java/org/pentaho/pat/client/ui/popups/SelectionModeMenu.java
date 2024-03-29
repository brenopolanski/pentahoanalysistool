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

import java.util.Arrays;
import java.util.List;

import org.gwt.mosaic.ui.client.PopupMenu;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.widgets.DimensionSimplePanel;
import org.pentaho.pat.client.ui.widgets.MeasureLabel;
import org.pentaho.pat.client.ui.widgets.MemberSelectionLabel;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
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
public class SelectionModeMenu extends PopupMenu {

    public final static String SELECTION_MODE_MENU = "pat-SelectionModeMenu"; //$NON-NLS-1$
    public class SelectionModeClearCommand implements Command {

        /*
         * (non-Javadoc)
         * 
         * @see com.google.gwt.user.client.Command#execute()
         */
        /**
         * Code to execute on click.
         */
        public final void execute() {

            final MemberSelectionLabel targetLabel = (MemberSelectionLabel) getSource();
            final String dimName = targetLabel.getDimension();
            final List<String> dimSelections = Arrays.asList(targetLabel.getFullPath());

/*            ServiceFactory.getQueryInstance().clearSelection(Pat.getSessionID(), Pat.getCurrQuery(), dimName, new AsyncCallback<Object>() {
                        public void onFailure(final Throwable caught) {
                            MessageBox.error(Pat.CONSTANTS.error(), MessageFactory.getInstance()
                                    .noSelectionCleared(caught.getLocalizedMessage()));

                        }

                        public void onSuccess(final Object result) {
                            targetLabel.setSelectionMode(CLEAR);
                            DimensionBrowserWindow.getDimensionMenuPanel().syncTreeAndList(targetLabel, CLEAR);

                        }
                    });*/
            SelectionModeMenu.this.hide();
        }
    }

    public class SelectionModeCommand implements Command {

        /** The selection mode. */
        private int selectionMode = -1;

        /**
         * The Constructor.
         * 
         * @param selectionMode
         *            the selection mode
         */
        public SelectionModeCommand(final int selectionMode) {
            this.selectionMode = selectionMode;
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
            final MemberSelectionLabel targetLabel = (MemberSelectionLabel) getSource();
            final String dimName = targetLabel.getDimension();

            final List<String> dimSelections = Arrays.asList(targetLabel.getFullPath());

            final String selection = setSelectionMode(selectionMode);
    //        ServiceFactory.getQueryInstance().createSelection(Pat.getSessionID(), Pat.getCurrQuery(), , selection, new AsyncCallback<StringTree>() {

                   //     public void onFailure(final Throwable arg0) {
                   //         MessageBox.error(Pat.CONSTANTS.error(), MessageFactory.getInstance()
                  //                  .noSelectionSet(arg0.getLocalizedMessage()));

                  //      }

                  //      public void onSuccess(final StringTree arg0) {
                  //          targetLabel.setSelectionMode(selectionMode);
                    //        if(dimensionSimplePanel == null){
                   //         DimensionBrowserWindow.getDimensionMenuPanel().syncTreeAndList(targetLabel, selectionMode);
                   //         }
                   //         if(dimensionSimplePanel != null){
    //                        	updateChildren(selection);
                  //          }
                  //      }

                //    });

            SelectionModeMenu.this.hide();
        }
    }

    /** The Constant MEMBER. */
    public static final int MEMBER = 0;

    /** The Constant CHILDREN. */
    public static final int CHILDREN = 1;

    /** The Constant INCLUDE_CHILDREN. */
    public static final int INCLUDE_CHILDREN = 2;

    /** The Constant SIBLINGS. */
    public static final int SIBLINGS = 3;

    public static final int DESCENDANTS = 4;

    public static final int ANCESTORS = 5;

    /** The Constant CLEAR. */
    public static final int CLEAR = -1;

    /** The source. */
    private static Widget source;

    public static Widget getSource() {
        return source;
    }

    public static void setSource(final Widget source2) {
        source = source2;
    }


	private DimensionSimplePanel dimensionSimplePanel;

	private MeasureLabel label;
	
    public SelectionModeMenu() {
        super();
        init();
        this.setStyleName(SELECTION_MODE_MENU);
    }

    public SelectionModeMenu(DimensionSimplePanel dimensionSimplePanel, MeasureLabel label) {
    	super();
        init();
        this.setStyleName(SELECTION_MODE_MENU);
        this.dimensionSimplePanel = dimensionSimplePanel;
        this.source = source;
	}

	private void init() {
        this.setAutoOpen(true);
        this.addItem(new MenuItem(Pat.CONSTANTS.member(), new SelectionModeCommand(MEMBER)));
        this.addItem(new MenuItem(Pat.CONSTANTS.children(), new SelectionModeCommand(CHILDREN)));
        this.addItem(new MenuItem(Pat.CONSTANTS.includeChildren(), new SelectionModeCommand(
                INCLUDE_CHILDREN)));
        this.addItem(new MenuItem(Pat.CONSTANTS.siblings(), new SelectionModeCommand(SIBLINGS)));
        this.addItem(new MenuItem(Pat.CONSTANTS.descendants(), new SelectionModeCommand(DESCENDANTS)));
        this.addItem(new MenuItem(Pat.CONSTANTS.ancestors(), new SelectionModeCommand(ANCESTORS)));
        this.addItem(new MenuItem(Pat.CONSTANTS.clearSelections(), new SelectionModeClearCommand()));
    }

    public final String setSelectionMode(final int selectionMode) {
        String selection = ""; //$NON-NLS-1$
        switch (selectionMode) {
        case 0:
            selection = "MEMBER"; //$NON-NLS-1$
            break;
        case 1:
            selection = "CHILDREN"; //$NON-NLS-1$
            break;
        case 2:
            selection = "INCLUDE_CHILDREN"; //$NON-NLS-1$
            break;
        case 3:
            selection = "SIBLINGS"; //$NON-NLS-1$
            break;
        case 4:
            selection = "DESCENDANTS"; //$NON-NLS-1$
            break;
        case 5:
            selection = "ANCESTORS"; //$NON-NLS-1$

        default:
            break;
        }
        return selection;

    }

    /**
     * Show the context menu.
     * 
     * @param event
     *            the event
     * @param selectedItem
     *            the selected item
     */
    public final void showContextMenu(final Event event, final MemberSelectionLabel selectedItem) {

        setSource(selectedItem);

    }

    public final void showContextMenu(final MemberSelectionLabel selectedItem) {

        setSource(selectedItem);

    }

    protected final String getDimensionName(final MemberSelectionLabel targetLabel) {
        final Tree tree = (Tree) targetLabel.getParent();
        final TreeItem rootItem = tree.getItem(0);
        final Label widget = (Label) rootItem.getWidget();

        return widget.getText();
    }

    
}
