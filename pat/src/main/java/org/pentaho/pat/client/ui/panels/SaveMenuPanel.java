/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.ui.panels;


import java.util.List;

import org.gwt.mosaic.core.client.CoreConstants;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.ListBox;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.ListBox.CellRenderer;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.list.DefaultListModel;
import org.gwt.mosaic.ui.client.list.Filter;
import org.gwt.mosaic.ui.client.list.FilterProxyListModel;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.widgets.LabelTextBox;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.QuerySaveModel;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class SaveMenuPanel extends LayoutComposite{

    /**
     */
    private TextBox textBox;

    LabelTextBox ltb = new LabelTextBox();
    /**
     */
    private FilterProxyListModel<QuerySaveModel, String> filterModel;

    private Timer filterTimer = new Timer() {
        @Override
        public void run() {
          filterModel.filter(textBox.getText());
        }
      };


    
    public SaveMenuPanel(){
        final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(
                Orientation.VERTICAL));
            layoutPanel.setPadding(0);

            textBox = new TextBox();
            textBox.addKeyPressHandler(new KeyPressHandler() {
              public void onKeyPress(KeyPressEvent event) {
                filterTimer.schedule(CoreConstants.DEFAULT_DELAY_MILLIS);
              }
            });

            layoutPanel.add(textBox, new BoxLayoutData(FillStyle.HORIZONTAL));
            layoutPanel.add(createListBox(), new BoxLayoutData(FillStyle.BOTH));
            
            ltb.setTextBoxLabelText("Save as:");
            layoutPanel.add(ltb);
           this.getLayoutPanel().add(layoutPanel);

    }
    
    private Widget createRichListBoxCell(QuerySaveModel item) {
        final FlexTable table = new FlexTable();
        final FlexCellFormatter cellFormatter = table.getFlexCellFormatter();

        table.setWidth("100%");
        table.setBorderWidth(0);
        table.setCellPadding(3);
        table.setCellSpacing(0);
        table.setStyleName("RichListBoxCell");
        
        table.setWidget(0, 0, Pat.IMAGES.database_add().createImage());
        cellFormatter.setRowSpan(0, 0, 3);
        cellFormatter.setWidth(0, 0, "32px");
        
        table.setHTML(0, 1, "<b>" + item.getName() + "</b>");
        
        table.setText(1, 0, "Connection: " + item.getConnection());
        table.setText(2, 0, "Last Updated: " + item.getSavedDate());

        return table;
      }

    public ListBox<?> createListBox() {
        final ListBox<QuerySaveModel> listBox = new ListBox<QuerySaveModel>();
        listBox.setCellRenderer(new CellRenderer<QuerySaveModel>() {
          public void renderCell(ListBox<QuerySaveModel> listBox, int row, int column,
              QuerySaveModel item) {
            switch (column) {
              case 0:
                listBox.setWidget(row, column, createRichListBoxCell(item));
                break;
              default:
                throw new RuntimeException("Should not happen");
            }
          }
        });

        final DefaultListModel<QuerySaveModel> model = new DefaultListModel<QuerySaveModel>();
       
        ServiceFactory.getQueryInstance().getSavedQueries(Pat.getSessionID(), new AsyncCallback<List<QuerySaveModel>>(){

            public void onFailure(Throwable arg0) {
             MessageBox.error("error", "Message");  
            }

            public void onSuccess(List<QuerySaveModel> arg0) {
                if(arg0!=null){
                for (int i=0; i< arg0.size(); i++){
                   
                   model.add(arg0.get(i));
               }
                   
            }
            }
        });
        
        
        
        
        filterModel = new FilterProxyListModel<QuerySaveModel, String>(model);
        filterModel.setModelFilter(new Filter<QuerySaveModel, String>() {
          public boolean select(QuerySaveModel element, String filter) {
            final String regexp = ".*" + filter + ".*";
            if (regexp == null || regexp.length() == 0) {
              return true;
            }
            return element.getName().matches(regexp);
          }
        });

        listBox.setModel(filterModel);

        return listBox;
      }

    public void save(){
        ServiceFactory.getQueryInstance().saveQuery(Pat.getSessionID(), Pat.getCurrQuery(), ltb.getTextBoxText(), Pat.getCurrConnection(), new AsyncCallback(){

            public void onFailure(Throwable arg0) {
                MessageBox.error("Error", "fuckup");                
            }

            public void onSuccess(Object arg0) {
                
                MessageBox.info("Cool", "Save Successful");
                
            }
            
        }); 
    }
}
