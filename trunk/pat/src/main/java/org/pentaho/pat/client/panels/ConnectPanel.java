package org.pentaho.pat.client.panels;

import org.gwt.mosaic.ui.client.ListBox.CellRenderer;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ResizableWidget;
import com.google.gwt.widgetideas.client.ResizableWidgetCollection;
import com.gwtext.client.widgets.Window;

import org.gwt.mosaic.forms.client.builder.PanelBuilder;
import org.gwt.mosaic.forms.client.factories.ButtonBarFactory;
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.ui.client.ComboBox;
import org.gwt.mosaic.ui.client.DecoratedTabLayoutPanel;
import org.gwt.mosaic.ui.client.ListBox;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.layout.BaseLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.HasLayoutManager;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.list.DefaultListModel;
import org.pentaho.pat.client.events.SourcesConnectionEvents;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.listeners.ConnectionListenerCollection;

import org.pentaho.pat.client.util.GuidFactory;
import org.pentaho.pat.client.util.MessageFactory;
import org.pentaho.pat.client.util.ServiceFactory;


public class ConnectPanel extends WindowPanel implements SourcesConnectionEvents {
	
	TextBox connectionText;
	TextBox serverText;
	TextBox portText;
	TextBox usernameText;
	TextBox passwordText;
	TextBox cubeText;
	TextBox genericServerText;
	TextBox genericUsernameText;
	TextBox genericPasswordText;
	TextBox genericCubeText;
	TextBox databaseText;
	TextBox genericPortText;
	TextBox genericDatabaseText;
	FileUpload fileUpload;
	Button connectBtn2;
	Button connectBtn;
	Button uploadBtn;
	ListBox<Driver> supportedDriverCombo;
	ComboBox genericDriverCombo;
	Label debuglabel;
	Label uploadlabel;
	FileUpload genericFileUpload;

	private DecoratedTabLayoutPanel tabPanel;

	private String schemafilename;
	static String queryTypeGroup = "QUERY_TYPE"; //$NON-NLS-1$
	boolean connectionEstablished = false;
	ConnectionListenerCollection connectionListeners;

	public ConnectPanel() {
		
		this.setTitle("Register new Mondrian Connection");
		this.setWidget(onInitialize());
		this.setWidth("700");
	}


	public boolean isConnectionEstablished() {
		return connectionEstablished;
	}

	public void setConnectionEstablished(boolean connectionEstablished) {
		this.connectionEstablished = connectionEstablished;
	}

	public void connect(String connectionStr) {
		if (!isConnectionEstablished()) {
			ServiceFactory.getInstance().connect(connectionStr,
					GuidFactory.getGuid(), new AsyncCallback() {
				public void onSuccess(Object result) {
					Boolean booleanResult = (Boolean) result;
					if (booleanResult.booleanValue()) {
						setConnectionEstablished(true);
						connectionListeners
						.fireConnectionMade(ConnectPanel.this);
					} else {
						setConnectionEstablished(false);
						connectionListeners
						.fireConnectionBroken(ConnectPanel.this);
					}
					connectBtn
					.setText(isConnectionEstablished() ? MessageFactory
							.getInstance().disconnect()
							: MessageFactory.getInstance()
							.connect());
					ConnectPanel.this.hide();
					
					
				}

				public void onFailure(Throwable caught) {
					System.out.println(caught.getLocalizedMessage());
					com.google.gwt.user.client.Window.alert(MessageFactory.getInstance()
							.no_connection_param(
									caught.getLocalizedMessage()));
					setConnectionEstablished(false);
					connectBtn
					.setText((isConnectionEstablished() ? MessageFactory
							.getInstance().disconnect()
							: MessageFactory.getInstance()
							.connect()));
					
				}
			});

		}

	}

	public void disconnect() {
		if (isConnectionEstablished()) {
			ServiceFactory.getInstance().disconnect(GuidFactory.getGuid(),
					new AsyncCallback() {
						public void onFailure(Throwable caught) {
							// Window.alert(MessageFactory.getInstance().
							// no_connection_param
							// (caught.getLocalizedMessage()));
							setConnectionEstablished(false);
							connectionListeners
									.fireConnectionBroken(ConnectPanel.this);
							connectBtn
									.setText(isConnectionEstablished() ? MessageFactory
											.getInstance().disconnect()
											: MessageFactory.getInstance()
													.connect());
							
							
							
						}

						public void onSuccess(Object result) {
							setConnectionEstablished(false);
							connectionListeners
									.fireConnectionBroken(ConnectPanel.this);
							connectBtn
									.setText(isConnectionEstablished() ? MessageFactory
											.getInstance().disconnect()
											: MessageFactory.getInstance()
													.connect());
						}
					});
		}
	}
	

	public String getSchemafilename() {
		return schemafilename;
	}

	public void setSchemafilename(String schemafilename) {
		this.schemafilename = schemafilename;
	}
	  protected Widget onInitialize() {
		    // Create a layout panel to align the widgets
		    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(
		            Orientation.VERTICAL));

		    // Create a tab panel
		    tabPanel = new DecoratedTabLayoutPanel();


		    // Add a supportedJDBC tab
		    tabPanel.add(createSupportedJDBCContent(), "Supported JDBC");

		    //Add a genericJDBC tab
		    tabPanel.add(createGenericJDBCContent(), "Generic JDBC");
		    
		    tabPanel.ensureDebugId("cwTabPanel");

		    tabPanel.addTabListener(new TabListener() {
		    	public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
			        return true;
			      }
		    	public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
			       pack();
			        
			      }
			    });
		    layoutPanel.add(tabPanel, new BoxLayoutData(FillStyle.BOTH));
		    
		    return layoutPanel;
		  }

	  
	  @Override
	  protected void onLoad(){
	  	tabPanel.selectTab(0);
	  	super.onLoad();
	  }
		  /**
		   * Create content for layout.
		   */

		  private LayoutPanel createSupportedJDBCContent() {
		    final LayoutPanel layoutPanel = new LayoutPanel(new BorderLayout());
		    layoutPanel.setPadding(0);

		    //Initiate form components
		    serverText = new TextBox();
		    portText = new TextBox();
		    databaseText = new TextBox();
		    usernameText = new TextBox();
		    passwordText = new TextBox();		    
		    fileUpload = new FileUpload();
		    supportedDriverCombo = new ListBox<Driver>(new String[] {
		            "Name", "Sample URL", "Driver"});

		    supportedDriverCombo.setCellRenderer(new CellRenderer<Driver>() {
		        public void renderCell(ListBox<Driver> listBox, int row, int column,
		            Driver item) {
		          switch (column) {
		            case 0:
		              listBox.setText(row, column, item.getName());
		              break;
		            case 1:
		              listBox.setText(row, column, item.getSampleUrl());
		              break;
		            case 2:
		              listBox.setText(row, column, String.valueOf(item.getDriverTag()));
		              break;
		            default:
		              throw new RuntimeException("Should not happen");
		          }
		        }
		      });

		    final DefaultListModel<Driver> model = (DefaultListModel<Driver>) supportedDriverCombo.getModel();
		    model.add(new Driver("MySQL", "jdbc:mysql://localhost/mydatabase", "com.mysql.jdbc.Driver"));
		    model.add(new Driver("HSQL", "", "org.hsqldb.jdbcDriver"));

		    supportedDriverCombo.addChangeListener(new ChangeListener() {
		      public void onChange(Widget sender) {
		        
		      }
		    });

		    connectBtn = new Button(MessageFactory.getInstance().connect(), new ClickListener(){
					public void onClick(Widget sender) {
						if (connectBtn.getText().equals(
								MessageFactory.getInstance().connect())) {
							
							String cStr = "jdbc:mondrian:Jdbc=";
		
					
									if (supportedDriverCombo.getItem(
								            supportedDriverCombo.getSelectedIndex()).getName().equals("MySQL")) {
									//jdbc:mysql://localhost:3306/sampledata
									cStr = cStr + "jdbc:mysql://" + serverText.getText();
									if (portText.getText().length() > 0 ) cStr = cStr + ":" + portText.getText();
									cStr = cStr + "/" + databaseText.getText();
									cStr = cStr + "?user=" + usernameText.getText(); 
									if (passwordText.getText().length() > 0 ) cStr = cStr + "&password=" + passwordText.getText();
									}
									if (supportedDriverCombo.getItem(
								            supportedDriverCombo.getSelectedIndex()).getName().equals("HSQL")) {
										//jdbc:hsqldb:hsql://localhost:9001/hibernate
										cStr = cStr + "jdbc:hsqldb:hsql://" + serverText.getText();
										if (portText.getText().length() > 0 ) cStr = cStr + ":" + portText.getText();
										cStr = cStr + "/" + databaseText.getText();
										cStr = cStr + ";username=" + usernameText.getText();
										if (passwordText.getText().length() > 0 ) cStr = cStr + ";password=" + passwordText.getText();
										
											
									}
									
									cStr = cStr + ";Catalog="  + fileUpload.getFilename()
									+ ";JdbcDrivers=" + supportedDriverCombo.getItem(
								            supportedDriverCombo.getSelectedIndex()).getDriverTag();

							connect(cStr);
							//debuglabel.setText(cStr);

						} else if (connectBtn.getText().equals(
								MessageFactory.getInstance().disconnect())) {
							disconnect();
						}
					}
				});

		    uploadBtn = new Button("Upload Schema", new ClickListener(){
				public void onClick(Widget sender) {
					// TODO thats not a nice way to tell PAT the servlet
			//		.getForm().submit("./schemaupload",null,Connection.POST,"... loading",false);
					
					
				}
			});			  
			  
		    
		    // Create a FormLayout instance on the given column and row specs.
		    // For almost all forms you specify the columns; sometimes rows are
		    // created dynamically. In this case the labels are right aligned.
		    FormLayout supportedJDBCLayout = new FormLayout(
		        //"right:pref, 3dlu, pref, 7dlu, right:pref, 3dlu, pref", // cols
		    	"right:pref, 100px, pref, 100px, right:pref, 100px, pref", // cols
		        "p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p"); // rows

		    // Specify that columns 1 & 5 as well as 3 & 7 have equal widths.
		    supportedJDBCLayout.setColumnGroups(new int[][] { {1, 5}, {3, 7}});

		    // Create a builder that assists in adding components to the container.
		    // Wrap the panel with a standardized border.
		    PanelBuilder builder = new PanelBuilder(supportedJDBCLayout);
		    
		    builder.addSeparator("General", CellConstraints.xyw(1, 1, 7));



		    builder.addLabel("Driver", CellConstraints.xy(1, 3));
		    builder.add(supportedDriverCombo, CellConstraints.xy(3, 3));
		    supportedDriverCombo.ensureDebugId("mosaicAbstractComboBox-normal-supportedDriverCombo");
		     
		    builder.addLabel("Server", CellConstraints.xy(1, 5));
		    builder.add(serverText, CellConstraints.xy(3, 5));
		    
		    builder.addLabel("Port", CellConstraints.xy(5, 5));
		    builder.add(portText, CellConstraints.xy(7, 5));
		    
		    builder.addLabel("Database", CellConstraints.xy(1, 7));
		    builder.add(databaseText, CellConstraints.xy(3, 7));
		    
		    builder.addLabel("Username", CellConstraints.xy(1, 9));
		    builder.add(usernameText, CellConstraints.xy(3, 9));
		    
		    builder.addLabel("Password", CellConstraints.xy(5, 9));
		    builder.add(passwordText, CellConstraints.xy(7, 9));
		    
		    builder.addLabel("Cube", CellConstraints.xy(1, 11));
		    builder.add(fileUpload, CellConstraints.xyw(3, 11, 5));
		    
		    builder.add(ButtonBarFactory.buildLeftAlignedBar(connectBtn), CellConstraints.xy(1, 13));
		 // The builder holds the layout container that we now return.
		    layoutPanel.add(builder.getPanel());

		    
			
			
			/*fpanel1.addFormListener(new FormListener() {
				public void onActionComplete(com.gwtext.client.widgets.form.Form form, int httpStatus, String responseText) {
					
					if (httpStatus == 200)
					{
						uploadlabel.setText("Schema:" + cubeText.getText());
						connectBtn.enable();
						uploadBtn.disable();
						String tmp = responseText.substring(responseText.indexOf("<pre>")+5,responseText.indexOf("</pre>"));
						setSchemafilename(tmp);
						
					}
					else
						com.google.gwt.user.client.Window.alert("ERROR: Schema could not be uploaded");
					
				};
				public void onActionFailed(Form form, int httpStatus,
						String responseText) {
					// TODO Auto-generated method stub
					//com.google.gwt.user.client.Window.alert("onactionfailed:" +responseText);
					
				}
				
				public boolean doBeforeAction(Form form) {
					// TODO Auto-generated method stub
				//	com.google.gwt.user.client.Window.alert("dobefore:" +form.findField("file").getValueAsString());
					return true;
				}
			});
			
			 
			 Hidden login = new Hidden();
	         login.setName("mode");
	         login.setValue("login");
	        */
		    
		    return layoutPanel;
		  }
		  
		  

		  /**
		   * Create content for layout.
		   */

		  private LayoutPanel createGenericJDBCContent() {
		    final LayoutPanel layoutPanel = new LayoutPanel();
		    layoutPanel.setPadding(0);


		    //Initiate form components
		    genericServerText = new TextBox();
		    genericPortText = new TextBox();
		    genericDatabaseText = new TextBox();
		    genericUsernameText = new TextBox();
		    genericPasswordText = new TextBox();
		    genericFileUpload = new FileUpload();
		    
		 // Create a FormLayout instance on the given column and row specs.
		    // For almost all forms you specify the columns; sometimes rows are
		    // created dynamically. In this case the labels are right aligned.
		    FormLayout genericJDBCLayout = new FormLayout(
		        //"right:pref, 3dlu, pref, 7dlu, right:pref, 3dlu, pref", // cols
		    		"right:pref, 100px, pref, 100px, right:pref, 100px, pref", // cols
	        "p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p"); // rows

		    // Specify that columns 1 & 5 as well as 3 & 7 have equal widths.
		    genericJDBCLayout.setColumnGroups(new int[][] { {1, 5}, {3, 7}});

		    // Create a builder that assists in adding components to the container.
		    // Wrap the panel with a standardized border.
		    PanelBuilder genericBuilder = new PanelBuilder(genericJDBCLayout);
		    
		    genericBuilder.addSeparator("General", CellConstraints.xyw(1, 1, 7));

		    genericBuilder.addLabel("Database", CellConstraints.xy(1, 3));
		    genericBuilder.add(genericDriverCombo = new ComboBox(), CellConstraints.xy(3, 3));
		    supportedDriverCombo.ensureDebugId("mosaicAbstractComboBox-normal-genericDriverCombo");
		    
		    genericBuilder.addLabel("Server", CellConstraints.xy(1, 5));
		    genericBuilder.add(genericServerText, CellConstraints.xy(3, 5));
		    
		    genericBuilder.addLabel("Port", CellConstraints.xy(5, 5));
		    genericBuilder.add(genericPortText, CellConstraints.xy(7, 5));
		    
		    genericBuilder.addLabel("Database", CellConstraints.xy(1, 7));
		    genericBuilder.add(genericDatabaseText, CellConstraints.xy(3, 7));
		    
		    genericBuilder.addLabel("Username", CellConstraints.xy(1, 9));
		    genericBuilder.add(genericUsernameText, CellConstraints.xy(3, 9));
		    
		    genericBuilder.addLabel("Password", CellConstraints.xy(5, 9));
		    genericBuilder.add(genericPasswordText, CellConstraints.xy(7, 9));
		    
		    genericBuilder.addLabel("Cube", CellConstraints.xy(1, 11));
		    genericBuilder.add(genericFileUpload, CellConstraints.xyw(3, 11, 5));

		    
		 // The builder holds the layout container that we now return.
		    layoutPanel.add(genericBuilder.getPanel());

		    
		    return layoutPanel;
		  }

		  /*
		   * (non-Javadoc)
		   * 
		   * @seeorg.pentaho.halogen.client.listeners.SourcesConnectionEvents#
		   * addConnectionListener
		   * (org.pentaho.halogen.client.listeners.ConnectionListener)
		   */
		  public void addConnectionListener(ConnectionListener listener) {
		          if (connectionListeners == null) {
		                  connectionListeners = new ConnectionListenerCollection();
		          }
		          connectionListeners.add(listener);
		  }

		  /*
		   * (non-Javadoc)
		   * 
		   * @seeorg.pentaho.halogen.client.listeners.SourcesConnectionEvents#
		   * removeClickListener
		   * (org.pentaho.halogen.client.listeners.ConnectionListener)
		   */
		  public void removeClickListener(ConnectionListener listener) {
		          if (connectionListeners != null) {
		                  connectionListeners.remove(listener);
		          }
		  }



}

/**
 * 
 * @return
 */
class Driver {
  private String name;
  private String sampleurl;
  private String drivertag;

  public Driver(String name, String sampleurl, String drivertag) {
    this.name = name;
    this.sampleurl = sampleurl;
    this.drivertag = drivertag;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSampleUrl() {
    return sampleurl;
  }

  public void setSampleUrl(String sampleurl) {
    this.sampleurl = sampleurl;
  }

  public String getDriverTag() {
	    return drivertag;
	  }

	  public void setDriverTag(String drivertag) {
	    this.drivertag = drivertag;
	  }
  @Override
  public String toString() {
    return getName() + " " + getSampleUrl() + " " + getDriverTag();
  }

  
}

