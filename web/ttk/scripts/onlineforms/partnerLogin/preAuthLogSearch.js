/** @ (#) preAuthLogSearch.js 20 Nov 2015 
 * Project     : TTK Healthcare Services
 * File        : preAuthLogSearch.js
 * Author      : Kishor kumar S H
 * Company     : RCS Technologies
 * Date Created: 20 Nov 2015
 *
 * @author 		 : Kishor kumar S H
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */

//on proceed corporate
function onSearch()
{
	if(!JS_SecondSubmit)
	{
	document.forms[1].mode.value="doSearchPreAuth";
   	document.forms[1].action="/SearchPartnerPreAuthLogsAction.do";
   	JS_SecondSubmit=true;
   	document.forms[1].submit();
	}
}


function edit(rownum)
{
    document.forms[1].reset();
    //document.forms[1].mode.value="doCorpFocusedView";
    document.forms[1].mode.value="doViewAuthDetails";
    document.forms[1].rownum.value=rownum;
    document.forms[1].action = "/ViewPartnerAuthorizationDetails.do";
    document.forms[1].submit();
}//end of edit(rownum)

function edit3(rownum)
{
	if(!JS_SecondSubmit)
	{
 document.forms[1].mode.value ="doView";
 document.forms[1].rownum.value=rownum;
 document.forms[1].action = "/OnlinePartnerPreAuthProceedAction.do?fromFlag=preAuth";
 JS_SecondSubmit=true;
 document.forms[1].submit();
	}
} 


//function to sort based on the link selected
function toggle(sortid)
{
	document.forms[1].reset();
    document.forms[1].mode.value="doSearchPreAuth";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/PartnerSearchPreAuthLogsAction.do";
    document.forms[1].submit();
}//end of toggle(sortid)

//function to display the selected page
function pageIndex(pagenumber)
{
	document.forms[1].reset();
    document.forms[1].mode.value="doSearchPreAuth";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/PartnerSearchPreAuthLogsAction.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber).

//function to display previous set of pages
function PressBackWard()
{
	if(!JS_SecondSubmit)
	{
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/PartnerSearchPreAuthLogsAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of PressBackWard()

//function to display next set of pages
function PressForward()
{
	if(!JS_SecondSubmit)
	{
    document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/PartnerSearchPreAuthLogsAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of PressForward()
function edit2(rownum)
{
	var openPage = "/OnlineReportsAction.do?mode=doViewCommonForAll&module=preAuthorizationFile&rownum="+rownum;
   var w = screen.availWidth - 10;
   var h = screen.availHeight - 49;
   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
   window.open(openPage,'',features);
}

function onEnhancementNew(rownum)
{
	if(!JS_SecondSubmit)
	{
 document.forms[1].mode.value ="doViewEnhance";
 document.forms[1].rownum.value=rownum;
 document.forms[1].action = "/OnlinePreAuthProceedAction.do?fromFlag=preAuthEnhance";
 JS_SecondSubmit=true;
 document.forms[1].submit();
	}
} 
