package org.pentaho.pat.rpc.dto;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Selection  implements IsSerializable, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String member;
    String dimension;
    String hierachyname;
    String levelname;
    String operator;
    
    public Selection(){
	
    }
    
    String getName() {
	return null;
    }

    void setName(String name) {
    }

    String getMember() {
	return null;
    }

    String getDimension() {
	return null;
    }

    String getHierarchyName() {
	return null;
    }

    String getLevelName() {
	return null;
    }

    Operator getOperator() {
	return null;
    }

    // @pre operator != null
    void setOperator(Operator operator) {
    }

    /**
     * Defines which selection operators are allowed, relative to
     * a root member.
     */
    public enum Operator {
        /**
         * Only the root member will be selected.
         */
        MEMBER,
        /**
         * Only the children of the root member will be selected.
         * This excludes the root member itself.
         * <p>Implemented via the MDX .Children member property.
         */
        CHILDREN,
        /**
         * The root member will be selected along with all it's
         * children.
         */
        INCLUDE_CHILDREN,
        /**
         * Will select the root member along with all it's siblings.
         * <p>Implemented via the MDX .Siblings member property.
         */
        SIBLINGS,
        /**
         * Selects the set of the ascendants of a specified member,
         * including the member itself.
         * <p>Implemented via the MDX Ascendants() function.
         */
        ANCESTORS,
        /**
         * Selects the set of the descendants of a specified member,
         * including the member itself.
         * <p>Implemented via the MDX Descendants() function.
         */
        DESCENDANTS;
    }

}
