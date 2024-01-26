<%
/** @ (#) claimsDetailedReport.jsp
 * Project     : TTK Healthcare Services
 * File        : claimsDetailedReport.jsp
 * Author      : Deepthi Meesala
 * Company     : Vidal Health
 * Date Created: 28 March 2022
 *
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
pageContext.setAttribute("sStatus", Cache.getCacheObject("autoRejclaimStatusList"));
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

<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
 <tr>
<td nowrap>Treatment Start date:<span class="mandatorySymbol">*</span><br>
	
  <html:text name="frmClaimDetailedReports" property="tmtfromDate" maxlength="10" styleClass="textBoxDate"/>
  <a NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate"
												HREF="#" onClick="javascript:show_calendar('CalendarObjectPARDate','forms[1].tmtfromDate',document.forms[1].tmtfromDate.value,'',event,148,178);return false;"
												
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"><img
												src="/ttk/images/CalendarIcon.gif" alt="Calendar"
												name="tmtfromDate" width="24" height="17" border="0"
												align="absmiddle"></a>
  </td>
 
  <td nowrap>Treatment End date:<span class="mandatorySymbol">*</span><br>
  <html:text name="frmClaimDetailedReports" property="tmttoDate" maxlength="10" styleClass="textBoxDate"/>
  <a NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate"
												HREF="#"
												onClick="javascript:show_calendar('CalendarObjectPARDate','forms[1].tmttoDate',document.forms[1].tmttoDate.value,'',event,148,178);return false;"
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"><img
												src="/ttk/images/CalendarIcon.gif" alt="Calendar"
												name="toDate" width="24" height="17" border="0"
												align="absmiddle"></a>
  </td>
  <td nowrap>Claim Submission From Date:<span class="mandatorySymbol">*</span><br>
  <html:text name="frmClaimDetailedReports" property="clmFromDate" maxlength="10" styleClass="textBoxDate"/>
  <a NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate"
												HREF="#"
												onClick="javascript:show_calendar('CalendarObjectPARDate','forms[1].clmFromDate',document.forms[1].clmFromDate.value,'',event,148,178);return false;"
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"><img
												src="/ttk/images/CalendarIcon.gif" alt="Calendar"
												name="clmFromDate" width="24" height="17" border="0"
												align="absmiddle"></a>
  </td>
  
  <td nowrap>Claim Submission To Date:<span class="mandatorySymbol">*</span><br>
  <html:text name="frmClaimDetailedReports" property="clmToDate" maxlength="10" styleClass="textBoxDate"/>
  <a NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate"
												HREF="#"
												onClick="javascript:show_calendar('CalendarObjectPARDate','forms[1].clmToDate',document.forms[1].clmToDate.value,'',event,148,178);return false;"
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"><img
												src="/ttk/images/CalendarIcon.gif" alt="Calendar"
												name="toDate" width="24" height="17" border="0"
												align="absmiddle"></a>
  </td>
  
   <td nowrap>Al Koot Id:<br>
	  <html:text property="alKootId" name="frmClaimDetailedReports" styleClass="textBox textBoxMedLarge"/>
	  </td>
 </tr>
 <tr>
 <td nowrap>Provider Name: <span class="mandatorySymbol">*</span><br>
		    <html:select property="sProviderName" name="frmClaimDetailedReports"  styleClass="selectBox selectBoxMoreMedium">
		    	 <html:option value="">Any</html:option>
  				<html:options collection="ProviderList"  property="cacheId" labelProperty="cacheDesc"/>
		    </html:select>
        </td>
	    
       
        
 	 <td nowrap>Claim Status:<br>
	    <html:select property="claimStatus" name="frmClaimDetailedReports" styleClass="selectBox selectBoxMedium">
		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="sStatus" label="cacheDesc" value="cacheId" />
        </html:select>
      </td>
      
	   <td nowrap>Claims Batch Number:<br>
	  <html:text property="batchNo" name="frmClaimDetailedReports" styleClass="textBox textBoxMedLarge"/>
	  </td>
	
	
	  <td nowrap>Batch Reference Number:<br>
	  <html:text property="batchRefNo" name="frmClaimDetailedReports" styleClass="textBox textBoxMedLarge" />
	  </td>
	  
	   
	  <td nowrap>Claim Number:<br>
	  <html:text property="claimNo" name="frmClaimDetailedReports" styleClass="textBox textBoxMedLarge"/>
	  </td>
	
	
	
	<!--  <td>
	  	<a href="#" accesskey="s" onClick="javascript:onClmDetailSearch(this)"   class="search"><img src="/ttk/images/SearchIcon.gif" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>
	  </td>	 -->
	
	</tr>
	 
	  
	  
	  
	 
</table>
	




	<div align="center">
	<table align="center">
	<tr>
	<td colspan="4">&nbsp;</td>
     	<td colspan="4">&nbsp;</td>
     	<td colspan="4">&nbsp;</td>
	
 <td colspan="4">&nbsp;</td><br>
 <td colspan="4"><button name="mybutton" class="olbtnLarge" accesskey="d" onclick="onExportToResubmissionExcel();" type="button"><u>E</u>xport to Excel</button></td>

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

