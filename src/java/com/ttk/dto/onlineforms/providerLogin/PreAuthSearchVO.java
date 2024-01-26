/** 
 * @ (#) PreAuthSearch.java 23rd Nov 2015 
 * Author      : KISHOR KUMAR S H
 * Company     : RCS TECHNOLOGIES
 * Date Created: 23rd Nov 2015
 *
 * @author 		 : KISHOR KUMAR S H
 * Modified by   : KISHOR KUMAR S H
 * Modified date : 23rd Nov 2015
 * Reason        :
 *
 */
package com.ttk.dto.onlineforms.providerLogin;

import com.ttk.dto.BaseVO;


public class PreAuthSearchVO extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String preAuthNo;
	private String receivedDate;
	private String patientName;
	private String benefitType;
	private String treatingDoctor;
	private String approvalNo;
	private String status;
	private long totalGrossAmt;
	private long partnerPatSeqId;
	public long getPartnerPatSeqId() {
		return partnerPatSeqId;
	}
	public void setPartnerPatSeqId(long partnerPatSeqId) {
		this.partnerPatSeqId = partnerPatSeqId;
	}
	public long getTotalGrossAmt() {
		return totalGrossAmt;
	}
	public void setTotalGrossAmt(long totalGrossAmt) {
		this.totalGrossAmt = totalGrossAmt;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	private String statusID;
	private String patientCardId;
	private Long patAuthSeqId;
	private String strShortfallImageName = "Blank";
	private String strShortfallImageTitle = "";
	private String strShortfallYN = "";
	
	//view Auth Parameters
	private String preAuthType;
	private String admissionDate;
	private String previousApprAmt;
	private String requestedAmt;
	private String vidalBranch;
	private String probableDischargeDt;
	private String beneficiaryId;
	private String providerName;
	private String countryName;
	private String empanelId;	
	private String emirate;
	private String city;
	private String apprStatus;
	private String decisionDt;
	private String decisionDtOfClaim;
	private String policyNo;
	public String getDecisionDtOfClaim() {
		return decisionDtOfClaim;
	}
	public void setDecisionDtOfClaim(String decisionDtOfClaim) {
		this.decisionDtOfClaim = decisionDtOfClaim;
	}

	//aDDITIONAL PARAMETERS FOR CLAIMS
	private String batchNo;
	private String invoiceNo;
	private String chequeNo;
	private Long clmSeqId;;
	private Long clmBatchSeqId;
	private String claimType;
	private String claimSubmittedDate;

	private String treatmentDate;
	private String claimedAmount;
	private String preAuthRefNo;
	private String emirateID;
	private String enhance;
	private String enhanceYN;
	 private String enhanceImageName="Blank";
	 private String enhanceImageTitle="";
	 private String preauthEnhanceImageName="Blank";
	 private String preauthEnhanceImageTitle="";
	 private String enhancedPreauthYN;
	 private String submitYN;
	 private String appealImageName="Blank";
	 private String apealImageTitle="";
	 private String inProImageName="Blank";
	 private String inProImageTitle="";
	 
	 public String getStrShortfallImageName() {
			return strShortfallImageName;
		}
		public void setStrShortfallImageName(String strShortfallImageName) {
			this.strShortfallImageName = strShortfallImageName;
		}
		public String getStrShortfallImageTitle() {
			return strShortfallImageTitle;
		}
		public void setStrShortfallImageTitle(String strShortfallImageTitle) {
			this.strShortfallImageTitle = strShortfallImageTitle;
		}
		public String getStrShortfallYN() {
			return strShortfallYN;
		}
		public void setStrShortfallYN(String strShortfallYN) {
			this.strShortfallYN = strShortfallYN;
		}
	
	 private String eventReferenceNo;
		
		public String getEventReferenceNo() {
			return eventReferenceNo;
		}

		public void setEventReferenceNo(String eventReferenceNo) {
			this.eventReferenceNo = eventReferenceNo;
		}
	 
	 
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Long getClmSeqId() {
		return clmSeqId;
	}
	public void setClmSeqId(Long clmSeqId) {
		this.clmSeqId = clmSeqId;
	}
	public Long getClmBatchSeqId() {
		return clmBatchSeqId;
	}
	public void setClmBatchSeqId(Long clmBatchSeqId) {
		this.clmBatchSeqId = clmBatchSeqId;
	}
	public String getPreviousApprAmt() {
		return previousApprAmt;
	}
	public void setPreviousApprAmt(String previousApprAmt) {
		this.previousApprAmt = previousApprAmt;
	}
	public String getRequestedAmt() {
		return requestedAmt;
	}
	public void setRequestedAmt(String requestedAmt) {
		this.requestedAmt = requestedAmt;
	}
	public String getVidalBranch() {
		return vidalBranch;
	}
	public void setVidalBranch(String vidalBranch) {
		this.vidalBranch = vidalBranch;
	}
	public String getProbableDischargeDt() {
		return probableDischargeDt;
	}
	public void setProbableDischargeDt(String probableDischargeDt) {
		this.probableDischargeDt = probableDischargeDt;
	}
	public String getBeneficiaryId() {
		return beneficiaryId;
	}
	public void setBeneficiaryId(String beneficiaryId) {
		this.beneficiaryId = beneficiaryId;
	}
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	public String getEmpanelId() {
		return empanelId;
	}
	public void setEmpanelId(String empanelId) {
		this.empanelId = empanelId;
	}
	public String getEmirate() {
		return emirate;
	}
	public void setEmirate(String emirate) {
		this.emirate = emirate;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getApprStatus() {
		return apprStatus;
	}
	public void setApprStatus(String apprStatus) {
		this.apprStatus = apprStatus;
	}
	public String getDecisionDt() {
		return decisionDt;
	}
	public void setDecisionDt(String decisionDt) {
		this.decisionDt = decisionDt;
	}

	public String getPreAuthNo() {
		return preAuthNo;
	}
	public void setPreAuthNo(String preAuthNo) {
		this.preAuthNo = preAuthNo;
	}
	public String getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getBenefitType() {
		return benefitType;
	}
	public void setBenefitType(String benefitType) {
		this.benefitType = benefitType;
	}
	public String getTreatingDoctor() {
		return treatingDoctor;
	}
	public void setTreatingDoctor(String treatingDoctor) {
		this.treatingDoctor = treatingDoctor;
	}
	public String getApprovalNo() {
		return approvalNo;
	}
	public void setApprovalNo(String approvalNo) {
		this.approvalNo = approvalNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPatientCardId() {
		return patientCardId;
	}
	public void setPatientCardId(String patientCardId) {
		this.patientCardId = patientCardId;
	}
	/**
	 * @return the admissionDate
	 */
	public String getAdmissionDate() {
		return admissionDate;
	}
	/**
	 * @param admissionDate the admissionDate to set
	 */
	public void setAdmissionDate(String admissionDate) {
		this.admissionDate = admissionDate;
	}
	/**
	 * @return the preAuthType
	 */
	public String getPreAuthType() {
		return preAuthType;
	}
	/**
	 * @param preAuthType the preAuthType to set
	 */
	public void setPreAuthType(String preAuthType) {
		this.preAuthType = preAuthType;
	}
	/**
	 * @return the statusID
	 */
	public String getStatusID() {
		return statusID;
	}
	/**
	 * @param statusID the statusID to set
	 */
	public void setStatusID(String statusID) {
		this.statusID = statusID;
	}
	/**
	 * @return the patAuthSeqId
	 */
	public Long getPatAuthSeqId() {
		return patAuthSeqId;
	}
	/**
	 * @param patAuthSeqId the patAuthSeqId to set
	 */
	public void setPatAuthSeqId(Long patAuthSeqId) {
		this.patAuthSeqId = patAuthSeqId;
	}
	/**
	 * @return the claimType
	 */
	public String getClaimType() {
		return claimType;
	}
	/**
	 * @param claimType the claimType to set
	 */
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	/**
	 * @return the chequeNo
	 */
	public String getChequeNo() {
		return chequeNo;
	}
	
	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}
	/**
	 * @param chequeNo the chequeNo to set
	 */
	
	public String getPreAuthRefNo() {
		return preAuthRefNo;
	}
	public void setPreAuthRefNo(String preAuthRefNo) {
		this.preAuthRefNo = preAuthRefNo;
	}
	public String getEmirateID() {
		return emirateID;
	}
	public void setEmirateID(String emirateID) {
		this.emirateID = emirateID;
	}
	public String getEnhanceImageName() {
		return enhanceImageName;
	}
	public void setEnhanceImageName(String enhanceImageName) {
		this.enhanceImageName = enhanceImageName;
	}
	public String getEnhanceImageTitle() {
		return enhanceImageTitle;
	}
	public void setEnhanceImageTitle(String enhanceImageTitle) {
		this.enhanceImageTitle = enhanceImageTitle;
	}
	public String getEnhance() {
		return enhance;
	}
	public void setEnhance(String enhance) {
		this.enhance = enhance;
	}
	public String getEnhanceYN() {
		return enhanceYN;
	}
	public void setEnhanceYN(String enhanceYN) {
		this.enhanceYN = enhanceYN;
	}
	public String getClaimSubmittedDate() {
		return claimSubmittedDate;
	}
	public void setClaimSubmittedDate(String claimSubmittedDate) {
		this.claimSubmittedDate = claimSubmittedDate;
	}
	public String getClaimedAmount() {
		return claimedAmount;
	}
	public void setClaimedAmount(String claimedAmount) {
		this.claimedAmount = claimedAmount;
	}
	public String getTreatmentDate() {
		return treatmentDate;
	}
	public void setTreatmentDate(String treatmentDate) {
		this.treatmentDate = treatmentDate;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getPreauthEnhanceImageName() {
		return preauthEnhanceImageName;
	}
	public void setPreauthEnhanceImageName(String preauthEnhanceImageName) {
		this.preauthEnhanceImageName = preauthEnhanceImageName;
	}
	public String getPreauthEnhanceImageTitle() {
		return preauthEnhanceImageTitle;
	}
	public void setPreauthEnhanceImageTitle(String preauthEnhanceImageTitle) {
		this.preauthEnhanceImageTitle = preauthEnhanceImageTitle;
	}
	public String getEnhancedPreauthYN() {
		return enhancedPreauthYN;
	}
	public void setEnhancedPreauthYN(String enhancedPreauthYN) {
		this.enhancedPreauthYN = enhancedPreauthYN;
	}
	public String getSubmitYN() {
		return submitYN;
	}
	public void setSubmitYN(String submitYN) {
		this.submitYN = submitYN;
	}
	public String getAppealImageName() {
		return appealImageName;
	}
	public void setAppealImageName(String appealImageName) {
		this.appealImageName = appealImageName;
	}
	public String getApealImageTitle() {
		return apealImageTitle;
	}
	public void setApealImageTitle(String apealImageTitle) {
		this.apealImageTitle = apealImageTitle;
	}
	public String getInProImageName() {
		return inProImageName;
	}
	public void setInProImageName(String inProImageName) {
		this.inProImageName = inProImageName;
	}
	public String getInProImageTitle() {
		return inProImageTitle;
	}
	public void setInProImageTitle(String inProImageTitle) {
		this.inProImageTitle = inProImageTitle;
	}
	/*public String getAppealFlag() {
		return appealFlag; 
	}
	public void setAppealFlag(String appealFlag) {
		this.appealFlag = appealFlag;
	}*/
	
	
		
}
