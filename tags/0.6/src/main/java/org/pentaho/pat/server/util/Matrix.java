/*
 * Copyright (C) 2009 Thomas Barber
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
package org.pentaho.pat.server.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pentaho.pat.rpc.dto.celltypes.AbstractBaseCell;
import org.pentaho.pat.rpc.dto.celltypes.DataCell;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;

public class Matrix {

    private Map<List<Integer>, AbstractBaseCell> map = new HashMap<List<Integer>, AbstractBaseCell>();

    private int width = 0;

    private int height = 0;

    private int offset;

    public Matrix() {
    }

    /**
     * Creats a Matrix.
     * 
     * @param width
     *            Width of matrix
     * @param height
     *            Height of matrix
     */
    public Matrix(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Sets the value at a particular coordinate
     * 
     * @param x
     *            X coordinate
     * @param y
     *            Y coordinate
     * @param value
     *            Value
     * @param right
     *            Whether value is right-justified
     * @param sameAsPrev
     *            Whether value is the same as the previous value. If true, some formats separators between cells
     */
    public void set(final int x, final int y, final DataCell cell) {
        map.put(Arrays.asList(x, y), cell);
        assert x >= 0 && x < width : x;
        assert y >= 0 && y < height : y;
    }

    /**
     * Sets the value at a particular coordinate
     * 
     * @param x
     *            X coordinate
     * @param y
     *            Y coordinate
     * @param value
     *            Value
     * @param right
     *            Whether value is right-justified
     * @param sameAsPrev
     *            Whether value is the same as the previous value. If true, some formats separators between cells
     */
    public void set(final int x, final int y, final MemberCell value) {
        map.put(Arrays.asList(x, y), value);
        assert x >= 0 && x < width : x;
        assert y >= 0 && y < height : y;
    }

    /**
     * Returns the cell at a particular coordinate.
     * 
     * @param x
     *            X coordinate
     * @param y
     *            Y coordinate
     * @return Cell
     */
    public AbstractBaseCell get(final int x, final int y) {
        return map.get(Arrays.asList(x, y));
    }

    /**
     * Return the width of the created matrix.
     * 
     * @return the width
     */
    public int getMatrixWidth() {
        return width;
    }

    /**
     * Return the height of the matrix.
     * 
     * @return the height
     */
    public int getMatrixHeight() {
        return height;
    }

    /**
     * Return the generated hashmap.
     * 
     * @return the map
     */
    public Map<List<Integer>, AbstractBaseCell> getMap() {
        return map;
    }

    /**
     * 
     * Set the header/row data offset.
     * 
     * @param offset
     */
    public void setOffset(final int offset) {
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
