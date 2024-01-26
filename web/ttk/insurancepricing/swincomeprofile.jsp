<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>

	<script type="text/javascript" SRC="/ttk/scripts/validation.js"></script>
    <script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
    <script type="text/javascript" src="/ttk/scripts/insurancepricing/swpricinghome.js"></script>	
     <script type="text/javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script> 
     <script language="javascript" src="/ttk/scripts/utils.js"></script>
     <SCRIPT LANGUAGE="JavaScript">
	bAction = false; //to avoid change in web board in product list screen //to clarify
	var TC_Disabled = true;
	
	
</SCRIPT>
<%

	int iRowCount = 0;
	int iRowCount2table = 0;
	String livesViewMode = "";
	String textbox = "textBox textBoxVerySmall";

%>

<!-- S T A R T : Content/Form Area -->
<html:form action="/SwInsPricingActionIncome.do"   method="post"  enctype="multipart/form-data"> 
<div class="contentArea" id="contentArea">
	<!-- S T A R T : Page Title -->
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		  <tr>
	    		<td><bean:write name="frmSwIncomeProfile" property="caption"/></td>
			    <td width="43%" align="right" class="webBoard">&nbsp;</td>
		  </tr>
	</table>
		<logic:notEmpty name="frmSwIncomeProfile"	property="pricingNumberAlert">
				<table align="center" class="errorContainer" border="0"	cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/warning.gif" title="Warning" alt="Warning"
							width="16" height="16" align="absmiddle">&nbsp; <bean:write
								name="frmSwIncomeProfile" property="pricingNumberAlert" /></td>
					</tr>
				</table>
			</logic:notEmpty>
	<logic:notEmpty name="updated" scope="request">
  <table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
   <tr>
     <td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
         <bean:message name="updated" scope="request"/>
     </td>
   </tr>
  </table>
 </logic:notEmpty>
 
 	<logic:notEmpty name="notifyerror" scope="request">
  <table align="center" class="errorContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
   <tr>
     <td><img src="/ttk/images/warning.gif" title="Warning" alt="Warning" width="16" height="16" align="absmiddle">&nbsp;
         <bean:write name="notifyerror" scope="request"/>
     </td>
   </tr>
  </table>
 </logic:notEmpty>
 
  	<logic:notEmpty name="successMsg" scope="request">
  <table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
   <tr>
     <td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
         <bean:write name="successMsg" scope="request"/>
     </td>
   </tr>
  </table>
 </logic:notEmpty>
 
  	<logic:notEmpty name="errorMsg" scope="request">
  <table align="center" class="errorContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
   <tr>
     <td><img src="/ttk/images/warning.gif" title="Warning" alt="Warning" width="16" height="16" align="absmiddle">&nbsp;
         <bean:write name="errorMsg" scope="request"/>
     </td>
   </tr>
  </table>
 </logic:notEmpty>
 
 
 

 <logic:empty name="updated" scope="request">
<%--   		<logic:notEmpty name="frmSwIncomeProfile"	property="successMsg">
				<table align="center" class="errorContainer" border="0"	cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/SuccessIcon.gif" alt="Success"
							width="16" height="16" align="absmiddle">&nbsp; <bean:write
								name="frmSwIncomeProfile" property="successMsg" /></td>
					</tr>
				</table>
			</logic:notEmpty>
 </logic:empty>
 
 <logic:empty name="updated" scope="request">
 		<logic:notEmpty name="frmSwIncomeProfile"	property="notifyerror">
				<table align="center" class="errorContainer" border="0"	cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/warning.gif" alt="Warning"
							width="16" height="16" align="absmiddle">&nbsp; <bean:write
								name="frmSwIncomeProfile" property="notifyerror" /></td>
					</tr>
				</table>
			</logic:notEmpty> --%>
 </logic:empty>
 
 

 

	<!-- E N D : Page Title -->
	<!-- S T A R T : Search Box -->
	<html:errors/>
	
	<%-- <logic:match name="frmSwIncomeProfile" property="Message" value="N"> --%>

<!-- 		temp assigned to 21 because benefit seq id is from 1 to 6
 -->	
 <%
 Long tempSeq	=	new Long(21);  int copyobj = 0; String[] reqTotalCoverdLives = null;
 String[] reqNatCoverdLives = null;
 Long totaltempSeq	=	new Long(21);
 
 if(((String[])request.getAttribute("totalCoverdLives"))!=null);
 reqTotalCoverdLives = ((String[])request.getAttribute("totalCoverdLives"));
 
 if(((String[])request.getAttribute("natCoverdLives"))!=null);
 reqNatCoverdLives = ((String[])request.getAttribute("natCoverdLives"));
 Double numberOfLives=0.0d;
/*  String numberOfLives1=null; */
 if(session.getAttribute("numberOfLives")!=null){
	 numberOfLives= (Double) session.getAttribute("numberOfLives");
/*  numberOfLives=Double.parseDouble(numberOfLives1); */
 }
 
 %>
 
 <div style="width:68%" align="left">
 	 <br><br>
 	 <logic:notEmpty name="frmSwIncomeProfile" property="profileBenefitList">
 	   <table align="center"  class="gridWithPricing" border="0" cellspacing="1" cellpadding="0">
			       <tr>
          	    <td width="100%" height="28" nowrap class="fieldGroupHead-r">
      		       File Name : 	<html:file  property="stmFile" styleId="stmFile"/>   <!--  required -->
      		       <%
             if(TTKCommon.isAuthorized(request,"Edit")) {
               %>	
      		     &nbsp; <button type="button" name="uploadButton" accesskey="u" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'"     onClick="javascript:onUploadMemDetails()"><u>U</u>pload File</button>
      		     <%
               }
              %>  
               </td>
               <td  height="28" nowrap class="fieldGroupHeader-r">
               <font color="color:#495879;">Show Template :</font><a href="#" onClick="javascript:showTemplate()" >Sample upload file</a>
      		</td>
        </tr>
        <tr>
        <td width="100%" height="28" nowrap class="fieldGroupHead-r" style="padding-left: 70px;"><a href="#" onClick="javascript:viewFiles()" >View census file</a></td><td  height="15" nowrap class="fieldGroupHeader-r">
 	 <b> </b> <font style="color:red;"><i>Note: Please note the upload option here only supports .xls files.</i></font>
 	 </td>
        </tr>
		 </table>
 	 </logic:notEmpty>
 	 
 <div style="width:72%" align="left"> 
<logic:notEmpty name="frmSwIncomeProfile" property="profileBenefitList">
	
  <table align="center"  class="gridWithPricingleft" border="0" cellspacing="1" cellpadding="0">
       <tr ID="listsubheader" class="gridHeader">
			      <td ID="listsubheader" class="gridHeader" width="20%">Total covered lives:</td>
			      <td ID="listsubheader" class="gridHeader"  width="20%">
					<b>&nbsp;
					<label id="totalNoOfLives_id"><bean:write name="frmSwIncomeProfile" property="totalNoOfLives"/> </label>
					</b>
			      </td>
			   
			      <td ID="listsubheader" class="gridHeader" width="30%">Total covered lives eligible for maternity :</td>
			      <td ID="listsubheader" class="gridHeader" width="30%">
				  <b>&nbsp;	<label id="totalMaternityLives_id"><bean:write name="frmSwIncomeProfile" property="TotalMaternityLives"/> </label> </b>

			      </td>	
			     </tr>
			     </table>

	
	
		<logic:iterate id="item" name="frmSwIncomeProfile" indexId="i" property="profileBenefitList">
		 <bean:define id="benf_typeseqid1" name="item" property="benf_typeseqid1" type="java.lang.Long"/>
		  <bean:define id="benfdesc1" name="item" property="benfdesc1" type="java.lang.String"/>
		    <bean:define id = "dentalLivesYN" name = "frmSwIncomeProfile" property="dentalLivesYN"  type="java.lang.String" />
		       <bean:define id = "opticalLivesYN" name = "frmSwIncomeProfile" property="opticalLivesYN"  type="java.lang.String" />
		    
		  
		<%
	   Long benf_typeseqid1comp = benf_typeseqid1;
		if(tempSeq!=benf_typeseqid1comp){
			tempSeq	=	benf_typeseqid1comp; 
			copyobj = 0;
		
		
			if(benfdesc1.equalsIgnoreCase("Optical") ||  benfdesc1.equalsIgnoreCase("Dental")){
			livesViewMode = "readonly";
			textbox = "textBox textBoxVerySmallDisabled";
			} else{
			livesViewMode = "";
			textbox = "textBox textBoxVerySmall";
		   }//end else%>
		   

			
			<%if(benfdesc1.equalsIgnoreCase("Maternity")){  %>
			<table align="center" class="gridWithPricingleft" border="0" cellspacing="1" cellpadding="0"> 
			<tr class="gridEvenRow">
			<td  width="40%">Total covered lives<!-- -Inpatient/Outpatient --></td>
			<td width="30%" class="textLabelBold"> 
				<html:text property="sumTotalLives"  styleId="sumTotalLives" readonly="true" styleClass="textBox textBoxVerySmallDisabled" ></html:text> 
			</td>
			<td width="30%" >
			<b>100%</b>	</td>
			</tr>
			</table>
			<%} %>
			
			<%if(benfdesc1.equalsIgnoreCase("Dental")){  %>
			<table align="center" class="gridWithPricingleft" border="0" cellspacing="1" cellpadding="0">
			 <tr  class="gridEvenRow">
			<td colspan="2" width="40%">Total covered lives-Maternity</td><td></td>
			<td width="30%" class="textLabelBold"> 
				<html:text property="sumTotalLivesMaternity"  styleId="sumTotalLivesMaternity" readonly="true" styleClass="textBox textBoxVerySmallDisabled" ></html:text> 
				</td>
				<td width="30%" >
			<b>100%</b>	</td>
			</tr>
			</table>
			<%} %>
			
			<%-- <%if(benfdesc1.equalsIgnoreCase("Optical")){  %>
			<table align="center" class="gridWithPricingleft" border="0" cellspacing="1" cellpadding="0"> 
			<tr  class="gridEvenRow">
			<td colspan="2" width="40%">Total covered lives-Dental</td><td></td>
			<td width="30%" class="textLabelBold"> 
				<html:text property="sumTotalLivesDental"  styleId="sumTotalLivesDental" readonly="" styleClass="textBox textBoxVerySmallDisabled" ></html:text> 
				
			</td>	
			<td width="30%" >
			<b>100%</b>	</td>
			</tr>
			</table>
			<%} %> --%>
			
			<%String displaytable = ""; 
			if(benfdesc1.equalsIgnoreCase("Dental") || benfdesc1.equalsIgnoreCase("Optical")){
				 displaytable = "none";
			}
			%>
			<table align="center"  style="display:<%=displaytable%>" class="gridWithPricingleft" border="0" cellspacing="1" cellpadding="0">
	<tr><th colspan="5" align="center" ID="listsubheader" class="gridHeader">
	 <%
	if(benfdesc1.equalsIgnoreCase("Maternity")){%>
		Maternity Eligible Lives By Age Band
	<%}else{%> 
	Covered Lives By Age Band<%--   - <bean:write name="item" property="benfdesc1" --%>
	<%} %>
	</th></tr>
	<tr>
		<td width="20%" ID="listsubheader" CLASS="gridHeader">Gender&nbsp;</td>
		<td width="20%" ID="listsubheader" CLASS="gridHeader">Age Range&nbsp;</td>
		<td width="30%" ID="listsubheader" CLASS="gridHeader">Total Covered Lives &nbsp;</td>
		<td width="30%" ID="listsubheader" CLASS="gridHeader">Overall Portfolio Distribution(%) &nbsp;</td>
		</tr>
		<% %>
		
	<%}//}else{
		 if(iRowCount%2==0) { %>
				<tr class="gridOddRow">
			<%
			  } else { %>
  				<tr class="gridEvenRow">
  			<%
			  } %>
			  	<td width="20%" class="formLabel"><bean:write name="item" property="gndrdesc1" /></td>
			  	<td width="20%" class="formLabel"><bean:write name="item" property="age_range1" /></td>
			  	<td width="15%" class="textLabelBold"> 
			  	<% if(reqTotalCoverdLives!=null){%>
			  	<input type="text"  name="totalCoverdLives" id="<%="benf_typeseqid"+benf_typeseqid1+"["+copyobj+"]"%>" value="<%=reqTotalCoverdLives[iRowCount]%>" <%=livesViewMode%>  class="<%=textbox%>"  onchange="isCopy(this);isTotalLives(this,<%=copyobj%>,<%=benf_typeseqid1%>)"  onkeyup="isNumericOnly(this);"> 
				<%}else{%>
				<input type="text"  name="totalCoverdLives" id="<%="benf_typeseqid"+benf_typeseqid1+"["+copyobj+"]"%>" value="<bean:write name="item" property="totalCoverdLives1"/>" class="<%=textbox%>" <%=livesViewMode%> class="textBox textBoxVerySmall" onchange="isCopy(this);isTotalLives(this,<%=copyobj%>,<%=benf_typeseqid1%>)" onkeyup="isNumericOnly(this);">
				<%}%>
				</td>
				<td width="20%" class="formLabel"><bean:write name="item" property="ovrprtflio_dstr1" />
					<INPUT TYPE="hidden" NAME="gndrdesc" value="<bean:write property='gndrdesc1' name='item'/>"> 
					<INPUT TYPE="hidden" NAME="age_range" value="<bean:write property='age_range1' name='item'/>"> 
				<%-- 	<INPUT TYPE="hidden" NAME="totalCoverdLives" value="<bean:write property='totalCoverdLives1' name='item'/>">  --%>
					<INPUT TYPE="hidden" NAME="ovrprtflio_dstr" value="<bean:write property='ovrprtflio_dstr1' name='item'/>"> 
					<INPUT TYPE="hidden" NAME="benf_typeseqid" value="<bean:write property='benf_typeseqid1' name='item'/>"> 
					<INPUT TYPE="hidden" NAME="gndrtypeseqid" value="<bean:write property='gndrtypeseqid1' name='item'/>"> 
					<INPUT TYPE="hidden" NAME="age_rngseqid" value="<bean:write property='age_rngseqid1' name='item'/>"> 
					<INPUT TYPE="hidden" NAME="benf_lives_seq_id" value="<bean:write property='benf_lives_seq_id1' name='item'/>"> 
					<INPUT TYPE="hidden" NAME="benfdesc" value="<bean:write property='benfdesc1' name='item'/>"> 
					
				</td>
			</tr>
			
			

		<%//end of total sum display
			iRowCount++;
			copyobj++;
		//}//else close
		
			%>
	
		</logic:iterate>
		
	<%-- 	<table align="center" class="gridWithPricingleft" border="0" cellspacing="1" cellpadding="0"> 
		<tr  class="gridEvenRow">
			<td colspan="2" width="40%">Total covered lives-Optical</td><td></td>
			<td width="30%" class="textLabelBold"> 
				<html:text property="sumTotalLivesOptical"  styleId="sumTotalLivesOptical" readonly="" styleClass="textBox textBoxVerySmallDisabled" ></html:text> 
				</td>
				<td width="30%" >
			<b>100%</b>	</td>
			</tr>
			</table> --%>
		
	
	</logic:notEmpty>

	
		<logic:notEmpty name="frmSwIncomeProfile" property="profileNationalityList">		
	<table align="center" class="gridWithPricingleft" border="0" cellspacing="1" cellpadding="0">
	    <tr>
		<td width="40%" ID="listsubheader" CLASS="gridHeader">Nationality Category&nbsp;</td>
		<td width="30%" ID="listsubheader" CLASS="gridHeader">Total Covered Lives&nbsp;</td>
		<td width="30%" ID="listsubheader" CLASS="gridHeader">Overall Portfolio Distribution(%) &nbsp;</td>
		</tr>
		
		<logic:iterate id="item" name="frmSwIncomeProfile" indexId="j"  property="profileNationalityList">
		<bean:define id="natl_typeseqid1" name="item" property="natl_typeseqid1" type="java.lang.Long"/>
	     <%
		 if(j%2==0) { %>
				<tr class="gridOddRow">
			<%
			  } else { %>
  				<tr class="gridEvenRow">
  			<%
			  } %> 
			  
			  	<td width="40%" class="formLabel"><bean:write name="item" property="natl_name1" /></td>
			  	<td width="30%" class="textLabelBold">
			  	<% if(reqNatCoverdLives!=null){%>
			  	<input type="text"  name="natCoverdLives"  id="<%="natl_typeseqid["+j+"]"%>" value="<%=reqNatCoverdLives[j]%>" onchange="isTotalNatLives(this,<%=j%>)"  class="textBox textBoxVerySmall"  onkeyup="isNumericOnly(this);"> 
				<%}else{%>
				<input type="text"  name="natCoverdLives"  id="<%="natl_typeseqid["+j+"]"%>" onchange="isTotalNatLives(this,<%=j%>)" value="<bean:write name="item" property="natCoverdLives1"/>" class="textBox textBoxVerySmall"  onkeyup="isNumericOnly(this);"> 
				<%}%> 
			  	<td width="30%" class="formLabel"><bean:write name="item" property="natovrprtflio_dstr1" />
			  	
					<INPUT TYPE="hidden" NAME="natl_name" value="<bean:write property='natl_name1' name='item'/>"> 
					<INPUT TYPE="hidden" NAME="natovrprtflio_dstr" value="<bean:write property='natovrprtflio_dstr1' name='item'/>"> 
					<INPUT TYPE="hidden" NAME="natl_typeseqid" value="<bean:write property='natl_typeseqid1' name='item'/>"> 
					<INPUT TYPE="hidden" NAME="natl_seqid" value="<bean:write property='natl_seqid1' name='item'/>"> 
				</td>
			</tr>
			
		</logic:iterate>
		</table>
		<table align="center" class="gridWithPricingleft" border="0" cellspacing="1" cellpadding="0">
			 <tr  class="gridEvenRow">
			<td colspan="" width="40%">Total covered lives</td><td></td>
			<td width="30%" class="textLabelBold"> 
				<html:text property="sumNationalityLives"  styleId="sumNationalityLives" readonly="true" styleClass="textBox textBoxVerySmallDisabled" ></html:text> 
				</td>
				<td width="30%" >
			<b>100%</b>	</td>
			</tr>
			</table>
		
		<logic:notEmpty name="frmSwIncomeProfile" property="profileBenefitList">
	<table align="right" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
		 <tr> 
		     <td colspan="3" align="right">
      		 <%
             if(TTKCommon.isAuthorized(request,"Edit")) {
             %>
      		   <button type="button" name="Button" accesskey="a" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onSaveIncomePartial('partialsave');">P<u>a</u>rtial Save</button>&nbsp;
      			<button type="button" name="Button" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onSaveIncome('save');"><u>S</u>ave</button>&nbsp;
      			<button type="button" name="Button" accesskey="p" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onSaveIncome('saveProceed');"><u>P</u>roceed >></button>&nbsp;  <!-- onClick="javascript:onViewPlanDesign();" -->
 			<%
            }
          %>
		  	<button type="button" name="Button" accesskey="b" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onCloseIncome();"><u>B</u>ack</button>&nbsp;

		    </td>
	  	</tr>
	</table>
</logic:notEmpty>
	
		
		</logic:notEmpty>
	 

	<!-- E N D : Grid -->
	<!-- S T A R T : Buttons and Page Counter -->
	
</div>
</div>
</div>
	<input type="hidden" name="child" value="">
	<INPUT TYPE="hidden" NAME="rownum" VALUE="">
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	<INPUT TYPE="hidden" NAME="DayCareProduct" VALUE="">
	<INPUT TYPE="hidden" NAME="dentalLivesYN" VALUE="<bean:write name="frmSwIncomeProfile" property="dentalLivesYN"/>">
	<INPUT TYPE="hidden" NAME="opticalLivesYN" VALUE="<bean:write name="frmSwIncomeProfile" property="opticalLivesYN"/>">
	<INPUT TYPE="hidden" NAME="totalNoOfLives" VALUE="<bean:write name="frmSwIncomeProfile" property="totalNoOfLives"/>">
	<INPUT TYPE="hidden" NAME="TotalMaternityLives" VALUE="<bean:write name="frmSwIncomeProfile" property="TotalMaternityLives"/>">
	<INPUT TYPE="hidden" NAME="numberOfLives" VALUE="${numberOfLives}">
	<INPUT TYPE="hidden" NAME="renewalYN" VALUE="${sessionScope.renewalYN}">
	<INPUT TYPE="hidden" NAME="policyNumber" VALUE="${sessionScope.policyNumber}">
	<!-- E N D : Buttons and Page Counter -->
</html:form>
<!-- E N D : Content/Form Area -->