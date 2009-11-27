/*
 * Copyright (C) 2009 Luc Boudreau
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *
 */
package org.pentaho.pat.server.servlet;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.olap4j.OlapException;
import org.pentaho.pat.rpc.ISession;
import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.rpc.dto.CubeConnection.ConnectionType;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.pentaho.pat.server.data.pojo.SavedConnection;
import org.pentaho.pat.server.messages.Messages;
import org.pentaho.pat.server.services.SessionService;

public class SessionServlet extends AbstractServlet implements ISession {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(SessionServlet.class);

    private SessionService sessionService;

    public void init() throws ServletException {
        super.init();
        sessionService = (SessionService) applicationContext.getBean("sessionService"); //$NON-NLS-1$
        if (sessionService == null) {
            throw new ServletException(Messages.getString("Servlet.SessionServiceNotFound")); //$NON-NLS-1$
        }
    }

    public void connect(final String sessionId, final String connectionId) throws RpcException {
        try {
            this.sessionService.connect(getCurrentUserId(), sessionId, connectionId);
        } catch (OlapException e) {
            LOG.error(Messages.getString("Servlet.Session.ConnectionFailed"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Session.ConnectionFailed"), e); //$NON-NLS-1$
        }
    }

    public CubeConnection getConnection(final String sessionId, final String connectionName) throws RpcException {
        final SavedConnection savedConn = this.sessionService.getConnection(getCurrentUserId(), connectionName);
        return savedConn == null ? null : this.convert(savedConn);
    }

    public String saveConnection(final String sessionId, final CubeConnection connection) throws RpcException {
        try {
            final SavedConnection sc = this.convert(connection);
            this.sessionService.saveConnection(getCurrentUserId(), sc);
            return sc.getId();
        } catch (Exception e) {
            LOG.error(Messages.getString("Servlet.Session.SchemaFileSystemAccessError"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Session.SchemaFileSystemAccessError"), e); //$NON-NLS-1$
        }
    }

    public CubeConnection[] getConnections(final String sessionId) throws RpcException {
        final List<SavedConnection> savedConnections = this.sessionService.getConnections(getCurrentUserId());
        CubeConnection[] cubeConnections = new CubeConnection[savedConnections.size()];
        for (int cpt = 0; cpt < savedConnections.size(); cpt++) {
            cubeConnections[cpt] = convert(savedConnections.get(cpt));
        }
        return cubeConnections;
    }

    public CubeConnection[] getActiveConnections(final String sessionId) throws RpcException {
        final List<CubeConnection> connections = new ArrayList<CubeConnection>();
        for (SavedConnection connection : this.sessionService.getActiveConnections(getCurrentUserId(), sessionId)) {
            connections.add(convert(connection));
        }
        return connections.toArray(new CubeConnection[connections.size()]);
    }

    public void deleteConnection(final String sessionId, final String connectionName) throws RpcException {
        this.sessionService.deleteConnection(getCurrentUserId(), connectionName);
    }

    public void disconnect(final String sessionId, final String connectionId) throws RpcException {
        sessionService.disconnect(getCurrentUserId(), sessionId, connectionId);
    }

    public void closeSession(final String sessionId) throws RpcException {
        sessionService.releaseSession(getCurrentUserId(), sessionId);
    }

    public String createSession() throws RpcException {
        return sessionService.createNewSession(getCurrentUserId());
    }

    private CubeConnection convert(final SavedConnection sc) {

        final CubeConnection cc = new CubeConnection();
        cc.setName(sc.getName());
        cc.setDriverClassName(sc.getDriverClassName());
        cc.setConnectionType(ConnectionType.valueOf(sc.getType().toString()));
        cc.setUrl(sc.getUrl());
        cc.setUsername(sc.getUsername());
        cc.setPassword(sc.getPassword());
        cc.setCatalog(sc.getCatalog());
        cc.setSchemaData(sc.getSchemaData());
        cc.setId(sc.getId());

        return cc;
    }

    private SavedConnection convert(final CubeConnection cc) {

        final SavedConnection sc = new SavedConnection(cc.getId());

        sc.setName(cc.getName());
        sc.setDriverClassName(cc.getDriverClassName());
        sc.setUrl(cc.getUrl());
        sc.setCatalog(cc.getCatalog());
        sc.setUsername(cc.getUsername());
        sc.setPassword(cc.getPassword());
        sc.setType(org.pentaho.pat.server.data.pojo.ConnectionType.getInstance(cc.getConnectionType().name()));
        sc.setSchemaData(cc.getSchemaData());

        return sc;
    }

}
