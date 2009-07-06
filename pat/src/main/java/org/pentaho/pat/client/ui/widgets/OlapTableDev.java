/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.ui.widgets;

import java.util.ArrayList;
import java.util.List;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.LiveTable;
import org.pentaho.pat.client.util.PatTableModel;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.gen2.table.client.IterableTableModel;
import com.google.gwt.gen2.table.client.TableDefinition;
import com.google.gwt.gen2.table.client.TableModel;
import com.google.gwt.gen2.table.client.TableModelHelper.Request;
import com.google.gwt.gen2.table.client.TableModelHelper.SerializableResponse;
import com.google.gwt.user.client.Window;

/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class OlapTableDev extends LayoutComposite {

	public OlapTableDev(){
		super();
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();

/*		scrollTable = new ScrollTable2(dataTable, headerTable);
		layoutPanel.add(scrollTable);
*/
	}


	public void initTable(){
		PatTableModel patTableModel = new PatTableModel();
		final List data = patTableModel.getRowData();
		
	    TableModel tableModel = new IterableTableModel(data) {
	        @Override
	        public int getRowCount() {
	          return data.size();
	        }
	    

	    @Override
	      public void requestRows(Request request, Callback callback) {
	        int numRows = request.getNumRows();
	        List list = new ArrayList();
	        for (int i = 0, n = numRows; i < n; i++) {
	          list.add(data.get(request.getStartRow() + i));
	        }
	        SerializableResponse response = new SerializableResponse(list);
	        callback.onRowsReady(request, response);
	      }
	    };

	    final LiveTable table = new LiveTable(tableModel,
	        createTableDefinition());
	    // table.setContextMenu(createContextMenu());
	    table.addDoubleClickHandler(new DoubleClickHandler() {
	      public void onDoubleClick(DoubleClickEvent event) {
	        Window.alert(event.getSource().getClass().getName());
	      }
	    });

	}

	/**
	 *TODO JAVADOC
	 *
	 * @return
	 */
	private TableDefinition createTableDefinition() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
