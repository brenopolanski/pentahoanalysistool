/*
 * Copyright (C) 2009 Paul Stoellberger
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

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.pat.plugin.PatLifeCycleListener;
import org.pentaho.pat.plugin.util.PatSolutionFile;
import org.pentaho.pat.plugin.util.PluginConfig;
import org.pentaho.pat.rpc.IPlatform;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.pentaho.pat.server.messages.Messages;
import org.pentaho.pat.server.services.QueryService;
import org.pentaho.pat.server.services.SessionService;
import org.pentaho.platform.api.engine.ISolutionFile;
import org.pentaho.platform.api.repository.ISolutionRepository;
import org.pentaho.platform.engine.core.solution.ActionInfo;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.pentaho.platform.engine.core.system.PentahoSystem;

/**
 * @author Paul Stoellberger
 * 
 */
public class PlatformServlet extends AbstractServlet implements IPlatform {

    private static final long serialVersionUID = 1L;

    private QueryService queryService;

    private final static Log LOG = LogFactory.getLog(PlatformServlet.class);

    public void init() throws ServletException {
        super.init();
        queryService = (QueryService) applicationContext.getBean("queryService"); //$NON-NLS-1$
        final SessionService sessionService = (SessionService) applicationContext.getBean("sessionService"); //$NON-NLS-1$
        if (queryService == null) {
            throw new ServletException(Messages.getString("Servlet.QueryServiceNotFound")); //$NON-NLS-1$
        }
        if (sessionService == null) {
            throw new ServletException(Messages.getString("Servlet.SessionServiceNotFound")); //$NON-NLS-1$
        }
    }

    public  void saveQueryToSolution(String sessionId, String queryId, String connectionId, String solution,
            String path, String name, String localizedFileName) throws RpcException {
        try {

            //String fullPath = ActionInfo.buildSolutionPath(solution, path, name);
            ISolutionRepository repository = PentahoSystem.get(ISolutionRepository.class, PentahoSessionHolder.getSession());
            try {
                PatSolutionFile solutionFile = new PatSolutionFile(name,name,"",connectionId,queryId);
                String xml = solutionFile.toXml();

                if (!name.endsWith(".xpav")) {
                    name = name + ".xpav";
                }
                String base = PentahoSystem.getApplicationContext().getSolutionRootPath();
                String parentPath = ActionInfo.buildSolutionPath(solution, path, "");
                ISolutionFile parentFile = repository.getSolutionFile(parentPath, ISolutionRepository.ACTION_CREATE);
                String filePath = parentPath + ISolutionRepository.SEPARATOR + name;
                ISolutionFile fileToSave = repository.getSolutionFile(filePath, ISolutionRepository.ACTION_UPDATE);

                if (fileToSave != null || (!repository.resourceExists(filePath) && parentFile != null)) {
                    repository.publish(base, '/' + parentPath, name, xml.getBytes(), true);
                    LOG.debug(PluginConfig.PAT_PLUGIN_NAME + " : Published " + solution + " / " + path + " / " + name );
                } else {
                    throw new Exception(org.pentaho.pat.plugin.messages.Messages.getString("PlatformServlet.ErrorPublish"));
                }
            } catch (Exception e) {
                LOG.error(e.getMessage(),e);
                throw new Exception(e);
            }

        }
        catch (Exception e) {
            LOG.error("Platform Servlet Error",e);
            throw new RpcException("test");
        }
    }

}
