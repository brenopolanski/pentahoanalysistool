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

package org.pentaho.pat.client;

import org.gwt.mosaic.core.client.util.DelayedRunnable;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwtwidgets.client.util.Location;
import org.gwtwidgets.client.util.WindowUtils;
import org.pentaho.pat.client.i18n.IGuiConstants;
import org.pentaho.pat.client.ui.images.IGuiImages;
import org.pentaho.pat.client.ui.panels.LogoPanel;
import org.pentaho.pat.client.ui.panels.MenuBar;
import org.pentaho.pat.client.util.State;
import org.pentaho.pat.client.util.StyleSheetLoader;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.CubeItem;
import org.pentaho.pat.rpc.dto.IAxis;
import org.pentaho.pat.rpc.dto.enums.DrillType;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The entry point for the whole application
 * 
 * @created Jan 1, 2009
 * @since 0.1
 * @author Tom Barber
 * 
 */
public class Pat implements EntryPoint {

    /**
     * PatImages ImageBundle.
     */
    public static final IGuiImages IMAGES = (IGuiImages) GWT.create(IGuiImages.class);

    /**
     * The current style theme.
     */
    public final static String CUR_THEME = IGuiConstants.STYLE_THEMES[0];

    /**
     * The base style name.
     */
    public static final String DEF_STYLE_NAME = "Pat"; //$NON-NLS-1$

    public static final String CSS = ".css";

    /**
     * The {@link Application}.
     */
    private final Application app;

    /**
     * Global State.
     */
    private static State applicationState = new State();

    private static String currQuery = null;

    private static String currConnection = null;

    private static CubeItem currCube = null;

    private static String currCubeName;
    
    private static String currScenario;
    
    private static IAxis measuresAxis;

    private static DrillType currDrillType = DrillType.POSITION;

    public static String getCurrConnection() {
        return currConnection;
    }

    public final static void setCurrConnection(final String currConnection) {
        Pat.currConnection = currConnection;
    }

    /**
     * Returns the SESSION_ID.
     * 
     * @return SESSION_ID
     */
    public static String getSessionID() {
        return applicationState.getSession();
    }

    /**
     * @return State
     */
    public static State getApplicationState() {
        return applicationState;
    }

    /**
     *TODO JAVADOC
     * 
     * @return the currQuery
     */
    public static String getCurrQuery() {
        return currQuery;
    }

    /**
     * 
     *TODO JAVADOC
     * 
     * @param currQuery
     *            the currQuery to set
     */
    public static void setCurrQuery(final String currQuery) {
        Pat.currQuery = currQuery;
    }

    /**
     * Get the style name of the reference element defined in the current GWT theme style sheet.
     * 
     * @param prefix
     *            the prefix of the reference style name
     * @return the style name
     */
    private static String getCurrentReferenceStyleName(final String prefix) {
        StringBuffer a = new StringBuffer(prefix + "-Reference-" + CUR_THEME);
        if (LocaleInfo.getCurrentLocale().isRTL()) {
            a.append("-rtl");
        }

        return a.toString();
    }

    public Pat() {
        // parse possible parameters
        parseInitialStateFromParameter();
        
        app = new Application();
        assignSessionID(null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
     */
    public final void onModuleLoad() {

        updateStyleSheets();
        new DelayedRunnable() {
            @Override
            public void run() {
                com.google.gwt.user.client.DOM.getElementById("splash").getStyle().setProperty("display", "none"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
        };
    }

    /**
     * Update the style sheets to reflect the current theme and direction.
     */
    public void updateStyleSheets() {
        // Generate the names of the style sheets to include
        // TODO remove un-needed stuff
        String gwtStyleSheet = "gwt/" + CUR_THEME + "/" + CUR_THEME + CSS; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        String gwtMosStyleSheet = "gwt/" + CUR_THEME + "/mosaic.css"; //$NON-NLS-1$ //$NON-NLS-2$
        String scStyleSheet = CUR_THEME + "/Showcase.css"; //$NON-NLS-1$

        final String widgetStyleSheet = "/widgets.css"; //$NON-NLS-1$ 
        final String halogenStyleSheet = "/halogen.css"; //$NON-NLS-1$ 
        if (LocaleInfo.getCurrentLocale().isRTL()) {
            gwtStyleSheet = gwtStyleSheet.replace(".css", "_rtl.css"); //$NON-NLS-1$ //$NON-NLS-2$
            gwtMosStyleSheet = gwtMosStyleSheet.replace(".css", //$NON-NLS-1$
                    "_rtl.css"); //$NON-NLS-1$
            scStyleSheet = scStyleSheet.replace(".css", "_rtl.css"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        // Find existing style sheets that need to be removed
/*        boolean styleSheetsFound = false;
        final HeadElement headElem = StyleSheetLoader.getHeadElement();
        final List<Element> toRemove = new ArrayList<Element>();
        final NodeList<Node> children = headElem.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            final Node node = children.getItem(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                final Element elem = Element.as(node);
                if (elem.getTagName().equalsIgnoreCase("link") //$NON-NLS-1$
                        && elem.getPropertyString("rel").equalsIgnoreCase(//$NON-NLS-1$
                                "stylesheet")) { //$NON-NLS-1$
                    styleSheetsFound = true;
                    final String href = elem.getPropertyString("href"); //$NON-NLS-1$
                    // If the correct style sheets are already loaded, then we
                    // should have
                    // nothing to remove.
                    if (!href.contains(gwtStyleSheet) && !href.contains(gwtMosStyleSheet)
                            && !href.contains(scStyleSheet)) {
                        toRemove.add(elem);
                    }
                }
            }
        }

        // Return if we already have the correct style sheets
        if (styleSheetsFound && toRemove.isEmpty()) {
            return;
        }
        // Detach the app while we manipulate the styles to avoid rendering
        // issues
         
         
         RootPanel.get().remove(app);
       // app.removeFromParent();

        // Remove the old style sheets
        for (final Element elem : toRemove) {
            headElem.removeChild(elem);
        }
        */
        // Load the GWT theme style sheet
        final String modulePath = GWT.getModuleBaseURL();
        final Command callback = new Command() {
            public void execute() {

                // Different themes use different background colors for the body
                // element, but IE only changes the background of the visible
                // content
                // on the page instead of changing the background color of the
                // entire
                // page. By changing the display style on the body element, we
                // force
                // IE to redraw the background correctly.
                RootPanel.getBodyElement().getStyle().setProperty("display", "none"); //$NON-NLS-1$ //$NON-NLS-2$
                RootPanel.getBodyElement().getStyle().setProperty("display", ""); //$NON-NLS-1$ //$NON-NLS-2$
                app.attach();
                LogoPanel.spinWheel(false);
            }
        };

        StyleSheetLoader.loadStyleSheet(modulePath + gwtStyleSheet, getCurrentReferenceStyleName("gwt"), callback); //$NON-NLS-1$
        StyleSheetLoader
                .loadStyleSheet(modulePath + gwtMosStyleSheet, getCurrentReferenceStyleName("mosaic"), callback); //$NON-NLS-1$
        // Load the showcase specific style sheet after the GWT & Mosaic theme
        // style
        // sheet so that custom styles supercede the theme styles.
        StyleSheetLoader.loadStyleSheet(modulePath + scStyleSheet,
                getCurrentReferenceStyleName("Application"), callback); //$NON-NLS-1$
        StyleSheetLoader.loadStyleSheet(modulePath + widgetStyleSheet,
                getCurrentReferenceStyleName("widgets"), callback); //$NON-NLS-1$
        StyleSheetLoader.loadStyleSheet(modulePath + halogenStyleSheet,
                getCurrentReferenceStyleName("halogen"), callback); //$NON-NLS-1$

    }

    /**
     * Sets the SESSION_ID.
     */
    private void assignSessionID(final String session) {
        if (session == null && Pat.getSessionID() == null) {
            ServiceFactory.getSessionInstance().createSession(new AsyncCallback<String>() {
                public void onFailure(final Throwable exception) {
                    MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                            .failedSessionID(exception.getLocalizedMessage()));
                }

                public void onSuccess(final String sessionId) {
                    applicationState.setSession(sessionId);
                    setupPat();
                    Application.setupActiveQueries();
                    
                }
            });
        } else {
            applicationState.setSession(session);
        }
    }

    /** 
     * This hackery has to be done so we get to the /pentaho context where our 
     * servlets lives (/pentaho/...).  If we don't parse out the plugin-related
     * parts of the module url,  the GWT client code will wrongly POST to /pentaho/content/pat-res/pat/servlet.
     * 
     * @return the true URL to the rpc service
     */
    public static String getBaseUrl() {
      String moduleUrl = GWT.getModuleBaseURL();
      
      //
      //Set the base url appropriately based on the context in which we are running this client
      //
      if(moduleUrl.indexOf("content") > -1) {
        //we are running the client in the context of a BI Server plugin, so 
        //point the request to the GWT rpc proxy servlet
        String baseUrl = moduleUrl.substring(0, moduleUrl.indexOf("content"));
        //NOTE: the dispatch URL ("gechoService") must match the bean id for 
        //this service object in your plugin.xml.  "gwtrpc" is the servlet 
        //that handles plugin gwt rpc requests in the BI Server.
        return  baseUrl + "gwtrpc/";
      }
      //we are running this client in hosted mode, so point to the servlet 
      //defined in war/WEB-INF/web.xml
      return moduleUrl;
    }

    /**
     * Parses possible Parameters and sets initial State.
     */
    private void parseInitialStateFromParameter() {
        final Location loadURL = WindowUtils.getLocation();
        final State.Mode mode = State.Mode.getModeByParameter(loadURL.getParameter("MODE")); //$NON-NLS-1$
        if (mode == null) {
            applicationState.setMode(State.Mode.DEFAULT);
        } else {
            applicationState.setMode(mode);
        }
        final String _sessionParam = loadURL.getParameter("SESSION"); //$NON-NLS-1$
        applicationState.setSession(_sessionParam);

    }
    
    private void setupPat() {
        MenuBar.enableConnect(true);
        MenuBar.enableCube(true);
        MenuBar.enableLoad(true);
        
    }
    public static void saveQueryToSolution(final String solution, final String path,final String name,final String localizedFileName) {
        ServiceFactory.getQueryInstance().saveQuery(Pat.getSessionID(), Pat.getCurrQuery(), name,
                Pat.getCurrConnection(), Pat.getCurrCube(), Pat.getCurrCubeName(), new AsyncCallback<Object>() {

                    public void onFailure(final Throwable arg0) {
                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                .failedSaveQuery(arg0.getLocalizedMessage()));
                    }

                    public void onSuccess(final Object arg0) {
                        ServiceFactory.getPlatformInstance().saveQueryToSolution(getSessionID(), getCurrQuery(), getCurrConnection(), solution, path, name, localizedFileName, new AsyncCallback<Object>() {

                            public void onFailure(Throwable arg0) {
                                MessageBox.info(ConstantFactory.getInstance().error(), "ERROR");
                                
                            }

                            public void onSuccess(Object arg0) {
                                MessageBox.info("Success", "File Saved");
                                refreshSolutionRepo();
                            }
                            
                        });

                    }

        });
    }


    public static void saveQueryAsCda(final String solution, final String path,final String name,final String localizedFileName) {
        ServiceFactory.getPlatformInstance().saveQueryAsCda(getSessionID(), getCurrQuery(), getCurrConnection(), solution, path, name, localizedFileName, new AsyncCallback<Object>() {

            public void onFailure(Throwable arg0) {
                MessageBox.info(ConstantFactory.getInstance().error(), "ERROR");

            }

            public void onSuccess(Object arg0) {
                MessageBox.info("Success", "File Saved");
                refreshSolutionRepo();
            }

        });


    }
    
    public static native void refreshSolutionRepo()
    /*-{
    if (typeof top.mantle_initialized != "undefined" && top.mantle_initialized == true) {
        top.mantle_refreshRepository();
        }
    }-*/;
    
    public static Boolean isPlugin() {
        // TODO maybe add a more secure way to check if PAT is running as plugin or standalone
        String moduleUrl = GWT.getModuleBaseURL();
        if(moduleUrl.indexOf("content") > -1) {
         return true;
        }
        return false;
    }
    
    public static void executeQuery(final Widget sender, final String queryId ) {
        ServiceFactory.getQueryInstance().executeQuery(Pat.getSessionID(),queryId,
                new AsyncCallback<CellDataSet>() {

                    public void onFailure(final Throwable arg0) {
                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                .failedQuery(arg0.getLocalizedMessage()));
                    }

                    public void onSuccess(final CellDataSet result1) {
                        GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryExecuted(
                                sender, queryId, result1);
                    }

                });

    }
    /**
     *TODO JAVADOC
     * 
     * @param cubeName
     */
    public final static void setCurrCubeName(final String cubeName) {

        currCubeName = cubeName;

    }

    public static String getCurrCubeName() {

        return currCubeName;
    }

    public final static void setCurrCube(final CubeItem cube) {

        currCube = cube;
    }

    public static CubeItem getCurrCube() {
        return currCube;
    }

    public final static void setDrillType(final DrillType drillType) {
        currDrillType = drillType;

    }

    public static DrillType getCurrDrillType() {
        return currDrillType;

    }

    public static void setCurrScenario(String scenario) {
	currScenario = scenario;
	
    }
    
    public static String getCurrScenario(){
	return currScenario;
    }

    public static void setMeasuresAxis(IAxis measuresDimension) {
	Pat.measuresAxis = measuresDimension;
    }

    public static IAxis getMeasuresAxis() {
	return measuresAxis;
    }

}
