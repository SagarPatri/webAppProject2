

package com.ttk.dto.insurancepricing;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;

import com.ttk.dto.BaseVO;

public class SwPolicyConfigVO  extends BaseVO {  

 private Long lngGroupProfileSeqID=null;
 private Long cpmSeqID=null;
 private Long finaldataSeqID=null;
 
 private Date policyEffDate=null;
 private String strpolicyEffDate = null;
 private Date policyExpDate=null;
 private String strpolicyExpDate=null;
 
 private String clientCode="";

 private String effectiveYear="";
 private Long policyDurationPerMonth = null;
 private Long noOfLives = null;
 private String inPatientCPM = "";
 private String outPatientCPM = "";
 private String maternityCPM = "";
 private String opticalCPM = "";
 private String dentalCPM = "";
 private String allExlMaternity = "";
 private String finalweightage = "";


 private String inpatientcrediblty = "";
 private String outpatientcrediblty = "";
 private String maternitycrediblty = "";
 private String opticalcrediblty = "";
 private String dentalcrediblty = "";
 private String calCPM_FlagYN = "";
 private String alertMsg = "";

 
 
private ArrayList<Object> alPastData = null;

 private String load_DeductType = "";
 private Long load_DeductTypeSeqId = null;
 private Long grp_load_SeqId = null;
 private String load_DeductTypePercentage = "";
 private ArrayList<Object> alLoadingData = null;
 private ArrayList<Object> alLoadingGrosspremium = null;
 

 private String slNo = "";
 private String dataType = "";
 private String policyNo = "";
 private Long trend = null;
 private String loadComments = "";
 
 private Long fincpmSeqID=null;
 private Date finpolicyEffDate=null;
 private Date finpolicyExpDate=null;
 private String finclientCode="";
 private String fineffectiveYear="";
 private Long finpolicyDurationPerMonth = null;
 private Long finnoOfLives = null;
 private String fininPatientCPM = "";
 private String finoutPatientCPM = "";
 private String finmaternityCPM = "";
 private String finopticalCPM = "";
 private String findentalCPM = "";
 private String finallExlMaternity = "";
 private String finfinalweightage = "";

 
 private String findataType = "";
 private String finpolicyNo = "";
 private String pricingNumberAlert = "";
 
 
/*Demographic data*/
 private Long demographicSeqId = null;
 private String demoSlNo = "";
 private String demodataType = "";
 private String demoPolicyEffDate = "";
 private String demoPolicyExpDate="";
 private String demoClientCode="";
 private String demoPolicyNo = "";
 private String demoAlertMsg = "";
 private String demoEffectiveYear="";
 private String demoPolicyDurationPerMonth = "";
 private String demoNoOfLives = "";
 private String demoAverageAge = "";
 private String demoNationality = "";
 private String demoAreaOfCover = "";
 private String demoNetwork = "";
 private String demoMaximumBenfitLimit = "";
 private String demoOpCopay = "";
 private String demoOPDeductable = "";
 private String demoMaternityCoverage = "";
 private String demoMaternityLimit = "";
 private String demoOpticalCoverage = "";
 private String demoOpticalLimit = "";
 private String demoOpticalCopay = "";
 private String demoDentalCoverage = "";
 private String demoDentalLimit = "";
 private String demoDentalCopay = "";
 private String demoAlahli = "";
 private String maternityCopay;
 private ArrayList<Object> alDemoPastData = null;
 
 
 public String getFindataType() {
	return findataType;
}

public void setFindataType(String findataType) {
	this.findataType = findataType;
}

public String getFinpolicyNo() {
	return finpolicyNo;
}

public void setFinpolicyNo(String finpolicyNo) {
	this.finpolicyNo = finpolicyNo;
}


 
 public Long getFincpmSeqID() {
	return fincpmSeqID;
}

public void setFincpmSeqID(Long fincpmSeqID) {
	this.fincpmSeqID = fincpmSeqID;
}

public Date getFinpolicyEffDate() {
	return finpolicyEffDate;
}

public void setFinpolicyEffDate(Date finpolicyEffDate) {
	this.finpolicyEffDate = finpolicyEffDate;
}



public Date getFinpolicyExpDate() {
	return finpolicyExpDate;
}

public void setFinpolicyExpDate(Date finpolicyExpDate) {
	this.finpolicyExpDate = finpolicyExpDate;
}

public String getFinclientCode() {
	return finclientCode;
}

public void setFinclientCode(String finclientCode) {
	this.finclientCode = finclientCode;
}

public String getFineffectiveYear() {
	return fineffectiveYear;
}

public void setFineffectiveYear(String fineffectiveYear) {
	this.fineffectiveYear = fineffectiveYear;
}

public Long getFinpolicyDurationPerMonth() {
	return finpolicyDurationPerMonth;
}

public void setFinpolicyDurationPerMonth(Long finpolicyDurationPerMonth) {
	this.finpolicyDurationPerMonth = finpolicyDurationPerMonth;
}

public Long getFinnoOfLives() {
	return finnoOfLives;
}

public void setFinnoOfLives(Long finnoOfLives) {
	this.finnoOfLives = finnoOfLives;
}

public String getFininPatientCPM() {
	return fininPatientCPM;
}

public void setFininPatientCPM(String fininPatientCPM) {
	this.fininPatientCPM = fininPatientCPM;
}

public String getFinoutPatientCPM() {
	return finoutPatientCPM;
}

public void setFinoutPatientCPM(String finoutPatientCPM) {
	this.finoutPatientCPM = finoutPatientCPM;
}

public String getFinmaternityCPM() {
	return finmaternityCPM;
}

public void setFinmaternityCPM(String finmaternityCPM) {
	this.finmaternityCPM = finmaternityCPM;
}

public String getFinopticalCPM() {
	return finopticalCPM;
}

public void setFinopticalCPM(String finopticalCPM) {
	this.finopticalCPM = finopticalCPM;
}

public String getFindentalCPM() {
	return findentalCPM;
}

public void setFindentalCPM(String findentalCPM) {
	this.findentalCPM = findentalCPM;
}

public String getFinallExlMaternity() {
	return finallExlMaternity;
}

public void setFinallExlMaternity(String finallExlMaternity) {
	this.finallExlMaternity = finallExlMaternity;
}

public String getFinfinalweightage() {
	return finfinalweightage;
}

public void setFinfinalweightage(String finfinalweightage) {
	this.finfinalweightage = finfinalweightage;
}


 
 

 public Long getLngGroupProfileSeqID() {
		return lngGroupProfileSeqID;
	}

	public void setLngGroupProfileSeqID(Long lngGroupProfileSeqID) {
		this.lngGroupProfileSeqID = lngGroupProfileSeqID;
	} 
 
	
	public Long getCpmSeqID() {
		return cpmSeqID;
	}

	public void setCpmSeqID(Long cpmSeqID) {
		this.cpmSeqID = cpmSeqID;
	}


	 public Date getPolicyEffDate() {
		return policyEffDate;
	}

	public void setPolicyEffDate(Date policyEffDate) {
		this.policyEffDate = policyEffDate;
	}

	public Date getPolicyExpDate() {
		return policyExpDate;
	}

	public void setPolicyExpDate(Date policyExpDate) {
		this.policyExpDate = policyExpDate;
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getEffectiveYear() {
		return effectiveYear;
	}

	public void setEffectiveYear(String effectiveYear) {
		this.effectiveYear = effectiveYear;
	}

	public Long getPolicyDurationPerMonth() {
		return policyDurationPerMonth;
	}

	public void setPolicyDurationPerMonth(Long policyDurationPerMonth) {
		this.policyDurationPerMonth = policyDurationPerMonth;
	}

	public Long getNoOfLives() {
		return noOfLives;
	}

	public void setNoOfLives(Long noOfLives) {
		this.noOfLives = noOfLives;
	}

	
	
/*
	public BigDecimal getFinalweightage() {
		return finalweightage;
	}

	public void setFinalweightage(BigDecimal finalweightage) {
		this.finalweightage = finalweightage;
	}*/

	public String getSlNo() {
		return slNo;
	}

	public void setSlNo(String slNo) {
		this.slNo = slNo;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public Long getTrend() {
		return trend;
	}

	public void setTrend(Long trend) {
		this.trend = trend;
	}

	public ArrayList<Object> getAlPastData() {
		return alPastData;
	}

	public void setAlPastData(ArrayList<Object> alPastData) {
		this.alPastData = alPastData;
	}

	
	
	 public String getLoad_DeductType() {
			return load_DeductType;
		}

		public void setLoad_DeductType(String load_DeductType) {
			this.load_DeductType = load_DeductType;
		}

		public Long getLoad_DeductTypeSeqId() {
			return load_DeductTypeSeqId;
		}

		public void setLoad_DeductTypeSeqId(Long load_DeductTypeSeqId) {
			this.load_DeductTypeSeqId = load_DeductTypeSeqId;
		}

		
		public ArrayList<Object> getAlLoadingData() {
			return alLoadingData;
		}

		public void setAlLoadingData(ArrayList<Object> alLoadingData) {
			this.alLoadingData = alLoadingData;
		}

		public Long getGrp_load_SeqId() {
			return grp_load_SeqId;
		}

		public void setGrp_load_SeqId(Long grp_load_SeqId) {
			this.grp_load_SeqId = grp_load_SeqId;
		}

	

		public String getFinalweightage() {
			return finalweightage;
		}

		public void setFinalweightage(String finalweightage) {
			this.finalweightage = finalweightage;
		}
		
		public String getInPatientCPM() {
			return inPatientCPM;
		}

		public void setInPatientCPM(String inPatientCPM) {
			this.inPatientCPM = inPatientCPM;
		}

		public String getOutPatientCPM() {
			return outPatientCPM;
		}

		public void setOutPatientCPM(String outPatientCPM) {
			this.outPatientCPM = outPatientCPM;
		}

		public String getMaternityCPM() {
			return maternityCPM;
		}

		public void setMaternityCPM(String maternityCPM) {
			this.maternityCPM = maternityCPM;
		}

		public String getOpticalCPM() {
			return opticalCPM;
		}

		public void setOpticalCPM(String opticalCPM) {
			this.opticalCPM = opticalCPM;
		}

		public String getDentalCPM() {
			return dentalCPM;
		}

		public void setDentalCPM(String dentalCPM) {
			this.dentalCPM = dentalCPM;
		}

		public String getAllExlMaternity() {
			return allExlMaternity;
		}

		public void setAllExlMaternity(String allExlMaternity) {
			this.allExlMaternity = allExlMaternity;
		}

		public String getLoad_DeductTypePercentage() {
			return load_DeductTypePercentage;
		}

		public void setLoad_DeductTypePercentage(String load_DeductTypePercentage) {
			this.load_DeductTypePercentage = load_DeductTypePercentage;
		}

		public String getLoadComments() {
			return loadComments;
		}

		public void setLoadComments(String loadComments) {
			this.loadComments = loadComments;
		}

		public String getStrpolicyEffDate() {
			return strpolicyEffDate;
		}

		public void setStrpolicyEffDate(String strpolicyEffDate) {
			this.strpolicyEffDate = strpolicyEffDate;
		}

		public String getStrpolicyExpDate() {
			return strpolicyExpDate;
		}

		public void setStrpolicyExpDate(String strpolicyExpDate) {
			this.strpolicyExpDate = strpolicyExpDate;
		}

		public Long getFinaldataSeqID() {
			return finaldataSeqID;
		}

		public void setFinaldataSeqID(Long finaldataSeqID) {
			this.finaldataSeqID = finaldataSeqID;
		}

		public String getCalCPM_FlagYN() {
			return calCPM_FlagYN;
		}

		public void setCalCPM_FlagYN(String calCPM_FlagYN) {
			this.calCPM_FlagYN = calCPM_FlagYN;
		}

		public String getAlertMsg() {
			return alertMsg;
		}

		public void setAlertMsg(String alertMsg) {
			this.alertMsg = alertMsg;
		}

		public String getPricingNumberAlert() {
			return pricingNumberAlert;
		}

		public void setPricingNumberAlert(String pricingNumberAlert) {
			this.pricingNumberAlert = pricingNumberAlert;
		}

		 public String getInpatientcrediblty() {
				return inpatientcrediblty;
			}

			public void setInpatientcrediblty(String inpatientcrediblty) {
				this.inpatientcrediblty = inpatientcrediblty;
			}

			public String getOutpatientcrediblty() {
				return outpatientcrediblty;
			}

			public void setOutpatientcrediblty(String outpatientcrediblty) {
				this.outpatientcrediblty = outpatientcrediblty;
			}

			public String getMaternitycrediblty() {
				return maternitycrediblty;
			}

			public void setMaternitycrediblty(String maternitycrediblty) {
				this.maternitycrediblty = maternitycrediblty;
			}

			public String getOpticalcrediblty() {
				return opticalcrediblty;
			}

			public void setOpticalcrediblty(String opticalcrediblty) {
				this.opticalcrediblty = opticalcrediblty;
			}

			public String getDentalcrediblty() {
				return dentalcrediblty;
			}

			public void setDentalcrediblty(String dentalcrediblty) {
				this.dentalcrediblty = dentalcrediblty;
			}
			
			

			
				public String getDemoPolicyEffDate() {
					return demoPolicyEffDate;
				}

				public void setDemoPolicyEffDate(String demoPolicyEffDate) {
					this.demoPolicyEffDate = demoPolicyEffDate;
				}

				public String getDemoPolicyExpDate() {
					return demoPolicyExpDate;
				}

				public void setDemoPolicyExpDate(String demoPolicyExpDate) {
					this.demoPolicyExpDate = demoPolicyExpDate;
				}

				public String getDemoClientCode() {
					return demoClientCode;
				}

				public void setDemoClientCode(String demoClientCode) {
					this.demoClientCode = demoClientCode;
				}

				public String getDemoPolicyNo() {
					return demoPolicyNo;
				}

				public void setDemoPolicyNo(String demoPolicyNo) {
					this.demoPolicyNo = demoPolicyNo;
				}

				public String getDemoEffectiveYear() {
					return demoEffectiveYear;
				}

				public void setDemoEffectiveYear(String demoEffectiveYear) {
					this.demoEffectiveYear = demoEffectiveYear;
				}

				public String getDemoPolicyDurationPerMonth() {
					return demoPolicyDurationPerMonth;
				}

				public void setDemoPolicyDurationPerMonth(String demoPolicyDurationPerMonth) {
					this.demoPolicyDurationPerMonth = demoPolicyDurationPerMonth;
				}

				public String getDemoNoOfLives() {
					return demoNoOfLives;
				}

				public void setDemoNoOfLives(String demoNoOfLives) {
					this.demoNoOfLives = demoNoOfLives;
				}

				public String getDemoAverageAge() {
					return demoAverageAge;
				}

				public void setDemoAverageAge(String demoAverageAge) {
					this.demoAverageAge = demoAverageAge;
				}

				public String getDemoNationality() {
					return demoNationality;
				}

				public void setDemoNationality(String demoNationality) {
					this.demoNationality = demoNationality;
				}

				public String getDemoAreaOfCover() {
					return demoAreaOfCover;
				}

				public void setDemoAreaOfCover(String demoAreaOfCover) {
					this.demoAreaOfCover = demoAreaOfCover;
				}

				public String getDemoNetwork() {
					return demoNetwork;
				}

				public void setDemoNetwork(String demoNetwork) {
					this.demoNetwork = demoNetwork;
				}

				public String getDemoMaximumBenfitLimit() {
					return demoMaximumBenfitLimit;
				}

				public void setDemoMaximumBenfitLimit(String demoMaximumBenfitLimit) {
					this.demoMaximumBenfitLimit = demoMaximumBenfitLimit;
				}

				public String getDemoOpCopay() {
					return demoOpCopay;
				}

				public void setDemoOpCopay(String demoOpCopay) {
					this.demoOpCopay = demoOpCopay;
				}

				public String getDemoOPDeductable() {
					return demoOPDeductable;
				}

				public void setDemoOPDeductable(String demoOPDeductable) {
					this.demoOPDeductable = demoOPDeductable;
				}

				public String getDemoMaternityCoverage() {
					return demoMaternityCoverage;
				}

				public void setDemoMaternityCoverage(String demoMaternityCoverage) {
					this.demoMaternityCoverage = demoMaternityCoverage;
				}

				public String getDemoMaternityLimit() {
					return demoMaternityLimit;
				}

				public void setDemoMaternityLimit(String demoMaternityLimit) {
					this.demoMaternityLimit = demoMaternityLimit;
				}

				public String getDemoOpticalCoverage() {
					return demoOpticalCoverage;
				}

				public void setDemoOpticalCoverage(String demoOpticalCoverage) {
					this.demoOpticalCoverage = demoOpticalCoverage;
				}

				public String getDemoOpticalLimit() {
					return demoOpticalLimit;
				}

				public void setDemoOpticalLimit(String demoOpticalLimit) {
					this.demoOpticalLimit = demoOpticalLimit;
				}

				public String getDemoOpticalCopay() {
					return demoOpticalCopay;
				}

				public void setDemoOpticalCopay(String demoOpticalCopay) {
					this.demoOpticalCopay = demoOpticalCopay;
				}

				public String getDemoDentalCoverage() {
					return demoDentalCoverage;
				}

				public void setDemoDentalCoverage(String demoDentalCoverage) {
					this.demoDentalCoverage = demoDentalCoverage;
				}

				public String getDemoDentalLimit() {
					return demoDentalLimit;
				}

				public void setDemoDentalLimit(String demoDentalLimit) {
					this.demoDentalLimit = demoDentalLimit;
				}

				public String getDemoDentalCopay() {
					return demoDentalCopay;
				}

				public void setDemoDentalCopay(String demoDentalCopay) {
					this.demoDentalCopay = demoDentalCopay;
				}

				public String getDemoSlNo() {
					return demoSlNo;
				}

				public void setDemoSlNo(String demoSlNo) {
					this.demoSlNo = demoSlNo;
				}

				public String getDemodataType() {
					return demodataType;
				}

				public void setDemodataType(String demodataType) {
					this.demodataType = demodataType;
				}

				public String getDemoAlertMsg() {
					return demoAlertMsg;
				}

				public void setDemoAlertMsg(String demoAlertMsg) {
					this.demoAlertMsg = demoAlertMsg;
				}

				public ArrayList<Object> getAlDemoPastData() {
					return alDemoPastData;
				}

				public void setAlDemoPastData(ArrayList<Object> alDemoPastData) {
					this.alDemoPastData = alDemoPastData;
				}

				public Long getDemographicSeqId() {
					return demographicSeqId;
				}

				public void setDemographicSeqId(Long demographicSeqId) {
					this.demographicSeqId = demographicSeqId;
				}

				public ArrayList<Object> getAlLoadingGrosspremium() {
					return alLoadingGrosspremium;
				}

				public void setAlLoadingGrosspremium(
						ArrayList<Object> alLoadingGrosspremium) {
					this.alLoadingGrosspremium = alLoadingGrosspremium;
				}

				public String getDemoAlahli() {
					return demoAlahli;
				}

				public void setDemoAlahli(String demoAlahli) {
					this.demoAlahli = demoAlahli;
				}

				public String getMaternityCopay() {
					return maternityCopay;
				}

				public void setMaternityCopay(String maternityCopay) {
					this.maternityCopay = maternityCopay;
				}	
}