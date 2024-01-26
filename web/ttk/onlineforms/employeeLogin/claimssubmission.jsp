<%
/**
 * @ (#) addgeneraldetail.jsp 21st Sep 2005
 * Project      : TTK HealthCare Services
 * File         : addgeneraldetail.jsp
 * Author       : Srikanth H M
 * Company      : Span Systems Corporation
 * Date Created : 21st Sep 2005
 * @author       :
 * Modified by   : Krishna K H
 * Modified date : 13 Mar 2006
 * Reason        :
 */
%>
<%@page import="java.util.ArrayList"%>
<%@ page import="com.ttk.common.WebBoardHelper,com.ttk.common.TTKCommon,com.ttk.dto.usermanagement.UserSecurityProfile"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<script language="JavaScript" src="/ttk/scripts/validation.js"></script>


<script language="javascript" src="/ttk/scripts/onlineforms/claimssubmission.js"></script>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<head>
	<link rel="stylesheet" type="text/css" href="css/style.css" />
	<link rel="stylesheet" type="text/css" href="css/autoComplete.css" />
	<script language="javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script>
	<script language="javascript" src="/ttk/scripts/jquery.autocomplete.js"></script>
	<SCRIPT>
	</SCRIPT>
</head>





<style type="text/css">
.download_form_class {
    width: 15%;
    float: right;
    margin-right: 50px;
    background-color: rgb(0, 26, 102);
    color: #fff;
    font-weight: bold;
    height: 30px;
    margin-right: 28px;
    border-radius: 5px;
    margin-top: 14px;
}
.save_button_class {
    margin-right: 50px;
    background-color: rgb(0, 26, 102);
    color: #fff;
    font-weight: bold;
    height: 5%;
}

.claim_submission_class{
    width: 90%;
    margin-left: 80px;
}
.formContainerWeblogin TD {
    padding: 1px;
    padding: 1px;
    text-align: left;
    font-family: Arial, Helvetica, sans-serif;
}



.successContainer, .errorContainer{
margin-left: -37px;
}


</style>

<script>
var JS_Focus_ID="<%=TTKCommon.checkNull(request.getAttribute("focusID"))%>";
</script>

<%
UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
request.setAttribute("loginType", userSecurityProfile.getLoginType());
String editable=null;
boolean accViewMode=true;
boolean conViewMode=true;
pageContext.setAttribute("destnationbank",Cache.getCacheObject("destnationbank"));
pageContext.setAttribute("OutsideQatarCountryList", Cache.getCacheObject("OutsideQatarCountryList"));
pageContext.setAttribute("countryCode", Cache.getCacheObject("countryCode"));
/* pageContext.setAttribute("alCityList", Cache.getCacheObject("alCityList"));
pageContext.setAttribute("alDistList", Cache.getCacheObject("nationalities"));
pageContext.setAttribute("alBranchList", Cache.getCacheObject("nationalities")); */
if(session.getAttribute("editable")!=null)
	editable=(String) session.getAttribute("editable");
if("AccountInfo".equals(editable))
	accViewMode=false;

if("ContactInfo".equals(editable))
	conViewMode=false;

/* String fileIdStr=null;
int fileId=0;
if(request.getAttribute("fileId")!=null){
	fileIdStr=(String) request.getAttribute("fileId");
}
fileId=Integer.parseInt(fileIdStr); */
%>
	<div class="contentArea" id="contentArea">
	
	<c:if test="${requestScope.loginType eq 'EMPL'}">
	<div>
	<table align="center" class="pageTitle" border="0" cellspacing="0"
		cellpadding="0">
		<tr>
			<td width="57%">Claims Submission - <bean:write name="claimSubmissionForm" property="caption" /></td>
		</tr>
	</table>
	</div>
	</c:if>
		<div class="claim_submission_class">
			<html:form action="/EmpClaimSubmissionAction.do"  method="post" enctype="multipart/form-data">
	<!-- S T A R T : Page Title -->

	
		
		<html:errors/>
		
	<logic:notEmpty name="Batch_Seq_ID" scope="request">
			 <script>
				alert("Batch Number :"+"<bean:write name="frmEmpClaimSubmission" property="Batch_No"/>");
			</script> 
	</logic:notEmpty>
	
		<!-- S T A R T : Success Box -->
		<logic:notEmpty name="successMsg" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
			  		<td>
			  			<img src="/ttk/images/SuccessIcon.gif" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
						<bean:message name="successMsg" scope="request"/>
			  		</td>
				</tr>
			</table>
		</logic:notEmpty>
		<!-- E N D : Success Box -->
	<logic:notEmpty name="fileError" scope="request" >
		<table align="center" class="errorContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td><img src="/ttk/images/ErrorIcon.gif" alt="Alert" title="Alert" width="16" height="16" align="absmiddle">&nbsp;
	          <bean:write name="fileError" scope="request"/>
	        </td>
	      </tr>
   		 </table>
   </logic:notEmpty>
   <c:if test="${requestScope.loginType eq 'EMPL'}">
   <table align="center" class="formContainerWeblogin" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="formLabel"><input type="button" name="downloadForm" value="Download Claim Form" class="download_form_class" onclick="javascript:onGetDownloadForm();"></td>
				</tr>
			</table>
   <fieldset>
		<legend>Bank Account Information </legend>
			<table align="center" class="formContainerWeblogin" border="0" cellspacing="0" cellpadding="0">
				<tr>
              <td width="21%" class="formLabel">Bank Location: <span class="mandatorySymbol">*</span></td>
                 <td>
          <html:select property="bankAccountQatarYN" name="claimSubmissionForm" styleClass="selectBox selectBoxMedium"   onchange="onChangeQatarYN()" disabled="<%=accViewMode%>">
                    <html:option value="Y">Within Qatar</html:option>
                    <html:option value="N">Outside Qatar</html:option>
                    </html:select>        
            </td>
         </tr>
				<tr>
					<td class="formLabel">Bank Account Holder Name: </td>
					<td class="textLabel">
					 <html:text property="accountName" name="claimSubmissionForm" styleClass="textBox textBoxMedium" style="background-color: #EEEEEE;" readonly="true" /> 
					</td>
					
					<td class="formLabel">Bank Name: <span class="mandatorySymbol">*</span></td><TD><%-- <html:text property="bankname" name="claimSubmissionForm" styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="60" readonly="<%=accViewMode%>" disabled="<%=accViewMode%>" /> --%>
					<logic:equal name="claimSubmissionForm" property="bankAccountQatarYN" value="N">
  					   <html:text property="bankname" name="claimSubmissionForm" styleId="state1" onkeyup="ConvertToUpperCase(event.srcElement);" styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="60" readonly="<%=accViewMode%>" disabled="<%=accViewMode%>" />
  					  </logic:equal>
  					  <logic:notEqual name="claimSubmissionForm" property="bankAccountQatarYN" value="N">	
                 		   <html:select property="bankname" name="claimSubmissionForm" styleId="state1"  styleClass="selectBox selectBoxMedium" onchange="onChangeBank('state1')" readonly="<%=accViewMode%>" disabled="<%=accViewMode%>" >
                   			 <html:option value="">Select from list</html:option>
                  			  <html:optionsCollection name="destnationbank" label="cacheDesc"  value="cacheId" />
                 			   </html:select>
                 			</logic:notEqual>
					 </TD>
					</tr>
					<tr>
					<td width="21%" class="formLabel">Bank City:  <span class="mandatorySymbol">*</span> </td>
                 <td>
					<logic:equal name="claimSubmissionForm" property="bankAccountQatarYN" value="N">
                   <html:text property="bankState" name="claimSubmissionForm" styleId="state2" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60" />
                   </logic:equal>
                  <logic:notEqual name="claimSubmissionForm" property="bankAccountQatarYN" value="N">	
                    <html:select property="bankState" name="claimSubmissionForm" styleId="state2" styleClass="selectBox selectBoxMedium" onchange="onChangeState('state2')" readonly="<%=accViewMode%>" disabled="<%=accViewMode%>" >
                    <html:option value="">Select from list</html:option> 
                    <html:optionsCollection name="alCityList"  label="cacheDesc" value="cacheId" />
                    </html:select>
               </logic:notEqual>
			   
                </td>
               
					<logic:equal name="claimSubmissionForm" property="bankAccountQatarYN" value="N">
					 <td width="19%" class="formLabel">Bank Area:   </td>
                 <td>
                   <html:text property="bankcity" name="claimSubmissionForm" styleId="state3" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60" />
                    </td>
                   </logic:equal>
                  <logic:notEqual name="claimSubmissionForm" property="bankAccountQatarYN" value="N">	
                   <td width="19%" class="formLabel">Bank Area: <span class="mandatorySymbol">*</span></td>
                 <td>
                    <html:select property="bankcity" name="claimSubmissionForm" styleId="state3" styleClass="selectBox selectBoxMedium" onchange="onChangeCity('state3')" readonly="<%=accViewMode%>" disabled="<%=accViewMode%>" >
                          <html:option value="">Select from list</html:option> 
                  		  <html:optionsCollection name="alDistList" label="cacheDesc" value="cacheId" /> 
                    </html:select>
                      </td>
                    </logic:notEqual>
              
                </tr>
					<tr>
					
					<logic:equal name="claimSubmissionForm" property="bankAccountQatarYN" value="N">
					<td width="21%" class="formLabel">Bank Branch:</td>
                 <td>
                   <html:text property="bankBranchText" name="claimSubmissionForm" styleId="state4" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60" />
                   </td>
                   </logic:equal>
                   <logic:notEqual name="claimSubmissionForm" property="bankAccountQatarYN" value="N">
                   <td width="21%" class="formLabel">Bank Branch:<span class="mandatorySymbol">*</span></td>
                 <td>
                    <html:select property="bankBranch" name="claimSubmissionForm" styleId="state4" styleClass="selectBox selectBoxMedium" readonly="<%=accViewMode%>" disabled="<%=accViewMode%>" ><!--  onchange="onChangeBranch('state4')"  -->
                    <html:option value="">Select from list</html:option>
                    <html:optionsCollection name="alBranchList" label="cacheDesc" value="cacheId" /> 
                    </html:select>
                    </td>
                    </logic:notEqual>
					
                
					<td class="formLabel">Swift Code: <span class="mandatorySymbol">*</span></td><td class="textLabel">
					<logic:equal name="claimSubmissionForm" property="bankAccountQatarYN" value="N">
                 <html:text property="ifsc" name="claimSubmissionForm" styleClass="textBox textBoxMedium"  maxlength="60" />
                 </logic:equal>
                 <logic:notEqual name="claimSubmissionForm" property="bankAccountQatarYN" value="N">
                  <html:text property="ifsc" name="claimSubmissionForm" styleClass="textBox textBoxMedium" style="background-color: #EEEEEE;" maxlength="60" readonly="true"/>
                </logic:notEqual>
					</td>
					</tr>
					<tr>
				<td width="21%" class="formLabel">Bank Code: </td>
                 <td width="30%">
                  <html:text property="neft" name="claimSubmissionForm" styleClass="textBox textBoxMedium" maxlength="60" readonly="<%=accViewMode%>" disabled="<%=accViewMode%>"/>
                 </td>
                 <td class="formLabel">IBAN No/Bank Account No.: <span class="mandatorySymbol">*</span></td>
					<td class="textLabel"><html:text property="bankAccNbr" name="claimSubmissionForm" styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="29" readonly="<%=accViewMode%>" disabled="<%=accViewMode%>" /></td>
		
				</tr>
				
				<tr>
				<td class="formLabel">Country:<span class="mandatorySymbol">*</span> </td>
        <td>
         <logic:equal name="claimSubmissionForm" property="bankAccountQatarYN" value="N">
         	<html:select property ="bankCountry" name="claimSubmissionForm" styleClass="selectBox selectBoxMedium" readonly="<%=accViewMode%>" disabled="<%=accViewMode%>">
        	     <html:option value="">Select From List</html:option>
                 <html:options collection="OutsideQatarCountryList" property="cacheId" labelProperty="cacheDesc"/>
          </html:select>
         </logic:equal>
         <logic:notEqual name="claimSubmissionForm" property="bankAccountQatarYN" value="N">
        	<html:select property ="bankCountry" name="claimSubmissionForm" styleClass="selectBox selectBoxMedium" readonly="<%=accViewMode%>" disabled="<%=accViewMode%>">
        	     <html:option value="">Select From List</html:option>
                 <html:options collection="countryCode" property="cacheId" labelProperty="cacheDesc"/>
          </html:select>
        </logic:notEqual>
        </td>
                </tr>
				
				<%if("AccountInfo".equals(editable)){ %>
				<tr><td class="formLabel"><input type="button" name="saveForm" value="Update" id="save_acc_id" class="save_button_class" onclick="javascript:onSaveAccountInfo();"></td></tr>
				<%} %>
			</table>
			
			<span class="mandatorySymbol"><font size=2 color= "red" style="font-family: Arial, Helvetica, sans-serif;">Please <a href="#" onclick="javascript:onEditAcctInfo();" style="color:#ff0000;">Click <b>Here</b></a> if you wish to change Bank account information</font></span>
	</fieldset>
	
	<fieldset>
		<legend>Contact Information </legend>
			<table align="center" class="formContainerWeblogin" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="formLabel">Contact Number: </td>
					<td class="textLabel"><html:text property="customerNbr" name="claimSubmissionForm" styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="60" readonly="<%=conViewMode%>" disabled="<%=conViewMode%>" />
					</td>
					<td class="formLabel">Email Id: </td><TD class="textLabel">
					<html:text property="emailID2" name="claimSubmissionForm" styleClass="textBoxWeblogin textBoxMediumWeblogin"  readonly="true" disabled="true" style="background-color: #EEEEEE;" /></TD>
					</tr>
					<%if("ContactInfo".equals(editable)){ %>
					<tr><td class="formLabel"><input type="button" name="saveForm" value="Update" id="save_con_id" class="save_button_class" onclick="javascript:onSaveAccountInfo();"></td></tr>
					<%} %>
			</table>
			<span class="mandatorySymbol"><font size=2 color= "red" style="font-family: Arial, Helvetica, sans-serif;">Please <a href="#" onclick="javascript:onEditContactInfo();" style="color:#ff0000;">Click <b>Here</b></a> if you wish to change Contact information</font></span>
	</fieldset>
   </c:if>
   
	<fieldset>
		<legend>Claim submission </legend>
			<table align="center" class="formContainerWeblogin" border="0" cellspacing="0" cellpadding="0" id="claim_sub_table_id">
				<tr class="formFields">
					<td class="formLabelWeblogin">Requested Amount: <span class="mandatorySymbol">*</span></td>
					<td>
					<html:text property="requestedAmount" styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="60" onkeyup="zeroVal(this);isNumeric(this);"/>
					</td>
					
				</tr>
							
				<tr class="formFields">
					<td class="formLabelWeblogin">Invoice No: <span class="mandatorySymbol">*</span></td>
					<td>
					<html:text property="invoiceno" styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="60" />
					</td>
				</tr>
				
				<tr class="formFields">
					<td class="formLabelWeblogin">Currency :<span class="mandatorySymbol">*</span></td>
					<td>
					<html:text property="currencyAccepted" styleId="currencyAccepted" value="QAR"
							styleClass="textBox textBoxMedium textBoxDisabled" readonly="false" /><a href="#"
						onclick="openRadioList('currencyAccepted','CURRENCY_GROUP','option');clearConversionRate();">
							<img src="/ttk/images/search_edit.gif" width="18" height="18"
							alt="Select Currency" border="0" align="bottom">
					</a></td>
				</tr>

			 <tr class="formFields">
					<td class="formLabel">Upload Claim Form:<span class="mandatorySymbol">*</span></td>
					<td class="formTextLabel" >
					<c:if test="${requestScope.loginType ne 'EMPL'}">
						<html:file property="file" ></html:file> &nbsp;
					</c:if>
					<c:if test="${requestScope.loginType eq 'EMPL'}">
					<div><p><b>Upload Claim form and other supporting documents like hospital bills, discharge summary, reports, etc. One after the another(up to 5 files allowed)</b></p></div>
					</c:if>
					</td>	
			</tr> 
			
				<tr class="formFields">
					 <td class="formLabel"></td> 
					<td class="formTextLabel" ><html:file property="file" styleId="file00" onchange="javascript:getFileInfo(this);"></html:file> &nbsp;</td>	
				</tr> 
				<tr class="formFields">
					 <td class="formLabel"></td> 
					<td class="formTextLabel" ><html:file property="file1" styleId="file01" onchange="javascript:getFileInfo(this);" style="display:none;"></html:file> &nbsp;</td>	
				</tr>
				<tr class="formFields">
					 <td class="formLabel"></td> 
					<td class="formTextLabel" ><html:file property="file2" styleId="file02" onchange="javascript:getFileInfo(this);" style="display:none;"></html:file> &nbsp;</td>	
				</tr>
				<tr class="formFields">
					 <td class="formLabel"></td> 
					<td class="formTextLabel" ><html:file property="file3" styleId="file03" onchange="javascript:getFileInfo(this);" style="display:none;"></html:file> &nbsp;</td>	
				</tr>
				<tr class="formFields">
					 <td class="formLabel"></td> 
					<td class="formTextLabel" ><html:file property="file4" styleId="file04" onchange="javascript:getFileInfo(this);" style="display:none;"></html:file> &nbsp;</td>	
				</tr>
				<%-- <tr class="formFields">
					 <td class="formLabel"></td> 
					<td class="formTextLabel" ><html:file property="file5" styleId="file05" onchange="javascript:getFileInfo(this);" style="display:none;"></html:file> &nbsp;</td>	
				</tr>
				<tr class="formFields">
					 <td class="formLabel"></td> 
					<td class="formTextLabel" ><html:file property="file6" styleId="file06" onchange="javascript:getFileInfo(this);" style="display:none;"></html:file> &nbsp;</td>	
				</tr>
				<tr class="formFields">
					 <td class="formLabel"></td> 
					<td class="formTextLabel" ><html:file property="file7" styleId="file07" onchange="javascript:getFileInfo(this);" style="display:none;"></html:file> &nbsp;</td>	
				</tr>
				<tr class="formFields">
					 <td class="formLabel"></td> 
					<td class="formTextLabel" ><html:file property="file8" styleId="file08" onchange="javascript:getFileInfo(this);" style="display:none;"></html:file> &nbsp;</td>	
				</tr>
				
				<tr class="formFields">
					 <td class="formLabel"></td> 
					<td class="formTextLabel" ><html:file property="file9" styleId="file09" onchange="javascript:getFileInfo(this);" style="display:none;"></html:file> &nbsp;</td>	
				</tr> --%> 
				 <%-- <tr class="formFields">
					 <td class="formLabel"></td> 
					<td class="formTextLabel" ><html:file property="file" ></html:file> &nbsp;</td>	
				</tr>
				<tr class="formFields">
					 <td class="formLabel"></td> 
					<td class="formTextLabel" ><html:file property="file" ></html:file> &nbsp;</td>	
				</tr>
				<tr class="formFields">
					 <td class="formLabel"></td> 
					<td class="formTextLabel" ><html:file property="file" ></html:file> &nbsp;</td>	
				</tr>
				<tr class="formFields">
					 <td class="formLabel"></td> 
					<td class="formTextLabel" ><html:file property="file" ></html:file> &nbsp;</td>	
				</tr> --%> 
				<!-- </table> -->
<!-- 				<table align="center" class="formContainerWeblogin" border="0" cellspacing="0" cellpadding="0" > -->
	<div>
				<tr class="formFields trFormField">
					 <td class="formLabel"></td> 
					<td class="formTextLabel" ><span class="mandatorySymbol"><b><font size=2 color= "red">Supported File Type: .pdf, .png, .jpeg<br/>Note: Maximum file size allowed 3MB per file</font></b></span> &nbsp;</td>	
				</tr><br/><br/>
				<tr class="formFields trFormField"><td colspan="2">
				<div class="main_info_text_class">
				<B>Note:</B>
				<div class="info_text_class">
				<ul style="list-style-type:square">
				  <li>For any claims above QAR 5,000 or equivalent, please post/submit original documents to AlKoot Insurance. Kindly mention your claim number on the documents for easy tracking.</li>
				</ul>
				</div>
				<br/>
				</div>
				
				<!-- Please post/Submit original claim documents to Alkoot Office Also mention the claim No. sent to your emai id/ Mobile No. on the documents. -->
				
				
				</td></tr>
		 <tr class="formFields trFormField">
            <td></td>
     		  <td>
     		  	<c:if test="${requestScope.loginType ne 'EMPL'}">
       			 <font size=2 color= "red"><br>Please upload only .pdf file type</font>
       			 </c:if>
				<!-- <font size=2 color="#0000FF"><br>2.Size should not more then 2MB </font> -->  
  	 		 </td>
     	 </tr> 
     	 </div>
		</table>
	</fieldset>
		
	<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
  			<tr class=formFields>
    			<td width="100%" align="center" style="padding-right: 200px;">	
    			<c:choose>
    			<c:when test="${requestScope.loginType eq 'EMPL'}">
    			<div><button type="button" name="Button" accesskey="s" class="button_design_class" onMouseout="this.className='button_design_class'" onMouseover="this.className='button_design_class buttonsHover'" onClick="javascript:onUserSubmit();"><u>S</u>ubmit</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 				<button type="button" name="Button2" accesskey="c" class="button_design_class" onMouseout="this.className='button_design_class'" onMouseover="this.className='button_design_class buttonsHover'" onClick="javascript:onNewClose();"><u>C</u>lose</button></div>
    			</c:when>
    			<c:otherwise>
    			<button type="button" name="Button" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onUserSubmit();"><u>S</u>ave</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 				<button type="button" name="Button2" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose();"><u>C</u>lose</button>
    			</c:otherwise>
    			</c:choose>
		    		
 				</td>
  			</tr>  		
		</table>
		<!-- E N D : Buttons -->
		
	<input type="hidden" name="mode" value="">
	<input type="hidden" name="focusID" value="">
	<input type="hidden" name="editable" value="<%=editable%>">
	<input type="hidden" name="loginType" value="<%=userSecurityProfile.getLoginType()%>">
	
<html:hidden property="policyClaimGrpSeqID"/> 
<html:hidden property="memberClaimSeqID"/> 
<html:hidden property="claimSeqId"/> 
<html:hidden property="claimBatchNumber"/> 
<html:hidden property="claimBatchSeqId"/> 
<html:hidden property="Batch_No"/> 
<html:hidden property="alertMsg"/> 

	</html:form>
		</div>
		
	</div>
	