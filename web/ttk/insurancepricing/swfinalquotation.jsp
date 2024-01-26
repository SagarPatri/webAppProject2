<html>
<body>
<%
/**
 * @ (#) usersupport.jsp
 * Project      : TTK Software Support
 * Author       : Shruti
 * Company      : 
 * Date Created : 
 *
 * @author       : Shruti
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,com.ttk.business.common.SecurityManagerBean,com.ttk.dto.usermanagement.UserSecurityProfile"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@page import="java.util.ArrayList" %>
<%@page import="com.ttk.common.TTKPropertiesReader"%>


<script type="text/javascript" SRC="/ttk/scripts/validation.js"></script>
    <script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
    <script type="text/javascript" src="/ttk/scripts/insurancepricing/swfinalquotation.js"></script>	
     <script type="text/javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script> 
<SCRIPT LANGUAGE="JavaScript">
	bAction = false; //to avoid change in web board in product list screen //to clarify
	var TC_Disabled = true;
</SCRIPT>
 <%
 	pageContext.setAttribute("insuranceCompany", Cache.getCacheObject("insuranceCompany"));
 	pageContext.setAttribute("alkootProducts", Cache.getCacheObject("alkootProducts"));
	pageContext.setAttribute("logicType", Cache.getCacheObject("logicType"));
	pageContext.setAttribute("creditNotePeriod", Cache.getCacheObject("creditNotePeriod"));

	
	int iRowCount = 0;
	String authorityYN = "N";
%>
<!-- S T A R T : Search Box -->

	<html:form action="/SwFinalGenerateQuotationAction.do"  method="post" enctype="multipart/form-data"> 
	
	<logic:notEmpty name="fileName" scope="request">
		<script language="JavaScript">
			var w = screen.availWidth - 10;
			var h = screen.availHeight - 82;
			var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
			window.open("/SwFinalGenerateReport.do?mode=doPrintAcknowledgementfile&displayFile=<bean:write name="fileName"/>&reportType=PDF",'PrintAcknowledgement',features);
		</script>
	</logic:notEmpty>
	
	<logic:notEmpty name="quatationFIle" scope="request">
	
		<script language="JavaScript">
			var w = screen.availWidth - 10;
			var h = screen.availHeight - 82;
			var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
			window.open("/SwFinalGenerateReport.do?mode=doPrintAcknowledgement&reportType=PDF",'PrintAcknowledgement',features);
		</script>
	
	</logic:notEmpty>
	
	
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		  <tr>
		    <td width="65%"></td> 
		  </tr>
	</table>
	<div class="contentArea" id="contentArea">
	<html:errors />
	<logic:notEmpty name="successMsg" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="middle">&nbsp;
						<bean:write name="successMsg" scope="request"/>
				  </td>
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
<fieldset>
    <legend>Quotation</legend>
    <table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="22%" class="formLabel">Insurance Company:<span class="mandatorySymbol">*</span></td>
        <td width="29%" class="textLabelBold">
       <!--  -->
        <html:select property="companyName" styleId="companyName" styleClass="selectBox selectBoxLargest" onchange="getInsCode(this);getproductList(this);">
		       <%--   <html:option value="">Select from list</html:option> --%>
		          <html:optionsCollection name="insuranceCompany" label="cacheDesc" value="cacheId" />
         </html:select>
       
        
   <%--      <td width="20%" class="formLabel">Insurance Code: <span class="mandatorySymbol">*</span></td>
        <td width="31%" class="textLabelBold">
        <html:text property="officeCode" styleId="officeCode" readonly="true" styleClass="textBox textBoxMedium textBoxDisabled"/>

        </td> --%>
        <td class="formLabel" width="20%">Product Name: <span class="mandatorySymbol">*</span></td>
		        	<td width="30%">
				        <html:select  property="productSeqID"  styleClass="selectBox selectBoxLargest" disabled="" >
			        		<html:option value="">Select from list</html:option>
			        		<html:optionsCollection name="alkootProducts" label="cacheDesc" value="cacheId" />
<%-- 			        	  <html:optionsCollection  name="frmSwFinalQuote" property="alInsProducts" value="cacheId" label="cacheDesc"/>
 --%>			        		
			        		
			        	</html:select>
		        	</td>
      </tr>
      <tr><td>&nbsp;</td></tr>
       <tr>
			       <td class="formLabel" width="30%">Refund condition :<span class="mandatorySymbol">*</span></td>
			      <td class="textLabel">
				       <html:select name="frmSwFinalQuote" property="logicType"    styleClass="selectBox selectBoxMedium"  >
							  <html:option value="">Select from list</html:option>
							<html:optionsCollection name="logicType" value="cacheId" label="cacheDesc"/>
					  </html:select>
			      </td>	
				  <td class="formLabel" width="30%">Administration Charges :<span class="mandatorySymbol">*</span></td>
			      <td class="textLabel">
				     	<html:text name="frmSwFinalQuote" onkeyup="isNumeric(this);" property="administrationCharges" onkeyup="isNumeric(this);"  styleClass="textBox textBoxVerySmall" /><b style="color: black;">%</b>
			      </td>	
			    </tr>
			    <tr>
			       <td class="formLabel" width="30%">Duration for credit note generation :<span class="mandatorySymbol">*</span></td>
			      <td class="textLabel">
				       <html:select name="frmSwFinalQuote" property="creditGeneration" onchange="onchangeCreditNote(this)" styleClass="selectBox selectBoxMedium"  >
							  <html:option value="">Select from list</html:option>
							<html:optionsCollection name="creditNotePeriod" value="cacheId" label="cacheDesc"/>
						 </html:select>
			      </td>	
				      <logic:match name="frmSwFinalQuote" property="creditGeneration" value="OTHD"> 
			       <td class="formLabel" width="30%">Credit Note (Others) :</td>
			      <td class="textLabel">
				   		<html:text name="frmSwFinalQuote" readonly="" onkeyup="isNumericOnly(this);" property="creditGenerationOth" styleClass="textBox textBoxLarge" />
			      </td>
			      </logic:match>
			      	 <logic:notMatch name="frmSwFinalQuote" property="creditGeneration" value="OTHD">
			      	      <td class="formLabel" width="30%">Credit Note (Others) :</td>
			      		  <td class="textLabel">
				   		  <html:text name="frmSwFinalQuote" readonly="true" onkeyup="isNumericOnly(this);" property="creditGenerationOth" styleClass="textBox textBoxLarge textBoxDisabled" />
			      		  </td>
			      	 </logic:notMatch>   
			    </tr>
      <tr>
      <td class="formLabel" width="30%">Maternity Age Band :<span class="mandatorySymbol">*</span></td>
			      <td class="textLabel">
				   Min. <html:text name="frmSwFinalQuote" onkeyup="isNumeric(this);" property="maternityMinBand" onkeyup="isNumeric(this);"  styleClass="textBox textBoxPercentageSmallest" />
			     &nbsp;Max. <html:text name="frmSwFinalQuote" onkeyup="isNumeric(this);" property="maternityMaxBand" onkeyup="isNumeric(this);"  styleClass="textBox textBoxPercentageSmallest" />
			     
			      </td>
			    </tr>
      
      <tr><td>&nbsp;</td></tr>
     
       <tr>
      	<td class="formLabel" width="20%">Batch Number:</td>
		<td width="30%">
		<b> <bean:write name="frmSwFinalQuote" property="batchNumber"/> </b><html:hidden property="batchNumber" /><html:hidden property="enrollbatchSeqid" />
       </td>
      
      	<td class="formLabel" width="20%">Policy Number: </td>
		  <td width="30%">
		<b><bean:write name="frmSwFinalQuote" property="policyNumber"/> </b>
		 </td>
        </tr>
       	<logic:notEmpty name="frmSwFinalQuote" property="policyNumber" >
        <tr><td colspan="3"></td>
        <td><a href="#" onClick="javascript:softcopyUpload()" ><i>Softcopy Bulk Member Upload</i></a></td>
        </tr>
        </logic:notEmpty>

 	<logic:empty name="frmSwFinalQuote" property="policyNumber" >
		 <tr> 
		     <td colspan="5" align="center" >
      		   <button type="button" name="Button" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onSave();"><u>S</u>ave</button>&nbsp;
		    </td>
	  	</tr>
	  	</logic:empty>
	  	
	  	</table>
	  	  </fieldset>
	  	  <logic:notEmpty name="frmSwFinalQuote" property="productSeqID" >
	  	  
	  	<table><tr><td>&nbsp;</td></tr></table>
	<%
	if(TTKCommon.isAuthorized(request,"Edit")) {
	%>
	    <fieldset>
	  		 <table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
	  		<tr> 
		     <td colspan="3" align="center">
		     <logic:empty name="frmSwFinalQuote" property="policyNumber" >
      		 &nbsp;<a href="#" id="" onclick="javascript:ongenerateQuotation();"><i><b>Generate Quotation</b></i></a>&nbsp;&nbsp;&nbsp;
      		 </logic:empty>
      		 &nbsp;<a href="#" id="" onclick="javascript:onsearchQuotation();"><i><b>Refresh Quotation Log</b></i></a>&nbsp;&nbsp;&nbsp;
      		
<!--              <button type="button" name="Button" accesskey="g" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:ongenerateQuotation();"><u>G</u>enerate Quotation</button>&nbsp;
 -->          
		    </td>
	  	</tr>
	  	<%} %>
	 
       
	</table>
    </fieldset>
    
	<fieldset><legend>Quotation Log</legend>
			<ttk:HtmlGrid name="tableDataquo"/>
	</fieldset>
	 <table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
    <ttk:PageLinks name="tableDataquo"/>	
    </table>
	
	<logic:notEmpty name="frmSwFinalQuote" property="finalQuotationNo" >
	<fieldset><legend>Final Quotation for approval</legend>
	 <%
			 String trendfactorscreen1 = (String)request.getSession().getAttribute("trendFactor");
		
			 if(trendfactorscreen1.equalsIgnoreCase("Y"))
			 {
				if(TTKCommon.isAuthorized(request,"Authority")) {
					authorityYN = "Y";
				%> 
		 <table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
		<tr>
			 <%-- <logic:match name="frmSwFinalQuote" property="trendFactor" value="Y"> --%>
			
				<td colspan="3">	  					
				<b>Trend factor is less than 6%  approval is required ,Approved(Y/N)</b><html:checkbox property="trendfactorYN" styleId="trendfactorYN"/>
				
				</td>	 				
				
			<td>&nbsp;</td>
			<%-- </logic:match> --%>
		</tr>
		</table>
		<%} //trend factor if
			 }//authority if
			%>
  
    <table align="center" class="buttonsContainer" border="0" cellspacing="0" cellpadding="0">
	 <tr align="center">
	   	<td colspan="3"> <b>Final Quotation No. :  <bean:write name="frmSwFinalQuote" property="finalQuotationNo"/> </b>   &nbsp;
   		<html:link href="javascript:onViewEmailDocument('file1');"><img src="/ttk/images/ModifiedIcon.gif" title="Quotation Source Documents" alt="Quotation Source Documents" width="16" height="16" border="0" align="absmiddle"></html:link>
	   </td>
	    </tr>
	<%
	if(TTKCommon.isAuthorized(request,"Edit")) {
	%>
	    <tr align="center">
	   	<td colspan="2">Source documents : &nbsp;
   		<html:file property="file1" styleId="file1"   /> &nbsp;
<%--    		<html:link href="javascript:onViewEmailDocument('file1');"><img src="/ttk/images/ModifiedIcon.gif" alt="Quotation Source Documents" width="16" height="16" border="0" align="absmiddle"></html:link> &nbsp; &nbsp;
 --%>	    <button type="button" name="Button" accesskey="a" class="buttonsPricingLarge" onMouseout="this.className='buttonsPricingLarge'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onAcceptQuotation();"><u>A</u>pprove Quotation</button>&nbsp;
	    </td>
	    </tr>
	<tr align="center"><td colspan="3" >
	

		<logic:notEmpty name="frmSwFinalQuote" property="policyNumber" >
		<button type="button" name="Button" accesskey="a" class="buttonsPricingLarge" onMouseout="this.className='buttonsPricingLarge'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onPolicyCopy();">Generate <u>P</u>olicy Copy</button>&nbsp;
		</logic:notEmpty>
	
	<%} 
	%>
	<button type="button" name="Button" accesskey="b" class="buttonsPricingSmall" onMouseout="this.className='buttonsPricingSmall'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onCloseQuotation();"><u>B</u>ack</button>&nbsp;
	</td></tr>
	</table>
	</fieldset>
	</logic:notEmpty>
		
	<logic:empty name="frmSwFinalQuote" property="finalQuotationNo" >
		<table align="center" class="buttonsContainer" border="0" cellspacing="0" cellpadding="0">
	 	<tr align="center">
	   	<td>
		<button type="button" name="Button" accesskey="b" class="buttonsPricingSmall" onMouseout="this.className='buttonsPricingSmall'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onCloseQuotation();"><u>B</u>ack</button>&nbsp;
	</td></tr>
	</table>
	</logic:empty>
	<table>
	<tr>  <td colspan="4">
					<font color=red><i><b>Note :</b>  Upload file size should not exceed 3 MB.Please Upload DOC,XLS OR PDF files.</i></font> 
				</td></tr>
	</table>
	
	</logic:notEmpty>
	
	 </div>
<!-- <div id ="loader" class="loader"></div>	  -->
	 
   <input type="hidden" name="mode" value=""/>
   <input type="hidden" name="child" value="">
	<INPUT TYPE="hidden" NAME="rownum" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	<INPUT TYPE="hidden" NAME="groupseqid" VALUE="<bean:write name="frmSwFinalQuote" property="lngGroupProfileSeqID" />">
	<INPUT TYPE="hidden" NAME="addedBy" VALUE="<%=(TTKCommon.getUserSeqId(request))%>">
   <input type="hidden" name="reportType" value=""/>
   		<html:hidden property="hidInsuranceSeqID"/>
<%--    <html:hidden  name="frmSwFinalQuote" property="polcopyseqid"/>  --%>
     <!-- Required data for softcopy upload.-->
  <!--   <input type="hidden" name="policyNumber" id="policy_num" > -->
  <!--  <input type="hidden" name="productSeqID" id="productTyp_num" > -->
  <!--   <input type="hidden" name="insuranceCompany" id="insComp_num" > -->

<%--   	<html:hidden  name="frmSwFinalQuote"  property="productSeqID" /> --%>
   	<html:hidden  name="frmSwFinalQuote"  property="policySeqid" />
   	<html:hidden  name="frmSwFinalQuote"  property="groupId" />
   	<html:hidden  name="frmSwFinalQuote"  property="finalQuotationNo" />
   	<html:hidden  name="frmSwFinalQuote"  property="finalPolcopyseqid" />
   	<html:hidden  name="frmSwFinalQuote"  property="finalQuotationdocs" />
   	<html:hidden  name="frmSwFinalQuote"  property="trendFactorYNValue" />
   	
   	
	<INPUT TYPE="hidden" NAME="authority" VALUE="<%=authorityYN%>">
    <input type="hidden" name="userSeqId" value="<%= TTKCommon.getUserID(request)%>">
    <input type="hidden" name="EnrollmentUploadURL" value="<%=TTKPropertiesReader.getPropertyValue("EnrollmentUploadURL") %>"/>
    <input type="hidden" name="EnrollmentSoftUploadURL" value="<%=TTKPropertiesReader.getPropertyValue("EnrolmentSoftCopyUrl") %>"/>		
    <input type="hidden" name="trendFactor" value="<%=request.getSession().getAttribute("trendFactor")%>">
   
   
    <script language="javascript">
    trendfactorload();
		</script>
   
   </html:form> 
   </body>
   </html>



