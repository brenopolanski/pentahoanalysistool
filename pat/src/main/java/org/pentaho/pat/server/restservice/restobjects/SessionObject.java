package org.pentaho.pat.server.restservice.restobjects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SessionObject {

    
    
    private String sessionId;

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @XmlElement(name = "session", required = true)
    public String getSessionId() {
        return sessionId;
    }
    
    
}
