package org.pentaho.pat.server.restservice.restobjects;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class QueryObject {

    
    
    private String queryId;
    private String name;
	private DimensionObject dob;

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    @XmlAttribute(name = "id", required = true)
    public String getQueryId() {
        return queryId;
    }

    public void setQueryName(String queryName) {
        this.name = queryName;
    }

    @XmlAttribute(name = "name", required = true)
    public String getQueryName() {
        return name;
    }
    
	public void setDimensions(DimensionObject dob) {
		this.dob = dob;		
	}
    
	@XmlElement(name = "dimensions", required = true)
    public DimensionObject getDimensions(){
    	return dob;
    }
    
}
