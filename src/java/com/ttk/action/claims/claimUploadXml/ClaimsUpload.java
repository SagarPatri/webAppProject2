package com.ttk.action.claims.claimUploadXml;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ttk.action.processedcliams.AuditClaimData;


@XmlRootElement
public class ClaimsUpload {
	
	private String hospSeqId;
	private String receivedDate;
	private String addedBy;
	private String sourceType;
	private List<ClaimsData> claimsData;
	private List<ClaimsReSubmissionData> claimsReSubmissionData;
	
	private List<AuditClaimData> auditClaimData;
	
	
	private String count;
	
	public String getHospSeqId() {
		return hospSeqId;
	}
	@XmlAttribute
	public void setHospSeqId(String hospSeqId) {
		this.hospSeqId = hospSeqId;
	}
	public String getReceivedDate() {
		return receivedDate;
	}
	@XmlAttribute
	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}
	public String getAddedBy() {
		return addedBy;
	}
	@XmlAttribute
	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}
	public String getSourceType() {
		return sourceType;
	}
	@XmlAttribute
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public List<ClaimsData> getClaimsData() {
		return claimsData;
	}
	@XmlElement
	public void setClaimsData(List<ClaimsData> claimsData) {
		this.claimsData = claimsData;
	}
	public String getCount() {
		return count;
	}
	@XmlAttribute
	public void setCount(String count) {
		this.count = count;
	}
	public List<ClaimsReSubmissionData> getClaimsReSubmissionData() {
		return claimsReSubmissionData;
	}
	@XmlElement
	public void setClaimsReSubmissionData(List<ClaimsReSubmissionData> claimsReSubmissionData) {
		this.claimsReSubmissionData = claimsReSubmissionData;
	}
	public List<AuditClaimData> getAuditClaimData() {
		return auditClaimData;
	}
	public void setAuditClaimData(List<AuditClaimData> auditClaimData) {
		this.auditClaimData = auditClaimData;
	}
	
}

