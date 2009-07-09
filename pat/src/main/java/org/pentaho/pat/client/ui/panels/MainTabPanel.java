package org.pentaho.pat.client.ui.panels;

import java.util.Iterator;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.DecoratedTabLayoutPanel;
import org.gwt.mosaic.ui.client.HTML;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.Label;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.pentaho.pat.client.Application;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.panels.MainMenu.MenuItem;
import org.pentaho.pat.client.ui.widgets.DataWidget;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MainTabPanel extends LayoutComposite {
	
	private final static DecoratedTabLayoutPanel contentWrapper = new DecoratedTabLayoutPanel();
	private static int counter = 0;
	/** The base style name. */
	public static final String DEF_STYLE_NAME = "Application"; //$NON-NLS-1$


	
	public MainTabPanel() {
		super();
		final LayoutPanel baseLayoutPanel = getLayoutPanel();
		
		contentWrapper.addStyleName(DEF_STYLE_NAME + "-content-wrapper"); //$NON-NLS-1$
		contentWrapper.addSelectionHandler(new SelectionHandler<Integer>() {
			public void onSelection(final SelectionEvent<Integer> selectEvent) {
				final Widget widget =contentWrapper.getWidget(selectEvent.getSelectedItem());
				if (widget instanceof QueryPanel){
					Application.getMenuPanel().showNamedMenu(MenuItem.Dimensions);
					ServiceFactory.getSessionInstance().setCurrentCube(Pat.getSessionID(), ((QueryPanel) widget).getCube(), new AsyncCallback<Object>(){

						public void onFailure(final Throwable arg0) {
							//TODO proper messages
							MessageBox.error("Balls", "Couldn't set the cube");
						}

						public void onSuccess(final Object arg0) {


							ServiceFactory.getQueryInstance().setCurrentQuery(Pat.getSessionID(), ((QueryPanel) widget).getQuery(), new AsyncCallback<Object>(){

								public void onFailure(final Throwable arg0) {
									//TODO proper messages
									MessageBox.error("Balls", "Couldn't set the query");
								}

								public void onSuccess(final Object arg0) {
									GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryChanged(MainTabPanel.this);
								}

							});

						}

					});

				}
				if (widget instanceof WelcomePanel){
					//mainPanel.showMenu(0);
				}


			}
		});
		
		baseLayoutPanel.add(contentWrapper);
	}

	/**
	 * 
	 * Creates a new Closeable tab.
	 *
	 * @param widget
	 * @param string
	 * @param index
	 * @return a closeable tab for a tab panel.
	 */
	private static Widget tabCloseLabel(final Widget widget, final String string, final int index) {
		final HorizontalPanel hPanel = new HorizontalPanel();
		final Label label = new Label(string);
		DOM.setStyleAttribute(label.getElement(), "whiteSpace", "nowrap"); //$NON-NLS-1$ //$NON-NLS-2$
		final ImageButton closeBtn = new ImageButton(Pat.IMAGES.closeButton());
		closeBtn.addClickHandler(new ClickHandler() {
			public void onClick(final ClickEvent event) {
				final int widgetIndex = contentWrapper.getWidgetIndex(widget);
				if (widgetIndex == contentWrapper.getSelectedTab()) {
					contentWrapper.remove(widgetIndex);
					contentWrapper.selectTab(widgetIndex - 1);
				} else {
					contentWrapper.remove(widgetIndex);
					contentWrapper.layout();
				}
				if (contentWrapper.getWidgetCount() == 1) {
					if (Application.getMenuPanel().showNamedMenu(MenuItem.Cubes) == false) 
						Application.getMenuPanel().getStackPanel().showStack(0);
					Application.getMenuPanel().getStackPanel().layout();
				}
				counter--;
			}
		});
		hPanel.add(label);
		hPanel.add(new HTML("&nbsp&nbsp&nbsp")); //$NON-NLS-1$
		hPanel.add(closeBtn);
		return hPanel;
	}
	
	/**
	 * Adds a new Tab to the contentPanel.
	 *
	 * @param content
	 * @param tabName
	 */
	public static void addContent(final DataWidget content, final String tabName) {

		boolean test = false;
		if (content != null) {
			if (content instanceof WelcomePanel){
				final Iterator iter = contentWrapper.iterator();
				while ( iter.hasNext() ){
					if (iter.next() instanceof WelcomePanel) {
						test = true;
					}
				}

				if(!test)
				{
					contentWrapper.add(content, tabName);
					counter++;
					contentWrapper.layout();
				}

			}
			else{
				contentWrapper.add(content, tabCloseLabel(content, tabName, counter));
				contentWrapper.selectTab(counter);
				counter++;
				contentWrapper.layout();

			}

		}
	}
	
	protected static DataWidget copyMatrix(final DataWidget source, DataWidget destination) {

		if (source != null) {

			if (source instanceof WelcomePanel) {
				destination = new WelcomePanel();
				final String name = source.getName();
				((WelcomePanel) destination).setName(name);
			}

			else if (source instanceof QueryPanel) {

				destination = new QueryPanel();
				((QueryPanel) destination).setName(((QueryPanel) source).getName());
				((QueryPanel) destination).setCube(((QueryPanel) source).getCube());
				((QueryPanel) destination).setQuery(((QueryPanel) source).getQuery());
			}
		}
		return destination;
	}

	public static void displayContentWidget(final DataWidget content) {
		if (content != null) {
			if (!content.isInitialized()) {
				content.initialize();
			}
			DataWidget contentdupe = null;
			contentdupe = copyMatrix(content, contentdupe);
			addContent(contentdupe, content.getName());
		}
	}
}
