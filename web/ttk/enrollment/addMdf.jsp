<%@page import="com.ttk.dto.enrollment.MemberVO"%>
<%@page import="java.util.ArrayList"%> 
<%
/** @ (#) addMdf.jsp 28, Feb 2022
 * Project     : Vidal Health
 * File        : addMdf.jsp
 * Author      : Kishor kumar S H
 * Company     : Vidal Health
 * Date Created: 28, July 2022
 *
 * @author 		 : Kishor kumar S H
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */
%>

<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,org.apache.struts.action.DynaActionForm"%>

<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/enrollment/addMdf.js"></script>
<script type="text/Javascript">
</script>
<SCRIPT LANGUAGE="JavaScript">
	var JS_SecondSubmit=false;
</SCRIPT>	
<%
	//added for Policy Deductable - KOC-1277
	DynaActionForm frmAddMdfMember = (DynaActionForm)request.getSession().getAttribute("frmAddMdfMember");
	ArrayList<MemberVO> alUploadedMdfFiles	=	(ArrayList<MemberVO>)request.getSession().getAttribute("alUploadedMdfFiles");
	boolean viewmode=true;boolean checkmode=true;
	if(TTKCommon.isAuthorized(request,"Edit"))
	{
		viewmode=false;
	}//end of if(TTKCommon.isAuthorized(request,"Edit"))
	//ended
	pageContext.setAttribute("viewmode",new Boolean(viewmode));
%>

<html:form action="/AddMdfMemberAction.do" enctype="multipart/form-data"> 
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
    	<tr>
	   		<td width="100%"><bean:write name="frmAddMdfMember" property="caption" /></td>
	   		<td align="right">&nbsp;&nbsp;&nbsp;</td>    
  		</tr>
	</table>
<!-- E N D : Page Title --> 

<div class="contentArea" id="contentArea">
<html:errors/>
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
	<fieldset>
	
 <legend>Upload MDF</legend>

	<table align="center" class="gridWithCheckBox zeroMargin" border="0" cellspacing="0" cellpadding="0">
      
      <tr> 
      <td> Select Member <span class="mandatorySymbol">*</span>: </td>
      	<td> 
      		<html:select property="enrollmentId"> 
      			<html:option value="">---Select from List---</html:option>
      			<html:optionsCollection property="enrollmentIds" label="cacheDesc" value="cacheId"/>
      		</html:select>
      	</td>	
      	<td> Select File <span class="mandatorySymbol">*</span>: <html:file property="files"/> </td>
      </tr>	
      <tr><td> &nbsp; </td></tr>
      <tr> <td colspan="2"> <span style="color: green"> Only PDF and JPG files are allowed. Maximum allowed file size is 2 MB</span></td> </tr> 
    </table>
         

	
	</fieldset>
<!-- S T A R T : Buttons -->

<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" align="center">
    <logic:notEmpty name="frmAddMdfMember" property="membersDetailsInfo">
    	 <logic:match name="viewmode" value="false">
    	 	<button type="button" name="Button" accesskey="u" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:return onUpload();"><u>U</u>pload</button>&nbsp;
    	 </logic:match>
  </logic:notEmpty>
		    <button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose()"><u>C</u>lose</button>&nbsp;
    </td>
    
  </tr>
</table>
<!-- E N D : Buttons -->


<!-- S T A R T SELECT TABLE MEMBER DETAILS -->
<fieldset>
	
<legend>MDF File Uploads</legend>
  <table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0" style="border: 1px solid #cccccc; ">
      <tr> 
      	<th class="gridHeader"> Sl No. </th>
      	<th class="gridHeader"> Enrollment Id </th>
      	<th class="gridHeader"> Member Name </th>
      	<th class="gridHeader"> File Name </th>
      	<th class="gridHeader"> Date of Upload </th>
		<%if(TTKCommon.isAuthorized(request, "deleteMdfFiles")){ %>
      		<th class="gridHeader"> Delete </th>
      	<%} %>		
      </tr>	
      
<%--       <logic:notEmpty name="alUploadedMdfFiles" scope="request">
      	<logic:iterate id="memberVO" name="alUploadedMdfFiles">
      	<tr> <td class="textLabel"> <%=(++i) %> </td> 
      		<logic:iterate id="str" name="strArr" >
	      		<td class="textLabel"> <%=str %> - <%=str %> </td>
	      	</logic:iterate>
	      	<td class="textLabel"> <%memberVO.getName() %> </td>
	      	<td class="textLabel"> <a href="#" onClick="onDeleteMdfFile()"><img src="/ttk/images/DeleteIcon.gif" title="Clear Group" alt="Delete File" width="16" height="16" border="0" align="absmiddle"></a></td>
	      </tr>	
      	</logic:iterate>
      </logic:notEmpty> --%>
<logic:notEmpty name="alUploadedMdfFiles" scope="session">      
      <%
      MemberVO vos	=	null;
      for(int i=0; i<alUploadedMdfFiles.size(); i++){
    	  vos	=	alUploadedMdfFiles.get(i);
      %>
      	<tr> 
      		<td class="textLabel"> <%=(i + 1) %> </td>
      		<td> <%= vos.getEnrollmentID() %> </td>
      		<td> <%= vos.getMemName() %> </td>
      		<td> <a href="#" onClick="viewMdfFile('<%=vos.getStrFamilyName() %>')"> <%= vos.getName() %>  </a> </td><!--  File Name -->
      		<td> <%= vos.getUpdatedDate() %> </td>
      		<%if(TTKCommon.isAuthorized(request, "deleteMdfFiles")){ %>
	      		<td class="textLabel"> <a href="#" onClick="onDeleteMdfFile('<%=vos.getMemberSeqID()%>','<%=vos.getMemberTypeID() %>')"><img src="/ttk/images/DeleteIcon.gif" title="Delete File" alt="Delete File" width="16" height="16" border="0" align="absmiddle"></a></td>
	      	<%} %>
      	</tr>	
      <%} %>
</logic:notEmpty>      
    </table>
</fieldset>
	
<!-- E N D SELECT TABLE MEMBER DETAILS -->
</div>

<!-- E N D : Form Fields -->
<INPUT TYPE="hidden" NAME="mode" value="">
<html:hidden property="memSeqId"/>

</html:form>
	<!-- E N D : Content/Form Area -->