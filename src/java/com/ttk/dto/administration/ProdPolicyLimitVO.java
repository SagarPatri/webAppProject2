/**
 * @ (#) ProdPolicyLimitVO.java Nov 14, 2007
 * Project 	     : TTK HealthCare Services
 * File          : ProdPolicyLimitVO.java
 * Author        : RamaKrishna K M
 * Company       : Span Systems Corporation
 * Date Created  : Nov 14, 2007
 *
 * @author       :  RamaKrishna K M
 * Modified by   :  
 * Modified date :  
 * Reason        :  
 */

package com.ttk.dto.administration;

import com.ttk.dto.BaseVO;

public class ProdPolicyLimitVO extends BaseVO{
	
	private Long lngLimitSeqID = null;
	private Long lngProdPolicySeqID = null;
	private String strEnrolTypeID = "";
	private String strEnrolDesc = "";
	private String strFlag = "";
	private Integer intRenewalDays = null;
	
	
	private String dental = "";
	private String optical = "";
	private String opMaternity = "";
	private String outpatient = "";
	private String prescription = "";
	
	private String partnerDental = "";
	private String partnerOptical = "";
	private String partnerOpMaternity = "";
	private String partnerOutpatient = "";
	private String partnerPrescription = "";
	
	public String getDental() {
		return dental;
	}

	public void setDental(String dental) {
		this.dental = dental;
	}

	public String getOptical() {
		return optical;
	}

	public void setOptical(String optical) {
		this.optical = optical;
	}

	public String getOpMaternity() {
		return opMaternity;
	}

	public void setOpMaternity(String opMaternity) {
		this.opMaternity = opMaternity;
	}

	public String getOutpatient() {
		return outpatient;
	}

	public void setOutpatient(String outpatient) {
		this.outpatient = outpatient;
	}

	public String getPrescription() {
		return prescription;
	}

	public void setPrescription(String prescription) {
		this.prescription = prescription;
	}

	/** Retrieve the RenewalDays
	 * @return Returns the intRenewalDays.
	 */
	public Integer getRenewalDays() {
		return intRenewalDays;
	}//end of getRenewalDays()
	
	/** Sets the RenewalDays
	 * @param intRenewalDays The intRenewalDays to set.
	 */
	public void setRenewalDays(Integer intRenewalDays) {
		this.intRenewalDays = intRenewalDays;
	}//end of setRenewalDays(Integer intRenewalDays)
	
	/** Retrieve the LimitSeqID
	 * @return Returns the lngLimitSeqID.
	 */
	public Long getLimitSeqID() {
		return lngLimitSeqID;
	}//end of getLimitSeqID()
	
	/** Sets the LimitSeqID
	 * @param lngLimitSeqID The lngLimitSeqID to set.
	 */
	public void setLimitSeqID(Long lngLimitSeqID) {
		this.lngLimitSeqID = lngLimitSeqID;
	}//end of setLimitSeqID(Long lngLimitSeqID)
	
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
	
	/** Retrieve the EnrolDesc
	 * @return Returns the strEnrolDesc.
	 */
	public String getEnrolDesc() {
		return strEnrolDesc;
	}//end of getEnrolDesc()
	
	/** Sets the EnrolDesc
	 * @param strEnrolDesc The strEnrolDesc to set.
	 */
	public void setEnrolDesc(String strEnrolDesc) {
		this.strEnrolDesc = strEnrolDesc;
	}//end of setEnrolDesc(String strEnrolDesc)
	
	/** Retrieve the EnrolTypeID
	 * @return Returns the strEnrolTypeID.
	 */
	public String getEnrolTypeID() {
		return strEnrolTypeID;
	}//end of getEnrolTypeID()
	
	/** Sets the EnrolTypeID
	 * @param strEnrolTypeID The strEnrolTypeID to set.
	 */
	public void setEnrolTypeID(String strEnrolTypeID) {
		this.strEnrolTypeID = strEnrolTypeID;
	}//end of setEnrolTypeID(String strEnrolTypeID)
	
	/** Retrieve the Flag
	 * @return Returns the strFlag.
	 */
	public String getFlag() {
		return strFlag;
	}//end of getFlag()
	
	/** Sets the Flag
	 * @param strFlag The strFlag to set.
	 */
	public void setFlag(String strFlag) {
		this.strFlag = strFlag;
	}//end of setFlag(String strFlag)

	public String getPartnerDental() {
		return partnerDental;
	}

	public void setPartnerDental(String partnerDental) {
		this.partnerDental = partnerDental;
	}

	public String getPartnerOptical() {
		return partnerOptical;
	}

	public void setPartnerOptical(String partnerOptical) {
		this.partnerOptical = partnerOptical;
	}

	public String getPartnerOpMaternity() {
		return partnerOpMaternity;
	}

	public void setPartnerOpMaternity(String partnerOpMaternity) {
		this.partnerOpMaternity = partnerOpMaternity;
	}

	public String getPartnerOutpatient() {
		return partnerOutpatient;
	}

	public void setPartnerOutpatient(String partnerOutpatient) {
		this.partnerOutpatient = partnerOutpatient;
	}

	public String getPartnerPrescription() {
		return partnerPrescription;
	}

	public void setPartnerPrescription(String partnerPrescription) {
		this.partnerPrescription = partnerPrescription;
	}
	
}//end of ProdPolicyLimitVO
