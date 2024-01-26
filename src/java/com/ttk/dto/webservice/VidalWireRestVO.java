package com.ttk.dto.webservice;
 
public class VidalWireRestVO {
	private String strUsername = "";
    private String strPassword = "";
    private String strErrorMessage = "";
    private String strFirstPasswordYN="";
    private String StrPwdExpiryYN="";
    private String strPwdAlertMsg="";
    private String firstLoginYN="";
    private String loginStatus="";
    private String policyGroupSeqID="";
    private String policySeqID="";
    private String strUserID="";
    private String strGroupID="";
    private String strPolicyNo="";
    private String memberSeqId="";

    private String otpNumber="";

    
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
	public String getGroupID() {
		return strGroupID;
	}
	/**
	 * @param strGroupID the strGroupID to set
	 */
	public void setGroupID(String strGroupID) {
		this.strGroupID = strGroupID;
	}
	public String getMemberSeqId() {
		return memberSeqId;
	}
	public void setMemberSeqId(String memberSeqId) {
		this.memberSeqId = memberSeqId;
	}
	public String getOtpNumber() {
		return otpNumber;
	}
	public void setOtpNumber(String otpNumber) {
		this.otpNumber = otpNumber;
	}

}
