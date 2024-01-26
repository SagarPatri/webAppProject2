package com.ttk.dto.webservice;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.apache.struts.upload.FormFile;

import com.ttk.dto.BaseVO;



public class OnlineIntimationVO extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long lngIntimationSeqID = null;
	private String strIntimationNbr = "";
	private String strEnrollmentID = "";
	private Long policyGrpSeqID = null;
	private Long memberSeqID = null;
	private String strMemName = "";
	private String strGenderDesc = "";
	private String intGenDate = "";
	private String strIntGenTime="";
    private String strIntGenDay="";
	private String ailmentDesc = "";
	private String strProvisionalDiagnosis = "";
	private String strPhoneNbr = "";
	private String strMobileNbr = "";
	private String strEmailID = "";
	private String strRemarks = "";
	private String strTTKRemarks = "";
	//private Date dtTTKRespondedDate = null; 
	private String strTTKRespondedTime="";
    private String strTTKRespondedDay="";
	private String strSubmittedYN = "";
	//private OnlineHospitalVO onlineHospitalVO = null;
	private String strPolicyNbr = "";
	private String strStatusTypeID = "";
	private String strStatusDesc = "";
	private String hospitalName=""; //Hospital name
	private String strHospAddress="";// Address
	private BigDecimal bdEstimatedCost = null;
	private String strTTKNarrative = "";//TPA_NARRATIVE
	
	private String strBarCodeNbr = "";
	private String strDocumentTypeDesc = "";
	private String strReceivedTime = "";
	private String strReceivedDay = "";
	private String strSourceTypeID = "";
	private String strClaimTypeID = "";
	private String strRequestedAmt = "";
	private BigDecimal requestedAmt = null;//@Pattern(regexp="^(([0])*[1-9]{1}\\d{0,9}(\\.[0]{1,2})?)$|^\\s*$", message="RequestedAmt should be whole number")
	private Long lngOfficeSeqID = null;
	private String strOfficeName = "";
	//private String strStatusTypeID = "";
	//private String strRemarks = "";
	private Long lngCourierSeqID = null;
	private String strCourierNbr = "";
	private ArrayList<Object> alClaimDocumentVOList = null;
	//private ClaimantVO claimantVO = null;
	private String strClaimNbr = "";
	private String strShortfallID = "";
	private Long lngShortfallSeqID = null;
	private ArrayList alPrevClaimList = new ArrayList();
	private Long lngParentClmSeqID = null;
	private String strCurrentClaimNbr = "";
	private ArrayList alShortfallVO = null;
	
	
	private Long lngInwardSeqID = null;
	private Long lngClaimSeqID = null;
	private String strInwardNbr = "";
	//private String strEnrollmentID="";
	private String strClaimantName="";
	private String strGroupName = "";
	private String strClaimTypeDesc = "";
	//private Date dtReceivedDate = null;
	private Long lngClmEnrollDtlSeqID = null;
	//private String strClaimTypeID = "";
	private String strDocumentTypeID = "";
	 private String strNotifyPhoneNbr = "";
	 private String strHolderName = "";
	 private String strIfscCode = "";
	 private String strBankName = "";
	 private String strBranchCode = "";
	 private String strBranchAddress = "";
	 private String strAccountNumber = "";
	 private String dtAdmissionDate=null;
	 private String strAdmissionTime="";
	 private String strAdmissionDay="";
	 private String dtDischargeDate=null;
	 private String strDischargeTime="";
	 private String strDischargeDay="";
	 private String strAilmentDetail="";
	 private String strTreatmentDetail="";
	 private String strClaimSubType="";
	 private Long lngInsSeqID = null;
	 private Long lngPolicySeqID = null;
	private String strName = "";
	private String strPolicyHolderName = "";
	private String strEmployeeNbr = "";
    private String strEmployeeName = "";
    private String strInsCustCode =""; //ins_customer_code
    private String strCertificateNo =""; //certificate_no
    private String strInsScheme =""; //ins_scheme
    private Long lngClmSeqID = null;
    private String strAlterEmailID = "";
    private String strAlterPhone = "";
    private String stateId;
    private String stateName;
    private String cityID;
    private String cityName;
    private String hospDateofAdmission = "";
    private String physicianPhoneNo = "";
    private String nameOfPhysician = "";
    private String templateType = "";
    private String shortfallType = "";
    private String vType = "";
    private String param = "";
    private String[] documentListType;
    private String[] sheets;
    private String[] docTypeID;
    private String[] reasonTypeID;
    private Integer intIndex = 0;
    
    private String drugName= "";
    private String quantity= "";
    private String timesPerDay= "";
    private String noOfDays= "";
    private String intervalTypeId= "";
    private String medTimeTypeId= "";
    private Date startDate= null;
    private Long rem_seq_id= null;
    private byte[] claimSubmission ;
    private InputStream fileDataOInputStream = null;
	private int pdfFileSize = 0;
	
	private Long clm_batch_seq_id =null;
    private String clm_batch_number = "";
    private String invoice_Number = "";
    private String req_currency_type = "";
    private String dateOfBirth = "";
    private String password = "";
 
	private String address1 = "";
    private String address2 = "";
    private String address3 = "";
    private String pincode = "";
    private String countryId = "";
    private Long addressSeqID = null;
	private String genderGeneralId = "";

	private String medStartDate = "";
	private String fileType    = "";
	private String strMicr;//IBAN Number
	
	private String bankaccno    = "";
	private String bankacctype   = "";
	private String bankaccdestination   = "";
	private String bankstate   = "";
	private String bankacity   = "";
	private String bankbranch   = ""; 
	private String BenifitTypeVal;
	private String EnrollId;
	private Long HospSeqId = null;
	   private byte[] shortfallSubmission ;
	 
	 
	   private String SwiftCode;
	   private String BankAccountQatarYN;
	   private String bankBranchText;
	   private String bankCountry;
	   private String app_version;
	   private String device_type;
	   
	   
	    public String getMedStartDate() {
			return medStartDate;
		}

		public void setMedStartDate(String medStartDate) {
			this.medStartDate = medStartDate;
		}


    
    
    
	public String getAlterPhone() {
		return strAlterPhone;
	}
	public void setAlterPhone(String strAlterPhone) {
		this.strAlterPhone = strAlterPhone;
	}
	public String getAlterEmailID() {
		return strAlterEmailID;
	}
	public void setAlterEmailID(String strAlterEmailID) {
		this.strAlterEmailID = strAlterEmailID;
	}
    
    
    
   
	/**
	 * @return the lngClmSeqID
	 */
	public Long getClmSeqID() {
		return lngClmSeqID;
	}

	/**
	 * @param lngClmSeqID the lngClmSeqID to set
	 */
	public void setClmSeqID(Long lngClmSeqID) {
		this.lngClmSeqID = lngClmSeqID;
	}

	/**
	 * @return the strInsScheme
	 */
	public String getInsScheme() {
		return strInsScheme;
	}

	/**
	 * @param strInsScheme the strInsScheme to set
	 */
	public void setInsScheme(String strInsScheme) {
		this.strInsScheme = strInsScheme;
	}

	/**
	 * @return the strCertificateNo
	 */
	public String getCertificateNo() {
		return strCertificateNo;
	}

	/**
	 * @param strCertificateNo the strCertificateNo to set
	 */
	public void setCertificateNo(String strCertificateNo) {
		this.strCertificateNo = strCertificateNo;
	}

	/**
	 * @return the strInsCustCode
	 */
	public String getInsCustCode() {
		return strInsCustCode;
	}

	/**
	 * @param strInsCustCode the strInsCustCode to set
	 */
	public void setInsCustCode(String strInsCustCode) {
		this.strInsCustCode = strInsCustCode;
	}

	/**
	 * @return the strEmployeeName
	 */
	public String getEmployeeName() {
		return strEmployeeName;
	}

	/**
	 * @param strEmployeeName the strEmployeeName to set
	 */
	public void setEmployeeName(String strEmployeeName) {
		this.strEmployeeName = strEmployeeName;
	}

	/**
	 * @return the strEmployeeNbr
	 */
	public String getEmployeeNbr() {
		return strEmployeeNbr;
	}

	/**
	 * @param strEmployeeNbr the strEmployeeNbr to set
	 */
	public void setEmployeeNbr(String strEmployeeNbr) {
		this.strEmployeeNbr = strEmployeeNbr;
	}

	/**
		 * @return the strPolicyHolderName
		 */
		public String getPolicyHolderName() {
			return strPolicyHolderName;
		}

		/**
		 * @param strPolicyHolderName the strPolicyHolderName to set
		 */
		public void setPolicyHolderName(String strPolicyHolderName) {
			this.strPolicyHolderName = strPolicyHolderName;
		}

	/**
		 * @return the strName
		 */
		public String getName() {
			return strName;
		}

		/**
		 * @param strName the strName to set
		 */
		public void setName(String strName) {
			this.strName = strName;
		}

	/**
		 * @return the lngPolicySeqID
		 */
		public Long getPolicySeqID() {
			return lngPolicySeqID;
		}

		/**
		 * @param lngPolicySeqID the lngPolicySeqID to set
		 */
		public void setPolicySeqID(Long lngPolicySeqID) {
			this.lngPolicySeqID = lngPolicySeqID;
		}

	/**
	 * @return the lngInsSeqID
	 */
	public Long getInsSeqID() {
		return lngInsSeqID;
	}

	/**
	 * @param lngInsSeqID the lngInsSeqID to set
	 */
	public void setInsSeqID(Long lngInsSeqID) {
		this.lngInsSeqID = lngInsSeqID;
	}

	/**
	 * @return the strClaimSubType
	 */
	public String getClaimSubType() {
		return strClaimSubType;
	}

	/**
	 * @param strClaimSubType the strClaimSubType to set
	 */
	public void setClaimSubType(String strClaimSubType) {
		this.strClaimSubType = strClaimSubType;
	}

	/**
	 * @return the strTreatmentDetail
	 */
	public String getTreatmentDetail() {
		return strTreatmentDetail;
	}

	/**
	 * @param strTreatmentDetail the strTreatmentDetail to set
	 */
	public void setTreatmentDetail(String strTreatmentDetail) {
		this.strTreatmentDetail = strTreatmentDetail;
	}

	/**
	 * @return the strAilmentDetail
	 */
	public String getAilmentDetail() {
		return strAilmentDetail;
	}

	/**
	 * @param strAilmentDetail the strAilmentDetail to set
	 */
	public void setAilmentDetail(String strAilmentDetail) {
		this.strAilmentDetail = strAilmentDetail;
	}

	/**
	 * @return the strDischargeDay
	 */
	public String getDischargeDay() {
		return strDischargeDay;
	}

	/**
	 * @param strDischargeDay the strDischargeDay to set
	 */
	public void setDischargeDay(String strDischargeDay) {
		this.strDischargeDay = strDischargeDay;
	}

	/**
	 * @return the strDischargeTime
	 */
	public String getDischargeTime() {
		return strDischargeTime;
	}

	/**
	 * @param strDischargeTime the strDischargeTime to set
	 */
	public void setDischargeTime(String strDischargeTime) {
		this.strDischargeTime = strDischargeTime;
	}

	
	

	/**
	 * @return the strAdmissionDay
	 */
	public String getAdmissionDay() {
		return strAdmissionDay;
	}

	/**
	 * @param strAdmissionDay the strAdmissionDay to set
	 */
	public void setAdmissionDay(String strAdmissionDay) {
		this.strAdmissionDay = strAdmissionDay;
	}

	/**
	 * @return the strAdmissionTime
	 */
	public String getAdmissionTime() {
		return strAdmissionTime;
	}

	/**
	 * @param strAdmissionTime the strAdmissionTime to set
	 */
	public void setAdmissionTime(String strAdmissionTime) {
		this.strAdmissionTime = strAdmissionTime;
	}

	/**
	 * @return the dtAdmissionDate
	 */
	

	/**
	 * @param dtAdmissionDate the dtAdmissionDate to set
	 */
	

	/**
	 * @return the strAccountNumber
	 */
	public String getAccountNumber() {
		return strAccountNumber;
	}

	/**
	 * @param strAccountNumber the strAccountNumber to set
	 */
	public void setAccountNumber(String strAccountNumber) {
		this.strAccountNumber = strAccountNumber;
	}

	/**
	 * @return the strBranchAddress
	 */
	public String getBranchAddress() {
		return strBranchAddress;
	}

	/**
	 * @param strBranchAddress the strBranchAddress to set
	 */
	public void setBranchAddress(String strBranchAddress) {
		this.strBranchAddress = strBranchAddress;
	}

	/**
	 * @return the strBranchCode
	 */
	public String getBranchCode() {
		return strBranchCode;
	}

	/**
	 * @param strBranchCode the strBranchCode to set
	 */
	public void setBranchCode(String strBranchCode) {
		this.strBranchCode = strBranchCode;
	}

	/**
	 * @return the strBankName
	 */
	public String getBankName() {
		return strBankName;
	}

	/**
	 * @param strBankName the strBankName to set
	 */
	public void setBankName(String strBankName) {
		this.strBankName = strBankName;
	}

	/**
	 * @return the strIfscCode
	 */
	public String getIfscCode() {
		return strIfscCode;
	}

	/**
	 * @param strIfscCode the strIfscCode to set
	 */
	public void setIfscCode(String strIfscCode) {
		this.strIfscCode = strIfscCode;
	}

	/**
	 * @return the strHolderName
	 */
	public String getHolderName() {
		return strHolderName;
	}

	/**
	 * @param strHolderName the strHolderName to set
	 */
	public void setHolderName(String strHolderName) {
		this.strHolderName = strHolderName;
	}

	/**
	 * @return the strNotifyPhoneNbr
	 */
	public String getNotifyPhoneNbr() {
		return strNotifyPhoneNbr;
	}

	/**
	 * @param strNotifyPhoneNbr the strNotifyPhoneNbr to set
	 */
	public void setNotifyPhoneNbr(String strNotifyPhoneNbr) {
		this.strNotifyPhoneNbr = strNotifyPhoneNbr;
	}

	/** Retrieve the DocumentTypeID
	 * @return Returns the strDocumentTypeID.
	 */
	public String getDocumentTypeID() {
		return strDocumentTypeID;
	}//end of getDocumentTypeID()
	
	/** Sets the DocumentTypeID
	 * @param strDocumentTypeID The strDocumentTypeID to set.
	 */
	public void setDocumentTypeID(String strDocumentTypeID) {
		this.strDocumentTypeID = strDocumentTypeID;
	}//end of setDocumentTypeID(String strDocumentTypeID)
	


	/** Retrieve the ClmEnrollDtlSeqID
	 * @return Returns the lngClmEnrollDtlSeqID.
	 */
	public Long getClmEnrollDtlSeqID() {
		return lngClmEnrollDtlSeqID;
	}//end of getClmEnrollDtlSeqID()

	/** Sets the ClmEnrollDtlSeqID
	 * @param lngClmEnrollDtlSeqID The lngClmEnrollDtlSeqID to set.
	 */
	public void setClmEnrollDtlSeqID(Long lngClmEnrollDtlSeqID) {
		this.lngClmEnrollDtlSeqID = lngClmEnrollDtlSeqID;
	}//end of setClmEnrollDtlSeqID(Long lngClmEnrollDtlSeqID)

	/** Retrieve the ReceivedDate
	 * @return Returns the dtReceivedDate.
	 */
	
	/** Retrieve the ReceivedDate
	 * @return Returns the dtReceivedDate.
	 */
	
	/** Retrieve the ClaimSeqID
	 * @return Returns the lngClaimSeqID.
	 */
	public Long getClaimSeqID() {
		return lngClaimSeqID;
	}//end of getClaimSeqID()
	
	/** Sets the ClaimSeqID
	 * @param lngClaimSeqID The lngClaimSeqID to set.
	 */
	public void setClaimSeqID(Long lngClaimSeqID) {
		this.lngClaimSeqID = lngClaimSeqID;
	}//end of setClaimSeqID(Long lngClaimSeqID)
	
	/** Retrieve the InwardSeqID
	 * @return Returns the lngInwardSeqID.
	 */
	public Long getInwardSeqID() {
		return lngInwardSeqID;
	}//end of getInwardSeqID()
	
	/** Sets the InwardSeqID
	 * @param lngInwardSeqID The lngInwardSeqID to set.
	 */
	public void setInwardSeqID(Long lngInwardSeqID) {
		this.lngInwardSeqID = lngInwardSeqID;
	}//end of setInwardSeqID(Long lngInwardSeqID)
	
	/** Retrieve the ClaimantName
	 * @return Returns the strClaimantName.
	 */
	public String getClaimantName() {
		return strClaimantName;
	}//end of getClaimantName()
	
	/** Sets the ClaimantName
	 * @param strClaimantName The strClaimantName to set.
	 */
	public void setClaimantName(String strClaimantName) {
		this.strClaimantName = strClaimantName;
	}//end of setClaimantName(String strClaimantName)
	
	/** Retrieve the Claim Type Description
	 * @return Returns the strClaimTypeDesc.
	 */
	public String getClaimTypeDesc() {
		return strClaimTypeDesc;
	}//end of getClaimTypeDesc()
	
	/** Sets the Claim Type Description
	 * @param strClaimTypeDesc The strClaimTypeDesc to set.
	 */
	public void setClaimTypeDesc(String strClaimTypeDesc) {
		this.strClaimTypeDesc = strClaimTypeDesc;
	}//end of setClaimTypeDesc(String strClaimTypeDesc)
	

	/** Retrieve the GroupName
	 * @return Returns the strGroupName.
	 */
	public String getGroupName() {
		return strGroupName;
	}//end of getGroupName()
	
	/** Sets the GroupName
	 * @param strGroupName The strGroupName to set.
	 */
	public void setGroupName(String strGroupName) {
		this.strGroupName = strGroupName;
	}//end of setGroupName(String strGroupName)
	
	/** Retrieve the Inward Number
	 * @return Returns the strInwardNbr.
	 */
	public String getInwardNbr() {
		return strInwardNbr;
	}//end of getInwardNbr()
	
	/** Sets the Inward Number
	 * @param strInwardNbr The strInwardNbr to set.
	 */
	public void setInwardNbr(String strInwardNbr) {
		this.strInwardNbr = strInwardNbr;
	}//end of setInwardNbr(String strInwardNbr)

	
	
	
	/** Retrieve the ArrayList of ShortfallVO
	 * @return Returns the alShortfallVO.
	 */
	public ArrayList getShortfallVO() {
		return alShortfallVO;
	}//end of getShortfallVO()

	/** Sets the ArrayList of ShortfallVO
	 * @param alShortfallVO The alShortfallVO to set.
	 */
	public void setShortfallVO(ArrayList alShortfallVO) {
		this.alShortfallVO = alShortfallVO;
	}//end of setShortfallVO(ArrayList alShortfallVO)
	
	/** Retrieve the CurrentClaimNbr
	 * @return Returns the strCurrentClaimNbr.
	 */
	public String getCurrentClaimNbr() {
		return strCurrentClaimNbr;
	}//end of getCurrentClaimNbr()

	/** Sets the CurrentClaimNbr
	 * @param strCurrentClaimNbr The strCurrentClaimNbr to set.
	 */
	public void setCurrentClaimNbr(String strCurrentClaimNbr) {
		this.strCurrentClaimNbr = strCurrentClaimNbr;
	}//end of setCurrentClaimNbr(String strCurrentClaimNbr)

	/** Retrieve the ParentClmSeqID
	 * @return Returns the lngParentClmSeqID.
	 */
	public Long getParentClmSeqID() {
		return lngParentClmSeqID;
	}//end of getParentClmSeqID()

	/** Sets the ParentClmSeqID
	 * @param lngParentClmSeqID The lngParentClmSeqID to set.
	 */
	public void setParentClmSeqID(Long lngParentClmSeqID) {
		this.lngParentClmSeqID = lngParentClmSeqID;
	}//end of setParentClmSeqID(Long lngParentClmSeqID)

	/** Retrieve the PrevClaimList
	 * @return Returns the alPrevClaimList.
	 */
	public ArrayList getPrevClaimList() {
		return alPrevClaimList;
	}//end of getPrevClaimList()

	/** Sets the PrevClaimList
	 * @param alPrevClaimList The alPrevClaimList to set.
	 */
	public void setPrevClaimList(ArrayList alPrevClaimList) {
		this.alPrevClaimList = alPrevClaimList;
	}//end of setPrevClaimList(ArrayList alPrevClaimList)

	/** Retrieve the ShortfallID
	 * @return Returns the strShortfallID.
	 */
	public String getShortfallID() {
		return strShortfallID;
	}//end of getShortfallID()

	/** Sets the ShortfallID
	 * @param strShortfallID The strShortfallID to set.
	 */
	public void setShortfallID(String strShortfallID) {
		this.strShortfallID = strShortfallID;
	}//end of setShortfallID(String strShortfallID)

	/** Retrieve the ClaimNbr
	 * @return Returns the strClaimNbr.
	 */
	public String getClaimNbr() {
		return strClaimNbr;
	}//end of getClaimNbr()

	/** Sets the ClaimNbr
	 * @param strClaimNbr The strClaimNbr to set.
	 */
	public void setClaimNbr(String strClaimNbr) {
		this.strClaimNbr = strClaimNbr;
	}//end of setClaimNbr(String strClaimNbr)


	/** Retrieve the DocumentVOList
	 * @return Returns the alClaimDocumentVOList.
	 */
	public ArrayList<Object> getClaimDocumentVOList() {
		return alClaimDocumentVOList;
	}//end of getClaimDocumentVOList()

	/** Sets the DocumentVOList
	 * @param alClaimDocumentVOList The alClaimDocumentVOList to set.
	 */
	public void setClaimDocumentVOList(ArrayList<Object> alClaimDocumentVOList) {
		this.alClaimDocumentVOList = alClaimDocumentVOList;
	}//end of setClaimDocumentVOList(ArrayList alClaimDocumentVOList)

	/** Retrieve the CourierSeqID
	 * @return Returns the lngCourierSeqID.
	 */
	public Long getCourierSeqID() {
		return lngCourierSeqID;
	}//end of getCourierSeqID()
	
	/** Sets the CourierSeqID
	 * @param lngCourierSeqID The lngCourierSeqID to set.
	 */
	public void setCourierSeqID(Long lngCourierSeqID) {
		this.lngCourierSeqID = lngCourierSeqID;
	}//end of setCourierSeqID(Long lngCourierSeqID)
	
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
	
	/** Retrieve the Requested Amount
	 * @return Returns the bdRequestedAmt.
	 */
	public BigDecimal getRequestedAmt() {
		return requestedAmt;
	}//end of getRequestedAmt()
	
	/** Sets the Requested Amount
	 * @param bdRequestedAmt The bdRequestedAmt to set.
	 */
	public void setRequestedAmt(BigDecimal bdRequestedAmt) {
		this.requestedAmt = bdRequestedAmt;
	}//end of setRequestedAmt(BigDecimal bdRequestedAmt)
	
	/** Retrieve the Office Seq ID
	 * @return Returns the lngOfficeSeqID.
	 */
	public Long getOfficeSeqID() {
		return lngOfficeSeqID;
	}//end of getOfficeSeqID()
	
	/** Sets the Office Seq ID
	 * @param lngOfficeSeqID The lngOfficeSeqID to set.
	 */
	public void setOfficeSeqID(Long lngOfficeSeqID) {
		this.lngOfficeSeqID = lngOfficeSeqID;
	}//end of setOfficeSeqID(Long lngOfficeSeqID)
	
	/** Retrieve the BarCodeNbr
	 * @return Returns the strBarCodeNbr.
	 */
	public String getBarCodeNbr() {
		return strBarCodeNbr;
	}//end of getBarCodeNbr()
	
	/** Sets the BarCodeNbr
	 * @param strBarCodeNbr The strBarCodeNbr to set.
	 */
	public void setBarCodeNbr(String strBarCodeNbr) {
		this.strBarCodeNbr = strBarCodeNbr;
	}//end of setBarCodeNbr(String strBarCodeNbr)
	
	/** Retrieve the ClaimTypeID
	 * @return Returns the strClaimTypeID.
	 */
	public String getClaimTypeID() {
		return strClaimTypeID;
	}//end of getClaimTypeID()
	
	/** Sets the ClaimTypeID
	 * @param strClaimTypeID The strClaimTypeID to set.
	 */
	public void setClaimTypeID(String strClaimTypeID) {
		this.strClaimTypeID = strClaimTypeID;
	}//end of setClaimTypeID(String strClaimTypeID)
	
	/** Retrieve the Document Type Description
	 * @return Returns the strDocumentTypeDesc.
	 */
	public String getDocumentTypeDesc() {
		return strDocumentTypeDesc;
	}//end of getDocumentTypeDesc()
	
	/** Sets the Document Type Description
	 * @param strDocumentTypeDesc The strDocumentTypeDesc to set.
	 */
	public void setDocumentTypeDesc(String strDocumentTypeDesc) {
		this.strDocumentTypeDesc = strDocumentTypeDesc;
	}//end of setDocumentTypeDesc(String strDocumentTypeDesc)
	
	/** Retrieve the Office Name
	 * @return Returns the strOfficeName.
	 */
	public String getOfficeName() {
		return strOfficeName;
	}//end of getOfficeName()
	
	/** Sets the Office Name
	 * @param strOfficeName The strOfficeName to set.
	 */
	public void setOfficeName(String strOfficeName) {
		this.strOfficeName = strOfficeName;
	}//end of setOfficeName(String strOfficeName)
	
	/** Retrieve the ReceivedDay
	 * @return Returns the strReceivedDay.
	 */
	public String getReceivedDay() {
		return strReceivedDay;
	}//end of getReceivedDay()
	
	/** Sets the ReceivedDay
	 * @param strReceivedDay The strReceivedDay to set.
	 */
	public void setReceivedDay(String strReceivedDay) {
		this.strReceivedDay = strReceivedDay;
	}//end of setReceivedDay(String strReceivedDay)
	
	/** Retrieve the ReceivedTime
	 * @return Returns the strReceivedTime.
	 */
	public String getReceivedTime() {
		return strReceivedTime;
	}//end of getReceivedTime()
	
	/** Sets the ReceivedTime
	 * @param strReceivedTime The strReceivedTime to set.
	 */
	public void setReceivedTime(String strReceivedTime) {
		this.strReceivedTime = strReceivedTime;
	}//end of setReceivedTime(String strReceivedTime)
	

	/** Retrieve the SourceTypeID
	 * @return Returns the strSourceTypeID.
	 */
	public String getSourceTypeID() {
		return strSourceTypeID;
	}//end of getSourceTypeID()
	
	/** Sets the SourceTypeID
	 * @param strSourceTypeID The strSourceTypeID to set.
	 */
	public void setSourceTypeID(String strSourceTypeID) {
		this.strSourceTypeID = strSourceTypeID;
	}//end of setSourceTypeID(String strSourceTypeID)
	

	/** Retrieve the ShortfallSeqID
	 * @return Returns the lngShortfallSeqID.
	 */
	public Long getShortfallSeqID() {
		return lngShortfallSeqID;
	}//end of getShortfallSeqID()

	/** Sets the ShortfallSeqID
	 * @param lngShortfallSeqID The lngShortfallSeqID to set.
	 */
	public void setShortfallSeqID(Long lngShortfallSeqID) {
		this.lngShortfallSeqID = lngShortfallSeqID;
	}//end of setShortfallSeqID(Long lngShortfallSeqID)

	
	/** Retrieve the TTKNarrative
	 * @return Returns the strTTKNarrative.
	 */
	public String getTTKNarrative() {
		return strTTKNarrative;
	}//end of getTTKNarrative()

	/** Sets the TTKNarrative
	 * @param strTTKNarrative The strTTKNarrative to set.
	 */
	public void setTTKNarrative(String strTTKNarrative) {
		this.strTTKNarrative = strTTKNarrative;
	}//end of setTTKNarrative(String strTTKNarrative)

	/** Retrieve the EstimatedCost
	 * @return Returns the bdEstimatedCost.
	 */
	public BigDecimal getEstimatedCost() {
		return bdEstimatedCost;
	}//end of getEstimatedCost()

	/** Sets the EstimatedCost
	 * @param bdEstimatedCost The bdEstimatedCost to set.
	 */
	public void setEstimatedCost(BigDecimal bdEstimatedCost) {
		this.bdEstimatedCost = bdEstimatedCost;
	}//end of setEstimatedCost(BigDecimal bdEstimatedCost)

	/** Retrieve the HospAddress
	 * @return Returns the strHospAddress.
	 */
	public String getHospAddress() {
		return strHospAddress;
	}//end of getHospAddress()

	/** Sets the HospAddress
	 * @param strHospAddress The strHospAddress to set.
	 */
	public void setHospAddress(String strHospAddress) {
		this.strHospAddress = strHospAddress;
	}//end of setHospAddress(String strHospAddress)

	/** Retrieve the HospitalName
	 * @return Returns the strHospitalName.
	 */
	public String getHospitalName() {
		return hospitalName;
	}//end of getHospitalName()

	/** Sets the HospitalName
	 * @param strHospitalName The strHospitalName to set.
	 */
	public void setHospitalName(String strHospitalName) {
		this.hospitalName = strHospitalName;
	}//end of setHospitalName(String strHospitalName)

	/** Retrieve the IntGenDay
	 * @return Returns the strIntGenDay.
	 */
	public String getIntGenDay() {
		return strIntGenDay;
	}//end of getIntGenDay()

	/** Sets the IntGenDay
	 * @param strIntGenDay The strIntGenDay to set.
	 */
	public void setIntGenDay(String strIntGenDay) {
		this.strIntGenDay = strIntGenDay;
	}//end of setIntGenDay(String strIntGenDay)

	/** Retrieve the IntGenTime
	 * @return Returns the strIntGenTime.
	 */
	public String getIntGenTime() {
		return strIntGenTime;
	}//end of getIntGenTime()

	/** Sets the IntGenTime
	 * @param strIntGenTime The strIntGenTime to set.
	 */
	public void setIntGenTime(String strIntGenTime) {
		this.strIntGenTime = strIntGenTime;
	}//end of setIntGenTime(String strIntGenTime)

	/** Retrieve the TTKRespondedDay
	 * @return Returns the strTTKRespondedDay.
	 */
	public String getTTKRespondedDay() {
		return strTTKRespondedDay;
	}//end of getTTKRespondedDay()

	/** Sets the TTKRespondedDay
	 * @param strTTKRespondedDay The strTTKRespondedDay to set.
	 */
	public void setTTKRespondedDay(String strTTKRespondedDay) {
		this.strTTKRespondedDay = strTTKRespondedDay;
	}//end of setTTKRespondedDay(String strTTKRespondedDay)

	/** Retrieve the TTKRespondedTime
	 * @return Returns the strTTKRespondedTime.
	 */
	public String getTTKRespondedTime() {
		return strTTKRespondedTime;
	}//end of getTTKRespondedTime()

	/** Sets the TTKRespondedTime
	 * @param strTTKRespondedTime The strTTKRespondedTime to set.
	 */
	public void setTTKRespondedTime(String strTTKRespondedTime) {
		this.strTTKRespondedTime = strTTKRespondedTime;
	}//end of setTTKRespondedTime(String strTTKRespondedTime)

	/** Retrieve the StatusTypeID
	 * @return Returns the strStatusTypeID.
	 */
	public String getStatusTypeID() {
		return strStatusTypeID;
	}//end of getStatusTypeID()

	/** Sets the StatusTypeID
	 * @param strStatusTypeID The strStatusTypeID to set.
	 */
	public void setStatusTypeID(String strStatusTypeID) {
		this.strStatusTypeID = strStatusTypeID;
	}//end of setStatusTypeID(String strStatusTypeID)

	/** Retrieve the PolicyNbr
	 * @return Returns the strPolicyNbr.
	 */
	public String getPolicyNbr() {
		return strPolicyNbr;
	}//end of getPolicyNbr()

	/** Sets the PolicyNbr
	 * @param strPolicyNbr The strPolicyNbr to set.
	 */
	public void setPolicyNbr(String strPolicyNbr) {
		this.strPolicyNbr = strPolicyNbr;
	}//end of setPolicyNbr(String strPolicyNbr)

	/** Retrieve the StatusDesc
	 * @return Returns the strStatusDesc.
	 */
	public String getStatusDesc() {
		return strStatusDesc;
	}//end of getStatusDesc()

	/** Sets the StatusDesc
	 * @param strStatusDesc The strStatusDesc to set.
	 */
	public void setStatusDesc(String strStatusDesc) {
		this.strStatusDesc = strStatusDesc;
	}//end of setStatusDesc(String strStatusDesc)

	/** Retrieve the TTKRespondedDate
	 * @return Returns the dtTTKRespondedDate.
	 */
	
	/** Sets the TTKRespondedDate
	 * @param dtTTKRespondedDate The dtTTKRespondedDate to set.
	 */
	
	/** Retrieve the TTKRemarks
	 * @return Returns the strTTKRemarks.
	 */
	public String getTTKRemarks() {
		return strTTKRemarks;
	}//end of getTTKRemarks()

	/** Sets the TTKRemarks
	 * @param strTTKRemarks The strTTKRemarks to set.
	 */
	public void setTTKRemarks(String strTTKRemarks) {
		this.strTTKRemarks = strTTKRemarks;
	}//end of setTTKRemarks(String strTTKRemarks)

	/** Retrieve the onlineHospitalVO
	 * @return Returns the onlineHospitalVO.
	 */
	

	/** Sets the onlineHospitalVO
	 * @param onlineHospitalVO The onlineHospitalVO to set.
	 */
	

	/** Retrieve the Expected DOA
	 * @return Returns the dtExpectedDOA.
	 */
	
	/** Sets the Expected DOA
	 * @param dtExpectedDOA The dtExpectedDOA to set.
	 */
	
	
	/** Retrieve the Expected DOD
	 * @return Returns the dtExpectedDOD.
	 */
	
	/** Sets the Expected DOD
	 * @param dtExpectedDOD The dtExpectedDOD to set.
	 */
	
	/** This method returns the Intimate Generated Date
     * @return Returns the dtIntGenDate.
     */
   
	/** Retrieve the Intimate Generated Date
	 * @return Returns the dtIntGenDate.
	 */
	
	/** Sets the Intimate Generated Date
	 * @param dtIntGenDate The dtIntGenDate to set.
	 */
	
	
	/** Retrieve the Intimation SeqID.
	 * @return Returns the lngIntimationSeqID.
	 */
	public Long getIntimationSeqID() {
		return lngIntimationSeqID;
	}//end of getIntimationSeqID()
	
	/** Sets the Intimation SeqID.
	 * @param lngIntimationSeqID The lngIntimationSeqID to set.
	 */
	public void setIntimationSeqID(Long lngIntimationSeqID) {
		this.lngIntimationSeqID = lngIntimationSeqID;
	}//end of setIntimationSeqID(Long lngIntimationSeqID)
	
	/** Retrieve the Member SeqID
	 * @return Returns the lngMemberSeqID.
	 */
	public Long getMemberSeqID() {
		return memberSeqID;
	}//end of getMemberSeqID() 
	
	/** Sets the Member SeqID
	 * @param lngMemberSeqID The lngMemberSeqID to set.
	 */
	public void setMemberSeqID(Long lngMemberSeqID) {
		this.memberSeqID = lngMemberSeqID;
	}//end of setMemberSeqID(Long lngMemberSeqID)
	
	/** Retrieve the Policy Group SeqID
	 * @return Returns the lngPolicyGrpSeqID.
	 */
	public Long getPolicyGrpSeqID() {
		return policyGrpSeqID;
	}//end of getPolicyGrpSeqID() 
	
	/** Sets the Policy Group SeqID
	 * @param lngPolicyGrpSeqID The lngPolicyGrpSeqID to set.
	 */
	public void setPolicyGrpSeqID(Long lngPolicyGrpSeqID) {
		this.policyGrpSeqID = lngPolicyGrpSeqID;
	}//end of setPolicyGrpSeqID(Long lngPolicyGrpSeqID) 
	
	/** Retrieve the Ailment Description
	 * @return Returns the strAilmentDesc.
	 */
	public String getAilmentDesc() {
		return ailmentDesc;
	}//end of getAilmentDesc() 
	
	/** Sets the Ailment Description
	 * @param strAilmentDesc The strAilmentDesc to set.
	 */
	public void setAilmentDesc(String strAilmentDesc) {
		this.ailmentDesc = strAilmentDesc;
	}//end of setAilmentDesc(String strAilmentDesc)
	
	/** Retrieve the Email ID
	 * @return Returns the strEmailID.
	 */
	public String getEmailID() {
		return strEmailID;
	}//end of getEmailID()
	
	/** Sets the Email ID
	 * @param strEmailID The strEmailID to set.
	 */
	public void setEmailID(String strEmailID) {
		this.strEmailID = strEmailID;
	}//end of setEmailID(String strEmailID)
	
	/** Retrieve the Enrollment ID
	 * @return Returns the strEnrollmentID.
	 */
	public String getEnrollmentID() {
		return strEnrollmentID;
	}//end of getEnrollmentID() 
	
	/** Sets the Enrollment ID
	 * @param strEnrollmentID The strEnrollmentID to set.
	 */
	public void setEnrollmentID(String strEnrollmentID) {
		this.strEnrollmentID = strEnrollmentID;
	}//end of setEnrollmentID(String strEnrollmentID) 
	
	/** Retrieve the Gender Description
	 * @return Returns the strGenderDesc.
	 */
	public String getGenderDesc() {
		return strGenderDesc;
	}//end of getGenderDesc() 
	
	/** Sets the Gender Description
	 * @param strGenderDesc The strGenderDesc to set.
	 */
	public void setGenderDesc(String strGenderDesc) {
		this.strGenderDesc = strGenderDesc;
	}//end of setGenderDesc(String strGenderDesc)
	
	/** Retrieve the Intimation Number
	 * @return Returns the strIntimationNbr.
	 */
	public String getIntimationNbr() {
		return strIntimationNbr;
	}//end of getIntimationNbr()
	
	/** Sets the Intimation Number
	 * @param strIntimationNbr The strIntimationNbr to set.
	 */
	public void setIntimationNbr(String strIntimationNbr) {
		this.strIntimationNbr = strIntimationNbr;
	}//end of setIntimationNbr(String strIntimationNbr) 
	
	/** Retrieve the Member Name
	 * @return Returns the strMemName.
	 */
	public String getMemName() {
		return strMemName;
	}//end of getMemName
	
	/** Sets the Member Name
	 * @param strMemName The strMemName to set.
	 */
	public void setMemName(String strMemName) {
		this.strMemName = strMemName;
	}//end of setMemName(String strMemName) 
	
	/** Retrieve the Mobile Number
	 * @return Returns the strMobileNbr.
	 */
	public String getMobileNbr() {
		return strMobileNbr;
	}//end of getMobileNbr()
	
	/** Sets the Mobile Number
	 * @param strMobileNbr The strMobileNbr to set.
	 */
	public void setMobileNbr(String strMobileNbr) {
		this.strMobileNbr = strMobileNbr;
	}//end of setMobileNbr(String strMobileNbr) 
	
	/** Retrieve the Phone Number
	 * @return Returns the strPhoneNbr.
	 */
	public String getPhoneNbr() {
		return strPhoneNbr;
	}//end of getPhoneNbr()
	
	/** Sets the Phone Number
	 * @param strPhoneNbr The strPhoneNbr to set.
	 */
	public void setPhoneNbr(String strPhoneNbr) {
		this.strPhoneNbr = strPhoneNbr;
	}//end of setPhoneNbr(String strPhoneNbr)
	
	/** Retrieve the Provisional Diagnosis
	 * @return Returns the strProvisionalDiagnosis.
	 */
	public String getProvisionalDiagnosis() {
		return strProvisionalDiagnosis;
	}//end of getProvisionalDiagnosis()
	
	/** Sets the Provisional Diagnosis
	 * @param strProvisionalDiagnosis The strProvisionalDiagnosis to set.
	 */
	public void setProvisionalDiagnosis(String strProvisionalDiagnosis) {
		this.strProvisionalDiagnosis = strProvisionalDiagnosis;
	}//end of setProvisionalDiagnosis(String strProvisionalDiagnosis)
	
	/** Retrieve the Remarks
	 * @return Returns the strRemarks.
	 */
	public String getRemarks() {
		return strRemarks;
	}//end of getRemarks()
	
	/** Sets the Remarks
	 * @param strRemarks The strRemarks to set.
	 */
	public void setRemarks(String strRemarks) {
		this.strRemarks = strRemarks;
	}//end of setRemarks(String strRemarks)
	
	/** Retrieve the Submitted YN.
	 * @return Returns the strSubmittedYN.
	 */
	public String getSubmittedYN() {
		return strSubmittedYN;
	}//end of getSubmittedYN()
	
	/** Sets the Submitted YN.
	 * @param strSubmittedYN The strSubmittedYN to set.
	 */
	public void setSubmittedYN(String strSubmittedYN) {
		this.strSubmittedYN = strSubmittedYN;
	}//end of setSubmittedYN(String strSubmittedYN)
	public String getIntGenDate() {
		return intGenDate;
	}
	public void setIntGenDate(String intGenDate) {
		this.intGenDate = intGenDate;
	}
	public String getStateId() {
		return stateId;
	}
	public void setStateId(String stateId) {
		this.stateId = stateId;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getCityID() {
		return cityID;
	}
	public void setCityID(String cityID) {
		this.cityID = cityID;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getHospDateofAdmission() {
		return hospDateofAdmission;
	}
	public void setHospDateofAdmission(String hospDateofAdmission) {
		this.hospDateofAdmission = hospDateofAdmission;
	}
	public String getPhysicianPhoneNo() {
		return physicianPhoneNo;
	}
	public void setPhysicianPhoneNo(String physicianPhoneNo) {
		this.physicianPhoneNo = physicianPhoneNo;
	}
	public String getNameOfPhysician() {
		return nameOfPhysician;
	}
	public void setNameOfPhysician(String nameOfPhysician) {
		this.nameOfPhysician = nameOfPhysician;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public String getShortfallType() {
		return shortfallType;
	}

	public void setShortfallType(String shortfallType) {
		this.shortfallType = shortfallType;
	}

	public String getvType() {
		return vType;
	}

	public void setvType(String vType) {
		this.vType = vType;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getDtAdmissionDate() {
		return dtAdmissionDate;
	}

	public void setDtAdmissionDate(String dtAdmissionDate) {
		this.dtAdmissionDate = dtAdmissionDate;
	}

	public String getDtDischargeDate() {
		return dtDischargeDate;
	}

	public void setDtDischargeDate(String dtDischargeDate) {
		this.dtDischargeDate = dtDischargeDate;
	}

	public String[] getDocumentListType() {
		return documentListType;
	}

	public void setDocumentListType(String[] documentListType) {
		this.documentListType = documentListType;
	}

	public String[] getSheets() {
		return sheets;
	}

	public void setSheets(String[] sheets) {
		this.sheets = sheets;
	}

	public String[] getDocTypeID() {
		return docTypeID;
	}

	public void setDocTypeID(String[] docTypeID) {
		this.docTypeID = docTypeID;
	}

	public String[] getReasonTypeID() {
		return reasonTypeID;
	}

	public void setReasonTypeID(String[] reasonTypeID) {
		this.reasonTypeID = reasonTypeID;
	}

	
	public String getStrRequestedAmt() {
		return strRequestedAmt;
	}
	public void setStrRequestedAmt(String strRequestedAmt) {
		this.strRequestedAmt = strRequestedAmt;
	}
	public Integer getIndex() {
		return intIndex;
	}
	public void setIndex(Integer intIndex) {
		this.intIndex = intIndex;
	}
	public String getDrugName() {
		return drugName;
	}
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getTimesPerDay() {
		return timesPerDay;
	}
	public void setTimesPerDay(String timesPerDay) {
		this.timesPerDay = timesPerDay;
	}
	public String getNoOfDays() {
		return noOfDays;
	}
	public void setNoOfDays(String noOfDays) {
		this.noOfDays = noOfDays;
	}
	public String getIntervalTypeId() {
		return intervalTypeId;
	}
	public void setIntervalTypeId(String intervalTypeId) {
		this.intervalTypeId = intervalTypeId;
	}
	public String getMedTimeTypeId() {
		return medTimeTypeId;
	}
	public void setMedTimeTypeId(String medTimeTypeId) {
		this.medTimeTypeId = medTimeTypeId;
	}
	
	public Long getRem_seq_id() {
		return rem_seq_id;
	}
	public void setRem_seq_id(Long rem_seq_id) {
		this.rem_seq_id = rem_seq_id;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public byte[] getClaimSubmission() {
		return claimSubmission;
	}
	public void setClaimSubmission(byte[] claimSubmission) {
		this.claimSubmission = claimSubmission;
	}
	public InputStream getFileDataOInputStream() {
		return fileDataOInputStream;
	}
	public void setFileDataOInputStream(InputStream fileDataOInputStream) {
		this.fileDataOInputStream = fileDataOInputStream;
	}
	public int getPdfFileSize() {
		return pdfFileSize;
	}
	public void setPdfFileSize(int pdfFileSize) {
		this.pdfFileSize = pdfFileSize;
	}
	
	public Long getClm_batch_seq_id() {
		return clm_batch_seq_id;
	}
	public void setClm_batch_seq_id(Long clm_batch_seq_id) {
		this.clm_batch_seq_id = clm_batch_seq_id;
	}
	public String getClm_batch_number() {
		return clm_batch_number;
	}
	public void setClm_batch_number(String clm_batch_number) {
		this.clm_batch_number = clm_batch_number;
	}
	public String getInvoice_Number() {
		return invoice_Number;
	}
	public void setInvoice_Number(String invoice_Number) {
		this.invoice_Number = invoice_Number;
	}
	public String getReq_currency_type() {
		return req_currency_type;
	}
	public void setReq_currency_type(String req_currency_type) {
		this.req_currency_type = req_currency_type;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	   public String getAddress1() {
			return address1;
		}
		public void setAddress1(String address1) {
			this.address1 = address1;
		}
		public String getAddress2() {
			return address2;
		}
		public void setAddress2(String address2) {
			this.address2 = address2;
		}
		public String getAddress3() {
			return address3;
		}
		public void setAddress3(String address3) {
			this.address3 = address3;
		}
	
		public String getPincode() {
			return pincode;
		}
		public void setPincode(String pincode) {
			this.pincode = pincode;
		}
		public String getCountryId() {
			return countryId;
		}
		public void setCountryId(String countryId) {
			this.countryId = countryId;
		}
		public Long getAddressSeqID() {
			return addressSeqID;
		}
		public void setAddressSeqID(Long addressSeqID) {
			this.addressSeqID = addressSeqID;
		}
		public String getGenderGeneralId() {
			return genderGeneralId;
		}
		public void setGenderGeneralId(String genderGeneralId) {
			this.genderGeneralId = genderGeneralId;
		}

		public String getFileType() {
			return fileType;
		}

		public void setFileType(String fileType) {
			this.fileType = fileType;
		}
	
	

	/*public String getDocumentListType() {
		return documentListType;
	}

	public void setDocumentListType(String documentListType) {
		this.documentListType = documentListType;
	}*/
		
		public String getMicr() {
			return strMicr;
		}
		
		public void setMicr(String strMicr) {
			this.strMicr = strMicr;
		}
		
		public Long getHospSeqId() {
			return HospSeqId;
		}//end of getMemberSeqID() 
		
		public void setHospSeqId(Long HospSeqId) {
			this.HospSeqId = HospSeqId;
		}//end of setMemberSeqID(Long lngMemberSeqID)
		
		
		public String getEnrollId() {
			return EnrollId;
		}//end of getMemberSeqID() 
		
		public void setEnrollId(String EnrollId) {
			this.EnrollId = EnrollId;
		}//end of setMemberSeqID(Long lngMemberSeqID)
		
		
		public String getBenifitTypeVal() {
			return BenifitTypeVal;
		}//end of getMemberSeqID() 
		
		public void setBenifitTypeVal(String BenifitTypeVal) {
			this.BenifitTypeVal = BenifitTypeVal;
		}//end of setMemberSeqID(Long lngMemberSeqID)
		
		
		
		
		
		

		public String getBankaccno() {
			return bankaccno;
		}

		public void setBankaccno(String bankaccno) {
			this.bankaccno = bankaccno;
		}

		public String getBankacctype() {
			return bankacctype;
		}

		public void setBankacctype(String bankacctype) {
			this.bankacctype = bankacctype;
		}

		public String getBankaccdestination() {
			return bankaccdestination;
		}

		public void setBankaccdestination(String bankaccdestination) {
			this.bankaccdestination = bankaccdestination;
		}

		public String getBankstate() {
			return bankstate;
		}

		public void setBankstate(String bankstate) {
			this.bankstate = bankstate;
		}

		public String getBankacity() {
			return bankacity;
		}

		public void setBankacity(String bankacity) {
			this.bankacity = bankacity;
		}

		public String getBankbranch() {
			return bankbranch;
		}

		public void setBankbranch(String bankbranch) {
			this.bankbranch = bankbranch;
		}
		public byte[] getShortfallSubmission() {
			return shortfallSubmission;
		}
		public void setShortfallSubmission(byte[] shortfallSubmission) {
			this.shortfallSubmission = shortfallSubmission;
		}

		public String getSwiftCode() {
			return SwiftCode;
		}

		public void setSwiftCode(String swiftCode) {
			SwiftCode = swiftCode;
		}

		public String getBankAccountQatarYN() {
			return BankAccountQatarYN;
		}

		public void setBankAccountQatarYN(String bankAccountQatarYN) {
			BankAccountQatarYN = bankAccountQatarYN;
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

		public String getApp_version() {
			return app_version;
		}

		public void setApp_version(String app_version) {
			this.app_version = app_version;
		}

		public String getDevice_type() {
			return device_type;
		}

		public void setDevice_type(String device_type) {
			this.device_type = device_type;
		}
}
