package org.pentaho.pat.rpc.dto;

import java.util.List;

public class MemberLabelItem {

	
	private List<String> parents;
	
	private String name;
	
	private String caption;
	
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
}
