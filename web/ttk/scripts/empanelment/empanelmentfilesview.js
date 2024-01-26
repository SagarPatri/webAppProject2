//java script for the grouplist.jsp screen in the Group Registration of Empanelment flow.


function isValidated()
{
		//checks if received Date is entered
		if(document.forms[1].fileUploadDate.value.length!=0)
		{
			if(isDate(document.forms[1].fileUploadDate,"File Upload Date")==false){
				document.forms[1].fileUploadDate.value	=	"";
				return false;
			}
		}// end of if(document.forms[1].sRecievedDate.value.length!=0)
		return true;
}//end of isValidated()
function onCloseViewUpload()
{
	document.forms[1].mode.value = "doClose";
	document.forms[1].child.value="";
	document.forms[1].action = "/HRFileSearchAction.do";
    document.forms[1].submit();
}//end of onContactClose()


//function Search
function onSearch()
{
	if(!JS_SecondSubmit)
	 {
	    if(isValidated())
		{
	    	
			trimForm(document.forms[1]);
	    	document.forms[1].mode.value = "doSearch";
	    	document.forms[1].action = "/HRFileSearchAction.do";
			JS_SecondSubmit=true;
	    	document.forms[1].submit();
	    }// end of if(isValidated())
	 }//end of if(!JS_SecondSubmit)
}//end of onSearch()

//function edit(rownum)
function edit(rownum){
	var msg = confirm("Are you sure you want download file?");
	if(msg)
	{
    document.forms[1].mode.value="doViewUploadFiles";
    document.forms[1].rownum.value=rownum;
    //document.forms[1].tab.value ="Groups";
    document.forms[1].action = "/HRFileSearchAction.do";
    document.forms[1].submit();
	}
}//end of edit(rownum)
//function to display previous set of pages
function PressBackWard()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/HRFileSearchAction.do";
    document.forms[1].submit();
}//end of PressBackWard()

//function to display next set of pages
function PressForward()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/HRFileSearchAction.do";
    document.forms[1].submit();
}//end of PressForward()



//function on click of pageIndex
function pageIndex(strPageIndex)
{
	document.forms[1].reset();
	document.forms[1].mode.value="doSearch";
	document.forms[1].selectedroot.value="";
	document.forms[1].pageId.value=strPageIndex;
    document.forms[1].action="/HRFileSearchAction.do";
	document.forms[1].submit();
}//end of pageIndex()

// function to completed the document 

function completeDocument()
{
	if(!mChkboxValidation(document.forms[1])){
	document.forms[1].mode.value="docompleteDocument";
    document.forms[1].action="/HRFileSearchAction.do";
	document.forms[1].submit();
	}
}//end of completeDocument()

function toggle(sortid)
{
	document.forms[1].reset();
	var status = document.forms[1].listofStatus.value;
	if(status == "Pending" && activelink == "Status Changed Date"){
		
	}else{
    document.forms[1].mode.value="doSearch";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/HRFileSearchAction.do";
    document.forms[1].submit();
	}
}//end of toggle(sortid)
