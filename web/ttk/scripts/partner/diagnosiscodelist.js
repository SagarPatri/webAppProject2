function diagnosisCodeSearch(element){
	if(!JS_SecondSubmit){
	    	document.forms[1].mode.value = "diagnosisCodeSearch";
	    	document.forms[1].action = "/PartnerClaimDiagnosisSearchAction.do";
	    	 JS_SecondSubmit=true;
	    	 element.innerHTML	=	"<b>"+"Please Wait...!!"+"</b>";
	    	document.forms[1].submit();
	}
}

function edit(rownum){	
	if(!JS_SecondSubmit){
    document.forms[1].mode.value="doSelectDiagnosisCode";
    document.forms[1].rownum.value=rownum;
    document.forms[1].action = "/OnlinePartnerClaimsSubmission.do?diagnosicScreen=diagnosicScreen";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of edit(rownum)
function pageIndex(pagenumber)
{
	if(!JS_SecondSubmit){
    document.forms[1].reset();
    document.forms[1].mode.value="diagnosisCodeSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/PartnerClaimDiagnosisSearchAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of pageIndex(pagenumber)

function PressForward()
{
	if(!JS_SecondSubmit){
    document.forms[1].reset();
    document.forms[1].mode.value ="doDiagnosisCodeForward";
    document.forms[1].action = "/PartnerClaimDiagnosisSearchAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of PressForward()
function PressBackWard()
{
	if(!JS_SecondSubmit){
    document.forms[1].reset();
    document.forms[1].mode.value ="doDiagnosisCodeBackward";
    document.forms[1].action = "/PartnerClaimDiagnosisSearchAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of PressBackWard()
function toggle(sortid)
{
    document.forms[1].reset();
    if(!JS_SecondSubmit){
    document.forms[1].mode.value="diagnosisCodeSearch";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/PartnerClaimDiagnosisSearchAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
    }
}//end of toggle(sortid)

function closeDiagnosis()
{
	 if(!JS_SecondSubmit){
    document.forms[1].mode.value="doClose";
    document.forms[1].action = "/OnlinePartnerClaimsSubmission.do?focusObj=ailmentDescription";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	 }
}