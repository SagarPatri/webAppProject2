package com.ttk.common.webservices;


import javax.naming.InitialContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import oracle.xdb.XMLType;

import org.apache.axis.AxisFault;

import com.ttk.business.webservice.WebServiceManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.business.common.SecurityManager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;

import com.ttk.dao.impl.webservice.PreApprovalStatusResponse;
import com.ttk.dto.usermanagement.UserVO;
import com.ttk.common.webservices.helper.ValidatorResourceHelper;

public class AlkootPreapprovalWebService {

	public String addPreapproval(String empanelId, String userName,String password, String inputPreapprovalXML, byte[] inputPdfData ) throws AxisFault {
		String strResult = "";
		String strRandomNo="";
		int strResult1;
		StringBuilder sb = new StringBuilder();
		try {
				//For Authentication empanelId, userName, password
			ArrayList<Object> alUserList = new ArrayList<Object>();
			UserVO userVO = new UserVO();
			userVO.setLoginType("HOS");
			userVO.setGroupID(empanelId);
			userVO.setUSER_ID(userName);
			userVO.setPassword(password);		
			alUserList = (ArrayList<Object>) this.getSecurityManagerObject().externalLogin(userVO);
			
			//For Saving XML to Database
			 strRandomNo=(String) TTKCommon.checkNull(alUserList.get(4));
			if(strRandomNo.equalsIgnoreCase("ABC")){
				WebServiceManager webServiceManager = this.getWebServiceManagerObject();
				strResult = webServiceManager.alAlhiPreauth(inputPreapprovalXML,empanelId);
				sb.append(strResult);
			}			
			if(strResult.contains("SUCCESS")){
				if(strResult.contains("PreApprovalNo:")){
					String strArrValue[] = strResult.split("PreApprovalNo:");
					String strPreauthNo = strArrValue[strArrValue.length - 1]; 
					WebServiceManager webServiceManager = this.getWebServiceManagerObject();
					if (inputPdfData!=null){
					strResult1=webServiceManager.saveDocUploads(strPreauthNo,inputPdfData);
					}
				}
			}
		}// end of try
		catch (TTKException expTTK) {
			sb.append("<ErrorInfo>");	
			//System.out.println("expTTK.getMessage()::" + expTTK.getMessage());		
			if (expTTK.getMessage().equals("error.usermanagement.login.empaneluserinvalid")) {
				sb.append("User Credentials Are Wrong");
			}
			sb.append("</ErrorInfo>");
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			exp.printStackTrace();
			AxisFault fault = new AxisFault();
			fault.setFaultCodeAsString(ValidatorResourceHelper.getResourceBundle().getString(exp.getMessage()));
			throw fault;
		}// end of catch
		return sb.toString();
	}// end of  addPreapproval(String empanelId, String userName,String password, String inputPreapprovalXML)

	/*To check the pre-approval status*/
	
	public String checkPreauthStatus(String empanelId, String userName,String password, String preapprovalNo) throws AxisFault {
		String strResult = "";
		PreApprovalStatusResponse preApprovalStatusResponse = new PreApprovalStatusResponse();
		String strRandomNo="";
		StringBuilder sb = new StringBuilder();
		try {
				//For Authentication empanelId, userName, password
			ArrayList<Object> alUserList = new ArrayList<Object>();
			UserVO userVO = new UserVO();
			userVO.setLoginType("HOS");
			userVO.setGroupID(empanelId);
			userVO.setUSER_ID(userName);
			userVO.setPassword(password);		
			alUserList = (ArrayList<Object>) this.getSecurityManagerObject().externalLogin(userVO);
			
			//For Checking Pre-Approval Status
			 strRandomNo=(String) TTKCommon.checkNull(alUserList.get(4));
			if(strRandomNo.equalsIgnoreCase("ABC")){
				WebServiceManager webServiceManager = this.getWebServiceManagerObject();
				preApprovalStatusResponse = webServiceManager.checkPreapprovalStatus(preapprovalNo);
				StringWriter stringWriter =new StringWriter();
				JAXBContext jaxbContext = JAXBContext.newInstance(PreApprovalStatusResponse.class);
	  			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	  			// output pretty printed
	  			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	  			jaxbMarshaller.marshal(preApprovalStatusResponse, stringWriter);
	  			//System.out.println(stringWriter.toString());
				sb.append(stringWriter);
			}			
		}// end of try
		catch (TTKException expTTK) {
			sb.append("<ErrorInfo>");	
			//System.out.println("expTTK.getMessage()::" + expTTK.getMessage());		
			if (expTTK.getMessage().equals("error.usermanagement.login.empaneluserinvalid")) {
				sb.append("User Credentials Are Wrong");
			}
			sb.append("</ErrorInfo>");
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			exp.printStackTrace();
			AxisFault fault = new AxisFault();
			fault.setFaultCodeAsString(ValidatorResourceHelper.getResourceBundle().getString(exp.getMessage()));
			throw fault;
		}// end of catch
		return sb.toString();
	}// end of checkPreauthStatus(String empanelId, String userName,String password, String preapprovalNo)

	
	private WebServiceManager getWebServiceManagerObject() throws TTKException {
		WebServiceManager webServiceManager = null;
		try {
			if (webServiceManager == null) {
				InitialContext ctx = new InitialContext();
				webServiceManager = (WebServiceManager) ctx.lookup("java:global/TTKServices/business.ejb3/WebServiceManagerBean!com.ttk.business.webservice.WebServiceManager");
			}// end if(webServiceManager == null)
		}// end of try
		catch (Exception exp) {
			throw new TTKException(exp, "webservice");
		}// end of catch
		return webServiceManager;
	}// end getWebServiceManagerObject()

	
	
	private SecurityManager getSecurityManagerObject() throws TTKException {
		SecurityManager securityManager = null;
		try {
			if (securityManager == null) {
				InitialContext ctx = new InitialContext();
				securityManager = (SecurityManager) ctx.lookup("java:global/TTKServices/business.ejb3/SecurityManagerBean!com.ttk.business.common.SecurityManager");																																
			}// end if
		}// end of try
		catch (Exception exp) {
			throw new TTKException(exp, "security");
		}// end of catch(Exception exp)
		return securityManager;
	}// end getSecurityManagerObject()

	
	
}
