package org.pentaho.pat.rpc.dto;

import java.io.Serializable;
import java.util.List;

public class MemberLabelItem implements Serializable, Comparable<MemberLabelItem> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5090483873584711899L;

	private List<String> parents;
	
	private String name;
	
	private String caption;
	
	public MemberLabelItem(){
	}
	
	public MemberLabelItem(String name, String caption, List<String> parents){
		this.setName(name);
		this.setCaption(caption);
		this.setParents(parents);
	}

	public void setParents(List<String> parents) {
		this.parents = parents;
	}

	public List<String> getParents() {
		return parents;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getCaption() {
		return caption;
	}

    public int compareTo(MemberLabelItem o) {
        if (this.caption != null)
            if (o != null)
                return this.caption.compareTo(o.getName());
        if (this.name != null)
            if (o != null)
                return this.name.compareTo(o.getName());
        return 0;
    }
}
