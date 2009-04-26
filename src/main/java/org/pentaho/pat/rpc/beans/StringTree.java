package org.pentaho.pat.rpc.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class StringTree implements Serializable, IsSerializable {
    private static final long serialVersionUID = 1L;

    String value;

    List<StringTree> children;
    StringTree parent;

    public StringTree() {
        children = new ArrayList<StringTree>();
    }

    public StringTree(String value, StringTree parent) {
        this();
        this.value = value;
        this.parent = parent;
        if (this.parent != null) {
            this.parent.addChild(this);
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void addChild(StringTree stringTree) {
        children.add(stringTree);
    }

    public List<StringTree> getChildren() {
        return children;
    }

    public StringTree getParent() {
        return parent;
    }

    public void setParent(StringTree parent) {
        this.parent = parent;
    }

    public boolean hasChildren() {
        return children.size() > 0;
    }

}
