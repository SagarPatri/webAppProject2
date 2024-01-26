/*
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */
      var sdPageForwardNum="1";
	  var sdPageLinkNum="1";
	  var sdXhttp;
	  var varMainDivID;
  function onSearchData(searchMode,searchMethod,fieldID,dispID,urlAction,selectMethod) {
	  //clear last search data
	  if(varMainDivID!=null&&varMainDivID!=""){
		  var lastSpcDiv=document.getElementById(varMainDivID);    
		  lastSpcDiv.innerHTML="";
		  lastSpcDiv.style.display="none";
	  }
var spcDiv;

varMainDivID=dispID;
     spcDiv=document.getElementById(dispID);    
     spcDiv.innerHTML="";
	 spcDiv.style.display="none";
	  
     if(searchMode==undefined){
      searchMode="new";        
   	  sdPageLinkNum="1";
   	  sdPageForwardNum="1";
   	  spcDiv.innerHTML="";
	  spcDiv.style.display="none";
     }else if(searchMode==="new"){
          searchMode="new";        
    	  sdPageLinkNum="1";    	 
     } else{
          searchMode="old";         
     }
	    
	   var fieldValue= document.getElementById(fieldID).value;
	//   var dateOfTreatment= document.forms[1].dateOfTreatment.value;
		if(fieldValue!==null&&fieldValue!==""){				
		var path;
		if(document.forms[1].dateOfTreatment !=null ){
			var dateOfTreatment= document.forms[1].dateOfTreatment.value;
			path=urlAction+"&paramName="+fieldID+"&sdPageLinkNum="+sdPageLinkNum+"&"+fieldID+"="+fieldValue+"&sdPageForwardNum="+sdPageForwardNum+"&searchMode="+searchMode+"&dateOfTreatment="+dateOfTreatment+"&selectMethod="+selectMethod; 
		}
		else if(document.forms[1].treatmentDate !=null ){
			var treatmentDate= document.forms[1].treatmentDate.value;
			path=urlAction+"&paramName="+fieldID+"&sdPageLinkNum="+sdPageLinkNum+"&"+fieldID+"="+fieldValue+"&sdPageForwardNum="+sdPageForwardNum+"&searchMode="+searchMode+"&treatmentDate="+treatmentDate+"&selectMethod="+selectMethod; 
		}
		else{
			path=urlAction+"&paramName="+fieldID+"&sdPageLinkNum="+sdPageLinkNum+"&"+fieldID+"="+fieldValue+"&sdPageForwardNum="+sdPageForwardNum+"&searchMode="+searchMode;
		}
	  var varSearchData=getSearchData(path);

      
	  if(varSearchData!==null&&varSearchData!==""&&varSearchData.length>1){

		  spcDiv.style.display="";

		  var arrSearchData,varSplitData,noOfrows=0;
		  
       
        	arrSearchData =varSearchData.split("@");
        	 noOfrows=arrSearchData[0];
        	 varSplitData=arrSearchData[1];
       
		  
		  if(varSplitData.length>0) {

			  
	      var arrData=varSplitData.split("|");
	      
	      if(arrData.length>0){
		      
	          var sdmDiv,sdsDiv;

	        	  sdmDiv = document.createElement('div');
		          sdmDiv.id = "sdmTextDiv";
		          sdmDiv.className = "sdmTextDiv";
		          
		          if(noOfrows<5){
		          sdmDiv.style.height = "100px";
		          }
	          
	          for(var i=0;i<arrData.length-1;i++){
	        	  
	          sdsDiv = document.createElement('div');
	          sdsDiv.className = 'sdTextDiv';
			  var idData=arrData[i].split("#");
			  var idDataToSplit = idData[1].split("--");
			    
	           sdsDiv.innerHTML = idDataToSplit[0];
	           var funNameWithParametres;
	           
	           if(selectMethod == "onSelectDrugDesc")
	           {
	        	   funNameWithParametres="onSelectData('"+dispID+"','"+fieldID+"','"+selectMethod+"','"+idData[0]+"','"+idData[1]+"','"+idData[2]+"','"+idData[3]+"');";    
	           }
	           else
	          {		
	        	    funNameWithParametres="onSelectData('"+dispID+"','"+fieldID+"','"+selectMethod+"','"+idData[0]+"','"+idData[1]+"');";  		
	          }
	           sdsDiv.setAttribute("onclick",funNameWithParametres);      
	                    
	           sdmDiv.appendChild(sdsDiv);
	           
	          }//for(var i=0;i<arrData.length-1;i++){
	        
	          spcDiv.appendChild(sdmDiv);
	          
	        //calling page link function
	          createPageLiks(searchMethod,noOfrows,dispID);
	          
	        }// if(arrData.length>0){
	       }//if(arrSearchData.length>0) {
	      else{
	    	  document.getElementById(dispID).style.display="none";
	        }
	       }//if(sData!==null&&sData!==""&&sData.length>1){
	     else {
		  document.getElementById(dispID).style.display="none";
	      }
	  }//if(sObj.value!==null&&sObj.value!==""){ 
	  }//function onInsSearch(sObj) {
  
  function getSearchData(path){
	  if(sdXhttp==null){
	  if (window.XMLHttpRequest) {
		    sdXhttp = new XMLHttpRequest();
		    } else {
		    // code for IE6, IE5
		    sdXhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
	  }
	     
	  sdXhttp.open("POST", path, false);
	  sdXhttp.send();
	  var varSearchData=sdXhttp.responseText;
	  return varSearchData;
	  }
  

  
	  
  function createPageLiks(searchMethod,noOfrows,dispID) {

	  var spcDiv;
	  		spcDiv=document.getElementById(dispID);
      
	//creating page links
	             var sdlmDiv,sdlsDiv,forwardDiv,backwardDiv;
	             sdlmDiv = document.createElement('div');
	             sdlmDiv.className = 'sdPageLinks';           
	        	var tempNoOfrows=noOfrows;


	        	if(sdPageForwardNum!=null&&sdPageForwardNum!=""&&parseInt(sdPageForwardNum)>1){
	        		 
	        		  backwardDiv = document.createElement('div');
	        		  backwardDiv.className = 'sdPageBackward';
	        		  backwardDiv.setAttribute("onclick","sdPageBackward('"+searchMethod+"');");
	        		  backwardDiv.innerText="<";
	        		  sdlmDiv.appendChild(backwardDiv);
		        	  }
	        	
	        	var linkLabel;
	        	if(sdPageForwardNum==null||sdPageForwardNum==""||sdPageForwardNum==="1")linkLabel=0;
	        	else linkLabel=(parseInt(sdPageForwardNum)*parseInt(10)-10);
	        	
	        	if(noOfrows>100)noOfrows=noOfrows-1;
	        	
	        	  for(var i=1;0<noOfrows;i++){
	            	  
	        		sdlsDiv = document.createElement('div');
	        		
	        		sdlsDiv.innerText=(parseInt(i)+parseInt(linkLabel));
	        		sdlsDiv.id = 'sdPageLink';
	        		if(sdPageLinkNum==i){
	        			sdlsDiv.className='sdCurrentPageLink';
		        		}else{
	        		sdlsDiv.setAttribute("onclick","sdPageLink('"+i+"','"+searchMethod+"');");
	        		sdlsDiv.className = 'sdPageLink';
		        	}
	        		
	        		sdlmDiv.appendChild(sdlsDiv);
	        		
	        		noOfrows=noOfrows-10;
	        	  }
                if(tempNoOfrows>100){
	        		  
	        		  forwardDiv = document.createElement('div');
	        		  forwardDiv.className = 'sdPageForward';
	        		  forwardDiv.setAttribute("onclick","sdPageForward('"+searchMethod+"');");
	        		  forwardDiv.innerText=">";
	        		  sdlmDiv.appendChild(forwardDiv);
		        	}
	        	  
	        	  var sdCloseDiv = document.createElement('span');
	        	  sdCloseDiv.className = 'sdCloseBtn';
	        	  sdCloseDiv.setAttribute("onclick","sdCloseSearch('"+dispID+"');");
	        	  sdCloseDiv.innerText="Close";
	        	  sdlmDiv.appendChild(sdCloseDiv);
	        	  
	        	  spcDiv.appendChild(sdlmDiv);
	        	  
  }
  
  
  function sdPageLink(argPageLink,argSearchMethod) {
	  sdPageLinkNum=argPageLink; 	
	  if(argSearchMethod!=null&&argSearchMethod!=""&&argSearchMethod.length>1){
	  window[argSearchMethod]('old');
	  }

  }
  
  
	 
  function sdPageForward(argSearchMethod) {

	     if(sdPageForwardNum==null||sdPageForwardNum==""||sdPageForwardNum=="1")sdPageForwardNum="1";
	  sdPageForwardNum=parseInt(sdPageForwardNum)+parseInt("1");		
	  if(argSearchMethod!=null&&argSearchMethod!=""&&argSearchMethod.length>1){
	  window[argSearchMethod]('new');
	  }

  }
  
  function sdPageBackward(argSearchMethod) {

	     if(sdPageForwardNum==null||sdPageForwardNum==""||sdPageForwardNum=="1")sdPageForwardNum="1";
	  sdPageForwardNum=parseInt(sdPageForwardNum)-parseInt("1");		
	  if(argSearchMethod!=null&&argSearchMethod!=""&&argSearchMethod.length>1){
	  window[argSearchMethod]('new');
	  }

}
  function onSelectData(sdDivID,searchFieldID,funName,param1,param2,param3,param4) {
	  
	  if(document.getElementById("loginTypeProviderid")!=null && document.getElementById("loginTypeProviderid").value =="providerLogin"){
		  
	 
	  var regex = /^[a-zA-Z\d\(),-\s]+$/;
	 
	  var test1 = regex.test(param2);
	  
	  if(test1 && searchFieldID !="clinicianName"){
	  var idDataToSplit = param2.split("--");
	  if(searchFieldID=="onSelectIcdDesc" || searchFieldID=="icdDesc"){
		 
		  document.getElementById('diagnosiscountid').value=idDataToSplit[1];
		  document.getElementById(searchFieldID).value=idDataToSplit[0];
	  }else if(searchFieldID=="activityCodeDesc"){
		  document.getElementById('activityicdcount_id').value=idDataToSplit[1];
		  document.getElementById(searchFieldID).value=idDataToSplit[0]; 
	  }else if(searchFieldID=="clinicianId"){
		  document.getElementById(searchFieldID).value=idDataToSplit[0];
	  }else if(searchFieldID=="icdCode"){
		 
		  document.getElementById(searchFieldID).value=idDataToSplit[0];
	  }
      
	  }else{
		  var regex = /^[a-zA-Z\d\,.-\s]+$/;
		  var test1 = regex.test(param2);
		  
		  if(test1 && searchFieldID !="clinicianName"){
			  var idDataToSplit = param2.split("--");
		      document.getElementById('diagnosiscountid').value=idDataToSplit[1];
			  document.getElementById(searchFieldID).value=idDataToSplit[0]; 
		  }else{
			  var regex = /^[a-zA-Z\d\(),-\s]+$/;
			  var test1 = regex.test(param2); 
			 
			  if(test1 && searchFieldID !="clinicianName"){
				  var idDataToSplit = param2.split("--");
			      document.getElementById('diagnosiscountid').value=idDataToSplit[1];
				  document.getElementById(searchFieldID).value=idDataToSplit[0]; 
			  }else{
				  
		  document.getElementById(searchFieldID).value=param2;
			  }
		  }
	  }
    }else{
    	document.getElementById(searchFieldID).value=param2;
    }
	  document.getElementById(sdDivID).innerHTML=""; 
	  document.getElementById(sdDivID).style.display="none";
	  
	  if(funName!=null&&funName!=""&&funName.length>1){	
		 if(funName == "onSelectDrugDesc")
		 {
			 window[funName](param1,param2,param3,param4);
		 }
		 else 
		 { 
			 window[funName](param1);
		 }
	  }
  }
 
  function sdCloseSearch(sdDivID) {
	  document.getElementById(sdDivID).innerHTML=""; 
	  document.getElementById(sdDivID).style.display="none";	 
  }
  
  
  
  function clearSearchData(event)
  {
	  var char = event.which || event.keyCode;
	  if(char==27){
	  //clear last search data
	  if(varMainDivID!=null&&varMainDivID!=""){
		  var lastSpcDiv=document.getElementById(varMainDivID);    
		  lastSpcDiv.innerHTML="";
		  lastSpcDiv.style.display="none";
	  }
	  }
  }
  
  function sdConvertToUpperCase(charObj)
  {
  	charObj.value=charObj.value.toUpperCase();
  }
  