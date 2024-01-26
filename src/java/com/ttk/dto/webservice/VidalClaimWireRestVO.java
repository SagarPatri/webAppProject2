package com.ttk.dto.webservice;

import java.util.Date;

public class VidalClaimWireRestVO {
	private String strUsername = "";
    private String strPassword = "";
    private String strErrorMessage = "";
    private String strFirstPasswordYN="";
    private String StrPwdExpiryYN="";
    private String strPwdAlertMsg="";
    private String firstLoginYN="",strBankAccNo="",strAddress1="",strProductName="",strRemarkd="",strVfrm="",strVupt="";
    private String loginStatus="",strFeedbackSeqId="",strNotification="",strSelfFeedback="";
    private String policyGroupSeqID="",strImage="",strCompanyName="",strLocation="",strHospitalId="";
    private String policySeqID="",strPolicyHolderName="",strRelationShip="";
    private String strUserID="",strCompanyCode="",strDOB="",strDOJ="";
    private String strPreauth_int_seq_Id="",strNon_Medical="",strAdded_By="";
    private String strPreauth_int_Id="";
    private String strTpaEnrol_Id="",strValidFrom="";
    private String strMemAge="",strUpdatedBy="",strUpdatedDate="";
    private String strGender="",strLevel="";
    private String strIntimationGenDate="",strAdded_Date="";
    private String strMemberSeqId="";
    private String strStatusGenralId="",strAmmdtPl="",strStatusPl="",strProxy_Exclusion="";
    private String dtDoa="";
    private String strHosName="",strMem_name="",strOfflineFlagYN="",strTotalApprovedAmount="";
    private String strStatusDesc="";
    private String strLikelyDoH="";
    private String strPhyName="";
    private String strPhyPhNo="";
    private String strStatusTypeId="",strBenefit="",strInsurance_Id="";
    private String strCityTypeId="";
    private String strStateTypeId="",strShortfallPl="",strSettlementNo="";
    private String strAilDesc="",strClaimStatus="",strClaimSeqID="";
    private String strPolicyNo="",strTollFreeNo="",strCerficateNo="";
    private String strPatientName="",strClaimTypeID="",strDocumentTypeID="";
    private String strGroupId="",strValidUpto="";
    private Long strCallLogSeqId=null;
    private Long strInwardSeqID=null;
    private String strInwardNbr="",strtEnrollmentID="",strClaimantName="",strGroupName="",strClaimTypeDesc="";
    private Long strClmEnrollDtlSeqID=null;
    private Long preAuthSeqID=null;
    private String decisionDate = null;
    private String preAuthNumber = null;

    
    public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}
    public String getLoginStatus() {
		return loginStatus;
	}
    public String getFirstLoginYN() {
		return firstLoginYN;
	}
    public void setFirstLoginYN(String firstLoginYN) {
		this.firstLoginYN = firstLoginYN;
	}
	public String getUsername() {
		return strUsername;
	}
	public void setUsername(String strUsername) {
		this.strUsername = strUsername;
	}
	public String getPassword() {
		return strPassword;
	}
	public void setPassword(String strPassword) {
		this.strPassword = strPassword;
	}
	public String getErrorMessage() {
		return strErrorMessage;
	}
	public void setErrorMessage(String strErrorMessage) {
		this.strErrorMessage = strErrorMessage;
	}
	public String getFirstPasswordYN() {
		return strFirstPasswordYN;
	}
	public void setFirstPasswordYN(String strFirstPasswordYN) {
		this.strFirstPasswordYN = strFirstPasswordYN;
	}
	public String getPwdExpiryYN() {
		return StrPwdExpiryYN;
	}
	public void setPwdExpiryYN(String strPwdExpiryYN) {
		StrPwdExpiryYN = strPwdExpiryYN;
	}
	public String getPwdAlertMsg() {
		return strPwdAlertMsg;
	}
	public void setPwdAlertMsg(String strPwdAlertMsg) {
		this.strPwdAlertMsg = strPwdAlertMsg;
	}
	/**
	 * @return the policyGroupSeqID
	 */
	public String getPolicyGroupSeqID() {
		return policyGroupSeqID;
	}
	/**
	 * @param policyGroupSeqID the policyGroupSeqID to set
	 */
	public void setPolicyGroupSeqID(String policyGroupSeqID) {
		this.policyGroupSeqID = policyGroupSeqID;
	}
	/**
	 * @return the policySeqID
	 */
	public String getPolicySeqID() {
		return policySeqID;
	}
	/**
	 * @param policySeqID the policySeqID to set
	 */
	public void setPolicySeqID(String policySeqID) {
		this.policySeqID = policySeqID;
	}
	/**
	 * @return the strUserID
	 */
	public String getUserID() {
		return strUserID;
	}
	/**
	 * @param strUserID the strUserID to set
	 */
	public void setUserID(String strUserID) {
		this.strUserID = strUserID;
	}
	/**
	 * @return the strPolicyNo
	 */
	public String getPolicyNo() {
		return strPolicyNo;
	}
	/**
	 * @param strPolicyNo the strPolicyNo to set
	 */
	public void setPolicyNo(String strPolicyNo) {
		this.strPolicyNo = strPolicyNo;
	}
	/**
	 * @return the strGroupID
	 */
	
	/**
	 * @return the strPreauth_int_seq_Id
	 */
	public String getPreauth_int_seq_Id() {
		return strPreauth_int_seq_Id;
	}
	/**
	 * @param strPreauth_int_seq_Id the strPreauth_int_seq_Id to set
	 */
	public void setPreauth_int_seq_Id(String strPreauth_int_seq_Id) {
		this.strPreauth_int_seq_Id = strPreauth_int_seq_Id;
	}
	/**
	 * @return the strTpaEnrol_Id
	 */
	public String getTpaEnrol_Id() {
		return strTpaEnrol_Id;
	}
	/**
	 * @param strTpaEnrol_Id the strTpaEnrol_Id to set
	 */
	public void setTpaEnrol_Id(String strTpaEnrol_Id) {
		this.strTpaEnrol_Id = strTpaEnrol_Id;
	}
	/**
	 * @return the strPreauth_int_Id
	 */
	public String getPreauth_int_Id() {
		return strPreauth_int_Id;
	}
	/**
	 * @param strPreauth_int_Id the strPreauth_int_Id to set
	 */
	public void setPreauth_int_Id(String strPreauth_int_Id) {
		this.strPreauth_int_Id = strPreauth_int_Id;
	}
	/**
	 * @return the strMemAge
	 */
	public String getMemAge() {
		return strMemAge;
	}
	/**
	 * @param strMemAge the strMemAge to set
	 */
	public void setMemAge(String strMemAge) {
		this.strMemAge = strMemAge;
	}
	/**
	 * @return the strIntimationGenDate
	 */
	public String getIntimationGenDate() {
		return strIntimationGenDate;
	}
	/**
	 * @param strIntimationGenDate the strIntimationGenDate to set
	 */
	public void setIntimationGenDate(String strIntimationGenDate) {
		this.strIntimationGenDate = strIntimationGenDate;
	}
	/**
	 * @return the strGender
	 */
	public String getGender() {
		return strGender;
	}
	/**
	 * @param strGender the strGender to set
	 */
	public void setGender(String strGender) {
		this.strGender = strGender;
	}
	/**
	 * @return the strMemberSeqId
	 */
	public String getMemberSeqId() {
		return strMemberSeqId;
	}
	/**
	 * @param strMemberSeqId the strMemberSeqId to set
	 */
	public void setMemberSeqId(String strMemberSeqId) {
		this.strMemberSeqId = strMemberSeqId;
	}
	/**
	 * @return the strHosName
	 */
	public String getHosName() {
		return strHosName;
	}
	/**
	 * @param strHosName the strHosName to set
	 */
	public void setHosName(String strHosName) {
		this.strHosName = strHosName;
	}
	/**
	 * @return the strStatusGenralId
	 */
	public String getStatusGenralId() {
		return strStatusGenralId;
	}
	/**
	 * @param strStatusGenralId the strStatusGenralId to set
	 */
	public void setStatusGenralId(String strStatusGenralId) {
		this.strStatusGenralId = strStatusGenralId;
	}
	/**
	 * @return the strStatusDesc
	 */
	public String getStatusDesc() {
		return strStatusDesc;
	}
	/**
	 * @param strStatusDesc the strStatusDesc to set
	 */
	public void setStatusDesc(String strStatusDesc) {
		this.strStatusDesc = strStatusDesc;
	}
	/**
	 * @return the strPhyName
	 */
	public String getPhyName() {
		return strPhyName;
	}
	/**
	 * @param strPhyName the strPhyName to set
	 */
	public void setPhyName(String strPhyName) {
		this.strPhyName = strPhyName;
	}
	/**
	 * @return the strPhyPhNo
	 */
	public String getPhyPhNo() {
		return strPhyPhNo;
	}
	/**
	 * @param strPhyPhNo the strPhyPhNo to set
	 */
	public void setPhyPhNo(String strPhyPhNo) {
		this.strPhyPhNo = strPhyPhNo;
	}
	/**
	 * @return the strLikelyDoH
	 */
	public String getLikelyDoH() {
		return strLikelyDoH;
	}
	/**
	 * @param strLikelyDoH the strLikelyDoH to set
	 */
	public void setLikelyDoH(String strLikelyDoH) {
		this.strLikelyDoH = strLikelyDoH;
	}
	/**
	 * @return the strStatusTypeId
	 */
	public String getStatusTypeId() {
		return strStatusTypeId;
	}
	/**
	 * @param strStatusTypeId the strStatusTypeId to set
	 */
	public void setStatusTypeId(String strStatusTypeId) {
		this.strStatusTypeId = strStatusTypeId;
	}
	/**
	 * @return the strCityTypeId
	 */
	public String getCityTypeId() {
		return strCityTypeId;
	}
	/**
	 * @param strCityTypeId the strCityTypeId to set
	 */
	public void setCityTypeId(String strCityTypeId) {
		this.strCityTypeId = strCityTypeId;
	}
	/**
	 * @return the strAilDesc
	 */
	public String getAilDesc() {
		return strAilDesc;
	}
	/**
	 * @param strAilDesc the strAilDesc to set
	 */
	public void setAilDesc(String strAilDesc) {
		this.strAilDesc = strAilDesc;
	}
	/**
	 * @return the strMem_name
	 */
	public String getMem_name() {
		return strMem_name;
	}
	/**
	 * @param strMem_name the strMem_name to set
	 */
	public void setMem_name(String strMem_name) {
		this.strMem_name = strMem_name;
	}
	/**
	 * @return the strCallLogSeqId
	 */
	public Long getCallLogSeqId() {
		return strCallLogSeqId;
	}
	/**
	 * @param strCallLogSeqId the strCallLogSeqId to set
	 */
	public void setCallLogSeqId(Long strCallLogSeqId) {
		this.strCallLogSeqId = strCallLogSeqId;
	}
	/**
	 * @return the strGroupId
	 */
	public String getGroupId() {
		return strGroupId;
	}
	/**
	 * @param strGroupId the strGroupId to set
	 */
	public void setGroupId(String strGroupId) {
		this.strGroupId = strGroupId;
	}
	/**
	 * @return the strPatientName
	 */
	public String getPatientName() {
		return strPatientName;
	}
	/**
	 * @param strPatientName the strPatientName to set
	 */
	public void setPatientName(String strPatientName) {
		this.strPatientName = strPatientName;
	}
	/**
	 * @return the strStateTypeId
	 */
	public String getStateTypeId() {
		return strStateTypeId;
	}
	/**
	 * @param strStateTypeId the strStateTypeId to set
	 */
	public void setStateTypeId(String strStateTypeId) {
		this.strStateTypeId = strStateTypeId;
	}
	/**
	 * @return the dtDoa
	 */
	public String getDtDoa() {
		return dtDoa;
	}
	/**
	 * @param dtDoa the dtDoa to set
	 */
	public void setDtDoa(String dtDoa) {
		this.dtDoa = dtDoa;
	}
	/**
	 * @return the strInwardSeqID
	 */
	public Long getInwardSeqID() {
		return strInwardSeqID;
	}
	/**
	 * @param strInwardSeqID the strInwardSeqID to set
	 */
	public void setInwardSeqID(Long strInwardSeqID) {
		this.strInwardSeqID = strInwardSeqID;
	}
	/**
	 * @return the strInwardNbr
	 */
	public String getInwardNbr() {
		return strInwardNbr;
	}
	/**
	 * @param strInwardNbr the strInwardNbr to set
	 */
	public void setInwardNbr(String strInwardNbr) {
		this.strInwardNbr = strInwardNbr;
	}
	/**
	 * @return the strClmEnrollDtlSeqID
	 */
	public Long getClmEnrollDtlSeqID() {
		return strClmEnrollDtlSeqID;
	}
	/**
	 * @param strClmEnrollDtlSeqID the strClmEnrollDtlSeqID to set
	 */
	public void setClmEnrollDtlSeqID(Long strClmEnrollDtlSeqID) {
		this.strClmEnrollDtlSeqID = strClmEnrollDtlSeqID;
	}
	/**
	 * @return the strtEnrollmentID
	 */
	public String getEnrollmentID() {
		return strtEnrollmentID;
	}
	/**
	 * @param strtEnrollmentID the strtEnrollmentID to set
	 */
	public void setEnrollmentID(String strtEnrollmentID) {
		this.strtEnrollmentID = strtEnrollmentID;
	}
	/**
	 * @return the strClaimantName
	 */
	public String getClaimantName() {
		return strClaimantName;
	}
	/**
	 * @param strClaimantName the strClaimantName to set
	 */
	public void setClaimantName(String strClaimantName) {
		this.strClaimantName = strClaimantName;
	}
	/**
	 * @return the strGroupName
	 */
	public String getGroupName() {
		return strGroupName;
	}
	/**
	 * @param strGroupName the strGroupName to set
	 */
	public void setGroupName(String strGroupName) {
		this.strGroupName = strGroupName;
	}
	/**
	 * @return the strClaimTypeDesc
	 */
	public String getClaimTypeDesc() {
		return strClaimTypeDesc;
	}
	/**
	 * @param strClaimTypeDesc the strClaimTypeDesc to set
	 */
	public void setClaimTypeDesc(String strClaimTypeDesc) {
		this.strClaimTypeDesc = strClaimTypeDesc;
	}
	/**
	 * @return the strClaimTypeID
	 */
	public String getClaimTypeID() {
		return strClaimTypeID;
	}
	/**
	 * @param strClaimTypeID the strClaimTypeID to set
	 */
	public void setClaimTypeID(String strClaimTypeID) {
		this.strClaimTypeID = strClaimTypeID;
	}
	/**
	 * @return the strDocumentTypeID
	 */
	public String getDocumentTypeID() {
		return strDocumentTypeID;
	}
	/**
	 * @param strDocumentTypeID the strDocumentTypeID to set
	 */
	public void setDocumentTypeID(String strDocumentTypeID) {
		this.strDocumentTypeID = strDocumentTypeID;
	}
	/**
	 * @return the strClaimStatus
	 */
	public String getClaimStatus() {
		return strClaimStatus;
	}
	/**
	 * @param strClaimStatus the strClaimStatus to set
	 */
	public void setClaimStatus(String strClaimStatus) {
		this.strClaimStatus = strClaimStatus;
	}
	/**
	 * @return the strOfflineFlagYN
	 */
	public String getOfflineFlagYN() {
		return strOfflineFlagYN;
	}
	/**
	 * @param strOfflineFlagYN the strOfflineFlagYN to set
	 */
	public void setOfflineFlagYN(String strOfflineFlagYN) {
		this.strOfflineFlagYN = strOfflineFlagYN;
	}
	/**
	 * @return the strClaimSeqID
	 */
	public String getClaimSeqID() {
		return strClaimSeqID;
	}
	/**
	 * @param strClaimSeqID the strClaimSeqID to set
	 */
	public void setClaimSeqID(String strClaimSeqID) {
		this.strClaimSeqID = strClaimSeqID;
	}
	/**
	 * @return the strTotalApprovedAmount
	 */
	public String getTotalApprovedAmount() {
		return strTotalApprovedAmount;
	}
	/**
	 * @param strTotalApprovedAmount the strTotalApprovedAmount to set
	 */
	public void setTotalApprovedAmount(String strTotalApprovedAmount) {
		this.strTotalApprovedAmount = strTotalApprovedAmount;
	}
	/**
	 * @return the strShortfallPl
	 */
	public String getShortfallPl() {
		return strShortfallPl;
	}
	/**
	 * @param strShortfallPl the strShortfallPl to set
	 */
	public void setShortfallPl(String strShortfallPl) {
		this.strShortfallPl = strShortfallPl;
	}
	/**
	 * @return the strAmmdtPl
	 */
	public String getAmmdtPl() {
		return strAmmdtPl;
	}
	/**
	 * @param strAmmdtPl the strAmmdtPl to set
	 */
	public void setAmmdtPl(String strAmmdtPl) {
		this.strAmmdtPl = strAmmdtPl;
	}
	/**
	 * @return the strStatusPl
	 */
	public String getStatusPl() {
		return strStatusPl;
	}
	/**
	 * @param strStatusPl the strStatusPl to set
	 */
	public void setStatusPl(String strStatusPl) {
		this.strStatusPl = strStatusPl;
	}
	/**
	 * @return the strSettlementNo
	 */
	public String getSettlementNo() {
		return strSettlementNo;
	}
	/**
	 * @param strSettlementNo the strSettlementNo to set
	 */
	public void setSettlementNo(String strSettlementNo) {
		this.strSettlementNo = strSettlementNo;
	}
	/**
	 * @return the strBenefit
	 */
	public String getBenefit() {
		return strBenefit;
	}
	/**
	 * @param strBenefit the strBenefit to set
	 */
	public void setBenefit(String strBenefit) {
		this.strBenefit = strBenefit;
	}
	/**
	 * @return the strProxy_Exclusion
	 */
	public String getProxy_Exclusion() {
		return strProxy_Exclusion;
	}
	/**
	 * @param strProxy_Exclusion the strProxy_Exclusion to set
	 */
	public void setProxy_Exclusion(String strProxy_Exclusion) {
		this.strProxy_Exclusion = strProxy_Exclusion;
	}
	/**
	 * @return the strNon_Medical
	 */
	public String getNon_Medical() {
		return strNon_Medical;
	}
	/**
	 * @param strNon_Medical the strNon_Medical to set
	 */
	public void setNon_Medical(String strNon_Medical) {
		this.strNon_Medical = strNon_Medical;
	}
	/**
	 * @return the strAdded_By
	 */
	public String getAdded_By() {
		return strAdded_By;
	}
	/**
	 * @param strAdded_By the strAdded_By to set
	 */
	public void setAdded_By(String strAdded_By) {
		this.strAdded_By = strAdded_By;
	}
	/**
	 * @return the strAdded_Date
	 */
	public String getAdded_Date() {
		return strAdded_Date;
	}
	/**
	 * @param strAdded_Date the strAdded_Date to set
	 */
	public void setAdded_Date(String strAdded_Date) {
		this.strAdded_Date = strAdded_Date;
	}
	/**
	 * @return the strUpdatedDate
	 */
	public String getUpdatedDate() {
		return strUpdatedDate;
	}
	/**
	 * @param strUpdatedDate the strUpdatedDate to set
	 */
	public void setUpdatedDate(String strUpdatedDate) {
		this.strUpdatedDate = strUpdatedDate;
	}
	/**
	 * @return the strUpdatedBy
	 */
	public String getUpdatedBy() {
		return strUpdatedBy;
	}
	/**
	 * @param strUpdatedBy the strUpdatedBy to set
	 */
	public void setUpdatedBy(String strUpdatedBy) {
		this.strUpdatedBy = strUpdatedBy;
	}
	/**
	 * @return the strLevel
	 */
	public String getLevel() {
		return strLevel;
	}
	/**
	 * @param strLevel the strLevel to set
	 */
	public void setLevel(String strLevel) {
		this.strLevel = strLevel;
	}
	/**
	 * @return the strInsurance_Id
	 */
	public String getInsurance_Id() {
		return strInsurance_Id;
	}
	/**
	 * @param strInsurance_Id the strInsurance_Id to set
	 */
	public void setInsurance_Id(String strInsurance_Id) {
		this.strInsurance_Id = strInsurance_Id;
	}
	/**
	 * @return the strTollFreeNo
	 */
	public String getTollFreeNo() {
		return strTollFreeNo;
	}
	/**
	 * @param strTollFreeNo the strTollFreeNo to set
	 */
	public void setTollFreeNo(String strTollFreeNo) {
		this.strTollFreeNo = strTollFreeNo;
	}
	/**
	 * @return the strCerficateNo
	 */
	public String getCerficateNo() {
		return strCerficateNo;
	}
	/**
	 * @param strCerficateNo the strCerficateNo to set
	 */
	public void setCerficateNo(String strCerficateNo) {
		this.strCerficateNo = strCerficateNo;
	}
	/**
	 * @return the strValidFrom
	 */
	public String getValidFrom() {
		return strValidFrom;
	}
	/**
	 * @param strValidFrom the strValidFrom to set
	 */
	public void setValidFrom(String strValidFrom) {
		this.strValidFrom = strValidFrom;
	}
	/**
	 * @return the strValidUpto
	 */
	public String getValidUpto() {
		return strValidUpto;
	}
	/**
	 * @param strValidUpto the strValidUpto to set
	 */
	public void setValidUpto(String strValidUpto) {
		this.strValidUpto = strValidUpto;
	}
	/**
	 * @return the strCompanyCode
	 */
	public String getCompanyCode() {
		return strCompanyCode;
	}
	/**
	 * @param strCompanyCode the strCompanyCode to set
	 */
	public void setCompanyCode(String strCompanyCode) {
		this.strCompanyCode = strCompanyCode;
	}
	/**
	 * @return the strDOJ
	 */
	public String getDOJ() {
		return strDOJ;
	}
	/**
	 * @param strDOJ the strDOJ to set
	 */
	public void setDOJ(String strDOJ) {
		this.strDOJ = strDOJ;
	}
	/**
	 * @return the strDOB
	 */
	public String getDOB() {
		return strDOB;
	}
	/**
	 * @param strDOB the strDOB to set
	 */
	public void setDOB(String strDOB) {
		this.strDOB = strDOB;
	}
	/**
	 * @return the strCompanyName
	 */
	public String getCompanyName() {
		return strCompanyName;
	}
	/**
	 * @param strCompanyName the strCompanyName to set
	 */
	public void setCompanyName(String strCompanyName) {
		this.strCompanyName = strCompanyName;
	}
	/**
	 * @return the strImage
	 */
	public String getImage() {
		return strImage;
	}
	/**
	 * @param strImage the strImage to set
	 */
	public void setImage(String strImage) {
		this.strImage = strImage;
	}
	/**
	 * @return the strPolicyHolderName
	 */
	public String getPolicyHolderName() {
		return strPolicyHolderName;
	}
	/**
	 * @param strPolicyHolderName the strPolicyHolderName to set
	 */
	public void setPolicyHolderName(String strPolicyHolderName) {
		this.strPolicyHolderName = strPolicyHolderName;
	}
	/**
	 * @return the strRelationShip
	 */
	public String getRelationShip() {
		return strRelationShip;
	}
	/**
	 * @param strRelationShip the strRelationShip to set
	 */
	public void setRelationShip(String strRelationShip) {
		this.strRelationShip = strRelationShip;
	}
	/**
	 * @return the strBankAccNo
	 */
	public String getBankAccNo() {
		return strBankAccNo;
	}
	/**
	 * @param strBankAccNo the strBankAccNo to set
	 */
	public void setBankAccNo(String strBankAccNo) {
		this.strBankAccNo = strBankAccNo;
	}
	/**
	 * @return the strVupt
	 */
	public String getVupt() {
		return strVupt;
	}
	/**
	 * @param strVupt the strVupt to set
	 */
	public void setVupt(String strVupt) {
		this.strVupt = strVupt;
	}
	/**
	 * @return the strVfrm
	 */
	public String getVfrm() {
		return strVfrm;
	}
	/**
	 * @param strVfrm the strVfrm to set
	 */
	public void setVfrm(String strVfrm) {
		this.strVfrm = strVfrm;
	}
	/**
	 * @return the strRemarkd
	 */
	public String getRemarkd() {
		return strRemarkd;
	}
	/**
	 * @param strRemarkd the strRemarkd to set
	 */
	public void setRemarkd(String strRemarkd) {
		this.strRemarkd = strRemarkd;
	}
	/**
	 * @return the strProductName
	 */
	public String getProductName() {
		return strProductName;
	}
	/**
	 * @param strProductName the strProductName to set
	 */
	public void setProductName(String strProductName) {
		this.strProductName = strProductName;
	}
	/**
	 * @return the strAddress1
	 */
	public String getAddress1() {
		return strAddress1;
	}
	/**
	 * @param strAddress1 the strAddress1 to set
	 */
	public void setAddress1(String strAddress1) {
		this.strAddress1 = strAddress1;
	}
	/**
	 * @return the strNotification
	 */
	public String getNotification() {
		return strNotification;
	}
	/**
	 * @param strNotification the strNotification to set
	 */
	public void setNotification(String strNotification) {
		this.strNotification = strNotification;
	}
	/**
	 * @return the strSelfFeedback
	 */
	public String getSelfFeedback() {
		return strSelfFeedback;
	}
	/**
	 * @param strSelfFeedback the strSelfFeedback to set
	 */
	public void setSelfFeedback(String strSelfFeedback) {
		this.strSelfFeedback = strSelfFeedback;
	}
	/**
	 * @return the strFeedbackSeqId
	 */
	public String getFeedbackSeqId() {
		return strFeedbackSeqId;
	}
	/**
	 * @param strFeedbackSeqId the strFeedbackSeqId to set
	 */
	public void setFeedbackSeqId(String strFeedbackSeqId) {
		this.strFeedbackSeqId = strFeedbackSeqId;
	}
	/**
	 * @return the strLocation
	 */
	public String getLocation() {
		return strLocation;
	}
	/**
	 * @param strLocation the strLocation to set
	 */
	public void setLocation(String strLocation) {
		this.strLocation = strLocation;
	}
	/**
	 * @return the strHospitalId
	 */
	public String getHospitalId() {
		return strHospitalId;
	}
	/**
	 * @param strHospitalId the strHospitalId to set
	 */
	public void setHospitalId(String strHospitalId) {
		this.strHospitalId = strHospitalId;
	}
	public Long getPreAuthSeqID() {
		return preAuthSeqID;
	}
	public void setPreAuthSeqID(Long preAuthSeqID) {
		this.preAuthSeqID = preAuthSeqID;
	}

	public String getDecisionDate() {
		return decisionDate;
	}
	public void setDecisionDate(String decisionDate) {
		this.decisionDate = decisionDate;
	}
	public String getPreAuthNumber() {
		return preAuthNumber;
	}
	public void setPreAuthNumber(String preAuthNumber) {
		this.preAuthNumber = preAuthNumber;
	}
	
	

}
