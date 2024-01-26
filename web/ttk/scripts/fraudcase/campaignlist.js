function onSearch()
{
 
	if(document.getElementById("campaignReceivedFrom").value != "")
	{
		if(isDate(document.getElementById("campaignReceivedFrom"),"Date")==false)
		{
			document.getElementById("campaignReceivedFrom").focus();
			return false;
		}
	}
	if(document.getElementById("campaignReceivedTo").value != "")
	{
		if(isDate(document.getElementById("campaignReceivedTo"),"Date")==false)
		{
			document.getElementById("campaignReceivedTo").focus();
			return false;
		}
	}
	
	var startDate =document.getElementById("campaignReceivedFrom").value;    	
	var endDate=document.getElementById("campaignReceivedTo").value;				
	
	if( !((document.getElementById("campaignReceivedFrom").value)==="") && !((document.getElementById("campaignReceivedTo").value)===""))
	{
		var sdate = startDate.split("/");
		var altsdate=sdate[1]+"/"+sdate[0]+"/"+sdate[2];
		altsdate=new Date(altsdate);
		
		var edate =endDate.split("/");
		var altedate=edate[1]+"/"+edate[0]+"/"+edate[2];
		altedate=new Date(altedate);
		
		var Startdate = new Date(altsdate);
		var EndDate =  new Date(altedate);
		
		if(EndDate < Startdate)
		 {
			alert("Campaign Received To date should be greater then or equal to Campaign Received From date.");
			document.getElementById("campaignReceivedTo").value="";
			document.getElementById("campaignReceivedTo").focus();
			return ;
		 }
	} 
	
	var campaignStatus = document.getElementById("campaignStatus").value;
	
	if(!JS_SecondSubmit)
 {
	document.forms[1].mode.value = "doSearch";
	document.forms[1].action = "/CFDCampaignSearchAction.do?"+"&campaignStatus="+campaignStatus;
	JS_SecondSubmit=true;
	document.forms[1].submit();
  }
}

function onAdd(){
		document.forms[1].reset();
		document.forms[1].rownum.value = "";
		document.forms[1].tab.value = "Campaign Deatils";
	 	document.forms[1].mode.value = "addCompagin";
	 	document.forms[1].action = "/CFDCampaignDetailAction.do";
	 	document.forms[1].submit();
	  
}


// 1: sorty by 
function toggle(sortid)
{
	document.forms[1].reset();
	document.forms[1].mode.value = "doSearch";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/CFDCampaignSearchAction.do";
    document.forms[1].submit();
}//end of toggle(sortid)

// 2: page nos
function pageIndex(pagenumber)
{
    document.forms[1].reset();
    document.forms[1].mode.value = "doSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/CFDCampaignSearchAction.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber)

// 3: function to display next set of pages
function PressForward()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/CFDCampaignSearchAction.do";
    document.forms[1].submit();
}//end of PressForward()

// 4: function to display previous set of pages
function PressBackWard()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/CFDCampaignSearchAction.do";
    document.forms[1].submit();
}//end of PressBackWard()

// clip board
function copyToWebBoard()
{
    if(!mChkboxValidation(document.forms[1]))
    {
	    document.forms[1].mode.value = "doCopyToWebBoard";
   		document.forms[1].action = "/CFDCampaignSearchAction.do";
	    document.forms[1].submit();
    }
}


function edit(rownum){
    document.forms[1].mode.value="doView";
	document.forms[1].rownum.value=rownum;
    document.forms[1].tab.value ="Campaign Deatils";
    document.forms[1].action = "/CFDCampaignSearchAction.do";
    document.forms[1].submit();
}//end of edit(rownum)

