/**
 * TODO JAVADOC
 */
package org.pentaho.pat.rpc.dto;

/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class QuerySaveModel {

    private String name;
    private String connection;
    private String savedDate;
    
    public QuerySaveModel(String name, String connection, String savedDate) {
        this.name = name;
        this.connection = connection;
        this.savedDate = savedDate;
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
    public String getSavedDate() {
        return savedDate;
    }

    /**
     *
     *TODO JAVADOC
     * @param savedDate the savedDate to set
     */
    public void setSavedDate(String savedDate) {
        this.savedDate = savedDate;
    }
    
    @Override
    public String toString() {
      return getName() + " " + getConnection() + " " + getSavedDate();  //$NON-NLS-1$//$NON-NLS-2$
    }

}
