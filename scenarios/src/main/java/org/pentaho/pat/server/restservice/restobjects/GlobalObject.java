package org.pentaho.pat.server.restservice.restobjects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is the Global Session Object I chatted with Mark Cahill about, when it doesn't work blame him ;)
 * @author tombarber
 *
 */
@SuppressWarnings("restriction")
@XmlRootElement
public class GlobalObject {

    @XmlElement(name = "sessionObject", required = true)
    SessionObject sob;
    
    @XmlElement(name = "connectionObject", required = true)
    ConnectionObject cob;
    
    @SuppressWarnings("unused")
    private GlobalObject(){}
    
    public GlobalObject(SessionObject string) {
        sob = string;
    }
    
    public void setActiveConnections(ConnectionObject activeConnections){
        this.cob=activeConnections;
    }
    
    public ConnectionObject getActiveConnections(){
        return cob;
        
    }

}
