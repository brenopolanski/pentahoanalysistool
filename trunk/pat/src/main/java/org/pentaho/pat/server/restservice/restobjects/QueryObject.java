package org.pentaho.pat.server.restservice.restobjects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class QueryObject {

    
    
    private String queryId;

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    @XmlElement(name = "queryId", required = true)
    public String getQueryId() {
        return queryId;
    }
    
    
}
