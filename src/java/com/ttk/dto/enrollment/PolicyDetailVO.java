/**
 * @ (#) PolicyDetailVO.java Jan 31, 2006
 * Project 	     : TTK HealthCare Services
 * File          : PolicyDetailVO.java
 * Author        : RamaKrishna K M
 * Company       : Span Systems Corporation
 * Date Created  : Jan 31, 2006
 *
 * @author       :  RamaKrishna K M
 * Modified by   :  
 * Modified date :  
 * Reason        :  
 */

package com.ttk.dto.enrollment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;






import org.apache.struts.upload.FormFile;

import com.ttk.dto.enrollment.MemberAddressVO;

public class PolicyDetailVO extends PolicyVO{
	
	
private String capitationPolicy = "";
	private String authorityProductCode = "";
    private String healthAuthority = "";
	private String strPolicyHolderName = "";
    private String strINSStatusTypeID = "";
    private String strSubTypeID = "";
    private Date dtStartDate = null;
    private Date dtEndDate = null;
    private Date dtIssueDate = null;
    private String strProposalYN = "";
    private Date dtDeclarationDate = null;
    private String strPolicyStatusID = "";
    private String strTPAStatusTypeID = "";
    private Long lngOfficeSeqID = null;
    private Long lngGroupRegnSeqID = null;
    private Date dtRecdDate = null;
    private String strBankAccNbr = "";
    private String strCondonedYN = "";
    private String strIssueChqTypeID = "";
    private BigDecimal bdTotalSumInsured = null;
    private BigDecimal bdTotalPremium = null;
    private BigDecimal bdCorpStopLoss = null;
    private String strBeneficiaryName = "";
    private String strRemarks = "";
    private String strRelationTypeID = "";
    private String strPANNbr = null;
    private String strProposalFormYN = "";
    private Long lngProductSeqID = null;
    private MemberAddressVO address = null;
    private String strCardPhotoYN = "";
    private Integer intCardClearance = null;
    private Integer intClaimClearance = null;
    private Long lngEmployeeCnt = null;
    private Long lngMemberCnt = null;
    private String strRenewalPolicyYn = "";
    private String strEnrollOnEmployeeNbr ="";
    private String strCardDispatchID = "";
    private Long lngInsuranceSeqID = null ;
    private Integer intReviewCount = null;
    private Long lngEventSeqID = null;
    private Integer intRequiredReviewCnt = null;
    private String strCardPrintYN = "";
    private Long lngTemplateID = null;
    private String strInsScheme = "";
    private String strReview = "";
    private String strInsertMode = "";
    private String strSumWording = "";
    private String strPremiumWording = "";
    private String strBankName ="";
    private String strBranch = "";
    private String strBankPhone = "";
    private String strMICRCode = "";
    private Long lngBankSeqID = null;
    private Integer intPreAuthClearance = null;
    private String strClarificationTypeID= "";
    private Date dtClarifiedDate = null;
    private String strINSStatus = "";
    private String strPolicySubType = "";
    private String strPolicyStatus = "";
    private String strProductName = "";
    private String strAdminStatusID = "";
    private Long lngGroupBranchSeqID = null;
    private String strEndorseGenTypeID = "";
    private String strEndorseTypeDesc = "";
    private String strDMSRefID = "";
    private String strBufferAllowedYN = "";
    private BigDecimal bdTotalBufferAmt = null;
    private String strAdmnAuthorityTypeID = "";
    private String strAllocatedTypeID = "";
    private BigDecimal bdAllocatedAmt = null;
    private String strBankAccountName = "";
    private String strPhotoPresentYN = "";
    private String strPolicyNotLegibleYN = "";
    private ArrayList alProductList = new ArrayList(); // Product List
    private Long lngTenure = null;
    private String strEventName = "";
    private String strSchemeName = "";
    private String strPrevSchemeName = "";
    private String strPolicyRemarks = "";
    private String strDomicilaryTypeID = "";
    private String strDischargeVoucherMandatoryYN = "";//DISCHARGE_VOUCH_MANDATORY_YN
    private String strInsuranceEndYN = "";//INSURANCE_ENDORSEMENT_YN
    private String strProductChangeYN = "";//For Renewal Policy Product Change
    private Integer intNoofPhotosRcvd = null;//NUMBER_OF_PHOTOS_RCVD
    private Integer intTenureDays = null;
    private String strBufferRecYN = "";//BUFFER_REC_YN
    private String strDOBOChangeYN = "";//RENEW_USING_DIFF_BRANCH_YN
	private String strBrokerRecYN = "";//BUFFER_REC_YN
    private String strStopPreAuthsYN = "N";
    private String strStopClaimsYN = "N";
	private String strCashBenefitYN = "N";//KOC 1270 for hospital cash benefit
    private String strConvCashBenefitYN = "N";//KOC 1270 for hospital cash benefit
    private BigDecimal bdAddedSIAmt = null;
    private Integer intNoofFamiliesAdded=null;
    private Integer intActiveMembers=null;
    private Integer intCancelMembers=null;
    private String  strAddedSIWording="";
    private String productNetworkType;
    private String tariffType="";
    private String currencyFormat;
    private String currencyFormat1;
    private String reInsurer;
    private String limitCurrencyFormat;
    private BigDecimal bdLimitPerMember = null;
    private BigDecimal matPremium = null;
    private BigDecimal exlMatPremium = null;
    private String memcountYN="";
    private String lineOfBussiness="";
    private String memberStatus="";
    private Date stopCashlessDate;
    private Date stopClaimsDate;
    private String outpatientbenfTypeCond;
    private String mailNotificationYN;
    private String mailNotificationhiddenYN;
	private String networkSortNum="";
    private String benefitTypeProvided;
    
    private Long premiumSeqId = null;
    private String minimumAge = null;
    private String maximumAge = null;
    private String premium = null;
    private String gender= null;
    private String deleteYN= null;
    private ArrayList brokerNameList = new ArrayList(); // brokerNameList List
    private Long brokerSeqID=null;
	private BigDecimal brokerCommission=null;
    
    public BigDecimal getBrokerCommission() {
		return brokerCommission;
	}

	public void setBrokerCommission(BigDecimal brokerCommission) {
		this.brokerCommission = brokerCommission;
	}

	public Long getBrokerSeqID() {
		return brokerSeqID;
	}

	public void setBrokerSeqID(Long brokerSeqID) {
		this.brokerSeqID = brokerSeqID;
	}



	




	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCapitationPolicy() {
		return capitationPolicy;
	}

	public void setCapitationPolicy(String capitationPolicy) {
		this.capitationPolicy = capitationPolicy;
	}
    
    public String getAuthorityProductCode() {				
		return authorityProductCode;
	}

	public void setAuthorityProductCode(String authorityProductCode) {
		this.authorityProductCode = authorityProductCode;
	}
    public String getReInsurer() {
		return reInsurer;
	}

	public void setReInsurer(String reInsurer) {
		this.reInsurer = reInsurer;
	}

	public String getCurrencyFormat1() {
		return currencyFormat1;
	}

	public void setCurrencyFormat1(String currencyFormat1) {
		this.currencyFormat1 = currencyFormat1;
	}

	public String getCurrencyFormat2() {
		return currencyFormat2;
	}

	public void setCurrencyFormat2(String currencyFormat2) {
		this.currencyFormat2 = currencyFormat2;
	}

	private String currencyFormat2;
    
     //Changes as per Change request 1216B IBM CR
    
    private String strMemberBufferYN="N";
    private String strMemberBuffer="";

    //added for koc 1278 PED
    private String otherTPAInsuredname = "";
    private String otherTPAPolicyNr = "";
    private Date TPAStartDate = null;
    private Date TPAEndDate = null;
    private String otherTPASIValue = "";
    private String otherTPADelayCondoned = "";
    private String otherTPAPortApproved = "";
        
	//KOC 1286 for OPD Benefit
    private String stropdClaimsYN = "N";
	private Date dtStartDate1 = null;
    private Date dtEndDate1 = null;
     //added for KOC-1273
   	private Long lngBrokerID = null;
    
    private String strBrokerYN="";
    private String strCriticalBenefitYN = "";
    private String strSurvivalPeriodYN = "";
    private String strPatMailTo="";
	private String strPatMailCC="";
	private String strPatEnableYN="";
	private String strClmMailTo="";
	private String strClmMailCC="";
	private String strClmEnableYN="";
	
	private String strHealthCheckType = "";//OPD_4_hosptial
    private String StrPaymentTo = "";//OPD_4_hosptial
    private String strHrAppYN="";//added for hyundai buffer
    private String strCriBufferYN="";//added for hyundai buffer
    
    private String currency;
    private String policyFileName;
    private FormFile policyFileCopy; 
    
  //intX- Reinsurer
    private float lngReInsShare;
    private float lngInsShare;
    private String pi;
    private String nonpi;
    
	//card benefits
	private String coInsurance="";
	private String deductable="";
	private String classRoomType="";
	private String planType="";
	private String maternityYN="";
	private String maternityCopay="";
	private String opticalYN="";
	private String opticalCopay="";
	private String dentalYN="";
	private String dentalCopay="";
	private String eligibility="";
	private String ipopServices="";
	private String pharmaceutical=""; 
	
	
	 private String logicType="";
	 private String administrationCharges="";
	 private String creditGeneration="";
	 private String maternityMinBand="";
	 private String maternityMaxBand="";
	 private String creditGenerationOth="";
	 private String treatiesPlanId;
	 private String reinsurerId;
	 private String treatyType;
	 public String getCoInsurance() {
		return coInsurance;
	}

	public void setCoInsurance(String coInsurance) {
		this.coInsurance = coInsurance;
	}

	public String getDeductable() {
		return deductable;
	}

	public void setDeductable(String deductable) {
		this.deductable = deductable;
	}

	public String getClassRoomType() {
		return classRoomType;
	}

	public void setClassRoomType(String classRoomType) {
		this.classRoomType = classRoomType;
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

	public String getMaternityYN() {
		return maternityYN;
	}

	public void setMaternityYN(String maternityYN) {
		this.maternityYN = maternityYN;
	}

	public String getMaternityCopay() {
		return maternityCopay;
	}

	public void setMaternityCopay(String maternityCopay) {
		this.maternityCopay = maternityCopay;
	}

	public String getOpticalYN() {
		return opticalYN;
	}

	public void setOpticalYN(String opticalYN) {
		this.opticalYN = opticalYN;
	}

	public String getOpticalCopay() {
		return opticalCopay;
	}

	public void setOpticalCopay(String opticalCopay) {
		this.opticalCopay = opticalCopay;
	}

	public String getDentalYN() {
		return dentalYN;
	}

	public void setDentalYN(String dentalYN) {
		this.dentalYN = dentalYN;
	}

	public String getDentalCopay() {
		return dentalCopay;
	}

	public void setDentalCopay(String dentalCopay) {
		this.dentalCopay = dentalCopay;
	}

	public String getEligibility() {
		return eligibility;
	}

	public void setEligibility(String eligibility) {
		this.eligibility = eligibility;
	}

	public String getIpopServices() {
		return ipopServices;
	}

	public void setIpopServices(String ipopServices) {
		this.ipopServices = ipopServices;
	}

	public String getPharmaceutical() {
		return pharmaceutical;
	}

	public void setPharmaceutical(String pharmaceutical) {
		this.pharmaceutical = pharmaceutical;
	}

    
	public String getHrAppYN() {
		return strHrAppYN;
	}

	public void setHrAppYN(String strHrAppYN) {
		this.strHrAppYN = strHrAppYN;
	}

	//OPD_4_hosptial
	public String getPaymentTo() {
		return StrPaymentTo;
	}

	public void setPaymentTo(String strPaymentTo) {
		StrPaymentTo = strPaymentTo;
	}

	public String getHealthCheckType() {
		return strHealthCheckType;
	}

	public void setHealthCheckType(String strHealthCheckType) {
		this.strHealthCheckType = strHealthCheckType;
	}
	//OPD_4_hosptial
	
	//PatMailTo  ,PatMailCC  ,PatEnableYN  ,ClmMailTo  ,ClmMailCC   ,  ClmEnableYN
	 public String getPatMailTo() {
			return strPatMailTo;
		}

	public void setPatMailTo(String patMailTo) {
		strPatMailTo = patMailTo;
	}

	public String getPatMailCC() {
		return strPatMailCC;
	}

	public void setPatMailCC(String patMailCC) {
		strPatMailCC = patMailCC;
	}

	public String getPatEnableYN() {
		return strPatEnableYN;
	}

	public void setPatEnableYN(String patEnableYN) {
		strPatEnableYN = patEnableYN;
	}

	public String getClmMailTo() {
		return strClmMailTo;
	}

	public void setClmMailTo(String clmMailTo) {
		strClmMailTo = clmMailTo;
	}

	public String getClmMailCC() {
		return strClmMailCC;
	}

	public void setClmMailCC(String clmMailCC) {
		strClmMailCC = clmMailCC;
	}

	public String getClmEnableYN() {
		return strClmEnableYN;
	}

	public void setClmEnableYN(String clmEnableYN) {
		strClmEnableYN = clmEnableYN;
	}

	//1274A
	
    
    /**
	 * @param TPAStartDate the TPAStartDate to set
	 */
	public void setTPAStartDate(Date TPAStartDate) {
		this.TPAStartDate = TPAStartDate;
	}

	/**
	 * @return the TPAStartDate
	 */
	public Date getTPAStartDate() {
		return TPAStartDate;
	}

	/**
	 * @param TPAEndDate the TPAEndDate to set
	 */
	public void setTPAEndDate(Date TPAEndDate) {
		this.TPAEndDate = TPAEndDate;
	}

	/**
	 * @return the TPAEndDate
	 */
	public Date getTPAEndDate() {
		return TPAEndDate;
	}

	/**
	 * @param otherTPAInsuredname the otherTPAInsuredname to set
	 */
	public void setOtherTPAInsuredname(String otherTPAInsuredname) {
		this.otherTPAInsuredname = otherTPAInsuredname;
	}

	/**
	 * @return the otherTPAInsuredname
	 */
	public String getOtherTPAInsuredname() {
		return otherTPAInsuredname;
	}

	/**
	 * @param otherTPAPolicyNr the otherTPAPolicyNr to set
	 */
	public void setOtherTPAPolicyNr(String otherTPAPolicyNr) {
		this.otherTPAPolicyNr = otherTPAPolicyNr;
	}

	/**
	 * @return the otherTPAPolicyNr
	 */
	public String getOtherTPAPolicyNr() {
		return otherTPAPolicyNr;
	}

	/**
	 * @param otherTPASIValue the otherTPASIValue to set
	 */
	public void setOtherTPASIValue(String otherTPASIValue) {
		this.otherTPASIValue = otherTPASIValue;
	}

	/**
	 * @return the otherTPASIValue
	 */
	public String getOtherTPASIValue() {
		return otherTPASIValue;
	}

	/**
	 * @param otherTPADelayCondoned the otherTPADelayCondoned to set
	 */
	public void setOtherTPADelayCondoned(String otherTPADelayCondoned) {
		this.otherTPADelayCondoned = otherTPADelayCondoned;
	}

	/**
	 * @return the otherTPADelayCondoned
	 */
	public String getOtherTPADelayCondoned() {
		return otherTPADelayCondoned;
	}

	/**
	 * @param otherTPAPortApproved the otherTPAPortApproved to set
	 */
	public void setOtherTPAPortApproved(String otherTPAPortApproved) {
		this.otherTPAPortApproved = otherTPAPortApproved;
	}

	/**
	 * @return the otherTPAPortApproved
	 */
	public String getOtherTPAPortApproved() {
		return otherTPAPortApproved;
	}
	//added for koc 1278 PED
    
    /**
	 * @return the stropdClaimsYN
	 */
	public String getopdClaimsYN() {
		return stropdClaimsYN;
	}

	/**
	 * @param stropdClaimsYN the stropdClaimsYN to set
	 */
	public void setopdClaimsYN(String stropdClaimsYN) {
		this.stropdClaimsYN = stropdClaimsYN;
	}
	//KOC 1286 for OPD Benefit
    /**
	 * @param strConvCashBenefitYN the strConvCashBenefitYN to set
	 */
	public void setConvCashBenefitYN(String strConvCashBenefitYN) {
		this.strConvCashBenefitYN = strConvCashBenefitYN;
	}

	/**
	 * @return the strConvCashBenefitYN
	 */
	public String getConvCashBenefitYN() {
		return strConvCashBenefitYN;
	}
	
	/**
	 * @param strCashBenefitYN the strCashBenefitYN to set
	 */
	public void setCashBenefitYN(String strCashBenefitYN) {
		this.strCashBenefitYN = strCashBenefitYN;
	}

	/**
	 * @return the strCashBenefitYN
	 */
	public String getCashBenefitYN() {
		return strCashBenefitYN;
	}
	//KOC 1270 for hospital cash benefit
    /**
    /**
    
   
   /**
	 * @param memberBufferYN the memberBufferYN to set
	 */
	public void setMemberBufferYN(String memberBufferYN) {
		strMemberBufferYN = memberBufferYN;
	}

	/**
	 * @return the memberBufferYN
	 */
	public String getMemberBufferYN() {
		return strMemberBufferYN;
	}
    
    //Changes as per Change request 1216B IBM CR
    
    /** Retrieve the AddedSIWording
	 * @return the strAddedSIWording
	 */
	public String getAddedSIWording() {
		return strAddedSIWording;
	}//end of getAddedSIWording()

	/** Sets the AddedSIWording
	 * @param strAddedSIWording the strAddedSIWording to set
	 */
	public void setAddedSIWording(String strAddedSIWording) {
		this.strAddedSIWording = strAddedSIWording;
	}//end of setAddedSIWording(String strAddedSIWording)

	/** Retrieve the ActiveMembers
	 * @return the intActiveMembers
	 */
	public Integer getActiveMembers() {
		return intActiveMembers;
	}//end of getActiveMembers() 

	/** Sets the ActiveMembers
	 * @param intActiveMembers the intActiveMembers to set
	 */
	public void setActiveMembers(Integer intActiveMembers) {
		this.intActiveMembers = intActiveMembers;
	}//end of setActiveMembers(Integer intActiveMembers)

	/** Retrieve the CancelMembers
	 * @return the intCancelMembers
	 */
	public Integer getCancelMembers() {
		return intCancelMembers;
	}//end of getCancelMembers()

	/** Sets the CancelMembers
	 * @param intCancelMembers the intCancelMembers to set
	 */
	public void setCancelMembers(Integer intCancelMembers) {
		this.intCancelMembers = intCancelMembers;
	}//end of setCancelMembers(Integer intCancelMembers)

	/** Retrieve the AddedSIAmt
	 * @return the bdAddedSIAmt
	 */
	public BigDecimal getAddedSIAmt() {
		return bdAddedSIAmt;
	}//end of getAddedSIAmt() 

	/** Sets the AddedSIAmt
	 * @param bdAddedSIAmt the bdAddedSIAmt to set
	 */
	public void setAddedSIAmt(BigDecimal bdAddedSIAmt) {
		this.bdAddedSIAmt = bdAddedSIAmt;
	}//end of setAddedSIAmt(BigDecimal bdAddedSIAmt)

	/** Retrieve the NoofFamiliesAdded
	 * @return the intNoofFamiliesAdded
	 */
	public Integer getNoofFamiliesAdded() {
		return intNoofFamiliesAdded;
	}//end of getNoofFamiliesAdded()

	/** Sets the NoofFamiliesAdded
	 * @param intNoofFamiliesAdded the intNoofFamiliesAdded to set
	 */
	public void setNoofFamiliesAdded(Integer intNoofFamiliesAdded) {
		this.intNoofFamiliesAdded = intNoofFamiliesAdded;
	}//end of setNoofFamiliesAdded(Integer intNoofFamiliesAdded)

	/** Retrieve the DOBOChangeYN.
	 * @return Returns the strDOBOChangeYN.
	 */
	public String getDOBOChangeYN() {
		return strDOBOChangeYN;
	}//end of getDOBOChangeYN()

	/** Sets the DOBOChangeYN.
	 * @param strDOBOChangeYN The strDOBOChangeYN to set.
	 */
	public void setDOBOChangeYN(String strDOBOChangeYN) {
		this.strDOBOChangeYN = strDOBOChangeYN;
	}//end of setDOBOChangeYN(String strDOBOChangeYN)

	/** Retrieve the BufferRecYN.
	 * @return Returns the strBufferRecYN.
	 */
	public String getBufferRecYN() {
		return strBufferRecYN;
	}//end of getBufferRecYN()

	/** Sets the BufferRecYN.
	 * @param strBufferRecYN The strBufferRecYN to set.
	 */
	public void setBufferRecYN(String strBufferRecYN) {
		this.strBufferRecYN = strBufferRecYN;
	}//end of setBufferRecYN(String strBufferRecYN)
	 public String getBrokerRecYN() {
			return strBrokerRecYN;
		}

		public void setBrokerRecYN(String strBrokerRecYN) {
			this.strBrokerRecYN = strBrokerRecYN;
		}
	/** Retrieve the TenureDays.
	 * @return Returns the intTenureDays.
	 */
	public Integer getTenureDays() {
		return intTenureDays;
	}//end of getTenureDays()

	/** Sets the TenureDays.
	 * @param intTenureDays The intTenureDays to set.
	 */
	public void setTenureDays(Integer intTenureDays) {
		this.intTenureDays = intTenureDays;
	}//end of setTenureDays(Integer intTenureDays)

	/** Retrieve the NoofPhotosRcvd.
	 * @return Returns the intNoofPhotosRcvd.
	 */
	public Integer getNoofPhotosRcvd() {
		return intNoofPhotosRcvd;
	}//end of getNoofPhotosRcvd()

	/** Sets the NoofPhotosRcvd.
	 * @param intNoofPhotosRcvd The intNoofPhotosRcvd to set.
	 */
	public void setNoofPhotosRcvd(Integer intNoofPhotosRcvd) {
		this.intNoofPhotosRcvd = intNoofPhotosRcvd;
	}//end of setNoofPhotosRcvd(Integer intNoofPhotosRcvd)

	/** Retrieve the DomicilaryTypeID.
	 * @return Returns the strDomicilaryTypeID.
	 */
	public String getDomicilaryTypeID() {
		return strDomicilaryTypeID;
	}//end of getDomicilaryTypeID()

	/** Sets the DomicilaryTypeID.
	 * @param strDomicilaryTypeID The strDomicilaryTypeID to set.
	 */
	public void setDomicilaryTypeID(String strDomicilaryTypeID) {
		this.strDomicilaryTypeID = strDomicilaryTypeID;
	}//end of setDomicilaryTypeID(String strDomicilaryTypeID)

	/** Retrieve the PolicyRemarks.
	 * @return Returns the strPolicyRemarks.
	 */
	public String getPolicyRemarks() {
		return strPolicyRemarks;
	}//end of getPolicyRemarks()

	/** Sets the PolicyRemarks.
	 * @param strPolicyRemarks The strPolicyRemarks to set.
	 */
	public void setPolicyRemarks(String strPolicyRemarks) {
		this.strPolicyRemarks = strPolicyRemarks;
	}//end of setPolicyRemarks(String strPolicyRemarks)

	/** Retrieve the Previous Scheme Name.
	 * @return Returns the strPrevSchemeName.
	 */
	public String getPrevSchemeName() {
		return strPrevSchemeName;
	}//end of getPrevSchemeName()

	/** Sets the Previous Scheme Name.
	 * @param strPrevSchemeName The strPrevSchemeName to set.
	 */
	public void setPrevSchemeName(String strPrevSchemeName) {
		this.strPrevSchemeName = strPrevSchemeName;
	}//end of setPrevSchemeName(String strPrevSchemeName)

	/** Retrieve the Scheme Name.
	 * @return Returns the strSchemeName.
	 */
	public String getSchemeName() {
		return strSchemeName;
	}//end of getSchemeName()

	/** Sets the Scheme Name.
	 * @param strSchemeName The strSchemeName to set.
	 */
	public void setSchemeName(String strSchemeName) {
		this.strSchemeName = strSchemeName;
	}//end of setSchemeName(String strSchemeName)

	/** Retrieve the EventName.
	 * @return Returns the strEventName.
	 */
	public String getEventName() {
		return strEventName;
	}//end of getEventName()

	/** Sets the EventName.
	 * @param strEventName The strEventName to set.
	 */
	public void setEventName(String strEventName) {
		this.strEventName = strEventName;
	}//end of setEventName(String strEventName)

	/** Retrieve the Tenure.
	 * @return Returns the lngTenure.
	 */
	public Long getTenure() {
		return lngTenure;
	}//end of getTenure()

	/** Sets the Tenure.
	 * @param lngTenure The lngTenure to set.
	 */
	public void setTenure(Long lngTenure) {
		this.lngTenure = lngTenure;
	}//end of setTenure(Long lngTenure)

	/** Retrieve the ProductList.
	 * @return Returns the alProductList.
	 */
	public ArrayList getProductList() {
		return alProductList;
	}//end of getProductList()

	/** Sets the ProductList.
	 * @param alProductList The alProductList to set.
	 */
	public void setProductList(ArrayList alProductList) {
		this.alProductList = alProductList;
	}//end of setProductList(ArrayList alProductList)

	/** Retrieve the Endorse Type Description.
	 * @return Returns the strEndorseTypeDesc.
	 */
	public String getEndorseTypeDesc() {
		return strEndorseTypeDesc;
	}//end of getEndorseTypeDesc()

	/** Sets the Endorse Type Description.
	 * @param strEndorseTypeDesc The strEndorseTypeDesc to set.
	 */
	public void setEndorseTypeDesc(String strEndorseTypeDesc) {
		this.strEndorseTypeDesc = strEndorseTypeDesc;
	}//end of setEndorseTypeDesc(String strEndorseTypeDesc)

	/** Retrieve the PhotoPresentYN.
	 * @return Returns the strPhotoPresentYN.
	 */
	public String getPhotoPresentYN() {
		return strPhotoPresentYN;
	}//end of getPhotoPresentYN()

	/** Sets the PhotoPresentYN.
	 * @param strPhotoPresentYN The strPhotoPresentYN to set.
	 */
	public void setPhotoPresentYN(String strPhotoPresentYN) {
		this.strPhotoPresentYN = strPhotoPresentYN;
	}//end of setPhotoPresentYN(String strPhotoPresentYN)

	/** Retrieve the PolicyNotLegibleYN.
	 * @return Returns the strPolicyNotLegibleYN.
	 */
	public String getPolicyNotLegibleYN() {
		return strPolicyNotLegibleYN;
	}//end of getPolicyNotLegibleYN()

	/** Sets the PolicyNotLegibleYN.
	 * @param strPolicyNotLegibleYN The strPolicyNotLegibleYN to set.
	 */
	public void setPolicyNotLegibleYN(String strPolicyNotLegibleYN) {
		this.strPolicyNotLegibleYN = strPolicyNotLegibleYN;
	}//end of setPolicyNotLegibleYN(String strPolicyNotLegibleYN)

	/** Retrieve the Bank Account Name.
	 * @return Returns the strBankAccountName.
	 */
	public String getBankAccountName() {
		return strBankAccountName;
	}//end of getBankAccountName()

	/** Sets the Bank Account Name.
	 * @param strBankAccountName The strBankAccountName to set.
	 */
	public void setBankAccountName(String strBankAccountName) {
		this.strBankAccountName = strBankAccountName;
	}//end of setBankAccountName(String strBankAccountName)

	/** Retrieve the Allocated Amount.
	 * @return Returns the bdAllocatedAmt.
	 */
	public BigDecimal getAllocatedAmt() {
		return bdAllocatedAmt;
	}//end of getAllocatedAmt()

	/** Sets the Allocated Amount.
	 * @param bdAllocatedAmt The bdAllocatedAmt to set.
	 */
	public void setAllocatedAmt(BigDecimal bdAllocatedAmt) {
		this.bdAllocatedAmt = bdAllocatedAmt;
	}//end of setAllocatedAmt(BigDecimal bdAllocatedAmt)

	/** Retrieve the Total Buffer Amount.
	 * @return Returns the bdTotalBufferAmt.
	 */
	public BigDecimal getTotalBufferAmt() {
		return bdTotalBufferAmt;
	}//end of getTotalBufferAmt()

	/** Sets the Total Buffer Amount.
	 * @param bdTotalBufferAmt The bdTotalBufferAmt to set.
	 */
	public void setTotalBufferAmt(BigDecimal bdTotalBufferAmt) {
		this.bdTotalBufferAmt = bdTotalBufferAmt;
	}//end of setTotalBufferAmt(BigDecimal bdTotalBufferAmt)

	/** Retrieve the Administering Authority Type ID.
	 * @return Returns the strAdmnAuthorityTypeID.
	 */
	public String getAdmnAuthorityTypeID() {
		return strAdmnAuthorityTypeID;
	}//end of getAdmnAuthorityTypeID()

	/** Sets the Administering Authority Type ID.
	 * @param strAdmnAuthorityTypeID The strAdmnAuthorityTypeID to set.
	 */
	public void setAdmnAuthorityTypeID(String strAdmnAuthorityTypeID) {
		this.strAdmnAuthorityTypeID = strAdmnAuthorityTypeID;
	}//end of setAdmnAuthorityTypeID(String strAdmnAuthorityTypeID)

	/** Retrieve the Allocated Type ID.
	 * @return Returns the strAllocatedTypeID.
	 */
	public String getAllocatedTypeID() {
		return strAllocatedTypeID;
	}//end of getAllocatedTypeID()

	/** Sets the Allocated Type ID.
	 * @param strAllocatedTypeID The strAllocatedTypeID to set.
	 */
	public void setAllocatedTypeID(String strAllocatedTypeID) {
		this.strAllocatedTypeID = strAllocatedTypeID;
	}//end of setAllocatedTypeID(String strAllocatedTypeID)

	/** Retrieve the Buffer Allowed YN.
	 * @return Returns the strBufferAllowedYN.
	 */
	public String getBufferAllowedYN() {
		return strBufferAllowedYN;
	}//end of getBufferAllowedYN() 

	/** Sets the Buffer Allowed YN.
	 * @param strBufferAllowedYN The strBufferAllowedYN to set.
	 */
	public void setBufferAllowedYN(String strBufferAllowedYN) {
		this.strBufferAllowedYN = strBufferAllowedYN;
	}//end of setBufferAllowedYN(String strBufferAllowedYN)

	/** Retrieve the DMS Ref ID.
	 * @return Returns the strDMSRefID.
	 */
	public String getDMSRefID() {
		return strDMSRefID;
	}//end of getDMSRefID()

	/** Sets the DMS Ref ID.
	 * @param strDMSRefID The strDMSRefID to set.
	 */
	public void setDMSRefID(String strDMSRefID) {
		this.strDMSRefID = strDMSRefID;
	}//end of setDMSRefID(String strDMSRefID)

	/** This method returns the address.
     * @return Returns the address.
     */
    public MemberAddressVO getAddress() {
        return address;
    }//end of getAddress()
    
    /** This method sets the address.
     * @param address The address to set.
     */
    public void setAddress(MemberAddressVO address) {
        this.address = address;
    }//end of setAddress(MemberAddressVO address)
    
    /** This method returns the Total Premium.
     * @return Returns the bdTotalPremium.
     */
    public BigDecimal getTotalPremium() {
        return bdTotalPremium;
    }//end of getTotalPremium()
    
    /** This method sets the Total Premium.
     * @param bdTotalPremium The bdTotalPremium to set.
     */
    public void setTotalPremium(BigDecimal bdTotalPremium) {
        this.bdTotalPremium = bdTotalPremium;
    }//end of setTotalPremium(BigDecimal bdTotalPremium)
    
    /** This method returns the Total Premium.
     * @return Returns the bdCorpStopLoss.
     */
    public BigDecimal getCorpStopLoss() {
        return bdCorpStopLoss;
    }//end of getTotalPremium()
    
    /** This method sets the Total Premium.
     * @param bdCorpStopLoss The bdCorpStopLoss to set.
     */
    public void setCorpStopLoss(BigDecimal bdCorpStopLoss) {
        this.bdCorpStopLoss = bdCorpStopLoss;
    }//end of setCorpStopLoss(BigDecimal bdCorpStopLoss)
    
    
    
    /** This method returns the Total Sum Insured.
     * @return Returns the bdTotalSumInsured.
     */
    public BigDecimal getTotalSumInsured() {
        return bdTotalSumInsured;
    }//end of getTotalSumInsured()
    
    /** This method sets the Total Sum Insured.
     * @param bdTotalSumInsured The bdTotalSumInsured to set.
     */
    public void setTotalSumInsured(BigDecimal bdTotalSumInsured) {
        this.bdTotalSumInsured = bdTotalSumInsured;
    }//end of setTotalSumInsured(BigDecimal bdTotalSumInsured)
    
    /** This method returns the Declaration Date.
     * @return Returns the dtDeclarationDate.
     */
    public Date getDeclarationDate() {
        return dtDeclarationDate;
    }//end of getDeclarationDate()
    
    /** This method sets the Declaration Date.
     * @param dtDeclarationDate The dtDeclarationDate to set.
     */
    public void setDeclarationDate(Date dtDeclarationDate) {
        this.dtDeclarationDate = dtDeclarationDate;
    }//end of setDeclarationDate(Date dtDeclarationDate)
    
    /** This method returns the End Date. 
     * @return Returns the dtEndDate.
     */
    public Date getEndDate() {
        return dtEndDate;
    }//end of getEndDate()
    
    /** This method sets the End Date. 
     * @param dtEndDate The dtEndDate to set.
     */
    public void setEndDate(Date dtEndDate) {
        this.dtEndDate = dtEndDate;
    }//end of setEndDate(Date dtEndDate)
    
    /** This method returns the Issue Date. 
     * @return Returns the dtIssueDate.
     */
    public Date getIssueDate() {
        return dtIssueDate;
    }//end of getIssueDate()
    
    /** This method sets the Issue Date. 
     * @param dtIssueDate The dtIssueDate to set.
     */
    public void setIssueDate(Date dtIssueDate) {
        this.dtIssueDate = dtIssueDate;
    }//end of setIssueDate(Date dtIssueDate)
    
    /** This method returns the Recd Date. 
     * @return Returns the dtRecdDate.
     */
    public Date getRecdDate() {
        return dtRecdDate;
    }//end of getRecdDate()
    
    /** This method sets the Recd Date.
     * @param dtRecdDate The dtRecdDate to set.
     */
    public void setRecdDate(Date dtRecdDate) {
        this.dtRecdDate = dtRecdDate;
    }//end of setRecdDate(Date dtRecdDate)
    
    /** This method returns the Start Date.
     * @return Returns the dtStartDate.
     */
    public Date getStartDate() {
        return dtStartDate;
    }//end of getStartDate()
    
    /** This method sets the Start Date.
     * @param dtStartDate The dtStartDate to set.
     */
    public void setStartDate(Date dtStartDate) {
        this.dtStartDate = dtStartDate;
    }//end of setStartDate(Date dtStartDate)
    
    /** This method returns the Card Clearance.
     * @return Returns the intCardClearance.
     */
    public Integer getCardClearance() {
        return intCardClearance;
    }//end of getCardClearance()
    
    /** This method sets the Card Clearance.
     * @param intCardClearance The intCardClearance to set.
     */
    public void setCardClearance(Integer intCardClearance) {
        this.intCardClearance = intCardClearance;
    }//end of setCardClearance(Integer intCardClearance)
    
    /** This method returns the Claim Clearance.
     * @return Returns the intClaimClearance.
     */
    public Integer getClaimClearance() {
        return intClaimClearance;
    }//end of getClaimClearance()
    
    /** This method sets the Claim Clearance.
     * @param intClaimClearance The intClaimClearance to set.
     */
    public void setClaimClearance(Integer intClaimClearance) {
        this.intClaimClearance = intClaimClearance;
    }//end of setClaimClearance(Integer intClaimClearance)
    
    /** This method returns the Group Registration Sequence ID.
     * @return Returns the lngGroupRegnSeqID.
     */
    public Long getGroupRegnSeqID() {
        return lngGroupRegnSeqID;
    }//end of getGroupRegnSeqID()
    
    /** This method sets the Group Registration Sequence ID.
     * @param lngGroupRegnSeqID The lngGroupRegnSeqID to set.
     */
    public void setGroupRegnSeqID(Long lngGroupRegnSeqID) {
        this.lngGroupRegnSeqID = lngGroupRegnSeqID;
    }//end of setGroupRegnSeqID(Long lngGroupRegnSeqID)
    
    /** This method returns the Office Sequence ID
     * @return Returns the lngOfficeSeqID.
     */
    public Long getOfficeSeqID() {
        return lngOfficeSeqID;
    }//end of getOfficeSeqID()
    
    /** This method sets the Office Sequence ID.
     * @param lngOfficeSeqID The lngOfficeSeqID to set.
     */
    public void setOfficeSeqID(Long lngOfficeSeqID) {
        this.lngOfficeSeqID = lngOfficeSeqID;
    }//end of setOfficeSeqID(Long lngOfficeSeqID)
    
    /** This method returns the PAN Number.
     * @return Returns the strPANNbr.
     */
    public String getPANNbr() {
        return strPANNbr;
    }//end of getPANNbr()
    
    /** This method sets the PAN Number.
     * @param strPANNbr The strPANNbr to set.
     */
    public void setPANNbr(String strPANNbr) {
        this.strPANNbr = strPANNbr;
    }//end of setPANNbr(String strPANNbr)
    
    /** This method returns the Product Sequence ID.
     * @return Returns the lngProductSeqID.
     */
    public Long getProductSeqID() {
        return lngProductSeqID;
    }//end of getProductSeqID()
    
    /** This method sets the Product Sequence ID.
     * @param lngProductSeqID The lngProductSeqID to set.
     */
    public void setProductSeqID(Long lngProductSeqID) {
        this.lngProductSeqID = lngProductSeqID;
    }//end of setProductSeqID(Long lngProductSeqID)
    
    /** This method returns the Bank Account Number.
     * @return Returns the strBankAccNbr.
     */
    public String getBankAccNbr() {
        return strBankAccNbr;
    }//end of getBankAccNbr()
    
    /** This method sets the Bank Account Number.
     * @param strBankAccNbr The strBankAccNbr to set.
     */
    public void setBankAccNbr(String strBankAccNbr) {
        this.strBankAccNbr = strBankAccNbr;
    }//end of setBankAccNbr(String strBankAccNbr)
    
    /** This method returns the Beneficiary Name.
     * @return Returns the strBeneficiaryName.
     */
    public String getBeneficiaryName() {
        return strBeneficiaryName;
    }//end of getBeneficiaryName()
    
    /** This method sets the Beneficiary Name.
     * @param strBeneficiaryName The strBeneficiaryName to set.
     */
    public void setBeneficiaryName(String strBeneficiaryName) {
        this.strBeneficiaryName = strBeneficiaryName;
    }//end of setBeneficiaryName(String strBeneficiaryName)
    
    /** This method returns the CardPhotoYN.
     * @return Returns the strCardPhotoYN.
     */
    public String getCardPhotoYN() {
        return strCardPhotoYN;
    }//end of getCardPhotoYN()
    
    /** This method sets the CardPhotoYN.
     * @param strCardPhotoYN The strCardPhotoYN to set.
     */
    public void setCardPhotoYN(String strCardPhotoYN) {
        this.strCardPhotoYN = strCardPhotoYN;
    }//end of setCardPhotoYN(String strCardPhotoYN)
    
    /** This method returns the CondonedYN.
     * @return Returns the strCondonedYN.
     */
    public String getCondonedYN() {
        return strCondonedYN;
    }//end of getCondonedYN()
    
    /** This method sets the CondonedYN.
     * @param strCondonedYN The strCondonedYN to set.
     */
    public void setCondonedYN(String strCondonedYN) {
        this.strCondonedYN = strCondonedYN;
    }//end of setCondonedYN(String strCondonedYN)
    
    /** This method returns the Policy Holder Name.
     * @return Returns the strPolicyHolderName.
     */
    public String getPolicyHolderName() {
        return strPolicyHolderName;
    }//end of getPolicyHolderName()
    
    /** This method sets the Policy Holder Name.
     * @param strPolicyHolderName The strPolicyHolderName to set.
     */
    public void setPolicyHolderName(String strPolicyHolderName) {
        this.strPolicyHolderName = strPolicyHolderName;
    }//end of setPolicyHolderName(String strPolicyHolderName)
    
    /** This method returns the Policy Status ID.
     * @return Returns the strPolicyStatusID.
     */
    public String getPolicyStatusID() {
        return strPolicyStatusID;
    }//end of getPolicyStatusID()
    
    /** This method sets the Policy Status ID.
     * @param strPolicyStatusID The strPolicyStatusID to set.
     */
    public void setPolicyStatusID(String strPolicyStatusID) {
        this.strPolicyStatusID = strPolicyStatusID;
    } //end of setPolicyStatusID(String strPolicyStatusID)
    
    /** This method returns the ProposalFormYN.
     * @return Returns the strProposalFormYN.
     */
    public String getProposalFormYN() {
        return strProposalFormYN;
    }//end of getProposalFormYN()
    
    /** This method sets the ProposalFormYN.
     * @param strProposalFormYN The strProposalFormYN to set.
     */
    public void setProposalFormYN(String strProposalFormYN) {
        this.strProposalFormYN = strProposalFormYN;
    }//end of setProposalFormYN(String strProposalFormYN)
    
    /** This method returns the ProposalYN.
     * @return Returns the strProposalYN.
     */
    public String getProposalYN() {
        return strProposalYN;
    }//end of getProposalYN()
    
    /** This method sets the ProposalYN.
     * @param strProposalYN The strProposalYN to set.
     */
    public void setProposalYN(String strProposalYN) {
        this.strProposalYN = strProposalYN;
    }//end of setProposalYN(String strProposalYN)
    
    /** This method returns the RelationTypeID.
     * @return Returns the strRelationTypeID.
     */
    public String getRelationTypeID() {
        return strRelationTypeID;
    }//end of getRelationTypeID()
    
    /** This method sets the RelationTypeID.
     * @param strRelationTypeID The strRelationTypeID to set.
     */
    public void setRelationTypeID(String strRelationTypeID) {
        this.strRelationTypeID = strRelationTypeID;
    }//end of setRelationTypeID(String strRelationTypeID)
    
    /** This method returns the Remarks.
     * @return Returns the strRemarks.
     */
    public String getRemarks() {
        return strRemarks;
    }//end of getRemarks()
    
    /** This method sets the Remarks.
     * @param strRemarks The strRemarks to set.
     */
    public void setRemarks(String strRemarks) {
        this.strRemarks = strRemarks;
    }//end of setRemarks(String strRemarks)
    
    /** This method returns the SubTypeID.
     * @return Returns the strSubTypeID.
     */
    public String getSubTypeID() {
        return strSubTypeID;
    }//end of getSubTypeID()
    
    /** This method sets the SubTypeID.
     * @param strSubTypeID The strSubTypeID to set.
     */
    public void setSubTypeID(String strSubTypeID) {
        this.strSubTypeID = strSubTypeID;
    }//end of setSubTypeID(String strSubTypeID)
    
    /** This method returns the TPA Status General Type ID.
     * @return Returns the strTPAStatusTypeID.
     */
    public String getTPAStatusTypeID() {
        return strTPAStatusTypeID;
    }//end of getTPAStatusTypeID()
    
    /** This method sets the TPA Status General Type ID.
     * @param strTPAStatusTypeID The strTPAStatusTypeID to set.
     */
    public void setTPAStatusTypeID(String strTPAStatusTypeID) {
        this.strTPAStatusTypeID = strTPAStatusTypeID;
    }//end of setTPAStatusTypeID(String strTPAStatusTypeID)
    
    /** This method returns the INSStatusGeneralTypeID.
     * @return Returns the strINSStatusTypeID.
     */
    public String getINSStatusTypeID() {
        return strINSStatusTypeID;
    }//end of getINSStatusTypeID()
    
    /** This method sets the INSStatusGeneralTypeID. 
     * @param strINSStatusTypeID The strINSStatusTypeID to set.
     */
    public void setINSStatusTypeID(String strINSStatusTypeID) {
        this.strINSStatusTypeID = strINSStatusTypeID;
    }//end of setINSStatusTypeID(String strINSStatusTypeID)
    
    /** This method returns the Employee Count.
     * @return Returns the lngEmployeeCnt.
     */
    public Long getEmployeeCnt() {
        return lngEmployeeCnt;
    }//end of getEmployeeCnt()
    
    /** This method sets the Employee Count.
     * @param lngEmployeeCnt The lngEmployeeCnt to set.
     */
    public void setEmployeeCnt(Long lngEmployeeCnt) {
        this.lngEmployeeCnt = lngEmployeeCnt;
    }//end of setEmployeeCnt(Long lngEmployeeCnt)
    
    /** This method returns the Total Member Count. 
     * @return Returns the lngMemberCnt.
     */
    public Long getMemberCnt() {
        return lngMemberCnt;
    }//end of getMemberCnt()
    
    /** This method sets the Total Member Count.
     * @param lngMemberCnt The lngMemberCnt to set.
     */
    public void setMemberCnt(Long lngMemberCnt) {
        this.lngMemberCnt = lngMemberCnt;
    }//end of setMemberCnt(Long lngMemberCnt)
    
    /** This method returns the TPA Cheque Issued General Type.
     * @return Returns the strIssueChqTypeID.
     */
    public String getIssueChqTypeID() {
        return strIssueChqTypeID;
    }//end of getIssueChqTypeID()
    
    /** This method sets the TPA Cheque Issued General Type.
     * @param strIssueChqTypeID The strIssueChqTypeID to set.
     */
    public void setIssueChqTypeID(String strIssueChqTypeID) {
        this.strIssueChqTypeID = strIssueChqTypeID;
    }//end of setIssueChqTypeID(String strIssueChqTypeID)

	/** This method returns the Renewal policy yes or no.
	 * @return Returns the strRenewalPolicyYn.
	 */
	public String getRenewalPolicyYn() {
		return strRenewalPolicyYn;
	}// End of getRenewalPolicyYn()

	/** This method sets the Renewal Policy Yes or no.
	 * @param strRenewalPolicyYn The strRenewalPolicyYn to set.
	 */
	public void setRenewalPolicyYn(String strRenewalPolicyYn) {
		this.strRenewalPolicyYn = strRenewalPolicyYn;
	}// End of setRenewalPolicyYn(String strRenewalPolicyYn)

	/** This method returns the Enrollment is Based on Employee number or not. 
	 * @return Returns the strEnrollOnEmployeeNbr.
	 */
	public String getEnrollOnEmployeeNbr() {
		return strEnrollOnEmployeeNbr;
	}// end of getEnrollOnEmployeeNbr()

	/** This method sets the Enrollment is Based on Employee number or not. 
	 * @param strEnrollOnEmployeeNbr The strEnrollOnEmployeeNbr to set.
	 */
	public void setEnrollOnEmployeeNbr(String strEnrollOnEmployeeNbr) {
		this.strEnrollOnEmployeeNbr = strEnrollOnEmployeeNbr;
	}//end of setEnrollOnEmployeeNbr(String strEnrollOnEmployeeNbr)

	/** This method returns the Card Dispatch Type ID.
	 * @return Returns the strCardDispatchID.
	 */
	public String getCardDispatchID() {
		return strCardDispatchID;
	}//End of getCardDispatchID()

	/** This method sets the Card Dispatch Type ID.
	 * @param strCardDispatchID The strCardDispatchID to set.
	 */
	public void setCardDispatchID(String strCardDispatchID) {
		this.strCardDispatchID = strCardDispatchID;
	}// end of setCardDispatchID(String strCardDispatchID

	/** This method returns the Insurance Sequence ID.
	 * @return Returns the lngInsuranceSeqID.
	 */
	public Long getInsuranceSeqID() {
		return lngInsuranceSeqID;
	}// Edn of getInsuranceSeqID()

	/** This method sets the Insurance Sequence ID.  
	 * @param lngInsuranceSeqID The lngInsuranceSeqID to set.
	 */
	public void setInsuranceSeqID(Long lngInsuranceSeqID) {
		this.lngInsuranceSeqID = lngInsuranceSeqID;
	}// End of setInsuranceSeqID(Long lngInsuranceSeqID) 
	
    /** This method returns the Review Count.
     * @return Returns the intReviewCount.
     */
    public Integer getReviewCount() {
        return intReviewCount;
    }//end of getReviewCount()
    
    /** This method sets the Review Count.
     * @param intReviewCount The intReviewCount to set.
     */
    public void setReviewCount(Integer intReviewCount) {
        this.intReviewCount = intReviewCount;
    }//end of setReviewCount(String strReviewCount)
    
    /** This method returns the Required Review Count.
     * @return Returns the intRequiredReviewCnt.
     */
    public Integer getRequiredReviewCnt() {
        return intRequiredReviewCnt;
    }//end of getRequiredReviewCnt()
    
    /** This method sets the Required Review Count.
     * @param intRequiredReviewCnt The intRequiredReviewCnt to set.
     */
    public void setRequiredReviewCnt(Integer intRequiredReviewCnt) {
        this.intRequiredReviewCnt = intRequiredReviewCnt;
    }//end of setRequiredReviewCnt(Integer intRequiredReviewCnt)
    
    /** This method returns the Event Seq ID.
     * @return Returns the lngEventSeqID.
     */
    public Long getEventSeqID() {
        return lngEventSeqID;
    }//end of getEventSeqID()
    
    /** This method sets the Event Seq ID.
     * @param lngEventSeqID The lngEventSeqID to set.
     */
    public void setEventSeqID(Long lngEventSeqID) {
        this.lngEventSeqID = lngEventSeqID;
    }//end of setEventSeqID(Long lngEventSeqID)
    
    /** This method returns the Template ID.
     * @return Returns the lngTemplateID.
     */
    public Long getTemplateID() {
        return lngTemplateID;
    }//end of getTemplateID()
    
    /** This method sets the Template ID.
     * @param lngTemplateID The lngTemplateID to set.
     */
    public void setTemplateID(Long lngTemplateID) {
        this.lngTemplateID = lngTemplateID;
    }//end of setTemplateID(Long lngTemplateID)
    
    /** This method returns the Card Print YN.
     * @return Returns the strCardPrintYN.
     */
    public String getCardPrintYN() {
        return strCardPrintYN;
    }//end of getCardPrintYN()
    
    /** This method sets the Card Print YN.
     * @param strCardPrintYN The strCardPrintYN to set.
     */
    public void setCardPrintYN(String strCardPrintYN) {
        this.strCardPrintYN = strCardPrintYN;
    }//end of setCardPrintYN(String strCardPrintYN)
    
    /** This method returns the INS Scheme.
     * @return Returns the strInsScheme.
     */
    public String getInsScheme() {
        return strInsScheme;
    }//end of getInsScheme()
    
    /** This method sets the INS Scheme.
     * @param strInsScheme The strInsScheme to set.
     */
    public void setInsScheme(String strInsScheme) {
        this.strInsScheme = strInsScheme;
    }//end of setInsScheme(String strInsScheme)
    
    /** This method returns the Review.
     * @return Returns the strReview.
     */
    public String getReview() {
        return strReview;
    }//end of getReview()
    
    /** This method sets the Review.
     * @param strReview The strReview to set.
     */
    public void setReview(String strReview) {
        this.strReview = strReview;
    }//end of setReview(String strReview)
    
    /** This method returns the Insert Mode.
     * @return Returns the strInsertMode.
     */
    public String getInsertMode() {
        return strInsertMode;
    }//end of getInsertMode()
    
    /** This method sets the Insert Mode.
     * @param strInsertMode The strInsertMode to set.
     */
    public void setInsertMode(String strInsertMode) {
        this.strInsertMode = strInsertMode;
    }//end of setInsertMode(String strInsertMode)
    
    /** This method returns the Premium Wording.
     * @return Returns the strPremiumWording.
     */
    public String getPremiumWording() {
        return strPremiumWording;
    }//end of getPremiumWording()
    
    /** This method sets the Premium Wording.
     * @param strPremiumWording The strPremiumWording to set.
     */
    public void setPremiumWording(String strPremiumWording) {
        this.strPremiumWording = strPremiumWording;
    }//end of setPremiumWording(String strPremiumWording)
    
    /** This method returns the Sum Wording. 
     * @return Returns the strSumWording.
     */
    public String getSumWording() {
        return strSumWording;
    }//end of getSumWording()
    
    /** This method sets the Sum Wording.
     * @param strSumWording The strSumWording to set.
     */
    public void setSumWording(String strSumWording) {
        this.strSumWording = strSumWording;
    }//end of setSumWording(String strSumWording)
    
    /** This method returns the Bank Name.
     * @return Returns the strBankName.
     */
    public String getBankName() {
        return strBankName;
    }//end of getBankName()
    
    /** This method sets the Bank Name.
     * @param strBankName The strBankName to set.
     */
    public void setBankName(String strBankName) {
        this.strBankName = strBankName;
    }//end of setBankName(String strBankName)
    
    /** This method returns the Bank Phone.
     * @return Returns the strBankPhone.
     */
    public String getBankPhone() {
        return strBankPhone;
    }//end of getBankPhone()
    
    /** This method sets the Bank Phone.
     * @param strBankPhone The strBankPhone to set.
     */
    public void setBankPhone(String strBankPhone) {
        this.strBankPhone = strBankPhone;
    }//end of setBankPhone(String strBankPhone)
    
    /** This method returns the Bank Branch.
     * @return Returns the strBranch.
     */
    public String getBranch() {
        return strBranch;
    }//end of getBranch()
    
    /** This method sets the Bank Branch.
     * @param strBranch The strBranch to set.
     */
    public void setBranch(String strBranch) {
        this.strBranch = strBranch;
    }//end of setBranch(String strBranch)
    
    /** This method returns the MICR Code.
     * @return Returns the strMICRCode.
     */
    public String getMICRCode() {
        return strMICRCode;
    }//end of getMICRCode()
    
    /** This method sets the MICR Code.
     * @param strMICRCode The strMICRCode to set.
     */
    public void setMICRCode(String strMICRCode) {
        this.strMICRCode = strMICRCode;
    }//end of setMICRCode(String strMICRCode)
    
    /** This method returns the Bank Seq ID.
     * @return Returns the lngBankSeqID.
     */
    public Long getBankSeqID() {
        return lngBankSeqID;
    }//end of getBankSeqID()
    
    /** This method sets the bank Seq ID.
     * @param lngBankSeqID The lngBankSeqID to set.
     */
    public void setBankSeqID(Long lngBankSeqID) {
        this.lngBankSeqID = lngBankSeqID;
    }//end of setBankSeqID(Long lngBankSeqID)
    
    /** This method returns the Pre-Auth Clearance.
     * @return Returns the intPreAuthClearance.
     */
    public Integer getPreAuthClearance() {
        return intPreAuthClearance;
    }//end of getPreAuthClearance()
    
    /** This method sets the Pre-Auth Clearance.
     * @param intPreAuthClearance The intPreAuthClearance to set.
     */
    public void setPreAuthClearance(Integer intPreAuthClearance) {
        this.intPreAuthClearance = intPreAuthClearance;
    }//end of setPreAuthClearance(Integer intPreAuthClearance)
    
    /**
     * This method returns the Clarification Type ID.
     * @return Returns the strClarificationTypeID.
     */
    public String getClarificationTypeID() {
        return strClarificationTypeID;
    }//end of getClarificationTypeID()
    /**
     * This method sets the Clarification Type ID.
     * @param strClarificationTypeID The strClarificationTypeID to set.
     */
    public void setClarificationTypeID(String strClarificationTypeID) {
        this.strClarificationTypeID = strClarificationTypeID;
    }//end of setClarificationTypeID(String strClarificationTypeID)
    
    /**
     * This method returns the Clarified Date.
     * @return Returns the dtClarifiedDate.
     */
    public Date getClarifiedDate() {
        return dtClarifiedDate;
    }//end of getClarifiedDate()
    /**
     * This method sets the Clarified Date.
     * @param dtClarifiedDate The dtClarifiedDate to set.
     */
    public void setClarifiedDate(Date dtClarifiedDate) {
        this.dtClarifiedDate = dtClarifiedDate;
    }//end of setClarifiedDate(Date dtClarifiedDate)

	/** This method returns the Insurance Status.
	 * @return Returns the strINSStatuts.
	 */
	public String getINSStatus() {
		return strINSStatus;
	}//End of getINSStatus()

	/** This method sets the Insurance Status.
	 * @param strINSStatus The strINSStatus to set.
	 */
	public void setINSStatus(String strINSStatus) {
		this.strINSStatus = strINSStatus;
	}//End of setINSStatuts(String strINSStatuts)

	/** This method returns the Policy Sub Type.
	 * @return Returns the strPolicySubType.
	 */
	public String getPolicySubType() {
		return strPolicySubType;
	}//End of getPolicySubType()

	/** This method sets the Policy Sub Tpe.
	 * @param strPolicySubType The strPolicySubType to set.
	 */
	public void setPolicySubType(String strPolicySubType) {
		this.strPolicySubType = strPolicySubType;
	}//End of setPolicySubType(String strPolicySubType)

	/** This method returns the Product Name.
	 * @return Returns the strProductName.
	 */
	public String getProductName() {
		return strProductName;
	}// End of getProductName()

	/** This method sets the Product Name.
	 * @param strProductName The strProductName to set.
	 */
	public void setProductName(String strProductName) {
		this.strProductName = strProductName;
	}//End of setProductName(String strProductName)

	/** This method returns the Admin Status General Type ID.
	 * @return Returns the strAdminStatusID.
	 */
	public String getAdminStatusID() {
		return strAdminStatusID;
	}// End of getAdminStatusID()

	/** This method sets the Admin Status General Type ID.
	 * @param strAdminStatusID The strAdminStatusID to set.
	 */
	public void setAdminStatusID(String strAdminStatusID) {
		this.strAdminStatusID = strAdminStatusID;
	}// End of setAdminStatusID(String strAdminStatusID)

    /** This method returns the Group Branch Seq ID.
     * @return Returns the lngGroupBranchSeqID.
     */
    public Long getGroupBranchSeqID() {
        return lngGroupBranchSeqID;
    }//end of getGroupBranchSeqID()
    
    /** This method sets the Group Branch Seq ID.
     * @param lngGroupBranchSeqID The lngGroupBranchSeqID to set.
     */
    public void setGroupBranchSeqID(Long lngGroupBranchSeqID) {
        this.lngGroupBranchSeqID = lngGroupBranchSeqID;
    }//end of setGroupBranchSeqID(Long lngGroupBranchSeqID)
    
    /** This method returns the Policy Status Description.
     * @return Returns the strPolicyStatus.
     */
    public String getPolicyStatus() {
        return strPolicyStatus;
    }//end of getPolicyStatus()
    
    /** This method sets the Policy Status Description.
     * @param strPolicyStatus The strPolicyStatus to set.
     */
    public void setPolicyStatus(String strPolicyStatus) {
        this.strPolicyStatus = strPolicyStatus;
    }//end of setPolicyStatus(String strPolicyStatus)
    
	/** This method returns the Endorsement General Type ID.
	 * @return Returns the strEndorseGenTypeID.
	 */
	public String getEndorseGenTypeID() {
		return strEndorseGenTypeID;
	}//end of getEndorseGenTypeID()

	/** This method sets the Endorsement General Type ID.
	 * @param strEndorseGenTypeID The strEndorseGenTypeID to set.
	 */
	public void setEndorseGenTypeID(String strEndorseGenTypeID) {
		this.strEndorseGenTypeID = strEndorseGenTypeID;
	}//end of setEndorseGenTypeID(String strEndorseGenTypeID)

	/** Retrieve the DischargeVoucherMandatoryYN.
	 * @return Returns the strDischargeVoucherMandatoryYN.
	 */
	public String getDischargeVoucherMandatoryYN() {
		return strDischargeVoucherMandatoryYN;
	}//end of getDischargeVoucherMandatoryYN()

	/** Sets the DischargeVoucherMandatoryYN.
	 * @param strDischargeVoucherMandatoryYN The strDischargeVoucherMandatoryYN to set.
	 */
	public void setDischargeVoucherMandatoryYN(String strDischargeVoucherMandatoryYN) {
		this.strDischargeVoucherMandatoryYN = strDischargeVoucherMandatoryYN;
	}//end of setDischargeVoucherMandatoryYN(String strDischargeVoucherMandatoryYN)

	/** Retrieve the Insurance Endorsement YN.
	 * @return Returns the strInsuranceEndYN.
	 */
	public String getInsuranceEndYN() {
		return strInsuranceEndYN;
	}//end of getInsuranceEndYN()

	/** Sets the Insurance Endorsement YN.
	 * @param strInsuranceEndYN The strInsuranceEndYN to set.
	 */
	public void setInsuranceEndYN(String strInsuranceEndYN) {
		this.strInsuranceEndYN = strInsuranceEndYN;
	}//end of setInsuranceEndYN(String strInsuranceEndYN)

	/** Retrieve the ProductChangeYN.
	 * @return Returns the strProductChangeYN.
	 */
	public String getProductChangeYN() {
		return strProductChangeYN;
	}//end of getProductChangeYN()

	/** Sets the ProductChangeYN.
	 * @param strProductChangeYN The strProductChangeYN to set.
	 */
	public void setProductChangeYN(String strProductChangeYN) {
		this.strProductChangeYN = strProductChangeYN;
	}//end of setProductChangeYN(String strProductChangeYN)

	/** Retrieve the StopPreAuthsYN
     * @return strStopPreAuthsYN String StopPreAuthsYN
     */
    public String getStopPreAuthsYN() {
        return strStopPreAuthsYN;
    }//end of getStopPreAuthsYN()
    
    /** Sets the StopPreAuthsYN
     * @param strStopPreAuthsYN String StopPreAuthsYN
     */
    public void setStopPreAuthsYN(String strStopPreAuthsYN) {
        this.strStopPreAuthsYN = strStopPreAuthsYN;
    }//end of setStopPreAuthsYN(String strStopPreAuthsYN)
    
    /** Retrieve the StopClaimsYN
     * @return strStopClaimsYN String StopClaimsYN
     */
    public String getStopClaimsYN() {
        return strStopClaimsYN;
    }//end of getStopClaimsYN()
    
    /** Sets the StopClaimsYN
     * @param strStopClaimsYN String StopClaimsYN
     */
    public void setStopClaimsYN(String strStopClaimsYN) {
        this.strStopClaimsYN = strStopClaimsYN;
    }//end of setStopClaimsYN(String strStopClaimsYN)
	
	
	public Date getStartDate1() {
        return dtStartDate1;
    }//end of getStartDate()
    
    /** This method sets the Start Date.
     * @param dtStartDate The dtStartDate to set.
     */
    public void setStartDate1(Date dtStartDate1) {
        this.dtStartDate1 = dtStartDate1;
    }//end of setStartDate(Date dtStartDate)
	
	
	public Date getEndDate1() {
        return dtEndDate1;
    }//end of getEndDate()
    
    /** This method sets the End Date. 
     * @param dtEndDate The dtEndDate to set.
     */
    public void setEndDate1(Date dtEndDate1) {
        this.dtEndDate1 = dtEndDate1;
    }//end of setEndDate(Date dtEndDate)
    /** This method returns the Issue Date. 
     * @return Returns the dtIssueDate.
     */
    
	public Long getBrokerID() {
		return lngBrokerID;
	}

	public void setBrokerID(Long lngBrokerID) {
		this.lngBrokerID = lngBrokerID;
	}

	/**
	 * @return the strBrokerYN
	 */
	public String getBrokerYN() {
		return strBrokerYN;
	}

	/**
	 * @param strBrokerYN the strBrokerYN to set
	 */
	public void setBrokerYN(String strBrokerYN) {
		this.strBrokerYN = strBrokerYN;
	}
    //added for KOC-1273
    public String getCriticalBenefitYN() {
		return strCriticalBenefitYN;
	}

	public void setCriticalBenefitYN(String strCriticalBenefitYN) {
		this.strCriticalBenefitYN = strCriticalBenefitYN;
	}
	public String getSurvivalPeriodYN() {
		return strSurvivalPeriodYN;
	}

	public void setSurvivalPeriodYN(String strSurvivalPeriodYN) {
		this.strSurvivalPeriodYN = strSurvivalPeriodYN;
	}

	public String getCriBufferYN() {
		return strCriBufferYN;
	}

	public void setCriBufferYN(String strCriBufferYN) {
		this.strCriBufferYN = strCriBufferYN;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrencyFormat() {
		return currencyFormat;
	}

	public void setCurrencyFormat(String currencyFormat) {
		this.currencyFormat = currencyFormat;
	}

	public String getProductNetworkType() {
		return productNetworkType;
	}

	public void setProductNetworkType(String productNetworkType) {
		this.productNetworkType = productNetworkType;
	}

	public String getPolicyFileName() {
		return policyFileName;
	}

	public void setPolicyFileName(String policyFileName) {
		this.policyFileName = policyFileName;
	}

	public FormFile getPolicyFileCopy() {
		return policyFileCopy;
	}

	public void setPolicyFileCopy(FormFile policyFileCopy) {
		this.policyFileCopy = policyFileCopy;
	}

	public String getTariffType() {
		return tariffType;
	}

	public void setTariffType(String tariffType) {
		this.tariffType = tariffType;
	}
	public float getReInsShare() {
		return lngReInsShare;
	}

	public void setReInsShare(float lngReInsShare) {
		this.lngReInsShare = lngReInsShare;
	}

	public float getInsShare() {
		return lngInsShare;
	}

	public void setInsShare(float lngInsShare) {
		this.lngInsShare = lngInsShare;
	}

	public String getPi() {
		return pi;
	}

	public void setPi(String pi) {
		this.pi = pi;
	}

	public String getNonpi() {
		return nonpi;
	}

	public void setNonpi(String nonpi) {
		this.nonpi = nonpi;
	}

	public String getHealthAuthority() {
		return healthAuthority;
	}

	public void setHealthAuthority(String healthAuthority) {
		this.healthAuthority = healthAuthority;
	}
	public String getLimitCurrencyFormat() {
		return limitCurrencyFormat;
	}

	public void setLimitCurrencyFormat(String limitCurrencyFormat) {
		this.limitCurrencyFormat = limitCurrencyFormat;
	}
	public BigDecimal getMatPremium() {
		return matPremium;
	}

	public void setMatPremium(BigDecimal matPremium) {
		this.matPremium = matPremium;
	}

	public BigDecimal getExlMatPremium() {
		return exlMatPremium;
	}

	public void setExlMatPremium(BigDecimal exlMatPremium) {
		this.exlMatPremium = exlMatPremium;
	}

	public String getLogicType() {
		return logicType;
	}

	public void setLogicType(String logicType) {
		this.logicType = logicType;
	}

	public String getAdministrationCharges() {
		return administrationCharges;
	}

	public void setAdministrationCharges(String administrationCharges) {
		this.administrationCharges = administrationCharges;
	}

	public String getCreditGeneration() {
		return creditGeneration;
	}

	public void setCreditGeneration(String creditGeneration) {
		this.creditGeneration = creditGeneration;
	}

	public String getMaternityMaxBand() {
		return maternityMaxBand;
	}

	public void setMaternityMaxBand(String maternityMaxBand) {
		this.maternityMaxBand = maternityMaxBand;
	}

	public String getMaternityMinBand() {
		return maternityMinBand;
	}

	public void setMaternityMinBand(String maternityMinBand) {
		this.maternityMinBand = maternityMinBand;
	}

	public String getCreditGenerationOth() {
		return creditGenerationOth;
	}

	public void setCreditGenerationOth(String creditGenerationOth) {
		this.creditGenerationOth = creditGenerationOth;
	}



	public String getMemcountYN() {
		return memcountYN;
	}

	public void setMemcountYN(String memcountYN) {
		this.memcountYN = memcountYN;
	}


	

    public BigDecimal getLimitPerMember() {
        return bdLimitPerMember;
    }//end of getTotalSumInsured()
    

    public void setLimitPerMember(BigDecimal bdLimitPerMember) {
        this.bdLimitPerMember = bdLimitPerMember;
    }//end of setTotalSumInsured(BigDecimal bdTotalSumInsured)

	public String getLineOfBussiness() {
		return lineOfBussiness;
	}

	public void setLineOfBussiness(String lineOfBussiness) {
		this.lineOfBussiness = lineOfBussiness;
	}

	public String getMemberStatus() {
		return memberStatus;
	}

	public void setMemberStatus(String memberStatus) {
		this.memberStatus = memberStatus;
	}
	public String getOutpatientbenfTypeCond() {
		return outpatientbenfTypeCond;
	}

	public void setOutpatientbenfTypeCond(String outpatientbenfTypeCond) {
		this.outpatientbenfTypeCond = outpatientbenfTypeCond;
	}
	public Date getStopCashlessDate() {
		return stopCashlessDate;
	}

	public void setStopCashlessDate(Date stopCashlessDate) {
		this.stopCashlessDate = stopCashlessDate;
	}

	public Date getStopClaimsDate() {
		return stopClaimsDate;
	}

	public void setStopClaimsDate(Date stopClaimsDate) {
		this.stopClaimsDate = stopClaimsDate;
	}
	
	public String getMailNotificationYN() {
		return mailNotificationYN;
	}

	public void setMailNotificationYN(String mailNotificationYN) {
		this.mailNotificationYN = mailNotificationYN;
	}

	public String getMailNotificationhiddenYN() {
		return mailNotificationhiddenYN;
	}

	public void setMailNotificationhiddenYN(String mailNotificationhiddenYN) {
		this.mailNotificationhiddenYN = mailNotificationhiddenYN;
	}


	public String getNetworkSortNum() {
		return networkSortNum;
	}

	public void setNetworkSortNum(String networkSortNum) {
		this.networkSortNum = networkSortNum;
	}
	public String getBenefitTypeProvided() {
		return benefitTypeProvided;
	}

	public void setBenefitTypeProvided(String benefitTypeProvided) {
		this.benefitTypeProvided = benefitTypeProvided;
	}

  public String getTreatyType() {
		return treatyType;
	}

	public void setTreatyType(String treatyType) {
		this.treatyType = treatyType;
	}

	public String getTreatiesPlanId() {
		return treatiesPlanId;
	}

	public void setTreatiesPlanId(String treatiesPlanId) {
		this.treatiesPlanId = treatiesPlanId;
	}

	public String getReinsurerId() {
		return reinsurerId;
	}

	public void setReinsurerId(String reinsurerId) {
		this.reinsurerId = reinsurerId;
	}

	public Long getPremiumSeqId() {
		return premiumSeqId;
	}

	public void setPremiumSeqId(Long premiumSeqId) {
		this.premiumSeqId = premiumSeqId;
	}

	public String getMinimumAge() {
		return minimumAge;
	}

	public void setMinimumAge(String minimumAge) {
		this.minimumAge = minimumAge;
	}

	public String getMaximumAge() {
		return maximumAge;
	}

	public void setMaximumAge(String maximumAge) {
		this.maximumAge = maximumAge;
	}

	public String getPremium() {
		return premium;
	}

	public void setPremium(String premium) {
		this.premium = premium;
	}

	public String getDeleteYN() {
		return deleteYN;
	}

	public void setDeleteYN(String deleteYN) {
		this.deleteYN = deleteYN;
	}

	
	public ArrayList getBrokerNameList() {
		return brokerNameList;
	}

	public void setBrokerNameList(ArrayList brokerNameList) {
		this.brokerNameList = brokerNameList;
	}
}//end of PolicyDetailVO
