package com.ttk.action.claims.claimUploadXml;
import javax.xml.bind.annotation.XmlAttribute;

public class ClaimsReSubmissionData {

	String slno;
	String memName;
	String memId;
	String parentClaimNumber;
	String parentClaimSettelmentNumber;
	String servDt;
	String actType;
	String internalCode;
	String servDesc;
	String cptCode;
	String resubmissionRequestedAmount;
	String qty;
	String toothNo;
	String alKootRemarks;
	String resubmissionJustification;

	
	public String getSlno() {
		return slno;
	}
	@XmlAttribute
	public void setSlno(String slno) {
		this.slno = slno;
	}

	public String getMemName() {
		return memName;
	}
	@XmlAttribute
	public void setMemName(String memName) {
		this.memName = memName;
	}
	
	public String getMemId() {
		return memId;
	}
	@XmlAttribute
	public void setMemId(String memId) {
		this.memId = memId;
	}
	
	public String getParentClaimNumber() {
		return parentClaimNumber;
	}
	@XmlAttribute
	public void setParentClaimNumber(String parentClaimNumber) {
		this.parentClaimNumber = parentClaimNumber;
	}

	public String getParentClaimSettelmentNumber() {
		return parentClaimSettelmentNumber;
	}
	@XmlAttribute
	public void setParentClaimSettelmentNumber(String parentClaimSettelmentNumber) {
		this.parentClaimSettelmentNumber = parentClaimSettelmentNumber;
	}
	
	public String getServDt() {
		return servDt;
	}
	@XmlAttribute
	public void setServDt(String servDt) {
		this.servDt = servDt;
	}
	
	public String getActType() {
		return actType;
	}
	@XmlAttribute
	public void setActType(String actType) {
		this.actType = actType;
	}
	
	public String getInternalCode() {
		return internalCode;
	}
	@XmlAttribute
	public void setInternalCode(String internalCode) {
		this.internalCode = internalCode;
	}
	
	public String getServDesc() {
		return servDesc;
	}
	@XmlAttribute
	public void setServDesc(String servDesc) {
		this.servDesc = servDesc;
	}
	
	public String getCptCode() {
		return cptCode;
	}
	@XmlAttribute
	public void setCptCode(String cptCode) {
		this.cptCode = cptCode;
	}
	
	public String getResubmissionRequestedAmount() {
		return resubmissionRequestedAmount;
	}
	@XmlAttribute
	public void setResubmissionRequestedAmount(String resubmissionRequestedAmount) {
		this.resubmissionRequestedAmount = resubmissionRequestedAmount;
	}

	public String getQty() {
		return qty;
	}
	@XmlAttribute
	public void setQty(String qty) {
		this.qty = qty;
	}
	
	public String getToothNo() {
		return toothNo;
	}
	@XmlAttribute
	public void setToothNo(String toothNo) {
		this.toothNo = toothNo;
	}
	
	public String getAlKootRemarks() {
		return alKootRemarks;
	}
	@XmlAttribute
	public void setAlKootRemarks(String alKootRemarks) {
		this.alKootRemarks = alKootRemarks;
	}

	public String getResubmissionJustification() {
		return resubmissionJustification;
	}
	@XmlAttribute
	public void setResubmissionJustification(String resubmissionJustification) {
		this.resubmissionJustification = resubmissionJustification;
	}

	@Override
	public int hashCode() {
		String s = this.slno 
		+this.memName
		+this. memId
		+this.parentClaimNumber
		+this.parentClaimSettelmentNumber
		+this. servDt
		+this. actType
		+this. internalCode
		+this. servDesc
		+this. cptCode
		+this.resubmissionRequestedAmount
		+this. qty
		+this. toothNo
		+this.alKootRemarks
		+this.resubmissionJustification;
		if(!(s.trim().equals("")))
		return s.hashCode();
		else
		return 0;
	}
	
}

