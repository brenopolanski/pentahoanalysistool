/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
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
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public abstract class FailBox extends WindowPanel {

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

  private static void alert(MessageBoxType type, String caption, String message, String error) {
    final FailBox alert = new FailBox(type, caption) {
      @Override
      public void onClose(boolean result) {
        hide();
      }
    };
    alert.setAnimationEnabled(true);
    int preferredWidth = Window.getClientWidth();
    preferredWidth = Math.max(preferredWidth / 3, 256);
    alert.setWidth(preferredWidth + "px"); //$NON-NLS-1$
    
    final Button buttonOK = new Button(ConstantFactory.getInstance().ok());
    buttonOK.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        alert.hide();
      }
    });
    alert.getButtonPanel().add(buttonOK);
    LayoutPanel lp = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
    DisclosurePanel dPanel = new DisclosurePanel("STACK");
    ScrollLayoutPanel sPanel = new ScrollLayoutPanel();
    sPanel.add(new WidgetWrapper(new HTML(error),
            HasAlignment.ALIGN_LEFT, HasAlignment.ALIGN_TOP));
    sPanel.setHeight("50px"); //$NON-NLS-1$
    dPanel.add(sPanel);
    dPanel.setOpen(true);
    lp.add(new WidgetWrapper(new HTML(message),
            HasAlignment.ALIGN_LEFT, HasAlignment.ALIGN_TOP));
    lp.add(dPanel);
    alert.setWidget(lp);
    alert.showModal();
    dPanel.setOpen(false);
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

  private LayoutPanel buttonPanel = new LayoutPanel();

  private Image image;

  private WidgetWrapper imageWrapper;

  public FailBox() {
    this(DEFAULT_TYPE, null);
  }

  public FailBox(MessageBoxType type) {
    this(type, null);
  }

  public FailBox(MessageBoxType type, String text) {
    this(type, text, false);
  }

  public FailBox(MessageBoxType type, String text, boolean autoHide) {
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

  public FailBox(String text) {
    this(DEFAULT_TYPE, text, false);
  }

  public FailBox(String text, boolean autoHide) {
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
    switch (key) {
      case KeyCodes.KEY_ESCAPE:
        onClose(false);
        break;
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
      HTML html = new HTML();
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
    if (widget != w) {
      if (widget != null) {
        layoutPanel.remove(widget);
      }
      widget = w;
      layoutPanel.add(widget);
    }
  }

}
