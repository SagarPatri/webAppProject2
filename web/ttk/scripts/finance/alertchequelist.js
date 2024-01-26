//java script for the cheques list screen in the finance module of cheque information flow.

//function to call edit screen
function edit(rownum)
{
    document.forms[1].mode.value="doViewChequeForAlert";
    document.forms[1].rownum.value=rownum;
    document.forms[1].tab.value="Alert";
    document.forms[1].action = "/ChequeSearchAction.do";
    document.forms[1].submit();
}//end of edit(rownum)


function onSearch(element)
{
   if(!JS_SecondSubmit)
 {
        trimForm(document.forms[1]);
       var claimno = document.getElementById("sclaimnoid").value;
       var claimsettlenumber = document.getElementById("sclaimsettlenumber").value;
       
       if(claimno !="" || claimsettlenumber !=""){
    	   
    	   document.forms[1].mode.value = "doSearchForAlert";
   	    document.forms[1].action = "/ChequeSearchAction.do";
   	    JS_SecondSubmit=true;
   	    element.innerHTML	=	"<b>"+"Please Wait...!!"+"</b>";
   	    document.forms[1].submit();
    	 
       }else{
       
    	   alert("Enter Claim No. or Claim Settlement No.");
     	    return;
       }
  }//end of if(!JS_SecondSubmit)
}//end of onSearch()
