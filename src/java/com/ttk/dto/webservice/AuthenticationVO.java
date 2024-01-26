package com.ttk.dto.webservice;



import java.io.InputStream;

import com.ttk.dto.BaseVO;

public class AuthenticationVO extends BaseVO {
	
	private static final long serialVersionUID = 1L;
	
	private String userID;
	private String employeeNo;
	private String dateOfBirth;
	private String dateOfJoining;
	private String dateOfMarriage;
	private String gender;
	private String sumInsurred;
	private String grade;
	
	private String accountNo;
	private String branch;
	private String IFSCCode;
	private String bankName;
	private String disclaimerDetails;
	
	private String mobileNo;
	private String employeeName;
	private String address1;
	private String address2;
	private String phone1;
	private String phone2;
	private String emailID;
	private String state;
	private String city;
	private String country;
	private String pinCode;
	private Long covPolicySeqID;

	private Integer claimIntimation;
	private String claimPending;
	private String totalAmount;
	private String addressSeqID;
	private Long policyGroupSeqID;
	private Long policySeqID;
	//private MultipartFile profileImage;
	private String profileFile;
	private InputStream inputStream;
	private String groupRegSeqID;
	//Added for Accenture Second Phase CR
	private String contactUsYN;
	private String faqsCorYN;
	private String strGroupId;
	private String strPolicyNo;
	private String strPasswordAlert;
	private String strWindowPeriodAlert;
	/**
	 * @return the contactUsYN
	 */
	public String getContactUsYN() {
		return contactUsYN;
	}
	/**
	 * @param contactUsYN the contactUsYN to set
	 */
	public void setContactUsYN(String contactUsYN) {
		this.contactUsYN = contactUsYN;
	}
	/**
	 * @return the faqsCorYN
	 */
	public String getFaqsCorYN() {
		return faqsCorYN;
	}
	/**
	 * @param faqsCorYN the faqsCorYN to set
	 */
	public void setFaqsCorYN(String faqsCorYN) {
		this.faqsCorYN = faqsCorYN;
	}
	public String getClaimPending() {
		return claimPending;
	}
	public void setClaimPending(String claimPending) {
		this.claimPending = claimPending;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getDisclaimerDetails() {
		return disclaimerDetails;
	}
	public void setDisclaimerDetails(String disclaimerDetails) {
		this.disclaimerDetails = disclaimerDetails;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String getEmployeeNo() {
		return employeeNo;
	}
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
	
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getDateOfJoining() {
		return dateOfJoining;
	}
	public void setDateOfJoining(String dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}
	public String getDateOfMarriage() {
		return dateOfMarriage;
	}
	public void setDateOfMarriage(String dateOfMarriage) {
		this.dateOfMarriage = dateOfMarriage;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getSumInsurred() {
		return sumInsurred;
	}
	public void setSumInsurred(String sumInsurred) {
		this.sumInsurred = sumInsurred;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getIFSCCode() {
		return IFSCCode;
	}
	public void setIFSCCode(String iFSCCode) {
		IFSCCode = iFSCCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getPhone1() {
		return phone1;
	}
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	public String getEmailID() {
		return emailID;
	}
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPinCode() {
		return pinCode;
	}
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	public Integer getClaimIntimation() {
		return claimIntimation;
	}
	public void setClaimIntimation(Integer claimIntimation) {
		this.claimIntimation = claimIntimation;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getAddressSeqID() {
		return addressSeqID;
	}
	public void setAddressSeqID(String addressSeqID) {
		this.addressSeqID = addressSeqID;
	}
	public Long getCovPolicySeqID() {
		return covPolicySeqID;
	}
	public void setCovPolicySeqID(Long covPolicySeqID) {
		this.covPolicySeqID = covPolicySeqID;
	}
	public Long getPolicyGroupSeqID() {
		return policyGroupSeqID;
	}
	public void setPolicyGroupSeqID(Long policyGroupSeqID) {
		this.policyGroupSeqID = policyGroupSeqID;
	}
	
	public String getProfileFile() {
		return profileFile;
	}
	public void setProfileFile(String profileFile) {
		this.profileFile = profileFile;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public String getGroupRegSeqID() {
		return groupRegSeqID;
	}
	public void setGroupRegSeqID(String groupRegSeqID) {
		this.groupRegSeqID = groupRegSeqID;
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
	 * @return the strPasswordAlert
	 */
	public String getPasswordAlert() {
		return strPasswordAlert;
	}
	/**
	 * @param strPasswordAlert the strPasswordAlert to set
	 */
	public void setPasswordAlert(String strPasswordAlert) {
		this.strPasswordAlert = strPasswordAlert;
	}
	/**
	 * @return the strWindowPeriodAlert
	 */
	public String getWindowPeriodAlert() {
		return strWindowPeriodAlert;
	}
	/**
	 * @param strWindowPeriodAlert the strWindowPeriodAlert to set
	 */
	public void setWindowPeriodAlert(String strWindowPeriodAlert) {
		this.strWindowPeriodAlert = strWindowPeriodAlert;
	}
	/**
	 * @return the policySeqID
	 */
	public Long getPolicySeqID() {
		return policySeqID;
	}
	/**
	 * @param policySeqID the policySeqID to set
	 */
	public void setPolicySeqID(Long policySeqID) {
		this.policySeqID = policySeqID;
	}


}

