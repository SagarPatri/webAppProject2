/** 
 *  @ (#)ClaimantVO.java Apr 10, 2006
 *  Project       : TTK HealthCare Services
 *  File          : ClaimantVO.java
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

package com.ttk.dto.preauth;

import com.ttk.dto.BaseVO;




public class DiagnosisDetailsVO extends BaseVO{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/**
	 * 
	 */
private String	primaryAilment;
private String	ailmentDescription;
private String	icdCode;
private Long icdCodeSeqId;
private String presentingComplaints;
//Added For PED
private Integer durAilment=new Integer(0);
private String durationFlag="";

private String preAuthNo;
private Long preAuthSeqID;
private Long claimSeqID;
private Long diagSeqId;
private String authType;
private long diagnosiscount;
public String getDiagnosisType() {
	return diagnosisType;
}
public void setDiagnosisType(String diagnosisType) {
	this.diagnosisType = diagnosisType;
}
private String diagnosisType;
private String primaryIcdCode;
private String secondaryIcdCode;
private String primaryIcdDesc;
private String secondaryIcdDesc;
private String toothNumber;
private String quantity;

private String enhanceYN="N";
public DiagnosisDetailsVO() {
	// TODO Auto-generated constructor stub
}
public DiagnosisDetailsVO(String primaryAilment, String ailmentDescription,
		String icdCode, Long icdCodeSeqId, String presentingComplaints,
		String preAuthNo, Long preAuthSeqID, Long diagSeqId) {
	super();
	this.primaryAilment = primaryAilment;
	this.ailmentDescription = ailmentDescription;
	this.icdCode = icdCode;
	this.icdCodeSeqId = icdCodeSeqId;
	this.presentingComplaints = presentingComplaints;
	this.preAuthNo = preAuthNo;
	this.preAuthSeqID = preAuthSeqID;
	this.diagSeqId = diagSeqId;	
}

public DiagnosisDetailsVO(String icdCode,String ailmentDescription,String primaryAilment,Long diagSeqId,Long icdCodeSeqId) {
	super();
	this.primaryAilment = primaryAilment;
	this.ailmentDescription = ailmentDescription;
	this.icdCode = icdCode;
	this.diagSeqId = diagSeqId;
	this.icdCodeSeqId=icdCodeSeqId;
}
public DiagnosisDetailsVO(String presentingComplaints, String primaryIcdCode,
		String primaryIcdDesc, String secondaryIcdCode, String secondaryIcdDesc, String toothNumber,
		String quantity) {

	super();
	
	this.presentingComplaints = presentingComplaints;
	this.primaryIcdCode = primaryIcdCode;
	this.primaryIcdDesc = primaryIcdDesc;
	this.secondaryIcdCode = secondaryIcdCode;
	this.secondaryIcdDesc = secondaryIcdDesc;
	this.toothNumber = toothNumber;
	this.quantity = quantity;

}

public String getEnhanceYN() {
	return enhanceYN;
}
public void setEnhanceYN(String enhanceYN) {
	this.enhanceYN = enhanceYN;
}
public String getPrimaryAilment() {
	return primaryAilment;
}
public void setPrimaryAilment(String primaryAilment) {
	this.primaryAilment = primaryAilment;
}
public String getIcdCode() {
	return icdCode;
}
public void setIcdCode(String icdCode) {
	this.icdCode = icdCode;
}
public Long getIcdCodeSeqId() {
	return icdCodeSeqId;
}
public void setIcdCodeSeqId(Long icdCodeSeqId) {
	this.icdCodeSeqId = icdCodeSeqId;
}
public String getPresentingComplaints() {
	return presentingComplaints;
}
public void setPresentingComplaints(String presentingComplaints) {
	this.presentingComplaints = presentingComplaints;
}
public String getAilmentDescription() {
	return ailmentDescription;
}
public void setAilmentDescription(String ailmentDescription) {
	this.ailmentDescription = ailmentDescription;
}
public String getPreAuthNo() {
	return preAuthNo;
}
public void setPreAuthNo(String preAuthNo) {
	this.preAuthNo = preAuthNo;
}
public Long getPreAuthSeqID() {
	return preAuthSeqID;
}
public void setPreAuthSeqID(Long preAuthSeqID) {
	this.preAuthSeqID = preAuthSeqID;
}
public Long getDiagSeqId() {
	return diagSeqId;
}
public void setDiagSeqId(Long diagSeqId) {
	this.diagSeqId = diagSeqId;
}
public String getAuthType() {
	return authType;
}
public void setAuthType(String authType) {
	this.authType = authType;
}
public Long getClaimSeqID() {
	return claimSeqID;
}
public void setClaimSeqID(Long claimSeqID) {
	this.claimSeqID = claimSeqID;
}
public Integer getDurAilment() {
	return durAilment;
}
public void setDurAilment(Integer durAilment) {
	this.durAilment = durAilment;
}
public String getDurationFlag() {
	return durationFlag;
}
public void setDurationFlag(String durationFlag) {
	this.durationFlag = durationFlag;
}
public long getDiagnosiscount() {
	return diagnosiscount;
}
public void setDiagnosiscount(long diagnosiscount) {
	this.diagnosiscount = diagnosiscount;
}
public String getPrimaryIcdCode() {
	return primaryIcdCode;
}
public void setPrimaryIcdCode(String primaryIcdCode) {
	this.primaryIcdCode = primaryIcdCode;
}
public String getSecondaryIcdCode() {
	return secondaryIcdCode;
}
public void setSecondaryIcdCode(String secondaryIcdCode) {
	this.secondaryIcdCode = secondaryIcdCode;
}
public String getPrimaryIcdDesc() {
	return primaryIcdDesc;
}
public void setPrimaryIcdDesc(String primaryIcdDesc) {
	this.primaryIcdDesc = primaryIcdDesc;
}
public String getSecondaryIcdDesc() {
	return secondaryIcdDesc;
}
public void setSecondaryIcdDesc(String secondaryIcdDesc) {
	this.secondaryIcdDesc = secondaryIcdDesc;
}
public String getToothNumber() {
	return toothNumber;
}
public void setToothNumber(String toothNumber) {
	this.toothNumber = toothNumber;
}
public String getQuantity() {
	return quantity;
}
public void setQuantity(String quantity) {
	this.quantity = quantity;
}
}//end of ClaimantVO.java