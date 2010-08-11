package org.pentaho.pat.server.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.pentaho.pat.rpc.exceptions.RpcException;
import org.pentaho.pat.server.service.QueryService;
import org.pentaho.pat.server.servlet.QueryServlet;
import org.pentaho.pat.server.servlet.SessionServlet;

public class QueryServiceImpl/* implements QueryService*/{
    SessionServlet ss = new SessionServlet();
    QueryServlet qs = new QueryServlet();
    String sessionId;
    String connectionId;
    String cubeName;
    
    public String createSession(HttpServletRequest request){
       try {
        return ss.createSession();
    } catch (RpcException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return null;
    }
    
    public void executeQuery(HttpServletRequest request, String queryID) {
        // TODO Auto-generated method stub
        
    }

    public String[] getDimensions(HttpServletRequest request, String queryID) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean loadQuery(HttpServletRequest request, String queryID) {
        // TODO Auto-generated method stub
        return false;
    }

    public String newQuery(HttpServletRequest request) {
        try {
            qs.createNewQuery(sessionId, connectionId, cubeName);
        } catch (RpcException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveQuery(HttpServletRequest request, String queryID) {
        // TODO Auto-generated method stub
        return false;
    }

}
