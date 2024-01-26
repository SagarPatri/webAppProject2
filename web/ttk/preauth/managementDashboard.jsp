<%
/** @ (#) dashboard.jsp 4/16/2020
 * Project     : ProjectX
 * File        : dashboard.jsp
 * Author      : Rahul Singh 
 * Company     : Vidal
 * Date Created: 4/16/2020
 *
 * @author 		 : Rahul Singh
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */
%>


<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.HashMap,java.util.ArrayList,com.ttk.common.PreAuthWebBoardHelper"%>





<script language="javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script>
<script language="javascript" src="/ttk/scripts/jquery.autocomplete.js"></script>
<script type="text/javascript" SRC="/ttk/scripts/validation.js"></script>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<!-- <script type="text/javascript"
	src="/ttk/scripts/preauth/preauthgeneral.js"></script> -->
<!-- <script type="text/javascript"
	src="/ttk/scripts/preauth/preauthgeneral-async.js"></script> -->	
<script type="text/javascript" src="/ttk/scripts/preauth/dashboard.js"></script>
		<head>
		
		 <script language="javascript">
/* var JS_Focus_ID="divMainId"; */
var JS_Focus_ID="<%=TTKCommon.checkNull(request.getAttribute("focusObj"))%>";
var eventFire = false;
</script>			
	<style type="text/css">
	.divManclass{
 width: 1150px;
 height: 230px;
 /* border: 1px solid; */
 font-size:16px;
/*  margin-left:5%;
 margin-right:5%; */
/*  background-color: rgb(132, 166, 57); */
 background-color: white;
 }
	
 .spanClass{
   cursor: pointer;
  font-size:18px;
 }	
	
	</style>

	</head>
	<body>
	<% boolean accessFlag = TTKCommon.isAuthorized(request, "DashBordPreauthView"); %>
	
<table align="center" class="pageTitle" border="0" cellspacing="0"
        cellpadding="0">
        <tr>
            <td width="57%">List of Pre-Approval</td>
           
            <td width="33%" align="right" class="webBoard">&nbsp;<%@ include
                    file="/ttk/common/toolbar.jsp"%></td>
                     <td width="10%" height="15px;"><button onclick="javascript:closemanagementdashboard()">Close</button></td>
                    
        </tr>
    </table>
    <!-- E N D : Page Title -->
    <%-- <html:errors /> --%>
<div class="contentArea" id="contentArea">
<html:form action="/PreAuthManagementAction.do" >

<html:errors /> 
<div class="divManclass" id="divMainId">
 <table>
 <tr>
 <td>
 <div style="width: 180px; height: 210px;border-right: 1x solid black;">
<div style="width: 140px;height: 70px;border: 1px solid #149814f2;margin-top: 15%;margin-left:14%;text-align: center; ">
 <div style="width: 138px;height: 68px;border: 1px groove green;">
 <span><b>Fresh</b></span><br>
 <span class="spanClass"><b><a href="javascript:queueStatus('FRS','Fresh','<bean:write name="frmDashboardList" property="fress_case"/>');" style="text-decoration: none;color:green;"><bean:write name="frmDashboardList" property="fress_case"/></a></b></span>
 </div>
 </div>
 
 <div style="width: 140px;height: 70px;border: 1px solid #149814f2;margin-top: 18%;margin-left:14%;text-align: center;">
 <div style="width: 138px;height: 68px;border: 1px groove green;">
 <span><b>For Decision</b></span><br>
 <span class="spanClass"><b><a href="javascript:queueStatus('FRD','For Decision','<bean:write name="frmDashboardList" property="coding_cases"/>')" style="text-decoration: none;color:red;"><bean:write name="frmDashboardList" property="coding_cases"/></a></b></span>/<span class="spanClass"><b><a href="javascript:queueStatus('HIG','For Decision','<bean:write name="frmDashboardList" property="highValuePA"/>')" style="text-decoration: none;color:red"><bean:write name="frmDashboardList" property="highValuePA"/></a></b></span>
 </div>
 </div>
 </div>
 
 </td>
 <td>
 <div style="width:400px;
    height:210px;
    border-right: 1x solid black;;">
 <div style="width: 370px;height: 20px;text-align: center;border: 1px solid #149814f2;margin-left: 2%;"> 
  <div style="width: 368px;height: 19px;border: 1px groove green;margin-left: 2%;"> 
 <span><b>Shortfalls</b></span>
 </div>
 </div>
 <div style="display: inline-block;margin-top: 5%;text-align: center;">
 <table>
 <tr>
 <td>
 <div style="width:150px;height: 50px;border: 1px solid green;">
 <span>Raised</span><br>
 <span class="spanClass"><a href="javascript:queueStatus('SFO','Shortfall Raised','<bean:write name="frmDashboardList" property="shortfall_cases"/>')" style="text-decoration: none;color:blue;"><bean:write name="frmDashboardList" property="shortfall_cases"/></a></span>
 </div>
 </td>
 <td>
 <div style="width:190px;height: 50px;border: 1px solid green;">
 <span>Responded</span><br>
 <span class="spanClass"><a href="javascript:queueStatus('SFR','Shortfall Responded','<bean:write name="frmDashboardList" property="shortfall_respond_ases"/>')" style="text-decoration: none;color:blue;"><bean:write name="frmDashboardList" property="shortfall_respond_ases"/></a></span>
 </div>
 </td>
 </tr>
 </table>
 </div>
 <div style="width: 370px;height: 20px;text-align: center;border: 1px solid #149814f2;margin-left: 2%;"> 
  <div style="width: 368px;height: 19px;border: 1px groove green;margin-left: 2%;"> 
 <span><b>Enhancement Shortfall</b></span>
  </div> 
 </div>
 
 <div style="display: inline-block;margin-top: 5%;text-align: center;">
 <table>
 <tr>
 <td>
 <div style="width: 150px;height: 50px;border: 1px solid green;">
 <span>Raised</span><br>
 <span class="spanClass"><a href="javascript:queueStatus('SFE','Enhancement Shortfall Raised','<bean:write name="frmDashboardList" property="enhanced_shortfall_cases"/>')" style="text-decoration: none;color:green;"><bean:write name="frmDashboardList" property="enhanced_shortfall_cases"/></a></span>
 </div>
 </td>
 <td>
 <div style="width:190px;height: 50px;border: 1px solid green;">
 <span>Responded</span><br>
 <span class="spanClass"><a href="javascript:queueStatus('SER','Enhancement Shortfall Responded','<bean:write name="frmDashboardList" property="enhanced_shrtfal_res_cases"/>')" style="text-decoration: none;color:green;"><bean:write name="frmDashboardList" property="enhanced_shrtfal_res_cases"/></a></span>
 </div>
 </td>
 </tr>
 </table>
 </div>
 </div> 
 </td>
 <td>
 
 <div style="width: 250px;
    height: 210px;
    border-right: 1x solid black;">
    
    <!--  <div style="width: 230px; height: 230px;border-right: 3px solid black;"> -->
<div style="width: 160px;height: 50px;border: 1px solid #149814f2;margin-top:14%;margin-left:12%;text-align: center;">
 <div style="width: 158px;height: 48px;border: 1px groove green;">
 <span><b>Enhanced</b></span><br>
 <span class="spanClass"><b><a href="javascript:queueStatus('ENH','Enhance','<bean:write name="frmDashboardList" property="enhance_inp_cases"/>')" style="text-decoration: none;color:green;"><bean:write name="frmDashboardList" property="enhance_inp_cases"/></a></b></span>
 </div>
 </div>
 
 <div style="width: 160px;height: 50px;border: 1px solid #149814f2;margin-top: 14%;margin-left:12%;text-align: center;">
 <div style="width: 158px;height: 48px;border: 1px groove green;">
 <span><b>Appeal</b></span><br>
 <span class="spanClass"><b><a href="javascript:queueStatus('APL','Appeal','<bean:write name="frmDashboardList" property="appeal_inp_cases"/>')" style="text-decoration: none;color:blue;"><bean:write name="frmDashboardList" property="appeal_inp_cases"/></a></b></span>
 </div>
 </div>
 <!-- </div> -->
    </div>
 </td>
 <td>
 <div style="width: 285px;
    height: 210px;">
    <div style="width: 240px;height: 25px;margin-top: 10%;">
    <div style="width: 238px;height: 23px;border: 1px groove green;">
    <span><b>Approved</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><span class="spanClass"><a href="javascript:queueStatus('APR','Approved','<bean:write name="frmDashboardList" property="approved_cases"/>')" style="text-decoration: none;color:green;"><bean:write name="frmDashboardList" property="approved_cases"/></a></span></b></span>
    </div>
    </div>
    
     <div style="width: 240px;height: 25px;margin-top: 10%;">
    <div style="width: 238px;height: 23px;border: 1px groove green;">
    <span><b>Rejected</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><span class="spanClass"><a href="javascript:queueStatus('REJ','Rejected','<bean:write name="frmDashboardList" property="rejected_cases"/>')" style="text-decoration: none;color:red;"><bean:write name="frmDashboardList" property="rejected_cases"/></a></span></b></span>
    </div>
    </div> 
      <div style="width: 240px;height: 25px;margin-top: 10%;">
    <div style="width: 238px;height: 23px;border: 1px groove green;">
    <span><b>For Resolution</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><span class="spanClass"><a href="javascript:queueStatus('WIP','For Resolution','<bean:write name="frmDashboardList" property="forResolution"/>')" style="text-decoration: none;color:blue;"><bean:write name="frmDashboardList" property="forResolution"/></a></span></b></span>
    </div>
    </div> 
  
    
    </div>
 </td>
 </tr>
 </table>
</div>


<div style="margin-top: 24px; font-size: 13px;">User Information</div>
	
	 <ttk:HtmlGrid name="tDUserInfo"/>
	 <div>
			<table class="table">
				<ttk:PageLinks2 name="tDUserInfo" />
			</table>
			</div>
	  
	  <%-- <ttk:PageLinks name="tDUserInfo"/>  --%>
 


       <div style="margin-top: 24px; font-size: 13px;">To-Do Task</div>
		<ttk:HtmlGrid name="tDSelfAssignment"/>
		 <div>
			<table class="table">
				<ttk:PageLinks2 name="tDSelfAssignment" />
			</table>
			</div>
	
			
			<div>
            <div>
               <div>
				<div>
                     <div  style="margin-top:23px; font-size:11px;color: darkblue">
                     	<!-- Pre-Authorization Requests -->
                     	<table>
                     	<tr>
                     <td><bean:write name="frmDashboardList" property="search_type_name"/> </td>
                        <td class="count"><bean:write name="frmDashboardList" property="search_type_count"/></td>
                        <td class="item-selected">&nbsp;<img src="/ttk/images/Refresh.gif" title="refresh" alt="refresh" id="refID" style="cursor: pointer; width: 13px;" onclick="javascript:onrefresh()"></td>     
                  </tr>
                    </table>
                     </div>
                     
                     <div>
                      
                        
                           	  <div style="margin-top:23px;">
                           	  	<span id="basic-addon1">
                           	  	<html:select styleClass="form-control" styleId="searchTypeId" name="frmDashboardList" property="searchType">
                           	  	<html:option value="PID">Pre-Auth No.</html:option>                     
                     			<!-- <option value="REF">Reference No.</option> -->
                     			<html:option value="EID">Enrollment Id.</html:option>                     			
                     			<!-- <option value="MIL">Mail Id.</option> -->
                     			</html:select></span>
  								<input type="text" id="searchDataId" name="searchData"  class="form-control" placeholder="Search By" aria-describedby="basic-addon1"/>
  								<span class="input-group-addon" style="cursor:pointer;" onclick="javascript:quickSearch('QREP')" id="basic-addon1"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" ></span>
							  </div>
							  <input type="hidden" name="quicksearch" id="quicksearchId" value="">     
                     </div>
                  </div>
				</div>
			</div>
		</div>
		
		<!-- S T A R T : Grid -->
	    <ttk:HtmlGrid name="tableData" />
	     <div>
			<table class="table">
				<ttk:PageLinks2 name="tableData" />
			</table>
			</div>
		<%-- </html:form> --%>
	
	
	
	<!-- short_fall -->
    
           <INPUT TYPE="hidden" NAME="sortId" id="sortId" VALUE="">
			<INPUT TYPE="hidden" NAME="pageId" id="pageId">
			<INPUT TYPE="hidden" NAME="rownum" id="rownumId" VALUE=''>
			<INPUT TYPE="hidden" NAME="tableName" id="tableNameId" VALUE="">
			<input type="hidden" name="child" id="childId" value="">
			<input type="hidden" name="queueStatus" id="queueId" value="">
			<INPUT TYPE="hidden" NAME="tab" id="tabId"VALUE="">
			<INPUT TYPE="hidden" NAME="mode" VALUE="">
			<INPUT TYPE="hidden" NAME="leftlink" VALUE="">
			<INPUT TYPE="hidden" NAME="sublink" VALUE="">
			<input type="hidden" name="accessflag" id="accessflagid" value="<%=accessFlag%>">
			<input type="hidden" name="search_type_name" id="search_type_name_id" value="<%= request.getSession().getAttribute("search_type_name") %>">
			<input type="hidden"  id="searchID" value="<%= request.getSession().getAttribute("searchID") %>">
			<input type="hidden"  id="searchVal" value="<%= request.getSession().getAttribute("searchVal") %>">
			<input type="hidden"  id="queueStatus" value="<%= request.getSession().getAttribute("queueStatus") %>">
 </html:form> 
    </div>
	
	</body>
	
	<script>
	   var myVar = setInterval(myTimer, 50000);  
	function myTimer() {		 
		
		    document.forms[0].mode.value="doDisplayManaDashbordDefault";
			 document.forms[0].action = "/PreAuthManagementDeaultAction.do?";
			 document.forms[0].submit();
		} 
	</script> 
	
	
