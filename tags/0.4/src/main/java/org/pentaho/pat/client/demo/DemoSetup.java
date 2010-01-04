package org.pentaho.pat.client.demo;

import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.panels.ConnectionManagerPanel;
import org.pentaho.pat.client.ui.panels.MainMenu;
import org.pentaho.pat.client.ui.panels.MainTabPanel;
import org.pentaho.pat.client.ui.panels.QueryPanel;
import org.pentaho.pat.client.ui.panels.MainMenu.MenuItem;
import org.pentaho.pat.client.ui.panels.QueryPanel.QueryMode;
import org.pentaho.pat.client.util.ConnectionItem;
import org.pentaho.pat.client.ui.windows.ConnectionWindow;
import org.pentaho.pat.client.util.State;
import org.pentaho.pat.client.util.State.Mode;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.rpc.dto.CubeConnection.ConnectionType;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class DemoSetup {

	private State.Mode demoMode;
	private String session;
	
	public DemoSetup(State.Mode demoMode, String session) {
		this.demoMode = demoMode;
		this.session = session;
		if (demoMode != State.Mode.DEFAULT) {
			CubeConnection demoConnection = getDemoConnection();
			connect(demoConnection);
			
		}
	}

	private CubeConnection getDemoConnection() {
		CubeConnection cc = new CubeConnection(ConnectionType.XMLA);
		cc.setName("demo Connection 1"); //$NON-NLS-1$
		cc.setUrl("http://localhost:8080/pentaho/Xmla"); //$NON-NLS-1$
		cc.setUsername("joe"); //$NON-NLS-1$
		cc.setPassword("password"); //$NON-NLS-1$
		return cc;	
	}

	private boolean connect(final CubeConnection cc) {
		ServiceFactory.getSessionInstance().connect(Pat.getSessionID(), cc, new AsyncCallback<Object>() {
			public void onFailure(final Throwable arg0) {
			}

			public void onSuccess(final Object o) {
				GlobalConnectionFactory.getInstance().getConnectionListeners().fireConnectionMade(new Widget());
				if (demoMode.isShowConnections()) {
					ConnectionManagerPanel.addConnection(new ConnectionItem("123",getDemoConnection().getName(),true));
				}

				if (demoMode == State.Mode.ONECUBE) {
					setupDimensionMenu();
				}
				if (demoMode == Mode.OLAPTABLE) {
					String mdx = "select NON EMPTY Crossjoin(Hierarchize(Union({[Region].[All Regions]}, [Region].[All Regions].Children)), {[Measures].[Actual]}) ON COLUMNS," + //$NON-NLS-1$
							" NON EMPTY Hierarchize(Union({[Department].[All Departments]}, [Department].[All Departments].Children)) ON ROWS " + //$NON-NLS-1$
							" from [Quadrant Analysis] "; //$NON-NLS-1$
					ServiceFactory.getQueryInstance().executeMdxQuery(Pat.getSessionID(), mdx, new AsyncCallback<CellDataSet>() {

					public void onFailure(Throwable arg0) {
						
					}

					public void onSuccess(CellDataSet matrix) {
						GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryExecuted(new ConnectionWindow(), "demoQuery", matrix); //$NON-NLS-1$
						
					}
					
				});

				}
			}
		});

		return true;
	}
	
	private void setupDimensionMenu() {
		final QueryPanel widget = new QueryPanel("Demo Query Quadrant Analysis"); //$NON-NLS-1$
		 widget.setCube("Quadrant Analysis"); //$NON-NLS-1$
		ServiceFactory.getSessionInstance().setCurrentCube(Pat.getSessionID(), "Quadrant Analysis", new AsyncCallback<Object>() { //$NON-NLS-1$
			public void onFailure(final Throwable arg0) { }

			public void onSuccess(final Object setCurrentCubeResult) {
				ServiceFactory.getQueryInstance().createNewQuery(Pat.getSessionID(), new AsyncCallback<String>() {
					public void onFailure(final Throwable arg0) { 	}
					public void onSuccess(final String newQuery) {
						widget.setQuery(newQuery);
						ServiceFactory.getQueryInstance().setCurrentQuery(Pat.getSessionID(), newQuery, new AsyncCallback<Object>() {
							public void onFailure(final Throwable arg0) { }
							public void onSuccess(final Object arg0) {
								// TODO change way of accessing other widget elements
								widget.setSelectedQueryMode(QueryMode.QUERY_MODEL);
								MainMenu.getDimensionPanel().createDimensionList();
								MainMenu.getDimensionPanel().layout();
								MainTabPanel.displayContentWidget(widget);
								MainMenu.showNamedMenu(MenuItem.Dimensions);
								MainMenu.getStackPanel().layout();
							}
						});
					}
				});
			}
		});
	}


}