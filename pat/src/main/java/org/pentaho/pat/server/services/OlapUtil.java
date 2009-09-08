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
	public static StringTree parseMembers(String[] uniqueMemberNames, StringTree parentNode) {
	    StringTree currentNode = parentNode;
	    for (int i=1; i<uniqueMemberNames.length; i++) {
	      currentNode = OlapUtil.findOrCreateNode(currentNode, uniqueMemberNames[i]);
	    }
	    return parentNode;
	  }
	  public static StringTree findOrCreateNode(StringTree parent, String srchString) {
		    StringTree found = null;
		    for (int i=0; i<parent.getChildren().size() && found == null; i++) {
		      StringTree targetNode = (StringTree)parent.getChildren().get(i);
		      if (targetNode.getValue().equals(srchString)) {
		        found = targetNode;
		      }
		    }
		    if (found == null) {  // couldn't find it in the children so we'll create it
		      found = new StringTree(srchString, parent);
		    }
		    return found;
		  }

	  
	  /**
	   * @param dimName
	   * @param query
	   * @return result
	   */
	  public static QueryDimension getQueryDimension(Query query, String dimName) {
	    Map<Axis, QueryAxis> axes = query.getAxes();
	    Set<Axis> keySet = axes.keySet();
	    QueryDimension result = null;
	    for (Axis axi : keySet) {
	      QueryAxis qAxis = axes.get(axi);
	      for (QueryDimension testQDim : qAxis.getDimensions()) {
	        if (testQDim.getName().equals(dimName)) {
	          result = testQDim;
	          break;
	        }
	      }
	      if (result != null) {
	        break;
	      }
	    }
	    return result;
	  }
	  
	  /**
	   * @param memberNames  in the form memberNames[0] = "All Products", memberNames[1] = "Food", memberNames[2] = "Snacks"
	   * @return a String in the following format "[All Products].[Food].[Snacks]
	   */
	  public static String normalizeMemberNames(String[] memberNames) {
	    StringBuffer buffer = new StringBuffer();
	    for (String name : memberNames) {
	      buffer.append("[").append(name).append("]."); //$NON-NLS-1$ //$NON-NLS-2$
	    }
	    if (buffer.length() > 0) {
	      buffer.deleteCharAt(buffer.length()-1); // Remove the last "."
	    }
	    
	    return buffer.toString();
	  }
	  
	  /**
	   * @param path
	   * @param selections
	   * @return null
	   */
	  public static Selection findSelection(String path, List<Selection> selections) {
	    for (Selection selection : selections) {
	      if (selection.getName().equals(path)) {
	        return selection;
	      }
	    }
	    return null;
	  }

	  /**
	   * @param path
	   * @param dim
	   */
	  @SuppressWarnings("deprecation")
	public static Selection findSelection(String path, QueryDimension dim) {
	    path = "[" + dim.getName() + "]." + path; //$NON-NLS-1$ //$NON-NLS-2$
		return findSelection(path, dim.getSelections());
	  }
	  
	  public static CellDataSet cellSet2Matrix(CellSet cellSet){
	      if (cellSet == null) {
		      return null;
		    }
	      PatCellSetFormatter pcsf = new PatCellSetFormatter();
		    
	      Matrix matrix = pcsf.format(cellSet);
	      CellDataSet cds = new CellDataSet(matrix.getMatrixWidth(), matrix.getMatrixHeight());
	      
	      int z = 0;
		final BaseCell[][] bodyvalues = new BaseCell[matrix.getMatrixHeight() - matrix.getOffset() + 2][matrix.getMatrixWidth()];
		for (int y = matrix.getOffset(); y < matrix.getMatrixHeight(); y++) {

			for (int x = 0; x < matrix.getMatrixWidth(); x++) {
				bodyvalues[z][x] = matrix.get(x, y);
			}
			z++;
		}	
	      
	      cds.setCellSetBody(bodyvalues);
	      
	      //final BaseCell[][] headervalues = new BaseCell[matrix.getMatrixHeight()][matrix.getMatrixWidth()];
	      final BaseCell[][] headervalues = new BaseCell[matrix.getOffset()][matrix.getMatrixWidth()];
		for (int y = 0; y < matrix.getOffset(); y++) {
			for (int x = 0; x < matrix.getMatrixWidth(); x++) {
				headervalues[y][x] = matrix.get(x, y);
				//headervalues[y][x].setParentDimension(matrix.get(x, y));
			}
		}
		
		cds.setCellSetHeaders(headervalues);
	      cds.setOffset(matrix.getOffset());
	      return cds;
	      
	  }
	  
	
			  /**
			   * @param formattedValue
			   * @return color
			   */
			  public static String getColorValue(String formattedValue) {
			    String[] values = formattedValue.split("\\|"); //$NON-NLS-1$
			    String color = null;

			    if (values.length > 2) {  // We've got attributes
			      for (int i=2; i<values.length; i++) {
			        if (values[i].startsWith("style")) { //$NON-NLS-1$
			          String colorString = values[i].split("=")[1]; //$NON-NLS-1$
			          if (colorString.equalsIgnoreCase("black")) { //$NON-NLS-1$
			            color = "#000000"; //$NON-NLS-1$
			          } else if (colorString.equalsIgnoreCase("blue")) { //$NON-NLS-1$
			            color = "#0000FF"; //$NON-NLS-1$
			          } else if (colorString.equalsIgnoreCase("cyan")) { //$NON-NLS-1$
			            color = "#00FFFF"; //$NON-NLS-1$
			          } else if (colorString.equalsIgnoreCase("dark-gray")) { //$NON-NLS-1$
			            color = "#A9A9A9"; //$NON-NLS-1$
			          } else if (colorString.equalsIgnoreCase("gray")) { //$NON-NLS-1$
			            color = "#808080"; //$NON-NLS-1$
			          } else if (colorString.equalsIgnoreCase("green")) { //$NON-NLS-1$
			            color = "#008000"; //$NON-NLS-1$
			          } else if (colorString.equalsIgnoreCase("light-gray")) { //$NON-NLS-1$
			            color = "#D3D3D3"; //$NON-NLS-1$
			          } else if (colorString.equalsIgnoreCase("magenta")) { //$NON-NLS-1$
			            color = "#FF00FF"; //$NON-NLS-1$
			          } else if (colorString.equalsIgnoreCase("orange")) { //$NON-NLS-1$
			            color = "#FFA500"; //$NON-NLS-1$
			          } else if (colorString.equalsIgnoreCase("pink")) { //$NON-NLS-1$
			            color = "#FFC0CB"; //$NON-NLS-1$
			          } else if (colorString.equalsIgnoreCase("red")) { //$NON-NLS-1$
			            color = "#FF0000"; //$NON-NLS-1$
			          } else if (colorString.equalsIgnoreCase("white")) { //$NON-NLS-1$
			            color = "#FFFFFF"; //$NON-NLS-1$
			          } else if (colorString.equalsIgnoreCase("yellow")) { //$NON-NLS-1$
			            color = "#FFFF00"; //$NON-NLS-1$
			          } else {
			            color = colorString;
			          }
			        }
			      }
			    }
			    
			    return color;
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
