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

package org.pentaho.pat;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.security.InvalidParameterException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.pentaho.platform.api.engine.IParameterProvider;
import org.pentaho.platform.api.repository.ISolutionRepository;
import org.pentaho.platform.engine.core.solution.ActionInfo;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.engine.services.solution.SimpleContentGenerator;
import org.pentaho.platform.util.messages.LocaleHelper;
import org.pentaho.platform.util.xml.dom4j.XmlDom4JHelper;

/**
 * A simple content generator that redirects .xpav content to the Pentaho 
 *  Analysis Tool (http://code.google.com/p/pentahoanalysistool/).
 *
 * @author gmoran
 *
 */
public class PatContentGenerator extends SimpleContentGenerator {


    private static final long serialVersionUID = -9180003935693305152L;

    public static final String xmlaUrlParameter =       "XMLA_URL";
    public static final String xmlaUsernameParameter =  "XMLA_USERNAME";
    public static final String xmlaPasswordParameter =  "XMLA_PASSWORD";

    ArrayList <String> problemMessages = new ArrayList<String>();

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


        String url = null;
        if (repository.resourceExists(fullPath)) {
            Document doc = repository.getResourceAsDocument(fullPath);
            url = getLaunchURL(doc);
        }

        if (null==url){
            // Something bad happened... send error messages back to the client.
            super.createContent();
        }else{
            // Someday, we'll do something smarter here, but for now, we just redirect to PAT, then leave 
            // it alone to do it's thing.
            outputHandler.setOutput("redirect", url);
        }
    }

    @Override
    public void createContent(OutputStream out) throws Exception {
        //      outputHandler.setOutput("redirect", "http://localhost:8080/pentaho/content/pat/pat-res/pat.html");
        //    
        //    StringBuilder html = new StringBuilder();
        //    html.append("<html>");
        //    html.append("<h3>Pentaho Analysis Tool failed to launch. Hopefully some of the messages provided below will prove useful in figuring out why.</h3>");
        //    html.append("<ol>");
        //    for (String  message : problemMessages) {
        //      html.append("<li>").append(message).append("</li>");
        //    }
        //    html.append("</ol>");
        //    html.append("</html>");
        //    out.write(html.toString().getBytes(LocaleHelper.getSystemEncoding()));

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

    private String getLaunchURL(Document doc)throws Exception{

        //TODO Provide a model for Pentaho Analysis View content so that we are not parsing raw 
        //     XML in the business logic.

        // REQUIRED: the XML/A url and the PAT application url

        String appConnection = XmlDom4JHelper.getNodeText( "/pav/app-connection", doc, null);
        if (null==appConnection){
            problemMessages.add("Invalid analysis view content. The application connection must be defined. Double check the xml in your .xpav file for errors.");
        }

        String xmlaConnection = XmlDom4JHelper.getNodeText( "/pav/xmla-provider/@url", doc, null);
        if (null==xmlaConnection){
            problemMessages.add("Invalid analysis view content. The XML/A connection must be defined.  Double check the xml in your .xpav file for errors.");
        }

        if ((xmlaConnection != null) && (xmlaConnection.trim().length()>0)){
            try {
                xmlaConnection = URLEncoder.encode(xmlaConnection, LocaleHelper.getSystemEncoding());
            } catch (Exception e) {
                problemMessages.add("Encountered encoding problem encoding " + xmlaConnection);
            }
        }

        if (problemMessages.size()>0){
            return null;
        }

        // OPTIONAL: credentials for XML/A provider... very very prototype...

        String xmlaUser = XmlDom4JHelper.getNodeText( "/pav/xmla-provider/@username", doc, "");
        String xmlaPassword = XmlDom4JHelper.getNodeText( "/pav/xmla-provider/@password", doc, "");
        String url = appConnection; //.concat("?").concat(xmlaUrlParameter).concat("=").concat(xmlaConnection);
        //    if ((xmlaUser!=null) &&(xmlaPassword!=null)){   
        //      url = url.concat("&").concat(xmlaUsernameParameter).concat("=").concat(xmlaUser).concat("&")
        //               .concat(xmlaPasswordParameter).concat("=").concat(xmlaPassword);
        //    }

        return url;
    }

    public String getMimeType() {
        return "text/html";
    }

    public Log getLogger() {
        return LogFactory.getLog(PatContentGenerator.class);
    }

}
