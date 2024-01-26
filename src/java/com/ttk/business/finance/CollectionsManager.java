package com.ttk.business.finance;

import java.io.InputStream;
import java.util.ArrayList;

import javax.ejb.Local;

import com.ttk.common.exception.TTKException;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.finance.CollectionDetailsVO;
import com.ttk.dto.finance.CollectionsVO;

@Local
public interface CollectionsManager {
	ArrayList<CollectionsVO> getCollectionsList(ArrayList<Object> searchData) throws TTKException;

	CollectionsVO getPolicyDetails(String policySeqId) throws TTKException;

	ArrayList<CollectionsVO> getInvoiceDetails(ArrayList<Object> searchData) throws TTKException;

	CollectionsVO getTotalPremiumDetails(ArrayList<Object> searchData)  throws TTKException;

	TTKReportDataSource getExportReport(ArrayList<Object> alGenerateXL) throws TTKException;

	Long saveCollection(CollectionsVO collectionsVO, InputStream inputStream, int formFileSize, String finalPath)throws TTKException;

	CollectionsVO getCollectionData(long collectionSeqId)throws TTKException;

	ArrayList<CollectionsVO> getCollectionDetailsList(ArrayList<Object> alSearchParams)throws TTKException;

	TTKReportDataSource downLoadCollectionDtls(ArrayList<Object> alGenerateXL)throws TTKException;

	int doReverseTrasaction(CollectionsVO collectionsVO) throws TTKException;

	ArrayList<CollectionsVO> getCollectionTotalDetailsList(ArrayList<Object> alSearchParams) throws TTKException;
}
