package org.pentaho.pat.client.deprecated.ui.panels;


import java.util.Iterator;

import org.pentaho.pat.client.deprecated.ui.panels.MainMenu.MenuItem;
import org.pentaho.pat.client.deprecated.ui.widgets.DataWidget;
import org.pentaho.pat.client.deprecated.util.State;
import org.pentaho.pat.client.deprecated.util.factory.ConstantFactory;
import org.pentaho.pat.client.deprecated.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.deprecated.util.factory.ServiceFactory;
import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.DecoratedTabLayoutPanel;
import org.gwt.mosaic.ui.client.HTMLLabel;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.TextLabel;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.FillLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.pentaho.pat.client.Pat;

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
		final LayoutPanel rootLayoutPanel = getLayoutPanel();
		//rootLayoutPanel.setLayout(new BoxLayout(Orientation.HORIZONTAL));
		contentWrapper.addStyleName("pat-content-wrapper"); //$NON-NLS-1$
		contentWrapper.addSelectionHandler(new SelectionHandler<Integer>() {
			public void onSelection(final SelectionEvent<Integer> selectEvent) {
				final Widget widget =contentWrapper.getWidget(selectEvent.getSelectedItem());
				
				if (widget instanceof QueryPanel){
//					ServiceFactory.getSessionInstance().setCurrentCube(Pat.getSessionID(), ((QueryPanel) widget).getCube(), new AsyncCallback<Object>(){
//
//						public void onFailure(final Throwable arg0) {
//							//TODO proper messages 
//							
//							MessageBox.error(ConstantFactory.getInstance().error(), ConstantFactory.getInstance().cube());
//						}
//
//						public void onSuccess(final Object arg0) {
//
//
//							ServiceFactory.getQueryInstance().setCurrentQuery(Pat.getSessionID(), ((QueryPanel) widget).getQuery(), new AsyncCallback<Object>(){
//
//								public void onFailure(final Throwable arg0) {
//									//TODO proper messages
//									MessageBox.error(ConstantFactory.getInstance().error(), ConstantFactory.getInstance().noConnection());
//								}
//
//								public void onSuccess(final Object arg0) {
//									GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryChanged(MainTabPanel.this);
//								}
//
//							});
//
//						}
//
//					});

				}
			}
		});
		
		rootLayoutPanel.add(contentWrapper);
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
		final TextLabel label = new TextLabel(string);
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
				if (contentWrapper.getWidgetCount() == 1 || contentWrapper.getWidgetCount() == 0) {
					if (MainMenu.showNamedMenu(MenuItem.Cubes) == false) {
						MainMenu.getStackPanel().showStack(0);
					}
					MainMenu.getStackPanel().layout();
				}
				counter--;
			}
		});
		hPanel.add(label);
		hPanel.add(new HTMLLabel("&nbsp&nbsp&nbsp")); //$NON-NLS-1$
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

		boolean isWelcomePanel = false;
		if (content != null) {
			if (content instanceof WelcomePanel){
				final Iterator<Widget> iter = contentWrapper.iterator();
				while ( iter.hasNext() ){
					if (iter.next() instanceof WelcomePanel) {
						isWelcomePanel = true;
					}
				}

				if(!isWelcomePanel)
				{
					contentWrapper.add(content, tabName);
					counter++;
					contentWrapper.layout();
				}

			}
			else{
				if (Pat.getApplicationState().getMode().equals(State.Mode.ONECUBE)) {
					contentWrapper.add(content, tabName);
				}
				else {
					contentWrapper.add(content, tabCloseLabel(content, tabName, counter));
				}
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
