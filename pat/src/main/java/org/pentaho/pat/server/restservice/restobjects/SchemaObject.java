package org.pentaho.pat.server.restservice.restobjects;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SchemaObject {
	 
	private static class SubSchema{
		@XmlAttribute(name = "name", required = true)
		String name;
		
		
		CubeObject cob;
		


		public void setSchemaName(String name) {
			this.name = name;
			
		}

		public void setSchemaCube(CubeObject schema) {
			this.cob = schema;
			
		}

	}
		//@XmlElementWrapper(name = "cube")
    	@XmlElement(name = "schema", required = true)
		private List<SubSchema> names = new ArrayList<SubSchema>();

		public void addSchema(String name, CubeObject cob){
			SubSchema sub = new SubSchema();
			sub.setSchemaName(name);
			sub.setSchemaCube(cob);
			names.add(sub);
		}



  


}