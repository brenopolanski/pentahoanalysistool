/**
 * TODO JAVADOC
 */
package org.pentaho.pat.rpc.dto;

import java.io.Serializable;
import java.util.Date;

import org.pentaho.pat.rpc.dto.enums.QueryType;

/**
 *TODO JAVADOC
 * 
 * @author bugg
 * 
 */
public class QuerySaveModel implements Serializable {

    /**
     *TODO JAVADOC
     */
    private static final long serialVersionUID = 1L;

    private String name;

    private String connection;
    
    private String queryId;

    private Date savedDate;

    private String id;

    private CubeItem cube;

    private String cubeName;

    private QueryType queryType;
    
    @SuppressWarnings("unused")
    private QuerySaveModel() {

    }

    /*
     * public QuerySaveModel(String id, String name, String connection, Date savedDate) { this.name = name;
     * this.connection = connection; this.id = id; this.savedDate = savedDate; }
     */
    public QuerySaveModel(final String id,final String queryId, final String name, final String connection, final CubeItem cube,
            final String cubeName, final Date updatedDate, final QueryType queryType) {
        this.name = name;
        this.queryId = queryId;
        this.connection = connection;
        this.id = id;
        this.cube = cube;
        this.cubeName = cubeName;
        this.savedDate = updatedDate;
        this.setQueryType(queryType);
    }

    /**
     *TODO JAVADOC
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     *TODO JAVADOC
     * 
     * @param name
     *            the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     *TODO JAVADOC
     * 
     * @return the connection
     */
    public String getConnection() {
        return connection;
    }

    /**
     * 
     *TODO JAVADOC
     * 
     * @param connection
     *            the connection to set
     */
    public void setConnection(final String connection) {
        this.connection = connection;
    }

    /**
     *TODO JAVADOC
     * 
     * @return the savedDate
     */
    public Date getSavedDate() {
        return savedDate;
    }

    /**
     * 
     *TODO JAVADOC
     * 
     * @param savedDate
     *            the savedDate to set
     */
    public void setSavedDate(final Date savedDate) {
        this.savedDate = savedDate;
    }

    @Override
    public String toString() {
        return getName() + " " + getConnection() + " " + getSavedDate(); //$NON-NLS-1$//$NON-NLS-2$
    }

    /**
     * 
     *TODO JAVADOC
     * 
     * @param id
     *            the id to set
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     *TODO JAVADOC
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     *TODO JAVADOC
     * 
     * @param cube
     *            the cube to set
     */
    public void setCube(final CubeItem cube) {
        this.cube = cube;
    }

    /**
     *TODO JAVADOC
     * 
     * @return the cube
     */
    public CubeItem getCube() {
        return cube;
    }

    /**
     * 
     *TODO JAVADOC
     * 
     * @param cubeName
     *            the cubeName to set
     */
    public void setCubeName(final String cubeName) {
        this.cubeName = cubeName;
    }

    /**
     *TODO JAVADOC
     * 
     * @return the cubeName
     */
    public String getCubeName() {
        return cubeName;
    }

    /**
     *
     *TODO JAVADOC
     * @param queryType the queryType to set
     */
    public void setQueryType(final QueryType queryType) {
        this.queryType = queryType;
    }

    /**
     *TODO JAVADOC
     * @return the queryType
     */
    public QueryType getQueryType() {
        return queryType;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getQueryId() {
        return queryId;
    }

}
