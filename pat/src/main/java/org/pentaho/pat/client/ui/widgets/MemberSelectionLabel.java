package org.pentaho.pat.client.ui.widgets;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.pat.client.images.PatImages;
import org.pentaho.pat.client.images.SelectionModeImageBundle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * @author wseyler
 * 
 */
/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class MemberSelectionLabel extends HorizontalPanel implements SourcesClickEvents {
	
		/**
		 * PatImages ImageBundle
		 */
		protected transient SelectionModeImageBundle selectionImageBundle = GWT.create(SelectionModeImageBundle.class);

	/**
	 *TODO JAVADOC
	 */
	protected transient ClickListenerCollection clickListeners;

	/**
	 *TODO JAVADOC
	 */
	private final transient Label label = new Label();
	/**
	 *TODO JAVADOC
	 */
	private Image image;
	/**
	 *TODO JAVADOC
	 */
	private TreeItem treeItem;

	/**
	 *TODO JAVADOC
	 *
	 */
	public MemberSelectionLabel() {
		super();
		this.sinkEvents(Event.BUTTON_LEFT | Event.BUTTON_RIGHT);
		// selectionImageBundle =
		// (SelectionModeImageBundle)GWT.create(SelectionModeImageBundle.class);

		setStyleName("olap-MemberSelectionLabel"); //$NON-NLS-1$

		this.add(label);
	}

	/**
	 *TODO JAVADOC
	 *
	 * @param text
	 */
	public MemberSelectionLabel(final String text) {
		label.setText(text);
	}

	/**
	 *TODO JAVADOC
	 *
	 * @param image
	 */
	public void setImage(final Image image) {
		if (this.image != null) {
			this.remove(this.image);
		}
		this.image = image;
		if (this.image != null) {
			this.add(this.image);
		}
	}

	/**
	 *TODO JAVADOC
	 *
	 * @return
	 */
	public String getText() {
		return label.getText();
	}

	/**
	 *TODO JAVADOC
	 *
	 * @param text
	 */
	public void setText(final String text) {
		label.setText(text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.user.client.ui.SourcesClickEvents#addClickListener(com
	 * .google.gwt.user.client.ui.ClickListener)
	 */
	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.SourcesClickEvents#addClickListener(com.google.gwt.user.client.ui.ClickListener)
	 */
	public void addClickListener(final ClickListener listener) {
		if (clickListeners == null) {
			clickListeners = new ClickListenerCollection();
		}
		clickListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.user.client.ui.SourcesClickEvents#removeClickListener(
	 * com.google.gwt.user.client.ui.ClickListener)
	 */
	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.SourcesClickEvents#removeClickListener(com.google.gwt.user.client.ui.ClickListener)
	 */
	public void removeClickListener(final ClickListener listener) {
		if (clickListeners != null) {
			clickListeners.remove(listener);
		}
	}

	/**
	 *TODO JAVADOC
	 *
	 * @return
	 */
	public TreeItem getTreeItem() {
		return treeItem;
	}

	/**
	 *TODO JAVADOC
	 *
	 * @param treeItem
	 */
	public void setTreeItem(final TreeItem treeItem) {
		this.treeItem = treeItem;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.Widget#onBrowserEvent(com.google.gwt.user.client.Event)
	 */
	@Override
	public void onBrowserEvent(final Event event) {
		super.onBrowserEvent(event);
		switch (DOM.eventGetType(event)) { // NOPMD by bugg on 20/04/09 20:16
		case Event.ONCLICK:
			if (clickListeners != null) {
				clickListeners.fireClick(this);
			}
			break;
		}
	}

	
	  public void setSelectionMode(final int mode) { 
		  Image selectionImage = null;
	  switch (mode) { 
	  case SelectionModePopup.MEMBER: selectionImage = selectionImageBundle.memberSelectIcon().createImage(); 
	  break; 
	  case SelectionModePopup.CHILDREN: selectionImage = selectionImageBundle.childrenSelectIcon().createImage(); 
	  break; 
	  case SelectionModePopup.INCLUDE_CHILDREN: selectionImage = selectionImageBundle.includeChildrenSelectIcon().createImage(); 
	  break;
	  case SelectionModePopup.SIBLINGS: selectionImage = selectionImageBundle.siblingsSelectIcon().createImage(); 
	  }
	  setImage(selectionImage); 
	  }
	 

	/**
	 *TODO JAVADOC
	 *
	 * @return
	 */
	public Label getLabel() {
		return label;
	}

	/**
	 * @return
	 */
	/**
	 *TODO JAVADOC
	 *
	 * @return
	 */
	public String[] getFullPath() {
		final List pathList = new ArrayList();
		pathList.add(label.getText());
		TreeItem currentTreeItem = treeItem;
		while (currentTreeItem.getParentItem() != null && currentTreeItem.getParentItem().getWidget() instanceof MemberSelectionLabel) {
			currentTreeItem = currentTreeItem.getParentItem();
			pathList.add(0, ((MemberSelectionLabel) currentTreeItem.getWidget()).getText());
		}
		final String[] values = new String[pathList.size()];
		return (String[]) pathList.toArray(values);
	}

}
