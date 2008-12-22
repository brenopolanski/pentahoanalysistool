/*
 */


package org.pentaho.pat.client.services;



import com.google.gwt.user.client.rpc.RemoteService;


/**
 * @author Tom Barber
 *
 */
public interface Olap4JService extends RemoteService {
	  public Boolean connect(String connectStr, String guid);
	  public Boolean disconnect(String guid);
	  public Boolean setCube(String cubeName, String guid);
	  public String[] getDimensions(String axis, String guid);
	  public String[] getCubes(String guid);
}
