package com.ttk.dto.finance;

import com.ttk.dto.BaseVO;

public class PharmacySearchVO extends BaseVO{

	private static final long serialVersionUID = 1L;
	
	private String ddcCode;
	private String shortDesc;
	private String startDate;
	private String endDate;
	private String gender;
	private String qatarExcYN;
	private String status;
	private String pharSeqId;
	private String reviewedYN;
	public String getDdcCode() {
		return ddcCode;
	}
	public void setDdcCode(String ddcCode) {
		this.ddcCode = ddcCode;
	}
	public String getShortDesc() {
		return shortDesc;
	}
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getQatarExcYN() {
		return qatarExcYN;
	}
	public void setQatarExcYN(String qatarExcYN) {
		this.qatarExcYN = qatarExcYN;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPharSeqId() {
		return pharSeqId;
	}
	public void setPharSeqId(String pharSeqId) {
		this.pharSeqId = pharSeqId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/**
	 * @return the reviewedYN
	 */
	public String getReviewedYN() {
		return reviewedYN;
	}
	/**
	 * @param reviewedYN the reviewedYN to set
	 */
	public void setReviewedYN(String reviewedYN) {
		this.reviewedYN = reviewedYN;
	}
	
	
}

