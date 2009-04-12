package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.forms.client.builder.PanelBuilder;
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.TextBox;

public class connectTest extends LayoutPanel {

	public connectTest(){
		this.setLayout(new BorderLayout());
		createSupportedJDBCContent();
	}
    private void createSupportedJDBCContent() {
        
        final FormPanel formPanel = new FormPanel();
        
        // Initiate form components
       TextBox serverText = new TextBox();
       TextBox portText = new TextBox();
       TextBox databaseText = new TextBox();
       TextBox usernameText = new TextBox();
       TextBox passwordText = new TextBox();
 
       
        // Create a FormLayout instance on the given column and row specs.
        // For almost all forms you specify the columns; sometimes rows are
        // created dynamically. In this case the labels are right aligned.
        FormLayout supportedJDBCLayout = new FormLayout(
            // "right:pref, 3dlu, pref, 7dlu, right:pref, 3dlu, pref", // cols
            "right:pref, 3dlu, pref, 8dlu, right:pref, 3dlu, pref", // cols //$NON-NLS-1$
            "p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p"); // rows //$NON-NLS-1$

        // Specify that columns 1 & 5 as well as 3 & 7 have equal widths.
        supportedJDBCLayout.setColumnGroups(new int[][] { {1, 5}, {3, 7}});

        // Create a builder that assists in adding components to the container.
        // Wrap the panel with a standardized border.
        PanelBuilder builder = new PanelBuilder(supportedJDBCLayout);

        builder.addSeparator("General", CellConstraints.xyw(1, 1, 7)); //$NON-NLS-1$

        builder.addLabel("Driver", CellConstraints.xy(1, 3)); //$NON-NLS-1$
    //   builder.add(supportedDriverCombo, CellConstraints.xyw(3, 3, 5));

  //supportedDriverCombo.ensureDebugId("mosaicAbstractComboBox-normal-supportedDriverCombo");

        builder.addLabel("Server", CellConstraints.xy(1, 5)); //$NON-NLS-1$
        builder.add(serverText, CellConstraints.xy(3, 5));

        builder.addLabel("Port", CellConstraints.xy(5, 5)); //$NON-NLS-1$
        builder.add(portText, CellConstraints.xy(7, 5));

        builder.addLabel("Database", CellConstraints.xy(1, 7)); //$NON-NLS-1$
        builder.add(databaseText, CellConstraints.xy(3, 7));

        builder.addLabel("Username", CellConstraints.xy(1, 9)); //$NON-NLS-1$
        builder.add(usernameText, CellConstraints.xy(3, 9));

        builder.addLabel("Password", CellConstraints.xy(5, 9)); //$NON-NLS-1$
        builder.add(passwordText, CellConstraints.xy(7, 9));

        builder.addLabel("Cube", CellConstraints.xy(1, 11)); //$NON-NLS-1$
//        builder.add(fileUpload, CellConstraints.xyw(3, 11, 5));

  //      builder.add(ButtonBarFactory.buildLeftAlignedBar(connectBtn),
   //         CellConstraints.xy(3, 13));
        // The builder holds the layout container that we now return.
        formPanel.add(builder.getPanel());
        //layoutPanel.add(formPanel);
        this.add(formPanel);
        //return layoutPanel;
      }

}
