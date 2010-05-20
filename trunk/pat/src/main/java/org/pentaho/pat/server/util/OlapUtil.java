/*
 * Copyright (C) 2009 Luc Boudreau
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.olap4j.Axis;
import org.olap4j.CellSet;
import org.olap4j.OlapException;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.Member;
import org.olap4j.query.Query;
import org.olap4j.query.QueryAxis;
import org.olap4j.query.QueryDimension;
import org.olap4j.query.Selection;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.StringTree;
import org.pentaho.pat.rpc.dto.celltypes.AbstractBaseCell;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;
import org.pentaho.pat.server.messages.Messages;

public class OlapUtil {

    private static Map<String, CellSet> cellSetMap = new HashMap<String, CellSet>();

    /**
     * 
     * Converts a Olap4j cellset to a CellDataSet Matrix.
     * 
     * @param cellSet
     *            The Olap4j cellset.
     * @return cds The Matrix.
     */

    public static CellDataSet cellSet2Matrix(final CellSet cellSet) {
        if (cellSet == null) {
            return null;
        }
        final PatCellSetFormatter pcsf = new PatCellSetFormatter();

        final Matrix matrix = pcsf.format(cellSet);
        final CellDataSet cds = new CellDataSet(matrix.getMatrixWidth(), matrix.getMatrixHeight());

        int z = 0;
        final AbstractBaseCell[][] bodyvalues = new AbstractBaseCell[matrix.getMatrixHeight() - matrix.getOffset()][matrix
                .getMatrixWidth()];
        for (int y = matrix.getOffset(); y < matrix.getMatrixHeight(); y++) {

            for (int x = 0; x < matrix.getMatrixWidth(); x++) {
                bodyvalues[z][x] = matrix.get(x, y);
            }
            z++;
        }

        cds.setCellSetBody(bodyvalues);

        final AbstractBaseCell[][] headervalues = new AbstractBaseCell[matrix.getOffset()][matrix.getMatrixWidth()];
        for (int y = 0; y < matrix.getOffset(); y++) {
            for (int x = 0; x < matrix.getMatrixWidth(); x++) {
                headervalues[y][x] = matrix.get(x, y);
            }
        }
        cds.setCellSetHeaders(headervalues);
        cds.setOffset(matrix.getOffset());
        return cds;

    }

    /**
     * 
     * Finds or creates a node from a string tree.
     * 
     * @param parent
     *            The parent String Tree.
     * @param srchString
     *            The search string.
     * @return found String Tree.
     */
    public static StringTree findOrCreateNode(final StringTree parent, final String srchString, String caption) {
        StringTree found = null;
        for (int i = 0; i < parent.getChildren().size() && found == null; i++) {
            final StringTree targetNode = parent.getChildren().get(i);
            if (targetNode.getValue().equals(srchString)) {
                found = targetNode;
            }
        }
        if (found == null) {
            found = new StringTree(srchString, caption, parent);
        }
        return found;
    }

    /**
     * @param path
     * @param selections
     * @return null
     */
    public static Selection findSelection(final String path, final List<Selection> selections) {
        for (final Selection selection : selections) {
            if (selection.getName().equals(path)) {
                return selection;
            }
        }
        return null;
    }

    /**
     * @param path
     * @param selections
     * @return null
     */
    public static Selection findSelection(final String path, final List<Selection> selections,
            final Selection.Operator oper) {
        for (final Selection selection : selections) {
            if (selection.getName().equals(path) && selection.getOperator().equals(oper)) {
                return selection;
            }
        }
        return null;
    }

    /**
     * Finds all selections that share the same context and 
     * are on a lower level of the Selection including the selection of the given Path
     * @param path
     * @param dimension
     * @param member
     * @return selection
     */
    public static List<Selection> findSelection(final String path, final QueryDimension dimension,Member contextMember) {
        List<Selection> returnselections = new ArrayList<Selection>();
        Selection children = findSelection(path, dimension.getInclusions(), Selection.Operator.CHILDREN);
        if (children != null && children.getSelectionContext() != null ) {
            returnselections.add(children);
            for (final Selection selection : dimension.getInclusions()) {

                if (containsAllSelection(selection.getSelectionContext(),children.getSelectionContext())) {
                        if (children.getMember().getLevel().getDepth() < selection.getMember().getDepth())
                            returnselections.add(selection);
                    }
                }
        }

        return returnselections;
    }
    
    /**
     * @param path
     * @param dim
     */
    public static Selection findSelection(String path, final QueryDimension dim) {
        //path = "[" + dim.getName() + "]." + path; //$NON-NLS-1$ //$NON-NLS-2$
        return findSelection(path, dim.getInclusions());
    }
    
    public static Boolean containsSelection(Selection selection, List<Selection> collection) {
        if (selection == null || collection == null )
            return false;
        
        for (Selection item : collection) {
            if (item.getMember().getUniqueName().equals(selection.getMember().getUniqueName())) {
                return true;
            }
        }
        
        return false;
    }
    
    public static Boolean containsAllSelection(List<Selection> selection,List<Selection> collection) {
        if (selection == null || collection == null )
            return false;
        
        for (Selection item : selection) {
            Boolean ret = containsSelection(item, collection);
            if (ret == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * getCellSet returns a stored cellset for a query.
     * 
     * @param queryId
     *            The queryId.
     * @return
     */
    public static CellSet getCellSet(final String queryId) {
        return cellSetMap.get(queryId);

    }

    public static void deleteCellSet(final String queryId) {
        cellSetMap.remove(queryId);
    }

    /**
     * @param formattedValue
     * @return color
     */
    public static String getColorValue(final String formattedValue) {
        final String[] values = formattedValue.split("\\|"); //$NON-NLS-1$
        String color = null;

        if (values.length > 2)
            for (int i = 2; i < values.length; i++)
                if (values[i].startsWith("style")) { //$NON-NLS-1$
                    final String colorString = values[i].split("=")[1]; //$NON-NLS-1$
                    if (colorString.equalsIgnoreCase("black")) {//$NON-NLS-1$
                        color = "#000000"; //$NON-NLS-1$
                    } else if (colorString.equalsIgnoreCase("blue")) {//$NON-NLS-1$
                        color = "#0000FF"; //$NON-NLS-1$
                    } else if (colorString.equalsIgnoreCase("cyan")) {//$NON-NLS-1$

                        color = "#00FFFF"; //$NON-NLS-1$

                    } else if (colorString.equalsIgnoreCase("dark-gray")) {//$NON-NLS-1$
                        color = "#A9A9A9"; //$NON-NLS-1$
                    } else if (colorString.equalsIgnoreCase("gray")) {//$NON-NLS-1$
                        color = "#808080"; //$NON-NLS-1$
                    } else if (colorString.equalsIgnoreCase("green")) {//$NON-NLS-1$
                        color = "#008000"; //$NON-NLS-1$
                    } else if (colorString.equalsIgnoreCase("light-gray")) {//$NON-NLS-1$
                        color = "#D3D3D3"; //$NON-NLS-1$
                    } else if (colorString.equalsIgnoreCase("magenta")) {//$NON-NLS-1$
                        color = "#FF00FF"; //$NON-NLS-1$
                    } else if (colorString.equalsIgnoreCase("orange")) {//$NON-NLS-1$
                        color = "#FFA500"; //$NON-NLS-1$
                    } else if (colorString.equalsIgnoreCase("pink")) {//$NON-NLS-1$
                        color = "#FFC0CB"; //$NON-NLS-1$
                    } else if (colorString.equalsIgnoreCase("red")) {//$NON-NLS-1$
                        color = "#FF0000"; //$NON-NLS-1$
                    } else if (colorString.equalsIgnoreCase("white")) {//$NON-NLS-1$
                        color = "#FFFFFF"; //$NON-NLS-1$
                    } else if (colorString.equalsIgnoreCase("yellow")) {//$NON-NLS-1$
                        color = "#FFFF00"; //$NON-NLS-1$
                    } else {
                        color = colorString;
                    }
                }

        return color;
    }

    /**
     * 
     * Returns a Olap4J member from a search with a membercell member.
     * 
     * @param query
     *            The query to search.
     * @param dimension
     *            The dimension to search.
     * @param member
     *            The member to find.
     * @param cellSet
     *            The Olap4J CellSet.
     * @return
     */
    public static Member getMember(final Query query, final QueryDimension dimension, final MemberCell member,
            final CellSet cellSet) throws OlapException {

        // FIXME this method doesn't seem to be correct at all, member and memberout swapped?
        final Cube cube = query.getCube();
        Member memberOut = null;
        if (member.getRawValue() != null)
            memberOut = cube.lookupMember(member.getRawValue().toString());

        if (memberOut == null) {
            // Let's try with only the dimension name in front.
            if (member.getRawValue() != null) {
                final List<String> dimPlusMemberNames = new ArrayList<String>();
                dimPlusMemberNames.add(dimension.getName());

                dimPlusMemberNames.add(member.getRawValue().toString());
                memberOut = cube.lookupMember(dimPlusMemberNames.toArray(new String[dimPlusMemberNames.size()]));
            }
            if (memberOut == null) {
                final String membername = member.getUniqueName().substring(1, member.getUniqueName().length() - 1);
                final String[] memberNames = membername.split("\\]\\.\\["); //$NON-NLS-1$

                memberOut = cube.lookupMember(memberNames);

                if (memberOut == null)
                    // We failed to find the member.
                    throw new OlapException(Messages.getString("Services.Query.Selection.CannotFindMember"));//$NON-NLS-1$
            }

        }

        return memberOut;

    }

    /**
     * @param dimName
     * @param query
     * @return result
     */
    public static QueryDimension getQueryDimension(final Query query, final String dimName) {
        final Map<Axis, QueryAxis> axes = query.getAxes();
        final Set<Axis> keySet = axes.keySet();
        QueryDimension result = null;
        for (final Axis axi : keySet) {
            final QueryAxis qAxis = axes.get(axi);
            for (final QueryDimension testQDim : qAxis.getDimensions())
                if (testQDim.getName().equals(dimName)) {
                    result = testQDim;
                    break;
                }
            if (result != null)
                break;
        }
        return result;
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
     * @param memberNames
     *            in the form memberNames[0] = "All Products", memberNames[1] = "Food", memberNames[2] = "Snacks"
     * @return a String in the following format "[All Products].[Food].[Snacks]
     */
    public static String normalizeMemberNames(final String[] memberNames) {
        final StringBuffer buffer = new StringBuffer();
        /*for (final String name : memberNames) {
            buffer.append("[").append(name).append("]."); //$NON-NLS-1$ //$NON-NLS-2$
        }*/
        for (final String name: memberNames){
        	buffer.append(name);
        }
        /*if (buffer.length() > 0)
            buffer.deleteCharAt(buffer.length() - 1); // Remove the last "."
*/
        return buffer.toString();
    }

    public static StringTree parseMembers(final String[] uniqueMemberNames, String captionNames, final StringTree parentNode) {
        StringTree currentNode = parentNode;
        for (int i = 1; i < uniqueMemberNames.length; i++)
            currentNode = OlapUtil.findOrCreateNode(currentNode, uniqueMemberNames[i], captionNames);
        return parentNode;
    }

    /**
     * storeCellSet stores a cellset generated from a query so we can manipulate it at a later date.
     * 
     * @param cellSet
     * @param queryId
     * 
     */
    public static void storeCellSet(final String queryId, final CellSet cellSet) {
        if (cellSetMap.containsKey(queryId)) {
            cellSetMap.remove(queryId);
        }
        cellSetMap.put(queryId, cellSet);
    }

}
