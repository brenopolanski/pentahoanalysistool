package org.pentaho.pat.rpc.dto.celltypes;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class BaseCell implements Serializable, IsSerializable {

	private static final long serialVersionUID = 1L;

	/** The formatted value. */
	public String formattedValue;

	/** The raw value. */
	String rawValue;

	public boolean right = false;
	public boolean sameAsPrev = false;

	/**
	 * 
	 * Blank Constructor for serialization dont use.
	 *
	 */
	public BaseCell() {}

	/**
	 * 
	 * BaseCell Constructor, every cell type should inherit basecell.
	 *
	 * @param right
	 * @param sameAsPrev
	 */
	public BaseCell(final boolean right, final boolean sameAsPrev) {
		this.right = right;
		this.sameAsPrev = sameAsPrev;
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
	 * @param formattedValue
	 *            the new formatted value
	 */
	public void setFormattedValue(final String formattedValue) {
		this.formattedValue = formattedValue;
	}

	/**
	 * Sets the raw value.
	 * 
	 * @param rawValue
	 *            the new raw value
	 */
	public void setRawValue(final String rawValue) {
		this.rawValue = rawValue;
	}

	/**
	 * 
	 *TODO JAVADOC
	 *
	 * @param set
	 */
	public void setRight(final boolean set) {
		this.right = set;
	}

	/**
	 * 
	 * Set true if value is same as the previous one in the row.
	 *
	 * @param same
	 */
	public void setSameAsPrev(final boolean same) {
		this.sameAsPrev = same;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return formattedValue;
	}
}
