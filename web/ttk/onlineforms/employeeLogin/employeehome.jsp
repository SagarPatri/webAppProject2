<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile"%>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/onlineforms/EmployeeLogin/employeehome.js"></SCRIPT>
<script> TC_Disabled = true;</script>
<head>
<script type="text/javascript">
function edit(rownum)
{
	document.forms[1].rownum.value=rownum;
	var relationship='';
	 $.ajax({
 	     url :"/PreAuthReportEmployeeAction.do?mode=dogetRelatioship",
 	     dataType:"text",
 	     data :"rownum="+rownum,
 	     success : function(data) {
 	    	relationship=data;  
 	    	if(relationship==="Principal"){
 	   		document.forms[1].mode.value="doViewEmpDetails";
 	   	    document.forms[1].action="/AddEmplEnrollmentAction.do";
	 	   	}else{	
	 	   		document.forms[1].mode.value="doViewPolicy";
	 	   		document.forms[1].action="/OnlineEmplMemberDetailsAction.do";
	 	   	}
	 	   	document.forms[1].submit();
	 	    }
 	 });
	
}//end of editRoot(strRootIndex)
</script>
<style type="text/css">
.fieldset_class {
	padding-left: 9px;
	width: 79%;
	margin-left: 130px;
	font-size: 13px;
}

.textData {
	font-weight: bold;
	padding-bottom: 10px;
}

.tableDataList {
	width: 90%;
	margin-left: 50px;
	font-size: 12px;
}
.button_class {
    float: right;
    margin-right: 159px;
    padding-top: 15px;
    font-size: 13px;
}

.tob_class,.benefit_class,.ecard_class{
    background-color: #7293CB;
    font-weight: bold;
    width: 170px;
    height: 40px;
}
.info_text_class{
    color: #0c48a2;
    font-weight: bold;
    margin-left: 3px; 
    margin-top: -12px;
    font-size: 12px;
}


.info_text_close_class{
MARGIN-LEFT: 500px;
}
.selectBoxLargestWebloginNew {
    width: 325px;
}

.main_info_text_class {
    padding-left: 132px;
    margin-top: 40px;
    font-size: 14px;
    font-family: Arial, Helvetica, sans-serif;
}
.emplmember_class{
color:red;
}
</style>
</head>
<div class="contentArea" id="contentArea">
<h4 class="sub_heading">Enrollment Details</h4>
<%-- <div><div><ttk:OnlineHomeDetails/></div></div> --%>
<!-- <br/> -->
<html:errors/>
<html:form action="/EmployeeHomeAction.do">
<br/>
<div class="button_class">
	<input type="button" class="tob_class" onMouseout="this.className='tob_class'" onMouseover="this.className='tob_class buttonsHover'" value="Table of Benefits" onclick="javascript:onNodePolicyTob();"/> 
	<!-- &nbsp;&nbsp;<input type="button" class="benefit_class" value="Benefits Utilized" onclick="javascript:onBenefitUtilization();"/> -->
	&nbsp;&nbsp;<input type="button" class="benefit_class" onMouseout="this.className='benefit_class'" onMouseover="this.className='benefit_class buttonsHover'" value="Benefits Utilized" onclick="javascript:onBenefitUtilizationForMem();"/>  
	&nbsp;&nbsp;<input type="button" class="ecard_class" onMouseout="this.className='ecard_class'" onMouseover="this.className='ecard_class buttonsHover'" value="E-Cards" onclick="javascript:onEcards();"/>
</div>
<div>
<fieldset class="fieldset_class">
	<legend>Policy Information</legend>
	<div>
		<div>
			<table align="center" class="formContainer" border="0"
				cellspacing="0" cellpadding="0">

				<tr>
					<td>Policy No.</td>
					<td class="textData">&nbsp; <logic:notEmpty
							name="employeeHomeForm" property="policyNbr">
								: <bean:write name="employeeHomeForm" property="policyNbr" />
						</logic:notEmpty>
					</td>
					<td>Insurance Company</td>
					<td class="textData">&nbsp; <logic:notEmpty
							name="employeeHomeForm" property="insureName">
								: <bean:write name="employeeHomeForm" property="insureName" />
						</logic:notEmpty>
					</td>

				</tr>
				<tr>
					<td>Corporate Name</td>
					<td class="textData">&nbsp; <logic:notEmpty
							name="employeeHomeForm" property="insuredName">
								: <bean:write name="employeeHomeForm" property="insuredName" />
						</logic:notEmpty>
					</td>
					<td>Group Id</td>
					<td class="textData">&nbsp; <logic:notEmpty
							name="employeeHomeForm" property="groupId">
								: <bean:write name="employeeHomeForm" property="groupId" />
						</logic:notEmpty>
					</td>

				</tr>
				<tr>
					<td>Policy Start Date</td>
					<td class="textData">&nbsp; <logic:notEmpty
							name="employeeHomeForm" property="policyStartDate">
								: <bean:write name="employeeHomeForm" property="memPolicyStartDate" />
						</logic:notEmpty>
					</td>
					<td>Policy End Date</td>
					<td class="textData">&nbsp; <logic:notEmpty
							name="employeeHomeForm" property="policyEndDate">
								: <bean:write name="employeeHomeForm" property="memPolicyEndDate" />
						</logic:notEmpty>
					</td>

				</tr>
			</table>
		</div>
		<br/>
		<div class="tableDataList">
			<ttk:HtmlGrid name="employeeDataTable" />
		</div>
		<br/>
	</div>
</fieldset>
</div>
<div class="main_info_text_class">
<B>Note:</B>
<div class="info_text_class">
<ul style="list-style-type:square">
  <li>Please click on Al Koot ID of the member to view / modify Personal information of the member.</li>
  <li>Please click on Table of Benefits to download the treatment coverage as per your policy.</li>
  <li>Please click on Benefits Utilized to view the available limits for each policy benefit.</li>
  <li>Please click on E-Cards to download the E-Cards.</li>
</ul>
</div>
<br/>
</div>
<INPUT type=hidden name=rownum>
<INPUT type=hidden name=mode>
<input type="hidden" name="prodPolicySeqID" value="${sessionScope.prodPolicySeqID}">
</html:form>
</div>