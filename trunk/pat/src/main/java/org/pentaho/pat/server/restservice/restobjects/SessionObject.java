package org.pentaho.pat.server.restservice.restobjects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SessionObject {

    
    
    private String sessionId;

    private ConnectionObject cob;
    
    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @XmlElement(name = "session", required = true)
    public String getSessionId() {
        return sessionId;
    }
    
    public void setConnectionObject(ConnectionObject cob) {
        this.cob = cob;
    }

    @XmlElement(name = "connections", required = true)
    public ConnectionObject getConnectionObject() {
        return cob;
    }
    
    
}
