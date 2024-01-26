<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="org.apache.struts.action.DynaActionForm"%>
<%
/**
 * 
 * Project      : 
 * File         :
 * Author       : 
 * Company      : 
 * Date Created : 
 *
 * @author       : 
 * Modified by   : 
 * Modified date : 
 */
%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<%@ page import=" com.ttk.common.TTKCommon" %>

<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<!-- <script language="javascript" src="/ttk/scripts/preauth/preauthUploads.js"></script> -->
<%  
DynaActionForm frmDocsUpload=(DynaActionForm)request.getAttribute("frmDocsUpload");
    boolean viewmode=true;
   
     if(TTKCommon.isAuthorized(request,"Edit"))
    {
        viewmode=false;
    }//end of if(TTKCommon.isAuthorized(request,"Edit"))
    pageContext.setAttribute("viewmode",new Boolean(viewmode));
    String typeId = (String)request.getAttribute("typeId");
	//System.out.println("typeId ::-"+typeId);
    if("PAT".equals(typeId))
    pageContext.setAttribute("descriptionCode", Cache.getCacheObject("descriptionCodeDocUpload"));
    else if("CLM".equals(typeId))
    pageContext.setAttribute("descriptionCode", Cache.getCacheObject("claimsDocUpload"));
    else if(("HOS|PAT".equals(typeId))||("PBM|PAT".equals(typeId)||("PTR|PAT".equals(typeId))))
    pageContext.setAttribute("descriptionCode", Cache.getCacheObject("provDocsType"));
    else if(("PBM|CLM".equals(typeId))||("HOS|CLM".equals(typeId))||("PTR|CLM".equals(typeId)))
        pageContext.setAttribute("descriptionCode", Cache.getCacheObject("provDocsTypeForClaim"));
    else
    pageContext.setAttribute("descriptionCode", Cache.getCacheObject("descriptionCodeDocUpload"));
    String requestStatus=(String) session.getAttribute("requestStatus");
    String aplDocs = (String)request.getAttribute("APL");
%>
<html:form action="/DocsUploadList.do"  method="post" enctype="multipart/form-data">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
			 <tr>
    			<td>Uploads- <bean:write name="frmDocsUpload" property="caption"/></td>   
    		</tr>
	</table>
	<html:errors/>
	<!-- E N D : Page Title --> 
	<div style="width: 99%; float: right;">
	<div class="scrollableGrid" style="height:290px;">
	
	<logic:notEmpty name="notify" scope="request">
		<table align="center" class="errorContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td><img src="/ttk/images/ErrorIcon.gif" title="Alert" alt="Alert"  width="16" height="16" align="absmiddle">&nbsp;
	          <bean:write name="notify" scope="request"/>
	        </td>
	      </tr>
   	 </table>
   	 </logic:notEmpty>
   	 
	<!-- S T A R T : Success Box -->
	<logic:notEmpty name="updated" scope="request">
		<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td><img src="/ttk/images/SuccessIcon.gif" title="Alert" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
					<bean:message name="updated" scope="request"/>
				</td>
			</tr>
		</table>
	</logic:notEmpty>
	<!-- E N D : Success Box -->
	<!-- S T A R T : Form Fields -->
	<br>
	
	<!-- S T A R T : Grid -->
	<ttk:HtmlGrid name="tableDataDocsUpload" className="gridWithCheckBox zeroMargin"/>
	<!-- E N D : Grid -->
	
	
	</div>
	<!-- S T A R T : Buttons -->
	<br>
 <table class="buttonsSavetolistGrid" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%" align="right" nowrap class="formLabel">
			<%
	    		if(TTKCommon.isDataFound(request,"tableDataDocsUpload") ||TTKCommon.isDataFound(request,"tableDataDocsUpload")&& TTKCommon.isAuthorized(request,"Delete"))
	    			
				{
	    			if(requestStatus!=null&&(requestStatus.equals("Required Information")||requestStatus.equals("In-Progress")||requestStatus.equals("Shortfall")||requestStatus.equals("In Progress"))){
		    %>
					<button type="button" name="Button" accesskey="d" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onCommonDelete()"><u>D</u>elete</button>
			<%
	    			}}// end of if(TTKCommon.isDataFound(request,"tableDataLinkDetails") && TTKCommon.isAuthorized(request,"Delete"))
        	%>		
			</td>
		</tr>
	</table> 
	<!-- END: Buttons -->
	<% if((typeId.equals("CFD|CLM") || typeId.equals("CFD|PAT"))&&(requestStatus.equals("Required Information")||requestStatus.equals("INP")||requestStatus.equals("Shortfall")||requestStatus.equals("In Progress"))) {%>
		<fieldset>
		<legend>Uploading Documents Details</legend>
		<table class="formContainer" border="0" cellspacing="0" cellpadding="0" style="margin-top:5px;">
			
			<tr>
				
          		<td width="19%"    align="left" nowrap>&nbsp;&nbsp;Description:<span class="mandatorySymbol">*</span></td>
          		<td width="18%"    align="left" nowrap>
          			<!-- html:text property="description"  styleClass="textBox textBoxMedium" style="width75px;"  maxlength="250" readonly="<%=viewmode%>" /-->
          			
          			<html:select property ="description" styleClass="selectBox selectBoxMedium" disabled="<%=viewmode%>">
          					<html:option value="">--Select from list--</html:option>
                 			<html:option value="CFD">CFD Documents</html:option>
          			</html:select>
          		</td>
          	          
	          <td align="left">Browse File : <span class="mandatorySymbol">*</span></td>
			<td>
				<html:file property="file" styleId="file"/>
			</td>
			
			
          		
          		  
          	      </tr>
          	      
          	       <tr>
          	       	<td  colspan="4" align="right">
          	       		<logic:match name="viewmode" value="false">
									<button type="button" name="Button" accesskey="u" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:doCommonUpload()"><u>U</u>pload</button>&nbsp;&nbsp;&nbsp;
									<!-- button type="button" name="Button" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onReset()"><u>R</u>eset</button>&nbsp;&nbsp;&nbsp;-->
								</logic:match>
						
          	       	</td>
          	       </tr>
			  </table>
			  
		<!-- <table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>
			<font color=blue><b>Note :</b>  Please Upload DOC,XLS OR PDF files.</font> 
			</td>
     		
     	</tr>
	</table> -->
		</fieldset>
		<%}else if(requestStatus!=null&&typeId!=null&&(!typeId.equals("PBM|CLM"))&&(requestStatus.equals("Required Information")||requestStatus.equals("In-Progress")||requestStatus.equals("Shortfall")||requestStatus.equals("In Progress"))){ %>
	 
 	<fieldset>
		<legend>Uploading Documents Details</legend>
		<table class="formContainer" border="0" cellspacing="0" cellpadding="0" style="margin-top:5px;">
			
			<tr>
				
          		<td width="19%"    align="left" nowrap>&nbsp;&nbsp;Description:<span class="mandatorySymbol">*</span></td>
          		<td width="18%"    align="left" nowrap>
          			<!-- html:text property="description"  styleClass="textBox textBoxMedium" style="width75px;"  maxlength="250" readonly="<%=viewmode%>" /-->
          			
          			<html:select property ="description" styleId="description" styleClass="selectBox selectBoxMedium" disabled="<%=viewmode%>">
          					<html:option value="">--Select from list--</html:option>
                 			<html:options collection="descriptionCode" property="cacheId" labelProperty="cacheDesc"/>
          			</html:select>
          		</td>
          	          
	          <td align="left">Browse File : <span class="mandatorySymbol">*</span></td>
			<td>
				<html:file property="file" styleId="file"/>
			</td>
			
			
          		
          		  
          	      </tr>
          	      
          	       <tr>
          	       	<td  colspan="4" align="right">
          	       		<logic:match name="viewmode" value="false">
									<button type="button" name="Button" accesskey="u" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:doCommonUpload()"><u>U</u>pload</button>&nbsp;&nbsp;&nbsp;
									<!-- button type="button" name="Button" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onReset()"><u>R</u>eset</button>&nbsp;&nbsp;&nbsp;-->
								</logic:match>
						
          	       	</td>
          	       </tr>
			  </table>
			  
		<!-- <table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>
			<font color=blue><b>Note :</b>  Please Upload DOC,XLS OR PDF files.</font> 
			</td>
     		
     	</tr>
	</table> -->
		</fieldset>	 
		<%} %>
		
	<% 
			if(aplDocs!=null && aplDocs.length()>0){
	%>
		<fieldset>
		<legend>Uploading Documents Details</legend>
		<table class="formContainer" border="0" cellspacing="0" cellpadding="0" style="margin-top:5px;">
	<tr>
          		<td width="19%"    align="left" nowrap>&nbsp;&nbsp;Description:<span class="mandatorySymbol">*</span></td>
          		<td width="18%"    align="left" nowrap>
          			<!-- html:text property="description"  styleClass="textBox textBoxMedium" style="width75px;"  maxlength="250" readonly="<%=viewmode%>" /-->
          			
          			<html:select property ="description" styleId="description" styleClass="selectBox selectBoxMedium" disabled="<%=viewmode%>">
          					<html:option value="APL">Appeal Documents</html:option>
                 	<%-- 		<html:options collection="descriptionCode" property="cacheId" labelProperty="cacheDesc"/> --%>
          			</html:select>
          		</td>
          	          
	          <td align="left">Browse File : <span class="mandatorySymbol">*</span></td>
			<td>
				<html:file property="file" styleId="file"/>
			</td>
    </tr>
          	      
          	       <tr>
          	       	<td  colspan="4" align="right">
          	       		<logic:match name="viewmode" value="false">
									<button type="button" name="Button" accesskey="u" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:doCommonUpload()"><u>U</u>pload</button>&nbsp;&nbsp;&nbsp;
									<!-- button type="button" name="Button" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onReset()"><u>R</u>eset</button>&nbsp;&nbsp;&nbsp;-->
								</logic:match>
						
          	       	</td>
          	       </tr>
			  </table>
		</fieldset>	 
	<%} %>	
		 
		
		<center><button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onCommonUploadClose('${typeId}')"><u>C</u>lose</button>	<center>
		<br><br><br><br><br><br><br>
		
		<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
		
		<tr>
   	 		<td align="left"><br><font size="2"><b>Note:</b> File format should be any of these extensions (.pdf,.doc,.docx,.xls,.xlsx).</font></td>
   		</tr>
		<% 
			if(aplDocs!=null && aplDocs.length()>0){
		%>
			<tr>
   	 			<td align="left"><font size="2"><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;After uploading Appeal Documents please Save the appeal comments in the previous screen for further process.</font></td>
   			</tr>
   		<%} %>
   		
		
   		</table>
		</div>
		<!-- END : Form Fields -->
	<html:hidden property="mouDocSeqID"/>
	<INPUT TYPE="hidden" NAME="rownum" VALUE="">
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<input type="hidden" name="typeId" value="${typeId}" id="typeId" />
	<input type="hidden" name="seqId" value="${seqId}" id="seqId"/> 
	<html:hidden property="source_id" styleId="source_id" name="frmDocsUpload" value="${typeId}" />
	<html:hidden property="authType" styleId="authType" name="frmDocsUpload" value="${typeId}" />
	<html:hidden property="preAuthSeqID" />
</html:form>

<!-- E N D : Content/Form Area -->