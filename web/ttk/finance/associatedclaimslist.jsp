<%
/**
 * @ (#) claimsdebitlist.jsp 
 * Project      : TTK HealthCare Services
 * File         : associatedclaimslist.jsp
 * Author       : Balaji C R B
 * Company      : Span Systems Corporation
 * Date Created : 12th September 2007
 *
 * @author       :Balaji C R B
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
<script language="javascript" src="/ttk/scripts/finance/associatedclaimslist.js"></script>
<SCRIPT LANGUAGE="JavaScript">
	var TC_Disabled = true;
	var claimsAmt = new Array(<bean:write name="frmAssociatedClaims" property="strClaimsAmt" filter="false"/>);
	//var debitNoteAmt = <bean:write name="frmDebitNoteDetails" property="debitNoteAmt"/>;
</SCRIPT>
	<%
	    String strWebBoardId = String.valueOf(TTKCommon.getWebBoardId(request));
	    pageContext.setAttribute("WebBoardId", strWebBoardId);
		pageContext.setAttribute("claimType", Cache.getCacheObject("claimType"));
		pageContext.setAttribute("policyType", Cache.getCacheObject("enrollTypeCode"));
		pageContext.setAttribute("associatedList",Cache.getCacheObject("debitAssoc"));
		 //Changes added for Debit Note CR KOC1163
		pageContext.setAttribute("paymentMethod2", Cache.getCacheObject("paymentMethod2"));
	    // End changes added for Debit Note CR KOC1163
	%>
	<html:form action="/AssociatedClaimsSearchAction.do">
	<logic:match name="WebBoardId" value="null">
	<html:errors/>
	</logic:match>
	<logic:notMatch name="WebBoardId" value="null">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
  		<tr>
    		<td width="57%"><bean:write name="frmAssociatedClaims" property="caption"/></td>
   		</tr>
	</table>
	<!-- E N D : Page Title -->
	<div class="contentArea" id="contentArea">
	<html:errors/>
	
	<!-- S T A R T : Search Box -->
	<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
      <tr>
		<td nowrap>Claim Settlement No.:<br>
        <html:text  property="claimSettNo" styleClass="textBox textBoxMedium"  maxlength="60"/></td>

		<td nowrap>Enrollment Id:<br>
        <html:text property="enrollmentId" styleClass="textBox textBoxMedium" maxlength="60"/></td>

         <td nowrap>Member Name:<br>
            <html:text property="claimantName" styleClass="textBox textBoxMedium" maxlength="60"/>
        </td>
         <td nowrap>Claim Type:<br>
            <html:select property="claimType" styleClass="selectBox selectBoxMedium" >
				 <html:option value="">Any</html:option>
				 <html:optionsCollection name="claimType" label="cacheDesc" value="cacheId"/>
			</html:select>
		</td>
      </tr>
      <tr>
        
        <td nowrap>Policy Type:<br>
            <html:select property="policyType" styleClass="selectBox selectBoxMedium" >
				 <html:option value="">Any</html:option>
				 <html:optionsCollection name="policyType" label="cacheDesc" value="cacheId"/>
			</html:select>
		</td>
        <td nowrap>Payee:<br>
            <html:text property="payee" styleClass="textBox textBoxMedium" maxlength="60"/>
       </td>
       <td nowrap>Search In:<br>
            <html:select property="associatedList" styleClass="selectBox selectBoxMedium" >
				 <html:optionsCollection name="associatedList" label="cacheDesc" value="cacheId"/>
			</html:select>
		</td>
		<!-- Changes added for Debit Note CR KOC1163 -->
		<td nowrap>Bank A/c Details<br>
        	<html:select property="paymethod" styleClass="selectBox selectBoxMedium" onchange="enableField(this)" >
        		 <html:option value="">Any</html:option>
				 <html:optionsCollection name="paymentMethod2" label="cacheDesc" value="cacheId"/>
			</html:select>
		</td>
		<!-- End changes added for Debit Note CR KOC1163 -->
        <td width="100%" valign="bottom" nowrap>
        <a href="#" accesskey="s" onClick="javascript:onSearch()"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a></td>
      </tr>
    </table>
	<!-- E N D : Search Box -->

    <!-- S T A R T : Grid -->
    <ttk:HtmlGrid name="tableData"/>
    <!-- E N D : Grid -->

    <!-- S T A R T : Buttons and Page Counter -->
    <table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
		 <tr>
		    <td width="100%" nowrap align="right" colspan="2" >
		    <logic:notMatch property="debitType" name="frmDebitNoteDetails" value="DFL">
		    <%
		    if(TTKCommon.isDataFound(request,"tableData")&& TTKCommon.isAuthorized(request,"Add"))
		     {
		     %>
				<logic:match  property="associatedList" name="frmAssociatedClaims" value="DBU">
		      		<button type="button" name="Button1" accesskey="A" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onAssociateExclude('Associate')"><u>A</u>ssociate</button>&nbsp;
		    	</logic:match>
		    	<logic:notMatch property="sDebitType" name="frmDebitNoteList" value="Final">
		    	<logic:match property="associatedList" name="frmAssociatedClaims" value="DBA">
		     		 <button type="button" name="Button2" accesskey="E" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onAssociateExclude('Exclude')"><u>E</u>xclude</button>&nbsp;
		    	</logic:match>
		    </logic:notMatch>
		    <%
		   		 }//end of if(TTKCommon.isDataFound(request,"tableData")&& TTKCommon.isAuthorized(request,"Add"))
		    %>
		    </logic:notMatch>
		    <button type="button" name="Button3" accesskey="C" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onClose()"><u>C</u>lose</button>&nbsp;		      
          </td>
	  	</tr>
	  	<tr>
	  <ttk:PageLinks name="tableData"/>
	  </tr>
	</table>
    
    </div>
    </logic:notMatch>
    <!-- E N D : Buttons and Page Counter -->
    <INPUT TYPE="hidden" NAME="rownum" VALUE="">
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<input type="hidden" name="child" value="">
	</html:form>