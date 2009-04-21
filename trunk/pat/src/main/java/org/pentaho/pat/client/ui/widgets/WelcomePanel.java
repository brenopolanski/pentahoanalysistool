package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;

/**
 * Basic Welcome Panel that currently opens the PAT Homepage.
 *
 * @author tom(at)wamonline.org.uk
 */
public class WelcomePanel extends DataWidget {

	/** Name. */
	private final transient String name;

	/**
	 * Constructor pass panel Name.
	 *
	 * @param name the name
	 */
	public WelcomePanel(final String name) {
		super();
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.ui.widgets.DataWidget#getDescription()
	 */
	/**
	 * Get the panel description.
	 * @return null
	 */
	@Override
	public final String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.ui.widgets.DataWidget#getName()
	 */
	/**
	 * get the Panel Name.
	 * @return name;
	 */
	@Override
	public final String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.ui.widgets.DataWidget#onInitialize()
	 */
	/**
	 * Initialization routine.
	 * @return layoutPanel;
	 */
	@Override
	public final Widget onInitialize() {
		// Not Permanent, but better than a big white space.
		final LayoutPanel layoutPanel = new LayoutPanel();
		layoutPanel.add(new Frame("http://code.google.com/p/pentahoanalysistool/wiki/StartPage?tm=6")); //$NON-NLS-1$
		return layoutPanel;
	}

}
