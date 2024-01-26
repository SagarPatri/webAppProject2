<%
/**
 * @ (#) hospsearch.jsp 12th Sep 2005
 * Project      : TTK HealthCare Services
 * File         : hospsearch.jsp
 * Author       : Srikanth H M
 * Company      : Span Systems Corporation
 * Date Created : 12th Sep 2005
 *
 * @author       :
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/onlineforms/EmployeeLogin/hospsearch.js"></SCRIPT>

<SCRIPT LANGUAGE="JavaScript">
bAction = false;
var TC_Disabled = true;
</SCRIPT>

<style type="text/css">
.searchData_class {
    width: 80%;
    padding-left: 150px;
}

.searchList_class {
    width: 87%;
    margin-left: 101px;
}
.text_data_class{
     padding-left: 13%; 
}

/*media screen*/
.button_design_class {
    height: 35px;
    padding-left: 10px;
    padding-right: 10px;
    width: 14%;
}


.searchContainer td {
    padding-left: 54px;
    padding-right: 54px;
    padding-top: 7px;
    padding-bottom: 7px;
}



/* .searchContainer {
    color: #000000;
    border: 1px solid #000000;
    background-color: #EBEDF2;
    margin-top: 0px;
    padding: 16px;
    padding-bottom: 10px;
    padding-left: 125px;
    width: 98%;
} */
/* .query_div_class{
width: 12%;
padding-left: 45%;
} */

</style>

<%
	pageContext.setAttribute("subStatus",Cache.getCacheObject("subStatus"));
	/* pageContext.setAttribute("empanelStatusCode",Cache.getCacheObject("empanelStatusCode")); */
	pageContext.setAttribute("stateCode",Cache.getCacheObject("stateCode"));
	pageContext.setAttribute("cityCode",Cache.getCacheObject("cityCode"));
	pageContext.setAttribute("officeInfo",Cache.getCacheObject("officeInfo"));
	pageContext.setAttribute("gradeCode",Cache.getCacheObject("gradeCode"));
	pageContext.setAttribute("countryCodeList", Cache.getCacheObject("emplCountryCode"));//added for intX
	pageContext.setAttribute("providerType",Cache.getCacheObject("emplProviderType"));//added for intX
	pageContext.setAttribute("alNetworkTypeSearch", Cache.getCacheObject("primaryNetwork"));
%>
<!-- S T A R T : Content/Form Area -->
<div class="contentArea" id="contentArea">
<h4 class="sub_heading" style="width:15%;">Network Provider Search</h4>

<br/><html:errors/><br/>
<html:form action="/HospitalSearchAction.do" >

	<!-- E N D : Page Title -->
	<!-- S T A R T : Search Box -->
	<div class="searchList_class">
	<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
  		<tr>
	    	<td nowrap>Country <span class="mandatorySymbol">*</span>: <br>
   				<html:select property ="countryCode" styleId="countryCode" styleClass="selectBox selectBoxMedium" onchange="javascript:onGetStateList('countryCode');">
  					<html:option value="">Select from list</html:option>
          			<html:options collection="countryCodeList" property="cacheId" labelProperty="cacheDesc"/>
   				</html:select>
       		</td>
       		
        	
        	<td nowrap>State:<br>
		  		<html:select property="stateCode" styleId="stateCode" styleClass="selectBox selectBoxMedium"  onchange="javascript:onGetCityList('stateCode');">
                      <html:option value="">Any</html:option>
                      <html:optionsCollection name="stateCodeList" label="cacheDesc"  value="cacheId" />
                 </html:select>
	  		</td>		
        		
        		<td nowrap>City: <br>
   				<html:select property ="cityCode" styleClass="selectBox selectBoxMedium" >
  					<html:option value="">Select from list</html:option>
          			<html:optionsCollection name="cityCodeList" label="cacheDesc"  value="cacheId" />
   				</html:select>
       		</td>
       		
        		<td nowrap>Provider Type: <br>
   				<html:select property ="providerTypeId" styleClass="selectBox selectBoxMedium">
           			<html:option value="">Select from list</html:option>
           			<html:optionsCollection name="providerTypeList" label="cacheDesc"  value="cacheId" />
   				</html:select>
   			</td>
   			</tr><tr>	
	    	<td nowrap>Speciality:<br>
	       	   <html:select property="speciality" styleClass="selectBox selectBoxMedium">
                 <html:option value="">Any</html:option>
                 <html:optionsCollection name="specialityList" label="cacheDesc"  value="cacheId" />
              </html:select>
    		</td>
              
        <td nowrap>Network Type:<br>
			<html:select property="networkType" styleClass="selectBox selectBoxMedium">
					<html:option value="">--Select From List--</html:option>
		        	<html:optionsCollection name="networkTypeList" label="cacheDesc" value="cacheId" />
			</html:select>
		</td>
             <td width="100%" align="left" nowrap>
    				<a href="#" accesskey="s" onClick="javascript:onSearch()"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>
    		 </td> 
 		</tr>
   			
</table>
	</div>
	<div class="searchData_class">
	<!-- E N D : Search Box -->
	<!-- S T A R T : Grid -->
    <ttk:HtmlGrid name="networkProviderTable"/>
	
	<!-- E N D : Grid -->
	<!-- S T A R T : Buttons and Page Counter -->
	<div class="searchList_class" style="margin-left: 72px;">
	<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
		 
	  <ttk:PageLinks name="networkProviderTable"/>

	</table>
	</div>
	
	</div>
	
	<!-- <div class="query_div_class"><a href="#" onclick="javascript:onExportToExcel();" class="button_design_class">Export to Excel</a></div> -->
	<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="100%" align="center">
	     <button type="button" name="Button" accesskey="p" class="button_design_class" onMouseout="this.className='button_design_class'" onMouseover="this.className='button_design_class buttonsHover'" onClick="javascript:onExportToExcel();">Ex<u>p</u>ort to Excel</button>&nbsp;
	    </td>
	  </tr>
	</table>
	
	
	<!-- E N D : Buttons and Page Counter -->
	<!-- E N D : Content/Form Area -->
	<INPUT TYPE="hidden" NAME="rownum" VALUE="">
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	<input type="hidden" name="focusID" value="">
	<input type="hidden" name="count" value="<>">
</html:form>
</div>
<!-- E N D : Main Container Table -->