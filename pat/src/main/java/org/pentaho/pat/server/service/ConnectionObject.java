package org.pentaho.pat.server.service;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ConnectionObject {
	 
	private static class SubCob{
		
		@XmlElement(name = "connectionName", required = true)
		String name;
		
		@XmlElement(name = "connectionId", required = true)
		String id;
	    public void setText(String text) {
	        this.name = text;
	    }
	    
	    public void setId(String text) {
	        this.id = text;
	    }
	    

		
	}
//		@XmlElementWrapper(name = "Connection")
    	@XmlElement(name = "connection", required = true)
		private List<SubCob> names = new ArrayList<SubCob>();

	    
		public void addConnection(String id, String name){
			SubCob sub = new SubCob();
			sub.setId(id);
			sub.setText(name);
			names.add(sub);
		}




  


}