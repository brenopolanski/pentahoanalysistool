package org.pentaho.pat.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.pat.server.util.AbstractSchemaValidator;
import org.pentaho.pat.server.util.Base64Coder;
import org.pentaho.platform.web.servlet.IUploadFileServletPlugin;


public class PatSchemaUploader implements IUploadFileServletPlugin {

    private static final String DATA_START = "[SCHEMA_START]"; //$NON-NLS-1$

    private static final String DATA_END = "[/SCHEMA_END]"; //$NON-NLS-1$

    private static final String VALIDATION_START = "[VALIDATION_START]"; //$NON-NLS-1$

    private static final String VALIDATION_END = "[/VALIDATION_END]"; //$NON-NLS-1$

    private final static Log LOG = LogFactory.getLog(PatSchemaUploader.class);
    
    public String getFileExtension() {
        return ".mondrian.xml";
    }

    public long getMaxFileSize() {
        // TODO Auto-generated method stub
        return 300000;
    }

    public long getMaxFolderSize() {
        // TODO Auto-generated method stub
        return 300000;
    }

    public String getTargetFolder() {
        return "/system/pat-plugin/schema_temp/"; 
    }

    public void onSuccess(String filePath, HttpServletResponse response) {
        // TODO Auto-generated method stub
        try {
            
        File schemafile = new File(filePath);
        FileInputStream fis = new FileInputStream (schemafile);
        byte[] b = new byte[fis.available()];
        fis.read(b);
        fis.close();
        
        String schemaData = new String(b);
        String validationResult = AbstractSchemaValidator.validateAgainstXsd(schemaData);
        
        response.setContentType("text/plain"); //$NON-NLS-1$
        
        if (validationResult == null)
        {
            validationResult = ""; //$NON-NLS-1$
        }
            
        response.getWriter().print(VALIDATION_START + validationResult + VALIDATION_END);
        // Send a confirmation message to the client
        
        response.getWriter().print(DATA_START + (new String(Base64Coder.encode(b))) + DATA_END);
        response.setStatus(HttpServletResponse.SC_OK);
    } catch (Exception e) {
        LOG.error(e.getMessage());
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    // return the result to the client
    try {
        response.flushBuffer();
    } catch (IOException e) {
        LOG.error(e.getMessage(),e);
    }
       

        
    }

}
