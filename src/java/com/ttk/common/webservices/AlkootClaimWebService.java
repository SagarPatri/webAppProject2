package com.ttk.common.webservices;

import java.util.ArrayList;

import javax.naming.InitialContext;

import org.apache.axis.AxisFault;

import com.ttk.business.common.SecurityManager;
import com.ttk.business.webservice.WebServiceManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.common.webservices.helper.ValidatorResourceHelper;
import com.ttk.dto.usermanagement.UserVO;

public class AlkootClaimWebService {
	
	
	public String addClaim(String empanelId, String userName,String password, String inputPreapprovalXML, byte[] inputPdfData ) throws AxisFault {
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
				strResult = webServiceManager.alAlhiClaim(inputPreapprovalXML);
				sb.append(strResult);
			}			
			if(strResult.contains("SUCCESS")){
				if(strResult.contains("ClaimNo:")){
					String strArrValue[] = strResult.split("ClaimNo:");
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
