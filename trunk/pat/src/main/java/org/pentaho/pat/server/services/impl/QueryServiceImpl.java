package org.pentaho.pat.server.services.impl;

import java.util.List;

import org.olap4j.Axis;
import org.olap4j.query.Selection.Operator;
import org.pentaho.pat.server.data.QueryManager;
import org.pentaho.pat.server.services.QueryService;
import org.pentaho.pat.server.services.SessionService;

public class QueryServiceImpl implements QueryService {

	private SessionService sessionService;
	
	private QueryManager queryManager;
	
	public void setSessionService(SessionService service) {
		this.sessionService = service;
	}
	
	public void setQueryManager(QueryManager manager) {
		this.queryManager = manager;
	}
	
	public Boolean clearSelection(String guid, String dimensionName, List<String> memberNames) {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean createSelection(String guid, String dimensionName,
			List<String> memberNames, Operator selectionType) {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean moveDimension(String guid, Axis axis, String dimensionName) {
		// TODO Auto-generated method stub
		return null;
	}
}
