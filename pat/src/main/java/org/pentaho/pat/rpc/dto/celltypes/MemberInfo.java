package org.pentaho.pat.rpc.dto.celltypes;


import java.io.Serializable;


import com.google.gwt.user.client.rpc.IsSerializable;

public class MemberInfo extends BaseCell implements Serializable, IsSerializable{
    private static final long serialVersionUID = 1L;
 
    private boolean lastRow = false;

	private int offset; 
    public MemberInfo(){
	
    }
    
    public MemberInfo(boolean b, boolean c) {
	    this.right=b;
	    this.sameAsPrev=c;
	}

	/**
	 *TODO JAVADOC
	 * @return the lastRow
	 */
	public boolean isLastRow() {
		return lastRow;
	}

	/**
	 *
	 *TODO JAVADOC
	 * @param lastRow the lastRow to set
	 */
	public void setLastRow(boolean lastRow) {
		this.lastRow = lastRow;
	}
}
