//This is javascript file for clauselist.jsp

//This function is called when continue button is pressed
function onContinue()
{
		if(!getCheckBox())
		{
			alert("Please select at least one Clause");
			return false;
		}
	    document.forms[1].mode.value="doContinueMemberRule";
		document.forms[1].action = "/MemberClauseListAction.do";
		document.forms[1].submit();
}//end of onContinue()

//This function is called when Reset Button is pressed
function onReset()
{
	document.forms[1].reset();
}//end of onReset()

//This function is used to check whether any clause is selected or not
function getCheckBox()
{
	var NumElements=document.forms[1].elements.length;
	var chkStatus=false;
	var nCheckBox=0;
	for(n=0; n<NumElements;n++)
	{
			if(document.forms[1].elements[n].type=="checkbox")
			{
				nCheckBox++;
				var tt = document.forms[1].elements[n].name;
				if(tt.indexOf("chk") != -1)
				{
					if(document.forms[1].elements[n].checked==true)
					return true;
				}
			}
	}//end of for
	//There are no checkboxes to be checked, so simply return true;
	if(nCheckBox <=0) chkStatus=true;
	return chkStatus;
}//end of getCheckBox

//This function is used to select/clear all the clauses based on the identifier
function onCheckAll(checkFlag)
{
	for(var i=0;i<document.forms[1].length;i++)
	{
		if(document.forms[1].elements[i].type=="checkbox")
		{
			document.forms[1].elements[i].checked=checkFlag;
		}
	}//end of for(i=0;i<document.forms[1].length;i++)
}//end of function onCheckAll(checkFlag)