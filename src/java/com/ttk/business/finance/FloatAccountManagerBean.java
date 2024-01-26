/**
 *   @ (#) FloatAccountManagerBean.java June 07, 2006
 *   Project      : TTK HealthCare Services
 *   File         : FloatAccountManagerBean.java
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
import java.util.Date;
import java.util.HashMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagementType;

import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.impl.finance.DebitNoteDAOImpl;
import com.ttk.dao.impl.finance.FinanceDAOFactory;
import com.ttk.dao.impl.finance.FloatAccountDAOImpl;
import com.ttk.dao.impl.finance.InvoiceDAOImpl;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.finance.AssocGroupVO;
import com.ttk.dto.finance.ChequeDetailVO;
import com.ttk.dto.finance.DebitNoteVO;
import com.ttk.dto.finance.FloatAccountDetailVO;
import com.ttk.dto.finance.InvoiceVO;
import com.ttk.dto.finance.TransactionVO;

@Stateless
@javax.ejb.TransactionManagement(TransactionManagementType.BEAN)

public class FloatAccountManagerBean implements FloatAccountManager{

	private FinanceDAOFactory financeDAOFactory = null;
	private FloatAccountDAOImpl floatAccountDAOImpl = null;
	private DebitNoteDAOImpl debitNoteDAOImpl = null;
	private InvoiceDAOImpl invoiceDAO = null;

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
	 * This method returns the ArrayList, which contains the FloatAccountVO's which are populated from the database
	 * @param alSearchCriteria ArrayList which contains Search Criteria
	 * @return ArrayList of FloatAccountVO'S object's which contains the details of the Float Account
	 * @exception throws TTKException
	 */
	public ArrayList getFloatAccountList(ArrayList alSearchCriteria) throws TTKException
	{
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.getFloatAccountList(alSearchCriteria);
	}//end of getFloatAccountList(ArrayList alSearchCriteria)

	/**
	 * This method returns the FloatAccountDetailVO which contains the Float Account Detail information
	 * @param lngFloatAcctSeqID long value which contains FloatAccount seq id to get the Float Account Detail information
	 * @param lngUserSeqID long value which contains Logged-in User
	 * @return FloatAccountDetailVO this contains the Float Account Detail information
	 * @exception throws TTKException
	 */
	public FloatAccountDetailVO getFloatAccountDetail(long lngFloatAcctSeqID,long lngUserSeqID) throws TTKException
	{
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.getFloatAccountDetail(lngFloatAcctSeqID, lngUserSeqID);
	}//end of getFloatAccountDetail(long lngFloatAcctSeqID,long lngUserSeqID)

	/**
	 * This method saves the Float Account Detail information
	 * @param floatAccountDetailVO the object which contains the details of the Float account
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public long saveFloatAccount(FloatAccountDetailVO floatAccountDetailVO) throws TTKException
	{
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.saveFloatAccount(floatAccountDetailVO);
	}//end of saveFloatAccount(FloatAccountDetailVO floatAccountDetailVO)

	/**
	 * This method deletes the Float Account Detail information from the database
	 * @param alDeleteList ArrayList object which contains seq id for Finance flow to delete the Float Account information
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public int deleteFloatAccount(ArrayList alDeleteList) throws TTKException
	{
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.deleteFloatAccount(alDeleteList);
	}//end of deleteFloatAccount(ArrayList alDeleteList)

	/**
	 * This method returns the ArrayList, which contains the TransactionVO's which are populated from the database
	 * @param alSearchCriteria ArrayList which contains Search Criteria
	 * @return ArrayList of TransactionVO's object's which contains the details of the Float Transaction
	 * @exception throws TTKException
	 */
	public ArrayList getFloatTransactionList(ArrayList alSearchCriteria) throws TTKException
	{
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.getFloatTransactionList(alSearchCriteria);
	}//end of getFloatTransactionList(ArrayList alSearchCriteria)

	/**
	 * This method returns the TransactionVO which contains the Float transaction information
	 * @param lngFloatTransSeqID long value which contains float transaction seq id to get the Float Account Detail information
	 * @param lngFloatSeqID long value which contains float seq id to get the flaot account information
	 * @param lngUserSeqID long value which contains Logged-in User
	 * @return TransactionVO this contains the Transaction information
	 * @exception throws TTKException
	 */
	public TransactionVO getFloatTransactionDetail(long lngFloatTransSeqID, long lngFloatSeqID, long lngUserSeqID) throws TTKException
	{
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.getFloatTransactionDetail(lngFloatTransSeqID,lngFloatSeqID,lngUserSeqID);
	}//end of getFloatTransactionDetail(long lngFloatTransSeqID, long lngFloatSeqID, long lngUserSeqID)

	/**
	 * This method saves the Float Transaction information
	 * @param transactionVO the object which contains the details of the transaction
	 * @return long value contains Transaction Seq ID
	 * @exception throws TTKException
	 */
	public long saveFloatTransaction(TransactionVO transactionVO) throws TTKException
	{
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.saveFloatTransaction(transactionVO);
	}//end of saveFloatTransaction(TransactionVO transactionVO)

	/**
	 * This method returns the ArrayList, which contains the ChequeVO's which are populated from the database
	 * @param alSearchCriteria ArrayList which contains Search Criteria
	 * @return ArrayList of ChequeVO's object's which contains the details of the Claims
	 * @exception throws TTKException
	 */
	public ArrayList getClaimList(ArrayList alSearchCriteria) throws TTKException
	{
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.getClaimList(alSearchCriteria);
	}//end of getClaimList(ArrayList alSearchCriteria)

	/**
	 * This method returns the ChequeVO which contains the Claims information
	 * @param lngClaimSeqID long value which contains claims seq id to get the Claims information
	 * @param lngFloatAcctSeqID long value which contains FloatAccount seq id to get the Float Account Detail information
	 * @param lngUserSeqID long value which contains Logged-in User
	 * @return ChequeDetailVO this contains the Claims detail information
	 * @exception throws TTKException
	 */
	public ChequeDetailVO getClaimDetail(long lngClaimSeqID,long lngFloatAcctSeqID, long lngUserSeqID) throws TTKException
	{
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.getClaimDetail(lngClaimSeqID, lngFloatAcctSeqID, lngUserSeqID);
	}//end of getClaimDetailVO(long lngClaimSeqID, long lngFloatAcctSeqID long lngUserSeqID)

	/**
	 * This method saves the Claims information
	 * @param claimVO the object which contains the details of the Claim
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public int saveClaims(ChequeDetailVO chequeDetailVO) throws TTKException
	{
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.saveClaims(chequeDetailVO);
	}//end of saveClaims(ChequeDetailVO chequeDetailVO)

	/**
	 * This method generates the Cheque information
	 * @param alChequeList contains Seq ID's to Print the Cheque
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public TTKReportDataSource generateCheque(ArrayList alChequeList) throws TTKException
	{
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.generateCheque(alChequeList);
	}//end of generateCheque(ArrayList alChequeList)

	/**
     * This method saves the Reverse Transaction information
     * @param alReverseTransList arraylist which contains the details of Transaction Information
     * @return int the value which returns greater than zero for succcesssful execution of the task
     * @exception throws TTKException
     */
    public int reverseTransaction(ArrayList alReverseTransList) throws TTKException {
    	floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
    	return floatAccountDAOImpl.reverseTransaction(alReverseTransList);
    }//end of reverseTransaction(ArrayList alReverseTransList)

    /**
	 * This method returns the ArrayList, which contains the ChequeVO's which are populated from the database
	 * @param alSearchCriteria ArrayList which contains Search Criteria
	 * @return ArrayList of ChequeVO's object's which contains the details of the Payment Advice
	 * @exception throws TTKException
	 */
	public ArrayList getPaymentAdviceList(ArrayList alSearchCriteria) throws TTKException {
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.getPaymentAdviceList(alSearchCriteria);
	}//end of getPaymentAdviceList(ArrayList alSearchCriteria)
	
	//start changes for cr koc1103 and 1105
	/**
	 * This method returns the ArrayList, which contains the ChequeVO's which are populated from the database
	 * @param alSearchCriteria ArrayList which contains Search Criteria
	 * @return ArrayList of ChequeVO's object's which contains the details of the Payment Advice
	 * @exception throws TTKException
	 */
	public ArrayList getEftPaymentAdviceList(ArrayList alSearchCriteria) throws TTKException {
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.getEftPaymentAdviceList(alSearchCriteria);
	}//end of getPaymentAdviceList(ArrayList alSearchCriteria)
	/**
	 * This method returns the Void, which Generate mail
	 * @param ConcatenatedSeqID 
	 * @param lngUserSeqID
	 * @return  void
	 * @exception throws TTKException
	 */
	public void generateMail(String strConcatenatedSeqID,long lngUserSeqID)throws TTKException
	{
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		 floatAccountDAOImpl.generateMail(strConcatenatedSeqID,lngUserSeqID);
	}
	//end changes for cr koc1103 and 1105

	/**
	 * This method generates the Cheque information
	 * @param alChequeList contains Seq ID's to Print the Cheque
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public int generateChequeAdvice(ArrayList alChequeList) throws TTKException {
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.generateChequeAdvice(alChequeList);
	}//end of generateChequeAdvice(ArrayList alChequeList)

	/**
	 * This method generates the Cheque Advice XL information
	 * @param alChequeList contains Seq ID's to Print the Cheque
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public HashMap<String, TTKReportDataSource> generateChequeAdviceXL(ArrayList alChequeList) throws TTKException {
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.generateChequeAdviceXL(alChequeList);
	}//end of generateChequeAdviceXL(ArrayList alChequeList)
	
	
	public HashMap generateChequeAdviceENBDXL(ArrayList alChequeList) throws TTKException {
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.generateChequeAdviceENBDXL(alChequeList);
	}//end of generateChequeAdviceXL(ArrayList alChequeList)
	
	public HashMap generateChequeAdviceConsNew(ArrayList alChequeList) throws TTKException {
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.generateChequeAdviceConsNew(alChequeList);
	}//end of generateChequeAdviceXL(ArrayList alChequeList)
	
	
	
	public TTKReportDataSource generateChequeAdviceENBDConsXL(ArrayList alChequeList) throws TTKException {
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.generateChequeAdviceENBDConsXL(alChequeList);
	}//end of generateChequeAdviceENBDConsXL(ArrayList alChequeList)
	
	/**
	 * This method generates the Cheque Advice XL information
	 * @param alChequeList contains Seq ID's to Print the Cheque
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public TTKReportDataSource generateOICPaymentAdvice(ArrayList alChequeList) throws TTKException {
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.generateOICPaymentAdvice(alChequeList);
	}//end of generateOICPaymentAdvice(ArrayList alChequeList)
	
	
	/**
	 * This method generates the Cheque Advice Detail XL information
	 * @param alChequeList contains Seq ID's to Print the Cheque
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public TTKReportDataSource generateChequeAdviceXLDetails(ArrayList alChequeList) throws TTKException {
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.generateChequeAdviceXLDetails(alChequeList);
	}//end of generateChequeAdviceXLDetails(ArrayList alChequeList)
	
	// Change added for BOA CR KOC1220
	/**
	 * This method generates the Payment Advice Detail XL information
	 * @param alChequeList contains Seq ID's to Print the Cheque
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public TTKReportDataSource generateBOAXLDetails(ArrayList alChequeList) throws TTKException {
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.generateBOAXLDetails(alChequeList);
	}//end of generateBOAXLDetails(ArrayList alChequeList)
		

	/**
	 * This method returns the ArrayList, which contains the ChequeVO's which are populated from the database
	 * @param alSearchCriteria ArrayList which contains Search Criteria
	 * @return ArrayList of ChequeVO's object's which contains the details of the Payment Advice
	 * @exception throws TTKException
	 */
	public ArrayList getViewPaymentAdviceList(ArrayList alSearchCriteria) throws TTKException {
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.getViewPaymentAdviceList(alSearchCriteria);
	}//end of getViewPaymentAdviceList(ArrayList alSearchCriteria)

    /**
	 * This method generates the EFT information
	 * @param alChequeList contains Seq ID's to generate EFT information
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public int setFundTransfer(ArrayList alChequeList) throws TTKException{
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.setFundTransfer(alChequeList);
	}//end of setFundTransfer(ArrayList alChequeList)
	
	/**
	 * This method returns the ArrayList, which contains the DebitNoteVO's which are populated from the database
	 * @param alSearchCriteria ArrayList which contains Search Criteria
	 * @return ArrayList of DebitNoteVO's object's which contains the details of the Debit Note
	 * @exception throws TTKException
	 */
	public ArrayList getDebitNoteList(ArrayList alSearchCriteria) throws TTKException {
		debitNoteDAOImpl = (DebitNoteDAOImpl)this.getFinanceDAO("debitNote");
		return debitNoteDAOImpl.getDebitNoteList(alSearchCriteria);
	}//end of getDebitNoteList(ArrayList alSearchCriteria)
	
	/**
	 * This method returns the ArrayList, which contains the DebitNoteVO's which are populated from the database
	 * @param alSearchCriteria ArrayList which contains Search Criteria
	 * @return ArrayList of DebitNoteVO's object's which contains the details of the Debit Note
	 * @exception throws TTKException
	 */
	public ArrayList getBatchList(ArrayList alSearchCriteria) throws TTKException {
		debitNoteDAOImpl = (DebitNoteDAOImpl)this.getFinanceDAO("debitNote");
		return debitNoteDAOImpl.getBatchList(alSearchCriteria);
	}//end of getBatchList(ArrayList alSearchCriteria)
	
	/**
	 * This method deltes the Debit Note Deposit information
	 * @param strDebitSeqId String Object contains Pipe concatenated Debit Seq ID's
	 * @param lngUserSeqID long value contains User Seq ID
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public int deleteDebitNote(String strDebitSeqId,long lngUserSeqID) throws TTKException {
		debitNoteDAOImpl = (DebitNoteDAOImpl)this.getFinanceDAO("debitNote");
		return debitNoteDAOImpl.deleteDebitNote(strDebitSeqId,lngUserSeqID);
	}//end of deleteDebitNote(String strDebitSeqId,long lngUserSeqID)
	
	/**
	 * This method returns the DebitNoteVO which contains the Debit Note details
	 * @param lngDebitNoteSeqID long value which contains Debit Note seq id to get the Debit Note details
	 * @param lngUserSeqID long value which contains Logged-in User
	 * @return DebitNoteVO this contains the Debit Note details
	 * @exception throws TTKException
	 */
	public DebitNoteVO getDebitNoteDetail(long lngDebitNoteSeqID,long lngUserSeqID) throws TTKException {
		debitNoteDAOImpl = (DebitNoteDAOImpl)this.getFinanceDAO("debitNote");
		return debitNoteDAOImpl.getDebitNoteDetail(lngDebitNoteSeqID,lngUserSeqID);
	}//end of getDebitNoteDetail(long lngDebitNoteSeqID,long lngUserSeqID)
	
	/**
	 * This method saves the Debit Note details
	 * @param DebitNoteVO the object which contains Debit Note details
	 * @return long contains Debit Note Seq ID
	 * @exception throws TTKException
	 */
	public long saveDebitNoteDetail(DebitNoteVO debitNoteVO) throws TTKException {
		debitNoteDAOImpl = (DebitNoteDAOImpl)this.getFinanceDAO("debitNote");
		return debitNoteDAOImpl.saveDebitNoteDetail(debitNoteVO);
	}//end of saveDebitNoteDetail(DebitNoteVO debitNoteVO)
	
	/**
	 * This method returns the ArrayList, which contains the ChequeVO's which are populated from the database
	 * @param alSearchCriteria ArrayList which contains Search Criteria
	 * @return ArrayList of ChequeVO's object's which contains the details of the Claims
	 * @exception throws TTKException
	 */
	public ArrayList getDebitNoteClaimList(ArrayList alSearchCriteria) throws TTKException {
		debitNoteDAOImpl = (DebitNoteDAOImpl)this.getFinanceDAO("debitNote");
		return debitNoteDAOImpl.getDebitNoteClaimList(alSearchCriteria);
	}//end of getDebitNoteClaimList(ArrayList alSearchCriteria)
	
	/**
	 * This method Associates Claim(s) to Debit Note
	 * @param alClaimsList the object which contains the details of the Claims
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public int associateClaims(ArrayList alClaimsList) throws TTKException {
		debitNoteDAOImpl = (DebitNoteDAOImpl)this.getFinanceDAO("debitNote");
		return debitNoteDAOImpl.associateClaims(alClaimsList);
	}//end of associateClaims(ArrayList alClaimsList)
	
	/**
	 * This method returns the String which contains the Float Type ID for displaying the Transaction Type
	 * @param lngFloatSeqID long value which contains Float seq id to get the Float Type ID
	 * @return String this contains the Float Type ID
	 * @exception throws TTKException
	 */
	public String getFloatType(long lngFloatSeqID) throws TTKException {
		debitNoteDAOImpl = (DebitNoteDAOImpl)this.getFinanceDAO("debitNote");
		return debitNoteDAOImpl.getFloatType(lngFloatSeqID);
	}//end of getFloatType(long lngFloatSeqID)
	
	/**
	 * This method returns the ArrayList, which contains the DebitNoteDepositVO's which are populated from the database
	 * @param lngFloatSeqID long value which contains Float seq id to get the Debit Note details
	 * @param lngFloatTransSeqID long value which contains Float Transaction seq id to get the Debit Note details
	 * @return ArrayList of DebitNoteDepositVO's object's which contains the details of the Debit Note
	 * @exception throws TTKException
	 */
	public ArrayList<Object> getDebitNoteDepositList(long lngFloatSeqID,long lngFloatTransSeqID) throws TTKException {
		debitNoteDAOImpl = (DebitNoteDAOImpl)this.getFinanceDAO("debitNote");
		return debitNoteDAOImpl.getDebitNoteDepositList(lngFloatSeqID,lngFloatTransSeqID);
	}//end of getDebitNoteDepositList(long lngFloatSeqID,long lngFloatTransSeqID)
	
	/**
	 * This method saves the Debit Note Deposit information
	 * @param alDepositList ArrayList contains Debit Note Deposit information
	 * @param lngFloatTransSeqID long value contains Float Trans Seq ID
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public int saveDebitNoteDepositDetail(ArrayList alDepositList,long lngFloatTransSeqID) throws TTKException {
		debitNoteDAOImpl = (DebitNoteDAOImpl)this.getFinanceDAO("debitNote");
		return debitNoteDAOImpl.saveDebitNoteDepositDetail(alDepositList,lngFloatTransSeqID);
	}//end of saveDebitNoteDepositDetail(ArrayList alDepositList,long lngFloatTransSeqID)
	
	/**
	 * This method returns the ArrayList, which contains the ClaimDepositVO's which are populated from the database
	 * @param lngDebitSeqID long value which contains Debit seq id to get the Claim Deposit details
	 * @param lngFloatTransSeqID long value which contains Float Transaction seq id to get the Claim Deposit details
	 * @return ArrayList of ClaimDepositVO's object's which contains the details of the Claim Deposit
	 * @exception throws TTKException
	 */
	public ArrayList<Object> getClaimDepositList(long lngDebitSeqID,long lngFloatTransSeqID) throws TTKException {
		debitNoteDAOImpl = (DebitNoteDAOImpl)this.getFinanceDAO("debitNote");
		return debitNoteDAOImpl.getClaimDepositList(lngDebitSeqID,lngFloatTransSeqID);
	}//end of getClaimDepositList(long lngDebitSeqID,long lngFloatTransSeqID)
	
	/**
	 * This method saves the Claim(s) Deposit information
	 * @param alClaimDepositList ArrayList contains Claim(s) Deposit information
	 * @param lngDebitNoteTransSeqID Debit Note Trans Seq ID
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public int saveClaimDepositDetail(ArrayList alClaimDepositList,long lngDebitNoteTransSeqID) throws TTKException {
		debitNoteDAOImpl = (DebitNoteDAOImpl)this.getFinanceDAO("debitNote");
		return debitNoteDAOImpl.saveClaimDepositDetail(alClaimDepositList,lngDebitNoteTransSeqID);
	}//end of saveClaimDepositDetail(ArrayList alClaimDepositList,long lngDebitNoteTransSeqID)
	
	/**
     * This method returns the ArrayList, which contains the InvoiceVO's which are populated from the database
     * @param alSearchCriteria ArrayList which contains Search Criteria
     * @return ArrayList of InvoiceVO's object's which contains the details of the Invoice
     * @exception throws TTKException
     */
	public ArrayList getInvoiceList(ArrayList alSearchCriteria) throws TTKException {
		invoiceDAO = (InvoiceDAOImpl)this.getFinanceDAO("invoice");
		return invoiceDAO.getInvoiceList(alSearchCriteria);
	}//end of getInvoiceList(ArrayList alSearchCriteria)
	
	/**
	 * This method returns the InvoiceVO which contains the Invoice details
	 * @param lngInvoiceSeqID long value which contains invoice seq id to get the Invoice details
	 * @param lngUserSeqID long value which contains Logged-in User
	 * @return InvoiceVO this contains the Invoice details
	 * @exception throws TTKException
	 */
	public InvoiceVO getInvoiceDetail(long lngInvoiceSeqID,long lngUserSeqID) throws TTKException {
		invoiceDAO = (InvoiceDAOImpl)this.getFinanceDAO("invoice");
		return invoiceDAO.getInvoiceDetail(lngInvoiceSeqID,lngUserSeqID);
	}//end of getInvoiceDetail(long lngInvoiceSeqID,long lngUserSeqID)
	
	/**
	 * This method saves the Invoice details
	 * @param invoiceVO the object which contains Invoice details
	 * @return long contains Invoice Seq ID
	 * @exception throws TTKException
	 */
	public long saveInvoiceDetail(InvoiceVO invoiceVO) throws TTKException {
		invoiceDAO = (InvoiceDAOImpl)this.getFinanceDAO("invoice");
		return invoiceDAO.saveInvoiceDetail(invoiceVO);
	}//end of saveInvoiceDetail(InvoiceVO invoiceVO)
	
	/**
     * This method returns the ArrayList, which contains the InviceVO's which are populated from the database
     * @param alSearchCriteria ArrayList which contains Search Criteria
     * @return ArrayList of ChequeVO's object's which contains the details of the Invoice
     * @exception throws TTKException
     */
    public ArrayList getInvoicePolicyList(ArrayList alSearchCriteria) throws TTKException{
    	invoiceDAO = (InvoiceDAOImpl)this.getFinanceDAO("invoice");
        return invoiceDAO.getInvoicePolicyList(alSearchCriteria);
    }//end of getInvoicePolicyList(ArrayList alSearchCriteria)
    
    /**
	 * This method Associates Policies to the Invoice
	 * @param alPolicyList the object which contains the details of the Policies
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public int associatePolicy(ArrayList alPolicyList) throws TTKException {
		invoiceDAO = (InvoiceDAOImpl)this.getFinanceDAO("invoice");
		return invoiceDAO.associatePolicy(alPolicyList);
	}//end of associatePolicy(ArrayList alPolicyList)
	
	/**
	 * This method Associates/Excludes all the Policies to/from the Invoice
	 * @param alPolicyList the object which contains the details of the Policies
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public int associateAll(ArrayList alPolicyList) throws TTKException {
		invoiceDAO = (InvoiceDAOImpl)this.getFinanceDAO("invoice");
		return invoiceDAO.associateAll(alPolicyList);
	}//end of associateAll(ArrayList alPolicyList)
	
	/**
	 * This method returns the Max of Invoice to Date
	 * @return date the value which contains the Max of Invoice to Date
	 * @exception throws TTKException
	 */
	public Date getNewInvoiceFromDate() throws TTKException {
		invoiceDAO = (InvoiceDAOImpl)this.getFinanceDAO("invoice");
		return invoiceDAO.getNewInvoiceFromDate();
	}//end of getNewInvoiceFromDate()

    /**
     * This method returns the ArrayList, which contains the InviceVO's which are populated from the database
     * @param strPolicySeqID String which contains Policy Seq ID
     * @param strEnrollType String which contains Enrollment Type
     * @param lngAddedBy Long which contains Added User Seq ID
     * @return ArrayList of InviceVO's object's which contains the details of the Invice
     * @exception throws TTKException
     */
    public TTKReportDataSource getGenerateInvoiceList(String strPolicySeqID,String strEnrollType,Long lngAddedBy) throws TTKException{
    	invoiceDAO = (InvoiceDAOImpl)this.getFinanceDAO("invoice");
        return invoiceDAO.getGenerateInvoiceList(strPolicySeqID,strEnrollType,lngAddedBy);
    }//end of getInvoiceList(String strPolicySeqID)

    /**
     * This method deletes the Invoice Detail information from the database
     * @param alDeleteList ArrayList object which contains seq id for Invoice flow to delete the Invoice information
     * @return int the value which returns greater than zero for succcesssful execution of the task
     * @exception throws TTKException
     */
    public int deleteInvoice(ArrayList alDeleteList) throws TTKException
    {
    	invoiceDAO = (InvoiceDAOImpl)this.getFinanceDAO("invoice");
        return invoiceDAO.deleteInvoice(alDeleteList);
    }//end of deleteInvoice(ArrayList alDeleteList)

    /**
     * This method returns the ArrayList, which contains the InvoiceBatchVO's which are populated from the database
     * @param alSearchCriteria ArrayList which contains Search Criteria
     * @return ArrayList of InvoiceBatchVO's object's which contains the details of the Invoice
     * @exception throws TTKException
     */
    public ArrayList getViewInvoiceBatch(ArrayList alSearchCriteria) throws TTKException
    {
    	invoiceDAO = (InvoiceDAOImpl)this.getFinanceDAO("invoice");
        return invoiceDAO.getViewInvoiceBatch(alSearchCriteria);
    }//end of deleteInvoice(ArrayList alDeleteList)
    
    /**
	 * This method returns the String which contains the Batch file name to save the file with the Batch file name
	 * @param strBatchNo long value which contains Batch number to get the Batch file name
	 * @return String this contains the Batch file name
	 * @exception throws TTKException
	 */
	public String getBatchFileName(String strBatchNo) throws TTKException 
	{
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.getBatchFileName(strBatchNo);
	}//end of String getBatchFileName(String strBatchNo)
	
	/**
	 * This method returns the ArrayList, which contains the ChequeVO's which are populated from the database
	 * @param alSearchCriteria ArrayList which contains Search Criteria
	 * @return ArrayList of ChequeVO's object's which contains the details of the Claims
	 * @exception throws TTKException
	 */
	public ArrayList getBatchClaimList(ArrayList alSearchCriteria) throws TTKException{
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.getBatchClaimList(alSearchCriteria);
	}//end of getBatchClaimList(ArrayList alSearchCriteria)
	
	/**
	 * This method returns the ArrayList, which contains the AssocGroupVO's which are populated from the database.
	 * @param lngFloatSeqID Long which contains Search Criteria
	 * @return ArrayList of AssocGroupVO'S object's which contains the details of the Groups associated to Float Account
	 * @exception throws TTKException
	 */
	public ArrayList<Object> getFloatAssocGrpList(Long lngFloatSeqID) throws TTKException {
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.getFloatAssocGrpList(lngFloatSeqID);
	}//end of getFloatAssocGrpList(Long lngFloatSeqID)
	
	/**
	 * This method associates the Group information to Float account.
	 * @param assocGroupVO the object which contains the details of the Associate Group information
	 * @return int the value which returns greater than zero for successful execution of the task
	 * @exception throws TTKException
	 */
	public int saveFloatAssocGrp(AssocGroupVO assocGroupVO) throws TTKException {
		 floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		 return floatAccountDAOImpl.saveFloatAssocGrp(assocGroupVO);
	 }//end of saveFloatAssocGrp(AssocGroupVO assocGroupVO)

	/**
	 * This method deletes the Associated Group information from the database.
	 * @param lngFloatGrpAssocSeqID Long object which contains seq id for Finance flow to delete the Associate Group information
	 * @return int the value which returns greater than zero for successful execution of the task
	 * @exception throws TTKException
	 */
	public int deleteFloatAssocGrp(Long lngFloatGrpAssocSeqID) throws TTKException {
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.deleteFloatAssocGrp(lngFloatGrpAssocSeqID);
	}//end of deleteFloatAssocGrp(Long lngFloatGrpAssocSeqID)

	
	public long getENBDCountandAccNum(long alChequeList,String mode) throws TTKException {
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.getENBDCountandAccNum(alChequeList,mode);
		
	}
	
	/**
	 * This method returns the ArrayList, which contains the ChequeVO's which are populated from the database
	 * @param alSearchCriteria ArrayList which contains Search Criteria
	 * @return ArrayList of ChequeVO's object's which contains the details of the Claims
	 * @exception throws TTKException
	 */
	public ArrayList getExchangeRates(ArrayList alSearchCriteria) throws TTKException{
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.getExchangeRates(alSearchCriteria);
	}//end of getBatchClaimList(ArrayList alSearchCriteria)
	
	public ArrayList setPaymentUploadExcel(ArrayList<Object> alPrintCheque)throws TTKException
	{
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.setPaymentUploadExcel(alPrintCheque);
	}
	
	public ArrayList getLogDetailsExcelUpload(String startDate, String endDate,String flag)throws TTKException
	{
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.getLogDetailsExcelUpload(startDate,endDate,flag);
	}

	public ArrayList generateChequeAdviceUploadENBDXL(ArrayList<Object> alGenerateXL)throws TTKException
	{
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.generateChequeAdviceUploadENBDXL(alGenerateXL);
		
	}
	
	public ArrayList getLogDetailsPaymentAdviceExcelUpload(String startDate,String endDate)throws TTKException
	{
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.generateChequeAdviceUploadENBDXL(startDate,endDate);
	}

	@Override
	public ArrayList<Object> getSummaryPaymentUplad(String flag,int batchNo) throws TTKException {
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.getSummaryPaymentUplad(flag,batchNo);	
		}

	@Override
	public ArrayList getViewManualChequesList(ArrayList<Object> alSearchCriteria)throws TTKException {
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.getViewManualChequesList(alSearchCriteria);
	}

	@Override
	public ArrayList getBordereauxReportList(ArrayList<Object> searchData)throws TTKException {
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.getBordereauxReportList(searchData);
	}

	@Override
	public ArrayList generateBordereauxReport(ArrayList arrayList)throws TTKException {
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.generateBordereauxReport(arrayList);
	}
	
	@Override
	public ArrayList getBordereauxReportSearchList(ArrayList<Object> searchData)throws TTKException {
		floatAccountDAOImpl = (FloatAccountDAOImpl)this.getFinanceDAO("floataccount");
		return floatAccountDAOImpl.getBordereauxReportSearchList(searchData);
	}
}//end of FloatAccountManagerBean
