package org.pentaho.pat.server.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.olap4j.Cell;
import org.olap4j.CellSet;
import org.olap4j.CellSetAxis;
import org.olap4j.Position;
import org.olap4j.impl.CoordinateIterator;
import org.olap4j.impl.Olap4jUtil;
import org.olap4j.metadata.Member;
import org.pentaho.pat.rpc.dto.celltypes.DataCell;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;

public class PatCellSetFormatter  { 
    private Matrix matrix;

    /**
     * Creates a PatCellSetFormatter.
     */
    public PatCellSetFormatter() {

    }

    public Matrix format(CellSet cellSet) {
        // Compute how many rows are required to display the columns axis.
        final CellSetAxis columnsAxis;
        if (cellSet.getAxes().size() > 0) {
            columnsAxis = cellSet.getAxes().get(0);
        } else {
            columnsAxis = null;
        }
        AxisInfo columnsAxisInfo = computeAxisInfo(columnsAxis);

        // Compute how many columns are required to display the rows axis.
        final CellSetAxis rowsAxis;
        if (cellSet.getAxes().size() > 1) {
            rowsAxis = cellSet.getAxes().get(1);
        } else {
            rowsAxis = null;
        }
        AxisInfo rowsAxisInfo = computeAxisInfo(rowsAxis);

        if (cellSet.getAxes().size() > 2) {
            int[] dimensions = new int[cellSet.getAxes().size() - 2];
            for (int i = 2; i < cellSet.getAxes().size(); i++) {
                CellSetAxis cellSetAxis = cellSet.getAxes().get(i);
                dimensions[i - 2] = cellSetAxis.getPositions().size();
            }
            for (int[] pageCoords : CoordinateIterator.iterate(dimensions)) {
                matrix = formatPage(
                    cellSet,
                    pageCoords,
                    columnsAxis,
                    columnsAxisInfo,
                    rowsAxis,
                    rowsAxisInfo);
            }
        } else {
           matrix = formatPage(
                cellSet,
                new int[] {},
                columnsAxis,
                columnsAxisInfo,
                rowsAxis,
                rowsAxisInfo);
        }
        
       
	
        return matrix;
    }

    /**
     * Formats a two-dimensional page.
     * 
     * @param cellSet
     *            Cell set
     * @param pw
     *            Print writer
     * @param pageCoords
     *            Coordinates of page [page, chapter, section, ...]
     * @param columnsAxis
     *            Columns axis
     * @param columnsAxisInfo
     *            Description of columns axis
     * @param rowsAxis
     *            Rows axis
     * @param rowsAxisInfo
     *            Description of rows axis
     */
    private Matrix formatPage(CellSet cellSet, int[] pageCoords,
	    CellSetAxis columnsAxis, AxisInfo columnsAxisInfo,
	    CellSetAxis rowsAxis, AxisInfo rowsAxisInfo) {

	// Figure out the dimensions of the blank rectangle in the top left
	// corner.
	final int yOffset = columnsAxisInfo.getWidth();
	final int xOffsset = rowsAxisInfo.getWidth();

	// Populate a string matrix
	Matrix matrix = new Matrix(
		xOffsset
			+ (columnsAxis == null ? 1 : columnsAxis.getPositions()
				.size()), yOffset
			+ (rowsAxis == null ? 1 : rowsAxis.getPositions()
				.size()));

	// Populate corner
	for (int x = 0; x < xOffsset; x++) {
	    for (int y = 0; y < yOffset; y++) {
		MemberCell memberInfo = new MemberCell(false, x > 0);
		matrix.set(x, y, memberInfo);
	    }
	}

	// Populate matrix with cells representing axes
	// noinspection SuspiciousNameCombination
	populateAxis(matrix, columnsAxis, columnsAxisInfo, true, xOffsset);
	populateAxis(matrix, rowsAxis, rowsAxisInfo, false, yOffset);

	// Populate cell values
	for (Cell cell : cellIter(pageCoords, cellSet)) {
	    final List<Integer> coordList = cell.getCoordinateList();
	    int x = xOffsset;
	    if (coordList.size() > 0) {
		x += coordList.get(0);
	    }
	    int y = yOffset;
	    if (coordList.size() > 1) {
		y += coordList.get(1);
	    }
	    DataCell cellInfo = new DataCell(true, false);
	    cellInfo.setRawValue(cell.getFormattedValue());
	    String cellValue = cell.getFormattedValue(); // First try to get a
							 // formatted value
	    if (cellValue.length() < 1) {
		Number value = (Number) cell.getValue();
		if (value == null || value.doubleValue() < 1.23457E08) {
		    cellValue = "null"; //$NON-NLS-1$
		} else {
		    cellValue = cell.getValue().toString(); // Otherwise return
							    // the raw value
		}
	    }
	    cellInfo.setFormattedValue(getValueString(cellValue));
	    matrix.set(x, y, cellInfo);
	}
	return matrix;

    }
    

    /**
     * Populates cells in the matrix corresponding to a particular axis.
     *
     * @param matrix Matrix to populate
     * @param axis Axis
     * @param axisInfo Description of axis
     * @param isColumns True if columns, false if rows
     * @param offset Ordinal of first cell to populate in matrix
     */
    private void populateAxis(
        Matrix matrix,
        CellSetAxis axis,
        AxisInfo axisInfo,
        boolean isColumns,
        int offset)
    {
        if (axis == null) {
            return;
        }
        Member[] prevMembers = new Member[axisInfo.getWidth()];
        Member[] members = new Member[axisInfo.getWidth()];
        for (int i = 0; i < axis.getPositions().size(); i++) {
            final int x = offset + i;
            Position position = axis.getPositions().get(i);
            int yOffset = 0;
            final List<Member> memberList = position.getMembers();
            for (int j = 0; j < memberList.size(); j++) {
                Member member = memberList.get(j);
                final AxisOrdinalInfo ordinalInfo =
                    axisInfo.ordinalInfos.get(j);
                while (member != null) {
                    if (member.getDepth() < ordinalInfo.minDepth) {
                        break;
                    }
                    final int y =
                        yOffset
                        + member.getDepth()
                        - ordinalInfo.minDepth;
                    members[y] = member;
                    member = member.getParentMember();
                }
                yOffset += ordinalInfo.getWidth();
            }
            boolean same = true;
            for (int y = 0; y < members.length; y++) {
                Member member = members[y];
                same =
                    same
                    && i > 0
                    && Olap4jUtil.equal(prevMembers[y], member);
                                    
                MemberCell memberInfo = new MemberCell();
                if(member!=null){
                	if(x-1==offset){
                		memberInfo.setLastRow(true);
                	}
                	
                matrix.setOffset(offset);
                memberInfo.setRawValue(member.getCaption(null));
    	        memberInfo.setFormattedValue(member.getCaption(null));  // First try to get a formatted value
                }
                else
                {
                    memberInfo.setRawValue(""); //$NON-NLS-1$
                    memberInfo.setFormattedValue(""); //$NON-NLS-1$
                }
    	
                if (isColumns) {
                    memberInfo.setRight(false);
                    memberInfo.setSameAsPrev(same);
                    matrix.set(x, y, memberInfo);
                } else {
                    if (same) {
                       memberInfo.setFormattedValue(""); //$NON-NLS-1$
                       memberInfo.setRawValue(""); //$NON-NLS-1$
                    }
                    memberInfo.setRight(false);
                    memberInfo.setSameAsPrev(false);
		    //noinspection SuspiciousNameCombination
                    matrix.set(y, x, memberInfo);
                }
                prevMembers[y] = member;
                members[y] = null;
            }
        }
    }

    /**
     * Computes a description of an axis.
     *
     * @param axis Axis
     * @return Description of axis
     */
    private AxisInfo computeAxisInfo(CellSetAxis axis)
    {
        if (axis == null) {
            return new AxisInfo(0);
        }
        final AxisInfo axisInfo =
            new AxisInfo(axis.getAxisMetaData().getHierarchies().size());
        int p = -1;
        for (Position position : axis.getPositions()) {
            ++p;
            int k = -1;
            for (Member member : position.getMembers()) {
                ++k;
                final AxisOrdinalInfo axisOrdinalInfo =
                    axisInfo.ordinalInfos.get(k);
                final int topDepth =
                    member.isAll()
                        ? member.getDepth()
                        : member.getHierarchy().hasAll()
                            ? 1
                            : 0;
                if (axisOrdinalInfo.minDepth > topDepth
                    || p == 0)
                {
                    axisOrdinalInfo.minDepth = topDepth;
                }
                axisOrdinalInfo.maxDepth =
                    Math.max(
                        axisOrdinalInfo.maxDepth,
                        member.getDepth());
            }
        }
        return axisInfo;
    }

    /**
     * Returns an iterator over cells in a result.
     */
    private static Iterable<Cell> cellIter(
        final int[] pageCoords,
        final CellSet cellSet)
    {
        return new Iterable<Cell>() {
            public Iterator<Cell> iterator() {
                int[] axisDimensions =
                    new int[cellSet.getAxes().size() - pageCoords.length];
                assert pageCoords.length <= axisDimensions.length;
                for (int i = 0; i < axisDimensions.length; i++) {
                    CellSetAxis axis = cellSet.getAxes().get(i);
                    axisDimensions[i] = axis.getPositions().size();
                }
                final CoordinateIterator coordIter =
                    new CoordinateIterator(axisDimensions, true);
                return new Iterator<Cell>() {
                    public boolean hasNext() {
                        return coordIter.hasNext();
                    }

                    public Cell next() {
                        final int[] ints = coordIter.next();
                        final AbstractList<Integer> intList =
                            new AbstractList<Integer>() {
                                public Integer get(int index) {
                                    return index < ints.length
                                        ? ints[index]
                                        : pageCoords[index - ints.length];
                                }

                                public int size() {
                                    return pageCoords.length + ints.length;
                                }
                            };
                        return cellSet.getCell(intList);
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    /**
     * Description of a particular hierarchy mapped to an axis.
     */
    private static class AxisOrdinalInfo {
        int minDepth = 1;
        int maxDepth = 0;

        /**
         * Returns the number of matrix columns required to display this
         * hierarchy.
         */
        public int getWidth() {
            return maxDepth - minDepth + 1;
        }
    }

    /**
     * Description of an axis.
     */
    private static class AxisInfo {
        final List<AxisOrdinalInfo> ordinalInfos;

        /**
         * Creates an AxisInfo.
         *
         * @param ordinalCount Number of hierarchies on this axis
         */
        AxisInfo(int ordinalCount) {
            ordinalInfos = new ArrayList<AxisOrdinalInfo>(ordinalCount);
            for (int i = 0; i < ordinalCount; i++) {
                ordinalInfos.add(new AxisOrdinalInfo());
            }
        }

        /**
         * Returns the number of matrix columns required by this axis. The
         * sum of the width of the hierarchies on this axis.
         *
         * @return Width of axis
         */
        public int getWidth() {
            int width = 0;
            for (AxisOrdinalInfo info : ordinalInfos) {
                width += info.getWidth();
            }
            return width;
        }
    }

    /**
     * @param formattedValue
     * @return values
     */
    public static String getValueString(String formattedValue) {
      String[] values = formattedValue.split("\\|"); //$NON-NLS-1$
      if (values.length > 1) {
        return values[1];
      }
      return values[0];
    }
    
    }

