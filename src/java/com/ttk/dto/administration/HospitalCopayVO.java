/**
 * @ (#) HospitalCopayVO.java Nov 3, 2008
 * Project 	     : TTK HealthCare Services
 * File          : HospitalCopayVO.java
 * Author        : RamaKrishna K M
 * Company       : Span Systems Corporation
 * Date Created  : Nov 3, 2008
 *
 * @author       :  RamaKrishna K M
 * Modified by   :  
 * Modified date :  
 * Reason        :  
 */

package com.ttk.dto.administration;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.ttk.dto.BaseVO;

/**
 * @author ramakrishna_km
 *
 */
public class HospitalCopayVO extends BaseVO{
	
	private Long lngProdHospSeqID = null;
	private Long lngHospSeqID = null;
	private Long lngProdPolicySeqID = null;
	private BigDecimal bdCopayAmt = null;
	private BigDecimal bdCopayPerc = null;
	private String providerCopayBenefits = null;
	private String serviceType = null;
	private BigDecimal deductible = null;
	private String suffix = null;
	private String applyRule = null;
	private ArrayList alCopayDetails = null;
	private Long copaySeqId = null;
	private String benefitType = null;
	private String benefitTypeId = null;
	 private String strDeleteImageName = "DeleteIcon";
	    private String strDeleteImageTitle = "Delete";
	/** Retrieve the CopayAmt
	 * @return Returns the bdCopayAmt.
	 */
	public BigDecimal getCopayAmt() {
		return bdCopayAmt;
	}//end of getCopayAmt()
	
	public String toString() {
		return "HospitalCopayVO [lngProdHospSeqID=" + lngProdHospSeqID
				+ ", lngHospSeqID=" + lngHospSeqID + ", lngProdPolicySeqID="
				+ lngProdPolicySeqID + ", bdCopayAmt=" + bdCopayAmt
				+ ", bdCopayPerc=" + bdCopayPerc + ", providerCopayBenefits="
				+ providerCopayBenefits + ", serviceType=" + serviceType
				+ ", deductible=" + deductible + ", suffix=" + suffix
				+ ", applyRule=" + applyRule + "]";
	}

	/** Sets the CopayAmt
	 * @param bdCopayAmt The bdCopayAmt to set.
	 */
	public void setCopayAmt(BigDecimal bdCopayAmt) {
		this.bdCopayAmt = bdCopayAmt;
	}//end of setCopayAmt(BigDecimal bdCopayAmt)
	
	/** Retrieve the CopayPerc
	 * @return Returns the bdCopayPerc.
	 */
	public BigDecimal getCopayPerc() {
		return bdCopayPerc;
	}//end of getCopayPerc()
	
	/** Sets the CopayPerc
	 * @param bdCopayPerc The bdCopayPerc to set.
	 */
	public void setCopayPerc(BigDecimal bdCopayPerc) {
		this.bdCopayPerc = bdCopayPerc;
	}//end of setCopayPerc(BigDecimal bdCopayPerc)
	
	/** Retrieve the ProdHospSeqID
	 * @return Returns the lngProdHospSeqID.
	 */
	public Long getProdHospSeqID() {
		return lngProdHospSeqID;
	}//end of getProdHospSeqID()
	
	/** Sets the ProdHospSeqID
	 * @param lngProdHospSeqID The lngProdHospSeqID to set.
	 */
	public void setProdHospSeqID(Long lngProdHospSeqID) {
		this.lngProdHospSeqID = lngProdHospSeqID;
	}//end of setProdHospSeqID(Long lngProdHospSeqID)
	
	/** Retrieve the hospSeqID
	 * @return Returns the lngHospSeqID.
	 */
	public Long getHospSeqID() {
		return lngHospSeqID;
	}//end of getHospSeqID()
	
	/** Sets the hospSeqID
	 * @param lngHospSeqID The lngHospSeqID to set.
	 */
	public void setHospSeqID(Long lngHospSeqID) {
		this.lngHospSeqID = lngHospSeqID;
	}//end of setHospSeqID(Long lngHospSeqID)
	
	/** Retrieve the ProdPolicySeqID
	 * @return Returns the lngProdPolicySeqID.
	 */
	public Long getProdPolicySeqID() {
		return lngProdPolicySeqID;
	}//end of getProdPolicySeqID()
	
	/** Sets the ProdPolicySeqID
	 * @param lngProdPolicySeqID The lngProdPolicySeqID to set.
	 */
	public void setProdPolicySeqID(Long lngProdPolicySeqID) {
		this.lngProdPolicySeqID = lngProdPolicySeqID;
	}//end of setProdPolicySeqID(Long lngProdPolicySeqID)

	public String getProviderCopayBenefits() {
		return providerCopayBenefits;
	}

	public void setProviderCopayBenefits(String providerCopayBenefits) {
		this.providerCopayBenefits = providerCopayBenefits;
	}

	public BigDecimal getDeductible() {
		return deductible;
	}

	public void setDeductible(BigDecimal deductible) {
		this.deductible = deductible;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getApplyRule() {
		return applyRule;
	}

	public void setApplyRule(String applyRule) {
		this.applyRule = applyRule;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public ArrayList getAlCopayDetails() {
		return alCopayDetails;
	}

	public void setAlCopayDetails(ArrayList alCopayDetails) {
		this.alCopayDetails = alCopayDetails;
	}

	public Long getCopaySeqId() {
		return copaySeqId;
	}

	public void setCopaySeqId(Long copaySeqId) {
		this.copaySeqId = copaySeqId;
	}

	public String getBenefitType() {
		return benefitType;
	}

	public void setBenefitType(String benefitType) {
		this.benefitType = benefitType;
	}

	public String getDeleteImageName() {
		return strDeleteImageName;
	}

	public void setDeleteImageName(String strDeleteImageName) {
		this.strDeleteImageName = strDeleteImageName;
	}

	public String getDeleteImageTitle() {
		return strDeleteImageTitle;
	}

	public void setDeleteImageTitle(String strDeleteImageTitle) {
		this.strDeleteImageTitle = strDeleteImageTitle;
	}

	public String getBenefitTypeId() {
		return benefitTypeId;
	}

	public void setBenefitTypeId(String benefitTypeId) {
		this.benefitTypeId = benefitTypeId;
	}

}//end of HospitalCopayVO
