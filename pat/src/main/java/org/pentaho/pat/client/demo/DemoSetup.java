package org.pentaho.pat.client.demo;

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.panels.ConnectionManagerPanel;
import org.pentaho.pat.client.ui.panels.WelcomePanel;
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
		CubeConnection demoConnection = getDemoConnection();

		if (demoMode != State.Mode.STANDALONE) {
			connect(demoConnection);
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
		ServiceFactory.getSessionInstance().connect(session, cc, new AsyncCallback<Object>() {
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


}
