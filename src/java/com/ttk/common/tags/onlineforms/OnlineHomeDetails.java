/**
 
 * @ (#) OnlineHomeDetails.java Dec 28,2007
 * Project : TTK HealthCare Services
 * File : OnlineHomeDetails.java
 * Author : Balakrishna E
 * Company : Span Systems Corporation
 * Date Created : Dec 28,2007
 *
 * @author : Balakrishna E
 * Modified by :
 * Modified date :
 * Reason :
 */

package com.ttk.common.tags.onlineforms;

import java.net.URL;
import java.util.List;

import javax.activation.DataHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

//import org.apache.log4j.Logger;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.dao.impl.onlineforms.OnlineAccessDAOImpl;
import com.ttk.dto.usermanagement.UserSecurityProfile;

/**
 *  This class builds the Online home Details
 */
public class OnlineHomeDetails extends TagSupport{
	/**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;
    
//	private static Logger log = Logger.getLogger( OnlineHomeDetails.class );
	
	public int doStartTag() throws JspException{
		try
		{			
			Document onlinehome = (Document)pageContext.getRequest().getAttribute("onlinehome");
			//log.info(onlinehome.asXML());
			HttpServletRequest request=(HttpServletRequest)pageContext.getRequest();
			JspWriter out = pageContext.getOut();//Writer object to write the file
			UserSecurityProfile userSecurityProfile = ((UserSecurityProfile)
														request.getSession().getAttribute("UserSecurityProfile"));
			String strLoginType = userSecurityProfile.getLoginType();
			//log.info("strLoginType is :"+strLoginType);
			//log.info("getPolicyNo is :"+userSecurityProfile.getPolicyNo());
			String strPolicyNbr = userSecurityProfile.getPolicyNo();
			String strPolicySeqID =null;
			String strSelectedSeqId=TTKCommon.checkNull(request.getParameter("policySelect"));
		
			
			
		//	
		//	
			Element onlineElement=null;
			Element onlineplElement=null;
			Element onlinelinElement=null;
			Element onlineInscompElement=null;
			List  onlineHomedetailslist=null;
			List policyList = null;
			List onlineHomelink = null;
			List onlineHomeInscomp = null;
			URL url = null;
			if(onlinehome != null)
			{	
				if(!((List)onlinehome.selectNodes("//logodetails/logo")).isEmpty())
				{
					boolean bflag = false;
					boolean lineFlag = true;
					boolean lFlag = false;
					onlineHomedetailslist = (List)onlinehome.selectNodes("//logodetails/logo");
					
					
					out.print("<table align=\"center\" class=\"formContainerWeblogin\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
					out.print("<tr>");
					DataHandler dh = null;
					for(int i=0;(onlineHomedetailslist!=null && i<onlineHomedetailslist.size());i++)
					{
						onlineElement = (Element)onlineHomedetailslist.get(i);
						String str = TTKPropertiesReader.getPropertyValue("WebLoginImg")+onlineElement.valueOf("@path");
						
						url = new URL(str);
						dh = new DataHandler(url);
						try
						{
							dh.getInputStream();
							if(!bflag)
							{
								if(onlineElement.valueOf("@linkgentypeid").equals("WLG"))
								{
									out.print("<td style=\"padding-bottom:5px;\">");
									out.print("<img src=\""+TTKPropertiesReader.getPropertyValue("WebLoginImg")+onlineElement.valueOf("@path")+"\" style=\"padding-left:10px;padding-top:5px;\" alt=\"Logo\" width=\"198\" height=\"55\">");
									out.print("</img>");
									out.print("</td>");
									bflag=true;
									lFlag=true;
								}//end of if(!bflag)									
								else
								{
									out.print("<td width=\"290\" height=\"55\" style=\"padding-bottom:5px;\">");	
									out.print("</td>");
									if(onlineElement.valueOf("@linkgentypeid").equalsIgnoreCase("WLC"))
									{
										out.print("<td  valign=\"top\" nowrap style=\"padding-top:15px;padding-bottom:0px;\"> ");
										out.print("<img src=\""+TTKPropertiesReader.getPropertyValue("WebLoginImg")+onlineElement.valueOf("@path")+"\" alt=\"Logo\" width=\"300\" height=\"55\">");
										out.print("</img>");
										out.print("</td>");										
									}//end of if(onlineElement.valueOf("@linkgentypeid").equalsIgnoreCase("WLC"))
									else if(onlineElement.valueOf("@linkgentypeid").equals("WCT"))
									{
										out.print("<td  valign=\"middle\" nowrap align=\"left\" width=\"500\" height=\"55\" >");
										out.print("<h2 align=\"left\" style=\"color:#003366;\">"+onlineElement.valueOf("@weblinkdesc")+"</h2>");
										out.print("</td>");											
									}//end of else if(onlineElement.valueOf("@linkgentypeid").equals("WCT"))
									lFlag=true;
								}//end of else
							}//end of if(!bflag)
							else if(bflag)
							{									
								if(onlineElement.valueOf("@linkgentypeid").equalsIgnoreCase("WLC"))
								{
									out.print("<td  valign=\"top\" nowrap style=\"padding-top:15px;padding-bottom:0px;\"> ");
									out.print("<img src=\""+TTKPropertiesReader.getPropertyValue("WebLoginImg")+onlineElement.valueOf("@path")+"\" alt=\"Logo\" width=\"300\" height=\"55\">");
									out.print("</img>");
									out.print("</td>");									
								}//end of else if(bflag)
								else if(onlineElement.valueOf("@linkgentypeid").equals("WCT"))
								{
									out.print("<td  valign=\"middle\" nowrap align=\"left\" width=\"500\" height=\"55\" >");
									out.print("<h2 align=\"left\" style=\"color:#003366;\">"+onlineElement.valueOf("@weblinkdesc")+"</h2>");
									out.print("</td>");									
								}//end of else if(onlineElement.valueOf("@linkgentypeid").equals("WCT"))	
								lFlag=true;
							}//end of else if(bflag)								
						}catch(Exception e)
						{
							lineFlag = false;
							//log.info("inside catch block ");
							e.printStackTrace();
						}//end of catch
						finally{							
							out.print("</td>");
						}//end of finally						
					}//end of for(int i=0;(onlineHomedetailslist!=null && i<onlineHomedetailslist.size());i++)
					if(!lineFlag)
					{
						if(lFlag)
						{
							out.print("<td>");
							out.print("</td>");
							out.print("<td cwidth=\"3%\" valign=\"top\">");
							out.print("</td>");
							out.print("</tr>");
							out.print("<tr>");
							out.print("<td colspan=\"3\" valign=\"top\" align=\"left\" style=\"padding-left:9px;\">");
							out.print("<hr size=\"2\" width=\"99%\" noshade style=\"color:#cccccc;\">");
							out.print("</hr>");
						}//end of if(lFlag)
					}//end of if(lineFlag)
					else
					{
						out.print("<td>");
						out.print("</td>");
						out.print("<td cwidth=\"3%\" valign=\"top\">");
						out.print("</td>");
						out.print("</tr>");
						out.print("<tr>");
						out.print("<td colspan=\"3\" valign=\"top\" align=\"left\" style=\"padding-left:9px;\">");
						out.print("<hr size=\"2\" width=\"99%\" noshade style=\"color:#cccccc;\">");
						out.print("</hr>");
					}//end of else
					out.print("</tr>");
					out.print("</table>");
				}//end of if(!((List)onlinehome.selectNodes("//logodetails/logo")).isEmpty())
				
				if(!((List)onlinehome.selectNodes("//policydetails/policy")).isEmpty())
				{
					
				//	
					out.print("<table align=\"center\" width=\"100%\" class=\"formContainerWeblogin\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
					out.print("<tr>");
					if(!"EMPL".equals(strLoginType))
					out.print("<td nowrap height=\"25\" class=\"subHeader\" style=\"padding-top:10px;padding-left:20px\" >Home page information for Policy Number:&nbsp;");
					else
						out.print("<td nowrap height=\"25\" class=\"subHeader\" style=\"padding-top:10px;padding-left:20px;font-size:14px\" >Policy Number:&nbsp;");
					out.print("</td>");
					out.print("<td nowrap style=\"padding-top:10px;\">");
					
					policyList = (List)onlinehome.selectNodes("//policydetails/policy");
					if(strLoginType.equals("H"))
					{
						
						out.print("<select name=\"policySelect\" class=\"selectBoxWeblogin selectBoxLargestWeblogin\" onchange=\"javascript:onpolicychange();\">");
					//	
						for(int i=0;(policyList!=null && i<policyList.size());i++)
						{
							onlineplElement = (Element)policyList.get(i);
							
							//out.print("<option value=\""+onlineplElement.valueOf("@policyseqid")+"\" onchange=\"javascript:onlinepolicy('"+onlineplElement.valueOf("@policyseqid")+"' >"+onlineplElement.valueOf("@policyno")+"</option>");
							out.print("<option value=\""+onlineplElement.valueOf("@policyseqid")+"\" ");
							out.print(strSelectedSeqId.equals(onlineplElement.valueOf("@policyseqid"))? "selected":" ");
							
							if(i==0)
							{
								strPolicySeqID = onlineplElement.valueOf("@policyseqid");
							}//end of if(i==0)
							out.print(" >"+onlineplElement.valueOf("@policyno")+"</option>");
						}//end of for(int i=0;(policyList!=null && i<policyList.size());i++)
						out.print("</select>");
					}else
					{
						
					//out.print("<td nowrap height=\"25\" class=\"subHeader\" style=\"padding-top:10px;padding-left:10px\" >"+userSecurityProfile.getPolicyNo());
					out.print("<select name=\"policySelect\" class=\"selectBoxWeblogin selectBoxLargestWebloginNew\" onchange=\"javascript:onpolicychange();\">");
					//	out.print("<option value=\"\">Select From list</option>");
						for(int i=0;(policyList!=null && i<policyList.size());i++)
						{
							onlineplElement = (Element)policyList.get(i);
							
							//out.print("<option value=\""+onlineplElement.valueOf("@policyseqid")+"\" onchange=\"javascript:onlinepolicy('"+onlineplElement.valueOf("@policyseqid")+"' >"+onlineplElement.valueOf("@policyno")+"</option>");
							out.print("<option value=\""+onlineplElement.valueOf("@policyno")+"\" ");
							// Change added for KOC1227B
							out.print(strPolicyNbr.equals(onlineplElement.valueOf("@policyno"))? "selected":" ");
							if(i==0)
							{
								strPolicySeqID = onlineplElement.valueOf("@policyseqid");
								
							}//end of if(i==0)
							out.print(" >"+onlineplElement.valueOf("@policyno")+"</option>");
						}//end of for(int i=0;(policyList!=null && i<policyList.size());i++)
						out.print("</select>");
					}//end of else
					out.print("</td>");
					out.print("<td width=\"100%\">");
					out.print("</td>");
					out.print("</br>");
					out.print("</table>");
				}//end of if(!((List)onlinehome.selectNodes("//policydetails/policy")).isEmpty()) 
				if(!((List)onlinehome.selectNodes("//inscompinfo/insinfo")).isEmpty())
				{
					onlineHomeInscomp = (List)onlinehome.selectNodes("//inscompinfo/insinfo");
				
					for(int i=0;(onlineHomeInscomp!=null && i<onlineHomeInscomp.size());i++)
					{
						out.print("<table align=\"center\" width=\"100%\" class=\"formContainerWeblogin\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
						onlineInscompElement = (Element)onlineHomeInscomp.get(i);
					
						String[] str = onlineInscompElement.valueOf("@inscompinfo").split("</br>");
					//	
						for(int j=0; j<str.length; j++)
						{
							out.print("<tr>");
							out.print("<td colspan=\"3\" style=\"padding-top:10px;padding-left:20px\">");
							out.print("<div style=\"width:750px;\">");
							
							//out.print("<label style=\"font-face:tahoma;font-size:11px;font-weight:bold;color:#A83108;\">"+onlineInscompElement.valueOf("@inscompinfo").replaceAll("\n","</br>"));
							out.print("<label style=\"font-face:tahoma;font-size:11px;font-weight:bold;color:#A83108;\">"+str[j]);
							out.print("</label>");
							out.print("</div>");
							out.print("</td>");
							out.print("</tr>");
						}
						out.print("</table>");
						out.print("</br>");
					}//end of for(int i=0;(onlineHomeInscomp!=null && i<onlineHomeInscomp.size());i++)					
				}//end of if(!((List)onlinehome.selectNodes("//inscompinfo/insinfo")).isEmpty())
				if(!((List)onlinehome.selectNodes("//linkdetails/link")).isEmpty())
				{
					String strFileName = "";
					String strConCat = "";
					String strReportType = "";
					out.print("<div style=\"padding-left:25px;\">");
					out.print("<fieldset style=\"margin:0px; padding:0px;\">");
					out.print("<table align=\"center\" width=\"100%\" class=\"formContainerWeblogin\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
					
					out.print("<tr class=\"headerInfoValue\" style=\"background:#E6E9ED; padding-left:2px\"> ");
					out.print("<td height=\"20\">&nbsp;&nbsp;List of References ");
					out.print("</td>");
					out.print("</tr>");				
					onlineHomelink = (List)onlinehome.selectNodes("//linkdetails/link");
					for(int i=0;(onlineHomelink!=null && i<onlineHomelink.size());i++)
					{
						onlinelinElement = (Element)onlineHomelink.get(i);
						
						
						// Changes done for CR1168 Feedback Forms
						if(onlinelinElement.valueOf("@linktypeid").equals("WFB") && (strLoginType.equals("E")))
						{
							
							if(onlinelinElement.valueOf("@linkdesc").equalsIgnoreCase("TTK Feedback Form Cashless"))
							{
								out.print("<tr>");
								out.print("<td style=\"padding-top:10px;\">");
								out.print("<ul style=\"margin-bottom:0px;\">");
								out.print("<li>");
								out.print("<a href=\"#\" onclick=\"javascript:onCashless()\">"+onlinelinElement.valueOf("@linkdesc"));
								out.print("</a>");
								out.print("</li>");
								out.print("</ul></td></tr>");			
							}
							else if(onlinelinElement.valueOf("@linkdesc").equalsIgnoreCase("TTK Feedback Form Member Reimbursement"))
							{
								out.print("<tr>");
								out.print("<td style=\"padding-top:10px;\">");
								out.print("<ul style=\"margin-bottom:0px;\">");
								out.print("<li>");
								out.print("<a href=\"#\" onclick=\"javascript:OnMReimbursement()\">"+onlinelinElement.valueOf("@linkdesc"));
								out.print("</a>");
								out.print("</li>");
								out.print("</ul></td></tr>");							
							}
						}
						else if(onlinelinElement.valueOf("@reporttype").equals("WRC") || onlinelinElement.valueOf("@reporttype").equals("WRA")){							
							out.print("<tr>");
							out.print("<td style=\"padding-top:10px;\">");
							out.print("<ul style=\"margin-bottom:0px;\">");
							out.print("<li>");
							strReportType = onlinelinElement.valueOf("@reporttype");
							if(strLoginType.equals("H"))
							{
								//log.info("Lenth of seleted seq id length"+strSelectedSeqId);
								if(strSelectedSeqId.length() >0 && strSelectedSeqId != null)
								{
									strConCat = "|"+strReportType+"||"+strSelectedSeqId+"|";
								}//end of if(strSelectedSeqId.length() >0 && strSelectedSeqId != null)
								else
								{
									strConCat = "|"+strReportType+"||"+strPolicySeqID+"|";
								}//end of else
							}//end of if(strLoginType.equals("H"))
							else
							{
								strConCat = "|"+strReportType+"|"+strPolicyNbr+"||";
							}//end of else
								if(!onlinelinElement.valueOf("@linktypeid").equals("WFB"))
							out.print("<a href=\"javascript:void(0)\" onclick=\"openHospDoc('"+strConCat+"')\">"+onlinelinElement.valueOf("@linkdesc"));
								out.print("</a>");
								out.print("</li>");
								out.print("</ul></td></tr>");								
						}//end of if(onlinelinElement.valueOf("@linkpath") !="") linktypeid="WRC"
						else
						{
							//strFileName = TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/"+onlinelinElement.valueOf("@linkpath");			
								if(!onlinelinElement.valueOf("@linktypeid").equals("WFB")) 
								{
									out.print("<tr>");
									out.print("<td style=\"padding-top:10px;\">");
									out.print("<ul style=\"margin-bottom:0px;\">");
									out.print("<li>");
									strFileName = onlinelinElement.valueOf("@linkpath");
									out.print("<a href=\"javascript:void(0)\" onclick=\"openDoc('"+strFileName+"')\">"+onlinelinElement.valueOf("@linkdesc"));
									out.print("</a>");
									out.print("</li>");
									out.print("</ul></td></tr>");								
								}
						}//end of else
						
					}//end of for(int i=0;(onlineHomelink!=null && i<onlineHomelink.size());i++)
					// End of changes done for CR1168 Feedback Forms	
					out.print("</table>");
					out.print("</fieldset>");
					out.print("</div>");
				}//end of if(!((List)onlinehome.selectNodes("//linkdetails/link")).isEmpty())				
			}//end of if(doc != null)
			
			else
			{
				if(!strLoginType.equals("B"))
				{
				out.print("<table align=\"center\" class=\"formContainerWeblogin\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
				out.print("<tr>");
				out.print("<td width=\"22%\" height=\"25\" class=\"formLabelBold\">");
				out.print("<font size=\"2\" color=\"#0C48A2\">Home doesnt contain xml data</font>");
				out.print("</td>");
				out.print("</tr>");
				out.print("</table>");
				}
			}//end of else
		}//end of try block
		catch(NumberFormatException exp)
		{	
			exp.printStackTrace();
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
		}//end of catch block
		return SKIP_BODY;
	}//end of doStartTag()
	public int doEndTag() throws JspException {
		return EVAL_PAGE;//to process the rest of the page
	}//end doEndTag()
}//end of class OnlineHomeDetails
