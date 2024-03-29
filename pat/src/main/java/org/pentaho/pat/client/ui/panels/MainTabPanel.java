/*
 * Copyright (C) 2009 Thomas Barber
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

import java.util.ArrayList;
import java.util.List;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.DecoratedTabLayoutPanel;
import org.gwt.mosaic.ui.client.HTMLLabel;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.TextLabel;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.widgets.AbstractDataWidget;
import org.pentaho.pat.client.util.State;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Main Tab Panel Controls the tabs created to hold the various different types of containers, and allows closing of
 * them.
 * 
 * @created Aug 5, 2009
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 */
public class MainTabPanel extends LayoutComposite {

    private final static DecoratedTabLayoutPanel CONTENTWRAPPER = new DecoratedTabLayoutPanel();

    private static int counter = 0;

    /**
     *TODO JAVADOC
     * 
     */
    public MainTabPanel() {
        super();
        final LayoutPanel rootLayoutPanel = getLayoutPanel();
        // FIXME remove that and use style
        DOM.setStyleAttribute(rootLayoutPanel.getElement(), "background", "white"); //$NON-NLS-1$//$NON-NLS-2$
        DOM.setStyleAttribute(CONTENTWRAPPER.getElement(), "background", "white"); //$NON-NLS-1$ //$NON-NLS-2$

        CONTENTWRAPPER.addStyleName("pat-content-wrapper"); //$NON-NLS-1$
        CONTENTWRAPPER.addSelectionHandler(new SelectionHandler<Integer>() {
            public void onSelection(final SelectionEvent<Integer> selectEvent) {
                CONTENTWRAPPER.getWidget(selectEvent.getSelectedItem());
                final Widget _widget = CONTENTWRAPPER.getWidget(selectEvent.getSelectedItem());

                if (_widget instanceof OlapPanel || _widget instanceof MdxPanel) {
                    Pat.enableSaveButtons();
                    MainMenuBar.enableSave(true);
                    AbstractDataWidget widget = (AbstractDataWidget) _widget;
                    Pat.setCurrQuery(widget.getQueryId());
                    Pat.setCurrConnectionId(widget.getConnectionId());
                    Pat.setCurrCubeName(widget.getCube());
                    Pat.setCurrCube(widget.getCubeItem());
                    Pat.setCurrConnection(widget.getCubeConnection());
                    Pat.setCurrPanelType(widget.getPanelType());

                } else {
                    Pat.disableSaveButtons();
                    MainMenuBar.enableSave(false);
                }

            }
        });

        rootLayoutPanel.add(CONTENTWRAPPER);
    }

    public static Widget getSelectedWidget() {
        return CONTENTWRAPPER.getWidget(CONTENTWRAPPER.getSelectedTab());
    }

    /**
     * Adds a new Tab to the contentPanel.
     * 
     * @param content
     * @param tabName
     */
    public static void addContent(final AbstractDataWidget content, String tabName) {

        if (content != null) {

            Integer countName = 0;
            Boolean done = false;
            List<String> tabnames = new ArrayList<String>();

            for(Widget widget : CONTENTWRAPPER) {
                if (widget.getTitle() != null && widget.getTitle().length() > 0) {
                    tabnames.add(widget.getTitle());
                }
            }
            while (!done) {
                done = true;
                if(tabnames.contains(tabName) && countName == 0) {
                    countName++;
                    done = false;
                }
                if (tabnames.contains(tabName + " (" + countName + ")")) {
                    countName++;
                    done = false;
                }

            }

            if (countName > 0) {
                tabName = tabName + " (" + countName + ")";
            }
            content.setTitle(tabName);

            if (Pat.getApplicationState().getMode().equals(State.Mode.ONECUBE)) {
                CONTENTWRAPPER.add(content, tabName);
            } else {
                CONTENTWRAPPER.add(content, tabCloseLabel(content, tabName));
            }
            CONTENTWRAPPER.selectTab(counter);
            counter++;
            CONTENTWRAPPER.layout();
        }

    }

    /**
     * 
     *TODO JAVADOC
     * 
     * @param content
     */
    public static void displayContentWidget(final AbstractDataWidget content) {
        if (content != null) {
            addContent(content, content.getName());
        }
    }

    /**
     * 
     * Creates a new Closeable tab.
     * 
     * @param widget
     * @param string
     * @param idx
     * @return a closeable tab for a tab panel.
     */
    private static Widget tabCloseLabel(final Widget widget, final String string) {
        final HorizontalPanel hPanel = new HorizontalPanel();
        final TextLabel label = new TextLabel(string);
        DOM.setStyleAttribute(label.getElement(), "whiteSpace", "nowrap"); //$NON-NLS-1$ //$NON-NLS-2$
        final ImageButton closeBtn = new ImageButton(Pat.IMAGES.windowButtonClose());
        closeBtn.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                final int widgetIndex = CONTENTWRAPPER.getWidgetIndex(widget);
                if (widgetIndex == CONTENTWRAPPER.getSelectedTab()) {
                    CONTENTWRAPPER.remove(widgetIndex);
                    CONTENTWRAPPER.selectTab(widgetIndex - 1);
                } else {
                    CONTENTWRAPPER.remove(widgetIndex);
                    CONTENTWRAPPER.layout();
                }
                /*
                 * if (contentWrapper.getWidgetCount() == 1 || contentWrapper.getWidgetCount() == 0) { if
                 * (MainMenu.showNamedMenu(MenuItem.Cubes) == false) { MainMenu.getStackPanel().showStack(0); }
                 * MainMenu.getStackPanel().layout(); }
                 */
                counter--;
            }
        });
        hPanel.add(label);
        hPanel.add(new HTMLLabel("&nbsp&nbsp&nbsp")); //$NON-NLS-1$
        hPanel.add(closeBtn);
        return hPanel;
    }

}
