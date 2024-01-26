function saveCollection()
{
	var ref = document.forms[1].transactionRef.value;
	if(ref !=null){
	if(ref.indexOf(' ') >= 0){
		alert("Plase remove the space");
		document.getElementById("transactionRef").focus();
		 return false;
	}
	}
	var totalOutstandingQAR = document.forms[1].totalOutstandingAmnt.value;
	if(Number(totalOutstandingQAR)==0){
		alert("Save not allowed because Total Outstanding amount is zero");
		 return false;
	}
	
	var invoiceDate = document.forms[1].dueDate.value;
	var receivedDate = document.forms[1].receivedDate.value;
	
	var date1 = invoiceDate.split("/");
	var altDate1=date1[1]+"/"+date1[0]+"/"+date1[2];
		altDate1=new Date(altDate1);
	
		var date2 =receivedDate.split("/");
 		var altDate2=date2[1]+"/"+date2[0]+"/"+date2[2];
 		altDate2=new Date(altDate2);
 		
 		
 		var invdate = new Date(altDate1);
 	    var recdate =  new Date(altDate2);
 		
 	   if(recdate < invdate){
 		   alert("Received date should be always greater than or equal to the invoice date.");
 		  document.getElementById("receivedDate").focus();
 		   return false;
 		   
 	   }
 	    
 	   
 	   
 	  var today = new Date();
 	  var dd = today.getDate();
 	  var mm = today.getMonth()+1; //January is 0!
 	  var yyyy = today.getFullYear();

 	  if(dd<10) {
 	      dd = '0'+dd
 	  } 

 	  if(mm<10) {
 	      mm = '0'+mm
 	  } 

 	    today = mm + '/' + dd + '/' + yyyy;
    	today=new Date(today);
 	   
 	   
    	if(recdate > today){
  		   alert("Received Date cannot be future date");
  		  document.getElementById("receivedDate").focus();
		   return false;
  		   
  	   }
 	   
 	   
 	   
 	   
 	    
 	 /* var file = document.getElementById("uploadFile");
 	  var FileSize = file.files.size / 1024 / 1024 / 1024 ; // in MB
      if (FileSize > 3) {
          alert('File size exceeds 3 MB');
          return false;
      }*/
 	    
 		
	
	
	
	document.forms[1].mode.value="saveCollection";
	document.forms[1].tab.value="Collections";
	document.forms[1].action="/CollectionsSaveAction.do";
	document.forms[1].submit();
	
}



function backCollection()
{
	document.forms[1].reforward.value="edit";
	document.forms[1].mode.value="doBackCollection";
	//document.forms[1].rownum.value=rownum;
	document.forms[1].tab.value="Premium Distribution";
	document.forms[1].action="/CollectionsSearchAction.do";
	document.forms[1].submit();
	
}

function showBankFile(filename)
{
	var openPage = "/ReportsAction.do?mode=doViewCommonForAll&module=collectionDocs&fileName="+filename;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 49;
	var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
}


function reverseTransaction(collectionSeqId){
	
    var msg = confirm("Are you sure to reverse the transaction for selected record ?");
	if(msg)
	{
		var overrRemarks=prompt("Enter Reverse Transaction Remarks","");
		
		
		var space = overrRemarks.charCodeAt(0); 
		 if(space==32)
          {
                 alert("Reverse Transaction Remarks should not start with space.");
                 document.getElementById("deletionRemarks").value="";
                 document.getElementById("deletionRemarks").focus();
                 return;
          } 
		
		if(overrRemarks==null||overrRemarks===""){
	
		 alert("Reverse Transaction Remarks Are Required");
		 return;
	 }else if(overrRemarks.length<20){
		 alert("Reverse Transaction Remarks Should Not Less Than 20 Characters");
		 return;
	 }
		 document.forms[1].deletionRemarks.value=overrRemarks;
		 
	} 
	
	 if(!JS_SecondSubmit){
	document.forms[1].collectionsSeqId.value=collectionSeqId;
	document.forms[1].mode.value = "doReverse";	
	document.forms[1].tab.value="Collections";
	document.forms[1].action="/CollectionsSearchAction.do";
	JS_SecondSubmit=true;
	document.forms[1].submit();
		}
	}

function caluculateAmountDue(obj)
{
	
	var outAmt = document.getElementById("totalOutstandingAmnt").value;
	var recamt = obj.value;
	
	 var outAmt1 = document.forms[1].totalOutstandingAmnt.value;
	 var recamt1 = document.forms[1].amountReceivedQAR.value;
	 outAmt1=(outAmt1==null||outAmt1===""||outAmt1==="0")?0:outAmt1;
	 recamt1=(recamt1==null||recamt1===""||recamt1==="0")?0:recamt1;
	
     if(Number(recamt1) > Number(outAmt1)){
		
		alert("Amount Received should be always less than the Total Outstanding.");
		document.forms[1].amountReceivedQAR.value	=	"";
		document.forms[1].amountDueQAR.value	=	"";
		document.getElementById("amountReceivedQAR").focus();
		 return false;
	}
     if(recamt==""){
    	 document.forms[1].amountDueQAR.value=""; 
     }
     else{
    	    var dueAmnt = Number(outAmt)-Number(recamt);
    		document.forms[1].amountDueQAR.value=dueAmnt.toFixed(2);
     }
	
}

function decimalValidation() {
	
	 var recamt1 = document.forms[1].amountReceivedQAR.value;
		if(recamt1 !=null){
			var r1 = Number(recamt1);
			if(r1 > 0){
				document.forms[1].amountReceivedQAR.value=r1.toFixed(2);
			}
			else{
				document.forms[1].amountReceivedQAR.value="";
			}
			
		}
	 
}

function isNumeric(field) {
    var re = /^[0-9]*\.*[0-9]*$/;
    if (!re.test(field.value)) {
        alert("Entered data must be Numeric!");
		field.focus();
		field.value="";
		return false;
    }
}





