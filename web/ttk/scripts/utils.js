// Function to get the current document size.
function getDocumentSize() {
  var doc = new Object();
  if( typeof( window.innerWidth ) == 'number' ) {
    //Non-IE
    doc.width = window.innerWidth;
    doc.height = window.innerHeight;
  } else if( document.documentElement &&
      ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
    //IE 6+ in 'standards compliant mode'
    doc.width = document.documentElement.clientWidth;
    doc.height = document.documentElement.clientHeight;
  } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
    //IE 4 compatible
    doc.width = document.body.clientWidth;
    doc.height = document.body.clientHeight;
  }
  return doc;
}

// Function to resize the current document.
function resizeDocument(){
	var offSetHeight = 90;
	var obj = document.getElementById("mainContainerTable");
	var docSize = getDocumentSize();
	obj.style.height = docSize.height - offSetHeight + "px";
	var contentArea = document.getElementById("contentArea");
	if(contentArea){
		contentArea.style.height = docSize.height - 137 + "px";
	}
}

// Function to resize the current online document.
function onlineresizeDocument(){
	var offSetHeight = 135;
	var obj = document.getElementById("mainContainerTable");
	var docSize = getDocumentSize();
	obj.style.height = docSize.height - offSetHeight + "px";
	var contentArea = document.getElementById("contentArea");
	if(contentArea){
		contentArea.style.height = docSize.height - 162 + "px";
	}
}

// Function to simulate the DHTML drop down menu action.
function dropDownMenu(id, hideFlag){
	var obj = document.getElementById(id);
	if(hideFlag == 0){
		obj.style.display = "none";
		return;
	}
	if(hideFlag == 1){
			var firedobj = event.srcElement;
			var topelement = "body";
			var id= "idRO";
			var imgId = "drpArrow";
			while (firedobj.tagName!=topelement.toUpperCase() && firedobj.id!=id){
				firedobj = firedobj.parentElement;
			}
			if (firedobj.id==id){
				return;
			}
		obj.style.display = "none";
		return;
	}
	obj.style.display = "";
}

// Function to simulate the DHTML drop down menu action.
function mouseOver(obj){
	obj.className='rcDropDownTextHover'
}
// Function to simulate the DHTML drop down menu action.
function mouseOut(obj){
	obj.className='rcDropDownText'
}

// Used for prototype navigation
function goToURL(url){
		document.location.href=url;
}
// Used for prototype navigation
function delobj(){
  return confirm('Are you sure you want to delete the record(s)?');
}
// Used for prototype navigation
function checkCondition(){
	var obj = document.forms[0].searchby.options[document.forms[0].searchby.selectedIndex];
	goToURL(obj.value);
}
// Function to show/hide fields specific to certain contact types.
function showhideContactInfo(selObj){
	
	var selVal = selObj.options[selObj.selectedIndex].value;
	document.getElementById("general").style.display="none";
	document.getElementById("doctor").style.display="none";
	if(document.getElementById(selVal))
		document.getElementById(selVal).style.display="";
}
// Function to show/hide fields specific to certain status types.
function showhideStatusDetails(selObj){
	var selVal = selObj.options[selObj.selectedIndex].value;
	var empObj = document.getElementById("empanelvalidity");
	var disempObj = document.getElementById("disempaneldate");
	var closeObj = document.getElementById("closedate");
	var subStatusObj = document.getElementById("drpSubStatus");
	empObj.style.display="none";
	disempObj.style.display="none";
	closeObj.style.display="none";
	subStatusObj.style.display="";
	if(selVal == 'empanelled'){
		empObj.style.display="";
		subStatusObj.style.display="none";
	}
	else if(selVal == 'disempanelled'){
		disempObj.style.display="";
	}
	else if(selVal == 'closed'){
		closeObj.style.display="";
	}
}
function showhideStatusDetails1(selObj)
{
	var selVal = selObj.options[selObj.selectedIndex].value;
	var dagreeObj = document.getElementById("disagreedrp");
	dagreeObj.style.display="none";

	if(selVal == 'disagree'){
		dagreeObj.style.display="";
	}

}

function showhideFundDisbCode(selObj)
{
	var selVal = selObj.options[selObj.selectedIndex].value;
	var dagreeObj = document.getElementById("FloatObj");
	dagreeObj.style.display="none";

	if(selVal == 'FLT')
		dagreeObj.style.display="";
}

/*function openPopup(url,name,features) {
var WindowOpen = window.open(url,name,features);
try{
var obj = WindowOpen.name;
}
catch(e){
alert("System has been blocked by POP-UP BLOCKER.\nPlease disable the POP-UP BLOCKER and try again\nor\nPlease contact your system administrator. ");
}
}*/
// Function to launch the main application window in full screen mode.
function openFullScreenMode(openPage)
{
	
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 49;
	var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=0,width="+w+",height="+h;
	var childWin = window.open(openPage,'',features);
	if(window.XMLHttpRequest){
  		window.open('','_parent','');
 	}
 	else{
  		window.opener=self;
  	}
	window.close();
}

// Function to launch the main application window in full screen mode.
function openWebFullScreenMode(openPage)
{  
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 49;
	
		var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=0,width="+w+",height="+h;
	
	
	var childWin = window.open(openPage,'WebApplication',features);
	if(window.XMLHttpRequest){
		
  		window.open('','_parent','');
 	}
 	else{
 		
  		window.opener=self;
  	}
	
	 	window.close();
	
	
}

function openWebFullScreenModeYOO(openPage)
{
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 49;
	var features = "scrollbars=yes,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=0,width="+w+",height="+h;
	var childWin = window.open(openPage,'_self',features);
	
}

function openWebFullScreenModeWithScrollbar(openPage)
{  
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 49;
	
		var features = "scrollbars=yes,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=0,width="+w+",height="+h;
	
	
	var childWin = window.open(openPage,'WebApplication',features);
	if(window.XMLHttpRequest){
		
  		window.open('','_parent','');
 	}
 	else{
 		
  		window.opener=self;
  	}
	
	 	window.close();
	
	
}


function showhideButtonStatus(selObj)
{
	var selVal = selObj.options[selObj.selectedIndex].value;
	var drpObj = document.getElementById("ACT");
	document.forms[1].chngid.value="Inactivate";

	if(selVal == 'ICT'){
		document.forms[1].chngid.value="Activate";
	}
}

//this function is used to show/hide part of the Html docuement
function setState(obj_checkbox,id)
{
   if(obj_checkbox.checked)
   {
	   document.getElementById(id).style.display="";
   }
   else
   {
   	document.getElementById(id).style.display="none";
   }
}//end of setState(obj_checkbox,id)

function showhidePackageType(selObj)
{
	var selVal = selObj.options[selObj.selectedIndex].value;
	var dagreeObj = document.getElementById("NonPackage");
	dagreeObj.style.display="none";
	if(selVal == 'NPK')
	{
		dagreeObj.style.display="";
	}
}

// Function to expand/collapse the items.
function showhide(id,imgName,n)
{
	//alert("yes this");
   var imgObj = document.images[imgName];
   
   if(id=="clausetabcls.23"){
	   if(document.getElementById("dsp.23.92.2.1") != null){
	   if(document.getElementById("dsp.23.92.2.1").value=="WOP" || document.getElementById("dsp.23.92.2.1").value=="TSI"){
		   document.getElementById("dsp.23.92.2.3").disabled=true;  
	      }
	   }
	   if(document.getElementById("dsp.23.93.2.1") != null){
	   if(document.getElementById("dsp.23.93.2.1").value=="WOP" || document.getElementById("dsp.23.93.2.1").value=="TSI"){
		   document.getElementById("dsp.23.93.2.3").disabled=true;  
	     }
	   }
	   
	   if(document.getElementById("dsp.23.94.2.1") !=null){
	   if(document.getElementById("dsp.23.94.2.1").value=="WOP" || document.getElementById("dsp.23.94.2.1").value=="TSI"){
		   document.getElementById("dsp.23.94.2.3").disabled=true;  
	     }
	   }
	   
	   if(document.getElementById("dsp.23.95.2.1") != null){
	   if(document.getElementById("dsp.23.95.2.1").value=="WOP" || document.getElementById("dsp.23.95.2.1").value=="TSI"){
		   document.getElementById("dsp.23.95.2.3").disabled=true;  
	     }
	   }
	   
	   if(document.getElementById("dsp.23.96.2.1")!=null){
	   if(document.getElementById("dsp.23.96.2.1").value=="WOP" || document.getElementById("dsp.23.96.2.1").value=="TSI"){
		   document.getElementById("dsp.23.96.2.3").disabled=true;  
	     }
	   }
	   /*if(document.getElementById("dsp.23.97.2.1").value=="WOP" || document.getElementById("dsp.23.97.2.1").value=="TSI"){
		   document.getElementById("dsp.23.97.2.3").disabled=true;  
	   }*/
	   
	   if(document.getElementById("dsp.23.98.2.1") !=null){
	   if(document.getElementById("dsp.23.98.2.1").value=="WOP" || document.getElementById("dsp.23.98.2.1").value=="TSI"){
		   document.getElementById("dsp.23.98.2.3").disabled=true;  
	     }
	   }
	   
	   if(document.getElementById("dsp.23.99.2.1") !=null){
	   if(document.getElementById("dsp.23.99.2.1").value=="WOP" || document.getElementById("dsp.23.99.2.1").value=="TSI"){
		   document.getElementById("dsp.23.99.2.3").disabled=true;  
	    }
	   }
	   
	   if(document.getElementById("dsp.23.100.2.1") !=null){
	   if(document.getElementById("dsp.23.100.2.1").value=="WOP" || document.getElementById("dsp.23.100.2.1").value=="TSI"){
		   document.getElementById("dsp.23.100.2.3").disabled=true;  
	   }
	 }
	   
	   if(document.getElementById("dsp.23.101.2.1") !=null){
	   if(document.getElementById("dsp.23.101.2.1").value=="WOP" || document.getElementById("dsp.23.101.2.1").value=="TSI"){
		   document.getElementById("dsp.23.101.2.3").disabled=true;  
	   }
	}
	   if(document.getElementById("dsp.23.102.2.1") !=null){
	   if(document.getElementById("dsp.23.102.2.1").value=="WOP" || document.getElementById("dsp.23.102.2.1").value=="TSI"){
		   document.getElementById("dsp.23.102.2.3").disabled=true;  
	   }
	}
	   if(document.getElementById("dsp.23.103.2.1") !=null){
	   if(document.getElementById("dsp.23.103.2.1").value=="WOP" || document.getElementById("dsp.23.103.2.1").value=="TSI"){
		   document.getElementById("dsp.23.103.2.3").disabled=true;  
	   }
	 }
   }
   if(document.getElementById(id))
   {
	   var bFlag = (document.getElementById(id).style.display == "") ? 0 : 1;
	   if(bFlag)
	   {
			document.getElementById(id).style.display="";
			imgObj.src="/ttk/images/e.gif";
			imgObj.alt="Collapse";
	   }
	   else
	   {
			document.getElementById(id).style.display="none";
			imgObj.src="/ttk/images/c.gif";
			imgObj.alt="Expand";
	   }
	}
}

//**********************************************************
//This function takes the cursor to the first active input
//element in the form when page is loaded.
// User can also specify the ID of the Object that he wants to focus.
//**********************************************************
function handleFocus()
{
	if(typeof(JS_Focus_ID)!= 'undefined' && JS_Focus_ID!='')
	{
		if(document.getElementById(JS_Focus_ID))
		{
			document.getElementById(JS_Focus_ID).focus();	//to focus to particular element given by User
			return;
		}//end of if(document.getElementById(JS_Focus_ID))
	}//end of if(typeof(JS_Focus_ID)!= 'undefined' && JS_Focus_ID!='')
	if(typeof(JS_Focus_Disabled)!= 'undefined' && JS_Focus_Disabled==true)
	{
		return;
	}//end of if(typeof(JS_Focus_Disabled)!= 'undefined' && JS_Focus_Disabled==true)
	if(document.forms[1])	//if forms[1] exists then focus the first form element
	{
		var frmObj = document.forms[1];
		for (i = 0; i < frmObj.length; i++)
		{
			var element=frmObj.elements[i];
			var elementType = frmObj.elements[i].type;
			if((((element.disabled==false  && (elementType && ( elementType == "text" || elementType == "textarea" || elementType == "file" || elementType == "password" || elementType.toString().charAt(0) == "s" ))) && (element.id!="webboard" && element.id!="docviewer"))) && (donotFocus(element.id)==false))
			{
				if(typeof(element.readOnly)=='undefined')
				{
					element.focus();
					break;
				}
				else if(element.readOnly!=true)
				{
					element.focus();
					break;
				}//end of else if(element.readOnly!=true)
			}//end of if
		}//end of for (i = 0; i < frmObj.length; i++)
	}//end of if(document.forms[1])
}//end of handleFocus()

//*************************************************************
// This function checks whether the element to be focused
// or not checking against the JS_donotFocusIDs global variable
// which contains the array of ids to be not focused.
//*************************************************************
function donotFocus(id)
{
	var bFlag=false;
	if(typeof(JS_donotFocusIDs)!= 'undefined' && JS_donotFocusIDs.length >0)
	{
		for(j=0;j<JS_donotFocusIDs.length;j++)
		{
			if(JS_donotFocusIDs[j]==id)
			{
				bFlag=true;
				break;
			}
		}
	}
	return bFlag;
}
function isNumeric(field) {
        var re = /^[0-9]*\.*[0-9]*$/;
        if (!re.test(field.value)) {
            alert("Data entered must be Numeric!");
			field.focus();
			field.value="";
			return false;
        }
    }

function isNumericOnly(field) {
    var re = /^[0-9]*$/;
    if (!re.test(field.value)) {
        alert("Data entered must be Numeric!");
		field.focus();
		field.value="";
		return false;
    }
}



function isMandatorySingle(field1,field)
{
 		 var strControlValue1="";
		 strControlValue1 = document.getElementById(field1).value;
		// alert("strControlValue::2::::::::::"+strControlValue1);
		 if(strControlValue1 == ""){
			alert("Please enter Types of Claims ");
			field.focus();
			field.value="";
			return false;
		}
}

function isMandatory(field1,field2,field)
{
 		 var strControlValue1="";
 		 var strControlValue2="";
		 strControlValue1 = document.getElementById(field1).value;
		 strControlValue2 = document.getElementById(field2).value;
		// alert("strControlValue::2::::::::::"+strControlValue1+":::::"+strControlValue2);
		 if(strControlValue1 == "" || strControlValue2 == ""){
			alert("Please enter Types of Claims /Types of Encounter ");
			field.focus();
			field.value="";
			return false;
		}
}

function validateFields(){
	var formElementsLength = document.forms[1].elements.length;
	for(var i=0; i<formElementsLength; i++)
	{
		if(document.forms[1].elements[i].type == "text")
		{
				if(document.forms[1].elements[i].className!="textBox textDate")
					if(isNumeric(document.forms[1].elements[i])==false)
						return false;
		}
	}
	return true;
}
//*************************************************************
// This function open the popup window for country/state/account head/Relationship
// @param strControlName name of the control to which selected id is returned
// @param strTypeID identifier contains CON/STA/ACC/REL
//*************************************************************
function openList(strControlName,strTypeID)
{
 var w = 300;
 var h = 490;
 var strControlValue="";
 strControlValue = document.getElementById(strControlName).value;
 var openPage = "/ttk/common/showlist.jsp?typeId="+strTypeID+"&controlName="+strControlName+"&controlVal="+strControlValue+"&showRadio=N";
 var features = "scrollbars=1,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=0,width="+w+",height="+h;
 window.open(openPage,'',features);
}//end of openList(strControlName,strTypeID)


//*************************************************************
//This function open the popup window for country/state/account head/Relationship
//@param strControlName name of the control to which selected id is returned
//@param strTypeID identifier contains CON/STA/ACC/REL
//*************************************************************
function openListTariffIntX(strControlName,strTypeID,condParam)
{
alert("Inside Qatar utils.js openListTariffIntX");
var w = 350;
var h = 500;
var strCondParamValue="";
var strControlValue="";
strControlValue = document.getElementById(strControlName).value;
strCondParamValue	= document.getElementById(condParam).value;
alert(strCondParamValue);
/*if(strCondParamValue=="")
{
	alert("Select Provider First");
	return false;
}*/

if('GRP'==strCondParamValue)
{
	strCondParamValue	=	'GRP'+','+document.getElementById("grpProviderName").value;
}
else if('IND'==strCondParamValue)
{
	//strCondParamValue	=	'IND'+','+document.getElementById("grpProviderName").value;
}
else 
{
	strCondParamValue	=	'adminTariffNetwork'+','+document.getElementById(condParam).value;
}
var openPage = "/ttk/common/showlistintx.jsp?typeId="+strTypeID+"&controlName="+strControlName+"&controlVal="+strControlValue+"&showRadio=Y"+"&condParam="+strCondParamValue;
var features = "scrollbars=1,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=0,width="+w+",height="+h;
window.open(openPage,'',features);
}//end of openList(strControlName,strTypeID)


//*************************************************************
//This function open the popup window for country/state/account head/Relationship
//@param strControlName name of the control to which selected id is returned
//@param strTypeID identifier contains CON/STA/ACC/REL //test nag
//*************************************************************
function openListIntX(strControlName,strTypeID)
{
var w = 350;
var h = 500;
var strControlValue="";
if(document.getElementById("providerForNetworkId") != null){
	strControlValue =	document.getElementById("networkCategoryId").value;
}else{
strControlValue = document.getElementById(strControlName).value;
}
var openPage = "/ttk/common/showlistintx.jsp?typeId="+strTypeID+"&controlName="+strControlName+"&controlVal="+strControlValue+"&showRadio=N";
var features = "scrollbars=1,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=0,width="+w+",height="+h;
window.open(openPage,'',features);
}//end of openList(strControlName,strTypeID)


function openRadioListIntX(strControlName,strTypeID,condParam)
{
 var w = 300;
 var h = 490;
 var strControlValue="";
 strControlValue = document.getElementById(strControlName).value;
 strCondParamValue	= document.getElementById(condParam).value;
 if(strCondParamValue=="")
 {
 	alert("Select Provider First");
 	return false;
 }

 if('GRP'==strCondParamValue)
 {
 	strCondParamValue	=	'GRP';
 }
 else if('IND'==strCondParamValue)
 {
 	strCondParamValue	=	'IND';
 }
 else 
 {
 	strCondParamValue	=	'adminTariffNetwork'+','+document.getElementById(condParam).value;
 }
 var openPage = "/ttk/common/showlistRadioIntX.jsp?typeId="+strTypeID+"&controlName="+strControlName+"&controlVal="+strControlValue+"&showRadio=Y&condParam="+strCondParamValue;
 var features = "scrollbars=1,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=0,width="+w+",height="+h;
 window.open(openPage,'',features);
}//end of openRadioList(strControlName,strTypeID)


function openRadioListIntX1(strControlName,strTypeID)
{
 var w = 350;
 var h = 490;
 var strControlValue="";
 strControlValue = document.getElementById(strControlName).value;
 var openPage = "/ttk/common/showlistRadioIntX.jsp?typeId="+strTypeID+"&controlName="+strControlName+"&controlVal="+strControlValue+"&showRadio=Y";
 var features = "scrollbars=1,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=0,width="+w+",height="+h;
 window.open(openPage,'',features);
}//end of openRadioList(strControlName,strTypeID)


//*************************************************************
// This function open the popup window for showing the records from cache/ database and a single
// record can be selected from the Pop up.  
// @param strControlName name of the control to which selected id is returned
// @param strTypeID identifier for opening the records from the cache/Database 
//*************************************************************

function openRadioList(strControlName,strTypeID)
{
 var w = 300;
 var h = 490;
 var strControlValue="";
 strControlValue = document.getElementById(strControlName).value;

 var openPage = "/ttk/common/showlist.jsp?typeId="+strTypeID+"&controlName="+strControlName+"&controlVal="+strControlValue+"&showRadio=Y";
 var features = "scrollbars=1,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=0,width="+w+",height="+h;
 window.open(openPage,'',features);
}//end of openRadioList(strControlName,strTypeID)


//*****************************************************************************************
//This function is used to view the selected records from the Cache / database in the Pop
// up screen.
// @param strControlName name of the control to which selected id is returned
// @param strTypeID identifier for opening the records from the cache/Database
//*****************************************************************************************
function showList(strControlName,strTypeID)
{
 var w = 425;
 var h = 490;
 var features = "scrollbars=1,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=0,width="+w+",height="+h;
 var strControlValue="";
 strControlValue = document.getElementById(strControlName).value;
 if(strControlValue!='')
 {
 	window.open("/ShowListAction.do?mode=doView&amp;flag="+strTypeID+"&amp;value="+strControlValue,'',features);
 }//end of if(strControlValue!='')
 else
 {
 	return false;
 }//end of else
}//end of function showList(strControlName,strTypeID)


function onCommonUploadDocuments(strTypeId)
{
	if(!JS_SecondSubmit)
	{
	document.forms[1].mode.value="doDefaultUploadView";
	//var seqID	=	document.forms[1].seqId.value;
	var seqID = document.getElementById("seqId").value;
	if(seqID == "" || seqID==null){
		alert("Save Preauth before upload documents");
		return;
	}
	var preauthOrClaimType ="";
	if(document.getElementById("preauthorclaimswitchTypeId")!=null){
	 preauthOrClaimType = document.getElementById("preauthorclaimswitchTypeId").value;
	}
	if(strTypeId==="PAT"&&document.getElementById("patSeqId")){
		seqID=document.getElementById("patSeqId").value;
	}
	
	if(strTypeId=="CFD" && preauthOrClaimType=="PAT"){
		seqID=document.getElementById("seqId").value;
		strTypeId=strTypeId+"|"+preauthOrClaimType;
	}
	
	if(strTypeId=="CFD" && preauthOrClaimType=="CLM"){
		seqID=document.getElementById("seqId").value;
		strTypeId=strTypeId+"|"+preauthOrClaimType;
	}
	document.forms[1].action="/DocsUpload.do?typeId="+strTypeId+"&seqId="+seqID+"&preauthOrClaimType="+preauthOrClaimType;
	JS_SecondSubmit=true;
	document.forms[1].submit();
	}
}

function doCommonUpload()
{	
	var file	=	document.forms[1].file.value;

	if(file == "")
	{
		alert(" Browse File is mandatory.");
		return false;
	}
	
	if(!JS_SecondSubmit)
	  {
		trimForm(document.forms[1]);
		var seqID	=	document.getElementById("seqId").value;
		var strTypeId = document.getElementById("typeId").value;
		if(strTypeId==="PAT"&&document.getElementById("patSeqId")){
			seqID=document.getElementById("patSeqId").value;
		}
		/*if(strTypeId==="CFD|PAT"){
			seqID=document.getElementById("seqId").value;
		}*/
		document.forms[1].mode.value="doCommonDocUploads";
	
		if(strTypeId == "HOS|CLM"  || strTypeId == "HOS|PAT" || strTypeId == "PTR|CLM" || strTypeId == "PTR|PAT" || strTypeId == "PBM|CLM" || strTypeId == "PBM|PAT" ){
			document.forms[1].action = "/onlineDocsUpload.do?typeId="+strTypeId+"&seqId="+seqID;
		}else if(strTypeId==="CFD|PAT" || strTypeId==="CFD|CLM"){
			document.forms[1].action = "/DocsUpload.do?typeId="+strTypeId+"&seqId="+seqID;
		}else{
			document.forms[1].action = "/DocsUpload.do?typeId="+strTypeId+"&seqId="+seqID;
		}
		JS_SecondSubmit=true;
		document.forms[1].submit();
	}//end of if(!JS_SecondSubmit)
}//end of onSave()

function onCommonUploadClose(strTypeId)
{
	var seqId = document.forms[1].seqId.value
	if(!TrackChanges()) return false;//end of if(!TrackChanges())
    document.forms[1].mode.value="doUploadClose";
    document.forms[1].action="/DocsUpload.do?typeId="+strTypeId+"&seqId ="+seqId;
    document.forms[1].submit();
}//end of onClose()

function onCommonDelete()
{
	if(!mChkboxValidation(document.forms[1]))
    {
		var strTypeId = document.getElementById("typeId").value;
		var seqID	=	document.getElementById("seqId").value;
		var msg = confirm("Are you sure you want to delete the Information ?");
		if(msg)
		{
			document.forms[1].mode.value = "doDelete";
			
			if(strTypeId == "HOS|CLM"  || strTypeId == "HOS|PAT" || strTypeId == "PTR|CLM" || strTypeId == "PTR|PAT" || strTypeId == "PBM|CLM" || strTypeId == "PBM|PAT" ){
				document.forms[1].action = "/onlineDocsUpload.do?typeId="+strTypeId+"&seqId="+seqID;
			}else{
				document.forms[1].action = "/DocsUpload.do?typeId="+strTypeId+"&seqId="+seqID;
			}
		    document.forms[1].submit();
		}//end of if(msg)
    }//end of if(!mChkboxValidation(document.forms[1]))
}//end of onDelete()

function edit(rownum)
{
	//document.forms[1].fileName.value = strFileName;
	var openPage = "/DoViewUploadFiles.do?mode=doViewUploadFiles&module=mou&rownum="+rownum;
   var w = screen.availWidth - 10;
   var h = screen.availHeight - 49;
   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
   window.open(openPage,'',features);
}//end of edit(rownum)

function makeSomeFieldReadOnly(element,elementId){
   var limitValue = 	element.value;
   if("WOP"==limitValue || "TSI"==limitValue){
	   document.getElementById(elementId).value="";
	   document.getElementById(elementId).disabled=true;
   }else{
	   document.getElementById(elementId).disabled=false;
   }
}

function dateFormatValidation(elementId){
	
	var reg = /^([0-2][0-9]|(3)[0-1])(\/)(((0)[0-9])|((1)[0-2]))(\/)\d{4}$/;
	 var value = document.getElementById(elementId).value;
	 if(value!=""){
		    if(!reg.test(value)){
			  document.getElementById(elementId).value="";
			  alert("Invalid Date Format");
		      return false;
		      }
		    }
}