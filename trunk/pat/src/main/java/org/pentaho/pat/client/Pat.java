package org.pentaho.pat.client;

import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.CollapsedListener;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.Viewport;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.pentaho.pat.client.images.PatImages;
import org.pentaho.pat.client.panels.CubeExplorerPanel;
import org.pentaho.pat.client.panels.NorthPanel;
import org.pentaho.pat.client.panels.SouthPanel;
import org.pentaho.pat.client.panels.ToolBarPanel;
import org.pentaho.pat.client.util.ConstantFactory;
import org.pentaho.pat.client.util.MessageFactory;
import org.pentaho.pat.client.widgets.OlapPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * 
 * @author Tom Barber
 * 
 */
public class Pat implements CollapsedListener, EntryPoint {
	private CubeExplorerPanel cubeExplorerPanel; // Dimension GUI for defining the
	// query model
	private OlapPanel olapPanel; // Main Window
	private NorthPanel northPanel; // Contains the MDX and Filter GUI
	private SouthPanel drillPanel; // Contains the drill down information
	public static LayoutPanel borderLayoutPanel;
	private LayoutPanel boxPanel;
	public static final PatImages IMAGES = (PatImages) GWT.create(PatImages.class);
	
	
	public Pat() {
		super();

		init();
	}

	public void init() {
		// Create Surrounding Box Panel and Border Layout Panel
		boxPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
		borderLayoutPanel = new LayoutPanel(new BorderLayout());

		// Create a layout panel to align the widgets
		final ToolBarPanel toolBarPanel = new ToolBarPanel();

		borderLayoutPanel.setPadding(0);

		// MDX(north) panel
		northPanel = new NorthPanel("MDX Panel");
		final ImageButton collapseBtn1 = new ImageButton(Caption.IMAGES
				.toolCollapseUp());
		northPanel.getHeader().add(collapseBtn1, CaptionRegion.RIGHT);

		collapseBtn1.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				borderLayoutPanel.setCollapsed(northPanel, !borderLayoutPanel
						.isCollapsed(northPanel));
				borderLayoutPanel.layout();
			}
		});

		borderLayoutPanel.add(northPanel, new BorderLayoutData(Region.NORTH,
				100, 10, 250));
		borderLayoutPanel.setCollapsed(northPanel, true);

		borderLayoutPanel.addCollapsedListener(northPanel, this);

		// Drill(south) panel
		drillPanel = new SouthPanel("Drill Data");

		final ImageButton collapseBtn2 = new ImageButton(Caption.IMAGES
				.toolCollapseDown());
		drillPanel.getHeader().add(collapseBtn2, CaptionRegion.RIGHT);

		collapseBtn2.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				borderLayoutPanel.setCollapsed(drillPanel, !borderLayoutPanel
						.isCollapsed(drillPanel));
				borderLayoutPanel.layout();
			}
		});

		borderLayoutPanel.add(drillPanel, new BorderLayoutData(Region.SOUTH,
				100, 10, 250));
		borderLayoutPanel.setCollapsed(drillPanel, true);

		borderLayoutPanel.addCollapsedListener(drillPanel, this);

		// Dimension(west) Panel
		cubeExplorerPanel = new CubeExplorerPanel(ConstantFactory.getInstance()
				.dimensionPanel());
		final ImageButton collapseBtn3 = new ImageButton(Caption.IMAGES
				.toolCollapseLeft());
		cubeExplorerPanel.getHeader().add(collapseBtn3, CaptionRegion.RIGHT);
		toolBarPanel.addConnectionListener(cubeExplorerPanel);
		
		collapseBtn3.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				borderLayoutPanel.setCollapsed(cubeExplorerPanel,
						!borderLayoutPanel.isCollapsed(cubeExplorerPanel));
				borderLayoutPanel.layout();
			}
		});

		borderLayoutPanel.add(cubeExplorerPanel, new BorderLayoutData(Region.WEST,
				100, 10, 250));
		borderLayoutPanel.setCollapsed(cubeExplorerPanel, true);

		borderLayoutPanel.addCollapsedListener(cubeExplorerPanel, this);

		// OLAP(center) panel
		olapPanel = new OlapPanel("blank");
		
		
		borderLayoutPanel.add(olapPanel, new BorderLayoutData(Region.CENTER,
				true));
		boxPanel.add(toolBarPanel, new BoxLayoutData(FillStyle.HORIZONTAL));
		boxPanel.add(borderLayoutPanel, new BoxLayoutData(FillStyle.BOTH));
	}

	public void onCollapsedChange(Widget sender) {
		// TODO Auto-generated method stub

	}

	public void onModuleLoad() {
		final Viewport viewport = new Viewport();

		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			public void onUncaughtException(Throwable e) {
				String str = "";
				e.printStackTrace();
				Window.alert(str + e);
			}
		});
		viewport.add(boxPanel);
		RootPanel.get().add(viewport);
	}

}
