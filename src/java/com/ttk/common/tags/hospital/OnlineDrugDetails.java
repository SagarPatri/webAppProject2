/**
 * @ (#) OnlineDrugDetails.java July 18, 2015
 * Project : ProjectX
 * File : OnlineDrugDetails.java
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



//import org.apache.log4j.Logger;
import com.ttk.dto.preauth.DrugDetailsVO;

public class OnlineDrugDetails extends TagSupport
{
	private String flow;
	private String flag;
	private String preAuthNoYN;
	/**
	* Comment for <code>serialVersionUID</code>
	*/
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger( OnlineDrugDetails.class );
	
	public int doStartTag() throws JspException{
	ArrayList<DrugDetailsVO> alDrugDetails=null;
	
		try
		{
			double drugnetSum = 0.0;
			if(!"PATCreate".equals(getFlag())){
			if("CLM".equalsIgnoreCase(getFlow()))
			  alDrugDetails=(ArrayList<DrugDetailsVO>)pageContext.getSession().getAttribute("claimActivities");
			else if("PAT".equalsIgnoreCase(getFlow()))
			 alDrugDetails= (ArrayList<DrugDetailsVO>)pageContext.getSession().getAttribute("preauthDrugs");
				
	        JspWriter out = pageContext.getOut();//Writer Object to write the File
	       String gridOddRow="'gridOddRow'";
	       String gridEvenRow="'gridEvenRow'";
	        if(alDrugDetails != null){
	        	if(alDrugDetails.size()>=1){out.print("<br>");
	                		out.print("<table border='0' align='center' cellpadding='0' cellspacing='0' class='gridWithCheckBox'  style='width:100%;height:auto;'>");
	                			out.print("<tr>");
	                			out.print("<th align='center' class='gridHeader' title='Serial Number'>S.No</th>");
	                			out.print("<th align='center' class='gridHeader' title='Drug Code'>Drug Code</th>");
	                			out.print("<th align='center' class='gridHeader' title='Drug Description'>Drug Description</th>");
	                			out.print("<th align='center' class='gridHeader' title='Type Of Unit'>Type Of Unit</th>");
	                			out.print("<th align='center' class='gridHeader' title='Gross Price'>Unit Price</th>");
	                			out.print("<th align='center' class='gridHeader' title='Quantity Claimed'>Qty Clmd</th>");
	                			out.print("<th align='center' class='gridHeader' title='Gross Price'>Gross Price</th>");
	                			out.print("<th align='center' class='gridHeader' title='Discount Amount'>Disc Amt</th>");
	                			out.print("<th align='center' class='gridHeader' title='Net Amount After Discount'>Net Amt</th>");
	                			if("N".equals(getPreAuthNoYN()) || "E".equals(getPreAuthNoYN()))
	                				out.print("<th align='center' class='gridHeader' title='Delete'>Delete</th>");
	                			out.print("</tr>");
	        	
	                			int i=0;
	                			String delImage="<img src='/ttk/images/DeleteIcon.gif' alt='Delete Activity Details'width='12' height='12' border='0'>";

	                for(DrugDetailsVO drugDetailsVO:alDrugDetails){
	                		out.print("<tr class="+(i%2==0?gridEvenRow:gridOddRow)+">");
	                		out.print("<td align='cenetr'> <font size='2'>"+(i+1)+"</font></td>");
	                		out.print("<td align='center'>"+drugDetailsVO.getDrugCode()+"</td>");
	                		out.print("<td align='center'>"+drugDetailsVO.getDrugDesc()+"</td>");
	                		if("PCKG".equals(drugDetailsVO.getDrugunit()))
                				out.print("<td align='center'>Package</td>");
	                		else
	                			out.print("<td align='center'>Unit</td>");
	                		out.print("<td align='center'>"+drugDetailsVO.getUnitPrice()+"</td>");
	                		out.print("<td align='center'>"+drugDetailsVO.getDrugqty()+"</td>");
	                		out.print("<td align='center'>"+drugDetailsVO.getGrossAmount()+"</td>");
	                		out.print("<td align='center'>"+drugDetailsVO.getDiscount()+"</td>");
	                		out.print("<td align='center'>"+drugDetailsVO.getNetPriceAftDisc()+"</td>");
	                		if("N".equals(getPreAuthNoYN()) || "E".equals(getPreAuthNoYN()))
	                			out.print("<td align='center'><a href='#' accesskey='d'  onClick=\"javascript:deleteDrugDetails('"+i+"');\">"+delImage+"</a></td>");
	                		out.print("</tr>");
	                		drugnetSum = drugnetSum+ Double.parseDouble(String.valueOf(drugDetailsVO.getNetPriceAftDisc()));
	                		
	                		i++;
	                			}//for(ActivityDetailsVO activityDetails:alDrugDetails)
	                out.print("<input type='hidden' name='netamountiddrug' id='netamountiddrug' value="+drugnetSum+">");
	                out.print("</table>");
	        	}//if(alDrugDetails.size()>=1)
	        }//if(alDrugDetails != null)
			}else{
				
				if("CLM".equalsIgnoreCase(getFlow()))
					  alDrugDetails=(ArrayList<DrugDetailsVO>)pageContext.getSession().getAttribute("claimActivities");
					else if("PAT".equalsIgnoreCase(getFlow()))
					 alDrugDetails= (ArrayList<DrugDetailsVO>)pageContext.getSession().getAttribute("preauthDrugs");
						
			        JspWriter out = pageContext.getOut();//Writer Object to write the File
			       String gridOddRow="'gridOddRow'";
			       String gridEvenRow="'gridEvenRow'";
			        if(alDrugDetails != null){
			        	if(alDrugDetails.size()>=1){
			        		out.print("<table border='0' align='center' cellpadding='0' cellspacing='0' class='gridWithCheckBox'  style='width:100%;height:auto;'>");
	                		out.print("<tr>");
                			out.print("<th align='center' class='gridHeader' colspan='12'>Drugs</th>");
                			out.print("</tr>");
	                			out.print("<tr>");
	                			out.print("<th align='center' class='gridHeader' title='Serial Number'>S.No</th>");
	                			out.print("<th align='center' class='gridHeader' title='Activity Code'>ID</th>");
	                			out.print("<th align='center' class='gridHeader' title='Activity Description'>Activity Desc</th>");
	                			out.print("<th align='center' class='gridHeader' title='Status'>Status</th>");
	                			out.print("<th align='center' class='gridHeader' title='Unit Price'>Unit Price</th>");
	                			out.print("<th align='center' class='gridHeader' title='Quantity'>Quantity</th>");
	                			out.print("<th align='center' class='gridHeader' title='Request Amount'>Req. Amnt</th>");
	                			out.print("<th align='center' class='gridHeader' title='Approved Maount'>Appr. Amnt</th>");
	                			out.print("<th align='center' class='gridHeader' title='Patient Share'>Pat. Share</th>");
	                			out.print("<th align='center' class='gridHeader' title='Duration'>Duration</th>");
	                			out.print("<th align='center' class='gridHeader' title='Denial'>Denial</th>");
	                			out.print("<th align='center' class='gridHeader' title='Comments'>Comments</th>");
	                			out.print("</tr>");
			        	
			                			int i=0;

			                for(DrugDetailsVO drugDetailsVO:alDrugDetails){
			                		out.print("<tr class="+(i%2==0?gridEvenRow:gridOddRow)+">");
			                		out.print("<td align='cenetr'> <font size='2'>"+(i+1)+"</font></td>");
			                		out.print("<td align='center'>"+drugDetailsVO.getDrugCode()+"</td>");
			                		out.print("<td align='center'>"+drugDetailsVO.getDrugDesc()+"</td>");
			                		out.print("<td align='center'>"+drugDetailsVO.getStatus()+"</td>");
			                		out.print("<td align='center'>"+drugDetailsVO.getUnitPrice()+"</td>");
			                		out.print("<td align='center'>"+drugDetailsVO.getDrugqty()+"</td>");
			                		out.print("<td align='center'>"+drugDetailsVO.getRequestedAmount()+"</td>");
			                		out.print("<td align='center'>"+drugDetailsVO.getApprovedAmount()+"</td>");
			                		out.print("<td align='center'>"+drugDetailsVO.getPateintShare()+"</td>");
			                		out.print("<td align='center'>"+drugDetailsVO.getDays()+"</td>");
			                		out.print("<td align='center'>"+drugDetailsVO.getDenial()+"</td>");
			                		out.print("<td align='center'>"+drugDetailsVO.getRemarks()+"</td>");
			                		out.print("</tr>");
			                		i++;
			                			}//for(ActivityDetailsVO activityDetails:alDrugDetails)
			               out.print("</table>");
			        	}//if(alDrugDetails.size()>=1)
			        }//if(alDrugDetails != null)
				
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
