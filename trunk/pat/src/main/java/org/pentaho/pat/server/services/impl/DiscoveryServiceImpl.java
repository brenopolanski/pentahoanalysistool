package org.pentaho.pat.server.services.impl;

import java.util.List;

import org.apache.commons.collections.list.TreeList;
import org.olap4j.Axis;
import org.pentaho.pat.server.services.DiscoveryService;
import org.pentaho.pat.server.services.SessionService;

public class DiscoveryServiceImpl implements DiscoveryService {

	private SessionService sessionService;
	
	public List<String> getCubes(String guid) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getDimensions(Axis axis, String guid) {
		// TODO Auto-generated method stub
		return null;
	}

	public TreeList getMembers(String dimensionName, String guid) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setSessionService(SessionService service) {
		this.sessionService = service;
	}

}
