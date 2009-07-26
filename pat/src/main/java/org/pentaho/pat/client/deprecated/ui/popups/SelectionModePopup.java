package org.pentaho.pat.client.deprecated.ui.popups;

import java.util.Arrays;
import java.util.List;

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.deprecated.ui.widgets.MemberSelectionLabel;
import org.pentaho.pat.client.deprecated.util.factory.ConstantFactory;
import org.pentaho.pat.client.deprecated.util.factory.MessageFactory;
import org.pentaho.pat.client.deprecated.util.factory.ServiceFactory;

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

/**
 * The Class SelectionModePopup.
 */
public class SelectionModePopup extends PopupPanel {
	/**
	 * The Class SelectionModeClearCommand.
	 *
	 * @author wseyler
	 */
	public class SelectionModeClearCommand implements Command {

		/*
		 * (non-Javadoc)
		 *
		 * @see com.google.gwt.user.client.Command#execute()
		 */
	    	/**
	    	 * Code to execute on click.
	    	 */
		public final void execute() {

			final MemberSelectionLabel targetLabel = (MemberSelectionLabel) getSource();
			final String dimName = getDimensionName(targetLabel);
			final List<String> dimSelections = Arrays.asList(targetLabel.getFullPath());

			ServiceFactory.getQueryInstance().clearSelection(Pat.getSessionID(), dimName, dimSelections, new AsyncCallback<Object>() {
				public void onFailure(final Throwable caught) {
					MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().noSelectionCleared(caught.getLocalizedMessage()));

				}

				public void onSuccess(final Object result) {
					targetLabel.setSelectionMode(CLEAR);

				}
			});
			SelectionModePopup.this.hide();
		}
	}

	/**
	 * The Class SelectionModeCommand.
	 *
	 * @author wseyler
	 */
	public class SelectionModeCommand implements Command {

		/** The selection mode. */
		private transient int selectionMode = -1;

		/**
		 * The Constructor.
		 *
		 * @param selectionMode the selection mode
		 */
		public SelectionModeCommand(final int selectionMode) {
			this.selectionMode = selectionMode;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see com.google.gwt.user.client.Command#execute()
		 */
		/**
		 * The Command executed on click.
		 */
		public final void execute() {
			final MemberSelectionLabel targetLabel = (MemberSelectionLabel) getSource();
			final String dimName = getDimensionName(targetLabel);

			final List<String> dimSelections = Arrays.asList(targetLabel.getFullPath());

			final String selection = setSelectionMode(selectionMode);
			ServiceFactory.getQueryInstance().createSelection(Pat.getSessionID(), dimName, dimSelections, selection, new AsyncCallback<Object>() {

				public void onFailure(final Throwable arg0) {
					MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().noSelectionSet(arg0.getLocalizedMessage()));

				}

				public void onSuccess(final Object arg0) {
					targetLabel.setSelectionMode(selectionMode);

				}

			});

			SelectionModePopup.this.hide();
		}
	}

	/** The Constant MEMBER. */
	public static final int MEMBER = 0;

	/** The Constant CHILDREN. */
	public static final int CHILDREN = 1;

	/** The Constant INCLUDE_CHILDREN. */
	public static final int INCLUDE_CHILDREN = 2;



	/** The Constant SIBLINGS. */
	public static final int SIBLINGS = 3;

	/** The Constant CLEAR. */
	public static final int CLEAR = -1;

	/** The source. */
	private static Widget source;

	/**
	 * Gets the source.
	 *
	 * @return the source
	 */
	public static Widget getSource() {
		return source;
	}

	/**
	 * Sets the source.
	 *
	 * @param source2 the new source
	 */
	public static void setSource(final Widget source2) {
		source = source2;
	}

	/** The menu bar. */
	private transient MenuBar menuBar;

	/**
	 * Instantiates a new selection mode popup.
	 */
	public SelectionModePopup() {
		super(false, true);
		init();
	}

	/**
	 * Gets the dimension name.
	 *
	 * @param targetLabel the target label
	 *
	 * @return the dimension name
	 */
	protected final String getDimensionName(final MemberSelectionLabel targetLabel) {
		final Tree tree = (Tree) targetLabel.getParent();
		final TreeItem rootItem = tree.getItem(0);
		final Label rootLabel = (Label) rootItem.getWidget();
		return rootLabel.getText();
	}

	/**
	 * Gets the dimension name.
	 *
	 * @param targetLabel the target label
	 *
	 * @return the dimension name
	 */
	protected final String getDimensionName(final Tree targetLabel) {
		// Tree tree = (Tree) targetLabel.getParent();
		final TreeItem rootItem = targetLabel.getItem(0);
		final Label rootLabel = (Label) rootItem.getWidget();
		return rootLabel.getText();
	}



	/**
	 * Inits the.
	 */
	protected final void init() {
		menuBar = new MenuBar(true);
		menuBar.setAutoOpen(true);
		menuBar.addItem(new MenuItem(ConstantFactory.getInstance().member(), new SelectionModeCommand(MEMBER)));
		menuBar.addItem(new MenuItem(ConstantFactory.getInstance().children(), new SelectionModeCommand(CHILDREN)));
		menuBar.addItem(new MenuItem(ConstantFactory.getInstance().includeChildren(), new SelectionModeCommand(INCLUDE_CHILDREN)));
		menuBar.addItem(new MenuItem(ConstantFactory.getInstance().siblings(), new SelectionModeCommand(SIBLINGS)));
		menuBar.addItem(new MenuItem(ConstantFactory.getInstance().clearSelections(), new SelectionModeClearCommand()));

		this.setWidget(menuBar);
	}

	/**
	 * Sets the selection mode.
	 *
	 * @param selectionMode the selection mode
	 *
	 * @return the string
	 */
	public final String setSelectionMode(final int selectionMode) {
		String selection = ""; //$NON-NLS-1$
		switch (selectionMode) { // NOPMD by bugg on 20/04/09 21:37
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
		    default:
			break;
		}
		return selection;

	}

	/**
	 * Show the context menu.
	 *
	 * @param event the event
	 * @param selectedItem the selected item
	 */
	public final void showContextMenu(final Event event, final TreeItem selectedItem) {

		init();

		setSource(selectedItem.getWidget());

	}
}