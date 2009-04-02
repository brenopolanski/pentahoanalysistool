package org.pentaho.pat.rpc.exceptions;

import java.io.Serializable;

public class RpcException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public RpcException() {
        super();
    }
    
    public RpcException(String message) {
        super(message);
    }
    
    public RpcException(String message, Throwable cause) {
        super(message,cause);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }
}
