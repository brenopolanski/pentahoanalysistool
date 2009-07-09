package org.pentaho.pat.client.util;

import org.pentaho.pat.rpc.dto.celltypes.DataCell;

/**
 * The Class CellSpanInfo.
 */
public class CellSpanInfo {
	
	/** The info. */
	private final DataCell info;
	
	/** The span. */
	private int span = 1;

	/**
	 * Instantiates a new cell span info.
	 * 
	 * @param info the info
	 * @param span the span
	 */
	public CellSpanInfo(final DataCell info, final int span) {
		this.info = info;
		this.span = span;
	}

	/**
	 * Gets the info.
	 * 
	 * @return the info
	 */
	public DataCell getInfo() {
		return info;
	}

	/**
	 * Gets the span.
	 * 
	 * @return the span
	 */
	public int getSpan() {
		return span;
	}

}
