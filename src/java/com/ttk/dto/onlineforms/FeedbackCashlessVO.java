
/**
 * @ (#) FeedbackCashlessVO.java Apr 24, 2012
 * Project 	     : TTK HealthCare Services
 * File          : OnlineFeedbackManager.java
 * Author        : Manohar
 * Company       : RCS
 * Date Created  : Apr 24, 2012
 *
 * @author       :  Manohar
 * Modified by   :  
 * Modified date :  
 * Reason        :  
 */

/**
 * This class is added For CR KOC1168 Feedback Form 
 * 
 */
package com.ttk.dto.onlineforms;

import com.ttk.dto.BaseVO;
import java.util.ArrayList;

public class FeedbackCashlessVO extends BaseVO{
	
	private String insuredName = null;
	private Long employeeId = null;
	
	private Long questionSeqID=null;
	

	private Long answerSeqID=null;
	private String qustname = null;
	
	
	private String answerName = null;
	
	
	private String remarks = null;
	private String remarks1 = null;
	
	private ArrayList feedBack=null;
	private ArrayList alQuestionList=null;

	public ArrayList getAlQuestionList() {
		return alQuestionList;
	}

	public void setAlQuestionList(ArrayList alQuestionList) {
		this.alQuestionList = alQuestionList;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Long getQuestionSeqID() {
		return questionSeqID;
	}

	public void setQuestionSeqID(Long questionSeqID) {
		this.questionSeqID = questionSeqID;
	}

	public Long getAnswerSeqID() {
		return answerSeqID;
	}

	public void setAnswerSeqID(Long answerSeqID) {
		this.answerSeqID = answerSeqID;
	}

	public String getQustname() {
		return qustname;
	}

	public void setQustname(String qustname) {
		this.qustname = qustname;
	}

	public String getAnswerName() {
		return answerName;
	}

	public void setAnswerName(String answerName) {
		this.answerName = answerName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRemarks1() {
		return remarks1;
	}

	public void setRemarks1(String remarks1) {
		this.remarks1 = remarks1;
	}

	public ArrayList getFeedBack() {
		return feedBack;
	}

	public void setFeedBack(ArrayList feedBack) {
		this.feedBack = feedBack;
	}
}//end of FeedbackCashlessVO

