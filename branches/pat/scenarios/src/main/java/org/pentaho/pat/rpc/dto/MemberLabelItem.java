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
                return this.caption.compareTo(o.getCaption());
        if (this.name != null)
            if (o != null)
                return this.name.compareTo(o.getName());
        return 0;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((caption == null) ? 0 : caption.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MemberLabelItem other = (MemberLabelItem) obj;
        if (caption == null) {
            if (other.caption != null)
                return false;
        } else if (!caption.equals(other.caption))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
