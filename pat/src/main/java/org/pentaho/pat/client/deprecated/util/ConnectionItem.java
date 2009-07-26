package org.pentaho.pat.client.deprecated.util;

public class ConnectionItem {
	private String name;
	private String id;
	private Boolean isConnected = false;

	public ConnectionItem(String id, String name, boolean isConnected) {
		this.name = name;
		this.id = id;
		this.isConnected = isConnected;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(Boolean isConnected) {
		this.isConnected = isConnected;
	}
}