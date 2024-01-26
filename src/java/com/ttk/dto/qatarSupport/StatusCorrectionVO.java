package com.ttk.dto.qatarSupport;

import java.io.File;

import com.ttk.dto.BaseVO;

public class StatusCorrectionVO extends BaseVO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String claimSettleNumber;
	private String paymentType;
	private String claimType;
	private String financeStatus;
	private String batchNo;
	private String batchDate;
	private String paymentMethod;
	private String alkootId;
	private String corporateName;
	private String payeeName;
	private String approvedAmtInQAR;
	private String approvedAmtIncuredCurrency;
	private String incuredCurrencyFormate;
	private String sussessYN;
	
	
	private String totalNoOfRows;
	private String totalNoOfRowsPassed;
	private String totalNoOfRowsFailed;
	private String reviewFlag;
	private String remarks;
	
	
	public String getAlkootId() {
		return alkootId;
	}
	public void setAlkootId(String alkootId) {
		this.alkootId = alkootId;
	}
	public String getCorporateName() {
		return corporateName;
	}
	public void setCorporateName(String corporateName) {
		this.corporateName = corporateName;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public String getApprovedAmtInQAR() {
		return approvedAmtInQAR;
	}
	public void setApprovedAmtInQAR(String approvedAmtInQAR) {
		this.approvedAmtInQAR = approvedAmtInQAR;
	}
	public String getApprovedAmtIncuredCurrency() {
		return approvedAmtIncuredCurrency;
	}
	public void setApprovedAmtIncuredCurrency(String approvedAmtIncuredCurrency) {
		this.approvedAmtIncuredCurrency = approvedAmtIncuredCurrency;
	}
	public String getClaimSettleNumber() {
		return claimSettleNumber;
	}
	public void setClaimSettleNumber(String claimSettleNumber) {
		this.claimSettleNumber = claimSettleNumber;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getFinanceStatus() {
		return financeStatus;
	}
	public void setFinanceStatus(String financeStatus) {
		this.financeStatus = financeStatus;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getBatchDate() {
		return batchDate;
	}
	public void setBatchDate(String batchDate) {
		this.batchDate = batchDate;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getIncuredCurrencyFormate() {
		return incuredCurrencyFormate;
	}
	public void setIncuredCurrencyFormate(String incuredCurrencyFormate) {
		this.incuredCurrencyFormate = incuredCurrencyFormate;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getSussessYN() {
		return sussessYN;
	}
	public void setSussessYN(String sussessYN) {
		this.sussessYN = sussessYN;
	}
	/*public File getStmFile() {
		return stmFile;
	}
	public void setStmFile(File stmFile) {
		this.stmFile = stmFile;
	}*/
	/*public org.apache.struts.upload.FormFile getStmFile() {
		return stmFile;
	}
	public void setStmFile(org.apache.struts.upload.FormFile stmFile) {
		this.stmFile = stmFile;
	}*/
	/*public String getStmFile() {
		return stmFile;
	}
	public void setStmFile(String stmFile) {
		this.stmFile = stmFile;
	}*/
	public String getTotalNoOfRows() {
		return totalNoOfRows;
	}
	public void setTotalNoOfRows(String totalNoOfRows) {
		this.totalNoOfRows = totalNoOfRows;
	}
	public String getTotalNoOfRowsPassed() {
		return totalNoOfRowsPassed;
	}
	public void setTotalNoOfRowsPassed(String totalNoOfRowsPassed) {
		this.totalNoOfRowsPassed = totalNoOfRowsPassed;
	}
	public String getTotalNoOfRowsFailed() {
		return totalNoOfRowsFailed;
	}
	public void setTotalNoOfRowsFailed(String totalNoOfRowsFailed) {
		this.totalNoOfRowsFailed = totalNoOfRowsFailed;
	}
	public String getReviewFlag() {
		return reviewFlag;
	}
	public void setReviewFlag(String reviewFlag) {
		this.reviewFlag = reviewFlag;
	}
	
}