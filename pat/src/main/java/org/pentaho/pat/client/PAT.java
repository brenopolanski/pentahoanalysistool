package org.pentaho.pat.client;

import org.pentaho.pat.client.PATPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PAT implements EntryPoint {
	
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
		      public void onUncaughtException(Throwable e) {
		    	  String str = "";
		    		  e.printStackTrace();
		        Window.alert(str);
		      }
		});

		PATPanel tabPanel = new PATPanel();
		RootPanel root = RootPanel.get();
		root.setSize("1024px", "768px");
		root.add(tabPanel);
		tabPanel.setSize("100%", "100%");
			}
		
	
}
