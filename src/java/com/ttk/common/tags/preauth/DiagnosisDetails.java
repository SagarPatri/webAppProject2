/**
 * @ (#) PreAuthShortFfalls.java June 27, 2015
 * Project : ProjectX
 * File : PreAuthShortFfalls.java
 * Author : Nagababu K
 * Company :Vidal
 * Date Created : June 27, 2015
 *
 * @author : Nagababu K
 * Modified by :
 * Modified date :
 * Reason :
*/
package com.ttk.common.tags.preauth;

import java.util.ArrayList;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.ttk.dto.preauth.DiagnosisDetailsVO;
//import org.apache.log4j.Logger;

public class DiagnosisDetails extends TagSupport
{
	private String flow;
	/**
	* Comment for <code>serialVersionUID</code>
	*/
	private static final long serialVersionUID = 1L;
	//private static Logger log = Logger.getLogger( CitibankEnrolHistory.class );
	public int doStartTag() throws JspException
	{
		try
		{
			ArrayList<DiagnosisDetailsVO> diagnosis=null;
			if("PAT".equals(getFlow()))
				diagnosis= (ArrayList<DiagnosisDetailsVO>)pageContext.getSession().getAttribute("preauthDiagnosis");
			else if("CLM".equals(getFlow()))
				diagnosis= (ArrayList<DiagnosisDetailsVO>)pageContext.getSession().getAttribute("claimDiagnosis");
			String activelink = (String)pageContext.getSession().getAttribute("ACTIVELINK");
			JspWriter out = pageContext.getOut();//Writer Object to write the File
	       String gridOddRow="'gridOddRow'";
	       String gridEvenRow="'gridEvenRow'";
	        if(diagnosis != null)
	        {
	        	if(diagnosis.size()>=1){
	                		out.print("<table border='0' align='center' cellpadding='0' cellspacing='0' class='gridWithCheckBox'  style='width:850px;height:auto;'");
	                			out.print("<tr>");
	                			out.print("<th align='center' class='gridHeader'>ICD Code</th>");
	                			out.print("<th align='center' class='gridHeader'>Diagnosis Desription</th>");
	                			out.print("<th align='center' class='gridHeader'>Diagnosis Type</th>");
	                			out.print("<th align='center' class='gridHeader'>Delete</th>");
	                			out.print("</tr>");
	        	
	                			int i=0;
	                			String imagePath="<img src='/ttk/images/DeleteIcon.gif' alt='Delete Diagnosis Details'width='16' height='16' border='0'>";
	                			String principal="Principal";
	                		    String secondary="Secondary"; 
	                			for(DiagnosisDetailsVO detailsVO:diagnosis){
	                		out.print("<tr class="+(i%2==0?gridEvenRow:gridOddRow)+">");
	                		if(activelink !=null && !activelink.equals("CounterFraudDept")){
	                		out.print("<td align='center'><a href='#' accesskey='f' title='Edit Diagnosis Details'  onClick=\"javascript:editDiagnosisDetails('"+i+"');\">"+detailsVO.getIcdCode()+"</a></td>");
	                		out.print("<td align='left'>"+detailsVO.getAilmentDescription()+"</td>");
	                		out.print("<td align='center'>"+("Y".equals(detailsVO.getPrimaryAilment())?principal:secondary)+"</td>");
	                		out.print("<td align='center'><a href='#' onClick=\"javascript:deleteDiagnosisDetails('"+i+"');\">"+imagePath+"</a></td>");
	                		}else{
	                			out.print("<td align='center'><a href='#' accesskey='f' title='Edit Diagnosis Details'>"+detailsVO.getIcdCode()+"</a></td>");
		                		out.print("<td align='left'>"+detailsVO.getAilmentDescription()+"</td>");
		                		out.print("<td align='center'>"+("Y".equals(detailsVO.getPrimaryAilment())?principal:secondary)+"</td>");
		                		out.print("<td align='center'><a href='#'>"+imagePath+"</a></td>");
	                		}
	                		out.print("</tr>");
	                		i++;
	                			}
	                		out.print("</table>");
	        	}
	        }//end of if(diagnosis != null)
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
