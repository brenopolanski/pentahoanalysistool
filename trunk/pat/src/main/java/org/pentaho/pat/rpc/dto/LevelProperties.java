/**
 * TODO JAVADOC
 */
package org.pentaho.pat.rpc.dto;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class LevelProperties implements Serializable, IsSerializable {

    /**
     *TODO JAVADOC
     */
    private static final long serialVersionUID = 1L;
    private String levelName;
    private String propertyName;

    public LevelProperties(){
        super();
    }

    public LevelProperties(String levelName, String propertyName) {
      this.levelName = levelName;
      this.propertyName = propertyName;
    }

    public String getLevelName(){
        return levelName;
    }
    public String getPropertyName() {
      return propertyName;
    }

    public void setPropertyName(String propertyName) {
      this.propertyName = propertyName;
    }

    public void setLevelName(String levelName) {
      this.levelName = levelName;
    }

    @Override
    public String toString() {
      return getLevelName() + " " + getPropertyName();
    }
}
