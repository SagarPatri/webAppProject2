package com.ttk.dto.finance;

import com.ttk.dto.BaseVO;

public class CollectionsVO extends BaseVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String groupName;
	private String groupId;
	private String policyNum;
	private String policySeqId;
	private String policyStatus;
	private String lineOfBussiness;
	private String startDate;
	private String endDate;
	private String totalPremiumQAR;
	private String totalCollectionsQAR;
	private String totalOutstandingQAR;
	
	private Long collectionsSeqId;
	private String collectionNumber;
	private String invoiceSeqId;
	private String invoiceNo;
	private String invoiceAmount;
	private String invoiceDate;
	private String dueDate;
	private String membersList;
	private String switchTo;
	
	private String addCollectionImage;
	private String addCollectionImageTitle;
	private String removeCollectionImage;
	private String removeCollectionImageTitle;
	private String add;
	private String remove;
	private String batchName;
	
	private String totalPremiumSum;
	private String totalCollectionsSum;
	private String totalOutstandingSum;
	
	private String reforward;
	private String amountReceivedQAR;
	private String receivedDate;
	private String transactionRef;
	private String amountDueQAR;
	  private String fileName; 
	  private String deleteYN; 
	
	  private String deletionRemarks; 
	  private String hiddenInvoiceNo;
	  private String totalOutstandingAmnt;
	  
	  
	  
	  
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getPolicyNum() {
		return policyNum;
	}
	public void setPolicyNum(String policyNum) {
		this.policyNum = policyNum;
	}
	public String getPolicyStatus() {
		return policyStatus;
	}
	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
	}
	public String getLineOfBussiness() {
		return lineOfBussiness;
	}
	public void setLineOfBussiness(String lineOfBussiness) {
		this.lineOfBussiness = lineOfBussiness;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getTotalPremiumQAR() {
		return totalPremiumQAR;
	}
	public void setTotalPremiumQAR(String totalPremiumQAR) {
		this.totalPremiumQAR = totalPremiumQAR;
	}
	public String getTotalCollectionsQAR() {
		return totalCollectionsQAR;
	}
	public void setTotalCollectionsQAR(String totalCollectionsQAR) {
		this.totalCollectionsQAR = totalCollectionsQAR;
	}
	public String getTotalOutstandingQAR() {
		return totalOutstandingQAR;
	}
	public void setTotalOutstandingQAR(String totalOutstandingQAR) {
		this.totalOutstandingQAR = totalOutstandingQAR;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(String invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getMembersList() {
		return membersList;
	}
	public void setMembersList(String membersList) {
		this.membersList = membersList;
	}
	public String getSwitchTo() {
		return switchTo;
	}
	public void setSwitchTo(String switchTo) {
		this.switchTo = switchTo;
	}
	public String getAddCollectionImage() {
		return addCollectionImage;
	}
	public void setAddCollectionImage(String addCollectionImage) {
		this.addCollectionImage = addCollectionImage;
	}
	public String getAddCollectionImageTitle() {
		return addCollectionImageTitle;
	}
	public void setAddCollectionImageTitle(String addCollectionImageTitle) {
		this.addCollectionImageTitle = addCollectionImageTitle;
	}
	public String getRemoveCollectionImage() {
		return removeCollectionImage;
	}
	public void setRemoveCollectionImage(String removeCollectionImage) {
		this.removeCollectionImage = removeCollectionImage;
	}
	public String getRemoveCollectionImageTitle() {
		return removeCollectionImageTitle;
	}
	public void setRemoveCollectionImageTitle(String removeCollectionImageTitle) {
		this.removeCollectionImageTitle = removeCollectionImageTitle;
	}
	public String getAdd() {
		return add;
	}
	public void setAdd(String add) {
		this.add = add;
	}
	public String getRemove() {
		return remove;
	}
	public void setRemove(String remove) {
		this.remove = remove;
	}
	public String getPolicySeqId() {
		return policySeqId;
	}
	public void setPolicySeqId(String policySeqId) {
		this.policySeqId = policySeqId;
	}
	public String getInvoiceSeqId() {
		return invoiceSeqId;
	}
	public void setInvoiceSeqId(String invoiceSeqId) {
		this.invoiceSeqId = invoiceSeqId;
	}
	public String getTotalPremiumSum() {
		return totalPremiumSum;
	}
	public void setTotalPremiumSum(String totalPremiumSum) {
		this.totalPremiumSum = totalPremiumSum;
	}
	public String getTotalCollectionsSum() {
		return totalCollectionsSum;
	}
	public void setTotalCollectionsSum(String totalCollectionsSum) {
		this.totalCollectionsSum = totalCollectionsSum;
	}
	public String getTotalOutstandingSum() {
		return totalOutstandingSum;
	}
	public void setTotalOutstandingSum(String totalOutstandingSum) {
		this.totalOutstandingSum = totalOutstandingSum;
	}
	public String getReforward() {
		return reforward;
	}
	public void setReforward(String reforward) {
		this.reforward = reforward;
	}
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	public String getAmountReceivedQAR() {
		return amountReceivedQAR;
	}
	public void setAmountReceivedQAR(String amountReceivedQAR) {
		this.amountReceivedQAR = amountReceivedQAR;
	}
	public String getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}
	public String getAmountDueQAR() {
		return amountDueQAR;
	}
	public void setAmountDueQAR(String amountDueQAR) {
		this.amountDueQAR = amountDueQAR;
	}
	public String getTransactionRef() {
		return transactionRef;
	}
	public void setTransactionRef(String transactionRef) {
		this.transactionRef = transactionRef;
	}
	public Long getCollectionsSeqId() {
		return collectionsSeqId;
	}
	public void setCollectionsSeqId(Long collectionsSeqId) {
		this.collectionsSeqId = collectionsSeqId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getCollectionNumber() {
		return collectionNumber;
	}
	public void setCollectionNumber(String collectionNumber) {
		this.collectionNumber = collectionNumber;
	}
	public String getDeleteYN() {
		return deleteYN;
	}
	public void setDeleteYN(String deleteYN) {
		this.deleteYN = deleteYN;
	}
	public String getDeletionRemarks() {
		return deletionRemarks;
	}
	public void setDeletionRemarks(String deletionRemarks) {
		this.deletionRemarks = deletionRemarks;
	}
	public String getHiddenInvoiceNo() {
		return hiddenInvoiceNo;
	}
	public void setHiddenInvoiceNo(String hiddenInvoiceNo) {
		this.hiddenInvoiceNo = hiddenInvoiceNo;
	}
	public String getTotalOutstandingAmnt() {
		return totalOutstandingAmnt;
	}
	public void setTotalOutstandingAmnt(String totalOutstandingAmnt) {
		this.totalOutstandingAmnt = totalOutstandingAmnt;
	}
	
	
	
	

}
