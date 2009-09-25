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
package org.pentaho.pat.server.services;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.olap4j.Axis;
import org.olap4j.CellSet;
import org.olap4j.query.Query;
import org.olap4j.query.QueryAxis;
import org.olap4j.query.QueryDimension;
import org.olap4j.query.Selection;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.StringTree;
import org.pentaho.pat.rpc.dto.celltypes.BaseCell;
import org.pentaho.pat.server.util.Matrix;
import org.pentaho.pat.server.util.PatCellSetFormatter;

public class OlapUtil {
    public static CellDataSet cellSet2Matrix(final CellSet cellSet) {
        if (cellSet == null)
            return null;
    
        final PatCellSetFormatter pcsf = new PatCellSetFormatter();

        final Matrix matrix = pcsf.format(cellSet);
        final CellDataSet cds = new CellDataSet(matrix.getMatrixWidth(), matrix.getMatrixHeight());

        int z = 0;
        final BaseCell[][] bodyvalues = new BaseCell[matrix.getMatrixHeight() - matrix.getOffset()][matrix
                .getMatrixWidth()];
        for (int y = matrix.getOffset(); y < matrix.getMatrixHeight(); y++) {

            for (int x = 0; x < matrix.getMatrixWidth(); x++)
                bodyvalues[z][x] = matrix.get(x, y);
            z++;
        }

        cds.setCellSetBody(bodyvalues);

        // final BaseCell[][] headervalues = new BaseCell[matrix.getMatrixHeight()][matrix.getMatrixWidth()];
        final BaseCell[][] headervalues = new BaseCell[matrix.getOffset()][matrix.getMatrixWidth()];
        for (int y = 0; y < matrix.getOffset(); y++)
            for (int x = 0; x < matrix.getMatrixWidth(); x++)
                headervalues[y][x] = matrix.get(x, y);
        // headervalues[y][x].setParentDimension(matrix.get(x, y));

        cds.setCellSetHeaders(headervalues);
        cds.setOffset(matrix.getOffset());
        return cds;

    }

    public static StringTree findOrCreateNode(final StringTree parent, final String srchString) {
        StringTree found = null;
        for (int i = 0; i < parent.getChildren().size() && found == null; i++) {
            final StringTree targetNode = parent.getChildren().get(i);
            if (targetNode.getValue().equals(srchString))
                found = targetNode;
        }
        if (found == null)
            found = new StringTree(srchString, parent);
        return found;
    }

    /**
     * @param path
     * @param selections
     * @return null
     */
    public static Selection findSelection(final String path, final List<Selection> selections) {
        for (final Selection selection : selections)
            if (selection.getName().equals(path))
                return selection;
        return null;
    }

    /**
     * @param path
     * @param dim
     */
    @SuppressWarnings("deprecation")
    public static Selection findSelection(String path, final QueryDimension dim) {
        path = "[" + dim.getName() + "]." + path; //$NON-NLS-1$ //$NON-NLS-2$
        return findSelection(path, dim.getSelections());
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
                    if (colorString.equalsIgnoreCase("black"))
                        color = "#000000"; //$NON-NLS-1$
                    else if (colorString.equalsIgnoreCase("blue"))
                        color = "#0000FF"; //$NON-NLS-1$
                    else if (colorString.equalsIgnoreCase("cyan"))
                        color = "#00FFFF"; //$NON-NLS-1$
                    else if (colorString.equalsIgnoreCase("dark-gray"))
                        color = "#A9A9A9"; //$NON-NLS-1$
                    else if (colorString.equalsIgnoreCase("gray"))
                        color = "#808080"; //$NON-NLS-1$
                    else if (colorString.equalsIgnoreCase("green"))
                        color = "#008000"; //$NON-NLS-1$
                    else if (colorString.equalsIgnoreCase("light-gray"))
                        color = "#D3D3D3"; //$NON-NLS-1$
                    else if (colorString.equalsIgnoreCase("magenta"))
                        color = "#FF00FF"; //$NON-NLS-1$
                    else if (colorString.equalsIgnoreCase("orange"))
                        color = "#FFA500"; //$NON-NLS-1$
                    else if (colorString.equalsIgnoreCase("pink"))
                        color = "#FFC0CB"; //$NON-NLS-1$
                    else if (colorString.equalsIgnoreCase("red"))
                        color = "#FF0000"; //$NON-NLS-1$
                    else if (colorString.equalsIgnoreCase("white"))
                        color = "#FFFFFF"; //$NON-NLS-1$
                    else if (colorString.equalsIgnoreCase("yellow"))
                        color = "#FFFF00"; //$NON-NLS-1$
                    else
                        color = colorString;
                }

        return color;
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
        if (values.length > 1)
            return values[1];
        return values[0];
    }

    /**
     * @param memberNames
     *            in the form memberNames[0] = "All Products", memberNames[1] = "Food", memberNames[2] = "Snacks"
     * @return a String in the following format "[All Products].[Food].[Snacks]
     */
    public static String normalizeMemberNames(final String[] memberNames) {
        final StringBuffer buffer = new StringBuffer();
        for (final String name : memberNames)
            buffer.append("[").append(name).append("]."); //$NON-NLS-1$ //$NON-NLS-2$
        if (buffer.length() > 0)
            buffer.deleteCharAt(buffer.length() - 1); // Remove the last "."

        return buffer.toString();
    }

    public static StringTree parseMembers(final String[] uniqueMemberNames, final StringTree parentNode) {
        StringTree currentNode = parentNode;
        for (int i = 1; i < uniqueMemberNames.length; i++)
            currentNode = OlapUtil.findOrCreateNode(currentNode, uniqueMemberNames[i]);
        return parentNode;
    }
}
