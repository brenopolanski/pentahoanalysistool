package org.pentaho.pat.client.demo;

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.panels.ConnectionManagerPanel;
import org.pentaho.pat.client.ui.panels.MainMenu;
import org.pentaho.pat.client.ui.panels.MainTabPanel;
import org.pentaho.pat.client.ui.panels.QueryPanel;
import org.pentaho.pat.client.ui.panels.WelcomePanel;
import org.pentaho.pat.client.ui.panels.MainMenu.MenuItem;
import org.pentaho.pat.client.ui.widgets.ConnectXmlaPanel;
import org.pentaho.pat.client.util.State;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.rpc.dto.CubeConnection.ConnectionType;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class DemoSetup {

	private State.Mode demoMode;
	private String session;
	
	public DemoSetup(State.Mode demoMode, String session) {
		this.demoMode = demoMode;
		this.session = session;
		if (demoMode != State.Mode.STANDALONE) {
			CubeConnection demoConnection = getDemoConnection();
			connect(demoConnection);
			
			if (demoMode == State.Mode.ONECUBE || demoMode == State.Mode.OLAPTABLE) {
				setupDimensionMenu();
			}
		}
	}

	private CubeConnection getDemoConnection() {
		CubeConnection cc = new CubeConnection(ConnectionType.XMLA);
		cc.setName("demo Connection 1");
		cc.setUrl("http://localhost:8080/pentaho/Xmla");
		cc.setUsername("joe");
		cc.setPassword("password");
		return cc;	
	}

	private boolean connect(final CubeConnection cc) {
		ServiceFactory.getSessionInstance().connect(Pat.getSessionID(), cc, new AsyncCallback<Object>() {
			public void onFailure(final Throwable arg0) {
			}

			public void onSuccess(final Object o) {
				GlobalConnectionFactory.getInstance().getConnectionListeners().fireConnectionMade(new Widget());
				if (demoMode.isShowConnections()) {
					ConnectionManagerPanel.addConnection(cc);
				}
			}
		});

		return true;
	}
	
	private void setupDimensionMenu() {
		final QueryPanel widget = new QueryPanel("demo query");
		 widget.setCube("Quadrant Analysis");
		ServiceFactory.getSessionInstance().setCurrentCube(Pat.getSessionID(), "Quadrant Analysis", new AsyncCallback<String[]>() { //$NON-NLS-1$
			public void onFailure(final Throwable arg0) { }

			public void onSuccess(final String[] arg0) {
				ServiceFactory.getQueryInstance().createNewQuery(Pat.getSessionID(), new AsyncCallback<String>() {
					public void onFailure(final Throwable arg0) { 	}
					public void onSuccess(final String arg0) {
						widget.setQuery(arg0);
						ServiceFactory.getQueryInstance().setCurrentQuery(Pat.getSessionID(), arg0, new AsyncCallback<Object>() {
							public void onFailure(final Throwable arg0) { }
							public void onSuccess(final Object arg0) {
								// TODO change way of accessing other widget elements
								MainMenu.getDimensionPanel().createDimensionList();
								MainMenu.getDimensionPanel().layout();
								MainTabPanel.displayContentWidget(widget);
								MainMenu.showNamedMenu(MenuItem.Dimensions);
								MainMenu.getStackPanel().layout();
							}
						});
					}
				});
			}
		});
	}


}
