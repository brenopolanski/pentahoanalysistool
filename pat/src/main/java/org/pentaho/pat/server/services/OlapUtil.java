package org.pentaho.pat.server.services;

import org.pentaho.pat.client.util.StringTree;



public class OlapUtil {
	public static StringTree parseMembers(String[] uniqueMemberNames, StringTree parentNode) {
	    StringTree currentNode = parentNode;
	    for (int i=1; i<uniqueMemberNames.length; i++) {
	      currentNode = OlapUtil.findOrCreateNode(currentNode, uniqueMemberNames[i]);
	    }
	    return parentNode;
	  }
	  public static StringTree findOrCreateNode(StringTree parent, String srchString) {
		    StringTree found = null;
		    for (int i=0; i<parent.getChildren().size() && found == null; i++) {
		      StringTree targetNode = (StringTree)parent.getChildren().get(i);
		      if (targetNode.getValue().equals(srchString)) {
		        found = targetNode;
		      }
		    }
		    if (found == null) {  // couldn't find it in the children so we'll create it
		      found = new StringTree(srchString, parent);
		    }
		    return found;
		  }

}
