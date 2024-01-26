/** @ (#) dentalValidation.js 20 Nov 2015 
 * Project     : TTK Healthcare Services
 * File        : dentalValidation.js
 * Author      : Kishor kumar S H
 * Company     : VHC
 * Date Created: 11 09 2017
 *
 * @author 		 : Kishor kumar S H
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */

function checkDentoCombo(obj,type){
	  var overjet		=	document.getElementById("overJet").value;
	  var reverseJet	=	document.getElementById("reverseJet").value;
	  
	  if(overjet!='' && reverseJet!=''){//OVERJET WIRH REVERRSE OVERJET 
		  alert(type+" cannot be combined");
		  obj.value='';
		  return false;
	  }
	  if(reverseJet!='' && (document.getElementById("overBiteD").checked || document.getElementById("overBiteC").checked
			  	|| document.getElementById("overBiteI").checked))
	  {//REVERRSE OVERJET WITH OVER BITE
		  alert(type+" cannot be combined");
		  if(obj.type=='radio')
			  obj.checked	=	false;
		  else
			  obj.value='';
		  return false;
	  }
	  
	  if(reverseJet!='' && (document.getElementById("openbiteAntrio").value!='' || document.getElementById("openbitePosterior").value!=''
			  	|| document.getElementById("openbiteLateral").value!=''))
	  {//REVERRSE OVERJET WITH OPEN BITE
		  alert(type+" cannot be combined");
		  obj.value='';
		  return false;
	  }
	  
	  /*if((document.getElementById("overBiteD").checked || document.getElementById("overBiteC").checked
			  	|| document.getElementById("overBiteI").checked) &&
	  	(document.getElementById("openbiteAntrio").value!='' || document.getElementById("openbitePosterior").value!=''
			  	|| document.getElementById("openbiteLateral").value!=''))
	  {
		  alert(type+" cannot be combined");
		  if(obj.type=='radio')
			  obj.checked	=	false;
		  else
			  obj.value='';
		  return false;
	  }*/
}
function checkToothNo(obj,type){
	var result	=	"";
	  //Hypodentia with all check
	  var  hypodontiaAllteeth	=	document.getElementById("hypodontiaQuand1Teeth").value+"|"
									+document.getElementById("hypodontiaQuand2Teeth").value+"|"
									+document.getElementById("hypodontiaQuand3Teeth").value+"|"
									+document.getElementById("hypodontiaQuand4Teeth").value;

	  var submergerdTeethNo		 = document.getElementById("submergerdTeethNo").value;
	  var impededTeethEruptionNo = document.getElementById("impededTeethEruptionNo").value;
	  var impededTeethNo		 = document.getElementById("impededTeethNo").value;
	  var contactPointDisplacement= document.getElementById("contactPointDisplacement").value;
	  var  crossBiteAllteeth	=	document.getElementById("crossbiteAntrio").value+"|"
									+document.getElementById("crossbitePosterior").value+"|"
									+document.getElementById("crossbiteRetrucontract").value;
	  //Combination all Hypodentia cannot be same
	  var  hypodontia1Allteeth	=	document.getElementById("hypodontiaQuand2Teeth").value+"|"
									+document.getElementById("hypodontiaQuand3Teeth").value+"|"
									+document.getElementById("hypodontiaQuand4Teeth").value;
	  if(document.getElementById("hypodontiaQuand1Teeth").value!='' && document.getElementById("hypodontiaQuand1Teeth").value!=null){
		  
		  result	=	checkSameToothNos(document.getElementById("hypodontiaQuand1Teeth").value,hypodontia1Allteeth);
		  if(result==true)
		  {
			  alert(type+" should not be same tooth number as other Hypodontia");
			  obj.value='';
			  return false;
		  }
		  //Check hypodentia with cross Bites
		  result	=	checkSameToothNos(document.getElementById("hypodontiaQuand1Teeth").value,crossBiteAllteeth);
		  if(result==true)
		  {
			  alert("Hypodontia Quandrant 1 should not be same tooth number as Cross Bytes");
			  obj.value='';
			  return false;
		  }
	  }
	  hypodontia1Allteeth	=	document.getElementById("hypodontiaQuand1Teeth").value+"|"
								+document.getElementById("hypodontiaQuand3Teeth").value+"|"
								+document.getElementById("hypodontiaQuand4Teeth").value;
		if(document.getElementById("hypodontiaQuand2Teeth").value!='' && document.getElementById("hypodontiaQuand2Teeth").value!=null){
			  result	=	checkSameToothNos(document.getElementById("hypodontiaQuand2Teeth").value,hypodontia1Allteeth);
			  if(result==true)
			{
				alert(type+" should not be same tooth number as other Hypodontia");
				obj.value='';
				return false;
			}
			  result	=	checkSameToothNos(document.getElementById("hypodontiaQuand2Teeth").value,crossBiteAllteeth);
			  if(result==true)
			  {
				  alert("Hypodontia Quandrant 2 should not be same tooth number as Cross Bytes");
				  obj.value='';
				  return false;
			  }
		}
		hypodontia1Allteeth	=	document.getElementById("hypodontiaQuand1Teeth").value+"|"
								+document.getElementById("hypodontiaQuand2Teeth").value+"|"
								+document.getElementById("hypodontiaQuand4Teeth").value;
		if(document.getElementById("hypodontiaQuand3Teeth").value!='' && document.getElementById("hypodontiaQuand3Teeth").value!=null){
			result	=	checkSameToothNos(document.getElementById("hypodontiaQuand3Teeth").value,hypodontia1Allteeth);
			if(result==true)
			{
				alert(type+" should not be same tooth number as other Hypodontia");
				obj.value='';
				return false;
			}
			result	=	checkSameToothNos(document.getElementById("hypodontiaQuand3Teeth").value,crossBiteAllteeth);
			if(result==true)
			  {
				  alert("Hypodontia Quandrant 3 should not be same tooth number as Cross Bytes");
				  obj.value='';
				  return false;
			  }
		}
		hypodontia1Allteeth	=	document.getElementById("hypodontiaQuand1Teeth").value+"|"
								+document.getElementById("hypodontiaQuand2Teeth").value+"|"
								+document.getElementById("hypodontiaQuand3Teeth").value;
		if(document.getElementById("hypodontiaQuand4Teeth").value!='' && document.getElementById("hypodontiaQuand4Teeth").value!=null){
			result	=	checkSameToothNos(document.getElementById("hypodontiaQuand4Teeth").value,hypodontia1Allteeth);
			if(result==true)
			{
				alert(type+" should not be same tooth number as other Hypodontia");
				obj.value='';
				return false;
			}
			result	=	checkSameToothNos(document.getElementById("hypodontiaQuand4Teeth").value,crossBiteAllteeth);
			if(result==true)
			  {
				  alert("Hypodontia Quandrant 4 should not be same tooth number as Cross Bytes");
				  obj.value='';
				  return false;
			  }
		}
		
if(hypodontiaAllteeth!="" || hypodontiaAllteeth!=null){
	
	if(submergerdTeethNo!='' && submergerdTeethNo!=null){
		
		 result	=	checkSameToothNos(submergerdTeethNo,hypodontiaAllteeth);
		  if(result==true)
		  {
			  alert(type+" should not be same tooth number as Submerged");
			  obj.value='';
			  return false;
		  }
	  }
	
	  if(impededTeethEruptionNo!='' && impededTeethEruptionNo!=null){
		  result	=	checkSameToothNos(impededTeethEruptionNo,hypodontiaAllteeth);
		  if(result==true)
		  {
			  alert(type+" should not be same tooth number");
			  obj.value='';
			  return false;
		  }
	  }
	  if(impededTeethNo!='' && impededTeethNo!=null){
		  result	=	checkSameToothNos(impededTeethNo,hypodontiaAllteeth);
		  if(result==true)
		  {
			  alert(type+" should not be same tooth number");
			  obj.value='';
			  return false;
		  }
	  }
	  if(contactPointDisplacement!='' && contactPointDisplacement!=null){
		  result	=	checkSameToothNos(contactPointDisplacement,hypodontiaAllteeth);
		  if(result==true)
		  {
			  alert(type+" should not be same tooth number");
			  obj.value='';
			  return false;
		  }
	  }
	  
	}

}

function checkImpededTooth(obj,type){
	var result	=	"";
	  //Hypodentia with all check
	  var  hypodontiaAllteeth	=	document.getElementById("hypodontiaQuand1Teeth").value+"|"
									+document.getElementById("hypodontiaQuand2Teeth").value+"|"
									+document.getElementById("hypodontiaQuand3Teeth").value+"|"
									+document.getElementById("hypodontiaQuand4Teeth").value;
	  var  crossBiteAllteeth	=	document.getElementById("crossbiteAntrio").value+"|"
									+document.getElementById("crossbitePosterior").value+"|"
									+document.getElementById("crossbiteRetrucontract").value;

	  var impededTeethEruptionNo = document.getElementById("impededTeethEruptionNo").value;
	  var impededTeethNo		 = document.getElementById("impededTeethNo").value;
	  var submergerdTeethNo		 = document.getElementById("submergerdTeethNo").value;
	  var ectopicTeethNo		 = document.getElementById("ectopicTeethNo").value;
	  var contactPointDisplacement= document.getElementById("contactPointDisplacement").value;
	  var supernumeryTeethNo		= document.getElementById("supernumeryTeethNo").value;
	  if("Submerged"==type){
		  
		  
		  if(submergerdTeethNo!='' && submergerdTeethNo!=null){
			  result	=	checkSameToothNos(impededTeethNo,submergerdTeethNo);
			  if(result==true)
			  {
				  alert(type+" should not be same tooth number as Impacted");
				  obj.value='';
				  return false;
			  }
		  }
		  submergerdTeethNo		 = document.getElementById("submergerdTeethNo").value;
		  if(submergerdTeethNo!='' && submergerdTeethNo!=null){
			  result	=	checkSameToothNos(impededTeethEruptionNo,submergerdTeethNo);
			  if(result==true)
			  {
				  alert(type+" should not be same tooth number as Impeded");
				  obj.value='';
				  return false;
			  }
		  }
		  submergerdTeethNo		 = document.getElementById("submergerdTeethNo").value;
		  if(submergerdTeethNo!='' && submergerdTeethNo!=null){
			  result	=	checkSameToothNos(ectopicTeethNo,submergerdTeethNo);
			  if(result==true)
			  {
				  alert(type+" should not be same tooth number as Ectopic");
				  obj.value='';
				  return false;
			  }
		  }
		  submergerdTeethNo = document.getElementById("submergerdTeethNo").value;
		  if(submergerdTeethNo!='' && submergerdTeethNo!=null){
			  result	=	checkSameToothNos(contactPointDisplacement,submergerdTeethNo);
			  if(result==true)
			  {
				  alert(type+" should not be same tooth number as Displacement");
				  obj.value='';
				  return false;
			  }
		  }
		  
		  submergerdTeethNo		 = document.getElementById("submergerdTeethNo").value;
		  if(submergerdTeethNo!='' && submergerdTeethNo!=null){
			  result	=	checkSameToothNos(crossBiteAllteeth,submergerdTeethNo);
			  if(result==true)
			  {
				  alert(type+" should not be same tooth number as Cross bite");
				  obj.value='';
				  return false;
			  }
		  }
		  
		  submergerdTeethNo		 = document.getElementById("submergerdTeethNo").value;
		  if(submergerdTeethNo!='' && submergerdTeethNo!=null){
			  result	=	checkSameToothNos(submergerdTeethNo,hypodontiaAllteeth);
			  if(result==true)
			  {
				  alert(type+" should not be same tooth number as Hypodontia");
				  obj.value='';
				  return false;
			  }
		  }
	  }//Submerged
if("Impeded"==type){
	
	if(impededTeethEruptionNo!='' && impededTeethEruptionNo!=null){
		result	=	checkSameToothNos(impededTeethNo,impededTeethEruptionNo);
		if(result==true)
		  {
			  alert(type+" should not be same tooth number as Impacted");
			  obj.value='';
			  return false;
		  }
	  }
	impededTeethEruptionNo = document.getElementById("impededTeethEruptionNo").value;
	  if(impededTeethEruptionNo!='' && impededTeethEruptionNo!=null){
		  result	=	checkSameToothNos(impededTeethEruptionNo,hypodontiaAllteeth);
		  if(result==true)
		  {
			  alert(type+" should not be same tooth number as Hypodontia");
			  obj.value='';
			  return false;
		  }
	  }
	  impededTeethEruptionNo = document.getElementById("impededTeethEruptionNo").value;
	  if(impededTeethEruptionNo!='' && impededTeethEruptionNo!=null){
		  result	=	checkSameToothNos(crossBiteAllteeth,impededTeethEruptionNo);
		  if(result==true)
		  {
			  alert(type+" should not be same tooth number as Cross Bite");
			  obj.value='';
			  return false;
		  }
	  }
	  impededTeethEruptionNo = document.getElementById("impededTeethEruptionNo").value;
	  if(impededTeethEruptionNo!='' && impededTeethEruptionNo!=null){
		  result	=	checkSameToothNos(submergerdTeethNo,impededTeethEruptionNo);
		  if(result==true)
		  {
			  alert(type+" should not be same tooth number as Submerged");
			  obj.value='';
			  return false;
		  }
	  }
	  impededTeethEruptionNo = document.getElementById("impededTeethEruptionNo").value;
	  if(impededTeethEruptionNo!='' && impededTeethEruptionNo!=null){
		  result	=	checkSameToothNos(ectopicTeethNo,impededTeethEruptionNo);
		  if(result==true)
		  {
			  alert(type+" should not be same tooth number as Ectopic");
			  obj.value='';
			  return false;
		  }
	  }
	  
	  impededTeethEruptionNo = document.getElementById("impededTeethEruptionNo").value;
	  if(impededTeethEruptionNo!='' && impededTeethEruptionNo!=null){
		  result	=	checkSameToothNos(contactPointDisplacement,impededTeethEruptionNo);
		  if(result==true)
		  {
			  alert(type+" should not be same tooth number as Displacement");
			  obj.value='';
			  return false;
		  }
	  }
}//Impeded

if("Impacted"==type){
	impededTeethNo		 = document.getElementById("impededTeethNo").value;
	  if(impededTeethNo!='' && impededTeethNo!=null){
		  result	=	checkSameToothNos(hypodontiaAllteeth,impededTeethNo);
		  if(result==true)
		  {
			  alert(type+" should not be same tooth number as hypodontic");
			  obj.value='';
			  return false;
		  }
	  }
	  impededTeethNo		 = document.getElementById("impededTeethNo").value;
	  if(impededTeethNo!='' && impededTeethNo!=null){
		  result	=	checkSameToothNos(crossBiteAllteeth,impededTeethNo);
		  if(result==true)
		  {
			  alert(type+" should not be same tooth number as Cross Bite");
			  obj.value='';
			  return false;
		  }
	  }
	  impededTeethNo		 = document.getElementById("impededTeethNo").value;
	  if(impededTeethNo!='' && impededTeethNo!=null){
		  result	=	checkSameToothNos(submergerdTeethNo,impededTeethNo);
		  if(result==true)
		  {
			  alert(type+" should not be same tooth number as Submerged");
			  obj.value='';
			  return false;
		  }
	  }
	  impededTeethNo		 = document.getElementById("impededTeethNo").value;
	  if(impededTeethNo!='' && impededTeethNo!=null){
		  result	=	checkSameToothNos(impededTeethEruptionNo,impededTeethNo);
		  if(result==true)
		  {
			  alert(type+" should not be same tooth number as Impeded");
			  obj.value='';
			  return false;
		  }
	  }
	  impededTeethNo		 = document.getElementById("impededTeethNo").value;
	  if(impededTeethNo!='' && impededTeethNo!=null){
		  result	=	checkSameToothNos(ectopicTeethNo,impededTeethNo);
		  if(result==true)
		  {
			  alert(type+" should not be same tooth number as Impacted");
			  obj.value='';
			  return false;
		  }
	  }
	  impededTeethNo = document.getElementById("impededTeethNo").value;
	  if(impededTeethNo!='' && impededTeethNo!=null){
		  result	=	checkSameToothNos(contactPointDisplacement,impededTeethNo);
		  if(result==true)
		  {
			  alert(type+" should not be same tooth number as Displacement");
			  obj.value='';
			  return false;
		  }
	  }
	}//Impacted
if("Ectopic"==type){
	  
	ectopicTeethNo		 = document.getElementById("ectopicTeethNo").value;
	  if(ectopicTeethNo!='' && ectopicTeethNo!=null){
		  result	=	checkSameToothNos(submergerdTeethNo,ectopicTeethNo);
		  if(result==true)
		  {
			  alert(type+" should not be same tooth number as Submerged");
			  obj.value='';
			  return false;
		  }
	  }
	  ectopicTeethNo		 = document.getElementById("ectopicTeethNo").value;
	  if(ectopicTeethNo!='' && ectopicTeethNo!=null){
		  result	=	checkSameToothNos(impededTeethEruptionNo,ectopicTeethNo);
		  if(result==true)
		  {
			  alert(type+" should not be same tooth number as Impeded");
			  obj.value='';
			  return false;
		  }
	  }
	  ectopicTeethNo		 = document.getElementById("ectopicTeethNo").value;
	  if(ectopicTeethNo!='' && ectopicTeethNo!=null){
		  result	=	checkSameToothNos(impededTeethNo,ectopicTeethNo);
		  if(result==true)
		  {
			  alert(type+" should not be same tooth number as Impacted");
			  obj.value='';
			  return false;
		  }
	  }//Impacted
  }//Ectopic
	if("Supernumerary"==type){
		supernumeryTeethNo = document.getElementById("supernumeryTeethNo").value;
		  if(supernumeryTeethNo!='' && supernumeryTeethNo!=null){
			  result	=	checkSameToothNos(contactPointDisplacement,supernumeryTeethNo);
			  if(result==true)
			  {
				  alert(type+" should not be same tooth number as Displacement");
				  obj.value='';
				  return false;
			  }
		  }
		  supernumeryTeethNo = document.getElementById("supernumeryTeethNo").value;
		  if(supernumeryTeethNo!='' && supernumeryTeethNo!=null){
			  result	=	checkSameToothNos(crossBiteAllteeth,supernumeryTeethNo);
			  if(result==true)
			  {
				  alert(type+" should not be same tooth number as Cross Bite");
				  obj.value='';
				  return false;
			  }
		  }
	}
}

//CrossBite Validation
function checkCrossBite(obj,type){
	  //Hypodentia with all check
	  var  crossBiteAllteeth	=	document.getElementById("crossbiteAntrio").value+"|"
									+document.getElementById("crossbitePosterior").value+"|"
									+document.getElementById("crossbiteRetrucontract").value;
	  var contactPointDisplacement		 = document.getElementById("contactPointDisplacement").value;
	  var  hypodontiaAllteeth	=	document.getElementById("hypodontiaQuand1Teeth").value+"|"
									+document.getElementById("hypodontiaQuand2Teeth").value+"|"
									+document.getElementById("hypodontiaQuand3Teeth").value+"|"
									+document.getElementById("hypodontiaQuand4Teeth").value;
	  var impededTeethEruptionNo = document.getElementById("impededTeethEruptionNo").value;
	  var impededTeethNo		 = document.getElementById("impededTeethNo").value;
	  var submergerdTeethNo		 = document.getElementById("submergerdTeethNo").value;
	  var supernumeryTeethNo	 = document.getElementById("supernumeryTeethNo").value;
	 
	  //Individual Cross bites cannot be same
	  if(document.getElementById("crossbiteAntrio").value!=''){
		  if(document.getElementById("crossbiteAntrio").value==(document.getElementById("crossbitePosterior").value 
				  											|| document.getElementById("crossbiteRetrucontract").value))
			  {
					alert(type+" should not be same tooth number as other Crossbites");
				  obj.value='';
				  return false;
			  }
	  }
	  if(document.getElementById("crossbitePosterior").value!=''){
		  if(document.getElementById("crossbitePosterior").value==(document.getElementById("crossbiteAntrio").value 
					|| document.getElementById("crossbiteRetrucontract").value))
			{
				alert(type+" should not be same tooth number as other Crossbites");
			obj.value='';
			return false;
			}
	  }
	  if(document.getElementById("crossbiteRetrucontract").value!=''){
		  if(document.getElementById("crossbiteRetrucontract").value==(document.getElementById("crossbiteAntrio").value 
					|| document.getElementById("crossbitePosterior").value))
			{
			alert(type+" should not be same tooth number as other Crossbites");
			obj.value='';
			return false;
			}
	  }
	  
	  
if(crossBiteAllteeth!='' || crossBiteAllteeth!=null){
	  
	if(contactPointDisplacement!="" && contactPointDisplacement!=null){
		result	=	checkSameToothNos(crossBiteAllteeth,contactPointDisplacement);
		if(result==true)
			  {
				  alert(type+" should not be same tooth number");
				  obj.value='';
				  return false;
			  }
		}
	var crossbiteAntrio	=	document.getElementById("crossbiteAntrio").value;
	if(crossbiteAntrio!="" && crossbiteAntrio!=null){
		result	=	checkSameToothNos(hypodontiaAllteeth,crossbiteAntrio);
		if(result==true)
		  {
			  alert("Crossbite Anterior should not be same tooth number as Hypodontia");
			  obj.value='';
			  return false;
		  }
	}
	crossbiteAntrio	=	document.getElementById("crossbitePosterior").value;
	if(crossbiteAntrio!="" && crossbiteAntrio!=null){
		result	=	checkSameToothNos(hypodontiaAllteeth,crossbiteAntrio);
		if(result==true)
		  {
			  alert("Crossbite Posterior should not be same tooth number as Hypodontia");
			  obj.value='';
			  return false;
		  }
	}
	crossbiteAntrio	=	document.getElementById("crossbiteRetrucontract").value;
	if(crossbiteAntrio!="" && crossbiteAntrio!=null){
		result	=	checkSameToothNos(hypodontiaAllteeth,crossbiteAntrio);
		if(result==true)
		  {
			  alert("Crossbite retruded contact should not be same tooth number as Hypodontia");
			  obj.value='';
			  return false;
		  }
	}
	
	if(impededTeethNo!="" && impededTeethNo!=null){
		result	=	checkSameToothNos(crossBiteAllteeth,impededTeethNo);
		if(result==true)
		  {
			  alert(type+" should not be same tooth number");
			  obj.value='';
			  return false;
		  }
	}
	if(impededTeethEruptionNo!="" && impededTeethEruptionNo!=null){
		result	=	checkSameToothNos(crossBiteAllteeth,impededTeethEruptionNo);
		if(result==true)
		  {
			  alert(type+" should not be same tooth number");
			  obj.value='';
			  return false;
		  }
	}
	if(submergerdTeethNo!="" && submergerdTeethNo!=null){
		result	=	checkSameToothNos(crossBiteAllteeth,submergerdTeethNo);
		if(result==true)
		  {
			  alert(type+" should not be same tooth number");
			  obj.value='';
			  return false;
		  }
	}
	if(supernumeryTeethNo!="" && supernumeryTeethNo!=null){
		result	=	checkSameToothNos(crossBiteAllteeth,supernumeryTeethNo);
		if(result==true)
		  {
			  alert(type+" should not be same tooth number");
			  obj.value='';
			  return false;
		  }
	}

	if("Displacement"==type){
		hypodontiaAllteeth	=	document.getElementById("hypodontiaQuand1Teeth").value+"|"
								+document.getElementById("hypodontiaQuand2Teeth").value+"|"
								+document.getElementById("hypodontiaQuand3Teeth").value+"|"
								+document.getElementById("hypodontiaQuand4Teeth").value;
		contactPointDisplacement		 = document.getElementById("contactPointDisplacement").value;
		if(contactPointDisplacement!="" && contactPointDisplacement!=null){
			result	=	checkSameToothNos(hypodontiaAllteeth,contactPointDisplacement);
			if(result==true)
			  {
				  alert(type+" should not be same tooth number as Hypodontia");
				  obj.value='';
				  return false;
			  }
		}
		if(contactPointDisplacement!="" && contactPointDisplacement!=null){
			result	=	checkSameToothNos(crossBiteAllteeth,contactPointDisplacement);
			if(result==true)
			  {
				  alert(type+" should not be same tooth number as Cross Bite");
				  obj.value='';
				  return false;
			  }
		}
		impededTeethEruptionNo		 = document.getElementById("impededTeethEruptionNo").value;
		contactPointDisplacement		 = document.getElementById("contactPointDisplacement").value;
		if(impededTeethEruptionNo!="" && impededTeethEruptionNo!=null){
			result	=	checkSameToothNos(contactPointDisplacement,impededTeethEruptionNo);
			if(result==true)
			  {
				  alert(type+" should not be same tooth number as Impeded");
				  obj.value='';
				  return false;
			  }
		}
		impededTeethNo		 = document.getElementById("impededTeethNo").value;
		contactPointDisplacement		 = document.getElementById("contactPointDisplacement").value;
		if(impededTeethNo!="" && impededTeethNo!=null){
			result	=	checkSameToothNos(contactPointDisplacement,impededTeethNo);
			if(result==true)
			  {
				  alert(type+" should not be same tooth number as Impacted");
				  obj.value='';
				  return false;
			  }
		}
		submergerdTeethNo		 = document.getElementById("submergerdTeethNo").value;
		contactPointDisplacement		 = document.getElementById("contactPointDisplacement").value;
		if(submergerdTeethNo!="" && submergerdTeethNo!=null){
			result	=	checkSameToothNos(contactPointDisplacement,submergerdTeethNo);
			if(result==true)
			  {
				  alert(type+" should not be same tooth number as Submerged");
				  obj.value='';
				  return false;
			  }
		}
		supernumeryTeethNo		 = document.getElementById("supernumeryTeethNo").value;
		contactPointDisplacement		 = document.getElementById("contactPointDisplacement").value;
		if(supernumeryTeethNo!="" && supernumeryTeethNo!=null){
			result	=	checkSameToothNos(contactPointDisplacement,supernumeryTeethNo);
			if(result==true)
			  {
				  alert(type+" should not be same tooth number as Supernumerary");
				  obj.value='';
				  return false;
			  }
		}
	}
}
checkImpededTooth(obj,type);
}

function checkSameToothNos(obj1,obj2){
	
	var bool	=	false;
	var obj1SplitValues	=	obj1.split("|");
	var obj2SplitValues	=	obj2.split("|");

	for(var i=0;i<obj1SplitValues.length;i++)
	{
		for(var j=0;j<obj2SplitValues.length;j++)
		{
			if(obj1SplitValues[i]==obj2SplitValues[j]){
				bool	=	true;break;
				}
		}
		
			
	}
	return bool;
	
}