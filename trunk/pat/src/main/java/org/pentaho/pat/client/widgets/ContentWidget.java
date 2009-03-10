/*
 * Copyright 2008 Google Inc.
 * 
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.pentaho.pat.client.widgets;

import java.util.HashMap;
import java.util.Map;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.DecoratedTabLayoutPanel;
import org.gwt.mosaic.ui.client.StackLayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.util.ConstantFactory;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.HasDirection;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ResizableWidget;
import com.google.gwt.widgetideas.client.ResizableWidgetCollection;

/**
 * A widget used to show the examples in the content panel. It includes a tab
 * bar with options to view the example, view the source, or view the css style
 * rules.
 * <p>
 * This widget uses a lazy initialization mechanism so that the content is not
 * rendered until the onInitialize method is called, which happens the first
 * time the widget is added to the page. The data in the source and css tabs are
 * loaded using RPC call to the server.
 * 
 * <h3>CSS Style Rules</h3>
 * <ul class="css">
 * <li>.mosaic-sc-Page { Applied to the entire widget }</li>
 * <li>.sc-ContentWidget-tabBar { Applied to the TabBar }</li>
 * <li>.sc-ContentWidget-deckPanel { Applied to the DeckPanel }</li>
 * <li>.sc-ContentWidget-name { Applied to the name }</li>
 * <li>.sc-ContentWidget-description { Applied to the description }</li> </ul>
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public abstract class ContentWidget extends LayoutPanel {

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLE_NAME = "mosaic-sc-ContentWidget";

  /**
   * The static loading image displayed when loading CSS or source code.
   */
  private static String loadingImage;

  /**
   * The stack panel with the contents.
   */
  private StackLayoutPanel stackPanel;
  
  /**
   * A boolean indicating whether or not this widget has been initialized.
   */
  private boolean initialized = false;

  /**
   * A boolean indicating whether or not the RPC request for the source code has
   * been sent.
   */
  private boolean sourceLoaded = false;

  /**
   * The widget used to display source code.
   */
  private HTML sourceWidget = null;

  /**
   * A mapping of themes to style definitions.
   */
  private Map<String, String> styleDefs = null;

  /**
   * The widget used to display css style.
   */
  private HTML styleWidget = null;

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public ContentWidget() {
   
    setStyleName(DEFAULT_STYLE_NAME);
  }



private String createTabBarCaption(AbstractImagePrototype image, String text) {
    StringBuffer sb = new StringBuffer();
    sb.append("<table cellspacing='0px' cellpadding='0px' border='0px'><thead><tr>");
    sb.append("<td valign='middle'>");
    sb.append(image.getHTML());
    sb.append("</td><td valign='middle' style='white-space: nowrap;'>&nbsp;");
    sb.append(text);
    sb.append("</td></tr></thead></table>");
    return sb.toString();
  }

  /**
   * Get the description of this example.
   * 
   * @return a description for this example
   */
  public abstract String getDescription();

  public final String getId() {
    final String s = this.getClass().getName();
    return s.substring(s.lastIndexOf('.') + 1, s.length());
  }

  /**
   * Get the name of this example to use as a title.
   * 
   * @return a name for this example
   */
  public abstract String getName();


  /**
   * Initialize this widget by creating the elements that should be added to the
   * page.
   */
  public final void initialize() {
    if (initialized) {
      return;
    }
    initialized = true;

    stackPanel  = new StackLayoutPanel();
    //stackPanel.addTabListener(this);
    //stackPanel.setPadding(5);
    add(stackPanel);

    // Create the container for the main example
    final LayoutPanel panel1 = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));
    panel1.setPadding(0);
    panel1.setWidgetSpacing(0);
    stackPanel.add(panel1, createTabBarCaption(Pat.IMAGES.cube(),
        ConstantFactory.getInstance().data()+" ("+getName()+")"), true);


      final LayoutPanel panel2 = new LayoutPanel();
      sourceWidget = new HTML();
      sourceWidget.setStyleName(DEFAULT_STYLE_NAME + "-source");
      panel2.add(sourceWidget);
      stackPanel.add(panel2, createTabBarCaption(Pat.IMAGES.chart(),
          ConstantFactory.getInstance().chart()+" ("+getName()+")"), true);

    // Initialize the widget and add it to the page
    final Widget widget = onInitialize();
    if (widget != null) {
      panel1.add(widget, new BoxLayoutData(FillStyle.BOTH));
    }
    onInitializeComplete();
  }

  public final boolean isInitialized() {
    return initialized;
  }

  public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
    return true;
  }

  /**
   * When the widget is first initialized, this method is called. If it returns
   * a Widget, the widget will be added as the first tab. Return
   * <code>null</code> to disable the first tab.
   * 
   * @return
   */
  protected abstract Widget onInitialize();

  /**
   * Called when initialization has completed and the widget has been added to
   * the page.
   */
  protected void onInitializeComplete() {
    // Nothing to do here!
  }

  @Override
  protected void onLoad() {
    // Initialize this widget if we haven't already
    initialize();
  }



}
