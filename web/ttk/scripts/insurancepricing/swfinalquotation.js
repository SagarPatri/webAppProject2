

function edit(rownum){
	
   	var openPage = "/SwFinalGenerateQuotationAction.do?mode=doViewUploadDocs&rownum="+rownum;	 
   	var w = screen.availWidth - 10;
  	var h = screen.availHeight - 49;
  	var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
  	window.open(openPage,'',features);
}

function onsearchQuotation(){
	
	 if(!JS_SecondSubmit)
	   {
		   document.forms[1].mode.value="doSearch";
		   document.forms[1].action="/SwFinalGenerateQuotationAction.do";
		   JS_SecondSubmit=true;
		   document.forms[1].submit();
		}//end of if(!JS_SecondSubmit)
}


function ongenerateQuotation(){
	//for multiple files //working for single page
	  /* var partmeter = "?mode=doViewQuotaion&parameter="+document.forms[1].groupseqid.value+"&fileName=generalreports/GenerateQauotation.jrxml&reportID=GenerateQauotation&reportType=PDF";
	   document.forms[1].action="/SwFinalGenerateQuotationAction.do"+partmeter;
	   JS_SecondSubmit=true;
	   document.forms[1].submit();*/
	
	//for multiple page
	 if(!JS_SecondSubmit){ 
	 var partmeter = "?mode=doGenerateQuotaionDynamic&parameter="+document.forms[1].groupseqid.value+"&fileName=generalreports/GenerateQauotation.jrxml&reportID=GenerateQauotation&reportType=PDF";
	   document.forms[1].action="/SwFinalGenerateQuotationAction.do"+partmeter;
	   JS_SecondSubmit=true;
	   document.forms[1].submit();
	 }
}

function toggle(sortid)
{
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/SwFinalGenerateQuotationAction.do";
    document.forms[1].submit();
}//end of toggle(sortid)

//function to display the selected page
function pageIndex(pagenumber)
{
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/SwFinalGenerateQuotationAction.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber)

//function to display previous set of pages
function PressBackWard()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/SwFinalGenerateQuotationAction.do";
    document.forms[1].submit();
}//end of PressBackWard()

//function to display next set of pages
function PressForward()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/SwFinalGenerateQuotationAction.do";
    document.forms[1].submit();
}//end of PressForward()


function getproductList(insSeqid)
{
	var insSeqidvar = insSeqid.value;
	
	if(!TrackChanges()) return false;
 	document.forms[1].mode.value = "doProductList";
 	document.forms[1].action = "/SwFinalGenerateQuotationAction.do?insSeqid="+insSeqidvar;
 	document.forms[1].submit();		

}

//Ajax code to get Provider details dynamically. for intX
function getInsCode(obj)
{
	
	var provname = obj.options[obj.selectedIndex].text;

	if (window.ActiveXObject){
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		if (xmlhttp){
			xmlhttp.open("GET",'/EditBatchAction.do?mode=getInsCode&InsName=' +provname ,true);
			xmlhttp.onreadystatechange=insCode;
			xmlhttp.send();
		}
	}
	
}

function insCode(){
	var result,result_arr;
	if (xmlhttp.readyState==4){
		//alert(xmlhttp.status);
	if (xmlhttp.status==200){
				result = xmlhttp.responseText;
			result_arr = result.split(","); 
			
			document.getElementById("officeCode").value=result_arr[0];
		//	document.forms[1].hidInsuranceSeqID.value=result_arr[1];
			
		}
	}
}

function onCloseQuotation(){
	 	if(!TrackChanges()) return false;
	 	 document.forms[1].reset();
		    document.forms[1].mode.value= "doDefaultQuotation"; 
		    document.forms[1].tab.value ="Generate Quote";
		    document.forms[1].action="/SwGenerateQuotationAction.do"; 
		    document.forms[1].submit();
}

function onSave(){

	 if(!JS_SecondSubmit)
	   {
		   document.forms[1].mode.value="doSave";
		   document.forms[1].action="/SwFinalGenerateQuotationActionSave.do";
		   JS_SecondSubmit=true;
		   document.forms[1].submit();
		}//end of if(!JS_SecondSubmit)
	
}

function onViewEmailDocument(filename)
{
	
   	var openPage = "/SwFinalGenerateQuotationAction.do?mode=doViewEmailUploadDocs&filename="+filename;	 
   	var w = screen.availWidth - 10;
  	var h = screen.availHeight - 49;
  	var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
  	window.open(openPage,'',features);
}



function onPolicyCopy(){
       var partmeter = "?mode=doPolicyCopyDynamic&parameter="+document.forms[1].groupseqid.value+"&fileName=generalreports/PolicyCopy.jrxml&reportID=PolicyCopy&reportType=PDF";
	   var openPage = "/SwFinalGenerateQuotationAction.do"+partmeter;
	   var w = screen.availWidth - 10;
	   var h = screen.availHeight - 49;
	   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	   window.open(openPage,'',features);
	
}



function softcopyUpload()
{
	
	var EnrollmentSoftUploadURL	=	document.forms[1].EnrollmentSoftUploadURL.value;
	var userSeqId	=	document.forms[1].userSeqId.value;
	var policy_num = document.forms[1].policySeqid.value;
	var productTyp_num = document.forms[1].productSeqID.value;
	var insComp_num = document.forms[1].companyName.value;
	var grpId_num = document.forms[1].groupId.value;

 var param = { 'loginType' : 'Enrollment', 'userId': userSeqId ,'password' :'m','policy_num': policy_num,'productTyp_num':productTyp_num,'insComp_num':insComp_num,'grpId_num':grpId_num,'applogic':'QUO'};
 OpenWindowWithPost(EnrollmentSoftUploadURL, "width=1000, height=600, left=100, top=100, resizable=yes, scrollbars=yes", "NewFile", param);
}

function OpenWindowWithPost(url, windowoption, name, params)
{
 var form = document.createElement("form");
 form.setAttribute("method", "post");
 form.setAttribute("action", url);
 form.setAttribute("target", name);
 for (var i in params)
 {
   if (params.hasOwnProperty(i))
   {
     var input = document.createElement('input');
     input.type = 'hidden';
     input.name = i;
     input.value = params[i];
     form.appendChild(input);
   }
 }
 document.body.appendChild(form);
// window.open("", name, windowoption);
 form.submit();
 document.body.removeChild(form);
}

function onAcceptQuotation()
{
	var trendFactor	=	document.forms[1].trendFactor.value;
	
	if(trendFactor == "Y" ){
		if(document.forms[1].authority.value == "N"){
		alert("Trend factor is less than 6% approval is required .Approval is required from higher authority");
		return false;
	}else if(document.forms[1].authority.value == "Y" && !document.forms[1].trendfactorYN.checked){
		alert("Trend factor is less than 6% approval is required ");
		return false;
	}
	}
	

	if(document.forms[1].authority.value == "Y" ){
	if(document.forms[1].trendfactorYN.checked)
		document.forms[1].trendFactorYNValue.value="Y";
	else
		document.forms[1].trendFactorYNValue.value="N";
	}
	
	 if(!JS_SecondSubmit)
	   {
		   document.forms[1].mode.value="doAcceptQuotation";
		   document.forms[1].action="/SwAcceptQuotationAction.do";
		   JS_SecondSubmit=true;
		   document.forms[1].submit();
		}//end of if(!JS_SecondSubmit)

}


function trendfactorload()
{
	if(document.forms[1].trendFactorYNValue.value=="Y")
	    document.forms[1].trendfactorYN.checked=true;
	
	else
		document.forms[1].trendfactorYN.checked=false;
}


function onchangeCreditNote(obj)
{
	 if(obj.value == "OTHD"){
	
		document.getElementById("creditGenerationOth").className="textBox textBoxLarge ";
	 	document.getElementById("creditGenerationOth").readOnly="";
	 	document.getElementById("creditGenerationOth").value = "";
	 }else{
		var creditgenvaluedays = document.forms[1].creditGeneration.value;
	
		document.getElementById("creditGenerationOth").className="textBox textBoxLarge textBoxDisabled";
		document.getElementById("creditGenerationOth").readOnly="true";
		document.getElementById("creditGenerationOth").value = creditgenvaluedays;
	 }
}
