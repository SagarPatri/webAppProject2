<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,com.ttk.common.PreAuthWebBoardHelper,com.ttk.common.ClaimsWebBoardHelper,java.util.Date,java.text.SimpleDateFormat"%>

	<script type="text/javascript" SRC="/ttk/scripts/validation.js"></script>

    <script type="text/javascript" src="/ttk/scripts/insurancepricing/swinputscreen3.js"></script>	
     <script type="text/javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script>  
     <script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
	<script>
    var JS_Focus_Disabled =true;
    var JS_Focus_ID="<%=TTKCommon.checkNull(request.getAttribute("focusId"))%>";
	</script>
<style>

</style>
</head>
<body>
<%
	boolean viewmode=true;
	boolean bEnabled=false;
	boolean viewmode1=true;
	String strSubmissionType="";
	String ampm[] = {"AM","PM"};
	
	boolean blnAmmendmentFlow=false;
	if(TTKCommon.isAuthorized(request,"Edit"))
	{
		viewmode=false;
		viewmode1=false;
	}//end of if(TTKCommon.isAuthorized(request,"Edit"))
	pageContext.setAttribute("ampm",ampm);	
 	pageContext.setAttribute("natcategorylist",Cache.getCacheObject("natcategorylist"));
	
	
	boolean network=false;

%>

<html:form action="/SwInsPricingActionAdd.do" method="post" enctype="multipart/form-data"> 
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		  <tr>
		    <td width="65%"></td> 
		  </tr>
	</table>
		
	<div class="contentArea" id="contentArea">
	<html:errors/>
		<logic:notEmpty name="frmSwPricingscreen3"	property="pricingNumberAlert">
				<table align="center" class="errorContainer" border="0"	cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/warning.gif" alt="Warning"
							width="16" height="16" align="absmiddle">&nbsp; <bean:write
								name="frmSwPricingscreen3" property="pricingNumberAlert" /></td>
					</tr>
				</table>
			</logic:notEmpty>
	<logic:notEmpty name="errorMsg" scope="request">
    <table align="center" class="errorContainer"  border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><img src="/ttk/images/ErrorIcon.gif" alt="Error" width="16" height="16" align="middle" >&nbsp;
          <bean:write name="errorMsg" scope="request" />
          </td>
      </tr>
    </table>
   </logic:notEmpty>
	
	<!-- S T A R T : Success Box -->

		<logic:notEmpty name="updated" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td><img src="/ttk/images/SuccessIcon.gif" alt="Success" width="16" height="16" align="middle">&nbsp;
						<bean:message name="updated" scope="request"/>
				  </td>
				</tr>
			</table>
		</logic:notEmpty>
		<logic:notEmpty name="successMsg" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td><img src="/ttk/images/SuccessIcon.gif" alt="Success" width="16" height="16" align="middle">&nbsp;
						<bean:write name="successMsg" scope="request"/>
				  </td>
				</tr>
			</table>
		</logic:notEmpty> 	
		
    <!-- S T A R T : Form Fields -->
	
	<fieldset>
			<legend>In-patient</legend>
			<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
			        
			         <tr>
			       <td     class="formLabel"  width="30%"><b>Daily Accommodation and Hospital Charges :</b><span class="mandatorySymbol">*</span></td>
			     <td  class="textLabel" >
				       <html:select name="frmSwPricingscreen3" property="inpAccomodation" styleId="inpAccomodationid" onchange="" styleClass="selectBox selectBoxMedium"  >
							  <html:option value="">Select from list</html:option>
						 	  <html:option value="Y">Covered</html:option>
							  <html:option value="N">Not Covered</html:option>
					  </html:select>
			      </td>	
			   
			       <td class="formLabel" >Limit :
			      &nbsp;
				   	<html:text name="frmSwPricingscreen3" readonly="" onkeyup="isNumericOnly(this);" property="inpAccmdlimit" styleClass="textBox textBoxSmall" />
			      </td>
			  
			  	 <td class="formLabel" > Copay :
			      &nbsp;
				       <html:select name="frmSwPricingscreen3" property="inpAccmdCopay" styleId="inpAccmdCopay"  styleClass="selectBox selectBoxMedium"  >
						<html:option value="">Not applicable</html:option>
					  </html:select>
					  </td>
					  
					  <td class="formLabel" > Deductable :
			      &nbsp;
				       <html:select name="frmSwPricingscreen3" property="inpAccmdDeductable" styleId="inpAccmdDeductable"  styleClass="selectBox selectBoxMedium"  >
						<html:option value="">Not applicable</html:option>
					  </html:select>
					  </td> 
			    </tr>
			    
			 
			    </table></fieldset>
			   
			    <table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
			    
			    <tr>
			  <td colspan="4" align="center">
		
            <%
             if(TTKCommon.isAuthorized(request,"Edit")) {
             %>
             <button type="button" name="Button2" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onPartialSave('save')">Pa<u>r</u>tial save</button>&nbsp;
            <button type="button" name="Button2" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onSave('save')"><u>S</u>ave</button>&nbsp;
         	<button type="button" name="Button2" accesskey="p" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'"  onClick="onSave('saveProceed')"><u>P</u>roceed >></button>&nbsp; <!--  onClick="onIncomeprofile()" -->
         
          <%
            }
          %>
			  </td>
			  </tr>			  
			  </table>

<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
	<input type="hidden" name="child" value="">
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	<input type="hidden" name="child" value="">
	<html:hidden property="groupProfileSeqID" />
	<script type="text/javascript">
</script>
</html:form>
</div>
</body>
</html>


