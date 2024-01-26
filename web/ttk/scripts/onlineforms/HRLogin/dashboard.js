//java script for the policies list screen in the online forms module of enrollment flow.

//function to call edit screen

//function to display the selected page
function pageIndex(pagenumber)
{
	document.forms[1].reset();
    document.forms[1].mode.value="doDefault";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/OnlinePoliciesAction.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber)


//kocb
function onSearch()
{
	if(!JS_SecondSubmit)
	 {
		trimForm(document.forms[1]);
		document.forms[1].mode.value = "doSearch";
		document.forms[1].action = "/OnlinePoliciesAction.do";
		JS_SecondSubmit=true;
	    document.forms[1].submit();
	 }
}
function onDownLoadFiles(){
	var fileType=document.forms[1].fileTypeDownload.value;
	if(fileType==null||""==fileType||fileType.length<1){
		alert("Please Select File type to Download");
		return ;
	}
	document.forms[1].mode.value="doDownLoadFiles";
	document.forms[1].action="/OnlineDashBoardAction.do";
	document.forms[1].submit();
}//end of onDownLoadFiles()

function onUpLoadFiles(){
	var policy = document.forms[1].listofPolicysequenceId.value;
	var fileType=document.forms[1].fileTypeUpload.value;
	if(fileType==null||""==fileType||fileType.length<1){
		alert("Please Select File type to Upload");
		return ;
	}
	if(policy == null || policy == 0 || policy.length <1){
		alert("Please Select Policy to Upload");
		return ;
	}
	if(!JS_SecondSubmit)
	{
	document.forms[1].mode.value="doUpLoadFiles";
	document.forms[1].action="/OnlineDashBoardAction.do";
	JS_SecondSubmit=true;
	document.forms[1].submit();
	}
}//end of onUpLoadFile()
function onEnrollmentReports()
{
	document.forms[1].tab.value="HR Reports";	
    document.forms[1].sublink.value="HR";
    document.forms[1].leftlink.value="Online Reports";
	document.forms[1].mode.value="doDefault";
	document.forms[1].action="/OnlineReportsAction.do";
	document.forms[1].submit();
}//end of onDashBoardEnrollmentRefresh()


function onDashBoardEnrollmentRefresh()
{
	
	document.forms[1].mode.value="doDashBoardEnrollmentRefresh";
	document.forms[1].action="/OnlineDashBoardAction.do";
	document.forms[1].submit();
}//end of onDashBoardEnrollmentRefresh()

