package com.ttk.dao.impl.finance;

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;

import oracle.jdbc.driver.OracleTypes;
import oracle.jdbc.rowset.OracleCachedRowSet;

import org.apache.log4j.Logger;

import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.ResourceManager;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.finance.CollectionDetailsVO;
import com.ttk.dto.finance.CollectionsVO;

public class CollectionsDAOImpl implements BaseDAO {
	
	private static Logger log = Logger.getLogger(CollectionsDAOImpl.class );
	private static final String strCollectionsLis = "{CALL COLLECTION_PKG.SEARCH_POLICY_LIST(?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strGetInvoiceDetails ="{CALL COLLECTION_PKG.GET_INVOICE_LIST(?,?,?,?)}";
	private static final String strSaveCollection = "{CALL COLLECTION_PKG.SAVE_COLLECTION(?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strGetCollectionDetails = "SELECT COLLECTION_NUMBER,RECEIVED_AMOUNT,TO_CHAR(AMT_RECEIVED_DATE,'DD/MM/RRRR') AMT_RECEIVED_DATE,TRANSACTION_REF_NO,UPLOAD_FILE_NAME FROM TPA_COLLECTION_DTL WHERE COLLECTION_SEQ_ID = ?";
	private static final String strCollectionDetails = "{CALL COLLECTION_PKG.COLLECTION_LIST (?,?,?,?)}";
	
	private static final String strCollectionDtls = "{CALL COLLECTION_PKG.COLLECTION_REPORT(?,?)}";
	
	private static final String strReverseCollectionDtls =  "{CALL COLLECTION_PKG.REVERSE_COLLECTION(?,?,?,?)}";
	
	
	
	public ArrayList<CollectionsVO> getCollectionsList(ArrayList<Object> searchData) throws TTKException  {
		
		
		//System.out.println("Calling getCollectionsList  CollectionsDAOImpl");
		Collection<CollectionsVO> alResultList = new ArrayList<CollectionsVO>();
		Collection<CollectionsVO> addAllResultList = new ArrayList<CollectionsVO>();
		ResultSet rs = null;
		ResultSet rsSum = null;
		CollectionsVO collectionsVo = null;
		CollectionsVO collectiondetailsVo = null;
		try(Connection conn = ResourceManager.getConnection();
				CallableStatement cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCollectionsLis)
		) {
        	cStmtObject.setString(1,(String) searchData.get(0).toString().trim());// COR or IND
        	cStmtObject.setString(2,(String) searchData.get(1).toString().trim());//group name
        	cStmtObject.setString(3,(String) searchData.get(2).toString().trim());//group id
        	cStmtObject.setString(4,(String) searchData.get(3).toString().trim());//policy no
        	cStmtObject.setString(5,(String) searchData.get(4).toString().trim());//policy status
        	cStmtObject.setString(6,"POLICY_SEQ_ID");//
        	cStmtObject.setString(7,(String) searchData.get(6).toString().trim());
        	cStmtObject.setString(8,(String) searchData.get(7).toString().trim());
        	cStmtObject.setString(9,(String) searchData.get(8).toString().trim());
	        cStmtObject.registerOutParameter(10,OracleTypes.CURSOR);
	        cStmtObject.registerOutParameter(11,OracleTypes.CURSOR);
        	cStmtObject.execute();
	    rs = (java.sql.ResultSet)cStmtObject.getObject(10);
	    rsSum = (java.sql.ResultSet)cStmtObject.getObject(11);
	
	if(rs!=null){
		while(rs.next()){
			collectionsVo = new CollectionsVO();
			
			   collectionsVo.setGroupName(rs.getString("GROUP_NAME"));
		       collectionsVo.setLineOfBussiness(rs.getString("LINE_OF_BUSINESS"));
		       collectionsVo.setPolicySeqId(rs.getString("POLICY_SEQ_ID"));
		       collectionsVo.setPolicyNum(rs.getString("POLICY_NUMBER"));
		       collectionsVo.setStartDate(rs.getString("EFFECTIVE_FROM_DATE"));
		       collectionsVo.setEndDate(rs.getString("EFFECTIVE_TO_DATE"));
		       collectionsVo.setTotalPremiumQAR(rs.getString("TOT_PREMIUM"));
		       collectionsVo.setInvoiceAmount(rs.getString("TOT_INV_PREMIUM"));
		       collectionsVo.setTotalCollectionsQAR(rs.getString("TOT_COLLECTION"));
		       collectionsVo.setTotalOutstandingQAR(rs.getString("TOT_OUTSTANDING"));
		       alResultList.add(collectionsVo);
		}
	}
	
	return (ArrayList<CollectionsVO>) alResultList;
}

catch (SQLException sqlExp)
{
	throw new TTKException(sqlExp, "collections");
}//end of catch (SQLException sqlExp)
catch (Exception exp)
{
	throw new TTKException(exp, "collections");
}//end of catch (Exception exp)
	
}

	public CollectionsVO getPolicyDetails(String policySeqId) throws TTKException{
		
		//System.out.println("Calling getPolicyDetails  CollectionsDAOImpl  policySeqId==>"+policySeqId);
		Collection<Object> alResultList = new ArrayList<Object>();
		   Connection conn = null;
		    CallableStatement cStmtObject=null;
		    ResultSet rs = null;
		    ResultSet rsSum = null;
		    ResultSet rsInv = null;
		    CollectionsVO collectionsVo = new CollectionsVO();
		    
		    try {
		    	conn = ResourceManager.getConnection();
		    	cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetInvoiceDetails);
		    	cStmtObject.setLong(1, Long.parseLong(policySeqId));
				cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
				cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
				cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
				cStmtObject.execute();
				rs = (java.sql.ResultSet)cStmtObject.getObject(2);//policy details
				rsInv = (java.sql.ResultSet)cStmtObject.getObject(3);//invoice details
				rsSum = (java.sql.ResultSet)cStmtObject.getObject(4);//total sum 
				
				if(rs != null){
					while(rs.next()){
						   collectionsVo.setGroupName(rs.getString("GROUP_NAME"));
				    	   collectionsVo.setGroupId(rs.getString("GROUP_ID"));
					       collectionsVo.setPolicyNum(rs.getString("POLICY_NUMBER"));
					       collectionsVo.setLineOfBussiness(rs.getString("LINE_OF_BUSINESS"));
					       collectionsVo.setStartDate(rs.getString("EFFECTIVE_FROM_DATE"));
					       collectionsVo.setEndDate(rs.getString("EFFECTIVE_TO_DATE"));
				
				if(rsSum != null){
					while(rsSum.next()){
						collectionsVo.setTotalPremiumSum(rsSum.getString("TOT_INV_PREMIUM"));
						collectionsVo.setTotalCollectionsSum(rsSum.getString("TOTAL_COLLECTION"));
						collectionsVo.setTotalOutstandingSum(rsSum.getString("TOTAL_OUTSTANDING"));
						
					}
					}
					}
				}
				return collectionsVo;
			}
		    catch  (SQLException sqlExp) {
				throw new TTKException(sqlExp, "collections");
			}
		    catch (Exception exp)
		    {
		    	throw new TTKException(exp, "collections");
		    }//end of catch (Exception exp)
		    finally
		    {
		    	 //Nested Try Catch to ensure resource closure  
		    	try // First try closing the result set
		    	{
		    		try
		    		{
		    			if (rs != null) rs.close();
		    		}//end of try
		    		catch (SQLException sqlExp)
		    		{
		    			log.error("Error while closing the Resultset in CollectionsDAOImpl getInvoiceDetails()",sqlExp);
		    			throw new TTKException(sqlExp, "collections");
		    		}//end of catch (SQLException sqlExp)
		    		finally // Even if result set is not closed, control reaches here. Try closing the statement now.
		    		{
		    			try
		    			{
		    				if (cStmtObject != null) cStmtObject.close();
		    			}//end of try
		    			catch (SQLException sqlExp)
		    			{
		    				log.error("Error while closing the Statement in CollectionsDAOImpl getInvoiceDetails()",sqlExp);
		    				throw new TTKException(sqlExp, "collections");
		    			}//end of catch (SQLException sqlExp)
		    			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
		    			{
		    				try
		    				{
		    					if(conn != null) conn.close();
		    				}//end of try
		    				catch (SQLException sqlExp)
		    				{
		    					log.error("Error while closing the Connection in CollectionsDAOImpl getInvoiceDetails()",sqlExp);
		    					throw new TTKException(sqlExp, "collections");
		    				}//end of catch (SQLException sqlExp)
		    			}//end of finally Connection Close
		    		}//end of finally Statement Close
		    	}//end of try
		    	catch (TTKException exp)
		    	{
		    		throw new TTKException(exp, "collections");
		    	}//end of catch (TTKException exp)
		    	finally // Control will reach here in anycase set null to the objects 
		    	{
		    		rs = null;
		    		cStmtObject = null;
		    		conn = null;
		    	}//end of finally
		    }//end of finally	
		
		
	}


	public ArrayList<CollectionsVO> getInvoiceDetails(ArrayList<Object> searchData) throws TTKException {
		
		//System.out.println("Calling getInvoiceDetails  CollectionsDAOImpl ");
		Collection<Object> alResultList = new ArrayList<Object>();
		   Connection conn = null;
		    CallableStatement cStmtObject=null;
		    ResultSet rs = null;
		    ResultSet rsSum = null;
		    ResultSet rsInv = null;
		    CollectionsVO collectionsVo = null;
		    
		    try {
				
		    	conn = ResourceManager.getConnection();
		    	cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetInvoiceDetails);
		    	 String policySeqId = (String) searchData.get(0);
		    	
		    	cStmtObject.setLong(1,Long.parseLong(policySeqId));
				cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
				cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
				cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
				cStmtObject.execute();
				rs = (java.sql.ResultSet)cStmtObject.getObject(2);//policy details
				rsInv = (java.sql.ResultSet)cStmtObject.getObject(3);//invoice details
				rsSum = (java.sql.ResultSet)cStmtObject.getObject(4);//total sum 
				
				if(rsInv != null){
					while(rsInv.next()){
						      collectionsVo = new CollectionsVO();
						      collectionsVo.setInvoiceSeqId(rsInv.getString("INVOICE_SEQ_ID"));
					          collectionsVo.setInvoiceNo(rsInv.getString("INVOICE_NUMBER"));
					    	  collectionsVo.setInvoiceAmount(rsInv.getString("TOT_PREMIUM"));
					    	  collectionsVo.setInvoiceDate(rsInv.getString("INV_GEN_DATE"));
					    	  collectionsVo.setDueDate(rsInv.getString("DUE_DATE"));
					    	  collectionsVo.setTotalCollectionsQAR(rsInv.getString("TOT_COLLECTION"));
					    	  collectionsVo.setTotalOutstandingQAR(rsInv.getString("TOT_OUTSTANDING"));
					    	  collectionsVo.setMembersList("Download");
					    	  collectionsVo.setBatchName(rsInv.getString("BATCH_NAME"));
					    	  collectionsVo.setPolicyStatus(rsInv.getString("STATUS")); 
					    	
					    		   collectionsVo.setAdd("Add");
							       collectionsVo.setAddCollectionImage("AddIcon");
							       collectionsVo.setAddCollectionImageTitle("AddCollection");
					    	
					    	  alResultList.add(collectionsVo);
					  
					}
				}
			      
			}
		   catch  (SQLException sqlExp) {
				throw new TTKException(sqlExp, "collections");
			}
		    catch (Exception exp)
		    {
		    	throw new TTKException(exp, "collections");
		    }//end of catch (Exception exp)
		    finally
		    {
		    	 //Nested Try Catch to ensure resource closure  
		    	try // First try closing the result set
		    	{
		    		try
		    		{
		    			if (rs != null) rs.close();
		    		}//end of try
		    		catch (SQLException sqlExp)
		    		{
		    			log.error("Error while closing the Resultset in CollectionsDAOImpl getInvoiceDetails()",sqlExp);
		    			throw new TTKException(sqlExp, "collections");
		    		}//end of catch (SQLException sqlExp)
		    		finally // Even if result set is not closed, control reaches here. Try closing the statement now.
		    		{
		    			try
		    			{
		    				if (cStmtObject != null) cStmtObject.close();
		    			}//end of try
		    			catch (SQLException sqlExp)
		    			{
		    				log.error("Error while closing the Statement in CollectionsDAOImpl getInvoiceDetails()",sqlExp);
		    				throw new TTKException(sqlExp, "collections");
		    			}//end of catch (SQLException sqlExp)
		    			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
		    			{
		    				try
		    				{
		    					if(conn != null) conn.close();
		    				}//end of try
		    				catch (SQLException sqlExp)
		    				{
		    					log.error("Error while closing the Connection in CollectionsDAOImpl getInvoiceDetails()",sqlExp);
		    					throw new TTKException(sqlExp, "collections");
		    				}//end of catch (SQLException sqlExp)
		    			}//end of finally Connection Close
		    		}//end of finally Statement Close
		    	}//end of try
		    	catch (TTKException exp)
		    	{
		    		throw new TTKException(exp, "collections");
		    	}//end of catch (TTKException exp)
		    	finally // Control will reach here in anycase set null to the objects 
		    	{
		    		rs = null;
		    		cStmtObject = null;
		    		conn = null;
		    	}//end of finally
		    }//end of finally	
		
		return (ArrayList) alResultList;
	}


	public CollectionsVO getTotalPremiumDetails(ArrayList<Object> searchData)  throws TTKException {
		
		//System.out.println("Calling getCollectionsList  CollectionsDAOImpl");
		Collection<CollectionsVO> alResultList = new ArrayList<CollectionsVO>();
		Collection<CollectionsVO> addAllResultList = new ArrayList<CollectionsVO>();
		ResultSet rs = null;
		ResultSet rsSum = null;
		CollectionsVO collectionsVo = null;
		CollectionsVO collectiondetailsVo = null;
		
		
try(Connection conn = ResourceManager.getConnection();
	CallableStatement cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCollectionsLis)
		) {
	
        	cStmtObject.setString(1,(String) searchData.get(0).toString().trim());// COR or IND
        	cStmtObject.setString(2,(String) searchData.get(1).toString().trim());//group name
        	cStmtObject.setString(3,(String) searchData.get(2).toString().trim());//group id
        	cStmtObject.setString(4,(String) searchData.get(3).toString().trim());//policy no
        	cStmtObject.setString(5,(String) searchData.get(4).toString().trim());//policy status
        	cStmtObject.setString(6,"POLICY_SEQ_ID");//
        	cStmtObject.setString(7,(String) searchData.get(6).toString().trim());
        	cStmtObject.setString(8,(String) searchData.get(7).toString().trim());
        	cStmtObject.setString(9,(String) searchData.get(8).toString().trim());
	        cStmtObject.registerOutParameter(10,OracleTypes.CURSOR);
	        cStmtObject.registerOutParameter(11,OracleTypes.CURSOR);
        	cStmtObject.execute();
	    rs = (java.sql.ResultSet)cStmtObject.getObject(10);
	    rsSum = (java.sql.ResultSet)cStmtObject.getObject(11);
	
	if(rsSum!=null){
		while(rsSum.next()){
			collectiondetailsVo = new CollectionsVO();
			
			
			if(rsSum.getString("TOTAL_PREMIUM") !=null)
				collectiondetailsVo.setTotalPremiumSum(rsSum.getString("TOTAL_PREMIUM"));
			else
				collectiondetailsVo.setTotalPremiumSum("0");
			
			
			if(rsSum.getString("TOTAL_COLLECTION") !=null)
				collectiondetailsVo.setTotalCollectionsSum(rsSum.getString("TOTAL_COLLECTION"));
			else
				collectiondetailsVo.setTotalCollectionsSum("0");
			
			
			if(rsSum.getString("TOTAL_OUTSTANDING") !=null)
				collectiondetailsVo.setTotalOutstandingSum(rsSum.getString("TOTAL_OUTSTANDING"));
			else
				collectiondetailsVo.setTotalOutstandingSum("0");
			
		    alResultList.add(collectiondetailsVo);
		}
		}
	
	return collectiondetailsVo;
}

catch (SQLException sqlExp)
{
	throw new TTKException(sqlExp, "collections");
}//end of catch (SQLException sqlExp)
catch (Exception exp)
{
	throw new TTKException(exp, "collections");
}//end of catch (Exception exp)
	
}

	public TTKReportDataSource getExportReport(ArrayList<Object> alGenerateXL)throws TTKException {
	
		int iResult =0;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		ResultSet rsSum = null;
		OracleCachedRowSet crs = null;
        TTKReportDataSource reportDataSource =null;
        
        
        try {
        	conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCollectionsLis);
			cStmtObject.setString(1,(String) alGenerateXL.get(0).toString());// COR or IND
        	cStmtObject.setString(2,(String) alGenerateXL.get(1).toString().trim());//group name
        	cStmtObject.setString(3,(String) alGenerateXL.get(2).toString().trim());//group id
        	cStmtObject.setString(4,(String) alGenerateXL.get(3).toString().trim());//policy no
        	cStmtObject.setString(5,(String) alGenerateXL.get(4).toString().trim());//policy status
        	cStmtObject.setString(6,"POLICY_SEQ_ID");//ASC, 1, 101
        	cStmtObject.setString(7,"ASC");
        	cStmtObject.setString(8,"1");
        	cStmtObject.setString(9,"10000000");
	        cStmtObject.registerOutParameter(10,OracleTypes.CURSOR);
	        cStmtObject.registerOutParameter(11,OracleTypes.CURSOR);
        	cStmtObject.execute();
	       rs = (java.sql.ResultSet)cStmtObject.getObject(10);
	       rsSum = (java.sql.ResultSet)cStmtObject.getObject(11);
	       crs = new OracleCachedRowSet();
        	
	       if(rs !=null)
			{
				reportDataSource = new TTKReportDataSource();
				crs.populate(rs);
				reportDataSource.setData(crs);
			}//end of if(rs !=null)
        	
        	
		} catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "collections");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "collections");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */
			try // First try closing the result set
			{
				try
				{
					if (rs != null) rs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in CollectionsDAOImpl getExportReport()",sqlExp);
					throw new TTKException(sqlExp, "collections");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null)	cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in CollectionsDAOImpl generateOICPaymentAdvice()",sqlExp);
						throw new TTKException(sqlExp, "collections");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in CollectionsDAOImpl getExportReport()",sqlExp);
							throw new TTKException(sqlExp, "collections");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "collections");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return reportDataSource;
	}

	public Long saveCollection(CollectionsVO collectionsVO, InputStream inputStream, int formFileSize, String finalPath) throws TTKException{

		Long iResult = null;
	        Connection conn = null;
	        CallableStatement cStmtObject=null;
	        
		try {
			 conn = ResourceManager.getConnection();
	            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveCollection);
	            
	          /* if(collectionsVO.getCollectionsSeqId() != null){
					cStmtObject.setLong(1,collectionsVO.getCollectionsSeqId());
				}//end of if(collectionsVO.getCollectionsSeqId() != null)
				else{*/
					cStmtObject.setLong(1,0);
				//}
	            cStmtObject.setString(2, collectionsVO.getInvoiceSeqId().trim());
	            cStmtObject.setString(3, collectionsVO.getInvoiceNo().trim());
	            cStmtObject.setString(4, collectionsVO.getAmountReceivedQAR().trim());
	            cStmtObject.setString(5, collectionsVO.getAmountDueQAR().trim());
	            cStmtObject.setString(6, collectionsVO.getReceivedDate().trim());
	            cStmtObject.setString(7, collectionsVO.getTransactionRef().trim());
	            cStmtObject.setLong(8, collectionsVO.getUpdatedBy());
	            cStmtObject.setString(9, finalPath);
				cStmtObject.setBinaryStream(10, inputStream, formFileSize);
	            cStmtObject.registerOutParameter(11,Types.INTEGER);//ROW_PROCESSED
	            cStmtObject.registerOutParameter(1,Types.BIGINT);
	            cStmtObject.execute(); 
	            
	            iResult = (long) cStmtObject.getInt(1);//ROW_PROCESSED
		} 
		 catch (SQLException sqlExp) 
	        {
	            throw new TTKException(sqlExp, "collections");
	        }//end of catch (SQLException sqlExp)
	        catch (Exception exp) 
	        {
	            throw new TTKException(exp, "collections");
	        }//end of catch (Exception exp) 
	        finally
			{
	        	/* Nested Try Catch to ensure resource closure */ 
	        	try // First try closing the Statement
	        	{
	        		try
	        		{
	        			if (cStmtObject != null) cStmtObject.close();
	        		}//end of try
	        		catch (SQLException sqlExp)
	        		{
	        			log.error("Error while closing the Statement in CollectionsDAOImpl saveCollection()",sqlExp);
	        			throw new TTKException(sqlExp, "collections");
	        		}//end of catch (SQLException sqlExp)
	        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	        		{
	        			try
	        			{
	        				if(conn != null) conn.close();
	        			}//end of try
	        			catch (SQLException sqlExp)
	        			{
	        				log.error("Error while closing the Connection in CollectionsDAOImpl saveCollection()",sqlExp);
	        				throw new TTKException(sqlExp, "collections");
	        			}//end of catch (SQLException sqlExp)
	        		}//end of finally Connection Close
	        	}//end of try
	        	catch (TTKException exp)
	        	{
	        		throw new TTKException(exp, "collections");
	        	}//end of catch (TTKException exp)
	        	finally // Control will reach here in anycase set null to the objects 
	        	{
	        		cStmtObject = null;
	        		conn = null;
	        	}//end of finally
			}//end of finally
		
		return iResult;
	}

	public CollectionsVO getCollectionData(long collectionSeqId) throws TTKException{
		
		//System.out.println("Calling getCollectionData  CollectionsDAOImpl  collectionSeqId==>"+collectionSeqId);
		Collection<Object> alResultList = new ArrayList<Object>();
		   Connection conn = null;
		   PreparedStatement statement=null;
		    ResultSet rs = null;
		    CollectionsVO collectionsVo = new CollectionsVO();
		    
		    try {
		    	conn = ResourceManager.getConnection();
		    	statement = (java.sql.CallableStatement)conn.prepareCall(strGetCollectionDetails);
		    	statement.setLong(1, collectionSeqId);
		    	statement.execute();
				rs=statement.executeQuery();
				while(rs.next())
				{
					collectionsVo.setCollectionNumber(TTKCommon.checkNull(rs.getString("COLLECTION_NUMBER")));
					collectionsVo.setAmountReceivedQAR(TTKCommon.checkNull(rs.getString("RECEIVED_AMOUNT")));
					collectionsVo.setReceivedDate(TTKCommon.checkNull(rs.getString("AMT_RECEIVED_DATE")));
					collectionsVo.setTransactionRef(TTKCommon.checkNull(rs.getString("TRANSACTION_REF_NO")));
					collectionsVo.setFileName(TTKCommon.checkNull(rs.getString("UPLOAD_FILE_NAME")));
				}
				return collectionsVo;
			}
		    catch  (SQLException sqlExp) {
				throw new TTKException(sqlExp, "collections");
			}
		    catch (Exception exp)
		    {
		    	throw new TTKException(exp, "collections");
		    }//end of catch (Exception exp)
		    finally
		    {
		    	 //Nested Try Catch to ensure resource closure  
		    	try // First try closing the result set
		    	{
		    		try
		    		{
		    			if (rs != null) rs.close();
		    		}//end of try
		    		catch (SQLException sqlExp)
		    		{
		    			log.error("Error while closing the Resultset in CollectionsDAOImpl getCollectionData()",sqlExp);
		    			throw new TTKException(sqlExp, "collections");
		    		}//end of catch (SQLException sqlExp)
		    		finally // Even if result set is not closed, control reaches here. Try closing the statement now.
		    		{
		    			try
		    			{
		    				if (statement != null) statement.close();
		    			}//end of try
		    			catch (SQLException sqlExp)
		    			{
		    				log.error("Error while closing the Statement in CollectionsDAOImpl getCollectionData()",sqlExp);
		    				throw new TTKException(sqlExp, "collections");
		    			}//end of catch (SQLException sqlExp)
		    			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
		    			{
		    				try
		    				{
		    					if(conn != null) conn.close();
		    				}//end of try
		    				catch (SQLException sqlExp)
		    				{
		    					log.error("Error while closing the Connection in CollectionsDAOImpl getCollectionData()",sqlExp);
		    					throw new TTKException(sqlExp, "collections");
		    				}//end of catch (SQLException sqlExp)
		    			}//end of finally Connection Close
		    		}//end of finally Statement Close
		    	}//end of try
		    	catch (TTKException exp)
		    	{
		    		throw new TTKException(exp, "collections");
		    	}//end of catch (TTKException exp)
		    	finally // Control will reach here in anycase set null to the objects 
		    	{
		    		rs = null;
		    		statement = null;
		    		conn = null;
		    	}//end of finally
		    }//end of finally	
		
		
	}

	public ArrayList<CollectionsVO> getCollectionDetailsList(ArrayList<Object> alSearchParams) throws TTKException{
		
		//System.out.println("Calling getCollectionDetailsList  CollectionsDAOImpl");
		Collection<CollectionsVO> alResultList = new ArrayList<CollectionsVO>();
		Collection<CollectionsVO> addAllResultList = new ArrayList<CollectionsVO>();
		ResultSet rs = null;
		ResultSet rsSum = null;
		CollectionsVO collectionsVo = null;
		CollectionsVO collectiondetailsVo = null;
		try(Connection conn = ResourceManager.getConnection();
				CallableStatement cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCollectionDetails)
		     ) {
        	cStmtObject.setString(1,(String) alSearchParams.get(0).toString().trim());//invoice seq id
        	cStmtObject.setString(2,(String) alSearchParams.get(1).toString().trim());//invoice num 
	        cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
	        cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
        	cStmtObject.execute();
	    rs = (java.sql.ResultSet)cStmtObject.getObject(3);
	    rsSum = (java.sql.ResultSet)cStmtObject.getObject(4);
	if(rs!=null){
		while(rs.next()){
			collectionsVo = new CollectionsVO();
			
			   collectionsVo.setCollectionNumber(rs.getString("COLLECTION_NUMBER"));
		       collectionsVo.setCollectionsSeqId(rs.getLong("COLLECTION_SEQ_ID"));
		       collectionsVo.setInvoiceNo(rs.getString("INVOICE_NUMBER"));
		       collectionsVo.setAmountReceivedQAR(rs.getString("RECEIVED_AMOUNT"));
		       collectionsVo.setReceivedDate(rs.getString("AMT_RECEIVED_DATE"));
		       collectionsVo.setTransactionRef(rs.getString("TRANSACTION_REF_NO"));
		       collectionsVo.setDeleteYN(rs.getString("DELETED_YN"));
		       collectionsVo.setRemove("Reverse");
		       collectionsVo.setRemoveCollectionImage("DeleteIcon");
		       collectionsVo.setRemoveCollectionImageTitle("RemoveCollection");
		       collectionsVo.setFileName(TTKCommon.checkNull(rs.getString("UPLOAD_FILE_NAME")));
		       
		       alResultList.add(collectionsVo);
		}
	}
	
	return (ArrayList<CollectionsVO>) alResultList;
}

catch (SQLException sqlExp)
{
	throw new TTKException(sqlExp, "collections");
}//end of catch (SQLException sqlExp)
catch (Exception exp)
{
	throw new TTKException(exp, "collections");
}//end of catch (Exception exp)
	
}

	public TTKReportDataSource downLoadCollectionDtls(ArrayList<Object> alGenerateXL) throws TTKException {
	
		int iResult =0;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		ResultSet rsSum = null;
		OracleCachedRowSet crs = null;
        TTKReportDataSource reportDataSource =null;
        
        try {
        	conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCollectionDtls);
			cStmtObject.setString(1,(String) alGenerateXL.get(0).toString().trim());// COR or IND
	        cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
        	cStmtObject.execute();
	       rs = (java.sql.ResultSet)cStmtObject.getObject(2);
	       crs = new OracleCachedRowSet();
        	
	       if(rs !=null)
			{
				reportDataSource = new TTKReportDataSource();
				crs.populate(rs);
				reportDataSource.setData(crs);
			}//end of if(rs !=null)
        	
        	
		} catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "collections");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "collections");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */
			try // First try closing the result set
			{
				try
				{
					if (rs != null) rs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in CollectionsDAOImpl downLoadCollectionDtls()",sqlExp);
					throw new TTKException(sqlExp, "collections");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null)	cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in CollectionsDAOImpl downLoadCollectionDtls()",sqlExp);
						throw new TTKException(sqlExp, "collections");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in CollectionsDAOImpl downLoadCollectionDtls()",sqlExp);
							throw new TTKException(sqlExp, "collections");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "collections");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return reportDataSource;
	}

	public int doReverseTrasaction(CollectionsVO collectionsVO)  throws TTKException{

		int iResult = 0;
	        Connection conn = null;
	        CallableStatement cStmtObject=null;
	        
		try {
			 conn = ResourceManager.getConnection();
	            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strReverseCollectionDtls);
	            
				cStmtObject.setLong(1,collectionsVO.getCollectionsSeqId());
	            cStmtObject.setString(2, collectionsVO.getDeletionRemarks().trim());
	            cStmtObject.setLong(3, collectionsVO.getUpdatedBy());
	            cStmtObject.registerOutParameter(4,Types.INTEGER);//ROW_PROCESSED
	            cStmtObject.execute(); 
	            
	            iResult = cStmtObject.getInt(4);//ROW_PROCESSED
		} 
		 catch (SQLException sqlExp) 
	        {
	            throw new TTKException(sqlExp, "collections");
	        }//end of catch (SQLException sqlExp)
	        catch (Exception exp) 
	        {
	            throw new TTKException(exp, "collections");
	        }//end of catch (Exception exp) 
	        finally
			{
	        	/* Nested Try Catch to ensure resource closure */ 
	        	try // First try closing the Statement
	        	{
	        		try
	        		{
	        			if (cStmtObject != null) cStmtObject.close();
	        		}//end of try
	        		catch (SQLException sqlExp)
	        		{
	        			log.error("Error while closing the Statement in CollectionsDAOImpl doReverseTrasaction()",sqlExp);
	        			throw new TTKException(sqlExp, "collections");
	        		}//end of catch (SQLException sqlExp)
	        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	        		{
	        			try
	        			{
	        				if(conn != null) conn.close();
	        			}//end of try
	        			catch (SQLException sqlExp)
	        			{
	        				log.error("Error while closing the Connection in CollectionsDAOImpl doReverseTrasaction()",sqlExp);
	        				throw new TTKException(sqlExp, "collections");
	        			}//end of catch (SQLException sqlExp)
	        		}//end of finally Connection Close
	        	}//end of try
	        	catch (TTKException exp)
	        	{
	        		throw new TTKException(exp, "collections");
	        	}//end of catch (TTKException exp)
	        	finally // Control will reach here in anycase set null to the objects 
	        	{
	        		cStmtObject = null;
	        		conn = null;
	        	}//end of finally
			}//end of finally
		
		return iResult;
	}

	
	
	public ArrayList<CollectionsVO> getCollectionTotalDetailsList(ArrayList<Object> alSearchParams) throws TTKException{
		
		//System.out.println("Calling getCollectionDetailsList  CollectionsDAOImpl");
		Collection<CollectionsVO> alResultList = new ArrayList<CollectionsVO>();
		Collection<CollectionsVO> addAllResultList = new ArrayList<CollectionsVO>();
		ResultSet rs = null;
		ResultSet rsSum = null;
		CollectionsVO collectionsVo = null;
		CollectionsVO collectiondetailsVo = null;
		try(Connection conn = ResourceManager.getConnection();
				CallableStatement cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCollectionDetails)
		     ) {
        	cStmtObject.setString(1,(String) alSearchParams.get(0).toString().trim());//invoice seq id
        	cStmtObject.setString(2,(String) alSearchParams.get(1).toString().trim());//invoice num 
	        cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
	        cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
        	cStmtObject.execute();
	    rs = (java.sql.ResultSet)cStmtObject.getObject(3);
	    rsSum = (java.sql.ResultSet)cStmtObject.getObject(4);
	if(rsSum!=null){
		while(rsSum.next()){
			collectionsVo = new CollectionsVO();
			
			   collectionsVo.setGroupName(rsSum.getString("GROUP_NAME"));
	    	   collectionsVo.setGroupId(rsSum.getString("GROUP_ID"));
		       collectionsVo.setPolicyNum(rsSum.getString("POLICY_NUMBER"));
		       collectionsVo.setLineOfBussiness(rsSum.getString("LINE_OF_BUSINESS"));
		       collectionsVo.setStartDate(rsSum.getString("EFFECTIVE_FROM_DATE"));
		       collectionsVo.setEndDate(rsSum.getString("EFFECTIVE_TO_DATE"));
		       collectionsVo.setDueDate(rsSum.getString("DUE_DATE"));
		       collectionsVo.setInvoiceDate(rsSum.getString("INV_GEN_DATE"));
			   collectionsVo.setInvoiceAmount(rsSum.getString("TOT_PREMIUM"));
		       collectionsVo.setTotalCollectionsQAR(rsSum.getString("TOT_COLLECTION"));
		       collectionsVo.setTotalOutstandingAmnt(rsSum.getString("TOT_OUTSTANDING"));
		     
		       
		       alResultList.add(collectionsVo);
		}
	}
	
	return (ArrayList<CollectionsVO>) alResultList;
}

catch (SQLException sqlExp)
{
	throw new TTKException(sqlExp, "collections");
}//end of catch (SQLException sqlExp)
catch (Exception exp)
{
	throw new TTKException(exp, "collections");
}//end of catch (Exception exp)
	
}

}
