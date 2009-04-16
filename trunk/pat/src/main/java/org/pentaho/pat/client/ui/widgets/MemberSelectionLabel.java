package org.pentaho.pat.client.ui.widgets;

import java.util.ArrayList;
import java.util.List;

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
	// protected SelectionModeImageBundle selectionImageBundle;

	/**
	 *TODO JAVADOC
	 */
	protected ClickListenerCollection clickListeners;

	/**
	 *TODO JAVADOC
	 */
	private Label label = new Label();
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
	public MemberSelectionLabel(String text) {
		this();
		label.setText(text);
	}

	/**
	 *TODO JAVADOC
	 *
	 * @param image
	 */
	public void setImage(Image image) {
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
	public void setText(String text) {
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
	public void addClickListener(ClickListener listener) {
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
	public void removeClickListener(ClickListener listener) {
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
	public void setTreeItem(TreeItem treeItem) {
		this.treeItem = treeItem;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.Widget#onBrowserEvent(com.google.gwt.user.client.Event)
	 */
	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		switch (DOM.eventGetType(event)) {
		case Event.ONCLICK:
			if (clickListeners != null) {
				clickListeners.fireClick(this);
			}
			break;
		}
	}

	/*
	 * public void setSelectionMode(int mode) { Image selectionImage = null;
	 * switch (mode) { case SelectionModePopup.MEMBER: selectionImage =
	 * selectionImageBundle.member_select_icon().createImage(); break; case
	 * SelectionModePopup.CHILDREN: selectionImage =
	 * selectionImageBundle.children_select_icon().createImage(); break; case
	 * SelectionModePopup.INCLUDE_CHILDREN: selectionImage =
	 * selectionImageBundle.include_children_select_icon().createImage(); break;
	 * case SelectionModePopup.SIBLINGS: selectionImage =
	 * selectionImageBundle.siblings_select_icon().createImage(); }
	 * setImage(selectionImage); }
	 */

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
		List pathList = new ArrayList();
		pathList.add(label.getText());
		TreeItem currentTreeItem = treeItem;
		while (currentTreeItem.getParentItem() != null && currentTreeItem.getParentItem().getWidget() instanceof MemberSelectionLabel) {
			currentTreeItem = currentTreeItem.getParentItem();
			pathList.add(0, ((MemberSelectionLabel) currentTreeItem.getWidget()).getText());
		}
		String[] values = new String[pathList.size()];
		return (String[]) pathList.toArray(values);
	}

}
