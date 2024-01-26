package com.ttk.dto.common;

import java.io.Reader;

import com.ttk.dto.BaseVO;

public class DhpoWebServiceVO extends BaseVO{	
	
	/**
	 *
	 * 
	 */
	
	private static final long serialVersionUID = 3456863988176537498L;
	private String userID;
	private String password;
	private byte[] fileContent;
	private String xmlFileContent;
	private String fileName;
	private Integer transactionResult;
	private String errorMessage;
	private String fileType;
	private byte[] errorReport;
	private String fileID;
	private String downloadStatus;
	private String bifurcationYN;
	private Reader xmlFileReader;
	private String claimFrom;
	private Long fileSeqID;
	private String xmlRecievedData;
	private String userDownloadStatus;
	private String userDownloadDate;
	
	private String dhpoTxDate;
	private String dhpoClaimRecCount;
	private String shClaimRecCount;
	private String gnClaimRecCount;
	
	private String preAuthSeqID;
	private String preAuthUploadStatus;
	
	
	
	public void setPreAuthUploadStatus(String preAuthUploadStatus) {
		this.preAuthUploadStatus = preAuthUploadStatus;
	}
	public String getPreAuthUploadStatus() {
		return preAuthUploadStatus;
	}
	
	public void setPreAuthSeqID(String preAuthSeqID) {
		this.preAuthSeqID = preAuthSeqID;
	}
	public String getPreAuthSeqID() {
		return preAuthSeqID;
	}
	
	public void setDhpoTxDate(String dhpoTxDate) {
		this.dhpoTxDate = dhpoTxDate;
	}
	public String getDhpoTxDate() {
		return dhpoTxDate;
	}
	public String getDhpoClaimRecCount() {
		return dhpoClaimRecCount;
	}
	public void setDhpoClaimRecCount(String dhpoClaimRecCount) {
		this.dhpoClaimRecCount = dhpoClaimRecCount;
	}
	public String getShClaimRecCount() {
		return shClaimRecCount;
	}
	public void setShClaimRecCount(String shClaimRecCount) {
		this.shClaimRecCount = shClaimRecCount;
	}
	public String getGnClaimRecCount() {
		return gnClaimRecCount;
	}
	public void setGnClaimRecCount(String gnClaimRecCount) {
		this.gnClaimRecCount = gnClaimRecCount;
	}
	public String getXmlRecievedData() {
		return xmlRecievedData;
	}
	public void setXmlRecievedData(String xmlRecievedData) {
		this.xmlRecievedData = xmlRecievedData;
	}
	public String getUserDownloadStatus() {
		return userDownloadStatus;
	}
	public void setUserDownloadStatus(String userDownloadStatus) {
		this.userDownloadStatus = userDownloadStatus;
	}
	public String getUserDownloadDate() {
		return userDownloadDate;
	}
	public void setUserDownloadDate(String userDownloadDate) {
		this.userDownloadDate = userDownloadDate;
	}
	public void setFileSeqID(Long fileSeqID) {
		this.fileSeqID = fileSeqID;
	}
	public Long getFileSeqID() {
		return fileSeqID;
	}
	public void setClaimFrom(String claimFrom) {
		this.claimFrom = claimFrom;
	}
	public String getClaimFrom() {
		return claimFrom;
	}
	
	public void setXmlFileReader(Reader xmlFileReader) {
		this.xmlFileReader = xmlFileReader;
	}
	public Reader getXmlFileReader() {
		return xmlFileReader;
	}
	public void setBifurcationYN(String bifurcationYN) {
		this.bifurcationYN = bifurcationYN;
	}
	public String getBifurcationYN() {
		return bifurcationYN;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public byte[] getFileContent() {
		return fileContent;
	}
	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Integer getTransactionResult() {
		return transactionResult;
	}
	public void setTransactionResult(Integer transactionResult) {
		this.transactionResult = transactionResult;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public byte[] getErrorReport() {
		return errorReport;
	}
	public void setErrorReport(byte[] errorReport) {
		this.errorReport = errorReport;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFileID() {
		return fileID;
	}
	public void setFileID(String fileID) {
		this.fileID = fileID;
	}
	public String getDownloadStatus() {
		return downloadStatus;
	}
	public void setDownloadStatus(String downloadStatus) {
		this.downloadStatus = downloadStatus;
	}
	public String getXmlFileContent() {
		return xmlFileContent;
	}
	public void setXmlFileContent(String xmlFileContent) {
		this.xmlFileContent = xmlFileContent;
	}
	
}
