package org.pentaho.pat.client.test;

import com.google.gwt.core.client.EntryPoint;  
import com.google.gwt.user.client.ui.RootPanel;  
import com.gwtext.client.core.EventObject;  
import com.gwtext.client.widgets.Button;  
import com.gwtext.client.widgets.Panel;  
import com.gwtext.client.widgets.Window;  
import com.gwtext.client.widgets.layout.AccordionLayout;  
import com.gwtext.client.widgets.layout.HorizontalLayout;  
import com.gwtext.client.widgets.tree.XMLTreeLoader;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.AsyncTreeNode;  
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.core.Connection;  
   
public class AccordionPanel extends Panel {

	public AccordionPanel() {
		super();

		init();
	}
     public void init() {  
         this.add(createAccordionPanel());  
        }  
  
      private Panel createAccordionPanel() {  
         Panel accordionPanel = new Panel();  
           accordionPanel.setLayout(new AccordionLayout(true));  
      
            Panel panelOne = new Panel("Dimension 1");
            panelOne.setHeight(130);
        	TreePanel rowTree = new TreePanel();
    		rowTree.setDdGroup("myDDGroup");
    		rowTree.setAnimate(true);
    		rowTree.setEnableDD(true);
    		rowTree.setContainerScroll(true);
    		rowTree.setEnableDrop(true);
    		rowTree.setRootVisible(true);
    		rowTree.setAutoWidth(true);
    		rowTree.setAutoHeight(true);

    		 XMLTreeLoader loader = new XMLTreeLoader();  
    		             loader.setDataUrl("tree.xml");  
    		             loader.setMethod(Connection.GET);  
    		             loader.setRootTag("countries");  
    		             loader.setFolderIdMapping("@id");  
    		             loader.setLeafIdMapping("@id");  
    		             loader.setFolderTitleMapping("@title");  
    		             loader.setFolderTag("team");  
    		             loader.setLeafTitleMapping("@title");  
    		             loader.setLeafTag("country");  
    		             loader.setQtipMapping("@qtip");  
    		             loader.setDisabledMapping("@disabled");  
    		             loader.setCheckedMapping("@checked");  
    		             loader.setIconMapping("@icon");  
    		             loader.setAttributeMappings(new String[]{"@rank"});  
    		   
    		   
    		             final AsyncTreeNode root = new AsyncTreeNode("All Countries", loader);  
    		             rowTree.setRootNode(root);  
    		             root.expand(true, true);
    		             
    	panelOne.add(rowTree);
    	 Panel panelTwo = new Panel("Dimension 2");
    	 
            accordionPanel.add(panelOne);  
            accordionPanel.add(panelTwo);  
            accordionPanel.add(new Panel("Dimension 3"));
            accordionPanel.add(new Panel("Dimension 4"));
            accordionPanel.add(new Panel("Measures"));
            
        return accordionPanel;  
     }  
  }  