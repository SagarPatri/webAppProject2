package com.ttk.common.tags.processedcliams;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.ttk.dto.preauth.ActivityDetailsVO;

public class ActivityDetails extends TagSupport{
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger( ActivityDetails.class );
	//private String flow;
	@SuppressWarnings("unchecked")
	public int doStartTag() throws JspException{
		
		ArrayList<ActivityDetailsVO> alActivityDetails=null;
		
		 JspWriter out = pageContext.getOut();//Writer Object to write the File
	       String gridOddRow="'gridOddRow'";
	       String gridEvenRow="'gridEvenRow'";
		
	       alActivityDetails=(ArrayList<ActivityDetailsVO>)pageContext.getSession().getAttribute("claimActivities");
		
	       
	       try {
	    	out.print("<fieldset>");
			out.print("<legend>Activity Details</legend>");
			out.print("<table border='0' align='center' cellpadding='0' cellspacing='0' class='gridWithCheckBox'  style='width:100%;height:auto;'>");
			out.print("<tr>");
			out.print("<th align='center' class='gridHeader' title='Serial Number'>S.No</th>");
			out.print("<th align='center' class='gridHeader' title='Internal Code'>Intr Code</th>");
			out.print("<th align='center' class='gridHeader' title='Activity Code/Service Code'>Act Code</th>");
			out.print("<th align='center' class='gridHeader' title='Activity Type'>Act Type</th>");
			out.print("<th align='center' class='gridHeader' title='Activity Quantity'>Act Qty</th>");
			out.print("<th align='center' class='gridHeader' title='Approved Quantity'>Appr Qty</th>");
			out.print("<th align='center' class='gridHeader' title='Service Date'>Serv Date</th>");
			out.print("<th align='center' class='gridHeader' title='Requested Amount'>Req Amnt</th>");
			out.print("<th align='center' class='gridHeader' title='Gross Amount'>Grs Amt</th>");
			out.print("<th align='center' class='gridHeader' title='Discount Amount'>Disc Amt</th>");
			out.print("<th align='center' class='gridHeader' title='Discount Gross Amount'>Disc Grs</th>");
			out.print("<th align='center' class='gridHeader' title='Patient Share'>Ptn Share</th>");
			out.print("<th align='center' class='gridHeader' title='Net Amount'>Net Amt</th>");
			out.print("<th align='center' class='gridHeader' title='Dis Allowed Amount'>DisAlw Amt</th>");
			out.print("<th align='center' class='gridHeader' title='Allowed Amount'>Alw Amt</th>");
			out.print("<th align='center' class='gridHeader' title='Denial Code'>Den Code</th>");
			out.print("<th align='center' class='gridHeader' title='Override Date'>Ovr Date</th>");
			out.print("<th align='center' class='gridHeader' title='Approved Amount'>Appr Amnt</th>");
		
			out.print("</tr>");
			int i=0;
			 if(alActivityDetails != null)
		        {
			 for(ActivityDetailsVO activityDetails:alActivityDetails){
				 
				out.print("<tr class="+(i%2==0?gridEvenRow:gridOddRow)+">");
         		out.print("<td align='cenetr'>"+(i+1)+"</td>");
         		
         		out.print("<td align='center'>");
         		String denialDec=activityDetails.getInternal_description();
				String denialCode=activityDetails.getInternal_Code();
				
				String []descs=denialDec.split("[;]");
				String []codes=denialCode.split("[;]");
				if(codes!=null&&codes.length>=1&&descs!=null&&descs.length>=1){
				for(int j=0;j<codes.length;j++){
					try{
					if(codes[j].length()>1)	out.println("<div style='text-decoration: underline;color:blue;' title=\""+descs[j]+"\"><a href='#'>"+codes[j]+";</a></div>");
						
					}catch(Exception e){
						System.err.println("Error occurred in ActivityDetails.java cause Internal code description  is not present");
						}
					}
				}
				  
				   out.print("</td>");
				   out.print("<td align='center'>");
	         		String denialDec1=activityDetails.getActivity_description();
					String denialCode1=activityDetails.getActivity_Code();
					
					String []descs1=denialDec1.split("[;]");
					String []codes1=denialCode1.split("[;]");
					if(codes1!=null&&codes1.length>=1&&descs1!=null&&descs1.length>=1){
					for(int j=0;j<codes1.length;j++){
						try{
						if(codes1[j].length()>1)	out.println("<div style='text-decoration: underline;color:blue;' title=\""+descs1[j]+"\"><a href='#'>"+codes1[j]+";</a></div>");
							
						}catch(Exception e){
							System.err.println("Error occurred in ActivityDetails.java cause Internal code description  is not present");
							}
						}
					}
					   out.print("</td>");
         		
         		out.print("<td align='center'>"+activityDetails.getActivity_Type()+"</td>");
         		out.print("<td align='center'>"+activityDetails.getActivity_Quantity()+"</td>");
         		out.print("<td align='center'>"+activityDetails.getApproved_Quantity()+"</td>");
         		out.print("<td align='center'>"+activityDetails.getService_Date()+"</td>");
         		out.print("<td align='center'>"+activityDetails.getRequested_Amount()+"</td>");
         		out.print("<td align='center'>"+activityDetails.getGross_amount()+"</td>");
         		out.print("<td align='center'>"+activityDetails.getDiscounted_amount()+"</td>");
         		out.print("<td align='center'>"+activityDetails.getDiscounted_Gross_amount()+"</td>");
         		out.print("<td align='center'>"+activityDetails.getPatient_Share()+"</td>");
         		out.print("<td align='center'>"+activityDetails.getNet_amount()+"</td>");
         		out.print("<td align='center'>"+activityDetails.getDisallowed_Amount()+"</td>");
         		out.print("<td align='center'>"+activityDetails.getAllowed_Amount()+"</td>");
         		
         		out.print("<td align='center'>");
         		String denialDec2=activityDetails.getDenial_description();//denial_desc
				String denialCode2=activityDetails.getDenial_Code();//denial_code
				
				String []descs2=denialDec2.split("[;]");
				String []codes2=denialCode2.split("[;]");
				if(codes2!=null&&codes2.length>=1&&descs2!=null&&descs2.length>=1){
				for(int j=0;j<codes2.length;j++){
					try{
					if(codes2[j].length()>1)	out.println("<div style='text-decoration: underline;color:blue;' title=\""+descs2[j]+"\"><a href='#'>"+codes2[j]+";</a></div>");
						
					}catch(Exception e){
						System.err.println("Error occurred in ActivityDetails.java cause Activity code description  is not present");
						}
					}
				}
				   out.print("</td>");
				   
				   out.print("<td align='center'>"+activityDetails.getOver_ride_Date()+"</td>");
	         		out.print("<td align='center'>"+activityDetails.getApproved_amount()+"</td>");
				   
				   out.print("</tr>");
           		i++;
           			}//for(ActivityDetailsVO activityDetails:alActivityDetails)
			  
	       }
			 
			 out.print("</table>");  
			  out.print("</fieldset>");
			 
			 
			 
			 
	       }
	       catch (IOException e) {
			e.printStackTrace();
		}
	       return SKIP_BODY;	
	}
	
/*	public int doEndTag() throws JspException 
	{
		return EVAL_PAGE;//to process the rest of the page
	}*/
	
	//end doEndTag()
	/*public String getFlow() {
		return flow;
	}
	public void setFlow(String flow) {
		this.flow = flow;
	}	*/

}
