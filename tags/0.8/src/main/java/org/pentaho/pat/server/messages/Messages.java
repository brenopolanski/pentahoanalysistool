/*
 * Copyright (C) 2010 Paul Stoellberger
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


package org.pentaho.pat.server.messages;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public final class Messages {
    private static final String BUNDLE_NAME = "org.pentaho.pat.server.messages.messages"; //$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE = getResourceBundle(BUNDLE_NAME);
    
    private final static Log LOG = LogFactory.getLog(Messages.class);


    private Messages() {
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
