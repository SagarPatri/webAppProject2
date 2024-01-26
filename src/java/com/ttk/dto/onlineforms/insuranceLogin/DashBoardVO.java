/**
 * @ (#) DashBoardVO.java 28 Aug 2015
 * Project       : TTK HealthCare Services
 * File          : DashBoardVO.java
 * Author        : 
 * Company       : RCS
 * Date Created  : 28 Aug 2015
 * @author       : Kishor kumar S H
 * Modified by   : 
 * Modified date : 
 * Reason        : 
 */

package com.ttk.dto.onlineforms.insuranceLogin;
import java.util.Date;

import org.apache.struts.upload.FormFile;

public class DashBoardVO {
	

	private int livesCoverdPI;
	private int livesCoverdNonPI;
	private int premUnderPI;
	private int premUnderNonPI;
	private int claimsReceived;
	private int claimsPaid;
	private int amountClaimed;
	private int amountPaid;
	private int cashlessReceived;
	private int cashlessApproved;
	private String claimsPortfolioRatio;
	private int ttlNoOfProviders;
	private String topProv1;
	private String topProv2;
	private String topProv3;
	private String topProv4;
	private String topProv5;
	private String topProvNames;
	private String cardsTAT;
	private String cashlessTAT;
	private String claimsTAT;
	
	// added for Claims BulkUploadDashBoard
	private String sStartDate;
	private String sEndDate;
	private int vingsBlkExlCnt;
	private int providerBlkExlCnt;
	private int pbmBlkExlCnt;
	private int vingsRcvClmCnt;
	private int providerRcvClmCnt;
	private int pbmRcvClmCnt;
	private int vingsRcvActCnt;
	private int providerRcvActCnt;
	private int pbmRcvActCnt;
	private int vingsPrcClmCnt;
	private int providerPrcClmCnt;
	private int pbmPrcClmCnt;
	private int vingsPndClmCnt;
	private int providerPndClmCnt;
	private int pbmPndClmCnt;
	private int vingsPndActCnt;
	private int providerPndActCnt;
	private int pbmPndActCnt;
	private int providerBlkPrcCnt;
	private int pbmBlkPrcCnt;
	private int vingsBlkPrcCnt;

	public int getLivesCoverdPI() {
		return livesCoverdPI;
	}
	public void setLivesCoverdPI(int livesCoverdPI) {
		this.livesCoverdPI = livesCoverdPI;
	}
	public int getLivesCoverdNonPI() {
		return livesCoverdNonPI;
	}
	public void setLivesCoverdNonPI(int livesCoverdNonPI) {
		this.livesCoverdNonPI = livesCoverdNonPI;
	}
	public int getPremUnderPI() {
		return premUnderPI;
	}
	public void setPremUnderPI(int premUnderPI) {
		this.premUnderPI = premUnderPI;
	}
	public int getPremUnderNonPI() {
		return premUnderNonPI;
	}
	public void setPremUnderNonPI(int premUnderNonPI) {
		this.premUnderNonPI = premUnderNonPI;
	}
	public int getClaimsReceived() {
		return claimsReceived;
	}
	public void setClaimsReceived(int claimsReceived) {
		this.claimsReceived = claimsReceived;
	}
	public int getClaimsPaid() {
		return claimsPaid;
	}
	public void setClaimsPaid(int claimsPaid) {
		this.claimsPaid = claimsPaid;
	}
	public int getAmountClaimed() {
		return amountClaimed;
	}
	public void setAmountClaimed(int amountClaimed) {
		this.amountClaimed = amountClaimed;
	}
	public int getAmountPaid() {
		return amountPaid;
	}
	public void setAmountPaid(int amountPaid) {
		this.amountPaid = amountPaid;
	}
	public int getCashlessReceived() {
		return cashlessReceived;
	}
	public void setCashlessReceived(int cashlessReceived) {
		this.cashlessReceived = cashlessReceived;
	}
	public int getCashlessApproved() {
		return cashlessApproved;
	}
	public void setCashlessApproved(int cashlessApproved) {
		this.cashlessApproved = cashlessApproved;
	}
	public String getClaimsPortfolioRatio() {
		return claimsPortfolioRatio;
	}
	public void setClaimsPortfolioRatio(String claimsPortfolioRatio) {
		this.claimsPortfolioRatio = claimsPortfolioRatio;
	}
	public int getTtlNoOfProviders() {
		return ttlNoOfProviders;
	}
	public void setTtlNoOfProviders(int ttlNoOfProviders) {
		this.ttlNoOfProviders = ttlNoOfProviders;
	}
	public String getTopProv1() {
		return topProv1;
	}
	public void setTopProv1(String topProv1) {
		this.topProv1 = topProv1;
	}
	public String getTopProv2() {
		return topProv2;
	}
	public void setTopProv2(String topProv2) {
		this.topProv2 = topProv2;
	}
	public String getTopProv3() {
		return topProv3;
	}
	public void setTopProv3(String topProv3) {
		this.topProv3 = topProv3;
	}
	public String getTopProv4() {
		return topProv4;
	}
	public void setTopProv4(String topProv4) {
		this.topProv4 = topProv4;
	}
	public String getTopProv5() {
		return topProv5;
	}
	public void setTopProv5(String topProv5) {
		this.topProv5 = topProv5;
	}
	public String getCardsTAT() {
		return cardsTAT;
	}
	public void setCardsTAT(String cardsTAT) {
		this.cardsTAT = cardsTAT;
	}
	public String getCashlessTAT() {
		return cashlessTAT;
	}
	public void setCashlessTAT(String cashlessTAT) {
		this.cashlessTAT = cashlessTAT;
	}
	public String getClaimsTAT() {
		return claimsTAT;
	}
	public void setClaimsTAT(String claimsTAT) {
		this.claimsTAT = claimsTAT;
	}
	public String getTopProvNames() {
		return topProvNames;
	}
	public void setTopProvNames(String topProvNames) {
		this.topProvNames = topProvNames;
	}
	public int getVingsBlkExlCnt() {
		return vingsBlkExlCnt;
	}
	public void setVingsBlkExlCnt(int vingsBlkExlCnt) {
		this.vingsBlkExlCnt = vingsBlkExlCnt;
	}
	public int getProviderBlkExlCnt() {
		return providerBlkExlCnt;
	}
	public void setProviderBlkExlCnt(int providerBlkExlCnt) {
		this.providerBlkExlCnt = providerBlkExlCnt;
	}
	public int getPbmBlkExlCnt() {
		return pbmBlkExlCnt;
	}
	public void setPbmBlkExlCnt(int pbmBlkExlCnt) {
		this.pbmBlkExlCnt = pbmBlkExlCnt;
	}
	public String getsStartDate() {
		return sStartDate;
	}
	public void setsStartDate(String sStartDate) {
		this.sStartDate = sStartDate;
	}
	public String getsEndDate() {
		return sEndDate;
	}
	public void setsEndDate(String sEndDate) {
		this.sEndDate = sEndDate;
	}
	public int getVingsRcvClmCnt() {
		return vingsRcvClmCnt;
	}
	public void setVingsRcvClmCnt(int vingsRcvClmCnt) {
		this.vingsRcvClmCnt = vingsRcvClmCnt;
	}
	public int getProviderRcvClmCnt() {
		return providerRcvClmCnt;
	}
	public void setProviderRcvClmCnt(int providerRcvClmCnt) {
		this.providerRcvClmCnt = providerRcvClmCnt;
	}
	public int getPbmRcvClmCnt() {
		return pbmRcvClmCnt;
	}
	public void setPbmRcvClmCnt(int pbmRcvClmCnt) {
		this.pbmRcvClmCnt = pbmRcvClmCnt;
	}
	public int getVingsRcvActCnt() {
		return vingsRcvActCnt;
	}
	public void setVingsRcvActCnt(int vingsRcvActCnt) {
		this.vingsRcvActCnt = vingsRcvActCnt;
	}
	public int getProviderRcvActCnt() {
		return providerRcvActCnt;
	}
	public void setProviderRcvActCnt(int providerRcvActCnt) {
		this.providerRcvActCnt = providerRcvActCnt;
	}
	public int getPbmRcvActCnt() {
		return pbmRcvActCnt;
	}
	public void setPbmRcvActCnt(int pbmRcvActCnt) {
		this.pbmRcvActCnt = pbmRcvActCnt;
	}
	public int getVingsPrcClmCnt() {
		return vingsPrcClmCnt;
	}
	public void setVingsPrcClmCnt(int vingsPrcClmCnt) {
		this.vingsPrcClmCnt = vingsPrcClmCnt;
	}
	public int getProviderPrcClmCnt() {
		return providerPrcClmCnt;
	}
	public void setProviderPrcClmCnt(int providerPrcClmCnt) {
		this.providerPrcClmCnt = providerPrcClmCnt;
	}
	public int getPbmPrcClmCnt() {
		return pbmPrcClmCnt;
	}
	public void setPbmPrcClmCnt(int pbmPrcClmCnt) {
		this.pbmPrcClmCnt = pbmPrcClmCnt;
	}
	public int getVingsPndClmCnt() {
		return vingsPndClmCnt;
	}
	public void setVingsPndClmCnt(int vingsPndClmCnt) {
		this.vingsPndClmCnt = vingsPndClmCnt;
	}
	public int getProviderPndClmCnt() {
		return providerPndClmCnt;
	}
	public void setProviderPndClmCnt(int providerPndClmCnt) {
		this.providerPndClmCnt = providerPndClmCnt;
	}
	public int getPbmPndClmCnt() {
		return pbmPndClmCnt;
	}
	public void setPbmPndClmCnt(int pbmPndClmCnt) {
		this.pbmPndClmCnt = pbmPndClmCnt;
	}
	public int getVingsPndActCnt() {
		return vingsPndActCnt;
	}
	public void setVingsPndActCnt(int vingsPndActCnt) {
		this.vingsPndActCnt = vingsPndActCnt;
	}
	public int getProviderPndActCnt() {
		return providerPndActCnt;
	}
	public void setProviderPndActCnt(int providerPndActCnt) {
		this.providerPndActCnt = providerPndActCnt;
	}
	public int getPbmPndActCnt() {
		return pbmPndActCnt;
	}
	public void setPbmPndActCnt(int pbmPndActCnt) {
		this.pbmPndActCnt = pbmPndActCnt;
	}
	public int getProviderBlkPrcCnt() {
		return providerBlkPrcCnt;
	}
	public void setProviderBlkPrcCnt(int providerBlkPrcCnt) {
		this.providerBlkPrcCnt = providerBlkPrcCnt;
	}
	public int getPbmBlkPrcCnt() {
		return pbmBlkPrcCnt;
	}
	public void setPbmBlkPrcCnt(int pbmBlkPrcCnt) {
		this.pbmBlkPrcCnt = pbmBlkPrcCnt;
	}
	public int getVingsBlkPrcCnt() {
		return vingsBlkPrcCnt;
	}
	public void setVingsBlkPrcCnt(int vingsBlkPrcCnt) {
		this.vingsBlkPrcCnt = vingsBlkPrcCnt;
	}
	
	
}// End of DashBoardVO
