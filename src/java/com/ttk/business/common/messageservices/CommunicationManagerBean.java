/**
 * @ (#) CommunicationManagerBean.java 12th Jan 2006
 * Project      : TTK HealthCare Services
 * File         : CommunicationManagerBean.java
 * Author       : Balakrishna.E
 * Company      : Span Systems Corporation
 * Date Created : 12th Jan 2006
 *
 * @author       :
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.business.common.messageservices;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Logger;

import com.ttk.common.CurrencyConverterClient;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.common.messageservices.EmailHelper;
import com.ttk.common.messageservices.NotificationHelper;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.impl.common.CommunicationDAOFactory;
import com.ttk.dao.impl.common.CommunicationDAOImpl;
import com.ttk.dto.common.CommunicationOptionVO;

@Stateless
@javax.ejb.TransactionManagement(TransactionManagementType.BEAN)

public class CommunicationManagerBean implements CommunicationManager{

	private CommunicationDAOFactory communicationDAOFactory = null;
	private CommunicationDAOImpl communicationDAO = null;
	private static final Logger log = Logger.getLogger( CommunicationManagerBean.class);
	/**
	 * This method returns the instance of the data access object to initiate the required tasks
	 * @param strIdentifier String object identifier for the required data access object
	 * @return BaseDAO object
	 * @exception throws TTKException
	 */
	private BaseDAO getCommunicationDAO(String strIdentifier) throws TTKException
	{
		try
		{
			if(communicationDAOFactory == null)
			{
				communicationDAOFactory = new CommunicationDAOFactory();
			}//end of if (communicationDAOFactory == null)
			if(strIdentifier.equals("communication"))
			{
				return communicationDAOFactory.getDAO("communication");
			}//end of if(strIdentifier.equals("enrollment"))
			else
			{
				return null;
			}//end of else
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, "error."+strIdentifier+".dao");
		}//end of catch(Exception exp)
	}//End of getCommunicationDAO(String strIdentifier)

	/**
	 * This method calls the Notification methods based on Message Type and Message present status
	 * @param strMsgType The String Object which contains the type of the Message
	 * @exception throws TTKException
	 */
	public void sendMessage(String strMsgType) throws TTKException
	{
		ArrayList alMailList = new ArrayList();
		CommunicationOptionVO cOptionVO = new CommunicationOptionVO();
		try
		{
			alMailList = getSendMailList(strMsgType);
			//System.out.println("alMailList size::"+alMailList.size());
			//System.out.println("strMsgType"+strMsgType);
			if(alMailList != null)
            {
				//System.out.println("EMAIL message CLAIM count--->"+alMailList.size());
				for(int i=0;i<alMailList.size();i++)
				{
					cOptionVO = (CommunicationOptionVO)alMailList.get(i);
					if(TTKCommon.checkNull(cOptionVO.getMsgType()).equalsIgnoreCase("EMAIL") && TTKCommon.checkNull(cOptionVO.getPrsntYN()).equalsIgnoreCase("Y"))
					{
						try{
							//System.out.println("Email Count CLAIM count loop start--->>>>"+i);
						EmailHelper.message(cOptionVO);	
						//System.out.println("Email Count CLAIM count loop inside if after--->>>>"+i);
						}
						catch(Exception mex)
						{
							continue;
						}						
					}//end of if(TTKCommon.checkNull(cOptionVO.getMsgType()).equalsIgnoreCase("EMAIL") && TTKCommon.checkNull(cOptionVO.getPrsntYN()).equalsIgnoreCase("Y"))
				}//end for(int i=0;i<alMailList.size();i++)
			}//end of if(alMailList != null)
		}//end of try
		catch (Exception mex)
		{
			throw new TTKException(mex, "error.message");                                                    
		}//end of MessagingException block
	}//end of sendMessage(String strMsgType)
	
	
	/**
	 * This method calls the Notification methods based on Message Type and Message present status
	 * @param strMsgType The String Object which contains the type of the Message
	 * @exception throws TTKException
	 */
	public void sendMessagePreauth(String strMsgType) throws TTKException
	{
		ArrayList alMailList = new ArrayList();
		CommunicationOptionVO cOptionVO = new CommunicationOptionVO();
		try
		{
			alMailList = getSendMailListPreauth(strMsgType);
			//System.out.println("Email Count PREAUTH count--->>>>"+alMailList.size());
			
			if(alMailList != null)
			{
				for(int i=0;i<alMailList.size();i++)
				{
					try{
						//System.out.println("Email Count PREAUTH count loop start--->>>>"+i);
						cOptionVO = (CommunicationOptionVO)alMailList.get(i);
						if(TTKCommon.checkNull(cOptionVO.getMsgType()).equalsIgnoreCase("EMAIL") && TTKCommon.checkNull(cOptionVO.getPrsntYN()).equalsIgnoreCase("Y"))
						{
							//System.out.println("Email Count PREAUTH count loop inside if before--->>>>"+i);
							EmailHelper.message(cOptionVO);		
							//System.out.println("Email Count PREAUTH count loop inside if after--->>>>"+i);
	
						}//end of if(TTKCommon.checkNull(cOptionVO.getMsgType()).equalsIgnoreCase("EMAIL") && TTKCommon.checkNull(cOptionVO.getPrsntYN()).equalsIgnoreCase("Y"))
						//System.out.println("Email Count PREAUTH count loop End--->>>>"+i);
					}catch (Exception ex)
					{
						continue;
					}//end of MessagingException block
				}//end for(int i=0;i<alMailList.size();i++)
			}//end of if(alMailList != null)
		}//end of try
		catch (Exception mex)
		{
			throw new TTKException(mex, "error.message");                                                    
		}//end of MessagingException block
	}//end of sendMessage(String strMsgType)
	
	
	/**
	 * This method calls the Notification methods based on Message Type and Message present status
	 * @param strMsgType The String Object which contains the type of the Message
	 * @exception throws TTKException
	 */
	public void sendSMS(String strMsgType) throws TTKException
	{
		ArrayList alMailList = new ArrayList();
		CommunicationOptionVO cOptionVO = new CommunicationOptionVO();
		NotificationHelper notificationHelper = null;
		try
		{
			alMailList = getSendMailList(strMsgType);
			
			//System.out.println("alMailList:::::"+alMailList);
			if(alMailList != null)
			{  
				
			notificationHelper = new NotificationHelper();
				for(int i=0;i<alMailList.size();i++)
				{
					cOptionVO = (CommunicationOptionVO)alMailList.get(i);
				//	System.out.println("array fields::::"+alMailList.get(i));
					if(TTKCommon.checkNull(cOptionVO.getMsgType()).equalsIgnoreCase("SMS") && TTKCommon.checkNull(cOptionVO.getPrsntYN()).equalsIgnoreCase("Y"))
					{
						notificationHelper.sendSMS(cOptionVO);
						
					}//end of if(TTKCommon.checkNull(cOptionVO.getMsgType()).equalsIgnoreCase("EMAIL") && TTKCommon.checkNull(cOptionVO.getPrsntYN()).equalsIgnoreCase("Y"))
				}//end for(int i=0;i<alMailList.size();i++)
			}//end of if(alMailList != null)
		}//end of try
		catch (Exception mex)
		{
			throw new TTKException(mex, "error.message");
		}//end of MessagingException block
	}//end of sendSMS(String strMsgType)
	
	//sms preauth schedular
	
	public void sendSMSPreauth(String strMsgType) throws TTKException
	{
		ArrayList alMailList = new ArrayList();
		CommunicationOptionVO cOptionVO = new CommunicationOptionVO();
		NotificationHelper notificationHelper = null;
		try
		{
			alMailList = getSendMailListPreauth(strMsgType);
			if(alMailList != null)
			{
			notificationHelper = new NotificationHelper();
				for(int i=0;i<alMailList.size();i++)
				{
					cOptionVO = (CommunicationOptionVO)alMailList.get(i);
					if(TTKCommon.checkNull(cOptionVO.getMsgType()).equalsIgnoreCase("SMS") && TTKCommon.checkNull(cOptionVO.getPrsntYN()).equalsIgnoreCase("Y"))
					{
						notificationHelper.sendSMS(cOptionVO);
					}//end of if(TTKCommon.checkNull(cOptionVO.getMsgType()).equalsIgnoreCase("EMAIL") && TTKCommon.checkNull(cOptionVO.getPrsntYN()).equalsIgnoreCase("Y"))
				}//end for(int i=0;i<alMailList.size();i++)
			}//end of if(alMailList != null)
		}//end of try
		catch (Exception mex)
		{
			throw new TTKException(mex, "error.message");
		}//end of MessagingException block
	}//end of sendSMS(String strMsgType)
	
	//sms preauth schedular
	
	
	/*
	 * Get the live currency data from web service.
	 */
	public int updateLiveCurrencydata(Map<String,Object> currencyData,String strFlag) throws TTKException
	{
		communicationDAO = (CommunicationDAOImpl)this.getCommunicationDAO("communication");
		return communicationDAO.updateLiveCurrencydata(currencyData,strFlag);
	}//end of updateLiveCurrencydata(String strMsgType)
	
	public int updateLiveCurrencydata(ArrayList<String[]> alCurrency) throws TTKException
	{
		communicationDAO = (CommunicationDAOImpl)this.getCommunicationDAO("communication");
		return communicationDAO.updateLiveCurrencydata(alCurrency);
	}//end of updateLiveCurrencydata(String strMsgType)
	
	
	/**
	 * This method calls the Notification methods based on Message Type and Message present status
	 * @param strMsgType The String Object which contains the type of the Message
	 * @exception throws TTKException
	 */
	public void sendFax(String strMsgType) throws TTKException
	{
		ArrayList alMailList = new ArrayList();
		String strfuc = "";
		CommunicationOptionVO cOptionVO = new CommunicationOptionVO();
		NotificationHelper notificationHelper = null;
		try
		{
			alMailList = getSendMailList(strMsgType);
			if(alMailList != null)
			{
				notificationHelper = new NotificationHelper();
				for(int i=0;i<alMailList.size();i++)
				{
					cOptionVO = (CommunicationOptionVO)alMailList.get(i);
					if(TTKCommon.checkNull(cOptionVO.getMsgType()).equalsIgnoreCase("FAX") && TTKCommon.checkNull(cOptionVO.getPrsntYN()).equalsIgnoreCase("Y"))
					{
						if((TTKCommon.checkNull(cOptionVO.getFilePathName())!=null)){
							if(cOptionVO.getFilePathName().endsWith("pdf")){
								if((cOptionVO.getMsgID().equals("PREAUTH_SHORTFALL")) || (cOptionVO.getMsgID().equals("CLAIM_SHORTFALL")) || (cOptionVO.getMsgID().equals("PREAUTH_SHORTFALL_NHCP"))){
									strfuc = TTKPropertiesReader.getPropertyValue("shortfallrptdir")+cOptionVO.getFilePathName();
								}
								else if(cOptionVO.getMsgID().equals("PREAUTH_APPROVED") || cOptionVO.getMsgID().equals("PREAUTH_REJECTED") || 
										cOptionVO.getMsgID().equals("CLAIM_NHCP_APPROVE")|| cOptionVO.getMsgID().equals("PREAUTH_APPROVED_NHCP") || 
										cOptionVO.getMsgID().equals("PREAUTH_REJECTED_NHCP") ){
									strfuc = TTKPropertiesReader.getPropertyValue("authorizationrptdir")+cOptionVO.getFilePathName();
								}//end of if(cOptionVO.getMsgID().equals("PREAUTH_APPROVED"))
								File fileObj = new File(strfuc);
								if(fileObj.exists()){
									notificationHelper.sendFax(cOptionVO);
								}//end of if(fileObj.exists()) 
							}//end of if(cOptionVO.getFilePathName().endsWith("pdf"))
						}//end of if((TTKCommon.checkNull(cOptionVO.getFilePathName())!=null))
					}//end of if(TTKCommon.checkNull(cOptionVO.getMsgType()).equalsIgnoreCase("EMAIL") && TTKCommon.checkNull(cOptionVO.getPrsntYN()).equalsIgnoreCase("Y"))
				}//end for(int i=0;i<alMailList.size();i++)
			}//end of if(alMailList != null)
		}//end of try
		catch (Exception mex)
		{
			throw new TTKException(mex, "error.message");
		}//end of MessagingException block
	}//end of sendFax(String strMsgType)
	
	/**
     * This method returns the Arraylist of CommunicationOptionVO's  which contains list of notification records which will take care by Schedular
     * @return ArrayList of CommunicationOptionVO object which contains notification details
     * @exception throws TTKException
     */
    public ArrayList getSendMailList(String strMsgType) throws TTKException {
    	communicationDAO = (CommunicationDAOImpl)this.getCommunicationDAO("communication");
        return communicationDAO.getSendMailList(strMsgType);
    }//end of getSendMailList(String strMsgType)
    
    /**
     * This method returns the Arraylist of CommunicationOptionVO's  which contains list of notification records which will take care by Schedular
     * @return ArrayList of CommunicationOptionVO object which contains notification details
     * @exception throws TTKException
     */
    public ArrayList getSendMailListPreauth(String strMsgType) throws TTKException {
    	communicationDAO = (CommunicationDAOImpl)this.getCommunicationDAO("communication");
        return communicationDAO.getSendMailListPreauth(strMsgType);
    }//end of getSendMailList(String strMsgType)
    
    /**
     * This method gets Enrollment numbers from Card Details
     * @return String of strCardBatSeqID which contains CardBatSeqID
     * @exception throws TTKException
     */
    public void getEnrollNumbers(String strCardBatSeqID) throws TTKException{
    	communicationDAO = (CommunicationDAOImpl)this.getCommunicationDAO("communication");
        communicationDAO.getEnrollNumbers(strCardBatSeqID);
    }//end of getEnrollNumbers(String strCardBatSeqID)
    
    /**
     * This method inserts the Authorization Information
     * @param lngPatGenDtlSeqID PAT_GEN_DETAIL_SEQ_ID
     * @param strIdentifier Identifier for Preauth/Claim Approved/Rejected
     * @param lngUserID Long Which contains the User Seq ID
     * @exception throws TTKException
     */
    public void sendAuthorization(long lngPatGenDtlSeqID,String strIdentifier,Long lngUserID) throws TTKException {
    	communicationDAO = (CommunicationDAOImpl)this.getCommunicationDAO("communication");
    	communicationDAO.sendAuthorization(lngPatGenDtlSeqID,strIdentifier,lngUserID);
    }//end of sendAuthorization(long lngPatGenDtlSeqID,String strIdentifier,Long lngUserID)
    
  //added for Mail-SMS for Cigna
    /**
     * This method inserts the Authorization Information
     * @param lngPatGenDtlSeqID PAT_GEN_DETAIL_SEQ_ID
     * @param strIdentifier Identifier for Preauth/Claim Approved/Rejected
     * @param lngUserID Long Which contains the User Seq ID
     * @exception throws TTKException
     */
    public void sendCignaAuthorization(long lngPatGenDtlSeqID,String strIdentifier,Long lngUserID) throws TTKException {
    	communicationDAO = (CommunicationDAOImpl)this.getCommunicationDAO("communication");
    	communicationDAO.sendCignaAuthorization(lngPatGenDtlSeqID,strIdentifier,lngUserID);
    }//end of sendAuthorization(long lngPatGenDtlSeqID,String strIdentifier,Long lngUserID)
 

    /**
     * This method inserts the Online Information
     * @param strIdentifier Identifier for NEW_ONLINE_ENR_EMPLOYEE
     * @param strPolicyGrpSeqID PolicyGrpSeqID
     * @param lngUserID Long Which contains the User Seq ID
     * @exception throws TTKException
     */
    public void sendOnlineSoftCopy(String strIdentifier,long lngPatGenDtlSeqID,Long lngUserID) throws TTKException {
    	communicationDAO = (CommunicationDAOImpl)this.getCommunicationDAO("communication");
    	communicationDAO.sendOnlineSoftCopy(strIdentifier,lngPatGenDtlSeqID,lngUserID);
    }//end of sendOnlineSoftCopy(String strIdentifier,long lngPatGenDtlSeqID,Long lngUserID)
    
    /**
     * This method inserts the Jobs pending information 
     * @param strIdentifier Identifier for Jobstatus
     * @exception throws TTKException
     */
   public void insertRecord(String strIdentifier) throws TTKException {
    	communicationDAO = (CommunicationDAOImpl)this.getCommunicationDAO("communication");
    	communicationDAO.insertRecord(strIdentifier);
    }//end of insertRecord(String strIdentifier)
	 // Shortfall CR KOC1179
   /**
    * This method inserts the Email status for Shortfall Details 
    * @exception throws TTKException
    */
  public void sendShortfallDetails() throws TTKException {
   	communicationDAO = (CommunicationDAOImpl)this.getCommunicationDAO("communication");
   	communicationDAO.sendShortfallDetails();
   }//end of sendShortfallDetails()
    
//added for Cigna Mail Schedular
  public void sendCignaPreauthShorfallDetails() throws TTKException
  {
	  communicationDAO = (CommunicationDAOImpl)this.getCommunicationDAO("communication");
	  communicationDAO.sendCignaPreauthShorfallDetails();
  }//end of sendCignaPreauthShorfallDetails()
  

  public void sendClaimsBulkUploadEmailRpt(String msgID) throws TTKException
	{
		ArrayList alMailList = new ArrayList();
		CommunicationOptionVO cOptionVO = new CommunicationOptionVO();
		try
		{
			alMailList = getSendingMailList(msgID);
			if(alMailList != null)
			{
				for(int i=0;i<alMailList.size();i++)
				{
					try
					{
						cOptionVO = (CommunicationOptionVO)alMailList.get(i);
						if(TTKCommon.checkNull(cOptionVO.getMsgType()).equalsIgnoreCase("EMAIL"))
						{
							EmailHelper.message(cOptionVO);		
						}
					}
					catch (Exception ex)
					{
						continue;
					}
				}
			}
		}//end of try
		catch (Exception mex)
		{
			throw new TTKException(mex, "error.message");                                                    
		}
	}

  	public ArrayList getSendingMailList(String msgID) throws TTKException {
	  	communicationDAO = (CommunicationDAOImpl)this.getCommunicationDAO("communication");
	      return communicationDAO.getSendingMailList(msgID);
	  }
  
}//end of CommunicationManagerBean