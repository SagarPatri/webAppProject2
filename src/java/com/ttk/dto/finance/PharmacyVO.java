package com.ttk.dto.finance;

import java.math.BigDecimal;
import java.util.Date;

import com.ttk.dto.BaseVO;

public class PharmacyVO extends BaseVO{

	private static final long serialVersionUID = 1L;
	
	private Long pharmacySeqId=null;;
	private String tradeName;
	private String shortDesc;
	private String scientificName;
	private String activityCode;
	private String activityDesc;
	private Date startDate;
	private Date endDate;
	private Date addedDate;
	private String scientificCode;
	private BigDecimal unitPrice;
	private BigDecimal packagePrice;
	private BigDecimal granularUnit;
	private String gender;
	private String exclusionYN;
	private String qatarExclusionYN;
	private String recFlag;
	private String status;
	private String routeOfAdmin;
	private String registeredOwner;
	private String ingStrength;
	private String doasage;
	private String ddcId;
	private String qatarCode;
	private String isReviewedByVidal;
	private String isReviewedByVidalCheckBox;
	public String getTradeName() {
		return tradeName;
	}
	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}
	public String getShortDesc() {
		return shortDesc;
	}
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}
	public String getScientificName() {
		return scientificName;
	}
	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}
	public String getActivityDesc() {
		return activityDesc;
	}
	public void setActivityDesc(String activityDesc) {
		this.activityDesc = activityDesc;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getAddedDate() {
		return addedDate;
	}
	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}
	public String getScientificCode() {
		return scientificCode;
	}
	public void setScientificCode(String scientificCode) {
		this.scientificCode = scientificCode;
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
	public String getExclusionYN() {
		return exclusionYN;
	}
	public void setExclusionYN(String exclusionYN) {
		this.exclusionYN = exclusionYN;
	}
	public String getQatarExclusionYN() {
		return qatarExclusionYN;
	}
	public void setQatarExclusionYN(String qatarExclusionYN) {
		this.qatarExclusionYN = qatarExclusionYN;
	}
	public String getRecFlag() {
		return recFlag;
	}
	public void setRecFlag(String recFlag) {
		this.recFlag = recFlag;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRouteOfAdmin() {
		return routeOfAdmin;
	}
	public void setRouteOfAdmin(String routeOfAdmin) {
		this.routeOfAdmin = routeOfAdmin;
	}
	public String getRegisteredOwner() {
		return registeredOwner;
	}
	public void setRegisteredOwner(String registeredOwner) {
		this.registeredOwner = registeredOwner;
	}
	public String getIngStrength() {
		return ingStrength;
	}
	public void setIngStrength(String ingStrength) {
		this.ingStrength = ingStrength;
	}
	public String getDoasage() {
		return doasage;
	}
	public void setDoasage(String doasage) {
		this.doasage = doasage;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Long getPharmacySeqId() {
		return pharmacySeqId;
	}
	public void setPharmacySeqId(Long pharmacySeqId) {
		this.pharmacySeqId = pharmacySeqId;
	}
	public String getDdcId() {
		return ddcId;
	}
	public void setDdcId(String ddcId) {
		this.ddcId = ddcId;
	}
	/**
	 * @return the qatarCode
	 */
	public String getQatarCode() {
		return qatarCode;
	}
	/**
	 * @param qatarCode the qatarCode to set
	 */
	public void setQatarCode(String qatarCode) {
		this.qatarCode = qatarCode;
	}
	/**
	 * @return the activityCode
	 */
	public String getActivityCode() {
		return activityCode;
	}
	/**
	 * @param activityCode the activityCode to set
	 */
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	/**
	 * @return the isReviewedByVidal
	 */
	public String getIsReviewedByVidal() {
		return isReviewedByVidal;
	}
	/**
	 * @param isReviewedByVidal the isReviewedByVidal to set
	 */
	public void setIsReviewedByVidal(String isReviewedByVidal) {
		this.isReviewedByVidal = isReviewedByVidal;
	}
	/**
	 * @return the isReviewedByVidalCheckBox
	 */
	public String getIsReviewedByVidalCheckBox() {
		return isReviewedByVidalCheckBox;
	}
	/**
	 * @param isReviewedByVidalCheckBox the isReviewedByVidalCheckBox to set
	 */
	public void setIsReviewedByVidalCheckBox(String isReviewedByVidalCheckBox) {
		this.isReviewedByVidalCheckBox = isReviewedByVidalCheckBox;
	}
	

}
