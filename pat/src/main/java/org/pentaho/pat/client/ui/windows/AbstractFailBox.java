/*
 * Copyright (C) 2009 Thomas Barber
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *
 */
package org.pentaho.pat.client.ui.windows;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.UserAgent;
import org.gwt.mosaic.ui.client.MessageBoxImages;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.WidgetWrapper;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.pentaho.pat.client.util.factory.ConstantFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * @created Oct 18, 2009
 * @since 0.5.0
 * @author tom (at) wamonline.org.uk
 */
public abstract class AbstractFailBox extends WindowPanel {

    public interface ConfirmationCallback {
        void onResult(boolean result);
    }

    public enum MessageBoxType {
        ALERT, CONFIRM, ERROR, INFO, PASSWORD, PLAIN, PROMPT
    }

    public interface PromptCallback<T> {
        void onResult(T input);
    };

    /**
     * The caption images to use.
     */
    public static final MessageBoxImages MESSAGEBOX_IMAGES = (MessageBoxImages) GWT.create(MessageBoxImages.class);

    private static final MessageBoxType DEFAULT_TYPE = MessageBoxType.PLAIN;

    private static void alert(final MessageBoxType type, final String caption, final String message, final String error) {
        final AbstractFailBox alert = new AbstractFailBox(type, caption) {
            @Override
            public void onClose(final boolean result) {
                hide();
            }
        };
        alert.setAnimationEnabled(true);
        int preferredWidth = Window.getClientWidth();
        preferredWidth = Math.max(preferredWidth / 3, 256);
        alert.setWidth(preferredWidth + "px"); //$NON-NLS-1$
        int preferredHeight = Window.getClientHeight();
        preferredHeight = Math.max(preferredHeight / 3, 256);
        alert.setHeight(preferredHeight + "px"); //$NON-NLS-1$

        final Button buttonOK = new Button(ConstantFactory.getInstance().ok());
        buttonOK.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                alert.hide();
            }
        });
        alert.getButtonPanel().add(buttonOK);
        final LayoutPanel lp = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
        final DisclosurePanel dPanel = new DisclosurePanel(ConstantFactory.getInstance().errorDetail());
        dPanel.addOpenHandler(new OpenHandler<DisclosurePanel>() {

            public void onOpen(OpenEvent<DisclosurePanel> arg0) {
                if (alert.isShowing()) {
                    alert.pack();
                    alert.layout();
                }
            }

        });

        dPanel.addCloseHandler(new CloseHandler<DisclosurePanel>() {

            public void onClose(CloseEvent<DisclosurePanel> arg0) {
                if (alert.isShowing()) {

                    alert.pack();
                    alert.layout();
                }
            }

        });
        final ScrollLayoutPanel sPanel = new ScrollLayoutPanel();
        sPanel.add(new WidgetWrapper(new HTML(error), HasAlignment.ALIGN_LEFT, HasAlignment.ALIGN_TOP));
        sPanel.setHeight("50px"); //$NON-NLS-1$
        dPanel.add(sPanel);
        lp.add(new WidgetWrapper(new HTML(message), HasAlignment.ALIGN_LEFT, HasAlignment.ALIGN_TOP));
        lp.add(dPanel);
        alert.setWidget(lp);
        alert.showModal(false);
        if (alert.getOffsetWidth() < preferredWidth) {
            alert.setWidth(preferredWidth + "px"); //$NON-NLS-1$
            alert.center();
        }

        DeferredCommand.addCommand(new Command() {
            public void execute() {
                buttonOK.setFocus(true);
            }
        });
    }

    public static void alert(String caption, String message, String error) {
        alert(MessageBoxType.ALERT, caption, message, error);
    }

    public static void error(String caption, String message, String error) {
        alert(MessageBoxType.ERROR, caption, message, error);
    }

    private Widget widget;

    private  final LayoutPanel buttonPanel = new LayoutPanel();

    private Image image;

    private  WidgetWrapper imageWrapper;

    public AbstractFailBox() {
        this(DEFAULT_TYPE, null);
    }

    public AbstractFailBox(MessageBoxType type) {
        this(type, null);
    }

    public AbstractFailBox(MessageBoxType type, String text) {
        this(type, text, false);
    }

    public AbstractFailBox(MessageBoxType type, String text, boolean autoHide) {
        super(text, true, autoHide);

        final LayoutPanel layoutPanel = new LayoutPanel(new BorderLayout());
        super.setWidget(layoutPanel);
        layoutPanel.setWidgetSpacing(10);

        // (ggeorg) this is a workaround for the infamous Firefox cursor bug
        if (UserAgent.isGecko()) {
            DOM.setStyleAttribute(getLayoutPanel().getElement(), "overflow", "auto"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        final BoxLayout buttonPanelLayout = new BoxLayout(Orientation.HORIZONTAL);
        buttonPanelLayout.setLeftToRight(false);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanel.setPadding(5);
        setFooter(buttonPanel);

        if (type == MessageBoxType.ALERT) {
            setImage(MESSAGEBOX_IMAGES.dialogWarning().createImage());
        } else if (type == MessageBoxType.CONFIRM) {
            setImage(MESSAGEBOX_IMAGES.dialogQuestion().createImage());
        } else if (type == MessageBoxType.ERROR) {
            setImage(MESSAGEBOX_IMAGES.dialogError().createImage());
        } else if (type == MessageBoxType.INFO) {
            setImage(MESSAGEBOX_IMAGES.dialogInformation().createImage());
        } else if (type == MessageBoxType.PASSWORD) {
            setImage(MESSAGEBOX_IMAGES.dialogPassword().createImage());
        } else if (type == MessageBoxType.PROMPT) {
            setImage(MESSAGEBOX_IMAGES.dialogQuestion().createImage());
        }

        addStyleName("mosaic-MessageBox"); //$NON-NLS-1$
    }

    public AbstractFailBox(String text) {
        this(DEFAULT_TYPE, text, false);
    }

    public AbstractFailBox(String text, boolean autoHide) {
        this(DEFAULT_TYPE, text, autoHide);
    }

    public LayoutPanel getButtonPanel() {
        return buttonPanel;
    }

    public String getHTML() {
        if (widget instanceof HasHTML) {
            return ((HasHTML) widget).getHTML();
        } else {
            return null;
        }
    }

    public Image getImage() {
        return image;
    }

    public String getText() {
        if (widget instanceof HasText) {
            return ((HasText) widget).getText();
        } else {
            return null;
        }
    }

    public Widget getWidget() {
        return widget;
    }

    public abstract void onClose(boolean result);

    @Override
    public boolean onKeyDownPreview(char key, int modifiers) {
        if (key == KeyCodes.KEY_ESCAPE) {
            onClose(false);

        }
        return super.onKeyDownPreview(key, modifiers);
    }

    public void setHTML(String html) {
        if (widget instanceof HasHTML) {
            ((HasHTML) widget).setHTML(html);
        } else {
            setWidget(new HTML(html));
        }
    }

    public void setImage(Image image) {
        final LayoutPanel layoutPanel = (LayoutPanel) super.getWidget();
        if (this.image != image) {
            if (imageWrapper != null) {
                layoutPanel.remove(imageWrapper);
            }
            this.image = image;
            imageWrapper = new WidgetWrapper(image);
            layoutPanel.add(imageWrapper, new BorderLayoutData(Region.WEST));
        }
    }

    public void setText(final String text) {
        if (widget instanceof HasHTML) {
            ((HasHTML) widget).setText(text);
        } else {
            final HTML html = new HTML();
            html.setText(text);
            setWidget(html);
        }
    }

    public void setWidget(Widget w) {
        setWidget(w, -1);
    }

    public void setWidget(Widget w, final int padding) {
        final LayoutPanel layoutPanel = (LayoutPanel) super.getWidget();
        if (padding > -1) {
            layoutPanel.setPadding(padding);
        }
        if (!widget.equals(w)) {
            if (widget != null) {
                layoutPanel.remove(widget);
            }
            widget = w;
            layoutPanel.add(widget);
        }
    }

}
