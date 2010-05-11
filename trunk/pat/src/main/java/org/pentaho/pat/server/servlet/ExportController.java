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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Alignment;
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
import jxl.write.biff.RowsExceededException;

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
    
    public static final String extensionFile = ".xls"; //$NON-NLS-1$


    protected ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
            final Object command, final BindException errors) throws Exception {
        final QueryExportBean queryExportBean = (QueryExportBean) command;

        try {
            if(OlapUtil.getCellSet(queryExportBean.getQuery()) != null)  {

                byte[] resultExcel = exportExcel(queryExportBean.getQuery());
                if (resultExcel != null && resultExcel.length > 0) {
                    response.setContentType("application/vnd.ms-excel"); //$NON-NLS-1$
                    response.setHeader("Content-Disposition", "attachment; filename=PAT_Export.xls");
                    response.setHeader("Content-Length", ""+ resultExcel.length);
                    response.getOutputStream().write(resultExcel);
                    response.flushBuffer();

                }
                else {
                    LOG.error("Empty Excel resultset - nothing to return");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }

            }
            else {
                LOG.error("CellSet for query ID not found :" + queryExportBean.getQuery());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return null;
    }

    public static byte[] exportExcel(String queryId) throws IOException {
        exportResult = OlapUtil.cellSet2Matrix(OlapUtil.getCellSet(queryId));
        if (exportResult != null) {
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
            return export(result);
        }
        return new byte[0];
    }
    
    public static byte[] export(String[][] resultSet) {


        WritableWorkbook  wb = null;


        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            wb = Workbook.createWorkbook(bout);
            wb.setColourRGB(Colour.BLUE, 0xf0,0xf8,0xff);
            wb.setColourRGB(Colour.PALE_BLUE, 0xf9,0xf9,0xf9);
            WritableSheet sheet = wb.createSheet("Sheet", 0); //$NON-NLS-1$

            WritableCellFormat cf;

            if(resultSet.length > 0){
                boolean swapRows  = resultSet[0].length > 256 ? true : false;

                for(int i =  0; i < resultSet.length; i++){
                    String[] vs = resultSet[i];
                    for(int j = 0; j < vs.length ; j++){
                        //cf = i == 0 ? hcs : j != 0 ? cs : (i % 2 != 0 ? hcs : rcs);
                        cf = (i % 2 != 0 ? getEvenFormat() : getOddFormat());
                        String value = vs[j];
                        if(value == null || value == "null")  //$NON-NLS-1$
                            value=""; //$NON-NLS-1$

                        if(isDouble(value)){
                            WritableCellFormat vf = getNumberFormat();
                            vf.setBackground(cf.getBackgroundColour());
                            Number number = new Number(swapRows ? i : j,swapRows ? j : i,Double.parseDouble(value),vf);
                            sheet.addCell(number);
                        }
                        else{
                            Label label = new Label(swapRows ? i : j,swapRows ? j : i,value,cf);
                            sheet.addCell(label); 
                        }
                    }
                }



                wb.write();
                wb.close();
                byte[] output =bout.toByteArray();
                return output;

            }
        } catch (RowsExceededException e) {
            LOG.error(e.getMessage(),e);
        } catch (NumberFormatException e) {
            LOG.error(e.getMessage(),e);
        } catch (WriteException e) {
            LOG.error(e.getMessage(),e);
        } catch (IOException e) {
            LOG.error(e.getMessage(),e);
        }
        return new byte[0];
    }

private static WritableCellFormat getOddFormat() throws WriteException {
    WritableCellFormat cs = new WritableCellFormat();
    cs.setBorder(Border.ALL, BorderLineStyle.THIN);
    cs.setBackground(Colour.PALE_BLUE);
    return cs;
}
private static WritableCellFormat getEvenFormat() throws WriteException {
    WritableCellFormat cs = new WritableCellFormat();
    cs.setBorder(Border.ALL, BorderLineStyle.THIN);


    cs.setBackground(Colour.BLUE);
    return cs;
}
private static WritableCellFormat getNumberFormat() throws WriteException {
    WritableCellFormat cs = new WritableCellFormat(new NumberFormat("###,###,###.###")); //$NON-NLS-1$
    cs.setBorder(Border.ALL, BorderLineStyle.THIN);
    cs.setAlignment(Alignment.RIGHT);
    return cs;
}


public String getExtension() {
        return extensionFile;
}

public static boolean isDouble(String obj){
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