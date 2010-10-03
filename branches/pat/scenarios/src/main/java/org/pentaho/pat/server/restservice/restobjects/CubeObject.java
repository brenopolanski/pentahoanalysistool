package org.pentaho.pat.server.restservice.restobjects;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CubeObject {
	 
	private static class SubCube{
		
		String name;

		public void setcubeName(String name) {
			this.name = name;
			
		}

		@XmlAttribute(name = "cubename", required = true)
		public String getCubeName(){
			return name;
		}

	}
		//@XmlElementWrapper(name = "cube")
    	@XmlElement(name = "cube", required = true)
		private List<SubCube> names = new ArrayList<SubCube>();

	    
		public void addCube(String name){
			SubCube sub = new SubCube();
			sub.setcubeName(name);
			names.add(sub);
		}

	



  


}