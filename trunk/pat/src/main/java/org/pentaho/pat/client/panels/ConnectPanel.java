/**
 * 
 */
package org.pentaho.pat.client.panels;


import org.gwt.mosaic.ui.client.WindowPanel;

import org.pentaho.pat.client.util.GuidFactory;
import org.pentaho.pat.client.util.MessageFactory;
import org.pentaho.pat.client.util.ServiceFactory;

import org.pentaho.pat.client.events.SourcesConnectionEvents;
import org.pentaho.pat.client.listeners.ConnectionListener;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import org.gwt.mosaic.forms.client.builder.PanelBuilder;
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;

/**
 * @author paul
 *
 */
public class ConnectPanel extends WindowPanel implements
		SourcesConnectionEvents {

	Button connectBtn;
	TextBox serverTB;
	Label serverLbl;
	TextBox portTB;
	Label portLbl;
	TextBox databaseTB;
	Label databaseLbl;
	TextBox userTB;
	Label userLbl;
	TextBox passwordTB;
	Label passwordLbl;
	
	public ConnectPanel() {
		
		this.setTitle("Register new Mondrian Connection");
		MessageFactory.getInstance().disconnect();
		connectBtn = new Button(MessageFactory.getInstance().connect());
		serverTB = new TextBox();
		portTB = new TextBox();
		portLbl = new Label(MessageFactory.getInstance().port());
		databaseTB = new TextBox();
		databaseLbl = new Label(MessageFactory.getInstance().database());
		userTB = new TextBox();
		userLbl = new Label(MessageFactory.getInstance().username());
		passwordTB = new TextBox();
		passwordLbl = new Label(MessageFactory.getInstance().password());
		this.setWidget(onInitialize());
		this.setWidth("700");
	}
	
	
	  protected Widget onInitialize() {
		  FormLayout layout = new FormLayout(
			        "right:[40dlu,pref], 3dlu, 70dlu, 7dlu, "
			            + "right:[40dlu,pref], 3dlu, 70dlu",
			        "12px, pref, 12px, pref, 12px");

		  PanelBuilder builder = new PanelBuilder(layout);
		  builder.addLabel(MessageFactory.getInstance().server() + ":", CellConstraints.xy(1, 2));
		  builder.add(serverTB, CellConstraints.xy(3, 2));
		  builder.addLabel(MessageFactory.getInstance().port() + ":", CellConstraints.xy(5, 2));
		  builder.add(portTB, CellConstraints.xy(7, 2));
		  builder.addLabel(MessageFactory.getInstance().database() + ":", CellConstraints.xy(1, 4));
		  builder.add(databaseTB, CellConstraints.xyw(3, 4, 5));
		    
		    return builder.getPanel();
	  		}


	public void addConnectionListener(ConnectionListener listener) {
		// TODO Auto-generated method stub

	}

	public void removeClickListener(ConnectionListener listener) {
		// TODO Auto-generated method stub

	}

}
