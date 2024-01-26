package com.ttk.dao.impl.webservice;
import javax.xml.bind.annotation.XmlAttribute;

public class ActivityDetails {

	String slno;
	String activityCodeServiceCode;
	String quantity;
	String approvedQuantity;
	String startDate;
	String grossAmount;
	String discountAmount;
	String discountGrossAmount;
	String patientShareAmount;
	String netAmount;
	String disAllowedAmount;
	String allowedAmount;
	String denialCode;
	String denialCodeDesc;
	
	public String getSlno() {
		return slno;
	}
	@XmlAttribute
	public void setSlno(String slno) {
		this.slno = slno;
	}
	public String getActivityCodeServiceCode() {
		return activityCodeServiceCode;
	}
	@XmlAttribute
	public void setActivityCodeServiceCode(String activityCodeServiceCode) {
		this.activityCodeServiceCode = activityCodeServiceCode;
	}
	public String getQuantity() {
		return quantity;
	}
	@XmlAttribute
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getApprovedQuantity() {
		return approvedQuantity;
	}
	@XmlAttribute
	public void setApprovedQuantity(String approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}
	public String getStartDate() {
		return startDate;
	}
	@XmlAttribute
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getGrossAmount() {
		return grossAmount;
	}
	@XmlAttribute
	public void setGrossAmount(String grossAmount) {
		this.grossAmount = grossAmount;
	}
	public String getDiscountAmount() {
		return discountAmount;
	}
	@XmlAttribute
	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}
	public String getDiscountGrossAmount() {
		return discountGrossAmount;
	}
	@XmlAttribute
	public void setDiscountGrossAmount(String discountGrossAmount) {
		this.discountGrossAmount = discountGrossAmount;
	}
	public String getPatientShareAmount() {
		return patientShareAmount;
	}
	@XmlAttribute
	public void setPatientShareAmount(String patientShareAmount) {
		this.patientShareAmount = patientShareAmount;
	}
	public String getNetAmount() {
		return netAmount;
	}
	@XmlAttribute
	public void setNetAmount(String netAmount) {
		this.netAmount = netAmount;
	}
	public String getDisAllowedAmount() {
		return disAllowedAmount;
	}
	@XmlAttribute
	public void setDisAllowedAmount(String disAllowedAmount) {
		this.disAllowedAmount = disAllowedAmount;
	}
	public String getAllowedAmount() {
		return allowedAmount;
	}
	@XmlAttribute
	public void setAllowedAmount(String allowedAmount) {
		this.allowedAmount = allowedAmount;
	}
	public String getDenialCode() {
		return denialCode;
	}
	@XmlAttribute
	public void setDenialCode(String denialCode) {
		this.denialCode = denialCode;
	}
	public String getDenialCodeDesc() {
		return denialCodeDesc;
	}
	@XmlAttribute
	public void setDenialCodeDesc(String denialCodeDesc) {
		this.denialCodeDesc = denialCodeDesc;
	}
	
}

