<%
/** @ (#) policydetail.jsp Feb 1st, 2006
 * Project    	 : TTK Healthcare Services
 * File       	 : policydetail.jsp
 * Author     	 : Bhaskar Sandra
 * Company    	 : Span Systems Corporation
 * Date Created	 : Feb 1st, 2006
 * @author 		 : Bhaskar Sandra
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>

<%@ page import=" com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<script language="JavaScript" src="/ttk/scripts/validation.js"></script>
<script language="JavaScript" src="/ttk/scripts/inwardentry/policydetail.js"></script>
<%
	boolean viewmode=true;
	if(TTKCommon.isAuthorized(request,"Edit"))
	{
		viewmode=false;
	}
	pageContext.setAttribute("policyType",Cache.getCacheObject("enrolTypeCode"));
	pageContext.setAttribute("endorsementType",Cache.getCacheObject("endorsementType"));
	pageContext.setAttribute("ttkBranch",Cache.getCacheObject("officeInfo"));
	pageContext.setAttribute("productChange",Cache.getCacheObject("productChange"));
	pageContext.setAttribute("benefitType",Cache.getCacheObject("benefitType"));
%>

<!-- S T A R T : Content/Form Area -->
<html:form action="/PolicyDetailAction.do" method="post" >
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		<tr>
	    	<td><bean:write name="frmPolicyDetail" property="caption"/></td>
	    	<td align="right" class="webBoard"></td>
  		</tr>
	</table>
	<!-- E N D : Page Title -->
	<div class="contentArea" id="contentArea">
		<html:errors />
	
		<!-- S T A R T : Success Box -->
		<logic:notEmpty name="updated" scope="request">
		   	<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
			    	<td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
			    		<bean:message name="updated" scope="request"/>
			    	</td>
			 	</tr>
			</table>
		</logic:notEmpty>
		<!-- E N D : Success Box -->
	
		<!-- S T A R T : Form Fields -->
		<fieldset>
			<legend>Insurance Company</legend>
			<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
				<tr>
			    	<td width="20%" class="formLabel">Insurance Company:</td>
			    	<td width="30%" class="textLabelBold"><bean:write name="frmPolicyDetail" property="companyName" /></td>
			    	<td width="20%" class="formLabel">Insurance Code:</td>
			    	<td width="30%" class="textLabelBold"><bean:write name="frmPolicyDetail" property="officeCode" /></td>
				</tr>
			</table>
		</fieldset>
		<fieldset>
			<legend>Policy Information</legend>
			<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
	    		<tr>
		        	<td class="formLabel" width="20%">Policy Type: <span class="mandatorySymbol">*</span></td>
		        	<td width="30%">
		        		<html:select  property="policyTypeID"  styleClass="selectBox selectBoxMedium"  onchange="doSelectPolicyType()" disabled="<%= viewmode %>" >		        			 
		        			<html:optionsCollection name="policyType" value="cacheId" label="cacheDesc"/>
		            	</html:select>
					</td>
					
					<td class="formLabel" width="20%">Benefit Type Provided:<span class="mandatorySymbol">*</span></td>
		        	<td width="30%">
		        		<html:select  property="benefitTypeProvided"  styleClass="selectBox selectBoxMedium" disabled="<%= viewmode %>">		        			 
		        			<html:option value="">Select from list</html:option>
		        			<html:optionsCollection name="benefitType" value="cacheId" label="cacheDesc"/>
		            	</html:select>
					</td>
					
				</tr>		
		    	<tr>
		        	<td class="formLabel">Policy No.: <span class="mandatorySymbol">*</span></td>
		        	<td>
		        		<html:text  property="policyNbr" onkeypress='ConvertToUpperCase(event.srcElement);' maxlength="60" styleClass="textBox textBoxMedium" disabled="<%= viewmode %>" readonly="<%= viewmode %>" />
		        	</td>
		        	<td class="formLabel">Previous Policy No.:</td>
		        	<td>
		          		<html:text name="frmPolicyDetail" property="prevPolicyNbr"  onkeypress='ConvertToUpperCase(event.srcElement);' maxlength="60" styleClass="textBox textBoxMedium" disabled="<%= viewmode %>" readonly="<%= viewmode %>" />
		        	</td>
	        	</tr>
				<tr>	
					<td class="formLabel">Product Change: </td>
	        		<td>
	            		<html:select  property="productChangeYN"  styleClass="selectBox selectBoxMedium" disabled="<%= viewmode %>" >
	        				<html:option value="">Select from list</html:option>
	          				<html:optionsCollection name="productChange" value="cacheId" label="cacheDesc"/>
	            		</html:select>
	    			</td>
		        	<td class="formLabel" width="20%">Product Name: <span class="mandatorySymbol">*</span></td>
		        	<td width="30%">
				        <html:select  property="productSeqID"  styleClass="selectBox selectBoxMedium" disabled="<%= viewmode %>" >
			        		<html:option value="">Select from list</html:option>
			        		<html:optionsCollection property="alInsProducts" label="cacheDesc" value="cacheId" />
			        	</html:select>
		        	</td>
	      		</tr>
				<logic:match name="frmPolicyDetail" property="policyTypeID" value="NCR">
	       		<tr style="display:" id="Scheme">
	   			</logic:match>
				<logic:notMatch name="frmPolicyDetail" property="policyTypeID" value="NCR">
	      		<tr style="display:none;" id="Scheme">
				</logic:notMatch>
		    		<td class="formLabel">Policy Name: <span class="mandatorySymbol">*</span></td>
				    <td><html:text property="schemeName" styleClass="textBox textBoxMedium" disabled="<%=viewmode%>" readonly="<%=viewmode%>" onkeyup="ConvertToUpperCase(event.srcElement);"/></td>
		    		<td class="formLabel">Previous Policy Name:</td>
		    		<td><html:text property="prevSchemeName" styleClass="textBox textBoxMedium" disabled="<%=viewmode%>" readonly="<%=viewmode%>" onkeyup="ConvertToUpperCase(event.srcElement);"/></td>
		    	</tr>
	      		<tr>
		    		<td class="formLabel">Type of addition</td>
		        	<td>
		        		<html:select  property="endorseGenTypeID"  styleClass="selectBox selectBoxMedium" disabled="<%= viewmode %>" onchange="javascript:showCustEndorsNo()">
			        		<%-- <html:option value="">Select from list</html:option> --%>
			          		<html:optionsCollection name="endorsementType" value="cacheId" label="cacheDesc"/>
			            </html:select>
<!-- 			based on the req  inward entry will always enrollment,so select dfrpm the list changed to enrollment ,al koot dropdown value changed to endoresement 
 -->			        </td>
		        	 <logic:notMatch name="frmPolicyDetail" property="endorseGenTypeID" scope="session" value="END">
			    	<td>Cust. Endorsement No.: </td>
			    	<%--  <td><html:text property="endorsementNbr" onkeypress='ConvertToUpperCase(event.srcElement);' maxlength="60" styleId="EndosNo" styleClass="textBox textBoxMedium" readonly="true" /> </td> --%>
			    	<td><bean:write name="frmPolicyDetail" property="endorsementNbr" /></td>
			    	</logic:notMatch> 
			    	<logic:match name="frmPolicyDetail" property="endorseGenTypeID" scope="session" value="END">
			    	<td>Cust. Endorsement No.: </td>
			    	<%-- <td><html:text property="endorsementNbr" onkeypress='ConvertToUpperCase(event.srcElement);' maxlength="60" styleId="EndosNo" styleClass="textBox textBoxMedium" readonly="false" /></td> --%>
			    	<td><bean:write name="frmPolicyDetail" property="endorsementNbr" /></td>
			    	</logic:match>
	      		</tr>
	      		<tr>
	        		<td class="formLabel">Photo Present: </td>
	        		<td>
						<html:checkbox styleClass="margin-left:-4px;" property="photoPresentYN" value="Y"/>
	        		</td>
	        		<%-- <td class="formLabel">Policy Not Legible: </td>
	        		<td>
						<html:checkbox styleClass="margin-left:-4px;" property="policyNotLegibleYN" value="Y"/>
	        		</td> --%>
	        	</tr>
	        	<tr>
	        		<td class="formLabel">Al Koot Branch: </td>
	        		<td>
	        			<html:select property="officeSeqID"  styleClass="selectBox selectBoxMedium" disabled="<%= viewmode %>" >
	        				<html:option value="">Select from list</html:option>
	          				<html:optionsCollection name="ttkBranch" value="cacheId" label="cacheDesc"/>
	            		</html:select>
	        		</td>
	        		<td class="formLabel">No. of Photos: </td>
	        		<td>
	        			<html:text property="noofPhotosRcvd" maxlength="10" styleClass="textBox textBoxMedium" disabled="<%= viewmode %>" readonly="<%= viewmode %>" />	
	        		</td>
	      		</tr>
	      		<tr>
	        		<td class="formLabel">DO/BO Change: </td>
	        		<td>
	        			<html:select property="DOBOChangeYN"  styleClass="selectBox selectBoxMedium" disabled="<%= viewmode %>" >
	        				<html:option value="">Select from list</html:option>
	          				<html:optionsCollection name="productChange" value="cacheId" label="cacheDesc"/>
	            		</html:select>
	        		</td>
	        		
	        		<%-- <td class="formLabel" width="20%">Capitation Policy: <span class="mandatorySymbol">*</span></td>
		        	<td>
				        <html:select  property="capitationPolicy"  styleClass="selectBox selectBoxLargest" disabled="<%= viewmode %>" >
			        		<html:option value="">Select from list</html:option>
			        		<html:option value="Y">Yes</html:option>
			        		<html:option value="N">No</html:option>
			           	</html:select>
		        	</td>
	        		
	        		<td>&nbsp;</td> --%>
	      		</tr>
	    	</table>
		</fieldset>
		<logic:notEqual name="frmPolicyDetail" scope="session" property="policyTypeID" value="IND">
		<logic:notEqual name="frmPolicyDetail" scope="session" property="policyTypeID" value="">
		<fieldset>
		    <legend>
		    	<logic:notMatch name="frmPolicyDetail" scope="session" property="policyTypeID" value="ING">
		    	Corporate Information
		    	</logic:notMatch>
		    	<logic:match name="frmPolicyDetail" scope="session" property="policyTypeID" value="ING">
		    	Group Information
		    	</logic:match>
		    </legend>
		    <table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
		    	<tr>
		        	<td width="20%" class="formLabel">
			        	<logic:match name="frmPolicyDetail" scope="session" property="policyTypeID" value="COR">
			    		Corporate
				    	</logic:match>
				    	<logic:notMatch name="frmPolicyDetail" scope="session" property="policyTypeID" value="COR">
				    	Group
				    	</logic:notMatch>
				 		Name:
				 	</td>
		        	<td width="30%" class="textLabelBold"><bean:write name="frmPolicyDetail" property="groupName" />&nbsp;&nbsp;&nbsp;</td>
	   	        	<td width="20%" class="formLabel">Group Id: <span class="mandatorySymbol">*</span></td>
		        	<td width="30%" class="textLabelBold">
		        		<bean:write name="frmPolicyDetail" property="groupID" />
		        		<html:link href="javascript:changeCorporate();"><img src="/ttk/images/EditIcon.gif" title="Change Corporate" alt="Change Corporate" width="16" height="16" border="0" align="absmiddle"></html:link>
		        	</td>
		      	</tr>
		    </table>
		</fieldset>
		</logic:notEqual>
		</logic:notEqual>
		<input type="hidden" name="child" value="PolicyDetail">
		<html:hidden property="groupID" />
		<html:hidden property="groupName" />
		<html:hidden property="mode" />
		<html:hidden property="rownum" />
		<html:hidden property="addEdit" />
		<html:hidden property="hidEndorsementNbrINS" />
		<html:hidden property="hidEndorsementNbrTTK" />
		<!-- E N D : Form Fields -->
		<!-- S T A R T : Buttons -->
		<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
			<tr>
		    	<td width="100%" align="center">
					<button type="button" name="Button" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:doSave()"><u>S</u>ave</button>&nbsp;
					<button type="button" name="Button" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:doReset()"><u>R</u>eset</button>&nbsp;
					<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:doClose()"><u>C</u>lose</button>&nbsp;
		    	</td>
	    	</tr>
		</table>
		<!-- E N D : Buttons -->
	</div>
	<logic:notEmpty name="frmPolicyDetail" property="frmChanged">
	<script> ClientReset=false;TC_PageDataChanged=true;</script>
	</logic:notEmpty>
</html:form>
<!-- E N D : Content/Form Area -->