package org.pentaho.pat.client.ui.panels;

import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.StackLayoutPanel;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.widgets.DataWidget;
import org.pentaho.pat.client.ui.widgets.DimensionDropWidget;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.rpc.beans.Axis;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class OlapPanel extends DataWidget {

	/**
	 *TODO JAVADOC
	 */
	private Grid grid;
	/**
	 * The widget used to display source code.
	 */
	/**
	 *TODO JAVADOC
	 */
	private HTML sourceWidget = null;

	/**
	 * The stack panel with the contents.
	 */
	/**
	 *TODO JAVADOC
	 */
	private StackLayoutPanel stackPanel;

	/**
	 *TODO JAVADOC
	 */
	private ScrollPanel panel1;

	/**
	 *TODO JAVADOC
	 */
	private String name;


	/**
	 *TODO JAVADOC
	 *
	 * @param name
	 */
	public OlapPanel(String name) {
		super();
		this.name = name;
	}

	/**
	 *TODO JAVADOC
	 *
	 * @param image
	 * @param text
	 * @return
	 */
	private String createTabBarCaption(AbstractImagePrototype image, String text) {
		StringBuffer sb = new StringBuffer();
		sb.append("<table cellspacing='0px' cellpadding='0px' border='0px'><thead><tr>"); //$NON-NLS-1$
		sb.append("<td valign='middle'>"); //$NON-NLS-1$
		sb.append(image.getHTML());
		sb.append("</td><td valign='middle' style='white-space: nowrap;'>&nbsp;"); //$NON-NLS-1$
		sb.append(text);
		sb.append("</td></tr></thead></table>"); //$NON-NLS-1$
		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.ui.widgets.DataWidget#onInitialize()
	 */
	@Override
	public Widget onInitialize() {
		final LayoutPanel layoutPanel = new LayoutPanel(new BorderLayout());

		// MDX(north) panel
		final NorthPanel northPanel = new NorthPanel("MDX Panel"); //$NON-NLS-1$
		final ImageButton collapseBtn1 = new ImageButton(Caption.IMAGES.toolCollapseUp());
		northPanel.getHeader().add(collapseBtn1, CaptionRegion.RIGHT);

		collapseBtn1.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				layoutPanel.setCollapsed(northPanel, !layoutPanel.isCollapsed(northPanel));
				layoutPanel.layout();
			}
		});

		layoutPanel.add(northPanel, new BorderLayoutData(Region.NORTH, 100, 10, 250));
		layoutPanel.setCollapsed(northPanel, true);

		// Drill(south) panel
		final SouthPanel drillPanel = new SouthPanel("Drill Data"); //$NON-NLS-1$

		final ImageButton collapseBtn2 = new ImageButton(Caption.IMAGES.toolCollapseDown());
		drillPanel.getHeader().add(collapseBtn2, CaptionRegion.RIGHT);

		collapseBtn2.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				layoutPanel.setCollapsed(drillPanel, !layoutPanel.isCollapsed(drillPanel));
				layoutPanel.layout();
			}
		});

		layoutPanel.add(drillPanel, new BorderLayoutData(Region.SOUTH, 100, 10, 250));
		layoutPanel.setCollapsed(drillPanel, true);

		stackPanel = new StackLayoutPanel();
		layoutPanel.add(stackPanel);

		// Create the container for the main example
		panel1 = new ScrollPanel();
		// panel1.setPadding(0);
		// panel1.setWidgetSpacing(0);
		grid = new Grid(3, 2);

		grid.setWidget(0, 0, new DimensionDropWidget(ConstantFactory.getInstance().rows(), Axis.ROWS));
		grid.setWidget(1, 0, new DimensionDropWidget(ConstantFactory.getInstance().columns(), Axis.COLUMNS));
		grid.setWidget(2, 0, new DimensionDropWidget(ConstantFactory.getInstance().filter(), Axis.FILTER));

		panel1.add(grid);
		stackPanel.add(panel1, createTabBarCaption(Pat.IMAGES.cube(), ConstantFactory.getInstance().data() + " (" + getName() + ")"), //$NON-NLS-1$ //$NON-NLS-2$
				true);

		final LayoutPanel panel2 = new LayoutPanel();
		sourceWidget = new HTML();
		sourceWidget.setStyleName(DEFAULT_STYLE_NAME + "-source"); //$NON-NLS-1$
		panel2.add(sourceWidget);
		stackPanel.add(panel2, createTabBarCaption(Pat.IMAGES.chart(), ConstantFactory.getInstance().chart() + " (" + getName() + ")"), true); //$NON-NLS-1$ //$NON-NLS-2$

		stackPanel.showStack(0);

		return layoutPanel;

	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.ui.widgets.DataWidget#getDescription()
	 */
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.ui.widgets.DataWidget#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

}
