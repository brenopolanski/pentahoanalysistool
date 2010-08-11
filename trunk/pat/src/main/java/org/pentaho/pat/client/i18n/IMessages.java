package org.pentaho.pat.client.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.google.gwt.i18n.client.LocaleInfo;


public class IMessages {

  private static final String BUNDLE_NAME = "org.pentaho.pat.client.i18n.IGuiMessages"; //$NON-NLS-1$
    
  private static ResourceBundle messageBundle= getResourceBundle(BUNDLE_NAME);

  
  public static ResourceBundle getResourceBundle(String bundleName) {
      ResourceBundle bundle = null;
      try {
          String localeString = LocaleInfo.getCurrentLocale().toString();
          Locale locale = new Locale(localeString);
          bundle = ResourceBundle.getBundle(BUNDLE_NAME,locale);
          if (bundle == null) {
              bundle = ResourceBundle.getBundle(BUNDLE_NAME);
          }

      } catch (Exception e) {
         // LOG.error("Could not get localization bundle", e ); //$NON-NLS-1$
      }
      return bundle;


  }
  
  public static String getString(final String key) {
      try {
          return messageBundle.getString(key);
      } catch (MissingResourceException e) {
          return '!' + key + '!';
      }
  }
  
  public static String getString(final String key, final String... params) {
      try {
        return (MessageFormat.format(messageBundle.getString(key), (Object[])params));
      } catch (Exception e) {
        return ( '!' + key + '!');
      }
    }

  public static ResourceBundle getResourceBundle() {
    return messageBundle;
  }

  public static void setResourceBundle(ResourceBundle messageBundle) {
    IMessages.messageBundle = messageBundle;
  }

}