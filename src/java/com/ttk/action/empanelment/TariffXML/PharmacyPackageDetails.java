package com.ttk.action.empanelment.TariffXML;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;


public class PharmacyPackageDetails {

	private List<PharmacyTariffXMLTag> tariffVO;
	
	public PharmacyPackageDetails() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	public PharmacyPackageDetails(List<PharmacyTariffXMLTag> tariffVO) {
		super();
		this.tariffVO = tariffVO;
	}



	public List<PharmacyTariffXMLTag> getTariffVO() {
		return tariffVO;
	}
	
	
	
	@XmlElement(name="tariff")
	public void setTariffVO(List<PharmacyTariffXMLTag> tariffVO) {
		this.tariffVO = tariffVO;
	}


}
