package com.ttk.common.tags.claims;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.ttk.dto.claims.ClaimUploadErrorLogVO;

public class AutoRejectedClaimActDtls extends TagSupport{
	
	 private static final long serialVersionUID = 1L;
	    
	    private static Logger log = Logger.getLogger( AutoRejectedClaimActDtls.class );
	
	
	    public int doStartTag() throws JspException
		{
	    	
	    	/*ArrayList<ClaimUploadErrorLogVO> alBacthVO=null;
			alBacthVO= (ArrayList<ClaimUploadErrorLogVO>)pageContext.getSession().getAttribute("listOfClaims");*/
	    	
			 HttpServletRequest request=(HttpServletRequest)pageContext.getRequest();
	           String strShortFallType=(String)pageContext.getRequest().getAttribute("activityDetails");
	    	

		        JspWriter out = pageContext.getOut();//Writer object to write the file
		       String gridOddRow="'gridOddRow'";
		       String gridEvenRow="'gridEvenRow'";
	    	
	    	
		   	try {
				out.print("<table border='0' align='center' cellpadding='0' cellspacing='0' class='gridWithCheckBox'  style='width:750px;height:auto;'");
				out.print("<tr>");
				out.print("<th align='center' class='gridHeader'>Parent Claim Settlement NO.</th>");
				out.print("<th align='center' class='gridHeader'>Provider Invoice No.</th>");
				out.print("<th align='center' class='gridHeader'>Claim No.</th>");
				out.print("<th align='center' class='gridHeader'>Requested Amount</th>");
				out.print("<th align='center' class='gridHeader'>Delete</th>");
				out.print("</tr>");	
		   	
		   	
		   	
		   	
		   	
		   	
		   	
		   	
		   	
		   	
		   	
		   	} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				  
	    	
	    	
	    	
	    	
	    	
			return 0;
	    	
	    	
	    	
	    	
	    	
	    	
		}
	

}
