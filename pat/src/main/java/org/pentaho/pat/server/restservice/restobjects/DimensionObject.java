package org.pentaho.pat.server.restservice.restobjects;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("restriction")
@XmlRootElement
public class DimensionObject {

	public static class Dimension{
	@XmlElement(name = "name", required = true)
	String name;
	
	@XmlElement(name = "axis", required = true)
	String axis;
	
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setAxis(String axis){
		this.axis = axis;
	}
	}
	
	//@XmlElementWrapper(name = "cube")
	@XmlElement(name = "cube", required = true)
	private List<Dimension> names = new ArrayList<Dimension>();
	
	public void newDimension(String name, String axis){
		Dimension dim = new Dimension();
		dim.setName(name);
		dim.setAxis(axis);
		
		names.add(dim);
	}
	
}
