//java script for the tariff details screen in the empanelment of hospital flow.
function onBack()
{
	var switchTo = document.forms[1].switchTo.value;
	if(switchTo == "PHARMACY/CONSUMABLE TARIFF"){
	document.forms[1].mode.value="doDefault";
	document.forms[1].action = "/PharmacyTarrifSearchAction.do";
	}else{
	document.forms[1].mode.value="doDefaultEmpnlTariff";
    document.forms[1].action = "/TariffActionEmpanelment.do";
	}
    document.forms[1].submit();
}

function onSaveTariffItem()
{
	/*if(isPositiveInteger(document.forms[1].discAmount,"Discount")==false)
	  	 return false;*/
	var switchTo = document.forms[1].switchTo.value;
	if(document.forms[1].grossAmount.value=='')
	{
		alert("Please Enter Gross Amount");
		return false;
	}
	if(document.forms[1].discPercentage.value=='' || document.forms[1].discAmount.value=='')
	{
		alert("Either Discount Percent or Amount Should Enter");
		return false;
	}
	
	if(!JS_SecondSubmit)
    {
		document.forms[1].mode.value="doSaveTariffItem";
		if(switchTo == "PAC")
			 document.forms[1].action = "/SavePharmacyTarrifActionEmpanelment.do";
		else
			document.forms[1].action = "/SaveTarrifActionEmpanelment.do";
		JS_SecondSubmit=true;
	    document.forms[1].submit();
    }
}

function calcDiscount(obj)
{
	var val	=	obj.value;
	if(document.forms[1].grossAmount.value=='')
	{
		alert("Please Enter Gross Amount");
		return false;
	}
	var discAmount	=	(val/100)*document.forms[1].grossAmount.value;
	document.forms[1].discAmount.value	=	discAmount;
}

function calcDiscountOnAmt(obj)
{
	var val	=	obj.value;
	if(document.forms[1].grossAmount.value=='')
	{
		alert("Please Enter Gross Amount");
		return false;
	}
	document.forms[1].discPercentage.value	=	(val*100)/document.forms[1].grossAmount.value;
}

function selectActivityCode() {
    document.forms[1].mode.value = "doGeneral";
    document.forms[1].action ="/TarrifItemAction.do";
    document.forms[1].submit();
}

function activityCodeSearch(){
	document.forms[1].mode.value = "activityCodeSearch";
	document.forms[1].action = "/TarrifItemActionForActivity.do";
	document.forms[1].submit();
}//end of activityCodeSearch()

function pageIndex(pagenumber)
{
    document.forms[1].reset();
    document.forms[1].mode.value="activityCodeSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/TarrifItemActionForActivity.do";
    document.forms[1].submit();
}

function closeActivityCodeList()
{
    document.forms[1].mode.value="closeActivityCode";
    document.forms[1].action = "/TarrifItemActionForActivity.do";
    document.forms[1].submit();
}//end of closeActivityCode()

function PressForward()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doActivityCodeForward";
    document.forms[1].action = "/TarrifItemActionForActivity.do";
    document.forms[1].submit();
}//end of PressForward()

function PressBackWard()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doActivityCodeBackward";
    document.forms[1].action = "/TarrifItemActionForActivity.do";
    document.forms[1].submit();
}//end of PressBackWard()


function edit(rownum){	
    document.forms[1].mode.value="doSelectActivityCode";
    document.forms[1].rownum.value=rownum;
    document.forms[1].action = "/TarrifItemActionForActivity.do";
    document.forms[1].submit();
}//end of edit(rownum)
