package com.ttk.dao.impl.webservice;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class PreApprovalStatusResponse {
	
	private String preApprovalNo;
	private String preApprovalStatus;
	private String authorizationNumber;
	private String totalGrossAmount;
	private String totalDiscountAmount;
	private String totalDiscountGrossAmount;
	private String totalPatientShare;
	private String totalNetAmount;
	private String totalDisAllowedAmount;
	private String totalAllowedAmount;

	private List<ActivityDetails> listActivityDetails;
	
	public String getPreApprovalNo() {
		return preApprovalNo;
	}
	@XmlAttribute
	public void setPreApprovalNo(String preApprovalNo) {
		this.preApprovalNo = preApprovalNo;
	}
	public String getPreApprovalStatus() {
		return preApprovalStatus;
	}
	@XmlAttribute
	public void setPreApprovalStatus(String preApprovalStatus) {
		this.preApprovalStatus = preApprovalStatus;
	}
	public String getAuthorizationNumber() {
		return authorizationNumber;
	}
	@XmlAttribute
	public void setAuthorizationNumber(String authorizationNumber) {
		this.authorizationNumber = authorizationNumber;
	}
	public String getTotalGrossAmount() {
		return totalGrossAmount;
	}
	@XmlAttribute
	public void setTotalGrossAmount(String totalGrossAmount) {
		this.totalGrossAmount = totalGrossAmount;
	}
	public String getTotalDiscountAmount() {
		return totalDiscountAmount;
	}
	@XmlAttribute
	public void setTotalDiscountAmount(String totalDiscountAmount) {
		this.totalDiscountAmount = totalDiscountAmount;
	}
	public String getTotalDiscountGrossAmount() {
		return totalDiscountGrossAmount;
	}
	@XmlAttribute
	public void setTotalDiscountGrossAmount(String totalDiscountGrossAmount) {
		this.totalDiscountGrossAmount = totalDiscountGrossAmount;
	}
	public String getTotalPatientShare() {
		return totalPatientShare;
	}
	@XmlAttribute
	public void setTotalPatientShare(String totalPatientShare) {
		this.totalPatientShare = totalPatientShare;
	}
	public String getTotalNetAmount() {
		return totalNetAmount;
	}
	@XmlAttribute
	public void setTotalNetAmount(String totalNetAmount) {
		this.totalNetAmount = totalNetAmount;
	}
	public String getTotalDisAllowedAmount() {
		return totalDisAllowedAmount;
	}
	@XmlAttribute
	public void setTotalDisAllowedAmount(String totalDisAllowedAmount) {
		this.totalDisAllowedAmount = totalDisAllowedAmount;
	}
	public String getTotalAllowedAmount() {
		return totalAllowedAmount;
	}
	@XmlAttribute
	public void setTotalAllowedAmount(String totalAllowedAmount) {
		this.totalAllowedAmount = totalAllowedAmount;
	}
	public List<ActivityDetails> getListActivityDetails() {
		return listActivityDetails;
	}
	public void setListActivityDetails(List<ActivityDetails> listActivityDetails) {
		this.listActivityDetails = listActivityDetails;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


	
	
}

