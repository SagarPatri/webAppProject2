
/** @ (#) ReportDetailVO.java July 31, 2008
 * Project     : TTK Healthcare Services
 * File        : ReportDetailVO.java
 * Author      : Ajay Kumar
 * Company     : WebEdge Technologies Pvt.Ltd.
 * Date Created: July 31, 2008
 *
 * @author 		 : Ajay Kumar
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */



package com.ttk.dto.misreports;

import com.ttk.dto.BaseVO;

public class ReportDetailVO extends BaseVO {
	private String strUserId="";
	private String strUserName="";
	private String strUserLocation="";
	private String strTTKBranchName="";
	private String strInsCompanyName="";
	private String strInsDoBOCode="";
	private String strAgentCode="";
	private String strGroupPolicyNo="";
	private String strsType="";
	private String strStatus="";
	private String strStartDate = "";
    private String strEndDate = "";
    private String strReportId="";
    private String strReportName="";
    private String strReportType="";
    private String strReportLink="";
    private Long lReportSeqId=null;
    private String streType="";
    private String strsDomiciOption="";
    private String strtInwardRegister="";
    private String strsHospitalName="";
    private String strsClaimsType="";
    private String strInvestAgencyName="";
	private String strEnrolmentId="";
	private String strCorInsurer="";
	
	/**
	 * @return Returns the getEnrolmentId.
	 */
	public String getEnrolmentId() {
		return strEnrolmentId;
	}//end of public String getEnrolmentId()

	/**
	 * @param getEnrolmentId The getEnrolmentId to set.
	 */
	public void setEnrolmentId(String strEnrolmentId) {
		this.strEnrolmentId = strEnrolmentId;
	}//end of public void setStrAgentCode(String strAgentCode)

	/**
	 * @return Returns the strCorInsurer.
	 */
	public String getCorInsurer() {
		return strCorInsurer;
	}//end of public String getStrGroupPolicyNo()

	/**
	 * @param strCorInsurer The strCorInsurer to set.
	 */
	public void setCorInsurer(String strCorInsurer) {
		this.strCorInsurer = strCorInsurer;
	}// end of public void strCorInsurer(String strCorInsurer)
	/**
    
	/**
	 * Store the user id
	 * @param strUserId String the user id
	 */
	public void setUSER_ID(String strUserId)
	{
		this.strUserId = strUserId;
	}//end of setUSER_ID(String strUserId)

	/**
	 * Retrieve the user id
	 * @return strUserId String the user id
	 */
	public String getUSER_ID()
	{
		return strUserId;
	}//end of getUSER_ID()
	/**
     * Retrieve User Name
     * @return  strUserName String
     */
    public String getUserName() {
        return strUserName;
    }//end of getUserName()

    /**
     * Sets User Name
     * @param  strUserName String
     */
    public void setUserName(String strUserName) {
        this.strUserName = strUserName;
    }//end of setUserName(String strUserName)
    /**
     * Retrieve the Branch Name
     * @return  strBranchName String
     */
    public String getUserLocation() {
        return strUserLocation;
    }//end of getUserLocation()

    /**
     * Sets the Branch Name
     * @param  strBranchName String
     */
    public void setUserLocation(String strUserLocation) {
        this.strUserLocation = strUserLocation;
    }//end of setUserLocation(String strUserLocation)
    

	

	/**
	 * @return Returns the strTTKBranchName.
	 */
	public String getTTKBranchName() {
		return strTTKBranchName;
	}//end of public String getTTKBranchName()

	/**
	 * @param strTTKBranchName The strTTKBranchName to set.
	 */
	public void setTTKBranchName(String strTTKBranchName) {
		this.strTTKBranchName = strTTKBranchName;
	}//end of public void setTTKBranchName(String strTTKBranchName)

	

	

	/**
	 * @return Returns the strInsCompanyName.
	 */
	public String getInsCompanyName() {
		return strInsCompanyName;
	}//end of public String getInsCompanyName()

	/**
	 * @param strInsCompanyName The strInsCompanyName to set.
	 */
	public void setInsCompanyName(String strInsCompanyName) {
		this.strInsCompanyName = strInsCompanyName;
	}//end of public void setInsCompanyName(String strInsCompanyName)

	/**
	 * @return Returns the strInsDoBOCode.
	 */
	public String getInsDoBOCode() {
		return strInsDoBOCode;
	}//end of public String getInsDoBOCode()

	/**
	 * @param strInsDoBOCode The strInsDoBOCode to set.
	 */
	public void setInsDoBOCode(String strInsDoBOCode) {
		this.strInsDoBOCode = strInsDoBOCode;
	}//end of public void setInsDoBOCode(String strInsDoBOCode)

	/**
	 * @return Returns the strAgentCode.
	 */
	public String getAgentCode() {
		return strAgentCode;
	}//end of public String getStrAgentCode()

	/**
	 * @param strAgentCode The strAgentCode to set.
	 */
	public void setAgentCode(String strAgentCode) {
		this.strAgentCode = strAgentCode;
	}//end of public void setStrAgentCode(String strAgentCode)

	/**
	 * @return Returns the strGroupPolicyNo.
	 */
	public String getGroupPolicyNo() {
		return strGroupPolicyNo;
	}//end of public String getStrGroupPolicyNo()

	/**
	 * @param strGroupPolicyNo The strGroupPolicyNo to set.
	 */
	public void setGroupPolicyNo(String strGroupPolicyNo) {
		this.strGroupPolicyNo = strGroupPolicyNo;
	}// end of public void setStrGroupPolicyNo(String strGroupPolicyNo)
	/**Retrieve the sType
	 * @return Returns the sType.
	 */
	public String getSType() {
		return strsType;
	}//end of getSType()

	/**Sets the sType
	 * @param groupId The sType to set.
	 */
	public void setSType(String strsType) {
		this.strsType = strsType;
	}//end of setSType(String sType)
	/**Retrieve the sStatus
	 * @return Returns the sStatus.
	 */
	public String getSStatus() {
		return strStatus;
	}//end of getSStatus()

	/**Sets the sStatus
	 * @param  The sStatus to set.
	 */
	public void setSStatus(String strStatus) {
		this.strStatus = strStatus;
	}//end of setSStatus(String sStatus)
	/** This method returns the Start Date
     * @return Returns the strStartDate.
     */
    public String getStartDate() {
        return strStartDate;
    }//end of getStartDate()
    
    /** This method sets the Start Date
     * @param strStartDate The strStartDate to set.
     */
    public void setStartDate(String strStartDate) {
        this.strStartDate = strStartDate;
    }//end of setStartDate(Date strStartDate)
    /** This method returns the End Date 
     * @return Returns the strEndDate.
     */
    public String getEndDate() {
        return strEndDate;
    }//end of getEndDate()
    
    /** This method sets the End Date 
     * @param strEndDate The strEndDate to set.
     */
    public void setEndDate(String strEndDate) {
        this.strEndDate = strEndDate;
    }//end of setEndDate(Date strEndDate)

	/**
	 * @return Returns the strReportName.
	 */
	public String getReportName() {
		return strReportName;
	}//end of public String getReportName()

	/**
	 * @param strReportName The strReportName to set.
	 */
	public void setReportName(String strReportName) {
		this.strReportName = strReportName;
	}//end of public void setReportName(String strReportName)

	/**
	 * @return Returns the strReportId.
	 */
	public String getReportId() {
		return strReportId;
	}//end of public String getReportId()

	/**
	 * @param strReportId The strReportId to set.
	 */
	public void setReportId(String strReportId) {
		this.strReportId = strReportId;
	}//end of public void setReportId(String strReportId)

	/**
	 * @return Returns the strReportType.
	 */
	public String getReportType() {
		return strReportType;
	}//end of public String getReportType()

	/**
	 * @param strReportType The strReportType to set.
	 */
	public void setReportType(String strReportType) {
		this.strReportType = strReportType;
	}//end of public void setReportType(String strReportType) 

	/**
	 * @return Returns the strReportLink.
	 */
	public String getReportLink() {
		return strReportLink;
	}//end of public String getReportLink()

	/**
	 * @param strReportLink The strReportLink to set.
	 */
	public void setReportLink(String strReportLink) {
		this.strReportLink = strReportLink;
	}//end of public void setReportLink(String strReportLink)

	/**
	 * @return Returns the lReportSeqId.
	 */
	public Long getReportSeqId() {
		return lReportSeqId;
	}//end of public Long getReportSeqId()

	/**
	 * @param reportSeqId The lReportSeqId to set.
	 */
	public void setReportSeqId(Long reportSeqId) {
		lReportSeqId = reportSeqId;
	}//end of public void setReportSeqId(Long reportSeqId)

	/**
	 * @return Returns the streType.
	 */
	public String geteType() {
		return streType;
	}//end of public String geteType()

	/**
	 * @param streType The streType to set.
	 */
	public void seteType(String streType) {
		this.streType = streType;
	}//end of public void seteType(String streType)

	/**
	 * @return Returns the strsDomiciOption.
	 */
	public String getsDomiciOption() {
		return strsDomiciOption;
	}//end of public String getsDomiciOption()

	/**
	 * @param strsDomiciOption The strsDomiciOption to set.
	 */
	public void setsDomiciOption(String strsDomiciOption) {
		this.strsDomiciOption = strsDomiciOption;
	}//end of public void setsDomiciOption(String strsDomiciOption)

	/**
	 * @return Returns the strtInwardRegister.
	 */
	public String gettInwardRegister() {
		return strtInwardRegister;
	}//end of public String gettInwardRegister()

	/**
	 * @param strtInwardRegister The strtInwardRegister to set.
	 */
	public void settInwardRegister(String strtInwardRegister) {
		this.strtInwardRegister = strtInwardRegister;
	}//end of public void settInwardRegister(String strtInwardRegister)

	/**
	 * @return Returns the strsHospitalName.
	 */
	public String getsHospitalName() {
		return strsHospitalName;
	}//end of public String getsHospitalName()

	/**
	 * @param strsHospitalName The strsHospitalName to set.
	 */
	public void setsHospitalName(String strsHospitalName) {
		this.strsHospitalName = strsHospitalName;
	}//end of public void setsHospitalName(String strsHospitalName)

	/**
	 * @return Returns the strsClaimsType.
	 */
	public String getsClaimsType() {
		return strsClaimsType;
	}//end of public String getsClaimsType()

	/**
	 * @param strsClaimsType The strsClaimsType to set.
	 */
	public void setsClaimsType(String strsClaimsType) {
		this.strsClaimsType = strsClaimsType;
	}//end of public void setsClaimsType(String strsClaimsType)
	
	/**
	 * @return Returns the strInvestAgencyName.
	 */
	public String getInvestAgencyName() {
		return strInvestAgencyName;
	}//end of public String getInvestAgencyName()

	/**
	 * @param strInvestAgencyName The strInvestAgencyName to set.
	 */
	public void setInvestAgencyName(String strInvestAgencyName) {
		this.strInvestAgencyName = strInvestAgencyName;
	}//end of public void setInvestAgencyName(String strInvestAgencyName)

	
}//end of public class ReportDetailVO 
