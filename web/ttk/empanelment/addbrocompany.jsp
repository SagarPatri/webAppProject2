<%
/** @ (#) addinscompany.jsp 25th Nov 2005
 * Project     : TTK Healthcare Services
 * File        : addinscompany.jsp
 * Author      : Arun K N
 * Company     : Span Systems Corporation
 * Date Created: 25th Nov 2005
 *
 * @author 		 : Arun K N
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */
%>

<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.HashMap,org.apache.struts.action.DynaActionForm" %>

<script language="javascript" src="/ttk/scripts/utils.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script language="javascript" src="/ttk/scripts/empanelment/addbrocompany.js"></script>
	<link rel="stylesheet" type="text/css" href="css/style.css" />
	
	<link rel="stylesheet" type="text/css" href="css/autoComplete.css" />
	<script language="javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script>
	<script language="javascript" src="/ttk/scripts/jquery.autocomplete.js"></script>

      
<SCRIPT>
  $(document).ready(function() {
    $("#brokerName").autocomplete("auto.jsp?mode=brokerName");
   getInactive();
});
  
  function getBrokerCode(obj)
  {
	  $(document).ready(function() {
	  $("#brokerName").blur(function(){
	    	var ID	=	obj.value;
	        $.ajax({
	        		url: "/AsynchronousAction.do?mode=getCommonMethod&id="+ID+"&getType=BroCode", 
	        		success: function(result){
	      			var res	=	result.split("@");
	     			 document.forms[1].companyCode.value=res[0];
	        }}); 
	    });
	  });
  }
  
  function getInactive()
  {
var elementcheckedornot=document.forms[1].companyStatus[0].checked;


if(!elementcheckedornot){
	
document.getElementById("inactivelabel").style.display="";
document.getElementById("inactiveD").style.display="";
document.getElementById("inactivelabel2").style.display="";
document.getElementById("inactiveremarks").style.display="";

}else{
	document.getElementById("inactivelabel").style.display="none";

	document.getElementById("inactiveD").style.display="none";
	document.getElementById("inactivelabel2").style.display="none";
	document.getElementById("inactiveremarks").style.display="none";

	//document.getElementById("stopcshlessid").value="";
}
//document.getElementById("companyStatus").setAttribute("value","N");


  }
  
</SCRIPT>
 
<script language="javascript">
var JS_Focus_ID="<%=TTKCommon.checkNull(request.getParameter("focusID"))%>";
</script>
<%
	DynaActionForm frmCompanyDetails=(DynaActionForm)request.getSession().getAttribute("frmBroCompanyDetails");
	String strOffType=(String)frmCompanyDetails.get("officeType");
	pageContext.setAttribute("alOfficeInfo",Cache.getCacheObject("officeInfo"));
	pageContext.setAttribute("alSectorType",Cache.getCacheObject("sectorTypeCode"));
	pageContext.setAttribute("alCityCode",Cache.getCacheObject("cityCode"));
	pageContext.setAttribute("alStateCode",Cache.getCacheObject("stateCode"));
	pageContext.setAttribute("alCountryCode",Cache.getCacheObject("countryCode"));
	pageContext.setAttribute("alFundDisbCode",Cache.getCacheObject("fundDisbursalCode"));
	pageContext.setAttribute("alFrequencyCode",Cache.getCacheObject("frequencyCode"));

	//for decoding the office type to display the label
	HashMap hmOfficeInfo=new HashMap();
    hmOfficeInfo.put("IHO","Head Office");
    hmOfficeInfo.put("IRO","Regional Office");
    hmOfficeInfo.put("IDO","Divisional Office");
    hmOfficeInfo.put("IBO","Branch Office");

    boolean viewmode=true;
	if(TTKCommon.isAuthorized(request,"Edit"))
		viewmode=false;
	pageContext.setAttribute("viewmode",new Boolean(viewmode));

%>

<!-- S T A R T : Content/Form Area -->
	<html:form action="/UpdateBroCompanyAction.do" method="post" >

	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="100%">Broker - <bean:write name="frmBroCompanyDetails" property="caption"/></td>
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

    <!-- S T A R T : Form Fields -->
	<fieldset><legend>General</legend>
		<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
		  <tr>
		    <td class="formLabel">Office Type:</td>
		    <td class="textLabelBold"><%=hmOfficeInfo.get(strOffType)%></td>

			    <td>Al Koot Branch: <span class="mandatorySymbol">*</span> </td>
			    <td>
			    	<html:select property="TTKBranchCode" styleClass="selectBox selectBoxMedium" disabled="<%= viewmode %>">
        				<html:option value="">Select from list</html:option>
        				<html:options collection="alOfficeInfo" property="cacheId" labelProperty="cacheDesc"/>
			  		</html:select>
			    </td>

		  </tr>
		  <tr>
		    <td width="22%" class="formLabel">Broker Name: <span class="mandatorySymbol">*</span></td>
		    <td width="32%">
		    	<logic:match name="frmBroCompanyDetails" property="officeType" value="IHO">
		    		<html:text property="companyName" styleId="brokerName" styleClass="textBox textBoxLarge" maxlength="250" onkeyup="ConvertToUpperCase(event.srcElement);" disabled="<%= viewmode %>" onblur="getBrokerCode(this)"/>
		    	</logic:match>

		    	<logic:notMatch name="frmBroCompanyDetails" property="officeType" value="IHO">
					<b><bean:write name="frmBroCompanyDetails" property="companyName"/></b>
		    		<html:hidden property="companyName"/>
				</logic:notMatch>
		    </td>
		    <td width="22%" class="formLabel">&nbsp;</td>
		    <td width="24%">
		    	&nbsp;
		    </td>
		  </tr>
		  <tr>
		    	<logic:match name="frmBroCompanyDetails" property="officeType" value="IHO">
		    <td class="formLabel">Broker License No:  <span class="mandatorySymbol">*</span></td>
		 
		    <td>
		    
		    	<html:text property="companyCode" styleClass="textBox textBoxMedium" maxlength="30" onkeyup="ConvertToUpperCase(event.srcElement);" disabled="<%= viewmode %>"/>
		    </td>
		    </logic:match>
		    	    	<logic:notMatch name="frmBroCompanyDetails" property="officeType" value="IHO">
		    	    	
		    <td class="formLabel">Broker License No:  <span class="mandatorySymbol">*</span></td>
		 
		    <td>
		    <b><bean:write name="frmBroCompanyDetails" property="companyCode"/></b>
		    		<html:hidden property="companyCode"/>
		    </td>
		    </logic:notMatch>
		    <td class="formLabel">Broker Abbreviation: <span class="mandatorySymbol">*</span></td>
		    <td>
		    	<logic:match name="frmBroCompanyDetails" property="officeType" value="IHO">
		    		<html:text property="companyAbbreviation" styleClass="textBox textBoxSmall" maxlength="2" onkeyup="ConvertToUpperCase(event.srcElement);" disabled="<%= viewmode %>"/>
		    	</logic:match>

		    	<logic:notMatch name="frmBroCompanyDetails" property="officeType" value="IHO">
					<b><bean:write name="frmBroCompanyDetails" property="companyAbbreviation"/></b>
		    		<html:hidden property="companyAbbreviation"/>
				</logic:notMatch>
		    </td>
		  </tr>
		  <tr>
		  	<td class="formLabel">Sector Type: <span class="mandatorySymbol">*</span></td>
		    <td>
			    <logic:match name="frmBroCompanyDetails" property="officeType" value="IHO">
					<html:select property="sectorTypeCode" styleClass="selectBox selectBoxMedium" disabled="<%= viewmode %>">
        				<html:option value="">Select from list</html:option>
        				<html:options collection="alSectorType" property="cacheId" labelProperty="cacheDesc"/>
			  		</html:select>
				</logic:match>

				<logic:notMatch name="frmBroCompanyDetails" property="officeType" value="IHO">
					<b><bean:write name="frmBroCompanyDetails" property="sectorTypeDesc"/></b>
		    		<html:hidden property="sectorTypeCode"/>
				</logic:notMatch>
		    </td>
		    <td class="formLabel">Empanelment Date: <span class="mandatorySymbol">*</span></td>
		    <td>
		    	<html:text property="empanelmentDate" styleClass="textBox textBoxSmall" maxlength="10" disabled="<%= viewmode %>"/>
		    	<logic:match name="viewmode" value="false">
				    <A NAME="CalendarObjectempDate" ID="CalendarObjectempDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectempDate','forms[1].empanelmentDate',document.forms[1].empanelmentDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle"></a>
			    </logic:match>
		  	</td>
		  </tr>
		  <tr>
		  	<td class="formLabel">Active:</td>
	    	<td>
				<html:checkbox property="companyStatus" styleId="companyStatus" value="Y" disabled="<%= viewmode %>" onclick="getInactive()"/>
	    	</td>
	    	
	    	<%-- <td class="formLabel">License Number: <span class="mandatorySymbol">*</span></td>
	    	<td>
		    	<html:text property="licenseNo" styleClass="textBox textBoxMedium" maxlength="30" onkeyup="ConvertToUpperCase(event.srcElement);" disabled="<%= viewmode %>"/>
		    </td> --%>
		    <td class="formLabel">Registration Date: <span class="mandatorySymbol">*</span></td>
		    <td>
		    	<html:text property="regDate" styleClass="textBox textBoxSmall" maxlength="10" disabled="<%= viewmode %>"/>
		    	<logic:match name="viewmode" value="false">
				    <A NAME="CalendarObjectempDate" ID="CalendarObjectempDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectempDate','forms[1].regDate',document.forms[1].regDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle"></a>
			    </logic:match>
		  	</td>
	    	
			</tr>
		     <!--Shortfall CR KOC1179 -->
	    	<tr>
	    	<td class="formLabel">Email Id:</td>
	    	<td>
	    	<!-- bean:write  name="frmBroCompanyDetails"  property="companyEmailID"/-->
	        <html:text property="companyEmailID" styleClass="textBox textBoxMedium" maxlength="50"/>
        	</td>
        	
        	<td class="formLabel">License Expiry Date: <span class="mandatorySymbol">*</span></td>
		    <td>
		    	<html:text property="licenseExpDate" styleClass="textBox textBoxSmall" maxlength="10" disabled="<%= viewmode %>" styleId="lisence"/>
		    	<logic:match name="viewmode" value="false">
				    <A NAME="CalendarObjectempDate" ID="CalendarObjectempDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectempDate','forms[1].licenseExpDate',document.forms[1].licenseExpDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle"></a>
			    </logic:match>
		  	</td>		  			  	
        	</tr>
        	
<tr>
<td class="formLabel" ><span id="inactivelabel">Inactive from: <span class="mandatorySymbol">*</span></span></td>
		    <td>
		   <span id="inactiveD">
		    	<html:text property="inactiveDate" styleClass="textBox textBoxSmall" maxlength="10" disabled="<%= viewmode %>"/>
		    	<logic:match name="viewmode" value="false">
				<A NAME="CalendarObjectempDate" ID="CalendarObjectempDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectempDate','forms[1].inactiveDate',document.forms[1].inactiveDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle"></a>
			    </logic:match>
			   </span>
		  	</td>
		    
		    </tr>


		<tr>
		 <td height="50" class="formLabel"><span id="inactivelabel2" style="">Remarks:<span class="mandatorySymbol">*</span></span></td>
	  <td colspan="3" class="textLabel"><span id="inactiveremarks" style=""> <html:textarea property="remarks" cols="35" rows="2"/></span>
	 </td>
		</tr>				



        	<%-- <tr> 
        		<td> </td>
        		<td> </td>
        		<td class="formLabel">License Expiry Date: <span class="mandatorySymbol">*</span></td>
		    <td>
		    	<html:text property="licenseExpDate" styleClass="textBox textBoxSmall" maxlength="10" disabled="<%= viewmode %>"/>
		    	<logic:match name="viewmode" value="false">
				    <A NAME="CalendarObjectempDate" ID="CalendarObjectempDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectempDate','forms[1].licenseExpDate',document.forms[1].licenseExpDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle"></a>
			    </logic:match>
		  	</td>
        	</tr> --%>
        	<!-- End Shortfall CR KOC1179 -->
        	      	
<!--<logic:notEqual value="Y"  property="companyStatus"  name="frmBroCompanyDetails" > --> 
<!--  <span id="inactive" style="background-color: red;">-->






<!-- </logic:notEqual>  -->
		</table>
	</fieldset>
	<fieldset>
	<legend>Address Details</legend>
		<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td width="22%" class="formLabel">Address 1: <span class="mandatorySymbol">*</span></td>
	        <td width="32%">
	        	<html:text property="addressVO.address1" styleClass="textBox textBoxMedium" maxlength="250" disabled="<%= viewmode %>"/>
	        </td>
	        <td width="22%" class="formLabel">Address 2: </td>
	        <td width="24%">
	        	<html:text property="addressVO.address2" styleClass="textBox textBoxMedium" maxlength="250" disabled="<%= viewmode %>"/>
	        </td>
	      </tr>
	      <tr>
	        <td class="formLabel">Address 3: </td>
	        <td>
	        	<html:text property="addressVO.address3" styleClass="textBox textBoxMedium" maxlength="250" disabled="<%= viewmode %>"/>
	        </td>
	        <td class="formLabel">City: <span class="mandatorySymbol">*</span></td>
	        <td>
	       		<html:select property="addressVO.stateCode" styleId="stateCode" styleClass="selectBox selectBoxMedium" onchange="onChangeState()" disabled="<%= viewmode %>">
    				<html:option value="">Select from list</html:option>
    				<html:options collection="alStateCode" property="cacheId" labelProperty="cacheDesc"/>
		  		</html:select>
	        </td></tr>
	      <tr>
			<td class="formLabel">Area: <span class="mandatorySymbol">*</span></td>
	        <td>
		        <html:select property="addressVO.cityCode" styleId="cityCode" styleClass="selectBox selectBoxMedium" disabled="<%= viewmode %>">
    				<html:option value="">Select from list</html:option>
    				<html:optionsCollection property="alCityList" label="cacheDesc" value="cacheId"/>
		  		</html:select>
	        </td>
	        <td class="formLabel">PO Box: <span class="mandatorySymbol">*</span></td>
	        <td>
	        	<html:text property="addressVO.pinCode" styleClass="textBox textBoxSmall" maxlength="10" disabled="<%= viewmode %>"/>
	        </td>
	      </tr>
	      <tr>
	        <td class="formLabel">Country: <span class="mandatorySymbol">*</span></td>
	        <td colspan="3">
		      <html:select property="addressVO.countryCode" styleId="cnCode" styleClass="selectBox selectBoxMedium" disabled="<%= viewmode %>" onchange="getStates('state','stateCode')">
		      <html:option value="">Select from List</html:option>
        		<html:options collection="alCountryCode" property="cacheId" labelProperty="cacheDesc"/>
			  </html:select>
		   </td>
	      </tr>
	      
	      <tr>
  			<td class="formLabel">Phone No1 :<span class="mandatorySymbol">*</span></td>
  			<td>
				<html:text property="addressVO.isdCode"  styleId="isdCode1" styleClass="disabledfieldType" size="3" maxlength="3" readonly="true"/>&nbsp;
				<html:text property="addressVO.stdCode" styleId="stdCode1" styleClass="disabledfieldType" size="3" maxlength="3" readonly="true"/>&nbsp;
				
			
		   		<logic:empty name="frmBroCompanyDetails" property="addressVO.phoneNo1">
				<html:text property="addressVO.phoneNo1" styleId="phoneNbr1" styleClass="disabledfieldType" maxlength="15" value="Phone No1" onclick="changeMe(this)" onblur="getMe('Phone No1')"/>
				</logic:empty>
				
				<logic:notEmpty name="frmBroCompanyDetails" property="addressVO.phoneNo1">
		   			<html:text property="addressVO.phoneNo1" styleId="phoneNbr1" styleClass="disabledfieldType"  maxlength="15" onclick="changeMe(this)" onblur="getMe('Phone No1')"/> 
		   		</logic:notEmpty>
   		</td>
   		
		<td class="formLabel">Phone No2 :</td>
		<td>
				<html:text property="addressVO.isdCode" styleId="isdCode2" styleClass="disabledfieldType" size="3" maxlength="3" readonly="true"/>&nbsp;
				<html:text property="addressVO.stdCode"  styleId="stdCode2" styleClass="disabledfieldType" size="3" maxlength="3" readonly="true"/>&nbsp;
				
				
				<logic:empty name="frmBroCompanyDetails" property="addressVO.phoneNo2">
				<html:text property="addressVO.phoneNo2" styleId="phoneNbr2" styleClass="disabledfieldType" maxlength="15" value="Phone No2" onclick="changeMe(this)" onblur="getMe('Phone No2')"/>
				</logic:empty>
				
				<logic:notEmpty name="frmBroCompanyDetails" property="addressVO.phoneNo2">
		   			<html:text property="addressVO.phoneNo2" styleId="phoneNbr2" styleClass="disabledfieldType"  maxlength="15" onclick="changeMe(this)" onblur="getMe('Phone No2')"/> 
		   		</logic:notEmpty>    
   		</td>			
	</tr>
	
	    </table>
	</fieldset>
	<!-- E N D : Form Fields -->
    <!-- S T A R T : Buttons -->
	<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="100%" align="center">
	    	<logic:match name="viewmode" value="false">
    			<button type="button" name="Button" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onSave();"><u>S</u>ave</button>&nbsp;
				<button type="button" name="Button2" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onReset();"><u>R</u>eset</button>&nbsp;
    		</logic:match>
			<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose();"><u>C</u>lose</button>
	    </td>
	  </tr>
	</table>
	</div>
	<!-- E N D : Buttons -->
	<html:hidden property="officeType"/>
	<html:hidden property="insuranceSeqID"/>
	<input type="hidden" name="child" value="Add/Edit Broker">
	<input type="hidden" name="mode">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	<input type="hidden" name="companyStatus" value="">
	<input type="hidden" name="focusID" value="">
	<logic:notEmpty name="frmBroCompanyDetails" property="frmChanged">
		<script> ClientReset=false;TC_PageDataChanged=true;</script>
	</logic:notEmpty>
</html:form>
<!-- E N D : Content/Form Area -->