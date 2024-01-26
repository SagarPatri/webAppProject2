
package com.ttk.action.onlineforms;

import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ttk.action.TTKAction;

public class GetImageAction extends TTKAction{ 
    public ActionForward  dohelloImage(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        @SuppressWarnings("unchecked")    
        InputStream in=(InputStream) request.getSession().getAttribute("alImg");
    	ServletOutputStream out = response.getOutputStream();
    	 response.setContentType("image/png");
    	 response.setHeader("Content-Disposition","inline;filename=image.png");
    	byte[] buf = new byte[10*1024];
    	int len;
    	while ((len = in.read(buf)) > 0) {
    	out.write(buf, 0, len);
    	}   	
    	in.close();
    	out.flush();
    	out.close();      
     return null;
    }
   
}