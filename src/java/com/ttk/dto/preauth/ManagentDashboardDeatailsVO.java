package com.ttk.dto.preauth;

import com.ttk.dto.BaseVO;

public class ManagentDashboardDeatailsVO  extends BaseVO{
	
	 /**
	 * 
	 */
	 private static final long serialVersionUID = 1L;
	 private String userName = "";
	 private String userCurStatus = "";
	 private String noOfAssignedCases = "";
	 private String noOfCompletedCases = "";
	 private String officeCode = "";
	 
	
	 
	
	 
	
	public String getUserCurStatus() {
		return userCurStatus;
	}
	public void setUserCurStatus(String userCurStatus) {
		this.userCurStatus = userCurStatus;
	}
	public String getNoOfAssignedCases() {
		return noOfAssignedCases;
	}
	public void setNoOfAssignedCases(String noOfAssignedCases) {
		this.noOfAssignedCases = noOfAssignedCases;
	}
	public String getNoOfCompletedCases() {
		return noOfCompletedCases;
	}
	public void setNoOfCompletedCases(String noOfCompletedCases) {
		this.noOfCompletedCases = noOfCompletedCases;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOfficeCode() {
		return officeCode;
	}
	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	

}
