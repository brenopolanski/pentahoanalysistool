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

package org.pentaho.pat.plugin.util;

import java.io.InputStream;

import org.dom4j.Document;

import com.thoughtworks.xstream.XStream;

public class PatSolutionFile {

    private String title = null;
    private String description = null;
    private String author = null;
    private String icon = "pat-icon.png";
    
    private String queryXml = null;
    private String connectionId = null;
    
    public PatSolutionFile() {
        
    }
    
    public PatSolutionFile(String title, String description, String author) {
        this.title = title;
        this.description = description;
        this.author = author;
    }

    public PatSolutionFile(String title, String description, String author, String connectionId, String queryXml) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.connectionId = connectionId;
        this.queryXml = queryXml;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    public String getIcon() {
        return icon;
    }

    public String getQueryXml() {
        return queryXml;
    }

    public void setQueryXml(String queryXml) {
        this.queryXml = queryXml;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public String toXml() {
        final XStream xstream = new XStream();
        xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
        //xstream.alias("pav", PatSolutionFile.class); //$NON-NLS-1$
        
        return xstream.toXML(this);
    }
    
    public static PatSolutionFile convertDocument(Document doc) {
        final XStream xstream = new XStream();
        xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
//        xstream.alias("pav", PatSolutionFile.class); //$NON-NLS-1$
        
        Object oQuery = xstream.fromXML(doc.asXML());
        if (oQuery instanceof PatSolutionFile) {
            return (PatSolutionFile) oQuery;
        }
        else
            return null;
    }
    
    public static PatSolutionFile convertDocument(InputStream in) {
        final XStream xstream = new XStream();
        xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
//        xstream.alias("pav", PatSolutionFile.class); //$NON-NLS-1$
        
        Object oQuery = xstream.fromXML(in);
        if (oQuery instanceof PatSolutionFile) {
            return (PatSolutionFile) oQuery;
        }
        else
            return null;
    }

}
