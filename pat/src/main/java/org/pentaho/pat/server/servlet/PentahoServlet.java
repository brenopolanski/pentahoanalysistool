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

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.pentaho.pat.server.services.DiscoveryService;
import org.pentaho.pat.server.services.QueryService;
import org.pentaho.pat.server.services.SessionService;
import org.pentaho.pat.server.services.impl.SessionServiceImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

/**
 * This controller is meant to recieve redirects from Pentaho User Console and do the necessary preparations on PAT so a
 * user can create analysis views.
 * 
 * @author Luc Boudreau
 */
public class PentahoServlet implements InitializingBean, ServletContextAware {

    protected SessionService sessionService = null;

    protected QueryService queryService = null;

    protected DiscoveryService discoveryService = null;

    protected String redirectTarget = "/pat/Pat.jsp"; //$NON-NLS-1$

    protected String xmlaUrlParameter = "XMLA_URL"; //$NON-NLS-1$

    protected String xmlaUsernameParameter = "XMLA_USERNAME"; //$NON-NLS-1$

    protected String xmlaPasswordParameter = "XMLA_PASSWORD"; //$NON-NLS-1$

    public void simpleXmla(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws Exception {
        StringBuilder redirect = new StringBuilder(redirectTarget);

        // Validate url.
        String xmlaUrl = request.getParameter(xmlaUrlParameter);
        if (!this.verifyXmlaUrl(xmlaUrl))
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "A valid XMLA service URL is required."); //$NON-NLS-1$

        // Validate MDX
        // String mdxQuery = request.getParameter(mdxQueryParameter);
        // if (mdxQuery==null
        // || mdxQuery.length()<1) {
        // response.sendError(HttpServletResponse.SC_BAD_REQUEST,
        // "A valid MDX query is required.");
        // }

        // These two are optional. No need to validate.
        String xmlaUsername = request.getParameter(xmlaUsernameParameter);
        String xmlaPassword = request.getParameter(xmlaPasswordParameter);

        // Create a new session.
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String sessionId = this.sessionService.createNewSession(userId);

        // Build the URL
        String olap4jUrl = "jdbc:xmla:Server=".concat(xmlaUrl); //$NON-NLS-1$

        // Establish the connection
        ((SessionServiceImpl) this.sessionService).createConnection(userId, sessionId,
                "org.olap4j.driver.xmla.XmlaOlap4jDriver", olap4jUrl, xmlaUsername, xmlaPassword); //$NON-NLS-1$

        // Build the redirect URL
        redirect.append("?MODE=BISERVERPUC"); //$NON-NLS-1$
        redirect.append("&SESSION=").append(sessionId); //$NON-NLS-1$

        // Send the redirect HTTP message
        response.sendRedirect(request.getContextPath().concat(redirect.toString()));
    }

    private boolean verifyXmlaUrl(String xmlaUrl) {
        if (xmlaUrl == null)
            return false;

        try {
            URL url = new URL(xmlaUrl);
            // TODO support connection timeout.
            URLConnection connection = url.openConnection();
            if (connection instanceof HttpURLConnection) {
                HttpURLConnection.setFollowRedirects(true);
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.connect();
                return true;
            } else {
                return false;
            }
        } catch (Throwable t) {
            // TODO log this
            return false;
        }
    }

    public void setRedirectTarget(String redirectTarget) {
        this.redirectTarget = redirectTarget;
    }

    public void setXmlaUrlParameter(String xmlaUrlParameter) {
        this.xmlaUrlParameter = xmlaUrlParameter;
    }

    public void setXmlaUsernameParameter(String xmlaUsernameParameter) {
        this.xmlaUsernameParameter = xmlaUsernameParameter;
    }

    public void setXmlaPasswordParameter(String xmlaPasswordParameter) {
        this.xmlaPasswordParameter = xmlaPasswordParameter;
    }

    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public void setQueryService(QueryService queryService) {
        this.queryService = queryService;
    }

    public void setDiscoveryService(DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.sessionService);
        Assert.notNull(this.queryService);
        Assert.notNull(this.discoveryService);
    }

    public void setServletContext(ServletContext servletContext) {
    }
}
