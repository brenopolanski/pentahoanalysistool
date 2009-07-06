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
public class ColumnLabel extends CellLabel {
	
	Integer columnSpan;
	/**
	 *TODO JAVADOC
	 *
	 * @param formattedValue
	 * @param acrossCount
	 */
	public ColumnLabel(String formattedValue, int columnSpan) {
		this.setCellLabel(formattedValue);
		this.setColumnSpan(columnSpan);
	}

	/**
	 *TODO JAVADOC
	 * @return the columnSpan
	 */
	public final Integer getColumnSpan() {
		return columnSpan;
	}
	/**
	 *
	 *TODO JAVADOC
	 * @param columnSpan the columnSpan to set
	 */
	public final void setColumnSpan(Integer columnSpan) {
		this.columnSpan = columnSpan;
	}

}
