/**
 * @ (#) OnlineActivityDetails.java July 18, 2015
 * Project : ProjectX
 * File : OnlineActivityDetails.java
 * Author : Kishor Kumar S H
 * Company :RCS Technologies
 * Date Created : 22nd Mar 2016
 *
 * @author : Kishor Kumar S H
 * Modified by :
 * Modified date :
 * Reason :
*/
package com.ttk.common.tags.hospital;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.ttk.dto.preauth.ActivityDetailsVO;
//import org.apache.log4j.Logger;

public class OnlineActivityDetails extends TagSupport
{
	private String flow;
	private String flag;
	private String preAuthNoYN;
	/**
	* Comment for <code>serialVersionUID</code>
	*/
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger( OnlineActivityDetails.class );
	
	public int doStartTag() throws JspException{
	ArrayList<ActivityDetailsVO> alActivityDetails=null;
	
		try
		{
			double activitynetSum = 0.0;
			long activitytotalsumcount=0;
			if(!"PATCreate".equals(getFlag())&&!"MEMPATCreate".equals(getFlag())){
			if("CLM".equalsIgnoreCase(getFlow()))
			  alActivityDetails=(ArrayList<ActivityDetailsVO>)pageContext.getSession().getAttribute("claimActivities");
			else if("PAT".equalsIgnoreCase(getFlow()))
			 alActivityDetails= (ArrayList<ActivityDetailsVO>)pageContext.getSession().getAttribute("preauthActivities");
				
	        JspWriter out = pageContext.getOut();//Writer Object to write the File
	       String gridOddRow="'gridOddRow'";
	       String gridEvenRow="'gridEvenRow'";
	        if(alActivityDetails != null){
	        	if(alActivityDetails.size()>=1){out.print("<br>");
	                		out.print("<table border='0' align='center' cellpadding='0' cellspacing='0' class='gridWithCheckBox'  style='width:100%;height:auto;'>");
	                			out.print("<tr>");
	                			out.print("<th width='5%' align='center' class='gridHeader' title='Serial Number'>S.No</th>");
	                			out.print("<th width='10%' align='center' class='gridHeader' title='Activity Code'>Act Code</th>");
	                			out.print("<th width='20%' align='center' class='gridHeader' title='Activity Description'>Activity Desc</th>");
	                			out.print("<th width='10%' align='center' class='gridHeader' title='Internal Code'>Int Code</th>");
	                			out.print("<th width='15%' align='center' class='gridHeader' title='Internal Description'>Int Desc</th>");
	                			out.print("<th width='5%' align='center' class='gridHeader' title='Unit Price'>Unit Price</th>");
	                			out.print("<th width='10%' align='center' class='gridHeader' title='Gross Price'>Gross Price</th>");
	                			out.print("<th align='center' class='gridHeader' title='Discount Amount'>Disc Amt</th>");
	                			out.print("<th align='center' class='gridHeader' title='Quantity Claimed'>Qty Clmd</th>");
	                			/*out.print("<th align='center' class='gridHeader' title='Total Approved Amount'>Total Apr Amt</th>");*/
	                			out.print("<th align='center' class='gridHeader' title='Net Amount After Discount'>Net Amt</th>");
	                			if("N".equals(getPreAuthNoYN()) || "E".equals(getPreAuthNoYN()))
	                				out.print("<th width='5%'align='center' class='gridHeader' title='Delete'>Delete</th>");
	                			out.print("</tr>");
	        	
	                			int i=0;
	                			String delImage="<img src='/ttk/images/DeleteIcon.gif' alt='Delete Activity Details'width='12' height='12' border='0'>";

	                for(ActivityDetailsVO activityDetails:alActivityDetails){
	                		out.print("<tr class="+(i%2==0?gridEvenRow:gridOddRow)+">");
	                		out.print("<td align='cenetr'> <font size='2'>"+(i+1)+"</font></td>");
	                		out.print("<td align='center'>"+activityDetails.getActivityCode()+"</td>");
	                		out.print("<td align='center'>"+activityDetails.getActivityCodeDesc()+"</td>");
	                		out.print("<td align='center'>"+activityDetails.getInternalCode()+"</td>");
	                		out.print("<td align='center'>"+activityDetails.getInternalDesc()+"</td>");
	                		out.print("<td align='center'>"+activityDetails.getUnitPrice()+"</td>");
	                		out.print("<td align='center'>"+activityDetails.getGrossAmount()+"</td>");
	                		out.print("<td align='center'>"+activityDetails.getDiscount()+"</td>");
	                		out.print("<td align='center'>"+(activityDetails.getQuantity())+"</td>");
	                		/*out.print("<td align='center'>"+(activityDetails.getApprovedAmount())+"</td>");*/
	                		out.print("<td align='center' id='netAmountid'>"+(activityDetails.getNetAmount())+"</td>");
	                		activitynetSum = activitynetSum + Double.parseDouble(String.valueOf(activityDetails.getNetAmount()));
	                		activitytotalsumcount = activitytotalsumcount + activityDetails.getActivityicdcountflag();
	                		if("N".equals(getPreAuthNoYN()) || "E".equals(getPreAuthNoYN()))
	                			out.print("<td align='center'><a href='#' accesskey='d'  onClick=\"javascript:deleteActivityDetails('"+i+"');\">"+delImage+"</a></td>");
	                		
	                		out.print("</tr>");
	                		i++;
	                			}//for(ActivityDetailsVO activityDetails:alActivityDetails)
	                out.print("<input type='hidden' name='netamountidactivity' id='netamountidactivity' value="+activitynetSum+">");
	                out.print("<input type='hidden' name='activitytotalsumcount' id='activitytotalsumcountid' value="+activitytotalsumcount+">");
	                
	               out.print("</table>");
	        	}//if(alActivityDetails.size()>=1)
	        }//if(alActivityDetails != null)
			}else{
				
				if("CLM".equalsIgnoreCase(getFlow()))
					  alActivityDetails=(ArrayList<ActivityDetailsVO>)pageContext.getSession().getAttribute("claimActivities");
					else if("PAT".equalsIgnoreCase(getFlow()))
					 alActivityDetails= (ArrayList<ActivityDetailsVO>)pageContext.getSession().getAttribute("preauthActivities");
						
			       JspWriter out = pageContext.getOut();//Writer Object to write the File
			       String gridOddRow="'gridOddRow'";
			       String gridEvenRow="'gridEvenRow'";
			        if(alActivityDetails != null){
			        	if("MEMPATCreate".equals(getFlag())){
			        		if(alActivityDetails.size()>=1){
		                		out.print("<table border='0' align='center' cellpadding='0' cellspacing='0' class='gridWithCheckBox'  style='width:98%;height:auto;'>");
		                			out.print("<tr>");
		                			out.print("<th align='center' class='gridHeader' title='Activity Code'>Activity Code</th>");
		                			out.print("<th align='center' class='gridHeader' title='Activity Code'>Activity Description</th>");
		                			out.print("<th align='center' class='gridHeader' title='Internal Code'>Service Date</th>");
		                			out.print("<th align='center' class='gridHeader' title='Quantity'>Quantity</th>");
		                			out.print("<th align='center' class='gridHeader' title='Request Amount'>Reqested Amount</th>");
		                			out.print("<th align='center' class='gridHeader' title='Patient Share'>Patient Share</th>");
		                			out.print("<th align='center' class='gridHeader' title='Status'>DisAllowed Amount</th>");
		                			out.print("<th align='center' class='gridHeader' title='Status'>Approved Amount</th>");
		                			out.print("<th align='center' class='gridHeader' title='Comments'>Reason for Denial</th>");
		                			out.print("</tr>");
		                			int i=0;
		                for(ActivityDetailsVO activityDetails:alActivityDetails){
		                		out.print("<tr class="+(i%2==0?gridEvenRow:gridOddRow)+">");
		                		/*out.print("<td align='cenetr'> <font size='2'>"+(i+1)+"</font></td>");*/
		                		out.print("<td align='center'>"+activityDetails.getActivityCode()+"</td>");
		                		out.print("<td align='center'>"+activityDetails.getActivityCodeDesc()+"</td>");
		                		out.print("<td align='center'>"+activityDetails.getServiceDate()+"</td>");
		                		out.print("<td align='center'>"+activityDetails.getQuantity()+"</td>");
		                		out.print("<td align='center'>"+activityDetails.getProviderRequestedAmt()+"</td>");
		                		out.print("<td align='center'>"+activityDetails.getPatientShare()+"</td>");
		                		out.print("<td align='center'>"+activityDetails.getDisAllowedAmount()+"</td>");
		                		out.print("<td align='center'>"+activityDetails.getApprovedAmount()+"</td>");
		                		out.print("<td align='center'>"+activityDetails.getDenialRemarks()+"</td>");
		                		out.print("</tr>");
		                		i++;
		                			}//for(ActivityDetailsVO activityDetails:alActivityDetails)
		               out.print("</table>");
		        	}//if(alActivityDetails.size()>=1)
						}else{
							if(alActivityDetails.size()>=1){
		                		out.print("<table border='0' align='center' cellpadding='0' cellspacing='0' class='gridWithCheckBox'  style='width:100%;height:auto;'>");
		                		out.print("<tr>");
	                			out.print("<th align='center' class='gridHeader' colspan='13'>Activities</th>");
	                			out.print("</tr>");
		                			out.print("<tr>");
		                			out.print("<th align='center' class='gridHeader' title='Serial Number'>S.No</th>");
		                			out.print("<th align='center' class='gridHeader' title='Activity Code'>ID</th>");
		                			out.print("<th align='center' class='gridHeader' title='Internal Code'>Int Code</th>");
		                			out.print("<th align='center' class='gridHeader' title='Activity Description'>Activity Description</th>");
		                			out.print("<th align='center' class='gridHeader' title='Status'>Status</th>");
		                			out.print("<th align='center' class='gridHeader' title='Unit Price'>Unit Price</th>");
		                			out.print("<th align='center' class='gridHeader' title='Quantity'>Quantity</th>");
		                			out.print("<th align='center' class='gridHeader' title='Request Amount'>Req. Amnt</th>");
		                			/*out.print("<th align='center' class='gridHeader' title='Approved Maount'>Appr. Amnt</th>");*/
		                			out.print("<th align='center' class='gridHeader' title='Discount Amount'>Discount Amt</th>");	//	new
		                			out.print("<th align='center' class='gridHeader' title='Patient Share'>Pat. Share</th>");
		                			out.print("<th align='center' class='gridHeader' title='Duration'>Duration</th>");
		                			out.print("<th align='center' class='gridHeader' title='Denial'>Denial</th>");
		                			out.print("<th align='center' class='gridHeader' title='Comments'>Comments</th>");
		                			out.print("</tr>");
		        	
		                			int i=0;

		                for(ActivityDetailsVO activityDetails:alActivityDetails){
		                		out.print("<tr class="+(i%2==0?gridEvenRow:gridOddRow)+">");
		                		out.print("<td align='cenetr'> <font size='2'>"+(i+1)+"</font></td>");
		                		out.print("<td align='center'>"+activityDetails.getActivityCode()+"</td>");
		                		out.print("<td align='center'>"+activityDetails.getInternalCode()+"</td>");
		                		out.print("<td align='center'>"+activityDetails.getActivityCodeDesc()+"</td>");
		                		out.print("<td align='center'>"+activityDetails.getActivityStatus()+"</td>");
		                		out.print("<td align='center'>"+activityDetails.getUnitPrice()+"</td>");
		                		out.print("<td align='center'>"+activityDetails.getQuantity()+"</td>");
		                		out.print("<td align='center'>"+activityDetails.getProviderRequestedAmt()+"</td>");
		                		/*out.print("<td align='center'>"+activityDetails.getApprovedAmount()+"</td>");*/
		                		out.print("<td align='center'>"+activityDetails.getDiscount()+"</td>");				//	new
		                		out.print("<td align='center'>"+activityDetails.getPatientShare()+"</td>");
		                		out.print("<td align='center'>"+activityDetails.getMedicationDays()+"</td>");
		                		out.print("<td align='center'>"+activityDetails.getDenialCode()+"</td>");
		                		out.print("<td align='center'>"+activityDetails.getActivityRemarks()+"</td>");
		                		out.print("</tr>");
		                		i++;
		                			}//for(ActivityDetailsVO activityDetails:alActivityDetails)
		               out.print("</table>");
		        	}//if(alActivityDetails.size()>=1)
						}
			        }//if(alActivityDetails != null)
			        
	        	
	        }
			
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
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getPreAuthNoYN() {
		return preAuthNoYN;
	}
	public void setPreAuthNoYN(String preAuthNoYN) {
		this.preAuthNoYN = preAuthNoYN;
	}
}//end of DiagnosisDetails class 
