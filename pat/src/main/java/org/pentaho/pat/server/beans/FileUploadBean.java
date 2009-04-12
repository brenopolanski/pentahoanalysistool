package org.pentaho.pat.server.beans;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;


public final class FileUploadBean 
{
    private final transient Log log = LogFactory.getLog(getClass());

    private MultipartFile file;

    /**
     * @param _files
     */
    public void setFile(final MultipartFile _file) {
        file = _file;

        log.debug(String.format("files: %s", file)); //$NON-NLS-1$
    }

    /**
     * @return
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
