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
package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.ui.client.LayoutComposite;

/**
 * A widget used to show the examples in the content panel. It includes a tab bar with options to view the example, view
 * the source, or view the css style rules.
 * <p>
 * This widget uses a lazy initialization mechanism so that the content is not rendered until the onInitialize method is
 * called, which happens the first time the widget is added to the page. The data in the source and css tabs are loaded
 * using RPC call to the server.
 */
public abstract class AbstractDataWidget extends LayoutComposite {

    /** The default style name. */
    protected static final String DEF_STYLE_NAME = "Pat-ContentWidget"; //$NON-NLS-1$

    /**
     * Constructor.
     */
    public AbstractDataWidget() {
        super();
        setStyleName(DEF_STYLE_NAME);
    }

    /**
     * Get the description of this example.
     * 
     * @return a description for this example
     */
    public abstract String getDescription();

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public final String getId() {
        final String idStr = this.getClass().getName();
        return idStr.substring(idStr.lastIndexOf('.') + 1, idStr.length());
    }

    /**
     * Get the name of this example to use as a title.
     * 
     * @return a name for this example
     */
    public abstract String getName();

    /**
     * When the widget is first initialized, this method is called. If it returns a Widget, the widget will be added as
     * the first tab. Return <code>null</code> to disable the first tab.
     * 
     * @return the widget
     */
    protected abstract void initializeWidget();
    

}
