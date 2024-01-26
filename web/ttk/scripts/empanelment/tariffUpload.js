//java script for the tariff details screen in the empanelment of hospital flow.

function onSearch()
{
	if(!JS_SecondSubmit)
	 {
	    trimForm(document.forms[1]);
	    document.forms[1].mode.value = "doSearch";
	    document.forms[1].action = "/TariffAction.do";
		JS_SecondSubmit=true;
	    document.forms[1].submit();
	 }//end of if(!JS_SecondSubmit)
}//end of onSearch()
//function to sort based on the link selected
function toggle(sortid)
{
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/TariffAction.do";
    document.forms[1].submit();
}//end of toggle(sortid)

//function to display the selected page
function pageIndex(pagenumber)
{
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/TariffAction.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber)

//function to display previous set of pages
function PressBackWard()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/TariffAction.do";
    document.forms[1].submit();
}//end of PressBackWard()

//function to display next set of pages
function PressForward()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/TariffAction.do";
    document.forms[1].submit();
}//end of PressForward()

function onHistoryIcon(rownum)
{
	document.forms[1].reset();
	document.forms[1].rownum.value=rownum;
	document.forms[1].mode.value ="doViewRevisionHistory";
	document.forms[1].child.value="RevisePlan";
    document.forms[1].action = "/TariffAction.do";
	document.forms[1].submit();
}//end of function onHistoryIcon(rownum)

//for INTX
function onUploadTariff()
{
	if(!JS_SecondSubmit)
	 {
	    trimForm(document.forms[1]);
	    document.forms[1].mode.value = "doUploadTariff";
	   // document.forms[1].action = "/TariffUploadsAction.do?priceRefId="+priceRefId;
	    document.forms[1].action = "/TariffUploadsEmpanelmentAction.do";
		JS_SecondSubmit=true;
		
		document.forms[1].uploadButton.value = 'Please wait..';
		document.forms[1].uploadButton.disabled = true;
		
	    document.forms[1].submit();
	 }//end of if(!JS_SecondSubmit)
}


function onUploadPharmacyTariff()
{
	if(!JS_SecondSubmit)
	 {
	    trimForm(document.forms[1]);
	    document.forms[1].mode.value = "doUploadPharmacyTariff";
	   // document.forms[1].action = "/TariffUploadsAction.do?priceRefId="+priceRefId;
	    document.forms[1].action = "/PharmacyTariffUploadsEmpanelmentAction.do";
		JS_SecondSubmit=true;
		
		document.forms[1].uploadButton.value = 'Please wait..';
		document.forms[1].uploadButton.disabled = true;
		
	    document.forms[1].submit();
	 }//end of if(!JS_SecondSubmit)
}

function onBackTariff()
{
	if(!JS_SecondSubmit)
	 {
		document.forms[1].mode.value="doDefaultEmpnlTariff";
		document.forms[1].action = "/TariffActionEmpanelment.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}

function onBackPharmacyTariff()
{
	document.forms[1].mode.value="doDefaultEmpnlPharmacyTariff";
    document.forms[1].action = "/TariffActionEmpanelment.do";
    document.forms[1].submit();
}

function onChangeTariffType(obj)
{
	var tariffType	=	obj.value;
	//making all values to null
	/*document.forms[1].corpCode.value='';
	document.forms[1].payerCode.value='';
	document.forms[1].providerCode.value='';
	document.forms[1].networkType.value='';*/
	
	
	if(tariffType=='HOSP')
	{
		/*document.forms[1].corpCode.disabled=true;

		document.forms[1].payerCode.disabled=false;
		document.forms[1].providerCode.disabled=false;
		document.forms[1].networkType.disabled=false;*/
		document.getElementById("apayerCode").style.display="inline";
		//document.getElementById("aproviderCode").style.display="inline";
		document.getElementById("anetworkType").style.display="inline";
		//document.getElementById("acorpCode").style.display="none";
		document.getElementById("group").style.display	=	"none";
		document.getElementById("networkcategory").style.display	=	"none";
		document.getElementById("provider").style.display	=	"none";
		document.getElementById("providerlincenceId").style.display	=	"inline";
		
	}else if(tariffType=='GROP'){
		document.getElementById("group").style.display	=	"inline";
		document.getElementById("networkcategory").style.display	=	"inline";
		document.getElementById("provider").style.display	=	"inline";
		document.getElementById("providerlincenceId").style.display	=	"none";
		document.getElementById("providerGroupName").value=document.getElementById("groupNameId").value;
	}
	else if(tariffType=='INS')
	{
		/*document.forms[1].providerCode.disabled=true;
		document.forms[1].corpCode.disabled=true;

		document.forms[1].payerCode.disabled=false;
		document.forms[1].networkType.disabled=false;*/

		document.getElementById("apayerCode").style.display="inline";
		//document.getElementById("aproviderCode").style.display="none";
		document.getElementById("anetworkType").style.display="inline";
		//document.getElementById("acorpCode").style.display="none";
		
	}else if(tariffType=='COR')
	{
		/*document.forms[1].providerCode.disabled=true;
		
		document.forms[1].payerCode.disabled=false;
		document.forms[1].networkType.disabled=false;
		document.forms[1].corpCode.disabled=false;*/
		
		document.getElementById("apayerCode").style.display="inline";
		//document.getElementById("aproviderCode").style.display="none";
		document.getElementById("anetworkType").style.display="inline";
		//document.getElementById("acorpCode").style.display="inline";
		
	}else if(tariffType=='TPA')
	{
		/*document.forms[1].providerCode.disabled=true;
		
		document.forms[1].payerCode.disabled=true;
		document.forms[1].networkType.disabled=true;
		document.forms[1].corpCode.disabled=false;*/

		document.getElementById("apayerCode").style.display="none";
		//document.getElementById("aproviderCode").style.display="none";
		document.getElementById("anetworkType").style.display="inline";
		//document.getElementById("acorpCode").style.display="none";
		
	}
}

function tariffUploadFormat()
{
	
	var openPage = "/ReportsAction.do?mode=doViewCommonForAll&module=professionDocs&fileName=";
	   var w = screen.availWidth - 10;
	   var h = screen.availHeight - 49;
	   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	   window.open(openPage,'',features);
}

function pharmacyTariffUploadFormat()
{
	
	var openPage = "/ReportsAction.do?mode=doViewCommonForAll&module=pharmacyTariffUpload&fileName=";
	   var w = screen.availWidth - 10;
	   var h = screen.availHeight - 49;
	   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	   window.open(openPage,'',features);
}

function showErrorLog(obj)
{
	var openPage = "/ReportsAction.do?mode=doViewCommonForAll&module=tariffUploadLogs&fileName=obj";
	   var w = screen.availWidth - 10;
	   var h = screen.availHeight - 49;
	   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	   window.open(openPage,'',features);
}

//function to select multiple corporates
function onCorporate()
{
    document.forms[1].mode.value ="doCorporateDefault";
    document.forms[1].action = "/TariffSelectCorporatesAction.do";
    document.forms[1].submit();
}

function onModifyTariff()
{
	if(!JS_SecondSubmit)
	 {
	    trimForm(document.forms[1]);
		document.forms[1].mode.value="doShowTariff";
	    document.forms[1].action = "/ShowTariffItemsEmpanelment.do";
	    JS_SecondSubmit=true;
	    document.forms[1].submit();
	 }
}


function bulktariffmodifyFormat()
{
	
	var openPage = "/ReportsAction.do?mode=doViewCommonForAll&module=bulktariffmodify&fileName=";
	   var w = screen.availWidth - 10;
	   var h = screen.availHeight - 49;
	   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	   window.open(openPage,'',features);
}

function bulkpharmacymodifyFormat()
{
	
	var openPage = "/ReportsAction.do?mode=doViewCommonForAll&module=bulkpharmacymodify&fileName=";
	   var w = screen.availWidth - 10;
	   var h = screen.availHeight - 49;
	   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	   window.open(openPage,'',features);
}

function onUploadBulkModifyTariff()
{
	if(!JS_SecondSubmit)
	 {
	    trimForm(document.forms[1]);
	    document.forms[1].mode.value = "doUploadBulkModifyTariff";
	   // document.forms[1].action = "/TariffUploadsAction.do?priceRefId="+priceRefId;
	    document.forms[1].action = "/TariffUploadsBulkModifyAction.do";
		JS_SecondSubmit=true;
		
		document.forms[1].uploadButton.value = 'Please wait..';
		document.forms[1].uploadButton.disabled = true;
		
	    document.forms[1].submit();
	 }//end of if(!JS_SecondSubmit)
}

function startTariffUpload(obj){
	var batchRefNo =  document.forms[1].hiddenBatchRefNo.value;
	var tariffSwitch	=	obj;
		
	if(batchRefNo != ""){
		
		alert("Batch Reference Number is "+batchRefNo);
		document.forms[1].mode.value="doCreateTariffFromUploadedFile";
		document.forms[1].action="/TariffUploadsActionDetails.do?tariffSwitch="+tariffSwitch;	
		//document.getElementById("Button").innerHTML	=	'Please wait..';
		//document.forms[1].Button.disabled = true;
		document.forms[1].submit();
		
	}
}