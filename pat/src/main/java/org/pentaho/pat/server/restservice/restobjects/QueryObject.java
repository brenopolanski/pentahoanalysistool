package org.pentaho.pat.server.restservice.restobjects;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class QueryObject {

    
    
    private String queryId;
    private String queryname;
    private String catalog;
    private String cube;
    
	private AxisObject aob;

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    @XmlAttribute(name = "queryid", required = true)
    public String getQueryId() {
        return queryId;
    }

    public void setQueryName(String queryName) {
        this.queryname = queryName;
    }

    @XmlAttribute(name = "queryname", required = true)
    public String getQueryName() {
        return queryname;
    }
    
	public void setAxis(AxisObject aob) {
		this.aob = aob;		
	}
    
	@XmlElement(name = "axes", required = true)
    public AxisObject getAxes(){
    	return aob;
    }

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	@XmlAttribute(name = "catalog", required = true)
	public String getCatalog() {
		return catalog;
	}

	public void setCube(String cube) {
		this.cube = cube;
	}

	@XmlAttribute(name = "cube", required = true)
	public String getCube() {
		return cube;
	}
    
}
