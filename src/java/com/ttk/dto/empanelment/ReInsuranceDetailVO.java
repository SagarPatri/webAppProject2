
package com.ttk.dto.empanelment;

import java.util.Date;


//import com.ttk.common.TTKCommon;

public class ReInsuranceDetailVO extends InsuranceVO {
	private Long reins_seq_id;
	private Long addr_seq_id;
	private Long reins_bank_seq_id;
	private Long tpa_office_seq_id;
	private String reinsurerId;
	private String reinsurerName;
	private String reinsurerAddress;
	private String treatyID;
	private String reInsuranceStructureType;
	private String treatyType;
	private String pricingTerms;
	private String retentionSharePerc;
	private String reinsExpAllowancePerc;
	private String cedantsExpAllowancePerc;
	private String reInsBankAccountNumber;
	

	private String unexpiredPremReservBasis;
	private String frequencyORremittance;
	private String freqOfGenOfBordereaux;
	private String profitShareBasis;
	private String profitSharePercentage;
	private String profitSharePremiumPerc;
	private String profitShareClaimsPerc;
	private String profitShareExpensesPerc;
	private String reportingCurrency;
	private String remittingCurrency;
	private AddressVO addressVO;
	private String bankName;
	private String bankAddress1;
	private String bankAddress2;
	private String bankAddress3;
	private String IBANSwiftCode;
	private String bankBranch;
	private String accountType;
	private String bankAccountNo;
	private String bankPhone;
	private String activeYN;
	private String inactivatedDate;
	private Date empanelDate;
	private String reinsuranceSharePerc;
	private String spretentionSharePerc;
	private String spreinsuranceSharePerc;
	public String getReinsurerId() {
		return reinsurerId;
	}
	public void setReinsurerId(String reinsurerId) {
		this.reinsurerId = reinsurerId;
	}
	public String getReinsurerName() {
		return reinsurerName;
	}
	public void setReinsurerName(String reinsurerName) {
		this.reinsurerName = reinsurerName;
	}
	public String getReinsurerAddress() {
		return reinsurerAddress;
	}
	public void setReinsurerAddress(String reinsurerAddress) {
		this.reinsurerAddress = reinsurerAddress;
	}
	public String getTreatyID() {
		return treatyID;
	}
	public void setTreatyID(String treatyID) {
		this.treatyID = treatyID;
	}
	public String getReInsuranceStructureType() {
		return reInsuranceStructureType;
	}
	public void setReInsuranceStructureType(String reInsuranceStructureType) {
		this.reInsuranceStructureType = reInsuranceStructureType;
	}
	public String getTreatyType() {
		return treatyType;
	}
	public void setTreatyType(String treatyType) {
		this.treatyType = treatyType;
	}
	public String getPricingTerms() {
		return pricingTerms;
	}
	public void setPricingTerms(String pricingTerms) {
		this.pricingTerms = pricingTerms;
	}
	public String getRetentionSharePerc() {
		return retentionSharePerc;
	}
	public void setRetentionSharePerc(String retentionSharePerc) {
		this.retentionSharePerc = retentionSharePerc;
	}
	public String getReinsExpAllowancePerc() {
		return reinsExpAllowancePerc;
	}
	public void setReinsExpAllowancePerc(String reinsExpAllowancePerc) {
		this.reinsExpAllowancePerc = reinsExpAllowancePerc;
	}
	public String getCedantsExpAllowancePerc() {
		return cedantsExpAllowancePerc;
	}
	public void setCedantsExpAllowancePerc(String cedantsExpAllowancePerc) {
		this.cedantsExpAllowancePerc = cedantsExpAllowancePerc;
	}
	public String getReInsBankAccountNumber() {
		return reInsBankAccountNumber;
	}
	public void setReInsBankAccountNumber(String reInsBankAccountNumber) {
		this.reInsBankAccountNumber = reInsBankAccountNumber;
	}
	public String getIBANSwiftCode() {
		return IBANSwiftCode;
	}
	public void setIBANSwiftCode(String iBANSwiftCode) {
		IBANSwiftCode = iBANSwiftCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankAddress1() {
		return bankAddress1;
	}
	public void setBankAddress1(String bankAddress1) {
		this.bankAddress1 = bankAddress1;
	}
	public String getBankAddress2() {
		return bankAddress2;
	}
	public void setBankAddress2(String bankAddress2) {
		this.bankAddress2 = bankAddress2;
	}
	public String getBankAddress3() {
		return bankAddress3;
	}
	public void setBankAddress3(String bankAddress3) {
		this.bankAddress3 = bankAddress3;
	}
	public String getFrequencyORremittance() {
		return frequencyORremittance;
	}
	public void setFrequencyORremittance(String frequencyORremittance) {
		this.frequencyORremittance = frequencyORremittance;
	}
	public String getUnexpiredPremReservBasis() {
		return unexpiredPremReservBasis;
	}
	public void setUnexpiredPremReservBasis(String unexpiredPremReservBasis) {
		this.unexpiredPremReservBasis = unexpiredPremReservBasis;
	}
	public String getFreqOfGenOfBordereaux() {
		return freqOfGenOfBordereaux;
	}
	public void setFreqOfGenOfBordereaux(String freqOfGenOfBordereaux) {
		this.freqOfGenOfBordereaux = freqOfGenOfBordereaux;
	}
	public String getProfitShareBasis() {
		return profitShareBasis;
	}
	public void setProfitShareBasis(String profitShareBasis) {
		this.profitShareBasis = profitShareBasis;
	}
	public String getProfitSharePercentage() {
		return profitSharePercentage;
	}
	public void setProfitSharePercentage(String profitSharePercentage) {
		this.profitSharePercentage = profitSharePercentage;
	}
	public String getProfitSharePremiumPerc() {
		return profitSharePremiumPerc;
	}
	public void setProfitSharePremiumPerc(String profitSharePremiumPerc) {
		this.profitSharePremiumPerc = profitSharePremiumPerc;
	}
	public String getProfitShareClaimsPerc() {
		return profitShareClaimsPerc;
	}
	public void setProfitShareClaimsPerc(String profitShareClaimsPerc) {
		this.profitShareClaimsPerc = profitShareClaimsPerc;
	}
	public String getProfitShareExpensesPerc() {
		return profitShareExpensesPerc;
	}
	public void setProfitShareExpensesPerc(String profitShareExpensesPerc) {
		this.profitShareExpensesPerc = profitShareExpensesPerc;
	}
	public String getReportingCurrency() {
		return reportingCurrency;
	}
	public void setReportingCurrency(String reportingCurrency) {
		this.reportingCurrency = reportingCurrency;
	}
	public String getRemittingCurrency() {
		return remittingCurrency;
	}
	public void setRemittingCurrency(String remittingCurrency) {
		this.remittingCurrency = remittingCurrency;
	}
	public AddressVO getAddressVO() {
		return addressVO;
	}
	public void setAddressVO(AddressVO addressVO) {
		this.addressVO = addressVO;
	}
	public String getBankBranch() {
		return bankBranch;
	}
	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getBankAccountNo() {
		return bankAccountNo;
	}
	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}
	public String getBankPhone() {
		return bankPhone;
	}
	public void setBankPhone(String bankPhone) {
		this.bankPhone = bankPhone;
	}
	public Long getReins_seq_id() {
		return reins_seq_id;
	}
	public void setReins_seq_id(Long reins_seq_id) {
		this.reins_seq_id = reins_seq_id;
	}
	public Long getAddr_seq_id() {
		return addr_seq_id;
	}
	public void setAddr_seq_id(Long addr_seq_id) {
		this.addr_seq_id = addr_seq_id;
	}
	public Long getReins_bank_seq_id() {
		return reins_bank_seq_id;
	}
	public void setReins_bank_seq_id(Long reins_bank_seq_id) {
		this.reins_bank_seq_id = reins_bank_seq_id;
	}
	public Long getTpa_office_seq_id() {
		return tpa_office_seq_id;
	}
	public void setTpa_office_seq_id(Long tpa_office_seq_id) {
		this.tpa_office_seq_id = tpa_office_seq_id;
	}
	public String getActiveYN() {
		return activeYN;
	}
	public void setActiveYN(String activeYN) {
		this.activeYN = activeYN;
	}
	public String getInactivatedDate() {
		return inactivatedDate;
	}
	public void setInactivatedDate(String inactivatedDate) {
		this.inactivatedDate = inactivatedDate;
	}
	public Date getEmpanelDate() {
		return empanelDate;
	}
	public void setEmpanelDate(Date empanelDate) {
		this.empanelDate = empanelDate;
	}
	public String getReinsuranceSharePerc() {
		return reinsuranceSharePerc;
	}
	public void setReinsuranceSharePerc(String reinsuranceSharePerc) {
		this.reinsuranceSharePerc = reinsuranceSharePerc;
	}
	public String getSpretentionSharePerc() {
		return spretentionSharePerc;
	}
	public void setSpretentionSharePerc(String spretentionSharePerc) {
		this.spretentionSharePerc = spretentionSharePerc;
	}
	public String getSpreinsuranceSharePerc() {
		return spreinsuranceSharePerc;
	}
	public void setSpreinsuranceSharePerc(String spreinsuranceSharePerc) {
		this.spreinsuranceSharePerc = spreinsuranceSharePerc;
	}

	
	
}
