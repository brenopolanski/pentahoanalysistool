package org.pentaho.pat.client.panels;

import org.pentaho.pat.client.util.MessageFactory;
import org.pentaho.pat.client.widgets.OlapTable;

import com.gwtext.client.widgets.Panel;



public class OlapPanel extends Panel{
	public static OlapTable olapTable;
	public OlapPanel(){
		super();
		
		init();
	}
	
	public void init(){
		olapTable = new OlapTable(MessageFactory.getInstance());
		this.add(olapTable);
	}
}
