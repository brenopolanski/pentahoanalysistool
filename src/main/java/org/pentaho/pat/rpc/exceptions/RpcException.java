package org.pentaho.pat.rpc.exceptions;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RpcException extends Exception implements IsSerializable, Serializable {

    private static final long serialVersionUID = 1L;
    
    private String message = null;
    
    public RpcException() {
        super();
    }
    
    public RpcException(String message) {
        super(message);
        this.message = message;
    }
    
    public RpcException(String message, Throwable cause) {
        super(message,cause);
        this.message = message;
    }
    
    public RpcException(Throwable cause) {
        super(cause);
    }
    
    @Override
    public String getMessage() {
        return this.message;
    }
}
