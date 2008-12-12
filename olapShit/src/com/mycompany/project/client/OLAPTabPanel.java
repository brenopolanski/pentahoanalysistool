package com.mycompany.project.client;

import com.mycompany.project.client.panels.ConnectPanel;
import com.mycompany.project.client.panels.DimPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarMenuButton;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import com.gwtext.client.core.EventObject;

public class OLAPTabPanel extends FlexTable {

	DimPanel dimensionPanel;
	ConnectPanel connectWindow;

	public OLAPTabPanel() {
		super();

		init();
	}

	/**
	 * 
	 */
	private void init() {
		dimensionPanel = new DimPanel();
		connectWindow = new ConnectPanel();
		connectWindow.addConnectionListener(dimensionPanel);
		// ConnectionPanel connectionPanel = new ConnectionPanel();
		// ReportPanel reportPanel = new ReportPanel(dimensionPanel);

		// connectionPanel.addConnectionListener(dimensionPanel);
		// connectionPanel.addConnectionListener(reportPanel);

		// this.add(connectionPanel, MessageFactory.getInstance().connection());
		// this.add(dimensionPanel, MessageFactory.getInstance().selections());
		// this.add(reportPanel, MessageFactory.getInstance().report());
		final Toolbar toolbar = new Toolbar();
		this.setWidget(0, 0, toolbar);
		this.getCellFormatter().setHeight(0, 0, "30px");
		toolbar.setSize("100%", "30px");
		final BaseItemListenerAdapter listener = new BaseItemListenerAdapter() {
			@Override
			public void onClick(BaseItem connect, EventObject e) {
				connectWindow.show();
			}
		};
		Item connect = new Item("Connect");
		connect.addListener(listener);

		Menu submenu = new Menu();
		submenu.addItem(connect);
		final ToolbarMenuButton fileToolbarMenuButton = new ToolbarMenuButton(
		"File");
		fileToolbarMenuButton.setMenu(submenu);
		toolbar.addButton(fileToolbarMenuButton);

		final HorizontalSplitPanel horizontalSplitPanel = new HorizontalSplitPanel();
		this.setWidget(1, 0, horizontalSplitPanel);
		horizontalSplitPanel.setSize("100%", "100%");
		horizontalSplitPanel.setSplitPosition("20%");

		horizontalSplitPanel.setLeftWidget(dimensionPanel);
		dimensionPanel.setSize("100%", "100%");
		// selectTab(0);
		// this.addTabListener(connectionPanel);
	}

	public DimPanel getDimensionPanel() {
		return dimensionPanel;
	}
}
