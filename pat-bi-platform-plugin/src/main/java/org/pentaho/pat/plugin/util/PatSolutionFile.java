package org.pentaho.pat.plugin.util;

import org.dom4j.Document;

import com.thoughtworks.xstream.XStream;

public class PatSolutionFile {

    private String title = null;
    private String description = null;
    private String author = null;
    private String icon = "pat-icon.png";
    
    private String connectionId = null;
    private String queryId = null;
    
    public PatSolutionFile() {
        
    }
    
    public PatSolutionFile(String title, String description, String author) {
        this.title = title;
        this.description = description;
        this.author = author;
    }

    public PatSolutionFile(String title, String description, String author, String connectionId, String queryId) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.connectionId = connectionId;
        this.queryId = queryId;
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

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    public String getIcon() {
        return icon;
    }

    public String toXml() {
        final XStream xstream = new XStream();
        xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
        xstream.alias("pav", PatSolutionFile.class); //$NON-NLS-1$
        
        return xstream.toXML(this);
    }
    
    public static PatSolutionFile convertDocument(Document doc) {
        final XStream xstream = new XStream();
        xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
        xstream.alias("pav", PatSolutionFile.class); //$NON-NLS-1$
        
        Object oQuery = xstream.fromXML(doc.asXML());
        if (oQuery instanceof PatSolutionFile) {
            return (PatSolutionFile) oQuery;
        }
        else
            return null;
    }
}
