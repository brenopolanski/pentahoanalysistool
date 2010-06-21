/*
 * Copyright (C) 2009 Tom Barber
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
package org.pentaho.pat.client.i18n;

import com.google.gwt.i18n.client.Constants;

/**
 * Constants for Pat.
 * 
 * @created Aug 5, 2009
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 */
public interface IGuiConstants extends Constants {

    /** The Constant CURRENT_QUERY_NAME. */
    String CURRENT_QUERY_NAME = "current-query-name"; //$NON-NLS-1$
    /** The Constant CURRENT_CUBE_NAME. */
    String CURRENT_CUBE_NAME = "current-cube-name"; //$NON-NLS-1$
    /** The STYLE_THEMES. */
    String[] STYLE_THEMES = {"aegean", "standard", "chrome", "dark"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

    String sortAscending();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String checkErrorLog();

    /**
     * TODO JAVADOC
     * 
     * @return
     */
    String dimensionFetchFail();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String error();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String fileUpload();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String fileUploadFailed();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String fileUploadNoFile();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String jdbcDriver();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String jdbcUrl();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String mainTitle();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String mondrian();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String name();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String noJdbcDriverFound();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String password();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String registerNewConnection();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String save();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String schemaFile();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String success();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String upload();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String username();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String catalog();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String welcome();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String wiki();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String xmla();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String xmlaUrl();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String dimensions();

    /**
     * *TODO JAVADOC
     * 
     * @return
     */
    String unused();

    /**
     * *TODO JAVADOC
     * 
     * @return
     */
    String rows();

    /**
     * *TODO JAVADOC
     * 
     * @return
     */
    String columns();

    /**
     * *TODO JAVADOC
     * 
     * @return
     */
    String filter();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String properties();

    /**
     * 
     *TODO JAVADOC
     * 
     * @return
     */
    String member();

    /**
     * 
     *TODO JAVADOC
     * 
     * @return
     */
    String children();

    /**
     * 
     *TODO JAVADOC
     * 
     * @return
     */
    String includeChildren();

    /**
     * 
     *TODO JAVADOC
     * 
     * @return
     */
    String siblings();

    /**
     * 
     *TODO JAVADOC
     * 
     * @return
     */
    String clearSelections();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String cancel();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String executeQuery();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String connections();

    String connect();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String cubes();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String disconnect();

    String drillThrough();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String drillReplace();

    String drillUp();
    
    String drillNone();
    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String drillMember();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String drillPosition();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String pivot();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String hideBlankCells();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String showProperties();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String showFilters();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String showParent();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String ancestors();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String descendants();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String close();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String titleCubeBrowser();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String newQuery();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String newMdxQuery();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String titleDimensionBrowser();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String ok();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String sortDescending();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String sortBreakAscending();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String sortBreakDescending();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String sortFailed();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String hierarchizeFailed();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String path();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String pre();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String post();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String chartOptions();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String pieChart();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String barChart();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String lineChart();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String showMDX();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String mdx();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String grid();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String chart();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String top();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String bottom();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String left();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String right();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String load();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String shouldnthappen();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String layout();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String errorDetail();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String off();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String generalOptions();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String barChartOptions();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String glass();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String normal();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String threed();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String titles();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String chartTitle();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String xaxisLabel();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String yaxisLabel();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String legend();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String backgroundColor();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String barStyle();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String yaxisColor();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String yaxisGridColor();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String yaxisMin();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String yaxisMax();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String xaxisColor();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String xaxisGridColor();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String xaxisMin();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String xaxisMax();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String star();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String hollow();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String lineChartOptions();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String dotStyle();

    String sort();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String sortAZ();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String sortZA();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String exclude();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String clearExclusions();

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String warning();

    /**
     *TODO JAVADOC
     *
     * @return
     */
    String export();

    /**
     *TODO JAVADOC
     *
     * @return
     */
    String drillStyle();

    /**
     *TODO JAVADOC
     *
     * @return
     */
    String showBlankCells();

    /**
     *TODO JAVADOC
     *
     * @return
     */
    String connectStartup();

    /**
     *TODO JAVADOC
     *
     * @return
     */
    String measures();

    /**
     *TODO JAVADOC
     *
     * @return
     */
    String flat();

    /**
     *TODO JAVADOC
     *
     * @return
     */
    String hierarchical();

    /**
     *TODO JAVADOC
     *
     * @return
     */
    String logout();

    /**
     *TODO JAVADOC
     *
     * @return
     */
    String validateSchema();

    /**
     *TODO JAVADOC
     *
     * @return
     */
    String manageConnections();

    /**
     *TODO JAVADOC
     *
     * @return
     */
    String newConnection();

    /**
     *TODO JAVADOC
     *
     * @return
     */
    String file();

    /**
     *TODO JAVADOC
     *
     * @return
     */
    String help();

    /**
     *TODO JAVADOC
     *
     * @return
     */
    String about();

    /**
     *TODO JAVADOC
     *
     * @return
     */
    String noConnections();

    /**
     *TODO JAVADOC
     *
     * @return
     */
    String noCubes();

    /**
     *TODO JAVADOC
     *
     * @return
     */
    String openQuery();

    /**
     *TODO JAVADOC
     *
     * @return
     */
     String saveQuery();

     /**
     *TODO JAVADOC
     *
     * @return
     */
     String loading();

     /**
     *TODO JAVADOC
     *
     * @return
     */
     String cubeWindow();

     /**
     *TODO JAVADOC
     *
     * @return
     */
     String drillThroughPanel();
     
     String aboutPat();
     
     String viewSchema();
     
     String noSchema();
     
     String role();

}
