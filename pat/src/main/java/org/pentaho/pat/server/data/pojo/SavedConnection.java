package org.pentaho.pat.server.data.pojo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="connections")
public class SavedConnection {

    @Basic
	private String name;
	
    @Basic
	private String driverClassName;
	
    @Basic
	private String url;
	
    @Basic
	private String username;
	
    @Basic
	private String password;
    
    @Basic
    @Type(
        type = "org.pentaho.pat.server.data.pojo.ConnectionType"
    )
    private ConnectionType type;
    
    @Column(nullable=true)
    @Type(type="text")
    private String schema=null;

    @Id
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

    public ConnectionType getType() {
        return type;
    }

    public void setType(ConnectionType type) {
        this.type = type;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}
