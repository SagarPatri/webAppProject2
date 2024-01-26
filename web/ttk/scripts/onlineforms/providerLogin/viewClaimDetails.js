/** @ (#) viewAuthDetails.js 20 Nov 2015 
 * Project     : TTK Healthcare Services
 * File        : viewAuthDetails.js
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

//on Back
function onBack()
{
	document.forms[1].mode.value="doClaimsLog";
   	document.forms[1].action="/OnlineProviderClaimsSearchAction.do";
   	document.forms[1].submit();
}

function onViewShortfall(obj)
{
	document.forms[1].mode.value="doViewShortfall";
   	document.forms[1].action="/OnlineProviderClaimsShortfallAction.do?shortfallSeqId="+obj;
   	document.forms[1].submit();
}

function onSaveShortfall(obj)
{
	if(document.forms[1].file.value==""){
		alert("Please select a file to Upload");
		document.forms[1].file.focus();
		return false;
	}else{
		var strPath = document.forms[1].file.value;
		var strfileext = strPath.substring((strPath.lastIndexOf(".")+1),strPath.length);
		if (strfileext != "pdf")
		{
			alert(" Please Select .pdf File only." );
			document.forms[1].file.value="";
			document.forms[1].file.focus();
			return false;
		}
	}
	document.forms[1].mode.value="doSaveShortfall";
   	document.forms[1].action="/OnlineProviderClaimsShortfallAction.do";
   	document.forms[1].submit();
}

function onShortfall()
{
	document.forms[1].mode.value="doPreAuthShortfall";
   	document.forms[1].action="/ViewAuthorizationDetails.do";
   	document.forms[1].submit();
}

function edit(rownum)
{
    document.forms[1].reset();
    //document.forms[1].mode.value="doCorpFocusedView";
    document.forms[1].mode.value="doViewAuthDetails";
    document.forms[1].rownum.value=rownum;
    document.forms[1].action = "/ViewAuthorizationDetails.do";
    document.forms[1].submit();
}//end of edit(rownum)



function onGenerateLetter()
{
	/*var partmeter = "?mode=doGeneratePreAuthLetter";
	var openPage = "/OnlineReportsAction.do"+partmeter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	//document.forms[1].mode.value="doShowShortFalls";
	window.open(openPage,'',features);*/
	
	var settlementNo	=	document.forms[1].settlementNo.value;
	var clmSeqId		=	document.forms[1].clmSeqId.value;
	var clmStatus		=	document.forms[1].clmStatus.value;
	var openPage = "/OnlineReportsAction.do?mode=doViewCommonForAll&module=claimSettlementFile&settlementNo="+settlementNo+
	"&clmSeqId="+clmSeqId+"&clmStatus="+clmStatus;
   var w = screen.availWidth - 10;
   var h = screen.availHeight - 49;
   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
   window.open(openPage,'',features);
}


function onGenerateShortFall(){
	
    	var parameterValue="";
      	var sfTypeVal="MDI";//document.getElementById("type").value;
	   	var authType = "CLM";//document.forms[1].authType.value;
	   	var claimSeqID = "14380";//document.forms[1].claimSeqID.value;
	   	
      	var parameter="";		      
      	
      	parameterValue="|"+document.forms[1].shortfallSeqID.value+"|"+sfTypeVal+"|";
 	  		parameter = "?mode=doGenerateXmlReport&reportType=PDF&parameter=" +
 	  				""+parameterValue+"&fileName=generalreports/ShortfallMedDoc.jrxml&reportID=ShortfallMid" +
 	  						"&shortfallNo="+shortfallNo+"&authType=" +
 	  								""+authType+"&claimSeqID="+claimSeqID;

  	var openPage = "/ReportsAction.do"+parameter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	//document.forms[1].mode.value="doShowShortFalls";
	window.open(openPage,'',features);
}


function onShortfallDoc(obj)
{
	var partmeter = "?mode=doShowPreAuthShortfall&fileName="+obj+"";
	var openPage = "/OnlineReportsAction.do"+partmeter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	//document.forms[1].mode.value="doShowShortFalls";
	window.open(openPage,'',features);
}//Using For provider Login Short fall Letter

 
