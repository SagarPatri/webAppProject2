/**
 * @ (#) BinaryStreamReportServlet.java July 6, 2006
 * Project       : TTK HealthCare Services
 * File          : BinaryStreamReportServlet.java
 * Author        : Balaji C R B
 * Company       : Span Systems Corporation
 * Date Created  : July 6, 2006
 *
 * @author       : Balaji C R B
 * Modified by   :
 * Modified date :
 * Reason        :
 */
package com.ttk.common.tags;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class BinaryStreamReportServlet extends HttpServlet
{
	/**
	* Comment for <code>serialVersionUID</code>
	*/
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger( BinaryStreamReportServlet.class );
	
	public void service(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException
	{
		try{
			log.debug("inside BinaryStreamReportServlet service method::");
			String strReportType = req.getParameter("reportType");
			if("PDF".equals(strReportType))
			{
				//log.info("report type is pdf");
				res.setContentType("application/pdf");
			}//end of if("PDF".equals(strReportType))
			else if("EXCEL".equals(strReportType))
			{
				//log.info("report type is xls");
				res.setContentType("application/vnd.ms-excel");
			}//end of else if("EXCEL".equals(strReportType))
			else if("TXT".equals(strReportType))
			{
				log.info("report type is txt");
				res.setContentType("text/plain");
				res.addHeader("Content-Disposition","attachment; filename=Report.txt");
				
			}//end of else if("TXT".equals(strReportType)		
			else if("CSV".equals(strReportType))
			{
				res.setContentType("text/csv");
				res.setHeader("Content-Disposition", "attachment; filename=\"Report.csv\"");
			}
			
			if("EXCEL".equals(req.getAttribute("reportType"))){
				res.setContentType("application/vnd.ms-excel");
			}
			ByteArrayOutputStream baos = null;
			if(req.getParameter("displayFile")!=null)
			{
				FileInputStream fis = new FileInputStream(req.getParameter("displayFile"));
				BufferedInputStream bis = new BufferedInputStream(fis);
				baos=new ByteArrayOutputStream();
				int ch;
				while ((ch = bis.read()) != -1)
				{
					baos.write(ch);
				}//end of while ((ch = bis.read()) != -1)
				if(bis!=null)bis.close();
			}//end of if(req.getParameter("displayFile")!=null)
			else
			{
				baos = (ByteArrayOutputStream)req.getAttribute("boas");
			}//end of else
			
			if(baos==null)
			{
				baos = (ByteArrayOutputStream)req.getSession().getAttribute("boas");
			}//end of if(baos==null)
			OutputStream sos = res.getOutputStream();
			baos.writeTo(sos);	
		}//end of try
		catch(IOException ioExp)
		{
			ioExp.printStackTrace();
		}//end of catch(IOException ioExp)
		catch(Exception exp)
		{
			exp.printStackTrace();
		}//end of catch(Exception e)		
	}//end of service(HttpServletRequest req, HttpServletResponse res)
}//end of BinaryStreamReportServlet