package com.ttk.dto.empanelment;

/**
 * @ (#) ClaimBatchManager.java July 7, 2015
 * Project 	     : ProjectX
 * File          : ClaimBatchManager.java
 * Author        : Nagababu K
 * Company       : RCS Technologies
 * Date Created  : July 7, 2015
 *
 * @author       :  Nagababu K
 * Modified by   :  
 * Modified date :  
 * Reason        :  
 */



import java.math.BigDecimal;
import java.util.Date;

import oracle.sql.CLOB;

import com.ttk.dto.BaseVO;




public class ActivityDetailsVO extends BaseVO{

private static final long serialVersionUID = 1L;
private String claimtype;
private Integer  serialNo;
private String activityCodeDesc="";
private String activityCode="";
private BigDecimal copay;
private String modifier="";
private String searchType="";
private BigDecimal coinsurance;
private String unitType="";
private BigDecimal deductible;
private Float quantity;
private Float drugCodeQuantity;
private Float approvedQuantity;
private BigDecimal outOfPocket;
private String startDate="";
private BigDecimal patientShare;
private BigDecimal grossAmount;
private BigDecimal netAmount;
private BigDecimal discount;
private BigDecimal discountPerUnit;
private BigDecimal approvedAmount;
private BigDecimal allowedAmount;
private BigDecimal discountedGross;
private BigDecimal unitPrice;
private BigDecimal disAllowedAmount;
private BigDecimal providerRequestedAmt;

private BigDecimal rdisAllowedAmount;//r for rules
private BigDecimal routOfPocket;
private BigDecimal rdeductible;
private BigDecimal rcoinsurance;
private BigDecimal rcopay;
private String denialRemarks="";
private String activityRemarks="";
private String bundleId="";
private String internalCode="";
private String internalDesc="";
private String denialCode="";
private String denialDescription="";
private String packageId="";
private Long activityCodeSeqId;
private String activityTypeId;
private String preAuthNo;
private String claimNo;
private Long preAuthSeqID;
private Long claimSeqID;
private Date activityStartDate;
private String clinicianId;
private Long activityDtlSeqId;
private Long activitySeqId;
private String amountAllowed="";
private String activityTariffStatus="";
private String tariffYN="";
private String displayMsg="";
private String remarks="";
private String authType="";
private String overrideYN="";
private String networkProviderType;
private String clinicianName;
private String overrideRemarks;
private String activityStatus;
private Integer medicationDays;
private String posology;
private BigDecimal ricopar;
private BigDecimal ucr;
private Integer quantityInt;

private String activityServiceType;
private String activityServiceCode;
private String activityServiceCodeDesc;
private String serviceInternalCode;
private String benefitType;
private String benefitTypeActivity;
private BigDecimal nonNetworkCopay;


private String flowType = "";


private String unitPriceCurrencyType;
private BigDecimal convertedUnitPrice;
private String convertedUnitPricecurrencyType;
private BigDecimal conversionRate;

private String mophCodes="";

public Integer getQuantityInt() {
	return quantityInt;
}
public void setQuantityInt(Integer quantityInt) {
	this.quantityInt = quantityInt;
}
//private String totalAmountCurrency;
private Integer duration;
private String dateOfApproval;
public String getDateOfApproval() {
	return dateOfApproval;
}
public void setDateOfApproval(String dateOfApproval) {
	this.dateOfApproval = dateOfApproval;
}
private String miRef;
private String erxRef;
private String erxInstruction;
private Long tariffSeqId;

//Dental Benefit
private String toothNo;
private String toothNoReqYN;
private String statusMessage;
private String activityType;
private String newPharmacyYN;

private String provCopayFlag;
private String copayDeductFlag;
private String copayPerc;
private String memServiceCode;
private String memServiceDesc;

private String areaOfCoverCopay;
private String areaOfCoverDeduct;
private String areaOfCoverFlag;
public String getStatusMessage() {
	return statusMessage;
}
public void setStatusMessage(String statusMessage) {
	this.statusMessage = statusMessage;
}
public String getMiRef() {
	return miRef;
}
/*public void setMiRef(String miRef) {
	this.miRef = miRef;
}*/
public String getErxRef() {
	return erxRef;
}
public void setErxRef(String erxRef) {
	this.erxRef = erxRef;
}
public String getErxInstruction() {
	return erxInstruction;
}
public void setErxInstruction(String erxInstruction) {
	this.erxInstruction = erxInstruction;
}
public ActivityDetailsVO() {
	// TODO Auto-generated constructor stub
}
public ActivityDetailsVO(Integer serialNo,String activityCode,String activityCodeDesc,String modifier,String unitType,String startDate,String activityRemarks,String denialCode,String denialDescription,
		Float quantity,Float approvedQuantity,Long activitySeqId,Long activityDtlSeqId,
		                 BigDecimal grossAmount,BigDecimal discount,BigDecimal discountedGross,BigDecimal patientShare,BigDecimal netAmount,BigDecimal approvedAmount,BigDecimal disAllowedAmt,String preAuthNo) {
	this.serialNo=serialNo;
	this.activityCode=activityCode;
	this.modifier=modifier;
	this.unitType=unitType;
	this.quantity=quantity;
	this.startDate=startDate;
	this.grossAmount=grossAmount;
	this.discountedGross=discountedGross;
	this.patientShare=patientShare;
	this.netAmount=netAmount;
	this.approvedAmount=approvedAmount;
	this.disAllowedAmount=disAllowedAmt;
	this.denialDescription=denialDescription;
	this.activityRemarks=activityRemarks;
	this.activitySeqId=activitySeqId;
	this.activityDtlSeqId=activityDtlSeqId;
	this.activityCodeDesc=activityCodeDesc;
	this.denialCode=denialCode;
	this.discount=discount;
	this.approvedQuantity=approvedQuantity;
	this.preAuthNo	=	preAuthNo;
	
}

public void setDuration(Integer duration) {
	this.duration = duration;
}
public Integer getDuration() {
	return duration;
}
public void setBenefitType(String benefitType) {
	this.benefitType = benefitType;
}
public String getBenefitType() {
	return benefitType;
}
public void setActivityServiceCode(String activityServiceCode) {
	this.activityServiceCode = activityServiceCode;
}
public String getActivityServiceCode() {
	return activityServiceCode;
}
public void setActivityServiceType(String activityServiceType) {
	this.activityServiceType = activityServiceType;
}
public String getActivityServiceType() {
	return activityServiceType;
}
public Integer getSerialNo() {
	return serialNo;
}
public void setSerialNo(Integer serialNo) {
	this.serialNo = serialNo;
}
public String getActivityCode() {
	return activityCode;
}
public void setActivityCode(String activityCode) {
	this.activityCode = activityCode;
}
public BigDecimal getCopay() {
	return copay;
}
public void setCopay(BigDecimal copay) {
	this.copay = copay;
}
public String getModifier() {
	return modifier;
}
public void setModifier(String modifier) {
	this.modifier = modifier;
}
public BigDecimal getCoinsurance() {
	return coinsurance;
}
public void setCoinsurance(BigDecimal coinsurance) {
	this.coinsurance = coinsurance;
}
public String getUnitType() {
	return unitType;
}
public void setUnitType(String unitType) {
	this.unitType = unitType;
}
public BigDecimal getDeductible() {
	return deductible;
}
public void setDeductible(BigDecimal deductible) {
	this.deductible = deductible;
}

public BigDecimal getOutOfPocket() {
	return outOfPocket;
}
public void setOutOfPocket(BigDecimal outOfPocket) {
	this.outOfPocket = outOfPocket;
}
public String getStartDate() {
	return startDate;
}
public void setStartDate(String startDate) {
	this.startDate = startDate;
}
public BigDecimal getPatientShare() {
	return patientShare;
}
public void setPatientShare(BigDecimal patientShare) {
	this.patientShare = patientShare;
}
public BigDecimal getGrossAmount() {
	return grossAmount;
}
public void setGrossAmount(BigDecimal grossAmount) {
	this.grossAmount = grossAmount;
}
public BigDecimal getNetAmount() {
	return netAmount;
}
public void setNetAmount(BigDecimal netAmount) {
	this.netAmount = netAmount;
}
public BigDecimal getDiscount() {
	return discount;
}
public void setDiscount(BigDecimal discount) {
	this.discount = discount;
}
public BigDecimal getApprovedAmount() {
	return approvedAmount;
}
public void setApprovedAmount(BigDecimal approvedAmount) {
	this.approvedAmount = approvedAmount;
}
public BigDecimal getDiscountedGross() {
	return discountedGross;
}
public void setDiscountedGross(BigDecimal discountedGross) {
	this.discountedGross = discountedGross;
}

public String getBundleId() {
	return bundleId;
}
public void setBundleId(String bundleId) {
	this.bundleId = bundleId;
}
public String getInternalCode() {
	return internalCode;
}
public void setInternalCode(String internalCode) {
	this.internalCode = internalCode;
}

public String getPackageId() {
	return packageId;
}
public void setPackageId(String packageId) {
	this.packageId = packageId;
}
public Long getActivityCodeSeqId() {
	return activityCodeSeqId;
}
public void setActivityCodeSeqId(Long activityCodeSeqId) {
	this.activityCodeSeqId = activityCodeSeqId;
}
public String getActivityTypeId() {
	return activityTypeId;
}
public void setActivityTypeId(String activityTypeId) {
	this.activityTypeId = activityTypeId;
}
public String getPreAuthNo() {
	return preAuthNo;
}
public void setPreAuthNo(String preAuthNo) {
	this.preAuthNo = preAuthNo;
}
public Long getPreAuthSeqID() {
	return preAuthSeqID;
}
public void setPreAuthSeqID(Long preAuthSeqID) {
	this.preAuthSeqID = preAuthSeqID;
}
public Date getActivityStartDate() {
	return activityStartDate;
}
public void setActivityStartDate(Date activityStartDate) {
	this.activityStartDate = activityStartDate;
}
public String getClinicianId() {
	return clinicianId;
}
public void setClinicianId(String clinicianId) {
	this.clinicianId = clinicianId;
}
public String getActivityRemarks() {
	return activityRemarks;
}
public void setActivityRemarks(String activityRemarks) {
	this.activityRemarks = activityRemarks;
}
public String getActivityCodeDesc() {
	return activityCodeDesc;
}
public void setActivityCodeDesc(String activityCodeDesc) {
	this.activityCodeDesc = activityCodeDesc;
}
public Long getActivityDtlSeqId() {
	return activityDtlSeqId;
}
public void setActivityDtlSeqId(Long activityDtlSeqId) {
	this.activityDtlSeqId = activityDtlSeqId;
}
public Long getActivitySeqId() {
	return activitySeqId;
}
public void setActivitySeqId(Long activitySeqId) {
	this.activitySeqId = activitySeqId;
}
public String getDenialDescription() {
	return denialDescription;
}
public void setDenialDescription(String denialDescription) {
	this.denialDescription = denialDescription;
}
public BigDecimal getAllowedAmount() {
	return allowedAmount;
}
public void setAllowedAmount(BigDecimal allowedAmount) {
	this.allowedAmount = allowedAmount;
}
public String getAmountAllowed() {
	return amountAllowed;
}
public void setAmountAllowed(String amountAllowed) {
	this.amountAllowed = amountAllowed;
}

public String getSearchType() {
	return searchType;
}
public void setSearchType(String searchType) {
	this.searchType = searchType;
}
public String getActivityTariffStatus() {
	return activityTariffStatus;
}
public void setActivityTariffStatus(String activityTariffStatus) {
	this.activityTariffStatus = activityTariffStatus;
}
public BigDecimal getUnitPrice() {
	return unitPrice;
}
public void setUnitPrice(BigDecimal unitPrice) {
	this.unitPrice = unitPrice;
}
public String getTariffYN() {
	return tariffYN;
}
public void setTariffYN(String tariffYN) {
	this.tariffYN = tariffYN;
}
public String getDisplayMsg() {
	return displayMsg;
}
public void setDisplayMsg(String displayMsg) {
	this.displayMsg = displayMsg;
}
public Float getQuantity() {
	return quantity;
}
public void setQuantity(Float quantity) {
	this.quantity = quantity;
}
public Float getApprovedQuantity() {
	return approvedQuantity;
}
public void setApprovedQuantity(Float approvedQuantity) {
	this.approvedQuantity = approvedQuantity;
}
public String getRemarks() {
	return remarks;
}
public void setRemarks(String remarks) {
	this.remarks = remarks;
}

public String getDenialCode() {
	return denialCode;
}
public void setDenialCode(String denialCode) {
	this.denialCode = denialCode;
}
public Long getClaimSeqID() {
	return claimSeqID;
}
public void setClaimSeqID(Long claimSeqID) {
	this.claimSeqID = claimSeqID;
}
public String getClaimNo() {
	return claimNo;
}
public void setClaimNo(String claimNo) {
	this.claimNo = claimNo;
}
public String getAuthType() {
	return authType;
}
public void setAuthType(String authType) {
	this.authType = authType;
}
public BigDecimal getRdisAllowedAmount() {
	return rdisAllowedAmount;
}
public void setRdisAllowedAmount(BigDecimal rdisAllowedAmount) {
	this.rdisAllowedAmount = rdisAllowedAmount;
}
public BigDecimal getRdeductible() {
	return rdeductible;
}
public void setRdeductible(BigDecimal rdeductible) {
	this.rdeductible = rdeductible;
}
public BigDecimal getRoutOfPocket() {
	return routOfPocket;
}
public void setRoutOfPocket(BigDecimal routOfPocket) {
	this.routOfPocket = routOfPocket;
}
public BigDecimal getRcoinsurance() {
	return rcoinsurance;
}
public void setRcoinsurance(BigDecimal rcoinsurance) {
	this.rcoinsurance = rcoinsurance;
}
public BigDecimal getRcopay() {
	return rcopay;
}
public void setRcopay(BigDecimal rcopay) {
	this.rcopay = rcopay;
}
public BigDecimal getDisAllowedAmount() {
	return disAllowedAmount;
}
public void setDisAllowedAmount(BigDecimal disAllowedAmount) {
	this.disAllowedAmount = disAllowedAmount;
}
public BigDecimal getDiscountPerUnit() {
	return discountPerUnit;
}
public void setDiscountPerUnit(BigDecimal discountPerUnit) {
	this.discountPerUnit = discountPerUnit;
}
public String getOverrideYN() {
	return overrideYN;
}
public void setOverrideYN(String overrideYN) {
	this.overrideYN = overrideYN;
}
public String getNetworkProviderType() {
	return networkProviderType;
}
public void setNetworkProviderType(String networkProviderType) {
	this.networkProviderType = networkProviderType;
}
public String getClinicianName() {
	return clinicianName;
}
public void setClinicianName(String clinicianName) {
	this.clinicianName = clinicianName;
}
public String getOverrideRemarks() {
	return overrideRemarks;
}
public void setOverrideRemarks(String overrideRemarks) {
	this.overrideRemarks = overrideRemarks;
}
public String getActivityStatus() {
	return activityStatus;
}
public void setActivityStatus(String activityStatus) {
	this.activityStatus = activityStatus;
}
public Integer getMedicationDays() {
	return medicationDays;
}
public void setMedicationDays(Integer medicationDays) {
	this.medicationDays = medicationDays;
}
public String getPosology() {
	return posology;
}
public void setPosology(String posology) {
	this.posology = posology;
}
public BigDecimal getProviderRequestedAmt() {
	return providerRequestedAmt;
}
public void setProviderRequestedAmt(BigDecimal providerRequestedAmt) {
	this.providerRequestedAmt = providerRequestedAmt;
}
public String getActivityServiceCodeDesc() {
	return activityServiceCodeDesc;
}
public void setActivityServiceCodeDesc(String activityServiceCodeDesc) {
	this.activityServiceCodeDesc = activityServiceCodeDesc;
}

public BigDecimal getRicopar() {
	return ricopar;
}
public void setRicopar(BigDecimal ricopar) {
	this.ricopar = ricopar;
}
public BigDecimal getUcr() {
	return ucr;
}
public void setUcr(BigDecimal ucr) {
	this.ucr = ucr;
}
public String getClaimtype() {
	return claimtype;
}
public void setClaimtype(String claimtype) {
	this.claimtype = claimtype;
}
public String getDenialRemarks() {
	return denialRemarks;
}
public void setDenialRemarks(String denialRemarks) {
	this.denialRemarks = denialRemarks;
}
public BigDecimal getNonNetworkCopay() {
	return nonNetworkCopay;
}
public void setNonNetworkCopay(BigDecimal nonNetworkCopay) {
	this.nonNetworkCopay = nonNetworkCopay;
}

public String getBenefitTypeActivity() {
	return benefitTypeActivity;
}
public void setBenefitTypeActivity(String benefitTypeActivity) {
	this.benefitTypeActivity = benefitTypeActivity;
}
public String getFlowType() {
	return flowType;
}
public void setFlowType(String flowType) {
	this.flowType = flowType;
}
public String getInternalDesc() {
	return internalDesc;
}
public void setInternalDesc(String internalDesc) {
	this.internalDesc = internalDesc;
}
public Long getTariffSeqId() {
	return tariffSeqId;
}
public void setTariffSeqId(Long tariffSeqId) {
	this.tariffSeqId = tariffSeqId;
}
public String getUnitPriceCurrencyType() {
	return unitPriceCurrencyType;
}
public void setUnitPriceCurrencyType(String unitPriceCurrencyType) {
	this.unitPriceCurrencyType = unitPriceCurrencyType;
}
public BigDecimal getConvertedUnitPrice() {
	return convertedUnitPrice;
}
public void setConvertedUnitPrice(BigDecimal convertedUnitPrice) {
	this.convertedUnitPrice = convertedUnitPrice;
}
public String getConvertedUnitPricecurrencyType() {
	return convertedUnitPricecurrencyType;
}
public void setConvertedUnitPricecurrencyType(
		String convertedUnitPricecurrencyType) {
	this.convertedUnitPricecurrencyType = convertedUnitPricecurrencyType;
}
public BigDecimal getConversionRate() {
	return conversionRate;
}
public void setConversionRate(BigDecimal conversionRate) {
	this.conversionRate = conversionRate;
}
public String getToothNoReqYN() {
	return toothNoReqYN;
}
public void setToothNoReqYN(String toothNoReqYN) {
	this.toothNoReqYN = toothNoReqYN;
}
public String getToothNo() {
	return toothNo;
}
public void setToothNo(String toothNo) {
	this.toothNo = toothNo;
}
public String getServiceInternalCode() {
	return serviceInternalCode;
}
public void setServiceInternalCode(String serviceInternalCode) {
	this.serviceInternalCode = serviceInternalCode;
}
public String getProvCopayFlag() {
	return provCopayFlag;
}
public void setProvCopayFlag(String provCopayFlag) {
	this.provCopayFlag = provCopayFlag;
}
public String getCopayDeductFlag() {
	return copayDeductFlag;
}
public void setCopayDeductFlag(String copayDeductFlag) {
	this.copayDeductFlag = copayDeductFlag;
}
public String getCopayPerc() {
	return copayPerc;
}
public void setCopayPerc(String copayPerc) {
	this.copayPerc = copayPerc;
}
public String getMemServiceCode() {
	return memServiceCode;
}
public void setMemServiceCode(String memServiceCode) {
	this.memServiceCode = memServiceCode;
}
public String getMemServiceDesc() {
	return memServiceDesc;
}
public void setMemServiceDesc(String memServiceDesc) {
	this.memServiceDesc = memServiceDesc;
}
public String getActivityType() {
	return activityType;
}
public void setActivityType(String activityType) {
	this.activityType = activityType;
}
public String getNewPharmacyYN() {
	return newPharmacyYN;
}
public void setNewPharmacyYN(String newPharmacyYN) {
	this.newPharmacyYN = newPharmacyYN;
}
public Float getDrugCodeQuantity() {
	return drugCodeQuantity;
}
public void setDrugCodeQuantity(Float drugCodeQuantity) {
	this.drugCodeQuantity = drugCodeQuantity;
}
public String getMophCodes() {
	return mophCodes;
}
public void setMophCodes(String mophCodes) {
	this.mophCodes = mophCodes;
}




public String getAreaOfCoverCopay() {
	return areaOfCoverCopay;
}
public void setAreaOfCoverCopay(String areaOfCoverCopay) {
	this.areaOfCoverCopay = areaOfCoverCopay;
}
public String getAreaOfCoverDeduct() {
	return areaOfCoverDeduct;
}
public void setAreaOfCoverDeduct(String areaOfCoverDeduct) {
	this.areaOfCoverDeduct = areaOfCoverDeduct;
}
public String getAreaOfCoverFlag() {
	return areaOfCoverFlag;
}
public void setAreaOfCoverFlag(String areaOfCoverFlag) {
	this.areaOfCoverFlag = areaOfCoverFlag;
}

}//end of ClaimantVO.java