/**
 * TODO JAVADOC
 */
package org.pentaho.pat.server.data.pojo;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.pentaho.pat.rpc.dto.CubeItem;



/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
@Entity
@Table(name="QUERIES")
public class SavedQuery implements Comparable<SavedQuery> {

    /**
     * The UUID of this connection.
     */
    @Basic
    private String id;
    
    /**
     * The name of this connection.
     */
    @Basic
    private String name;
    
    @Basic
    private String xml;
    
    @Basic
    private String username;

    @Basic
    private String connectionId;
    
    @Basic 
    private String cubeName;
    
    @Basic
    private Date updatedDate;
    
    @Basic
    @Type(
        type = "org.pentaho.pat.rpc.dto.CubeItem"
    )
    private CubeItem cube;
    
    public SavedQuery() {
        this(null);
    }

    public SavedQuery(String uuid) {
        if (uuid == null) {
            this.id = UUID.randomUUID().toString();
        } else {
            this.id = uuid;
        }
    }
    
    @Id
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXml(){
        return xml;
    }
    
    public void setXml(String xml){
        this.xml = xml;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     *TODO JAVADOC
     * @param connectionId the connectionId to set
     */
    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    /**
     *TODO JAVADOC
     * @return the connectionId
     */
    public String getConnectionId() {
        return connectionId;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(SavedQuery arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     *TODO JAVADOC
     * @return the cubeName
     */
    public String getCubeName() {
        return cubeName;
    }

    /**
     *
     *TODO JAVADOC
     * @param cubeName the cubeName to set
     */
    public void setCubeName(String cubeName) {
        this.cubeName = cubeName;
    }

    /**
     *TODO JAVADOC
     *
     * @param cube
     */
    public void setCube(CubeItem cube) {
        this.cube = cube;
        
    }
    
    public CubeItem getCube(){
        return cube;
    }

    /**
     *
     *TODO JAVADOC
     * @param updatedDate the updatedDate to set
     */
    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     *TODO JAVADOC
     * @return the updatedDate
     */
    public Date getUpdatedDate() {
        return updatedDate;
    }
}
