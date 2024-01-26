/**
 *   @ (#) ChequeManagerBean.java June 07, 2006
 *   Project      : TTK HealthCare Services
 *   File         : ChequeManagerBean.java
 *   Author       : Balakrishna E
 *   Company      : Span Systems Corporation
 *   Date Created : June 07, 2006
 *
 *   @author       :  Balakrishna E
 *   Modified by   :
 *   Modified date :
 *   Reason        :
 */

package com.ttk.business.finance;

import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagementType;

import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.impl.finance.ChequeDAOImpl;
import com.ttk.dao.impl.finance.FinanceDAOFactory;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.finance.ChequeDetailVO;
import com.ttk.dto.finance.ChequeVO;

@Stateless
@javax.ejb.TransactionManagement(TransactionManagementType.BEAN)

public class ChequeManagerBean implements ChequeManager{

	private FinanceDAOFactory financeDAOFactory = null;
	private ChequeDAOImpl chequeDAOImpl = null;

	/**
	 * This method returns the instance of the data access object to initiate the required tasks
	 * @param strIdentifier String object identifier for the required data access object
	 * @return BaseDAO object
	 * @exception throws TTKException
	 */
	private BaseDAO getFinanceDAO(String strIdentifier) throws TTKException
	{
		try
		{
			if (financeDAOFactory == null)
				financeDAOFactory = new FinanceDAOFactory();
			if(strIdentifier!=null)
			{
				return financeDAOFactory.getDAO(strIdentifier);
			}//end of if(strIdentifier!=null)
			else
				return null;
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, "error."+strIdentifier+".dao");
		}//end of catch(Exception exp)
	}//End of getFinanceDAO(String strIdentifier)

	/**
	 * This method returns the ArrayList, which contains the ChequeVO's which are populated from the database
	 * @param alSearchCriteria ArrayList which contains Search Criteria
	 * @return ArrayList of ChequeVO'S object's which contains the details of the cheque
	 * @exception throws TTKException
	 */
	public ArrayList getChequeList(ArrayList alSearchCriteria) throws TTKException
	{
		chequeDAOImpl = (ChequeDAOImpl)this.getFinanceDAO("cheque");
		return chequeDAOImpl.getChequeList(alSearchCriteria);
	}//end of getChequeList(ArrayList alSearchCriteria)

	/**
	 * This method returns the ChequeDetailVO which contains the Cheque Detail information
	 * @param lngSeqID long value which contains cheque seq id to get the Cheque Detail information
	 * @param lngUserSeqID long value which contains Logged-in User
	 * @return ChequeDetailVO this contains the Cheque Detail information
	 * @exception throws TTKException
	 */
	public ChequeDetailVO getChequeDetail(Long lngSeqID, Long lngPaymentSeqId, Long lngFolatSeqId, Long lngUserSeqID) throws TTKException
	{
		chequeDAOImpl = (ChequeDAOImpl)this.getFinanceDAO("cheque");
		return chequeDAOImpl.getChequeDetail(lngSeqID,lngPaymentSeqId, lngFolatSeqId,lngUserSeqID);
	}//end of getChequeDetail(long lngSeqID,lngUserSeqID)

	/**
	 * This method saves the Cheque Detail information
	 * @param chequeDetailVO the object which contains the details of the Cheque
	 * @return long value which contains Cheque Seq ID
	 * @exception throws TTKException
	 */
	public long saveCheque(ChequeDetailVO chequeDetailVO) throws TTKException
	{
		chequeDAOImpl =(ChequeDAOImpl)this.getFinanceDAO("cheque");
		return chequeDAOImpl.saveCheque(chequeDetailVO);
	}//end of saveCheque(ChequeDetailVO chequeDetailVO)

	/**
	 * This method update the Cheque Detail information from maintenance-finance.
	 * @param chequeVO the object which contains the details of the Cheque
	 * @return int value which contains result
	 * @exception throws TTKException
	 */
	public int updateChequeInfo(ChequeVO chequeVO) throws TTKException
	{
		chequeDAOImpl =(ChequeDAOImpl)this.getFinanceDAO("cheque");
		return chequeDAOImpl.updateChequeInfo(chequeVO);
	}//end of updateChequeInfo(ChequeVO chequeVO) throws TTKException
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	**
    * This method Fetches the list of files uploaded details
    * @param mouDocumentVO mouDocumentVO object which contains the Tds Certificate details
    * @return int value, greater than zero indicates successful execution of the task
    * @exception throws TTKException
    */
	public ArrayList<Object> getBankDocsUploadsList(String claimSettNo) throws TTKException
   {
		chequeDAOImpl = (ChequeDAOImpl)this.getFinanceDAO("cheque");
		return chequeDAOImpl.getBankDocsUploadsList(claimSettNo);
   }//end of saveMouUploads(MOUDocumentVO mouDocumentVO,ArrayList alFileAUploadList) throws TTKException

	@Override
	public TTKReportDataSource doGeneratePaymenySummaryRpt( String parameterValue, String reportID) throws TTKException {
		chequeDAOImpl = (ChequeDAOImpl)this.getFinanceDAO("cheque");
		return chequeDAOImpl.doGeneratePaymenySummaryRpt(parameterValue,reportID);
	}

	@Override
	public Object getPartnersList() throws TTKException {
		chequeDAOImpl = (ChequeDAOImpl)this.getFinanceDAO("cheque");
		return chequeDAOImpl.getPartnersList();
	}

	@Override
	public TTKReportDataSource pendingMemberClaimsRpt(String parameterValue,String reportID) throws TTKException{
		chequeDAOImpl = (ChequeDAOImpl)this.getFinanceDAO("cheque");
		return chequeDAOImpl.pendingMemberClaimsRpt(parameterValue,reportID);
	}

	@Override
	public TTKReportDataSource pendingNetWorkClaimsRpt(String parameterValue,String reportID) throws TTKException {
		chequeDAOImpl = (ChequeDAOImpl)this.getFinanceDAO("cheque");
		return chequeDAOImpl.pendingNetWorkClaimsRpt(parameterValue,reportID);
	}

	@Override
	public TTKReportDataSource pendingPartnerClaimsRpt(String parameterValue,String reportID) throws TTKException {
		chequeDAOImpl = (ChequeDAOImpl)this.getFinanceDAO("cheque");
		return chequeDAOImpl.pendingPartnerClaimsRpt(parameterValue,reportID);
	}
	
	/**
     * This method deletes the particular TdsCertificate Info from the database,based on the certificate seq id's present in the arraylist
     * @param ArrayList alCertificateDelete
     * @return int It signifies the number of rows deleted in the database. 
     * @exception throws TTKException
     *//*
	public int deleteDocsUpload(ArrayList<Object> alCertificateDelete,String gateway) throws TTKException
	{
		chequeDAOImpl = (ChequeDAOImpl)this.getFinanceDAO("cheque");
		return chequeDAOImpl.deleteDocsUpload(alCertificateDelete,gateway);
	}//end of deleteAssocCertificatesInfo(ArrayList<Object> alCertificateDelete)
*/ 	
 		
	
	
	
	
	
	
	
	
	
}//end of ChequeManagerBean