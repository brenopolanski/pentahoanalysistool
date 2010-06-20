/*
 * Copyright (C) 2010 Paul Stoellberger
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

package org.pentaho.pat.plugin;

import java.io.OutputStream;
import java.security.InvalidParameterException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.pentaho.pat.plugin.messages.Messages;
import org.pentaho.pat.plugin.util.PatSolutionFile;
import org.pentaho.pat.plugin.util.PluginConfig;
import org.pentaho.pat.server.servlet.ExportController;
import org.pentaho.pat.server.servlet.QueryServlet;
import org.pentaho.pat.server.servlet.SessionServlet;
import org.pentaho.platform.api.engine.IParameterProvider;
import org.pentaho.platform.api.engine.IServiceManager;
import org.pentaho.platform.api.repository.IContentItem;
import org.pentaho.platform.api.repository.ISolutionRepository;
import org.pentaho.platform.engine.core.solution.ActionInfo;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.engine.services.solution.SimpleContentGenerator;
import org.pentaho.platform.util.messages.LocaleHelper;

/**
 * A simple content generator that redirects .xpav content to PAT 
 *
 * @author Paul Stoellberger
 *
 */
public class PatContentGenerator extends SimpleContentGenerator {


    private static final long serialVersionUID = -9180003935693305152L;
    private static final Log LOG = LogFactory.getLog(PatContentGenerator.class);

    PatSolutionFile solutionFile = null;

    @Override
    public void createContent() throws Exception {

        if( outputHandler == null ) {
            LOG.error(Messages.getString("ContentGenerator.OutputHandler.Null")); //$NON-NLS-1$
            throw new InvalidParameterException(Messages.getString("ContentGenerator.OutputHandler.Null"));  //$NON-NLS-1$
        }

        IParameterProvider requestParams = parameterProviders.get( IParameterProvider.SCOPE_REQUEST );
        if( requestParams == null ) {
            LOG.error(Messages.getString("ContentGenerator.Params.ProviderIsNull")); //$NON-NLS-1$
            throw new NullPointerException(Messages.getString("ContentGenerator.Params.ProviderIsNull"));  //$NON-NLS-1$
        }
        String solution = requestParams.getStringParameter("solution", null); //$NON-NLS-1$
        String path = requestParams.getStringParameter("path", null); //$NON-NLS-1$
        String action = requestParams.getStringParameter("action", null); //$NON-NLS-1$
        String fullPath = ActionInfo.buildSolutionPath(solution, path, action);
        ISolutionRepository repository = PentahoSystem.get(ISolutionRepository.class, userSession);
        if( repository == null ) {
            LOG.error(Messages.getString("ContentGenerator.RepositoryAccessFailed")); //$NON-NLS-1$
            throw new NullPointerException(Messages.getString("ContentGenerator.RepositoryAccessFailed"));  //$NON-NLS-1$
        }
        if (repository.resourceExists(fullPath)) {
            Document doc = repository.getResourceAsDocument(fullPath);
            solutionFile = PatSolutionFile.convertDocument(doc);
            if (solutionFile == null) {
                LOG.error(Messages.getString("ContentGenerator.CantParseSolutionfile")); //$NON-NLS-1$
                throw new NullPointerException(Messages.getString("ContentGenerator.CantParseSolutionfile"));  //$NON-NLS-1$
            }
            try {
                IServiceManager serviceManager = (IServiceManager) PentahoSystem.get(IServiceManager.class, PentahoSessionHolder.getSession());
                QueryServlet targetQueryBean = (QueryServlet)serviceManager.getServiceBean("gwt","query.rpc");
                SessionServlet targetSessionBean = (SessionServlet)serviceManager.getServiceBean("gwt","session.rpc");
                String sessionId = targetSessionBean.createSession();
                targetQueryBean.addBootstrapQuery(sessionId, solutionFile.getConnectionId(), solutionFile.getTitle(), solutionFile.getQueryXml());
                super.createContent();
            }
            catch (Exception e) {
                LOG.error(Messages.getString("ContentGenerator.CantInjectQuery"),e); //$NON-NLS-1$
                throw new NullPointerException(Messages.getString("ContentGenerator.CantInjectQuery"));  //$NON-NLS-1$
            }

        }
        else if(requestParams.getStringParameter("query", null) != null) {
            OutputStream out = null;

            IContentItem contentItem = outputHandler.getOutputContentItem("response", "content", "", instanceId, getMimeType()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

            if (contentItem == null) {
                LOG.error(Messages.getString("ContentGenerator.NoContentItem")); //$NON-NLS-1$
                throw new NullPointerException(Messages.getString("ContentGenerator.NoContentItem"));  //$NON-NLS-1$
            }

            out = contentItem.getOutputStream(null);
            
            byte[] resultExcel = ExportController.exportExcel(requestParams.getStringParameter("query", null));
            if (resultExcel != null && resultExcel.length > 0) {
                contentItem.setMimeType("application/vnd.ms-excel");
                out.write(resultExcel);
            }

            out.flush();
            out.close();
        }
        else {
            super.createContent();
            LOG.debug(PluginConfig.PAT_PLUGIN_NAME + " : CONTENT GENERATOR - DEFAULT OUTPUT");   
        }

    }

    @Override
    public void createContent(OutputStream out) throws Exception {
        try {
            StringBuilder html = new StringBuilder();
            html.append("<html>");
            html.append("<head>");

            html.append("<meta name='gwt:module' content='org.pentaho.pat.Pat'/>");
            html.append("     <meta name=\"gwt:property\" content=\"locale=<%= request.getLocale() %>\">");
            html.append("</head>");
            html.append("<body>");
            html.append("<script language=\"javascript\" src=\"/pentaho/content/pat-res/pat/pat.nocache.js\"></script>");
            html.append("<iframe src=\"javascript:''\" id=\"__gwt_historyFrame\" tabIndex='-1' style=\"position: absolute; width: 0; height: 0; border: 0\"></iframe>");
            html.append("<div id=\"splash\" style=\"height:100%;width:100%;\">");
            html.append("<table width=\"100%\" height=\"90%\">");
            html.append("<tr>");
            html.append("<td width=\"100%\" height=\"100%\" align=\"center\" valign=\"middle\">");
            html.append("<div id=\"splash_loading\" alttext=\"Error\">Loading</div>");
            html.append("</td>");
            html.append("</tr>");
            html.append("</table>");
            html.append("</div>");
            html.append("</body>");
            html.append("</html>");
            out.write(html.toString().getBytes(LocaleHelper.getSystemEncoding()));

        } catch (Exception e) {
            throw new Exception(Messages.getString("ContentGenerator.GwtStartupError"),e);
        }


    }

    public String getMimeType() {
        return "text/html";
    }

    public Log getLogger() {
        return LogFactory.getLog(PatContentGenerator.class);
    }

}
