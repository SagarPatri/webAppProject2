package com.ttk.business.finance;

import java.io.InputStream;
import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagementType;

import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.impl.finance.CollectionsDAOFactory;
import com.ttk.dao.impl.finance.CollectionsDAOImpl;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.finance.CollectionDetailsVO;
import com.ttk.dto.finance.CollectionsVO;



@Stateless
@javax.ejb.TransactionManagement(TransactionManagementType.BEAN)

public class CollectionsManagerBean  implements CollectionsManager{

	
	 private CollectionsDAOImpl collectionsDAO = null;
	 private CollectionsDAOFactory collectionCareDAOFactory = null;
	
	
	 private BaseDAO getCollectionsDAO(String strIdentifier) throws TTKException
	    {
	        try
	        {
	            if (collectionCareDAOFactory == null)
	            	collectionCareDAOFactory = new CollectionsDAOFactory();
	            if(strIdentifier!=null)
	            {
	                return collectionCareDAOFactory.getDAO(strIdentifier);
	            }//end of if(strIdentifier!=null)
	            else
	                return null;
	        }//end of try
	        catch(Exception exp)
	        {
	            throw new TTKException(exp, "error."+strIdentifier+".dao");
	        }//end of catch(Exception exp)
	    }//End of getCallCenterDAO(String strIdentifier)
	
	 @Override
		public ArrayList<CollectionsVO> getCollectionsList(ArrayList<Object> searchData) throws TTKException {
			collectionsDAO = (CollectionsDAOImpl)this.getCollectionsDAO("collections");
		        return collectionsDAO.getCollectionsList(searchData);
		}

	@Override
	public CollectionsVO getPolicyDetails(String policySeqId) throws TTKException {
		collectionsDAO = (CollectionsDAOImpl)this.getCollectionsDAO("collections");
        return collectionsDAO.getPolicyDetails(policySeqId);
	}

	@Override
	public ArrayList<CollectionsVO> getInvoiceDetails(ArrayList<Object> searchData) throws TTKException {
		collectionsDAO = (CollectionsDAOImpl)this.getCollectionsDAO("collections");
        return collectionsDAO.getInvoiceDetails(searchData);
	}

	@Override
	public CollectionsVO getTotalPremiumDetails(ArrayList<Object> searchData) throws TTKException {
		collectionsDAO = (CollectionsDAOImpl)this.getCollectionsDAO("collections");
        return collectionsDAO.getTotalPremiumDetails(searchData);
	}

	@Override
	public TTKReportDataSource getExportReport(ArrayList<Object> alGenerateXL) throws TTKException {
		collectionsDAO = (CollectionsDAOImpl)this.getCollectionsDAO("collections");
        return collectionsDAO.getExportReport(alGenerateXL);
	}

	@Override
	public Long saveCollection(CollectionsVO collectionsVO, InputStream inputStream, int formFileSize, String finalPath) throws TTKException {
		collectionsDAO = (CollectionsDAOImpl)this.getCollectionsDAO("collections");
        return collectionsDAO.saveCollection(collectionsVO,inputStream,formFileSize,finalPath);
	}

	@Override
	public CollectionsVO getCollectionData(long collectionSeqId) throws TTKException {
		collectionsDAO = (CollectionsDAOImpl)this.getCollectionsDAO("collections");
        return collectionsDAO.getCollectionData(collectionSeqId);
	}

	@Override
	public ArrayList<CollectionsVO> getCollectionDetailsList(ArrayList<Object> alSearchParams) throws TTKException {
		collectionsDAO = (CollectionsDAOImpl)this.getCollectionsDAO("collections");
        return collectionsDAO.getCollectionDetailsList(alSearchParams);
	}

	@Override
	public TTKReportDataSource downLoadCollectionDtls(ArrayList<Object> alGenerateXL) throws TTKException {
		collectionsDAO = (CollectionsDAOImpl)this.getCollectionsDAO("collections");
        return collectionsDAO.downLoadCollectionDtls(alGenerateXL);
	}

	@Override
	public int doReverseTrasaction(CollectionsVO collectionsVO) throws TTKException {
		collectionsDAO = (CollectionsDAOImpl)this.getCollectionsDAO("collections");
        return collectionsDAO.doReverseTrasaction(collectionsVO);
	}

	@Override
	public ArrayList<CollectionsVO> getCollectionTotalDetailsList(ArrayList<Object> alSearchParams) throws TTKException {
		collectionsDAO = (CollectionsDAOImpl)this.getCollectionsDAO("collections");
        return collectionsDAO.getCollectionTotalDetailsList(alSearchParams);
	}

}
