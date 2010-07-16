package org.pentaho.pat.rpc.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class StringTree implements Serializable, IsSerializable {
    private static final long serialVersionUID = 1L;

    private String value;

    private String caption;
    
    private List<StringTree> children;

    private StringTree parent;

    public StringTree() {
        children = new ArrayList<StringTree>();
    }

    public StringTree(final String value, final StringTree parent) {
        this();
        this.value = value;
        this.parent = parent;
        if (this.parent != null) {
            this.parent.addChild(this);
        }
    }

    public StringTree(final String value, final String caption, final StringTree parent) {
        this();
        this.value = value;
        this.caption = caption;
        this.parent = parent;
        if (this.parent != null) {
            this.parent.addChild(this);
        }
    }
    
    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(final String caption) {
        this.caption = caption;
    }
    public void addChild(final StringTree stringTree) {
        children.add(stringTree);
    }

    public List<StringTree> getChildren() {
        return children;
    }

    public StringTree getParent() {
        return parent;
    }

    public void setParent(final StringTree parent) {
        this.parent = parent;
    }

    public boolean hasChildren() {
        return children.size() > 0;
    }

}
