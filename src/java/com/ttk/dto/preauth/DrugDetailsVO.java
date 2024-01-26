package com.ttk.dto.preauth;

import java.math.BigDecimal;

import com.ttk.dto.BaseVO;

public class DrugDetailsVO extends BaseVO{

	private static final long serialVersionUID = 1L;

	

	//Drug Details

	private String drugCode;
	private String drugDesc;
	private String posology;
	private String days;
	private String drugunit;
	private Float drugqty;
	private BigDecimal grossAmount;
	private BigDecimal discount;
	private BigDecimal disPrec;
	private BigDecimal netPriceAftDisc;
	private BigDecimal claimedQty;
	private BigDecimal ttlAmnt;
	
	private BigDecimal unitPrice;
	private BigDecimal packagePrice;
	private BigDecimal granularUnit;
	private Long drugSeqId;
	private BigDecimal requestedAmount;
	private BigDecimal approvedAmount;
	private BigDecimal pateintShare;
	private String denial;
	private String remarks;
	private String status;
	private String searchType;
	private BigDecimal noOfUnits;
	private String enhancedYN="N";
	public String getEnhancedYN() {
		return enhancedYN;
	}
	public void setEnhancedYN(String enhancedYN) {
		this.enhancedYN = enhancedYN;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	public BigDecimal getPackagePrice() {
		return packagePrice;
	}
	public void setPackagePrice(BigDecimal packagePrice) {
		this.packagePrice = packagePrice;
	}
	public BigDecimal getGranularUnit() {
		return granularUnit;
	}
	public void setGranularUnit(BigDecimal granularUnit) {
		this.granularUnit = granularUnit;
	}
	public BigDecimal getGrossAmount() {
		return grossAmount;
	}
	public void setGrossAmount(BigDecimal grossAmount) {
		this.grossAmount = grossAmount;
	}
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	public BigDecimal getNetPriceAftDisc() {
		return netPriceAftDisc;
	}
	public void setNetPriceAftDisc(BigDecimal netPriceAftDisc) {
		this.netPriceAftDisc = netPriceAftDisc;
	}
	public BigDecimal getClaimedQty() {
		return claimedQty;
	}
	public void setClaimedQty(BigDecimal claimedQty) {
		this.claimedQty = claimedQty;
	}
	public BigDecimal getTtlAmnt() {
		return ttlAmnt;
	}
	public void setTtlAmnt(BigDecimal ttlAmnt) {
		this.ttlAmnt = ttlAmnt;
	}
	public String getDrugCode() {
		return drugCode;
	}
	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}
	public String getDrugDesc() {
		return drugDesc;
	}
	public void setDrugDesc(String drugDesc) {
		this.drugDesc = drugDesc;
	}
	public String getPosology() {
		return posology;
	}
	public void setPosology(String posology) {
		this.posology = posology;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getDrugunit() {
		return drugunit;
	}
	public void setDrugunit(String drugunit) {
		this.drugunit = drugunit;
	}
	public Float getDrugqty() {
		return drugqty;
	}
	public void setDrugqty(Float drugqty) {
		this.drugqty = drugqty;
	}
	/**
	 * @return the disPrec
	 */
	public BigDecimal getDisPrec() {
		return disPrec;
	}
	/**
	 * @param disPrec the disPrec to set
	 */
	public void setDisPrec(BigDecimal disPrec) {
		this.disPrec = disPrec;
	}
	public Long getDrugSeqId() {
		return drugSeqId;
	}
	public void setDrugSeqId(Long drugSeqId) {
		this.drugSeqId = drugSeqId;
	}
	public BigDecimal getRequestedAmount() {
		return requestedAmount;
	}
	public void setRequestedAmount(BigDecimal requestedAmount) {
		this.requestedAmount = requestedAmount;
	}
	public BigDecimal getApprovedAmount() {
		return approvedAmount;
	}
	public void setApprovedAmount(BigDecimal approvedAmount) {
		this.approvedAmount = approvedAmount;
	}
	public BigDecimal getPateintShare() {
		return pateintShare;
	}
	public void setPateintShare(BigDecimal pateintShare) {
		this.pateintShare = pateintShare;
	}
	public String getDenial() {
		return denial;
	}
	public void setDenial(String denial) {
		this.denial = denial;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public BigDecimal getNoOfUnits() {
		return noOfUnits;
	}
	public void setNoOfUnits(BigDecimal noOfUnits) {
		this.noOfUnits = noOfUnits;
	}
}

