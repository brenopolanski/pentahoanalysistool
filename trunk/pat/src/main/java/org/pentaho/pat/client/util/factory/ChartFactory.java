/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.util.factory;

import java.util.Arrays;
import java.util.List;

import org.pentaho.pat.client.ui.panels.ChartPanel.ChartType;
import org.pentaho.pat.client.util.PatTableModel;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.celltypes.BaseCell;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.rednels.ofcgwt.client.model.ChartData;
import com.rednels.ofcgwt.client.model.Legend;
import com.rednels.ofcgwt.client.model.Legend.Position;
import com.rednels.ofcgwt.client.model.axis.Label;
import com.rednels.ofcgwt.client.model.axis.XAxis;
import com.rednels.ofcgwt.client.model.axis.YAxis;
import com.rednels.ofcgwt.client.model.elements.BarChart;
import com.rednels.ofcgwt.client.model.elements.PieChart;
import com.rednels.ofcgwt.client.model.elements.BarChart.BarStyle;

/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class ChartFactory {

    
private ChartData getPieChartData(CellDataSet matrix, String chartTitle) {
        
        matrix.getCellSetHeaders();
        PatTableModel patTableModel = new PatTableModel(matrix);
        BaseCell[][] rowData = patTableModel.getRowData();
        int rowColCount =0; 
        for (int i = 0; i< patTableModel.getColumnCount(); i++){
            if (rowData[0][i] instanceof MemberCell)
                rowColCount++;
        }

        ChartData cd = new ChartData(chartTitle, "font-size: 14px; font-family: Verdana; text-align: center;");
        cd.setBackgroundColour("#ffffff");
        cd.setLegend(new Legend(Position.RIGHT, true));

        PieChart pie = new PieChart();        
        pie.setAlpha(0.5f);
        pie.setRadius(130);
        pie.setNoLabels(true);
        pie.setTooltip("#label# $#val#<br>#percent#");
        pie.setGradientFill(true);
        pie.setColours("#ff0000", "#00aa00", "#0000ff", "#ff9900", "#ff00ff");
        pie.setAnimateOnShow(true);
        pie.setAnimation(new PieChart.PieBounceAnimation(30));

        final List<BaseCell[]> data = Arrays.asList(patTableModel.getRowData());
        for (int i=0; i<data.size(); i++){
            BaseCell[] cell = data.get(i);
            int rc = 0;
            while(cell[rc].getRawValue()==null){
                rc++;
            }
            if (isParsableToInt(cell[rowColCount].getRawValue()));
            pie.addSlices(new PieChart.Slice(Integer.parseInt(cell[rowColCount].getRawValue()), cell[rc].getRawValue()));
        }
        cd.addElements(pie);
        return cd;
}

private ChartData getBarChartGlassData(CellDataSet matrix, String chartTitle) {
    
    matrix.getCellSetHeaders();
    PatTableModel patTableModel = new PatTableModel(matrix);
    BaseCell[][] rowData = patTableModel.getRowData();
    int rowColCount =0; 
    for (int i = 0; i< patTableModel.getColumnCount(); i++){
        if (rowData[0][i] instanceof MemberCell)
            rowColCount++;
    }
    
    ChartData cd2 = new ChartData(chartTitle, "font-size: 14px; font-family: Verdana; text-align: center;");
    cd2.setBackgroundColour("#ffffff");
    XAxis xa = new XAxis();
    
    cd2.setXAxis(xa);
    YAxis ya = new YAxis();
    ya.setSteps(16);
    
    cd2.setYAxis(ya);
    BarChart bchart2 = new BarChart(BarStyle.GLASS);
    bchart2.setColour("#00aa00");
    bchart2.setTooltip("$#val#");

    final List<BaseCell[]> data = Arrays.asList(patTableModel.getRowData());
    Label[] labels = new Label[data.size()];
    for (int i=0; i<data.size(); i++){
        BaseCell[] cell = data.get(i);
        int rc = 0;
        while(cell[rc].getRawValue()==null){
            rc++;
        }
        labels[i]= new Label(cell[rc].getRawValue(), 45);
        
    }
    
    xa.addLabels(labels);
    
    int maxval=0;
    for (int i=0; i<data.size(); i++){
        BaseCell[] cell = data.get(i);
        int rc = 0;
        while(cell[rc].getRawValue()==null){
            rc++;
        }
        if (isParsableToInt(cell[rowColCount].getRawValue()));{
        bchart2.addValues(Integer.parseInt(cell[rowColCount].getRawValue()));
        if (Integer.parseInt(cell[rowColCount].getRawValue())>maxval)
            maxval=Integer.parseInt(cell[rowColCount].getRawValue());
        }
        
    }
    ya.setMax(maxval);
    cd2.addElements(bchart2);
    return cd2;
}


    public boolean isParsableToInt(String i)
    {
    try
    {
    Integer.parseInt(i);
    return true;
    }
    catch(NumberFormatException nfe)
    {
    return false;
    }
    }



    /**
     *TODO JAVADOC
     *
     * @param pie
     * @param matrix
     * @param string
     * @return
     */
    public ChartData getChart(ChartType pie, CellDataSet matrix, String string) {
        if(pie.equals(ChartType.PIE))
            return getPieChartData(matrix, string);
        else if(pie.equals(ChartType.BAR))
                return getBarChartGlassData(matrix, string);
        else
        return null;
    }
    
    private void createImageDialog(String imgurl) {         
        final DialogBox imageDb = new DialogBox();
        imageDb.setText("Image Capture of Chart");

        VerticalPanel dbContents = new VerticalPanel();
        dbContents.setSpacing(4);
        imageDb.setWidget(dbContents);

        Image chartImg = new Image(imgurl);
        
        chartImg.setSize("250", "200");
        dbContents.add(chartImg);
        
        Button closeButton = new Button("Close", new ClickHandler() {
                public void onClick(ClickEvent event) {
                        imageDb.hide();
                }
        });
        dbContents.add(closeButton);
        dbContents.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);

        imageDb.center();
        imageDb.show();
}


}
