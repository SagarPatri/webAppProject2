<%
/**
 * @ (#) premiumDetails.jsp Dec 10, 2020
 * Project 	     : ProjectX
 * File          : premiumDetails.jsp
 * Author        : Deepthi Meesala
 * Company       : RCS Technologies
 * Date Created  : Dec 10, 2020
 *
 * Modified by   :  
 * Modified date :  
 * Reason        :  
 */
%>

<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.ttk.common.security.Cache,com.ttk.common.TTKCommon,java.util.Date,java.text.SimpleDateFormat" %>
<%@page import="org.apache.struts.action.DynaActionForm"%>
	<script type="text/javascript" SRC="/ttk/scripts/validation.js"></script>
	<script type="text/javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script> 
    <script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
    <script type="text/javascript" src="/ttk/scripts/enrollment/premiumDetails.js"></script>
    <script type="text/javascript" src="/ttk/scripts/jquery/ttk-jquery.js"></script>
	<script type="text/javascript" src="/ttk/scripts/async.js"></script>
    <SCRIPT LANGUAGE="JavaScript">
	var JS_SecondSubmit=false;
    </SCRIPT> 
    
    
    
    <html:form action="/PremiumDetailsAction.do" >
    
    <html:errors/>
    <logic:notEmpty name="successMsg" scope="request">
				<table align="center" class="successContainer" border="0"
					cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/SuccessIcon.gif" title="Success"
							width="16" height="16" align="absmiddle">&nbsp; <bean:write
								name="successMsg" scope="request" /></td>
					</tr>
				</table>
			</logic:notEmpty>
    
    
    
    
    <div class="contentArea" id="contentArea">
    <fieldset>
    <legend>Add Premium Details</legend>
    <br>
    <legend>Premium Details Dashboard</legend>
     <table  border="1" cellspacing="0" cellpadding="0" class="gridWithCheckBox" style="margin-top: 15PX">
     <tr  class="gridHeader">
			<th align="center" style="width:10%;">S.NO</th>
			<th align="center" style="width:10%;">Minimum Age</th>
			<th align="center" style="width:10%;">Maximum Age</th>
			<th align="center" style="width:10%;">Gender</th>
			<th align="center" style="width:10%;">Premium</th>
			<th align="center" style="width:10%;">Delete</th>
			
			</tr>
			
			
		<%-- 	<tr>
			
		 <td align="center">
		<a href="#" accesskey="g"  onClick="javascript:doViewHistory('${historyList[0]}');"><c:out value="${historyList[1]}"/></a> 
		<!-- <c:out value="11"/> <a href="#" accesskey="g"  onClick="javascript:edit('1111');" alt="Edit Premium"></a> -->
		<a href="#" accesskey="g"  onClick="javascript:edit('1111');"><c:out value="1"/></a> 
		
		</td>
		<td></td>
		<td></td>
		<td></td>
		
		<td></td>
		
		
		<td>
		<a href="#" accesskey="g"  onClick="javascript:deletePremium('1');"><img src="/ttk/images/DeleteIcon.gif" alt="Delete Premium" width="11" height="11" border="0">
		</a>
		<a href="#" onClick="deleteProviderCopay(<bean:write name="hospitalCopayVO" property="copaySeqId"/>)">
				<img src="/ttk/images/DeleteIcon.gif" alt="Delete Provier Copay" width="16" height="16" border="0">
				</a>
		
		
		</td>
		
		
		
		
		
			
			</tr> --%>
			
		 	<%
java.util.ArrayList<String[]> premiumList=(java.util.ArrayList<String[]>)session.getAttribute("premiumList");
if(premiumList!=null&& premiumList.size() > 0 )
{
for(String[] strDetails:premiumList){
	
%>
	<tr>
	<td align="center">
	 <a href="#" accesskey="g"  onClick="javascript:edit('<%=strDetails[1] %>');">
	 <%=strDetails[0] %>
	 </a>
	</td>
	<td align="center">
	<%=strDetails[3] %>
	</td>
	<td align="center">
	<%=strDetails[4] %>
	</td>
	<td align="center">
	<%=strDetails[5] %>
	</td>
	
	
	<td align="center">
	<%=strDetails[6] %>
	</td>
	
	<td align="center">
		<a href="#" accesskey="g"  onClick="javascript:deletePremium('<%=strDetails[1] %>');">
		<img src="/ttk/images/DeleteIcon.gif" title="Delete Premium" width="11" height="11" border="0"></a>
	</td>
	
	</tr>
	<%	
}
}
%>		
     
    
     </table>
     
     <br><br><br>
     
    <table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
    
     <tr>
     <td nowrap>Minimun Age:<br>
    <%--  <html:text property="minimumAge" name="frmPremiumDetails" styleId="minimumAge" onkeyup="isPositiveIntegerPre(this,'Minimum Age');ageCompare();" /> --%>
   
   <html:text property="minimumAge" styleId="minimumAge" styleClass="textBox textBoxMedium" maxlength="5" onkeyup="isNumaricOnly(this)" />
   
     &nbsp; </td>
     
     
     <td nowrap>Maximum Age:<br>
   <%--   <html:text property="maximumAge" name="frmPremiumDetails" styleId="maximumAge" onkeyup="isPositiveIntegerPre(this,'Maximum Age');ageCompare();" /> --%>
  
  <html:text property="maximumAge" styleId="maximumAge" styleClass="textBox textBoxMedium" maxlength="5" onkeyup="isNumaricOnly(this)" />
  
  
    &nbsp; </td>
      
     
      <td nowrap>Gender:<br>
    <%--  <html:text property="minimumAge" styleClass="textBox textDate" /> --%>
     
     <html:select property="gender" name="frmPremiumDetails" styleClass="selectBox" styleId="gender">
	 <html:option value="ALL">ALL</html:option>
	 <html:option value="MAL">Male</html:option>
	 <html:option value="FEM">Female</html:option>
	</html:select>
      &nbsp;</td> 
    
     
     <td nowrap>Premium:<br>
     <html:text property="premium" name="frmPremiumDetails" styleId="premium"  onkeyup="isNumeric(this)" />
     </td>
     
    
     <td width="100%" align="center">
	 <button type="button" name="Button" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:addPremiumDtls()"><u>A</u>dd</button>&nbsp;
	 </td>
    
     
     
     
    </tr>
    </table>
    
    </fieldset>
    
    
    
    <table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
		  <tr>
		    <td width="100%" align="center">
		    
		      	<!-- <button type="button" name="Button" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onUserSubmit()"><u>S</u>ave</button>&nbsp; -->
				<button type="button" name="Button" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onReset()"><u>R</u>eset</button>&nbsp;
		        <button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose()"><u>C</u>lose</button>&nbsp;
		   
		    </td>
		</tr>
		</table>
    
    </div>
    
    
<INPUT TYPE="hidden" NAME="mode" VALUE="">
<input type="hidden" name="child" value=""> 
    
  <html:hidden property="policySeqID" value=""/>
  <html:hidden property="premiumSeqId" value=""/>  
  
  <%--  <html:hidden property="switchType" value=""/>   --%>
  <INPUT TYPE="hidden" NAME="switType" id="switType" value='<bean:write name="frmPolicyList" property="switchType"/>'/>
  <INPUT TYPE="hidden" NAME="memcountYN" id="memcountYN" value='<bean:write name="frmPolicyDetails" property="memcountYN"/>'/>
    
    </html:form>