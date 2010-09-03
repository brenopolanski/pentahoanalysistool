package org.pentaho.pat.server.restservice.restobjects;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CubeObject {
	 
	private static class SubCube{
		@XmlElement(name = "name", required = true)
		String name;
		@XmlElement(name = "connectionId", required = true)
		String id;
		@XmlElement(name = "catalog", required = true)
		private String catalog;
		@XmlElement(name = "schema", required = true)
		private String schema;

		@XmlElement(name = "dimensions", required = true)
		private DimensionObject dob;
		
	    public void setConnectionId(String text) {
	        this.id = text;
	    }

		public void setCatalog(String catalog) {
			this.catalog = catalog;
			
		}

		public void setcubeName(String name) {
			this.name = name;
			
		}

		public void setCubeSchema(String schema) {
			this.schema = schema;
			
		}
		
		public void setDimensions(DimensionObject dob) {
			this.dob = dob;
			
		}
	}
		//@XmlElementWrapper(name = "cube")
    	@XmlElement(name = "cube", required = true)
		private List<SubCube> names = new ArrayList<SubCube>();

	    
		public void addCube(String connectionId, String catalog, String name, String schema){
			SubCube sub = new SubCube();
			sub.setConnectionId(connectionId);
			sub.setCatalog(catalog);
			sub.setcubeName(name);
			sub.setCubeSchema(schema);
			names.add(sub);
		}

		public void addCube(String connectionId, String catalog, String name, String schema, DimensionObject dimensionList){
			SubCube sub = new SubCube();
			sub.setConnectionId(connectionId);
			sub.setCatalog(catalog);
			sub.setcubeName(name);
			sub.setCubeSchema(schema);
			sub.setDimensions(dimensionList);
			names.add(sub);
		}



  


}