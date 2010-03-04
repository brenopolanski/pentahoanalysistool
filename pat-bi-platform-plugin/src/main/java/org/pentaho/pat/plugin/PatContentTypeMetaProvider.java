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

package org.pentaho.pat.plugin;

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.pentaho.pat.plugin.messages.Messages;
import org.pentaho.pat.plugin.util.PatSolutionFile;
import org.pentaho.platform.api.engine.IFileInfo;
import org.pentaho.platform.api.engine.ISolutionFile;
import org.pentaho.platform.api.engine.SolutionFileMetaAdapter;
import org.pentaho.platform.engine.core.solution.FileInfo;
import org.pentaho.platform.util.xml.dom4j.XmlDom4JHelper;

/**
 * Retrieve content metadata from the .xpav content file.
 * 
 * @author Paul Stoellberger
 */
public class PatContentTypeMetaProvider extends SolutionFileMetaAdapter {

    private static final Log LOG = LogFactory.getLog(PatContentTypeMetaProvider.class);

    public IFileInfo getFileInfo(ISolutionFile solutionFile, InputStream in) {
        try {
            Document doc = XmlDom4JHelper.getDocFromStream(in);
            if (doc == null) {
                LOG.error(Messages.getString("ContentTypeMetaProvider.CantParseXpavFile")); //$NON-NLS-1$
                return null;
            }
            PatSolutionFile patFile = PatSolutionFile.convertDocument(doc);

            String title = patFile.getTitle();
            String author = patFile.getAuthor();
            String description = patFile.getDescription();
            String icon = patFile.getIcon();

            IFileInfo info = new FileInfo();
            info.setAuthor(author);
            info.setDescription(description);
            info.setIcon(icon);
            info.setTitle(title);
            return info;

        } catch (Exception e) {
            LOG.error(getClass().toString(), e);
        }
        return null;
    }
}
