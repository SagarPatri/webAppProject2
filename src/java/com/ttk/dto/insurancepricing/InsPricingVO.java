/**
 * @ (#) InsuranceFeedbackVO.java Oct 24, 2005
 * Project      : ttkHealthCareServices
 * File         : InsuranceFeedbackVO.java
 * Author       : Ravindra
 * Company      : SpanSystem Corp:
 * Date Created : Oct 24, 2005
 *
 * @author       :  Ravindra

 * Modified by   : 
 * Modified date : 
 * Reason        : 
 */
package com.ttk.dto.insurancepricing;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;

import com.ttk.common.TTKCommon;
import com.ttk.dto.BaseVO;

import java.util.ArrayList;

import org.apache.struts.upload.FormFile;
public class InsPricingVO extends BaseVO{
     
	 private Long finaldataSeqID=null;
    private String country="";
    private String currency="";
    private String groupName="";
    private String noofEmployees="";
    private String additionalPremium="";
    private String averageAge="";
    private String employeeGender="";
    private String familyCoverage="";
    private String globalCoverge="";
    private Long groupProfileSeqID= null;
    private String[] profileID=null;
    private String[] profileValue=null;
    private String[] profileGroup=null;
    private String[] profilePercentage=null;
    private Long incomeProfileSeqID= null;
    private String profileID1=null;
    private String profileValue1=null;
    private String profileGroup1=null;
    private String profilePercentage1=null;
    private Long transProfileSeqID1= null;
    private Long[] transProfileSeqID= null;
    private String profileGroupValue1=null;
    private String[] profileGroupValue=null;
    private String nameOfInsurer="";
    private String nameOfTPA="";
    private String eligibility="";
    private String takafulQuote="";
    private String planName="";


	private String benecoverFlagYN = "";
    private String calCPMFlagYN = "";
    //private ArrayList profileIncomeList = null;
    
    private String areaOfCover="";
    private String newdataentry = "";
    private String pricingNumberAlert = "";
    
    private String notifyerror="";   // added by govind
    private String successMsg = "";  // added by govind
    
    //sw pricing start
    private Date coverStartDate=null;
    private Date coverEndDate=null;
    private String clientCode="";
    private String previousPolicyNo="";
    private String pricingRefno="";    
    private String totalCovedLives="";
    private String totalLivesMaternity="";
    private String trendFactor="";
    private String comments = "";
    
    
    private Long[] benf_typeseqid= null;
    private String[] benfdesc=null;
    private Long[] gndrtypeseqid= null;
    private String[] gndrdesc=null;
    private Long[] age_rngseqid= null;
    private String[] age_range=null;
    private String[] ovrprtflio_dstr= null;
    private Long[] natl_typeseqid= null;
    private String[] natl_name=null;
    private String[] totalCoverdLives=null;
    private Long[] benf_lives_seq_id=null;
    private String[] natCoverdLives=null;
    private Long[] natl_seqid= null;
    private String[] natovrprtflio_dstr= null;

    
    

    private Long benf_lives_seq_id1 = null;
    private Long benf_typeseqid1= null;
    private String benfdesc1 ="";
    private Long gndrtypeseqid1= null;
    private String gndrdesc1="";
    private Long age_rngseqid1= null;
    private String age_range1="";
    private String ovrprtflio_dstr1=null;
    private Long natl_typeseqid1= null;
    private String natl_name1="";
    private String totalCoverdLives1="";
    private String natCoverdLives1="";
    private Long natl_seqid1=null;
    private String natovrprtflio_dstr1=null;
    

    
    
    private String renewalYN = "";
    private String maternityYN = "";
    private String  natcategorylist="";
 	private String areaOfCoverList="";
    private String networkList="";
    private String  maxBenifitList="";
    private String  dentalLimitList="";
    private String maternityLimitList="";
    private String opticalLimitList="";
    private String opCopayList="";
    private String opCopayListDesc="";
    private String dentalcopayListDesc="";
    private String opticalCopayListDesc="";
    private String opDeductableListDesc="";
    
    private String opDeductableList="";
    private String  dentalcopayList="";
    private String  opticalCopayList="";
    private String  opticalFrameLimitList="";
    private String  physiocoverage="";
    private String vitDcoverage="";
    private String vaccImmuCoverage="";
    private String matComplicationCoverage="";
    private String  psychiatrycoverage="";
    private String deviatedNasalSeptum="";
    private String obesityTreatment="";
	
    private String dentalYN ="";
    private String dentalLivesYN="";
    private String opticalYN="";
    private String opticalLivesYN="";
    private String  maxBeneLimitOth = "";
    private String  dentalLimitOth = "";
    private String  opticalLimitOth = "";
    private String maternityLimitOth = "";
  
    private String loadingFlagYN = "";
    private String pricingDocs = "";
    private String strPolicyNumberFlag = "";    
    private String sumTotalLives = "";
    private String sumTotalLivesMaternity = "";
    private String sumTotalLivesDental = "";
    private String sumTotalLivesOptical = "";
    private String sumNationalityLives = "";
    private String policyNumber = "";
    private String clientName="";
    private String completeSaveYN="";
   
    
    private FormFile sourceAttchments1; 
    private FormFile sourceAttchments2; 
    private FormFile sourceAttchments3; 
    private FormFile sourceAttchments4; 
    private FormFile sourceAttchments5; 
    
    private InputStream inputstreamdoc1 = null;
    private InputStream inputstreamdoc2 = null;
    private InputStream inputstreamdoc3 = null;
    private InputStream inputstreamdoc4 = null;
    private InputStream inputstreamdoc5 = null;
    
    private String attachmentname1="";
    private String attachmentname2="";
    private String attachmentname3="";
    private String attachmentname4="";
    private String attachmentname5="";    
  
	private String inpatientBenefit="";
    private String outpatientBenefit="";
    private String gastricBinding="";
    private String healthScreen="";
    private String chronicLimit="";
    private String orthodonticsCopay="";
    private String orthodonticsCopayDesc="";
    private String opCopaypharmacy="";
    private String opCopaypharmacyDesc="";
    private String opInvestigation="";
    private String opInvestigationDesc="";
    private String opCopyconsultn="";
    private String opCopyconsultnDesc="";
    private String opCopyothers = "";
    private String opCopyothersDesc = "";
    private String  alAhlihospital="";
    private String opCopyalahlihosp="";
    private String opCopyalahlihospDesc="";
    private String opPharmacyAlAhli="";
    private String opPharmacyAlAhliDesc="";
    private String  opConsultAlAhli="";
    private String  opConsultAlAhliDesc="";
    private String  opInvestnAlAhli="";
    private String  opInvestnAlAhliDesc="";
    private String opothersAlAhli = "";  
    private String opothersAlAhliDesc = "";  
    private String alAhlihospOPservices = "";

    private String  chronicLimitOth="";
    private String opdeductableserviceYN= "";
    private String demographicflagYN= "";
    private String alertmsgscreen1 = "";
    private String pricingmodifyYN = "";

 
    private String policycategory = "";  
   
    private String inpAccomodation="";
    private Long inpAccmdlimit=null;
    private String inpAccmdCopay="";
    private String inpAccmdDeductable="";
    private double numberOfLives=0.0d;
    private String completeSaveYNInSc2="";
    private String riskPremiumDate="";
    private String ipCopay="";
    private String ipCopayPerc;
    private String ipCopayAtAlAhli;
    private String ipCopayAtAlAhliPerc;
    private String maternityCopay;
    private String maternityCopayPerc;
    
    
    public String getSuccessMsg() {
		return successMsg;
	}

	public void setSuccessMsg(String successMsg) {
		this.successMsg = successMsg;
	}

	public String getNotifyerror() {
		return notifyerror;
	}

	public void setNotifyerror(String notifyerror) {
		this.notifyerror = notifyerror;
	}
    
    public InputStream getInputstreamdoc1() {
		return inputstreamdoc1;
	}

	public void setInputstreamdoc1(InputStream inputstreamdoc1) {
		this.inputstreamdoc1 = inputstreamdoc1;
	}

	public InputStream getInputstreamdoc2() {
		return inputstreamdoc2;
	}

	public void setInputstreamdoc2(InputStream inputstreamdoc2) {
		this.inputstreamdoc2 = inputstreamdoc2;
	}

	public InputStream getInputstreamdoc3() {
		return inputstreamdoc3;
	}

	public void setInputstreamdoc3(InputStream inputstreamdoc3) {
		this.inputstreamdoc3 = inputstreamdoc3;
	}

	public InputStream getInputstreamdoc4() {
		return inputstreamdoc4;
	}

	public void setInputstreamdoc4(InputStream inputstreamdoc4) {
		this.inputstreamdoc4 = inputstreamdoc4;
	}

	public InputStream getInputstreamdoc5() {
		return inputstreamdoc5;
	}

	public void setInputstreamdoc5(InputStream inputstreamdoc5) {
		this.inputstreamdoc5 = inputstreamdoc5;
	}

	
    
    public String getAttachmentname1() {
		return attachmentname1;
	}

	public void setAttachmentname1(String attachmentname1) {
		this.attachmentname1 = attachmentname1;
	}

	public String getAttachmentname2() {
		return attachmentname2;
	}

	public void setAttachmentname2(String attachmentname2) {
		this.attachmentname2 = attachmentname2;
	}

	public String getAttachmentname3() {
		return attachmentname3;
	}

	public void setAttachmentname3(String attachmentname3) {
		this.attachmentname3 = attachmentname3;
	}

	public String getAttachmentname4() {
		return attachmentname4;
	}

	public void setAttachmentname4(String attachmentname4) {
		this.attachmentname4 = attachmentname4;
	}

	public String getAttachmentname5() {
		return attachmentname5;
	}

	public void setAttachmentname5(String attachmentname5) {
		this.attachmentname5 = attachmentname5;
	}

	
    
    
    public String getStrPolicyNumberFlag() {
		return strPolicyNumberFlag;
	}

	public void setStrPolicyNumberFlag(String strPolicyNumberFlag) {
		this.strPolicyNumberFlag = strPolicyNumberFlag;
	}

	public FormFile getSourceAttchments1() {
		return sourceAttchments1;
	}

	public void setSourceAttchments1(FormFile sourceAttchments1) {
		this.sourceAttchments1 = sourceAttchments1;
	}

	public FormFile getSourceAttchments2() {
		return sourceAttchments2;
	}

	public void setSourceAttchments2(FormFile sourceAttchments2) {
		this.sourceAttchments2 = sourceAttchments2;
	}

	public FormFile getSourceAttchments3() {
		return sourceAttchments3;
	}

	public void setSourceAttchments3(FormFile sourceAttchments3) {
		this.sourceAttchments3 = sourceAttchments3;
	}

	public FormFile getSourceAttchments4() {
		return sourceAttchments4;
	}

	public void setSourceAttchments4(FormFile sourceAttchments4) {
		this.sourceAttchments4 = sourceAttchments4;
	}

	public FormFile getSourceAttchments5() {
		return sourceAttchments5;
	}

	public void setSourceAttchments5(FormFile sourceAttchments5) {
		this.sourceAttchments5 = sourceAttchments5;
	}

	
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the noofEmployees
	 */
	public String getNoofEmployees() {
		return noofEmployees;
	}

	/**
	 * @param noofEmployees the noofEmployees to set
	 */
	public void setNoofEmployees(String noofEmployees) {
		this.noofEmployees = noofEmployees;
	}

	/**
	 * @return the additionalPremium
	 */
	public String getAdditionalPremium() {
		return additionalPremium;
	}

	/**
	 * @param additionalPremium the additionalPremium to set
	 */
	public void setAdditionalPremium(String additionalPremium) {
		this.additionalPremium = additionalPremium;
	}

	/**
	 * @return the averageAge
	 */
	public String getAverageAge() {
		return averageAge;
	}

	/**
	 * @param averageAge the averageAge to set
	 */
	public void setAverageAge(String averageAge) {
		this.averageAge = averageAge;
	}

	/**
	 * @return the employeeGender
	 */
	public String getEmployeeGender() {
		return employeeGender;
	}

	/**
	 * @param employeeGender the employeeGender to set
	 */
	public void setEmployeeGender(String employeeGender) {
		this.employeeGender = employeeGender;
	}

	/**
	 * @return the familyCoverage
	 */
	public String getFamilyCoverage() {
		return familyCoverage;
	}

	/**
	 * @param familyCoverage the familyCoverage to set
	 */
	public void setFamilyCoverage(String familyCoverage) {
		this.familyCoverage = familyCoverage;
	}

	/**
	 * @return the globalCoverge
	 */
	public String getGlobalCoverge() {
		return globalCoverge;
	}

	/**
	 * @param globalCoverge the globalCoverge to set
	 */
	public void setGlobalCoverge(String globalCoverge) {
		this.globalCoverge = globalCoverge;
	}

	/**
	 * @return the groupProfileSeqID
	 */
	public Long getGroupProfileSeqID() {
		return groupProfileSeqID;
	}

	/**
	 * @param groupProfileSeqID the groupProfileSeqID to set
	 */
	public void setGroupProfileSeqID(Long groupProfileSeqID) {
		this.groupProfileSeqID = groupProfileSeqID;
	}

	

	/**
	 * @return the incomeProfileSeqID
	 */
	public Long getIncomeProfileSeqID() {
		return incomeProfileSeqID;
	}

	/**
	 * @param incomeProfileSeqID the incomeProfileSeqID to set
	 */
	public void setIncomeProfileSeqID(Long incomeProfileSeqID) {
		this.incomeProfileSeqID = incomeProfileSeqID;
	}

	/**
	 * @return the profileID
	 */
	public String[] getProfileID() {
		return profileID;
	}

	/**
	 * @param profileID the profileID to set
	 */
	public void setProfileID(String[] profileID) {
		this.profileID = profileID;
	}

	/**
	 * @return the profileValue
	 */
	public String[] getProfileValue() {
		return profileValue;
	}

	/**
	 * @param profileValue the profileValue to set
	 */
	public void setProfileValue(String[] profileValue) {
		this.profileValue = profileValue;
	}

	/**
	 * @return the profileGroup
	 */
	public String[] getProfileGroup() {
		return profileGroup;
	}

	/**
	 * @param profileGroup the profileGroup to set
	 */
	public void setProfileGroup(String[] profileGroup) {
		this.profileGroup = profileGroup;
	}

	/**
	 * @return the profilePercentage
	 */
	public String[] getProfilePercentage() {
		return profilePercentage;
	}

	/**
	 * @param profilePercentage the profilePercentage to set
	 */
	public void setProfilePercentage(String[] profilePercentage) {
		this.profilePercentage = profilePercentage;
	}

	/**
	 * @return the profileID1
	 */
	public String getProfileID1() {
		return profileID1;
	}

	/**
	 * @param profileID1 the profileID1 to set
	 */
	public void setProfileID1(String profileID1) {
		this.profileID1 = profileID1;
	}

	/**
	 * @return the profileValue1
	 */
	public String getProfileValue1() {
		return profileValue1;
	}

	/**
	 * @param profileValue1 the profileValue1 to set
	 */
	public void setProfileValue1(String profileValue1) {
		this.profileValue1 = profileValue1;
	}

	/**
	 * @return the profileGroup1
	 */
	public String getProfileGroup1() {
		return profileGroup1;
	}

	/**
	 * @param profileGroup1 the profileGroup1 to set
	 */
	public void setProfileGroup1(String profileGroup1) {
		this.profileGroup1 = profileGroup1;
	}

	/**
	 * @return the profilePercentage1
	 */
	public String getProfilePercentage1() {
		return profilePercentage1;
	}

	/**
	 * @param profilePercentage1 the profilePercentage1 to set
	 */
	public void setProfilePercentage1(String profilePercentage1) {
		this.profilePercentage1 = profilePercentage1;
	}

	/**
	 * @return the transProfileSeqID1
	 */
	public Long getTransProfileSeqID1() {
		return transProfileSeqID1;
	}

	/**
	 * @param transProfileSeqID1 the transProfileSeqID1 to set
	 */
	public void setTransProfileSeqID1(Long transProfileSeqID1) {
		this.transProfileSeqID1 = transProfileSeqID1;
	}

	/**
	 * @return the transProfileSeqID
	 */
	public Long[] getTransProfileSeqID() {
		return transProfileSeqID;
	}

	/**
	 * @param transProfileSeqID the transProfileSeqID to set
	 */
	public void setTransProfileSeqID(Long[] transProfileSeqID) {
		this.transProfileSeqID = transProfileSeqID;
	}

	/**
	 * @return the profileGroupValue1
	 */
	public String getProfileGroupValue1() {
		return profileGroupValue1;
	}

	/**
	 * @param profileGroupValue1 the profileGroupValue1 to set
	 */
	public void setProfileGroupValue1(String profileGroupValue1) {
		this.profileGroupValue1 = profileGroupValue1;
	}

	/**
	 * @return the profileGroupValue
	 */
	public String[] getProfileGroupValue() {
		return profileGroupValue;
	}

	/**
	 * @param profileGroupValue the profileGroupValue to set
	 */
	public void setProfileGroupValue(String[] profileGroupValue) {
		this.profileGroupValue = profileGroupValue;
	}

	/**
	 * @return the nameOfInsurer
	 */
	public String getNameOfInsurer() {
		return nameOfInsurer;
	}

	/**
	 * @param nameOfInsurer the nameOfInsurer to set
	 */
	public void setNameOfInsurer(String nameOfInsurer) {
		this.nameOfInsurer = nameOfInsurer;
	}

	/**
	 * @return the nameOfTPA
	 */
	public String getNameOfTPA() {
		return nameOfTPA;
	}

	/**
	 * @param nameOfTPA the nameOfTPA to set
	 */
	public void setNameOfTPA(String nameOfTPA) {
		this.nameOfTPA = nameOfTPA;
	}

	/**
	 * @return the eligibility
	 */
	public String getEligibility() {
		return eligibility;
	}

	/**
	 * @param eligibility the eligibility to set
	 */
	public void setEligibility(String eligibility) {
		this.eligibility = eligibility;
	}

	/**
	 * @return the takafulQuote
	 */
	public String getTakafulQuote() {
		return takafulQuote;
	}

	/**
	 * @param takafulQuote the takafulQuote to set
	 */
	public void setTakafulQuote(String takafulQuote) {
		this.takafulQuote = takafulQuote;
	}

	/**
	 * @return the planName
	 */
	public String getPlanName() {
		return planName;
	}

	/**
	 * @param planName the planName to set
	 */
	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getAreaOfCover() {
		return areaOfCover;
	}

	public void setAreaOfCover(String areaOfCover) {
		this.areaOfCover = areaOfCover;
	}

	public Date getCoverStartDate() {
		return coverStartDate;
	}

	public void setCoverStartDate(Date coverStartDate) {
		this.coverStartDate = coverStartDate;
	}

	public Date getCoverEndDate() {
		return coverEndDate;
	}

	public void setCoverEndDate(Date coverEndDate) {
		this.coverEndDate = coverEndDate;
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getPreviousPolicyNo() {
		return previousPolicyNo;
	}

	public void setPreviousPolicyNo(String previousPolicyNo) {
		this.previousPolicyNo = previousPolicyNo;
	}

	public String getTotalCovedLives() {
		return totalCovedLives;
	}

	public void setTotalCovedLives(String totalCovedLives) {
		this.totalCovedLives = totalCovedLives;
	}

	public String getTotalLivesMaternity() {
		return totalLivesMaternity;
	}

	public void setTotalLivesMaternity(String totalLivesMaternity) {
		this.totalLivesMaternity = totalLivesMaternity;
	}

	public String getTrendFactor() {
		return trendFactor;
	}

	public void setTrendFactor(String trendFactor) {
		this.trendFactor = trendFactor;
	}

/*	*//**
	 * @return the profileIncomeList
	 *//*
	public ArrayList getProfileIncomeList() {
		return profileIncomeList;
	}

	*//**
	 * @param profileIncomeList the profileIncomeList to set
	 *//*
	public void setProfileIncomeList(ArrayList profileIncomeList) {
		this.profileIncomeList = profileIncomeList;
	}
*/
	
	 public Long[] getBenf_typeseqid() {
			return benf_typeseqid;
		}

		public void setBenf_typeseqid(Long[] benf_typeseqid) {
			this.benf_typeseqid = benf_typeseqid;
		}

		public String[] getBenfdesc() {
			return benfdesc;
		}

		public void setBenfdesc(String[] benfdesc) {
			this.benfdesc = benfdesc;
		}

		public Long[] getGndrtypeseqid() {
			return gndrtypeseqid;
		}

		public void setGndrtypeseqid(Long[] gndrtypeseqid) {
			this.gndrtypeseqid = gndrtypeseqid;
		}

		public String[] getGndrdesc() {
			return gndrdesc;
		}

		public void setGndrdesc(String[] gndrdesc) {
			this.gndrdesc = gndrdesc;
		}

		public Long[] getAge_rngseqid() {
			return age_rngseqid;
		}

		public void setAge_rngseqid(Long[] age_rngseqid) {
			this.age_rngseqid = age_rngseqid;
		}

		public String[] getAge_range() {
			return age_range;
		}

		public void setAge_range(String[] age_range) {
			this.age_range = age_range;
		}

		public String[] getOvrprtflio_dstr() {
			return ovrprtflio_dstr;
		}

		public void setOvrprtflio_dstr(String[] ovrprtflio_dstr) {
			this.ovrprtflio_dstr = ovrprtflio_dstr;
		}

		public Long[] getNatl_typeseqid() {
			return natl_typeseqid;
		}

		public void setNatl_typeseqid(Long[] natl_typeseqid) {
			this.natl_typeseqid = natl_typeseqid;
		}

		public String[] getNatl_name() {
			return natl_name;
		}

		public void setNatl_name(String[] natl_name) {
			this.natl_name = natl_name;
		}

		public Long getBenf_typeseqid1() {
			return benf_typeseqid1;
		}

		public void setBenf_typeseqid1(Long benf_typeseqid1) {
			this.benf_typeseqid1 = benf_typeseqid1;
		}

		public String getBenfdesc1() {
			return benfdesc1;
		}

		public void setBenfdesc1(String benfdesc1) {
			this.benfdesc1 = benfdesc1;
		}

		public Long getGndrtypeseqid1() {
			return gndrtypeseqid1;
		}

		public void setGndrtypeseqid1(Long gndrtypeseqid1) {
			this.gndrtypeseqid1 = gndrtypeseqid1;
		}

		public String getGndrdesc1() {
			return gndrdesc1;
		}

		public void setGndrdesc1(String gndrdesc1) {
			this.gndrdesc1 = gndrdesc1;
		}

		public Long getAge_rngseqid1() {
			return age_rngseqid1;
		}

		public void setAge_rngseqid1(Long age_rngseqid1) {
			this.age_rngseqid1 = age_rngseqid1;
		}

		public String getAge_range1() {
			return age_range1;
		}

		public void setAge_range1(String age_range1) {
			this.age_range1 = age_range1;
		}

		public String getOvrprtflio_dstr1() {
			return ovrprtflio_dstr1;
		}

		public void setOvrprtflio_dstr1(String ovrprtflio_dstr1) {
			this.ovrprtflio_dstr1 = ovrprtflio_dstr1;
		}

		public Long getNatl_typeseqid1() {
			return natl_typeseqid1;
		}

		public void setNatl_typeseqid1(Long natl_typeseqid1) {
			this.natl_typeseqid1 = natl_typeseqid1;
		}

		public String getNatl_name1() {
			return natl_name1;
		}

		public void setNatl_name1(String natl_name1) {
			this.natl_name1 = natl_name1;
		}

		public String[] getTotalCoverdLives() {
			return totalCoverdLives;
		}

		public void setTotalCoverdLives(String[] totalCoverdLives) {
			this.totalCoverdLives = totalCoverdLives;
		}

		public String getTotalCoverdLives1() {
			return totalCoverdLives1;
		}

		public void setTotalCoverdLives1(String totalCoverdLives1) {
			this.totalCoverdLives1 = totalCoverdLives1;
		}

		   public String getNatcategorylist() {
				return natcategorylist;
			}

			public void setNatcategorylist(String natcategorylist) {
				this.natcategorylist = natcategorylist;
			}

			public String getAreaOfCoverList() {
				return areaOfCoverList;
			}

			public void setAreaOfCoverList(String areaOfCoverList) {
				this.areaOfCoverList = areaOfCoverList;
			}

			public String getNetworkList() {
				return networkList;
			}

			public void setNetworkList(String networkList) {
				this.networkList = networkList;
			}

			public String getMaxBenifitList() {
				return maxBenifitList;
			}

			public void setMaxBenifitList(String maxBenifitList) {
				this.maxBenifitList = maxBenifitList;
			}

			public String getDentalLimitList() {
				return dentalLimitList;
			}

			public void setDentalLimitList(String dentalLimitList) {
				this.dentalLimitList = dentalLimitList;
			}

			public String getMaternityLimitList() {
				return maternityLimitList;
			}

			public void setMaternityLimitList(String maternityLimitList) {
				this.maternityLimitList = maternityLimitList;
			}

			public String getOpticalLimitList() {
				return opticalLimitList;
			}

			public void setOpticalLimitList(String opticalLimitList) {
				this.opticalLimitList = opticalLimitList;
			}

			public String getOpCopayList() {
				return opCopayList;
			}

			public void setOpCopayList(String opCopayList) {
				this.opCopayList = opCopayList;
			}

			public String getOpDeductableList() {
				return opDeductableList;
			}

			public void setOpDeductableList(String opDeductableList) {
				this.opDeductableList = opDeductableList;
			}

			public String getDentalcopayList() {
				return dentalcopayList;
			}

			public void setDentalcopayList(String dentalcopayList) {
				this.dentalcopayList = dentalcopayList;
			}

			public String getOpticalCopayList() {
				return opticalCopayList;
			}

			public void setOpticalCopayList(String opticalCopayList) {
				this.opticalCopayList = opticalCopayList;
			}

			public String getPhysiocoverage() {
				return physiocoverage;
			}

			public void setPhysiocoverage(String physiocoverage) {
				this.physiocoverage = physiocoverage;
			}

			public String getVitDcoverage() {
				return vitDcoverage;
			}

			public void setVitDcoverage(String vitDcoverage) {
				this.vitDcoverage = vitDcoverage;
			}

			public String getVaccImmuCoverage() {
				return vaccImmuCoverage;
			}

			public void setVaccImmuCoverage(String vaccImmuCoverage) {
				this.vaccImmuCoverage = vaccImmuCoverage;
			}

			public String getMatComplicationCoverage() {
				return matComplicationCoverage;
			}

			public void setMatComplicationCoverage(String matComplicationCoverage) {
				this.matComplicationCoverage = matComplicationCoverage;
			}

			public String getPsychiatrycoverage() {
				return psychiatrycoverage;
			}

			public void setPsychiatrycoverage(String psychiatrycoverage) {
				this.psychiatrycoverage = psychiatrycoverage;
			}

			public String getDeviatedNasalSeptum() {
				return deviatedNasalSeptum;
			}

			public void setDeviatedNasalSeptum(String deviatedNasalSeptum) {
				this.deviatedNasalSeptum = deviatedNasalSeptum;
			}

			public String getObesityTreatment() {
				return obesityTreatment;
			}

			public void setObesityTreatment(String obesityTreatment) {
				this.obesityTreatment = obesityTreatment;
			}

			public String getMaternityYN() {
				return maternityYN;
			}

			public void setMaternityYN(String maternityYN) {
				this.maternityYN = maternityYN;
			}

			public String getDentalYN() {
				return dentalYN;
			}

			public void setDentalYN(String dentalYN) {
				this.dentalYN = dentalYN;
			}

			public String getDentalLivesYN() {
				return dentalLivesYN;
			}

			public void setDentalLivesYN(String dentalLivesYN) {
				this.dentalLivesYN = dentalLivesYN;
			}

			public String getOpticalYN() {
				return opticalYN;
			}

			public void setOpticalYN(String opticalYN) {
				this.opticalYN = opticalYN;
			}

			public String getOpticalLivesYN() {
				return opticalLivesYN;
			}

			public void setOpticalLivesYN(String opticalLivesYN) {
				this.opticalLivesYN = opticalLivesYN;
			}

			public String getRenewalYN() {
				return renewalYN;
			}

			public void setRenewalYN(String renewalYN) {
				this.renewalYN = renewalYN;
			}

			public String getMaxBeneLimitOth() {
				return maxBeneLimitOth;
			}

			public void setMaxBeneLimitOth(String maxBeneLimitOth) {
				this.maxBeneLimitOth = maxBeneLimitOth;
			}

			public String getDentalLimitOth() {
				return dentalLimitOth;
			}

			public void setDentalLimitOth(String dentalLimitOth) {
				this.dentalLimitOth = dentalLimitOth;
			}

			public String getOpticalLimitOth() {
				return opticalLimitOth;
			}

			public void setOpticalLimitOth(String opticalLimitOth) {
				this.opticalLimitOth = opticalLimitOth;
			}

			public String getMaternityLimitOth() {
				return maternityLimitOth;
			}

			public void setMaternityLimitOth(String maternityLimitOth) {
				this.maternityLimitOth = maternityLimitOth;
			}

			public String getOpCopayListDesc() {
				return opCopayListDesc;
			}

			public void setOpCopayListDesc(String opCopayListDesc) {
				this.opCopayListDesc = opCopayListDesc;
			}

			public Long[] getBenf_lives_seq_id() {
				return benf_lives_seq_id;
			}

			public void setBenf_lives_seq_id(Long[] benf_lives_seq_id) {
				this.benf_lives_seq_id = benf_lives_seq_id;
			}

			public Long getBenf_lives_seq_id1() {
				return benf_lives_seq_id1;
			}

			public void setBenf_lives_seq_id1(Long benf_lives_seq_id1) {
				this.benf_lives_seq_id1 = benf_lives_seq_id1;
			}

			public String getDentalcopayListDesc() {
				return dentalcopayListDesc;
			}

			public void setDentalcopayListDesc(String dentalcopayListDesc) {
				this.dentalcopayListDesc = dentalcopayListDesc;
			}

			public String getOpticalCopayListDesc() {
				return opticalCopayListDesc;
			}

			public void setOpticalCopayListDesc(String opticalCopayListDesc) {
				this.opticalCopayListDesc = opticalCopayListDesc;
			}

			public String getOpDeductableListDesc() {
				return opDeductableListDesc;
			}

			public void setOpDeductableListDesc(String opDeductableListDesc) {
				this.opDeductableListDesc = opDeductableListDesc;
			}

		

			public String getBenecoverFlagYN() {
				return benecoverFlagYN;
			}

			public void setBenecoverFlagYN(String benecoverFlagYN) {
				this.benecoverFlagYN = benecoverFlagYN;
			}

			public String getCalCPMFlagYN() {
				return calCPMFlagYN;
			}

			public void setCalCPMFlagYN(String calCPMFlagYN) {
				this.calCPMFlagYN = calCPMFlagYN;
			}

			

			public String getNatCoverdLives1() {
				return natCoverdLives1;
			}

			public void setNatCoverdLives1(String natCoverdLives1) {
				this.natCoverdLives1 = natCoverdLives1;
			}

			public Long[] getNatl_seqid() {
				return natl_seqid;
			}

			public void setNatl_seqid(Long[] natl_seqid) {
				this.natl_seqid = natl_seqid;
			}

			

			public String[] getNatCoverdLives() {
				return natCoverdLives;
			}

			public void setNatCoverdLives(String[] natCoverdLives) {
				this.natCoverdLives = natCoverdLives;
			}

			public Long getNatl_seqid1() {
				return natl_seqid1;
			}

			public void setNatl_seqid1(Long natl_seqid1) {
				this.natl_seqid1 = natl_seqid1;
			}

			public String getComments() {
				return comments;
			}

			public void setComments(String comments) {
				this.comments = comments;
			}

			
			public String getLoadingFlagYN() {
				return loadingFlagYN;
			}

			public void setLoadingFlagYN(String loadingFlagYN) {
				this.loadingFlagYN = loadingFlagYN;
			}

			public String getPricingDocs() {
				return pricingDocs;
			}

			public void setPricingDocs(String pricingDocs) {
				this.pricingDocs = pricingDocs;
			}

			

			public String getPricingRefno() {
				return pricingRefno;
			}

			public void setPricingRefno(String pricingRefno) {
				this.pricingRefno = pricingRefno;
			}

			public Long getFinaldataSeqID() {
				return finaldataSeqID;
			}

			public void setFinaldataSeqID(Long finaldataSeqID) {
				this.finaldataSeqID = finaldataSeqID;
			}

			public String getSumTotalLives() {
				return sumTotalLives;
			}

			public void setSumTotalLives(String sumTotalLives) {
				this.sumTotalLives = sumTotalLives;
			}

			public String getSumTotalLivesMaternity() {
				return sumTotalLivesMaternity;
			}

			public void setSumTotalLivesMaternity(String sumTotalLivesMaternity) {
				this.sumTotalLivesMaternity = sumTotalLivesMaternity;
			}

			public String getSumTotalLivesDental() {
				return sumTotalLivesDental;
			}

			public void setSumTotalLivesDental(String sumTotalLivesDental) {
				this.sumTotalLivesDental = sumTotalLivesDental;
			}

			public String getSumTotalLivesOptical() {
				return sumTotalLivesOptical;
			}

			public void setSumTotalLivesOptical(String sumTotalLivesOptical) {
				this.sumTotalLivesOptical = sumTotalLivesOptical;
			}

			/**
			 * @return the strPolicyNumberFlag
			 */
			public String getPolicyNumberFlag() {
				return strPolicyNumberFlag;
			}

			/**
			 * @param strPolicyNumberFlag the strPolicyNumberFlag to set
			 */
			public void setPolicyNumberFlag(String strPolicyNumberFlag) {
				this.strPolicyNumberFlag = strPolicyNumberFlag;
			}

			public String getPolicyNumber() {
				return policyNumber;
			}

			public void setPolicyNumber(String policyNumber) {
				this.policyNumber = policyNumber;
			}

			public String getSumNationalityLives() {
				return sumNationalityLives;
			}

			public void setSumNationalityLives(String sumNationalityLives) {
				this.sumNationalityLives = sumNationalityLives;
			}

			public String getClientName() {
				return clientName;
			}

			public void setClientName(String clientName) {
				this.clientName = clientName;
			}

			public String getNewdataentry() {
				return newdataentry;
			}

			public void setNewdataentry(String newdataentry) {
				this.newdataentry = newdataentry;
			}

			public String getPricingNumberAlert() {
				return pricingNumberAlert;
			}

			public void setPricingNumberAlert(String pricingNumberAlert) {
				this.pricingNumberAlert = pricingNumberAlert;
			}

			public String[] getNatovrprtflio_dstr() {
				return natovrprtflio_dstr;
			}

			public void setNatovrprtflio_dstr(String[] natovrprtflio_dstr) {
				this.natovrprtflio_dstr = natovrprtflio_dstr;
			}

			public String getNatovrprtflio_dstr1() {
				return natovrprtflio_dstr1;
			}

			public void setNatovrprtflio_dstr1(String natovrprtflio_dstr1) {
				this.natovrprtflio_dstr1 = natovrprtflio_dstr1;
			}

			public String getCompleteSaveYN() {
				return completeSaveYN;
			}

			public void setCompleteSaveYN(String completeSaveYN) {
				this.completeSaveYN = completeSaveYN;
			}

			  public String getInpatientBenefit() {
					return inpatientBenefit;
				}

			public String getInpAccomodation() {
				return inpAccomodation;
			}

			public void setInpAccomodation(String inpAccomodation) {
				this.inpAccomodation = inpAccomodation;
			}

			public Long getInpAccmdlimit() {
				return inpAccmdlimit;
			}

			public void setInpAccmdlimit(Long inpAccmdlimit) {
				this.inpAccmdlimit = inpAccmdlimit;
			}

			public String getInpAccmdCopay() {
				return inpAccmdCopay;
			}

			public void setInpAccmdCopay(String inpAccmdCopay) {
				this.inpAccmdCopay = inpAccmdCopay;
			}

			public String getInpAccmdDeductable() {
				return inpAccmdDeductable;
			}

			public void setInpAccmdDeductable(String inpAccmdDeductable) {
				this.inpAccmdDeductable = inpAccmdDeductable;
			}

			public String getPricingmodifyYN() {
				return pricingmodifyYN;
			}

			public void setPricingmodifyYN(String pricingmodifyYN) {
				this.pricingmodifyYN = pricingmodifyYN;
			}

			
			

				public void setInpatientBenefit(String inpatientBenefit) {
					this.inpatientBenefit = inpatientBenefit;
				}

				public String getOutpatientBenefit() {
					return outpatientBenefit;
				}

				public void setOutpatientBenefit(String outpatientBenefit) {
					this.outpatientBenefit = outpatientBenefit;
				}

				public String getGastricBinding() {
					return gastricBinding;
				}

				public void setGastricBinding(String gastricBinding) {
					this.gastricBinding = gastricBinding;
				}

				public String getHealthScreen() {
					return healthScreen;
				}

				public void setHealthScreen(String healthScreen) {
					this.healthScreen = healthScreen;
				}

				public String getChronicLimit() {
					return chronicLimit;
				}

				public void setChronicLimit(String chronicLimit) {
					this.chronicLimit = chronicLimit;
				}

				public String getOrthodonticsCopay() {
					return orthodonticsCopay;
				}

				public void setOrthodonticsCopay(String orthodonticsCopay) {
					this.orthodonticsCopay = orthodonticsCopay;
				}

				public String getOpCopaypharmacy() {
					return opCopaypharmacy;
				}

				public void setOpCopaypharmacy(String opCopaypharmacy) {
					this.opCopaypharmacy = opCopaypharmacy;
				}

				public String getOpInvestigation() {
					return opInvestigation;
				}

				public void setOpInvestigation(String opInvestigation) {
					this.opInvestigation = opInvestigation;
				}

				public String getOpCopyconsultn() {
					return opCopyconsultn;
				}

				public void setOpCopyconsultn(String opCopyconsultn) {
					this.opCopyconsultn = opCopyconsultn;
				}

				public String getAlAhlihospital() {
					return alAhlihospital;
				}

				public void setAlAhlihospital(String alAhlihospital) {
					this.alAhlihospital = alAhlihospital;
				}

				public String getOpCopyalahlihosp() {
					return opCopyalahlihosp;
				}

				public void setOpCopyalahlihosp(String opCopyalahlihosp) {
					this.opCopyalahlihosp = opCopyalahlihosp;
				}

				public String getOpPharmacyAlAhli() {
					return opPharmacyAlAhli;
				}

				public void setOpPharmacyAlAhli(String opPharmacyAlAhli) {
					this.opPharmacyAlAhli = opPharmacyAlAhli;
				}

				public String getOpConsultAlAhli() {
					return opConsultAlAhli;
				}

				public void setOpConsultAlAhli(String opConsultAlAhli) {
					this.opConsultAlAhli = opConsultAlAhli;
				}

				public String getOpInvestnAlAhli() {
					return opInvestnAlAhli;
				}

				public void setOpInvestnAlAhli(String opInvestnAlAhli) {
					this.opInvestnAlAhli = opInvestnAlAhli;
				}

				public String getChronicLimitOth() {
					return chronicLimitOth;
				}

				public void setChronicLimitOth(String chronicLimitOth) {
					this.chronicLimitOth = chronicLimitOth;
				}

				public String getOpdeductableserviceYN() {
					return opdeductableserviceYN;
				}

				public void setOpdeductableserviceYN(
						String opdeductableserviceYN) {
					this.opdeductableserviceYN = opdeductableserviceYN;
				}

				public String getOpCopyothers() {
					return opCopyothers;
				}

				public void setOpCopyothers(String opCopyothers) {
					this.opCopyothers = opCopyothers;
				}

				public String getPolicycategory() {
					return policycategory;
				}

				public void setPolicycategory(String policycategory) {
					this.policycategory = policycategory;
				}

				public String getOpothersAlAhli() {
					return opothersAlAhli;
				}

				public void setOpothersAlAhli(String opothersAlAhli) {
					this.opothersAlAhli = opothersAlAhli;
				}

				public String getOrthodonticsCopayDesc() {
					return orthodonticsCopayDesc;
				}

				public void setOrthodonticsCopayDesc(
						String orthodonticsCopayDesc) {
					this.orthodonticsCopayDesc = orthodonticsCopayDesc;
				}

				public String getOpCopaypharmacyDesc() {
					return opCopaypharmacyDesc;
				}

				public void setOpCopaypharmacyDesc(String opCopaypharmacyDesc) {
					this.opCopaypharmacyDesc = opCopaypharmacyDesc;
				}

				public String getOpCopyconsultnDesc() {
					return opCopyconsultnDesc;
				}

				public void setOpCopyconsultnDesc(String opCopyconsultnDesc) {
					this.opCopyconsultnDesc = opCopyconsultnDesc;
				}

				public String getOpInvestigationDesc() {
					return opInvestigationDesc;
				}

				public void setOpInvestigationDesc(String opInvestigationDesc) {
					this.opInvestigationDesc = opInvestigationDesc;
				}

				public String getOpCopyalahlihospDesc() {
					return opCopyalahlihospDesc;
				}

				public void setOpCopyalahlihospDesc(String opCopyalahlihospDesc) {
					this.opCopyalahlihospDesc = opCopyalahlihospDesc;
				}

				public String getOpPharmacyAlAhliDesc() {
					return opPharmacyAlAhliDesc;
				}

				public void setOpPharmacyAlAhliDesc(String opPharmacyAlAhliDesc) {
					this.opPharmacyAlAhliDesc = opPharmacyAlAhliDesc;
				}

				public String getOpConsultAlAhliDesc() {
					return opConsultAlAhliDesc;
				}

				public void setOpConsultAlAhliDesc(String opConsultAlAhliDesc) {
					this.opConsultAlAhliDesc = opConsultAlAhliDesc;
				}

				public String getOpInvestnAlAhliDesc() {
					return opInvestnAlAhliDesc;
				}

				public void setOpInvestnAlAhliDesc(String opInvestnAlAhliDesc) {
					this.opInvestnAlAhliDesc = opInvestnAlAhliDesc;
				}

				public String getOpothersAlAhliDesc() {
					return opothersAlAhliDesc;
				}

				public void setOpothersAlAhliDesc(String opothersAlAhliDesc) {
					this.opothersAlAhliDesc = opothersAlAhliDesc;
				}

				public String getOpCopyothersDesc() {
					return opCopyothersDesc;
				}

				public void setOpCopyothersDesc(String opCopyothersDesc) {
					this.opCopyothersDesc = opCopyothersDesc;
				}

				public String getDemographicflagYN() {
					return demographicflagYN;
				}

				public void setDemographicflagYN(String demographicflagYN) {
					this.demographicflagYN = demographicflagYN;
				}

				public String getAlAhlihospOPservices() {
					return alAhlihospOPservices;
				}

				public void setAlAhlihospOPservices(String alAhlihospOPservices) {
					this.alAhlihospOPservices = alAhlihospOPservices;
				}

				public String getAlertmsgscreen1() {
					return alertmsgscreen1;
				}

				public void setAlertmsgscreen1(String alertmsgscreen1) {
					this.alertmsgscreen1 = alertmsgscreen1;
				}

				public String getOpticalFrameLimitList() {
					return opticalFrameLimitList;
				}

				public void setOpticalFrameLimitList(String opticalFrameLimitList) {
					this.opticalFrameLimitList = opticalFrameLimitList;
				}

				public double getNumberOfLives() {
					return numberOfLives;
				}

				public void setNumberOfLives(double numberOfLives) {
					this.numberOfLives = numberOfLives;
				}

				public String getCompleteSaveYNInSc2() {
					return completeSaveYNInSc2;
				}

				public void setCompleteSaveYNInSc2(String completeSaveYNInSc2) {
					this.completeSaveYNInSc2 = completeSaveYNInSc2;
				}

				public String getRiskPremiumDate() {
					return riskPremiumDate;
				}

				public void setRiskPremiumDate(String riskPremiumDate) {
					this.riskPremiumDate = riskPremiumDate;
				}

				public String getIpCopay() {
					return ipCopay;
				}

				public void setIpCopay(String ipCopay) {
					this.ipCopay = ipCopay;
				}

				public String getIpCopayPerc() {
					return ipCopayPerc;
				}

				public void setIpCopayPerc(String ipCopayPerc) {
					this.ipCopayPerc = ipCopayPerc;
				}

				public String getIpCopayAtAlAhli() {
					return ipCopayAtAlAhli;
				}

				public void setIpCopayAtAlAhli(String ipCopayAtAlAhli) {
					this.ipCopayAtAlAhli = ipCopayAtAlAhli;
				}

				public String getIpCopayAtAlAhliPerc() {
					return ipCopayAtAlAhliPerc;
				}

				public void setIpCopayAtAlAhliPerc(String ipCopayAtAlAhliPerc) {
					this.ipCopayAtAlAhliPerc = ipCopayAtAlAhliPerc;
				}

				public String getMaternityCopay() {
					return maternityCopay;
				}

				public void setMaternityCopay(String maternityCopay) {
					this.maternityCopay = maternityCopay;
				}

				public String getMaternityCopayPerc() {
					return maternityCopayPerc;
				}

				public void setMaternityCopayPerc(String maternityCopayPerc) {
					this.maternityCopayPerc = maternityCopayPerc;
				}

				
				
    
}// End of InsuranceFeedbackVO

