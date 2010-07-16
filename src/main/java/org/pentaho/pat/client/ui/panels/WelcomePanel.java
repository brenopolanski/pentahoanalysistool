/*
 * Copyright (C) 2009 Paul Stoellberger
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA *
 */

package org.pentaho.pat.client.ui.panels;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Alignment;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.widgets.AbstractDataWidget;
import org.pentaho.pat.client.util.PanelUtil.PanelType;
import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.rpc.dto.CubeItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;

/**
 * Basic Welcome Panel that currently opens the PAT Homepage.
 * 
 * @created Jun 26, 2009
 * @since
 * @author Paul Stoellberger
 */
public class WelcomePanel extends AbstractDataWidget {

    /** Name. */
    private String name;

    /**
     * The PAT Welcome Panel. Currently allows connection editing and opening of the PAT Wiki.
     * 
     */
    public WelcomePanel() {
        super();
        initializeWidget();
    }

    /**
     * Constructor pass panel Name.
     * 
     * @param name
     *            the name
     */
    public WelcomePanel(final String name) {
        super();
        this.name = name;
        initializeWidget();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.deprecated.ui.widgets.DataWidget#getDescription()
     */
    /**
     * Get the panel description.
     * 
     * @return null
     */
    @Override
    public final String getDescription() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.deprecated.ui.widgets.DataWidget#getName()
     */
    /**
     * get the Panel Name.
     * 
     * @return name;
     */
    @Override
    public final String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.deprecated.ui.widgets.DataWidget#onInitialize()
     */
    /**
     * Initialization routine.
     * 
     * @return layoutPanel
     */
    public final Widget onInitialize() {
        final BoxLayout boxLayout = new BoxLayout();
        boxLayout.setOrientation(Orientation.VERTICAL);
        final LayoutPanel layoutPanel = new LayoutPanel(boxLayout);
        // FIXME remove that and use style
        DOM.setStyleAttribute(layoutPanel.getElement(), "background", "white"); //$NON-NLS-1$ //$NON-NLS-2$
        ((BoxLayout) layoutPanel.getLayout()).setAlignment(Alignment.CENTER);

       /* final LayoutPanel buttonBar = new LayoutPanel(new BoxLayout());
        ((BoxLayout) buttonBar.getLayout()).setAlignment(Alignment.CENTER);
        buttonBar.setWidgetSpacing(20);
        final ToolButton patwikiBtn = new ToolButton(ButtonHelper.createButtonLabel(Pat.IMAGES.help_index(),
                ConstantFactory.getInstance().wiki(), ButtonLabelType.TEXT_ON_RIGHT), new ClickHandler() {
            public void onClick(final ClickEvent arg0) {
                Window.open("http://code.google.com/p/pentahoanalysistool/wiki/StartPage?tm=6", "_blank", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
        });

        buttonBar.add(patwikiBtn);
*/
//        layoutPanel.add(buttonBar, new BoxLayoutData(-1.0, -1.0));

        String url = "welcome.html";
        if (Pat.isPlugin()) {
            url = GWT.getModuleBaseURL().substring(0, GWT.getModuleBaseURL().indexOf("content"));
            url = url != null && url.length() > 0 ? url + "content/pat-res/pat/welcome.html" : url;
        }
        
        Frame frame = new Frame(url);
        
        
        layoutPanel.add(frame, new BoxLayoutData(FillStyle.BOTH));
        
        return layoutPanel;
    }

    /**
     * 
     * Set the panel name.
     * 
     * @param name
     */
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    protected void initializeWidget() {
        getLayoutPanel().add(onInitialize());

    }

    @Override
    public String getConnectionId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getCube() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CubeConnection getCubeConnection() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CubeItem getCubeItem() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PanelType getPanelType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getQueryId() {
        // TODO Auto-generated method stub
        return null;
    }
}