package com.ttk.dto.preauth;

import com.ttk.dto.BaseVO;

public class AuthorizationDashboardVO extends BaseVO{
	 private static final long serialVersionUID = 1L;
	 private String preauthNo ="";
	 private String status = "";
	 private String receivedDate ="";
	 private String enrolmentId ="";
	 private String tat="";
	 private String corporateName ="";
	 private String claimantName ="";
	 private String patRequestedAmount = "";
	 private String assigneeName = "";
	 private String location;
	 private String remark ="";
	 private Long enrollDtlSeqID = null;		
	 private Long preAuthSeqID = null;
	 private Long memberSeqID = null;
	 private Long hospitalRefSeqID = null;
	 private Long policySeqID = null;
	 private Long contactSeqId = null;
	 
		private String updateTime="";
		private String elapsedTime="";
		private String  receivedTime ="";
		
		private String imgIconOne = "";
		private String imgIconTwo = "";
		
	public String getPreauthNo() {
		return preauthNo;
	}
	public void setPreauthNo(String preauthNo) {
		this.preauthNo = preauthNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}
	public String getEnrolmentId() {
		return enrolmentId;
	}
	public void setEnrolmentId(String enrolmentId) {
		this.enrolmentId = enrolmentId;
	}
	public String getTat() {
		return tat;
	}
	public void setTat(String tat) {
		this.tat = tat;
	}
	public String getCorporateName() {
		return corporateName;
	}
	public void setCorporateName(String corporateName) {
		this.corporateName = corporateName;
	}
	public String getClaimantName() {
		return claimantName;
	}
	public void setClaimantName(String claimantName) {
		this.claimantName = claimantName;
	}
	public String getPatRequestedAmount() {
		return patRequestedAmount;
	}
	public void setPatRequestedAmount(String patRequestedAmount) {
		this.patRequestedAmount = patRequestedAmount;
	}
	public String getAssigneeName() {
		return assigneeName;
	}
	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Long getEnrollDtlSeqID() {
		return enrollDtlSeqID;
	}
	public void setEnrollDtlSeqID(Long enrollDtlSeqID) {
		this.enrollDtlSeqID = enrollDtlSeqID;
	}
	public Long getPreAuthSeqID() {
		return preAuthSeqID;
	}
	public void setPreAuthSeqID(Long preAuthSeqID) {
		this.preAuthSeqID = preAuthSeqID;
	}
	public Long getMemberSeqID() {
		return memberSeqID;
	}
	public void setMemberSeqID(Long memberSeqID) {
		this.memberSeqID = memberSeqID;
	}
	public Long getHospitalRefSeqID() {
		return hospitalRefSeqID;
	}
	public void setHospitalRefSeqID(Long hospitalRefSeqID) {
		this.hospitalRefSeqID = hospitalRefSeqID;
	}
	public Long getPolicySeqID() {
		return policySeqID;
	}
	public void setPolicySeqID(Long policySeqID) {
		this.policySeqID = policySeqID;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getElapsedTime() {
		return elapsedTime;
	}
	public void setElapsedTime(String elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	public String getReceivedTime() {
		return receivedTime;
	}
	public void setReceivedTime(String receivedTime) {
		this.receivedTime = receivedTime;
	}
	public String getImgIconOne() {
		return imgIconOne;
	}
	public void setImgIconOne(String imgIconOne) {
		this.imgIconOne = imgIconOne;
	}
	public String getImgIconTwo() {
		return imgIconTwo;
	}
	public void setImgIconTwo(String imgIconTwo) {
		this.imgIconTwo = imgIconTwo;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getContactSeqId() {
		return contactSeqId;
	}
	public void setContactSeqId(Long contactSeqId) {
		this.contactSeqId = contactSeqId;
	}

	 
}
