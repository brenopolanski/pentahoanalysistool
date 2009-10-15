/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.ui.windows;

import org.gwt.mosaic.ui.client.DisclosureLayoutPanel;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.WidgetWrapper;
import org.gwt.mosaic.ui.client.MessageBox.MessageBoxType;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;

/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class FailBox {

    public static void alert(String caption, String message, String error) {
        final MessageBox alert = new MessageBox(MessageBox.MessageBoxType.ALERT, caption) {
          @Override
          public void onClose(boolean result) {
            hide();
          }
        };
        alert.setAnimationEnabled(true);
        int preferredWidth = Window.getClientWidth();
        preferredWidth = Math.max(preferredWidth / 3, 256);
        alert.setWidth(preferredWidth + "px");
        alert.setHeight("300px");
        alert.isResizable();
        final Button buttonOK = new Button("OK");
        buttonOK.addClickHandler(new ClickHandler() {
          public void onClick(ClickEvent event) {
            alert.hide();
          }
        });
        alert.getButtonPanel().add(buttonOK);
        
        final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
        DisclosureLayoutPanel panel = new DisclosureLayoutPanel("Full Error");
        panel.add(new WidgetWrapper(new HTML(error), 
                HasAlignment.ALIGN_LEFT, HasAlignment.ALIGN_TOP));
        
        layoutPanel.add(new WidgetWrapper(new HTML(message),
            HasAlignment.ALIGN_LEFT, HasAlignment.ALIGN_TOP));

        layoutPanel.add(panel);
        
        alert.setWidget(layoutPanel);
        alert.showModal();

        if (alert.getOffsetWidth() < preferredWidth) {
          alert.setWidth(preferredWidth + "px");
          alert.center();
        }

        DeferredCommand.addCommand(new Command() {
          public void execute() {
            buttonOK.setFocus(true);
          }
        });
      }

}
