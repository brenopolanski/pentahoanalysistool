/*
 * Copyright (C) 2009 Paul Stoellberger
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *
 */
package org.pentaho.pat.server.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.log4j.Logger;
import org.pentaho.pat.client.util.table.PatTableModel;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.celltypes.AbstractBaseCell;
import org.pentaho.pat.server.beans.QueryExportBean;
import org.pentaho.pat.server.util.OlapUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractCommandController;

/**
 *  Controller for exporting a Query
 * 
 * @created Nov 29, 2009
 * @since 0.5.1
 * @author Paul Stoellberger
 */
public class ExportController extends AbstractCommandController  implements InitializingBean {


    private static final Logger LOG = Logger.getLogger(ExportController.class);
    
    public static CellDataSet exportResult = null;
    
    private WritableCellFormat cs;
    private WritableCellFormat hcs;
    private WritableCellFormat rcs;
    private WritableCellFormat csn;

    private ServletOutputStream outputStream;


    public static final String extensionFile = ".xls";

    
    protected ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
            final Object command, final BindException errors) throws Exception {
        final QueryExportBean queryExportBean = (QueryExportBean) command;
        
        try {
            exportResult = OlapUtil.cellSet2Matrix(OlapUtil.getCellSet(queryExportBean.getQuery()));
            if (exportResult != null) {
                response.setContentType("application/vnd.ms-excel"); //$NON-NLS-1$
                response.setHeader("filename", "export.xls");
                this.outputStream = response.getOutputStream();
                PatTableModel table = new PatTableModel(exportResult);
                AbstractBaseCell[][] rowData = table.getRowData();
                AbstractBaseCell[][] rowHeader = table.getColumnHeaders();
                
                String[][] result = new String[rowHeader.length + rowData.length][];
                for (int x = 0; x<rowHeader.length;x++) {
                    List<String> cols = new ArrayList<String>();
                    for(int y = 0; y < rowHeader[x].length;y++) {
                        cols.add(rowHeader[x][y].getFormattedValue()); 
                    }
                    result[x]= cols.toArray(new String[cols.size()]);
                    
                }
                for (int x = 0; x<rowData.length ;x++) {
                    int xTarget = rowHeader.length + x;
                    List<String> cols = new ArrayList<String>();
                    for(int y = 0; y < rowData[x].length;y++) {
                        cols.add(rowData[x][y].getFormattedValue()); 
                    }
                    result[xTarget]= cols.toArray(new String[cols.size()]);
                    
                }
               
                export(result);
                                          
                response.setStatus(HttpServletResponse.SC_OK);
                outputStream.flush();
                outputStream.close();

            }
            else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return null;
    }

    public void export(String[][] resultSet) {
        
        
        WritableWorkbook  wb = null;
        
        try {
 
                wb = Workbook.createWorkbook(outputStream);
                WritableSheet sheet = wb.createSheet("Sheet", 0);
                setCellsStyles();
                WritableCellFormat cf;
                
                if(resultSet.length > 0){
                        
                        boolean swapRows  = resultSet[0].length > 256 ? true : false;
                        
                for(int i =  0; i < resultSet.length; i++){
                        String[] vs = resultSet[i];
                        for(int j = 0; j < vs.length ; j++){
                                cf = i == 0 ? hcs : j != 0 ? cs : (i % 2 != 0 ? hcs : rcs);
                                String value = vs[j];
                                if(value == null || value == "null") 
                                    value="";
                                    
                                if(isDouble(value)){
                                        Number number = new Number(swapRows ? i : j,swapRows ? j : i,Double.parseDouble(value),csn);
                                        sheet.addCell(number);
                                }
                                else{
                                        Label label = new Label(swapRows ? i : j,swapRows ? j : i,value,cf);
                                sheet.addCell(label); 
                                }
                        }
                }
                }
        
        
        wb.write();
        } catch (IOException e) {
                LOG.error( "IO ERROR" );
        } catch (WriteException e) {
                LOG.error( "WRITE ERROR");
        } catch (Exception e) {
                LOG.error( ("Error writing Excel Export") );
        }
        finally{
            try {
                wb.close();
                } catch (Exception e){
                        logger.error( "IO ERROR");
                }
        }
}

private void setCellsStyles() throws WriteException {
        
        cs = new WritableCellFormat();
        cs.setBorder(Border.ALL, BorderLineStyle.THIN);
        //cs.setShrinkToFit(true);
        csn = new WritableCellFormat(new NumberFormat("###,###,###.###"));
        csn.setBorder(Border.ALL, BorderLineStyle.THIN);
        //csn.setShrinkToFit(true);
        
        hcs = new WritableCellFormat();
        hcs.setBorder(Border.ALL, BorderLineStyle.THIN);
        hcs.setBackground(Colour.GRAY_50);
        //hcs.setShrinkToFit(true);
        
        rcs = new WritableCellFormat();
        rcs.setBorder(Border.ALL, BorderLineStyle.THIN);
        rcs.setBackground(Colour.GRAY_25);
        //rcs.setShrinkToFit(true);

}

public String getExtension() {
        return extensionFile;
}

public boolean isDouble(String obj){
    try{
            Double.parseDouble(obj);
    }catch(NumberFormatException e){
            return false;
    }
    return true;
}

public void afterPropertiesSet() throws Exception {
    // TODO Auto-generated method stub
    
}



}