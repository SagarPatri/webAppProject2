package com.ttk.business.preauth;

import java.util.ArrayList;

import javax.ejb.Local;

import com.ttk.common.exception.TTKException;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.preauth.PreAuthAssignmentVO;
import com.ttk.dto.preauth.PreDashboardVO;

@Local
public interface PreAuthDashboardManager {

	public PreDashboardVO getManagementDetails(Long userSeqId) throws TTKException;
	
	public ArrayList getDataEntryUserInfo(ArrayList alSearchCreteria) throws TTKException;
	
	public ArrayList getDataEntrySelfAssinmentCase(ArrayList alSearchCreteria) throws TTKException;
	
	public ArrayList getDataEntryActiveUsers(ArrayList al) throws TTKException;
	
	public ArrayList<CacheObject> getAssignedUserList(Long contactSeqid) throws TTKException;
	
	public Long saveAssignedUser(PreAuthAssignmentVO preAuthAssignmentVO) throws TTKException;
	
	public ArrayList getPreauthUserDashBoadinfo(ArrayList alSearchCreteria) throws TTKException;
	
	public ArrayList getPreauthBoardinfo(ArrayList alSearchCreteria) throws TTKException;
	
	public PreDashboardVO getDoctorDashboardDetails(Long userSeqId) throws TTKException;
}
