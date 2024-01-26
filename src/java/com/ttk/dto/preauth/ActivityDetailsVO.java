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

package com.ttk.dto.preauth;

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
private String providerInternalCode="";
private String providerInternalDescription="";
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
private String alAhliYN;;
private String serviceSeqId;
private String serviceDate;
private String otherRemarks;
private String overrideRemarksDesc;
private String overrideRemarksCode;

private String tpaDenialCode;
private String tpaDenialDescription;
private long activityicdcountflag;

private String alkootRemarks="";
private String resubRemarks="";

private BigDecimal granularUnit;
private BigDecimal noOfUnits;
private String mophDrugYN;
private String overrideSubRemarks;
private String overrideSubRemarksDesc;
private String networkType;
private String enhancedFlag="N";
private String claimType;
private BigDecimal providerQty;
private String disableQty="N";


public String getDisableQty() {
	return disableQty;
}
public void setDisableQty(String disableQty) {
	this.disableQty = disableQty;
}
public String getClaimType() {
	return claimType;
}
public void setClaimType(String claimType) {
	this.claimType = claimType;
}
public String getEnhancedFlag() {
	return enhancedFlag;
}
public void setEnhancedFlag(String enhancedFlag) {
	this.enhancedFlag = enhancedFlag;
}
public String getTpaDenialCode() {
	return tpaDenialCode;
}
public void setTpaDenialCode(String tpaDenialCode) {
	this.tpaDenialCode = tpaDenialCode;
}
public String getTpaDenialDescription() {
	return tpaDenialDescription;
										}
public void setTpaDenialDescription(String tpaDenialDescription) {
	this.tpaDenialDescription = tpaDenialDescription;
}
public String getServiceDate() {
	return serviceDate;
}
public void setServiceDate(String serviceDate) {
	this.serviceDate = serviceDate;
}
public String getServiceSeqId() {
	return serviceSeqId;
}
public void setServiceSeqId(String serviceSeqId) {
	this.serviceSeqId = serviceSeqId;
}
private String mophCodes="";
private String actCarryFwdYN="";
private String removedDenialCode;
private String removedDenialDescription;

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



public String getInternal_Code() {
	return internal_Code;
}
public void setInternal_Code(String internal_Code) {
	this.internal_Code = internal_Code;
}
public String getActivity_Code() {
	return activity_Code;
}
public void setActivity_Code(String activity_Code) {
	this.activity_Code = activity_Code;
}
public String getActivity_Type() {
	return activity_Type;
}
public void setActivity_Type(String activity_Type) {
	this.activity_Type = activity_Type;
}
public String getActivity_Quantity() {
	return activity_Quantity;
}
public void setActivity_Quantity(String activity_Quantity) {
	this.activity_Quantity = activity_Quantity;
}
public String getApproved_Quantity() {
	return approved_Quantity;
}
public void setApproved_Quantity(String approved_Quantity) {
	this.approved_Quantity = approved_Quantity;
}
public String getService_Date() {
	return service_Date;
}
public void setService_Date(String service_Date) {
	this.service_Date = service_Date;
}
public String getRequested_Amount() {
	return requested_Amount;
}
public void setRequested_Amount(String requested_Amount) {
	this.requested_Amount = requested_Amount;
}
public String getGross_amount() {
	return gross_amount;
}
public void setGross_amount(String gross_amount) {
	this.gross_amount = gross_amount;
}
public String getDiscounted_amount() {
	return discounted_amount;
}
public void setDiscounted_amount(String discounted_amount) {
	this.discounted_amount = discounted_amount;
}
public String getDiscounted_Gross_amount() {
	return discounted_Gross_amount;
}
public void setDiscounted_Gross_amount(String discounted_Gross_amount) {
	this.discounted_Gross_amount = discounted_Gross_amount;
}
public String getPatient_Share() {
	return patient_Share;
}
public void setPatient_Share(String patient_Share) {
	this.patient_Share = patient_Share;
}
public String getNet_amount() {
	return net_amount;
}
public void setNet_amount(String net_amount) {
	this.net_amount = net_amount;
}
public String getDisallowed_Amount() {
	return disallowed_Amount;
}
public void setDisallowed_Amount(String disallowed_Amount) {
	this.disallowed_Amount = disallowed_Amount;
}
public String getAllowed_Amount() {
	return allowed_Amount;
}
public void setAllowed_Amount(String allowed_Amount) {
	this.allowed_Amount = allowed_Amount;
}
public String getDenial_Code() {
	return denial_Code;
}
public void setDenial_Code(String denial_Code) {
	this.denial_Code = denial_Code;
}
public String getOver_ride_Date() {
	return over_ride_Date;
}
public void setOver_ride_Date(String over_ride_Date) {
	this.over_ride_Date = over_ride_Date;
}
public String getApproved_amount() {
	return approved_amount;
}
public void setApproved_amount(String approved_amount) {
	this.approved_amount = approved_amount;
}
private String internal_Code;
private String activity_Code;
private String activity_Type;
private String activity_Quantity;
private String approved_Quantity;
private String service_Date;
private String requested_Amount;
private String gross_amount;
private String discounted_amount;
private String discounted_Gross_amount;
private String patient_Share;
private String net_amount;
private String disallowed_Amount;
private String allowed_Amount;
private String denial_Code;
private String over_ride_Date;
private String approved_amount;
private String denial_description;
private String internal_description;
private String activity_description;




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
	// 

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

public ActivityDetailsVO(int serialNo, String internal_Code,String internal_description, String activity_Code,String activity_description,
		String activity_Type, String activity_Quantity,
		String approved_Quantity, String service_Date, String requested_Amount,
		String gross_amount, String discounted_amount,
		String discounted_Gross_amount, String patient_Share,
		String net_amount, String disallowed_Amount, String allowed_Amount,
		String denial_Code,String denial_description ,String over_ride_Date, String approved_amount) {
	
	
	
	this.serialNo=serialNo;
	this.internal_Code=internal_Code;
	this.setInternal_description(internal_description);
	this.activity_Code=activity_Code; 
	this.setActivity_description(activity_description); 
	this.internal_Code=internal_Code;
	this.activity_Type=activity_Type;
	this.activity_Quantity=activity_Quantity;
	this.approved_Quantity=approved_Quantity;
	this.service_Date=service_Date;
	this.requested_Amount=requested_Amount;
	this.gross_amount=gross_amount;
	this.discounted_amount=discounted_amount;
	this.discounted_Gross_amount=discounted_Gross_amount;
	this.patient_Share=patient_Share;
	this.net_amount=net_amount;
	this.disallowed_Amount=disallowed_Amount;
	this.allowed_Amount=allowed_Amount;
	this.denial_Code=denial_Code;
	this.setDenial_description(denial_description);
	this.over_ride_Date=over_ride_Date;
	this.approved_amount=approved_amount;
	
	
	
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
public String getProviderInternalCode() {
	return providerInternalCode;
}
public void setProviderInternalCode(String providerInternalCode) {
	this.providerInternalCode = providerInternalCode;
}
public String getProviderInternalDescription() {
	return providerInternalDescription;
}
public void setProviderInternalDescription(String providerInternalDescription) {
	this.providerInternalDescription = providerInternalDescription;
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
/**
 * @return the alAhliYN
 */
public String getAlAhliYN() {
	return alAhliYN;
}
/**
 * @param alAhliYN the alAhliYN to set
 */
public void setAlAhliYN(String alAhliYN) {
	this.alAhliYN = alAhliYN;
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
public String getActCarryFwdYN() {
	return actCarryFwdYN;
}
public void setActCarryFwdYN(String actCarryFwdYN) {
	this.actCarryFwdYN = actCarryFwdYN;
}
public String getOtherRemarks() {
	return otherRemarks;
}
public void setOtherRemarks(String otherRemarks) {
	this.otherRemarks = otherRemarks;
}
public String getOverrideRemarksDesc() {
	return overrideRemarksDesc;
}
public void setOverrideRemarksDesc(String overrideRemarksDesc) {
	this.overrideRemarksDesc = overrideRemarksDesc;
}
public String getOverrideRemarksCode() {
	return overrideRemarksCode;
}
public void setOverrideRemarksCode(String overrideRemarksCode) {
	this.overrideRemarksCode = overrideRemarksCode;
}
public String getRemovedDenialCode() {
	return removedDenialCode;
}
public void setRemovedDenialCode(String removedDenialCode) {
	this.removedDenialCode = removedDenialCode;
}
public String getRemovedDenialDescription() {
	return removedDenialDescription;
}
public void setRemovedDenialDescription(String removedDenialDescription) {
	this.removedDenialDescription = removedDenialDescription;
}
public long getActivityicdcountflag() {
	return activityicdcountflag;
}
public void setActivityicdcountflag(long activityicdcountflag) {
	this.activityicdcountflag = activityicdcountflag;
}
public String getAlkootRemarks() {
	return alkootRemarks;
}
public void setAlkootRemarks(String alkootRemarks) {
	this.alkootRemarks = alkootRemarks;
}
public String getResubRemarks() {
	return resubRemarks;
}
public void setResubRemarks(String resubRemarks) {
	this.resubRemarks = resubRemarks;
}
public BigDecimal getGranularUnit() {
	return granularUnit;
}
public void setGranularUnit(BigDecimal granularUnit) {
	this.granularUnit = granularUnit;
}
public BigDecimal getNoOfUnits() {
	return noOfUnits;
}
public void setNoOfUnits(BigDecimal noOfUnits) {
	this.noOfUnits = noOfUnits;
}
public String getMophDrugYN() {
	return mophDrugYN;
}
public void setMophDrugYN(String mophDrugYN) {
	this.mophDrugYN = mophDrugYN;
}
public String getDenial_description() {
	return denial_description;
}
public void setDenial_description(String denial_description) {
	this.denial_description = denial_description;
}
public String getInternal_description() {
	return internal_description;
}
public void setInternal_description(String internal_description) {
	this.internal_description = internal_description;
}
public String getActivity_description() {
	return activity_description;
}
public void setActivity_description(String activity_description) {
	this.activity_description = activity_description;
}
public String getOverrideSubRemarks() {
	return overrideSubRemarks;
}
public void setOverrideSubRemarks(String overrideSubRemarks) {
	this.overrideSubRemarks = overrideSubRemarks;
}
public String getOverrideSubRemarksDesc() {
	return overrideSubRemarksDesc;
}
public void setOverrideSubRemarksDesc(String overrideSubRemarksDesc) {
	this.overrideSubRemarksDesc = overrideSubRemarksDesc;
}
public String getNetworkType() {
	return networkType;
}
public void setNetworkType(String networkType) {
	this.networkType = networkType;
}
public BigDecimal getProviderQty() {
	return providerQty;
}
public void setProviderQty(BigDecimal providerQty) {
	this.providerQty = providerQty;
}



}//end of ClaimantVO.java