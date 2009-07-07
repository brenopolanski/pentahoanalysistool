package org.pentaho.pat.rpc.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pentaho.pat.rpc.dto.CellInfo;
import com.google.gwt.user.client.rpc.IsSerializable;

public class Matrix implements Serializable, IsSerializable {
    private static final long serialVersionUID = 1L;
    private Map<List<Integer>, BaseCell> map =
        new HashMap<List<Integer>, BaseCell>();
    public int width=0;
    public int height=0;

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
        CellInfo cell)
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
        MemberInfo value)
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
}
