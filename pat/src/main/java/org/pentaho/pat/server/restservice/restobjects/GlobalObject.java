package org.pentaho.pat.server.restservice.restobjects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is the Global Session Object I chatted with Mark Cahill about, when it doesn't work blame him ;)
 * @author tombarber
 *
 */
@XmlRootElement
public class GlobalObject {

    @XmlElement(name = "sessionObject", required = true)
    SessionObject sob;
    
    @XmlElement(name = "connectionObject", required = true)
    ConnectionObject cob;
    
    public GlobalObject(SessionObject string) {
        sob = string;
    }
    
    public void setActiveConnections(ConnectionObject activeConnections){
        
    }
    
    public ConnectionObject getActiveConnections(){
        return null;
        
    }

}
