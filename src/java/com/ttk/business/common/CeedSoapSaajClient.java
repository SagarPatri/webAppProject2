package com.ttk.business.common;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.dom4j.Document;

import com.ttk.dto.onlineforms.providerLogin.PbmPreAuthVO;
import com.ttk.dto.preauth.ActivityDetailsVO;
import com.ttk.dto.preauth.DiagnosisDetailsVO;

public class CeedSoapSaajClient {


    public static String execute(String serviceType, PbmPreAuthVO pbmPreAuthVO)throws Exception{

        // Create SOAP Connection
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
      
        pbmPreAuthVO.getIcdCode();
        // Send SOAP Message to SOAP Server
        String url = "http://dss.dimensions-healthcare.com:88/ws/cds.wsdl";
        SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(pbmPreAuthVO), url);
       
        // print SOAP Response
        System.out.print("Response SOAP Message::1:::"+soapResponse);

      
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        soapResponse.writeTo(baos);

        soapConnection.close();
    return new String(baos.toByteArray());
    }

    
    
    public static SOAPMessage createSOAPRequest(PbmPreAuthVO pbmPreAuthVO) throws Exception {
//    	System.out.println("xmlData:::"+xmlData.asXML().toString());
    	 MessageFactory messageFactory = MessageFactory.newInstance();
		 SOAPMessage soapMessage = messageFactory.createMessage();
		 soapMessage.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");
		 soapMessage.setProperty(SOAPMessage.WRITE_XML_DECLARATION, "true");
		 SOAPPart soapPart = soapMessage.getSOAPPart();
		 String nameSpaceURI	=	"http://cds.soap.webservice.service.dimensions.com/";
   	         SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
   	         soapEnvelope.addNamespaceDeclaration("cds", nameSpaceURI);
   	         SOAPHeader soapHeader = soapEnvelope.getHeader(); 
   	        
	   	    SOAPBody soapBody = soapEnvelope.getBody();
			SOAPElement soapBodyElem = soapBody.addChildElement("getClaimsEdits", "cds"); 
		
		    SOAPElement soapUserElem = soapBodyElem.addChildElement("username");
	        soapUserElem.addTextNode("Vidal");//UserName
	        SOAPElement soapPwdElem = soapBodyElem.addChildElement("password");
	        soapPwdElem.addTextNode("v1D@Lcd$Pwd");//Password
	        SOAPElement soapServicesElem = soapBodyElem.addChildElement("services");
	        soapServicesElem.addTextNode("1");//services
	        SOAPElement soapstrictCheck = soapBodyElem.addChildElement("isStrictCheck");
	        soapstrictCheck.addTextNode("1");//isStrictCheck
	        
	        SOAPElement soapclaims = soapBodyElem.addChildElement("claims"); 

	        SOAPElement soapclaimsId = soapclaims.addChildElement("claimID");
	        soapclaimsId.addTextNode(pbmPreAuthVO.getPreAuthSeqID().toString());//services
	        
	    

			ArrayList allPbmActivityDetails= new ArrayList();
			allPbmActivityDetails = pbmPreAuthVO.getDrugDetails();

			ActivityDetailsVO activityVO	=	new ActivityDetailsVO();
			for(int i=0;i<allPbmActivityDetails.size();i++){
	
			activityVO	=	(ActivityDetailsVO) allPbmActivityDetails.get(i);
						
	        
	        
	        SOAPElement soapclaimSubList = soapclaims.addChildElement("claimSubmissionActivityList"); 
	        SOAPElement soapActivity = soapclaimSubList.addChildElement("activityCode");
	        soapActivity.addTextNode(activityVO.getMophCodes());//as per confirmation from yasmin
	        SOAPElement activityId = soapclaimSubList.addChildElement("activityId");
	        activityId.addTextNode(activityVO.getMophCodes());//as per confirmation from yasmin
	        SOAPElement activitySource = soapclaimSubList.addChildElement("activitySource");
	        activitySource.addTextNode("CURRENT");//services
	        SOAPElement activityStart = soapclaimSubList.addChildElement("activityStart");
	        activityStart.addTextNode(activityVO.getStartDate());//services
	        SOAPElement activityType = soapclaimSubList.addChildElement("activityType");
	        activityType.addTextNode(activityVO.getActivityServiceType());//services
	        
	        SOAPElement claimSubmissionObservationList = soapclaimSubList.addChildElement("claimSubmissionObservationList"); 
	        SOAPElement observationCode = claimSubmissionObservationList.addChildElement("observationCode");
	        observationCode.addTextNode("");//services
	        SOAPElement observationType = claimSubmissionObservationList.addChildElement("observationType");
	        observationType.addTextNode("");//services
	        SOAPElement observationValue = claimSubmissionObservationList.addChildElement("observationValue");
	        observationValue.addTextNode("");//services
	        SOAPElement observationValueType = claimSubmissionObservationList.addChildElement("observationValueType");
	        observationValueType.addTextNode("");//services
	        
	        
	        SOAPElement clinician = soapclaimSubList.addChildElement("clinician");
	        clinician.addTextNode("");//services
	        SOAPElement orderingClinician = soapclaimSubList.addChildElement("orderingClinician");
	        orderingClinician.addTextNode("");//services
	        SOAPElement priorAuthorizationId = soapclaimSubList.addChildElement("priorAuthorizationId");
	        priorAuthorizationId.addTextNode("");//services
	        SOAPElement quantity = soapclaimSubList.addChildElement("quantity");
	        quantity.addTextNode(activityVO.getDrugCodeQuantity().toString());//services
	        SOAPElement systemActivityCodes = soapclaimSubList.addChildElement("systemActivityCodes");
	        systemActivityCodes.addTextNode("");//services
    }
	        
	        ArrayList allIcdDetails= new ArrayList();
			allIcdDetails = pbmPreAuthVO.getIcdDetails();
			DiagnosisDetailsVO diagnosisDetailsVO	=	new DiagnosisDetailsVO();
			for(int i=0;i<allIcdDetails.size();i++){
			diagnosisDetailsVO	=	(DiagnosisDetailsVO) allIcdDetails.get(i);
	        
	        SOAPElement claimSubmissionDiagnosisList = soapclaims.addChildElement("claimSubmissionDiagnosisList"); 
	        SOAPElement diagnosisCode = claimSubmissionDiagnosisList.addChildElement("diagnosisCode");
	        diagnosisCode.addTextNode(diagnosisDetailsVO.getIcdCode());//services
	        SOAPElement diagnosisSource = claimSubmissionDiagnosisList.addChildElement("diagnosisSource");
	        diagnosisSource.addTextNode("CURRENT");//services
	        SOAPElement diagnosisType = claimSubmissionDiagnosisList.addChildElement("diagnosisType");
	        diagnosisType.addTextNode(diagnosisDetailsVO.getPrimaryAilment());//services
	        SOAPElement systemDiagnosisCodes = claimSubmissionDiagnosisList.addChildElement("systemDiagnosisCodes");
	        systemDiagnosisCodes.addTextNode("");//services
			}
			
			
	        SOAPElement claimSubmissionEncounter = soapclaims.addChildElement("claimSubmissionEncounter"); 
	        SOAPElement encounterEnd = claimSubmissionEncounter.addChildElement("encounterEnd");
	        encounterEnd.addTextNode("");//services
	        SOAPElement encounterEndType = claimSubmissionEncounter.addChildElement("encounterEndType");
	        encounterEndType.addTextNode("");//services
	        SOAPElement encounterStart = claimSubmissionEncounter.addChildElement("encounterStart");
	        encounterStart.addTextNode("");//services
	        SOAPElement encounterStartType = claimSubmissionEncounter.addChildElement("encounterStartType");
	        encounterStartType.addTextNode("");//services
	        SOAPElement encounterType = claimSubmissionEncounter.addChildElement("encounterType");
	        encounterType.addTextNode("");//services
	        SOAPElement facilityId = claimSubmissionEncounter.addChildElement("facilityId");
	        facilityId.addTextNode("");//services
	        SOAPElement patientId = claimSubmissionEncounter.addChildElement("patientId");
	        patientId.addTextNode("");//services
	        SOAPElement transferDestination = claimSubmissionEncounter.addChildElement("transferDestination");
	        transferDestination.addTextNode("");//services
	        SOAPElement transferSource = claimSubmissionEncounter.addChildElement("transferSource");
	        transferSource.addTextNode("");//services
	        
	       
	        SOAPElement claimSubmissionPerson = soapclaims.addChildElement("claimSubmissionPerson"); 
	        SOAPElement breastFeedingDays = claimSubmissionPerson.addChildElement("breastFeedingDays");
	        breastFeedingDays.addTextNode("");
	        SOAPElement creatinineClearance = claimSubmissionPerson.addChildElement("creatinineClearance");
	        creatinineClearance.addTextNode("");
	        SOAPElement dateOfBirth = claimSubmissionPerson.addChildElement("dateOfBirth");
	        dateOfBirth.addTextNode(pbmPreAuthVO.getDobDate());
	        SOAPElement gender = claimSubmissionPerson.addChildElement("gender");
	        gender.addTextNode(pbmPreAuthVO.getMemGender());
	        SOAPElement hight = claimSubmissionPerson.addChildElement("hight");
	        hight.addTextNode("");
	        SOAPElement memberId = claimSubmissionPerson.addChildElement("memberId");
	        memberId.addTextNode("");
	        SOAPElement nationalId = claimSubmissionPerson.addChildElement("nationalId");
	        nationalId.addTextNode("");
	        SOAPElement notes = claimSubmissionPerson.addChildElement("notes");
	        notes.addTextNode("");
	        SOAPElement patientIdperson = claimSubmissionPerson.addChildElement("patientIdperson");
	        patientIdperson.addTextNode("");
	        SOAPElement pregnancyDays = claimSubmissionPerson.addChildElement("pregnancyDays");
	        pregnancyDays.addTextNode("");
	        SOAPElement weight = claimSubmissionPerson.addChildElement("weight");
	        weight.addTextNode("");
	        
	        SOAPElement payerId = soapclaims.addChildElement("payerId");
	        payerId.addTextNode("");
	        SOAPElement providerId = soapclaims.addChildElement("providerId");
	        providerId.addTextNode("");
	        
	        
		 soapMessage.saveChanges();
		 soapMessage.writeTo(System.out);
		 FileOutputStream fOut = new FileOutputStream("/home/tipsint/jboss-as-7.1.3.Final/bin/ProjectX/Tariff/Administration/SoapInput.xml");
		 soapMessage.writeTo(fOut);
		
		
		 return soapMessage;
	 }

    
    
public static void writeFileToDisk(String data, String fileName){
		
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
				fw = new FileWriter(fileName);
				bw = new BufferedWriter(fw);
				bw.write(data);

				//System.out.println("Done");
				
		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
		
	}
} 

