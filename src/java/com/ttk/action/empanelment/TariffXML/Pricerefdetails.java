package com.ttk.action.empanelment.TariffXML;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Pricerefdetails {

	private String corporateid;
	private String insuranceid;
	private String empanelnumber;
	private Long userid;
	private String tariffflag;
	private String networktype;
	private String discAt;
	private String uploadType;
	
	private PackageDetails packageDetails;
	private PharmacyPackageDetails pharmacyPackageDetails;
	private String licenceNo;
	
	private String groupNetwork;
	public Pricerefdetails() {
		// TODO Auto-generated constructor stub
	}
	
	public Pricerefdetails(String corporateid, String insuranceid,
			String empanelnumber, Long userid, String tariffflag,
			String networktype, PackageDetails packageDetails,String discAt,String uploadType) {
		super();
		this.corporateid = corporateid;
		this.insuranceid = insuranceid;
		this.empanelnumber = empanelnumber;
		this.userid = userid;
		this.tariffflag = tariffflag;
		this.networktype = networktype;
		this.packageDetails = packageDetails;
		this.discAt = discAt;
		this.uploadType = uploadType;
	}
	
	public Pricerefdetails(String corporateid, String insuranceid,
			String empanelnumber, Long userid, String tariffflag,
			String networktype, PackageDetails packageDetails,String discAt,String uploadType, String licenceNo, String groupNetwork) {
		super();
		this.corporateid = corporateid;
		this.insuranceid = insuranceid;
		this.empanelnumber = empanelnumber;
		this.userid = userid;
		this.tariffflag = tariffflag;
		this.networktype = networktype;
		this.packageDetails = packageDetails;
		this.discAt = discAt;
		this.uploadType = uploadType;
		this.licenceNo = licenceNo;
		this.groupNetwork = groupNetwork;
	}
	
	public Pricerefdetails(String corporateid, String insuranceid,
			String empanelnumber, Long userid, String tariffflag,
			String networktype, PharmacyPackageDetails pharmacyPackageDetails,String discAt,String uploadType) {
		super();
		this.corporateid = corporateid;
		this.insuranceid = insuranceid;
		this.empanelnumber = empanelnumber;
		this.userid = userid;
		this.tariffflag = tariffflag;
		this.networktype = networktype;
		this.setPharmacyPackageDetails(pharmacyPackageDetails);
		this.discAt = discAt;
		this.uploadType = uploadType;
	}




	public PackageDetails getPackageDetails() {
		return packageDetails;
	}
	@XmlElement
	public void setPackageDetails(PackageDetails packageDetails) {
		this.packageDetails = packageDetails;
	}
	
	public String getNetworktype() {
		return networktype;
	}
	@XmlAttribute
	public void setNetworktype(String networktype) {
		this.networktype = networktype;
	}
	public String getCorporateid() {
		return corporateid;
	}
	@XmlAttribute
	public void setCorporateid(String corporateid) {
		this.corporateid = corporateid;
	}
	public String getInsuranceid() {
		return insuranceid;
	}
	@XmlAttribute
	public void setInsuranceid(String insuranceid) {
		this.insuranceid = insuranceid;
	}
	public String getEmpanelnumber() {
		return empanelnumber;
	}
	@XmlAttribute
	public void setEmpanelnumber(String empanelnumber) {
		this.empanelnumber = empanelnumber;
	}
	public Long getUserid() {
		return userid;
	}
	@XmlAttribute
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public String getTariffflag() {
		return tariffflag;
	}
	@XmlAttribute
	public void setTariffflag(String tariffflag) {
		this.tariffflag = tariffflag;
	}
	public String getDiscAt() {
		return discAt;
	}
	@XmlAttribute
	public void setDiscAt(String discAt) {
		this.discAt = discAt;
	}

	public PharmacyPackageDetails getPharmacyPackageDetails() {
		return pharmacyPackageDetails;
	}

	@XmlElement
	public void setPharmacyPackageDetails(PharmacyPackageDetails pharmacyPackageDetails) {
		this.pharmacyPackageDetails = pharmacyPackageDetails;
	}

	public String getUploadType() {
		return uploadType;
	}

	@XmlAttribute
	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}

	public String getLicenceNo() {
		return licenceNo;
	}

	@XmlAttribute
	public void setLicenceNo(String licenceNo) {
		this.licenceNo = licenceNo;
	}

	public String getGroupNetwork() {
		return groupNetwork;
	}

	@XmlAttribute
	public void setGroupNetwork(String groupNetwork) {
		this.groupNetwork = groupNetwork;
	}

}

