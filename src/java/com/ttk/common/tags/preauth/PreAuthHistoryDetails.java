/**
 * @ (#) PreAuthHistoryDetails.java August 20, 2015
 * Project : ProjectX
 * File : PreAuthHistoryDetails.java
 * Author : Nagababu K
 * Company :RCS Technologies
 * Date Created : August 20, 2015
 *
 * @author : Nagababu K
 * Modified by :
 * Modified date :
 * Reason :
*/
package com.ttk.common.tags.preauth;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Node;
//import org.apache.log4j.Logger;

public class PreAuthHistoryDetails extends TagSupport
{
	private String flow;
	/**
	* Comment for <code>serialVersionUID</code>
	*/
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger( PreAuthHistoryDetails.class );
	
	public int doStartTag() throws JspException{
	
		try
		{
			
	       JspWriter out = pageContext.getOut();//Writer Object to write the File
	       String gridOddRow="'gridOddRow'";
	       String gridEvenRow="'gridEvenRow'";
	      
	       Document document=(Document)pageContext.getRequest().getAttribute("preAuthHistoryDoc");
	        if(document!=null){
	       	Node pNode=document.selectSingleNode("/preauthorizationhistory/preauthorizationdetails");
	       	List<Node> dNodes=document.selectNodes("/preauthorizationhistory/preauthorizationdetails/diagnosysdetails");
	       	List<Node> aNodes=document.selectNodes("/preauthorizationhistory/preauthorizationdetails/activitydetails");
	       
	       	out.print("<fieldset>");
	       	out.print("<legend>Pre-Authorization Details</legend>");
	       			if(pNode!=null){ 
	       				out.print("<table align='center' class='formContainer' border='0' cellspacing='10px' cellpadding='0'>");
	       				out.print("<tr>");
	       				out.print("<td  class='formLabel'>Pre-Approval No.:</td>");
	       				out.print("<td  class='textLabelBold'>"+pNode.valueOf("@preauthnumber")+"</td>");
	       				out.print("<td class='formLabel'>Mode of Pre-Approval:</td>");
	       				out.print("<td class='textLabel'>"+pNode.valueOf("@source")+"</td>");
	       				out.print("</tr>");
	       				out.print("<tr>");
	       				out.print("<td class='formLabel' width='25%'>Admission Date / Time:</td>");
	       				out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@admissiondate")+"</td>");
	       				out.print("<td class='formLabel' width='25%'>Discharge Date:</td>");
	       				out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@discharegedate")+"</td>");			    
	       				out.print("</tr>");
	       			    out.print("<tr>");
	       			    out.print("<td class='formLabel' width='25%'>Member Id:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@memberid")+"</td>");
	       			    out.print("<td class='formLabel' width='25%'>Patient Name:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@memname")+"</td>");
	       			    out.print("</tr>");
	       			    out.print("<tr>");
	       			    out.print("<td class='formLabel' width='25%'>Age:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@age")+"</td>");
	       			    out.print("<td class='formLabel' width='25%'>Qatar Id:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@emirateid")+"</td>");
	       			    out.print("</tr>");
	       			    
	       			    out.print("<tr>");
	       			    out.print("<td class='formLabel' width='25%'>VIP:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@vip")+"</td>");
	       			    out.print("<td class='formLabel' width='25%'></td>");
	       			    out.print("<td class='textLabel' width='25%'></td>");
	       			    out.print("</tr>");
	       			    
	       			    out.print("<tr>");
	       			    out.print("<td class='formLabel' width='25%'>System Of Medicine:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@sysmed")+"</td>");
	       			    out.print("<td class='formLabel' width='25%'>Event Reference No.:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@event_no")+"</td>");
	       			    out.print("</tr>");
	       			    out.print("<tr>");
	       			    out.print("<td class='formLabel' width='25%'>Preapproval Network Y/N:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@network")+"</td>");
	       			    out.print("<td class='formLabel' width='25%'>Submission Category:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@processtype")+"</td>");
	       			    out.print("</tr>");
	       			   
	       			    
	       			    out.print("<tr>");
	       			    out.print("<td class='formLabel' width='25%'>Encounter Type:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@encountertype")+"</td>");
	       			    out.print("<td class='formLabel' width='25%'>Encounter Facility Id:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@encfacilityid")+"</td>");
	       			    out.print("</tr>");
	       			    out.print("<tr>");
	       			    out.print("<td class='formLabel' width='25%'>Encounter Start Type:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@encstarttype")+"</td>");
	       			    out.print("<td class='formLabel' width='25%'>Encounter End Type:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@encendtype")+"</td>");
	       			    out.print("</tr>");
	       			    
	       			    out.print("<tr>");
	       			    out.print("<td class='formLabel' width='25%'>Authorization No.:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@authnumber")+"</td>");
	       			    out.print("<td class='formLabel' width='25%'>Status:</td>");
	       			    out.print("<td class='textLabelBold' width='25%'>"+pNode.valueOf("@status")+"</td>");
	       			    out.print("</tr>");
	       			    out.print("<tr>");
	       			    out.print("<td class='formLabel' width='25%'>Insurer Name:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@payername")+"</td>");
	       			    out.print("<td class='formLabel' width='25%'>Insurer Id:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@payerid")+"</td>");
	       			    out.print("</tr>");
	       			    out.print("<tr>");
	       			    /*out.print("<td class='formLabel' width='25%'>Payer Name:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@payername")+"</td>");*/
	       			    out.print("<td class='formLabel' width='25%'>Provider Name:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@providername")+"</td>");
	       			    out.print("<td class='formLabel' width='25%'>Provider License Id:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@providerid")+"</td>");
	       			    out.print("</tr>");
	       			    out.print("<tr>");
	       			    out.print("<td class='formLabel' width='25%'>Provider Details:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@providerdetails")+"</td>");
	       			    out.print("<td class='formLabel' width='25%'>Benefit Type:</td>");
	       			    out.print("<td class='textLabel' width='25%' style=\"COLOR: black; FONT-WEIGHT: bold\">"+pNode.valueOf("@trmtcatype")+"</td>");
	       			    out.print("</tr>");
	       			   
	       			    out.print("<tr>");
                        out.print("<td class='formLabel' width='25%'>Empanel Number:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@empanenumber")+"</td>");
	       			    out.print("<td class='formLabel' width='25%'>Treatment Type:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@treatmenttype")+"</td>");
	       			    out.print("</tr>");
	       			 
	       			    out.print("<tr>");
	       			 if(pNode.valueOf("@trmtcatype").contains("MATERNITY")){
	       				 out.print("<td class='formLabel' width='25%'>Date Of LMP:</td>");	
	       				 out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@dlmp")+"</td>");
	       			 	 out.print("<td class='formLabel' width='25%'>Nature Of Conception:</td>");
	       				out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@conception")+"</td>");
	       			   }
	       			    out.print("</tr>");
	       			    
	       			    if(pNode.valueOf("@trmtcatype").contains("MATERNITY")){
		       			    out.print("<tr>");
		       			    out.print("<td class='formLabel' width='25%'>Mode of delivery:</td>");
		       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@modeofdelivery")+"</td>");
		       			    out.print("<td class='formLabel' width='25%'>Maternity Coverage Limit:</td>");
		       			    if("Y".equals(pNode.valueOf("@matcomplctonyn")))
		       			    out.print("<td class='textLabel' width='25%'>"+"Maternity Complication cover"+"</td>");
		       			    else if("N".equals(pNode.valueOf("@matcomplctonyn")))
			       			out.print("<td class='textLabel' width='25%'>"+"Maternity Normal Cover"+"</td>");
		       			    out.print("</tr>");
		       			  
		       			 }
	       			    
	       			    
	       			    out.print("<tr>");
	       			    out.print("<td class='formLabel' width='25%'>Gross Amount:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@grossamt")+"</td>");
	       			    out.print("<td class='formLabel' width='25%'>Net Amount:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@netamt")+"</td>");
	       			    out.print("</tr>");
	       			    
	       			     out.print("<tr>");
	       			     out.print("<td class='formLabel' width='25%'>Patient Share:</td>");
	       			     out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@patientshare")+"</td>");
	       			     out.print("<td class='formLabel' width='25%'>Approved Amount:</td>");
	       			     out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@approvedamt")+"</td>");
	       			     out.print("</tr>");
	       			   
	       			    out.print("<tr>");
	       			    out.print("<td class='formLabel' width='25%'>Denial Reason:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@denialreason")+"</td>");
	       			    out.print("<td class='formLabel' width='25%'>Final Remarks:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@remarks")+"</td>");
	       			    out.print("</tr>");
	       			    
	       			    out.print("<tr>");
	       			    out.print("<td class='formLabel' width='25%'>Available Sum Insured:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@avasuminsured")+"</td>");
	       			    out.print("<td class='formLabel' width='25%'>Linked to Claim No.:</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@claimnumber")+"</td>");
	       			    out.print("</tr>");
	       			
	       			
	       			    out.print("<tr>");
	       			    out.print("<td class='formLabel' width='25%'>Assigned TO:</td>");
	       			    out.print("<td class='textLabelBold' width='25%'>"+pNode.valueOf("@assignto")+"</td>");	
	       			    out.print("<td class='formLabel' width='25%'>Internal Remarks</td>");
	       			    out.print("<td class='textLabel' width='25%'>"+pNode.valueOf("@internalremarks")+"</td>");		   
		   
	       			    out.print("</tr>");
	       			 out.print("<tr>");
	       			    out.print("<td class='formLabel' width='25%'>Internal Remarks Status:</td>");
	       			    out.print("<td class='textLabelBold' width='25%'>"+pNode.valueOf("@internalremarksstatus")+"</td>");	
	       			    out.print("</tr>");
	       			 out.print("</table>");			 
	       			  } //	if(pNode!=null){ 
	       	out.print("</fieldset>");
	       	
	        out.print("<fieldset>");		
	        out.print("<legend>Diagnosis Details</legend>");
	        out.print("<table border='0' align='center' cellpadding='0' cellspacing='0' class='gridWithCheckBox'  style='width:99%;height:auto;'>");
			out.print("<tr>");
			out.print("<th class='gridHeader' align='center'>ICD Code</th>");
			out.print("<th class='gridHeader' align='center'>Ailment Desription</th>");			   
			out.print("<th class='gridHeader' align='center'>Primary Ailment</th>");	
			out.print("<th class='gridHeader' align='center'>Copay</th>");
			out.print("<th class='gridHeader' align='center'>Denial Reason</th>");			   
			out.print("<th class='gridHeader' align='center'>Remarks</th>");		    			    
			out.print("</tr>");
			int di=0;
	       	if(dNodes!=null){			
	       			for(Node dNode:dNodes){			
	       				out.print("<tr class="+(di%2==0?gridEvenRow:gridOddRow)+">");
	       			out.print("<td align='center'>"+dNode.valueOf("@diagcode")+"</td>");
	       		    out.print("<td align='center'>"+dNode.valueOf("@description")+"</td>");		     
	       		    out.print("<td align='center'>"+dNode.valueOf("@primary")+"</td>");	
	       		    out.print("<td align='center'>"+dNode.valueOf("@copay") +"</td>");	
	       		    out.print("<td align='center'>"+dNode.valueOf("@denialreason")+"</td>");	
	       		    out.print("<td align='center'>"+dNode.valueOf("@remarks")+"</td>");				 			  				  
	       		    out.print("</tr>");	
	       		    di++;
	       			  } 
	       			}
	       	out.print("</table>");
	       	out.print("</fieldset>");	
	       	
	       	out.print("<fieldset>");	         
	       	out.print("<legend>Activity Details</legend>");
	       	out.print("<table border='0' align='center' cellpadding='0' cellspacing='0' class='gridWithCheckBox'  style='width:99%;height:auto;'>");
			out.print("<tr>");
			out.print("<th align='center' class='gridHeader' title='Activity Code'>Act Code</th>");
			if("DENTAL".equals(pNode.valueOf("@trmtcatype"))){
				out.print("<th align='center' class='gridHeader' title='Tooth No.'>Tth No.</th>");
			}
			out.print("<th align='center' class='gridHeader' title='Modifier'>Mod</th>");
			out.print("<th align='center' class='gridHeader' title='Unit Type'>Unt Type</th>");
			out.print("<th align='center' class='gridHeader' title='Quantity'>Qty</th>");
			out.print("<th align='center' class='gridHeader' title='Approved Quantity'>Apr Qty</th>");
			out.print("<th align='center' class='gridHeader' title='Start Date'>Str Date</th>");
			out.print("<th align='center' class='gridHeader' title='Gross Amount'>Grs Amt</th>");
			out.print("<th align='center' class='gridHeader' title='Discount Amount'>Disc Amt</th>");
			out.print("<th align='center' class='gridHeader' title='Discount Gross Amount'>Disc Grs</th>");
			out.print("<th align='center' class='gridHeader' title='Patient Share'>Ptn Share</th>");
			out.print("<th align='center' class='gridHeader' title='Net Amount'>Net Amt</th>");
			out.print("<th align='center' class='gridHeader' title='Allowed Amount'>Alw Amt</th>");
			out.print("<th align='center' class='gridHeader' title='Approved Amount'>Apr Amt</th>");
			out.print("<th align='center' class='gridHeader' title='Denial Code'>Den Code</th>");
			out.print("<th align='center' class='gridHeader' title='Remarks'>Remarks</th>");
			out.print("<th align='center' class='gridHeader' title='Observations'>Observ</th>");
			out.print("</tr>");

			int ai=0;
			String addImage="<img src='/ttk/images/EditIcon.gif' alt='Add Observ Details' width='16' height='16' border='0' align='absmiddle'>";
	       	if(aNodes!=null){				
	       				for(Node aNode:aNodes){
	       					
	       			out.print("<tr class="+(ai%2==0?gridEvenRow:gridOddRow)+">");	       			
	       			out.print("<td style=\"cursor: pointer;\" title=\""+aNode.valueOf("@actdesc")+"\" align='center'>"+aNode.valueOf("@actid")+"</td>");
	       			if("DENTAL".equals(pNode.valueOf("@trmtcatype"))){
	       				out.print("<td align='center'>"+aNode.valueOf("@toothnum")+"</td>");
	       			}
	       			out.print("<td align='center'>"+aNode.valueOf("@modifier")+"</td>");
	       			out.print("<td align='center'>"+aNode.valueOf("@unittype")+"</td>");
	       			out.print("<td align='center'>"+aNode.valueOf("@qty")+"</td>");
	       			out.print("<td align='center'>"+aNode.valueOf("@apprqty")+"</td>");
	       			out.print("<td align='center'>"+aNode.valueOf("@startdate")+"</td>");
	       			out.print("<td align='center'>"+aNode.valueOf("@gross")+"</td>");
	       			out.print("<td align='center'>"+aNode.valueOf("@disc")+"</td>");
	       			out.print("<td align='center'>"+aNode.valueOf("@discgross")+"</td>");
	       			out.print("<td align='center'>"+aNode.valueOf("@patientshare")+"</td>");
	       			out.print("<td align='center'>"+aNode.valueOf("@net")+"</td>");
	       			out.print("<td align='center'>"+aNode.valueOf("@allowedamt")+"</td>");
	       			out.print("<td align='center'>"+aNode.valueOf("@appramt")+"</td>");
	       			//out.print("<td align='center'>"+aNode.valueOf("@copayperc")+"</td>");
	       			//out.print("<td align='center'>"+aNode.valueOf("@copay")+"</td>");
	       			//out.print("<td align='center'>"+aNode.valueOf("@ded")+"</td>");
	       			//out.print("<td align='center'>"+aNode.valueOf("@currency")+"</td>");				
	       		  out.print("<td align='center' style=\"cursor: pointer;\" title='"+aNode.valueOf("@denialdesc")+"'>"+aNode.valueOf("@denialcode")+"</td>");
	       		  String remarks=aNode.valueOf("@remarks")==null?"":aNode.valueOf("@remarks");
				  String rmrks=(remarks.length()>6)?remarks.substring(0, 5)+"...":remarks;
	       		  out.print("<td align='center' style=\"cursor: pointer;\" title='"+remarks+"'>"+rmrks+"</td>");
	       		  out.print("<td align='center'><a href='#' accesskey='o'  onClick=\"javascript:viewObservs('"+aNode.valueOf("@actdtlseqid")+"');\">"+addImage+"</a></td>");
	       		  out.print("</tr>");
	       			ai++;
	       		}//	for(Node aNode:aNodes){	
	       	}//	if(aNodes!=null){	
	       			
	       	out.print("</table>");
	       	out.print("</fieldset>");
	      }//  if(document!=null)
	        	 		
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
}//end of PreAuthHistoryDetails class 
