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

import java.text.DecimalFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.olap4j.Cell;
import org.olap4j.CellSet;
import org.olap4j.CellSetAxis;
import org.olap4j.OlapException;
import org.olap4j.Position;
import org.olap4j.impl.CoordinateIterator;
import org.olap4j.impl.Olap4jUtil;
import org.olap4j.metadata.Member;
import org.olap4j.metadata.NamedList;
import org.olap4j.metadata.Property;
import org.pentaho.pat.rpc.dto.celltypes.AbstractBaseCell;
import org.pentaho.pat.rpc.dto.celltypes.DataCell;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;

public class PatCellSetFormatter {
    /**
     * Description of an axis.
     */
    private static class AxisInfo {
        final List<AxisOrdinalInfo> ordinalInfos;

        /**
         * Creates an AxisInfo.
         * 
         * @param ordinalCount
         *            Number of hierarchies on this axis
         */
        AxisInfo(final int ordinalCount) {
            ordinalInfos = new ArrayList<AxisOrdinalInfo>(ordinalCount);
            for (int i = 0; i < ordinalCount; i++) {
                ordinalInfos.add(new AxisOrdinalInfo());
            }
        }

        /**
         * Returns the number of matrix columns required by this axis. The sum of the width of the hierarchies on this
         * axis.
         * 
         * @return Width of axis
         */
        public int getWidth() {
            int width = 0;
            for (final AxisOrdinalInfo info : ordinalInfos) {
                width += info.getWidth();
            }
            return width;
        }
    }

    /**
     * Description of a particular hierarchy mapped to an axis.
     */
    private static class AxisOrdinalInfo {
        private int minDepth = 1;

        private int maxDepth = 0;

        /**
         * Returns the number of matrix columns required to display this hierarchy.
         */
        public int getWidth() {
            return maxDepth - minDepth + 1;
        }
    }

    /**
     * @param formattedValue
     * @return values
     */
    public static String getValueString(final String formattedValue) {
        final String[] values = formattedValue.split("\\|"); //$NON-NLS-1$
        if (values.length > 1) {
            return values[1];
        }
        return values[0];
    }

    /**
     * Returns an iterator over cells in a result.
     */
    private static Iterable<Cell> cellIter(final int[] pageCoords, final CellSet cellSet) {
        return new Iterable<Cell>() {
            public Iterator<Cell> iterator() {
                final int[] axisDimensions = new int[cellSet.getAxes().size() - pageCoords.length];
                assert pageCoords.length <= axisDimensions.length;
                for (int i = 0; i < axisDimensions.length; i++) {
                    final CellSetAxis axis = cellSet.getAxes().get(i);
                    axisDimensions[i] = axis.getPositions().size();
                }
                final CoordinateIterator coordIter = new CoordinateIterator(axisDimensions, true);
                return new Iterator<Cell>() {
                    public boolean hasNext() {
                        return coordIter.hasNext();
                    }

                    public Cell next() {
                        final int[] ints = coordIter.next();
                        final AbstractList<Integer> intList = new AbstractList<Integer>() {
                            @Override
                            public Integer get(final int index) {
                                return index < ints.length ? ints[index] : pageCoords[index - ints.length];
                            }

                            @Override
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

    private Matrix matrix;

    public Matrix format(final CellSet cellSet) {
        // Compute how many rows are required to display the columns axis.
        final CellSetAxis columnsAxis;
        if (cellSet.getAxes().size() > 0) {
            columnsAxis = cellSet.getAxes().get(0);
        } else {
            columnsAxis = null;
        }
        final AxisInfo columnsAxisInfo = computeAxisInfo(columnsAxis);

        // Compute how many columns are required to display the rows axis.
        final CellSetAxis rowsAxis;
        if (cellSet.getAxes().size() > 1) {
            rowsAxis = cellSet.getAxes().get(1);
        } else {
            rowsAxis = null;
        }
        final AxisInfo rowsAxisInfo = computeAxisInfo(rowsAxis);

        if (cellSet.getAxes().size() > 2) {
            final int[] dimensions = new int[cellSet.getAxes().size() - 2];
            for (int i = 2; i < cellSet.getAxes().size(); i++) {
                final CellSetAxis cellSetAxis = cellSet.getAxes().get(i);
                dimensions[i - 2] = cellSetAxis.getPositions().size();
            }
            for (final int[] pageCoords : CoordinateIterator.iterate(dimensions)) {
                matrix = formatPage(cellSet, pageCoords, columnsAxis, columnsAxisInfo, rowsAxis, rowsAxisInfo);
            }
        } else {
            matrix = formatPage(cellSet, new int[] {}, columnsAxis, columnsAxisInfo, rowsAxis, rowsAxisInfo);
        }

        return matrix;
    }

    /**
     * Computes a description of an axis.
     * 
     * @param axis
     *            Axis
     * @return Description of axis
     */
    private AxisInfo computeAxisInfo(final CellSetAxis axis) {
        if (axis == null) {
            return new AxisInfo(0);
        }
        final AxisInfo axisInfo = new AxisInfo(axis.getAxisMetaData().getHierarchies().size());
        int p = -1;
        for (final Position position : axis.getPositions()) {
            ++p;
            int k = -1;
            for (final Member member : position.getMembers()) {
                ++k;
                final AxisOrdinalInfo axisOrdinalInfo = axisInfo.ordinalInfos.get(k);
                if (axisOrdinalInfo.minDepth > member.getDepth() || p == 0) {
                    axisOrdinalInfo.minDepth = member.getDepth();
                }
                axisOrdinalInfo.maxDepth = Math.max(axisOrdinalInfo.maxDepth, member.getDepth());
            }
        }
        return axisInfo;
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
    private Matrix formatPage(final CellSet cellSet, final int[] pageCoords, final CellSetAxis columnsAxis,
            final AxisInfo columnsAxisInfo, final CellSetAxis rowsAxis, final AxisInfo rowsAxisInfo) {

        // Figure out the dimensions of the blank rectangle in the top left
        // corner.
        final int yOffset = columnsAxisInfo.getWidth();
        final int xOffsset = rowsAxisInfo.getWidth();

        // Populate a string matrix
        final Matrix matrix = new Matrix(xOffsset + (columnsAxis == null ? 1 : columnsAxis.getPositions().size()),
                yOffset + (rowsAxis == null ? 1 : rowsAxis.getPositions().size()));

        // Populate corner
        for (int x = 0; x < xOffsset; x++) {
            for (int y = 0; y < yOffset; y++) {
                final MemberCell memberInfo = new MemberCell(false, x > 0);
                matrix.set(x, y, memberInfo);
            }
        }
        // Populate matrix with cells representing axes
        // noinspection SuspiciousNameCombination
        populateAxis(matrix, columnsAxis, columnsAxisInfo, true, xOffsset);
        populateAxis(matrix, rowsAxis, rowsAxisInfo, false, yOffset);

        // Populate cell values
        for (final Cell cell : cellIter(pageCoords, cellSet)) {
            final List<Integer> coordList = cell.getCoordinateList();
            int x = xOffsset;
            if (coordList.size() > 0)
                x += coordList.get(0);
            int y = yOffset;
            if (coordList.size() > 1)
                y += coordList.get(1);
            final DataCell cellInfo = new DataCell(true, false, coordList);

            for (int z = 0; z < matrix.getMatrixHeight(); z++) {
                final AbstractBaseCell headerCell = matrix.get(x, z);

                if (headerCell instanceof MemberCell && ((MemberCell) headerCell).getUniqueName() != null) {
                } else {
                    cellInfo.setParentColMember((MemberCell) matrix.get(x, z - 1));
                    break;
                }
            }

            for (int z = 0; z < matrix.getMatrixWidth(); z++) {
                final AbstractBaseCell headerCell = matrix.get(z, y);
                if (headerCell instanceof MemberCell && ((MemberCell) headerCell).getUniqueName() != null) {

                } else {
                    cellInfo.setParentRowMember((MemberCell) matrix.get(z - 1, y));
                    break;
                }
            }
            
            NamedList<Property> proplist = null;
            try {
                proplist = cell.getCellSet().getMetaData().getCellProperties();
                for(int i = 0; i<proplist.size(); i++){
                	
                   // cellInfo.setProperty(proplist.get(i).getName(), cell.getPropertyValue(proplist.get(i)).toString());
               }
          
            } catch (OlapException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            
            
            if (cell.getValue() != null)
                if (cell.getValue() instanceof Number)
                    cellInfo.setRawNumber((Number) cell.getValue());
            String cellValue = cell.getFormattedValue(); // First try to get a
            // formatted value

            if (cellValue == null || cellValue.equals("null")) { //$NON-NLS-1$
                cellValue =""; //$NON-NLS-1$
            }
            if ( cellValue.length() < 1) {
                final Object value =  cell.getValue();
                if (value == null  || value.equals("null")) //$NON-NLS-1$
                    cellValue = ""; //$NON-NLS-1$
                else {
                    try {
                        DecimalFormat myFormatter = new DecimalFormat("#,###.###"); //$NON-NLS-1$
                        String output = myFormatter.format(cell.getValue());
                        cellValue = output;
                    }
                    catch (Exception e) {
                        // TODO: handle exception
                    }
                }
                // the raw value
            }
            cellInfo.setFormattedValue(getValueString(cellValue));
            matrix.set(x, y, cellInfo);
        }
        return matrix;

    }

    /**
     * Populates cells in the matrix corresponding to a particular axis.
     * 
     * @param matrix
     *            Matrix to populate
     * @param axis
     *            Axis
     * @param axisInfo
     *            Description of axis
     * @param isColumns
     *            True if columns, false if rows
     * @param offset
     *            Ordinal of first cell to populate in matrix
     */
    private void populateAxis(final Matrix matrix, final CellSetAxis axis, final AxisInfo axisInfo,
            final boolean isColumns, final int offset) {
        if (axis == null)
            return;
        final Member[] prevMembers = new Member[axisInfo.getWidth()];
        final MemberCell[] prevMemberInfo = new MemberCell[axisInfo.getWidth()];
        final Member[] members = new Member[axisInfo.getWidth()];

        for (int i = 0; i < axis.getPositions().size(); i++) {
            final int x = offset + i;
            final Position position = axis.getPositions().get(i);
            int yOffset = 0;
            final List<Member> memberList = position.getMembers();

            for (int j = 0; j < memberList.size(); j++) {
                Member member = memberList.get(j);
                final AxisOrdinalInfo ordinalInfo = axisInfo.ordinalInfos.get(j);
                    if (member.getDepth() < ordinalInfo.minDepth)
                        break;
                    final int y = yOffset + member.getDepth() - ordinalInfo.minDepth;
                    members[y] = member;
                yOffset += ordinalInfo.getWidth();
            }

            boolean expanded = false;
            boolean same = true;
            for (int y = 0; y < members.length; y++) {
                final MemberCell memberInfo = new MemberCell();
                final Member member = members[y];
                final List<String> memberPath = new ArrayList<String>();
                expanded = false;
                Boolean exit = false;
                if (member != null) {

//                    System.out.println(" ");
//                    System.out.println(" ");
//                    System.out.println(" ");
//                    System.out.println("############################# Member: " + member.getUniqueName() + " ######################");
                    for (int z = i+1; z < axis.getPositionCount() && exit == false; z++) {
//                        System.out.println("------------------------------------POSITION " + z +" ------------------------------------");
                        List<Member> posMembers = axis.getPositions().get(z).getMembers();
                        for (int k = 0;k<posMembers.size();k++) {
                            Member possibleChild = posMembers.get(k);
                            if (possibleChild.getParentMember() !=  null && possibleChild.getParentMember().equals(member)) {
                                expanded = true;
                                exit = true;
                                break;
                            
                            }
                            if (member.getUniqueName().equals(possibleChild.getUniqueName())) {
                                if (posMembers.size() == 1) {
                                    exit = true;
                                    break;
                                }
                                else {
                                    Boolean notExpanded = false;
                                    for (int t = k+1;t<posMembers.size() && notExpanded == false;t++) {
                                        Member prevPosMember = axis.getPositions().get(z-1).getMembers().get(t);
                                        if (posMembers.get(t).getDimension().equals(prevPosMember.getDimension())
                                                &&
                                                posMembers.get(t).getHierarchy().equals(prevPosMember.getHierarchy())
                                                &&
                                                !axis.getPositions().get(z-1).getMembers().get(t).equals(posMembers.get(t))  ) {

                                            notExpanded = true;
                                        }
                                    }
                                    if (!notExpanded) {
                                        exit = true;
                                        break;
                                    }
                                }
                            }
                            
//                          
                        }
                    }
                }
                if (member != null)
                    memberPath.add(member.getUniqueName());
                memberInfo.setMemberPath(memberPath);
                memberInfo.setExpanded(expanded);
                same = same && i > 0 && Olap4jUtil.equal(prevMembers[y], member);

                
                if (member != null) {
                    if (x - 1 == offset)
                        memberInfo.setLastRow(true);

                    matrix.setOffset(offset);
                    memberInfo.setRawValue(member.getCaption(null));
                    memberInfo.setFormattedValue(member.getCaption(null)); // First try to get a formatted value
                    memberInfo.setParentDimension(member.getDimension().getName());
                    memberInfo.setUniquename(member.getUniqueName());
                    memberInfo.setChildMemberCount(member.getChildMemberCount());
                    NamedList<Property> values = member.getLevel().getProperties();
                    for(int j=0; j<values.size();j++){
                        memberInfo.setProperty(values.get(j).getCaption(null), member.getPropertyFormattedValue(values.get(j)));
                    }
                    
                    if (y > 0) {
                        for (int previ = y-1; previ >= 0;previ--) {
                            if(prevMembers[previ] != null) {
                                memberInfo.setRightOf(prevMemberInfo[previ]);
                                memberInfo.setRightOfDimension(prevMembers[previ].getDimension().getName());
                                previ = -1;
                            }
                        }
                    }


                    if (member.getParentMember() != null)
                        memberInfo.setParentMember(member.getParentMember().getUniqueName());

                } else {
                    memberInfo.setRawValue(null);
                    memberInfo.setFormattedValue(null);
                    memberInfo.setParentDimension(null);
                }

                if (isColumns) {
                    memberInfo.setRight(false);
                    memberInfo.setSameAsPrev(same);
                    if (member != null)
                        memberInfo.setParentDimension(member.getDimension().getName());
                    matrix.set(x, y, memberInfo);
                } else {
                    if (same) {
                        memberInfo.setFormattedValue(null);
                        memberInfo.setRawValue(null);
                        memberInfo.setParentDimension(null);
                    }
                    memberInfo.setRight(false);
                    memberInfo.setSameAsPrev(false);

                    matrix.set(y, x, memberInfo);
                }
                prevMembers[y] = member;
                prevMemberInfo[y] = memberInfo;
                members[y] = null;
            }
        }
    }
}
