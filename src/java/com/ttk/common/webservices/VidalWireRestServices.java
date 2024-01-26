package com.ttk.common.webservices;
 
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.naming.InitialContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
//import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Base64;
import org.apache.pdfbox.util.PDFMergerUtility;
import org.apache.poi.util.IOUtils;
//import org.dom4j.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import com.lowagie.tools.arguments.FileArgument;
import com.ttk.business.common.MobileEcard;
import com.ttk.business.common.messageservices.CommunicationManager;

import javax.ws.rs.core.Response.ResponseBuilder; 



















//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
import com.ttk.business.webservice.WebServiceManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.common.messageservices.EmailHelper;
import com.ttk.common.webservices.vo.partner.GeneralDetails;
import com.ttk.common.webservices.vo.partner.Partner;
import com.ttk.dto.common.CommunicationOptionVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;
import com.ttk.dto.webservice.AuthenticationVO;
import com.ttk.dto.webservice.OnlineIntimationVO;
import com.ttk.dto.webservice.VidalClaimWireRestVO;
import com.ttk.dto.webservice.VidalWireRestVO;

import javax.ws.rs.core.MediaType;  
import javax.ws.rs.core.Response;  

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;  
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;  









//Added by vishwa
import java.io.File;
import java.util.Iterator;
import java.util.List;
 






import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
 






import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.FileOutputStream;







import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;



import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;






@Path("/mobileapp")
public class VidalWireRestServices {
	
	@POST
    @Path("/loginServiceNewUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getLoginRegistration(@HeaderParam("ocoUserName")String ocoUserName,@HeaderParam("ocoUserPassword")String ocoUserPassword ){
	
	VidalWireRestVO vidalwirerestvo=new VidalWireRestVO();
	
       JSONArray arra = new JSONArray();	        
        LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
        try {
 	        
        	String strUserName=TTKCommon.checkNull(ocoUserName);
        	String strUserPwd=TTKCommon.checkNull(ocoUserPassword);
        	if(strUserName.length()<1||strUserPwd.length()<1){
        		
        		mapObject.put("loginsuccessFlag", "N");
        		mapObject.put("alertMsg", "username or password empty");       		
        	}else
            {	                
        		vidalwirerestvo.setUsername(strUserName);
        		vidalwirerestvo.setPassword(strUserPwd);
        		
            WebServiceManager webServiceManager=this.getWebServiceManagerObject();
            
            VidalWireRestVO  returnVidalWireRestVO= webServiceManager.getLoginRegister(vidalwirerestvo);
            
             webServiceManager.sendEmailImmediate("EMAIL"); 
             webServiceManager.sendSmsImmediate("SMS");     
             
            mapObject.put("OTP_Number", returnVidalWireRestVO.getOtpNumber());
            mapObject.put("alertMsg", returnVidalWireRestVO.getPwdAlertMsg());
        	mapObject.put("loginsuccessFlag",returnVidalWireRestVO.getLoginStatus());//Y or N
        	mapObject.put("policy_Group_seq_id", returnVidalWireRestVO.getPolicyGroupSeqID());
           			
            }
           
        } 
            
         catch (TTKException tte) {	
        	 tte.printStackTrace();        	
        	 mapObject.put("loginsuccessFlag", "N");
 	        mapObject.put("alertMsg", "Unknown Error1");
        	
            
        }catch (Exception e) {
        	e.printStackTrace();
        	 mapObject.put("loginsuccessFlag", "N");
	        mapObject.put("alertMsg", "Unknown Error2");	            
		}
       
        arra.put(mapObject);
     
 
		return arra.toString();
        
    } 
	
	
	@POST
    @Path("/loginServiceNewUserOTP")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getLoginRegistrationOTP(@HeaderParam("ocoUserName")String ocoUserName,@HeaderParam("ocoUserPassword")String ocoUserPassword,@HeaderParam("ocoOTP")String ocoOTP,@HeaderParam("ocoPolicyGrpSeqId")String ocoPolicyGrpSeqId){
		
	VidalWireRestVO vidalwirerestvo=new VidalWireRestVO();
	
       JSONArray arra = new JSONArray();	        
        LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
        try {
 	        
        	String strUserName=TTKCommon.checkNull(ocoUserName);
        	String strUserPwd=TTKCommon.checkNull(ocoUserPassword);
        	
        	if(strUserName.length()<1||strUserPwd.length()<1){
        		
        		mapObject.put("loginsuccessFlag", "N");
        		mapObject.put("alertMsg", "username or password empty");       		
        	}else if (ocoOTP.length()<1){
        		mapObject.put("loginsuccessFlag", "N");
        		mapObject.put("alertMsg", "OTP empty");
        		
        	}else {	                
        		vidalwirerestvo.setUsername(strUserName);
        		vidalwirerestvo.setPassword(strUserPwd);
        		vidalwirerestvo.setOtpNumber(ocoOTP);
        		vidalwirerestvo.setPolicyGroupSeqID(ocoPolicyGrpSeqId);

        		
        		
            WebServiceManager webServiceManager=this.getWebServiceManagerObject();
            
            VidalWireRestVO  returnVidalWireRestVO= webServiceManager.getLoginRegisterOTP(vidalwirerestvo);
            
            mapObject.put("alertMsg", returnVidalWireRestVO.getPwdAlertMsg());
        	mapObject.put("loginsuccessFlag",returnVidalWireRestVO.getLoginStatus());//Y or N
        	//mapObject.put("policy_Group_seq_id", returnVidalWireRestVO.getPolicyGroupSeqID());
           			
            }
           
        } 
            
         catch (TTKException tte) {	
        	 tte.printStackTrace();        	
        	 mapObject.put("loginsuccessFlag", "N");
 	        mapObject.put("alertMsg", "Unknown Error1");
        	
            
        }catch (Exception e) {
        	e.printStackTrace();
        	 mapObject.put("loginsuccessFlag", "N");
	        mapObject.put("alertMsg", "Unknown Error2");	            
		}
       
        arra.put(mapObject);
     
 
		return arra.toString();
        
    } 
	
	
	@POST
	    @Path("/loginService")
	    @Produces(MediaType.APPLICATION_JSON)
	    @Consumes(MediaType.APPLICATION_JSON)
	    public String getLoginData(@HeaderParam("ocoUserName")String ocoUserName,@HeaderParam("ocoUserPassword")String ocoUserPassword ){
	

		VidalWireRestVO vidalwirerestvo=new VidalWireRestVO();
		AuthenticationVO authenticationVO=new AuthenticationVO();
	       JSONArray arra = new JSONArray();	        
	        LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
	        try {
	 	        
	        	String strUserName=TTKCommon.checkNull(ocoUserName);
	        	String strUserPwd=TTKCommon.checkNull(ocoUserPassword);
	        	if(strUserName.length()<1||strUserPwd.length()<1){
	        		
	        		mapObject.put("loginsuccessFlag", "N");
	        		mapObject.put("alertMsg", "username or password empty");       		
	        	}else
	            {	                
	        		vidalwirerestvo.setUsername(strUserName);
	        		vidalwirerestvo.setPassword(strUserPwd);
	        		
	            WebServiceManager webServiceManager=this.getWebServiceManagerObject();
	            
	            VidalWireRestVO  returnVidalWireRestVO= webServiceManager.getLoginService(vidalwirerestvo);
	            
	            mapObject.put("alertMsg", returnVidalWireRestVO.getPwdAlertMsg());
	        	mapObject.put("loginsuccessFlag",returnVidalWireRestVO.getLoginStatus());//Y or N
	        	mapObject.put("policy_Group_seq_id", returnVidalWireRestVO.getPolicyGroupSeqID());
	        	mapObject.put("policy_number", returnVidalWireRestVO.getPolicyNo());
	        	mapObject.put("group_id", returnVidalWireRestVO.getGroupID());

	           			
	            }
	           
	        } 
	            
	         catch (TTKException tte) {	tte.printStackTrace();        	
		     	tte.printStackTrace();
	        	mapObject.put("loginsuccessFlag", "N");
	        	mapObject.put("alertMsg", "Unknown Error1");	
	            
	        }catch (Exception e) {
	        	e.printStackTrace();
	        	 mapObject.put("loginsuccessFlag", "N");
		        mapObject.put("alertMsg", "Unknown Error2");	            
			}
	       
	        arra.put(mapObject);
	     
    		return arra.toString();
	        
	    } 
	
	@POST
    @Path("/forgetPwdLoginService")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getForgotPwdLoginData(@HeaderParam("ocoUserName")String ocoUserName){
		
        JSONArray arra = new JSONArray();	     
        VidalWireRestVO vidalwirerestvo=new VidalWireRestVO();
        LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
       
        try {
 	        
        	String strUserName=TTKCommon.checkNull(ocoUserName);
        	
        	if(strUserName.trim().length()==0){
        		
        		mapObject.put("loginsuccessFlag", "F");
        		mapObject.put("alertMsg", "please enter username");       		
        	}else
            {	                
            WebServiceManager webServiceManager=this.getWebServiceManagerObject();
        
    		vidalwirerestvo.setUsername(strUserName);
            VidalWireRestVO  returnVidalWireRestVO= webServiceManager.getLoginForgetPwdService(vidalwirerestvo);

        
            mapObject.put("alertMsg", returnVidalWireRestVO.getPwdAlertMsg());
        	mapObject.put("loginsuccessFlag",returnVidalWireRestVO.getLoginStatus());//Y or N
           
            }
            
        } catch (TTKException tte) {	        	
        	
        	tte.printStackTrace();
        	mapObject.put("loginsuccessFlag", "F");
	        mapObject.put("alertMsg", "Unknown Error1");	
            
        }catch (Exception e) {
        	e.printStackTrace();
        	 mapObject.put("loginsuccessFlag", "F");
	        mapObject.put("alertMsg", "Unknown Error2");	            
		}
       
        arra.put(mapObject);
		return arra.toString();
        
    }
	///////////////////////////////////////////////////Preauth/PreApproval Service//////////////////////////////////////////////////////
	
	
	 @POST
	    @Path("/selectEnrollDetail")
	    @Produces(MediaType.APPLICATION_JSON)
	    @Consumes(MediaType.APPLICATION_JSON)
	    public String getSelectEnrollDetail(@HeaderParam("ocoPolicyGroupSeq")Long ocoPolicyGroupSeq){
		

			OnlineIntimationVO onlineIntimationVO = new OnlineIntimationVO();
	        JSONArray arra = new JSONArray();	
	        LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
	        LinkedHashMap<String, Object> mapObject1 = new LinkedHashMap<String, Object>();
	        ArrayList alResult =new ArrayList();
	        try {
	 	        
	        	Long PolicyGroupSeq = ocoPolicyGroupSeq;
	        	if(PolicyGroupSeq.SIZE<1){
	        		
	        		mapObject.put("enrollFlag", "N");
	        		mapObject.put("alertMsg", "please enter PolicyGroupid");       		
	        	}else
	            {	
	        	
	        	onlineIntimationVO.setPolicyGrpSeqID(PolicyGroupSeq);
	            WebServiceManager webServiceManager=this.getWebServiceManagerObject();
	            alResult = webServiceManager.getDependentList(onlineIntimationVO);
	            JSONArray arra1 = new JSONArray(alResult);
	            
	    	     if(arra1 != null && arra1.length() >0)
				{
	    	    	mapObject1.put("result", arra1.get(0));
	    	    	 mapObject.put("alertMsg",(String) arra1.get(1));
	  	            mapObject.put("enrollFlag", (String) arra1.get(2));
	  	           
				
				}else{
	               	
	               	mapObject.put("enrollFlag", "N");
	           		mapObject.put("alertMsg", "No Data Found");
	               }
	            }
	            
	        } catch (TTKException tte) {	        	
	        	
	        	try{
	        		String errorMsg="no data found";
	                 mapObject.put("enrollFlag", "N");
		             mapObject.put("alertMsg",errorMsg);
		        		
		            }catch(Exception ie) {
	            	ie.printStackTrace();
	            	mapObject.put("enrollFlag", "N");
		        	mapObject.put("alertMsg", "Unknown Error1");	
	            	 }
	            
	        }catch (Exception e) {
	        	
	        	e.printStackTrace();
	        	 mapObject.put("enrollFlag", "N");
		        mapObject.put("alertMsg", "Unknown Error2");	            
		   }
	       
	        arra.put(mapObject);
	        arra.put(mapObject1);
			return arra.toString();
	        
	    }
	 
	 
	 @POST
	    @Path("/selectPreApprovalList")
	    @Produces(MediaType.APPLICATION_JSON)
	    @Consumes(MediaType.APPLICATION_JSON)
	    public String getSelectPreApprovalList(@HeaderParam("ocoMemSeq")Long ocoMemSeq){
			

			OnlineIntimationVO onlineIntimationVO = new OnlineIntimationVO();
	        JSONArray arra = new JSONArray();	
	        LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
	        LinkedHashMap<String, Object> mapObject1 = new LinkedHashMap<String, Object>();
	        ArrayList alResult =new ArrayList();
	        try {
	 	        
	        	Long memberSeq = ocoMemSeq;
	        	if(ocoMemSeq.SIZE<1){
	        		
	        		mapObject.put("preAuthFlag", "N");
	        		mapObject.put("alertMsg", "please enter Member Seq ID");       		
	        	}else
	            {	
	        	
	        	onlineIntimationVO.setMemberSeqID(memberSeq);
	            WebServiceManager webServiceManager=this.getWebServiceManagerObject();
	            alResult = webServiceManager.getPreAuthList(onlineIntimationVO);
	            JSONArray arra1 = new JSONArray(alResult);
	            
	    	     if(arra1 != null && arra1.length() >0)
				{
					//for(int i=0;i<arra1.length();i++){
						mapObject.put("preAuthFlag", "Y");
			            mapObject1.put("result", arra1.get(0));
			            mapObject.put("alertMsg","search successfully");
			          //  arra.put(mapObject1).toString();
					//}
				
				}else{
	               	
	               	mapObject.put("preAuthFlag", "N");
	           		mapObject.put("alertMsg", "No Data Found");
	               }
	            }
	            
	        } catch (TTKException tte) {	        	
	        	
	        	try{
	        		String errorMsg="no data found";
	                 mapObject.put("preAuthFlag", "N");
		             mapObject.put("alertMsg",errorMsg);
		        		
		            }catch(Exception ie) {
	            	ie.printStackTrace();
	            	mapObject.put("preAuthFlag", "N");
		        	mapObject.put("alertMsg", "Unknown Error1");	
	            	 }
	            
	        }catch (Exception e) {
	        	
	        	e.printStackTrace();
	        	 mapObject.put("preAuthFlag", "N");
		        mapObject.put("alertMsg", "Unknown Error2");	            
		   }
	       
	        arra.put(mapObject);
	         arra.put(mapObject1);
			return arra.toString();
	        
	    }
	 
	 @POST
	    @Path("/selectPreApprovalDetail")
	    @Produces(MediaType.APPLICATION_JSON)
	    @Consumes(MediaType.APPLICATION_JSON)
	    public String getPreApprovalDetail(@HeaderParam("ocoPreAuthseqid")Long ocoPreAuthseqid){
			

		 VidalClaimWireRestVO vidalClaimWireRestVO = new VidalClaimWireRestVO();
	        JSONArray arra = new JSONArray();	
	        LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
	        LinkedHashMap<String, Object> mapObject1 = new LinkedHashMap<String, Object>();
	        ArrayList alResult =new ArrayList();
	        try {
	 	        
	        	Long ocoPreAuthseq = ocoPreAuthseqid;
	        	if(ocoPreAuthseqid.SIZE<1){
	        		
	        		mapObject.put("preAuthFlag", "N");
	        		mapObject.put("alertMsg", "please enter preauth Seq ID");       		
	        	}else
	            {	
	        	
	        		vidalClaimWireRestVO.setPreAuthSeqID(ocoPreAuthseq);	
	            WebServiceManager webServiceManager=this.getWebServiceManagerObject();
	            alResult = webServiceManager.getPreAuthDetails(vidalClaimWireRestVO);
	            JSONArray arra1 = new JSONArray(alResult);
	            
	    	     if(alResult != null && alResult.size() >0)
				{
	    	    	     	    	 
	    	    	 mapObject1.put("GENERAL_DETAILS",alResult.get(0));
	    	    	 mapObject1.put("DENIAL_DETAILS", alResult.get(1));
	    	    	 mapObject1.put("ACTIVITY_DETAILS", alResult.get(2));
	    	    	 String Status = (String) alResult.get(3);String PreauthFlag= (String) alResult.get(4);
	    	    	
	    	    	 mapObject.put("preAuthFlag", PreauthFlag);
					 mapObject.put("alertMsg",Status);
	    	    	 
					//}
				
				}else{
	               	
	               	mapObject.put("preAuthFlag", "N");
	           		mapObject.put("alertMsg", "No Data Found");
	               }
	            }
	            
	        } catch (TTKException tte) {	        	
	        	
	        	try{
	        		String errorMsg="no data found";
	                 mapObject.put("preAuthFlag", "N");
		             mapObject.put("alertMsg",errorMsg);
		        		
		            }catch(Exception ie) {
	            	ie.printStackTrace();
	            	mapObject.put("preAuthFlag", "N");
		        	mapObject.put("alertMsg", "Unknown Error1");	
	            	 }
	            
	        }catch (Exception e) {
	        	
	        	e.printStackTrace();
	        	 mapObject.put("preAuthFlag", "N");
		        mapObject.put("alertMsg", "Unknown Error2");	            
		   }
	       
	        arra.put(mapObject1);
	        arra.put(mapObject);
			return arra.toString();
	        
	    }
	 
	 
	 ///////////////////////////////////////////Preauth/PreApproval Service//////////////////////////////////////////////////
	
	//////////////////////////////////////////////////My Policy Start///////////////////////////////////////////////
	
	 @POST
	    @Path("/selectPolicyDetail")
	    @Produces(MediaType.APPLICATION_JSON)
	    @Consumes(MediaType.APPLICATION_JSON)
	    public String getPolicyDetail(@HeaderParam("ocoPolicyGrpseqid")Long ocoPolicyGrpseqid){
			

		 VidalClaimWireRestVO vidalClaimWireRestVO = new VidalClaimWireRestVO();
	        JSONArray arra = new JSONArray();	
	        LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
	        LinkedHashMap<String, Object> mapObject1 = new LinkedHashMap<String, Object>();
	        ArrayList alResult =new ArrayList();
	        try {
	 	        
	        	Long ocoPreAuthseq = ocoPolicyGrpseqid;
	        	if(ocoPolicyGrpseqid.SIZE<1){
	        		
	        		mapObject.put("policyFlag", "N");
	        		mapObject.put("alertMsg", "please enter preauth Seq ID");       		
	        	}else
	            {	
	            WebServiceManager webServiceManager=this.getWebServiceManagerObject();
	            alResult = webServiceManager.getPolicyDetails(ocoPolicyGrpseqid);
	            JSONArray arra1 = new JSONArray(alResult);
	            
	    	     if(alResult != null && alResult.size() >0)
				{
					
			            mapObject1.put("POLICY_DETAILS", alResult.get(0));
			            mapObject1.put("DEPENDENT_DETAILS", alResult.get(1));
			            mapObject1.put("BENEFIT_DETAILS", alResult.get(2));
			            String Status = (String) alResult.get(3);String PolicyFlag= (String) alResult.get(4);
		    	    	 mapObject.put("PolicyFlag", PolicyFlag);
						 mapObject.put("alertMsg",Status);
			            
			         // arra.put(mapObject1).toString();
				//	}
				
				}else{
	               	
	               	mapObject.put("policyFlag", "N");
	           		mapObject.put("alertMsg", "No Data Found");
	               }
	            }
	            
	        } catch (TTKException tte) {	        	
	        	
	        	try{
	        		String errorMsg="no data found";
	                 mapObject.put("policyFlag", "N");
		             mapObject.put("alertMsg",errorMsg);
		        		
		            }catch(Exception ie) {
	            	ie.printStackTrace();
	            	mapObject.put("policyFlag", "N");
		        	mapObject.put("alertMsg", "Unknown Error1");	
	            	 }
	            
	        }catch (Exception e) {
	        	
	        	e.printStackTrace();
	        	 mapObject.put("policyFlag", "N");
		        mapObject.put("alertMsg", "Unknown Error2");	            
		   }
	       
	        arra.put(mapObject1);
	        arra.put(mapObject);
			return arra.toString();
	        
	    }
	 
	 
	 @Path("/policyTOBPdf")
	 @POST
	 @Produces(MediaType.APPLICATION_JSON)
	 @Consumes(MediaType.APPLICATION_JSON)
	 public Response policyTOBPdf(@HeaderParam("ocoPolicyGrpseqId")Long ocoPolicyGrpseqId){
	 	

	 	ResponseBuilder response = null ;
	 	try {
	 		
	 		WebServiceManager webServiceManager=this.getWebServiceManagerObject();
	 	    byte[] bytesArray=webServiceManager.getPolicyTOBpdf(ocoPolicyGrpseqId);//new byte[fis.available()];
	 	   
	 	    response = Response.ok(Base64.encodeBase64String(bytesArray));
	 	   
	 		
	 		}catch (Exception e) {
	 	e.printStackTrace();
	 	 
	 }
	 return response.build();
	 }
	 
	 //////////////////////////////////////////////////My Policy End///////////////////////////////////////////////
	 
	 //////////////////////////////////////////////////Health Record and Claims Start///////////////////////////////////////////////
	 
	 @POST
	    @Path("/selectClaimsList")
	    @Produces(MediaType.APPLICATION_JSON)
	    @Consumes(MediaType.APPLICATION_JSON)
	    public String getSelectClaimsList(@HeaderParam("ocoMemSeq")Long ocoMemSeq){
			
		 
		 OnlineIntimationVO onlineIntimationVO = new OnlineIntimationVO();
	        JSONArray arra = new JSONArray();	
	        LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
	        LinkedHashMap<String, Object> mapObject1 = new LinkedHashMap<String, Object>();
	        ArrayList alResult =new ArrayList();
	        try {
	 	        
	        	Long memberSeq = ocoMemSeq;
	        	if(ocoMemSeq.SIZE<1){
	        		
	        		mapObject.put("claimsFlag", "N");
	        		mapObject.put("alertMsg", "please enter Member Seq ID");       		
	        	}else
	            {	
	        	
	        	onlineIntimationVO.setMemberSeqID(memberSeq);
	            WebServiceManager webServiceManager=this.getWebServiceManagerObject();
	            alResult = webServiceManager.getClaimList(onlineIntimationVO);
	            JSONArray arra1 = new JSONArray(alResult);
	         
	    	     if(arra1 != null && arra1.length() >0)
				{
					//for(int i=0;i<arra1.length();i++){
					
						mapObject.put("claimsFlag", "Y");
			            mapObject1.put("result", arra1.get(0));
			            mapObject.put("alertMsg","search successfully");
			        //  arra.put(mapObject1).toString();
				//	}
				
				}else{
	               	
	               	mapObject.put("claimsFlag", "N");
	           		mapObject.put("alertMsg", "No Data Found");
	               }
	            }
	            
	        } catch (TTKException tte) {	        	
	        	
	        	try{
	        		String errorMsg="no data found";
	                 mapObject.put("claimsFlag", "N");
		             mapObject.put("alertMsg",errorMsg);
		        		
		            }catch(Exception ie) {
	            	ie.printStackTrace();
	            	mapObject.put("claimsFlag", "N");
		        	mapObject.put("alertMsg", "Unknown Error1");	
	            	 }
	            
	        }catch (Exception e) {
	        	
	        	e.printStackTrace();
	        	 mapObject.put("claimsFlag", "N");
		        mapObject.put("alertMsg", "Unknown Error2");	            
		   }
	       
	      
	        arra.put(mapObject);
	        arra.put(mapObject1);
	        
			return arra.toString();
	        
	    }
	 
	 @POST
	    @Path("/selectClaimsDetail")
	    @Produces(MediaType.APPLICATION_JSON)
	    @Consumes(MediaType.APPLICATION_JSON)
	    public String getClaimsDetail(@HeaderParam("ocoClaimseqid")Long ocoClaimseqid){
		
		 VidalClaimWireRestVO vidalClaimWireRestVO = new VidalClaimWireRestVO();
	        JSONArray arra = new JSONArray();	
	        LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
	        LinkedHashMap<String, Object> mapObject1 = new LinkedHashMap<String, Object>();
	        ArrayList alResult =new ArrayList();
	        try {
	 	        
	        	Long ocoClmeqid = ocoClaimseqid;
	        	if(ocoClaimseqid.SIZE<1){
	        		
	        		mapObject.put("claimsFlag", "N");
	        		mapObject.put("alertMsg", "please enter preauth Seq ID");       		
	        	}else
	            {	
	        	
	        		vidalClaimWireRestVO.setPreAuthSeqID(ocoClmeqid);	
	            WebServiceManager webServiceManager=this.getWebServiceManagerObject();
	            alResult = webServiceManager.getClaimDetails(vidalClaimWireRestVO);
	            JSONArray arra1 = new JSONArray(alResult);
	            
	    	     if(alResult != null && alResult.size() >0)
				{
					
	    	    	 mapObject1.put("GENERAL_DETAILS",alResult.get(0));
	    	    	 mapObject1.put("DENIAL_DETAILS", alResult.get(1));
	    	    	 mapObject1.put("ACTIVITY_DETAILS", alResult.get(2));
	    	    	 mapObject1.put("SHORTFALL_QUESTION", alResult.get(3));
	    	    	 String Status = (String) alResult.get(4);String PreauthFlag= (String) alResult.get(5);
	    	    	 
	    	    	 mapObject.put("claimsFlag", PreauthFlag);
					 mapObject.put("alertMsg",Status);
	    	    	 
					//}
				
				}else{
	               	
	               	mapObject.put("claimsFlag", "N");
	           		mapObject.put("alertMsg", "No Data Found");
	               }
	            }
	            
	        } catch (TTKException tte) {	        	
	        	
	        	try{
	        		String errorMsg="no data found";
	                 mapObject.put("claimsFlag", "N");
		             mapObject.put("alertMsg",errorMsg);
		        		
		            }catch(Exception ie) {
	            	ie.printStackTrace();
	            	mapObject.put("claimsFlag", "N");
		        	mapObject.put("alertMsg", "Unknown Error1");	
	            	 }
	            
	        }catch (Exception e) {
	        	
	        	e.printStackTrace();
	        	 mapObject.put("claimsFlag", "N");
		        mapObject.put("alertMsg", "Unknown Error2");	            
		   }
	       
	        arra.put(mapObject1);
	        arra.put(mapObject);
			return arra.toString();
	        
	    }
	 

	 //////////////////////////////////////////////////Health Record and Claims Start///////////////////////////////////////////////
	 
	 ///////////////////////////////////////////////////My Profile//////////////////////////////////////////////////////////////
	
	 
	 @POST
	    @Path("/myprofile")
	    @Produces(MediaType.APPLICATION_JSON)
	    @Consumes(MediaType.APPLICATION_JSON)
	    public String getMyProfile(@HeaderParam("ocoPolicyGroupSeq")Long ocoPolicyGroupSeq){
		

			OnlineIntimationVO onlineIntimationVO = new OnlineIntimationVO();
	        JSONArray arra = new JSONArray();	
	        LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
	        LinkedHashMap<String, Object> mapObject1 = new LinkedHashMap<String, Object>();
	        ArrayList alResult =new ArrayList();
	        try {
	 	        
	        	Long PolicyGroupSeq = ocoPolicyGroupSeq;
	        	if(PolicyGroupSeq.SIZE<1){
	        		
	        		mapObject.put("myprofileFlag", "N");
	        		mapObject.put("alertMsg", "please enter PolicyGroupid");       		
	        	}else
	            {	
	        	
	        	onlineIntimationVO.setPolicyGrpSeqID(PolicyGroupSeq);
	            WebServiceManager webServiceManager=this.getWebServiceManagerObject();
	            alResult = webServiceManager.getMyProfile(onlineIntimationVO);
	            JSONArray arra1 = new JSONArray(alResult);
	            
	    	     if(arra1 != null && arra1.length() >0)
				{
					for(int i=0;i<arra1.length();i++){
						mapObject.put("myprofileFlag", "Y");
						
			            mapObject1.put("result", arra1.get(i));
			            mapObject.put("alertMsg","search successfully");
			          //arra.put(mapObject1).toString();
					}
				
				}else{
	               	
	               	mapObject.put("myprofileFlag", "N");
	           		mapObject.put("alertMsg", "No Data Found");
	               }
	            }
	            
	        } catch (TTKException tte) {	        	
	        	
	        	try{
	        		String errorMsg="no data found";
	                 mapObject.put("myprofileFlag", "N");
		             mapObject.put("alertMsg",errorMsg);
		        		
		            }catch(Exception ie) {
	            	ie.printStackTrace();
	            	mapObject.put("myprofileFlag", "N");
		        	mapObject.put("alertMsg", "Unknown Error1");	
	            	 }
	            
	        }catch (Exception e) {
	        	
	        	e.printStackTrace();
	        	 mapObject.put("myprofileFlag", "N");
		        mapObject.put("alertMsg", "Unknown Error2");	            
		   }
	       
	        arra.put(mapObject);
	        arra.put(mapObject1);
			return arra.toString();
	        
	    }
	 
	 
	 @POST
	 @Path("/updateMyProfile")
	 @Produces(MediaType.APPLICATION_JSON)
	 @Consumes(MediaType.APPLICATION_JSON)
	  public String updateMyProfile(@HeaderParam("ocoAddressSeqId")Long ocoAddressSeqId,@HeaderParam("ocoPolicyGroupSeq")Long ocoPolicyGroupSeq,@HeaderParam("ocoMemberSeq")Long ocoMemberSeq,@HeaderParam("ocoPassword")String ocoPassword,@HeaderParam("ocoDateOfBirth")String ocoDateOfBirth,@HeaderParam("ocoGenderTypeID")String ocoGenderTypeID,
	    		@HeaderParam("ocoMobileNo")String ocoMobileNo,@HeaderParam("ocoEmailId")String ocoEmailId,@HeaderParam("ocoAddress1")String ocoAddress1,
	    		@HeaderParam("ocoAddress2")String ocoAddress2,@HeaderParam("ocoAddress3")String ocoAddress3,@HeaderParam("ocoPinCode")String ocoPinCode,
	    		@HeaderParam("ocoCountryTypeId")String ocoCountryTypeId,@HeaderParam("ocoStateId")String ocoStateId,@HeaderParam("ocoAreaId")String ocoAreaId){
	 	OnlineIntimationVO onlineIntimationVO = new OnlineIntimationVO();
		

	 	JSONArray arra = new JSONArray();	
	     LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
	     LinkedHashMap<String, Object> mapObject1 = new LinkedHashMap<String, Object>();
	     ArrayList alResult =new ArrayList();
	 	//String startDate=TTKCommon.checkNull(ocoStartDate);

	     try {
	 	   
	     		if(ocoPolicyGroupSeq.SIZE<1 || ocoAddressSeqId.SIZE<1 || ocoMemberSeq.SIZE<1 ) {
	         		mapObject.put("myprofileFlag", "N");
	         		mapObject.put("alertMsg", "please enter PolicyGroupSeq/ AddressSeqId /MemberSeqId");  
	     		}else if(ocoDateOfBirth.toString().length()<1 ){
	     		
	     			mapObject.put("myprofileFlag", "N");
	         		mapObject.put("alertMsg", "please enter Date Of Birth");  
	         	}else if(ocoGenderTypeID.length()<1 ){
	     			mapObject.put("myprofileFlag", "N");
	         		mapObject.put("alertMsg", "please enter GenderGeneralTypeId");  
	         	}else if(ocoCountryTypeId.length()<1 ){
		     			mapObject.put("myprofileFlag", "N");
		         		mapObject.put("alertMsg", "please Select Country");  
	         	}else if(ocoStateId.length()<1 ){
	     			mapObject.put("myprofileFlag", "N");
	         		mapObject.put("alertMsg", "please Select State");  
	         	}else
	         {	
				onlineIntimationVO.setAddressSeqID(ocoAddressSeqId);
				onlineIntimationVO.setPolicyGrpSeqID(ocoPolicyGroupSeq);
				onlineIntimationVO.setMemberSeqID(ocoMemberSeq);
	  			onlineIntimationVO.setPassword(ocoPassword);
	  		//	onlineIntimationVO.setGenderDesc(ocoGenderTypeID);
	  			onlineIntimationVO.setDateOfBirth(ocoDateOfBirth);

	  			
	  			onlineIntimationVO.setMobileNbr(ocoMobileNo);
	  			onlineIntimationVO.setEmailID(ocoEmailId);
	  			
	  			onlineIntimationVO.setAddress1(ocoAddress1);
	  		//	onlineIntimationVO.setAddress2(ocoAddress2);
	  		//	onlineIntimationVO.setAddress3(ocoAddress3);
	  			onlineIntimationVO.setCityID(ocoAreaId);
	  			onlineIntimationVO.setStateId(ocoStateId);
	  			onlineIntimationVO.setCountryId(ocoCountryTypeId);
	  			onlineIntimationVO.setPincode(ocoPinCode);
	  			onlineIntimationVO.setGenderGeneralId(ocoGenderTypeID);
	  		//	onlineIntimationVO.setPolicyGrpSeqID(ocoPolicyGroupSeq);

	  		

	     	
	     	
	         WebServiceManager webServiceManager=this.getWebServiceManagerObject();
	         ArrayList arra1 = webServiceManager.updateMyprofile(onlineIntimationVO);
	         int result = (int) arra1.get(0);
	         if( result >0)
	 		{
	 				mapObject.put("myprofileFlag", "Y");
	 		        mapObject.put("alertMsg", "Updated Successfully");	
	 	          //  mapObject1.put("result", arra1.get(i));
	 	          //arra.put(mapObject1).toString();
	 		
	 		}
	         
	     }//end else
	     		
	      
	     }  catch (TTKException tte) {	        	
	     	
	     	try{
	     		String errorMsg="Data Not inserted";
	              mapObject.put("myprofileFlag", "N");
	              mapObject.put("alertMsg",errorMsg);
	         		
	             }catch(Exception ie) {
	         	ie.printStackTrace();
	         	mapObject.put("myprofileFlag", "N");
	         	mapObject.put("alertMsg", "Unknown Error1");	
	         	 }
	         
	     }catch (Exception e) {
	     	
	     	e.printStackTrace();
	     	 mapObject.put("myprofileFlag", "N");
	         mapObject.put("alertMsg", "Unknown Error2");	            
	    }
	    
	     arra.put(mapObject);
	   //  arra.put(mapObject1);
	 	return arra.toString();
	     
	 }
	 
	/*    public String getSaveMyProfile(@HeaderParam("ocoPassword")String ocoPassword,@HeaderParam("ocoDateOfBirth")String ocoDateOfBirth,@HeaderParam("ocoGender")String ocoGender,
	    		@HeaderParam("ocoMobileNo")String ocoMobileNo,@HeaderParam("ocoEmailId")String ocoEmailId,@HeaderParam("ocoAddress")String ocoAddress){
			*/
	 
	 //////////////////////////////////////////////////////My profile End///////////////////////////////////
	 
	 
 ///////////////////////////////////////////////////HealthTip Image//////////////////////////////////////////////////////////////
	
	 
	 @POST
	    @Path("/healthTips")
	    @Produces(MediaType.APPLICATION_JSON)
	    @Consumes(MediaType.APPLICATION_JSON)
	    public String getHealthTips(@HeaderParam("ocoStrDate")String ocoStrDate){
		 

			OnlineIntimationVO onlineIntimationVO = new OnlineIntimationVO();
	    	 byte[] data=null;
	        try {
	 	        
	        	String ocoStrDateToday = ocoStrDate;
	        		
	        	
	        	onlineIntimationVO.setDtAdmissionDate(ocoStrDateToday);
	            WebServiceManager webServiceManager=this.getWebServiceManagerObject();
	            data = webServiceManager.getHealthTips(onlineIntimationVO);
	           
	            
	        }catch (Exception e) {
	        	
	        	e.printStackTrace();
		   }
	        return (data != null) ? Base64.encodeBase64String(data) : "alertMsg :no data found";
	    }
	 
	 //////////////////////////////////////////////////////HealthTip Image//////////////////////////////////
	 
///////////////////////////////////////////////////Ecard//////////////////////////////////////////////////////////////
		
	 

@Path("/ecardPdf")
@POST
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public Response getEcardpdf(@HeaderParam("ocoMemSeq")Long ocoMemSeq,@HeaderParam("ocoPolicyGrpseqId")Long ocoPolicyGrpseqId,@HeaderParam("ocoTemplateid")String ocoTemplateid){
	

	OnlineIntimationVO onlineIntimationVO = new OnlineIntimationVO();
	ResponseBuilder response = null ;
	String EmptyString = "No Data Found";

try {
		onlineIntimationVO.setMemberSeqID(ocoMemSeq);
		onlineIntimationVO.setTemplateType(ocoTemplateid);
		WebServiceManager webServiceManager=this.getWebServiceManagerObject();
		   
	    byte[] data=MobileEcard.getMobileAppQatar(ocoMemSeq, ocoPolicyGrpseqId,ocoTemplateid);//new byte[fis.available()];
	     response = Response.ok(Base64.encodeBase64String(data));

		
		}catch (Exception e) {
	e.printStackTrace();
	 
}
return response.build();
}


@Path("/download")
@GET
@Produces({"application/pdf","text/plain","image/jpeg","application/xml","application/vnd.ms-excel"})
public Response getEcardpdf123(@HeaderParam("ocoMemSeq")Long ocoMemSeq,@HeaderParam("ocoPolicyGrpseqId")Long ocoPolicyGrpseqId,@HeaderParam("ocoTemplateid")String ocoTemplateid)
{
	//NOT IN USE CODE IS ONLY FOR REFERENCE DIRECT DOWNLOAD
	

	ResponseBuilder response = null ;
try{
	
   // File download = new File("D:/home/tipsint/jboss-as-7.1.3.Final/bin/shortfall/1110000003-02.pdf");
   // FileInputStream fis=new FileInputStream(download);
	ocoPolicyGrpseqId=17683595l;
	ocoTemplateid="General";
	
	ocoMemSeq=(long) 37587916;
    
    byte[] data=MobileEcard.getMobileAppQatar(ocoMemSeq, ocoPolicyGrpseqId,ocoTemplateid);//new byte[fis.available()];
  //  fis.read(data);
    
   // ByteArrayOutputStream arrayOutputStream=new ByteArrayOutputStream();
 
     response = Response.ok((Object)data);
    
    response.header("Content-Disposition", "attachment; filename=sample123.pdf");
    
   // if(fis!=null)fis.close();
   
}catch (Exception e) {
	e.printStackTrace();
} 
return response.build();
}
//////////////////////////////////////////////////////My profile End///////////////////////////////////




@Path("/ecardPdfimage")
@POST
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public String getEcardpdfimage(@HeaderParam("ocoMemSeq")Long ocoMemSeq,@HeaderParam("ocoPolicyGrpseqId")Long ocoPolicyGrpseqId,@HeaderParam("ocoTemplateid")String ocoTemplateid){
//NOT IN USE CODE IS ONLY FOR REFERENCE


OnlineIntimationVO onlineIntimationVO = new OnlineIntimationVO();
ResponseBuilder response = null ;
String strResult="";

try {
	onlineIntimationVO.setMemberSeqID(ocoMemSeq);
	onlineIntimationVO.setTemplateType(ocoTemplateid);
	WebServiceManager webServiceManager=this.getWebServiceManagerObject();
	   
 
	if(ocoMemSeq.SIZE>1 || ocoPolicyGrpseqId.SIZE>1 || ocoTemplateid.length()>1 ) {	
		
	  String pdfFileName=MobileEcard.getMobileAppQatarImage(ocoMemSeq, ocoPolicyGrpseqId,ocoTemplateid);//new byte[fis.available()];
      String strJpegFileName= MobileEcard.getJpegFileName(ocoMemSeq.toString(), pdfFileName);
      strResult=MobileEcard.getImageByteData(strJpegFileName);
	}
   else{strResult="No Data Found";	}

	
	}catch (Exception e) {
e.printStackTrace();
}
return strResult;
}



@Path("/formdownload")
@POST
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public Response formdownload(@HeaderParam("formName")String formName)
{
	

	ResponseBuilder response = null ;
	String filaPath = "";
try{
	if(formName.equalsIgnoreCase("CLAIMS")){
	 filaPath=TTKPropertiesReader.getPropertyValue("GlobalDownload")+"claimform.pdf";
	}else if(formName.equalsIgnoreCase("PREAPPROVAL")){
	 filaPath=TTKPropertiesReader.getPropertyValue("GlobalDownload")+"preapproval.pdf";
	}else if(formName.equalsIgnoreCase("SupportDocEng")){
	  filaPath=TTKPropertiesReader.getPropertyValue("GlobalDownload")+"AlKootMembershipGuideEng.pdf";
	}else if(formName.equalsIgnoreCase("SupportDocArabic")){
		  filaPath=TTKPropertiesReader.getPropertyValue("GlobalDownload")+"AlKootMembershipGuideArabic.pdf";
	}
	else if(formName.equalsIgnoreCase("PolicyExcEng")){
		  filaPath=TTKPropertiesReader.getPropertyValue("GlobalDownload")+"PolicyExclusionEng.pdf";
	}
	else if(formName.equalsIgnoreCase("PolicyExcArabic")){
		  filaPath=TTKPropertiesReader.getPropertyValue("GlobalDownload")+"PolicyExclusionArabic.pdf";
	}
	 File file = new File(filaPath);  
     response = Response.ok((Object) file);
     
     byte[] bytesArray = new byte[(int) file.length()];
     FileInputStream fis = new FileInputStream(file);
     fis.read(bytesArray); //read file into bytes[]
     fis.close();
    // response.header("Content-Disposition","attachment; filename=\"submissionform.pdf\"");  
     response = Response.ok(Base64.encodeBase64String(bytesArray));
   
}catch (Exception e) {
	e.printStackTrace();
} 
return response.build();
}

///////////////////////////////////////////////////FAQ//////////////////////////////////////////////////////////////

			
@POST
@Path("/frequentQuestion")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public String getFAQ(){
	
	

    JSONArray arra = new JSONArray();	
    LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
    LinkedHashMap<String, Object> mapObject1 = new LinkedHashMap<String, Object>();
    ArrayList alResult =new ArrayList();
    try {
	        
    	
        WebServiceManager webServiceManager=this.getWebServiceManagerObject();
        alResult = webServiceManager.getFAQ();
        JSONArray arra1 = new JSONArray(alResult);
        
	     if(arra1 != null && arra1.length() >0)
		{
			for(int i=0;i<arra1.length();i++){
				mapObject.put("faqFlag", "Y");
		        mapObject.put("alertMsg", "Search Successfully");	
	            mapObject1.put("result", arra1.get(i));
	          //arra.put(mapObject1).toString();
			}
		
		}
        
    } catch (TTKException tte) {	        	
    	
    	try{
    		String errorMsg="no data found";
             mapObject.put("faqFlag", "N");
             mapObject.put("alertMsg",errorMsg);
        		
            }catch(Exception ie) {
        	ie.printStackTrace();
        	mapObject.put("faqFlag", "N");
        	mapObject.put("alertMsg", "Unknown Error1");	
        	 }
        
    }catch (Exception e) {
    	
    	e.printStackTrace();
    	 mapObject.put("faqFlag", "N");
        mapObject.put("alertMsg", "Unknown Error2");	            
   }
   
    arra.put(mapObject);
    arra.put(mapObject1);
	return arra.toString();
    
}
//////////////////////////////////////////////////////My profile End///////////////////////////////////


///////////////////////////////////////////////////////Med Reminder////////////////////////////////////////



@POST
   @Path("/selectMedReminderDetails")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public String getSelectMedReminderDetails(@HeaderParam("ocoPolicyGroupSeq")Long ocoPolicyGroupSeq){
	

		OnlineIntimationVO onlineIntimationVO = new OnlineIntimationVO();
       JSONArray arra = new JSONArray();	
       LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
       LinkedHashMap<String, Object> mapObject1 = new LinkedHashMap<String, Object>();
       ArrayList alResult =new ArrayList();
       ArrayList alIntervalType =new ArrayList();
       ArrayList alMedTimeType =new ArrayList();

       
       try {
	        
       	Long PolicyGroupSeq = ocoPolicyGroupSeq;
       	if(PolicyGroupSeq.SIZE<1){
       		
       		mapObject.put("medFlag", "N");
       		mapObject.put("alertMsg", "please enter PolicyGroupid");       		
       	}else
           {	
       	
           onlineIntimationVO.setPolicyGrpSeqID(PolicyGroupSeq);
           WebServiceManager webServiceManager=this.getWebServiceManagerObject();
           alResult = webServiceManager.getDependentList(onlineIntimationVO);
           alIntervalType = webServiceManager.getIntervalTypeList();
           alMedTimeType = webServiceManager.getMedTimeList();

           JSONArray arra1 = new JSONArray(alResult);
          
           
   	     if(arra1 != null && arra1.length() >0)
			{
   	    	 		mapObject1.put("result",  arra1.get(0));
				    mapObject1.put("intervalType", alIntervalType.get(0));
		            mapObject1.put("Med_Time_Type", alMedTimeType.get(0));
		            mapObject.put("alertMsg", (String) arra1.get(1));
		            mapObject.put("medFlag", (String)arra1.get(2));
				
			
			}else{
              	
              	mapObject.put("medFlag", "N");
          		mapObject.put("alertMsg", "Dependent Data Found");
              }
           }
           
       } catch (TTKException tte) {	        	
       	
       	try{
       		String errorMsg="no data found";
                mapObject.put("medFlag", "N");
	             mapObject.put("alertMsg",errorMsg);
	        		
	            }catch(Exception ie) {
           	ie.printStackTrace();
           	mapObject.put("medFlag", "N");
	        	mapObject.put("alertMsg", "Unknown Error1");	
           	 }
           
       }catch (Exception e) {
       	
       	e.printStackTrace();
       	 mapObject.put("medFlag", "N");
	        mapObject.put("alertMsg", "Unknown Error2");	            
	   }
      
       arra.put(mapObject);
       arra.put(mapObject1);
		return arra.toString();
       
   }

@POST
@Path("/saveMedReminder")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public String saveMedReminder(@HeaderParam("ocoPolicyGroupSeq")Long ocoPolicyGroupSeq,@HeaderParam("ocoMemSeqid")Long ocoMemSeqid,@HeaderParam("ocoDrugName")String ocoDrugName,
@HeaderParam("ocoQuantity")String ocoQuantity,@HeaderParam("ocoTimesperDay")String ocoTimesperDay,@HeaderParam("ocoNoDays")String ocoNoDays,
@HeaderParam("ocoIntervalTypeId")String ocoIntervalTypeId,@HeaderParam("ocoMedTimeTypeId")String ocoMedTimeTypeId,@HeaderParam("ocoStartDate")String ocoStartDate){
	


	OnlineIntimationVO onlineIntimationVO = new OnlineIntimationVO();
    JSONArray arra = new JSONArray();	
    LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
    LinkedHashMap<String, Object> mapObject1 = new LinkedHashMap<String, Object>();
    ArrayList alResult =new ArrayList();
	//String startDate=TTKCommon.checkNull(ocoStartDate);

    try {
	        
    	/*SimpleDateFormat dateFormate =new SimpleDateFormat("dd-MM-yyyy");
    	Date date = dateFormate.parse(startDate);
    	if(!startDate.equals(date))
		{
			TTKException expTTK = new TTKException();
			expTTK.setMessage("java.text.ParseException");
			throw expTTK;
		}*/
    	
    	Long PolicyGroupSeq = ocoPolicyGroupSeq;
    	String strDrugName =ocoDrugName;
    	if(strDrugName.length()<1){
    		mapObject.put("medFlag", "N");
    		mapObject.put("alertMsg", "Please enter the Drug name");    
    	}else if(strDrugName.equalsIgnoreCase("null")){
    		mapObject.put("medFlag", "N");
	  		mapObject.put("alertMsg", "Input cannot be accepted as Null .Please Correct the same");  
	  		
		} 	else if(PolicyGroupSeq.SIZE<1||ocoMemSeqid.SIZE<1||ocoDrugName.length()<1||ocoStartDate.toString().length()<1||ocoQuantity.length()<1 ||ocoMedTimeTypeId.length()<1 ||ocoTimesperDay.length()<1 || ocoNoDays.length()<1) {
        		mapObject.put("medFlag", "N");
        		mapObject.put("alertMsg", "please enter PolicyGroupSeq,ocoMemSeqid,ocoDrugName,ocoStartDate,ocoQuantity,ocoMedTimeTypeId,ocoTimesperDay,ocoNoDays");  
		}
    		else
        {	
    	onlineIntimationVO.setPolicyGrpSeqID(PolicyGroupSeq);
    	onlineIntimationVO.setMemberSeqID(ocoMemSeqid);
    	onlineIntimationVO.setQuantity(ocoQuantity);
    	onlineIntimationVO.setDrugName(ocoDrugName);

    	onlineIntimationVO.setTimesPerDay(ocoTimesperDay);
    	onlineIntimationVO.setNoOfDays(ocoNoDays);
    	onlineIntimationVO.setIntervalTypeId(ocoIntervalTypeId);
    	onlineIntimationVO.setMedTimeTypeId(ocoMedTimeTypeId);
    	onlineIntimationVO.setMedStartDate(ocoStartDate);

    	
    	
        WebServiceManager webServiceManager=this.getWebServiceManagerObject();
       int result = webServiceManager.saveMedReminder(onlineIntimationVO);
        
	     if(result >0)
		{
				mapObject.put("medFlag", "Y");
	            mapObject.put("alertMsg","Data inserted successfully");
		
		}else{
           	
           	mapObject.put("medFlag", "N");
       		mapObject.put("alertMsg", "Not a Valid Data");
           }
        }
        
    } //end try
     
   /* catch (ParseException exp3) {
 	   exp3.printStackTrace(); 
 	  mapObject.put("status", "F");
          mapObject.put("Status", "Date format should be dd/MM/yyyy");
			
    }*/catch (TTKException tte) {	        	
    	
    	try{
    		String errorMsg="no data found";
             mapObject.put("medFlag", "N");
             mapObject.put("alertMsg",errorMsg);
        		
            }catch(Exception ie) {
        	ie.printStackTrace();
        	mapObject.put("medFlag", "N");
        	mapObject.put("alertMsg", "Unknown Error1");	
        	 }
        
    }catch (Exception e) {
    	
    	e.printStackTrace();
    	 mapObject.put("medFlag", "N");
        mapObject.put("alertMsg", "Unknown Error2");	            
   }
   
    arra.put(mapObject);
    arra.put(mapObject1);
	return arra.toString();
    
}

@POST
@Path("/selectMedReminder")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public String getSelectMedReminder(@HeaderParam("ocoRemSeqId")Long ocoRemSeqId){


	OnlineIntimationVO onlineIntimationVO = new OnlineIntimationVO();
    JSONArray arra = new JSONArray();	
    LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
    LinkedHashMap<String, Object> mapObject1 = new LinkedHashMap<String, Object>();
    ArrayList alResult =new ArrayList();
    try {
	        
    	Long RemSeqId = ocoRemSeqId;
    	if(RemSeqId.SIZE<1){
    		
    		mapObject.put("medFlag", "N");
    		mapObject.put("alertMsg", "please enter MemberSeqId");       		
    	}else
        {	
    	
    	onlineIntimationVO.setRem_seq_id(RemSeqId);
        WebServiceManager webServiceManager=this.getWebServiceManagerObject();
        alResult = webServiceManager.getMedReminder(onlineIntimationVO);
        JSONArray arra1 = new JSONArray(alResult);
        
	     if(arra1 != null && arra1.length() >0)
		{
	            mapObject1.put("result", arra1.get(0));
	            mapObject.put("medFlag", (String) arra1.get(1));
	            mapObject.put("alertMsg",(String) arra1.get(2));
	          //arra.put(mapObject1).toString();
		
		}else{
           	
           	mapObject.put("medFlag", "N");
       		mapObject.put("alertMsg", "No Data Found");
           }
        }
        
    } catch (TTKException tte) {	        	
    	
    	try{
    		String errorMsg="no data found";
             mapObject.put("medFlag", "N");
             mapObject.put("alertMsg",errorMsg);
        		
            }catch(Exception ie) {
        	ie.printStackTrace();
        	mapObject.put("medFlag", "N");
        	mapObject.put("alertMsg", "Unknown Error1");	
        	 }
        
    }catch (Exception e) {
    	
    	e.printStackTrace();
    	 mapObject.put("enrollFlag", "N");
        mapObject.put("alertMsg", "Unknown Error2");	            
   }
   
    arra.put(mapObject);
    arra.put(mapObject1);
	return arra.toString();
    
}

@POST
@Path("/deleteMedReminder")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public String getDeleteMedReminder(@HeaderParam("ocoRemSeqId")Long ocoRemSeqId){


	OnlineIntimationVO onlineIntimationVO = new OnlineIntimationVO();
    JSONArray arra = new JSONArray();	
    LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
    LinkedHashMap<String, Object> mapObject1 = new LinkedHashMap<String, Object>();
    ArrayList alResult =new ArrayList();
    try {
	        
    	Long RemSeqId = ocoRemSeqId;
    	if(RemSeqId.SIZE<1){
    		
    		mapObject.put("medFlag", "N");
    		mapObject.put("alertMsg", "please enter MemberSeqId");       		
    	}else
        {	
    	
    	onlineIntimationVO.setRem_seq_id(RemSeqId);
        WebServiceManager webServiceManager=this.getWebServiceManagerObject();
      
        int deleteYN = webServiceManager.deleteMedReminder(onlineIntimationVO);
        
            
	     if(deleteYN > 0)
		{
	            mapObject1.put("result", "1");
	            mapObject.put("medFlag", "Y");
	            mapObject.put("alertMsg","deleted successfully");
		
		}else{
           	
		    mapObject1.put("result", "0");
           	mapObject.put("medFlag", "N");
       		mapObject.put("alertMsg", "No Data Found");
           }
        }
        
    } catch (TTKException tte) {	        	
    	
    	try{
    		String errorMsg="no data found";
             mapObject.put("medFlag", "N");
             mapObject.put("alertMsg",errorMsg);
        		
            }catch(Exception ie) {
        	ie.printStackTrace();
        	mapObject.put("medFlag", "N");
        	mapObject.put("alertMsg", "Unknown Error1");	
        	 }
        
    }catch (Exception e) {
    	
    	e.printStackTrace();
    	 mapObject.put("enrollFlag", "N");
        mapObject.put("alertMsg", "Unknown Error2");	            
   }
   
    arra.put(mapObject);
    arra.put(mapObject1);
	return arra.toString();
    
}


@POST
@Path("/selectMedReminderHistory")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public String getSelectMedReminderHistory(@HeaderParam("ocoMemSeqId")Long ocoMemSeqId){
	


	OnlineIntimationVO onlineIntimationVO = new OnlineIntimationVO();
    JSONArray arra = new JSONArray();	
    LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
    LinkedHashMap<String, Object> mapObject1 = new LinkedHashMap<String, Object>();
    ArrayList alResult =new ArrayList();
    try {
	        
    	Long MemSeqId = ocoMemSeqId;
    	if(MemSeqId.SIZE<1){
    		
    		mapObject.put("medFlag", "N");
    		mapObject.put("alertMsg", "please enter Policy Group seq Id");       		
    	}else
        {	
    	
    	onlineIntimationVO.setMemberSeqID(MemSeqId);
        WebServiceManager webServiceManager=this.getWebServiceManagerObject();
        alResult = webServiceManager.getMedReminderHistory(onlineIntimationVO);
        JSONArray arra1 = new JSONArray(alResult);
        
	     if(arra1 != null && arra1.length() >0)
		{
	    	    mapObject1.put("result", arra1.get(0));
	            mapObject.put("alertMsg",(String) arra1.get(1));
	            mapObject.put("medFlag", (String) arra1.get(2));

		
		}else{
           	
           	mapObject.put("medFlag", "N");
       		mapObject.put("alertMsg", "No Data Found");
           }
        }
        
    } catch (TTKException tte) {	        	
    	
    	try{
    		String errorMsg="no data found";
             mapObject.put("medFlag", "N");
             mapObject.put("alertMsg",errorMsg);
        		
            }catch(Exception ie) {
        	ie.printStackTrace();
        	mapObject.put("medFlag", "N");
        	mapObject.put("alertMsg", "Unknown Error1");	
        	 }
        
    }catch (Exception e) {
    	
    	e.printStackTrace();
    	 mapObject.put("enrollFlag", "N");
        mapObject.put("alertMsg", "Unknown Error2");	            
   }
   
    arra.put(mapObject);
    arra.put(mapObject1);
	return arra.toString();
    
}

///////////////////////////////////////////////////////Med ReminderEnd////////////////////////////////////////


///////////////////////////////////////////////////Claim Submission pdf//////////////////////////////////////////////////////////////
			
			
@POST
@Path("/saveClaimSubmissionPdf")
//   @Consumes(MediaType.MULTIPART_FORM_DATA)  

@Consumes(MediaType.APPLICATION_OCTET_STREAM)  
@Produces(MediaType.APPLICATION_JSON)
public String saveClaimSubmissionPdf(@FormDataParam("file") InputStream uploadedInputStream,@HeaderParam("ocoMemseqId")Long ocoMemseqId,@HeaderParam("ocoInvoiceNo")String ocoInvoiceNo,
		                        @HeaderParam("ocoRequestedAmt")BigDecimal ocoRequestedAmt,@HeaderParam("ocoRequestedCurrency")String ocoRequestedCurrency,@HeaderParam("ocoEmailID")String ocoEmailID,@HeaderParam("ocoFileType")String ocoFileType){


	// String   fileLocation = "D://VireWebservices/claimForm" +ocoMemseqId+".pdf";  
	 OnlineIntimationVO onlineIntimationVO = new OnlineIntimationVO();
	 ArrayList alResult =new ArrayList();
     WebServiceManager webServiceManager;
     JSONArray arra = new JSONArray();	
     LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
     LinkedHashMap<String, Object> mapObject1 = new LinkedHashMap<String, Object>();
  try {  
         
	String strOcoInvoiceNo=TTKCommon.checkNull(ocoInvoiceNo.toLowerCase());
  
  	
  	if(strOcoInvoiceNo.length()<1){
  		
  		mapObject.put("loginsuccessFlag", "N");
  		mapObject.put("alertMsg", "username or password empty");    
  	}else{
	
		  if ((ocoFileType.equalsIgnoreCase("jpeg")|| (ocoFileType.equalsIgnoreCase("jpg"))|| (ocoFileType.equalsIgnoreCase("gif")) ||(ocoFileType.equalsIgnoreCase("png")
				  ||(ocoFileType.equalsIgnoreCase("pdf"))))){
		     int read = 0;  
		        byte[] bytes = new byte[1024];  
		     
		        bytes	=	org.apache.commons.io.IOUtils.toByteArray(uploadedInputStream);
		   
				webServiceManager = this.getWebServiceManagerObject();
		       onlineIntimationVO.setClaimSubmission(bytes);
		       onlineIntimationVO.setMemberSeqID(ocoMemseqId);
		       onlineIntimationVO.setInvoice_Number(ocoInvoiceNo);
		       onlineIntimationVO.setRequestedAmt(ocoRequestedAmt);
		       onlineIntimationVO.setReq_currency_type(ocoRequestedCurrency);
		       onlineIntimationVO.setEmailID(ocoEmailID);
		       onlineIntimationVO.setFileDataOInputStream(uploadedInputStream);
		       onlineIntimationVO.setFileType(ocoFileType);
		
		
		       alResult = webServiceManager.saveOnlineClaimSubmission(onlineIntimationVO);
		       if(alResult != null && alResult.size() >0)
				{
		       mapObject.put("alertMsg",(String) alResult.get(0));
		       mapObject.put("Batch_No",(String) alResult.get(1));
		       mapObject.put("claimSubFlag","Y");
		
				}
		       else{
		    	   mapObject.put("alertMsg","No data found");
		           mapObject.put("claimSubFlag","N");
		       }
		  }else{
			  mapObject.put("alertMsg","Please Upload File formats pdf/jpeg/jpg/gif/png");
		      mapObject.put("claimSubFlag","N");
		  }
  	}//else check null
  } catch (TTKException tte) {	        	
    	
    	try{
    		String errorMsg="Exception occured";
             mapObject.put("claimSubFlag", "N");
             mapObject.put("alertMsg",errorMsg);
        		
            }catch(Exception ie) {
        	ie.printStackTrace();
        	mapObject.put("claimSubFlag", "N");
        	mapObject.put("alertMsg", "Unknown Error1");	
        	 }
        
    }catch (Exception e) {
    	
    	e.printStackTrace();
    	 mapObject.put("claimSubFlag", "N");
        mapObject.put("alertMsg", "Unknown Error2");	            
   }
   
    arra.put(mapObject);
  //  arra.put(mapObject1);
	return arra.toString();
    
}
		


			
		/*	
			@POST
			@Path("/getClaimSubmissionPdf")
			
			@Produces(MediaType.APPLICATION_JSON)
			@Consumes(MediaType.APPLICATION_JSON)
			public Response getClaimSubmissionPdf(@HeaderParam("ocoPolicyGrpseqId")Long ocoPolicyGrpseqId){
			
				String   fileLocation = "D://VireWebservices/claimdoc" +ocoPolicyGrpseqId+".pdf";  
				String   fileLocation2 = "D://VireWebservices/claimdoc" +ocoPolicyGrpseqId+".pdf";  
				@FormDataParam("file2") InputStream uploadedInputStream2,
				OnlineIntimationVO onlineIntimationVO = new OnlineIntimationVO();
				 ArrayList alResult =new ArrayList();
			     WebServiceManager webServiceManager;
			     File file=null;
			     BufferedInputStream bis =null;
			     ByteArrayOutputStream baos=null;
			     OutputStream sos = null;
			     String fileName = "claimdoc";
			  try {  
			         
				
					try {
						webServiceManager = this.getWebServiceManagerObject();
		               
		                onlineIntimationVO.setPolicyGrpSeqID(ocoPolicyGrpseqId);
		                alResult = webServiceManager.getOnlineClaimSubmission(onlineIntimationVO);
					} catch (TTKException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	     
	              bis = new BufferedInputStream((InputStream) alResult.get(0));
	              baos=new ByteArrayOutputStream();
	              int ch;
                  while ((ch = bis.read()) != -1) baos.write(ch);
                  //sos = Response.
                  HttpServletResponse response = null;
                  response.setContentType("application/pdf");
                  baos.writeTo(sos); 
                  baos.flush();     
                  sos.flush();
	              
	              
	         	 FileOutputStream out = new FileOutputStream(new File(fileLocation));  
	                int read = 0;  
	                InputStream buff =  (InputStream) alResult.get(0);
	                byte[] bytes = new byte[1024];  
	                out = new FileOutputStream(new File(fileLocation));  
	                while ((read = buff.read(bytes)) != -1) {
	                    out.write(bytes, 0, read);  
	                }  
	                
	                out.flush();  
	                out.close(); 
	                
				 
	            } catch (IOException e) {e.printStackTrace();}  
	            String output = "File successfully downloaded to : " + fileLocation;  
	            return Response.status(200).entity(output).build();  
			  }*/
			
		
			@POST
			@Path("/claimSubmissionDetails")
			
		    @Consumes(MediaType.APPLICATION_JSON)  
			@Produces(MediaType.APPLICATION_JSON)
			public String claimSubmissionTrigger(@HeaderParam("ocoPolicyGrpseqId")Long ocoPolicyGrpseqId){
			
				

				 ArrayList alResult =new ArrayList();
				 OnlineIntimationVO onlineIntimationVO = new OnlineIntimationVO();
				 WebServiceManager webServiceManager;
			     JSONArray arra = new JSONArray();	
			     LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
			     LinkedHashMap<String, Object> mapObject1 = new LinkedHashMap<String, Object>();
			  try {  
			         
				
					webServiceManager = this.getWebServiceManagerObject();
	                onlineIntimationVO.setPolicyGrpSeqID(ocoPolicyGrpseqId);
	                alResult = webServiceManager.claimSubmissionTrigger(onlineIntimationVO);
	                if(alResult != null && alResult.size() >0)
	        		{
	        	    	    mapObject1.put("currencyId", alResult.get(0));
	        	    	    mapObject1.put("Result", alResult.get(1));
	        	    		mapObject.put("claimSubFlag", "Y");
		               		mapObject.put("alertMsg", "Search successfully");
	        	          	        		
	        		}else{
	                   	
	                   	mapObject.put("claimSubFlag", "N");
	               		mapObject.put("alertMsg", "No Data Found");
	                   }
	             
			  } catch (TTKException tte) {	        	
			    	
			    	try{
			    		String errorMsg="Exception occured";
			             mapObject.put("claimSubFlag", "N");
			             mapObject.put("alertMsg",errorMsg);
			        		
			            }catch(Exception ie) {
			        	ie.printStackTrace();
			        	mapObject.put("claimSubFlag", "N");
			        	mapObject.put("alertMsg", "Unknown Error1");	
			        	 }
			        
			    }catch (Exception e) {
			    	
			    	e.printStackTrace();
			    	 mapObject.put("claimSubFlag", "N");
			        mapObject.put("alertMsg", "Unknown Error2");	            
			   }
			   
			    arra.put(mapObject);
			    arra.put(mapObject1);
				return arra.toString();
			    
			}
			
			////////////////////////////////////////////Provider login//////////////////////////////
			@POST
		    @Path("/ProviderDataShare")
		    @Produces(MediaType.APPLICATION_JSON)
		    @Consumes(MediaType.APPLICATION_JSON)
		    public String getProviderDataShare(@HeaderParam("ocomodule")String ocomodule ,@HeaderParam("alkCountryId")String alkCountryId,@HeaderParam("alStateId")String alStateId){
				
			//AuthenticationVO authenticationVO=new AuthenticationVO();
				// input values : STATE ,COUNTRY ,PROTYPE ,PROSPECIALITY,GENDER, AREA
			

		       JSONArray arra = new JSONArray();	        
		        LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
		        LinkedHashMap<String,Object > mapObject1 = new LinkedHashMap<String, Object>();
		       ArrayList<Object> SearchList = new ArrayList<Object>();
		        int SaveList;
		        ArrayList<Object> al=new ArrayList<Object>();
		        try {
		 	    
		        	if(ocomodule.equals("")){
		        		mapObject.put("providerFlag", "N");
		        		mapObject.put("alertMsg", "Please Enter Module "); 
		        	}else if(ocomodule.equals("STATE")){
		        		if(alkCountryId.equals("")){
		            		mapObject.put("providerFlag", "N");
		            		mapObject.put("alertMsg", "Please choose Country");       		
		        		}
		        	}
		        	else if(ocomodule.equals("AREA")){
		        		if(alStateId.equals("")){
		            		mapObject.put("providerFlag", "N");
		            		mapObject.put("alertMsg", "Please choose State");       		
		        		}
		        	}
		                 
		        	
		        	
		            WebServiceManager webServiceManager=this.getWebServiceManagerObject();
		           if(ocomodule.equals("COUNTRY") || ocomodule.equals("STATE") || ocomodule.equals("AREA") || ocomodule.equals("GENDER") ||  ocomodule.equals("PROSPECIALITY") || ocomodule.equals("PROTYPE") || ocomodule.equals("NWTYPE")) 
		           {
		        	   if(ocomodule.equals("COUNTRY")){            	
		              SearchList = webServiceManager.getCountryList();
		            }else if (ocomodule.equals("STATE")){
		            	  SearchList = webServiceManager.getStateList(alkCountryId);
		            }else if (ocomodule.equals("AREA")){
		            	  SearchList = webServiceManager.getAreaList(alStateId);
		            }else if (ocomodule.equals("GENDER")){
		            	  SearchList = webServiceManager.getProTypeSpeciality(ocomodule);
		            }else if (ocomodule.equals("NWTYPE")){
		            	  SearchList = webServiceManager.getProTypeSpeciality(ocomodule);
		            } 
		            else{
		            	 SearchList = webServiceManager.getProTypeSpeciality(ocomodule);
		            }
		        	
		         // JSONArray arra1 = new JSONArray(SearchList);
				            
				     	   if(SearchList != null && SearchList.size() >0)
							  {
						            mapObject1.put("result", SearchList.get(0));
						            mapObject.put("providerFlag", (String) SearchList.get(1));
						            mapObject.put("alertMsg",(String) SearchList.get(2));
							}
				     	  else{
				                	
				               	mapObject.put("providerFlag", "N");
				           		mapObject.put("alertMsg", "No data found");
				               }
		           }else{
		            	mapObject.put("providerFlag", "N");
		           		mapObject.put("alertMsg", "Please Enter Module");
		            }
		          
		      	
		           
		        } 
		            
		        catch (TTKException tte) {	        	
		        	
		        	try{
		        		 String errorMsg="Error While Searching Dashboard Data.....";
		                 mapObject.put("providerFlag", "N");
			             mapObject.put("alertMsg",errorMsg);  		
			            }catch(Exception ie) {
		            	ie.printStackTrace();
		            	mapObject.put("providerFlag", "N");
			        	mapObject.put("alertMsg", "Error While Searching Dashboard Data!.....");	
		            	 }
		            
		        }catch (Exception e) {
		        	e.printStackTrace();
		        	 mapObject.put("providerFlag", "N");
			        mapObject.put("alertMsg", "Error While Searching Dashboard Data?.....");	            
			   }
		       
		        arra.put(mapObject);
		        arra.put(mapObject1);
				return arra.toString();
		        
		    } 
			
			
			/*''''''''''''''''''''''''''''provider  search'''''''''''''''''''''''''''''''''''''''''''''*/
			@POST
		    @Path("/providerSearch")
		    @Produces(MediaType.APPLICATION_JSON)
		    @Consumes(MediaType.APPLICATION_JSON)
		    public String getProviderSearch(@HeaderParam("alkCountry")String alkCountry ,@HeaderParam("alkState")String alkState,@HeaderParam("alkProviderType")String alkProviderType,@HeaderParam("alkSpeciality")String alkSpeciality){
			

			//AuthenticationVO authenticationVO=new AuthenticationVO();
		       JSONArray arra = new JSONArray();	        
		        LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
		        LinkedHashMap<String,Object > mapObject1 = new LinkedHashMap<String, Object>();
		       ArrayList<Object> SearchList = new ArrayList<Object>();
		        int SaveList;
		        try {
		 	        
		        	String stralkCountry=TTKCommon.checkNull(alkCountry);
		        	String stralkState=TTKCommon.checkNull(alkState);
		        	String stralkProviderType=TTKCommon.checkNull(alkProviderType);
		        	String stralkSpeciality=TTKCommon.checkNull(alkSpeciality);
		        	if(stralkCountry.equals("")){
		        		mapObject.put("providerFlag", "N");
		        		mapObject.put("alertMsg", "Please Enter Country "); 
		        	}else if(stralkState.equals("")){
		            		mapObject.put("providerFlag", "N");
		            		mapObject.put("alertMsg", "Please choose State");       		
		            	}/*else if(alkProviderType.equals("")){
		            		mapObject.put("status", "F");
		            		mapObject.put("error_message", "Please Enter Provider Type ");       		
		        	      }else if(stralkSpeciality.equals("")){
		    		        mapObject.put("status", "F");
		    		        mapObject.put("error_message", "Please Enter Specility ");       		
		    	        	}*/
		        	      else
		                  {	 
		       
		            WebServiceManager webServiceManager=this.getWebServiceManagerObject();
		            SearchList = webServiceManager.searchProviderList(stralkCountry,stralkState,stralkProviderType,alkSpeciality,null);
		         
		          JSONArray arra1 = new JSONArray(SearchList);
		            
		     	   if(arra1 != null && arra1.length() >0)
					  {
						for(int i=0;i<arra1.length();i++){
							 mapObject.put("providerFlag", "Y");
				            mapObject1.put("result", arra1.get(i));
				            mapObject.put("alertMsg","search successfully");
				          arra.put(mapObject1).toString();
				     }
					}
		     	  else{
		               	
		               	mapObject.put("providerFlag", "N");
		           		mapObject.put("alertMsg", "no data found");
		               }
		           	 
		      	   }
		           
		        } 
		            
		        catch (TTKException tte) {	        	
		        	
		        	try{
		        		 String errorMsg="Error While Searching Dashboard Data.....";
		                 mapObject.put("providerFlag", "N");
			             mapObject.put("alertMsg",errorMsg);  		
			            }catch(Exception ie) {
		            	ie.printStackTrace();
		            	mapObject.put("providerFlag", "N");
			        	mapObject.put("alertMsg", "Error While Searching Dashboard Data!.....");	
		            	 }
		            
		        }catch (Exception e) {
		        	e.printStackTrace();
		        	 mapObject.put("providerFlag", "N");
			        mapObject.put("alertMsg", "Error While Searching Dashboard Data?.....");	            
			   }
		       
		        arra.put(mapObject);
		        //arra.put(mapObject1);
				return arra.toString();
		        
		    }  
			
			
			@POST
		    @Path("/contactus")
		    @Produces(MediaType.APPLICATION_JSON)
		    @Consumes(MediaType.APPLICATION_JSON)
		    public String getSaveContactUs(@HeaderParam("alAlkootId")String alAlkootId,@HeaderParam("alMobileNo")String alMobileNo,@HeaderParam("alEmailId")String alEmailId ,@HeaderParam("alSubModule")String alSubModule,@HeaderParam("alIssueDesc")String alIssueDesc){
			

			//AuthenticationVO authenticationVO=new AuthenticationVO();
		       JSONArray arra = new JSONArray();	        
		        LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
		        LinkedHashMap<String,Object > mapObject1 = new LinkedHashMap<String, Object>();
		       // ArrayList<Object> SearchList = new ArrayList<Object>();
		        int SaveList;
		        ArrayList<Object> al=new ArrayList<Object>();
		        try {
		 	        
		        	String strAlkootId=TTKCommon.checkNull(alAlkootId);
		        	String strMobileNo=TTKCommon.checkNull(alMobileNo);
		        	String stralEmailId=TTKCommon.checkNull(alEmailId);
		        	String stralSubModule=TTKCommon.checkNull(alSubModule);
		        	String stralIssueDesc=TTKCommon.checkNull(alIssueDesc);
		        	if(alAlkootId.equals("") || alMobileNo.equals("") || stralEmailId.equals("")){
		        		mapObject.put("contactFlag", "N");
		        		mapObject.put("alertMsg", "Please Enter Alkoot Id/Mobile No./Email Id"); 
		        	}else if(stralSubModule.equals("")){
		            		mapObject.put("contactFlag", "N");
		            		mapObject.put("alertMsg", "Please choose Sub Module name");       		
		            	}else if(stralIssueDesc.equals("")){
		            		mapObject.put("contactFlag", "N");
		            		mapObject.put("alertMsg", "Please Enter Issue Desc ");       		
		        	}else
		            {	 
		        		al.add(stralEmailId);
		        		al.add(stralSubModule);
		        		al.add(stralIssueDesc);
		            WebServiceManager webServiceManager=this.getWebServiceManagerObject();
		            SaveList = webServiceManager.saveContactUs(strAlkootId,strMobileNo,alEmailId,alSubModule,alIssueDesc);
		                 			 
		            if(SaveList > 0)
		   			{
		   				   mapObject.put("contactFlag", "Y");
		   		           mapObject.put("alertMsg","Your query will be attended by our representative shortly");
		   		     
		   			}
		           	  else{
		                     	mapObject.put("contactFlag", "N");
		                 		mapObject.put("alertMsg", "Something went wrong Data not inserted.");
		                 }
		      	
		      	   }
		           
		        } 
		            
		        catch (TTKException tte) {	        	
		        	
		        	try{
		        		 String errorMsg="Error While Searching Dashboard Data.....";
		                 mapObject.put("contactFlag", "N");
			             mapObject.put("alertMsg",errorMsg);  		
			            }catch(Exception ie) {
		            	ie.printStackTrace();
		            	mapObject.put("contactFlag", "N");
			        	mapObject.put("alertMsg", "Error While Searching Dashboard Data!.....");	
		            	 }
		            
		        }catch (Exception e) {
		        	e.printStackTrace();
		        	 mapObject.put("contactFlag", "N");
			        mapObject.put("alertMsg", "Error While Searching Dashboard Data?.....");	            
			   }
		       
		        arra.put(mapObject);
		        arra.put(mapObject1);
				return arra.toString();
		        
		    } 
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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

	 
	 
	 
	 	private static final String FILE_UPLOAD_PATH = "/home/tipsint/jboss-as-7.1.3.Final/bin/MobAppWebservices";
	//    private static final String CANDIDATE_NAME = "candidateName";
	 //   private static final String SUCCESS_RESPONSE = "Successful";
	 //   private static final String FAILED_RESPONSE = "Failed";
	     
	    @POST
	    @Consumes(MediaType.MULTIPART_FORM_DATA)
	    @Produces(MediaType.APPLICATION_JSON)
	  @Path("/saveClaimSubmissionMultiplePdf")
	    public String saveClaimSubmissionMultiplePdf(@Context HttpServletRequest request,@HeaderParam("ocoMemseqId")Long ocoMemseqId,@HeaderParam("ocoInvoiceNo")String ocoInvoiceNo,
                @HeaderParam("ocoRequestedAmt")BigDecimal ocoRequestedAmt,@HeaderParam("ocoRequestedCurrency")String ocoRequestedCurrency,@HeaderParam("ocoEmailID")String ocoEmailID,@HeaderParam("ocoFileType")String ocoFileType)
	    {
		
	//	String responseStatus = SUCCESS_RESPONSE;
	//	String candidateName = null;

		OnlineIntimationVO onlineIntimationVO = new OnlineIntimationVO();
		boolean checkFile=true;
		ArrayList alResult = new ArrayList();
		WebServiceManager webServiceManager;
		JSONArray arra = new JSONArray();
		LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();

		try {		
			String strOcoInvoiceNo=ocoInvoiceNo;
			String strRequestedCurrency=ocoRequestedCurrency;
		
	
			 if(strOcoInvoiceNo.length()<1 ){
        		mapObject.put("claimSubFlag", "N");
        		mapObject.put("alertMsg", "Please enter the Invoice No.");    
        		 checkFile=false;//same checkFile flag is used for mandatory fields
        	}else  if(strRequestedCurrency.length()<1 ){
         		mapObject.put("claimSubFlag", "N");
         		mapObject.put("alertMsg", "Please enter the Requested currency.");    
         		 checkFile=false;//same checkFile flag is used for mandatory fields
         	}
			
			if(strOcoInvoiceNo.equalsIgnoreCase("null")){
        		mapObject.put("claimSubFlag", "N");
		  		mapObject.put("alertMsg", "Input cannot be accepted as Null .Please Correct the same");  
		  		checkFile=false;
			
        	}else if (ocoRequestedAmt == null || ocoRequestedAmt.compareTo(new BigDecimal("0.00")) == 0 ) {
        		mapObject.put("claimSubFlag", "N");
        		mapObject.put("alertMsg", "Requested amount should be greater than 0");    
        		 checkFile=false;//same checkFile flag is used for mandatory fields
        	}        		
			else{
			
				// checks whether there is a file upload request or not
				if (ServletFileUpload.isMultipartContent(request)) {
				
					final FileItemFactory factory = new DiskFileItemFactory();
					final ServletFileUpload fileUpload = new ServletFileUpload(factory);
					String finalFileName = "";
					final List items = fileUpload.parseRequest(request);

					if (items != null) {
						final Iterator iter = items.iterator();
						String fileToSaveName = "";
						String filefinalplaced = "";
						while (iter.hasNext()) {
							final FileItem item = (FileItem) iter.next();
							final String itemName = item.getName();
							final String fieldName = item.getFieldName();
							final String fieldValue = item.getString();
								
							String strFileExt = itemName.substring(itemName.lastIndexOf(".") + 1,itemName.length());
							filefinalplaced = FILE_UPLOAD_PATH + File.separator+ ocoMemseqId.toString();
							if ((strFileExt.equalsIgnoreCase("jpeg")|| (strFileExt.equalsIgnoreCase("jpg"))|| (strFileExt.equalsIgnoreCase("gif")) || (strFileExt.equalsIgnoreCase("png") ||
									(strFileExt.equalsIgnoreCase("pdf")))))
							{
								
								checkFile=true;
								
												/*Creation of Folder*/
								File folder = new File(filefinalplaced);
								if (!folder.exists()) {
									
									folder.mkdir();
								} // End Of if(!folder.exists()) 
								
											/*	If File Other Than PDF */
								if (!(strFileExt.equalsIgnoreCase("pdf"))) {	
								final File savedFile = new File(filefinalplaced+ File.separator + itemName);
							
								item.write(savedFile);
								String input = filefinalplaced + File.separator+ itemName;
								String itemoutname = itemName.replace("."+ strFileExt, "");
								String output = filefinalplaced+ File.separator + itemoutname + ".pdf";						
								imagetoPDF(input, output);// Converting Image To PDF							
								fileToSaveName = fileToSaveName + itemoutname+ ".pdf" + "|";
							} else {
								final File savedFile = new File(filefinalplaced+ File.separator + itemName);
							
								item.write(savedFile);
								fileToSaveName = fileToSaveName + itemName+ "|";
							}
					   } else {
						  
						   checkFile=false;
								mapObject.put("alertMsg","Please Upload File formats pdf/jpeg/jpg/gif/png");
								mapObject.put("claimSubFlag", "N");
								  break;
					   }					
				}// end of while (iter.hasNext())
						
						
						
						if(checkFile==true){
				

						
						/* -Marging All  PDF In Singal PDF-*/
						String filelist[] = fileToSaveName.split("\\|");
						PDFMergerUtility ut = new PDFMergerUtility();
						for (int i = 0; i < filelist.length; i++) {
							
							ut.addSource(filefinalplaced + File.separator+ filelist[i]);
						}
						finalFileName = ocoMemseqId.toString();
						ut.setDestinationFileName(filefinalplaced+ File.separator + finalFileName + ".pdf");
						ut.mergeDocuments();
						
							/*Saving To DataBase*/
						int read = 0;
						byte[] bytes = new byte[1024];
						InputStream uploadedInputStream = new FileInputStream(filefinalplaced + File.separator+ finalFileName + ".pdf");
						bytes = org.apache.commons.io.IOUtils.toByteArray(uploadedInputStream);

						webServiceManager = this.getWebServiceManagerObject();
						onlineIntimationVO.setClaimSubmission(bytes);
						onlineIntimationVO.setMemberSeqID(ocoMemseqId);
						onlineIntimationVO.setInvoice_Number(ocoInvoiceNo);
						onlineIntimationVO.setRequestedAmt(ocoRequestedAmt);
						onlineIntimationVO.setReq_currency_type(ocoRequestedCurrency);
						onlineIntimationVO.setEmailID(ocoEmailID);
						onlineIntimationVO.setFileDataOInputStream(uploadedInputStream);
						onlineIntimationVO.setFileType(ocoFileType);

						alResult = webServiceManager.saveOnlineClaimSubmission(onlineIntimationVO);

						String exceptionmsg = (String) alResult.get(0);
						String batchNo = (String) alResult.get(1);
						String claimSubFlag ="";
						
						if("null".equals(batchNo)){
							claimSubFlag = "N";
						}
						else{
							claimSubFlag = "Y";
						}
						
						
						if("Y".equals(claimSubFlag)){
							if (alResult != null && alResult.size() > 0) {
							mapObject.put("alertMsg", (String) alResult.get(0));
							mapObject.put("Batch_No", (String) alResult.get(1));
							mapObject.put("claimSubFlag", "Y");
}
							
							}
					
						else{
							if(exceptionmsg !=""){
								mapObject.put("alertMsg", exceptionmsg);
								mapObject.put("claimSubFlag", "N");
							}
							else{
								mapObject.put("alertMsg", "No data found");
								mapObject.put("claimSubFlag", "N");
							}
							
						}
						
					}//End Of 	if(checkFile==true)
				}//End Of if (items != null)
			}//End OF if (ServletFileUpload.isMultipartContent(request)) {
		}//end of null validation
	
		} catch (TTKException tte) {
				String errorMsg = "Exception occured";
				mapObject.put("claimSubFlag", "N");
				mapObject.put("alertMsg", errorMsg);
		} 		
		catch (FileNotFoundException FNFE){			
			if(checkFile==false){
				mapObject.put("alertMsg","Please Upload File formats pdf/jpeg/jpg/gif/png");
				mapObject.put("claimSubFlag", "N");			
			}else{	
			FNFE.printStackTrace();
			mapObject.put("claimSubFlag", "N");
			mapObject.put("alertMsg", "Unknown Error1");
			}
		}	
		catch (Exception e) {
			e.printStackTrace();
			mapObject.put("claimSubFlag", "N");
			mapObject.put("alertMsg", "Unknown Error2");
		}
		arra.put(mapObject);
		return arra.toString();
}
	   
 
	    public static void  imagetoPDF(String input, String output) {  	
            try {
    	    	Image image = Image.getInstance(input);
    	    	Document document = new Document(image);
    	    	FileOutputStream fos = new FileOutputStream(output);
    	    	PdfWriter writer = PdfWriter.getInstance(document, fos);
              document.open();
              image.setAbsolutePosition(0, 0);
              document.add(image);
              document.close();
            }
            catch (Exception e) {
              e.printStackTrace();
            }
          }//End Of imagetoPDF(String input, String output)

	    
	    @POST
		 @Path("/updateMyProfilenew")
		 @Produces(MediaType.APPLICATION_JSON)
		 @Consumes(MediaType.APPLICATION_JSON)
		  public String updateMyProfilenew(@HeaderParam("ocoAddressSeqId")Long ocoAddressSeqId,@HeaderParam("ocoPolicyGroupSeq")Long ocoPolicyGroupSeq,@HeaderParam("ocoMemberSeq")Long ocoMemberSeq,@HeaderParam("ocoPassword")String ocoPassword,@HeaderParam("ocoDateOfBirth")String ocoDateOfBirth,@HeaderParam("ocoGenderTypeID")String ocoGenderTypeID,
		    		@HeaderParam("ocoMobileNo")String ocoMobileNo,@HeaderParam("ocoEmailId")String ocoEmailId,@HeaderParam("ocoAddress1")String ocoAddress1,
		    		@HeaderParam("ocoAddress2")String ocoAddress2,@HeaderParam("ocoAddress3")String ocoAddress3,@HeaderParam("ocoPinCode")String ocoPinCode,
		    		@HeaderParam("ocoCountryTypeId")String ocoCountryTypeId,@HeaderParam("ocoStateId")String ocoStateId,@HeaderParam("ocoAreaId")String ocoAreaId,
		    		@HeaderParam("ocobankaccnumber")String ocobankaccnumber,@HeaderParam("ocobankacctype")String ocobankacctype,@HeaderParam("ocobankdestination")String ocobankdestination	,
		    		@HeaderParam("ocobankcity")String ocobankcity,@HeaderParam("ocobankarea")String ocobankarea,@HeaderParam("ocobankbranch")String ocobankbranch,		
		    		@HeaderParam("ocoIBANNumber")String ocoIBANNumber,
		    		@HeaderParam("ocoSwiftCode")String ocoSwiftCode ,
		    		@HeaderParam("ocoBankAccountQatarYN")String ocoBankAccountQatarYN,
		    		@HeaderParam("ocobankbranchText")String ocobankbranchText,
		    		@HeaderParam("ocoBankCountry")String ocoBankCountry
				  )
	    {
		 	OnlineIntimationVO onlineIntimationVO = new OnlineIntimationVO();
			
			 java.util.Date date=new java.util.Date();  
			   
		       
		 	JSONArray arra = new JSONArray();	
		     LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
		     LinkedHashMap<String, Object> mapObject1 = new LinkedHashMap<String, Object>();
		     ArrayList alResult =new ArrayList();
		 	//String startDate=TTKCommon.checkNull(ocoStartDate);

		     try {
		    	 String strIBANNumber=ocoIBANNumber;
		    	 String strbankaccnumber=ocobankaccnumber;
		    	 String strPassword=ocoPassword;
		    	 boolean truevalue = true;
		    	 
		    	 if(strPassword.length()<1){
		        		mapObject.put("myprofileFlag", "N");
		        		mapObject.put("alertMsg", "Please enter teh Password");  
					}
		    	else if(strbankaccnumber != null && !TTKCommon.isAlphaNumeric(strbankaccnumber) == truevalue){
		        		mapObject.put("myprofileFlag", "N");
		        		mapObject.put("alertMsg", "Account Number should be alphanumeric");  
					}
					else if(strIBANNumber != null &&  !TTKCommon.isAlphaNumeric(strIBANNumber) == truevalue){
		        		mapObject.put("myprofileFlag", "N");
		        		mapObject.put("alertMsg", "IBAN Number should be alphanumeric");  
					}
		    	 
		    	
					else if(("null").equalsIgnoreCase(strIBANNumber) || ("null").equalsIgnoreCase(strbankaccnumber) ||  ("null").equalsIgnoreCase(strPassword)){
		        		mapObject.put("myprofileFlag", "N");
				  		mapObject.put("alertMsg", "Input cannot be accepted as Null .Please Correct the same");  
					
					}
					
					else if(ocoPolicyGroupSeq.SIZE<1 || ocoAddressSeqId.SIZE<1 || ocoMemberSeq.SIZE<1 ) {
		         		mapObject.put("myprofileFlag", "N");
		         		mapObject.put("alertMsg", "please enter PolicyGroupSeq/ AddressSeqId /MemberSeqId");  
		     		}else if(ocoDateOfBirth.toString().length()<1 ){
		     		
		     			mapObject.put("myprofileFlag", "N");
		         		mapObject.put("alertMsg", "please enter Date Of Birth");  
		         	}else if(TTKCommon.getConvertStringToDateformatChange(ocoDateOfBirth).after(TTKCommon.getDate())){
		     		
		     			mapObject.put("myprofileFlag", "N");
		         		mapObject.put("alertMsg", "Date Of Birth should be less than current date");  
		         	}
		     		
		     		else if(ocoGenderTypeID.length()<1 ){
		     			mapObject.put("myprofileFlag", "N");
		         		mapObject.put("alertMsg", "please enter GenderGeneralTypeId");  
		         	}else if(ocoCountryTypeId.length()<1 ){
			     			mapObject.put("myprofileFlag", "N");
			         		mapObject.put("alertMsg", "please Select Country");  
		         	}else if(ocoStateId.length()<1 ){
		     			mapObject.put("myprofileFlag", "N");
		         		mapObject.put("alertMsg", "please Select State");  
		         	}else
		         {	
					onlineIntimationVO.setAddressSeqID(ocoAddressSeqId);
					onlineIntimationVO.setPolicyGrpSeqID(ocoPolicyGroupSeq);
					onlineIntimationVO.setMemberSeqID(ocoMemberSeq);
				
		  			onlineIntimationVO.setPassword(ocoPassword);
		  		//	onlineIntimationVO.setGenderDesc(ocoGenderTypeID);
		  			onlineIntimationVO.setDateOfBirth(ocoDateOfBirth);

		  			
		  			onlineIntimationVO.setMobileNbr(ocoMobileNo);
		  			onlineIntimationVO.setEmailID(ocoEmailId);
		  			
		  			onlineIntimationVO.setAddress1(ocoAddress1);
		  		//	onlineIntimationVO.setAddress2(ocoAddress2);
		  		//	onlineIntimationVO.setAddress3(ocoAddress3);
		  			onlineIntimationVO.setCityID(ocoAreaId);
		  			onlineIntimationVO.setStateId(ocoStateId);
		  			onlineIntimationVO.setCountryId(ocoCountryTypeId);
		  			onlineIntimationVO.setPincode(ocoPinCode);
		  			onlineIntimationVO.setGenderGeneralId(ocoGenderTypeID);
		  		//	onlineIntimationVO.setPolicyGrpSeqID(ocoPolicyGroupSeq);

		  			onlineIntimationVO.setMicr(ocoIBANNumber);
		  			onlineIntimationVO.setBankaccno(ocobankaccnumber);
		  			onlineIntimationVO.setBankacctype(ocobankacctype);
		  			onlineIntimationVO.setBankaccdestination(ocobankdestination);
		  			onlineIntimationVO.setBankstate(ocobankcity);
		  			onlineIntimationVO.setBankacity(ocobankarea);
		  			onlineIntimationVO.setBankbranch(ocobankbranch);	
		  			
		  			onlineIntimationVO.setSwiftCode(ocoSwiftCode);	
		  			onlineIntimationVO.setBankAccountQatarYN(ocoBankAccountQatarYN);	
		  			onlineIntimationVO.setBankBranchText(ocobankbranchText);
		  			onlineIntimationVO.setBankCountry(ocoBankCountry);
		  			
		         WebServiceManager webServiceManager=this.getWebServiceManagerObject();
		         ArrayList arra1 = webServiceManager.updateMyprofile(onlineIntimationVO);
		         int result = (int) arra1.get(0);
		         String exceptionalert = (String) arra1.get(1);
		        
		         
		         if( result >0)
		 		{
		 				mapObject.put("myprofileFlag", "Y");
		 		        mapObject.put("alertMsg", "Updated Successfully");	
		 	          //  mapObject1.put("result", arra1.get(i));
		 	          //arra.put(mapObject1).toString();
		 		
		 		}else{
		 			if(exceptionalert.length()>0){
		 				  mapObject.put("myprofileFlag", "N");
			              mapObject.put("alertMsg",exceptionalert);
		 			}
		 			else{
		 				 String errorMsg="Data Not inserted";
			              mapObject.put("myprofileFlag", "N");
			              mapObject.put("alertMsg",errorMsg);
		 			}
		 			
		 		}
		         
		     }//end else
		     		
		      
		     }  catch (TTKException tte) {	        	
		     	
		     	try{
		     		String errorMsg="Data Not inserted";
		              mapObject.put("myprofileFlag", "N");
		              mapObject.put("alertMsg",errorMsg);
		         		
		             }catch(Exception ie) {
		         	ie.printStackTrace();
		         	mapObject.put("myprofileFlag", "N");
		         	mapObject.put("alertMsg", "Unknown Error1");	
		         	 }
		         
		     }catch (Exception e) {
		     	
		     	e.printStackTrace();
		     	 mapObject.put("myprofileFlag", "N");
		         mapObject.put("alertMsg", "Unknown Error2");	            
		    }
		    
		     arra.put(mapObject);
		   //  arra.put(mapObject1);
		 	return arra.toString();
		     
		 }    
	    
	    
	    
	    @POST
	    @Path("/providerSearchnew")
	    @Produces(MediaType.APPLICATION_JSON)
	    @Consumes(MediaType.APPLICATION_JSON)
	    public String getProviderSearchnew(@HeaderParam("alkCountry")String alkCountry ,@HeaderParam("alkState")String alkState,@HeaderParam("alkProviderType")String alkProviderType,@HeaderParam("alkSpeciality")String alkSpeciality,
	    		@HeaderParam("alkNetworkType")String alkNetworkType){
		

		//AuthenticationVO authenticationVO=new AuthenticationVO();
	       JSONArray arra = new JSONArray();	        
	        LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
	        LinkedHashMap<String,Object > mapObject1 = new LinkedHashMap<String, Object>();
	       ArrayList<Object> SearchList = new ArrayList<Object>();
	        int SaveList;
	        try {
	 	        
	        	String stralkCountry=TTKCommon.checkNull(alkCountry);
	        	String stralkState=TTKCommon.checkNull(alkState);
	        	String stralkProviderType=TTKCommon.checkNull(alkProviderType);
	        	String stralkSpeciality=TTKCommon.checkNull(alkSpeciality);
	        	if(stralkCountry.equals("")){
	        		mapObject.put("providerFlag", "N");
	        		mapObject.put("alertMsg", "Please Enter Country "); 
	        	}else if(stralkState.equals("")){
	            		mapObject.put("providerFlag", "N");
	            		mapObject.put("alertMsg", "Please choose State");       		
	            	}/*else if(alkProviderType.equals("")){
	            		mapObject.put("status", "F");
	            		mapObject.put("error_message", "Please Enter Provider Type ");       		
	        	      }else if(stralkSpeciality.equals("")){
	    		        mapObject.put("status", "F");
	    		        mapObject.put("error_message", "Please Enter Specility ");       		
	    	        	}*/
	        	      else
	                  {	 
	       
	            WebServiceManager webServiceManager=this.getWebServiceManagerObject();
	            SearchList = webServiceManager.searchProviderList(stralkCountry,stralkState,stralkProviderType,alkSpeciality,alkNetworkType);
	         
	          JSONArray arra1 = new JSONArray(SearchList);
	            
	     	   if(arra1 != null && arra1.length() >0)
				  {
					for(int i=0;i<arra1.length();i++){
						 mapObject.put("providerFlag", "Y");
			            mapObject1.put("result", arra1.get(i));
			            mapObject.put("alertMsg","search successfully");
			          arra.put(mapObject1).toString();
			     }
				}
	     	  else{
	               	
	               	mapObject.put("providerFlag", "N");
	           		mapObject.put("alertMsg", "no data found");
	               }
	           	 
	      	   }
	           
	        } 
	            
	        catch (TTKException tte) {	        	
	        	
	        	try{
	        		 String errorMsg="Error While Searching Dashboard Data.....";
	                 mapObject.put("providerFlag", "N");
		             mapObject.put("alertMsg",errorMsg);  		
		            }catch(Exception ie) {
	            	ie.printStackTrace();
	            	mapObject.put("providerFlag", "N");
		        	mapObject.put("alertMsg", "Error While Searching Dashboard Data!.....");	
	            	 }
	            
	        }catch (Exception e) {
	        	e.printStackTrace();
	        	 mapObject.put("providerFlag", "N");
		        mapObject.put("alertMsg", "Error While Searching Dashboard Data?.....");	            
		   }
	       
	        arra.put(mapObject);
	        //arra.put(mapObject1);
			return arra.toString();
	        
	    }  
	    
/*	    To Print Claim General and Dental form */	
	    
	  /*  @Path("/getClaimForms")
	    @POST
	    @Produces(MediaType.APPLICATION_JSON)
	    @Consumes(MediaType.APPLICATION_JSON)
	    public Response getClaimForms(@HeaderParam("ocoMemberSeq")Long ocoMemberSeq,@HeaderParam("ocoBenifitTypeVal")String ocoBenifitTypeVal){
	    	 System.out.println(" mobapp getClaimForms VidalWireRest");

	    	OnlineIntimationVO onlineIntimationVO = new OnlineIntimationVO();
	    	ResponseBuilder response = null ;
	    	String EmptyString = "No Data Found";

	    try {
	    	
	    		onlineIntimationVO.setMemberSeqID(ocoMemberSeq);
	    		onlineIntimationVO.setBenifitTypeVal(ocoBenifitTypeVal);
	    		WebServiceManager webServiceManager=this.getWebServiceManagerObject();
	    		   
	    	    byte[] data=MobileEcard.getClaimForm(ocoMemberSeq,ocoBenifitTypeVal);//new byte[fis.available()];
	    	     response = Response.ok(Base64.encodeBase64String(data));

	    		
	    		}catch (Exception e) {
	    	e.printStackTrace();
	    	 
	    }
	    return response.build();
	    }*/
  
	    
	    @Path("/getClaimForms")
	    @POST
	    @Produces(MediaType.APPLICATION_JSON)
	    @Consumes(MediaType.APPLICATION_JSON)
	    public Response getClaimForms(@HeaderParam("ocoMemberSeq")Long ocoMemberSeq,@HeaderParam("ocoBenifitTypeVal")String ocoBenifitTypeVal){
	    	

	    	ResponseBuilder response = null ;
	    	String filaPath = "";
	    try{
	    	if(ocoBenifitTypeVal.equalsIgnoreCase("CGF")){
	    	 filaPath=TTKPropertiesReader.getPropertyValue("GlobalDownload")+"ClaimformGeneral.pdf";
	    	}else if(ocoBenifitTypeVal.equalsIgnoreCase("CDF")){
	    	 filaPath=TTKPropertiesReader.getPropertyValue("GlobalDownload")+"ClaimformDental.pdf";
	    	}
	    	 File file = new File(filaPath);  
	         response = Response.ok((Object) file);
	         
	         byte[] bytesArray = new byte[(int) file.length()];
	         FileInputStream fis = new FileInputStream(file);
	         fis.read(bytesArray); //read file into bytes[]
	         fis.close();
	        // response.header("Content-Disposition","attachment; filename=\"submissionform.pdf\"");  
	         response = Response.ok(Base64.encodeBase64String(bytesArray));
	       
	    }catch (Exception e) {
	    	e.printStackTrace();
	    } 
	    return response.build();
	    }
	    
	    
	    
	    /*	 Dropdown for Benefit Type value  */	
	    
	    @POST
	    @Path("/claimFormDataShare")
	    @Produces(MediaType.APPLICATION_JSON)
	    @Consumes(MediaType.APPLICATION_JSON)
	    public String getClaimFormDataShare(@HeaderParam("ocomodule")String ocomodule){
			
		

	       JSONArray arra = new JSONArray();	        
	        LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
	        LinkedHashMap<String,Object > mapObject1 = new LinkedHashMap<String, Object>();
	       ArrayList<Object> SearchList = new ArrayList<Object>();
	        int SaveList;
	        ArrayList<Object> al=new ArrayList<Object>();
	        try {
	 	
	 	   if(("").equals(ocomodule)){
	        		mapObject.put("claimFormFlag", "N");
	        		mapObject.put("alertMsg", "Please Enter Module "); 
	        	}
	                 	        		        	
	            WebServiceManager webServiceManager=this.getWebServiceManagerObject();
	        
	        	   if(ocomodule.equals("BENFITTYPE")){            	
	        	   SearchList = webServiceManager.getBenifitTypeList();
	          	        	
	         // JSONArray arra1 = new JSONArray(SearchList);
			            
			     	   if(SearchList != null && SearchList.size() >0)
						  {
					            mapObject1.put("result", SearchList.get(0));
					            mapObject.put("claimFormFlag", (String) SearchList.get(1));
					            mapObject.put("alertMsg",(String) SearchList.get(2));
						}
			     	  else{
			                	
			               	mapObject.put("claimFormFlag", "N");
			           		mapObject.put("alertMsg", "No data found");
			               }
	           }else{
	            	mapObject.put("claimFormFlag", "N");
	           		mapObject.put("alertMsg", "Please Enter Module");
	            }
	          	      		           
	        } 
	            
	        catch (TTKException tte) {	        	
	        	
	        	try{
	        		 String errorMsg="Error While Searching Dashboard Data.....";
	                 mapObject.put("claimFormFlag", "N");
		             mapObject.put("alertMsg",errorMsg);  		
		            }catch(Exception ie) {
	            	ie.printStackTrace();
	            	mapObject.put("claimFormFlag", "N");
		        	mapObject.put("alertMsg", "Error While Searching Dashboard Data!.....");	
	            	 }
	            
	        }catch (Exception e) {
	        	e.printStackTrace();
	        	 mapObject.put("claimFormFlag", "N");
		        mapObject.put("alertMsg", "Error While Searching Dashboard Data?.....");	            
		   }
	       
	        arra.put(mapObject);
	        arra.put(mapObject1);
			return arra.toString();
	        
	    } 
		    
	    
	    @POST
	    @Path("/Bankdatashare")
	    @Produces(MediaType.APPLICATION_JSON)
	    @Consumes(MediaType.APPLICATION_JSON)
	    public String getBankdataShare(@HeaderParam("ocomodule")String ocomodule ,@HeaderParam("ocobankdestination")String ocobankdestination,@HeaderParam("ocobankcity")String ocobankcity,@HeaderParam("ocobankarea")String ocobankarea,@HeaderParam("ocobankbranch")String ocobankbranch){
			
		
			// input values : ACC_TYPE , BANK_DESTINATION , BANK_AREA, BANK_CITY ,BANK_BRANCH
			

	       JSONArray arra = new JSONArray();	        
	        LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
	        LinkedHashMap<String,Object > mapObject1 = new LinkedHashMap<String, Object>();
	       ArrayList<Object> SearchList = new ArrayList<Object>();
	        int SaveList;
	        ArrayList<Object> al=new ArrayList<Object>();
	        try {
	 	    
	        	if(("").equals(ocomodule)){ 
	        		mapObject.put("bankdetailFlag", "N");
	        		mapObject.put("alertMsg", "Please Enter Module "); 
	        	}else if(("BANK_CITY").equalsIgnoreCase(ocomodule)){
	        		if(("").equals(ocobankdestination)){
	            		mapObject.put("bankdetailFlag", "N");
	            		mapObject.put("alertMsg", "Please choose Bank Destination");       		
	        		}
	        	}
	        	else if(("BANK_AREA").equalsIgnoreCase(ocomodule)){
	        		if(("").equals(ocobankcity)){
	            		mapObject.put("bankdetailFlag", "N");
	            		mapObject.put("alertMsg", "Please choose City");       		
	        		}
	        	}
	        	else if(("BANK_BRANCH").equalsIgnoreCase(ocomodule)){
	        		if(("").equals(ocobankarea)){
	            		mapObject.put("bankdetailFlag", "N");
	            		mapObject.put("alertMsg", "Please choose Area");       		
	        		}
	        	}
	                 
	        
	            WebServiceManager webServiceManager=this.getWebServiceManagerObject();
	           if(ocomodule.equals("ACC_TYPE") || ocomodule.equals("BANK_DESTINATION") || ocomodule.equals("BANK_CITY") || ocomodule.equals("BANK_AREA") ||  ocomodule.equals("BANK_BRANCH") ) 
	           {
	        	 
	             SearchList = webServiceManager.getbanksearchData(ocomodule,ocobankdestination,ocobankcity,ocobankarea,ocobankbranch);
	           
	        	
	         // JSONArray arra1 = new JSONArray(SearchList);
			            
			     	   if(SearchList != null && SearchList.size() >0)
						  {
					            mapObject1.put("result", SearchList.get(0));
					            mapObject.put("bankdetailFlag", (String) SearchList.get(1));
					            mapObject.put("alertMsg",(String) SearchList.get(2));
						}
			     	  else{
			                	
			               	mapObject.put("bankdetailFlag", "N");
			           		mapObject.put("alertMsg", "No data found");
			               }
	           }else{
	            	mapObject.put("bankdetailFlag", "N");
	           		mapObject.put("alertMsg", "Please Enter Module");
	            }
	          
	      	
	           
	        } 
	            
	        catch (TTKException tte) {	        	
	        	
	        	try{
	        		 String errorMsg="Error While Searching Dashboard Data.....";
	                 mapObject.put("bankdetailFlag", "N");
		             mapObject.put("alertMsg",errorMsg);  		
		            }catch(Exception ie) {
	            	ie.printStackTrace();
	            	mapObject.put("bankdetailFlag", "N");
		        	mapObject.put("alertMsg", "Error While Searching Dashboard Data!.....");	
	            	 }
	            
	        }catch (Exception e) {
	        	e.printStackTrace();
	        	 mapObject.put("bankdetailFlag", "N");
		        mapObject.put("alertMsg", "Error While Searching Dashboard Data?.....");	            
		   }
	       
	        arra.put(mapObject);
	        arra.put(mapObject1);
			return arra.toString();
	        
	    }    
	    @POST
	    @Path("/memberWindowPeriod")
	    @Produces(MediaType.APPLICATION_JSON)
	    @Consumes(MediaType.APPLICATION_JSON)
	    public String getMemberWindowPeriod(@HeaderParam("ocoUserId")Long ocoUserId){
			

			OnlineIntimationVO onlineIntimationVO = new OnlineIntimationVO();
	        JSONArray arra = new JSONArray();	
	        LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
	        LinkedHashMap<String, Object> mapObject1 = new LinkedHashMap<String, Object>();
	        ArrayList alResult =new ArrayList();
	        try {
	 	        
	        	//Long memberSeq = ocoUserId;
	        	if(ocoUserId.SIZE<1){
	        		
	        		mapObject.put("memberWindowPeriodFlag", "N");
	        		mapObject.put("alertMsg", "please enter User ID");       		
	        	}else
	            {	
	        	
	        //	onlineIntimationVO.setMemberSeqID(memberSeq);
	            WebServiceManager webServiceManager=this.getWebServiceManagerObject();
	            alResult = webServiceManager.getWindowPeriod(ocoUserId);
	            JSONArray arra1 = new JSONArray(alResult);
	            
	    	     if(arra1 != null && arra1.length() >0)
				{
					//for(int i=0;i<arra1.length();i++){
						mapObject.put("memberWindowPeriodFlag", "Y");
			            mapObject1.put("result", arra1.get(0));
			            mapObject.put("alertMsg","get successfully");
			          //  arra.put(mapObject1).toString();
					//}
				
				}else{
	               	
	               	mapObject.put("memberWindowPeriodFlag", "N");
	           		mapObject.put("alertMsg", "No Data Found");
	               }
	            }
	            
	        } catch (TTKException tte) {	        	
	        	
	        	try{
	        		String errorMsg="no data found";
	                 mapObject.put("memberWindowPeriodFlag", "N");
		             mapObject.put("alertMsg",errorMsg);
		        		
		            }catch(Exception ie) {
	            	ie.printStackTrace();
	            	mapObject.put("memberWindowPeriodFlag", "N");
		        	mapObject.put("alertMsg", "Unknown Error1");	
	            	 }
	            
	        }catch (Exception e) {
	        	
	        	e.printStackTrace();
	        	 mapObject.put("memberWindowPeriodFlag", "N");
		        mapObject.put("alertMsg", "Unknown Error2");	            
		   }
	       
	        arra.put(mapObject);
	         arra.put(mapObject1);
			return arra.toString();
	        
	    }
	    
	 	private static final String SHORTFALL_UPLOAD_PATH = "/home/tipsint/jboss-as-7.1.3.Final/bin/MobApp";     
		    @POST
		    @Consumes(MediaType.MULTIPART_FORM_DATA)
		    @Produces(MediaType.APPLICATION_JSON)
		  @Path("/saveShortfallSubmissions")
		    public String saveShortfallSubmissions(@Context HttpServletRequest request,@HeaderParam("ocoShortfallseqId")Long ocoShortfallseqId,@HeaderParam("ocoRemaks")String ocoRemaks)
		    {
			

			OnlineIntimationVO onlineIntimationVO = new OnlineIntimationVO();
			boolean checkFile=true;
			ArrayList alResult = new ArrayList();
			WebServiceManager webServiceManager;
			JSONArray arra = new JSONArray();
			LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
			try {		
					// checks whether there is a file upload request or not
				String strRemaks = ocoRemaks;
				if(strRemaks.length()<1){
	        		mapObject.put("claimSubFlag", "N");
	        		mapObject.put("alertMsg", "Please enter the remarks");    
	        	}else if(strRemaks.equalsIgnoreCase("null")){
	        		mapObject.put("claimSubFlag", "N");
			  		mapObject.put("alertMsg", "Input cannot be accepted as Null .Please Correct the same");  	
	        	}
			   		else  if (ServletFileUpload.isMultipartContent(request)) {
						final FileItemFactory factory = new DiskFileItemFactory();
						final ServletFileUpload fileUpload = new ServletFileUpload(factory);
						String finalFileName = "";
						final List items = fileUpload.parseRequest(request);
						if (items != null) {
							final Iterator iter = items.iterator();
							String fileToSaveName = "";
							String filefinalplaced = "";
							while (iter.hasNext()) {
								final FileItem item = (FileItem) iter.next();
								final String itemName = item.getName();
								final String fieldName = item.getFieldName();
								final String fieldValue = item.getString();
									
								String strFileExt = itemName.substring(itemName.lastIndexOf(".") + 1,itemName.length());
								filefinalplaced = SHORTFALL_UPLOAD_PATH + File.separator+ ocoShortfallseqId.toString();
								if ((strFileExt.equalsIgnoreCase("jpeg")|| (strFileExt.equalsIgnoreCase("jpg"))|| (strFileExt.equalsIgnoreCase("gif")) || (strFileExt.equalsIgnoreCase("png") ||
										(strFileExt.equalsIgnoreCase("pdf")))))
								{						
									checkFile=true;
									
													/*Creation of Folder*/
									File folder = new File(filefinalplaced);
									if (!folder.exists()) {
										folder.mkdir();
									} // End Of if(!folder.exists()) 
									
												/*	If File Other Than PDF */
									if (!(strFileExt.equalsIgnoreCase("pdf"))) {	
									final File savedFile = new File(filefinalplaced+ File.separator + itemName);
									item.write(savedFile);
									String input = filefinalplaced + File.separator+ itemName;
									String itemoutname = itemName.replace("."+ strFileExt, "");
									String output = filefinalplaced+ File.separator + itemoutname + ".pdf";						
									imagetoPDF(input, output);// Converting Image To PDF							
									fileToSaveName = fileToSaveName + itemoutname+ ".pdf" + "|";
								} else {
									final File savedFile = new File(filefinalplaced+ File.separator + itemName);
									item.write(savedFile);
									fileToSaveName = fileToSaveName + itemName+ "|";
								}
						   } else {
							   checkFile=false;
									mapObject.put("alertMsg","Please Upload File formats pdf/jpeg/jpg/gif/png");
									mapObject.put("claimSubFlag", "N");
									  break;
						   }					
					}// end of while (iter.hasNext())
														
							if(checkFile==true){			
							/* -Marging All  PDF In Singal PDF-*/
							String filelist[] = fileToSaveName.split("\\|");
							PDFMergerUtility ut = new PDFMergerUtility();
							for (int i = 0; i < filelist.length; i++) {
								ut.addSource(filefinalplaced + File.separator+ filelist[i]);
							}
							finalFileName = ocoShortfallseqId.toString();
							ut.setDestinationFileName(filefinalplaced+ File.separator + finalFileName + ".pdf");
							ut.mergeDocuments();
							
								/*Saving To DataBase*/
							int read = 0;
							byte[] bytes = new byte[1024];
							InputStream uploadedInputStream = new FileInputStream(filefinalplaced + File.separator+ finalFileName + ".pdf");
							bytes = org.apache.commons.io.IOUtils.toByteArray(uploadedInputStream);

							webServiceManager = this.getWebServiceManagerObject();
							onlineIntimationVO.setShortfallSubmission(bytes);
							onlineIntimationVO.setShortfallSeqID(ocoShortfallseqId);
							onlineIntimationVO.setFileDataOInputStream(uploadedInputStream);
							onlineIntimationVO.setRemarks(ocoRemaks);
							alResult = webServiceManager.saveShortfallSubmission(onlineIntimationVO);
							if (alResult != null) {
								mapObject.put("shortfallSubmissionsFlag", "Y");
				 		        mapObject.put("alertMsg", "Updated Successfully");	

							} else {
								String errorMsg="Data Not inserted";
					              mapObject.put("shortfallSubmissionsFlag", "N");
					              mapObject.put("alertMsg",errorMsg);
							}
							
						}//End Of 	if(checkFile==true)
					}//End Of if (items != null)
				}//End OF if (ServletFileUpload.isMultipartContent(request)) {
		
			} catch (TTKException tte) {
					String errorMsg = "Exception occured";
					mapObject.put("shortfallSubmissionsFlag", "N");
					mapObject.put("alertMsg", errorMsg);
			} 		
			catch (FileNotFoundException FNFE){			
				if(checkFile==false){
					mapObject.put("alertMsg","Please Upload File formats pdf/jpeg/jpg/gif/png");
					mapObject.put("shortfallSubmissionsFlag", "N");			
				}else{	
				FNFE.printStackTrace();
				mapObject.put("shortfallSubmissionsFlag", "N");
				mapObject.put("alertMsg", "Unknown Error1");
				}
			}	
			catch (Exception e) {
				e.printStackTrace();
				mapObject.put("shortfallSubmissionsFlag", "N");
				mapObject.put("alertMsg", "Unknown Error2");
			}
			arra.put(mapObject);
			return arra.toString();
	}
		 
		    
		    ////////////////////////////////////MObile APP Phase 3 CC///////////////////////
		    
			////////////////////////////////////////////Provider login//////////////////////////////
			@POST
		    @Path("/ProviderNWtypeDataShare")
		    @Produces(MediaType.APPLICATION_JSON)
		    @Consumes(MediaType.APPLICATION_JSON)
		    public String getProviderNWtypeDataShare(@HeaderParam("ocoPolicyGrpseqid")String ocoPolicyGrpseqid){
			

		       JSONArray arra = new JSONArray();	        
		        LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
		        LinkedHashMap<String,Object > mapObject1 = new LinkedHashMap<String, Object>();
		       ArrayList<Object> SearchList = new ArrayList<Object>();
		        int SaveList;
		        ArrayList<Object> al=new ArrayList<Object>();
		        try {
		        	String PolicyGrpseqid=TTKCommon.checkNull(ocoPolicyGrpseqid);
		        	
		        	WebServiceManager webServiceManager=this.getWebServiceManagerObject();
		        	 
		        	if(PolicyGrpseqid.length() < 1){
		        		mapObject.put("providerFlag", "N");
		        		mapObject.put("alertMsg", "Please Enter Policy Group Seq Id "); 
		        	}
		        	
		        	else{
		            SearchList = webServiceManager.getProviderNWtype(PolicyGrpseqid);
		           // System.out.println("SearchList---->"+SearchList.size());
				            if(SearchList != null && SearchList.size() >0)
							  {
						            mapObject1.put("result", SearchList.get(0));
						            mapObject.put("providerFlag", (String) SearchList.get(1));
						            mapObject.put("alertMsg",(String) SearchList.get(2));
							}else{
				               	mapObject.put("providerFlag", "N");
				           		mapObject.put("alertMsg", "No data found");
				               }
		        	}
		        } 
		        catch (TTKException tte) {	        	
		        	
		        	try{
		        		 String errorMsg="Error While Searching Dashboard Data.....";
		                 mapObject.put("providerFlag", "N");
			             mapObject.put("alertMsg",errorMsg);  		
			            }catch(Exception ie) {
		            	ie.printStackTrace();
		            	mapObject.put("providerFlag", "N");
			        	mapObject.put("alertMsg", "Error While Searching Dashboard Data!.....");	
		            	 }
		            
		        }catch (Exception e) {
		        	e.printStackTrace();
		        	 mapObject.put("providerFlag", "N");
			        mapObject.put("alertMsg", "Error While Searching Dashboard Data?.....");	            
			   }
		       
		        arra.put(mapObject);
		        arra.put(mapObject1);
				return arra.toString();
		        
		    } 
	    
	 
			
			
			
			
			@POST
		    @Path("/partnerContactDetails")
		    @Produces(MediaType.APPLICATION_JSON)
		    @Consumes(MediaType.APPLICATION_JSON)			
			
			  public String getPartnerContact(@HeaderParam("ocoLanguage")String ocoLanguage){
				
				 File memberXMLFile=null;
				
				 JSONArray arra = new JSONArray();	
				 LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
			        LinkedHashMap<String,Object > mapObject1 = new LinkedHashMap<String, Object>();
			        String  jsonString="";
			        GeneralDetails generalDetails =new GeneralDetails();
			        ObjectMapper objectMapper=new ObjectMapper();				     
				     objectMapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
				 try {
					
					if("AR".equals(ocoLanguage)){
						
						 memberXMLFile=new File(TTKPropertiesReader.getPropertyValue("PartnerContactArabicXML"));
					}
					else{
						 memberXMLFile=new File(TTKPropertiesReader.getPropertyValue("PartnerContactXML"));
					}
					  
					   SAXReader reader = new SAXReader();
					     org.dom4j.Document document = (org.dom4j.Document) reader.read(memberXMLFile);
					     
					    String countryDescNode=document.selectSingleNode("//PartnerContactDetails/GeneralDetails/CountryDesc").getText();
					    String contryPhoneNoNode = document.selectSingleNode("//PartnerContactDetails/GeneralDetails/ContryPhoneNo").getText();  
					    String worldContctDescNode = document.selectSingleNode("//PartnerContactDetails/GeneralDetails/WorldContactInfoDesc").getText();
					    String worldContctPhNode = document.selectSingleNode("//PartnerContactDetails/GeneralDetails/WorldContactInfoNo").getText(); 
					    String emailIdDescNode = document.selectSingleNode("//PartnerContactDetails/GeneralDetails/EmailIdDesc").getText(); 
					    String emailIdNode = document.selectSingleNode("//PartnerContactDetails/GeneralDetails/EmailId").getText(); 
					    String globalPartnersDescNode = document.selectSingleNode("//PartnerContactDetails/GlobalPartnersDesc").getText(); 
					    String tableHeaderNode = document.selectSingleNode("//PartnerList/TableHeaders/PartnerNameDesc").getText(); 
					    String regionDescNode = document.selectSingleNode("//PartnerList/TableHeaders/RegionDesc").getText(); 
					    String contactListDescNode = document.selectSingleNode("//PartnerList/TableHeaders/ContactListDesc").getText(); 
					 
					    String addressDescNode = document.selectSingleNode("//AddressInfo/AddressDesc").getText(); 
					    String insCompanyNameNode = document.selectSingleNode("//AddressInfo/InsCompanyName").getText(); 
					    String poBoxandCitycNode = document.selectSingleNode("//AddressInfo/PoBoxandCity").getText(); 
					     
					    generalDetails.setCountryDesc(countryDescNode);
					    generalDetails.setContryPhoneNo(contryPhoneNoNode);
					    generalDetails.setWorldContactInfoDesc(worldContctDescNode);
					    generalDetails.setWorldContactInfoNo(worldContctPhNode);
					    generalDetails.setEmailIdDesc(emailIdDescNode);
					    generalDetails.setEmailId(emailIdNode); 
					    generalDetails.setGlobalPartnersDesc(globalPartnersDescNode);
					    generalDetails.setPartnerNameDesc(tableHeaderNode);
					    generalDetails.setRegionDesc(regionDescNode);
					    generalDetails.setContactListDesc(contactListDescNode);
					    generalDetails.setAddressDesc(addressDescNode);
					    generalDetails.setInsCompanyName(insCompanyNameNode);
					    generalDetails.setPoBoxandCity(poBoxandCitycNode);
					     
					    Partner partner=null;
					     List<Node> nodeList=document.selectNodes("//PartnerContactDetails/PartnerList/Partner");
					     if(nodeList!=null&&nodeList.size()>0){
					    	 int partnersSize=nodeList.size();
					    	 Partner[] partners=new Partner[partnersSize]; 
					    	 
					    	 for(int i=0;i<partnersSize;i++){
					    		Node  partnerNode=nodeList.get(i);
					    		String partnerName =partnerNode.selectSingleNode("./PartnerName").getText();
					    		  partner=new Partner();
					    		 partner.setPartnerName(partnerName);
					    		 
					    		 List<Node> regionNameNodeList= partnerNode.selectNodes("./Region/RegionName");
					    		 
					    		 if(regionNameNodeList!=null&&regionNameNodeList.size()>0){
					    			int rnSize= regionNameNodeList.size();
					    			 String[] regionNames=new String[rnSize];
					    			 for (int j=0;j<rnSize;j++){
					    			 Node regionNameNode=regionNameNodeList.get(j);
					    			 regionNames[j]=regionNameNode.getText();
					    			 }
					    		 
					    			 partner.setRegionNames(regionNames);
					    		 }
					    		 
					    		 List<Node> contactNodeList= partnerNode.selectNodes("./ContactInfoList/ContactNumber");
					    		 if(contactNodeList!=null&&contactNodeList.size()>0){
					    			 int contactSize= contactNodeList.size();
					    			 String[] contactList=new String[contactSize];
					    			 for (int k=0;k<contactSize;k++){
					    				 Node contactListNode=contactNodeList.get(k);
					    				 contactList[k]=contactListNode.getText();
					    			 }
					    			 partner.setContactNumber(contactList);
					    		 }
					    		 partners[i]=partner; 
					    	 }
					    	 generalDetails.setPartners(partners);
					     }
					     
					    generalDetails.setStatusCode(200);
					    generalDetails.setStatusDesc("SUCCESS");    
					    jsonString = objectMapper.writeValueAsString(generalDetails);
				 }
				catch(Exception e){
										e.printStackTrace();
										 generalDetails.setStatusCode(500);
										    generalDetails.setStatusDesc(e.getMessage());   
										    try{
										    jsonString = objectMapper.writeValueAsString(generalDetails);
										    }catch(Exception exception){
										    	exception.printStackTrace();
										    	return "ERROR";
										    }
				
				}
				
				
				 return jsonString;
			}
			
			 @POST
			    @Path("/sendMailConfirmation")
			    @Produces(MediaType.APPLICATION_JSON)
			    @Consumes(MediaType.APPLICATION_JSON)
			    public String getSendMailConfirmation(@HeaderParam("ocoMemberSeqIds")String ocoMemberSeqs){

					OnlineIntimationVO onlineIntimationVO = new OnlineIntimationVO();
			        JSONArray arra = new JSONArray();	
			        LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
			        LinkedHashMap<String, Object> mapObject1 = new LinkedHashMap<String, Object>();
			        ArrayList alResult =new ArrayList();
			        try {
			        	String memberSeqId = ocoMemberSeqs;
			        	if(memberSeqId==null||memberSeqId==""){
			        		mapObject.put("enrollFlag", "N");
			        		mapObject.put("alertMsg", "please select member");       		
			        	}
			        	else{
			        	   WebServiceManager webServiceManager=this.getWebServiceManagerObject();
			        	  int mailstatus=webServiceManager.sendCertificateMail(memberSeqId);
			        	if(mailstatus>0){
			        		mapObject.put("enrollFlag", "Y");
			        		mapObject.put("alertMsg", "Policy Certificate will be sent to the registered email id within 24 hours");  
			        	}
			        	else{
			        		mapObject.put("enrollFlag", "N");
			        		mapObject.put("alertMsg", "Mail sending failed");  
			        	}
			        }
			        	
			        }	
			        	catch (TTKException tte) {	        	
			        	
			        	try{
			        		String errorMsg="no data found";
			                 mapObject.put("enrollFlag", "N");
				             mapObject.put("alertMsg",errorMsg);
				        		
				            }catch(Exception ie) {
			            	ie.printStackTrace();
			            	mapObject.put("enrollFlag", "N");
				        	mapObject.put("alertMsg", "Unknown Error1");	
			            	 }
			            
			        }catch (Exception e) {
			        	
			        	e.printStackTrace();
			        	 mapObject.put("enrollFlag", "N");
				        mapObject.put("alertMsg", "Unknown Error2");	            
				   }
			        arra.put(mapObject);
					return arra.toString();
			        
			    }
			
			
			 @POST
			 @Path("/updateDependentDetails")
			 @Produces(MediaType.APPLICATION_JSON)
			 @Consumes(MediaType.APPLICATION_JSON)
			  public String updaeDependentDetails(@HeaderParam("ocoMemberSeq")Long ocoMemberSeq ,
			    		@HeaderParam("ocoEmailId")String ocoEmailId,
			    		@HeaderParam("ocoMobileNo")String ocoMobileNo
					  )
		    {
		    	 System.out.println("===============updateDependentDetails=================");
			 	OnlineIntimationVO onlineIntimationVO = new OnlineIntimationVO();
			       
			 	JSONArray arra = new JSONArray();	
			     LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
			     ArrayList alResult =new ArrayList();

			     try {
			    	    onlineIntimationVO.setMemberSeqID(ocoMemberSeq);
			  			onlineIntimationVO.setMobileNbr(ocoMobileNo);
			  			onlineIntimationVO.setEmailID(ocoEmailId);
			  			
			  			if(ocoMobileNo==null||ocoMobileNo==""){
			        		mapObject.put("enrollFlag", "N");
			        		mapObject.put("alertMsg", "Please enter Mobile Number");       		
			        	}
			  			else if(ocoEmailId==null||ocoEmailId==""){
			        		mapObject.put("enrollFlag", "N");
			        		mapObject.put("alertMsg", "Please enter Email ID");       		
			        	}
			  			else{
			  			
			         WebServiceManager webServiceManager=this.getWebServiceManagerObject();
			         ArrayList arra1 = webServiceManager.updateDependentDetails(onlineIntimationVO);
			         int result = (int) arra1.get(0);
			         
			            mapObject.put("myprofileFlag", "Y");
		 		        mapObject.put("alertMsg", "Updated Successfully");
			       
			  			}
			     }  catch (TTKException tte) {	        	
			     	
			     	try{
			     		String errorMsg="Data Not inserted";
			              mapObject.put("myprofileFlag", "N");
			              mapObject.put("alertMsg",errorMsg);
			         		
			             }catch(Exception ie) {
			         	ie.printStackTrace();
			         	mapObject.put("myprofileFlag", "N");
			         	mapObject.put("alertMsg", "Unknown Error1");	
			         	 }
			     }catch (Exception e) {
			     	e.printStackTrace();
			     	 mapObject.put("myprofileFlag", "N");
			         mapObject.put("alertMsg", "Unknown Error2");	            
			    }
			     arra.put(mapObject);
			   //  arra.put(mapObject1);
			 	return arra.toString();
			     
			 }    
			
			
			 @POST
			    @Path("/selectBenefitLimitMemberList")
			    @Produces(MediaType.APPLICATION_JSON)
			    @Consumes(MediaType.APPLICATION_JSON)
			    public String getBenefitLimitMemberList(@HeaderParam("ocoPolicyGroupSeq")Long ocoPolicyGroupSeq){
				

					OnlineIntimationVO onlineIntimationVO = new OnlineIntimationVO();
			        JSONArray arra = new JSONArray();	
			        LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
			        LinkedHashMap<String, Object> mapObject1 = new LinkedHashMap<String, Object>();
			        ArrayList alResult =new ArrayList();
			        try {
			 	        
			        	Long PolicyGroupSeq = ocoPolicyGroupSeq;
			        	if(PolicyGroupSeq.SIZE<1){
			        		
			        		mapObject.put("enrollFlag", "N");
			        		mapObject.put("alertMsg", "please enter PolicyGroupid");       		
			        	}else
			            {	
			        	
			        	onlineIntimationVO.setPolicyGrpSeqID(PolicyGroupSeq);
			            WebServiceManager webServiceManager=this.getWebServiceManagerObject();
			            alResult = webServiceManager.getDependentList(onlineIntimationVO);
			            JSONArray arra1 = new JSONArray(alResult);
			            
			    	     if(arra1 != null && arra1.length() >0)
						{
			    	    	mapObject1.put("result", arra1.get(0));
			    	    	 mapObject.put("alertMsg",(String) arra1.get(1));
			  	            mapObject.put("enrollFlag", (String) arra1.get(2));
			  	           
						
						}else{
			               	
			               	mapObject.put("enrollFlag", "N");
			           		mapObject.put("alertMsg", "No Data Found");
			               }
			            }
			            
			        } catch (TTKException tte) {	        	
			        	
			        	try{
			        		String errorMsg="no data found";
			                 mapObject.put("enrollFlag", "N");
				             mapObject.put("alertMsg",errorMsg);
				        		
				            }catch(Exception ie) {
			            	ie.printStackTrace();
			            	mapObject.put("enrollFlag", "N");
				        	mapObject.put("alertMsg", "Unknown Error1");	
			            	 }
			            
			        }catch (Exception e) {
			        	
			        	e.printStackTrace();
			        	 mapObject.put("enrollFlag", "N");
				        mapObject.put("alertMsg", "Unknown Error2");	            
				   }
			       
			        arra.put(mapObject);
			        arra.put(mapObject1);
					return arra.toString();
			        
			    }
			
			
			 
			 
			 
			 
			 
			 @POST
			    @Path("/selectBenefitType")
			    @Produces(MediaType.APPLICATION_JSON)
			    @Consumes(MediaType.APPLICATION_JSON)
					
				 public String getBenefitTypeList(){

			       JSONArray arra = new JSONArray();	        
			        LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
			        LinkedHashMap<String,Object > mapObject1 = new LinkedHashMap<String, Object>();
			       ArrayList<Object> SearchList = new ArrayList<Object>();
			        int SaveList;
			        ArrayList<Object> al=new ArrayList<Object>();
			        try {
			        	
			            WebServiceManager webServiceManager=this.getWebServiceManagerObject();
			          
			            SearchList = webServiceManager.getBenifitTypeListNew();
					            
					     	   if(SearchList != null && SearchList.size() >0)
								  {
							            mapObject1.put("result", SearchList.get(0));
							            mapObject.put("providerFlag", (String) SearchList.get(1));
							            mapObject.put("alertMsg",(String) SearchList.get(2));
								}
					     	  else{
					                	
					               	mapObject.put("providerFlag", "N");
					           		mapObject.put("alertMsg", "No data found");
					               }
			           
			        }
			        catch (TTKException tte) {	        	
			        	
			        	try{
			        		 String errorMsg="Error While Searching Dashboard Data.....";
			                 mapObject.put("providerFlag", "N");
				             mapObject.put("alertMsg",errorMsg);  		
				            }catch(Exception ie) {
			            	ie.printStackTrace();
			            	mapObject.put("providerFlag", "N");
				        	mapObject.put("alertMsg", "Error While Searching Dashboard Data!.....");	
			            	 }
			            
			        }catch (Exception e) {
			        	e.printStackTrace();
			        	 mapObject.put("providerFlag", "N");
				        mapObject.put("alertMsg", "Error While Searching Dashboard Data?.....");	            
				   }
			       
			        arra.put(mapObject); 
			        arra.put(mapObject1);
					return arra.toString();
			        
			    } 
					 
			 
			 
			 
			 
			 @POST
			    @Path("/selectMemberBenefitLimit")
			    @Produces(MediaType.APPLICATION_JSON)
			    @Consumes(MediaType.APPLICATION_JSON)
			    public String getMemberBenefitLimit(@HeaderParam("ocoMemberSeqId")Long ocoMemberSeqId,@HeaderParam("ocoBenefitType")String ocoBenefitType){
				

					OnlineIntimationVO onlineIntimationVO = new OnlineIntimationVO();
			        JSONArray arra = new JSONArray();	
			        LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
			        LinkedHashMap<String, Object> mapObject1 = new LinkedHashMap<String, Object>();
			        ArrayList alResult =new ArrayList();
			        try {
			 	        
			        	Long memberSeqID = ocoMemberSeqId;
			        	if(memberSeqID.SIZE<1){
			        		
			        		mapObject.put("enrollFlag", "N");
			        		mapObject.put("alertMsg", "please enter MemberSeqId");       		
			        	}else
			            {	
			        	
			        	onlineIntimationVO.setMemberSeqID(memberSeqID);
			        	onlineIntimationVO.setBenifitTypeVal(ocoBenefitType);;
			            WebServiceManager webServiceManager=this.getWebServiceManagerObject();
			            alResult = webServiceManager.getBenefitLimit(onlineIntimationVO);
			            JSONArray arra1 = new JSONArray(alResult);
			            
			    	     if(arra1 != null && arra1.length() >0)
						{
			    	    	mapObject1.put("result", arra1.get(0));
			    	    	 mapObject.put("alertMsg",(String) arra1.get(1));
			  	            mapObject.put("enrollFlag", (String) arra1.get(2));
			  	           
						
						}else{
			               	
			               	mapObject.put("enrollFlag", "N");
			           		mapObject.put("alertMsg", "No Data Found");
			               }
			            }
			            
			        } catch (TTKException tte) {	        	
			        	
			        	try{
			        		String errorMsg="no data found";
			                 mapObject.put("enrollFlag", "N");
				             mapObject.put("alertMsg",errorMsg);
				        		
				            }catch(Exception ie) {
			            	ie.printStackTrace();
			            	mapObject.put("enrollFlag", "N");
				        	mapObject.put("alertMsg", "Unknown Error1");	
			            	 }
			            
			        }catch (Exception e) {
			        	
			        	e.printStackTrace();
			        	 mapObject.put("enrollFlag", "N");
				        mapObject.put("alertMsg", "Unknown Error2");	            
				   }
			       
			        arra.put(mapObject);
			        arra.put(mapObject1);
					return arra.toString();
			        
			    }		 
			 
		
			 
			 @POST
			    @Path("/getForceUpdateAppVersion")
			    @Produces(MediaType.APPLICATION_JSON)
			    @Consumes(MediaType.APPLICATION_JSON)
					
				 public String getForceUpdateAppVersion(@HeaderParam("ocoApp_version")String app_version,@HeaderParam("ocoDevice_type")String device_type){
					
				 		System.out.println("********************API calling getForceUpdateAppVersion*********************");
					OnlineIntimationVO onlineIntimationVO = new OnlineIntimationVO();
			        JSONArray arra = new JSONArray();	
			        LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
			        LinkedHashMap<String, Object> mapObject1 = new LinkedHashMap<String, Object>();
			        ArrayList alResult =new ArrayList();
			        try {
			        	
			        	onlineIntimationVO.setApp_version(app_version);
			        	onlineIntimationVO.setDevice_type(device_type);
			            WebServiceManager webServiceManager=this.getWebServiceManagerObject();
			         String   forceUpdateFlag = webServiceManager.getAppVersionDetails(onlineIntimationVO);
					   
					   if("N".equals(forceUpdateFlag)){
						  
						   mapObject.put("SUCCESS","1");
						   mapObject.put("device_type",onlineIntimationVO.getDevice_type());
						   mapObject.put("force_update","0");
					   }
					   else{
						   mapObject.put("SUCCESS","1");
						   mapObject.put("device_type",onlineIntimationVO.getDevice_type());
						   mapObject.put("force_update","1"); 
					   }
			         
			             
			        } catch (TTKException tte) {	        	
			        	
			        	try{
			        		String errorMsg="no data found";
			                 mapObject.put("SUCCESS", "0");
				             mapObject.put("alertMsg",errorMsg);
				        		
				            }catch(Exception ie) {
			            	ie.printStackTrace();
			            	 mapObject.put("SUCCESS", "0");
				        	mapObject.put("alertMsg", "Unknown Error1");	
			            	 }
			            
			        }catch (Exception e) {
			        	
			        	e.printStackTrace();
			        	 mapObject.put("SUCCESS", "0");
				        mapObject.put("alertMsg", "Unknown Error2");	            
				   }
			       
			        arra.put(mapObject);
			       // arra.put(mapObject1);
					return arra.toString();
			        
			    } 
					 
	 
			 
			 
}//end of class
