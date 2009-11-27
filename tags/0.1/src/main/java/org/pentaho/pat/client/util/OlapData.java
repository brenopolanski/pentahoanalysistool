/*
 * Copyright 2007 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 *
 * @created Feb 1, 2008 
 * @author wseyler
 */


package org.pentaho.pat.client.util;

import java.io.Serializable;

/**
 * @author wseyler
 *
 */
public class OlapData implements Serializable {
  RowHeaders rowHeaders;
  ColumnHeaders columnHeaders;
  CellData cellData;
  
  public OlapData() {
    super();
  }

  public OlapData(RowHeaders rowHeaders, ColumnHeaders columnHeaders, CellData cellData) {
    this();
    this.rowHeaders = rowHeaders;
    this.columnHeaders = columnHeaders;
    this.cellData = cellData;
  }

  public RowHeaders getRowHeaders() {
    return rowHeaders;
  }

  public void setRowHeaders(RowHeaders rowHeaders) {
    this.rowHeaders = rowHeaders;
  }

  public ColumnHeaders getColumnHeaders() {
    return columnHeaders;
  }

  public void setColumnHeaders(ColumnHeaders columnHeaders) {
    this.columnHeaders = columnHeaders;
  }

  public CellData getCellData() {
    return cellData;
  }

  public void setCellData(CellData cellData) {
    this.cellData = cellData;
  }
  
  
}