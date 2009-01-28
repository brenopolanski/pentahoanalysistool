package org.pentaho.pat.server.data.pojo;

public class SavedConnection {

	private String name = null;
	
	private String driverClassName = null;
	
	private String url = null;
	
	private String username = null;
	
	private String password = null;

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

	public void setPassword(String assword) {
		this.password = assword;
	}
	
	
}
