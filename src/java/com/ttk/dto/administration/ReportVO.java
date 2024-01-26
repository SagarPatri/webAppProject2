/**
 * @ (#) ReportVO.java Oct 21, 2005
 * Project      : TTK HealthCare Services
 * File         : ReportVO
 * Author       : Ramakrishna K M
 * Company      : Span Systems Corporation
 * Date Created : Oct 21, 2005
 *
 * @author       :  Ramakrishna K M
 * Modified by   : 
 * Modified date : 
 * Reason        : 
 */
package com.ttk.dto.administration;

import java.util.ArrayList;


public class ReportVO {
	
    public ArrayList getAlColumns() {
		return alColumns;
	}

	public void setAlColumns(ArrayList alColumns) {
		this.alColumns = alColumns;
	}

	private ArrayList alColumns	=	null;
    
}//end of ReportVO
