/**
 * 
 */
package org.pentaho.pat.rpc.beans;

import java.io.Serializable;
/**
 * @author pstoellberger
 *
 */
public class CubeConnection implements Serializable {
	
	
	// TODO Is this the correct way to serialize?
	private static final long serialVersionUID = 1L;

	private String name = null;
	
	private String driverClassName = null;
	
	private String url = null;
	
	private String username = null;
	
	private String password = null;
	
	private ConnectionType connectionType = null;

	public enum ConnectionType {
		XMLA, JDBC
	}
	
	
	public CubeConnection(ConnectionType cType) {
		connectionType = cType;
	}
	
	public CubeConnection() {
	}
	
	public String getConnectionString() {
		String connectStr ="";
		if (connectionType == ConnectionType.JDBC) {
			
		}
		return connectStr;
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public ConnectionType getConnectionType() {
		return connectionType;
	}
	public void setConnectionType(ConnectionType connectionType) {
		this.connectionType = connectionType;
	}
	
	
}
