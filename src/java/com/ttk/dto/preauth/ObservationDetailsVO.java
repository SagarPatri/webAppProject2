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




public class ObservationDetailsVO extends BaseVO{

private static final long serialVersionUID = 1L;
private Long preAuthSeqID;
private Long claimSeqID;
private Long observSeqId; 
private Long activityDtlSeqId;
private String observType;
private String observCode;
private String observCodeDesc;
private String observValue;
private String observValueType;
private String observValueTypeDesc;
private String observRemarks;
private String authType;


public String getObservType() {
	return observType;
}
public void setObservType(String observType) {
	this.observType = observType;
}
public String getObservCode() {
	return observCode;
}
public void setObservCode(String observCode) {
	this.observCode = observCode;
}
public String getObservValue() {
	return observValue;
}
public void setObservValue(String observValue) {
	this.observValue = observValue;
}
public String getObservValueType() {
	return observValueType;
}
public void setObservValueType(String observValueType) {
	this.observValueType = observValueType;
}
public Long getActivityDtlSeqId() {
	return activityDtlSeqId;
}
public void setActivityDtlSeqId(Long activityDtlSeqId) {
	this.activityDtlSeqId = activityDtlSeqId;
}
public String getObservRemarks() {
	return observRemarks;
}
public void setObservRemarks(String observRemarks) {
	this.observRemarks = observRemarks;
}
public Long getPreAuthSeqID() {
	return preAuthSeqID;
}
public void setPreAuthSeqID(Long preAuthSeqID) {
	this.preAuthSeqID = preAuthSeqID;
}
public Long getObservSeqId() {
	return observSeqId;
}
public void setObservSeqId(Long observSeqId) {
	this.observSeqId = observSeqId;
}
public String getObservCodeDesc() {
	return observCodeDesc;
}
public void setObservCodeDesc(String observCodeDesc) {
	this.observCodeDesc = observCodeDesc;
}
public String getObservValueTypeDesc() {
	return observValueTypeDesc;
}
public void setObservValueTypeDesc(String observValueTypeDesc) {
	this.observValueTypeDesc = observValueTypeDesc;
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

}//end of ObservationDetailsVO.java