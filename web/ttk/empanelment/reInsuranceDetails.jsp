<%
/** @ (#) addinscompany.jsp 25th Nov 2005
 * Project     : TTK Healthcare Services
 * File        : addinscompany.jsp
 * Author      : Arun K N
 * Company     : Span Systems Corporation
 * Date Created: 25th Nov 2005
 * @author 		 : 
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
<script language="javascript" src="/ttk/scripts/empanelment/reInsuranceList.js"></script>

<head>
	
	<link rel="stylesheet" type="text/css" href="css/style.css" />
	
	<link rel="stylesheet" type="text/css" href="css/autoComplete.css" />
	<script language="javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script>
	<script language="javascript" src="/ttk/scripts/jquery.autocomplete.js"></script>

      
<SCRIPT>
  $(document).ready(function() {
    $("#insName").autocomplete("auto.jsp?mode=authStandard");
}); 
  function getInsCode(obj)
  {
	  $(document).ready(function() {
	  $("#insName").blur(function(){
	    	var ID	=	obj.value;
	        $.ajax({
	        		url: "/AsynchronousAction.do?mode=getCommonMethod&id="+ID+"&getType=InsCode", 
	        		success: function(result){
	      			var res	=	result.split("@");
	     			 document.forms[1].companyCode.value=res[0];
	        }}); 
	    });
	  });
  }
  function getMe(obj)
  {
  	//alert(obj);
  	if(obj=='Phone No1')
  	{
  		if(document.getElementById("phoneNbr1").value=="")
  			document.getElementById("phoneNbr1").value=obj;
  		
  	}else if(obj=='Phone No2')
  	{
  		if(document.getElementById("phoneNbr2").value=="")
  			document.getElementById("phoneNbr2").value=obj;
  	}
  }

  function changeMe(obj)
  {
  	var val	=	obj.value;
  	//alert(val);
  	if(val=='Phone No1')
  	{
  		document.getElementById("phoneNbr1").value="";
  	}
  	if(val=='Phone No2')
  	{
  		document.getElementById("phoneNbr2").value="";
  	}
  }
</SCRIPT>

</head>
<script language="javascript">
var JS_Focus_ID="<%=TTKCommon.checkNull(request.getParameter("focusID"))%>";
</script>
<%
	/* DynaActionForm frmCompanyDetails=(DynaActionForm)request.getSession().getAttribute("frmInsCompanyDetails");
	String strOffType=(String)frmCompanyDetails.get("officeType");
 */
/* 	pageContext.setAttribute("alOfficeInfo",Cache.getCacheObject("officeInfo"));
	pageContext.setAttribute("alSectorType",Cache.getCacheObject("sectorTypeCode"));
	pageContext.setAttribute("alCityCode",Cache.getCacheObject("cityCode"));
	pageContext.setAttribute("alStateCode",Cache.getCacheObject("stateCode"));
	
	 */
	pageContext.setAttribute("alCountryCode",Cache.getCacheObject("countryCode"));
	pageContext.setAttribute("alStateCode",Cache.getCacheObject("stateCode"));
	pageContext.setAttribute("reInsuranceStructureType",Cache.getCacheObject("reInsuranceStructureType"));
	pageContext.setAttribute("treatyType",Cache.getCacheObject("treatyType"));
	pageContext.setAttribute("pricingTerms",Cache.getCacheObject("pricingTerms"));
	pageContext.setAttribute("profitShareBasis",Cache.getCacheObject("ProfitShareBasis"));
	pageContext.setAttribute("frequencyOfGenOfBordereaux",Cache.getCacheObject("FrequencyOfGenOfBordereaux"));
	pageContext.setAttribute("unexpiredPremReserveBasis",Cache.getCacheObject("UnexpiredPremReserveBasis"));
	pageContext.setAttribute("frequencyOfRemittance",Cache.getCacheObject("FrequencyOfRemittance"));
	
	/* //added for Mail-SMS Template for Cigna
	pageContext.setAttribute("notificinfo",Cache.getCacheObject("notificinfo"));
	pageContext.setAttribute("alUserRestrictionGroup",Cache.getCacheObject("userRestrictionGroup"));//KOC Cigna_insurance_resriction  
 */
	//for decoding the office type to display the label

	

    boolean viewmode=true;
	if(TTKCommon.isAuthorized(request,"Edit"))
		viewmode=false;
	pageContext.setAttribute("viewmode",new Boolean(viewmode));

%>

<logic:empty name="ErrorDisplay">
<!-- S T A R T : Content/Form Area -->
	<html:form action="/AddReInsuranceAction.do" method="post" >

	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="100%">Re-Insurance - <bean:write name="frmReInsCompanyDetails" property="caption"/></td>
	    <td align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
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
		    <td class="formLabel">Reinsurer ID:<span class="mandatorySymbol">*</span></td>
		    <td>
		        <html:text property="reinsurerId" styleClass="textBox textBoxMedium" maxlength="15" />
		    </td>
		    <td class="formLabel">Reinsurer Name:<span class="mandatorySymbol">*</span></td>
		    <td>
		        <html:text property="reinsurerName" styleClass="textBox textBoxMedium" maxlength="250" />
		    </td>
		</tr>    
		  <tr>
		    <%-- <td class="formLabel">Reinsurer Address:<span class="mandatorySymbol">*</span></td>
		    <td>
		        <html:text property="reinsurerAddress" styleClass="textBox textBoxSmall" maxlength="250" />
		    </td> --%>

			    <td>Treaty ID: <span class="mandatorySymbol">*</span> </td>
			 <td>
		        <html:text property="treatyID" styleClass="textBox textBoxMedium" maxlength="250" />
		    </td>
				<td class="formLabel">Reinsurance Structure Type: <span class="mandatorySymbol">*</span></td>
		    <td>
		    	<%-- <html:text property="reInsuranceStructureType" styleId="insName" styleClass="textBox textBoxMedium" maxlength="250"/> --%>
		    	 <html:select property="reInsuranceStructureType" styleClass="selectBox selectBoxMedium" >
        				<html:option value="">Select from list</html:option>
        				<html:options collection="reInsuranceStructureType" property="cacheId" labelProperty="cacheDesc"/>
			  	</html:select> 
		    </td>
		  </tr>
		  <tr>
		    
		    <td class="formLabel">Treaty Type: <span class="mandatorySymbol">*</span></td>
		    <td>
		    	<%-- <html:text property="treatyType" styleId="treatyType" styleClass="textBox textBoxMedium" maxlength="250"/> --%>
		    	<html:select property="treatyType" styleClass="selectBox selectBoxMedium" >
        				<html:option value="">Select from list</html:option>
        				<html:options collection="treatyType" property="cacheId" labelProperty="cacheDesc"/>
			  	</html:select>
		    </td>
		    <td class="formLabel">Pricing Terms:  <span class="mandatorySymbol">*</span></td>
		    <td>
		    	<%-- <html:text property="pricingTerms" styleClass="textBox textBoxMedium" maxlength="30" onkeyup="ConvertToUpperCase(event.srcElement);" /> --%>
		        <html:select property="pricingTerms" styleClass="selectBox selectBoxMedium" >
        				<html:option value="">Select from list</html:option>
        				<html:options collection="pricingTerms" property="cacheId" labelProperty="cacheDesc"/>
			  	</html:select>
		    </td>
		  </tr>
		  <tr>
		    
		    <td class="formLabel">Qs Retention Share %: <span class="mandatorySymbol">*</span></td>
		    <td>
		    		<html:text property="retentionSharePerc" styleId="retentionSharePercid" styleClass="textBox textBoxMedium" maxlength="3" onkeyup="isNumaricOnly(this)"/>
		    </td>
		    
			<td class="formLabel">Qs ReInsurer Share %: <span class="mandatorySymbol">*</span></td>
		    <td>
		    		<html:text property="reinsuranceSharePerc" styleId="reinsuranceSharePercid" styleClass="textBox textBoxMedium" maxlength="3" onkeyup="isNumaricOnly(this)"/>
		    </td>
		  </tr>
		  <tr>
		    
		    <td class="formLabel">Sp Retention Share %: <span class="mandatorySymbol">*</span></td>
		    <td>
		    		<html:text property="spretentionSharePerc" styleId="spretentionSharePercid" styleClass="textBox textBoxMedium" maxlength="3" onkeyup="isNumaricOnly(this)"/>
		    </td>
		    
			<td class="formLabel">Sp ReInsurer Share %: <span class="mandatorySymbol">*</span></td>
		    <td>
		    		<html:text property="spreinsuranceSharePerc" styleId="spreinsuranceSharePercid" styleClass="textBox textBoxMedium" maxlength="3" onkeyup="isNumaricOnly(this)"/>
		    </td>
		  </tr>
		  
		  <tr>
		 
		 <td class="formLabel">Cedants expense allowance %: <span class="mandatorySymbol">*</span></td>
		    <td>
		    	<html:text property="cedantsExpAllowancePerc" styleClass="textBox textBoxMedium" maxlength="3" />
<%-- 		    	<html:text property="cedantsExpAllowancePerc" styleClass="textBox textBoxSmall" maxlength="10" disabled="<%= viewmode %>"/> --%>
		    	
		  	</td> 	
		<td class="formLabel">Reinsurer's expense allowance %: <span class="mandatorySymbol">*</span></td>
		   <td>
		    		<html:text property="reinsExpAllowancePerc" styleClass="textBox textBoxMedium" maxlength="3" />
		    </td>
		    
		  </tr>
		  
		  
		  
		 <%--  <tr>
		  	<td class="formLabel">IBAN/ Swift Code:<span class="mandatorySymbol">*</span></td>
	    	<td>
				<html:text property="IBANSwiftCode" styleClass="textBox textBoxMedium" maxlength="3" />
	    	</td>
	    	<td class="formLabel">Bank Name:<span class="mandatorySymbol">*</span></td>
	    	<td>
	        <html:text property="bankName" styleClass="textBox textBoxMedium" maxlength="50"/>
        	</td>
        	</tr> --%>
        	
        	<%-- <tr>
		  	<td class="formLabel">Bank Address 1:<span class="mandatorySymbol">*</span></td>
	    	<td>
				<html:text property="bankAddress1" styleClass="textBox textBoxMedium" maxlength="250" />
	    	</td>
	    	<td class="formLabel">Bank Address 2:</td>
	    	<td>
	        <html:text property="bankAddress2" styleClass="textBox textBoxMedium" maxlength="250"/>
        	</td>
        	</tr> --%>
        	<tr>
		  	<%-- <td class="formLabel">Bank Address 3:<span class="mandatorySymbol">*</span></td>
	    	<td>
				<html:text property="bankAddress3" styleClass="textBox textBoxMedium" maxlength="250" />
	    	</td> --%>
	    	
        	</tr>
        	<tr>
		  	<td class="formLabel">Unexpired Premium Reserve Basis:<span class="mandatorySymbol">*</span></td>
	    	<td>
	<%-- 			<html:text property="unexpiredPremReservBasis" styleClass="textBox textBoxMedium" maxlength="250" /> --%>
				<html:select property="unexpiredPremReservBasis" styleClass="selectBox selectBoxMedium" >
        				<html:option value="">Select from list</html:option>
        				<html:options collection="unexpiredPremReserveBasis" property="cacheId" labelProperty="cacheDesc"/>
			  	</html:select>
	    	</td>
	    	<td class="formLabel">Frequency of generation  of bordereaux:<span class="mandatorySymbol">*</span></td>
	    	<td>
	        <%-- <html:text property="freqOfGenOfBordereaux" styleClass="textBox textBoxMedium" maxlength="50"/> --%>
	           <html:select property="freqOfGenOfBordereaux" styleClass="selectBox selectBoxMedium" >
        				<html:option value="">Select from list</html:option>
        				<html:options collection="frequencyOfGenOfBordereaux" property="cacheId" labelProperty="cacheDesc"/>
			  	</html:select> 
			  	
        	</td>
        	</tr>
        	<%-- <tr>
		  	<td class="formLabel">Profit Share basis:<span class="mandatorySymbol">*</span></td>
	    	<td>
				<html:text property="profitShareBasis" styleClass="textBox textBoxMedium" maxlength="250" />
				<html:select property="profitShareBasis" styleClass="selectBox selectBoxMedium" >
        				<html:option value="">Select from list</html:option>
        				<html:options collection="profitShareBasis" property="cacheId" labelProperty="cacheDesc"/>
			  	</html:select> 
	    	</td>
	    	<td class="formLabel">Profit Share %:<span class="mandatorySymbol">*</span></td>
	    	<td>
	        <html:text property="profitSharePercentage" styleClass="textBox textBoxMedium" maxlength="3"/>
        	</td>
        	</tr>
        	<tr>
		  	<td class="formLabel">Profit Share Premium %:<span class="mandatorySymbol">*</span></td>
	    	<td>
				<html:text property="profitSharePremiumPerc" styleClass="textBox textBoxMedium" maxlength="250" />
				
	    	</td>
	    	<td class="formLabel">Profit Share Claims %:<span class="mandatorySymbol">*</span></td>
	    	<td>
	        <html:text property="profitShareClaimsPerc" styleClass="textBox textBoxMedium" maxlength="3"/>
        	</td>
        	</tr> --%>
        	<tr>
		  	<%-- <td class="formLabel">Profit Share Expenses %:<span class="mandatorySymbol">*</span></td>
	    	<td>
				<html:text property="profitShareExpensesPerc" styleClass="textBox textBoxMedium" maxlength="250" />
				
	    	</td> --%>
	    	<td class="formLabel">Reporting currency:<span class="mandatorySymbol">*</span></td>
	    	<td>
	        <html:text property="reportingCurrency" styleClass="textBox textBoxMedium" maxlength="3"/>
        	</td>
        	
        	<td class="formLabel">Remitting currency:<span class="mandatorySymbol">*</span></td>
	    	<td>
				<html:text property="remittingCurrency" styleClass="textBox textBoxMedium" maxlength="250" />
	    	</td>
        	</tr>
        	<tr>
		  	<td class="formLabel">Empanelment Date:<span class="mandatorySymbol">*</span></td>
	    	<td>
				<html:text property="empanelDate" styleClass="textBox textBoxMedium" maxlength="250" />
				<A NAME="CalendarObjectempDate" ID="CalendarObjectempDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectempDate','frmReInsCompanyDetails.empanelDate',document.frmReInsCompanyDetails.empanelDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle"></a>
	    	</td>
        	</tr>
		</table>
	</fieldset>
	<fieldset>
	<legend>Address Details</legend>
		<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
			<tr>
			<td class="formLabel">Address 1: <span class="mandatorySymbol">*</span></td>
	        <td>
	        <html:textarea  property="addressVO.address1" cols="30" rows="2" />
									
		       <%-- <html:text property="address1" styleClass="textBox textBoxSmall" maxlength="10" disabled="<%= viewmode %>"/> --%>
<%-- 		       <html:text property="address1" styleClass="textBox textBoxSmall" maxlength="10" disabled="<%= viewmode %>"/> --%>
	        </td>
	        <td class="formLabel">Address 2:</td>
	        <td>
	          <html:textarea  property="addressVO.address2" cols="30" rows="2"/>
	        	<%-- <html:text property="address2" styleClass="textBox textBoxSmall" maxlength="10" disabled="<%= viewmode %>"/> --%>
	        </td>
	      </tr>
			<tr>
			<td class="formLabel">Address 3:</td>
	        <td>
	        <html:textarea  property="addressVO.address3" cols="30" rows="2"/>
		       <%-- <html:text property="address3" styleClass="textBox textBoxSmall" maxlength="10" disabled="<%= viewmode %>"/> --%>
	        </td>
	        <td class="formLabel">City: <span class="mandatorySymbol">*</span></td>
	        <td>
	        	<%-- <html:text property="addressVO.cityCode" styleClass="textBox textBoxSmall" maxlength="10" /> --%>
	        	<html:select property="addressVO.stateCode" styleId="stateCode" styleClass="selectBox selectBoxMedium" onchange="onChangeState(this)" >
    				<html:option value="">Select from list</html:option>
    				<html:options collection="alStateCode" property="cacheId" labelProperty="cacheDesc"/>
		  		</html:select>
		  		
	        </td>
	      </tr>
			<tr>
			<td class="formLabel">Area: <span class="mandatorySymbol">*</span></td>
	        <td>
		        <html:select property="addressVO.cityCode" styleId="cityCode" styleClass="selectBox selectBoxMedium" >
    				<html:option value="">Select from list</html:option>
    				<html:optionsCollection property="alCityList" label="cacheDesc" value="cacheId"/>
		  		</html:select>
	        </td>
	        <td class="formLabel">PO Box: <span class="mandatorySymbol">*</span></td>
	        <td>
	        	<html:text property="addressVO.pinCode" styleClass="textBox textBoxSmall" maxlength="10" />
	        </td>
	      </tr>
	      <tr>
	        <td class="formLabel">Country: <span class="mandatorySymbol">*</span></td>
	        <td>
		      <html:select property="addressVO.countryCode" styleId="cnCode" styleClass="selectBox selectBoxMedium"  onchange="getStates('state','stateCode')">
		      	<html:option value="">Select from list</html:option>
        		<html:options collection="alCountryCode" property="cacheId" labelProperty="cacheDesc"/>
			  </html:select>
		   </td>
		   
		   
		  
	      </tr>
	      
	      <tr>
	       <td class="formLabel">Phone No1 :<span class="mandatorySymbol">*</span></td>
        			<td>
    				<html:text property="addressVO.isdCode" styleId="isdCode1" styleClass="disabledfieldType" size="3" maxlength="3" readonly="true"/>&nbsp;
    				<html:text property="addressVO.stdCode" styleId="stdCode1" styleClass="disabledfieldType" size="3" maxlength="3" readonly="true"/>&nbsp;
    				
    			
	        		<%-- <logic:empty name="frmInsCompanyDetails" property="addressVO.phoneNo1">
	    			<html:text property="phoneNo1" styleId="phoneNbr1" styleClass="disabledfieldType" maxlength="15" value="Phone No1" onclick="changeMe(this)" onblur="getMe('Phone No1')"/>
    				</logic:empty>
    				
    				<logic:notEmpty name="frmInsCompanyDetails" property="addressVO.phoneNo1"> --%>
	        			<html:text property="addressVO.phoneNo1" styleId="phoneNbr1" styleClass="disabledfieldType"  maxlength="15" /> 
	        			<%-- <html:text property="addressVO.phoneNo1" styleId="phoneNbr1" styleClass="disabledfieldType"  maxlength="15" <%-- onclick="changeMe(this)" onblur="getMe('Phone No1')" /> --%>
	        		<%-- </logic:notEmpty> --%>
	        		</td>
        			
    				<td class="formLabel">Phone No2 :</td>
        			<td>
    				<html:text property="addressVO.isdCode" styleId="isdCode2" styleClass="disabledfieldType" size="3" maxlength="3" readonly="true"/>&nbsp;
    				<html:text property="addressVO.stdCode" styleId="stdCode2" styleClass="disabledfieldType" size="3" maxlength="3" readonly="true"/>&nbsp;
    				
    				
					<%-- <logic:empty name="frmInsCompanyDetails" property="addressVO.phoneNo2">
	    			<html:text property="addressVO.phoneNo2" styleId="phoneNbr2" styleClass="disabledfieldType" maxlength="15" value="Phone No2" onclick="changeMe(this)" onblur="getMe('Phone No2')"/>
    				</logic:empty>
    				
    				<logic:notEmpty name="frmInsCompanyDetails" property="addressVO.phoneNo2"> --%>
	        			<html:text property="addressVO.phoneNo2" styleId="phoneNbr2" styleClass="disabledfieldType"  maxlength="15" /> 
<%-- 	        			<html:text property="addressVO.phoneNo2" styleId="phoneNbr2" styleClass="disabledfieldType"  maxlength="15" <%-- onclick="changeMe(this)" onblur="getMe('Phone No2')" /> --%>
	        		<%-- </logic:notEmpty>  --%>   	
	        		</td>		
      			</tr>
      			</table>
      			</fieldset>
	 <fieldset>
	<legend>Bank Account Information</legend>
		<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
		<tr>
	    	<td class="formLabel">Bank Name:<span class="mandatorySymbol">*</span></td>
	    	<td>
	        <html:text property="bankName" styleClass="textBox textBoxMedium" maxlength="60"/>
        	</td>
        	<td class="formLabel">Branch:<span class="mandatorySymbol">*</span></td>
	    	<td>
				<html:text property="bankBranch" styleClass="textBox textBoxMedium" maxlength="60" />
	    	</td>
        	</tr>
        	
        	<tr>
        	<td class="formLabel">Account Type:<span class="mandatorySymbol">*</span></td>
	    	<td>
				<html:text property="accountType" styleClass="textBox textBoxMedium" maxlength="60" />
	    	</td>
        	<td class="formLabel">Bank Account No.:<span class="mandatorySymbol">*</span></td>
	    	<td>
				<html:text property="bankAccountNo" styleClass="textBox textBoxMedium" maxlength="60" />
	    	</td>
        	</tr>
        	<tr>
        	<td class="formLabel">SWIFT Code:<span class="mandatorySymbol">*</span></td>
	    	<td>
				<html:text property="IBANSwiftCode" styleClass="textBox textBoxMedium" maxlength="60" />
	    	</td>
        	<td class="formLabel">Bank Phone.:<span class="mandatorySymbol">*</span></td>
	    	<td>
				<html:text property="bankPhone" styleClass="textBox textBoxMedium" maxlength="25" />
	    	</td>
        	</tr>
        	
	      <tr>
		  	<td class="formLabel">Bank Address 1:<span class="mandatorySymbol">*</span></td>
	    	<td>
	    	<html:textarea  property="bankAddress1" cols="30" rows="2" />
				<%-- <html:text property="bankAddress1" styleClass="textBox textBoxMedium" maxlength="250" /> --%>
	    	</td>
	    	<td class="formLabel">Bank Address 2:</td>
	    	<td>
	    	<html:textarea  property="bankAddress2" cols="30" rows="2" />
	       <%--  <html:text property="bankAddress2" styleClass="textBox textBoxMedium" maxlength="250"/> --%>
        	</td>
        	</tr>
	      <tr>
	      
	        <td class="formLabel">Bank Address 3:</td>
	    	<td>
	    	<html:textarea  property="bankAddress3" cols="30" rows="2" />
				<%-- <html:text property="bankAddress3" styleClass="textBox textBoxMedium" maxlength="250" /> --%>
	    	</td>
	       
	      </tr>

	    </table>
	</fieldset> 
	
	
	
	
	<!-- E N D : Form Fields -->
    <!-- S T A R T : Buttons -->
	<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="100%" align="center">

    		<button type="button" name="Button" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onSave();"><u>S</u>ave</button>&nbsp;
			<button type="button" name="Button2" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onReset();"><u>R</u>eset</button>&nbsp;
			<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose();"><u>C</u>lose</button>
	    </td>
	  </tr>
	</table>
	</div>
	<!-- E N D : Buttons -->
	<%-- <html:hidden property="officeType"/>
	<html:hidden property="insuranceSeqID"/>
	<input type="hidden" name="child" value="Add/Edit Insurance Company"> --%>
	<input type="hidden" name="mode">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	<input type="hidden" name="child" value="General">
	<input type="hidden" name="focusID" value="">
	
	<%-- <input type="hidden" name="companyStatus" value="">
	<input type="hidden" name="focusID" value="">
	<logic:notEmpty name="frmInsCompanyDetails" property="frmChanged">
		<script> ClientReset=false;TC_PageDataChanged=true;</script>
	</logic:notEmpty> --%>
</html:form>
</logic:empty>
<logic:notEmpty name="ErrorDisplay">
		<html:errors/>
</logic:notEmpty>
<!-- E N D : Content/Form Area -->