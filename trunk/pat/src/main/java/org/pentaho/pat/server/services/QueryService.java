package org.pentaho.pat.server.services;

public interface QueryService {
	
	public Boolean moveDimension(
		String axisName, String DimName, String queryId, String guid);
	
	public Boolean createSelection(
		String dimName, String[] memberNames, Integer selectionType, 
		String queryId, String guid);
	
	public Boolean clearSelection(String dimName, String[] memberNames, 
		String queryId, String guid);
}
