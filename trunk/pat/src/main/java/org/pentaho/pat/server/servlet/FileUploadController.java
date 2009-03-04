package org.pentaho.pat.server.servlet;
import java.io.File; 
import java.io.IOException; 
import org.springframework.stereotype.Controller; 
import org.springframework.ui.ModelMap; 
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RequestMethod; 
import org.springframework.web.bind.annotation.RequestParam; 
import org.springframework.web.multipart.MultipartFile; 
import org.springframework.web.servlet.ModelAndView; 
 
@Controller 
public class FileUploadController { 
 
    private File basedir; 

    public void setBasedir(File basedir) {
        this.basedir = basedir;
}

//        @SuppressWarnings("unchecked") 
        @RequestMapping(value = "/schemaupload", method = RequestMethod.POST) 
        public ModelAndView postUploadForm(@RequestParam(value = "file") MultipartFile file) { 
                ModelAndView mav = new ModelAndView(); 
                try { 
            file.transferTo(new File(this.basedir, "temp.test")); 
        } catch (IllegalStateException e1) { 
            // TODO Auto-generated catch block 
            e1.printStackTrace(); 
        } catch (IOException e1) { 
            // TODO Auto-generated catch block 
            e1.printStackTrace(); 
        } 
                
        mav.setViewName("success"); 
//       
//        ModelMap map = mav.getModelMap(); 
//                
//                try { 
//                        map.put("testObj", new String(file.getBytes()) + " " + file.getSize()); 
//                } catch (IOException e) { 
//                        e.printStackTrace(); 
//                } 
//                
//                map.put("tasks", this.taskDao.list()); 
//                
                return mav; 
        } 

} 
