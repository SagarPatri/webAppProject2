/**
 * @ (#) MemberVO.java Feb 02, 2006
 * Project 	     : TTK HealthCare Services
 * File          : MemberVO.java
 * Author        : Krishna K H
 * Company       : Span Systems Corporation
 * Date Created  : Feb 02, 2006
 *
 * @author       :  Krishna K H
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.dto.enrollment;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.io.InputStream;

import com.ttk.common.TTKCommon;

public class MemberDetailVO extends MemberVO {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String strOccupationTypeID= "";
    private String strOccupation = "";
    private String strCustomerID= "";
    private String strCategoryTypeID= "";
    private String strStatus= "";
    private String strStatusDesc="";
    private Date dtInceptionDate = null;
    private Date dtExitDate = null;
    private String strPEDPresentYN= "";
    private String strPolicyStatusID= "";
    private String strMemberTypeID= "";
    private String strPhotoPresentYN= "";
    private String strCardPrintYN= "";
    private Date dtCardPrintDate = null;
    private Date dtDocDispatchDate = null;
    private String strCardBatchNbr = "";
    private String strCourierNbr = "";
    private String strClarificationTypeID= "";
    private Integer intCardPrintCnt=null;
    private Date dtClarifiedDate = null;
    private String strRemarks= "";
    private String strCertificateNbr= "";
    private String strCreditCardNbr = "";
    private String strCustomerCode= "";
    private MemberAddressVO memberAddressVO = null;
    private Date dtPolicyExpireDate = null;
    private String strRenewYN="";
    private Integer iRenewCount=null;
    private Date dtDeclarationDate=null;
    private String strProposalFormYN="";
    private Long lngPlanSeqID = null;
    private String strCustEndorseNbr = "";

    private String strOrderNbr = "";
    private String strPrevOrderNbr = "";
    private String strEmployeeNbr = "";
    private String strDepartment = "";
    private String strDesignation = "";
    private HashMap<String,String> strDesignations;
    private String strBeneficiaryname = "";
    private Long lngGroupRegnSeqID = null;
    private Long lngInsSeqID = null;
    private Long lngOfficeSeqID = null;
    private String strCustomerNbr = "";
    private Long lngSeqID = null; //To store Endorsement and Policy Sequence ID
    private Date dtDateOfIncept = null;
    private Long lngBankSeqID = null;
    private String strBankAccNbr = "";
    private String strAccountName = "";
    private String strBankName ="";
    private String strBranch = "";
    private String strBankPhone = "";
    private String strMICRCode = "";
    private String strGenderYN = "";//'Y'-if gender defines in Database and 'N'-Gender has not defined in database
    private String strAssocGenderRel = "";
    private String strMemberRemarks = "";
    private String strStopPatClmYN = "";
    private Date dtReceivedAfter = null;
    private Date dtDateOfMarriage = null;
    private String strPanNbr = "";
    private String strCardDesc = "";
    private String strInsuredCode = "";//INS_INSURED_CODE
    private String strDiabetesCoverYN = "";
    private String strHyperTensCoverYN = "";
    private String strSerialNumber = "";
    private String strFamilyNbr ="";
    
    private String nationality="";
    private String maritalStatus="";
    private String emirateId="";
    private String passportNumber="";
    private String globalNetMemberID="";
    
    // added for Capitation Policy....16
  //  private Integer ageRange =  null;
    private Integer minAge = null;
    private Integer maxAge = null;
    private double medicalPremium;
    private double insurerMargin;
    private double maternityPremium;
    private double brokerMargin;
    private double opticalPremium;
    private double tapMargin;
    private double dentalPremium;
    private double reInsBrkMargin;
    private double wellnessPremium;
    private double otherMargin;
    private double grossPremium;
    private double ipNetPremium;
    private double netPremium;
    private long claimSeqID;
    private String claimNumber;
    private long claim_hosp_assoc_seq_id;
    private long hosp_seq_id;
    private String claim_type;
    private String memPolicyStartDate=null;
    private String memPolicyEndDate=null;
    private String claimBatchNumber=null;
    private String contactNumber=null;
    private String memberTreatmentStartDate=null;
   
    private String bankAccountQatarYN;
    private String bankBranchText;
    private String bankCountry;
    private String countryName;
    private String finalRemarks;
    private String corporateName;
    private String employeeName;
    private String alkootId;
	private String stopPreAuthsYN;
	private Date stopPreauthDate;
	private String stopClaimsYN;
	private Date stopClaimsDate;
    private String mailNotificationYN;
    private String mailNotificationhiddenYN;
    //cost centre addition
	private String groupNumber;

	public String getGroupNumber() {
		return groupNumber;
	}
	public void setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
	}
	public String getFinalRemarks() {
		return finalRemarks;
	}
	public void setFinalRemarks(String finalRemarks) {
		this.finalRemarks = finalRemarks;
	}
	public String getMemberTreatmentStartDate() {
		return memberTreatmentStartDate;
	}
	public void setMemberTreatmentStartDate(String memberTreatmentStartDate) {
		this.memberTreatmentStartDate = memberTreatmentStartDate;
	}
	public String getClaimBatchNumber() {
		return claimBatchNumber;
	}
	public void setClaimBatchNumber(String claimBatchNumber) {
		this.claimBatchNumber = claimBatchNumber;
	}
	public String getMemPolicyStartDate() {
		return memPolicyStartDate;
	}
	public void setMemPolicyStartDate(String memPolicyStartDate) {
		this.memPolicyStartDate = memPolicyStartDate;
	}
	public String getMemPolicyEndDate() {
		return memPolicyEndDate;
	}
	public void setMemPolicyEndDate(String memPolicyEndDate) {
		this.memPolicyEndDate = memPolicyEndDate;
	}
	
	public long getClaimSeqID() {
		return claimSeqID;
	}
	public void setClaimSeqID(long claimSeqID) {
		this.claimSeqID = claimSeqID;
	}
	public String getClaimNumber() {
		return claimNumber;
	}
	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	public long getClaim_hosp_assoc_seq_id() {
		return claim_hosp_assoc_seq_id;
	}
	public void setClaim_hosp_assoc_seq_id(long claim_hosp_assoc_seq_id) {
		this.claim_hosp_assoc_seq_id = claim_hosp_assoc_seq_id;
	}
	public long getHosp_seq_id() {
		return hosp_seq_id;
	}
	public void setHosp_seq_id(long hosp_seq_id) {
		this.hosp_seq_id = hosp_seq_id;
	}
	public String getClaim_type() {
		return claim_type;
	}
	public void setClaim_type(String claim_type) {
		this.claim_type = claim_type;
	}

		//Added for IBM.....14
	    private String strStopOPtInYN = "N";
	    private String strValidEmailPhYN ="N";
		private String strV_OPT_ALLOWED = "";//Added by Praveen for Optin Weblogin config
		private String strwindowsOPTYN = "";//ADDED BY REKHA
		private String strchkpreauthclaims = "";//ADDED BY REKHA
		private String EmailID2="";
		private String MobileNo="";
		//koc for griavance
		private String strMarriageHideYN = "";
		
		private String secondName;
		private String familyName;
		private String uIDNumber;
		private String salaryBand;
		private String vipYN;
		private String commissionBased;
		private String employeeSecondName;
		private String employeeFamilyName;
	    private String strResidentialLocation;
	    private String strWorkLocation;
	    private String strEmailID3="";//added for EmailID2-KOC-1216
	    private InputStream jpgInputStream;
	    private String bankState;//bank state
	    private String strBankcity;//Bnak city
	    private String strBankBranch;//branch name
	    private String strBankInsureName ="";
	    private InputStream  imageData =null;
	    private String photoYN;
		private String invoiceYN = "";
		private String creditnoteYN = "";		
      
	    private String sNo;
	    private String preAuthNumber;
	    private String providerName;
	    private String sStatus;
	    private String queryType;
	    private String customerQuery;
	    
		public String getContactNumber() {
			return contactNumber;
		}
		public void setContactNumber(String contactNumber) {
			this.contactNumber = contactNumber;
		}
		public String getQueryType() {
			return queryType;
		}
		public void setQueryType(String queryType) {
			this.queryType = queryType;
		}
		public String getCustomerQuery() {
			return customerQuery;
		}
		public void setCustomerQuery(String customerQuery) {
			this.customerQuery = customerQuery;
		}
		public String getsStatus() {
			return sStatus;
		}
		public void setsStatus(String sStatus) {
			this.sStatus = sStatus;
		}

		private Long preAuthSeqId;

		public Long getPreAuthSeqId() {
			return preAuthSeqId;
		}
		public void setPreAuthSeqId(Long preAuthSeqId) {
			this.preAuthSeqId = preAuthSeqId;
		}
		public String getsNo() {
			return sNo;
		}
		public void setsNo(String sNo) {
			this.sNo = sNo;
		}
		public String getPreAuthNumber() {
			return preAuthNumber;
		}
		public void setPreAuthNumber(String preAuthNumber) {
			this.preAuthNumber = preAuthNumber;
		}
		public String getProviderName() {
			return providerName;
		}
		public void setProviderName(String providerName) {
			this.providerName = providerName;
		}
		public void setSalaryBand(String salaryBand) {
			this.salaryBand = salaryBand;
		}
		public String getSalaryBand() {
			return salaryBand;
		}
		public void setuIDNumber(String uIDNumber) {
			this.uIDNumber = uIDNumber;
		}
		public String getuIDNumber() {
			return uIDNumber;
		}
		
		public void setSecondName(String secondName) {
			this.secondName = secondName;
		}
		public String getSecondName() {
			return secondName;
		}
		public void setFamilyName(String familyName) {
			this.familyName = familyName;
		}
		public String getFamilyName() {
			return familyName;
		}
		public void setMarriageHideYN(String strMarriageHideYN) {
			this.strMarriageHideYN = strMarriageHideYN;
		}

		public String getMarriageHideYN() {
			return strMarriageHideYN;
		}

		//koc for griavance

		//added by Praveen for koc-1216
//added as per KOC 1261
		private String groupId="";
	    /**
		 * @param groupId the groupId to set
		 */
		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}

		/**
		 * @return the groupId
		 */
		public String getGroupId() {
			return groupId;
		}
		//added as per KOC 1261


		

		//Added for IBM....14.1
			public void setValidEmailPhYN(String EmailMobPh) {
				this.strValidEmailPhYN = EmailMobPh;
			}

			public String getValidEmailPhYN() {
				return strValidEmailPhYN;
			}
			public String getMobileNo() {
				return MobileNo;
			}

			public void setMobileNo(String mobileNo) {
				MobileNo = mobileNo;
			}

			public String getEmailID2() {
				return EmailID2;
			}

			public void setEmailID2(String emailID2) {
				EmailID2 = emailID2;
			}


    /** Retrieve the InsuredCode
	 * @return Returns the strInsuredCode.
	 */
	public String getInsuredCode() {
		return strInsuredCode;
	}//end of getInsuredCode()

	/** Sets the InsuredCode
	 * @param strInsuredCode The strInsuredCode to set.
	 */
	public void setInsuredCode(String strInsuredCode) {
		this.strInsuredCode = strInsuredCode;
	}//end of setInsuredCode(String strInsuredCode)

	/** Retrieve the Card Description
	 * @return Returns the strCardDesc.
	 */
	public String getCardDesc() {
		return strCardDesc;
	}//end of getCardDesc()

	/** Sets the Card Description
	 * @param strCardDesc The strCardDesc to set.
	 */
	public void setCardDesc(String strCardDesc) {
		this.strCardDesc = strCardDesc;
	}//end of setCardDesc(String strCardDesc)

    /** Retrieve the PrevOrderNbr
	 * @return Returns the strPrevOrderNbr.
	 */
	public String getPrevOrderNbr() {
		return strPrevOrderNbr;
	}//end of getPrevOrderNbr()

	/** Sets the PrevOrderNbr
	 * @param strPrevOrderNbr The strPrevOrderNbr to set.
	 */
	public void setPrevOrderNbr(String strPrevOrderNbr) {
		this.strPrevOrderNbr = strPrevOrderNbr;
	}//end of setPrevOrderNbr(String strPrevOrderNbr)

	/** Retrieve the PanNbr
	 * @return Returns the strPanNbr.
	 */
	public String getPanNbr() {
		return strPanNbr;
	}//end of getPanNbr()

	/** Sets the PanNbr
	 * @param strPanNbr The strPanNbr to set.
	 */
	public void setPanNbr(String strPanNbr) {
		this.strPanNbr = strPanNbr;
	}//end of setPanNbr(String strPanNbr)

	/** Retrieve the DateOfMarriage
	 * @return Returns the dtDateOfMarriage.
	 */
	public Date getDateOfMarriage() {
		return dtDateOfMarriage;
	}//end of getDateOfMarriage()

	/** Sets the DateOfMarriage
	 * @param dtDateOfMarriage The dtDateOfMarriage to set.
	 */
	public void setDateOfMarriage(Date dtDateOfMarriage) {
		this.dtDateOfMarriage = dtDateOfMarriage;
	}//end of setDateOfMarriage(Date dtDateOfMarriage)

	/** Retrieve the DocDispatchDate
	 * @return Returns the dtDocDispatchDate.
	 */
	public Date getDocDispatchDate() {
		return dtDocDispatchDate;
	}//end of getDocDispatchDate()

	/** Sets the DocDispatchDate
	 * @param dtDocDispatchDate The dtDocDispatchDate to set.
	 */
	public void setDocDispatchDate(Date dtDocDispatchDate) {
		this.dtDocDispatchDate = dtDocDispatchDate;
	}//end of setDocDispatchDate(Date dtDocDispatchDate)

	/** Retrieve the ReceivedAfter
	 * @return Returns the dtReceivedAfter.
	 */
	public Date getReceivedAfter() {
		return dtReceivedAfter;
	}//end of getReceivedAfter()

	/** Sets the ReceivedAfter
	 * @param dtReceivedAfter The dtReceivedAfter to set.
	 */
	public void setReceivedAfter(Date dtReceivedAfter) {
		this.dtReceivedAfter = dtReceivedAfter;
	}//end of setReceivedAfter(Date dtReceivedAfter)

	/** Retrieve the StopPatClmYN
	 * @return Returns the strStopPatClmYN.
	 */
	public String getStopPatClmYN() {
		return strStopPatClmYN;
	}//end of getStopPatClmYN()

	/** Sets the StopPatClmYN
	 * @param strStopPatClmYN The strStopPatClmYN to set.
	 */
	public void setStopPatClmYN(String strStopPatClmYN) {
		this.strStopPatClmYN = strStopPatClmYN;
	}//end of setStopPatClmYN(String strStopPatClmYN)

	/** Retrieve the MemberRemarks
	 * @return Returns the strMemberRemarks.
	 */
	public String getMemberRemarks() {
		return strMemberRemarks;
	}//end of getMemberRemarks()

	/** Sets the MemberRemarks
	 * @param strMemberRemarks The strMemberRemarks to set.
	 */
	public void setMemberRemarks(String strMemberRemarks) {
		this.strMemberRemarks = strMemberRemarks;
	}//end of setMemberRemarks(String strMemberRemarks)

	/**
     * Retrieve the Status Description
     * @return  strStatusDesc String
     */
    public String getStatusDesc() {
        return strStatusDesc;
    }//end of getStatusDesc()

    /**
     * Sets the Status Description
     * @param  strStatusDesc String
     */
    public void setStatusDesc(String strStatusDesc) {
        this.strStatusDesc = strStatusDesc;
    }//end of setStatusDesc(String strStatusDesc)

    /** Retrieve the AssocGenderRel
	 * @return Returns the strAssocGenderRel.
	 */
	public String getAssocGenderRel() {
		return strAssocGenderRel;
	}//end of getAssocGenderRel()

	/** Sets the AssocGenderRel
	 * @param strAssocGenderRel The strAssocGenderRel to set.
	 */
	public void setAssocGenderRel(String strAssocGenderRel) {
		this.strAssocGenderRel = strAssocGenderRel;
	}//end of setAssocGenderRel(String strAssocGenderRel)

	/** Retrieve the Gender YN
	 * @return Returns the strGenderYN.
	 */
	public String getGenderYN() {
		return strGenderYN;
	}//end of getGenderYN()

	/** Sets the Gender YN
	 * @param strGenderYN The strGenderYN to set.
	 */
	public void setGenderYN(String strGenderYN) {
		this.strGenderYN = strGenderYN;
	}//end of setGenderYN(String strGenderYN)

	/** This method returns the Occupation Type ID
     * @return Returns the strOccupationTypeID.
     */
    public String getOccupationTypeID() {
        return strOccupationTypeID;
    }//end of getOccupationTypeID()

    /** This method sets the Occupation Type ID
     * @param strOccupationTypeID The Occupation Type ID to set.
     */
    public void setOccupationTypeID(String strOccupationTypeID) {
        this.strOccupationTypeID = strOccupationTypeID;
    }//end of setOccupationTypeID(String strOccupationTypeID)

    /**
     * This method returns the Customer ID
     * @return Returns the strCustomerID.
     */
    public String getCustomerID() {
        return strCustomerID;
    }//end of getCustomerID()
    /**
     * This method sets the Customer ID
     * @param strCustomerID The strCustomerID to set.
     */
    public void setCustomerID(String strCustomerID) {
        this.strCustomerID = strCustomerID;
    }//end of setCustomerID(String strCustomerID)
    /**
     * This method returns the Category Type ID
     * @return Returns the strCategoryTypeID.
     */
    public String getCategoryTypeID() {
        return strCategoryTypeID;
    }//end of getCategoryTypeID()
    /**
     * This method sets the Category Type ID
     * @param strCategoryTypeID The strCategoryTypeID to set.
     */
    public void setCategoryTypeID(String strCategoryTypeID) {
        this.strCategoryTypeID = strCategoryTypeID;
    }//end of setCategoryTypeID(String strCategoryTypeID)
    /**
     * This method returns the Status
     * @return Returns the strStatus.
     */
    public String getStatus() {
        return strStatus;
    }//end of getStatus()
    /**
     * This method sets the Status
     * @param strStatus The strStatus to set.
     */
    public void setStatus(String strStatus) {
        this.strStatus = strStatus;
    }//end of setStatus(String strStatus)
    /**
     * This method returns the Inception Date
     * @return Returns the dtInceptionDate.
     */
    public Date getInceptionDate() {
        return dtInceptionDate;
    }//end of getInceptionDate()
    /**
     * This method sets the Inception Date
     * @param dtInceptionDate The dtInceptionDate to set.
     */
    public void setInceptionDate(Date dtInceptionDate) {
        this.dtInceptionDate = dtInceptionDate;
    }//end of setInceptionDate(Date dtInceptionDate)
    /**
     * This method returns the Exit Date
     * @return Returns the dtExitDate.
     */
    public Date getExitDate() {
        return dtExitDate;
    }//end of getExitDate()
    /**
     * This method sets the Exit Date
     * @param dtExitDate The dtExitDate to set.
     */
    public void setExitDate(Date dtExitDate) {
        this.dtExitDate = dtExitDate;
    }//end of setExitDate(Date dtExitDate)
    /**
     * This method returns the PEDPresentYN
     * @return Returns the strPEDPresentYN.
     */
    public String getPEDPresentYN() {
        return strPEDPresentYN;
    }//end of getPEDPresentYN()
    /**
     * This method sets the PEDPresentYN
     * @param strPEDPresentYN The strPEDPresentYN to set.
     */
    public void setPEDPresentYN(String strPEDPresentYN) {
        this.strPEDPresentYN = strPEDPresentYN;
    }//end of setPEDPresentYN(String strPEDPresentYN)
    /**
     * This method returns the Policy Status ID
     * @return Returns the strPolicyStatusID.
     */
    public String getPolicyStatusID() {
        return strPolicyStatusID;
    }//end of getPolicyStatusID()
    /**
     * This method sets the Policy Status ID
     * @param strPolicyStatusID The strPolicyStatusID to set.
     */
    public void setPolicyStatusID(String strPolicyStatusID) {
        this.strPolicyStatusID = strPolicyStatusID;
    }//end of setPolicyStatusID(String strPolicyStatusID)
    /**
     * This method returns the Member Type ID
     * @return Returns the strMemberTypeID.
     */
    public String getMemberTypeID() {
        return strMemberTypeID;
    }//end of getMemberTypeID()
    /**
     * This method sets the Member Type ID
     * @param strMemberTypeID The strMemberTypeID to set.
     */
    public void setMemberTypeID(String strMemberTypeID) {
        this.strMemberTypeID = strMemberTypeID;
    }//end of setMemberTypeID(String strMemberTypeID)

    /**
     * This method returns the PhotoPresentYN
     * @return Returns the strPhotoPresentYN.
     */
    public String getPhotoPresentYN() {
        return strPhotoPresentYN;
    }//end of getPhotoPresentYN()
    /**
     * This method sets the PhotoPresentYN
     * @param strPhotoPresentYN The strPhotoPresentYN to set.
     */
    public void setPhotoPresentYN(String strPhotoPresentYN) {
        this.strPhotoPresentYN = strPhotoPresentYN;
    }//end of setPhotoPresentYN(String strPhotoPresentYN)
    /**
     * This method returns the CardPrintYN
     * @return Returns the strCardPrintYN.
     */
    public String getCardPrintYN() {
        return strCardPrintYN;
    }//end of getCardPrintYN()
    /**
     * This method sets the CardPrintYN
     * @param strCardPrintYN The strCardPrintYN to set.
     */
    public void setCardPrintYN(String strCardPrintYN) {
        this.strCardPrintYN = strCardPrintYN;
    }//end of setCardPrintYN(String strCardPrintYN)
    /**
     * This method returns the Card Print Date
     * @return Returns the dtCardPrintDate.
     */
    public Date getCardPrintDate() {
        return dtCardPrintDate;
    }//end of getCardPrintDate()
    /**
     * This method sets the Card Print Date
     * @param dtCardPrintDate The dtCardPrintDate to set.
     */
    public void setCardPrintDate(Date dtCardPrintDate) {
        this.dtCardPrintDate = dtCardPrintDate;
    }//end of setCardPrintDate(Date dtCardPrintDate)
    /**
     * This method returns the Clarification Type ID
     * @return Returns the strClarificationTypeID.
     */
    public String getClarificationTypeID() {
        return strClarificationTypeID;
    }//end of getClarificationTypeID()
    /**
     * This method sets the Clarification Type ID
     * @param strClarificationTypeID The strClarificationTypeID to set.
     */
    public void setClarificationTypeID(String strClarificationTypeID) {
        this.strClarificationTypeID = strClarificationTypeID;
    }//end of setClarificationTypeID(String strClarificationTypeID)
    /**
     * This method returns the Card Print Count
     * @return Returns the intCardPrintCnt.
     */
    public Integer getCardPrintCnt() {
        return intCardPrintCnt;
    }//end of getCardPrintCnt()
    /**
     * This method sets the Card Print Count
     * @param intCardPrintCnt The intCardPrintCnt to set.
     */
    public void setCardPrintCnt(Integer intCardPrintCnt) {
        this.intCardPrintCnt = intCardPrintCnt;
    }//end of setCardPrintCnt(Integer intCardPrintCnt)
    /**
     * This method returns the Clarified Date
     * @return Returns the dtClarifiedDate.
     */
    public Date getClarifiedDate() {
        return dtClarifiedDate;
    }//end of getClarifiedDate()
    /**
     * This method sets the Clarified Date
     * @param dtClarifiedDate The dtClarifiedDate to set.
     */
    public void setClarifiedDate(Date dtClarifiedDate) {
        this.dtClarifiedDate = dtClarifiedDate;
    }//end of setClarifiedDate(Date dtClarifiedDate)
    /**
     * This method returns the Remarks
     * @return Returns the strRemarks.
     */
    public String getRemarks() {
        return strRemarks;
    }//end of getRemarks()
    /**
     * This method sets the Remarks
     * @param strRemarks The strRemarks to set.
     */
    public void setRemarks(String strRemarks) {
        this.strRemarks = strRemarks;
    }//end of setRemarks(String strRemarks)
    /**
     * This method returns the Certificate Number
     * @return Returns the strCertificateNbr.
     */
    public String getCertificateNbr() {
        return strCertificateNbr;
    }//end of getCertificateNbr()
    /**
     * This method sets the Certificate Number
     * @param strCertificateNbr The strCertificateNbr to set.
     */
    public void setCertificateNbr(String strCertificateNbr) {
        this.strCertificateNbr = strCertificateNbr;
    }//end of setCertificateNbr(String strCertificateNbr)

    /**
     * This method returns the Customer Code
     * @return Returns the strCustomerCode.
     */
    public String getCustomerCode() {
        return strCustomerCode;
    }//end of getCustomerCode()
    /**
     * This method sets the Customer Code
     * @param strCustomerCode The strCustomerCode to set.
     */
    public void setCustomerCode(String strCustomerCode) {
        this.strCustomerCode = strCustomerCode;
    }//end of setCustomerCode(String strCustomerCode)
    /**
     * This method returns the MemberAddressVO
     * @return Returns the MemberAddressVO.
     */
    public MemberAddressVO getMemberAddressVO() {
        return memberAddressVO;
    }//end of getMemberAddressVO()
    /**
     * This method sets the MemberAddressVO
     * @param memberAddressVO The memberAddressVO to set.
     */
    public void setMemberAddressVO(MemberAddressVO memberAddressVO) {
        this.memberAddressVO = memberAddressVO;
    }//end of setMemberAddressVO(MemberAddressVO memberAddressVO)

    /**
     * This method returns the Policy Expire Date
     * @return Returns the dtPolicyExpireDate.
     */
    public Date getPolicyExpireDate() {
        return dtPolicyExpireDate;
    }//end of getPolicyExpireDate()
    /**
     * This method sets the Policy Expire Date
     * @param dtPolicyExpireDate The dtPolicyExpireDate to set.
     */
    public void setPolicyExpireDate(Date dtPolicyExpireDate) {
        this.dtPolicyExpireDate = dtPolicyExpireDate;
    }//end of setPolicyExpireDate(Date dtPolicyExpireDate)
    /**
     * This method returns the RenewYN
     * @return Returns the strRenewYN.
     */
    public String getRenewYN() {
        return strRenewYN;
    }//end of getRenewYN()
    /**
     * This method sets the RenewYN
     * @param strRenewYN The strRenewYN to set.
     */
    public void setRenewYN(String strRenewYN) {
        this.strRenewYN = strRenewYN;
    }//end of setRenewYN(String strRenewYN)
    /**
     * This method sets the Renew Count
     * @param iRenewCount The iRenewCount to set.
     */
    public void setRenewCount(Integer iRenewCount) {
        this.iRenewCount = iRenewCount;
    }//end of setRenewYN(Integer iRenewCount)
    /**
     * This method returns the Renew Count
     * @return Returns the iRenewCount.
     */
    public Integer getRenewCount() {
        return iRenewCount;
    }//end of getRenewCount()

   /**
     * This method returns the Declaration Date
     * @return Returns the dtDeclarationDate.
     */
    public Date getDeclarationDate() {
        return dtDeclarationDate;
    }//end of getDeclarationDate()
    /**
     * This method sets the Declaration Date
     * @param dtDeclarationDate The dtDeclarationDate to set.
     */
    public void setDeclarationDate(Date dtDeclarationDate) {
        this.dtDeclarationDate = dtDeclarationDate;
    }//end of setDeclarationDate(Date dtDeclarationDate)
    /**
     * This method returns the ProposalFormYN
     * @return Returns the strProposalFormYN.
     */
    public String getProposalFormYN() {
        return strProposalFormYN;
    }//end of getProposalFormYN()
    /**
     * This method sets the ProposalFormYN
     * @param strProposalFormYN The strProposalFormYN to set.
     */
    public void setProposalFormYN(String strProposalFormYN) {
        this.strProposalFormYN = strProposalFormYN;
    }//end of setProposalFormYN(String strProposalFormYN)
    /**
     * This method returns the Plan Sequence ID
     * @return Returns the lngPlanSeqID.
     */
    public Long getPlanSeqID() {
        return lngPlanSeqID;
    }//end of getPlanSeqID()
    /**
     * This method sets the Plan Sequence ID
     * @param lngPlanSeqID The lngPlanSeqID to set.
     */
    public void setPlanSeqID(Long lngPlanSeqID) {
        this.lngPlanSeqID = lngPlanSeqID;
    }//end of setPlanSeqID(Long lngPlanSeqID)

    /** This method returns the Order Number
     * @return Returns the strOrderNbr.
     */
    public String getOrderNbr() {
        return strOrderNbr;
    }//end of getOrderNbr()

    /** This method sets the Order Number
     * @param strOrderNbr The strOrderNbr to set.
     */
    public void setOrderNbr(String strOrderNbr) {
        this.strOrderNbr = strOrderNbr;
    }//end of setOrderNbr(String strOrderNbr)

    /** This method returns the Employee Number
     * @return Returns the strEmployeeNbr.
     */
    public String getEmployeeNbr() {
        return strEmployeeNbr;
    }//end of getEmployeeNbr()

    /** This method sets the Employee Number
     * @param strEmployeeNbr The strEmployeeNbr to set.
     */
    public void setEmployeeNbr(String strEmployeeNbr) {
        this.strEmployeeNbr = strEmployeeNbr;
    }//end of setEmployeeNbr(String strEmployeeNbr)

    /** This method returns the Group Registration Seq ID
     * @return Returns the lngGroupRegnSeqID.
     */
    public Long getGroupRegnSeqID() {
        return lngGroupRegnSeqID;
    }//end of getGroupRegnSeqID()

    /** This method sets the Group Registration Seq ID
     * @param lngGroupRegnSeqID The lngGroupRegnSeqID to set.
     */
    public void setGroupRegnSeqID(Long lngGroupRegnSeqID) {
        this.lngGroupRegnSeqID = lngGroupRegnSeqID;
    }//end of setGroupRegnSeqID(Long lngGroupRegnSeqID)

    /** This method returns the Ins Seq ID
     * @return Returns the lngInsSeqID.
     */
    public Long getInsSeqID() {
        return lngInsSeqID;
    }//end of getInsSeqID()

    /** This method sets the Ins Seq ID
     * @param lngInsSeqID The lngInsSeqID to set.
     */
    public void setInsSeqID(Long lngInsSeqID) {
        this.lngInsSeqID = lngInsSeqID;
    }//end of setInsSeqID(Long lngInsSeqID)

    /** This method returns the Office Seq ID
     * @return Returns the lngOfficeSeqID.
     */
    public Long getOfficeSeqID() {
        return lngOfficeSeqID;
    }//end of getOfficeSeqID()

    /** This method sets the Office Seq ID
     * @param lngOfficeSeqID The lngOfficeSeqID to set.
     */
    public void setOfficeSeqID(Long lngOfficeSeqID) {
        this.lngOfficeSeqID = lngOfficeSeqID;
    }//end of setOfficeSeqID(Long lngOfficeSeqID)

    /** This method returns the Beneficiary Name
     * @return Returns the strBeneficiaryname.
     */
    public String getBeneficiaryname() {
        return strBeneficiaryname;
    }//end of getBeneficiaryname()

    /** This method sets the Beneficiary Name
     * @param strBeneficiaryname The strBeneficiaryname to set.
     */
    public void setBeneficiaryname(String strBeneficiaryname) {
        this.strBeneficiaryname = strBeneficiaryname;
    }//end of setBeneficiaryname(String strBeneficiaryname)

    /** This method returns the Department
     * @return Returns the strDepartment.
     */
    public String getDepartment() {
        return strDepartment;
    }//end of getDepartment()

    /** This method sets the Department
     * @param strDepartment The strDepartment to set.
     */
    public void setDepartment(String strDepartment) {
        this.strDepartment = strDepartment;
    }//end of setDepartment(String strDepartment)

    /** This method returns the Designation
     * @return Returns the strDesignation.
     */
    public String getDesignation() {
        return strDesignation;
    }//end of getDesignation()

    /** This method sets the Designation
     * @param strDesignation The strDesignation to set.
     */
    public void setDesignation(String strDesignation) {
        this.strDesignation = strDesignation;
    }//end of setDesignation(String strDesignation)

    /** This method returns the Customer Number
     * @return Returns the strCustomerNbr.
     */
    public String getCustomerNbr() {
        return strCustomerNbr;
    }//end of getCustomerNbr()

    /** This method sets the Customer Number
     * @param strCustomerNbr The strCustomerNbr to set.
     */
    public void setCustomerNbr(String strCustomerNbr) {
        this.strCustomerNbr = strCustomerNbr;
    }//end of setCustomerNbr(String strCustomerNbr)

   /** This method returns the Endorsement or Policy Seqence ID
     * @return Returns the lngSeqID.
     */
    public Long getSeqID() {
        return lngSeqID;
    }//end of getSeqID()

    /** This method sets the Endorsement or Policy Seqence ID
     * @param lngSeqID The lngSeqID to set.
     */
    public void setSeqID(Long lngEndorsementSeqID) {
        this.lngSeqID = lngEndorsementSeqID;
    }//end of setSeqID(Long lngSeqID)

    /** This method returns the Date Of Inception
     * @return Returns the dtDateOfIncept.
     */
    public String getDateOfIncept() {
        return TTKCommon.getFormattedDate(dtDateOfIncept);
    }//end of getDateOfIncept()

    /** This method sets the Date Of Inception
     * @param dtDateOfIncept The dtDateOfIncept to set.
     */
    public void setDateOfIncept(Date dtDateOfIncept) {
        this.dtDateOfIncept = dtDateOfIncept;
    }//end of setDateOfIncept(Date dtDateOfIncept)

    /** This method returns the Occupation
     * @return Returns the strOccupation.
     */
    public String getOccupation() {
        return strOccupation;
    }//end of getOccupation()

    /** This method sets the Occupation
     * @param strOccupation The strOccupation to set.
     */
    public void setOccupation(String strOccupation) {
        this.strOccupation = strOccupation;
    }//end of setOccupation(String strOccupation)

    /** This method returns the Bank Seq ID
     * @return Returns the lngBankSeqID.
     */
    public Long getBankSeqID() {
        return lngBankSeqID;
    }//end of getBankSeqID()

    /** This method sets the bank Seq ID
     * @param lngBankSeqID The lngBankSeqID to set.
     */
    public void setBankSeqID(Long lngBankSeqID) {
        this.lngBankSeqID = lngBankSeqID;
    }//end of setBankSeqID(Long lngBankSeqID)

    /** This method returns the Bank Account Number
     * @return Returns the strBankAccNbr.
     */
    public String getBankAccNbr() {
        return strBankAccNbr;
    }//end of getBankAccNbr()

    /** This method sets the Bank Account Number
     * @param strBankAccNbr The strBankAccNbr to set.
     */
    public void setBankAccNbr(String strBankAccNbr) {
        this.strBankAccNbr = strBankAccNbr;
    }//end of setBankAccNbr(String strBankAccNbr)

    /** This method returns the Bank Name
     * @return Returns the strBankName.
     */
    public String getBankName() {
        return strBankName;
    }//end of getBankName()

    /** This method sets the Bank Name
     * @param strBankName The strBankName to set.
     */
    public void setBankName(String strBankName) {
        this.strBankName = strBankName;
    }//end of setBankName(String strBankName)

    /** This method returns the Bank Phone
     * @return Returns the strBankPhone.
     */
    public String getBankPhone() {
        return strBankPhone;
    }//end of getBankPhone()

    /** This method sets the Bank Phone
     * @param strBankPhone The strBankPhone to set.
     */
    public void setBankPhone(String strBankPhone) {
        this.strBankPhone = strBankPhone;
    }//end of setBankPhone(String strBankPhone)

    /** This method returns the Bank Branch
     * @return Returns the strBranch.
     */
    public String getBranch() {
        return strBranch;
    }//end of getBranch()

    /** This method sets the Bank Branch
     * @param strBranch The strBranch to set.
     */
    public void setBranch(String strBranch) {
        this.strBranch = strBranch;
    }//end of setBranch(String strBranch)

    /** This method returns the MICR Code
     * @return Returns the strMICRCode.
     */
    public String getMICRCode() {
        return strMICRCode;
    }//end of getMICRCode()

    /** This method sets the MICR Code
     * @param strMICRCode The strMICRCode to set.
     */
    public void setMICRCode(String strMICRCode) {
        this.strMICRCode = strMICRCode;
    }//end of setMICRCode(String strMICRCode)

	/** Retrieve the CreditCardNbr
	 * @return Returns the strCreditCardNbr.
	 */
	public String getCreditCardNbr() {
		return strCreditCardNbr;
	}//end of getCreditCardNbr()

	/** Sets the CreditCardNbr
	 * @param strCreditCardNbr The strCreditCardNbr to set.
	 */
	public void setCreditCardNbr(String strCreditCardNbr) {
		this.strCreditCardNbr = strCreditCardNbr;
	}//end of setCreditCardNbr(String strCreditCardNbr)

	/** Retrieve the CardBatchNbr
	 * @return Returns the strCardBatchNbr.
	 */
	public String getCardBatchNbr() {
		return strCardBatchNbr;
	}//end of getCardBatchNbr()

	/** Sets the CardBatchNbr
	 * @param strCardBatchNbr The strCardBatchNbr to set.
	 */
	public void setCardBatchNbr(String strCardBatchNbr) {
		this.strCardBatchNbr = strCardBatchNbr;
	}//end of setCardBatchNbr(String strCardBatchNbr)

	/** Retrieve the CourierNbr
	 * @return Returns the strCourierNbr.
	 */
	public String getCourierNbr() {
		return strCourierNbr;
	}//end of getCourierNbr()

	/** Sets the CourierNbr
	 * @param strCourierNbr The strCourierNbr to set.
	 */
	public void setCourierNbr(String strCourierNbr) {
		this.strCourierNbr = strCourierNbr;
	}//end of setCourierNbr(String strCourierNbr)

	/** This method returns the Group Name
     * @return Returns the strGroupName.
     */
    /*public String getGroupName() {
        return strGroupName;
    }//end of getGroupName()
*/
    /** This method sets the Group Name
     * @param strGroupName The strGroupName to set.
     */
    /*public void setGroupName(String strGroupName) {
        this.strGroupName = strGroupName;
    }//end of setGroupName(String strGroupName)
*/
	/** Retrieve the Customer Endorsement Number
	 * @return Returns the strCustEndorseNbr.
	 */
	public String getCustEndorseNbr() {
		return strCustEndorseNbr;
	}//end of getCustEndorseNbr()

	/** Sets the Customer Endorsement Number
	 * @param strCustEndorseNbr The strCustEndorseNbr to set.
	 */
	public void setCustEndorseNbr(String strCustEndorseNbr) {
		this.strCustEndorseNbr = strCustEndorseNbr;
	}//end of setCustEndorseNbr(String strCustEndorseNbr)

	/** Retrieve the Account Name
	 * @return Returns the Account Name.
	 */
	public String getAccountName() {
		return strAccountName;
	}//end of getAccountName()

	/** Sets the Account Name
	 * @param accountName The Account Name to set.
	 */
	public void setAccountName(String strAccountName) {
		this.strAccountName = strAccountName;
	}//end of setAccountName(String strAccountName)

	/** Retrieve the DiabetesCoverYN
	 * @return the strDiabetesCoverYN
	 */
	public String getDiabetesCoverYN() {
		return strDiabetesCoverYN;
	}//end of getDiabetesCoverYN()

	/** Sets the DiabetesCoverYN
	 * @param strDiabetesCoverYN the strDiabetesCoverYN to set
	 */
	public void setDiabetesCoverYN(String strDiabetesCoverYN) {
		this.strDiabetesCoverYN = strDiabetesCoverYN;
	}//end of setDiabetesCoverYN(String strDiabetesCoverYN)

	/** Retrieve the HyperTensCoverYN
	 * @return the strHyperTensCoverYN
	 */
	public String getHyperTensCoverYN() {
		return strHyperTensCoverYN;
	}// end of getHyperTensCoverYN()

	/** Sets the HyperTensCoverYN
	 * @param strHyperTensCoverYN the strHyperTensCoverYN to set
	 */
	public void setHyperTensCoverYN(String strHyperTensCoverYN) {
		this.strHyperTensCoverYN = strHyperTensCoverYN;
	}//end of setHyperTensCoverYN(String strHyperTensCoverYN)

	/** Retrieve the SerialNumber
	 * @return the strSerialNumber
	 */
	public String getSerialNumber() {
		return strSerialNumber;
	}

	/** Sets the SerialNumber
	 * @param strSerialNumber the strSerialNumber to set
	 */
	public void setSerialNumber(String strSerialNumber) {
		this.strSerialNumber = strSerialNumber;
	}

	/** Retrieve the FamilyNbr
	 * @return the strFamilyNbr
	 */
	public String getFamilyNbr() {
		return strFamilyNbr;
	}//end of getFamilyNbr()

	/** Sets the FamilyNbr
	 * @param strFamilyNbr the strFamilyNbr to set
	 */
	public void setFamilyNbr(String strFamilyNbr) {
		this.strFamilyNbr = strFamilyNbr;
	}// end of setFamilyNbr(String strFamilyNbr)

	//Added for IBM....14.1
		public void setStopOPtInYN(String StopOPtInYN) {
			this.strStopOPtInYN = StopOPtInYN;
		}

		public String getStopOPtInYN() {
			return strStopOPtInYN;
		}


		//Added by Praveen for opt in weblogin
		public String getV_OPT_ALLOWED() {
			return strV_OPT_ALLOWED;
		}

		public void setV_OPT_ALLOWED(String strVOPTALLOWED) {
			strV_OPT_ALLOWED = strVOPTALLOWED;
		}

		//ADDED BY REKHA
		public String getwindowsOPTYN() {
			return strwindowsOPTYN;
		}
		public void setwindowsOPTYN(String strwindowsOPTYN) {
			this.strwindowsOPTYN = strwindowsOPTYN;
		}

		public String getchkpreauthclaims() {
			return strchkpreauthclaims;
		}

		public void setchkpreauthclaims(String strchkpreauthclaims) {
			this.strchkpreauthclaims = strchkpreauthclaims;
		}

		public HashMap<String,String> getStrDesignations() {
			return strDesignations;
		}

		public void setStrDesignations(HashMap<String,String> strDesignations) {
			this.strDesignations = strDesignations;
		}

		public String getNationality() {
			return nationality;
		}

		public void setNationality(String nationality) {
			this.nationality = nationality;
		}

		public String getMaritalStatus() {
			return maritalStatus;
		}

		public void setMaritalStatus(String maritalStatus) {
			this.maritalStatus = maritalStatus;
		}

		public String getEmirateId() {
			return emirateId;
		}

		public void setEmirateId(String emirateId) {
			this.emirateId = emirateId;
		}

		public String getPassportNumber() {
			return passportNumber;
		}

		public void setPassportNumber(String passportNumber) {
			this.passportNumber = passportNumber;
		}
		public String getVipYN() {
			return vipYN;
		}
		public void setVipYN(String vipYN) {
			this.vipYN = vipYN;
		}
		public String getGlobalNetMemberID() {
			return globalNetMemberID;
		}
		public void setGlobalNetMemberID(String globalNetMemberID) {
			this.globalNetMemberID = globalNetMemberID;
		}
		public String getCommissionBased() {
			return commissionBased;
		}
		public void setCommissionBased(String commissionBased) {
			this.commissionBased = commissionBased;
		}
	/*	public Integer getAgeRange() {
			return ageRange;
		}
		public void setAgeRange(Integer ageRange) {
			this.ageRange = ageRange;
		} 
	*/	
		public Integer getMinAge() {
			return minAge;
		}
		public void setMinAge(Integer minAge) {
			this.minAge = minAge;
		}
		public Integer getMaxAge() {
			return maxAge;
		}
		public void setMaxAge(Integer maxAge) {
			this.maxAge = maxAge;
		}
		public double getInsurerMargin() {
			return insurerMargin;
		}
		public void setInsurerMargin(double insurerMargin) {
			this.insurerMargin = insurerMargin;
		}
		public double getMedicalPremium() {
			return medicalPremium;
		}
		public void setMedicalPremium(double medicalPremium) {
			this.medicalPremium = medicalPremium;
		}
		public double getBrokerMargin() {
			return brokerMargin;
		}
		public void setBrokerMargin(double brokerMargin) {
			this.brokerMargin = brokerMargin;
		}
		public double getMaternityPremium() {
			return maternityPremium;
		}
		public void setMaternityPremium(double maternityPremium) {
			this.maternityPremium = maternityPremium;
		}
		public double getOpticalPremium() {
			return opticalPremium;
		}
		public void setOpticalPremium(double opticalPremium) {
			this.opticalPremium = opticalPremium;
		}
		public double getTapMargin() {
			return tapMargin;
		}
		public void setTapMargin(double tapMargin) {
			this.tapMargin = tapMargin;
		}
		public double getDentalPremium() {
			return dentalPremium;
		}
		public void setDentalPremium(double dentalPremium) {
			this.dentalPremium = dentalPremium;
		}
		public double getReInsBrkMargin() {
			return reInsBrkMargin;
		}
		public void setReInsBrkMargin(double reInsBrkMargin) {
			this.reInsBrkMargin = reInsBrkMargin;
		}
		public double getWellnessPremium() {
			return wellnessPremium;
		}
		public void setWellnessPremium(double wellnessPremium) {
			this.wellnessPremium = wellnessPremium;
		}
		public double getGrossPremium() {
			return grossPremium;
		}
		public void setGrossPremium(double grossPremium) {
			this.grossPremium = grossPremium;
		}
		public double getOtherMargin() {
			return otherMargin;
		}
		public void setOtherMargin(double otherMargin) {
			this.otherMargin = otherMargin;
		}
		public double getIpNetPremium() {
			return ipNetPremium;
		}
		public void setIpNetPremium(double ipNetPremium) {
			this.ipNetPremium = ipNetPremium;
		}
		public double getNetPremium() {
			return netPremium;
		}
		public void setNetPremium(double netPremium) {
			this.netPremium = netPremium;
		}
		public String getEmployeeSecondName() {
			return employeeSecondName;
		}
		public void setEmployeeSecondName(String employeeSecondName) {
			this.employeeSecondName = employeeSecondName;
		}
		public String getEmployeeFamilyName() {
			return employeeFamilyName;
		}
		public void setEmployeeFamilyName(String employeeFamilyName) {
			this.employeeFamilyName = employeeFamilyName;
		}
		public String getEmailID3() {
			return strEmailID3;
		    }
		public String getInvoiceYN() {
			return invoiceYN;
		}
		public void setInvoiceYN(String invoiceYN) {
			this.invoiceYN = invoiceYN;
		}
		public String getCreditnoteYN() {
			return creditnoteYN;
		}
		public void setCreditnoteYN(String creditnoteYN) {
			this.creditnoteYN = creditnoteYN;
		}
	

		public void setEmailID3(String strEmailID3) {
			this.strEmailID3 = strEmailID3;
		    }
		 public void setResidentialLocation(String strResidentialLocation) {
				this.strResidentialLocation = strResidentialLocation;
			}
		     public String getResidentialLocation() {
				return strResidentialLocation;
			}
		     public void setWorkLocation(String strWorkLocation) {
		 		this.strWorkLocation = strWorkLocation;
		 	}
		      public String getWorkLocation() {
		 		return strWorkLocation;
		 	}

		  	public InputStream getJpgInputStream() {
				return jpgInputStream;
			}
			public void setJpgInputStream(InputStream jpgInputStream) {
				this.jpgInputStream = jpgInputStream;
			}
			public String getBankState() {
				return bankState;
			}
			
			public void setBankState(String bankState) {
				this.bankState = bankState;
			}
			public String getBankcity() {
				return strBankcity;
			}
			
			public void setBankcity(String strBankcity) {
				this.strBankcity = strBankcity;
			}
			public String getBankBranch() {
				return strBankBranch;
			}
			
			public void setBankBranch(String strBankBranch) {
				this.strBankBranch = strBankBranch;
			}
		    public String getBankInsureName() {
		        return strBankInsureName;
		    }//end of getBankName()

		    /** This method sets the Bank Name
		     * @param strBankName The strBankName to set.
		     */
		    public void setBankInsureName(String strBankInsureName) {
		        this.strBankInsureName = strBankInsureName;
		    }//end of setBankName(String strBankName)
			public InputStream getImageData() {
				return imageData;
			}
			public void setImageData(InputStream imageData) {
				this.imageData = imageData;
			}
		
			public String getPhotoYN() {
				return photoYN;
			}
			public void setPhotoYN(String photoYN) {
				this.photoYN = photoYN;
			}
			public String getBankAccountQatarYN() {
				return bankAccountQatarYN;
			}
			public void setBankAccountQatarYN(String bankAccountQatarYN) {
				this.bankAccountQatarYN = bankAccountQatarYN;
			}
			public String getBankBranchText() {
				return bankBranchText;
			}
			public void setBankBranchText(String bankBranchText) {
				this.bankBranchText = bankBranchText;
			}
			public String getBankCountry() {
				return bankCountry;
			}
			public void setBankCountry(String bankCountry) {
				this.bankCountry = bankCountry;
			}
			public String getCountryName() {
				return countryName;
			}
			public void setCountryName(String countryName) {
				this.countryName = countryName;
			}
			public String getCorporateName() {
				return corporateName;
			}
			public void setCorporateName(String corporateName) {
				this.corporateName = corporateName;
			}
			public String getEmployeeName() {
				return employeeName;
			}
			public void setEmployeeName(String employeeName) {
				this.employeeName = employeeName;
			}
			public String getAlkootId() {
				return alkootId;
			}
			public void setAlkootId(String alkootId) {
				this.alkootId = alkootId;
			}
			public String getStopPreAuthsYN() {
				return stopPreAuthsYN;
			}
			public void setStopPreAuthsYN(String stopPreAuthsYN) {
				this.stopPreAuthsYN = stopPreAuthsYN;
			}
			public Date getStopPreauthDate() {
				return stopPreauthDate;
			}
			public void setStopPreauthDate(Date stopPreauthDate) {
				this.stopPreauthDate = stopPreauthDate;
			}
			public String getStopClaimsYN() {
				return stopClaimsYN;
			}
			public void setStopClaimsYN(String stopClaimsYN) {
				this.stopClaimsYN = stopClaimsYN;
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


             
 }//end of MemberDetailVO
