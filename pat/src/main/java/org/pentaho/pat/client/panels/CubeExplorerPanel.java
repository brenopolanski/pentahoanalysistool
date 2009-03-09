/**
 * 
 */
package org.pentaho.pat.client.panels;



import java.util.ArrayList;
import java.util.List;

import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.ListBox;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.list.DefaultListModel;
import org.pentaho.pat.client.events.SourcesConnectionEvents;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.listeners.ConnectionListenerCollection;
import org.pentaho.pat.client.util.FlexTableCellDragController;
import org.pentaho.pat.client.util.GuidFactory;
import org.pentaho.pat.client.util.ServiceFactory;
import org.pentaho.pat.client.widgets.DemoFlexTable;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.FastTree;
import com.google.gwt.widgetideas.client.FastTreeItem;
import com.google.gwt.widgetideas.client.HasFastTreeItems;


/**
 * @author Tom Barber
 * 
 */
public class CubeExplorerPanel extends CaptionLayoutPanel implements ConnectionListener,
SourcesConnectionEvents  {

	private static final String AXIS_NONE = "none"; //$NON-NLS-1$
	private static final String AXIS_UNUSED = "UNUSED"; //$NON-NLS-1$
	private static final String AXIS_FILTER = "FILTER"; //$NON-NLS-1$
	private static final String AXIS_COLUMNS = "COLUMNS"; //$NON-NLS-1$
	private static final String AXIS_ROWS = "ROWS"; //$NON-NLS-1$
	private static final String AXIS_PAGES = "PAGES"; //$NON-NLS-1$
	private static final String AXIS_CHAPTERS = "CHAPTERS"; //$NON-NLS-1$
	private static final String AXIS_SECTIONS = "SECTIONS"; //$NON-NLS-1$
	private static DefaultListModel<String> model;
	private static FastTreeItem a;
	private ConnectionListenerCollection connectionListeners;
	public static FlexTableCellDragController tableRowDragController;

	public CubeExplorerPanel(String text) {
		super(text);
		
	    init();
	}

	public void init() {
		
		 final LayoutPanel vBox = new LayoutPanel(
			        new BoxLayout(Orientation.VERTICAL));
		
			    vBox.setPadding(0);
			    vBox.setWidgetSpacing(0);

	    
			    final FastTree t = new FastTree();
			    a = t.addItem("Available Cubes");

		
			    final ScrollPanel panel = new ScrollPanel();
			    vBox.add(panel);
			    vBox.setPadding(0);
			    panel.add(t);
			    vBox.add(panel, new BoxLayoutData(FillStyle.BOTH));
			   

			    this.add(vBox);
			    
	}
	
	private void populateCubeTree() {
		ServiceFactory.getInstance().getCubes(GuidFactory.getGuid(),
				new AsyncCallback() {
					public void onSuccess(Object result1) {
						if (result1 != null) {
							String[][] cubeNames = (String[][]) result1;
						for (int i = 0; i < cubeNames.length; i++) {
							if (a.getChildCount() > 0)
								a.removeItems();
							
								a.addItem(cubeNames[i][1]);
							}
						}
					}
						public void onFailure(Throwable caught) {
					}
				});

	}
	
	
	public static void populateDimensions() {
		List axis = new ArrayList();
		axis.add(AXIS_NONE);
		axis.add(AXIS_ROWS);
		axis.add(AXIS_COLUMNS);
		axis.add(AXIS_FILTER);
		populateDimensions(axis);
	}

	public static void populateDimensions(List axis) {
		if (axis.contains(AXIS_NONE)) {
			ServiceFactory.getInstance().getDimensions(AXIS_NONE,
					GuidFactory.getGuid(), new AsyncCallback() {
				public void onSuccess(Object result) {
					String[] dimStrs = (String[]) result;
					if (a.getChildCount() > 0)
					a.removeItems();

					for (int j = 0; j < dimStrs.length; j++) {
						a.addItem(dimStrs[j]);
						
					}

				}

				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub

				}

			});
		}
		if (axis.contains(AXIS_ROWS)) {
			ServiceFactory.getInstance().getDimensions(AXIS_ROWS,
					GuidFactory.getGuid(), new AsyncCallback() {

				public void onSuccess(Object result) {
					String[] dimStrs = (String[]) result;
					
						if (dimStrs.length>0){
						int i = dimStrs.length;
						//getDimensionTree(dimStrs[i-1], rowNode);
						}
					
				}

				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
				}
			});
		}

		if (axis.contains(AXIS_COLUMNS)) {
			ServiceFactory.getInstance().getDimensions(AXIS_COLUMNS,
					GuidFactory.getGuid(), new AsyncCallback() {

				public void onSuccess(Object result) {
					String[] dimStrs = (String[]) result;
					
						if (dimStrs.length>0){
						int i = dimStrs.length;
					//	getDimensionTree(dimStrs[i-1], columnNode);
						}
						
				}

				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
				}
			});
		}

		if (axis.contains(AXIS_FILTER)) {
			ServiceFactory.getInstance().getDimensions(AXIS_FILTER,
					GuidFactory.getGuid(), new AsyncCallback() {

				public void onSuccess(Object result) {

				}

				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub

				}
			});
		}
	}

	public void onConnectionBroken(Widget sender) {
		// TODO Auto-generated method stub

	}

	public void onConnectionMade(Widget sender) {
		// TODO Auto-generated method stub
		populateCubeTree();
	}

	public void addConnectionListener(ConnectionListener listener) {
		if (connectionListeners == null) {
			connectionListeners = new ConnectionListenerCollection();
		}
		connectionListeners.add(listener);
	}

	public void removeConnectionListener(ConnectionListener listener) {
		if (connectionListeners != null) {
			connectionListeners.remove(listener);
		}

	}

}
