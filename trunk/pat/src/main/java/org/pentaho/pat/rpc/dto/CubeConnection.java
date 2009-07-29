/**
 * 
 */
package org.pentaho.pat.rpc.dto;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
/**
 * @author pstoellberger
 *
 */
public class CubeConnection implements Serializable,IsSerializable {
	
	
	private static final long serialVersionUID = 1L;

	private String id = null;
	
	private String name = null;
	
	private String driverClassName = null;
	
	private String url = null;
	
	private String catalog = null;
	
	private String username = null;
	
	private String password = null;
	
	private ConnectionType connectionType = null;

	private String schemaData = null;

	public enum ConnectionType implements IsSerializable {
		XMLA, Mondrian
	}
	
	
	public CubeConnection(ConnectionType cType) {
		connectionType = cType;
	}
	
	public CubeConnection() {
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

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

    public String getSchemaData() {
        return schemaData;
    }

    public void setSchemaData(String schemaData) {
        this.schemaData = schemaData;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
}
