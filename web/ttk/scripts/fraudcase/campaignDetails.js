
function onSave()
{
	var campaignName = document.getElementById("campaignName").value;
	var cfdProviderName = document.getElementById("cfdProviderName").value;
	var campaginStartDate = document.getElementById("campaginStartDate").value;
	
	if(campaignName == null || campaignName == "")
	{		
			alert("Please enter Campaign Name.");
			document.getElementById("campaignName").focus();
			return false;
	}
	if(cfdProviderName == null || cfdProviderName == "")
	{		
			alert("Please select any Provider Name.");
			document.getElementById("cfdProviderName").focus();
			return false;
	}
	if(campaginStartDate == null || campaginStartDate == "")
	{		
			alert("Please enter Campaign Start Date.");
			document.getElementById("campaginStartDate").focus();
			return false;
	}
	
	if(document.getElementById("campaginStartDate").value != "")
	{
		if(isDate(document.getElementById("campaginStartDate"),"Date")==false)
		{
			document.getElementById("campaginStartDate").focus();
			return false;
		}
	}
	
	var displayCampStatusFlag = document.getElementById("displayCampStatusFlag").value;
	// alert("displayCampStatusFlag = "+displayCampStatusFlag);
	if(displayCampStatusFlag == 'Y')
	{
		var campaginStatus = document.getElementById("campaginStatus").value;
		if(displayCampStatusFlag =="Y" && campaginStatus == "CLOSED")
		{	// alert("inside edit link");
			
			var campaginEndDate = document.getElementById("campaginEndDate").value; 
			if(campaginEndDate == null || campaginEndDate == "")
			{		
					alert("Please enter Campaign End Date.");
					document.getElementById("campaginEndDate").focus();
					return false;
			}
		
			if(document.getElementById("campaginEndDate").value != "")
			{
				if(isDate(document.getElementById("campaginEndDate"),"Date")==false)
				{
					document.getElementById("campaginEndDate").focus();
					return false;
				}
			}
		
			var startDate =document.getElementById("campaginStartDate").value;    	
			var endDate=document.getElementById("campaginEndDate").value;				
			
			if( !((document.getElementById("campaginStartDate").value)==="") && !((document.getElementById("campaginEndDate").value)===""))
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
					alert("Campaign End Date should be greater than or equal to Campaign Start Date.");
					document.getElementById("campaginEndDate").value="";
					document.getElementById("campaginEndDate").focus();
					return ;
				 }
			}
				var campaignRemarks = document.getElementById("campaignRemarks").value;
				if(campaignRemarks == null || campaignRemarks == "")
				{		
						alert("Please select Campaign Remarks.");
						document.getElementById("campaignRemarks").focus();
						return false;
				}
			
			} // end of if(displayCampStatusFlag =="Y" && campaginStatus == "CLOSE")
	}// end of if(displayCampStatusFlag == 'Y')
		
	document.forms[1].mode.value = "doSave";
	document.forms[1].action = "/CFDCampaignDetailAction.do";
 	document.forms[1].submit();
}

function onClose()
{
	document.forms[1].mode.value = "doClose";
	document.forms[1].tab.value = "Search";
 	document.forms[1].action = "/CFDCampaignDetailAction.do";
 	document.forms[1].submit();
}

function onChangeCampaignStatus()
{
	document.forms[1].mode.value = "doChangeCampaignStatus";
	document.forms[1].action = "/CFDCampaignDetailAction.do";
 	document.forms[1].submit();
}
