/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.ui.windows;

import org.gwt.mosaic.core.client.CoreConstants;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.ListBox;
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
import org.pentaho.pat.rpc.dto.QuerySaveModel;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Timer;
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
        model.add(new QuerySaveModel("QueryModel 1", "Pentaho XMLA/A", "21-05-09"));
        model.add(new QuerySaveModel("QueryModel 2", "Mondrian Test Connection", "21-05-09"));
        model.add(new QuerySaveModel("Pauls QueryModel", null, "21-05-09"));
        model.add(new QuerySaveModel("Toms the best", "Pentaho XML/A", "21-05-09"));

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

}
