function clinicianSearch(element){
	if(!JS_SecondSubmit)
	 {  
	    	document.forms[1].mode.value = "clinicianSearch";
	    	document.forms[1].action = "/ClinicianAction.do";
	    	JS_SecondSubmit=true;
	    	element.innerHTML	=	"<b>"+"Please Wait...!!"+"</b>";
	    	document.forms[1].submit();
	 }
}//end of activityCodeSearch()

function edit(rownum){	
	if(!JS_SecondSubmit)
	 { 
		document.forms[1].mode.value="doSelectClinicianId";
		document.forms[1].rownum.value=rownum;
		document.forms[1].action = "/ClinicianAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of edit(rownum)
function pageIndex(pagenumber)
{
    document.forms[1].reset();
    document.forms[1].mode.value="clinicianSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/ClinicianAction.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber)

function PressForward()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doClinicianForward";
    document.forms[1].action = "/ClinicianAction.do";
    document.forms[1].submit();
}//end of PressForward()
function PressBackWard()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doClinicianBackward";
    document.forms[1].action = "/ClinicianAction.do";
    document.forms[1].submit();
}//end of PressBackWard()
function toggle(sortid)
{
    document.forms[1].reset();
    document.forms[1].mode.value="clinicianSearch";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/ClinicianAction.do";
    document.forms[1].submit();
}//end of toggle(sortid)
function goGeneral()
{
	if(!JS_SecondSubmit)
	 {  
		document.forms[1].mode.value="doClose";
		document.forms[1].action = "/ClinicianAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of closeClinician()
function closeClinicians(){
	if(!JS_SecondSubmit)
	 {  	
		document.forms[1].mode.value="closeClinicians";
		document.forms[1].action = "/ClinicianAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of closeClinician()

