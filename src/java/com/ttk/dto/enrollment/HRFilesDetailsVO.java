/**
 * @ (#) OnlineAccessPolicyVO.java Aug 3, 2006
 * Project      : TTK HealthCare Services
 * File         : OnlineAccessPolicyVO.java
 * Author       : Arun K N
 * Company      : Span Systems Corporation
 * Date Created : Aug 3, 2006
 *
 * @author       :  Arun K N
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.dto.enrollment;

import java.util.Date;

import com.ttk.common.TTKCommon;

/**
 *  This Data transfer object is used to hold the Polciy Information
 *  when user works with Policy in OnlineAccess Flow
 *
 */
public class HRFilesDetailsVO extends PolicyVO {

                         
    private String hrUploadDate="";                       
    private String strHRUserID="";              
    private String strFilePath;    
       
    private String strFileName;                     
    private String strUploadMode;  
    private String strGroupID;
    
    private String uploadedBy;
    private String strStatus;
    private String statusChangedDate;
    private String statusChangedBy;
    
    private Integer fileSequenceId;
    
    private String fileUploadedFor;
    
	public String getStrHRUserID() {
		return strHRUserID;
	}
	public void setStrHRUserID(String strHRUserID) {
		this.strHRUserID = strHRUserID;
	}
	public String getStrFilePath() {
		return strFilePath;
	}
	public void setStrFilePath(String strFilePath) {
		this.strFilePath = strFilePath;
	}
	public String getStrFileName() {
		return strFileName;
	}
	public void setStrFileName(String strFileName) {
		this.strFileName = strFileName;
	}
	public String getStrUploadMode() {
		return strUploadMode;
	}
	public void setStrUploadMode(String strUploadMode) {
		this.strUploadMode = strUploadMode;
	}
	public String getStrGroupID() {
		return strGroupID;
	}
	public void setStrGroupID(String strGroupID) {
		this.strGroupID = strGroupID;
	}
	public String getHrUploadDate() {
		return hrUploadDate;
	}
	public void setHrUploadDate(String hrUploadDate) {
		this.hrUploadDate = hrUploadDate;
	}
	
	public String getUploadedBy() {
		return uploadedBy;
	}
	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}
	public String getStrStatus() {
		return strStatus;
	}
	public void setStrStatus(String strStatus) {
		this.strStatus = strStatus;
	}
	public String getStatusChangedDate() {
		return statusChangedDate;
	}
	public void setStatusChangedDate(String statusChangedDate) {
		this.statusChangedDate = statusChangedDate;
	}
	public String getStatusChangedBy() {
		return statusChangedBy;
	}
	public void setStatusChangedBy(String statusChangedBy) {
		this.statusChangedBy = statusChangedBy;
	}
	public Integer getFileSequenceId() {
		return fileSequenceId;
	}
	public void setFileSequenceId(Integer fileSequenceId) {
		this.fileSequenceId = fileSequenceId;
	}
	public String getFileUploadedFor() {
		return fileUploadedFor;
	}
	public void setFileUploadedFor(String fileUploadedFor) {
		this.fileUploadedFor = fileUploadedFor;
	}
	
	
    
}//end of OnlineAccessPolicyVO
