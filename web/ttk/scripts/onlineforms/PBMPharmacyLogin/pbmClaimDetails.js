

//function to display previous set of pages
function onExit()
{
   
    document.forms[1].mode.value ="doExit";
    document.forms[1].action = "/PbmClaimAction.do";
    document.forms[1].submit();
}//end of PressBackWard()