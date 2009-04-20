package org.pentaho.pat.client.ui.widgets;

import java.util.Arrays;
import java.util.List;

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public class SelectionModePopup extends PopupPanel {
	public static final int MEMBER = 0;
	public static final int CHILDREN = 1;
	public static final int INCLUDE_CHILDREN = 2;
	public static final int SIBLINGS = 3;
	public static final int CLEAR = -1;

	

	private static Widget source;
	private transient MenuBar menuBar;

	public SelectionModePopup() {
		super(false, true);
		init();
	}

	/**
	   * 
	   */
	protected void init() {
		menuBar = new MenuBar(true);
		menuBar.setAutoOpen(true);
		menuBar.addItem(new MenuItem(ConstantFactory.getInstance().member(), new SelectionModeCommand(MEMBER)));
		menuBar.addItem(new MenuItem(ConstantFactory.getInstance().children(), new SelectionModeCommand(CHILDREN)));
		menuBar.addItem(new MenuItem(ConstantFactory.getInstance().includechildren(), new SelectionModeCommand(INCLUDE_CHILDREN)));
		menuBar.addItem(new MenuItem(ConstantFactory.getInstance().siblings(), new SelectionModeCommand(SIBLINGS)));
		menuBar.addItem(new MenuItem(ConstantFactory.getInstance().clearselections(), new SelectionModeClearCommand()));

		this.setWidget(menuBar);
	}

	/**
	 * @param targetLabel
	 * @return
	 */
	protected String getDimensionName(final MemberSelectionLabel targetLabel) {
		final Tree tree = (Tree) targetLabel.getParent();
		final TreeItem rootItem = tree.getItem(0);
		final Label rootLabel = (Label) rootItem.getWidget();
		return rootLabel.getText();
	}

	/**
	 * @param targetLabel
	 * @return
	 */
	protected String getDimensionName(final Tree targetLabel) {
		// Tree tree = (Tree) targetLabel.getParent();
		final TreeItem rootItem = targetLabel.getItem(0);
		final Label rootLabel = (Label) rootItem.getWidget();
		return rootLabel.getText();
	}

	public static void setSource(final Widget source2) {
		source = source2;
	}

	public static Widget getSource() {
		return source;
	}

	/**
	 * @author wseyler
	 * 
	 */
	public class SelectionModeCommand implements Command {
		protected transient int selectionMode = -1;

		/**
		 * @param selectionMode
		 * @param member
		 */
		public SelectionModeCommand(final int selectionMode) {
			this.selectionMode = selectionMode;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.google.gwt.user.client.Command#execute()
		 */
		public void execute() {
			final MemberSelectionLabel targetLabel = (MemberSelectionLabel) getSource();
			final String dimName = getDimensionName(targetLabel);

			final List<String> dimSelections = Arrays.asList(targetLabel.getFullPath());

			final String selection = setSelectionMode(selectionMode);
			ServiceFactory.getQueryInstance().createSelection(Pat.getSessionID(), dimName, dimSelections, selection, new AsyncCallback() {

				public void onFailure(final Throwable arg0) {
					MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().noselectionset(arg0.getLocalizedMessage()));
				
				}

				public void onSuccess(final Object arg0) {
					targetLabel.setSelectionMode(selectionMode);
					
				}

			});

			SelectionModePopup.this.hide();
		}
	}



	/**
	 *TODO JAVADOC
	 * 
	 * @param event
	 * @param selectedItem
	 */
	public void showContextMenu(final Event event, final TreeItem selectedItem) {
		
			init();
		
		setSource(selectedItem.getWidget());

	}

	public String setSelectionMode(final int selectionMode) {
		String selection = ""; //$NON-NLS-1$
		switch (selectionMode) {
		case 0:
			selection = "MEMBER"; //$NON-NLS-1$
			break;
		case 1:
			selection = "CHILDREN"; //$NON-NLS-1$
			break;
		case 2:
			selection = "INCLUDE_CHILDREN"; //$NON-NLS-1$
			break;
		case 3:
			selection = "SIBLINGS"; //$NON-NLS-1$
		}
		return selection;

	}

	/**
	 * @author wseyler
	 * 
	 */
	public class SelectionModeClearCommand implements Command {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.google.gwt.user.client.Command#execute()
		 */
		public void execute() {

			final MemberSelectionLabel targetLabel = (MemberSelectionLabel) getSource();
			final String dimName = getDimensionName(targetLabel);
			final List<String> dimSelections = Arrays.asList(targetLabel.getFullPath());

			ServiceFactory.getQueryInstance().clearSelection(Pat.getSessionID(), dimName, dimSelections, new AsyncCallback() {
				public void onFailure(final Throwable caught) {
					MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().noselectioncleared(caught.getLocalizedMessage()));
				
				}

				public void onSuccess(final Object result) {
					targetLabel.setSelectionMode(CLEAR);
				
				}
			});
			SelectionModePopup.this.hide();
		}
	}
}