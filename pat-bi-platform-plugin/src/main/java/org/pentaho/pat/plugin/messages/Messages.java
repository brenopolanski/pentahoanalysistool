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


package org.pentaho.pat.plugin.messages;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.platform.util.messages.LocaleHelper;


public final class Messages {

    private static final String BUNDLE_NAME = Messages.class.getPackage().getName() + ".messages"; //$NON-NLS-1$

    private final static Log LOG = LogFactory.getLog(Messages.class);

    private final static String PAT_PLUGIN = "PAT Plugin: ";
    private Messages() {
    }

    public static ResourceBundle getResourceBundle(String bundleName) {
        ResourceBundle bundle = null;
        try {
            Locale locale = LocaleHelper.getLocale();
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
            return getResourceBundle(BUNDLE_NAME).getString(key);
        } catch (Exception e) {
            return '!' + key + '!';
        }
    }
    
    public static String getString(final String key, final String... params) {
        try {
          return (PAT_PLUGIN + MessageFormat.format(getResourceBundle(BUNDLE_NAME).getString(key), (Object[])params));
        } catch (Exception e) {
          return (PAT_PLUGIN + '!' + key + '!');
        }
      }

}
