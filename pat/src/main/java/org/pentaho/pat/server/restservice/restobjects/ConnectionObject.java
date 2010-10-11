package org.pentaho.pat.server.restservice.restobjects;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("restriction")
@XmlRootElement
public class ConnectionObject {

	private static class SubCob {

		@XmlAttribute(name = "connectionname", required = true)
		String name;

		@XmlAttribute(name = "connectionid", required = true)
		String id;

		@XmlElement(name = "schemas", required = false)
		SchemaObject schemas;

		public void setText(String text) {
			this.name = text;
		}

		public void setId(String text) {
			this.id = text;
		}

		public void setSchemas(SchemaObject out) {
			this.schemas = out;

		}

	}

	// @XmlElementWrapper(name = "Connection")
	@XmlElement(name = "connection", required = true)
	private List<SubCob> names = new ArrayList<SubCob>();

	public void addConnection(String id, String name) {
		SubCob sub = new SubCob();
		sub.setId(id);
		sub.setText(name);
		names.add(sub);
	}

	public void addConnection(String id, String name, SchemaObject cubeobj) {
		SubCob sub = new SubCob();
		sub.setId(id);
		sub.setText(name);

		sub.setSchemas(cubeobj);
		names.add(sub);

	}

}