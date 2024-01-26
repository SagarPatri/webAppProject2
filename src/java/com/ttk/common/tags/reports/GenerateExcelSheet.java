/**
 * @ (#) ReportParameter.java July 4, 2006
 * Project       : TTK HealthCare Services
 * File          : ReportParameter.java
 * Author        : Srikanth H M
 * Company       : Span Systems Corporation
 * Date Created  : July 4, 2006
 *
 * @author       : Srikanth H M
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.common.tags.reports;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import oracle.net.aso.p;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.DOMReader;
import org.xml.sax.InputSource;

import com.ttk.common.TTKCommon;
import com.ttk.common.security.Cache;
import com.ttk.dto.common.CacheObject;






import java.io.IOException;
//import java.io.InputStream;
//import java.io.StringBufferInputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 *  This class builds the report parameter for generation of reports.
 */

public class GenerateExcelSheet extends TagSupport{

	/**
	* Comment for <code>serialVersionUID</code>
	*/
	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger( GenerateExcelSheet.class );
	public int doStartTag() throws JspException{
		JspWriter out =null;
		try
		{
			HttpServletRequest request=(HttpServletRequest)pageContext.getRequest();
			ArrayList<String> sheetHearderData	=	(ArrayList<String>)request.getSession().getAttribute("columnHeader"); 
			ArrayList<String[]> sheetColumnData	=	(ArrayList<String[]>)request.getSession().getAttribute("sheetColumnData"); 
			out= pageContext.getOut();
			out.print("<table cellpadding=\"1\"  cellspacing=\"1\" border=\"1\" bordercolor=\"gray\">");
			boolean ckeckError=false;
			 if(sheetColumnData.size()==1){
				 String[] outputArray=sheetColumnData.get(0);
				 if(outputArray.length==1 && "sql.error".equals(outputArray[0])){
					 ckeckError=true;
				 }
			 }
			 if(ckeckError==false){
				 if((sheetHearderData != null&&!sheetHearderData.isEmpty())&&(sheetColumnData != null&&!sheetColumnData.isEmpty())){
						out.println("<tr bgcolor=\"#9999FF\">");
						for(int inc=0;inc<sheetHearderData.size();inc++)
						out.println("<td style=\"text-align: center; height: 33px;\"><b>"+sheetHearderData.get(inc)+"</b></td>");				
						out.println("</tr>");
						String[] temp	=	new String[sheetColumnData.size()+1];
						for(int i=0;i<sheetColumnData.size();i++)	{
							temp	=	(String[])sheetColumnData.get(i);
							out.println("<tr>");
							for(int k=0;k<temp.length;k++){
								out.println("<td style=\"text-align: center;\">"+temp[k]+"</td>");
							}
							out.println("</tr>");
						}
					}else{
						out.println("<tr> <td colspan=\"5\"><font color=\"blue\" size=\"10\">No Record Found</font>");
					}
			 }else{
				 out.println("<tr> <td colspan=\"8\" style=\"text-align: center;\"><font color=\"red\" size=\"10\">There is some went wrong, Please contact Alkoot administrator.</font>");
			 }
			
			out.println("</table>");
		}//end of try block
		catch(Exception exp)
		{
			exp.printStackTrace();
			try {
				out.println("<tr> <td colspan=\"8\" style=\"text-align: center;\"><font color=\"red\" size=\"10\">There is some went wrong, Please contact Alkoot administrator.</font>");
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			log.info("error occured in ReportParameter Tag Library!!!!! ");
		}//end of catch block
		return SKIP_BODY;
	}//end of doStartTag()

	public int doEndTag() throws JspException
	{
		return EVAL_PAGE;//to process the rest of the page
	}//end doEndTag()
}//end of class ReportParameter