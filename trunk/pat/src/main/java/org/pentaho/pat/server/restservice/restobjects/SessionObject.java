package org.pentaho.pat.server.restservice.restobjects;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "session")
public class SessionObject {

    
    
    private String sessionId;

    private ConnectionObject cob;
    
    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @XmlAttribute(name = "id")
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
