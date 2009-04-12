package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;




public class WelcomePanel extends DataWidget{

	private String name;

	public WelcomePanel(String name){
		super();
		this.name = name;
	}
	


	@Override
	public Widget onInitialize(){
		//Not Permanent, but better than a big white space.
		LayoutPanel layoutPanel = new LayoutPanel();
		layoutPanel.add(new Frame("http://code.google.com/p/pentahoanalysistool/wiki/StartPage?tm=6")); //$NON-NLS-1$
	return layoutPanel;	
			}

		@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}


}
