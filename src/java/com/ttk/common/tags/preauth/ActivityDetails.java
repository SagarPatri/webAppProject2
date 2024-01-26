/**
 * @ (#) ActivityDetails.java July 18, 2015
 * Project : ProjectX
 * File : ActivityDetails.java
 * Author : Nagababu K
 * Company :RCS Technologies
 * Date Created : July 18, 2015
 *
 * @author : Nagababu K
 * Modified by :
 * Modified date :
 * Reason :
*/
package com.ttk.common.tags.preauth;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.ttk.common.TTKCommon;
import com.ttk.dto.preauth.ActivityDetailsVO;
//import org.apache.log4j.Logger;

public class ActivityDetails extends TagSupport
{
	private String flow;
	/**
	* Comment for <code>serialVersionUID</code>
	*/
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger( ActivityDetails.class );
	
	public int doStartTag() throws JspException{
	ArrayList<ActivityDetailsVO> alActivityDetails=null;
	
		try
		{
			if("CLM".equalsIgnoreCase(getFlow()))
			  alActivityDetails=(ArrayList<ActivityDetailsVO>)pageContext.getSession().getAttribute("claimActivities");
			else if("PAT".equalsIgnoreCase(getFlow()))
			 alActivityDetails= (ArrayList<ActivityDetailsVO>)pageContext.getSession().getAttribute("preauthActivities");
		  String activelink = (String)pageContext.getSession().getAttribute("ACTIVELINK");		
	        JspWriter out = pageContext.getOut();//Writer Object to write the File
	       String gridOddRow="'gridOddRow'";
	       String gridEvenRow="'gridEvenRow'";
	        if(alActivityDetails != null){
	        	if(alActivityDetails.size()>=1){
	        		if("PAT".equals(getFlow())){
	        				        	
	                		    out.print("<table border='0' align='center' cellpadding='0' cellspacing='0' class='gridWithCheckBox'  style='width:100%;height:auto;'>");
	                			out.print("<tr>");
	                			out.print("<th align='center' class='gridHeader' title='Serial Number'>S.No</th>");
	                			out.print("<th align='center' class='gridHeader' title='Activity Code/Service Code'>Act/Serv Code</th>");
	                			//out.print("<th align='center' class='gridHeader' title='Modifier'>Mod</th>");
	                			//out.print("<th align='center' class='gridHeader' title='Unit Type'>Unt Type</th>");	                			
	                			out.print("<th align='center' class='gridHeader' title='Quantity'>Qty</th>");
	                			out.print("<th align='center' class='gridHeader' title='Approved Quantity'>Apr Qty</th>");
	                			out.print("<th align='center' class='gridHeader' title='Start Date'>Str Date</th>");
	                			out.print("<th align='center' class='gridHeader' title='Gross Amount'>Grs Amt</th>");
	                			out.print("<th align='center' class='gridHeader' title='Discount Amount'>Disc Amt</th>");
	                			out.print("<th align='center' class='gridHeader' title='Discount Gross Amount'>Disc Grs</th>");
	                			out.print("<th align='center' class='gridHeader' title='Patient Share'>Ptn Share</th>");
	                			out.print("<th align='center' class='gridHeader' title='Net Amount'>Net Amt</th>");
	                			out.print("<th align='center' class='gridHeader' title='Dis Allowed Amount'>DisAlw Amt</th>");
	                			out.print("<th align='center' class='gridHeader' title='Allowed Amount'>Alw Amt</th>");
	                			out.print("<th align='center' class='gridHeader' title='Denial Code'>Den Code</th>");
	                			//out.print("<th align='center' class='gridHeader' title='Remarks'>Remarks</th>");
	                			out.print("<th align='center' class='gridHeader' title='Observations'>Observ</th>");
	                			out.print("<th align='center' class='gridHeader' title='Delete Activity Code'>Delt</th>");
	                			out.print("<th align='center' class='gridHeader' title='Override Activity Code'>Ovr</th>");
	                			out.print("</tr>");
	        	
	                			int i=0;
	                			String delImage="<img src='/ttk/images/DeleteIcon.gif' alt='Delete Activity Details' width='12' height='12' border='0'>";
	                			String addImage="<img src='/ttk/images/EditIcon.gif' alt='Add Observ Details' width='16' height='16' border='0' align='absmiddle'>";
	                			String overrideImg="<img src='ttk/images/Overridebutton.gif' alt='Override' width='50' height='17' border='0' align='absbottom'>";
	                			String overriddenImg="<img src='ttk/images/Overridebutton.gif' alt='Overridden' width='50' height='17' border='0' align='absbottom' style='background-color: green;'>";

	                for(ActivityDetailsVO activityDetails:alActivityDetails){
	                		out.print("<tr class="+(i%2==0?gridEvenRow:gridOddRow)+">");
	                		out.print("<td align='cenetr'>"+(i+1)+"</td>");
	                		if(activelink != null && !activelink.equals("CounterFraudDept")){
	                			 if(("Y").compareTo(activityDetails.getEnhancedFlag())==0){
	                				  
	             	       			out.print("<td align='center'><a href='#' accesskey='a' title=\""+activityDetails.getInternalDesc()+"\" onClick=\"javascript:editActivityDetails('"+activityDetails.getActivityDtlSeqId()+"');\">"+activityDetails.getActivityCode()+activityDetails.getActivityServiceCode()+"</a>&nbsp&nbsp<img src='/ttk/images/EnhancedPreauth.gif' alt='Enhanced' width='16' height='16' align='absmiddle'></td>");
	             	  }
	             	  else{
	              			out.print("<td align='center'><a href='#' accesskey='a' title=\""+activityDetails.getInternalDesc()+"\" onClick=\"javascript:editActivityDetails('"+activityDetails.getActivityDtlSeqId()+"');\">"+activityDetails.getActivityCode()+activityDetails.getActivityServiceCode()+"</a>");
	             }

	             	  }
	               
	                else{
	             		  if(("Y").compareTo(activityDetails.getEnhancedFlag())==0){
	             	                			out.print("<td align='center'><a href='#' accesskey='a' title=\""+activityDetails.getInternalDesc()+"\">"+activityDetails.getActivityCode()+activityDetails.getActivityServiceCode()+"</a>&nbsp&nbsp<img src='/ttk/images/EnhancedPreauth.gif' alt='Enhanced' width='16' height='16' align='absmiddle'></td>");
	             		  }
	             		  
	             	 else{
	                 			out.print("<td align='center'><a href='#' accesskey='a' title=\""+activityDetails.getInternalDesc()+"\">"+activityDetails.getActivityCode()+activityDetails.getActivityServiceCode()+"</a></td>");
	                 			  
	             		 }
	                }
	                		//out.print("<td align='center'>"+activityDetails.getModifier()+"</td>");
	                		//out.print("<td align='center'>"+activityDetails.getUnitType()+"</td>");
	                		if("Y".equals(activityDetails.getMophDrugYN())){
	                    		out.print("<td align='center' title=\""+"Claimed quantity is higher than indicated to be reviewed."+"\">" +activityDetails.getQuantity()+"</td>");
	                    		}
	                		else{
	                		out.print("<td align='center'>"+activityDetails.getQuantity()+"</td>");
	                		}
	                		out.print("<td align='center'>"+(activityDetails.getApprovedQuantity())+"</td>");
	                		out.print("<td align='center'>"+activityDetails.getStartDate()+"</td>");
	                		out.print("<td align='center'>"+activityDetails.getGrossAmount()+"</td>");
	                		out.print("<td align='center'>"+activityDetails.getDiscount()+"</td>");
	                		out.print("<td align='center'>"+activityDetails.getDiscountedGross()+"</td>");
	                		out.print("<td align='center'>"+activityDetails.getPatientShare()+"</td>");
	                		out.print("<td align='center'>"+activityDetails.getNetAmount()+"</td>");
	                		out.print("<td align='center'>"+activityDetails.getDisAllowedAmount()+"</td>");
	                		out.print("<td align='center'>"+activityDetails.getApprovedAmount()+"</td>");
	                		out.print("<td align='center'>");
	                		//out.print("<table>");
	                		
	                		String denialDec=activityDetails.getDenialDescription();//denial_desc
	        				String denialCode=activityDetails.getDenialCode();//denial_code
	        				
	        				String []descs=denialDec.split("[;]");
	        				String []codes=denialCode.split("[;]");
	        				if(codes!=null&&codes.length>=1&&descs!=null&&descs.length>=1){
	        				for(int j=0;j<codes.length;j++){
	        					try{
	        					//out.print("<tr style=''><td title=\""+descs[j]+"\">"+codes[j]+"</td></tr>");
	        					if(codes[j].length()>1)	out.println("<div style='text-decoration: underline;color:blue;' title=\""+descs[j]+"\"><a href='#'>"+codes[j]+";</a></div>");
	        						
	        					}catch(Exception e){
	        						System.err.println("Error occurred in ActivityDetails.java cause Activity code description  is not present");
	        						//log.error("Error Activity code description  is not present ", e);
	        						}
	        					}//for(int j=0;j<codes.length;j++)
	        				}//if(codes!=null&&codes.length>=1&&descs!=null&&descs.length>=1)
	        				  
	        				//out.print("</table>");
	        				   out.print("</td>");
	        				   
	        				 // String remarks=activityDetails.getRemarks()==null?"":activityDetails.getActivityRemarks();
	        				 // String rmrks=(remarks.length()>6)?remarks.substring(0, 5)+"...":remarks;
	        				  
	                		//out.print("<td align='center'><a href='#' accesskey='a' title=\""+remarks+"\" style='text-decoration: none;'>"+rmrks+"</a></td>");
	                		
	                		//	activities.add(new String[]{1sNo+"",2activityCode,3modifiers,4unityType,5quantity,6startDate,7grossAmt,8discount,9discountGross,10patientShare,11netAmt,12approAmt,13denialDec,14remarks,15activitySeqId,16activityDtlSeqId,17allowedAmt,18approvedQuantity,19activityCodeDec,20denialCode,21rmrks,22codesAndDescs});
	                //out.print("<td align='center'><a href='#' accesskey='o'  onClick=\"javascript:addObservs('"+activityDetails.getActivityCodeSeqId()+"');\">"+addImage+"</a></td>");
	        		if(activelink != null && !activelink.equals("CounterFraudDept")){	
	        			out.print("<td align='center'><a href='#' accesskey='o'  onClick=\"javascript:addObservs('"+activityDetails.getActivityDtlSeqId()+"');\">"+addImage+"</a></td>");
		                //out.print("<td align='center'><a href='#' accesskey='o'  onClick=\"javascript:deleteActivityDetails('"+activityDetails.getActivityCodeSeqId()+"');\">"+delImage+"</a></td>");
		                out.print("<td align='center'><a href='#' accesskey='o'  onClick=\"javascript:deleteActivityDetails('"+activityDetails.getActivityDtlSeqId()+"');\">"+delImage+"</a></td>");
	                if("Y".equals(activityDetails.getOverrideYN()))
	                out.print("<td align='center' style='background-color: green;'><a href='#' accesskey='a'  onClick=\"javascript:overrideActivityDetails('"+activityDetails.getActivityDtlSeqId()+"');\">"+overriddenImg+"</a></td>");
	                else if(!"Y".equals(activityDetails.getNewPharmacyYN())) 
	                	out.print("<td align='center'><a href='#' accesskey='a'  onClick=\"javascript:overrideActivityDetails('"+activityDetails.getActivityDtlSeqId()+"');\">"+overrideImg+"</a></td>");	
	        		}else{
	        			out.print("<td align='center'><a href='#' accesskey='o'>"+addImage+"</a></td>");
		                out.print("<td align='center'><a href='#' accesskey='o'>"+delImage+"</a></td>");
	        			if("Y".equals(activityDetails.getOverrideYN()))
	    	                out.print("<td align='center' style='background-color: green;'><a href='#' accesskey='a'>"+overriddenImg+"</a></td>");
	    	                else if(!"Y".equals(activityDetails.getNewPharmacyYN())) 
	    	                	out.print("<td align='center'><a href='#' accesskey='a'>"+overrideImg+"</a></td>");		
	        		}
	                out.print("</tr>");
	                		i++;
	                			}//for(ActivityDetailsVO activityDetails:alActivityDetails)
	               
	               out.print("</table>");
	        		}//if("PAT".equals(getFlow()))
	        		else if("CLM".equals(getFlow())){

	    	        	
            		    out.print("<table border='0' align='center' cellpadding='0' cellspacing='0' class='gridWithCheckBox'  style='width:100%;height:auto;'>");
            			out.print("<tr>");
            			out.print("<th align='center' class='gridHeader' title='Serial Number'>S.No</th>");
            			/*out.print("<th align='center' class='gridHeader' title='PreAuth Number'>Pre.No</th>");*/
            			out.print("<th align='center' class='gridHeader' title='Internal Code'>Intr.cd</th>");
            			out.print("<th align='center' class='gridHeader' title='Activity Code'>Act Code</th>");            			
            			out.print("<th align='center' class='gridHeader' title='Unit Type'>Unt Type</th>");
            			out.print("<th align='center' class='gridHeader' title='Quantity'>Qty</th>");
            			out.print("<th align='center' class='gridHeader' title='Approved Quantity'>Apr Qty</th>");
            			out.print("<th align='center' class='gridHeader' title='Start Date'>Str Date</th>");
            			out.print("<th align='center' class='gridHeader' title='Provider Requested Amount'>Req Amt</th>");
            			out.print("<th align='center' class='gridHeader' title='Gross Amount'>Grs Amt</th>");
            			out.print("<th align='center' class='gridHeader' title='Discount Amount'>Disc Amt</th>");
            			out.print("<th align='center' class='gridHeader' title='Discount Gross Amount'>Disc Grs</th>");
            			out.print("<th align='center' class='gridHeader' title='Patient Share'>Ptn Share</th>");            			
            			out.print("<th align='center' class='gridHeader' title='Net Amount'>Net Amt</th>");
            			out.print("<th align='center' class='gridHeader' title='Dis Allowed Amount'>DisAlw Amt</th>");
            			out.print("<th align='center' class='gridHeader' title='Allowed Amount'>Alw Amt</th>");
            			out.print("<th align='center' class='gridHeader' title='Denial Code'>Den Code</th>");
            			//out.print("<th align='center' class='gridHeader' title='Remarks'>Remarks</th>");
            			out.print("<th align='center' class='gridHeader' title='Observations'>Observ</th>");
            			out.print("<th align='center' class='gridHeader' title='Delete Activity Code'>Delt</th>");
            			out.print("<th align='center' class='gridHeader' title='Override Activity Code'>Ovr</th>");
            			out.print("</tr>");
    	
            			int i=0;
            			String delImage="<img src='/ttk/images/DeleteIcon.gif' alt='Delete Activity Details' width='12' height='12' border='0'>";
            			String addImage="<img src='/ttk/images/EditIcon.gif' alt='Add Observ Details' width='16' height='16' border='0' align='absmiddle'>";
            			String overrideImg="<img src='ttk/images/Overridebutton.gif' alt='Override' width='50' height='17' border='0' align='absbottom'>";
            			String overriddenImg="<img src='ttk/images/Overridebutton.gif' alt='Overridden' width='50' height='17' border='0' align='absbottom' style='background-color: green;'>";

            for(ActivityDetailsVO activityDetails:alActivityDetails){
            		out.print("<tr class="+(i%2==0?gridEvenRow:gridOddRow)+">");
            		out.print("<td align='cenetr'>"+(i+1)+"</td>");
            		/*out.print("<td align='center'>"+activityDetails.getPreAuthNo()+"</td>");*/
            		out.print("<td align='center'>"+activityDetails.getInternalCode()+"</td>");
            		if(activelink != null && !activelink.equals("CounterFraudDept")){
            		out.print("<td align='center'><a href='#' accesskey='a' title=\""+activityDetails.getInternalDesc()+"\" onClick=\"javascript:editActivityDetails('"+activityDetails.getActivityDtlSeqId()+"');\">"+activityDetails.getActivityCode()+"</a></td>");  
            		}else{
            			out.print("<td align='center'><a href='#' accesskey='a' title=\""+activityDetails.getInternalDesc()+"\">"+activityDetails.getActivityCode()+"</a></td>");	
            		}
            		out.print("<td align='center'>"+activityDetails.getUnitType()+"</td>");
            		if("Y".equals(activityDetails.getMophDrugYN())){
            		out.print("<td align='center' title=\""+"Claimed quantity is higher than indicated to be reviewed."+"\">" +activityDetails.getQuantity()+"</td>");
            		}
            		else{
                		out.print("<td align='center'>"+activityDetails.getQuantity()+"</td>");
                		}
            		out.print("<td align='center'>"+(activityDetails.getApprovedQuantity())+"</td>");
            		out.print("<td align='center'>"+activityDetails.getStartDate()+"</td>");
            		out.print("<td align='center'>"+activityDetails.getProviderRequestedAmt()+"</td>");
            		out.print("<td align='center'>"+activityDetails.getGrossAmount()+"</td>");
            		out.print("<td align='center'>"+activityDetails.getDiscount()+"</td>");
            		out.print("<td align='center'>"+activityDetails.getDiscountedGross()+"</td>");
            		out.print("<td align='center'>"+activityDetails.getPatientShare()+"</td>");            		
            		out.print("<td align='center'>"+activityDetails.getNetAmount()+"</td>");
            		out.print("<td align='center'>"+activityDetails.getDisAllowedAmount()+"</td>");
            		out.print("<td align='center'>"+activityDetails.getApprovedAmount()+"</td>");
            		out.print("<td align='center'>");
            		//out.print("<table>");
            		
            		String denialDec=activityDetails.getDenialDescription();//denial_desc
    				String denialCode=activityDetails.getDenialCode();//denial_code
    				
    				String []descs=denialDec.split("[;]");
    				String []codes=denialCode.split("[;]");
    				if(codes!=null&&codes.length>=1&&descs!=null&&descs.length>=1){
    				for(int j=0;j<codes.length;j++){
    					try{
    					//out.print("<tr style=''><td title=\""+descs[j]+"\">"+codes[j]+"</td></tr>");
    					if(codes[j].length()>1)	out.println("<div style='text-decoration: underline;color:blue;' title=\""+descs[j]+"\"><a href='#'>"+codes[j]+";</a></div>");
    						
    					}catch(Exception e){
    						System.err.println("Error occurred in ActivityDetails.java cause Activity code description  is not present");
    						//log.error("Error Activity code description  is not present ", e);
    						}
    					}//for(int j=0;j<codes.length;j++)
    				}//if(codes!=null&&codes.length>=1&&descs!=null&&descs.length>=1)
    				  
    				//out.print("</table>");
    				   out.print("</td>");
    				   
    				 // String remarks=activityDetails.getRemarks()==null?"":activityDetails.getActivityRemarks();
    				 // String rmrks=(remarks.length()>6)?remarks.substring(0, 5)+"...":remarks;
    				  
            		//out.print("<td align='center'><a href='#' accesskey='a' title=\""+remarks+"\" style='text-decoration: none;'>"+rmrks+"</a></td>");
            		
            		//	activities.add(new String[]{1sNo+"",2activityCode,3modifiers,4unityType,5quantity,6startDate,7grossAmt,8discount,9discountGross,10patientShare,11netAmt,12approAmt,13denialDec,14remarks,15activitySeqId,16activityDtlSeqId,17allowedAmt,18approvedQuantity,19activityCodeDec,20denialCode,21rmrks,22codesAndDescs});
            //out.print("<td align='center'><a href='#' accesskey='o'  onClick=\"javascript:addObservs('"+activityDetails.getActivityCodeSeqId()+"');\">"+addImage+"</a></td>");
    		if(activelink != null && !activelink.equals("CounterFraudDept")){		   
            out.print("<td align='center'><a href='#' accesskey='o'  onClick=\"javascript:addObservs('"+activityDetails.getActivityDtlSeqId()+"');\">"+addImage+"</a></td>");
            out.print("<td align='center'><a href='#' accesskey='o'  onClick=\"javascript:deleteActivityDetails('"+activityDetails.getActivityDtlSeqId()+"');\">"+delImage+"</a></td>");
            if("Y".equals(activityDetails.getOverrideYN()))
                out.print("<td align='center' style='background-color: green;'><a href='#' accesskey='a'  onClick=\"javascript:overrideActivityDetails('"+activityDetails.getActivityDtlSeqId()+"');\">"+overriddenImg+"</a></td>");
                else if(!"Y".equals(activityDetails.getNewPharmacyYN())) out.print("<td align='center'><a href='#' accesskey='a'  onClick=\"javascript:overrideActivityDetails('"+activityDetails.getActivityDtlSeqId()+"');\">"+overrideImg+"</a></td>");	
    		}else{
    			out.print("<td align='center'><a href='#' accesskey='o'>"+addImage+"</a></td>");
    			out.print("<td align='center'><a href='#' accesskey='o'>"+delImage+"</a></td>");
    			if("Y".equals(activityDetails.getOverrideYN()))
    	            out.print("<td align='center' style='background-color: green;'><a href='#' accesskey='a'>"+overriddenImg+"</a></td>");
    	            else if(!"Y".equals(activityDetails.getNewPharmacyYN())) out.print("<td align='center'><a href='#' accesskey='a'>"+overrideImg+"</a></td>");	
    		}
            //out.print("<td align='center'><a href='#' accesskey='o'  onClick=\"javascript:deleteActivityDetails('"+activityDetails.getActivityCodeSeqId()+"');\">"+delImage+"</a></td>");
            
            
            out.print("</tr>");
            		i++;
            			}//for(ActivityDetailsVO activityDetails:alActivityDetails)
           out.print("</table>");    		
	        		}
	        	}//if(alActivityDetails.size()>=1)
	        }//if(alActivityDetails != null)
		}//end of try
		catch(Exception exp)
        {
            exp.printStackTrace();
        }//end of catch block
        return SKIP_BODY;
	}//end of doStartTag()
	public int doEndTag() throws JspException 
	{
		return EVAL_PAGE;//to process the rest of the page
	}//end doEndTag()
	public String getFlow() {
		return flow;
	}
	public void setFlow(String flow) {
		this.flow = flow;
	}
}//end of DiagnosisDetails class 
