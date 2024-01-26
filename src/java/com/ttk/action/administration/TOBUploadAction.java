/**
 * @ (#) TOBUpload.java   SEP 15, 2017
  */

package com.ttk.action.administration;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

import com.ttk.action.TTKAction;
import com.ttk.action.table.TableData;
import com.ttk.business.administration.ProdPolicyConfigManager;
import com.ttk.business.onlineforms.providerLogin.OnlineProviderManager;
import com.ttk.business.preauth.PreAuthManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.administration.PlanVO;
import com.ttk.dto.preauth.ShortfallVO;

import formdef.plugin.util.FormUtils;

/**
 * This class is used to get the List of Plan for the product.
 * This also provides addition of plan for products.
 */

public class TOBUploadAction extends TTKAction
{
	private static Logger log = Logger.getLogger( TOBUploadAction.class );

	//Action mapping forwards.
	private static final String strTOBDetails="tobDetails";
	private static final String strTOBClose="tobClose";
	private static final String strProduct="product";


	/**
	 * This method is used to get List of Plans for the Product.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward  doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception 
	{
		try
		{
			log.info("Inside the doDefault method of TOB Upload");
			setLinks(request);
			DynaActionForm frmTOBUpload =(DynaActionForm)form;
	   		ProdPolicyConfigManager prodPolicyConfigManager	=	this.getProductPolicyConfigObject();
			StringBuffer sbfCaption=new StringBuffer();
			String strActiveSubLink=TTKCommon.getActiveSubLink(request);
			sbfCaption = sbfCaption.append(this.buildCaption(strActiveSubLink,request));
			String strForwardPath=null;
			Long prodPolicySeqId = TTKCommon.getWebBoardId(request);
		/*	if("Products".equals(strActiveSubLink)){
				strForwardPath=strProductPlanList;
			}//end of if("Products".equals(strActiveSubLink))
			else {*/
				strForwardPath=strTOBDetails;
		//	}//end of else
		 PlanVO planVO =	prodPolicyConfigManager.getPolicyFileFromDB(prodPolicySeqId);
		 String successYN = planVO.getUploaddocumentflag();
		    frmTOBUpload.set("uploaddocumentflag", successYN);
			frmTOBUpload.set("caption",sbfCaption.toString());
		
			request.getSession().setAttribute("frmTOBUpload",frmTOBUpload);
			return this.getForward(strForwardPath, mapping, request);
		} // end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProduct));
		}//end of catch(Exception exp)
	} // end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception 

	/**
	 * This method is used toSave Uploaded data.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	
	 public ActionForward doUploadSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		   		HttpServletResponse response) throws Exception{
		   	try{
		   		setOnlineLinks(request);
		   		log.debug("Inside the doSaveShortfall method of ProviderAction");
		   		DynaActionForm frmTOBUpload = (DynaActionForm)form;
		   		ProdPolicyConfigManager prodPolicyConfigManager	=	this.getProductPolicyConfigObject();
				// UPLOAD FILE STARTS
				FormFile formFile = null;
				formFile = (FormFile)frmTOBUpload.get("file");	
			
			if(formFile.getContentType().equalsIgnoreCase("application/pdf"))
			{
				Long prodPolicySeqId = TTKCommon.getWebBoardId(request);
				Long userSeqId = TTKCommon.getUserSeqId(request);
				String successYN = prodPolicyConfigManager.TOBUpload(prodPolicySeqId,formFile,userSeqId);
				frmTOBUpload.set("uploaddocumentflag", successYN);
				if(successYN.equals("1"))request.setAttribute("updated","message.addedSuccessfully");
			}else{
				TTKException expTTK = new TTKException();
				expTTK.setMessage("error.Upload.required");
				throw expTTK;
			}

				request.getSession().setAttribute("frmTOBUpload", frmTOBUpload);
		   		return this.getForward(strTOBDetails, mapping, request);
		   	}//end of try
		   	catch(TTKException expTTK)
				{
					return this.processExceptions(request, mapping, expTTK);
				}//end of catch(TTKException expTTK)
				catch(Exception exp)
				{
					return this.processExceptions(request, mapping, new TTKException(exp,strProduct));
				}//end of catch(Exception exp)
		   }//end of doSaveShortfall(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	/**
	 * This method is used to navigate to previous screen when close  button is clicked.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try
		{
			log.debug("Inside the doClose method of PlanListAction");
			setLinks(request);
			return mapping.findForward(strTOBClose);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProduct));
		}//end of catch(Exception exp)
	}//end of doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	/**
	 * Returns the ProdPolicyConfigManager session object for invoking methods on it.
	 * @return productPolicyConfigManager session object which can be used for method invocation
	 * @exception throws TTKException
	 */
	private ProdPolicyConfigManager getProductPolicyConfigObject() throws TTKException
	{
		ProdPolicyConfigManager productPolicyConfigManager = null;
		try
		{
			if(productPolicyConfigManager == null)
			{
				InitialContext ctx = new InitialContext();
				productPolicyConfigManager = (ProdPolicyConfigManager) ctx.lookup("java:global/TTKServices/business.ejb3/ProdPolicyConfigManagerBean!com.ttk.business.administration.ProdPolicyConfigManager");
			}//end of if(productPolicyConfigManager == null)
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, "product");
		}//end of catch(Exception exp)
		return productPolicyConfigManager;
	}//end of getProductPolicyConfigObject()


	/**
	 * This method  prepares the Caption based on the flow and retunrs it
	 * @param strActiveSubLink current Active sublink
	 * @param request current HttpRequest
	 * @return String caption built
	 * @throws TTKException
	 */
	private String buildCaption(String strActiveSubLink,HttpServletRequest request)throws TTKException
	{
		StringBuffer sbfCaption=new StringBuffer();
		if(strActiveSubLink.equals("Products"))
		{
			//Comments the Database hit for Title Display.
			/*DynaActionForm frmProductDetail =(DynaActionForm)request.getSession().getAttribute("frmProductGeneralInfo");
			sbfCaption.append("[").append(frmProductDetail.getString("companyName")).append("]");
			sbfCaption.append("	[").append(frmProductDetail.getString("productName")).append("]");*/
			
			DynaActionForm frmProductList = (DynaActionForm)request.getSession().getAttribute("frmProductGeneralInfo");
			sbfCaption.append("[").append(frmProductList.get("companyName")).append("]");
			sbfCaption.append(" [").append(frmProductList.get("productName")).append("]");			
			
		}//end of if(strActiveSubLink.equals("Products"))
		else if(strActiveSubLink.equals("Policies"))
		{
			DynaActionForm frmPoliciesDetail =(DynaActionForm)request.getSession().getAttribute("frmPoliciesEdit");
			sbfCaption.append("[").append(frmPoliciesDetail.getString("companyName")).append("]");
			sbfCaption.append("	[").append(frmPoliciesDetail.getString("policyNbr")).append("]");
		}//end of else if(strActiveSubLink.equals("Policies"))
		return sbfCaption.toString();
	}//end of buildCaption(String strActiveSubLink,HttpServletRequest request)
	
	public ActionForward doRemoveDoc(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		try
		{
			ProdPolicyConfigManager prodPolicyConfigManager	=	this.getProductPolicyConfigObject();
	   		Long prodPolicySeqId = TTKCommon.getWebBoardId(request);
	   	    String successYN = prodPolicyConfigManager.deleteFileFromDB(prodPolicySeqId);
	   	 if(successYN.equals("1")){
				request.setAttribute("updated","message.removedSuccessfully");
		}else if(successYN.equals("0")){
			TTKException expTTK = new TTKException();
			expTTK.setMessage("error.file.not.available");
			throw expTTK;
		}
	   	return this.getForward(strTOBDetails, mapping, request);	
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProduct));
		}//end of catch(Exception exp)
	}//end of doRemoveDoc(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	public ActionForward doViewUploadDocs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws TTKException{
		
      
	    String strFileNoRecords = TTKPropertiesReader.getPropertyValue("GlobalDownload")+"/noRecordsFoundForPolicy.pdf";

		 ByteArrayOutputStream baos=null;
	    OutputStream sos = null;
	    FileInputStream fis = null; 
	    BufferedInputStream bis =null;
	   
	  try{   
		  
      	ProdPolicyConfigManager prodPolicyConfigManager	=	this.getProductPolicyConfigObject();
      	Long prodPolicySeqId = TTKCommon.getWebBoardId(request);	
			
      	PlanVO fileinByteStream = prodPolicyConfigManager.getPolicyFileFromDB(prodPolicySeqId);
      	String successYN = fileinByteStream.getUploaddocumentflag();
			  InputStream  iStream= (InputStream) fileinByteStream.getIpstrea();
			  if(successYN == "1"){
	        	 bis = new BufferedInputStream(iStream);
		         baos=new ByteArrayOutputStream();
	           response.setContentType("application/pdf");
	           int ch;
              while ((ch = bis.read()) != -1) baos.write(ch);
              sos = response.getOutputStream();
              baos.writeTo(sos);  
			  }else if(successYN == "0"){
	  				File f = new File(strFileNoRecords);
	  	    	  OutputStream os = response.getOutputStream();
	  	          byte[] buf = new byte[8192];
	  	          InputStream is = new FileInputStream(f);
	  	          int c = 0;
	  	          while ((c = is.read(buf, 0, buf.length)) > 0) {
	  	              os.write(buf, 0, c);
	  	              os.flush();
	  	          }
			    }
	        }catch(Exception exp)
		            	{
		            		return this.processExceptions(request, mapping, new TTKException(exp,null));
		            	}//end of catch(Exception exp)
		          finally{
		                   try{
		                         if(baos!=null)baos.close();                                           
		                         if(sos!=null)sos.close();
		                         if(bis!=null)bis.close();
		                         if(fis!=null)fis.close();
		                         }catch(Exception exception){
		                         log.error(exception.getMessage(), exception);
		                         }                     
		                 }
	      return null;		 
	
		
	}

}//end of class PlanListAction
