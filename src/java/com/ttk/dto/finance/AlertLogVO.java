package com.ttk.dto.finance;

import java.util.LinkedHashMap;

public class AlertLogVO {
	private String logDate;
	private String logType;
	private String modifiedReason;
	private String remark;
	private String userName;
	private String remarksFor;
	private String remarksModified;
	private String remarksColumn;
	private String remarksLabel;
	private String strImageName="UserIcon";
    private String strImageTitle="User Log";
	public String getImageName() {
		return strImageName;
	}
	public void setImageName(String strImageName) {
		this.strImageName = strImageName;
	}
	public String getImageTitle() {
		return strImageTitle;
	}
	public void setImageTitle(String strImageTitle) {
		this.strImageTitle = strImageTitle;
	}
	private LinkedHashMap<String,Object> remarks;
	public String getRemarksFor() {
		return remarksFor;
	}
	public void setRemarksFor(String remarksFor) {
		this.remarksFor = remarksFor;
	}
	public String getRemarksModified() {
		return remarksModified;
	}
	public void setRemarksModified(String remarksModified) {
		this.remarksModified = remarksModified;
	}
	public String getRemarksColumn() {
		return remarksColumn;
	}
	public void setRemarksColumn(String remarksColumn) {
		this.remarksColumn = remarksColumn;
	}
	public String getRemarksLabel() {
		return remarksLabel;
	}
	public void setRemarksLabel(String remarksLabel) {
		this.remarksLabel = remarksLabel;
	}
	public String getLogDate() {
		return logDate;
	}
	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}
	public String getLogType() {
		return logType;
	}
	public void setLogType(String logType) {
		this.logType = logType;
	}
	public String getModifiedReason() {
		return modifiedReason;
	}
	public void setModifiedReason(String modifiedReason) {
		this.modifiedReason = modifiedReason;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUserName() {
		return userName;
	}
	public LinkedHashMap<String, Object> getRemarks() {
		return remarks;
	}
	public void setRemarks(LinkedHashMap<String, Object> remarks) {
		this.remarks = remarks;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
