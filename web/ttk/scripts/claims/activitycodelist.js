function activityCodeSearch(element){
	if(!JS_SecondSubmit)
	 {  
	    	document.forms[1].mode.value = "activityCodeSearch";
	    	document.forms[1].action = "/ClaimActivityListAction.do";
	    	JS_SecondSubmit=true;
	    	element.innerHTML	=	"<b>"+"Please Wait...!!"+"</b>";
	    	document.forms[1].submit();
	 }
}//end of activityCodeSearch()

function edit(rownum){	
	if(!JS_SecondSubmit)
	 {  
		document.forms[1].mode.value="doSelectActivityCode";
		document.forms[1].rownum.value=rownum;
		document.forms[1].action = "/ClaimActivityListAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of edit(rownum)
function pageIndex(pagenumber)
{
    document.forms[1].reset();
    document.forms[1].mode.value="activityCodeSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/ClaimActivityListAction.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber)

function PressForward()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doActivityCodeForward";
    document.forms[1].action = "/ClaimActivityListAction.do";
    document.forms[1].submit();
}//end of PressForward()
function PressBackWard()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doActivityCodeBackward";
    document.forms[1].action = "/ClaimActivityListAction.do";
    document.forms[1].submit();
}//end of PressBackWard()
function toggle(sortid)
{
    document.forms[1].reset();
    document.forms[1].mode.value="activityCodeSearch";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/ClaimActivityListAction.do";
    document.forms[1].submit();
}//end of toggle(sortid)

function closeActivityCodeList()
{
	if(!JS_SecondSubmit)
	 {  
		document.forms[1].mode.value="closeActivityCode";
		document.forms[1].action = "/ClaimActivityListAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of closeActivityCode()
function changeSearchType(obj){
	if(obj.value=="ACT")
		 document.getElementById("internalCode").style.display="none";
	else
		document.getElementById("internalCode").style.display="inline";
}
