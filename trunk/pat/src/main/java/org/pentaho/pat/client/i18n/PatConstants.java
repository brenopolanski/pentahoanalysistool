/*
 * Copyright (C) 2009 Tom Barber
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA *
 *
 * @created Apr 23, 2009 
 * @author Tom Barber
 */

package org.pentaho.pat.client.i18n;

import com.google.gwt.i18n.client.Constants;

/**
 * Constants for Pat.
 */

public interface PatConstants extends Constants {

	/** The Constant CURRENT_QUERY_NAME. */
	String CURRENT_QUERY_NAME = "current-query-name"; //$NON-NLS-1$

	/** The Constant CURRENT_CUBE_NAME. */
	String CURRENT_CUBE_NAME = "current-cube-name"; //$NON-NLS-1$

	/** The STYLE_THEMES. */
	String[] STYLE_THEMES = {"aegean" ,"standard", "chrome", "dark"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	/**
	 * Availablecubes.
	 *
	 * @return the string
	 */
	String availableCubes();

	/**
	 * Translated "Bottom".
	 *
	 * @return translated "Bottom"
	 * bottom
	 */
	String bottom();

	/**
	 * Translated "Cancel".
	 *
	 * @return translated "Cancel"
	 * cancel
	 */
	String cancel();

	/**
	 * Catalog.
	 *
	 * @return the string
	 */
	String catalog();

	/**
	 * Translated "Chart".
	 *
	 * @return translated "Chart"
	 * chart
	 */
	String chart();

	/**
	 * Translated "Chart Height:".
	 *
	 * @return translated "Chart Height:"
	 * chart_height
	 */
	String chartHeight();

	/**
	 * Translated "Chart Properties...".
	 *
	 * @return translated "Chart Properties..."
	 * chart_properties
	 */
	String chartProperties();

	/**
	 * Translated "Chart Title:".
	 *
	 * @return translated "Chart Title:"
	 * chart_title
	 */
	String chartTitle();

	/**
	 * Translated "Chart Width:".
	 *
	 * @return translated "Chart Width:"
	 * chart_width
	 */
	String chartWidth();

	/**
	 * Checkerrorlog.
	 *
	 * @return the string
	 */
	String checkErrorLog();

	/**
	 * Translated "Children".
	 *
	 * @return translated "Children"
	 * children
	 */
	String children();

	/**
	 * Translated "Clear".
	 *
	 * @return translated "Clear"
	 * clear_selections
	 */
	String clearSelections();

	/**
	 * Translated "Column Dimensions".
	 *
	 * @return translated "Column Dimensions"
	 * column_dimensions
	 */
	String columnDimensions();

	/**
	 * Columns.
	 *
	 * @return the string
	 */
	String columns();

	/**
	 * Connect.
	 *
	 * @return the string
	 */
	String connect();

	/**
	 * Translated "Connection".
	 *
	 * @return translated "Connection"
	 * connection
	 */
	String connection();

	/**
	 * Connectionestablished.
	 *
	 * @return the string
	 */
	String connectionEstablished();
	/* <Messages used by PAT> */
	/**
	 * Connectionstring.
	 *
	 * @return the string
	 */
	String connectionString();

	/**
	 * Translated "Cube".
	 *
	 * @return translated "Cube"
	 * cube
	 */
	String cube();

	/**
	 * Cubes.
	 *
	 * @return the string
	 */
	String cubes();

	/**
	 * Data.
	 *
	 * @return the string
	 */
	String data();

	/**
	 * Database.
	 *
	 * @return the string
	 */
	String database();

	/**
	 * Translated "Dimension Panel".
	 *
	 * @return translated "Dimension Panel"
	 * dimensionPanel
	 */
	String dimensionPanel();

	/**
	 * Translated "Dimensions".
	 *
	 * @return translated "Dimensions"
	 * dimensions
	 */
	String dimensions();

	/**
	 * Disconnect.
	 *
	 * @return the string
	 */
	String disconnect();

	/**
	 * Translated "Drill Panel".
	 *
	 * @return translated "Drill Panel"
	 * drillPanel
	 */
	String drillPanel();

	/**
	 * Error.
	 *
	 * @return the string
	 */
	String error();




	/**
	 * Translated "Execute MDX".
	 *
	 * @return translated "Execute MDX"
	 * execute_mdx
	 */
	String executeMdx();

	/**
	 * Translated "Execute Query Model".
	 *
	 * @return translated "Execute Query Model"
	 * execute_query
	 */
	String executeQuery();

	/**
	 * Translated "File".
	 *
	 * @return translated "File"
	 * file
	 */
	String file();

	/**
	 * Fileuploadfailed.
	 *
	 * @return the string
	 */
	String fileUploadFailed();

	/**
	 * Fileuploadnofile.
	 *
	 * @return the string
	 */
	String fileUploadNoFile();

	/**
	 * Filter.
	 *
	 * @return the string
	 */
	String filter();

	/**
	 * Translated "Filter Dimensions".
	 *
	 * @return translated "Filter Dimensions"
	 * filter_dimensions
	 */
	String filterDimensions();

	/**
	 * Translated "Grid".
	 *
	 * @return translated "Grid"
	 * grid
	 */
	String grid();

	/**
	 * Translated "Group Headers".
	 *
	 * @return translated "Group Headers"
	 * group_headers
	 */
	String groupHeaders();




	/**
	 * Translated "Hide Parents".
	 *
	 * @return translated "Hide Parents"
	 * hide_parents
	 */
	String hideParents();

	/**
	 * Home.
	 *
	 * @return the string
	 */
	String home();

	/**
	 * Translated "Include Children".
	 *
	 * @return translated "Include Children"
	 * include_children
	 */
	String includeChildren();

	/**
	 * Translated "jdbc_driver".
	 *
	 * @return translated "jdbc_driver"
	 * jdbc_driver
	 */
	String jdbcDriver();

	/**
	 * Translated "jdbc_url".
	 *
	 * @return translated "jdbc_url"
	 * jdbc_url
	 */
	String jdbcUrl();

	/**
	 * Translated "Left".
	 *
	 * @return translated "Left"
	 * left
	 */
	String left();


	/**
	 * Translated "Location:".
	 *
	 * @return translated "Location:"
	 * location
	 */
	String location();

	/**
	 * Translated "mainLinkHomepage".
	 *
	 * @return translated "mainLinkHomepage"
	 * mainLinkHomepage
	 */
	String mainLinkHomepage();

	/**
	 * Translated "Main Link Pat".
	 *
	 * @return translated "Main Link Pat"
	 * mainLinkPat
	 */
	String mainLinkPat();

	/**
	 * Translated "Main Sub Title".
	 *
	 * @return translated "Main Sub Title"
	 * mainSubTitle
	 */
	String mainSubTitle();

	/**
	 * Translated "Main Title".
	 *
	 * @return translated "Main Title"
	 * mainTitle
	 */
	String mainTitle();

	/**
	 * Translated "MDX/Filter Panel".
	 *
	 * @return translated "MDX/Filter Panel"
	 * mdxPanel
	 */
	String mdxPanel();

	/**
	 * Translated "MDX Query:".
	 *
	 * @return translated "MDX Query:"
	 * mdx_query
	 */
	String mdxQuery();

	/**
	 * Translated "Member".
	 *
	 * @return translated "Member"
	 * member
	 */
	String member();

	/**
	 * Mondrian.
	 *
	 * @return the string
	 */
	String mondrian();

	/**
	 * Translated "Move To Column".
	 *
	 * @return translated "Move To Column"
	 * move_to_column
	 */
	String moveToColumn();

	/**
	 * Translated "Move To Filter".
	 *
	 * @return translated "Move To Filter"
	 * move_to_filter
	 */
	String moveToFilter();

	/**
	 * Translated "Move To Row".
	 *
	 * @return translated "Move To Row"
	 * move_to_row
	 */
	String moveToRow();

	/**
	 * Translated "Next".
	 *
	 * @return translated "Next"
	 * next
	 */
	String next();

	/**
	 * Translated"Connection could not be established.  Either you did not press the connect button, or there is a problem with your connection string."
	 *
	 * @return translated"Connection could not be established.  Either you did not press the connect button, or there is a problem with your connection string."
	 * no_connection
	 */
	String noConnection();



	/**
	 * Translated "No Data Available.  Query may be invalid.".
	 *
	 * @return translated "No Data Available.  Query may be invalid."
	 * no_data
	 */
	String noData();

	/**
	 * Nojdbcdriverfound.
	 *
	 * @return the string
	 */
	String noJdbcDriverFound();

	/**
	 * Translated "null".
	 *
	 * @return translated "null"
	 * null_value
	 */
	String nullValue();

	/**
	 * Translated "OK".
	 *
	 * @return translated "OK"
	 * ok
	 */
	String ok();

	/**
	 * Translated "OLAP Chart".
	 *
	 * @return translated "OLAP Chart"
	 * olap_chart
	 */
	String olapChart();


	/**
	 * Password.
	 *
	 * @return the string
	 */
	String password();

	/**
	 * Translated "Pat Homepage".
	 *
	 * @return translated "Pat Homepage"
	 * pat_homepage
	 */
	String patHomepage();

	/**
	 * Translated "Pentaho Homepage".
	 *
	 * @return translated "Pentaho Homepage"
	 * pentaho_homepage
	 */
	String pentahoHomepage();

	/**
	 * Port.
	 *
	 * @return the string
	 */
	String port();

	/**
	 * Registernewconnection.
	 *
	 * @return the string
	 */
	String registerNewConnection();

	/**
	 * Registernewmondrianconnection.
	 *
	 * @return the string
	 */
	String registerNewMondrianConnection();

	/**
	 * Registernewxmlaconnection.
	 *
	 * @return the string
	 */
	String registerNewXmlaConnection();

	/**
	 * Translated "Report".
	 *
	 * @return translated "Report"
	 * report
	 */
	String report();


	/**
	 * Translated "Right".
	 *
	 * @return translated "Right"
	 * right
	 */
	String right();

	/* </Messages used by PAT> */
	/**
	 * Translated "Row Dimensions".
	 *
	 * @return translated "Row Dimensions"
	 * row_dimensions
	 */
	String rowDimensions();

	/**
	 * Rows.
	 *
	 * @return the string
	 */
	String rows();

	/**
	 * Schemafile.
	 *
	 * @return the string
	 */
	String schemaFile();

	/**
	 * Translated "Select Cube".
	 *
	 * @return translated "Select Cube"
	 * select_cube
	 */
	String selectCube();

	/**
	 * Translated "Selections".
	 *
	 * @return translated "Selections"
	 * selections
	 */
	String selections();

	/**
	 * Server.
	 *
	 * @return the string
	 */
	String server();

	/**
	 * Translated "Show Parents".
	 *
	 * @return translated "Show Parents"
	 * show_parents
	 */
	String showParents();

	/**
	 * Translated "Siblings".
	 *
	 * @return translated "Siblings"
	 * siblings
	 */
	String siblings();

	/**
	 * Success.
	 *
	 * @return the string
	 */
	String success();

	/**
	 * Translated "Swap Axis".
	 *
	 * @return translated "Swap Axis"
	 * swap_axis
	 */
	String swapAxis();

	/**
	 * Translated "Top".
	 *
	 * @return translated "Top"
	 * top
	 */
	String top();

	/**
	 * Translated "Ungroup Headers".
	 *
	 * @return translated "Ungroup Headers"
	 * ungroup_headers
	 */
	String unGroupHeaders();

	/**
	 * Unused.
	 *
	 * @return the string
	 */
	String unused();

	/**
	 * Upload.
	 *
	 * @return the string
	 */
	String upload();

	/**
	 * Translated "Use MDX".
	 *
	 * @return translated "Use MDX"
	 * use_mdx
	 */
	String useMdx();

	/**
	 * Username.
	 *
	 * @return the string
	 */
	String username();

	/**
	 * Translated "Use Schema Navigator".
	 *
	 * @return translated "Use Schema Navigator"
	 * use_schema_nav
	 */
	String useSchemaNav();

	/**
	 * Translated "Visible:".
	 *
	 * @return translated "Visible:"
	 * visible
	 */
	String visible();

	/**
	 * Welcome.
	 *
	 * @return the string
	 */
	String welcome();

	/**
	 * Xmla.
	 *
	 * @return the string
	 */
	String xmla();

	/**
	 * Xmlaurl.
	 *
	 * @return the string
	 */
	String xmlaUrl();

	String theme();
}
