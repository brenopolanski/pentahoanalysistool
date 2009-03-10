/**
 * 
 */
package org.pentaho.pat.client.panels;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.gwt.mosaic.showcase.client.ContentWidget;
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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeImages;
import com.google.gwt.user.client.ui.TreeItem;
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
	

	public static final CubeImages IMAGES = (CubeImages) GWT.create(CubeImages.class);
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
	private static TreeItem rootNode;
	public CubeExplorerPanel(String text) {
		super(text);
		
	    init();
	}
	  /**
	   * Images used in the {@link Application}.
	   */
	  public interface CubeTreeImages extends TreeImages {
	    /**
	     * An image indicating a leaf.
	     * 
	     * @return a prototype of this image
	     */
	    @Resource("noimage.png")
	    AbstractImagePrototype treeLeaf();
	  }
	  
	  /**
	   * A mapping of history tokens to their associated menu items.
	   */
	  private Map<String, TreeItem> itemTokens = new HashMap<String, TreeItem>();

	  /**
	   * A mapping of menu items to the widget display when the item is selected.
	   */
	  private Map<TreeItem, ContentWidget> itemWidgets = new HashMap<TreeItem, ContentWidget>();

	public void init() {
		
		 final LayoutPanel vBox = new LayoutPanel(
			        new BoxLayout(Orientation.VERTICAL));
		
			    vBox.setPadding(0);
			    vBox.setWidgetSpacing(0);

			    CubeTreeImages treeImages = GWT.create(CubeTreeImages.class);
			    final Tree t = new Tree(treeImages);
			    t.setAnimationEnabled(true);
			    t.addStyleName("cube-menu");
			    
			    rootNode = t.addItem("Available Cubes");
			    
		
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
	  /**
	   * Add an option to the main menu.
	   * 
	   * @param parent the {@link TreeItem} that is the option
	   * @param content the {@link ContentWidget} to display when selected
	   * @param image the icon to display next to the {@link TreeItem}
	   */
	  private static void setupMainMenuOption(TreeItem parent, String name,
	      AbstractImagePrototype image) {
	    // Create the TreeItem
	    TreeItem option = parent.addItem(image.getHTML() + " " + name);

	    // Map the item to its history token and content widget
	    itemWidgets.put(option, content);
	    itemTokens.put(getContentWidgetToken(content), option);
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
						//a.addItem(dimStrs[j]);
						setupMainMenuOption(rootNode, dimStrs[j],
						        IMAGES.calendar());		
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
