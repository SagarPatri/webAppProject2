/**
 * @ (#) UploadMOUDocsAction.java 31 Dec 2014
 * Project      : TTK HealthCare Services
 * File         :UploadMOUDocsAction.java
 * Author       : Kishor kumar S H
 * Company      : RCS Technologies
 * Date Created : 31 Dec 2014
 *
 * @author       : Kishor kumar S H
 * Modified by   : 
 * Modified date : 31 Dec 2014
 */
package com.ttk.dto.administration;

import org.apache.struts.upload.FormFile;

import com.ttk.dto.BaseVO;

/**
 * @author ramakrishna_km
 *
 */
public class MOUDocumentVO extends BaseVO{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 7699784150479850016L;

	public String getDateTime() {
		 return strDateTime;
	      //  return TTKCommon.getFormattedDate(dtDateTime);
	    }//end of getDateTime()
	 public void setDateTime(String strDateTime) {
	        this.strDateTime = strDateTime;
	    }//end of setReceivedDate(Date dtReceivedDate)
	 
	public String getUserId() {
		return strUserId;
	}

	public void setUserId(String strUserId) {
		this.strUserId = strUserId;
	}

	private Long lngMouDocSeqID = null;
	private String strDocName = "";
	private String strMouDocPath = "";
	private Long lngProviderSeqID = null;
	private String strDescription	=	null;
	private	FormFile file=null;
	private String strFileName	=	null;
	private String strDateTime	=	null;
	private String strUserId	=	null;
	
	/** Retrieve the setFileName
	 * @return Returns the strDescription.
	 */
	public String getFileName() {
		return strFileName;
	}//end of getFileName()
	
	/** Sets the strFileName
	 * @param strFileName The strFileName to set.
	 */
	public void setFileName(String strFileName) {
		this.strFileName = strFileName;
	}//end of setFileName(String strFileName)
	
	
	
	/**
	 * @param file the file to set
	 */
	public void setFile(FormFile file) {
		this.file = file;
	}
	/**
	 * @return the file
	 */
	public FormFile getFile() {
		return file;
	}
	
	
	/** Retrieve the Description
	 * @return Returns the strDescription.
	 */
	public String getDescription() {
		return strDescription;
	}//end of getDescription()
	
	/** Sets the Description
	 * @param strDescription The strDescriptione to set.
	 */
	public void setDescription(String strDescription) {
		this.strDescription = strDescription;
	}//end of setDocName(String strDocName)
	
	/** Retrieve the MouDocSeqID
	 * @return Returns the lngMouDocSeqID.
	 */
	public Long getMouDocSeqID() {
		return lngMouDocSeqID;
	}//end of getMouDocSeqID()
	
	/** Sets the MouDocSeqID
	 * @param lngMouDocSeqID The lngMouDocSeqID to set.
	 */
	public void setMouDocSeqID(Long lngMouDocSeqID) {
		this.lngMouDocSeqID = lngMouDocSeqID;
	}//end of setMouDocSeqID(Long lngMouDocSeqID)
	
	/** Retrieve the ProviderSeqID
	 * @return Returns the lngProviderSeqID.
	 */
	public Long getProviderSeqID() {
		return lngProviderSeqID;
	}//end of getProviderSeqID()
	
	/** Sets the ProviderSeqID
	 * @param lngProviderSeqID The lngProviderSeqID to set.
	 */
	public void setProviderSeqID(Long lngProviderSeqID) {
		this.lngProviderSeqID = lngProviderSeqID;
	}//end of setProviderSeqID(Long lngProviderSeqID)
	
	/** Retrieve the DocName
	 * @return Returns the strDocName.
	 */
	public String getDocName() {
		return strDocName;
	}//end of getDocName()
	
	/** Sets the DocName
	 * @param strDocName The strDocName to set.
	 */
	public void setDocName(String strDocName) {
		this.strDocName = strDocName;
	}//end of setDocName(String strDocName)
	
	/** Retrieve the MouDocPath
	 * @return Returns the strMouDocPath.
	 */
	public String getMouDocPath() {
		return strMouDocPath;
	}//end of getMouDocPath()
	
	/** Sets the MouDocPath
	 * @param strMouDocPath The strMouDocPath to set.
	 */
	public void setMouDocPath(String strMouDocPath) {
		this.strMouDocPath = strMouDocPath;
	}//end of setMouDocPath(String strMouDocPath)
}//end of ClauseDocumentVO
