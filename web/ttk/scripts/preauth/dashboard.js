/**
 * 
 */

function displayManaDashboard(){
	var flag = document.getElementById("securityflagid").value;
	if(flag=="false"){
		alert("Access Denied,Please Contact to administrator");
		return;
	}
	document.forms[1].mode.value="doDisplayManaDashbordDefault";
    document.forms[1].action = "/PreAuthManagementDeaultAction.do?";
    document.forms[1].submit();
}//end displayManaDashboard

function queueStatus(val,str,count){
	document.forms[1].mode.value="doDisplayManaDashbord";
    document.forms[1].action = "/PreAuthManagementAction.do?queueStatus="+val+"&searchType="+str+"&search_type_count="+count;
    document.forms[1].submit();
	
}

function quickSearch(str){
	
 var serId	= document.getElementById("searchTypeId").value;
 var serVal = document.getElementById("searchDataId").value;
 if(serVal =="" ||serVal==null){
	 alert("Please enter value to search criteria");
	 return false;
 }
 document.forms[1].mode.value="doDisplayManaDashbord";
 document.forms[1].action = "/PreAuthManagementAction.do?serId="+serId +"&serVal="+serVal;
 document.forms[1].submit();
		
}

function edit(rownum,strtablename){
	
	/*alert("edit...");*/
	/*	
	var features = "scrollbars=1,status=0,toolbar=0,top=0,left=0,resizable=1,menubar=0,width=950,height=700";
	window.open("/PreAuthManagementAction.do?mode=doDisplayManaDashbord", features);
	window.open ("/PreAuthAction.do?mode=doViewPreauth",'Ver_articulo',features); */
	var flag = document.getElementById("accessflagid").value;
	if(flag=="false"){
		alert("Access Denied,Please Contact to administrator");
		return;
	}
	
	document.forms[1].mode.value="doViewPreauth";
	document.getElementById("rownumId").value=rownum;
	document.getElementById("tableNameId").value=strtablename;
   /* document.forms[1].rownum.value=rownum;
    document.forms[1].tableName.value = strtablename;*/
    document.forms[1].leftlink.value ="Pre-Authorization";
    document.forms[1].sublink.value ="Processing";
    document.forms[1].tab.value ="System Preauth Approval";
    document.forms[1].action = "/PreAuthManagementPreauthViewAction.do";
    document.forms[1].submit();	
}


function onrefresh(){
	document.forms[1].mode.value="doDisplayManaDashbordDefault";
	 document.forms[1].action = "/PreAuthManagementDeaultAction.do";
	 document.forms[1].submit();
	 
}


function pageIndex(pagenumber,str){
	
  
    
    document.getElementById("childId").value=str;
    document.getElementById("pageId").value=pagenumber;
    
     var search_type_name = document.getElementById("search_type_name_id").value;
     document.forms[1].mode.value="doDisplayManaDashbord";
	 document.forms[1].action = "/PreAuthManagementAction.do?searchType="+search_type_name;
	 document.forms[1].submit();
	
}



function onUserAssignment(num){
/*	alert("num.."+num);	*/
	
	
	$.ajax({
        url :"/getAssignmentDetails.do?mode=getAssignDetails&rownumber="+num,
        dataType:"text",
		type:"POST",
		async:false,
	    context:document.body,
        success : function(responseData) {
        	
        /*	alert("responseData..."+responseData);*/
        	document.getElementById("dataid").innerHTML=responseData;
          }//success data
	 });//$.ajax(	
	
	  
	  $("#advance_search").show("slide", {
	        direction: "right"
	    }, 500);
	    $("#vd-overlay").addClass("vd-overlay-side-panel").fadeIn("slow");
}

function closemodel(){
	 $("#advance_search").hide("slide", {
	        direction: "right"
	    }, 500);
	    $("#vd-overlay").removeClass("vd-overlay-side-panel").fadeIn("slow");
}


function saveuserAssign(){
	var preseqid=document.getElementById("preauthSeqId").value;
	var preno=document.getElementById("preauthnoId").value;
	var assignto=document.getElementById("assigntoId").value;
	var remark=document.getElementById("remarkId").value;
	
	
	
	
	$.ajax({
        url :"/saveAssignUser.do?mode=doSaveUserAssign&preseqid="+preseqid +"&assignto="+assignto+"&remark="+remark,
        dataType:"text",
		type:"POST",
		async:false,
	    context:document.body,
        success : function(responseData) {
        	
        	
        	
          }//success data
	 });//$.ajax(
	
	
}

function displayDoctorDashboard(){
	
	var flag = document.getElementById("docSeqirityFlag").value;
	if(flag=="false"){
		alert("Access Denied,Please Contact to administrator");
		return;
	}
	var widhstr = screen.width;
	var highstr = screen.height;
	var features = "height="+highstr+",width="+widhstr+",resizable=yes,scrollbars=yes,toolbar=yes,menubar=yes,location=yes";
	window.open ("/PreAuthDoctorDashboardDeaultAction.do?mode=doDisplayDoctorDashboardDefault",'Ver_articulo',features);
}

function onUserIcon(rownum)
{
	if(!JS_SecondSubmit)
	 {
		
			document.forms[1].mode.value="doAssign";
			document.forms[1].child.value="Assign";
			document.forms[1].rownum.value=rownum;
			document.forms[1].action="/AssignToDashBordPreauthAction.do";
			JS_SecondSubmit=true;
			document.forms[1].submit();
		
	 }
}

function closemanagementdashboard(){
	document.forms[1].mode.value="doDefault";
    document.forms[1].action = "/PreAuthDashboardAction.do?";
    document.forms[1].submit();
	
}

function PressForward()
{
     
    document.forms[1].mode.value="doDisplayManaDashborddoforword";
    document.forms[1].action = "/PreAuthManagementAction.do?forwardpar=forward";
    document.forms[1].submit();
}//end of PressForward()


function PressForward(val)
{
	
    if(val ==='tableData'){
    var search_type_name = document.getElementById("search_type_name_id").value; 	
    document.forms[1].mode.value="doDisplayManaDashborddoforword";
    document.forms[1].action = "/PreAuthManagementAction.do?searchType="+search_type_name;
    document.forms[1].submit();
    }
}//end of PressForward()


function PressBackWard()
{
	 var search_type_name = document.getElementById("search_type_name_id").value; 
	    document.forms[1].mode.value="doDisplayManaDashborddobackword";
	    document.forms[1].action = "/PreAuthManagementAction.do?searchType="+search_type_name;
	    document.forms[1].submit();
}//end of PressBackWard()
