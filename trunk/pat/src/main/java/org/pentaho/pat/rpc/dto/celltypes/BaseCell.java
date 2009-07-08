package org.pentaho.pat.rpc.dto.celltypes;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class BaseCell implements Serializable, IsSerializable{


	/**
     * 
     */
    private static final long serialVersionUID = 1L;

	/** The formatted value. */
	public String formattedValue;
	
	/** The raw value. */
	String rawValue;
	
	public boolean right = false;
        public boolean sameAsPrev = false;
        
        public BaseCell(){
	
        }
    
        public BaseCell(boolean right, boolean sameAsPrev){
    	this.right=right;
    	this.sameAsPrev=sameAsPrev;
        }
        
        public void setRight(boolean set){
            this.right=set;
        }
        
        public void setSameAsPrev(boolean same){
            this.sameAsPrev=same;
        }
	/**
	 * Gets the formatted value.
	 * 
	 * @return the formatted value
	 */
	public String getFormattedValue() {
		return formattedValue;
	}

	/**
	 * Gets the raw value.
	 * 
	 * @return the raw value
	 */
	public String getRawValue() {
		return rawValue;
	}

	/**
	 * Sets the formatted value.
	 * 
	 * @param formattedValue the new formatted value
	 */
	public void setFormattedValue(final String formattedValue) {
		this.formattedValue = formattedValue;
	}

	/**
	 * Sets the raw value.
	 * 
	 * @param rawValue the new raw value
	 */
	public void setRawValue(final String rawValue) {
		this.rawValue = rawValue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return formattedValue;
	}
}
