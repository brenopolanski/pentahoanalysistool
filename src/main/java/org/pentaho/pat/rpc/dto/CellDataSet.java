package org.pentaho.pat.rpc.dto;

import java.io.Serializable;

import org.pentaho.pat.rpc.dto.celltypes.BaseCell;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CellDataSet implements IsSerializable, Serializable {

    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int width;
    private int height;

    private BaseCell[][] cellSetHeader;
    private BaseCell[][] cellSetBody;
    private int offset;
    
    public CellDataSet(){}
    public CellDataSet(int width, int height){
	        this.setWidth(width);
	        this.setHeight(height);
    }
    
    

    public BaseCell[][] getCellSetHeaders(){
	return cellSetHeader;
    }
    
    public void setCellSetHeaders(BaseCell[][] cellSet){
	this.cellSetHeader = cellSet;
    }
    
    public BaseCell[][] getCellSetBody(){
	return cellSetBody;
    }
    
    public void setCellSetBody(BaseCell[][] cellSet){
	this.cellSetBody = cellSet;
    }
    
    public void setOffset(int offset) {
	// TODO Auto-generated method stub
	this.offset = offset;
}
public int getOffset() {
	return offset;
	
}

/**
 * @param width the width to set
 */
public void setWidth(int width) {
    this.width = width;
}

/**
 * @return the width
 */
public int getWidth() {
    return width;
}

/**
 * @param height the height to set
 */
public void setHeight(int height) {
    this.height = height;
}

/**
 * @return the height
 */
public int getHeight() {
    return height;
}
}
