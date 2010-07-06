package org.pentaho.pat.client.ui.widgets;

import java.util.List;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.ISelectionListener;
import org.pentaho.pat.client.util.factory.EventFactory;
import org.pentaho.pat.rpc.dto.StringTree;
import org.pentaho.pat.rpc.dto.query.IAxis;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class QueryDesignTable extends LayoutComposite implements ISelectionListener {

    FlexTable flex = new FlexTable();

    private Object queryID;

    private boolean isfilter;

    public QueryDesignTable(String queryID, boolean isfilter) {
        this.isfilter = isfilter;
        if (isfilter) {
            Label filterLabel = new Label(Pat.CONSTANTS.filter());
            this.getLayoutPanel().setLayout(new BoxLayout());
            this.getLayoutPanel().add(filterLabel);
        }
        ScrollLayoutPanel slp = new ScrollLayoutPanel();
        this.queryID = queryID;
        slp.add(flex);
        flex.setStylePrimaryName("pat-QueryDesignTable");
        this.setSize("100%", "100%");

        this.getLayoutPanel().add(slp);
    }

    public void alterSelectionDisplay(MeasureLabel ml, int[] coords, IAxis axis) {

        if ((axis.equals(IAxis.ROWS) && !isfilter) || (isfilter && axis.equals(IAxis.FILTER))) {

            for (int i = 0; i < flex.getRowCount(); i++) {
                if (flex.isCellPresent(i, coords[1])) {
                    flex.removeCell(i, coords[1]);
                }
            }
        } else if (!isfilter && axis.equals(IAxis.COLUMNS)) {
            flex.removeRow(coords[0]);
        }
    }

    public void alterSelectionDisplay(MeasureLabel targetLabel, StringTree labels) {

        if ((targetLabel.getAxis().equals(IAxis.ROWS) && !isfilter)
                || (isfilter && targetLabel.getAxis().equals(IAxis.FILTER))) {

            DimensionSimplePanel dimPanel = ((DimensionSimplePanel) targetLabel.getParent());

            int[] parentcoords = dimPanel.getCoord();
            final List<StringTree> child = labels.getChildren();
            removeRowsFromFlexTable(flex, parentcoords);
            if (!flex.isCellPresent(parentcoords[0] + 1, 0)) {
                flex.insertRow(parentcoords[0] + 1);
            }

            if (!(labels.getCaption() == null)) {
                final Label parentLabel = new Label(labels.getCaption());

                flex.setWidget(parentcoords[0] + 1, parentcoords[1], parentLabel);

                for (int i = 0; i < 5; i++) {
                    if (i < child.size()) {
                        final Label memberLabel = new Label(child.get(i).getCaption());
                        if (!flex.isCellPresent(parentcoords[1] + i + 2, 0)) {
                            flex.insertRow(parentcoords[0] + i + 2);
                        }

                        flex.setWidget(parentcoords[0] + i + 2, parentcoords[1], memberLabel);
                    }

                }
            } else {
                for (int i = 0; i < 5; i++) {
                    if (i < child.size()) {
                        final Label memberLabel = new Label(child.get(i).getCaption());
                        if (!flex.isCellPresent(parentcoords[1] + i + 1, 0)) {
                            flex.insertRow(parentcoords[0] + i + 1);
                        }

                        flex.setWidget(parentcoords[0] + i + 1, parentcoords[1], memberLabel);
                    }

                }

            }

        } else if (!isfilter && targetLabel.getAxis().equals(IAxis.COLUMNS)) {

            DimensionSimplePanel dimPanel = ((DimensionSimplePanel) targetLabel.getParent());

            int[] parentcoords = dimPanel.getCoord();
            final List<StringTree> child = labels.getChildren();
            removeColsFromFlexTable(flex, parentcoords);
            // s flex.insertRow(parentcoords[0] + 1);

            if (!(labels.getCaption() == null)) {
                final Label parentLabel = new Label(labels.getCaption());

                flex.setWidget(parentcoords[0], parentcoords[1] + 1, parentLabel);

                for (int i = 0; i < 6; i++) {
                    if (i < child.size()) {
                        final Label memberLabel = new Label(child.get(i).getCaption());

                        flex.setWidget(parentcoords[0], parentcoords[1] + i + 2, memberLabel);
                    }

                }
            } else {
                for (int i = 0; i < 5; i++) {
                    if (i < child.size()) {
                        final Label memberLabel = new Label(child.get(i).getCaption());

                        flex.setWidget(parentcoords[0], parentcoords[1] + i + 1, memberLabel);
                    }

                }

            }
        }
    }

    public Widget getWidget(int i, int j) {
        return this.flex.getWidget(i, j);
    }

    @Override
    public void onLoad() {
        EventFactory.getSelectionInstance().addSelectionListener(QueryDesignTable.this);
    }

    public void onSelectionChange(String queryID, Widget sender, StringTree tree, String type) {
        if (this.queryID.equals(queryID) && this.isAttached()) {
            MeasureLabel ml = (MeasureLabel) sender;
            alterSelectionDisplay(ml, tree);
        }
    }

    public void onSelectionCleared(String currQuery, MeasureLabel label, int[] is, IAxis axis) {
        if (this.queryID.equals(queryID) && this.isAttached()) {
            alterSelectionDisplay(label, is, axis);
        }

    }

    @Override
    public void onUnload() {
        EventFactory.getSelectionInstance().removeSelectionListener(QueryDesignTable.this);
    }

    private void removeRowsFromFlexTable(FlexTable flexTable, int[] coords) {

        for (int z = 0; z < flexTable.getRowCount(); z++) {
            if (flexTable.isCellPresent(z, coords[1]) == true && flexTable.getWidget(z, coords[1]) instanceof Label) {

                boolean flag = false;

                Widget w = flexTable.getWidget(z, coords[1]);
                if (w != null) {
                    w.removeFromParent();
                }
                for (int i = 0; i < 100; i++) {
                    if (flexTable.isCellPresent(coords[0] + 1, i) && flexTable.getWidget(coords[0] + 1, i) != null) {
                        flag = true;
                        break;
                    }
                }
                if (flag == false) {
                    flexTable.removeRow(coords[0] + 1);
                }

            }
        }
    }

    private void removeColsFromFlexTable(FlexTable flex2, int[] coords) {

        while (flex2.isCellPresent(coords[0], coords[1] + 1)
                && flex2.getWidget(coords[0], coords[1] + 1) instanceof Label) {
            flex.removeCell(coords[0], coords[1] + 1);
        }

    }

    public void setWidget(int i, int j, Widget w) {
        this.flex.setWidget(i, j, w);

    }

    public void onMoveCol(String currQuery, int oldcol, int newcol) {
        if (this.queryID.equals(queryID) && this.isAttached() && !this.isfilter) {
            if(newcol<oldcol){
            for(int i = 0; i<flex.getRowCount(); i++){
                flex.insertCell(i, newcol);
                if(flex.isCellPresent(i, oldcol+1)){
                    Widget w = flex.getWidget(i, oldcol+1);
                    flex.setWidget(i, newcol, w);
                    flex.removeCell(i, oldcol+1);
                }
            }
            }
            else{
                for(int i = 0; i<flex.getRowCount(); i++){
                    flex.insertCell(i, newcol+1);
                    if(flex.isCellPresent(i, oldcol)){
                        Widget w = flex.getWidget(i, oldcol);
                        flex.setWidget(i, newcol+1, w);
                        flex.removeCell(i, oldcol);
                    }
                } 
            }
        }
        
    }

    public void onMoveRow(String currQuery, int oldrow, int newrow) {
        
        if (this.queryID.equals(queryID)  && !this.isfilter && this.isAttached()) {
            
            if(newrow<oldrow){
                flex.insertRow(newrow);
            for(int i = 0; i<flex.getCellCount(oldrow+1); i++){
                if(flex.isCellPresent(oldrow+1, i)){
                Widget w = flex.getWidget(oldrow+1, i);
                if(w!=null)
                flex.setWidget(newrow, i, w);
                }
            }
            flex.removeRow(oldrow+1);
        }
            else{
                flex.insertRow(newrow+1);
                for(int i = 0; i<flex.getCellCount(oldrow); i++){
                    if(flex.isCellPresent(oldrow, i)){
                    Widget w = flex.getWidget(oldrow, i);
                    if(w!=null)
                    flex.setWidget(newrow+1, i, w);
                    }
                }
                flex.removeRow(oldrow);
            }
        }
        
    }

}
