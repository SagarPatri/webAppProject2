<%
/**
 * @ (#) hospsearch.jsp 12th Sep 2005
 * Project      : TTK HealthCare Services
 * File         : hospsearch.jsp
 * Author       : Srikanth H M
 * Company      : Span Systems Corporation
 * Date Created : 12th Sep 2005
 *
 * @author       :
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/onlineforms/EmployeeLogin/hospsearch.js"></SCRIPT>

<SCRIPT LANGUAGE="JavaScript">
bAction = false;
var TC_Disabled = true;
</SCRIPT>

<style type="text/css">
.searchList_class{
    width: 80%;
    padding-left: 10%;
}
.searchData_class{
	width: 70%;
    padding-left: 15%;
}

.info_text_class {
	color: #0c48a2;
	font-weight: bold;
}

.main_info_text_class {
	padding-left: 205px;
	margin-top: 115px;
}

.info_text_close_class {
	MARGIN-LEFT: 500px;
}

textarea {
	overflow-y: scroll;
}

.button_class {
	float: right;
	margin-right: 305px;
	padding-top: 15px;
}

.diagnosis_fieldset_class {
	margin-left: 25px;
	width: 90%;
	border: 1px solid #CCCCCC;
	padding-top: 0px;
	padding-bottom: 10px;
}

.textAreaBox {
	height: 63px;
	width: 97%;
}

input {
	border: 0;
}

.errorMessage {
    color: #a83108;
    width: 46%;
    height: 20px;
    font-style: bold;
    margin-top: 0px;
    margin-right: 0px;
    margin-bottom: 0px;
    padding: 0px;
    font-family: Verdana, Arial, Helvetica, sans-serif;
    font-size: 15px;
    color: #ff0000;
    padding-left: 10%;
}

/* .formContainer {
    color: #000000;
    margin-top: 0px;
    padding: 3px;
    width: 98%;
    text-align: left;
    background-color: #fff;
} */

.customer_div_class{
    width: 36%;
    padding-left: 40%;
}

.table_div_class {
    padding-left: 0%;
    width: 61%;
    float: left;
}


.formContainer {
    color: #000000;
    margin-top: 0px;
    padding: 3px;
    width: 60%;
    background-color: #fff;
    padding-left: 0%;
    margin-left: 0px;
}

.searchContainer {
    color: #000000;
    border: 1px solid #000000;
    background-color: #EBEDF2;
    margin-top: 10px;
    padding: 14px;
    width: 98%;
    padding-left: 5%;
}

.contact_table_class {
    width: 60%;
    padding-left: 0%;
    font-size: 15px;
    font-weight: bold;
    color: #000000;
    font-variant: normal;
}



.query_div_class {
    width: 14%;
    padding-left: 13%;
}

.button_design_class {
    float: left;
    margin-left: 45px;
    height: 35px;
}


/*media screen*/
.buttonsContainer {
    background-color: #FFFFFF;
    margin-top: 20px;
    padding: 5px 5px 5px 5px;
    width: 88%;
}
.formLabel {
    color: #000000;
    border-bottom: 0px dotted #CCCCCC;
    padding: 12px;
}


</style>

<%
pageContext.setAttribute("queryCategory",Cache.getCacheObject("queryCategory"));//added for intX

%>
<!-- S T A R T : Content/Form Area -->
<div class="contentArea" id="contentArea">


<br/><html:errors/><br/>
<div>
<h4 class="sub_heading" style="width:13%;">Query Submission</h4>

</div>
<html:form action="/EmployeeHomeAction.do" >
	<%-- <div class="contact_table_class">
	<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
  		<tr><td nowrap class="textData">Query Category<br>
   				<html:select property ="queryCategory" name="employeeContactForm" styleId="queryCategory"  styleClass="selectBox selectBoxMedium" onchange="javascript:getQueryCategory();">
  					<html:option value="">Select from list</html:option>
          			<html:options collection="queryCategory" property="cacheId" labelProperty="cacheDesc"/>
   				</html:select>
       		</td></tr>

	</table>
	</div> --%>
	<div style="width: 100%;padding-left: 0%;">
	 <div id="error_message_id">
	 <input type="text" value="" class="errorMessage" id="networkTypeErrorMessage" readonly="readonly" /><br/>
<input type="text" value="" class="errorMessage" id="errorMessage" readonly="readonly" />
</div> 
<br/>
	<div class="table_div_class">
	<table class="formContainer" border="0" cellspacing="0" cellpadding="0">
     
		<tr>
		<td align="right" nowrap class="formLabel"> Alkoot ID :</td>
		 <td align="left" class="textLabel">
		 	<html:text property="enrollmentID" name="employeeContactForm" styleId="providerCode" styleClass="textBox textBoxMedium" readonly="true" style="background-color: rgb(238, 238, 238);" value="<%= (String)request.getSession().getAttribute("AuthLicenseNo") %>"/>
		</td>
		</tr>
		<tr>
        <td align="right" nowrap class="formLabel">Contact Number <span class="mandatorySymbol">*</span>:</td>
         <td align="left" class="textLabel">
		 	<html:text property="contactNumber" name="employeeContactForm" styleId="payerCode"  styleClass="textBox textBoxMedium" readonly="true" style="background-color: rgb(238, 238, 238);"/>
			
		</td>
		</tr>
		<tr>
		<td align="right" nowrap class="formLabel">Email ID : </td>
		 <td align="left" class="textLabel">
		 	<html:text property="emailID2" name="employeeContactForm" styleId="networkType" styleClass="textBox textBoxMedium" readonly="true" style="background-color: rgb(238, 238, 238);"/>
			
		</td>
		</tr>
		
		<tr>
		<td align="right" nowrap class="formLabel">Query Category <span class="mandatorySymbol">*</span>: </td>
		 <td align="left" class="textLabel">
		 
   				<html:select property ="queryType" name="employeeContactForm" styleId="queryType"  styleClass="selectBox selectBoxMedium">
  					<html:option value="">Select from list</html:option>
          			<html:options collection="queryCategory" property="cacheId" labelProperty="cacheDesc"/>
   				</html:select>
		 	<%-- <html:text property="queryType" name="employeeContactForm" styleId="queryType" styleClass="textBox textBoxMedium" readonly="true" disabled="disabled"/> --%>
		</td>
		</tr>
		
		<tr>
		<td align="right" nowrap class="formLabel">Query <span class="mandatorySymbol">*</span>: </td>
		 <td align="left" class="textLabel">
			<html:textarea name="employeeContactForm"  property="customerQuery"
			styleClass="textAreaBox" cols="50" rows="4" styleId="finalRemarksId" />
		</td>
		</tr>
		
		
      
      </table>
      </div>
	</div>
	
	<div id="id01" class="w3-modal">
 <div class="w3-modal-content w3-card-4 w3-animate-zoom">
  <!-- <div class="header_popup_class">
  <header class="w3-container"> 
   <div>
    <span onclick="document.getElementById('id01').style.display='none'" 
   class="w3-button w3-blue w3-xlarge w3-display-topright" onmouseover="this.className='w3-button w3-blue w3-xlarge w3-display-topright close_btn_class'" onmouseout="this.className='w3-button w3-blue w3-xlarge w3-display-topright'">
   <span class="close_button_class">&times;</span>
   <span class="close_button_class" style="font-size: 16px;">X</span>
   </span> 
   
   </div>
   <h2></h2>
  </header></div> -->
<div class="body_popup_class">
<p id="responseMessage"></p>
<!-- Your Query Will be attended <br/>by our representative shortly -->
<span id="show_popup"></span>
  </div>

  <div class="w3-container w3-padding footer_popup_class">
   <span class="w3-button poup_ok_class" 
   onclick="document.getElementById('id01').style.display='none'">OK</span>
  </div>
 </div>
</div>

	<br/>
	<!-- <div class="query_div_class"> -->
	<!-- <table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td align="center">
<a href="#" onclick="javascript:onSubmit();" class="button_design_class">Submit</a>	    </td>
	     <td align="center">
<a href="#" onclick="javascript:onQueryClose();" class="button_design_class">Back</a>
	    </td>
	  </tr>
	</table> -->
	<br/><br/>
	<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="100%" align="center">
	    <button type="button" name="Button" accesskey="s" class="button_design_class" onMouseout="this.className='button_design_class'" onMouseover="this.className='button_design_class buttonsHover'" onClick="javascript:onSubmit();"><u>S</u>ubmit</button>&nbsp;
	     <button type="button" name="Button" accesskey="c" class="button_design_class" onMouseout="this.className='button_design_class'" onMouseover="this.className='button_design_class buttonsHover'" onClick="javascript:onQueryClose();"><u>C</u>lose</button>&nbsp;
	    </td>
	  </tr>
	</table>
	
	<!-- </div> -->
	
	<INPUT TYPE="hidden" NAME="rownum" VALUE="">
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
</html:form>
</div>
<!-- E N D : Main Container Table -->