package org.pentaho.pat.server.restservice.restobjects;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import javax.xml.bind.annotation.XmlAccessType;

@SuppressWarnings("restriction")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="query")
public class QueryObject {

    @XmlAttribute(name = "queryid", required = true)
	private String queryId;
    
    @XmlAttribute(name = "queryname", required = true)
	private String queryname;
    
    @XmlAttribute(name = "catalog", required = true)
	private String catalog;
    
    @XmlAttribute(name = "cube", required = true)
	private String cube;

    @XmlElement(name="axis", required = true)
	private AxisObject[] aob;

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	
	public String getQueryId() {
		return queryId;
	}

	public void setQueryName(String queryName) {
		this.queryname = queryName;
	}

	
	public String getQueryName() {
		return queryname;
	}

	public void setAxis(AxisObject[] aob) {
		this.aob = aob;
	}

	
	public AxisObject[] getAxes() {
		return aob;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	
	public String getCatalog() {
		return catalog;
	}

	public void setCube(String cube) {
		this.cube = cube;
	}

	public String getCube() {
		return cube;
	}

}
