package org.pentaho.pat.server.data.pojo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ConnectionType implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    public static final ConnectionType XMLA = new ConnectionType("XMLA");
    public static final ConnectionType Mondrian = new ConnectionType("Mondrian");
    private static final Map<String, ConnectionType> INSTANCES = new HashMap<String, ConnectionType>();

    static {
        INSTANCES.put(XMLA.toString(), XMLA);
        INSTANCES.put(Mondrian.toString(), Mondrian);
    }

    private ConnectionType(String name) {
        this.name=name;
    }

    public String toString() {
        return name;
    }

    private Object readResolve() {
        return getInstance(name);
    }

    public static ConnectionType getInstance(String name) {
        return (ConnectionType)INSTANCES.get(name);
    }
}
