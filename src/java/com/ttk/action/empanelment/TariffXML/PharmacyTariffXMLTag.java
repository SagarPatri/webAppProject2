package com.ttk.action.empanelment.TariffXML;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAttribute;


public class PharmacyTariffXMLTag {
	 
	String activitycode;
	String internalcode;
	String servicename;
	String internalDesc;
	String activitydesc;
	//BigDecimal discamt;
	BigDecimal pkgPrince;
	BigDecimal pkgSize;
	BigDecimal discpercent;
	String fromdate;
	String expirydate;
	BigDecimal grossamt;


	
	

	public PharmacyTariffXMLTag() {
		// TODO Auto-generated constructor stub
	}

	public PharmacyTariffXMLTag(String activitycode, String internalcode,
			String servicename, String internalDesc, String activitydesc, BigDecimal grossamt,
			BigDecimal pkgPrince,BigDecimal pkgSize, BigDecimal discpercent, String fromdate,
			String expirydate) {
		this.activitycode = activitycode;
		this.internalcode = internalcode;
		this.servicename = servicename;
		this.internalDesc = internalDesc;
		this.activitydesc = activitydesc;
		this.grossamt = grossamt;
		this.pkgPrince = pkgPrince;
		this.pkgSize = pkgSize;
		this.discpercent = discpercent;
		this.fromdate = fromdate;
		this.expirydate = expirydate;
	}

	public String getActivitycode() {
		return activitycode;
	}

	public String getInternalcode() {
		return internalcode;
	}

	public String getServicename() {
		return servicename;
	}

	public String getInternalDesc() {
		return internalDesc;
	}
	
	public String getActivitydesc() {
		return activitydesc;
	}
	
	public BigDecimal getGrossamt() {
		return grossamt;
	}

	public BigDecimal getPkgPrince() {
		return pkgPrince;
	}

	public BigDecimal getDiscpercent() {
		return discpercent;
	}

	public String getFromdate() {
		return fromdate;
	}

	public String getExpirydate() {
		return expirydate;
	}

	@XmlAttribute
	public void setActivitycode(String activitycode) {
		this.activitycode = activitycode;
	}

	@XmlAttribute
	public void setInternalcode(String internalcode) {
		this.internalcode = internalcode;
	}

	@XmlAttribute
	public void setServicename(String servicename) {
		this.servicename = servicename;
	}

	@XmlAttribute
	public void setInternalDesc(String internalDesc) {
		this.internalDesc = internalDesc;
	}
	
	@XmlAttribute
	public void setActivitydesc(String activitydesc) {
		this.activitydesc = activitydesc;
	}

	@XmlAttribute
	public void setGrossamt(BigDecimal grossamt) {
		this.grossamt = grossamt;
	}

	@XmlAttribute
	public void setPkgPrince(BigDecimal pkgPrince) {
		this.pkgPrince = pkgPrince;
	}

	@XmlAttribute
	public void setDiscpercent(BigDecimal discpercent) {
		this.discpercent = discpercent;
	}

	@XmlAttribute
	public void setFromdate(String fromdate) {
		this.fromdate = fromdate;
	}

	@XmlAttribute
	public void setExpirydate(String expirydate) {
		this.expirydate = expirydate;
	}

	public BigDecimal getPkgSize() {
		return pkgSize;
	}

	@XmlAttribute
	public void setPkgSize(BigDecimal pkgSize) {
		this.pkgSize = pkgSize;
	}

	@Override
	public int hashCode() {
		String result = this.activitycode +
						this.internalcode+
						this.servicename+
						this.internalDesc+
						this.activitydesc+
						this.grossamt+
						this.pkgPrince+
						this.pkgSize+
						this.discpercent+
						this.fromdate+
						this.expirydate;
		if("".equals(result.trim()))
		return 0;
		else		
		return result.trim().hashCode();
	}
}
