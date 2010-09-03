package org.pentaho.pat.server.restservice.restobjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.pentaho.pat.rpc.dto.CubeItem;

@XmlRootElement
public class ConnectionObject {
	 
	private static class SubCob{
		
		@XmlElement(name = "connectionName", required = true)
		String name;
		
		@XmlElement(name = "connectionId", required = true)
		String id;
		
		@XmlElement(name = "cubes", required = false)
		List<CubeItem> cubes;
		
	    public void setText(String text) {
	        this.name = text;
	    }
	    
	    public void setId(String text) {
	        this.id = text;
	    }

        public void setCubes(CubeItem[] out) {
        this.cubes = Arrays.asList(out);    
            
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


        public void addConnection(String id, String name, CubeItem[] out) {
            SubCob sub = new SubCob();
            sub.setId(id);
            sub.setText(name);
            sub.setCubes(out);
            names.add(sub);
            
            
        }




  


}