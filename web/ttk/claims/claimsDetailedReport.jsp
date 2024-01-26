<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.HashMap,java.util.ArrayList,com.ttk.common.PreAuthWebBoardHelper"%>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile,com.ttk.dto.administration.WorkflowVO"%>
<head>
<style type="text/css">

table#textTable{
margin-left: 20px;
width: 500px;
display: inline;
}
table#textTable,table#textTable tr td{
border: 1px solid white;
background-color: #F0F0F0;
border-collapse: collapse;
}

table#textTable tr td{
text-align:left;
font-size: 18px;
padding: 20px;
}
#hexagonLinks{
display: inline;
float: right;
list-style: none;
}
#onlineTableTD{
text-align: center;
font-size: 14px;
}
#onlineSearchEnterTable{
border: none;
padding: 0;
}

.contentAreaHScroll {
    overflow: scroll;
    width: 95%;
    padding: 0px 0px 0px 7px;
    scrollbar-face-color: #bad2f4;
    scrollbar-shadow-color: #f0f2f8;
    scrollbar-darkshadow-color: #8290a2;
    scrollbar-highlight-color: #f0f2f8;
    scrollbar-3dlight-color: #ced3d8;
    scrollbar-track-color: #f0f2f8;
    scrollbar-arrow-color: #022665;
}

.gridWithCheckBox {
    border-top: 1px solid #A0A8BB;
    border-right: 1px solid #A0A8BB;
    border-bottom: 1px solid #A0A8BB;
    width: 95%;
    margin-top: 10px;
    text-align: left;
}



</style>
<script type="text/javascript" src="/ttk/scripts/validation.js"></script>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<SCRIPT type="text/javascript"  SRC="/ttk/scripts/claims/claimsReportList.js"></SCRIPT>
</head>
<%
pageContext.setAttribute("sStatus", Cache.getCacheObject("claimStatusList"));
pageContext.setAttribute("benefitType",Cache.getCacheObject("benefitTypes"));
pageContext.setAttribute("ProviderList",Cache.getCacheObject("ClmProviderList"));
pageContext.setAttribute("partnerName", Cache.getCacheObject("partnerNm"));
%>

<html:form action="/ProviderReportsAction.do" >

<table align="center" class="pageTitle" border="0" cellspacing="0"
			cellpadding="0">
			<tr>
				<td width="57%">Claim Detailed Report</td>
			</tr>

		</table>
<html:errors/>
<!-- <div id="sideHeadingMedium">Summary Reports</div> -->



<!-- <fieldset>
	<legend>Claim Detailed Report</legend> -->
<!-- <div align="center" style="border: 1px solid gray;border-radius: 20px;padding: 10px 20px 10px 20px;background-color: #F8F8F8;width: 97%;"> -->

 <div class="contentArea" id="contentArea">
<table align="center" class="tablePad"  border="0" cellspacing="0" cellpadding="0">
	 <tr>
				<td width="07%" nowrap class="textLabelBold">Switch To:</td>
		        <td width="90%">
			    <html:select property="switchType" styleId="switchType" name="frmClaimDetailedReports" styleClass="selectBox selectBoxMedium" onchange="onSwitch();">
		    		 <html:option value="QPR">Local Providers</html:option>
		    		 <html:option value="IPTR">International Partners</html:option>
		    	</html:select>
				</td>
	</tr>
</table>
<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
 <tr>
<td nowrap>Treatment Start date:<span class="mandatorySymbol">*</span><br>
	
  <html:text name="frmClaimDetailedReports" property="tmtfromDate" styleId="tmtfromDate" maxlength="10" styleClass="textBoxDate"/>
  <a NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate"
												HREF="#" onClick="javascript:show_calendar('CalendarObjectPARDate','forms[1].tmtfromDate',document.forms[1].tmtfromDate.value,'',event,148,178);return false;"
												
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"><img
												src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"
												name="tmtfromDate" width="24" height="17" border="0"
												align="absmiddle"></a>
  </td>
 
  <td nowrap>Treatment End date:<span class="mandatorySymbol">*</span><br>
  <html:text name="frmClaimDetailedReports" property="tmttoDate" styleId="tmttoDate" maxlength="10" styleClass="textBoxDate"/>
  <a NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate"
												HREF="#"
												onClick="javascript:show_calendar('CalendarObjectPARDate','forms[1].tmttoDate',document.forms[1].tmttoDate.value,'',event,148,178);return false;"
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"><img
												src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"
												name="toDate" width="24" height="17" border="0"
												align="absmiddle"></a>
  </td>
  <td nowrap>Claim Submission From Date:<span class="mandatorySymbol">*</span><br>
  <html:text name="frmClaimDetailedReports" property="clmFromDate" styleId="clmFromDate" maxlength="10" styleClass="textBoxDate"/>
  <a NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate"
												HREF="#"
												onClick="javascript:show_calendar('CalendarObjectPARDate','forms[1].clmFromDate',document.forms[1].clmFromDate.value,'',event,148,178);return false;"
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"><img
												src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"
												name="clmFromDate" width="24" height="17" border="0"
												align="absmiddle"></a>
  </td>
  
  <td nowrap>Claim Submission To Date:<span class="mandatorySymbol">*</span><br>
  <html:text name="frmClaimDetailedReports" property="clmToDate" styleId="clmToDate" maxlength="10" styleClass="textBoxDate"/>
  <a NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate"
												HREF="#"
												onClick="javascript:show_calendar('CalendarObjectPARDate','forms[1].clmToDate',document.forms[1].clmToDate.value,'',event,148,178);return false;"
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"><img
												src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"
												name="toDate" width="24" height="17" border="0"
												align="absmiddle"></a>
  </td>
  
 </tr>
 <tr>
 <td nowrap>Provider Name:<span class="mandatorySymbol">*</span> <br>
		    <html:select property="sProviderName" styleId="sProviderName" name="frmClaimDetailedReports"  styleClass="selectBox selectBoxMoreMedium">
		    	 <html:option value="">Select from list</html:option>
  				<html:options collection="ProviderList"  property="cacheId" labelProperty="cacheDesc"/>
		    </html:select>
        </td>
	    <logic:equal property="switchType" name="frmClaimDetailedReports" value="IPTR">
        <td nowrap>Partner Name:<br>
	    <html:select property="partnerNm" styleId="" name="frmClaimDetailedReports" styleClass="selectBox selectBoxMedium">
		  	 		<html:option value="">Select from list</html:option>
		        	<html:optionsCollection name="partnerName" label="cacheDesc" value="cacheId" />
        </html:select>
      	</td>
       </logic:equal> 
        <td nowrap>Patient Name:<br>
	  <html:text property="patientName" styleId="patientName" name="frmClaimDetailedReports" styleClass="textBox textBoxMedLarge"/>
 </td>
        
 	 <td nowrap>Claim Status:<br>
	    <html:select property="claimStatus" styleId="claimStatus" name="frmClaimDetailedReports" styleClass="selectBox selectBoxMedium">
		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="sStatus" label="cacheDesc" value="cacheId" />
        </html:select>
      </td>
	   
	  <td nowrap>Invoice Number:<br>
	  <html:text property="invoiceNo" styleId="invoiceNo" name="frmClaimDetailedReports" styleClass="textBox textBoxMedLarge"/>
	  </td>
	</tr>
	  <tr>
		<td nowrap>Claims Batch Number:<br>
	  <html:text property="batchNo" styleId="batchNo" name="frmClaimDetailedReports" styleClass="textBox textBoxMedLarge"/>
	  </td>
	  
	  <td nowrap>Al Koot Id:<br>
	  <html:text property="alKootId" styleId="alKootId" name="frmClaimDetailedReports" styleClass="textBox textBoxMedLarge"/>
	  </td>
	  
	  <td nowrap>Claim Number:<br>
	  <html:text property="claimNo" styleId="claimNo" name="frmClaimDetailedReports" styleClass="textBox textBoxMedLarge"/>
	  </td>
	  <td nowrap>Benefit Type:<br>
	  <html:select property="benefitType" styleClass="selectBox selectBoxMedium" styleId="benefitType" name="frmClaimDetailedReports">
           			<html:option value="">Select from list</html:option>
           			<html:options collection="benefitType" property="cacheId" labelProperty="cacheDesc"/>
       </html:select>
	  </td>
	  </tr>
	  <tr>
      <td nowrap>Event Reference Number:<br>
	  <html:text property="eventRefNo" styleId="eventRefNo" name="frmClaimDetailedReports" styleClass="textBox textBoxMedLarge" style="background-color: #EEEEEE;" readonly="true"/>
	  </td>
	  
	   <td nowrap>Qatar Id:<br>
	  <html:text property="qatarId" styleId="qatarId" name="frmClaimDetailedReports" styleClass="textBox textBoxMedLarge" style="background-color: #EEEEEE;" readonly="true"/>
	  </td>
	  
	  <td nowrap>Payment Reference Number:<br>
	  <html:text property="payRefNo" styleId="payRefNo" name="frmClaimDetailedReports" styleClass="textBox textBoxMedLarge"/>
       <!-- <a href="#"><button name="mybutton" class="olbtn" accesskey="s" onclick="return onSearch();" type="button"><u>S</u>earch</button></a> -->  
	  </td>
	 
	<logic:notEqual property="switchType" name="frmClaimDetailedReports" value="IPTR"> 
	  <td nowrap>Finance Batch Number:<br>
	  <html:text property="finBatchNo" styleId="finBatchNo" name="frmClaimDetailedReports" styleClass="textBox textBoxMedLarge"/>
	  </td>
	  <td nowrap>Finance Status:<br>
	    <html:select property="financeStatus" styleId="financeStatus" name="frmClaimDetailedReports" styleClass="selectBox selectBoxMedium">
		  	 		<html:option value="">Select from list</html:option>
		        	<html:option value="READY_TO_BANK">Ready To Bank</html:option>
		        	<html:option value="SENT_TO_BANK">Sent To Bank</html:option>
		        	<html:option value="PENDING">Pending</html:option>
		        	<html:option value="PAID">Paid</html:option>
        </html:select>
        
      </td>
    </logic:notEqual>  
	  <td>
	  	<a href="#" accesskey="s" onClick="javascript:onClmDetailSearch(this)"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>
	  </td>	
 </tr>
</table>
	
<!-- </div> -->

<!-- </fieldset> -->


<!-- E N D : Search Box -->
	<!-- S T A R T : Grid -->
		<ttk:HtmlGrid name="claimDetailedReport"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<!-- E N D : Grid -->
	<!-- S T A R T : Buttons and Page Counter -->
	<table  align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
     	<tr>
     	
     	<td><ttk:PageLinks name="claimDetailedReport"/></td>
     	</tr>	    	
	</table>
	<div align="center">
	<table align="center">
	<tr>
	<td colspan="4">&nbsp;</td>
     	<td colspan="4">&nbsp;</td>
     	<td colspan="4">&nbsp;</td>
	
 <td colspan="4">&nbsp;</td>
  <logic:equal property="switchType" name="frmClaimDetailedReports" value="QPR">
	  <td colspan="4"><button name="mybutton" class="olbtnLarge" accesskey="d" onclick="onExportToExcel();" type="button"><u>E</u>xport to Excel</button>
  </logic:equal>
   <logic:equal property="switchType" name="frmClaimDetailedReports" value="IPTR">
	  <td colspan="4"><button name="mybutton" class="olbtnLarge" accesskey="d" onclick="onExportToExcel();" type="button"><u>E</u>xport to Excel</button>
  </logic:equal>
  </td>
</table>




<!-- <div align="left">
<table>
<tr><td><b>NOTE:-</b></td></tr>
<tr><td><b><small>1)Wherever the amount approved or paid is as per the requested or net amount, please IGNORE the denial reasons, if any</small></b></td></tr>
<tr><td><b><small>2)Where the payment confirmation is not available with us, the reference number is not provided. For more details regarding the payment please contact Al Koot.</small></b></td></tr>  	
</table>
</div> -->

</div>
</div>
<!-- </div> -->
<input type="hidden" name="mode" value="">
<input type="hidden" name="rownum" value="">
<input type="hidden" name="sortId" value="">
<input type="hidden" name="pageId" value="">
<input type="hidden" name="child" value="">
<input type="hidden" name="backID" value="">
<input type="hidden" name="tab" value="">
</html:form>

