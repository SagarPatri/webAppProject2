/**
 * @ (#) LineItemVO.java Jul 15, 2006
 * Project 	     : TTK HealthCare Services
 * File          : LineItemVO.java
 * Author        : RamaKrishna K M
 * Company       : Span Systems Corporation
 * Date Created  : Jul 15, 2006
 *
 * @author       :  RamaKrishna K M
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.dto.claims;

import java.math.BigDecimal;

import com.ttk.dto.BaseVO;

public class ClaimUploadErrorLogVO extends BaseVO{

	private static final long serialVersionUID = 1L;
	
	private String referenceNo;
	private String fileName;
	private String strAddedDate;
	private String tariffType;
	private String tariffTypeDesc;
	private String sourceTypeId;
	
	
	private String iBatchRefNO;
	private String batchNO;
	private String invoiceNo;
	private String formDate;
	private String toDate;
	private String providerName;
	private String providerId;
	private String claimNo;
	private String alkootId;
	private String claimAge;
	private String memberName;
	private String claimType;
	private String assignedTo;
	private String recDate;
	private String status;
	
	
	private String previousClaimNo;
	private String activityType;
	private String parentClaimNo;
	private String internalServiceCode;
	private String serviceDate;
	private String serviceDescription;
	private String parentClaimSettlementNo;
	private String cptCode;
	private String reSubRecDate;
	private String reSubReqAmnt;
	private String reSubRemarks;
	private String quantity;
	private String alkootRemarks;
	private String toothNo;
	private String errorLogs;
	private String reSubJustification;
	private String editFlagYN;
	private String xmlSeqId;
	private String reSubInterSeqId;
	private String autoRecjectSeqId;
	private String hospSeqId;
	private String parentClaimSetlmntNo;
	private String hiddenPreviousClamNo;
	private String rejectionReason;
	private String claimAction;
	private String hiddenClaimAction;
	
	
	
	public String getPreviousClaimNo() {
		return previousClaimNo;
	}
	public void setPreviousClaimNo(String previousClaimNo) {
		this.previousClaimNo = previousClaimNo;
	}
	public String getActivityType() {
		return activityType;
	}
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	public String getParentClaimNo() {
		return parentClaimNo;
	}
	public void setParentClaimNo(String parentClaimNo) {
		this.parentClaimNo = parentClaimNo;
	}
	public String getInternalServiceCode() {
		return internalServiceCode;
	}
	public void setInternalServiceCode(String internalServiceCode) {
		this.internalServiceCode = internalServiceCode;
	}
	public String getServiceDate() {
		return serviceDate;
	}
	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}
	public String getServiceDescription() {
		return serviceDescription;
	}
	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}
	public String getParentClaimSettlementNo() {
		return parentClaimSettlementNo;
	}
	public void setParentClaimSettlementNo(String parentClaimSettlementNo) {
		this.parentClaimSettlementNo = parentClaimSettlementNo;
	}
	public String getCptCode() {
		return cptCode;
	}
	public void setCptCode(String cptCode) {
		this.cptCode = cptCode;
	}
	public String getReSubRecDate() {
		return reSubRecDate;
	}
	public void setReSubRecDate(String reSubRecDate) {
		this.reSubRecDate = reSubRecDate;
	}
	public String getReSubReqAmnt() {
		return reSubReqAmnt;
	}
	public void setReSubReqAmnt(String reSubReqAmnt) {
		this.reSubReqAmnt = reSubReqAmnt;
	}
	public String getReSubRemarks() {
		return reSubRemarks;
	}
	public void setReSubRemarks(String reSubRemarks) {
		this.reSubRemarks = reSubRemarks;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getAlkootRemarks() {
		return alkootRemarks;
	}
	public void setAlkootRemarks(String alkootRemarks) {
		this.alkootRemarks = alkootRemarks;
	}
	public String getToothNo() {
		return toothNo;
	}
	public void setToothNo(String toothNo) {
		this.toothNo = toothNo;
	}
	public String getErrorLogs() {
		return errorLogs;
	}
	public void setErrorLogs(String errorLogs) {
		this.errorLogs = errorLogs;
	}
	public String getReSubJustification() {
		return reSubJustification;
	}
	public void setReSubJustification(String reSubJustification) {
		this.reSubJustification = reSubJustification;
	}
	public String getiBatchRefNO() {
		return iBatchRefNO;
	}
	public void setiBatchRefNO(String iBatchRefNO) {
		this.iBatchRefNO = iBatchRefNO;
	}
	public String getBatchNO() {
		return batchNO;
	}
	public void setBatchNO(String batchNO) {
		this.batchNO = batchNO;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getFormDate() {
		return formDate;
	}
	public void setFormDate(String formDate) {
		this.formDate = formDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	public String getProviderId() {
		return providerId;
	}
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getAlkootId() {
		return alkootId;
	}
	public void setAlkootId(String alkootId) {
		this.alkootId = alkootId;
	}
	public String getClaimAge() {
		return claimAge;
	}
	public void setClaimAge(String claimAge) {
		this.claimAge = claimAge;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	public String getRecDate() {
		return recDate;
	}
	public void setRecDate(String recDate) {
		this.recDate = recDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReferenceNo() {
		return referenceNo;
	}
	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getStrAddedDate() {
		return strAddedDate;
	}
	public void setStrAddedDate(String strAddedDate) {
		this.strAddedDate = strAddedDate;
	}
	public String getSourceTypeId() {
		return sourceTypeId;
	}
	public void setSourceTypeId(String sourceTypeId) {
		this.sourceTypeId = sourceTypeId;
	}
	public String getTariffType() {
		return tariffType;
	}
	public void setTariffType(String tariffType) {
		this.tariffType = tariffType;
	}
	public String getTariffTypeDesc() {
		return tariffTypeDesc;
	}
	public void setTariffTypeDesc(String tariffTypeDesc) {
		this.tariffTypeDesc = tariffTypeDesc;
	}
	public String getEditFlagYN() {
		return editFlagYN;
	}
	public void setEditFlagYN(String editFlagYN) {
		this.editFlagYN = editFlagYN;
	}
	public String getXmlSeqId() {
		return xmlSeqId;
	}
	public void setXmlSeqId(String xmlSeqId) {
		this.xmlSeqId = xmlSeqId;
	}
	public String getHospSeqId() {
		return hospSeqId;
	}
	public void setHospSeqId(String hospSeqId) {
		this.hospSeqId = hospSeqId;
	}
	public String getAutoRecjectSeqId() {
		return autoRecjectSeqId;
	}
	public void setAutoRecjectSeqId(String autoRecjectSeqId) {
		this.autoRecjectSeqId = autoRecjectSeqId;
	}
	public String getReSubInterSeqId() {
		return reSubInterSeqId;
	}
	public void setReSubInterSeqId(String reSubInterSeqId) {
		this.reSubInterSeqId = reSubInterSeqId;
	}
	public String getParentClaimSetlmntNo() {
		return parentClaimSetlmntNo;
	}
	public void setParentClaimSetlmntNo(String parentClaimSetlmntNo) {
		this.parentClaimSetlmntNo = parentClaimSetlmntNo;
	}
	public String getHiddenPreviousClamNo() {
		return hiddenPreviousClamNo;
	}
	public void setHiddenPreviousClamNo(String hiddenPreviousClamNo) {
		this.hiddenPreviousClamNo = hiddenPreviousClamNo;
	}
	public String getRejectionReason() {
		return rejectionReason;
	}
	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}
	public String getClaimAction() {
		return claimAction;
	}
	public void setClaimAction(String claimAction) {
		this.claimAction = claimAction;
	}
	public String getHiddenClaimAction() {
		return hiddenClaimAction;
	}
	public void setHiddenClaimAction(String hiddenClaimAction) {
		this.hiddenClaimAction = hiddenClaimAction;
	}
	

	
	//end  added for koc 1308
}//end of LineItemVO
