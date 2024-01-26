
/**
 * @ (#) AlKootWebServices.java 26 Sept 2017
 * Project       : TTK HealthCare Services
 * File          : WebServices.java
 * Author        : Kishor kumar S H
 * Company       : Vidal Health
 * Date Created  : 26 Sept 2017
 *
 * @author       :  Kishor kumar S H
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.common.webservices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.naming.InitialContext;

import org.apache.axis.AxisFault;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.ttk.business.common.SecurityManager;
import com.ttk.business.onlineforms.OnlineAccessManager;
import com.ttk.business.webservice.WebServiceManager;
import com.ttk.common.exception.TTKException;
import com.ttk.common.webservices.helper.ValidatorResourceHelper;
import com.ttk.dto.preauth.CashlessDetailVO;
import com.ttk.dto.usermanagement.UserVO;
//import org.dom4j.Document;
public class AlKootWebServices
{
	private static Logger log = Logger.getLogger(AlKootWebServices.class);
	
	
	/**
     * This method method returns the String 
     * @param String object which contains the strVidalID for which data in is required.
     * @param String object which contains thestrIMEINumber for which data in is required.
     * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
    
    public String checkMemberEligibility(String empanelId,String userName,String password,String alKootId,String treatmentDate,String benifitType,String dob) throws org.apache.axis.AxisFault{
    	String strResult="";
        StringBuilder sb	=	new StringBuilder();
    	try
        {
    		log.info("INSIDE ALKOOTWEBSERVICES START");
            WebServiceManager webServiceManager=this.getWebServiceManagerObject();
            String eligibility=null;
            String reason=null;
            //Login check		
            Long hospSeqId=null,userSeqId=null;
            hospSeqId	=	webServiceManager.getSeqIds(empanelId,"HOS");
            userSeqId	=	webServiceManager.getSeqIds(empanelId,"USER");
            ArrayList<Object>alUserList = new ArrayList<Object>() ;
            UserVO userVO = new UserVO();
            userVO.setLoginType("HOS");
        	userVO.setGroupID(empanelId);
        	userVO.setUSER_ID(userName);
            userVO.setPassword(password);
            userVO.setGrpOrInd("IND"); 
            alUserList = (ArrayList<Object>)this.getSecurityManagerObject().externalLogin(userVO);
            /*for(int k=0;k<alUserList.size();k++)
            	System.out.println(alUserList.get(k));*/
            OnlineAccessManager onlineAccessManager = this.getOnlineAccessManagerObject();
            ArrayList alOnlineAccList	= onlineAccessManager.getValidateEnrollId(alKootId,hospSeqId,userSeqId,treatmentDate);
            String flag	=	(String)alOnlineAccList.get(1);
            String benefitType1=benifitType.toUpperCase();
			//this code is to get the member and other details based on the enroll id
			CashlessDetailVO cashlessDetailVO	= onlineAccessManager.geMemberDetailsOnEnrollId(alKootId,benefitType1,hospSeqId);
			String formattedMemDob=new SimpleDateFormat("dd-MM-yyyy").format(cashlessDetailVO.getMemDob());
			String formattedMemDob1=new SimpleDateFormat("dd/MM/yyyy").format(cashlessDetailVO.getMemDob());
			//cashlessDetailVO.setEnrollId(enrollId);
			strResult	=	cashlessDetailVO.getEligibility();		
			//Added by Vishwa For ( Year Comparison of  DOB )  
			String inputDob;
			if(dob.contains("-")){
				String dob1[] = dob.split("-");
				inputDob = dob1[dob1.length - 1];
			}else{
				String dob1[] = dob.split("/");
				 inputDob = dob1[dob1.length - 1];
			}
				
			String dob2[] = formattedMemDob.split("-");
			String dbDob1 = dob2[dob2.length - 1];
			
			String dob3[] = formattedMemDob1.split("/");
			String dbDob2 = dob3[dob3.length - 1];
			
		if(!(inputDob.equals(dbDob1) || inputDob.equals(dbDob2))){
				TTKException expTTK = new TTKException();
				expTTK.setMessage("error.dob.mismatch.alkootID");
				throw expTTK;
			}
			//NETWORKS VALIDATION
			String netArry[]=	new String[2];
			netArry	=	cashlessDetailVO.getNetworksArray();
			String netArrys[]	=	new String[cashlessDetailVO.getAssNetworksArray().size()];
			Map<String,String> map	=	cashlessDetailVO.getAssNetworksArray();
			
			Set keySet	=	map.keySet();
			Set valSet	=	map.keySet();
			Iterator it	=	 keySet.iterator();
			Iterator it1			=	 valSet.iterator();
			int	temp				=	0;
			int	compTemp			=	0;
			String compToPolicyNet	=	"";
			String compToPolicyNetVal=	"";
			String policyNet		=	"";
			String provNet			=	"";
			if(netArry!=null){
				policyNet	=	netArry[1];
				provNet		=	netArry[0];
			}
			ArrayList<String> networkOrder	=	new ArrayList<String>();
			
			while(it.hasNext() && it1.hasNext()){
				compToPolicyNet	=	(String)(it.next());
				if(compToPolicyNet.equals(policyNet))
					compTemp	=	temp;
				compToPolicyNetVal	=	map.get(it1.next());
				
				//System.out.println("compTemp::"+compTemp);

				networkOrder.add(compToPolicyNet);//adding Network names to arrayList of all networks
				netArrys[temp++]=	compToPolicyNetVal;//adding Y/N to array of all networks
				
			}
			if(temp>1)
				temp	=	temp-1;
			
			int tempPol		=	0;
			int tempProv	=	0;
			for(int i=0;i<=networkOrder.size()-1;i++)
			{
				if(policyNet.equals(networkOrder.get(i))){
					tempPol	=	i;
				}
				if(provNet.equals(networkOrder.get(i))){
					tempProv	=	i;
				}
			}
			
			
			
			 if("Y".equals(cashlessDetailVO.getLimit_exhausted_yn())){
					
					TTKException expTTK = new TTKException();
					expTTK.setMessage("error.no.benefit.Out-Patient.limit.exhausted");
					throw expTTK;
				}
			
			
			
			 //sb.append("<?xml version='1.0' standalone='yes'?>");
			 sb.append("<Eligibilty>");
			
			/*if(netArrys[compTemp].trim().equals("Y") && "YES".equals(cashlessDetailVO.getEligibility().trim())){//IF SELECTED OTHERS AT EMPANELMENT LEVEL
				sb.append("Yes"); sb.append("</Eligibilty>");
				eligibility="YES";
			}else*/
				
				if(tempProv>=tempPol && "YES".equals(cashlessDetailVO.getEligibility())){//IF POLICY NETWORK IS GREATER THAN PROVIDER NETWORK
				sb.append("Yes"); sb.append("</Eligibilty>");
				eligibility="YES";
			}else if(netArrys[compTemp].trim().equals("N") && "NO".equals(cashlessDetailVO.getEligibility().trim())
					&& tempProv==tempPol)//NOW MATERNITY CASE
			{
				sb.append("No"); sb.append("</Eligibilty>");
				eligibility="NO";
				reason="MATERNITY CASE";
			}
			/*else if("YES".equals(cashlessDetailVO.getAssociateYN())){
				sb.append("Yes"); sb.append("</Eligibilty>");
				eligibility="YES";
			}*/
			else if("YES".equals(cashlessDetailVO.getAssociateYN()) && "YES".equals(cashlessDetailVO.getEligibility())){
				sb.append("Yes"); sb.append("</Eligibilty>");
				eligibility="YES";
			}
			/*else if("NO".equals(cashlessDetailVO.getAssociateYN()) && "YES".equals(cashlessDetailVO.getEligibility())){
				sb.append("Yes"); sb.append("</Eligibilty>");
				eligibility="YES";
			}*/
			else {//REJECTION
				sb.append("No"); sb.append("</Eligibilty>");
				sb.append("<ReasonForRejection>");
				sb.append(cashlessDetailVO.getReasonForRejection());
				eligibility="NO";
			if(netArrys[compTemp].trim().equals("N") && "NO".equals(cashlessDetailVO.getEligibility().trim())
					&& tempProv==tempPol){//NOW
				sb.append("benefit is Not Covered for the Policy.");
				reason="Benefit is Not Covered for the Policy.";
			}else if(netArrys[compTemp].trim().equals("N") && "NO".equals(cashlessDetailVO.getEligibility().trim())
					&& tempProv<tempPol){
				sb.append("network is Not Eligible for the Policy.");
				reason="Network is Not Eligible for the Policy.";
			}else if(netArrys[compTemp].trim().equals("N") && "YES".equals(cashlessDetailVO.getEligibility().trim())
					&& tempProv<tempPol){//NOW
				sb.append("network is Not Eligible for the Policy.");
				reason="Network is Not Eligible for the Policy.";
			}else if(netArrys[compTemp].trim().equals("N") && "NO".equals(cashlessDetailVO.getEligibility().trim())
					&& tempProv==tempPol)
			{
				sb.append("member is not Eligible for this Benefit.");
				reason="Member is not Eligible for this Benefit.";
			}
			else if(tempProv<tempPol){
				sb.append("benefit is Not Covered for the Policy.");
				reason="Benefit is Not Covered for the Policy.";
			}	
			else{
				reason="Member is not eligible for this Benefit type.";
			}
			 sb.append("</ReasonForRejection>");
			}//NETWORKS VALIDATION ENDS
			
			
		 if("Y".equals(cashlessDetailVO.getBufferFlag())){
				sb.append("<WarningMessage>");
				sb.append("Please take preapproval as there is minimum balance in selected Benefit type.");
				sb.append("</WarningMessage>");
				sb.append("<BenefitTypeLimit>");
				sb.append(cashlessDetailVO.getAvailble_limit());
				 sb.append("</BenefitTypeLimit>");
				eligibility="YES";
			}
		
			 /* Capturing log start*/
			ArrayList<String> dataList=new ArrayList<>();
			dataList.add("AL AHLI");
			dataList.add(alKootId);
			dataList.add(benifitType);
			dataList.add(treatmentDate);
			dataList.add(eligibility);
			dataList.add(hospSeqId+"");
			dataList.add(null);
			dataList.add(null);
			dataList.add(null);
			dataList.add(null);
			dataList.add(null);
			dataList.add(userSeqId+"");
			dataList.add(null);
			dataList.add(empanelId+"");
			dataList.add(null);
			dataList.add(reason);
			onlineAccessManager.updateLogTable(dataList);
			/* Capturing log end*/
			
			
			//TOB S T A R T S
			String[] tobBenefits	=	onlineAccessManager.getTobForBenefits(benifitType,alKootId);
			cashlessDetailVO.setCoPay(tobBenefits[0]);
			cashlessDetailVO.setEligibility1(tobBenefits[1]);
			cashlessDetailVO.setDeductible(tobBenefits[2]);
			//request.setAttribute("tobBenefits","tobBenefits");
			
			sb.append("<TableOfBenefits>");
			sb.append("<Copay>");
				sb.append(tobBenefits[0]);
			sb.append("</Copay>");
			
			sb.append("<Eligibility>");
				sb.append(tobBenefits[1]);
			sb.append("</Eligibility>");
			
			sb.append("<Deductible>");
				sb.append(tobBenefits[2]);
			sb.append("</Deductible>");
			
			sb.append("<Co_Ins>");
				sb.append(tobBenefits[3]);
			sb.append("</Co_Ins>");
		
			sb.append("<Class>");
				sb.append(tobBenefits[4]);
			sb.append("</Class>");
		
			sb.append("<Maternity_yn>");
				sb.append(tobBenefits[5]);
			sb.append("</Maternity_yn>");
		
			sb.append("<Maternity_Copay>");
				sb.append(tobBenefits[6]);
			sb.append("</Maternity_Copay>");
		
			sb.append("<Optical_yn>");
				sb.append(tobBenefits[7]);
			sb.append("</Optical_yn>");
		
			sb.append("<Optical_Copay>");
				sb.append(tobBenefits[8]);
			sb.append("</Optical_Copay>");
		
			sb.append("<Dental_yn>");
				sb.append(tobBenefits[9]);
			sb.append("</Dental_yn>");
		
			sb.append("<Dental_Copay>");
				sb.append(tobBenefits[10]);
			sb.append("</Dental_Copay>");
		
			sb.append("<IP_OP_Services>");
				sb.append(tobBenefits[11]);
			sb.append("</IP_OP_Services>");
		
			sb.append("<Pharmaceuticals>");
				sb.append(tobBenefits[12]);
			sb.append("</Pharmaceuticals>");
		
		sb.append("</TableOfBenefits>");
			//TOB E N D S
			
		sb.append("<Patient_Details>");
		sb.append("<Membername>");
			sb.append(cashlessDetailVO.getMemberName());
		sb.append("</Membername>");
		sb.append("<Al_Koot_ID_No>");
			sb.append(cashlessDetailVO.getEnrollId());
		sb.append("</Al_Koot_ID_No>");
		sb.append("<Qatar_ID>");
			sb.append(cashlessDetailVO.getEmirateID());
		sb.append("</Qatar_ID>");
		sb.append("<Age>");
		sb.append(cashlessDetailVO.getAge());
		sb.append("</Age>");
		sb.append("<Gender>");
			sb.append(cashlessDetailVO.getGender());
		sb.append("</Gender>");
		sb.append("<Insurance_Company_Name>");
			sb.append(cashlessDetailVO.getPayer());
		sb.append("</Insurance_Company_Name>");
		sb.append("</Patient_Details>");
		
		sb.append("<Policy_Information>");
		sb.append("<Policy_No>");
			sb.append(cashlessDetailVO.getPolicyNo());
		sb.append("</Policy_No>");
		sb.append("<Policy_Start_Dt>");
			sb.append(cashlessDetailVO.getPolicyStDt());
		sb.append("</Policy_Start_Dt>");
		sb.append("<Policy_End_Dt>");
			sb.append(cashlessDetailVO.getPolicyEnDt());
		sb.append("</Policy_End_Dt>");
		sb.append("</Policy_Information>");
		
		sb.append("<Renewed_Policy_Information>");
		sb.append("<Renewed_Policy_Start_Dt>");
			sb.append(cashlessDetailVO.getRenewedMemberStartDate());
		sb.append("</Renewed_Policy_Start_Dt>");
		sb.append("<Renewed_Policy_End_Dt>");
			sb.append(cashlessDetailVO.getRenewedMemberEndDate());
		sb.append("</Renewed_Policy_End_Dt>");
		sb.append("</Renewed_Policy_Information>");
		
        log.info("INSIDE ALKOOTWEBSERVICES END");
        }//end of try
    	catch(TTKException expTTK)
        {
    		sb.append("<Eligibilty> No </Eligibilty>");
     		sb.append("<ErrorInfo>");
    		//System.out.println("expTTK.getMessage()::"+expTTK.getMessage());
        	if(expTTK.getMessage().equals("error.usermanagement.login.empaneluserinvalid"))
        	{ 
        		sb.append("User Credentials Are Wrong");
        	}
        	else if(expTTK.getMessage().equals("error.selffund.validateVidalID"))
        	{
        		sb.append("Given Alkoot Id is wrong");
        	}else if(expTTK.getMessage().equals("error.dateofAdimissionnotbetween.policyperiod"))
        	{
        		sb.append("Date of Adimission not between Policy Period");
        	}else if(expTTK.getMessage().equals("error.dob.mismatch.alkootID"))
        	{
        		sb.append("Date of Birth is not matching");
        	}else if(expTTK.getMessage().equals("error.intx.ActiveVidalId"))
        	{
        		sb.append("Al Koot Id is Not Active");
        	}else if(expTTK.getMessage().equals("error.database"))
        	{
        		//sb.append("Please check for Valid Inputs");
        		sb.append("Please enter valid Treatment Date");
        	}
        	else if(expTTK.getMessage().equals("error.no.benefit.limit.exhausted")) {
                sb.append("No.Benefit limit is exhausted.");
        	}
        	
        	else if(expTTK.getMessage().equals("error.no.benefit.Out-Patient.limit.exhausted")) {
                sb.append("Preapproval is restricted as the benefit balance is exhausted.");
        	}
        	
        	else if(expTTK.getMessage().equals("error.no.benefit.Dental.limit.exhausted")) {
                sb.append("Preapproval is restricted as the benefit balance is exhausted.");
        	}
        	else if(expTTK.getMessage().equals("error.no.benefit.Optical.limit.exhausted")) {
                sb.append("Preapproval is restricted as the benefit balance is exhausted.");
        	}
        	else if(expTTK.getMessage().equals("error.no.benefit.Health.limit.exhausted")) {
                sb.append("Preapproval is restricted as the benefit balance is exhausted.");
        	}
        	else if(expTTK.getMessage().equals("error.no.benefit.OutPatient.limit.exhausted")) {
                sb.append("Preapproval is restricted as the benefit balance is exhausted.");
        	}
        	else if(expTTK.getMessage().equals("error.no.benefit.InPatient.limit.exhausted")) {
                sb.append("Preapproval is restricted as the benefit balance is exhausted.");
        	}
        	
        	sb.append("</ErrorInfo>");
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            exp.printStackTrace();
            AxisFault fault=new AxisFault();
            fault.setFaultCodeAsString(ValidatorResourceHelper.getResourceBundle().getString(exp.getMessage()));
            throw fault;
        }//end of catch
        return sb.toString();
    }//end of individualLoginService(String strVidalID,StrIMEINumber)
    
    
    /**
     * Returns the WebServiceManager session object for invoking methods on it.
     * @return webServiceManager session object which can be used for method invokation
     * @exception throws TTKException
     */
    private WebServiceManager getWebServiceManagerObject() throws TTKException
    {
        WebServiceManager webServiceManager = null;
        try
        {
            if(webServiceManager == null)
            {
                InitialContext ctx = new InitialContext();
                webServiceManager = (WebServiceManager) ctx.lookup("java:global/TTKServices/business.ejb3/WebServiceManagerBean!com.ttk.business.webservice.WebServiceManager");
            }//end if(webServiceManager == null)
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp, "webservice");
        }//end of catch
        return webServiceManager;
    }//end getWebServiceManagerObject()

  
    private OnlineAccessManager getOnlineAccessManagerObject() throws TTKException
    {
    	OnlineAccessManager onlineAccessManager = null;
        try
        {
            if(onlineAccessManager == null)
            {
                InitialContext ctx = new InitialContext();
                //onlineAccessManager = (OnlineAccessManager) ctx.lookup(OnlineAccessManager.class.getName());
                onlineAccessManager = (OnlineAccessManager) ctx.lookup("java:global/TTKServices/business.ejb3/OnlineAccessManagerBean!com.ttk.business.onlineforms.OnlineAccessManager");
            }//end if
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp, "webservice");
        }//end of catch
        return onlineAccessManager;
    }//end of getOnlineAccessManagerObject()
   
    
    private SecurityManager getSecurityManagerObject() throws TTKException
    {
        SecurityManager securityManager = null;
        try
        {
            if(securityManager == null)
            {
                InitialContext ctx = new InitialContext();
                securityManager = (SecurityManager) ctx.lookup("java:global/TTKServices/business.ejb3/SecurityManagerBean!com.ttk.business.common.SecurityManager");	//changed for jboss upgradation
            }//end if
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp, "security");
        }//end of catch(Exception exp)
        return securityManager;
    }//end getSecurityManagerObject()
}//end of WebServices