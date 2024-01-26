package com.ttk.action.claims.claimUploadXml;
import javax.xml.bind.annotation.XmlAttribute;

public class ClaimsData {

	String slno;
	String invNo;
	String memName;
	String memId;
	String preAprNo;
	String dtOfTrtmt;
	String dtDisch;
	String sysOfMed;
	String benType;
	String encType;
	String clinicianId;
	String clinicianName;
	String symptoms;
	String princiICdCode;
	String princiICDesc;
	String secIcd1;
	String secIcd2;
	String secIcd3;
	String secIcd4;
	String secIcd5;
	String firstIncDt;
	String firstRepDt;
	String servDt;
	String actType;
	String internalCode;
	String servDesc;
	String cptCode;
	String amntClmnd;
	String qty;
	String toothNo;
	String dtOfLMP;
	String natOfConep;
	String observtn;
	String evntRefNo;
	
	
	String dateOfPrescription;
	String drugdescription;
	String mophCode;
	String unitType;
	String durationOfMedicine;
	
	
	public String getUnitType() {
		return unitType;
	}
     
	@XmlAttribute
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public String getDurationOfMedicine() {
		return durationOfMedicine;
	}

	@XmlAttribute
	public void setDurationOfMedicine(String durationOfMedicine) {
		this.durationOfMedicine = durationOfMedicine;
	}

	public String getMophCode() {
		return mophCode;
	}
	
	@XmlAttribute
	public void setMophCode(String mophCode) {
		this.mophCode = mophCode;
	}
	public String getDrugdescription() {
		return drugdescription;
	}
	@XmlAttribute
	public void setDrugdescription(String drugdescription) {
		this.drugdescription = drugdescription;
	}
	public String getDateOfPrescription() {
		return dateOfPrescription;
	}
	@XmlAttribute
	public void setDateOfPrescription(String dateOfPrescription) {
		this.dateOfPrescription = dateOfPrescription;
	}
	public String getSlno() {
		return slno;
	}
	@XmlAttribute
	public void setSlno(String slno) {
		this.slno = slno;
	}
	public String getInvNo() {
		return invNo;
	}
	@XmlAttribute
	public void setInvNo(String invNo) {
		this.invNo = invNo;
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
	public String getPreAprNo() {
		return preAprNo;
	}
	@XmlAttribute
	public void setPreAprNo(String preAprNo) {
		this.preAprNo = preAprNo;
	}
	public String getDtOfTrtmt() {
		return dtOfTrtmt;
	}
	@XmlAttribute
	public void setDtOfTrtmt(String dtOfTrtmt) {
		this.dtOfTrtmt = dtOfTrtmt;
	}
	public String getDtDisch() {
		return dtDisch;
	}
	@XmlAttribute
	public void setDtDisch(String dtDisch) {
		this.dtDisch = dtDisch;
	}
	public String getSysOfMed() {
		return sysOfMed;
	}
	@XmlAttribute
	public void setSysOfMed(String sysOfMed) {
		this.sysOfMed = sysOfMed;
	}
	public String getBenType() {
		return benType;
	}
	@XmlAttribute
	public void setBenType(String benType) {
		this.benType = benType;
	}
	public String getEncType() {
		return encType;
	}
	@XmlAttribute
	public void setEncType(String encType) {
		this.encType = encType;
	}
	public String getClinicianId() {
		return clinicianId;
	}
	@XmlAttribute
	public void setClinicianId(String clinicianId) {
		this.clinicianId = clinicianId;
	}
	public String getClinicianName() {
		return clinicianName;
	}
	@XmlAttribute
	public void setClinicianName(String clinicianName) {
		this.clinicianName = clinicianName;
	}
	public String getSymptoms() {
		return symptoms;
	}
	@XmlAttribute
	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}
	public String getPrinciICdCode() {
		return princiICdCode;
	}
	@XmlAttribute
	public void setPrinciICdCode(String princiICdCode) {
		this.princiICdCode = princiICdCode;
	}
	public String getPrinciICDesc() {
		return princiICDesc;
	}
	@XmlAttribute
	public void setPrinciICDesc(String princiICDesc) {
		this.princiICDesc = princiICDesc;
	}
	public String getSecIcd1() {
		return secIcd1;
	}
	@XmlAttribute
	public void setSecIcd1(String secIcd1) {
		this.secIcd1 = secIcd1;
	}
	public String getSecIcd2() {
		return secIcd2;
	}
	@XmlAttribute
	public void setSecIcd2(String secIcd2) {
		this.secIcd2 = secIcd2;
	}
	public String getSecIcd3() {
		return secIcd3;
	}
	@XmlAttribute
	public void setSecIcd3(String secIcd3) {
		this.secIcd3 = secIcd3;
	}
	public String getSecIcd4() {
		return secIcd4;
	}
	@XmlAttribute
	public void setSecIcd4(String secIcd4) {
		this.secIcd4 = secIcd4;
	}
	public String getSecIcd5() {
		return secIcd5;
	}
	@XmlAttribute
	public void setSecIcd5(String secIcd5) {
		this.secIcd5 = secIcd5;
	}
	public String getFirstIncDt() {
		return firstIncDt;
	}
	@XmlAttribute
	public void setFirstIncDt(String firstIncDt) {
		this.firstIncDt = firstIncDt;
	}
	public String getFirstRepDt() {
		return firstRepDt;
	}
	@XmlAttribute
	public void setFirstRepDt(String firstRepDt) {
		this.firstRepDt = firstRepDt;
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
	public String getAmntClmnd() {
		return amntClmnd;
	}
	@XmlAttribute
	public void setAmntClmnd(String amntClmnd) {
		this.amntClmnd = amntClmnd;
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
	public String getDtOfLMP() {
		return dtOfLMP;
	}
	@XmlAttribute
	public void setDtOfLMP(String dtOfLMP) {
		this.dtOfLMP = dtOfLMP;
	}
	public String getNatOfConep() {
		return natOfConep;
	}
	@XmlAttribute
	public void setNatOfConep(String natOfConep) {
		this.natOfConep = natOfConep;
	}
	public String getObservtn() {
		return observtn;
	}
	@XmlAttribute
	public void setObservtn(String observtn) {
		this.observtn = observtn;
	}
	public String getEvntRefNo() {
		return evntRefNo;
	}
	@XmlAttribute
	public void setEvntRefNo(String evntRefNo) {
		this.evntRefNo = evntRefNo;
	}
	
	@Override
	public int hashCode() {
		String s = this.slno 
		+this.invNo
		+this.memName
		+this. memId
		+this. preAprNo
		+this. dtOfTrtmt
		+this. dtDisch
		+this. sysOfMed
		+this. benType
		+this. encType
		+this. clinicianId
		+this. clinicianName
		+this. symptoms
		+this. princiICdCode
		+this. princiICDesc
		+this. secIcd1
		+this. secIcd2
		+this. secIcd3
		+this. secIcd4
		+this. secIcd5
		+this. firstIncDt
		+this. firstRepDt
		+this. servDt
		+this. actType
		+this. internalCode
		+this. servDesc
		+this. cptCode
		+this. amntClmnd
		+this. qty
		+this. toothNo
		+this. dtOfLMP
		+this. natOfConep
		+this. observtn
		+this. evntRefNo
		+this.dateOfPrescription
		+this.drugdescription;
		if(!(s.trim().equals("")))
		return s.hashCode();
		else
		return 0;
	}
	
}

