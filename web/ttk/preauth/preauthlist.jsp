<%
    /** @ (#) preauthlist.jsp April 21, 2006
     * Project     : TTK Healthcare Services
     * File        : preauthlist.jsp
     * Author      : Chandrasekaran J
     * Company     : Span Systems Corporation
     * Date Created: April 21, 2006
     *
     * @author 		 : Chandrasekaran J
     * Modified by   :
     * Modified date :
     * Reason        :
     *
     */
%>

<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk"%>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>

<%@ page
    import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.HashMap,java.util.ArrayList,com.ttk.common.PreAuthWebBoardHelper"%>
<%@ page
    import="com.ttk.dto.usermanagement.UserSecurityProfile,com.ttk.dto.administration.WorkflowVO"%>
<SCRIPT type="text/javascript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="/ttk/scripts/preauth/preauthlist.js"></script>

<script>
    bAction = false;
    var TC_Disabled = true;
</script>
<SCRIPT LANGUAGE="JavaScript">
var JS_SecondSubmit=false;
</SCRIPT>

<%
    pageContext.setAttribute("sAmount", Cache.getCacheObject("amount"));
    pageContext.setAttribute("sSource", Cache.getCacheObject("source"));
    pageContext.setAttribute("sStatus",
            Cache.getCacheObject("preauthStatus"));
    pageContext.setAttribute("sTtkBranch",
            Cache.getCacheObject("officeInfo"));
    pageContext.setAttribute("sAssignedTo",
            Cache.getCacheObject("assignedTo"));
    pageContext.setAttribute("sPreAuthType",
            Cache.getCacheObject("preauthType"));
    pageContext.setAttribute("ProviderList",
            Cache.getCacheObject("ProviderList"));
    pageContext.setAttribute("insuranceCompany",
            Cache.getCacheObject("insuranceCompany"));
    pageContext.setAttribute("source", Cache.getCacheObject("source"));
    pageContext.setAttribute("submissionCatagory",
            Cache.getCacheObject("submissionCatagory"));
    pageContext.setAttribute("preShortFallStatus",
            Cache.getCacheObject("preShortfallStatus"));
      pageContext.setAttribute("benefitTypes",Cache.getCacheObject("prebenefitTypes"));
	  	pageContext.setAttribute("PartnerList",Cache.getCacheObject("PartnerList"));

	 pageContext.setAttribute("internalRemarkStatus",Cache.getCacheObject("internalRemarkStatus"));

    HashMap hmWorkflow = ((UserSecurityProfile) request.getSession()
            .getAttribute("UserSecurityProfile")).getWorkFlowMap();
    ArrayList alWorkFlow = null;

    if (hmWorkflow != null && hmWorkflow.containsKey(new Long(3))) //to get the workflow of Pre-Auth
    {
        alWorkFlow = ((WorkflowVO) hmWorkflow.get(new Long(3)))
                .getEventVO();
    }

    pageContext.setAttribute("listWorkFlow", alWorkFlow);
%>
<!-- S T A R T : Content/Form Area -->
<html:form action="/PreAuthAction.do">
    <!-- S T A R T : Page Title -->
    <table align="center" class="pageTitle" border="0" cellspacing="0"
        cellpadding="0">
        <tr>
            <td width="57%">List of Pre-Approval</td>
            <td width="43%" align="right" class="webBoard">&nbsp;<%@ include
                    file="/ttk/common/toolbar.jsp"%></td>
        </tr>
    </table>
    <!-- E N D : Page Title -->
    <html:errors />
    <div class="contentArea" id="contentArea">
        <!-- S T A R T : Search Box -->
        <table align="center" class="searchContainer" border="0"
            cellspacing="0" cellpadding="0">
            <tr class="searchContainerWithTab">
                <td nowrap>PreApproval No.:<br> <html:text
                        property="sPreAuthNumber" name="frmPreAuthList"
                        styleClass="textBox textBoxLarge" maxlength="60" />
                </td>
<%-- <td nowrap>Authorization No.<br>
                <html:text property="sAuthorizationNO" name="frmPreAuthList"  styleClass="textBox textBoxLarge textBoxDisabled" maxlength="60" readonly="true" />
     </td>
 --%>


               <td nowrap>Provider Name:<br>
                <html:select property="sProviderName" name="frmPreAuthList"  styleClass="selectBox selectBoxMoreMedium">
                    <html:option value="">Any</html:option>
                    <html:options collection="ProviderList"  property="cacheId" labelProperty="cacheDesc"/>
              </html:select>
             </td>
                         
             <td nowrap>Mode of PreApproval Request:<br>
               <html:select name="frmPreAuthList" property="sSource" styleClass="selectBox selectBoxMoreMedium">
                    <html:option value="">Any</html:option>
                    <html:optionsCollection name="source" label="cacheDesc" value="cacheId" />
                  </html:select>
                <td nowrap>Received Date:<br> <html:text
                        property="sRecievedDate" name="frmPreAuthList"
                        styleClass="textBox textDate" maxlength="10" /><A
                    NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#"
                    onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].sRecievedDate',document.forms[1].sRecievedDate.value,'',event,148,178);return false;"
                    onMouseOver="window.status='Calendar';return true;"
                    onMouseOut="window.status='';return true;"><img
                        src="/ttk/images/CalendarIcon.gif" title="Calendar" name="mrkDate"
                        width="24" height="17" border="0" align="absmiddle"></a>
				</td>
		    
		    <td nowrap><html:select property="sPayerName"
						name="frmPreAuthList" styleClass="selectBox selectBoxMoreMedium">
						<%-- <html:option value="">Any</html:option> --%>
						<html:options collection="insuranceCompany" property="cacheId"
							labelProperty="cacheDesc" />
					</html:select></td>
			</tr>
			<tr>
                <td nowrap>Al Koot ID:<br> <html:text
                        property="sEnrollmentId" name="frmPreAuthList"
                        styleClass="textBox textBoxLarge" maxlength="60"
                        onkeyup="ConvertToUpperCase(event.srcElement)" />
                </td>
                <td nowrap>Member Name:<br> <html:text
                        property="sClaimantName" name="frmPreAuthList"
                        styleClass="textBox textBoxLarge" maxlength="250" />
                </td>

<td nowrap>Submission Type:<br>
	            <html:select property="sProcessType" styleClass="selectBox selectBoxMoreMedium">



		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="submissionCatagory" label="cacheDesc" value="cacheId" />

            	</html:select>
          	</td>				<logic:equal value="REQ" name="frmPreAuthList" property="sStatus">
					
				</logic:equal>
			
				<td nowrap>Assigned To:<br> <html:select
						property="sAssignedTo"
						styleClass="selectBox selectBoxSmall">
						<html:optionsCollection name="sAssignedTo" label="cacheDesc"
							value="cacheId" />
                    </html:select>
                </td>
				<td nowrap>If Others:<br> <html:text
						property="sSpecifyName"
						styleClass="textBox textBoxMedium selectBoxMediumCustomStyle"
						maxlength="60" />
				</td>
				</tr>
		<tr>
		
		  
             <td nowrap>Qatar ID:<br>
             <html:text property="sQatarId" name="frmPreAuthList" styleClass="textBox textBoxLarge" maxlength="60" />
        </td>
            
				<td nowrap>Event Reference Number :<br> <html:text
						property="eventReferenceNo" name="frmPreAuthList"
						styleClass="textBox textBoxLarge" maxlength="60" />
                </td>

				<!-- Payer Name:<br> -->
				
<td nowrap>Benefit Type:<br>
	            <html:select property="sBenefitType" styleClass="selectBox selectBoxMoreMedium">
		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="benefitTypes" label="cacheDesc"value="cacheId" />
            	</html:select>
          	</td>
			
				
<td nowrap>Pre-approval Req. Amount Range (In QAR):<br>
	            <html:select property="sAmountRange" styleClass="selectBox selectBoxSmall" onchange="javascript:onRangeChanged()">
					<html:option value="">Any</html:option>
					<html:option value="<="><=</html:option>
		  	 		<html:option value=">=">>=</html:option>
		  	 		<html:option value="=">=</html:option>
		  	 		<html:option value="<"><</html:option>
		  	 		<html:option value=">">></html:option>
            	</html:select>

                 <logic:equal value="" name="frmPreAuthList" property="sAmountRange">
                        <html:text property="sAmountRangeValue" name="frmPreAuthList"  styleClass="textBox textBoxSmall" maxlength="10" disabled="true" style="background-color: #EEEEEE;" value=""/>
                </logic:equal>
                <logic:notEqual value="" name="frmPreAuthList" property="sAmountRange">
                        <html:text property="sAmountRangeValue" name="frmPreAuthList"  styleClass="textBox textBoxSmall" maxlength="10"/>
                </logic:notEqual>
            </td>
			 <td nowrap>Partner Name.:<br>
        		<html:select property="sPartnerName" name="frmPreAuthList" styleClass="selectBox selectBoxMoreMedium">
		  	 		<html:option value="">Any</html:option>
		  	 		<html:optionsCollection name="PartnerList" label="cacheDesc" value="cacheId" />
            	</html:select>
        	</td>
        </tr>
            <tr>
		
		    <td nowrap>Policy No.:<br>
            	<html:text property="sPolicyNumber" name="frmPreAuthList"  styleClass="textBox textBoxLarge" maxlength="60"/>
        	</td>	
                <%-- <td nowrap>GlobalNet Member ID:<br>
                <html:text property="sGlobalNetMemID" name="frmPreAuthList"  styleClass="textBox textBoxLarge" maxlength="250"/>
            </td> --%>
				
				
		<td nowrap>Al Koot Branch:<br>
	            <html:select property="sTtkBranch" name="frmPreAuthList" styleClass="selectBox selectBoxMoreMedium">
		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="sTtkBranch" label="cacheDesc" value="cacheId" />

            	</html:select>
          	</td>
			  <td nowrap>Linked Claim No.:<br>
		    	<html:text property="sLinkedClaimNo" name="frmPreAuthList"  styleClass="textBox textBoxLarge" maxlength="60" />
		    </td>
 			<td nowrap> Internal Remark Status:<br> <html:select property="internalRemarkStatus" name="frmPreAuthList" styleClass="selectBox selectBoxMoreMedium">
						<html:option value="">Any</html:option>
	  				<html:optionsCollection name="internalRemarkStatus" label="cacheDesc" value="cacheId" />
					</html:select>
				</td>
			<td nowrap> Risk Level:<br> <html:select property="riskLevel" name="frmPreAuthList" styleClass="selectBox selectBoxMoreMedium">
					<html:option value="">Any</html:option>
	                <html:option value="LR">Low</html:option>
		  	 		<html:option value="IR">Intermediate</html:option>
		  	 		<html:option value="HR">High</html:option>
					</html:select>
				</td>		
		</tr>
		<tr>
		  <td nowrap> Status:<br> <html:select property="sStatus" name="frmPreAuthList" styleClass="selectBox selectBoxMoreMedium"
						onchange="javascript:onStatusChanged()">
						<html:option value="">Any</html:option>
		        	<html:optionsCollection name="sStatus" label="cacheDesc" value="cacheId" />
					</html:select>
				</td>
			 <logic:equal value="INP" name="frmPreAuthList" property="sStatus">
				<td  nowrap>In-Progress Status:<br>
         			<html:select property ="inProgressStatus" styleClass="selectBox selectBoxMoreMedium">
	           			<html:option value="">Any</html:option>
	           			<html:option  value="FRH">Fresh</html:option>
						<html:option  value="APL">Appeal</html:option>
						<html:option  value="ENH">Enhanced</html:option>
						<html:option  value="RES">Shortfall Responded</html:option>
       				</html:select>
       	   		</td>	
			</logic:equal>
			<td nowrap> CFD Investigation Status:<br> <html:select property="cfdInvestigationStatus" name="frmPreAuthList" styleClass="selectBox selectBoxMoreMedium">
				    <html:option value="">Any</html:option>
	                <html:option value="II">Investigation In-progress </html:option>
	                <html:option value="CA">Cleared for Approval</html:option>
		  	 		<html:option value="PCA">Partially Cleared For Approval</html:option>
		  	 		<html:option value="FD">Fraud Detected</html:option>
					</html:select>
				</td>	
			 
	   <%-- <logic:equal value="REQ" name="frmPreAuthList" property="sStatus">
					<td nowrap>ShortFall Status:<br> <html:select
							property="preShortFallStatus" name="frmPreAuthList"
							styleClass="selectBox selectBoxMoreMedium">
							<html:option value="">Any</html:option>
							<html:optionsCollection name="preShortFallStatus"
								label="cacheDesc" value="cacheId" />
						</html:select>

					</td>
				</logic:equal> --%>
				<td nowrap> Priority Corporate:<br> <html:select property="priorityCorporate" name="frmPreAuthList" styleClass="selectBox selectBoxMoreMedium"
						onchange="javascript:onStatusChanged()">
						<html:option value="">Any</html:option>
	           			<html:option  value="OPC">Priority Coporate Pre-Auth</html:option>
						<html:option  value="NPC">Non Priority Coporate Pre-Auth</html:option>
					</html:select>
					</td>
		<td>				
			<a href="#" accesskey="s" onClick="javascript:onSearch(this)"
					class="search"><img src="/ttk/images/SearchIcon.gif"
						title="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>

				
		
			</td>
		</tr>
		
                </table>
        <!-- E N D : Search Box -->
        <!-- S T A R T : Grid -->
        <ttk:HtmlGrid name="tableData" />
        <!-- E N D : Grid -->
        <!-- S T A R T : Buttons and Page Counter -->
        <table align="center" class="buttonsContainerGrid" border="0"
            cellspacing="0" cellpadding="0">
            <tr>
                <td width="27%"></td>
                <td width="73%" align="right">
                    <%
                        if (TTKCommon.isDataFound(request, "tableData")) {
                    %>
                    <button type="button" name="Button" accesskey="c"
                        class="buttonsCopyWB" onMouseout="this.className='buttonsCopyWB'"
                        onMouseover="this.className='buttonsCopyWB buttonsCopyWBHover'"
                        onClick="javascript:copyToWebBoard()">
                        <u>C</u>opy to Web Board
                    </button>&nbsp; <%
    }
        if (TTKCommon.isAuthorized(request, "Add")) {
 %>
                    <button type="button" name="Button2" accesskey="a" class="buttons"
                        onMouseout="this.className='buttons'"
                        onMouseover="this.className='buttons buttonsHover'"
                        onClick="addPreAuth()">
                        <u>A</u>dd
                    </button>&nbsp; <%
    }
        if (TTKCommon.isAuthorized(request, "Add Oral")) {
 %>
                    <button type="button" name="Button2" accesskey="o" class="buttons"
                        onMouseout="this.className='buttons'"
                        onMouseover="this.className='buttons buttonsHover'"
                        onClick="addOralPreAuth()">
                        Add<u>O</u>ral
                    </button>&nbsp; <%
    }
        if (TTKCommon.isDataFound(request, "tableData")
                && TTKCommon.isAuthorized(request, "Delete")) {
 %> <!-- <button type="button" name="Button" accesskey="d" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onDelete()"><u>D</u>elete</button> -->
                    <%
                        }
                    %>
                </td>
            </tr>
            <ttk:PageLinks name="tableData" />
            <tr>
                <td height="4" colspan="2"></td>
            </tr>
            <tr>
                <td colspan="2"><span class="textLabelBold">Legend: </span><img
                    src="/ttk/images/HighPriorityIcon.gif" title="High Priority"
                    width="16" height="16" align="absmiddle">- High
                    Priority&nbsp;&nbsp;&nbsp;<img
                    src="/ttk/images/MediumPriorityIcon.gif" title="Medium Priority"
                    width="16" height="16" align="absmiddle">&nbsp;- Medium
                    Priority&nbsp;&nbsp;&nbsp;<img
                    src="/ttk/images/LowPriorityIcon.gif" title="Low Priority" width="16"
                    height="16" align="absmiddle">&nbsp;- Low Priority</td>
            </tr>
        </table>
    </div>
    <!-- E N D : Buttons and Page Counter -->
    <INPUT TYPE="hidden" NAME="rownum" VALUE=''>
    <input type="hidden" name="child" value="">
    <INPUT TYPE="hidden" NAME="mode" VALUE="">
    <INPUT TYPE="hidden" NAME="sortId" VALUE="">
    <INPUT TYPE="hidden" NAME="pageId" VALUE="">
    <INPUT TYPE="hidden" NAME="tab" VALUE="">
    <html:hidden property="AssignAllFlagYN"/>
	<html:hidden property="AssignFlagYN"/>
</html:form>
<!-- E N D : Content/Form Area -->
