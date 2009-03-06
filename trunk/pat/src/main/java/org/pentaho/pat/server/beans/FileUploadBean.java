package org.pentaho.pat.server.beans;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.list.LazyList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile; 
import org.apache.commons.lang.builder.ReflectionToStringBuilder;


public final class FileUploadBean {
    private final transient Log log = LogFactory.getLog(getClass());

    private final transient Factory multipartFileFactory = new Factory() {
        public Object create() {
            return new Object();
        }
    };

    private MultipartFile file;

    /**
     * @param _files
     */
    public void setFile(final MultipartFile _file) {
        file = _file;

        log.debug(String.format("files: %s", file));
    }

    /**
     * @return
     */
    public MultipartFile getFile() {
        log.debug(String.format("files: %s", file));

        return file;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
