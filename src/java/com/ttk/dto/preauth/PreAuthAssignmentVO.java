/**
 * @ (#) PreAuthAssignmentVO.java Apr 17, 2006
 * Project      : TTK HealthCare Services
 * File         : PreAuthAssignmentVO.java
 * Author       : Arun K N
 * Company      : Span Systems Corporation
 * Date Created : Apr 17, 2006
 *
 * @author       :  Arun K N
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.dto.preauth;

import java.util.ArrayList;

import com.ttk.dto.BaseVO;


public class PreAuthAssignmentVO extends BaseVO{

    private Long lngClaimSeqID = null;
    private Long lngPreAuthSeqID=null;
    private String strSelectedPreAuthNos="";
    private Long lngDoctor=null;
    private String strRemarks="";
    private Long lngOfficeSeqID = null;
    private Long lngAssignUserSeqID = null;
    private ArrayList alAssignUserList = null;
    private Long lngPolicySeqID = null;
    private String strSuperUserYN="N";  //By Default it is set to No
    private String strSelectedPreAuthSeqId="";
    private String strSelectedClaimsSeqId="";
    private String strMultipleAssign="";
    private String strSelectedClaimNos="";
    private String strSelectedBatchNos="";
    private Long lngBatchSeqID=null;
    private String strAssignClaimStatus="";
    private String strAssignRemarks="";
    private String strOtherRemarks="";
    private String strSelectedBatchSeqId="";
    private String strAssignTo="";
    private String strAssignDate="";
	private String batchNO;
	private String providerName;
	private String claimType;
	private String submissionType;
	private String strAssignBy="";
    
    /**
     * Retrieve the Super User's status
     * @return  strSuperUserYN String
     */
    public String getSuperUserYN() {
        return strSuperUserYN;
    }//end of getSuperUserYN()

    /**
     * Sets the Super User's status
     * @param  strSuperUserYN String
     */
    public void setSuperUserYN(String strSuperUserYN) {
        this.strSuperUserYN = strSuperUserYN;
    }//end of setSuperUserYN(String strSuperUserYN)

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

    /** Retrieve the PolicySeqID
	 * @return Returns the lngPolicySeqID.
	 */
	public Long getPolicySeqID() {
		return lngPolicySeqID;
	}//end of getPolicySeqID()

	/** Sets the PolicySeqID
	 * @param lngPolicySeqID The lngPolicySeqID to set.
	 */
	public void setPolicySeqID(Long lngPolicySeqID) {
		this.lngPolicySeqID = lngPolicySeqID;
	}//end of setPolicySeqID(Long lngPolicySeqID)

	/** Retrieve the Assign User List
	 * @return Returns the alAssignUserList.
	 */
	public ArrayList getAssignUserList() {
		return alAssignUserList;
	}//end of getAssignUserList()

	/** Sets the Assign User List
	 * @param alAssignUserList The alAssignUserList to set.
	 */
	public void setAssignUserList(ArrayList alAssignUserList) {
		this.alAssignUserList = alAssignUserList;
	}//end of setAssignUserList(ArrayList alAssignUserList)

	/** Retrieve the Assign User Seq ID
	 * @return Returns the lngAssignUserSeqID.
	 */
	public Long getAssignUserSeqID() {
		return lngAssignUserSeqID;
	}//end of getAssignUserSeqID()

	/** Sets the Assign User Seq ID
	 * @param lngAssignUserSeqID The lngAssignUserSeqID to set.
	 */
	public void setAssignUserSeqID(Long lngAssignUserSeqID) {
		this.lngAssignUserSeqID = lngAssignUserSeqID;
	}//end of setAssignUserSeqID(Long lngAssignUserSeqID)

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

	/**
     * Retrieve the Assigned Doctor
     * @return  lngDoctor Long
     */
    public Long getDoctor() {
        return lngDoctor;
    }//end of getDoctor()

    /**
     * Sets the Assigned Doctor
     * @param  lngDoctor Long
     */
    public void setDoctor(Long lngDoctor) {
        this.lngDoctor = lngDoctor;
    }//end of setDoctor(Long lngDoctor)

    /**
     * Retrieve the Remarks
     * @return  strRemarks String
     */
    public String getRemarks() {
        return strRemarks;
    }//end of getRemarks()

    /**
     * Sets the Remarks
     * @param  strRemarks String
     */
    public void setRemarks(String strRemarks) {
        this.strRemarks = strRemarks;
    }//end of setRemarks(String strRemarks)

    /**
     * Retrieve the comma seperated PreAuth Nos to be assigned
     * @return  strSelectedPreAuthNos String
     */
    public String getSelectedPreAuthNos() {
        return strSelectedPreAuthNos;
    }//end of getSelectedPreAuthNos()

    /**
     * Sets the comma seperated PreAuth Nos to be assigned
     * @param  strSelectedPreAuthNos String
     */
    public void setSelectedPreAuthNos(String strSelectedPreAuthNos) {
        this.strSelectedPreAuthNos = strSelectedPreAuthNos;
    }//end of setSelectedPreAuthNos(String strSelectedPreAuthNos)

    /**
     * Retrieve the PreAuth Seq ID to be assigned
     * @return  lngPreAuthSeqID Long
     */
    public Long getPreAuthSeqID() {
        return lngPreAuthSeqID;
    }//end of getPreAuthSeqID()

    /**
     * Sets the PreAuth Seq ID to be assigned
     * @param  lngPreAuthSeqID Long
     */
    public void setPreAuthSeqID(Long lngPreAuthSeqID) {
        this.lngPreAuthSeqID = lngPreAuthSeqID;
    }//end of setPreAuthSeqID(Long lngPreAuthSeqID)
    
    
    
    
    
    
 /*   private ArrayList alAssignPreauthList = null;
    
    public ArrayList getAssignPreauthList() {
    	return alAssignPreauthList;
    }//end of getAssignUserList()

    *//** Sets the Assign User List
     * @param alAssignUserList The alAssignUserList to set.
     *//*
    public void setAssignPreauthList(ArrayList alAssignPreauthList) {
    	this.alAssignPreauthList = alAssignPreauthList;
    }//end of setAssignUserList(ArrayList alAssignUserList)
*/


public String getSelectedPreAuthSeqId() {
    return strSelectedPreAuthSeqId;
}//end of getSelectedPreAuthNos()

public void setSelectedPreAuthSeqId(String strSelectedPreAuthSeqId) {
    this.strSelectedPreAuthSeqId = strSelectedPreAuthSeqId;
}

public String getSelectedClaimsSeqId() {
    return strSelectedClaimsSeqId;
}//end of getSelectedPreAuthNos()

public void setSelectedClaimsSeqId(String strSelectedClaimsSeqId) {
    this.strSelectedClaimsSeqId = strSelectedClaimsSeqId;
}

public String getMultipleAssign() {
    return strMultipleAssign;
}//end of getSelectedPreAuthNos()

public void setMultipleAssign(String strMultipleAssign) {
    this.strMultipleAssign = strMultipleAssign;
}
public String getSelectedClaimNos() {
    return strSelectedClaimNos;
}//end of getSelectedPreAuthNos()

public void setSelectedClaimNos(String strSelectedClaimNos) {
    this.strSelectedClaimNos = strSelectedClaimNos;
}//end of setSelectedPreAuthNos(String strSelectedPreAuthNos)

public String getSelectedBatchNos() {
    return strSelectedBatchNos;
}//end of getSelectedBatchNos()

public void setSelectedBatchNos(String strSelectedBatchNos) {
    this.strSelectedBatchNos = strSelectedBatchNos;
}//end of setSelectedBatchNos(String strSelectedBatchNos)

public Long getBatchSeqID() {
    return lngBatchSeqID;
}//end of getPreAuthSeqID()

public void setBatchSeqID(Long lngBatchSeqID) {
    this.lngBatchSeqID = lngBatchSeqID;
}//end of setPreAuthSeqID(Long lngPreAuthSeqID)

public String getAssignClaimStatus() {
	return strAssignClaimStatus;
}

public void setAssignClaimStatus(String strAssignClaimStatus) {
	this.strAssignClaimStatus = strAssignClaimStatus;
}

public String getAssignRemarks() {
	return strAssignRemarks;
}

public void setAssignRemarks(String strAssignRemarks) {
	this.strAssignRemarks = strAssignRemarks;
}

public String getOtherRemarks() {
	return strOtherRemarks;
}

public void setOtherRemarks(String strOtherRemarks) {
	this.strOtherRemarks = strOtherRemarks;
}

public String getSelectedBatchSeqId() {
	return strSelectedBatchSeqId;
}

public void setSelectedBatchSeqId(String strSelectedBatchSeqId) {
	this.strSelectedBatchSeqId = strSelectedBatchSeqId;
}

public String getAssignTo() {
	return strAssignTo;
}

public void setAssignTo(String strAssignTo) {
	this.strAssignTo = strAssignTo;
}

public String getAssignDate() {
	return strAssignDate;
}

public void setAssignDate(String strAssignDate) {
	this.strAssignDate = strAssignDate;
}

public String getBatchNO() {
	return batchNO;
}
public void setBatchNO(String batchNO) {
	this.batchNO = batchNO;
}

public String getProviderName() {
	return providerName;
}
public void setProviderName(String providerName) {
	this.providerName = providerName;
}

public String getClaimType() {
	return claimType;
}
public void setClaimType(String claimType) {
	this.claimType = claimType;
}

public String getSubmissionType() {
	return submissionType;
}
public void setSubmissionType(String submissionType) {
	this.submissionType = submissionType;
}

public String getAssignBy() {
	return strAssignBy;
}

public void setAssignBy(String strAssignBy) {
	this.strAssignBy = strAssignBy;
}


}//end of PreAuthAssignmentVO.java