package org.pentaho.pat.rpc.dto.celltypes;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MemberCell extends BaseCell implements Serializable, IsSerializable {
	private static final long serialVersionUID = 1L;

	private boolean lastRow = false;

	private String parentDimension = null;
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
}
