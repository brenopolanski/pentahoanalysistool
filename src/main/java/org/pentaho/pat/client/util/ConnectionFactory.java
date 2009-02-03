package org.pentaho.pat.client.util;

import org.pentaho.pat.client.Connection;

import com.google.gwt.core.client.GWT;

public class ConnectionFactory {
	  static Connection connection = null;
	  public static Connection getInstance() {
	    if (connection == null) {
	      connection = (Connection) GWT.create(Connection.class);
	    }
	    return connection;
	  }
}
