<%@page import="org.apache.struts.action.DynaActionForm"%>
<%
/**
 * @ (#) UploadMOUDocsAction.java 31 Dec 2014
 * Project      : TTK HealthCare Services
 * File         :UploadMOUDocsAction.java
 * Author       : Kishor kumar S H
 * Company      : RCS Technologies
 * Date Created : 31 Dec 2014
 *
 * @author       : Kishor kumar S H
 * Modified by   : 
 * Modified date : 31 Dec 2014
 */
%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>

<script language="javascript" src="/ttk/scripts/empanelment/preAuthDocsList.js"></script>
<%  
DynaActionForm frmMOUDocs=(DynaActionForm)request.getAttribute("frmMOUDocs");
    boolean viewmode=true;
   
    if(TTKCommon.isAuthorized(request,"Edit"))
    {
        viewmode=false;
    }//end of if(TTKCommon.isAuthorized(request,"Edit"))
    pageContext.setAttribute("viewmode",new Boolean(viewmode));
    String Caption=(String)session.getAttribute("caption");
	pageContext.setAttribute("provDocsType", Cache.getCacheObject("provDocsType"));
%>

<html:form action="/UploadMOUCertificatesList.do"  method="post" enctype="multipart/form-data">
	<h4 class="sub_heading">Upload Documents</h4>

	<!-- S T A R T : Page Title -->
	<%-- <table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		<tr>
    		<td>Upload Documents- <bean:write name="frmMOUDocs" property="caption"/></td>   
    		</tr>
	</table> --%>
	<html:errors/>
	<!-- E N D : Page Title --> 
	<div style="width: 99%; float: right;">
	<div class="scrollableGrid" style="height:290px;">
	
	<logic:notEmpty name="notify" scope="request">
		<table align="center" class="errorContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td><img src="/ttk/images/ErrorIcon.gif" alt="Alert" title="Alert" width="16" height="16" align="absmiddle">&nbsp;
	          <bean:write name="notify" scope="request"/>
	        </td>
	      </tr>
   	 </table>
   	 </logic:notEmpty>
   	 
	<!-- S T A R T : Success Box -->
	<logic:notEmpty name="updated" scope="request">
		<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td><img src="/ttk/images/SuccessIcon.gif" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
					<bean:message name="updated" scope="request"/>
				</td>
			</tr>
		</table>
	</logic:notEmpty>
	<!-- E N D : Success Box -->
	<!-- S T A R T : Form Fields -->
	<br>
	
	<!-- S T A R T : Grid -->
	<ttk:HtmlGrid name="tableDataMouCertificates" className="gridWithCheckBox zeroMargin"/>
	<!-- E N D : Grid -->
	
	
	</div>
	<!-- S T A R T : Buttons -->
	<br>
	<%-- <table class="buttonsSavetolistGrid" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%" align="right" nowrap class="formLabel">
			<%
	    		if(TTKCommon.isDataFound(request,"tableDataAssociateCertificates") ||TTKCommon.isDataFound(request,"tableDataMouCertificates")&& TTKCommon.isAuthorized(request,"Delete"))
	    			
				{
		    %>
					<button type="button" name="Button" accesskey="d" class="olbtnSmall" onClick="javascript:onDelete();"><u>D</u>elete</button>
			<%
        		}// end of if(TTKCommon.isDataFound(request,"tableDataLinkDetails") && TTKCommon.isAuthorized(request,"Delete"))
        	%>		
			</td>
		</tr>
	</table> --%>
	<!-- END: Buttons -->
	<fieldset>
		<legend>Document Details</legend>
		<table class="formContainer" border="0" cellspacing="0" cellpadding="0" style="margin-top:5px;">
	<logic:empty name="preAuthNoYN" scope="session">
			<tr  style="border:1px solid;">
          		<td width="19%"    align="left" nowrap>&nbsp;&nbsp;Description:<span class="mandatorySymbol">*</span></td>
          		<td width="18%"    align="left" nowrap>
          			<!-- html:text property="description"  styleClass="textBox textBoxMedium" style="width75px;"  maxlength="250" readonly="<%=viewmode%>" /-->
          			
          			<html:select property ="description" styleClass="selectBox selectBoxMedium">
          					<html:option value="">--Select from list--</html:option>
                 			<html:options collection="provDocsType" property="cacheId" labelProperty="cacheDesc"/>
          			</html:select>
          		</td>
          	          
	          <td align="left">Browse File : <span class="mandatorySymbol">*</span></td>
			<td>
				<html:file property="file" styleId="file"/>
			</td>
			</tr>
   </logic:empty>   
          	       <tr>
          	       	<td  colspan="4" align="right">
          	       		
          	       			<logic:empty name="preAuthNoYN" scope="session">
								<button type="button" name="Button" accesskey="u" class="olbtnSmall"  onClick="javascript:onSave()"><u>U</u>pload</button>&nbsp;&nbsp;&nbsp;
							</logic:empty>
							
							<logic:equal value="preAuthEnhance" name="fromFlag" scope="session">
								<button type="button" name="Button" accesskey="u" class="olbtnSmall" onClick="javascript:onSave()"><u>U</u>pload</button>&nbsp;&nbsp;&nbsp;
							</logic:equal>
						
					<button type="button" name="Button" accesskey="c" class="olbtnSmall"  onClick="javascript:onClose()"><u>C</u>lose</button>		
          	       	</td>
          	       </tr>
			  </table>
			  
		<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>
			<font color=blue><b>Note :</b>  Please Upload Only PDF Files.</font> 
			</td>
     		
     	</tr>
	</table>
		</fieldset>	
		
		</div>
		<!-- END : Form Fields -->
	<html:hidden property="mouDocSeqID"/>
	<INPUT TYPE="hidden" NAME="rownum" VALUE="">
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<input type="hidden" name="fromFlag" value="<%=request.getSession().getAttribute("fromFlag")%>">
</html:form>

<!-- E N D : Content/Form Area -->