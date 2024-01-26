package com.ttk.common.tags.preauth;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Node;

public class ClaimPreAuthHistoryDetails extends TagSupport{
	private String flow;
	/**
	* Comment for <code>serialVersionUID</code>
	*/
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger( ClaimPreAuthHistoryDetails.class );
	
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
	       /* pageContext.getOut().print("<div class=\"contentArea\" id=\"contentArea\">");*/
	       //	pageContext.getOut().print("Pre-Authorization Details");
	       		
	        
	       	
	        out.print("<legend style='color:black; font-size:15px;bold'>");
	        out.print("<b>");
	        out.print("Pre-Authorization Details");
	        out.print("</b>");
	        out.print("</legend>");
	        		
	        		
	       	if(pNode!=null){ 
	       				pageContext.getOut().print("<table align='center' class='formContainer' border='0' cellspacing='10px' cellpadding='0'>");
	       				pageContext.getOut().print("<tr>");
	       				pageContext.getOut().print("<td  class='formLabel'>Pre-Approval No.:</td>");
	       				pageContext.getOut().print("<td  style='color:#0c48a2;' class='textLabelBold'>"+pNode.valueOf("@preauthnumber")+"</td>");
	       				pageContext.getOut().print("<td class='formLabel'>Mode of Pre-Approval:</td>");
	       				pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel'>"+pNode.valueOf("@source")+"</td>");
	       				pageContext.getOut().print("</tr>");
	       				pageContext.getOut().print("<tr>");
	       				pageContext.getOut().print("<td class='formLabel' width='25%'>Admission Date / Time:</td>");
	       				pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@admissiondate")+"</td>");
	       				pageContext.getOut().print("<td class='formLabel' width='25%'>Discharge Date:</td>");
	       				pageContext.getOut().print("<td style='color: #0c48a2;;' class='textLabel' width='25%'>"+pNode.valueOf("@discharegedate")+"</td>");			    
	       				pageContext.getOut().print("</tr>");
	       			    pageContext.getOut().print("<tr>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Member Id:</td>");
	       			    pageContext.getOut().print("<td style='color: #0c48a2;;' class='textLabel' width='25%'>"+pNode.valueOf("@memberid")+"</td>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Patient Name:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@memname")+"</td>");
	       			    pageContext.getOut().print("</tr>");
	       			    pageContext.getOut().print("<tr>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Age:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@age")+"</td>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Qatar Id:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@emirateid")+"</td>");
	       			    pageContext.getOut().print("</tr>");
	       			    
	       			    pageContext.getOut().print("<tr>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>VIP:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@vip")+"</td>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'></td>");
	       			    pageContext.getOut().print("<td class='textLabel' width='25%'></td>");
	       			    pageContext.getOut().print("</tr>");
	       			    
	       			    pageContext.getOut().print("<tr>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>System Of Medicine:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@sysmed")+"</td>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Event Reference No.:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@event_no")+"</td>");
	       			    pageContext.getOut().print("</tr>");
	       			    pageContext.getOut().print("<tr>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Preapproval Network Y/N:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@network")+"</td>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Submission Category:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@processtype")+"</td>");
	       			    pageContext.getOut().print("</tr>");
	       			   
	       			    
	       			    pageContext.getOut().print("<tr>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Encounter Type:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@encountertype")+"</td>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Encounter Facility Id:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@encfacilityid")+"</td>");
	       			    pageContext.getOut().print("</tr>");
	       			    pageContext.getOut().print("<tr>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Encounter Start Type:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@encstarttype")+"</td>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Encounter End Type:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@encendtype")+"</td>");
	       			    pageContext.getOut().print("</tr>");
	       			    
	       			    pageContext.getOut().print("<tr>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Authorization No.:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@authnumber")+"</td>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Status:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabelBold' width='25%'>"+pNode.valueOf("@status")+"</td>");
	       			    pageContext.getOut().print("</tr>");
	       			    pageContext.getOut().print("<tr>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Insurer Name:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@payername")+"</td>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Insurer Id:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@payerid")+"</td>");
	       			    pageContext.getOut().print("</tr>");
	       			    pageContext.getOut().print("<tr>");
	       			    /*pageContext.getOut().print("<td class='formLabel' width='25%'>Payer Name:</td>");
	       			    pageContext.getOut().print("<td class='textLabel' width='25%'>"+pNode.valueOf("@payername")+"</td>");*/
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Provider Name:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@providername")+"</td>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Provider License Id:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@providerid")+"</td>");
	       			    pageContext.getOut().print("</tr>");
	       			    pageContext.getOut().print("<tr>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Provider Details:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@providerdetails")+"</td>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Benefit Type:</td>");
	       			    pageContext.getOut().print("<td class='textLabel' width='25%' style=\"COLOR: #0c48a2; FONT-WEIGHT: bold\">"+pNode.valueOf("@trmtcatype")+"</td>");
	       			    pageContext.getOut().print("</tr>");
	       			   
	       			    pageContext.getOut().print("<tr>");
                        pageContext.getOut().print("<td class='formLabel' width='25%'>Empanel Number:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@empanenumber")+"</td>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Treatment Type:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@treatmenttype")+"</td>");
	       			    pageContext.getOut().print("</tr>");
	       			 
	       			    pageContext.getOut().print("<tr>");
	       			 if(pNode.valueOf("@trmtcatype").contains("MATERNITY")){
	       				 pageContext.getOut().print("<td class='formLabel' width='25%'>Date Of LMP:</td>");	
	       				 pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@dlmp")+"</td>");
	       			 	 pageContext.getOut().print("<td class='formLabel' width='25%'>Nature Of Conception:</td>");
	       				pageContext.getOut().print("<td  style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@conception")+"</td>");
	       			   }
	       			    pageContext.getOut().print("</tr>");
	       			    
	       			    if(pNode.valueOf("@trmtcatype").contains("MATERNITY")){
		       			    pageContext.getOut().print("<tr>");
		       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Mode of delivery:</td>");
		       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@modeofdelivery")+"</td>");
		       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Maternity Coverage Limit:</td>");
		       			    if("Y".equals(pNode.valueOf("@matcomplctonyn")))
		       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+"Maternity Complication cover"+"</td>");
		       			    else if("N".equals(pNode.valueOf("@matcomplctonyn")))
			       			pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+"Maternity Normal Cover"+"</td>");
		       			    pageContext.getOut().print("</tr>");
		       			  
		       			 }
	       			    
	       			    
	       			    pageContext.getOut().print("<tr>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Gross Amount:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@grossamt")+"</td>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Net Amount:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@netamt")+"</td>");
	       			    pageContext.getOut().print("</tr>");
	       			    
	       			     pageContext.getOut().print("<tr>");
	       			     pageContext.getOut().print("<td class='formLabel' width='25%'>Patient Share:</td>");
	       			     pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@patientshare")+"</td>");
	       			     pageContext.getOut().print("<td class='formLabel' width='25%'>Approved Amount:</td>");
	       			     pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@approvedamt")+"</td>");
	       			     pageContext.getOut().print("</tr>");
	       			   
	       			    pageContext.getOut().print("<tr>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Denial Reason:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@denialreason")+"</td>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Final Remarks:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@remarks")+"</td>");
	       			    pageContext.getOut().print("</tr>");
	       			    
	       			    pageContext.getOut().print("<tr>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Available Sum Insured:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@avasuminsured")+"</td>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Linked to Claim No.:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@claimnumber")+"</td>");
	       			    pageContext.getOut().print("</tr>");
	       			
	       			
	       			    pageContext.getOut().print("<tr>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Assigned TO:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabelBold' width='25%'>"+pNode.valueOf("@assignto")+"</td>");	
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Internal Remarks</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabel' width='25%'>"+pNode.valueOf("@internalremarks")+"</td>");		   
		   
	       			    pageContext.getOut().print("</tr>");
	       			    pageContext.getOut().print("<tr>");
	       			    pageContext.getOut().print("<td class='formLabel' width='25%'>Internal Remarks Status:</td>");
	       			    pageContext.getOut().print("<td style='color:#0c48a2;' class='textLabelBold' width='25%'>"+pNode.valueOf("@internalremarksstatus")+"</td>");	
	       			    pageContext.getOut().print("</tr>");
	       			 pageContext.getOut().print("</table>");			 
	       			  } //	if(pNode!=null){ 
	       	pageContext.getOut().print("</fieldset>");
	        
	        out.print("<legend style='color:black; font-size:15px;bold'>");
	        out.print("<b>");
	        out.print("Diagnosis Details");
	        out.print("</b>");
	        out.print("</legend>");
	        
	        pageContext.getOut().print("<table border='1' align='center' cellpadding='0' cellspacing='0' class='gridWithCheckBox'  style='width:99%;height:auto;'>");
			pageContext.getOut().print("<tr>");
			pageContext.getOut().print("<th class='gridHeader' style='background-color:#677BA8' align='center'>ICD Code</th>");
			pageContext.getOut().print("<th class='gridHeader' style='background-color:#677BA8' align='center'>Ailment Desription</th>");			   
			pageContext.getOut().print("<th class='gridHeader' style='background-color:#677BA8' align='center'>Primary Ailment</th>");	
			pageContext.getOut().print("<th class='gridHeader' style='background-color:#677BA8' align='center'>Copay</th>");
			pageContext.getOut().print("<th class='gridHeader' style='background-color:#677BA8' align='center'>Denial Reason</th>");			   
			pageContext.getOut().print("<th class='gridHeader' style='background-color:#677BA8' align='center'>Remarks</th>");		    			    
			pageContext.getOut().print("</tr>");
			int di=0;
	       	if(dNodes!=null){			
	       			for(Node dNode:dNodes){			
	       				pageContext.getOut().print("<tr class="+(di%2==0?gridEvenRow:gridOddRow)+">");
	       			pageContext.getOut().print("<td align='center'>"+dNode.valueOf("@diagcode")+"</td>");
	       		    pageContext.getOut().print("<td align='center'>"+dNode.valueOf("@description")+"</td>");		     
	       		    pageContext.getOut().print("<td align='center'>"+dNode.valueOf("@primary")+"</td>");	
	       		    pageContext.getOut().print("<td align='center'>"+dNode.valueOf("@copay") +"</td>");	
	       		    pageContext.getOut().print("<td align='center'>"+dNode.valueOf("@denialreason")+"</td>");	
	       		    pageContext.getOut().print("<td align='center'>"+dNode.valueOf("@remarks")+"</td>");				 			  				  
	       		    pageContext.getOut().print("</tr>");	
	       		    di++;
	       			  } 
	       			}
	       	pageContext.getOut().print("</table>");
	       	pageContext.getOut().print("</fieldset>");	
	       	
	        out.print("<legend style='color:black; font-size:15px;bold'>");
	        out.print("<b>");
	        out.print("Activity Details");
	        out.print("</b>");
	        out.print("</legend>");
	       	
	       	pageContext.getOut().print("<table border='1' align='center' cellpadding='0' cellspacing='0' class='gridWithCheckBox'  style='width:99%;height:auto;'>");
			pageContext.getOut().print("<tr>");
			pageContext.getOut().print("<th align='center' style='background-color:#677BA8' class='gridHeader' title='Activity Code'>Act Code</th>");
			if("DENTAL".equals(pNode.valueOf("@trmtcatype"))){
				pageContext.getOut().print("<th align='center' style='background-color:#677BA8' class='gridHeader' title='Tooth No.'>Tth No.</th>");
			}
			pageContext.getOut().print("<th align='center' style='background-color:#677BA8' class='gridHeader' title='Modifier'>Mod</th>");
			pageContext.getOut().print("<th align='center' style='background-color:#677BA8' class='gridHeader' title='Unit Type'>Unt Type</th>");
			pageContext.getOut().print("<th align='center' style='background-color:#677BA8' class='gridHeader' title='Quantity'>Qty</th>");
			pageContext.getOut().print("<th align='center' style='background-color:#677BA8' class='gridHeader' title='Approved Quantity'>Apr Qty</th>");
			pageContext.getOut().print("<th align='center' style='background-color:#677BA8' class='gridHeader' title='Start Date'>Str Date</th>");
			pageContext.getOut().print("<th align='center' style='background-color:#677BA8' class='gridHeader' title='Gross Amount'>Grs Amt</th>");
			pageContext.getOut().print("<th align='center' style='background-color:#677BA8' class='gridHeader' title='Discount Amount'>Disc Amt</th>");
			pageContext.getOut().print("<th align='center' style='background-color:#677BA8' class='gridHeader' title='Discount Gross Amount'>Disc Grs</th>");
			pageContext.getOut().print("<th align='center' style='background-color:#677BA8' class='gridHeader' title='Patient Share'>Ptn Share</th>");
			pageContext.getOut().print("<th align='center' style='background-color:#677BA8' class='gridHeader' title='Net Amount'>Net Amt</th>");
			pageContext.getOut().print("<th align='center' style='background-color:#677BA8' class='gridHeader' title='Allowed Amount'>Alw Amt</th>");
			pageContext.getOut().print("<th align='center' style='background-color:#677BA8' class='gridHeader' title='Approved Amount'>Apr Amt</th>");
			pageContext.getOut().print("<th align='center' style='background-color:#677BA8' class='gridHeader' title='Denial Code'>Den Code</th>");
			pageContext.getOut().print("<th align='center' style='background-color:#677BA8' class='gridHeader' title='Remarks'>Remarks</th>");
			/*pageContext.getOut().print("<th align='center' class='gridHeader' title='Observations'>Observ</th>");*/
			pageContext.getOut().print("</tr>");

			int ai=0;
			String addImage="<img src='/ttk/images/EditIcon.gif' alt='Add Observ Details' width='16' height='16' border='0' align='absmiddle'>";
	       	if(aNodes!=null){				
	       				for(Node aNode:aNodes){
	       					
	       			pageContext.getOut().print("<tr class="+(ai%2==0?gridEvenRow:gridOddRow)+">");	       			
	       			pageContext.getOut().print("<td style=\"cursor: pointer;\" title=\""+aNode.valueOf("@actdesc")+"\" align='center'>"+aNode.valueOf("@actid")+"</td>");
	       			if("DENTAL".equals(pNode.valueOf("@trmtcatype"))){
	       				pageContext.getOut().print("<td align='center'>"+aNode.valueOf("@toothnum")+"</td>");
	       			}
	       			pageContext.getOut().print("<td align='center'>"+aNode.valueOf("@modifier")+"</td>");
	       			pageContext.getOut().print("<td align='center'>"+aNode.valueOf("@unittype")+"</td>");
	       			pageContext.getOut().print("<td align='center'>"+aNode.valueOf("@qty")+"</td>");
	       			pageContext.getOut().print("<td align='center'>"+aNode.valueOf("@apprqty")+"</td>");
	       			pageContext.getOut().print("<td align='center'>"+aNode.valueOf("@startdate")+"</td>");
	       			pageContext.getOut().print("<td align='center'>"+aNode.valueOf("@gross")+"</td>");
	       			pageContext.getOut().print("<td align='center'>"+aNode.valueOf("@disc")+"</td>");
	       			pageContext.getOut().print("<td align='center'>"+aNode.valueOf("@discgross")+"</td>");
	       			pageContext.getOut().print("<td align='center'>"+aNode.valueOf("@patientshare")+"</td>");
	       			pageContext.getOut().print("<td align='center'>"+aNode.valueOf("@net")+"</td>");
	       			pageContext.getOut().print("<td align='center'>"+aNode.valueOf("@allowedamt")+"</td>");
	       			pageContext.getOut().print("<td align='center'>"+aNode.valueOf("@appramt")+"</td>");
	       			//pageContext.getOut().print("<td align='center'>"+aNode.valueOf("@copayperc")+"</td>");
	       			//pageContext.getOut().print("<td align='center'>"+aNode.valueOf("@copay")+"</td>");
	       			//pageContext.getOut().print("<td align='center'>"+aNode.valueOf("@ded")+"</td>");
	       			//pageContext.getOut().print("<td align='center'>"+aNode.valueOf("@currency")+"</td>");				
	       		  pageContext.getOut().print("<td align='center' style=\"cursor: pointer;\" title='"+aNode.valueOf("@denialdesc")+"'>"+aNode.valueOf("@denialcode")+"</td>");
	       		  String remarks=aNode.valueOf("@remarks")==null?"":aNode.valueOf("@remarks");
				  String rmrks=(remarks.length()>6)?remarks.substring(0, 5)+"...":remarks;
	       		  pageContext.getOut().print("<td align='center' style=\"cursor: pointer;\" title='"+remarks+"'>"+rmrks+"</td>");
	       		 // pageContext.getOut().print("<td align='center'><a href='#' accesskey='o'  onClick=\"javascript:viewObservs('"+aNode.valueOf("@actdtlseqid")+"');\">"+addImage+"</a></td>");
	       		  pageContext.getOut().print("</tr>");
	       			ai++;
	       		}//	for(Node aNode:aNodes){	
	       	}//	if(aNodes!=null){	
	       			
	       	pageContext.getOut().print("</table>");
	       	pageContext.getOut().print("</fieldset>");
	       	
	       /* pageContext.getOut().print("</div>");*/
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
}
