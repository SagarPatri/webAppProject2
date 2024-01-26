<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk"%>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ page language="java" import="java.util.*,javax.naming.*,java.io.* "
	contentType="application/vnd.ms-excel"%>
<%@ page
	import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.HashMap,java.util.ArrayList"%>
<%@ page
	import="com.ttk.dto.usermanagement.UserSecurityProfile,com.ttk.dto.administration.WorkflowVO"%>
<%@ page language="java" import="java.util.*,javax.naming.*,java.io.* "
	contentType="application/vnd.ms-excel"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

</head>
<body>
	<%
	String switchType="";
	String fileName="";
	if(request.getAttribute("RepswitchType")!=null){
		switchType=(String) request.getAttribute("RepswitchType");
	}
	if("CLMPTA".equals(switchType)){
		fileName="CFDReport.xls";
	}else{
		fileName="CFDLogDetailsReport.xls";
	}
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename="+fileName);
	%>
<ttk:GenerateExcelSheet/>

</body>
</html>