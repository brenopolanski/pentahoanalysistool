/*
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License, version 2 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/gpl-2.0.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 *
 * Copyright 2009 Pentaho Corporation.  All rights reserved. 
 * 
 */

package org.pentaho.pat.plugin;

import java.io.InputStream;

import org.dom4j.Document;
import org.pentaho.platform.api.engine.IFileInfo;
import org.pentaho.platform.api.engine.ISolutionFile;
import org.pentaho.platform.api.engine.SolutionFileMetaAdapter;
import org.pentaho.platform.engine.core.solution.FileInfo;
import org.pentaho.platform.util.xml.dom4j.XmlDom4JHelper;

/**
 * Retrieve content metadata from the .xpav content file.
 * @author gmoran
 *
 */
public class PatContentTypeMetaProvider extends SolutionFileMetaAdapter {

  public IFileInfo getFileInfo(ISolutionFile solutionFile, InputStream in) {
    try {
      Document doc = XmlDom4JHelper.getDocFromStream(in);
      if (doc == null) {
        return null;
      }

      String author = XmlDom4JHelper.getNodeText( "/pav/fileinfo/@author", doc, "");  //$NON-NLS-1$ //$NON-NLS-2$
      String description = XmlDom4JHelper.getNodeText( "/pav/fileinfo/@description", doc, "");  //$NON-NLS-1$ //$NON-NLS-2$
      String icon = XmlDom4JHelper.getNodeText( "/pav/fileinfo/@icon", doc, "");  //$NON-NLS-1$ //$NON-NLS-2$
      String title = XmlDom4JHelper.getNodeText( "/pav/fileinfo/@title", doc, "");  //$NON-NLS-1$ //$NON-NLS-2$


      IFileInfo info = new FileInfo();
      info.setAuthor(author);
      info.setDescription(description);
      info.setIcon(icon);
      info.setTitle(title);
      return info;

    } catch (Exception e) {
      if (logger != null) {
        logger.error(getClass().toString(), e);
      }
      return null;
    }
  }
}
