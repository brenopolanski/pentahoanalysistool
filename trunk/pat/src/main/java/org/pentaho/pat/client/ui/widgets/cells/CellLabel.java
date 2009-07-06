/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.ui.widgets.cells;



/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */

public abstract class CellLabel {
	
	String cellLabel;
	
	public CellLabel(){
		
	}
	
	/**
	 *TODO JAVADOC
	 * @return the cellLabel
	 */
	public final String getCellLabel() {
		return cellLabel;
	}

	/**
	 *
	 *TODO JAVADOC
	 * @param cellLabel the cellLabel to set
	 */
	public final void setCellLabel(String cellLabel) {
		this.cellLabel = cellLabel;
	}
}
