/**
 * 
 */
package org.pentaho.pat.client.panels;

import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.form.TextField;

/**
 * @author root
 *
 */
public class NorthPanel extends TabPanel {
	private TextField mdxField;
	private Panel mdxTab;
	private Panel filterTab;
	public NorthPanel()
	{
		super();
		
		init();
	}
	
	public void init(){
		this.setResizeTabs(true);  
		this.setMinTabWidth(115);  
		this.setTabWidth(135);  
		this.setEnableTabScroll(true);  
		this.setWidth("100%");  
		this.setHeight("100%");  
		this.setActiveTab(0); 
		this.setTabPosition(Position.BOTTOM); 
		mdxTab = new Panel();  
		mdxTab.setAutoScroll(true);  
		mdxTab.setTitle("MDX Tab");  
		mdxTab.setIconCls("tab-icon");  
		mdxField = new TextField();
		mdxField.setWidth("100%");
		mdxField.setHeight("100%");
		mdxTab.add(mdxField);
		filterTab = new Panel();  
		filterTab.setAutoScroll(true);  
		filterTab.setTitle("Filter Tab");  
		filterTab.setIconCls("tab-icon");  
		this.add(mdxTab);  
		this.add(filterTab);
	}

}
