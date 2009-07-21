package org.pentaho.pat.server.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pentaho.pat.rpc.dto.celltypes.BaseCell;
import org.pentaho.pat.rpc.dto.celltypes.DataCell;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;

public class Matrix {
    
    private Map<List<Integer>, BaseCell> map =
        new HashMap<List<Integer>, BaseCell>();
    private int width=0;
    private int height=0;
	private int offset;

    public Matrix(){}
    /**
     * Creats a Matrix.
     *
     * @param width Width of matrix
     * @param height Height of matrix
     */
    public Matrix(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Sets the value at a particular coordinate
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param value Value
     * @param right Whether value is right-justified
     * @param sameAsPrev Whether value is the same as the previous value.
     * If true, some formats separators between cells
     */
    public void set(
        int x,
        int y,
        DataCell cell)
    {
        map.put(
            Arrays.asList(x, y),
            cell);
        assert x >= 0 && x < width : x;
        assert y >= 0 && y < height : y;
    }
    
    /**
     * Sets the value at a particular coordinate
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param value Value
     * @param right Whether value is right-justified
     * @param sameAsPrev Whether value is the same as the previous value.
     * If true, some formats separators between cells
     */
    public void set(
        int x,
        int y,
        MemberCell value)
    {
        map.put(
            Arrays.asList(x, y),
            value);
        assert x >= 0 && x < width : x;
        assert y >= 0 && y < height : y;
    }
    /**
     * Returns the cell at a particular coordinate.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return Cell
     */
    public BaseCell get(int x, int y) {
        return map.get(Arrays.asList(x, y));
    }
	
    /**
	 * Return the width of the created matrix.
	 * @return the width
	 */
	public int getMatrixWidth() {
		return width;
	}
	
	/**
	 * Return the height of the matrix.
	 * @return the height
	 */
	public int getMatrixHeight() {
		return height;
	}
	
	/**
	 * Return the generated hashmap.
	 * @return the map
	 */
	public Map<List<Integer>, BaseCell> getMap() {
		return map;
	}
	
	/**
	 * 
	 * Set the header/row data offset.
	 *
	 * @param offset
	 */
	public void setOffset(int offset) {
		// TODO Auto-generated method stub
		this.offset = offset;
	}
	
	/**
	 * 
	 * Return the header/row data offset.
	 *
	 * @return offset
	 */
	public int getOffset() {
		return offset;
		
	}
	
}
