/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Creates a Label and Textbox widget.
 * 
 * @author tom(at)wamonline.org.uk
 * 
 */
public class LabelTextBox extends LayoutComposite {

    private final Label textBoxLabel = new Label();

    private final TextBox textBox = new TextBox();

    public LabelTextBox() {
        super();
        this.getLayoutPanel().setLayout(new BoxLayout(Orientation.HORIZONTAL));

        this.getLayoutPanel().add(textBoxLabel);
        this.getLayoutPanel().add(textBox, new BoxLayoutData(FillStyle.HORIZONTAL));
    }

    /**
     * Return the Label.
     * 
     * @return the textBoxLabel
     */
    public String getTextBoxLabelText() {
        return textBoxLabel.getText();
    }

    /**
     * 
     * Set the label.
     * 
     * @param textBoxLabel
     *            the textBoxLabel to set
     */
    public void setTextBoxLabelText(final String textBoxLabelText) {
        this.textBoxLabel.setText(textBoxLabelText);
    }

    /**
     * Return the textbox value.
     * 
     * @return the textBox
     */
    public String getTextBoxText() {
        return textBox.getText();
    }

    /**
     * 
     * Set the textbox value.
     * 
     * @param textBox
     *            the textBox to set
     */
    public void setTextBoxText(final String textBoxText) {
        this.textBox.setText(textBoxText);
    }
}
