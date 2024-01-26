<%
/** @ (#) TOBUpload.jsp 15TH JUNE 2017
 * Project       : QATAR
 * File          : TOBUpload.jsp
 * Author        : Shruthi
 * Company       : VIDAL
 * Date Created  : 15TH Sep 2017
 *
 *
 */
%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ page import=" com.ttk.common.TTKCommon" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Activity Details</title>
<SCRIPT type="text/javascript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script language="javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script>
<script language="javascript" src="/ttk/scripts/administration/TOBUpload.js"></script>	

<script type="text/javascript">
var JS_Focus_Disabled =true;
</script>
</head>
<body>
<!-- S T A R T : Content/Form Area -->	
	<html:form action="/TOBUpload.do"   method="post" enctype="multipart/form-data"> 	
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td>TOB Configuration - <bean:write name="frmTOBUpload" property="caption" scope="session"/></td>
	  </tr>
	</table>
	<!-- E N D : Page Title --> 
	<div class="contentArea" id="contentArea">
		<html:errors/>
			<logic:notEmpty name="updated" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
						<bean:message name="updated" scope="request"/>
					</td>
				</tr>
			</table>
		</logic:notEmpty>
    
    <!-- S T A R T : Buttons and Page Counter -->

 	
 	<fieldset> <legend>Upload Document</legend>
   	<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0" style="width: 80%">
		<tr>
	    	<td height="20" class="formLabel">Select File to Upload</td>
	    	<td class="textLabelBold"> : <html:file property="file" styleId="file" onclick=""/>
	    	<!-- RahulSingh -->
	    	
	  	<logic:equal name="frmTOBUpload" property="uploaddocumentflag" value="1" scope="session">
       		 <td colspan="2">
       		<a onclick="onViewDocument();" href="#" shape="" ><img src="/ttk/images/ModifiedIcon.gif" title="Click Here to View Document" alt="Click Here to View Document" width="16" height="16" border="0"></a>
			    	 <!-- <a onclick="onDelDoc();" href="#" shape="">
			    	 	<img width="16" height="16" alt="Delete File " src="/ttk/images/DeleteIcon.gif" border="0">
			    	 </a> -->
			    	 </td>
        </logic:equal>
	    	</td>
	  	</tr>
	  	
   	</table>
   	</fieldset>
   	
   	<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td align="center" nowrap>
	
   			<button type="button" name="Button" accesskey="a" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onTOBUpload();"><u>U</u>pload</button>&nbsp;
        
            <button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose();"><u>C</u>lose</button>&nbsp;
	    </td>
	  </tr>
	
 	</table>
 	
 	
	</div>
    <!-- E N D : Buttons and Page Counter -->

  
    <INPUT TYPE="hidden" NAME="mode" VALUE="">
   	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	</html:form>
	</body>
	</html>

<!-- E N D : Content/Form Area -->