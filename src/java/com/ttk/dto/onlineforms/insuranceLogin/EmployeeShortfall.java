package com.ttk.dto.onlineforms.insuranceLogin;

import com.ttk.dto.BaseVO;

public class EmployeeShortfall  extends BaseVO{

	private String dateOfShortfall;
	private String shortfallNo;
	private long shortfallSeqId;
	private String shortfallType;
	public long getShortfallSeqId() {
		return shortfallSeqId;
	}
	public void setShortfallSeqId(long shortfallSeqId) {
		this.shortfallSeqId = shortfallSeqId;
	}
	private String status;
	private String replyReceived;
	private String viewFile;
	private long preAuthSeqId;
	private String preAuthNo;
	private String claimNo;
	private long claimSeqId;
	public long getPreAuthSeqId() {
		return preAuthSeqId;
	}
	public void setPreAuthSeqId(long preAuthSeqId) {
		this.preAuthSeqId = preAuthSeqId;
	}
	public String getPreAuthNo() {
		return preAuthNo;
	}
	public void setPreAuthNo(String preAuthNo) {
		this.preAuthNo = preAuthNo;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public long getClaimSeqId() {
		return claimSeqId;
	}
	public void setClaimSeqId(long claimSeqId) {
		this.claimSeqId = claimSeqId;
	}
	public String getDateOfShortfall() {
		return dateOfShortfall;
	}
	public void setDateOfShortfall(String dateOfShortfall) {
		this.dateOfShortfall = dateOfShortfall;
	}
	public String getShortfallNo() {
		return shortfallNo;
	}
	public void setShortfallNo(String shortfallNo) {
		this.shortfallNo = shortfallNo;
	}
	public String getShortfallType() {
		return shortfallType;
	}
	public void setShortfallType(String shortfallType) {
		this.shortfallType = shortfallType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReplyReceived() {
		return replyReceived;
	}
	public void setReplyReceived(String replyReceived) {
		this.replyReceived = replyReceived;
	}
	public String getViewFile() {
		return viewFile;
	}
	public void setViewFile(String viewFile) {
		this.viewFile = viewFile;
	}
	
}
