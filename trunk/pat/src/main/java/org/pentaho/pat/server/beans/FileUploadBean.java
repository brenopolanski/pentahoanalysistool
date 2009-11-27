/*
 * Copyright (C) 2009 Luc Boudreau
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
package org.pentaho.pat.server.beans;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;

public final class FileUploadBean {
    private final  Log log = LogFactory.getLog(getClass());

    private MultipartFile file;

    /**
     * @param _file
     */
    public void setFile(final MultipartFile _file) {
        file = _file;

        log.debug(String.format("files: %s", file)); //$NON-NLS-1$
    }

    /**
     * @return file
     */
    public MultipartFile getFile() {
        log.debug(String.format("files: %s", file)); //$NON-NLS-1$

        return file;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
