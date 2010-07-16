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
public class MemberItem implements Serializable, IsSerializable {

    /**
     *TODO JAVADOC
     */
    private static final long serialVersionUID = 1L;
    private String memberName;
    private String captionName;

    public MemberItem(){
        super();
    }

    public MemberItem(String levelName, String propertyName) {
      this.memberName = levelName;
      this.captionName = propertyName;
    }

    public String getLevelName(){
        return memberName;
    }
    public String getPropertyName() {
      return captionName;
    }

    public void setPropertyName(String propertyName) {
      this.captionName = propertyName;
    }

    public void setLevelName(String levelName) {
      this.memberName = levelName;
    }

}
