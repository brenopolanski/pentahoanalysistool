package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;

// TODO: Auto-generated Javadoc
/**
 * TODO JAVADOC.
 * 
 * @author bugg
 */
public class WelcomePanel extends DataWidget {

	/** TODO JAVADOC. */
	private final transient String name;

	/**
	 * TODO JAVADOC.
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
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.ui.widgets.DataWidget#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.ui.widgets.DataWidget#onInitialize()
	 */
	@Override
	public Widget onInitialize() {
		// Not Permanent, but better than a big white space.
		final LayoutPanel layoutPanel = new LayoutPanel();
		layoutPanel.add(new Frame("http://code.google.com/p/pentahoanalysistool/wiki/StartPage?tm=6")); //$NON-NLS-1$
		return layoutPanel;
	}

}
