/**
 * TODO JAVADOC	
 */
package org.pentaho.pat.client.ui.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.LiveTable;
import org.gwt.mosaic.ui.client.table.DefaultColumnDefinition;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.QueryListener;
import org.pentaho.pat.client.util.PatTableModel;
import org.pentaho.pat.rpc.dto.Matrix;
import org.pentaho.pat.rpc.dto.celltypes.BaseCell;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.gen2.table.client.DefaultTableDefinition;
import com.google.gwt.gen2.table.client.IterableTableModel;
import com.google.gwt.gen2.table.client.TableDefinition;
import com.google.gwt.gen2.table.client.TableModel;
import com.google.gwt.gen2.table.client.TableModelHelper.Request;
import com.google.gwt.gen2.table.client.TableModelHelper.SerializableResponse;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class OlapTable extends LayoutComposite implements QueryListener {
	private Matrix olapData;
	private int offset;
	PatTableModel patTableModel;
	
	public OlapTable(){
		super();
	}
	
	public void initTable(){
		patTableModel = new PatTableModel(olapData);
		final List data = Arrays.asList(patTableModel.getRowData());
		offset = patTableModel.getOffset();
	    TableModel tableModel = new IterableTableModel(data) {
	        @Override
	        public int getRowCount() {
	          return data.size();
	        }


	    @Override
	      public void requestRows(Request request, Callback callback) {
		 int numRows;
	        if(olapData.getMatrixHeight()<50)
	            numRows = olapData.getMatrixHeight();
	        else 
	            numRows = request.getNumRows();
	        
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
	    table.setSize("100%", "100%");
	    this.getLayoutPanel().setSize("100%", "100%");
	    this.getLayoutPanel().add(table);
	    this.getLayoutPanel().layout();
	    this.layout();
	}

	/**
	 *TODO JAVADOC
	 *
	 * @return
	 */
	private TableDefinition<BaseCell[]> createTableDefinition() {
	    DefaultTableDefinition<BaseCell[]> tableDef = new DefaultTableDefinition<BaseCell[]>();

	    List colData = Arrays.asList(patTableModel.getColumnHeaders());
	    for (int i=0; i < olapData.getMatrixWidth(); i++){
		BaseCell[] headers = (BaseCell[]) colData.get(offset-1);
		
		final int cell = i;
		
		DefaultColumnDefinition<BaseCell[], String> colDef0 = new DefaultColumnDefinition<BaseCell[], String>(
		        headers[i].formattedValue) {
			@Override
		      public String getCellValue(BaseCell[] rowValue) {
		        return rowValue[cell].formattedValue;
		      }
		    };
		    colDef0.setColumnSortable(false);
		    colDef0.setColumnTruncatable(false);
		    tableDef.addColumnDefinition(colDef0);
	    }
	    return tableDef;
	  }


	
	public void setData(final Matrix olapData) {
		this.olapData = olapData;
		    initTable();
		
	}
	
	public void onQueryChange(Widget sender) {
		// TODO Auto-generated method stub
		
	}

	public void onQueryExecuted(String queryId, Matrix olapData) {
		
		if (Pat.getInitialState().getMode().isShowOnlyTable()) {
			setData(olapData);
		}
	}


	
}
