<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,com.ttk.common.WebBoardHelper,java.util.Date,java.text.SimpleDateFormat"%>

	<script type="text/javascript" SRC="/ttk/scripts/validation.js"></script>

    <script type="text/javascript" src="/ttk/scripts/insurancepricing/swpricinghome.js"></script>	
     <script type="text/javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script>  
     <script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
	<script>
    var JS_Focus_Disabled =true;
    var JS_Focus_ID="<%=TTKCommon.checkNull(request.getAttribute("focusId"))%>";
    $(document).ready(function(){
    	setInitialStates();
    });  
    /* if (window.attachEvent) {window.attachEvent('onload', setInitialStates());}
    else if (window.addEventListener) {window.addEventListener('load', setInitialStates(), false);}
    else {document.addEventListener('load', setInitialStates(), false);}   */   
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
	pageContext.setAttribute("areaOfCoverList",Cache.getCacheObject("areaOfCoverList"));
	pageContext.setAttribute("networkList", Cache.getCacheObject("networkList"));
	pageContext.setAttribute("maxBenifitList", Cache.getCacheObject("maxBenifitList"));
	pageContext.setAttribute("viewmode",new Boolean(viewmode));	
	pageContext.setAttribute("dentalLimitList", Cache.getCacheObject("dentalLimitList"));
	pageContext.setAttribute("maternityLimitList", Cache.getCacheObject("maternityLimitList"));
	pageContext.setAttribute("opticalLimitList", Cache.getCacheObject("opticalLimitList"));
	pageContext.setAttribute("opCopayList", Cache.getCacheObject("opCopayList"));
	pageContext.setAttribute("opticalCopayList", Cache.getCacheObject("opticalCopayList"));
	pageContext.setAttribute("opCopayDentalList", Cache.getCacheObject("opCopayDentalList"));
	pageContext.setAttribute("opCopayOrthoList", Cache.getCacheObject("opCopayOrthoList"));
	pageContext.setAttribute("opDeductableList", Cache.getCacheObject("opDeductableList"));
	
	pageContext.setAttribute("chronicLimitList", Cache.getCacheObject("chronicLimitList"));
	pageContext.setAttribute("orthodonticsList", Cache.getCacheObject("orthodonticsList"));
	pageContext.setAttribute("opCopaypharmacyList", Cache.getCacheObject("opCopaypharmacyList"));
	pageContext.setAttribute("opInvestigationList", Cache.getCacheObject("opInvestigationList"));
	pageContext.setAttribute("opCopyconsultnList", Cache.getCacheObject("opCopyconsultnList"));
	pageContext.setAttribute("opCopyalahlihospList", Cache.getCacheObject("opCopyalahlihospList"));
	pageContext.setAttribute("opPharmacyAlAhliList", Cache.getCacheObject("opPharmacyAlAhliList"));
	pageContext.setAttribute("opInvestnAlAhliList", Cache.getCacheObject("opInvestnAlAhliList"));
	pageContext.setAttribute("opConsultAlAhliList", Cache.getCacheObject("opConsultAlAhliList"));
	pageContext.setAttribute("opCopyothersList", Cache.getCacheObject("opCopyothersList"));
	pageContext.setAttribute("policycategory", Cache.getCacheObject("policycategory"));
	pageContext.setAttribute("opothersAlAhliList", Cache.getCacheObject("opothersAlAhliList"));
	pageContext.setAttribute("opticalFrameLimitList", Cache.getCacheObject("opticalFrameLimitList"));
	pageContext.setAttribute("ipCopayList", Cache.getCacheObject("ipCopayList"));
	pageContext.setAttribute("ipCopayAtAlAhliList", Cache.getCacheObject("ipCopayAtAlAhliList"));
	pageContext.setAttribute("maternityCopayLimitList", Cache.getCacheObject("maternityCopayLimitList"));
	boolean network=false;
%>

<html:form action="/SwInsPricingActionWeb.do" method="post" enctype="multipart/form-data"> 
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		  <tr>
		    <td width="65%"></td> 
		  </tr>
		  	<tr>
	    	 <td><bean:write name="frmSwPricing" property="caption"/></td>
			 <td align="right" class="webBoard">
			 		<logic:notEmpty name="frmSwPricing" property="groupProfileSeqID">
			 		<%@ include file="/ttk/common/toolbar.jsp" %>
			 		</logic:notEmpty >
			 </td>
		</tr>	  
		  
	</table>
	
	
	<%--  <logic:notEmpty name="fetchErrorData" scope="request">
			 <script>
				alert("<bean:write name="fetchErrorData" scope="request"/>");
			</script> 
	</logic:notEmpty>  --%>
		
	<div class="contentArea" id="contentArea">
	<html:errors/>
		<logic:notEmpty name="frmSwPricing"	property="alertmsgscreen1">
				<table align="center" class="errorContainer" border="0"	cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/warning.gif" title="Warning" alt="Warning"
							width="16" height="16" align="absmiddle">&nbsp; <bean:write
								name="frmSwPricing" property="alertmsgscreen1" /></td>
					</tr>
				</table>
			</logic:notEmpty>
			
			<logic:notEmpty name="frmSwPricing"	property="pricingNumberAlert">
				<table align="center" class="errorContainer" border="0"	cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/warning.gif" title="Warning" alt="Warning"
							width="16" height="16" align="absmiddle">&nbsp; <bean:write
								name="frmSwPricing" property="pricingNumberAlert" /></td>
					</tr>
				</table>
			</logic:notEmpty>
				
	<logic:notEmpty name="errorMsg" scope="request">
    <table align="center" class="errorContainer"  border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><img src="/ttk/images/ErrorIcon.gif" title="Error" alt="Error" width="16" height="16" align="middle" >&nbsp;
          <bean:write name="errorMsg" scope="request" />
          </td>
      </tr>
    </table>
   </logic:notEmpty>
	
	<!-- S T A R T : Success Box -->

		<logic:notEmpty name="updated" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="middle">&nbsp;
						<bean:message name="updated" scope="request"/>
				  </td>
				</tr>
			</table>
		</logic:notEmpty>
		<logic:notEmpty name="successMsg" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="middle">&nbsp;
						<bean:write name="successMsg" scope="request"/>
				  </td>
				</tr>
			</table>
		</logic:notEmpty> 	
		
    <!-- S T A R T : Form Fields -->
	
	<fieldset>
			<legend>Corporate details</legend>
			<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
			
				   <tr>
			       <td class="formLabel" width="30%">New client/renewal :<span class="mandatorySymbol">*</span></td>  <!-- onchange="renewalChange();" -->
			      <td class="textLabel">
				       <html:select name="frmSwPricing" property="renewalYN"  styleClass="selectBox selectBoxMedium"  onchange="checkPolicyStatus(this);">
							  <html:option value="">Select from list</html:option>
							   <html:option value="N">New Client</html:option>
							  <html:option value="Y">Renewal</html:option>
						 </html:select>
			      </td>	
			      <td class="formLabel"  width="30%"> Pricing reference number :</td>
			      <td class="formLabelspecial"><b> <bean:write name="frmSwPricing" property="pricingRefno"/> </b><html:hidden property="pricingRefno" /></td>
			      </tr>
			   <tr> 
			      
			      <td class="formLabel" width="30%">Client code :<span class="mandatorySymbol">*</span></td>
			      <td class="textLabel">
			     <html:text name="frmSwPricing" styleId ="clientcodeId" property="clientCode" readonly="true" styleClass="textBox textBoxSmall" />
			      <html:link  href="javascript:changeCorporate();"  ><img src="/ttk/images/EditIcon.gif" title="Change Corporate" alt="Change Corporate"  width="16" height="16" border="0" align="absmiddle"></html:link>
			      </td>	
			      
			       <td class="formLabel" width="30%">Client name :</td>
			      <td class="textLabel">
			     <html:text name="frmSwPricing"  property="clientName" readonly="true" styleClass="textBox textBoxLarge" />
			      </td>	
			      
			  </tr>

		<tr>
		<!-- 	Start      -->			   
		
	    <td class="formLabel"  width="30%">Past policy number:
	    <logic:match name="frmSwPricing" property="renewalYN" value="Y">
	    <span class="mandatorySymbol">*</span>
	    </logic:match>
	    </td>
			      
			       <logic:empty  property="clientCode" name="frmSwPricing" >
				      <td  width="30%" class="textLabel">		      
		 				<html:select property="previousPolicyNo"  styleClass="selectBox selectBoxMedium" onchange="checkPolicyNo(this);"  disabled="<%= viewmode %>">
							<html:option value="">Select from list</html:option>
			  				 <html:optionsCollection property="alpreviousPolicyNo" label="cacheDesc" value="cacheId" />
				    	</html:select>
				      </td>	
			     </logic:empty>
			     
			      <logic:notEmpty  property="clientCode" name="frmSwPricing" >
				      <td  width="30%" class="textLabel">		      
		   				<html:select property="previousPolicyNo"  styleClass="selectBox selectBoxMedium" onchange="checkPolicyNo(this);" disabled="<%= viewmode %>">
							<html:option value="">Select from list</html:option>
							<html:optionsCollection property="alpreviousPolicyNo" label="cacheDesc" value="cacheId" />
				    	</html:select>
    				 </td>	
			     </logic:notEmpty>
			     
<!-- 	end      -->			   


			       <td class="formLabel"  width="30%">Policy category:</td>
			      <td  width="30%" class="textLabel">		      
			   <%--   <html:text name="frmSwPricing"  property="policycategory" readonly="" styleClass="textBox textBoxLarge"  /> --%>
			     <bean:write name="frmSwPricing" property="policycategory"/> </b><html:hidden property="pricingRefno" />
			      </td>	
		</tr>

			  
			  
			    <tr>
			   
			      <td class="formLabel" width="30%" style="display:none;">Total covered lives :<span class="mandatorySymbol">*</span></td>
			      <td class="textLabel" style="display:none;">
				    	 <html:text name="frmSwPricing" property="totalCovedLives"  onkeyup="isNumericOnly(this);" onchange="validateLives(this);gettotallivesType();"  styleClass="textBox textBoxLarge" />

			      </td>
			      <td></td>
			      <td></td><td></td>
			      <td>
			      	 <button type="button" name="Button" accesskey="a" class="buttonsPricingLarge" onMouseout="this.className='buttonsPricingLarge'" onMouseover="this.className='buttonsPricingLarge buttonsHover'"	onClick="fetchScreen1();">Fetch Key Coverages</button>&nbsp;
			      </td>
			    </tr>
			    
			    </table>
			   </fieldset><br>
			   <fieldset><legend>Key coverages</legend>
			   	<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
			   <tr>
			   <td class="formLabel"  width="30%">Coverage start date :<span class="mandatorySymbol">*</span></td>
			      <td  width="30%" class="textLabel">	
			      <html:text property="coverStartDate" styleId="coverStartDate" onfocus="javascript:warnFutureDate();" onchange="javascript:warnFutureDate();" name="frmSwPricing" styleClass="textBox textDate" maxlength="10"/>
			      <A NAME="coverStartDate" ID="coverStartDate" HREF="#" onClick="javascript:show_calendar('coverStartDate','frmSwPricing.coverStartDate',document.frmSwPricing.coverStartDate.value,'',event,148,178,document.frmSwPricing.coverEndDate.value,'');return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"  width="24" height="17" border="0" align="absmiddle"></a>
			       </td>
			      	
			     <td class="formLabel" width="30%">Coverage end date :<span class="mandatorySymbol">*</span></td>
			      <td class="textLabel">
				    <html:text property="coverEndDate" name="frmSwPricing" styleClass="textBox textDate" maxlength="10" disabled="" readonly=""/>
			      <A NAME="coverEndDate" ID="coverEndDate" HREF="#" onClick="javascript:show_calendar('coverEndDate','frmSwPricing.coverEndDate',document.frmSwPricing.coverEndDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"  width="24" height="17" border="0" align="absmiddle"></a>
			      
			      </td>
			     </tr>
			   <tr>
			   
			       <td class="formLabel" width="30%">Inpatient(IP) :<span class="mandatorySymbol" >*</span></td>
			      <td class="textLabel">
					     <html:select name="frmSwPricing" property="inpatientBenefit" styleId="inpatientBenefit"  styleClass="selectBox selectBoxMedium" onchange="onchangeInpatient(this,'N')" ><%-- onchange="onchangeInpatient(this,'N')" --%>
							  <html:option value="">Select from list</html:option>
						 	  <html:option value="Y">Covered</html:option>
							  <html:option value="N">Not covered</html:option>
					  </html:select>
			      </td>	
			  
			  <td class="formLabel" width="30%">Outpatient(OP) :<span class="mandatorySymbol" >*</span></td>
			      <td class="textLabel">
								  
					    <html:select name="frmSwPricing" property="outpatientBenefit" styleId="outpatientBenefit" onchange="onchangeOutpatient(this,'N');" styleClass="selectBox selectBoxMedium"  >
							  <html:option value="">Select from list</html:option>
						 	  <html:option value="Y">Covered</html:option>
							  <html:option value="N">Not covered</html:option>
					  </html:select>
			      </td>	
			     </tr>
			   
			   
			    <tr>
			   
			       <td class="formLabel" width="30%">Maximum benefit limit (IP/OP) :<span class="mandatorySymbol">*</span></td>
			      <td class="textLabel">
								  
					   <html:select name="frmSwPricing" property="maxBenifitList" styleId="maxBenefitList" onchange="onchangeMaxBenefitType(this)" styleClass="selectBox selectBoxMedium">	
				      	<html:option value="">Select From List</html:option>						
							<html:optionsCollection name="maxBenifitList" value="cacheId" label="cacheDesc"/>
					  </html:select>
			      </td>	
			  
			 	 <logic:match name="frmSwPricing" property="maxBenifitList" value="45">
			       <td class="formLabel" width="30%">Maximum benefit limit (Others) :</td>
			      <td class="textLabel">
				   		<html:text name="frmSwPricing" readonly="" onkeyup="isNumericOnly(this); checkValue(this);" styleId ="maxBeneLimitOth" property="maxBeneLimitOth" styleClass="textBox textBoxLarge" /><br>
			      		
			      </td>
			      </logic:match>
			      
			       <logic:notMatch name="frmSwPricing" property="maxBenifitList" value="45">
			       <td class="formLabel" width="30%">Maximum benefit limit (Others) :</td>
			      <td class="textLabel">
				   		<html:text name="frmSwPricing" readonly="true" onkeyup="isNumericOnly(this); checkValue(this);" styleId ="maxBeneLimitOth" property="maxBeneLimitOth" styleClass="textBox textBoxLarge textBoxDisabled" /><br>
				   		
			      </td>
			      </logic:notMatch>
			     
			     </tr>
			     
			     <tr>
			       <td class="formLabel" width="30%">Area of cover :<span class="mandatorySymbol otherspan">*</span></td>
			      <td class="textLabel">
								  
					   <html:select name="frmSwPricing" property="areaOfCoverList" styleId="areaOfCoverList" styleClass="selectBox selectBoxMedium">	
				      	<html:option value="">Select from list</html:option>						
							<html:optionsCollection name="areaOfCoverList" value="cacheId" label="cacheDesc"/>
					  </html:select>
			      </td>	
			   
			       <td class="formLabel" width="30%">Network :<span class="mandatorySymbol otherspan">*</span></td>
			      <td class="textLabel">
								  
					   <html:select name="frmSwPricing" property="networkList" styleId="networkList" styleClass="selectBox selectBoxMedium" onchange="checkNetwork(this);">	
				      	<html:option value="">Select from list</html:option>						
							<html:optionsCollection name="networkList" value="cacheId" label="cacheDesc"/>
					  </html:select>
			      </td>	
			      </tr>
			      <tr> <td colspan="5" >&nbsp;</td> </tr>
			             <tr>
			      <td class="formLabel"  width="30%">OP copay/deductible applicable for? :<span class="mandatorySymbol spaniscopayOP1">*</span></td>
			      <td class="textLabel">
					   <html:select name="frmSwPricing" property="opdeductableserviceYN" onchange="onOPservices(this);"  styleClass="selectBox selectBoxMedium">	
						 <html:option value="">Select from list</html:option>
						 	  <html:option value="Y">All OP Services</html:option>
							  <html:option value="N">Part of OP Services</html:option>
					  </html:select>
			      </td>	
			      
			      <td class="formLabel"  width="30%">IP Copay :<span class="mandatorySymbol spaniscopayIP1">*</span></td>
			      <td class="textLabel">
					   <html:select name="frmSwPricing" property="ipCopay" onchange="onIpCopay(this);" styleId="ipCopayId" styleClass="selectBox selectBoxMedium">	
						 <html:option value="">Select from list</html:option>
						 	  <html:optionsCollection name="ipCopayList" value="cacheId" label="cacheDesc"/>
					  </html:select>
					  <html:hidden property="ipCopayPerc" name="frmSwPricing" />
			      </td>	
			      
			      </tr>
			         <tr>
			       <td class="formLabel" width="30%">OP copay :<span class="mandatorySymbol spaniscopayOP2" >*</span></td>
			      <td class="textLabel">
					   <html:select name="frmSwPricing" property="opCopayList" styleId="opCopayList" onchange="onOPCopay(this);" styleClass="selectBox selectBoxMedium">	
				      	<html:option value="">Select from list</html:option>						
							<html:optionsCollection name="opCopayList" value="cacheId" label="cacheDesc"/>
					  </html:select>
					  <html:hidden property="opCopayListDesc" name="frmSwPricing" />
			      </td>	
			  
			       <td class="formLabel" width="30%">OP deductible :<span class="mandatorySymbol spaniscopayOP2">*</span></td>
			      <td class="textLabel">
								  
					   <html:select name="frmSwPricing" property="opDeductableList" styleId="opDeductableList"  styleClass="selectBox selectBoxMedium">	
				      	<html:option value="">Select from list</html:option>						
							<html:optionsCollection name="opDeductableList" value="cacheId" label="cacheDesc"/>
					  </html:select>
				   <html:hidden property="opDeductableListDesc" name="frmSwPricing" />
			      </td>	
			        </tr>       
			
			  <tr>
			          <td class="formLabel" width="30%">OP pharmacy copay :<span class="mandatorySymbol spanOpservices">*</span></td>
			      <td class="textLabel">
								  
					   <html:select name="frmSwPricing" property="opCopaypharmacy" styleId="opCopaypharmacy" styleClass="selectBox selectBoxMedium" onchange="checkCopay(this);">	
				      	<html:option value="">Not applicable</html:option>						
							<html:optionsCollection name="opCopaypharmacyList" value="cacheId" label="cacheDesc"/>
					  </html:select>
			      </td>	
			      
			       <td class="formLabel" width="30%">OP investigation copay :<span class="mandatorySymbol spanOpservices">*</span></td>
			      <td class="textLabel">
								  
					   <html:select name="frmSwPricing" property="opInvestigation"  styleId="opInvestigation" styleClass="selectBox selectBoxMedium" onchange="checkCopay(this);">	
				      	<html:option value="">Not applicable</html:option>						
							<html:optionsCollection name="opInvestigationList" value="cacheId" label="cacheDesc"/>
					  </html:select>
			      </td>	
			      
			        </tr>
			        
			        
			         <tr>
			          <td class="formLabel" width="30%">OP Consultation copay / deductible :<span class="mandatorySymbol spanOpservices">*</span></td>
			      <td class="textLabel">
								  
					   <html:select name="frmSwPricing" property="opCopyconsultn"  styleId="opCopyconsultn" styleClass="selectBox selectBoxMedium" onchange="checkCopay(this);">	
				      	<html:option value="">Not applicable</html:option>						
							<html:optionsCollection name="opCopyconsultnList" value="cacheId" label="cacheDesc"/>
					  </html:select>
			      </td>	
			      
			       <td class="formLabel" width="30%">OP other services copay :<span class="mandatorySymbol spanOpservices">*</span></td>
			      <td class="textLabel">
								  
					   <html:select name="frmSwPricing" property="opCopyothers" styleId="opCopyothers"  styleClass="selectBox selectBoxMedium" onchange="checkCopay(this);">	
				      	<html:option value="">Not applicable</html:option>						
							<html:optionsCollection name="opCopyothersList"  value="cacheId" label="cacheDesc"/>
					  </html:select>
			      </td>	
			      </tr>
			         <tr> <td colspan="5" >&nbsp;</td> </tr>
			        
			         <tr>
			       <td class="formLabel" width="30%">Al Ahli hospital :<span class="mandatorySymbol otherspan">*</span></td>   <!-- setSelectModeType(); -->
			      <td class="textLabel">
				       <html:select name="frmSwPricing" property="alAhlihospital" styleId="alAhlihospital" onchange="onAhliHospChange(this);" styleClass="selectBox selectBoxMedium"  >
							  <html:option value="">Select from list</html:option>
						 	  <html:option value="Y">Covered</html:option>
							  <html:option value="N">Not covered</html:option>
					  </html:select>
			      </td>	
			      <td class="formLabel" width="30%">IP Copay at Al Ahli :<span class="mandatorySymbol ipCopayAtAlAhli">*</span></td>   <!-- setSelectModeType(); -->
			      <td class="textLabel">
				       <html:select name="frmSwPricing" property="ipCopayAtAlAhli" styleId="ipCopayAtAlAhliId" onchange="onIpCopayAtAlAhli(this);" styleClass="selectBox selectBoxMedium"  >
							 <html:option value="">Select from list</html:option>	
							 <html:optionsCollection name="ipCopayAtAlAhliList" value="cacheId" label="cacheDesc"/>
					  </html:select>
					  <html:hidden property="ipCopayAtAlAhliPerc" name="frmSwPricing" />
			      </td>	
			      </tr>
			      <tr>
			      <td class="formLabel" width="30%">OP copay at Al Ahli applicable for? :<span class="mandatorySymbol spaniscopayhosp">*</span></td>  
			      <td class="textLabel">
				       <html:select name="frmSwPricing" property="alAhlihospOPservices"  styleId="alAhlihospOPservices" onchange="onOPCopayAhliHosp(this);" styleClass="selectBox selectBoxMedium"  >
							  <html:option value="">Select from list</html:option>
						 	 <html:option value="Y">All OP Services</html:option>
							 <html:option value="N">Part of OP Services</html:option>
					  </html:select>
			      </td>	
			     
			      <td class="formLabel" width="30%">OP copay at Al Ahli :<span class="mandatorySymbol spancopayhosp">*</span></td>
			      <td class="textLabel">
								  
					   <html:select name="frmSwPricing" property="opCopyalahlihosp"  styleClass="selectBox selectBoxMedium">	
				      	<html:option value="">Not applicable</html:option>						
							<html:optionsCollection name="opCopyalahlihospList" value="cacheId" label="cacheDesc"/>
					  </html:select>
				  
			      </td>	
			      </tr>
			      
			     <tr>
			       <td class="formLabel" width="30%">OP pharmacy copay at Al Ahli :<span class="mandatorySymbol spanhospital3">*</span></td>
			      <td class="textLabel">
								  
					   <html:select name="frmSwPricing" property="opPharmacyAlAhli"  styleClass="selectBox selectBoxMedium">	
				      	<html:option value="">Not applicable</html:option>						
							<html:optionsCollection name="opPharmacyAlAhliList" value="cacheId" label="cacheDesc"/>
					  </html:select>
				  
			      </td>	
			         <td class="formLabel" width="30%">OP investigation copay at Al Ahli :<span class="mandatorySymbol spanhospital3">*</span></td>
			      <td class="textLabel">
								  
					   <html:select name="frmSwPricing" property="opInvestnAlAhli"  styleClass="selectBox selectBoxMedium">	
				      	<html:option value="">Not applicable</html:option>						
							<html:optionsCollection name="opInvestnAlAhliList" value="cacheId" label="cacheDesc"/>
					  </html:select>
				  
			      </td>	
			      
			      </tr> 
			      <tr>
			      
			      <td class="formLabel" width="30%">OP consultation copay at Al Ahli :<span class="mandatorySymbol spanhospital3">*</span></td>
			      <td class="textLabel">
								  
					   <html:select name="frmSwPricing" property="opConsultAlAhli"  styleClass="selectBox selectBoxMedium">	
				      	<html:option value="">Not applicable</html:option>						
							<html:optionsCollection name="opConsultAlAhliList" value="cacheId" label="cacheDesc"/>
					  </html:select>
				  
			      </td>	
			      
			       <td class="formLabel" width="30%">OP other services copay at Al Ahli :<span class="mandatorySymbol spanhospital3">*</span></td>
			      <td class="textLabel">
								  
					   <html:select name="frmSwPricing" property="opothersAlAhli"   styleClass="selectBox selectBoxMedium">	
				      	<html:option value="">Not applicable</html:option>						
							<html:optionsCollection name="opothersAlAhliList" value="cacheId" label="cacheDesc"/>
					  </html:select>
				  
			      </td>	
			      
			      </tr> 
			      
			      			        
			        <tr> <td colspan="5" >&nbsp;</td> </tr>
			       
			         <tr>
			       <td class="formLabel" width="30%">Optical :<span class="mandatorySymbol spanOptical1">*</span></td>   <!-- setSelectModeType(); -->
			      <td class="textLabel">
				       <html:select name="frmSwPricing" property="opticalYN" styleId="opticalYNid"   onchange="onOpticalChange(this);" styleClass="selectBox selectBoxMedium"  >
							  <html:option value="">Select from list</html:option>
						 	  <html:option value="Y">Covered</html:option>
							  <html:option value="N">Not covered</html:option>
					  </html:select>
			      </td>	
			   
		   		 
			       <td class="formLabel" width="30%">Is optical covered for all lives :<span class="mandatorySymbol spanOptical2">*</span></td>
			      <td class="textLabel">
				       <html:select name="frmSwPricing" property="opticalLivesYN" styleId="opticalLivesYN"   readonly="true"  styleClass="selectBox selectBoxMedium"  >
							 <html:option value="">Select from list</html:option>
							  <html:option value="Y">Yes</html:option>
							  <html:option value="N">No</html:option>
					  </html:select>
			      </td>	
			  
			  </tr>
			     <tr>
			  
			       <td class="formLabel" width="30%">Optical limit :<span class="mandatorySymbol spanOptical2">*</span></td>
			      <td class="textLabel">
					   <html:select name="frmSwPricing" property="opticalLimitList"  styleId="opticalLimitList"  onchange="onchangeOpticalType(this)" styleClass="selectBox selectBoxMedium">	
				      	<html:option value="">Not applicable</html:option>						
							<html:optionsCollection name="opticalLimitList" value="cacheId" label="cacheDesc"/>
					   </html:select>
			      </td>	
			   		
			   		 <logic:match name="frmSwPricing" property="opticalLimitList" value="45">
			    
			      		<td class="formLabel" width="30%">Optical limit (Others) :</td>
				   		<td class="textLabel">
							<html:text name="frmSwPricing" readonly="" onkeyup="isNumericOnly(this); checkValue(this);" styleId="opticalLimitOth" property="opticalLimitOth" styleClass="textBox textBoxLarge" /><br>
							
			      </td>
			      </logic:match>
			      	<logic:notMatch name="frmSwPricing" property="opticalLimitList" value="45">
			      	  <td class="formLabel" width="30%">Optical limit (Others) :</td>
			      	 <td class="textLabel">
				   		<html:text name="frmSwPricing" readonly="true"  onkeyup="isNumericOnly(this); checkValue(this);" styleId="opticalLimitOth"  property="opticalLimitOth" styleClass="textBox textBoxLarge textBoxDisabled" /><br>  <!-- onkeyup="isNumericOnly(this);" -->
			      	</td>
			      </logic:notMatch>
			        </tr>
			  
			    <tr>
			    <td class="formLabel" width="30%">Optical copay :<span class="mandatorySymbol spanOptical2">*</span></td>
			      <td class="textLabel">
				     <%--  <logic:match name="frmSwPricing" property="opticalYN" value="N">  --%>
				       <html:select name="frmSwPricing" property="opticalCopayList"  styleId="opticalCopayList"  styleClass="selectBox selectBoxMedium"  >
							  <html:option value="">Not applicable</html:option>
							  <html:optionsCollection name="opticalCopayList" value="cacheId" label="cacheDesc"/>
						 </html:select>
						<%--  </logic:match>
						  <logic:notMatch name="frmSwPricing" property="opticalYN" value="N">
						  <html:select name="frmSwPricing" property="opticalCopayList" styleId="opticalCopayList"  disabled="" styleClass="selectBox selectBoxMedium"  >
							  <html:option value="">Not applicable</html:option>
							  <html:optionsCollection name="opCopayList" value="cacheId" label="cacheDesc"/>
						 </html:select>
						 </logic:notMatch> --%>
						 
						 
					 <html:hidden property="opticalCopayListDesc" name="frmSwPricing" />
			      </td>	
			      <td class="formLabel" width="30%">Frames limit :<span class="mandatorySymbol spanOptical2">*</span></td>
			      <td class="textLabel">
				       <html:select name="frmSwPricing" property="opticalFrameLimitList"  styleId="opticalFrameLimitList"  styleClass="selectBox selectBoxMedium"  >
							  <html:option value="">Not applicable</html:option>
							  <html:optionsCollection name="opticalFrameLimitList" value="cacheId" label="cacheDesc"/>
						 </html:select>
					 
			      </td>
			        </tr>
			          <tr> <td colspan="5" >&nbsp;</td> </tr>
			         <tr>
			       <td class="formLabel" width="30%">Dental :<span class="mandatorySymbol spanDental1">*</span></td>
			      <td class="textLabel">
				       <html:select name="frmSwPricing" property="dentalYN" styleId="dentalYNid"   onchange="onDentalChange(this);" styleClass="selectBox selectBoxMedium"  >
							  <html:option value="">Select from list</html:option>
						 	  <html:option value="Y">Covered</html:option>
							  <html:option value="N">Not covered</html:option>
					  </html:select>
			      </td>	
			   
			    	
			       <td class="formLabel" width="30%">Is dental covered for all lives :<span class="mandatorySymbol spanDental1">*</span></td>
			      <td class="textLabel">
				       <html:select name="frmSwPricing" property="dentalLivesYN"  styleId="dentalLivesYN" readonly="true"  styleClass="selectBox selectBoxMedium"  >
							  <html:option value="">Select from list</html:option>
							   <html:option value="Y">Yes</html:option>
							  <html:option  value="N">No</html:option>
			        </html:select>
					  
			      </td>	
			   
			      
			 </tr>
			 
			   <tr>
			       <td class="formLabel" width="30%">Dental limit :<span class="mandatorySymbol spanDental2">*</span></td>
			      <td class="textLabel">
					   <html:select name="frmSwPricing" property="dentalLimitList" styleId="dentalLimitList"   onchange="onchangeDentalType(this)" styleClass="selectBox selectBoxMedium">	
				      	  <html:option value="">Not applicable</html:option>						
							<html:optionsCollection name="dentalLimitList" value="cacheId" label="cacheDesc"/>
					      </html:select>
			      </td>	
			      
			   
			  
	          <logic:match name="frmSwPricing" property="dentalLimitList" value="45"> 
			       <td class="formLabel" width="30%">Dental limit (Others) :</td>
			      <td class="textLabel">
				   		<html:text name="frmSwPricing" readonly="" onkeyup="isNumericOnly(this); checkValue(this);"  property="dentalLimitOth" styleId="dentalLimitOth" styleClass="textBox textBoxLarge" /><br>
				   		
			      </td>
			      </logic:match>
			      	 <logic:notMatch name="frmSwPricing" property="dentalLimitList" value="45">
			      	      <td class="formLabel" width="30%">Dental limit (Others) :</td>
			      		  <td class="textLabel">
				   		  <html:text name="frmSwPricing" readonly="true" onkeyup="isNumericOnly(this); checkValue(this);"  property="dentalLimitOth" styleId="dentalLimitOth" styleClass="textBox textBoxLarge textBoxDisabled" /><br>
				   		  
			      		  </td>
			      	 </logic:notMatch>   
			      
			    </tr>
			    
			      <tr>
			 
			       <td class="formLabel" width="30%">Dental copay/deductible :<span class="mandatorySymbol spanDental2">*</span></td>
			      <td class="textLabel">
				       <html:select name="frmSwPricing" property="dentalcopayList"  styleId="dentalcopayList"    styleClass="selectBox selectBoxMedium"  >
							  <html:option value="">Not applicable</html:option>
							<html:optionsCollection name="opCopayDentalList" value="cacheId" label="cacheDesc"/>
					  </html:select>
					   <html:hidden property="dentalcopayListDesc" name="frmSwPricing" />
			      </td>	
			      
			      <td class="formLabel" width="30%">Orthodontics :<span class="mandatorySymbol spanDental2">*</span></td>
			      <td class="textLabel">
				       <html:select name="frmSwPricing" property="orthodonticsCopay"  styleId="orthodonticsCopay"  styleClass="selectBox selectBoxMedium"  >
							  <html:option value="">Not applicable</html:option>
							<html:optionsCollection name="orthodonticsList" value="cacheId" label="cacheDesc"/>
					  </html:select>
					 
			      </td>	
		      
			      
			  </tr>
			 
			  <tr> <td colspan="5" >&nbsp;</td> </tr>
			        
			
			    <tr>
			       <td class="formLabel" width="30%">Maternity :<span class="mandatorySymbol spanMeternity1">*</span></td>
			      <td class="textLabel">
				       <html:select name="frmSwPricing" property="maternityYN" styleId="maternityYNId" onchange="onMaternityChange(this)" styleClass="selectBox selectBoxMedium"  >
							  <html:option value="">Select from list</html:option>
							  <html:option value="Y">Covered</html:option>
							  <html:option value="N">Not covered</html:option>
					  </html:select>
			      </td>	
			      
			      <td class="formLabel" width="30%">Maternity Copay :<span class="mandatorySymbol spanMaternityCopay">*</span></td>
			      <td class="textLabel">
						   <html:select name="frmSwPricing" property="maternityCopay" styleId="maternityCopay"  styleClass="selectBox selectBoxMedium" onchange="onMatCopay(this);" >	
					      	<html:option value="">Not applicable</html:option>						
								<html:optionsCollection name="maternityCopayLimitList" value="cacheId" label="cacheDesc"/>
						  </html:select>
						   <html:hidden property="maternityCopayPerc" name="frmSwPricing" styleId="maternityCopayPerc"/>
			      </td>	
			      
			      
			    <logic:match name="frmSwPricing" property="maternityYN" value="Y">  
			      <td class="formLabel" width="30%" style="display:none;">Total covered lives eligible for maternity :<span class="mandatorySymbol spanMeternity2">*</span></td> <!--  onchange="validateLives(this)" -->
			      <td class="textLabel">
				  		<html:text name="frmSwPricing" property="totalLivesMaternity" styleId="totalLivesMaternity" readonly="" onchange="validateLives(this)" onkeyup="isNumericOnly(this);" styleClass="textBox textBoxLarge" style="display:none;" />

			      </td>	
			      </logic:match>
			       <logic:notMatch name="frmSwPricing" property="maternityYN" value="Y">  
			      <td class="formLabel" width="30%" style="display:none;">Total covered lives eligible for maternity :<span class="mandatorySymbol spanMeternity2">*</span></td> <!--  onchange="validateLives(this)" -->
			      <td class="textLabel">
				  		<html:text name="frmSwPricing" property="totalLivesMaternity" styleId="totalLivesMaternity" readonly="true" onchange="validateLives(this)" onkeyup="isNumericOnly(this);" styleClass="textBox textBoxLarge textBoxDisabled" style="display:none;"/>

			      </td>	
				</logic:notMatch>	
						    
			  </tr>
			    <tr>
			       <td class="formLabel" width="30%">Maternity limit :<span class="mandatorySymbol spanMeternity2">*</span></td>
			      <td class="textLabel">
						   <html:select name="frmSwPricing" property="maternityLimitList" styleId="maternityLimitList"   onchange="onchangeMaternityType(this)" styleClass="selectBox selectBoxMedium">	
					      	<html:option value="">Not applicable</html:option>						
								<html:optionsCollection name="maternityLimitList" value="cacheId" label="cacheDesc"/>
						  </html:select>
			      </td>	
			      
				   <logic:match name="frmSwPricing" property="maternityLimitList" value="45">
			       <td class="formLabel" width="30%">Maternity limit (Others) :</td>
			      <td class="textLabel">
				   		<html:text name="frmSwPricing" readonly="" onkeyup="isNumericOnly(this); checkValue(this);" property="maternityLimitOth" styleId="maternityLimitOth" styleClass="textBox textBoxLarge" /><br>
				   		
			      </td>
			      </logic:match>
			       <logic:notMatch name="frmSwPricing" property="maternityLimitList" value="45">
			        <td class="formLabel" width="30%">Maternity limit (Others) :</td>
			      <td class="textLabel">
				   		<html:text name="frmSwPricing" readonly="true" onkeyup="isNumericOnly(this); checkValue(this);" property="maternityLimitOth" styleId="maternityLimitOth" styleClass="textBox textBoxLarge textBoxDisabled" />
				   		
			      </td>
			      </logic:notMatch> 
			        </tr>
			    
			  
			   <tr> <td colspan="5" >&nbsp;</td> </tr>
			    
			     <tr>
			 
			       <td class="formLabel" width="30%">Physiotherapy :<span class="mandatorySymbol otherspan">*</span></td>
			      <td class="textLabel">
				       <html:select name="frmSwPricing" property="physiocoverage" styleId="physiocoverage"  styleClass="selectBox selectBoxMedium"  >
							  <html:option value="">Select from list</html:option>
							   <html:option value="Y">Covered</html:option>
							  <html:option value="N">Not covered</html:option>
						 </html:select>
			      </td>	
			 
			       <td class="formLabel" width="30%">Vitamin D :<span class="mandatorySymbol otherspan">*</span></td>
			      <td class="textLabel">
				       <html:select name="frmSwPricing" property="vitDcoverage" styleId="vitDcoverage"  styleClass="selectBox selectBoxMedium"  >
							  <html:option value="">Select from list</html:option>
							   <html:option value="Y">Covered</html:option>
							  <html:option value="N">Not covered</html:option>
						 </html:select>
			      </td>	
			        </tr>
			    
			     <tr>
			   
			       <td class="formLabel" width="30%">Vaccinations & Immunizations :<span class="mandatorySymbol otherspan">*</span></td>
			      <td class="textLabel">
				       <html:select name="frmSwPricing" property="vaccImmuCoverage" styleId="vaccImmuCoverage"  styleClass="selectBox selectBoxMedium"  >
							  <html:option value="">Select from list</html:option>
							    <html:option value="Y">Covered</html:option>
							  <html:option value="N">Not covered</html:option>
						 </html:select>
			      </td>	
			  
			    
			       <td class="formLabel" width="30%">Maternity Complications :<span class="mandatorySymbol otherspan">*</span></td>
			      <td class="textLabel">
				       <html:select name="frmSwPricing" property="matComplicationCoverage" styleId="matComplicationCoverage"  styleClass="selectBox selectBoxMedium"  >
							  <html:option value="">Select from list</html:option>
							    <html:option value="Y">Covered</html:option>
							  <html:option value="N">Not covered</html:option>
						 </html:select>
			      </td>	
			  </tr>
			  <tr>
			       <td class="formLabel" width="30%">Psychiatry :<span class="mandatorySymbol otherspan">*</span></td>
			      <td class="textLabel">
				       <html:select name="frmSwPricing" property="psychiatrycoverage" styleId="psychiatrycoverage"  styleClass="selectBox selectBoxMedium"  >
							  <html:option value="">Select from list</html:option>
							    <html:option value="Y">Covered</html:option>
							  <html:option value="N">Not covered</html:option>
						 </html:select>
			      </td>	
			    
			       <td class="formLabel" width="30%">Deviated Nasal Septum :<span class="mandatorySymbol otherspan">*</span></td>
			      <td class="textLabel">
				       <html:select name="frmSwPricing" property="deviatedNasalSeptum" styleId="deviatedNasalSeptum"  styleClass="selectBox selectBoxMedium"  >
							  <html:option value="">Select from list</html:option>
							    <html:option value="Y">Covered</html:option>
							  <html:option value="N">Not covered</html:option>
						 </html:select>
			      </td>	
			 </tr>
			 <tr>
			     <td class="formLabel" width="30%">Obesity/ Bariatric surgeries :<span class="mandatorySymbol otherspan">*</span></td> <!-- onchange="onObesityChange(this);" -->
			      <td class="textLabel">
				       <html:select name="frmSwPricing" property="obesityTreatment"   styleId="obesityid"  styleClass="selectBox selectBoxMedium"  >
							  <html:option value="">Select from list</html:option>
							    <html:option value="Y">Covered</html:option>
							  <html:option value="N">Not covered</html:option>
						 </html:select>
			      </td>	
			      
			         
			    <%--    <td class="formLabel" width="30%">Gastric Binding & Gastric Sleeve :<span class="mandatorySymbol spanObesity">*</span></td>
			      <td class="textLabel">
				       <html:select name="frmSwPricing" property="gastricBinding"  styleId="gastricBinding" styleClass="selectBox selectBoxMedium"  >
							  <html:option value="">Select from list</html:option>
							    <html:option value="Y">Covered</html:option>
							  <html:option value="N">Not covered</html:option>
						 </html:select>
			      </td>		
			      
			      
			      
			    </tr>
			      <tr> <td colspan="5" >&nbsp;</td> </tr>			    
			     <tr> --%>
			     <td class="formLabel" width="30%">Health Screen:<span class="mandatorySymbol otherspan">*</span></td>
			      <td class="textLabel">
				       <html:select name="frmSwPricing" property="healthScreen" styleId="healthScreen"  styleClass="selectBox selectBoxMedium"  >
							  <html:option value="">Select from list</html:option>
							    <html:option value="Y">Covered</html:option>
							  <html:option value="N">Not covered</html:option>
						 </html:select>
			      </td>	
			       <td></td>
			    </tr>
			    
			    
			     <tr> <td colspan="5" >&nbsp;</td> </tr>
			    <tr>
					<td class="formLabel" >Other key coverages : </td>
			    <td nowrap colspan="4">	<html:textarea  property="comments" styleClass="textBox textAreaLong" />
			    	</td>
			    	<td></td>
			    	
			    	</tr>
			    	 <tr><td></td>
			    <td colspan="4">
					<font color=black><i><b>Note :</b>  These coverages may be considered by the user later in  'Loadings and Management' section </i></font> 
				</td>
			    </tr>
			    <tr>
			       <td class="formLabel" width="30%">Trend :<span class="mandatorySymbol">*</span></td>
			      <td class="textLabel">
				   	<html:text name="frmSwPricing" property="trendFactor" onkeyup="isNumeric(this);"  styleClass="textBox textBoxPercentageSmallest" /><b style="color: black;">%</b>
			      </td>
			    </tr> 
			    	<tr><td colspan="4">&nbsp;</td></tr>
			    	<tr>
			    	<td height="20" class="formLabel">Source documents/other attachments :</td>
	    			<td class="textLabelBold" ><html:file property="file1" styleId="file1"   /> </td>
	    			<td colspan="2"><html:link href="javascript:onViewDocument('file1');"><img src="/ttk/images/ModifiedIcon.gif" title="Pricing Source Documents" alt="Pricing Source Documents" width="16" height="16" border="0" align="absmiddle"></html:link>
			    	 <bean:write name="frmSwPricing" property="attachmentname1"/></td>
			    </tr>
			    
			    <tr>
			    	<td></td>
	    			<td class="textLabelBold"><html:file property="file2" styleId="file3"/> </td>
	    			<td colspan="2" ><html:link href="javascript:onViewDocument('file2');"><img src="/ttk/images/ModifiedIcon.gif" title="Pricing Source Documents" alt="Pricing Source Documents" width="16" height="16" border="0" align="absmiddle"></html:link>
			    	<bean:write name="frmSwPricing" property="attachmentname2"/></td>
			    </tr>
			    
			    <tr>
			  		 <td></td>
	    			<td class="textLabelBold"><html:file property="file3" styleId="file3"/> </td>
	    			<td colspan="2" ><html:link href="javascript:onViewDocument('file3');"><img src="/ttk/images/ModifiedIcon.gif" title="Pricing Source Documents" alt="Pricing Source Documents" width="16" height="16" border="0" align="absmiddle"></html:link>
	    			 <bean:write name="frmSwPricing" property="attachmentname3"/></td>
			    </tr>
			    
			    <tr>
			    	<td></td>
	    			<td class="textLabelBold"><html:file property="file4" styleId="file4"/> </td>
			    	<td colspan="2" ><html:link href="javascript:onViewDocument('file4');"><img src="/ttk/images/ModifiedIcon.gif" title="Pricing Source Documents" alt="Pricing Source Documents" width="16" height="16" border="0" align="absmiddle"></html:link>
			    	
			    	 <bean:write name="frmSwPricing" property="attachmentname4"/></td>
			    	
			    	<td></td>
			    </tr>
			    
			    <tr>
			    	<td></td>
	    			<td class="textLabelBold"><html:file property="file5" styleId="file5"/> </td>
	    		     <td colspan="2" >	<html:link href="javascript:onViewDocument('file5');"><img src="/ttk/images/ModifiedIcon.gif" title="Pricing Source Documents" alt="Pricing Source Documents" width="16" height="16" border="0" align="absmiddle"></html:link>
	    			 <bean:write name="frmSwPricing" property="attachmentname5"/></td>
			    	
			    	<td></td>
			    </tr>
			 
			 
<%-- 			  <logic:equal  property="pricingDocs" value="Y" name="frmSwPricing">
	    		 		<html:link href="javascript:onViewDocument();"><img src="/ttk/images/ModifiedIcon.gif" alt="Pricing Source Documents" width="16" height="16" border="0" align="absmiddle"></html:link>
 	    		   </logic:equal> --%>
			 
			     <!-- <tr> <td colspan="5" >&nbsp;</td> </tr> -->
			    
			 
			    </table>
			    
			  
			   
			    
			   </fieldset>
			   
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
	<html:hidden property="dentalLimitList" />
	<html:hidden property="opticalLimitList" />
	<html:hidden property="policyNumber" />
	<html:hidden property="newdataentry" />
	<html:hidden property="orthodonticsCopayDesc" name="frmSwPricing" />
	<html:hidden property="opCopaypharmacyDesc" name="frmSwPricing" />
	<html:hidden property="opInvestigationDesc" name="frmSwPricing" />
	<html:hidden property="opCopyconsultnDesc" name="frmSwPricing" />
	<html:hidden property="opCopyothersDesc" name="frmSwPricing" />
	<html:hidden property="opCopyalahlihospDesc" name="frmSwPricing" />
	<html:hidden property="opPharmacyAlAhliDesc" name="frmSwPricing" />
	<html:hidden property="opConsultAlAhliDesc" name="frmSwPricing" />
	<html:hidden property="opInvestnAlAhliDesc" name="frmSwPricing" />
	<html:hidden property="opothersAlAhliDesc" name="frmSwPricing" />
	<html:hidden property="policycategory" name="frmSwPricing" />
	<INPUT TYPE="hidden" NAME="networkType" id="networkTypeId" VALUE='<%=session.getAttribute("networkType") %>'/>
	<INPUT TYPE="hidden" NAME="fetchData" id="fetchData" VALUE='<%=request.getAttribute("fetchData") %>'/>
	<INPUT TYPE="hidden" NAME="policyFetchFlag" id="vidalPolicyId" value="${requestScope.policyFetchFlag}"/>
	<INPUT TYPE="hidden" NAME="coverStartDate_check" id="coverStartDate_check" value="<bean:write name="frmSwPricing" property="coverStartDate"/>"/>
		
	<script type="text/javascript">
//	onchangeOpticalType(document.forms[1].opticalLimitList);
//	onchangeMaternityType(document.forms[1].maternityLimitList);
//	onchangeDentalType(document.forms[1].dentalLimitList);
//	onchangeMaxBenefitType (document.forms[1].maxBenifitList);
	onchangeOutpatient(document.forms[1].outpatientBenefit,"Y");
	onDentalChange(document.forms[1].dentalYN);
	onOpticalChange(document.forms[1].opticalYN);
	onMaternityChange(document.forms[1].maternityYN);

	onAhliHospChange(document.forms[1].alAhlihospital);
	onOPservices(document.forms[1].opdeductableserviceYN);
	onOPCopayAhliHosp(document.forms[1].alAhlihospOPservices);
	onOPCopay(document.forms[1].opCopayList);

	//formalMessage();
	
	
</script>

	
</html:form>

</div>
</body>
</html>


