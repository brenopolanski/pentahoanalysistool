/*
 * Copyright (C) 2009 Tom Barber
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *
 */
package org.pentaho.pat.rpc.dto.celltypes;

import java.io.Serializable;
import com.google.gwt.user.client.rpc.IsSerializable;

public class MemberCell extends BaseCell implements Serializable, IsSerializable {
	private static final long serialVersionUID = 1L;

	private boolean lastRow = false;

	private boolean expanded;
	
	private String parentDimension = null;
	
	private String parentMember = null;
	
	private String rightOf = null;

    private String uniqueName;

    private int childMemberCount;
	/**
	 * 
	 * Blank Constructor for Serializable niceness, don't use it.
	 * 
	 */
	public MemberCell() {
	}

	/**
	 * 
	 * Creates a member cell.
	 * 
	 * @param b
	 * @param c
	 */
	public MemberCell(final boolean b, final boolean c) {
		this.right = b;
		this.sameAsPrev = c;
	}

	/**
	 * Returns true if this is the bottom row of the column headers(supposedly).
	 * 
	 * @return the lastRow
	 */
	public boolean isLastRow() {
		return lastRow;
	}

	/**
	 * 
	 * Set true if this is the bottom row of the column headers.
	 * 
	 * @param lastRow
	 *            the lastRow to set
	 */
	public void setLastRow(final boolean lastRow) {
		this.lastRow = lastRow;
	}
	
	public void setParentDimension(String parDim){
	    parentDimension = parDim;
	}
	
	public String getParentDimension(){
	    return parentDimension;
	}

    /**
     *TODO JAVADOC
     * @return the expanded
     */
    public boolean isExpanded() {
        return expanded;
    }

    /**
     *
     *TODO JAVADOC
     * @param expanded the expanded to set
     */
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    /**
     *TODO JAVADOC
     *
     * @param parentMember
     */
    public void setParentMember(String parentMember) {
        
        this.parentMember = parentMember;
        
    }
    
    public String getParentMember(){
        return parentMember;
    }

    /**
     *TODO JAVADOC
     *
     * @param uniqueName
     */
    public void setUniquename(String uniqueName) {
        
        this.uniqueName = uniqueName;
        
    }
    
    public String getUniqueName(){
        return uniqueName;
    }

    /**
     *TODO JAVADOC
     *
     * @param childMemberCount
     */
    public void setChildMemberCount(int childMemberCount) {
        this.childMemberCount=childMemberCount;
    }
    
    public int getChildMemberCount(){
        return childMemberCount;
    }

    /**
     *TODO JAVADOC
     *
     * @param uniqueName2
     */
    public void setRightOf(String uniqueName2) {
        this.rightOf= uniqueName2;
        
    }
    
    public String getRightOf(){
        return rightOf;
    }
}
