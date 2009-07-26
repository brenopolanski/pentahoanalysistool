/*
 * Copyright (C) 2009 Paul Stoellberger
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA *
 *
 * @created Jul 16, 2009 
 * @author Paul Stoellberger
 */

package org.pentaho.pat.client.deprecated.demo;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.WidgetWrapper;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutManager;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Alignment;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwtwidgets.client.util.WindowUtils;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.deprecated.util.State.Mode;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;


public class DemoPanel extends LayoutComposite {

	public DemoPanel() {
		super();
		LayoutPanel rootPanel = new LayoutPanel();
		final String patLocation = "http://" + WindowUtils.getLocation().getHost()+ WindowUtils.getLocation().getPath();
		rootPanel.setLayout(new BoxLayout(Orientation.VERTICAL,Alignment.CENTER));
		rootPanel.add(new WidgetWrapper(new HTML("<h2>PAT modes demonstration</h2>")), new BoxLayoutData(FillStyle.BOTH));
		rootPanel.add(new WidgetWrapper(new HTML("")), new BoxLayoutData(FillStyle.BOTH));
		rootPanel.add(new WidgetWrapper(new HTML("It is possible to introduce modes in which only specific elements of PAT are shown to the user. The one listed below demonstrate how PAT could look like when accessed by users with different permissions (STANDALONE, USER, BUSINESSUSER) or a lightweight version (ONECUBE, OLAPTABLE) for integration into other applications (e.g biserver)")), new BoxLayoutData(FillStyle.BOTH));
		rootPanel.add(new WidgetWrapper(new HTML("")), new BoxLayoutData(FillStyle.BOTH));
		
		if (Pat.getInitialState().getMode() != Mode.DEFAULT) {
			rootPanel.add(new WidgetWrapper(new HTML("<h4>CURRENT MODE: " + Pat.getInitialState().getMode().name()+"</h4>")), new BoxLayoutData(FillStyle.BOTH));
		}
		String subText = "<b>IMPORTANT</b> : The modes (except "+Mode.STANDALONE.name()+") need a running Pentaho BI-Server with standard configuration.They will try to access the XMLA Service at http://localhost:8080/pentaho/Xmla.";
		//rootPanel.add(new WidgetWrapper(new HTML(subText)), new BoxLayoutData(FillStyle.BOTH));
		
			 String subText2 = "You can always leave the mode demonstration by going to: <a href='"+patLocation+"'>"+patLocation+"</a><br>";
		rootPanel.add(new WidgetWrapper(new HTML(subText + subText2)), new BoxLayoutData(FillStyle.BOTH));
		rootPanel.add(new WidgetWrapper(new HTML("")), new BoxLayoutData(FillStyle.BOTH));
		LayoutPanel buttonPanel = new LayoutPanel(new BoxLayout(Orientation.HORIZONTAL,Alignment.CENTER));
		ToolButton standaloneButton = new ToolButton(Mode.STANDALONE.name());
		standaloneButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				Window.Location.assign(patLocation + "?MODE=STANDALONE");
			}
		});
		buttonPanel.add(standaloneButton);
		ToolButton userButton = new ToolButton(Mode.USER.name());
		userButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				Window.Location.assign(patLocation + "?MODE=USER");
			}
		});
		buttonPanel.add(userButton);
		ToolButton businessuserButton = new ToolButton(Mode.BUSINESSUSER.name());
		businessuserButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				Window.Location.assign(patLocation + "?MODE=BUSINESSUSER");
			}
		});
		buttonPanel.add(businessuserButton);
		ToolButton onecubeButton = new ToolButton(Mode.ONECUBE.name());
		onecubeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				Window.Location.assign(patLocation + "?MODE=ONECUBE");
			}
		});
		buttonPanel.add(onecubeButton);
		ToolButton olaptableButton = new ToolButton(Mode.OLAPTABLE.name());
		olaptableButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				Window.Location.assign(patLocation + "?MODE=OLAPTABLE");
			}
		});
		buttonPanel.add(olaptableButton);

		rootPanel.add(buttonPanel);
		getLayoutPanel().setLayout(new BoxLayout());
		getLayoutPanel().add(rootPanel,new BoxLayoutData(true));
		
	}
}
