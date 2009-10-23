/**
 * TODO JAVADOC
 */
package org.pentaho.pat.rpc.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class QuerySaveModel implements Serializable{

    /**
     *TODO JAVADOC
     */
    private static final long serialVersionUID = 1L;
    private String name;
    private String connection;
    private Date savedDate;
    private String id;
    
    private QuerySaveModel() {
        
    }
    
    public QuerySaveModel(String id, String name, String connection, Date savedDate) {
        this.name = name;
        this.connection = connection;
        this.id = id;
        this.savedDate = savedDate;
    }
    public QuerySaveModel(String id, String name, String connection) {
        this.name = name;
        this.connection = connection;
        this.id = id;
    }
    /**
     *TODO JAVADOC
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     *
     *TODO JAVADOC
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *TODO JAVADOC
     * @return the connection
     */
    public String getConnection() {
        return connection;
    }

    /**
     *
     *TODO JAVADOC
     * @param connection the connection to set
     */
    public void setConnection(String connection) {
        this.connection = connection;
    }

    /**
     *TODO JAVADOC
     * @return the savedDate
     */
    public Date getSavedDate() {
        return savedDate;
    }

    /**
     *
     *TODO JAVADOC
     * @param savedDate the savedDate to set
     */
    public void setSavedDate(Date savedDate) {
        this.savedDate = savedDate;
    }
    
    @Override
    public String toString() {
      return getName() + " " + getConnection() + " " + getSavedDate();  //$NON-NLS-1$//$NON-NLS-2$
    }

    /**
     *
     *TODO JAVADOC
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *TODO JAVADOC
     * @return the id
     */
    public String getId() {
        return id;
    }

}
