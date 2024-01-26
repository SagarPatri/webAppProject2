/**
 *  @ (#)PreAuthVO.java Apr 10, 2006
 *  Project       : TTK HealthCare Services
 *  File          : PreAuthVO.java
 *  Author        : Arun K N
 *  Company       : Span Systems Corporation
 *  Date Created  : Apr 10, 2006
 *
 *  @author       :  Arun K N
 *  Modified by   :
 *  Modified date :
 *  Reason        :
 *
 */

package com.ttk.dto.onlineforms;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.struts.upload.FormFile;

import com.ttk.common.TTKCommon;
import com.ttk.dto.BaseVO;


public class PartnerClaimSubmissionVo {
	
	private String enrollId;
	private String invoiceNo;
	private Date treatmentDate;
	private BigDecimal requestedAmt;
	private	String country;
	private	String providerName;
	private String presentingComplaints="";
	private	String benifitTypeID;
	private String icdCode;
	private String diagnosisDescription;
	private String icdCodeSeqId;
    private String diagSeqId;
    private String claimSeqID;
    private String batchSeqId;
    private String currency;
    private Long partnerSeqId;
    private Long userSeqId;
    private String claimBatchNo;
    private String memberValidorInvalid;
    private String claimNumber;
    private String attachmentname1="";
    private String attachmentname2="";
    private String attachmentname3="";
    private String attachmentname4="";
    private String attachmentname5="";
    private FormFile sourceAttchments1; 
    private FormFile sourceAttchments2; 
    private FormFile sourceAttchments3; 
    private FormFile sourceAttchments4; 
    private FormFile sourceAttchments5;
	public String getEnrollId() {
		return enrollId;
	}

	public void setEnrollId(String enrollId) {
		this.enrollId = enrollId;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public BigDecimal getRequestedAmt() {
		return requestedAmt;
	}

	public void setRequestedAmt(BigDecimal requestedAmt) {
		this.requestedAmt = requestedAmt;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getPresentingComplaints() {
		return presentingComplaints;
	}

	public void setPresentingComplaints(String presentingComplaints) {
		this.presentingComplaints = presentingComplaints;
	}

	public String getBenifitTypeID() {
		return benifitTypeID;
	}

	public void setBenifitTypeID(String benifitTypeID) {
		this.benifitTypeID = benifitTypeID;
	}

	public String getIcdCode() {
		return icdCode;
	}

	public void setIcdCode(String icdCode) {
		this.icdCode = icdCode;
	}
	
	public String getIcdCodeSeqId() {
		return icdCodeSeqId;
	}

	public void setIcdCodeSeqId(String icdCodeSeqId) {
		this.icdCodeSeqId = icdCodeSeqId;
	}

	public String getDiagSeqId() {
		return diagSeqId;
	}

	public void setDiagSeqId(String diagSeqId) {
		this.diagSeqId = diagSeqId;
	}

	public String getClaimSeqID() {
		return claimSeqID;
	}

	public void setClaimSeqID(String claimSeqID) {
		this.claimSeqID = claimSeqID;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAttachmentname1() {
		return attachmentname1;
	}

	public void setAttachmentname1(String attachmentname1) {
		this.attachmentname1 = attachmentname1;
	}

	public String getAttachmentname2() {
		return attachmentname2;
	}

	public void setAttachmentname2(String attachmentname2) {
		this.attachmentname2 = attachmentname2;
	}

	public String getAttachmentname3() {
		return attachmentname3;
	}

	public void setAttachmentname3(String attachmentname3) {
		this.attachmentname3 = attachmentname3;
	}

	public String getAttachmentname5() {
		return attachmentname5;
	}

	public void setAttachmentname5(String attachmentname5) {
		this.attachmentname5 = attachmentname5;
	}

	public String getAttachmentname4() {
		return attachmentname4;
	}

	public void setAttachmentname4(String attachmentname4) {
		this.attachmentname4 = attachmentname4;
	}

	public FormFile getSourceAttchments1() {
		return sourceAttchments1;
	}

	public void setSourceAttchments1(FormFile sourceAttchments1) {
		this.sourceAttchments1 = sourceAttchments1;
	}

	public FormFile getSourceAttchments2() {
		return sourceAttchments2;
	}

	public void setSourceAttchments2(FormFile sourceAttchments2) {
		this.sourceAttchments2 = sourceAttchments2;
	}

	public FormFile getSourceAttchments4() {
		return sourceAttchments4;
	}

	public void setSourceAttchments4(FormFile sourceAttchments4) {
		this.sourceAttchments4 = sourceAttchments4;
	}

	public FormFile getSourceAttchments3() {
		return sourceAttchments3;
	}

	public void setSourceAttchments3(FormFile sourceAttchments3) {
		this.sourceAttchments3 = sourceAttchments3;
	}

	public FormFile getSourceAttchments5() {
		return sourceAttchments5;
	}

	public void setSourceAttchments5(FormFile sourceAttchments5) {
		this.sourceAttchments5 = sourceAttchments5;
	}

	public String getBatchSeqId() {
		return batchSeqId;
	}

	public void setBatchSeqId(String batchSeqId) {
		this.batchSeqId = batchSeqId;
	}

	public Long getUserSeqId() {
		return userSeqId;
	}

	public void setUserSeqId(Long userSeqId) {
		this.userSeqId = userSeqId;
	}

	public String getMemberValidorInvalid() {
		return memberValidorInvalid;
	}

	public void setMemberValidorInvalid(String memberValidorInvalid) {
		this.memberValidorInvalid = memberValidorInvalid;
	}

	public String getClaimBatchNo() {
		return claimBatchNo;
	}

	public void setClaimBatchNo(String claimBatchNo) {
		this.claimBatchNo = claimBatchNo;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	public Long getPartnerSeqId() {
		return partnerSeqId;
	}

	public void setPartnerSeqId(Long partnerSeqId) {
		this.partnerSeqId = partnerSeqId;
	}

	public Date getTreatmentDate() {
		return treatmentDate;
	}

	public void setTreatmentDate(Date treatmentDate) {
		this.treatmentDate = treatmentDate;
	}

	public String getDiagnosisDescription() {
		return diagnosisDescription;
	}

	public void setDiagnosisDescription(String diagnosisDescription) {
		this.diagnosisDescription = diagnosisDescription;
	}
	
}//end of PartnerClaimSubmissionVo.java