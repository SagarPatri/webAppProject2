
//contains the javascript functions of investigationmonitorreport.jsp of MIS Reports module

function onChangeInsCompany()
{
    	document.forms[1].mode.value="doChangeInsCompany";
    	document.forms[1].focusID.value="TTKBranchCode";
    	document.forms[1].action="/InvestigatReportsAction.do";
    	document.forms[1].submit();
}//end of onChangeCity()

function onChangeInsBoDo()
{
	document.forms[1].mode.value="doChangeInsComDoBo";
   	document.forms[1].focusID.value="insCompanyCode";
	document.forms[1].action="/InvestigatReportsAction.do";
	document.forms[1].submit();
}

function onChangeLabel()
{
        //alert("onChangeStatus()");
    	document.forms[1].mode.value="doChangeLabel";
    	document.forms[1].action="/InvestigatReportsAction.do";
    	document.forms[1].submit();
}//end of onChangeLabel()

function onChangeStatus()
{
        //alert("onChangeStatus()");
    	document.forms[1].mode.value="doChangeStatus";
    	document.forms[1].action="/InvestigatReportsAction.do";
    	document.forms[1].submit();
}//end of onChangeStatus()

function onClose()
{
	document.forms[1].mode.value="doClose";
	document.forms[1].action.value="/InvestigatReportsAction.do";
	document.forms[1].submit();
}//end of onClose()


function onGenerateReport()
{
       if(document.getElementById("tTKBranchCode").value=="")
		{
			alert("Please Select Vidal Health Branch");
			document.getElementById("tTKBranchCode").focus();
			return false;
		}
		 if(document.getElementById("insCompanyCode").value=="")
		{
			alert("Please Select Healthcare Company");
			document.getElementById("insCompanyCode").focus();
			return false;
		}
      if(document.getElementById("sType").value=="")
		{
			alert("Please Select Claims Type");
			document.getElementById("sType").focus();
			return false;
		}
		if(document.getElementById("sStatus").value=="")
		{
			alert("Please Select Status");
			document.getElementById("sStatus").focus();
			return false;
		}
		
		if(isDate(document.getElementById("sStartDate"),"Start Date")==false)
		{
			//alert("Please Enter Start Date");
			document.getElementById("sStartDate").focus();
			return false;
		}
		
		if(isDate(document.getElementById("sEndDate"),"End Date")==false)
		{
			//alert("Please Enter End Date");
			document.getElementById("sEndDate").focus();
			return false;
		}
		
       if(document.getElementById("reportID").value=="")
		{
			alert("Please Select Report Name");
			document.getElementById("reportID").focus();
			return false;
		}
 	//alert("function onGenerateReport()");
 	var TTKBranchCode=document.getElementById("tTKBranchCode").value
 	var insCompanyCode=document.getElementById("insCompanyCode").value
 	var insBoDoCode=document.getElementById("insBoDoCode").value
 	var sAgentCode=document.getElementById("sAgentCode").value
 	var sGroupPolicyNo=document.getElementById("sGroupPolicyNo").value
 	var startDate = document.getElementById("sStartDate").value;
	var endDate = document.getElementById("sEndDate").value;
	var sType=document.getElementById("sType").value;
 	var sStatus=document.getElementById("sStatus").value;
 	var investAgencyname=document.getElementById("investAgencyName").value;
 	var sEnrolmentId=document.getElementById("sEnrolmentId").value;
 	var sCorInsurer=document.getElementById("sCorInsurer").value;
 	var selectbranchcode=document.getElementById("tTKBranchCode");
    var ttkbranchname=selectbranchcode.options[selectbranchcode.options.selectedIndex].text;
    var selectinscompcode=document.getElementById("insCompanyCode");
    var inscompname=selectinscompcode.options[selectinscompcode.options.selectedIndex].text;
    var selectinsbodocode=document.forms[1].insBoDoCode;
    var inscompbodo=selectinsbodocode.options[selectinsbodocode.options.selectedIndex].text;
    var selecttype=document.getElementById("sType");
    var types=selecttype.options[selecttype.options.selectedIndex].text;
    var selectstatus=document.getElementById("sStatus");
    var status=selectstatus.options[selectstatus.options.selectedIndex].text;
    var selectreportID=document.getElementById("reportID");
    var reportname=selectreportID.options[selectreportID.options.selectedIndex].text;
    var selectinvestAgency=document.getElementById("investAgencyName");
  	var investAgencyna=selectinvestAgency.options[selectinvestAgency.options.selectedIndex].text;
  	var reportlinkname= "Investigation Monitor";
  	var startlabelvalue=document.getElementById("lb").innerText;
         
         var endlabelvalue=document.getElementById("lab").innerText;
         
    var parameterValue;//= new String("|S|");
    document.forms[1].parameterValues.value=parameterValue;
	document.forms[1].mode.value="doGenerateReport";
	if(document.forms[1].reportType.value=="EXL")
	{
	    if(document.forms[1].reportID.value=="IMCIR")
	     {
		       var fileName = "reports/misreports/InvestigatStandardEXL.jrxml";
	     }//end of if(document.forms[1].reportID.value=="IMCIR")
	     else if(document.forms[1].reportID.value=="IMPAIR")
	     {
		       var fileName = "reports/misreports/InvestigatStandardPatEXL.jrxml";
	     }// end of else if(document.forms[1].reportID.value=="IMPAIR")
	}//END OF if(document.forms[1].reportType.value=="EXL")
	if(document.forms[1].reportType.value=="PDF")
	{
		if(document.forms[1].reportID.value=="IMCIR")
	     {
		      var fileName = "reports/misreports/InvestigatStandardEXL.jrxml";
	     }//end of if(document.forms[1].reportID.value=="IMCIR")
	     else if(document.forms[1].reportID.value=="IMPAIR")
	     {
		       var fileName = "reports/misreports/InvestigatStandardPatEXL.jrxml";
	     }// end of else if(document.forms[1].reportID.value=="IMPAIR")
	}// END OF if(document.forms[1].reportType.value=="PDF")
	
	
	//parameterValue = "|"+TTKBranchCode+"|"+insCompanyCode+"|"+insBoDoCode+"|"+sAgentCode+"|"+sGroupPolicyNo+"|"+startDate+"|"+endDate+"|"+sType+"|"+sStatus+"|";
	//parameterValue = "|"+TTKBranchCode+"|"+sType+"|"+insCompanyCode+"|"+insBoDoCode+"|"+sAgentCode+"|"+sGroupPolicyNo+"|"+startDate+"|"+endDate+"|"+sStatus+"|"+investAgencyname+"|";
	//var partmeter = "?mode=doGenerateReport&parameter="+parameterValue+"&fileName="+fileName+"&reportID="+document.forms[1].reportID.value+"&reportType="+document.forms[1].reportType.value+"&startDate="+startDate+"&endDate="+endDate+"&Location="+TTKBranchCode+"&reportlinkname="+reportlinkname+"&sGroupPolicyNo="+sGroupPolicyNo+"&sAgentCode="+sAgentCode+"&reportname="+reportname+"&types="+types+"&status="+status+"&ttkbranchname="+ttkbranchname+"&inscompname="+inscompname+"&inscompbodo="+inscompbodo+"&investAgencyna="+investAgencyna+"&startlabelvalue="+startlabelvalue+"&endlabelvalue="+endlabelvalue;
	parameterValue = "|"+TTKBranchCode+"|"+sType+"|"+insCompanyCode+"|"+insBoDoCode+"|"+sAgentCode+"|"+sGroupPolicyNo+"|"+startDate+"|"+endDate+"|"+sStatus+"|"+investAgencyname+"|"+sEnrolmentId+"|"+sCorInsurer+"|";
	var partmeter = "?mode=doGenerateReport&parameter="+parameterValue+"&fileName="+fileName+"&reportID="+document.forms[1].reportID.value+"&reportType="+document.forms[1].reportType.value+"&startDate="+startDate+"&endDate="+endDate+"&Location="+TTKBranchCode+"&reportlinkname="+reportlinkname+"&sGroupPolicyNo="+sGroupPolicyNo+"&sAgentCode="+sAgentCode+"&reportname="+reportname+"&types="+types+"&status="+status+"&ttkbranchname="+ttkbranchname+"&inscompname="+inscompname+"&inscompbodo="+inscompbodo+"&investAgencyna="+investAgencyna+"&startlabelvalue="+startlabelvalue+"&endlabelvalue="+endlabelvalue+"&sEnrolmentId="+sEnrolmentId+"&sCorInsurer="+sCorInsurer;
	//var partmeter = "?mode=doGenerateReport&parameter="+parameterValue+"&fileName="+fileName+"&reportID="+document.forms[1].reportID.value+"&reportType="+document.forms[1].reportType.value+"&startDate="+startDate+"&endDate="+endDate+"&Location="+TTKBranchCode;
	
	var openPage = "/InvestigatReportsAction.do"+partmeter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
	//document.forms[1].submit();
}//end of onGenerateReport
