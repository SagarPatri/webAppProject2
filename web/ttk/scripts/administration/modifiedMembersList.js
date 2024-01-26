
	function toggle(sortid)
	{
	    document.forms[1].mode.value="doDefault";
	    document.forms[1].sortId.value=sortid;
	    document.forms[1].action = "/ModifiedMembersAction.do";
	    document.forms[1].submit();
	}//end of toggle(sortid)
	
	//function to display the selected page
	function pageIndex(pagenumber)
	{ 
		document.forms[1].mode.value="doDefault";
	    document.forms[1].pageId.value=pagenumber;
	    document.forms[1].action = "/ModifiedMembersAction.do";
	    document.forms[1].submit();
	}//end of pageIndex(pagenumber)
	
	//function to display previous set of pages
	function PressBackWard()
	{ 
	    document.forms[1].mode.value ="doBackward"; 
	    document.forms[1].action = "/ModifiedMembersAction.do";
	    document.forms[1].submit();     
	}//end of PressBackWard()
	
	//function to display next set of pages
	function PressForward()
	{ 
	    document.forms[1].mode.value ="doForward";
	    document.forms[1].action = "/ModifiedMembersAction.do";
	    document.forms[1].submit();     
	}//end of PressForward()
	
	
	