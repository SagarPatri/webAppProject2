//java script for the tariff details screen in the Administration of hospital flow.

function onSearch()
{
	var switchTo = document.forms[1].switchTo.value;
	if(!JS_SecondSubmit)
	 {
	    trimForm(document.forms[1]);
	    document.forms[1].mode.value = "doSearchTariffItems";
	    if(switchTo == "PAC")
	    	document.forms[1].action = "/ShowSearchModifyPharmacyTariffItemsEmpanelment.do";	
	    else
	    	document.forms[1].action = "/ShowSearchModifyTariffItemsEmpanelment.do";
		JS_SecondSubmit=true;
	    document.forms[1].submit();
	   
	 }//end of if(!JS_SecondSubmit)
}//end of onSearch()

function UpdateDiscount()
{
	/*
	 * S T A R T
	 * THIS CODE COMMENTED, REMOVING THE MULTIPLE SELECTION OF PRICEREFNUMB AND TARIFS
	 * var commonClass	=	document.forms[1].elements["commonClass"];
	var ids			=	"";
	var resArray	=	new Array();
	for(var k=0;k<commonClass.length;k++)
	{
		if((commonClass[k].type	==	"text") && commonClass[k].value!="")
			ids	=	ids+"@"+commonClass[k].value+"#";
		
		else if(commonClass[k].type	==	"checkbox")
			if(commonClass[k].checked)
				ids	=	ids+"|"+commonClass[k].value+"|";
	}
	resArray.push(ids);
	document.forms[1].selectedIds.value	=	ids;
	E N D HERE
	*/
	
	/*if(document.forms[1].tariffTypeSearch.value=="")
	{
		alert("Please select Type of Tariff");
		document.forms[1].tariffTypeSearch.focus();
		return false;
	}*/
	
	/*
	 * select atleast one record  S T A R T
	 */
	var elements	=	document.forms[1].elements;
	var iFlag		=	"";
	var switchTo = document.forms[1].switchTo.value;
	for(var i=0;i<elements.length;i++)
	{
		if(elements[i].name == "chkopt")
		{
			if (elements[i].checked)
			{
				iFlag = 1;
				break;
			}//end of if (objform.elements[i].checked)
			else
			{
				iFlag = 0;
			}//end of else
		}//end of if(objform.elements[i].name == "chkopt")
	}//end of for(var i=0;i<elements.length;i++)
	if (iFlag == 0)
	{
		alert('Please select atleast one record');
		return false;
	}//end of if (iFlag == 0)
	/*
	 * select atleast one record  E N D S
	 */
	if(document.forms[1].discAmount.value=="")
	{
		alert("Please Enter Discount");
		document.forms[1].discAmount.focus();
		return false;
	}
	
	if(isPercentageCorrect(document.forms[1].discAmount,"Discount")==false)
	  	 return false;
	
	if(document.forms[1].discAmount.value>100 || document.forms[1].discAmount.value<0)
	{
		alert("Discount Percentage must be between 0 and 100%");
		document.forms[1].discAmount.focus();
		return false;
	}
	if(!JS_SecondSubmit)
	 {
	    trimForm(document.forms[1]);
	    document.forms[1].mode.value = "doModifyTariffItems";
	    if(switchTo == "PAC")
	    	document.forms[1].action = "/ModifyPharmacyTariffItemsEmpanelment.do";
	    else
	    	document.forms[1].action = "/ModifyTariffItemsEmpanelment.do";
		JS_SecondSubmit=true;
	    document.forms[1].submit();
	 }//end of if(!JS_SecondSubmit)
}

//function to check only positive numbers
function isNumberKey(evt)
{
   var charCode = (evt.which) ? evt.which : evt.keyCode;
   alert(charCode);
   if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57))
      return false;
   return true;
}

function validateDiscount()
{
    /*var pattern = /^-?[0-9]+(.[0-9]{1,2})?$/;
    var text = document.forms[1].discAmount.value;
    if (text.match(pattern)==null)
    {
		alert('Please Enter only Numbers');
		document.forms[1].discAmount.value	=	"";
    }*/
	
}
function onBack()
{
	document.forms[1].mode.value="doDefaultEmpnlTariff";
    document.forms[1].action = "/TariffActionEmpanelment.do";
    document.forms[1].submit();
}

function onBackPharmacy()
{
	document.forms[1].mode.value="doDefaultEmpnlPharmacyTariff";
    document.forms[1].action = "/TariffActionEmpanelment.do";
    document.forms[1].submit();
}

//function to sort based on the link selected
function toggle(sortid)
{
	var switchTo = document.forms[1].switchTo.value;
	
	document.forms[1].reset();
    document.forms[1].mode.value="doSearchTariffItems";
    document.forms[1].sortId.value=sortid;
    if(switchTo == "PAC")
    	document.forms[1].action = "/ShowPharmacyTariffItems.do";
    else
    	document.forms[1].action = "/ShowTariffItems.do";
    document.forms[1].submit();
}//end of toggle(sortid)
//function to display the selected page
function pageIndex(pagenumber)
{
	var switchTo = document.forms[1].switchTo.value;
	
	document.forms[1].reset();
    document.forms[1].mode.value="doSearchTariffItems";
    document.forms[1].pageId.value=pagenumber;
    if(switchTo == "PAC")
    	document.forms[1].action = "/ShowPharmacyTariffItems.do";
    else
    	document.forms[1].action = "/ShowTariffItems.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber)
//function to display previous set of pages
function PressBackWard()
{
	var switchTo = document.forms[1].switchTo.value;
	
	document.forms[1].reset();
    document.forms[1].mode.value ="doBackward";
    if(switchTo == "PAC")
    	document.forms[1].action = "/ShowPharmacyTariffItems.do";
    else
    	document.forms[1].action = "/ShowTariffItems.do";
    document.forms[1].submit();
}//end of PressBackWard()
//function to display next set of pages
function PressForward()
{
	var switchTo = document.forms[1].switchTo.value;
	
	document.forms[1].reset();
    document.forms[1].mode.value ="doForward";
    if(switchTo == "PAC")
    	document.forms[1].action = "/ShowPharmacyTariffItems.do";
    else
    	document.forms[1].action = "/ShowTariffItems.do";
    document.forms[1].submit();
}//end of PressForward()
//function to display pages based on search criteria