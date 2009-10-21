/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class LabelTextBox extends LayoutComposite {

    Label textBoxLabel = new Label();
    TextBox textBox = new TextBox();
    public LabelTextBox(){
        this.getLayoutPanel().setLayout(new BoxLayout(Orientation.HORIZONTAL));
        
        this.getLayoutPanel().add(textBoxLabel);
        this.getLayoutPanel().add(textBox);
    }
    
    /**
     *TODO JAVADOC
     * @return the textBoxLabel
     */
    public String getTextBoxLabelText() {
        return textBoxLabel.getText();
    }
    /**
     *
     *TODO JAVADOC
     * @param textBoxLabel the textBoxLabel to set
     */
    public void setTextBoxLabelText(String textBoxLabelText) {
        this.textBoxLabel.setText(textBoxLabelText);
    }
    /**
     *TODO JAVADOC
     * @return the textBox
     */
    public String getTextBoxText() {
        return textBox.getText();
    }
    /**
     *
     *TODO JAVADOC
     * @param textBox the textBox to set
     */
    public void setTextBoxText(String textBoxText) {
        this.textBox.setText(textBoxText);
    }
}
