package org.pentaho.pat.server.restservice.restobjects;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AxisObject {

	public static class Axis{
	
	String location;
	Boolean nonempty;	
	DimensionObject dimList;
	
	@XmlAttribute(name = "location", required = true)
	public String getLocation() {
		return location;
	}
	public void setLocation(String name) {
		this.location = name;
	}
	
	@XmlAttribute(name = "nonempty", required = true)
	public Boolean getNonEmpty() {
		return nonempty;
	}
		
	public void setNonEmpty(Boolean nonempty) {
		this.nonempty = nonempty;
	}
	
	@XmlElement(name = "dimensions", required = false)
	public DimensionObject getDims() {
		return dimList;
	}
	
	public void setDims(DimensionObject dims) {
		this.dimList = dims;
	}
	
	
	}
	
	
	@XmlElement(name = "axis", required = true)
	private List<Axis> axes = new ArrayList<Axis>();
	
	public void newAxis(String name, boolean nonempty, DimensionObject dims){
		Axis axis = new Axis();
		axis.setLocation(name);
		axis.setNonEmpty(nonempty);
		axis.setDims(dims);
		axes.add(axis);
	}
	
	public List<Axis> getAxisList(){
	    return axes;
	}
	
}
