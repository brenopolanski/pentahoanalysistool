/**
 * TODO JAVADOC
 */
package org.pentaho.pat.server.data.pojo;

import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(SavedQuery arg0) {
        // TODO Auto-generated method stub
        return 0;
    }
}
