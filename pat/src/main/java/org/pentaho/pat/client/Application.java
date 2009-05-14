/*
 * Copyright (C) 2009 Tom Barber
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA *
 *
 * @created Apr 23, 2009 
 * @author Tom Barber
 */

 package org.pentaho.pat.client;

import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.Viewport;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.FillLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.ui.panels.MainMenu;
import org.pentaho.pat.client.ui.panels.ToolBarPanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.TreeImages;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

/**
 * <p>
 * The Application wrapper that includes a menu bar, title and content.
 * </p>
 * <h3>CSS Style Rules</h3> <ul class="css"> <li>.Application { Applied to the
 * entire Application }</li> <li>.Application-top { The top portion of the
 * Application }</li> <li>.Application-title { The title widget }</li> <li>
 * .Application-links { The main external links }</li> <li>.Application-options
 * { The options widget }</li> <li>.Application-menu { The main menu }</li> <li>
 * .Application-content-wrapper { The element around the content }</li> </ul>
 *
 * @author tom(at)wamonline.org.uk
 */

public class Application extends Viewport { // NOPMD
									  // by
									  // bugg
									  // on
									  // 21/04/09
									  // 05:50
    /**
     * Images used in the {@link Application}.
     */
    public interface ApplicationImages extends TreeImages {

	/**
	 * An image indicating a leaf.
	 *
	 * @return a prototype of this image
	 */
	@Resource("noimage.png")
	AbstractImagePrototype treeLeaf();
    }

    /**
     * A listener to handle events from the Application.
     */
    public interface ApplicationListener {

	/**
	 * Fired when a menu item is selected.
	 *
	 * @param item
	 *            the item that was selected
	 */
	void onMenuItemSelected(com.google.gwt.user.client.ui.TreeItem item);
    }

    /** The wrapper around the content. */
    private static LayoutPanel contentWrapper;

    /** The base style name. */
    public static final String DEF_STYLE_NAME = "Application"; //$NON-NLS-1$

    /**
     * Returns the Applications Content Wrapper.
     *
     * @return the Content Wrapper
     */
    public static LayoutPanel getContentWrapper() {
	return contentWrapper;
    }

    /**
     * Sets the Applications Content Wrapper.
     *
     * @param contentWrapper
     *            The Application Content Wrapper
     */
    public static void setContentWrapper(final LayoutPanel contentWrapper) {
	Application.contentWrapper = contentWrapper;
    }

    /** The Application Main Panel. */
    private final transient MainMenu mainPanel;

    /** The panel that contains the title widget and links. */
    private transient FlexTable topPanel;

    /** The Application's Toolbar Panel. */
    private transient ToolBarPanel toolBarPanel;

    /** Application LayoutPanel. */
    private transient LayoutPanel layoutPanel;

    /** The bottom Panel for the Application, contains the main panels. */
    private static LayoutPanel bottomPanel;

    /**
     * Gets the bottom panel.
     *
     * @return the bottom panel
     */
    public static LayoutPanel getBottomPanel() {
	return bottomPanel;
    }

    /**
     * Sets the bottom panel.
     *
     * @param bottomPanel
     *            Returns the bottom Panel(dimension and data container)
     */
    // TODO add again when really needed, otherwise delete
//    private static void setBottomPanel(final LayoutPanel bottomPanel) {
//	Application.bottomPanel = bottomPanel;
//    }

    /**
     * Set the {@link Widget} to display in the content area.
     *
     * @param content
     *            the content widget
     */
    public static void setContent(final Widget content) {
	Application.contentWrapper.clear();
	if (content != null) {
	    Application.contentWrapper.add(content);
	}
    }

    /**
     * Constructor.
     */
    
    public Application() {
	super();
	
	// Setup the main layout widget
	layoutPanel = getWidget();
	layoutPanel.setLayout(new BoxLayout(Orientation.VERTICAL));

		// Setup the top panel with the title and links
		createTopPanel();
		layoutPanel.add(topPanel, new BoxLayoutData(FillStyle.HORIZONTAL));
	bottomPanel = new LayoutPanel(new BorderLayout());
	layoutPanel.add(bottomPanel, new BoxLayoutData(FillStyle.BOTH));

	// Add the main menu
	
	final CaptionLayoutPanel westPanel = new CaptionLayoutPanel();

	final ImageButton collapseBtn = new ImageButton(Caption.IMAGES
		.toolCollapseLeft());
	westPanel.getHeader().add(collapseBtn, CaptionRegion.RIGHT);

	collapseBtn.addClickHandler(new ClickHandler() {
	    public void onClick(final ClickEvent click_event) {
		bottomPanel.setCollapsed(westPanel, !layoutPanel
			.isCollapsed(westPanel));
		bottomPanel.layout();
	    }
	});
	
	bottomPanel.add(westPanel, new BorderLayoutData(Region.WEST, 200, 10,
		250));
	// Add the content wrapper
	setContentWrapper(new LayoutPanel(new FillLayout()));
	contentWrapper.addStyleName(DEF_STYLE_NAME + "-content-wrapper"); //$NON-NLS-1$
	bottomPanel.add(contentWrapper);
	mainPanel = new MainMenu();
	westPanel.add(mainPanel);

	//setContent(null);

    }

    

	/**
     * Create the panel at the top of the page that contains the title and
     * links.
     */
    private void createTopPanel() {
	final boolean isRTL = LocaleInfo.getCurrentLocale().isRTL();
	topPanel = new FlexTable();
	topPanel.setCellPadding(0);
	topPanel.setCellSpacing(0);
	topPanel.setStyleName(DEF_STYLE_NAME + "-top"); //$NON-NLS-1$
	final FlexCellFormatter formatter = topPanel.getFlexCellFormatter();

	// Setup the toolbar
	if (Pat.getInitialState().getMode().isShowToolbar())
	{
		toolBarPanel = new ToolBarPanel();
		topPanel.setWidget(0, 0, toolBarPanel);
	}
	formatter.setStyleName(0, 0, DEF_STYLE_NAME + "-menu"); //$NON-NLS-1$

	formatter.setColSpan(0, 0, 2);

	// Setup the title cell
	setTitleWidget(null);
	formatter.setStyleName(1, 0, DEF_STYLE_NAME + "-title"); //$NON-NLS-1$

	// Setup the options cell
	setOptionsWidget(null);
	formatter.setStyleName(1, 1, DEF_STYLE_NAME + "-options"); //$NON-NLS-1$
	if (isRTL) {
	    formatter.setHorizontalAlignment(1, 1,
		    HasHorizontalAlignment.ALIGN_LEFT);
	} else {
	    formatter.setHorizontalAlignment(1, 1,
		    HasHorizontalAlignment.ALIGN_RIGHT);
	}

	// Align the content to the top
	topPanel.getRowFormatter().setVerticalAlign(0,
		HasVerticalAlignment.ALIGN_TOP);
	topPanel.getRowFormatter().setVerticalAlign(1,
		HasVerticalAlignment.ALIGN_TOP);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mosaic.ui.client.layout.HasLayoutManager#getPreferredSize()
     */
    /**
     * Gets the preferred size.
     *
     * @return the widgets preferred size
     */
    // TODO what was this good for?
    //public final int[] getPreferredSize() {
//	return getWidget().getPreferredSize();
//    }

    /**
     * Gets the title widget.
     *
     * @return the {@link Widget} used as the title
     */
    public final Widget getTitleWidget() {
	return topPanel.getWidget(0, 0);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.gwt.mosaic.ui.client.Viewport#getWidget()
     */
    /**
     * Gets the widget.
     *
     * @return the widget.
     */
    // TODO is it ok to cast to LayoutPanel here?
    @Override
    protected final LayoutPanel getWidget() {
    	return (LayoutPanel)super.getWidget();
    }
    
    /**
     * Set the {@link Widget} to use as options, which appear to the right of
     * the title bar.
     *
     * @param options
     *            the options widget
     */
    public final void setOptionsWidget(final Widget options) {
	topPanel.setWidget(1, 1, options);
    }

    /**
     * Set the {@link Widget} to use as the title bar.
     *
     * @param title
     *            the title widget
     */
    public final void setTitleWidget(final Widget title) {
	topPanel.setWidget(1, 0, title);
    }
    
    public MainMenu getMainPanel() {
		return mainPanel;
	}

}
