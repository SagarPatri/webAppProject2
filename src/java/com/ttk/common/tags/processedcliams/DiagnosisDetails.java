package com.ttk.common.tags.processedcliams;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.ttk.dto.preauth.DiagnosisDetailsVO;

public class DiagnosisDetails extends TagSupport{
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger( DiagnosisDetails.class );
	//private String flow;	
	
	@SuppressWarnings("unchecked")
	public int doStartTag() throws JspException{
		
		ArrayList<DiagnosisDetailsVO> diagnosis=null;
		
		 JspWriter out = pageContext.getOut();//Writer Object to write the File
	       String gridOddRow="'gridOddRow'";
	       String gridEvenRow="'gridEvenRow'";
		
	       diagnosis=(ArrayList<DiagnosisDetailsVO>)pageContext.getSession().getAttribute("claimDiagnosis");
		
	       
	       try {
	    	out.print("<fieldset>");
		    out.print("<legend>Diagnosis Details</legend>");
			out.print("<table border='0' align='center' cellpadding='0' cellspacing='0' class='gridWithCheckBox'  style='width:100%;height:auto;'>");
			out.print("<tr>");
			out.print("<th align='center' class='gridHeader' title='Presenting complaints'>Presenting complaints</th>");
			out.print("<th align='center' class='gridHeader' title='Primary ICD Code'>Primary ICD Code</th>");
			out.print("<th align='center' class='gridHeader' title='Primary ICD Description'>Primary ICD Desc</th>");
			out.print("<th align='center' class='gridHeader' title='Secondary ICD Code'>Secondary ICD Code</th>");
			out.print("<th align='center' class='gridHeader' title='Secondary ICD Description'>Secondary ICD Desc</th>");
			out.print("<th align='center' class='gridHeader' title='Tooth Number'>Tooth Number</th>");
			out.print("<th align='center' class='gridHeader' title='Quantity'>Quantity</th>");
		
			out.print("</tr>");
			int i=0;
			 if(diagnosis != null)
		        {
				 for(DiagnosisDetailsVO diagnosisDetails:diagnosis){
				 out.print("<tr class="+(i%2==0?gridEvenRow:gridOddRow)+">");
         		//out.print("<td align='cenetr'>"+(i+1)+"</td>");
         		out.print("<td align='center'>"+diagnosisDetails.getPresentingComplaints()+"</td>");
         		out.print("<td align='center'>"+diagnosisDetails.getPrimaryIcdCode()+"</td>");
         		out.print("<td align='center'>"+diagnosisDetails.getPrimaryIcdDesc()+"</td>");
         		out.print("<td align='center'>"+diagnosisDetails.getSecondaryIcdCode()+"</td>");
         		out.print("<td align='center'>"+diagnosisDetails.getSecondaryIcdDesc()+"</td>");
         		out.print("<td align='center'>"+diagnosisDetails.getToothNumber()+"</td>");
         		out.print("<td align='center'>"+diagnosisDetails.getQuantity()+"</td>");
         		
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

	
	/*public int doEndTag() throws JspException 
	{
		return EVAL_PAGE;//to process the rest of the page
	}//end doEndTag()
	
	*/
	
	
	/*public String getFlow() {
		return flow;
	}
	public void setFlow(String flow) {
		this.flow = flow;
	}	*/
	
	
	
	
}
