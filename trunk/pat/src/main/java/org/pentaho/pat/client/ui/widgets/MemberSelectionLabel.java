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
package org.pentaho.pat.client.ui.widgets;

import org.pentaho.pat.client.ui.images.ISelectionModeImageBundle;
import org.pentaho.pat.client.ui.popups.SelectionModeMenu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

/**
 * The Class MemberSelectionLabel.
 * 
 * @created Sept 8, 2009
 * @author tom(at)wamonline.co.uk
 */
public class MemberSelectionLabel extends HorizontalPanel {

    /** PatImages ImageBundle. */
    private  ISelectionModeImageBundle selectionImageBundle = GWT.create(ISelectionModeImageBundle.class);

    /** Label. */
    private  final Label label = new Label();

    /** Image. */
    private Image image;

    private String dimension;

    private String[] fullPath;

    /**
     * Create the Label.
     */
    public MemberSelectionLabel() {
        super();
        this.sinkEvents(NativeEvent.BUTTON_LEFT | NativeEvent.BUTTON_RIGHT | Event.ONCONTEXTMENU);
        selectionImageBundle = (ISelectionModeImageBundle) GWT.create(ISelectionModeImageBundle.class);

        setStyleName("olap-MemberSelectionLabel"); //$NON-NLS-1$

        this.add(label);
    }

    /**
     * Create the label with some predefined text.
     * 
     * @param text
     *            the text
     */
    public MemberSelectionLabel(final String text) {
        this();
        label.setText(text);
    }

    /**
     * Gets the full path.
     * 
     * @return the full path
     */
    public String[] getFullPath() {
        return fullPath;
    }

    /**
     * Sets the full path.
     * 
     * @param full
     *            path
     */
    public void setFullPath(final String[] fullpath) {
        fullPath = fullpath;
    }

    /**
     * Get the Label.
     * 
     * @return the label
     */
    public Label getLabel() {
        return label;
    }

    /**
     * Get the label's text.
     * 
     * @return the text
     */
    public String getText() {
        return label.getText();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gwt.user.client.ui.Widget#onBrowserEvent(com.google.gwt.user .client.Event)
     */
    /**
     * Fires on browser clicks.
     * 
     * @param event
     *            the event
     */
    @Override
    public void onBrowserEvent(final Event event) {
        super.onBrowserEvent(event);
        switch (DOM.eventGetType(event)) { // NOPMD by bugg on 20/04/09 20:16
        case Event.ONCONTEXTMENU:
            break;
        case Event.ONCLICK:
            final SelectionModeMenu test = new SelectionModeMenu();
            // test.showContextMenu(event, getSelectedItem().getText(), getSelectedItem().getTree());
            test.showContextMenu(event, this);
            test.setPopupPositionAndShow(new PositionCallback() {
                public void setPosition(final int offsetWidth, final int offsetHeight) {
                    test.setPopupPosition(event.getClientX(), event.getClientY());
                }
            });
        default:
            break;
        }
    }

    /**
     * Sets the label's image.
     * 
     * @param image
     *            the image
     */
    public final void setImage(final Image image) {
        if (this.image != null) {
            this.remove(this.image);
        }
        this.image = image;
        if (this.image != null) {
            this.add(this.image);
        }
    }

    public Image getImage() {
        return image;
    }

    /**
     * Sets the selection mode.
     * 
     * @param mode
     *            the new selection mode
     */
    public final void setSelectionMode(final int mode) {
        Image selectionImage = null;
        switch (mode) {
        case SelectionModeMenu.MEMBER:
            selectionImage = selectionImageBundle.memberSelectIcon().createImage();
            break;
        case SelectionModeMenu.CHILDREN:
            selectionImage = selectionImageBundle.childrenSelectIcon().createImage();
            break;
        case SelectionModeMenu.INCLUDE_CHILDREN:
            selectionImage = selectionImageBundle.includeChildrenSelectIcon().createImage();
            break;
        case SelectionModeMenu.SIBLINGS:
            selectionImage = selectionImageBundle.siblingsSelectIcon().createImage();
        default:
            break;
        }
        setImage(selectionImage);
    }

    public final void setSelectionMode(final String mode) {
        Image selectionImage = null;
        if ("MEMBER".equals(mode)) { //$NON-NLS-1$
            selectionImage = selectionImageBundle.memberSelectIcon().createImage();
        } else if ("CHILDREN".equals(mode)) { //$NON-NLS-1$
            selectionImage = selectionImageBundle.childrenSelectIcon().createImage();
        } else if ("INCLUDE_CHILDREN".equals(mode)) { //$NON-NLS-1$
            selectionImage = selectionImageBundle.includeChildrenSelectIcon().createImage();
        } else if ("SIBLINGS".equals(mode)) { //$NON-NLS-1$
            selectionImage = selectionImageBundle.siblingsSelectIcon().createImage();
        }

        if (selectionImage != null) {
            setImage(selectionImage);
        }
    }

    /**
     * Sets the labels text.
     * 
     * @param text
     *            the text
     */
    public final void setText(final String text) {
        label.setText(text);
    }

    public void setDimension(final String dimension) {
        this.dimension = dimension;
    }

    public String getDimension() {
        return dimension;
    }
}
