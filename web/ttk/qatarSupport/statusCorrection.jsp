<%
/**
 * @ (#)statusCorrection.jsp
 * Project       : TTK HealthCare Services
 * File          : statusCorrection.jsp
 * Author        : Deepthi Meesala
 * Company       : RCS
 * Date Created  : 4th July 2019
 *
 * @author       : Deepthi Meesala
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>
 
 
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ page import=" com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>


<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/qatarSupport/statusCorrection.js"></SCRIPT>

<%
	pageContext.setAttribute("listfloatType",Cache.getCacheObject("floatType"));
	pageContext.setAttribute("listacctStatus",Cache.getCacheObject("acctStatus"));
	pageContext.setAttribute("listofficeInfo",Cache.getCacheObject("officeInfo"));
	pageContext.setAttribute("listinsuranceCompany",Cache.getCacheObject("insuranceCompany"));
	pageContext.setAttribute("paymentTypefin", Cache.getCacheObject("paymentTypefin"));
	pageContext.setAttribute("claimType", Cache.getCacheObject("claimType"));
	pageContext.setAttribute("paymentMethod", Cache.getCacheObject("paymentMethod1"));
	
%>


<SCRIPT LANGUAGE="JavaScript">
bAction = false;
var TC_Disabled = true;
</script>

	<html:form action="/StatusCorrectionAction.do"  method="post" enctype="multipart/form-data">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="57%">List of Float Accounts</td>
    <td align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
  </tr>
</table>
	<!-- E N D : Page Title -->
<div class="contentArea" id="contentArea">
	<html:errors />
	
	<logic:notEmpty name="updated" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="absmiddle">&nbsp; 
						<bean:message name="updated" scope="request" />
					</td>
				</tr>
			</table>
		</logic:notEmpty> 
	
	<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
      <tr>
      
       <td nowrap>Claim Settlement No.:<br>
            <html:text property="claimSettleNumber" styleClass="textBox textBoxMedium" maxlength="60"/>
         </td>
      
      
       <td nowrap> Payment Method:<br>
        	<html:select property="paymentMethod" styleClass="selectBox selectBoxMedium" onchange="enableField(this)" >
        	 <%-- <html:option value="">Any</html:option>  --%>
				 <html:optionsCollection name="paymentMethod" label="cacheDesc" value="cacheId"/>
			</html:select>
		</td>
      
      
      
        <td nowrap>Claim Type:<br>
            <html:select property="claimType" styleClass="selectBox selectBoxMedium" >
				  <html:option value="">Any</html:option>
				 <html:option value="CTM">Member</html:option>
				 <html:option value="CNH">Network</html:option>
				 <html:option value="PTN">Partner</html:option>
				 
			</html:select>
		</td>
      
        <td nowrap>Finance Status:<br>
            <html:select property="financeStatus" styleId="financeStatus" styleClass="selectBox selectBoxMedium" >
				<html:option value="SENT_TO_BANK">APPROVED BY FINANCE</html:option>
				<html:option value="READY_TO_BANK">READY_TO_BANK</html:option>
			</html:select>
		</td> 

      </tr>
      <tr>
       
       <td nowrap>Payment Advice Batch No.:<br>
            <html:text property="batchNo" styleId="batchNo" styleClass="textBox textBoxMedium" maxlength="60"  onkeyup="isNumaricOnly(this)" />
         </td>
      
      
        <td nowrap>Payment Advice Batch Date.:<br>
            <html:text property="batchDate" styleId="batchDate" styleClass="textBox textDate" maxlength="10"  />
             <A NAME="CalendarObjectempDate" ID="CalendarObjectempDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectempDate','forms[1].batchDate',document.forms[1].batchDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="dvReceivedDate" width="24" height="17" border="0" align="absmiddle" ></a>
         </td>    
       
         <td valign="bottom" nowrap><a href="#" accesskey="s" onClick="javascript:onSearch()"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a></td>        
	 </tr>
    </table>

	<!-- E N D : Search Box -->
    <!-- S T A R T : Grid -->
    <ttk:HtmlGrid name="tableData"/>
    <!-- E N D : Grid -->
    <!-- S T A R T : Buttons and Page Counter -->
 <!-- S T A R T : Buttons and Page Counter -->
    <table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
    
    <ttk:PageLinks name="tableData"/>
    
	  <tr>
	  
	  <td align="right" height="28" nowrap class="fieldGroupHeader" colspan="3">Ticket NO./Remarks:<br> </td>
	<td>
	     <html:textarea property="remarks" styleClass="textBox textAreaMediumht" />
	  
	  </td>
	  
	   <!--  <td width="73%" nowrap>&nbsp;
	    </td> -->
	    <td align="right" nowrap>

		<button type="button" name="Button2" accesskey="a" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onStatusCorrection()"><u>R</u>evert to Payments</button>&nbsp;
	    	
	    </td>
	  </tr>
  		
	</table>
   
   <table align="center"   border="0" cellspacing="0" cellpadding="0"></table>
   
   
   <table align="center"   border="0" cellspacing="0" cellpadding="0">
   
    <tr>
         <td><font color="color:#495879;">Show Template :</font><a href="#" onClick="javascript:showFinanceTemplate()" >Status Correction Report</a><br/><br/><br/></td>
  	</tr>	
      <tr>
      	<td width="100%" height="28" nowrap class="fieldGroupHeader">
      	File Name : <html:file property="stmFile"/>  
      	</td>
        <td>&nbsp;</td>
       
      </tr>
      <tr></tr>
    <tr>
        <td width="100%" height="28" nowrap class="fieldGroupHeader">
      		<!--<ttk:PageLinks name="tableData"/> 	-->
 		      	<button type="button" name="uploadButton" accesskey="u" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'"  onClick="javascript:onUploadClaimSettlementNumber()"><u>U</u>pload File</button>
 		</td>
        <td>&nbsp;</td>
	
        </tr>   
   </table>
   
<table align="center" border="0" cellspacing="0" cellpadding="0" class="buttonsContainerGrid"> 
       
       <tr> <td>  </td>
      
        
        <td>
  		<logic:equal value="Y"  property="sussessYN" name="frmStatusCorrection">
	<fieldset style="width: 90%"> <legend>Summary of your latest data uploaded</legend>
	 <table align="center"  border="0" cellspacing="0" cellpadding="0" style="width: 70%"> 
	    
			    <tr> 
			    	<td class="formLabel" width="50%"  align="left"> Total No. of Claims Uploaded </td> 
			    	<td width="50%" align="center"> <bean:write name="frmStatusCorrection" property="totalNoOfRows"/>  </td>
			    </tr><tr>
			    	<td class="formLabel" width="50%" align="left"> Total No. of Claims Success </td> 
			    	<td width="50%" align="center"> <bean:write name="frmStatusCorrection" property="totalNoOfRowsPassed"/>  </td>
			    </tr><tr>
			    	<td class="formLabel" width="50%" align="left"> Total No. of Claims Failed </td> 
			    	<td width="50%" align="center"> <bean:write name="frmStatusCorrection" property="totalNoOfRowsFailed"/>  </td>
			    </tr>
			    <tr><td>&nbsp;</td></tr> 
    <tr>
    <td colspan="2"> 
	<%-- <logic:notEqual value="0" property="totalNoOfRowsFailed" name="frmClaims"> --%>
		<a href="#" onclick="javascript:onDownloadPaymentUploadLog();">Click here</a> to download error log. 
	<%-- </logic:notEqual> --%>	
	</td> </tr>
	</table>
	</fieldset>
	</logic:equal>
        </td>
        </tr>
        </table>   
   
	<!-- E N D : Buttons and Page Counter -->

	<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	
 	</div>
	</html:form>