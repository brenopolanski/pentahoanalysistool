package org.pentaho.pat.client.ui.widgets;




import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.celltypes.DataCell;

import com.google.gwt.gen2.table.client.InlineCellEditor;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;

/**
 * An {@link InlineCellEditor} that can be used to edit {@link String Strings}.
 */
public class DataCellEditor extends InlineCellEditor<DataCellPanel> {
  /**
   * The text field used in this editor.
   */
  private TextBoxBase textBox;

  private DataCellPanel dataCell;
  /**
   * Construct a new {@link TextCellEditor} using a normal {@link TextBox}.
   */
  public DataCellEditor() {
    this(new TextBox());
  }

  /**
   * Construct a new {@link TextCellEditor} using the specified {@link TextBox}.
   *
   * @param textBox the text box to use
   */
  public DataCellEditor(TextBoxBase textBox) {
    super(textBox);
    this.textBox = textBox;
  }

  /**
   * Construct a new {@link TextCellEditor} using the specified {@link TextBox}
   * and images.
   *
   * @param textBox the text box to use
   * @param images the images to use for the accept/cancel buttons
   */
  public DataCellEditor(TextBoxBase textBox, InlineCellEditorImages images) {
    super(textBox, images);
    this.textBox = textBox;
  }

  @Override
  public void editCell(CellEditInfo cellEditInfo, DataCellPanel cellValue,
      Callback<DataCellPanel> callback) {
    super.editCell(cellEditInfo, cellValue, callback);
    dataCell = cellValue;
    textBox.setFocus(true);
  }

  /**
   * @return the text box used in the editor
   */
  protected TextBoxBase getTextBox() {
    return textBox;
  }

  @Override
  protected DataCellPanel getValue() {
      Number cellValue = Double.parseDouble(textBox.getText());
      dataCell.setCellNum(cellValue);
      ServiceFactory.getQueryInstance().alterCell(Pat.getCurrQuery(), Pat.getSessionID(), Pat.getCurrScenario(), Pat.getCurrConnection(), "123", new AsyncCallback<CellDataSet>(){

		public void onFailure(Throwable arg0) {
		    // TODO Auto-generated method stub
		    
		}

		public void onSuccess(CellDataSet arg0) {
		    // TODO Auto-generated method stub
		    
		}
		
	    });
      return dataCell;
  }

  @Override
  protected void setValue(DataCellPanel cell) {
     Number cellValue = cell.getCellNum();
    if (cellValue == null) {
      cellValue = 0.00;
    }
    
  
    textBox.setText(cellValue.toString());
  }
}

