/**
 * 
 */
package com.mycompany.project.client.panels;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.mycompany.project.client.listeners.ConnectionListener;

/**
 * @author Tom Barber
 *
 */
public class DimensionPanel extends FlexTable implements ConnectionListener  {
	/*TODO
	 * The Dimension Panel Needs to Handle the listing of the dimension tree and adding the dimensions
	 * to the rows/columns grids in a manner that also allows selecting of children etc(check Original halogen for ideas).
	 * There also needs to be an execute button of some kind, and preferable a 2nd(hidden pane) that users can select to show and
	 * insert MDX code.
	 */
	public DimensionPanel() {
		super();

		init();
	}
	
	public void init(){
		
	}
	public void onConnectionBroken(Widget sender) {
		// TODO Auto-generated method stub
		
	}

	public void onConnectionMade(Widget sender) {
		// TODO Auto-generated method stub
		
	}

}
