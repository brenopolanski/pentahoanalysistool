
package org.pentaho.pat.client.ui.widgets;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.pat.client.ui.images.SelectionModeImageBundle;
import org.pentaho.pat.client.ui.popups.SelectionModeMenu2;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

/**
 * The Class MemberSelectionLabel.
 *
 * @author wseyler
 */
public class MemberSelectionLabel extends HorizontalPanel  {

    /** PatImages ImageBundle. */
    private transient SelectionModeImageBundle selectionImageBundle = GWT
	    .create(SelectionModeImageBundle.class);

    /** Click Listener Collection. */
    //private transient ClickListenerCollection clickListeners;

    /** Label. */
    private final transient Label label = new Label();

    /** Image. */
    private Image image;

    /** TreeItems. */
    private TreeItem treeItem;

    /**
     * Create the Label.
     */
    public MemberSelectionLabel() {
	super();
	this.sinkEvents(Event.BUTTON_LEFT | Event.BUTTON_RIGHT | Event.ONCONTEXTMENU);
	 selectionImageBundle =
	 (SelectionModeImageBundle) GWT.create(SelectionModeImageBundle.class);

	setStyleName("olap-MemberSelectionLabel"); //$NON-NLS-1$

	this.add(label);
    }

    /**
     * Create the label with some predefined text.
     *
     * @param text the text
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
    public final String[] getFullPath() {
	final List<String> pathList = new ArrayList<String>();
	pathList.add(label.getText());
	TreeItem currentTreeItem = treeItem;
	while (currentTreeItem.getParentItem() != null
		&& currentTreeItem.getParentItem().getWidget() instanceof MemberSelectionLabel) {
	    currentTreeItem = currentTreeItem.getParentItem();
	    pathList.add(0,
		    ((MemberSelectionLabel) currentTreeItem.getWidget())
			    .getText());
	}
	final String[] values = new String[pathList.size()];
	return (String[]) pathList.toArray(values);
    }

    /**
     * Get the Label.
     *
     * @return the label
     */
    public final Label getLabel() {
	return label;
    }

    /**
     * Get the label's text.
     *
     * @return the text
     */
    public final String getText() {
	return label.getText();
    }

    /**
     * Return a treeItem.
     *
     * @return the tree item
     */
    public final TreeItem getTreeItem() {
	return treeItem;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.google.gwt.user.client.ui.Widget#onBrowserEvent(com.google.gwt.user
     * .client.Event)
     */
    /**
     * Fires on browser clicks.
     * @param event the event
     */
    @Override
    public void onBrowserEvent(final Event event) {
	super.onBrowserEvent(event);
	switch (DOM.eventGetType(event)) { // NOPMD by bugg on 20/04/09 20:16
	case Event.ONCONTEXTMENU:
	    break;
	case Event.ONCLICK:
	    final SelectionModeMenu2 test = new SelectionModeMenu2();
		//test.showContextMenu(event, getSelectedItem().getText(), getSelectedItem().getTree());
		test.showContextMenu(event, getTreeItem());
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
     * @param image the image
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

    /**
     * Sets the selection mode.
     *
     * @param mode the new selection mode
     */
    public final void setSelectionMode(final int mode) {
	Image selectionImage = null;
	switch (mode) {
	case SelectionModeMenu2.MEMBER:
	    selectionImage = selectionImageBundle.memberSelectIcon()
		    .createImage();
	    break;
	case SelectionModeMenu2.CHILDREN:
	    selectionImage = selectionImageBundle.childrenSelectIcon()
		    .createImage();
	    break;
	case SelectionModeMenu2.INCLUDE_CHILDREN:
	    selectionImage = selectionImageBundle.includeChildrenSelectIcon()
		    .createImage();
	    break;
	case SelectionModeMenu2.SIBLINGS:
	    selectionImage = selectionImageBundle.siblingsSelectIcon()
		    .createImage();
	default:
	    break;
	}
	setImage(selectionImage);
    }

    public final void setSelectionMode(final String mode) {
        Image selectionImage = null;
        if (mode.equals("MEMBER"))
            selectionImage = selectionImageBundle.memberSelectIcon()
            .createImage();
        else if (mode.equals("CHILDREN"))
            selectionImage = selectionImageBundle.childrenSelectIcon()
            .createImage();
        else if (mode.equals("INCLUDE_CHILDREN"))
            selectionImage = selectionImageBundle.includeChildrenSelectIcon()
            .createImage();
        else if (mode.equals("SIBLINGS"))
            selectionImage = selectionImageBundle.siblingsSelectIcon()
            .createImage();
        
        if(selectionImage!=null)
        setImage(selectionImage);
        }

    
    /**
     * Sets the labels text.
     *
     * @param text the text
     */
    public final void setText(final String text) {
	label.setText(text);
    }

    /**
     * Set the treeItem.
     *
     * @param treeItem the tree item
     */
    public final void setTreeItem(final TreeItem treeItem) {
	this.treeItem = treeItem;
    }



}
