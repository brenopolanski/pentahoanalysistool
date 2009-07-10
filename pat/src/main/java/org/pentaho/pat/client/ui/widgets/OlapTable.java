/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.ui.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.LiveTable;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
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
 * Creates the data table, using a hashmap matrix converted to an array and
 * inserted into a GWT Mosaic Live Table, which is a lazy loading scrolltable.
 *
 * @author tom (at) wamonline.org.uk
 *
 */
public class OlapTable extends LayoutComposite implements QueryListener {
	private Matrix olapData;
	private int offset;
	PatTableModel patTableModel;
	TableModel<BaseCell[]> tableModel;

	final LayoutPanel layoutPanel = getLayoutPanel();

	public OlapTable(){
		super();
		this.setSize("100%", "100%");  //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * Create the Live Table Column Definitions.
	 *
	 * @return tableDef
	 */
	private TableDefinition<BaseCell[]> createTableDefinition() {
		final DefaultTableDefinition<BaseCell[]> tableDef = new DefaultTableDefinition<BaseCell[]>();
		final List<BaseCell[]> colData = Arrays.asList(patTableModel.getColumnHeaders());
		for (int i=0; i < olapData.getMatrixWidth(); i++){
			final BaseCell[] headers = colData.get(offset-1);

			final int cell = i;

			final DefaultColumnDefinition<BaseCell[], String> colDef0 = new DefaultColumnDefinition<BaseCell[], String>(
					headers[i].formattedValue) {
				@Override
				public String getCellValue(final BaseCell[] rowValue) {
					if (rowValue[cell]==null) {
						return "";
					} else {
						return rowValue[cell].formattedValue;
					}
				}
			};
			colDef0.setColumnSortable(false);
			colDef0.setColumnTruncatable(false);
			tableDef.addColumnDefinition(colDef0);
		}
		return tableDef;
	}

	/**
	 * 
	 * Initialize the Live Table.
	 *
	 */
	public void initTable(){
		patTableModel = new PatTableModel(olapData);
		final List<BaseCell[]> data = Arrays.asList(patTableModel.getRowData());
		offset = patTableModel.getOffset();
		tableModel = new IterableTableModel<BaseCell[]>(data) {
			@Override
			public int getRowCount() {
				return data.size();
			}


			@Override
			public void requestRows(final Request request, final Callback<BaseCell[]> callback) {
				int numRows;
				if(olapData.getMatrixHeight()<50) {
					numRows = olapData.getMatrixHeight();
				} else {
					numRows = request.getNumRows();
				}

				final List<BaseCell[]> list = new ArrayList<BaseCell[]>();
				for (int i = 0, n = numRows; i < n; i++) {
					list.add(data.get(request.getStartRow() + i));
				}
				final SerializableResponse response = new SerializableResponse(list);
				callback.onRowsReady(request, response);
			}
		};


		final LiveTable<BaseCell[]> table = new LiveTable<BaseCell[]>(tableModel,
				createTableDefinition());
		// table.setContextMenu(createContextMenu());
		table.addDoubleClickHandler(new DoubleClickHandler() {
			public void onDoubleClick(final DoubleClickEvent event) {
				Window.alert(event.getSource().getClass().getName());
			}
		});
		layoutPanel.add(table);
		layoutPanel.layout();
	}



	public void onQueryChange(final Widget sender) {
		// TODO Auto-generated method stub

	}

	public void onQueryExecuted(final String queryId, final Matrix olapData) {

		if (Pat.getInitialState().getMode().isShowOnlyTable()) {
			setData(olapData);
		}
	}

	public void setData(final Matrix olapData) {
		this.olapData = olapData;
		initTable();

	}



}
