<%
/** @ (#) medicaldetails.jsp May 6, 2006
 * Project       : TTK Healthcare Services
 * File          : medicaldetails.jsp
 * Author        : Pradeep R
 * Company     	 : Span Systems Corporation
 * Date Created  : May 6, 2006
 *
 * @author 		 : Pradeep R
 * Modified by   : Raghavendra T M
 * Modified date : Aug 29th, 2007
 * Reason        : Changes are done to coding link
 *
 */
%>

<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,com.ttk.common.PreAuthWebBoardHelper,com.ttk.common.ClaimsWebBoardHelper" %>
<%@ page import="com.ttk.common.CodingClaimsWebBoardHelper"%>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/dataentrypreauth/medicaldetails.js"></script>
<SCRIPT LANGUAGE="JavaScript">
var TC_Disabled = true; //to avoid the alert message on change of form elements\

function additionalhospdetails()
{
  document.forms[1].mode.value="doDefault";
  document.forms[1].child.value="AdditionalHospitalDetails";
  document.forms[1].action="/AdditionalHospDetailsAction.do";
  document.forms[1].submit();
}//end of additionalhospdetails()
</SCRIPT>
<%
  boolean viewmode=true;
  String strAmmendment="";
  String strLink=TTKCommon.getActiveLink(request);
  pageContext.setAttribute("strLink",strLink);
  if(TTKCommon.isAuthorized(request,"Edit"))
  {
    viewmode=false;
  }//end of if(TTKCommon.isAuthorized(request,"Edit"))
  pageContext.setAttribute("ActiveLink",TTKCommon.getActiveLink(request));
  pageContext.setAttribute("ActiveSubLink",TTKCommon.getActiveSubLink(request));
  
   
%>

<!-- S T A R T : Content/Form Area -->

<html:form action="/DataEntryMedicalDetailsAction.do" >
<bean:define id="codingReviewYN" name="frmPreAuthMedical" property="codingReviewYN"/>
<!-- S T A R T : Page Title -->
     <table align="center" class="<%=TTKCommon.checkNull(PreAuthWebBoardHelper.getShowBandYN(request)).equals("Y") ? "pageTitleHilite" :"pageTitle" %>" border="0" cellspacing="0" cellpadding="0">
     <%
     if(TTKCommon.getActiveLink(request).equals("Claims"))
     {
     %>
       <table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
      <%
      }
      %>
        <tr>
        <td width="57%">Medical Details - <bean:write name="frmPreAuthMedical" property="caption"/></td>
        <td align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
        </tr>
    </table>
<!-- E N D : Page Title -->
<!-- added for koc 1075 -->
<%
if(TTKCommon.getActiveLink(request).equals("Coding")||TTKCommon.getActiveLink(request).equals("DataEntryCoding"))
{
%>
<logic:notEmpty name="codingclaimChange" scope="session">
 	<logic:equal name="frmPreAuthMedical" property="ailmentVO.ailmentSubTypeID" value="PED">
	  <table align="center" class="errorContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
	   <tr>
	     <td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
			  <bean:write property="ailmentVO.codingClaimMsg" name="frmPreAuthMedical"/>
		</td>
	   </tr>
	  </table>
	  </logic:equal>
</logic:notEmpty>
<%
}
%>
 
<!-- added for koc 1075 -->

<!-- S T A R T : Form Fields -->
<div class="contentArea" id="contentArea">
<html:errors/>
<logic:match name="frmPreAuthMedical" property="showClaims" value="N">
<fieldset>
  <legend>Ailment Details</legend>
  <table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0" >
    <tr>
      <td width="35%" height="20" class="formLabel"> Complaints:</td>
      <td width="65%" class="formLabelBold">
        <bean:write name="frmPreAuthMedical" property="ailmentVO.ailmentDesc"/>
        <%
            if(TTKCommon.isAuthorized(request,"Edit"))
        	{
        		if(!(TTKCommon.getActiveLink(request).equals("DataEntryCoding")))
        		{
           %>
		           <!--<logic:notMatch name="frmPreAuthMedical" property="codingReviewYN" value="Y">-->
		                &nbsp;&nbsp;<a href="#" onclick="onAilmentDesc();"><img src="ttk/images/EditIcon.gif" title="Ailment Details" alt="Ailment Details" border="0" align="absmiddle"></a>
		           <!--</logic:notMatch>-->
           <%
           		}//end of if(!(TTKCommon.getActiveLink(request).equals("Code CleanUp")))
             }//end of if(TTKCommon.isAuthorized(request,"Edit"))
           %>
      </td>
    </tr>
    <tr>
      <td height="20" class="formLabel"> System of Medicine:</td>
      <td class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.medicineSystemDesc"/></td>
    </tr>
    <tr>
      <td height="20" class="formLabel">Accident Related Cases:</td>
      <td class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.ailClaimTypeDesc"/></td>
    </tr>
    <tr>
          <td height="20" class="formLabel"> Specialty:</td>
          <td class="textLabel"><table cellpadding="0" cellspacing="0">
        <tr>
          <td class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.specialityDesc"/></td>
           <logic:match name="frmPreAuthMedical" property="ailmentVO.specialityDesc"   value="Surgical">
            <td valign="middle" style="padding:0 0 0 150px"><label style="vertical-align:top" class="formLabel">Surgery Type: </label>
            <td style="padding:0 0 0 25px"><bean:write name="frmPreAuthMedical" property="ailmentVO.surgeryDesc"/></td>
           </logic:match>
           <logic:match name="frmPreAuthMedical" property="ailmentVO.specialityDesc"   value="Maternity">
	         <td valign="middle" style="padding:0 0 0 15px"><label style="vertical-align:top" class="formLabel">Maternity Type:</label>
	         <td style="padding:0 0 0 10px"><bean:write name="frmPreAuthMedical" property="ailmentVO.maternityDesc"/></td>
	         <td valign="middle" style="padding:0 0 0 15px"><label style="vertical-align:top" class="formLabel">No. of Living Children:</label>
	         <td style="padding:0 0 0 10px"><bean:write name="frmPreAuthMedical" property="ailmentVO.livingChildrenNumber"/></td>
           </logic:match>
        </tr>
        </table>
        </td>
     </tr>
    <tr>
      <td height="20" class="formLabel">Since When:</td>
      <td valign="middle" class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.durationDesc"/> </td>
      </tr>
    <tr>
      <td height="20" class="formLabel"> Findings:</td>
      <td valign="middle" class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.clinicalFindings"/></td>
      </tr>
    <tr>
      <td height="20" class="formLabel"> Provisional Diagnosis:</td>
      <td class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.provisionalDiagnosis"/></td>
      </tr>
    <tr>
      <td height="20" nowrap class="formLabel"> Is Ailment a Complication of Previous Illness:</td>
      <td class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.previousIllnessYN"/></td>
    </tr>
    <tr>
      <td height="20" class="formLabel"> Plan of Treatment:</td>
      <td class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.treatmentPlanDesc"/></td>
      </tr>
    <tr>
      <td height="20" class="formLabel"> Special Investigation:</td>
      <td class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.investReports"/></td>
      </tr>
    <tr>
      <td height="20" class="formLabel"> Details of Treatment:</td>
      <td class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.lineOfTreatment"/></td>
      </tr>
    <tr>
      <td height="20" class="formLabel"> Duration of Hospitalization:</td>
      <td valign="middle" class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.hospitalizationDaysDesc"/></td>
    </tr>
    <tr>
      <td height="20" class="formLabel"> Probable Date of Discharge:</td>
      <td valign="middle" class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.probableDischargeDate"/></td>
    </tr>

    <tr>
      <td height="20" class="textLabelBold">Associated Illness: </td>
      <td class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.associatedIllness"/></td>
      </tr>
    <tr>
      <td height="20" class="textLabelBold">Pre-Existing Diseases</td>
      <td class="textLabel">
      <%
        if(TTKCommon.isAuthorized(request,"Special Permission"))
        {
      %>
          <logic:notEmpty name="frmPreAuthMedical" property="showped" >
             <%
               if((TTKCommon.isAuthorized(request,"Edit")&&!"Y".equals(codingReviewYN.toString().trim()))||TTKCommon.isAuthorized(request,"Add PED"))
               {
             %>
                 <a href="#" onclick="javascript:onPEDIcon();"><img src="ttk/images/PedIcon.gif" title="Pre-Existing Diseases" alt="Pre-Existing Diseases" width="16" height="16" border="0" align="absmiddle" ></a>
             <%
               }//end of if(TTKCommon.isAuthorized(request,"Edit"))
             %>
          </logic:notEmpty>

          <logic:empty name="frmPreAuthMedical" property="showped" >
              <img src="ttk/images/PedIcon.gif" title="Pre-Existing Diseases" alt="Pre-Existing Diseases" width="16" height="16" border="0" align="absmiddle" ></a>
          </logic:empty>
     <%
       }
     %>
     </td>
    </tr>
  </table>
</fieldset>
</logic:match>

<logic:match name="frmPreAuthMedical" property="showClaims" value="Y">
<logic:match name="frmPreAuthMedical" property="showAilmentYN" value="Y">
	<fieldset>
	  <legend><img src="ttk/images/e.gif" title="Collapse" alt="Collapse" name="ad" width="16" height="16" align="top" onClick="showhide('ailmentDetails','ad')">&nbsp;Ailment Details  &nbsp;&nbsp;</legend>
	  <table align="center" class="formContainer" border="0" id="ailmentDetails" cellspacing="0" cellpadding="0" style="display: ">
	  <tr>
	      <td width="35%" height="20" class="formLabel"> Complaints:</td>
	      <td width="65%" class="textLabel">
	        <bean:write name="frmPreAuthMedical" property="ailmentVO.ailmentDesc"/>
	           <%
	            if(TTKCommon.isAuthorized(request,"Edit"))
	            {
	            	if(!(TTKCommon.getActiveLink(request).equals("Code CleanUp")))
        			{
	           %>
		                <logic:notMatch name="frmPreAuthMedical" property="codingReviewYN" value="Y">
		                &nbsp;&nbsp;<a href="#" onclick="onAilmentDesc();"><img src="ttk/images/EditIcon.gif" title="Ailment Details" alt="Ailment Details" border="0" align="absmiddle"></a>
		                </logic:notMatch>
	           <%
	           		}//end 
	             }//end of if(TTKCommon.isAuthorized(request,"Edit"))
	           %>
	</td>
	    </tr>
	    <tr>
	      <td height="20" class="formLabel"> System of Medicine:</td>
	      <td class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.medicineSystemDesc"/></td>
	    </tr>
	    <tr>
	      <td height="20" class="formLabel">Accident Related Cases:</td>
	      <td class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.ailClaimTypeDesc"/></td>
	    </tr>
	    <tr>
          <td height="20" class="formLabel"> Specialty:</td>
          <td class="textLabel"><table cellpadding="0" cellspacing="0">
        <tr>
          <td class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.specialityDesc"/></td>
           <logic:match name="frmPreAuthMedical" property="ailmentVO.specialityDesc"   value="Surgical">
            <td valign="middle" style="padding:0 0 0 150px"><label style="vertical-align:top" class="formLabel">Surgery Type:  </label>
            <td style="padding:0 0 0 25px"><bean:write name="frmPreAuthMedical" property="ailmentVO.surgeryDesc"/></td>
           </logic:match>
           <logic:match name="frmPreAuthMedical" property="ailmentVO.specialityDesc"   value="Maternity">
	               <td valign="middle" style="padding:0 0 0 15px"><label style="vertical-align:top" class="formLabel">Maternity Type:</label>
	               <td style="padding:0 0 0 10px"><bean:write name="frmPreAuthMedical" property="ailmentVO.maternityDesc"/></td>
	               <td valign="middle" style="padding:0 0 0 15px"><label style="vertical-align:top" class="formLabel">No. of Living Children:</label>
	               <td style="padding:0 0 0 10px"><bean:write name="frmPreAuthMedical" property="ailmentVO.livingChildrenNumber"/></td>
           </logic:match>
        </tr>
        </table>
        </td>
        </tr>
	    <tr>
      		<td height="20" class="formLabel"> Disability Type:</td>
      		<td class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.disabilityTypeDesc"/></td>
    	</tr>	
	    <tr>
	      <td height="20" class="formLabel">Since When:</td>
	      <td valign="middle" class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.durationDesc"/> </td>
	      </tr>
	    <tr>
	      <td height="20" class="formLabel">Final Diagnosis:</td>
	      <td class="formLabelBold"><bean:write name="frmPreAuthMedical" property="ailmentVO.finalDiagnosis"/></td>
	      </tr>
	    <tr>
	      <td height="20" class="formLabel">History:</td>
	      <td class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.history"/></td>
	    </tr>
	    <tr>
	      <td height="20" class="formLabel">Family History: </td>
	      <td class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.familyHistory"/></td>
	    </tr>
	    <tr>
	      <td height="20" class="formLabel">Plan of Treatment:</td>
	      <td class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.treatmentPlanDesc"/></td>
	    </tr>
	    <tr>
	      <td height="20" class="formLabel"> Details of Treatment:</td>
	      <td class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.lineOfTreatment"/></td>
	    </tr>
	    <tr>
	      <td height="20" class="formLabel">Date of Surgery: </td>
	      <td class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.surgeryDate"/></td>
	    </tr>
	    <tr>
	      <td height="20" class="formLabel">Investigation:</td>
	      <td class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.investReports"/></td>
	      </tr>
	    <tr>
	      <td height="20" class="formLabel">Condition on Discharge:</td>
	      <td class="formLabelBold"><bean:write name="frmPreAuthMedical" property="ailmentVO.dischrgConditionDesc"/></td>
	    </tr>
	    <tr>
	      <td height="20" class="formLabel">Advice to Patient: </td>
	      <td class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.advToPatient"/></td>
	    </tr>
	    <tr>
	      <td height="20" class="textLabelBold">Associated Illness: </td>
	      <td class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.associatedIllness"/></td>
	    </tr>
	    <tr>
	      <td height="20" class="textLabelBold">Pre-Existing Diseases</td>
	      <td class="textLabel">
	        <%
			if(TTKCommon.isAuthorized(request,"Special Permission"))
			{
		      %>
			  <logic:notEmpty name="frmPreAuthMedical" property="showped" >
			     <%
			       if((TTKCommon.isAuthorized(request,"Edit")&&!"Y".equals(codingReviewYN.toString().trim()))||TTKCommon.isAuthorized(request,"Add PED"))
			       {
			     %>
				 <a href="#" onclick="javascript:onClaimsPEDIcon();"><img src="ttk/images/PedIcon.gif" title="Pre-Existing Diseases" alt="Pre-Existing Diseases" width="16" height="16" border="0" align="absmiddle" ></a>
			     <%
			       }//end of if(TTKCommon.isAuthorized(request,"Edit"))
			     %>
			  </logic:notEmpty>

			  <logic:empty name="frmPreAuthMedical" property="showped" >
			      <img src="ttk/images/PedIcon.gif" title="Pre-Existing Diseases" alt="Pre-Existing Diseases" width="16" height="16" border="0" align="absmiddle" ></a>
			  </logic:empty>
		     <%
		       }
		%>
	      </td>
	    </tr>
	</table>
	</fieldset>
	<%
	if(!TTKCommon.getActiveLink(request).equals("DataEntryClaims"))
	{
		%>
	<fieldset>
	  <legend><img src="ttk/images/e.gif" title="Collapse" alt="Collapse" name="doc" width="16" height="16" align="top" onClick="showhide('doctorOpinion','doc')">&nbsp;Doctor Opinion  &nbsp;&nbsp;</legend>
	  <table align="center" class="formContainer" border="0" id="doctorOpinion" cellspacing="0" cellpadding="0" style="display: ">
	  <tr>
		  <td width="35%" height="20" class="formLabel">Doctor Opinion:</td>
	      <td width="65%" class="textLabel">
	      <bean:write name="frmPreAuthMedical" property="ailmentVO.docOpinion"/>
	      <%
            if(TTKCommon.isAuthorized(request,"Doctor Opinion"))
        	{
        		if(TTKCommon.isAuthorized(request,"Edit"))
        		{
           %>
	       <a href="#" onclick="javascript:onDocOpinionIcon();"><img src="ttk/images/EditIcon.gif" title="Doctor Opinion" alt="Doctor Opinion" width="16" height="16" border="0" align="absmiddle" ></a>
	        <%
	           	}//end of if(TTKCommon.isAuthorized(request,"Edit"))
	        }//end of if(TTKCommon.isAuthorized(request,"Doctor Opinion"))
	        %>
	      </td>
	   	</tr>
		<tr>
			<td class="formLabel">Doctor's Name:</td>
			<td class="textLabel"><bean:write name="frmPreAuthMedical" property="ailmentVO.doctorName" /></td>
		</tr>
	</table>
	</fieldset>
	<%
	}%>
</logic:match>
<logic:notEmpty name="frmPreAuthMedical" property="hospitalAssocSeqID" >
  <fieldset>
  <legend><img src="ttk/images/c.gif" title="Expand" alt="Expand" name="ahd" width="16" height="16" align="top" onClick="showhide('additionalHospitalDetails','ahd')">&nbsp;Additional Hospital  Details&nbsp;&nbsp;
  <%
  	if(!(TTKCommon.getActiveLink(request).equals("Code CleanUp")))
  	{
  %>
  		<%if(TTKCommon.isAuthorized(request,"Edit")&&!"Y".equals(codingReviewYN.toString().trim())){%><a href="javascript:additionalhospdetails()"><img src="ttk/images/EditIcon.gif" title="Additional Hospital  Details" alt="Additional Hospital  Details" border="0" align="absmiddle"></a><%} %>
  <%
  	}//end of if(!("Code CleanUp".equals(TTKCommon.getActiveLink(request))))
  %>
  	</legend>		
    <table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0" id="additionalHospitalDetails" style="display: none;">
      <tr>
        <td width="35%" height="20" class="formLabel">Registration Number:</td>
        <td width="65%" class="textLabel">
          <bean:write name="frmPreAuthMedical" property="additionalHospitalDetailVO.regnNbr"/>
        </td>
      </tr>
      <tr>
        <td height="20" class="formLabel">Contact Name: </td>
        <td class="textLabel"><bean:write name="frmPreAuthMedical" property="additionalHospitalDetailVO.contactName"/></td>
      </tr>
      <tr>
        <td height="20" class="formLabel">Phone Number: </td>
        <td class="textLabel"><bean:write name="frmPreAuthMedical" property="additionalHospitalDetailVO.phoneNbr"/></td>
      </tr>
      <tr>
        <td height="20" class="formLabel">Number of Beds: </td>
        <td class="textLabel"><bean:write name="frmPreAuthMedical" property="additionalHospitalDetailVO.nbrOfBeds"/></td>
      </tr>
      <tr>
        <td height="20" class="formLabel">Fully Equipped: </td>
        <td class="textLabel"><bean:write name="frmPreAuthMedical" property="additionalHospitalDetailVO.fullyEquippedYN"/></td>
      </tr>
    </table>
  </fieldset>
</logic:notEmpty>
</logic:match>

<%
   //if(TTKCommon.isAuthorized(request,"SpecialPermission"))
   //{
%>

<%    if("Claims".equals(TTKCommon.getActiveLink(request)) || "Pre-Authorization".equals(TTKCommon.getActiveLink(request))||"DataEntryClaims".equals(TTKCommon.getActiveLink(request))||"DataEntryCoding".equals(TTKCommon.getActiveLink(request)))
    	{
    %>
<fieldset>
<legend>ICD / PCS Coding</legend>
    <!-- S T A R T : Grid -->

    <ttk:HtmlGrid name="MedicalTable"/>

    <!-- E N D : Grid -->
	<!-- S T A R T : Buttons and Page Counter -->
    <table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td width="52%" align="left">&nbsp;</td>
      <td width="48%" align="right">
         <logic:match name="strLink" value="Claims" >
       <%
          if(ClaimsWebBoardHelper.getAmmendmentYN(request).equals("N"))
	      {
	          if(TTKCommon.isAuthorized(request,"Edit"))
	          {
      %>
     	<logic:notEmpty name="frmPreAuthMedical" property="ailmentVO.ailmentDesc">
			<button type="button" name="Button2" accesskey="g" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onGenerateMedicalOpinion();"><u>G</u>enerate Medical Report</button>&nbsp;
		 </logic:notEmpty>
		 <%
          	  }
          }//end of if(ClaimsWebBoardHelper.getAmmendmentYN(request).equals("N"))
      %>
	  </logic:match>
      <!--<logic:match name="frmPreAuthMedical" property="codingReviewYN" value="Y">-->
      <%--<logic:match name="ActiveSubLink" value="PreAuth">--%>
      <%
        if("Pre-Authorization".equals(TTKCommon.getActiveLink(request)))
        {
      	if(TTKCommon.isAuthorized(request,"Add"))
          {
      %>
      		<button type="button" name="Button2" accesskey="a" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onAdd();"><u>A</u>dd</button>&nbsp;
      <%
          }
          if((TTKCommon.isDataFound(request,"MedicalTable"))&& (TTKCommon.isAuthorized(request,"Delete")))
          {
      %>
            <button type="button" name="Button2" accesskey="d" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onDeleteList();"><u>D</u>elete</button>&nbsp;
      <%
          }
        }
      %>
      <%--</logic:match>--%>
      <!--</logic:match>-->
      <!--<logic:match name="frmPreAuthMedical" property="codingReviewYN" value="Y">-->
      <%--<logic:match name="ActiveSubLink" value="Claims">--%>
      <%
      if("Claims".equals(TTKCommon.getActiveLink(request))||"DataEntryClaims".equals(TTKCommon.getActiveLink(request)))
      {
      %>
      <logic:match name="ActiveLink" value="Claims">
      <%if(ClaimsWebBoardHelper.getAmmendmentYN(request)!=null)
	      {
    	  	strAmmendment = ClaimsWebBoardHelper.getAmmendmentYN(request);
	      }
      %>
      </logic:match>
      <logic:match name="ActiveSubLink" value="Claims">
      <%if(CodingClaimsWebBoardHelper.getAmmendmentYN(request)!=null)
	      {
    	  	strAmmendment = CodingClaimsWebBoardHelper.getAmmendmentYN(request);
	      }
      %>
      </logic:match>
      <%if(strAmmendment.equals("N"))
	      {
	          if(TTKCommon.isAuthorized(request,"Add"))
	          {
      %>
 	     		<button type="button" name="Button2" accesskey="a" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onAdd();"><u>A</u>dd</button>&nbsp;
      <%
    	      }
	          if((TTKCommon.isDataFound(request,"MedicalTable"))&& (TTKCommon.isAuthorized(request,"Delete")))
	          {
      %>
            	<button type="button" name="Button2" accesskey="d" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onDeleteList();"><u>D</u>elete</button>&nbsp;
      <%
	        }
          }//end of if(ClaimsWebBoardHelper.getAmmendmentYN(request).equals("N"))
      }
      %>
      <%--</logic:match>--%>
      <!--</logic:match>-->
      </td>
      </tr>
    </table>
</fieldset>
<%
  }//end of if(TTKCommon.isAuthorized(request,"Special Permission"))
  //}
%>
</div>
<!-- E N D : Buttons and Page Counter -->
  <INPUT TYPE="hidden" NAME="mode" VALUE="">
  <input type="hidden" name="child" value="">
  <INPUT TYPE="hidden" NAME="rownum" VALUE="">
  <html:hidden property="ailmentVO.specialityDesc"/>
  <logic:match name="strLink" value="Claims" >
 	 <INPUT TYPE="hidden" NAME="claimSeqID" VALUE="<%=ClaimsWebBoardHelper.getClaimsSeqId(request) %>">
  </logic:match>
</html:form>

