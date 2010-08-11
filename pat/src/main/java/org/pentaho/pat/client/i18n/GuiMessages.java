package org.pentaho.pat.client.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class GuiMessages {
    private static final String BUNDLE_NAME = "org.pentaho.pat.client.i18n.IGuiMessages"; //$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE = getResourceBundle(BUNDLE_NAME);
    
    private final static Log LOG = LogFactory.getLog(GuiMessages.class);


    private GuiMessages() {
    }

    public static ResourceBundle getResourceBundle(String bundleName) {
        ResourceBundle bundle = null;
        try {
            String localeString = System.getProperty("user.language");
            Locale locale = new Locale(localeString);
            bundle = ResourceBundle.getBundle(BUNDLE_NAME,locale);
            if (bundle == null) {
                bundle = ResourceBundle.getBundle(BUNDLE_NAME);
            }

        } catch (Exception e) {
            LOG.error("Could not get localization bundle", e ); //$NON-NLS-1$
        }
        return bundle;


    }
    
    public static String getString(final String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
    
    public static String getString(final String key, final String... params) {
        try {
          return (MessageFormat.format(RESOURCE_BUNDLE.getString(key), (Object[])params));
        } catch (Exception e) {
          return ( '!' + key + '!');
        }
      }
}
