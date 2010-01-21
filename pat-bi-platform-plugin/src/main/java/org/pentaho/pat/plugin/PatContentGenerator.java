/*
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License, version 2 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/gpl-2.0.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 *
 * Copyright 2009 Pentaho Corporation.  All rights reserved. 
 * 
 */

package org.pentaho.pat.plugin;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.security.InvalidParameterException;
import java.util.ArrayList;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.pentaho.pat.plugin.util.PatSolutionFile;
import org.pentaho.pat.server.servlet.ExportController;
import org.pentaho.pat.server.servlet.QueryServlet;
import org.pentaho.platform.api.engine.IParameterProvider;
import org.pentaho.platform.api.engine.IServiceManager;
import org.pentaho.platform.api.repository.IContentItem;
import org.pentaho.platform.api.repository.ISolutionRepository;
import org.pentaho.platform.engine.core.solution.ActionInfo;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.engine.services.solution.SimpleContentGenerator;
import org.pentaho.platform.util.messages.LocaleHelper;
import org.pentaho.platform.util.xml.dom4j.XmlDom4JHelper;

/**
 * A simple content generator that redirects .xpav content to PAT 
 *
 * @author Paul Stoellberger
 *
 */
public class PatContentGenerator extends SimpleContentGenerator {


    private static final long serialVersionUID = -9180003935693305152L;

    ArrayList <String> problemMessages = new ArrayList<String>();
    PatSolutionFile solutionFile = null;

    @Override
    public void createContent() throws Exception {

        if( outputHandler == null ) {
            error( "No output handler" ); //$NON-NLS-1$
            throw new InvalidParameterException( "No output handler" );  //$NON-NLS-1$
        }

        // Get the .xpav content file from the repository, then grab the application info and
        // parameters needed to launch PAT...
        IParameterProvider requestParams = parameterProviders.get( IParameterProvider.SCOPE_REQUEST );
        String solution = requestParams.getStringParameter("solution", null); //$NON-NLS-1$
        String path = requestParams.getStringParameter("path", null); //$NON-NLS-1$
        String action = requestParams.getStringParameter("action", null); //$NON-NLS-1$
        String fullPath = ActionInfo.buildSolutionPath(solution, path, action);
        ISolutionRepository repository = PentahoSystem.get(ISolutionRepository.class, userSession);

        if (repository.resourceExists(fullPath)) {
            Document doc = repository.getResourceAsDocument(fullPath);
            solutionFile = PatSolutionFile.convertDocument(doc);
            if (solutionFile == null) {
                throw new Exception("Can not cast xpav file");
            }
            IServiceManager serviceManager = (IServiceManager) PentahoSystem.get(IServiceManager.class, PentahoSessionHolder.getSession());
            Object targetQueryBean = serviceManager.getServiceBean("gwt","query.rpc");
            ((QueryServlet)targetQueryBean).addBootstrapQuery(solutionFile.getQueryId());
            super.createContent();

        }
        else if(requestParams.getStringParameter("query", null) != null) {
            OutputStream out = null;
            if (outputHandler == null) {
                throw new InvalidParameterException("ERROR NO_OUTPUT_HANDLER");  //$NON-NLS-1$
            }

            IContentItem contentItem = outputHandler.getOutputContentItem("response", "content", "", instanceId, getMimeType()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

            if (contentItem == null) {
                throw new InvalidParameterException("ERROR NO CONTENT ITEM");  //$NON-NLS-1$
            }

            out = contentItem.getOutputStream(null);
            contentItem.setMimeType("application/vnd.ms-excel");
            ExportController.exportExcel(requestParams.getStringParameter("query", null), out);
            
        }
        else {
            super.createContent();
            System.out.println("--------- DEFAULT OUTPUT");   
        }
            //outputHandler.setOutput("redirect", url);

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
            e.printStackTrace();
        }


    }

    public String getMimeType() {
        return "text/html";
    }

    public Log getLogger() {
        return LogFactory.getLog(PatContentGenerator.class);
    }

}
