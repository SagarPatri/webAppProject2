<%
/**
 * @ (#) home.jsp Dec 26, 2007
 * Project      : TTK HealthCare Services
 * File         : home.jsp
 * Author       : Raghavendra T M
 * Company      : Span Systems Corporation
 * Date Created : Dec 26, 2007
 *
 * @author       :
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile"%>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/onlineforms/home.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<%
	UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
String newEnroll	=	(String)request.getAttribute("NewEnroll")==null?"":(String)request.getAttribute("NewEnroll");
%>
<html:form action="/OnlineHomeAction.do" >
<%-- <logic:equal name="loginType" value="H">
<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td>Home page Information</td>
        <td align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
      </tr>
    </table>
</logic:equal> --%>
<!-- S T A R T : Content/Form Area -->

<logic:notEmpty name="openurl" scope="request">
<script language="JavaScript">
var w = screen.availWidth - 10;
var h = screen.availHeight - 90;
aletr("test");
var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
window.open("<bean:write name="openurl" scope="request"/>",'',features);                                                
</script>                              
                </logic:notEmpty>	
<logic:equal name="loginType" value="HOS">
<h4 class="sub_heading">Provider Dashboard</h4>
</logic:equal>
	
	<%
	if(userSecurityProfile.getLoginType().equals("PTR")  ) 
	{
	%>
	<h4 class="sub_heading">Partner Dashboard</h4>
	<%} %>
<br>

	<!-- S T A R T : Page Title -->
<html:errors/>
<%if(newEnroll!=""){ %>
<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
  <tr>
  <td style="color: red;"><%= newEnroll%></td>
</tr>
</table>
<%} %>
<!-- S T A R T : Success Box -->
	<%
	if(userSecurityProfile.getLoginType().equals("E") && request.getAttribute("pwdalert") !=null ) 
	{
	%>
	<table align="center" class="errorContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td><img src="/ttk/images/ErrorIcon.gif" title="Alert" alt="Alert" width="16" height="16" align="absmiddle">&nbsp;
	          <% out.println(request.getAttribute("pwdalert"));  %>
	        </td>
	      </tr>
	       
   	 </table>
    <%
    }//end of if(userSecurityProfile.getLoginType().equals("E") && request.getAttribute("pwdalert") !=null )
    %>
    <%
	if(userSecurityProfile.getLoginType().equals("E") && request.getAttribute("windowprdalert") !=null ) 
	{
	%>
	<table align="center" class="errorContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
	       <tr>
	        <td><img src="/ttk/images/ErrorIcon.gif" title="Alert" alt="Alert" width="16" height="16" align="absmiddle">&nbsp;
	          <% out.println(request.getAttribute("windowprdalert"));  %>
	        </td>
	      </tr>
   	 </table>
    <%
    }//end of if(userSecurityProfile.getLoginType().equals("E") && request.getAttribute("windowprdalert") !=null )
    	
        
    %>
    
     <%
	if(userSecurityProfile.getLoginType().equals("HOS")  ) 
	{
	%>
	
	 <ttk:OnlineHospHomeDetails/>
	 <br> 
	 <table width="98%" align="center"  border="0" cellspacing="0" cellpadding="0" >
	
	<tr>
		<td align="center">
			 <button type="button" name="Button2" accesskey="p" class="olbtn" onClick="onProceed()"><u>P</u>roceed</button>
		</td>
	</tr>

	</table>
	<br>
    <%
  
    }//end of if(userSecurityProfile.getLoginType().equals("HOS")
    		
    if(userSecurityProfile.getLoginType().equals("PTR")  ) 
	{
	%>	
	 <ttk:OnlinePartnerHomeDetails/>
	 <% String limit = (String)request.getSession().getAttribute("limit"); %>
	 <br> 
	 <table width="98%" align="center"  border="0" cellspacing="0" cellpadding="0" >
	
	<tr>
		<td align="center">
			 <button type="button" name="Button2" accesskey="p" class="olbtn" onClick="onProceed1()"><u>P</u>roceed</button>
		</td>
	</tr>
	</table>
	<table width="98%" align="center"  border="0" cellspacing="0" cellpadding="0" >
	<tr>
	</tr>
		<tr>
			<td width="20%" class="formLabel">NOTE: Obtaining Approval Is Mandatory For Requested Amount i.e more than limit: <%= limit%></td>
    				<td width="30%" class="textLabelBold">
    				</td>	
		</tr>
	</table>
	<br>
    <%
    }//end of if(userSecurityProfile.getLoginType().equals("HOS")
    
    //kocb 
         else if(userSecurityProfile.getLoginType().equals("B"))
    	{
   %>
    		<table align="center" class="searchContainer" border="0" cellspacing="3" cellpadding="3">
    	      <tr>
    	      	
    	        <td width="25%" nowrap>Policy No.:<br>
    	            <html:text property="sPolicyNumber" name="frmOnlineHome" styleClass="textBoxWeblogin textBoxMediumWeblogin"  styleId="search2" maxlength="60" onkeypress="javascript:blockEnterkey(event.srcElement);"/>
    	        </td>
    	        <td nowrap>Group/Company Id:<br>
    	            	<html:text property="sGroupId" name="frmOnlineHome" styleClass="textBoxWeblogin textBoxMediumWeblogin"  styleId="search3" maxlength="60"/>
    	        </td>
    	        <td nowrap>Group/Company Name:<br>
    		        <html:text property="sGroupName" styleClass="textBoxWeblogin textBoxMediumWeblogin" styleId="search4" maxlength="40"/>
    			</td>
    		    	
    			<td>
    	        <a href="#" accesskey="s" onClick="javascript:onSearch()" class="search">
    		        <img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch
    	        </a>
    	    	</td>
    	    	 
    	    	
    	    	
    			</tr>				
    	    </table>
    	    <!-- div class="contentArea" id="contentArea"--><br>

  
<!-- S T A R T : Grid -->
		<ttk:HtmlGrid name="tableData"/>
	<!-- E N D : Grid -->
    <!-- S T A R T : Buttons and Page Counter -->
	<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="27%"></td>
  	    <ttk:PageLinks name="tableData"/>
  	  </tr>
	</table>
<!-- /div>-->


    <% 	}
    	else
    	{
     %>
   	 <ttk:OnlineHomeDetails/>
     
   	 <%}
    //kocbroker
    	if(userSecurityProfile.getLoginType().equals("B"))
    	{  
    %>
    <br>
   	 <table align="left"   border="0" cellspacing="0" cellpadding="0">
            <tr>
            	<td><b>Note :&nbsp;</b></td>
                  <td class="textLabel">To Navigate DashBoard details, please click on the Policy number.  
                  </td>
            
     </tr>
</table>
   <%} %>	 
<logic:equal name="loginType" value="H">
<fieldset align="center">
				<legend>Login Details</legend>
<div align="center" style="border: 1px solid gray;border-radius: 20px;padding: 20px 60px 20px 40px;background-color: #F8F8F8;width: 85%;margin-left: 5px;">
<table cellspacing="10" class="formContainerWeblogin">
    <tr>
  <th>Corporate Name</th><th>:</th><td><bean:write  property="corpName" name="hmCorporateDetails" scope="session"/> </td>
  <td></td>  
 </tr>
 <tr>
 <th width="25%">Corporate ID</th><th>:</th><td><bean:write property="corpId" name="hmCorporateDetails" scope="session"/> </td>
 <th width="20%">Address</th><th>:</th><td><bean:write property="address"  name="hmCorporateDetails" scope="session"/> </td>
 </tr>
 <tr>
 <th>Co-ordinator Mail ID(primary)</th><th>:</th><td> <bean:write property="mailPId" name="hmCorporateDetails"  scope="session"/> </td>
 <th>HR-Mail ID(secondary)</th><th>:</th><td> <bean:write property="mailSId" name="hmCorporateDetails" scope="session"/></td>
 </tr>
 <tr>
 <th>Area</th><th>:</th><td> <bean:write property="area" name="hmCorporateDetails" scope="session"/> </td>
 <th>City</th><th>:</th><td> <bean:write property="city" name="hmCorporateDetails" scope="session"/></td>
 </tr>
 <tr>
 <th>Country</th><th>:</th><td> <bean:write property="country" name="hmCorporateDetails" scope="session"/> </td>
 <th>Office Phone</th><th>:</th><td> <bean:write property="officeNO" name="hmCorporateDetails" scope="session"/> </td>
 </tr>
</table>
 </div>
</fieldset>
<table align="center">
<tr align="center">
 <td align="center" colspan="6">
 <div style="text-align: center;">
 <button type="button" name="mybutton" accesskey="p"  style="border-radius:25px;" class="olbtn" onClick="onHRProceed();"><u>P</u>roceed</button>
 </div>
 </td>
 </tr>
 
 </table>





</logic:equal>

<!--E N D : Content/Form Area -->
<!-- E N D : Main Container Table --> 
<INPUT TYPE="hidden" NAME="mode" VALUE="">
<INPUT TYPE="hidden" NAME="leftlink" VALUE="">
<INPUT TYPE="hidden" NAME="sublink" VALUE="">
<INPUT TYPE="hidden" NAME="tab" VALUE="">
<INPUT TYPE="hidden" NAME="seqID" VALUE="">
<INPUT TYPE="hidden" NAME="fileName" VALUE="">
<INPUT TYPE="hidden" NAME="rownum" VALUE="">
<INPUT TYPE="hidden" NAME="pageId" VALUE="">
<INPUT TYPE="hidden" NAME="sortId" VALUE="">
<INPUT TYPE="hidden" NAME="historymode" VALUE="">
<input type="hidden" name="child" value="">
<input type="hidden" name="loginType" value="H">
</html:form>

