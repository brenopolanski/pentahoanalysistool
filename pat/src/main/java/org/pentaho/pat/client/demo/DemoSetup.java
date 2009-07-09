package org.pentaho.pat.client.demo;

import org.pentaho.pat.client.util.State;
import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.rpc.dto.CubeConnection.ConnectionType;

public class DemoSetup {
	
	DemoSetup(State.Mode demoMode) {
		CubeConnection cc = new CubeConnection(ConnectionType.Mondrian);
		
		if (demoMode.isShowOnlyTable()) {
			
		}
			
	}

}
