package org.pentaho.pat.client.ui.widgets;

import java.util.List;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.pentaho.pat.client.listeners.ISelectionListener;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.rpc.dto.IAxis;
import org.pentaho.pat.rpc.dto.StringTree;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class QueryDesignTable extends LayoutComposite implements
		ISelectionListener {

	FlexTable flex = new FlexTable();
	private Object queryID;

	public QueryDesignTable(String queryID) {
		ScrollLayoutPanel slp = new ScrollLayoutPanel();
		this.queryID = queryID;
		slp.add(flex);
		// this.flex.setSize("100%", "100%");
		this.flex.setBorderWidth(10);
		DOM.setStyleAttribute(flex.getElement(), "background", "red");
		this.setSize("100%", "100%");
		this.getLayoutPanel().add(slp);
	}

	@Override
	public void onLoad() {
		GlobalConnectionFactory.getSelectionInstance().addSelectionListener(
				QueryDesignTable.this);
	}

	@Override
	public void onUnload() {
		GlobalConnectionFactory.getSelectionInstance().removeSelectionListener(
				QueryDesignTable.this);
	}

	public void alterSelectionDisplay(MeasureLabel ml, int[] coords, IAxis axis) {
		int row = 0;
		if (axis.equals(IAxis.ROWS)) {
			while (flex.isCellPresent(row, coords[1]) == true) {

				flex.removeCell(row, coords[1]);
				row++;
			}
		}
	}

	public void alterSelectionDisplay(MeasureLabel targetLabel,
			StringTree labels) {

		if (targetLabel.getAxis().equals(IAxis.ROWS)) {

			DimensionSimplePanel dimPanel = ((DimensionSimplePanel) targetLabel
					.getParent());

			int[] parentcoords = dimPanel.getCoord();
			final List<StringTree> child = labels.getChildren();
			removeRowsFromFlexTable(flex, parentcoords);
			if (!flex.isCellPresent(parentcoords[0] + 1, 0)) {
				flex.insertRow(parentcoords[0] + 1);
			}
			
			if(!(labels.getCaption()==null)){
			final Label parentLabel = new Label(labels.getCaption());
			
			flex.setWidget(parentcoords[0] + 1, parentcoords[1], parentLabel);
			
			for (int i = 2; i < 6; i++) {
				if (i < child.size()) {
					final Label memberLabel = new Label(child.get(i)
							.getCaption());
					if (!flex.isCellPresent(parentcoords[1] + i, 0)) {
						flex.insertRow(parentcoords[0] + i);
					}

					flex.setWidget(parentcoords[0] + i, parentcoords[1],
							memberLabel);
				}

			}
			}
			else{
				for (int i = 1; i < 5; i++) {
					if (i < child.size()) {
						final Label memberLabel = new Label(child.get(i)
								.getCaption());
						if (!flex.isCellPresent(parentcoords[1] + i, 0)) {
							flex.insertRow(parentcoords[0] + i);
						}

						flex.setWidget(parentcoords[0] + i, parentcoords[1],
								memberLabel);
					}

				}
				
			}

		} else if (targetLabel.getAxis().equals(IAxis.COLUMNS)) {

			DimensionSimplePanel dimPanel = ((DimensionSimplePanel) targetLabel
					.getParent());

			int[] parentcoords = dimPanel.getCoord();
			final List<StringTree> child = labels.getChildren();
			removeRowsFromFlexTable(flex, parentcoords);
			flex.insertRow(parentcoords[0] + 1);
			
			if(!(labels.getCaption()==null)){
			final Label parentLabel = new Label(labels.getCaption());
			
			
			flex.setWidget(parentcoords[0], parentcoords[1] + 1, parentLabel);
			
			for (int i = 2; i < 6; i++) {
				if (i < child.size()) {
					final Label memberLabel = new Label(child.get(i)
							.getCaption());

					flex.setWidget(parentcoords[0], parentcoords[1] + i,
							memberLabel);
				}
				
			}
			}else{
				for (int i = 1; i < 5; i++) {
					if (i < child.size()) {
						final Label memberLabel = new Label(child.get(i)
								.getCaption());

						flex.setWidget(parentcoords[0], parentcoords[1] + i,
								memberLabel);
					}
					
				}

			}
		}
	}

	private void removeRowsFromFlexTable(FlexTable flexTable, int[] coords) {
		
		for (int z = 0; z<flexTable.getRowCount(); z++)
			{
			if(flexTable.isCellPresent(z, coords[1]) == true		
				&& flexTable.getWidget(z, coords[1]) instanceof Label) {
			
			boolean flag =false;

			Widget w = flexTable.getWidget(z, coords[1]);
			if(w!=null){
			w.removeFromParent();
			}
			for(int i = 0; i<100; i++){
				if(flexTable.isCellPresent(coords[0]+1, i) && flexTable.getWidget(coords[0]+1, i)!=null){
					flag = true;
					break;
				}
			}
			if(flag == false){
				flexTable.removeRow(coords[0]+1);
			}

		}
			}
	}

	public void setWidget(int i, int j, Widget w) {
		this.flex.setWidget(i, j, w);

	}

	public Widget getWidget(int i, int j) {
		return this.flex.getWidget(i, j);
	}

	public void onSelectionChange(String queryID, Widget sender,
			StringTree tree, String type) {
		if (this.queryID.equals(queryID)) {
			MeasureLabel ml = (MeasureLabel) sender;
			alterSelectionDisplay(ml, tree);
		}
	}

	public void onSelectionCleared(String currQuery, MeasureLabel label,
			int[] is, IAxis axis) {
		alterSelectionDisplay(label, is, axis);

	}

}
