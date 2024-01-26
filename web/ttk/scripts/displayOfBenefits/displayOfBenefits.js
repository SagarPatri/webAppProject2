window.onload = function() {

resizeDocument();
$(".selectConfig").change(function(){
    if($(this).val()== "3")
    	$(this).closest('tr').find('.textBox, .selectBox').removeAttr("disabled");
    else
    {
    	$(this).closest('tr').find('.textBox, .selectBox').each(function(){
    	if(!($(this).hasClass("selectConfig"))){
    		$(this).removeAttr("value");
    		$(this).attr("disabled","disabled");
    	}});
    }
  });
};




function doSave()
{
	document.forms[1].mode.value="doSavePolicyBenefitDetails";
	document.forms[1].action="/SavePolicyBenefitDetailsAction.do";
	document.forms[1].submit();
}//end of doSave()


function onClose(){
	document.forms[1].mode.value="doClosePolicyBenefitDetails";
	document.forms[1].action="/SavePolicyBenefitDetailsAction.do";
	document.forms[1].submit();
}//end of onClose()