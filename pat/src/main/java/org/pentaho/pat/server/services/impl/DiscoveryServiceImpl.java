package org.pentaho.pat.server.services.impl;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.list.TreeList;
import org.olap4j.Axis;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.NamedList;
import org.pentaho.pat.server.data.ConnectionManager;
import org.pentaho.pat.server.services.DiscoveryService;
import org.pentaho.pat.server.services.SessionService;

public class DiscoveryServiceImpl implements DiscoveryService {

	private SessionService sessionService;
	
	private ConnectionManager connectionManager;
	
	
	
	public List<String> getCubes(String guid) {
		
		try {
			
			List<String> list = new ArrayList<String>();
			
			OlapConnection conn = this.connectionManager.getConnection(guid);
			
			if (conn == null)
				return list;
			
			NamedList<Cube> cubes = conn.getSchema().getCubes();
			
			for (int i = 0; i < cubes.size(); i++) {
				list.add(cubes.get(i).getName());
			}
			return list;
			
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OlapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	
	public void setConnectionManager(ConnectionManager manager) {
		this.connectionManager = manager;
	}

}
