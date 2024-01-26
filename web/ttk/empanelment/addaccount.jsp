<%
/**
 * @ (#) addaccount.jsp 28th Sep 2005
 * Project      : TTK HealthCare Services
 * File         : addaccount.jsp
 * Author       : Srikanth H M
 * Company      : Span Systems Corporation
 * Date Created : 28th Sep 2005
 *
 * @author       :
 * Modified by   : kishor kumar S H
 * Modified date : Dec 27 2014
 * Reason        :
 */
%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>

<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.ArrayList" %>
<%
	boolean viewmode=true;
	boolean viewmodePO=true;
	
	if(TTKCommon.isAuthorized(request,"Edit"))
	{
		viewmode=false;
	}//end of if(TTKCommon.isAuthorized(request,"Edit"))	
	pageContext.setAttribute("destnationbank",Cache.getCacheObject("destnationbank"));
	pageContext.setAttribute("stateCode", Cache.getCacheObject("stateCode"));
	pageContext.setAttribute("cityCode", Cache.getCacheObject("cityCode"));
	pageContext.setAttribute("countryCode", Cache.getCacheObject("countryCode"));
	pageContext.setAttribute("chequeCode",Cache.getCacheObject("chequeCode"));
	pageContext.setAttribute("payOrderType",Cache.getCacheObject("payOrderType"));
	pageContext.setAttribute("viewmode",new Boolean(viewmode));	
	pageContext.setAttribute("viewmodePO",new Boolean(viewmodePO));	
	pageContext.setAttribute("accounttype",Cache.getCacheObject("accounttype"));
	pageContext.setAttribute("OutsideQatarCountryList", Cache.getCacheObject("OutsideQatarCountryList"));
%>
<logic:match name="frmAccounts" property="hidpoEmpFeeCharged" value="N">
	<% viewmodePO=false; pageContext.setAttribute("viewmodePO",new Boolean(viewmodePO)); %>
</logic:match>


<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/empanelment/accounts.js"></script>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script language="javascript">
var JS_Focus_ID="<%=TTKCommon.checkNull(request.getParameter("focusID"))%>";
</script>
<SCRIPT LANGUAGE="JavaScript">
	var JS_SecondSubmit=false;
</SCRIPT>
<head>
	<link rel="stylesheet" type="text/css" href="css/style.css" />
	<link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css" />
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.min.js"></script>

	<script src="jquery-1.11.1.min.js"></script>
	
    <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"></script>
    <script src="http://code.jquery.com/jquery-1.8.2.js"></script>
	<script src="js/jquery.autocomplete.js"></script>
      
<SCRIPT>
$(document).ready(function() {
    $("#bankName").autocomplete("auto.jsp?mode=bankNameStad");
}); 
function getClinicainId(obj)
{
	document.getElementById("validHosp").innerHTML	=	'';
  var accountName	=	document.getElementById("actInNameOf").value;
  var HospitalSeqId	=	document.forms[1].HospitalSeqId.value;
  $(document).ready(function() {
  //$("#actInNameOf").blur(function(){
    	var ID	=	obj.value;
        $.ajax({
        		url: "/AsynchronousAction.do?mode=getCommonMethod&id="+accountName+"&getType=accountName&HospitalSeqId="+HospitalSeqId, 
        		success: function(result){
      				//var res	=	result.split("@");
      				if(result==="valid" || result.length==5){
      					document.getElementById("validHosp").innerHTML	=	'Name as per Trade License and Account Name is matching';
      					document.getElementById("validHosp").style.color = 'green';
      				}
      				else{
      					document.getElementById("validHosp").innerHTML	=	'Name as per Trade License and Account Name is not matching';
      					document.getElementById("validHosp").style.color = 'red';
      				}
					
        		}}); 
   		 //});
  });
}

</SCRIPT>

</head>
<%
//intX
ArrayList alaccounts=	(ArrayList)request.getSession().getAttribute("alaccounts");
String emplNum		=	(String)request.getSession().getAttribute("emplNum")==null?"":(String)request.getSession().getAttribute("emplNum");
boolean readMode	=	false;
if(!emplNum.equals(""))
	readMode	=	true;
Long HospitalSeqId= new Long(TTKCommon.getWebBoardId(request));//get the web board id
%>
<!-- S T A R T : Content/Form Area -->
<html:form action="/AccountsAction.do" >
<!-- S T A R T : Page Title -->
        <table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td width="51%">Account Details - <bean:write name="frmAccounts" property="caption"/></td>
	        <td align="right" class="webBoard">
				<a href="#" onClick="javascript:onAccountHistory()"><img src="ttk/images/HistoryIcon.gif" title="View History" alt="View History" width="16" height="16" border="0" align="absmiddle" class="icons"></a>&nbsp;&nbsp;<img src="ttk/images/IconSeparator.gif" width="1" height="15" align="absmiddle" class="icons">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %>
		    </td>
	      </tr>
	    </table>

	<!-- E N D : Page Title -->
	<div class="contentArea" id="contentArea">
	<html:errors/>
	<!-- S T A R T : Success Box -->
		<logic:notEmpty name="updated" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
						<bean:message name="updated" scope="request"/>
				  </td>
				</tr>
			</table>
		</logic:notEmpty>
	<!-- E N D : Success Box -->
	
	
	<!-- projectX Starts-->
	<fieldset>
    <legend>Account Details</legend>
	
	<logic:notEmpty name="alaccounts"> 
	
    	<table class="formContainer"  border="0" cellspacing="0" cellpadding="0">
     	 <tr class="borderBottom">
     	 
        	<td width="20%" class="textLabelBold" nowrap align="left">Bank Name </td>
        	<td width="20%" class="textLabelBold" nowrap align="left">Account Number</td>
        	<td width="20%" class="textLabelBold" nowrap align="left">IBAN Number   </td>
        	<td width="20%" class="textLabelBold" nowrap align="left">Start Date </td>
        	<td width="20%" class="textLabelBold" nowrap align="left">End Date </td>
        	
        </tr>
        
        
        <!-- Iterate Account Details for particular Hospital -->
        <logic:iterate id="accountDetailVO" name="alaccounts" indexId="i">
		 <tr class="<%=(i.intValue()%2)==0? "gridOddRow":"gridEvenRow"%>">	
				<td><bean:write name="accountDetailVO" property="bankName"/></td>	
				<td><bean:write name="accountDetailVO" property="accountNumber"/></td>	
				<td><bean:write name="accountDetailVO" property="bankIfsc"/></td>	
				<td><bean:write name="accountDetailVO" property="startDatestr"/></td>	
				<td><bean:write name="accountDetailVO" property="endDatestr"/></td>	
			</tr>
		</logic:iterate>
		
		
		<!-- below code removed as it is showing one line records for Hospital -->
        <!-- tr>	
		      <td> <bean:write name="frmAccounts" property="bankName"/> </td>
		      <td> <bean:write name="frmAccounts" property="accountNumber"/> </td>
		      <td> <bean:write name="frmAccounts" property="bankIfsc"/> </td>
		      <td> <bean:write name="frmAccounts" property="startDate"/> </td>
		      <td> <bean:write name="frmAccounts" property="endDate"/> </td>
		    
      	</tr-->
      	</table>
	
	</logic:notEmpty>
	
	
		
		<logic:empty name="alaccounts">
		<table class="formContainer"  border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td colspan="5" align="left"><font color="red"> <strong> Account details not available</strong> </font>	</td>
			</tr>
		</table>
		</logic:empty>
	</fieldset>
	<br/>
	<!-- projectX Ends-->
	
	
	
	
	<fieldset>
	
	
	 <logic:empty name="frmAccounts" property="partnerOrProvider">
	<legend>Provider Bank Account Details</legend>

	</logic:empty>
	
	<logic:notEmpty name="frmAccounts" property="partnerOrProvider">
	
	<legend>Partner Bank Account Details</legend>
	</logic:notEmpty>
	
	
	
	<!-- S T A R T : Form Fields -->
	<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
	
	<%--  <logic:notEmpty name="frmAccounts" property="partnerOrProvider">
	 <tr>
     <td width="21%" class="formLabel">Bank Location: <span class="mandatorySymbol">*</span></td>
                 <td>
                    <html:select property="bankAccountQatarYN" styleClass="selectBox selectBoxMedium"  onchange="onChangeQatarYN()"  >
                    <html:option value="Y">Within Qatar</html:option>
                    <html:option value="N">Outside Qatar</html:option>
                    </html:select>
                </td>
         </tr>
         </logic:notEmpty> --%>
        <logic:empty name="frmAccounts" property="partnerOrProvider">  
          <tr>
              <td width="21%" class="formLabel">Bank Location: <span class="mandatorySymbol">*</span></td>
                 <td>
                 
              <logic:notEmpty name="frmAccounts" property="emplNum"> 
		  <html:select property="bankAccountQatarYN" styleClass="selectBox selectBoxMedium"  disabled="true" readonly="true"  onchange="onChangeQatarYN()" >
                    <html:option value="Y">Within Qatar</html:option>
                    <html:option value="N">Outside Qatar</html:option>
                    </html:select>
	  	</logic:notEmpty>
	   	 <logic:empty name="frmAccounts" property="emplNum">
          <html:select property="bankAccountQatarYN" styleClass="selectBox selectBoxMedium" onchange="onChangeQatarYN()" >
                    <html:option value="Y">Within Qatar</html:option>
                    <html:option value="N">Outside Qatar</html:option>
                    </html:select>        
          </logic:empty>     
            </td>
         </tr>
       </logic:empty>  
         
	  <tr>
	   <logic:empty name="frmAccounts" property="partnerOrProvider">
	  <logic:notEmpty name="frmAccounts" property="emplNum">
	    <td width="20%" class="formLabel">Bank Name: <span class="mandatorySymbol">*</span></td>
          <td width="33%">
          
            <logic:equal name="frmAccounts" property="bankAccountQatarYN" value="N">
	         <html:text property="bankName" styleId="state1" styleClass="textBox textBoxMedium" disabled="true" readonly="true"  onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/>
	         </logic:equal> 
	          <logic:notEqual name="frmAccounts" property="bankAccountQatarYN" value="N">
             <html:select property="bankName" styleId="state1"  styleClass="selectBox selectBoxLarqge"  disabled="true" readonly="true" onchange="onChangeBank('state1')" >
             <html:option value="">Select from list</html:option>
             <html:optionsCollection name="destnationbank" label="cacheDesc" value="cacheId" />
             </html:select>
             </logic:notEqual>
         </td>                             
	   </logic:notEmpty>
	   <logic:empty name="frmAccounts" property="emplNum">
	    <td width="20%" class="formLabel">Bank Name: <span class="mandatorySymbol">*</span></td>
	    <td width="33%">   
	    
	               <logic:equal name="frmAccounts" property="bankAccountQatarYN" value="N">
	               <html:text property="bankName" styleId="state1" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/>
	               </logic:equal> 
	               <logic:notEqual name="frmAccounts" property="bankAccountQatarYN" value="N">
	               	<html:select property="bankName" styleId="state1"  styleClass="selectBox selectBoxLarqge"  onchange="onChangeBank('state1')" >
             		<html:option value="">Select from list</html:option>
             		<html:optionsCollection name="destnationbank" label="cacheDesc" value="cacheId" />
                   </html:select>
	               </logic:notEqual>
		</td>
	   </logic:empty>
	   </logic:empty>
	   <logic:notEmpty name="frmAccounts" property="partnerOrProvider">
	    <logic:notEmpty name="frmAccounts" property="bankName">
	    <td width="20%" class="formLabel">Bank Name: <span class="mandatorySymbol">*</span></td>
	    <td width="33%"><html:text property="bankName" styleId="bankName" styleClass="textBox textBoxLarge" maxlength="250" onkeyup="ConvertToUpperCase(event.srcElement);" disabled="<%=viewmode%>" readonly="<%=readMode %>"/>
	    </td>
	   </logic:notEmpty>
	   <logic:empty name="frmAccounts" property="bankName">
	    <td width="20%" class="formLabel">Bank Name: <span class="mandatorySymbol">*</span></td>
	    <td width="33%"><html:text property="bankName" styleId="bankName" styleClass="textBox textBoxLarge" maxlength="250" onkeyup="ConvertToUpperCase(event.srcElement);" style="background-color: #EEEEEE;" disabled="<%=viewmode%>" readonly="<%=readMode %>" />
	    </td>
	   </logic:empty>
	  </logic:notEmpty>
	  
	   <logic:notEmpty name="frmAccounts" property="accountNumber">
	    <td class="formLabel">Account No.:</td>
	    <td><html:text property="accountNumber" styleClass="textBox textBoxLarge" maxlength="60" disabled="true" readonly="true"/>
	 	</td>
	 </logic:notEmpty>
	 <logic:empty name="frmAccounts" property="accountNumber">
	    <td class="formLabel">Account No.:</td>
	    <td><html:text property="accountNumber" styleClass="textBox textBoxLarge" maxlength="60" style="background-color: #EEEEEE;"  disabled="true" readonly="true"/>
	 	</td>
	 </logic:empty>
	 </tr>
	 <logic:notEmpty name="frmAccounts" property="actInNameOf">
	 <tr>
		 <td colspan="3"> <div id="validHosp"> </div> </td> </tr>
	    <td class="formLabel">Account Name:</td>
	    <td><html:text property="actInNameOf" styleId="actInNameOf" styleClass="textBox textBoxLarge" maxlength="250" disabled="<%=viewmode%>" readonly="<%=readMode %>"/>
		</td>
	</logic:notEmpty>
	<logic:empty name="frmAccounts" property="actInNameOf">
		 <tr>
		 <td colspan="3"> <div id="validHosp"> </div> </td> </tr>
	    <td class="formLabel">Account Name:</td>
	    <td><html:text property="actInNameOf" styleId="actInNameOf" styleClass="textBox textBoxLarge" maxlength="250" style="background-color: #EEEEEE;"  disabled="<%=viewmode%>" readonly="<%=readMode %>" onblur="getClinicainId(this)"/>
		</td>
	</logic:empty>
	  </tr>
	  
	  <!-- adding IBAN and SWIFT code for intX -->
	  
	 <tr>
	  <logic:notEmpty name="frmAccounts" property="ibanNumber">
	    <td class="formLabel">Swift Code:</td>
	    <td><html:text property="ibanNumber" styleClass="textBox textBoxMedium" maxlength="60" disabled="<%=viewmode%>" readonly="<%=readMode %>" onkeyup="ConvertToUpperCase(event.srcElement);" onblur="swiftCodeValidation();"/>
	 	</td>
	 </logic:notEmpty>
	 <logic:empty name="frmAccounts" property="ibanNumber">
	    <td class="formLabel">Swift Code:</td>
	    <td><html:text property="ibanNumber" styleClass="textBox textBoxMedium" maxlength="60" style="background-color: #EEEEEE;"  disabled="<%=viewmode%>" readonly="<%=readMode %>" value="" onkeyup="ConvertToUpperCase(event.srcElement);" onblur="swiftCodeValidation();"/>
	 	</td>
	 </logic:empty>
	 <logic:notEmpty name="frmAccounts" property="swiftCode">
	    <td class="formLabel">IBAN No.: <span class="mandatorySymbol">*</span></td>
	    <td><html:text property="swiftCode" styleClass="textBox textBoxLarge" maxlength="250" disabled="<%=viewmode%>" readonly="<%=readMode %>" onkeyup="ConvertToUpperCase(event.srcElement);"/>
		</td>
	</logic:notEmpty>
	<logic:empty name="frmAccounts" property="swiftCode">
	    <td class="formLabel">IBAN No.: <span class="mandatorySymbol">*</span></td>
	    <td><html:text property="swiftCode" styleClass="textBox textBoxLarge" maxlength="250" style="background-color: #EEEEEE;"  disabled="<%=viewmode%>" readonly="<%=readMode %>" value="" onkeyup="ConvertToUpperCase(event.srcElement);"/>
		</td>
	</logic:empty>
	  </tr>
	  <!-- E N D -->
	  
	  <tr>
                <td class="formLabel"> Start Date<span class="mandatorySymbol">*</span> : </td>
                 <td>
        				<html:text property="startDate" styleClass="textBox textDate" maxlength="10" disabled="<%=viewmode%>" readonly="<%=readMode%>"/>
        				<logic:empty name="frmAccounts" property="emplNum">
        					<a name="CalendarObjectStartDate" id="CalendarObjectStartDate" href="#" onClick="javascript:show_calendar('CalendarObjectStartDate','frmAccounts.startDate',document.frmAccounts.startDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
        						<img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle">
        					</a>
        				</logic:empty>
        			</td>
        			<td class="formLabel"> End Date: </td>
                 <td>
        				<html:text property="endDate" styleClass="textBox textDate" maxlength="10" disabled="<%=viewmode%>" readonly="<%=readMode%>"/>
        				
        				
        					<logic:empty name="frmAccounts" property="emplNum">
        					<a name="CalendarObjectEndDate" id="CalendarObjectEndDate" href="#" onClick="javascript:show_calendar('CalendarObjectEndDate','frmAccounts.endDate',document.frmAccounts.endDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
        						<img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle">
        					</a>
        					</logic:empty>
        				
        			</td>
      </tr>
                 
                 
	  <tr>
	      <td colspan="4" height="5"></td>
	      </tr>
	      <tr>
	        <td colspan="4" class="formLabelBold">Bank Branch Details </td>
	      </tr>
		  <tr>
	        <td colspan="4" height="2"></td>
	      </tr>
	      
	  <tr>
		    <td class="formLabel">City:  <span class="mandatorySymbol">*</span> </td>
	    <td>
	       <logic:notEmpty name="frmAccounts" property="partnerOrProvider">
			  <logic:equal name="frmAccounts" property="bankAccountQatarYN" value="N">
			<html:text property="bankAddressVO.stateCode" styleId="state2" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/>
			</logic:equal>
			 <logic:notEqual name="frmAccounts" property="bankAccountQatarYN" value="N">
               <html:select property="bankAddressVO.stateCode" styleId="state2" styleClass="selectBox selectBoxMedium" onchange="onChangeState('state2')" >
                    <html:option value="">Select from list</html:option>
                    <logic:notEmpty name="frmAccounts" property="partnerOrProvider">	
                    <html:optionsCollection name="stateCode" label="cacheDesc" value="cacheId" />
                    </logic:notEmpty>
                    <logic:empty name="frmAccounts" property="partnerOrProvider">	
                    <html:optionsCollection name="alCityList" label="cacheDesc" value="cacheId" />
                    </logic:empty>
    	   	</html:select>
    	   	</logic:notEqual>
    	   	</logic:notEmpty>
    	   	 <logic:empty name="frmAccounts" property="partnerOrProvider">
    	   	<logic:empty name="frmAccounts" property="emplNum">
    	   	 <logic:equal name="frmAccounts" property="bankAccountQatarYN" value="N">
			<html:text property="bankAddressVO.stateCode" styleId="state2" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/>
			</logic:equal>
			 <logic:notEqual name="frmAccounts" property="bankAccountQatarYN" value="N">
               <html:select property="bankAddressVO.stateCode" styleId="state2" styleClass="selectBox selectBoxMedium" onchange="onChangeState('state2')" >
                    <html:option value="">Select from list</html:option> 
                    <logic:notEmpty name="frmAccounts" property="partnerOrProvider">	
                    <html:optionsCollection name="stateCode" label="cacheDesc" value="cacheId" />
                    </logic:notEmpty>
                    <logic:empty name="frmAccounts" property="partnerOrProvider">	
                    <html:optionsCollection name="alCityList" label="cacheDesc" value="cacheId" />
                    </logic:empty>
    	   	</html:select>
    	   	</logic:notEqual>
    	   	</logic:empty>
    	   	 <logic:notEmpty name="frmAccounts" property="emplNum">
    	   	  <logic:equal name="frmAccounts" property="bankAccountQatarYN" value="N">
			<html:text property="bankAddressVO.stateCode" styleId="state2" styleClass="textBox textBoxMedium" readonly="true" disabled="true"  onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/>
			</logic:equal>
			 <logic:notEqual name="frmAccounts" property="bankAccountQatarYN" value="N">
               <html:select property="bankAddressVO.stateCode" styleId="state2" styleClass="selectBox selectBoxMedium"  readonly="true" disabled="true"  onchange="onChangeState('state2')" >
                    <html:option value="">Select from list</html:option>
                    <logic:notEmpty name="frmAccounts" property="partnerOrProvider">	
                    <html:optionsCollection name="stateCode" label="cacheDesc" value="cacheId" />
                    </logic:notEmpty>
                    <logic:empty name="frmAccounts" property="partnerOrProvider">	
                    <html:optionsCollection name="alCityList" label="cacheDesc" value="cacheId" />
                    </logic:empty>
    	   	</html:select>
    	   	</logic:notEqual>
    	   	 </logic:notEmpty>
    	   	
    	   	
    	   </logic:empty>	
	   </td>
	     <logic:equal name="frmAccounts" property="bankAccountQatarYN" value="N">
	   	   <td class="formLabel">Area:</td>
	   	   </logic:equal>
	   	   <logic:notEqual name="frmAccounts" property="bankAccountQatarYN" value="N">
	   	   <td class="formLabel">Area: <span class="mandatorySymbol">*</span>  </td>
	   	   </logic:notEqual>
        <td>
        
        
         <logic:notEmpty name="frmAccounts" property="partnerOrProvider">
         	<logic:equal name="frmAccounts" property="bankAccountQatarYN" value="N">
         	<html:text property="bankAddressVO.cityCode" styleId="state3" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/>
         	</logic:equal>
         	<logic:notEqual name="frmAccounts" property="bankAccountQatarYN" value="N">
              <html:select property="bankAddressVO.cityCode" styleId="state3" styleClass="selectBox selectBoxMedium" onchange="onChangeCity('state3')" >
                  <html:option value="">Select from list</html:option> 
                    <logic:notEmpty name="frmAccounts" property="partnerOrProvider">	
                    <html:optionsCollection name="alCityList" label="cacheDesc" value="cacheId" />
                    </logic:notEmpty>
                    <logic:empty name="frmAccounts" property="partnerOrProvider">	
                     <html:optionsCollection name="alDistList" label="cacheDesc" value="cacheId" />
                    </logic:empty>
          	</html:select>
          	</logic:notEqual>
          	</logic:notEmpty>
          	<logic:empty name="frmAccounts" property="partnerOrProvider">
          	<logic:empty name="frmAccounts" property="emplNum">
          	<logic:equal name="frmAccounts" property="bankAccountQatarYN" value="N">
         	<html:text property="bankAddressVO.cityCode" styleId="state3" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/>
         	</logic:equal>
         	<logic:notEqual name="frmAccounts" property="bankAccountQatarYN" value="N">
              <html:select property="bankAddressVO.cityCode" styleId="state3" styleClass="selectBox selectBoxMedium" onchange="onChangeCity('state3')" >
                <html:option value="">Select from list</html:option> 
                    <logic:notEmpty name="frmAccounts" property="partnerOrProvider">	
                    <html:optionsCollection name="alCityList" label="cacheDesc" value="cacheId" />
                    </logic:notEmpty>
                    <logic:empty name="frmAccounts" property="partnerOrProvider">	
                     <html:optionsCollection name="alDistList" label="cacheDesc" value="cacheId" /> 
                  <%--  <html:optionsCollection name="alCityList" label="cacheDesc" value="cacheId" /> --%>
                    </logic:empty>
          	</html:select>
          	</logic:notEqual>
          	</logic:empty>
          	 <logic:notEmpty name="frmAccounts" property="emplNum">
          	<logic:equal name="frmAccounts" property="bankAccountQatarYN" value="N">
         	<html:text property="bankAddressVO.cityCode" styleId="state3" styleClass="textBox textBoxMedium" readonly="true" disabled="true" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/>
         	</logic:equal>
         	<logic:notEqual name="frmAccounts" property="bankAccountQatarYN" value="N">
              <html:select property="bankAddressVO.cityCode" styleId="state3" styleClass="selectBox selectBoxMedium" readonly="true" disabled="true" onchange="onChangeCity('state3')" >
                 <html:option value="">Select from list</html:option> 
                    <logic:notEmpty name="frmAccounts" property="partnerOrProvider">	
                    <html:optionsCollection name="alCityList" label="cacheDesc" value="cacheId" />
                    </logic:notEmpty>
                    <logic:empty name="frmAccounts" property="partnerOrProvider">	
                   <html:optionsCollection name="alDistList" label="cacheDesc" value="cacheId" /> 
                   <%--  <html:optionsCollection name="alCityList" label="cacheDesc" value="cacheId" /> --%>
                    </logic:empty>
          	</html:select>
          	</logic:notEqual>
          	
          	
          	</logic:notEmpty>
          </logic:empty>	
        </td>
       </tr> 
	  <tr>
	   <logic:empty name="frmAccounts" property="partnerOrProvider">
	   <logic:notEmpty name="frmAccounts" property="emplNum">
	    <td class="formLabel">Branch Name: <span class="mandatorySymbol">*</span> </td>
	    <td>
	  <logic:equal name="frmAccounts" property="bankAccountQatarYN" value="N">  
	   <html:text property="branchNameText" styleId="state4" styleClass="textBox textBoxMedium" readonly="true" disabled="true"  onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/>
	   </logic:equal>
	 <logic:notEqual name="frmAccounts" property="bankAccountQatarYN" value="N">  
 					<html:select property="branchName" styleId="state4" styleClass="selectBox selectBoxMedium" readonly="true" disabled="true" ><!--  onchange="onChangeBranch('state4')"  -->
                     <%-- <html:option value="">Select from list</html:option>  --%>
                     <html:optionsCollection name="alBranchList" label="cacheDesc" value="cacheId" />
                   <%--  <html:optionsCollection name="alCityList" label="cacheDesc" value="cacheId" /> --%>
                    </html:select>
                    </logic:notEqual>
                     </td>
	    </logic:notEmpty>
	    <logic:empty name="frmAccounts" property="emplNum">
	    <td class="formLabel">Branch Name: <span class="mandatorySymbol">*</span> </td>
	    <td>
	      <logic:equal name="frmAccounts" property="bankAccountQatarYN" value="N">  
	   <html:text property="branchNameText" styleId="state4" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/>
	   </logic:equal>
	    <logic:notEqual name="frmAccounts" property="bankAccountQatarYN" value="N">  
	   <html:select property="branchName" styleId="state4" styleClass="selectBox selectBoxMedium"><!--  onchange="onChangeBranch('state4')"  -->
                   <%--   <html:option value="">Select from list</html:option>  --%>
                     <html:optionsCollection name="alBranchList" label="cacheDesc" value="cacheId" /> 
                      <%--  <html:optionsCollection name="alCityList" label="cacheDesc" value="cacheId" /> --%>
                    </html:select>
                    </logic:notEqual>
         </td>
      </logic:empty>
    </logic:empty>
              
   <logic:notEmpty name="frmAccounts" property="partnerOrProvider">	
    <logic:notEmpty name="frmAccounts" property="branchName">
	    <td class="formLabel">Branch Name: <span class="mandatorySymbol">*</span></td>
	    <td><html:text property="branchName" styleClass="textBox textBoxLarge" onkeypress='ConvertToUpperCase(event.srcElement);' maxlength="250" disabled="<%=viewmode%>" readonly="<%=readMode %>"/></td>
	    </logic:notEmpty>
	    <logic:empty name="frmAccounts" property="branchName">
	    <td class="formLabel">Branch Name: <span class="mandatorySymbol">*</span></td>
	    <td><html:text property="branchName" styleClass="textBox textBoxLarge" onkeypress='ConvertToUpperCase(event.srcElement);' maxlength="250" style="background-color: #EEEEEE;"  disabled="<%=viewmode%>" readonly="<%=readMode %>" value=""/></td>
	    </logic:empty>
  </logic:notEmpty>
       
        <logic:empty name="frmAccounts" property="partnerOrProvider">
       <logic:notEmpty name="frmAccounts" property="emplNum">
       <td class="formLabel">Account Type: <span class="mandatorySymbol">*</span></td>
        <td>
                    <html:select property="bankAccType" styleClass="selectBox selectBoxMedium" readonly="true" disabled="true" >
                    <%-- <html:option value="">Select from list</html:option> --%>
                    <html:optionsCollection name="accounttype" label="cacheDesc" value="cacheId" />
                    </html:select>
                </td>
       </logic:notEmpty>  
       <logic:empty name="frmAccounts" property="emplNum">
        <td class="formLabel">Account Type: <span class="mandatorySymbol">*</span></td>
        <td>
                    <html:select property="bankAccType" styleClass="selectBox selectBoxMedium"  >
                    <%-- <html:option value="">Select from list</html:option> --%>
                    <html:optionsCollection name="accounttype" label="cacheDesc" value="cacheId" />
                    </html:select>
                </td>
       </logic:empty>
       </logic:empty>
              
	  </tr>
	  <logic:notEmpty name="frmAccounts" property="bankAddressVO.address1">
        <td width="20%" class="formLabel">Address 1: <!-- <span class="mandatorySymbol">*</span> --></td>
        <td width="33%"><html:text property="bankAddressVO.address1" styleClass="textBox textBoxMedium" maxlength="250" disabled="<%=viewmode%>" readonly="<%=readMode %>"/></td>
        </logic:notEmpty>
        <logic:empty name="frmAccounts" property="bankAddressVO.address1">
        <td width="20%" class="formLabel">Address 1: <!-- <span class="mandatorySymbol">*</span> --></td>
        <td width="33%"><html:text property="bankAddressVO.address1" styleClass="textBox textBoxMedium" maxlength="250" style="background-color: #EEEEEE;"  disabled="<%=viewmode%>" readonly="<%=readMode %>" value=""/></td>
        </logic:empty>
        <td width="17%" class="formLabel">Address 2:</td>
        <td width="30%"><html:text property="bankAddressVO.address2" styleClass="textBox textBoxMedium" maxlength="250" disabled="<%=viewmode%>" readonly="<%=readMode %>"/></td>
      </tr>
	  <tr>
	   <td class="formLabel">Address 3: </td>
 	    <td><html:text property="bankAddressVO.address3" styleClass="textBox textBoxMedium" maxlength="250" disabled="<%=viewmode%>" readonly="<%=readMode %>"/></td>
	  </tr>
	  <tr>
        <td class="formLabel">PO Box:<!-- <span class="mandatorySymbol">*</span> --></td>
        <td><html:text property="bankAddressVO.pinCode" styleClass="textBox textBoxSmall" maxlength="10" readonly="<%=readMode %>" disabled="<%=viewmode%>"/></td>
        
        
       
        <td class="formLabel">Country: <span class="mandatorySymbol">*</span> </td>
        <td colspan="3">
        
        <logic:empty name="frmAccounts" property="emplNum">
          <logic:equal name="frmAccounts" property="bankAccountQatarYN" value="N">
         	<html:select property ="bankAddressVO.countryCode" styleClass="selectBox selectBoxMedium">
        	     <html:option value="">Select From List</html:option>
                 <html:options collection="OutsideQatarCountryList" property="cacheId" labelProperty="cacheDesc"/>
          </html:select>
         </logic:equal>
         <logic:notEqual name="frmAccounts" property="bankAccountQatarYN" value="N">
        	<html:select property ="bankAddressVO.countryCode" styleClass="selectBox selectBoxMedium" >
        	     <html:option value="">Select From List</html:option>
                 <html:options collection="countryCode" property="cacheId" labelProperty="cacheDesc"/>
          </html:select>
         </logic:notEqual>
          </logic:empty>
            <logic:notEmpty name="frmAccounts" property="emplNum">
            <html:select property ="bankAddressVO.countryCode" styleClass="selectBox selectBoxMedium" readonly="true" disabled="true">
        		<html:option value="">--Select from list--</html:option>
                 <html:options collection="countryCode" property="cacheId" labelProperty="cacheDesc"/>
          </html:select>
            </logic:notEmpty>
          
         </td> 
      </tr>
      <tr>
        <td colspan="4" height="5"></td>
      </tr>
       <logic:empty name="frmAccounts" property="partnerOrProvider">
      <tr>
        <td colspan="4" class="formLabelBold">Provider Management Details </td>
        </tr>
	  <tr>
        <td colspan="4" height="2"></td>
      </tr>
	  <tr>
        <td class="formLabel">Management Name:</td>
        <td>
         <logic:empty name="frmAccounts" property="emplNum">
        <html:text property="managementName" styleClass="textBox textBoxLarge" maxlength="250" disabled="<%=viewmode%>" onkeyup="ConvertToUpperCase(event.srcElement);"/>
        </logic:empty>
         <logic:notEmpty name="frmAccounts" property="emplNum">
          <html:text property="managementName" styleClass="textBox textBoxLarge" maxlength="250" disabled="true" readonly="true" onkeyup="ConvertToUpperCase(event.srcElement);"/>
         </logic:notEmpty>
        </td>
        <td class="formLabel">Issue Cheques To:</td>
           <td>
           <logic:empty name="frmAccounts" property="emplNum">
           <html:select property="issueChqTo" styleClass="selectBox selectBoxMedium" disabled="<%=viewmode%>">
                 <html:options collection="chequeCode" property="cacheId" labelProperty="cacheDesc"/>
           </html:select>
           </logic:empty>
           <logic:notEmpty name="frmAccounts" property="emplNum">
            <html:select property="issueChqTo" styleClass="selectBox selectBoxMedium" disabled="true" readonly="true">
                 <html:options collection="chequeCode" property="cacheId" labelProperty="cacheDesc"/>
           </html:select>
           </logic:notEmpty>
           </td>
         </tr>
         </logic:empty>
         
         
         
     <!-- intX Finance Review-->
     <tr>
        <td colspan="4" class="formLabelBold">Finance Reviewed Details </td>
     </tr>
     <tr>
        <td colspan="4" height="2"></td>
      </tr>
	  <tr>
        <td class="formLabel">Finance Reviewed:</td>
        <td><bean:write name="frmAccounts" property="reviewedYN"/>
        <td class="formLabel">Reviewed By:</td>
        <td><bean:write name="frmAccounts" property="reviewedBy"/>
        </td>
     </tr>
     <tr>
        <td class="formLabel">Reviewed Date:</td>
        <td><bean:write name="frmAccounts" property="reviewedDate"/>
     </tr>
         
</table>
</fieldset>



	<fieldset>
	<legend>Bank Guarantee Details</legend>
	<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
	   <tr>
        <td class="formLabel">Bank Guarantee:</td>
        <td><html:checkbox property="bankGuantReqYN" value="Y" disabled="<%=viewmode%>"/>
        </td>
        <td>&nbsp;</td>
        <td class="formLabelBold">&nbsp;</td>
      </tr>
      <tr>
        <td width="20%" class="formLabel">Bank Name:</td>
        <td width="33%" class="formLabelBold"><bean:write name="frmAccounts" property="guaranteeBankName"/>
        </td>
        <td width="17%"><span class="formLabel">Amount :</span></td>
        <td width="30%" class="formLabelBold"><bean:write name="frmAccounts" property="guaranteeAmountWord"/> </td>
      </tr>
      <tr>
        <td class="formLabel">Commencement Date: </td>
        <td class="formLabelBold"><bean:write name="frmAccounts" property="guaranteeCommDate"/></td>
        <td class="formLabel">Expiry Date: </td>
        <td class="formLabelBold"><bean:write name="frmAccounts" property="guaranteeExpiryDate"/></td>
      </tr>
    </table>
	</fieldset>
	
	
	<!-- E N D : Form Fields -->
    <!-- S T A R T : Buttons -->
	<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="100%" align="center">
	     <%
			if(TTKCommon.isAuthorized(request,"Edit"))
			{
		%>
		    	<button type="button" name="Button" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onUserSubmit();"><u>S</u>ave</button>&nbsp;
				<button type="button" name="Button2" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onReset();"><u>R</u>eset</button>
		<%
		    }
	 	 %>
	 	 </td>
	  </tr>
	</table>
</div>






<!-- E N D : Buttons -->
<INPUT TYPE="hidden" NAME="mode" VALUE="">
<input type="hidden" name="child" value="">
<INPUT TYPE="hidden" NAME="hidpoEmpFeeCharged" VALUE="">
<INPUT TYPE="hidden" NAME="bankGuar" VALUE="">
<html:hidden property="tpaRegdDate"/>
<html:hidden property="partnerOrProvider"/>
<input type="hidden" name="focusID" value="">
<INPUT TYPE="hidden" NAME="emplFeeChrgYn" id="emplFeeChrgYn" VALUE="">
<INPUT TYPE="hidden" NAME="emplNum" VALUE="">

<input type="hidden" name="HospitalSeqId" value="<%= HospitalSeqId%>"/>
<INPUT TYPE="hidden" NAME="bankGuantReqYN" VALUE="">
<logic:notEmpty name="frmAccounts" property="frmChanged">
	<script> ClientReset=false;TC_PageDataChanged=true;</script>
</logic:notEmpty>
</html:form>
