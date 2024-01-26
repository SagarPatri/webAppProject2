
<%@page import="java.util.ArrayList"%>
<%
/**
 * @ (#)  bajaj separation 1274A
 * Reason        :  
 */
 %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@ page import=" com.ttk.common.TTKCommon,com.ttk.common.security.Cache,com.ttk.dao.impl.common.*"%>
<%@ page import="java.sql.PreparedStatement"%>
<%@ page import="com.ttk.dao.ResourceManager"%>
<%@ page import="com.ttk.common.exception.*"%>
<%@ page language="java" import="java.sql.*,java.io.*"%>

<%
	Connection connection = null;
	ResultSet resultSet = null;
	PreparedStatement statement = null;	
	String dataRunDate = "";	
	try {
		connection = ResourceManager.getConnection();
		statement = connection.prepareStatement("SELECT to_char(MAX(NVL(P.UPDATED_DATE,P.ADDED_DATE)),'DD-MM-YYYY')  AS  LAST_DML_DT FROM APP.TPA_PRICING_PREV_POL_DATA P");
		resultSet = (java.sql.ResultSet) statement.executeQuery();
		if (resultSet != null) {
			while (resultSet.next()) {			
						dataRunDate=(resultSet.getString("LAST_DML_DT"));            
			}// while(resultSet.next())
		}//if(resultSet != null)
	} catch (Exception e) {
		out.println(e.getMessage());
	} finally {/* Nested Try Catch to ensure resource closure */
		CommonClosure.closeOpenResources(resultSet,statement,null,connection,null,this,"");
	}
%>



<%@page import="oracle.jdbc.OracleTypes"%>

<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
  <script type="text/javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script>  
<script language="javascript" src="/ttk/scripts/insurancepricing/swpolicydesignconfig.js"></script>
	<%
  boolean viewmode=true;
	 String pastExplable = "";
	 String demopastExplable = "";	String INPCPM = "";	String OUTCPM = "";	String MATCPM = "";	String OPTCPM = "";	String DENTCPM = "";
	
	 String colordemoOpCopay=""; String colorOPDeductable=""; String colorAlahli="";String colorMaternityLt="";String colorOpticalLt="";String colorOpticalCpy="";
	 String colorDentalLt=""; String colorDentalCopay=""; String colorNationality="";
	 String colordurpermonth="";  String colorNoLives=""; String coloraverageAge="";
	 String colorAreaofCover=""; String colorNetwork=""; String colorMBL=""; String colorMaternityCopay="";
  %>
<!-- S T A R T : Content/Form Area -->
<html:form action="/SwPlanDesignConfigurationAction.do" method="post">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0"	cellpadding="0">
		<tr>
			<td><bean:write
				name="frmSwPolicyConfig" property="caption" /></td>
			<td align="right"></td>
			<td align="right"></td>
		</tr>
	</table>

	<!-- E N D : Page Title -->
	<div class="contentArea" style="overflow-x:visible; width:99%;" id="contentArea">
	<html:errors />
	 <!-- S T A R T : Success Box -->
	 <logic:notEmpty name="frmSwPolicyConfig"	property="pricingNumberAlert">
				<table align="center" class="errorContainer" border="0"	cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/warning.gif" title="Warning" alt="Warning"
							width="16" height="16" align="absmiddle">&nbsp; <bean:write
								name="frmSwPolicyConfig" property="pricingNumberAlert" /></td>
					</tr>
				</table>
			</logic:notEmpty>
	<logic:notEmpty name="updated" scope="request">
		<table align="center" class="successContainer" style="display: " border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td><img src="/ttk/images/SuccessIcon.gif" title="Success"alt="Success"
					width="16" height="16" align="absmiddle">&nbsp; 
					<bean:message name="updated" scope="request" /></td>
			</tr>
		</table>
	</logic:notEmpty> <!-- E N D : Success Box --> <!-- S T A R T : Form Fields -->
	
	 	<logic:notEmpty name="frmSwPolicyConfig"	property="alertMsg">
				<table align="center" class="errorContainer" border="0"	cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/warning.gif" title="Warning" alt="Warning"
							width="16" height="16" align="absmiddle">&nbsp; <bean:write
								name="frmSwPolicyConfig" property="alertMsg" /></td>
					</tr>
				</table>
			</logic:notEmpty>
	 <!-- demographic Data -->	
	 <div style="float:left;display:inline-block;" >			
	 <fieldset style="width:100%"><legend>Key Coverages and Demographic Details</legend>
          <br>
            <table align="center" class="gridWithPlanning" border="0" cellspacing="1" cellpadding="0">
			<tr>
			<logic:iterate id="alDemographicData" name="frmSwPolicyConfig"  property="alDemographicData">
				<bean:define id="demodataType" name="alDemographicData" property="demodataType" type="java.lang.String"/>
				<bean:define id="demoPolicyDurationPerMonth" name="alDemographicData" property="demoPolicyDurationPerMonth" type="java.lang.String"/>
				<bean:define id="demoNoOfLives" name="alDemographicData" property="demoNoOfLives" type="java.lang.String"/>
				<bean:define id="demoAverageAge" name="alDemographicData" property="demoAverageAge" type="java.lang.String"/>
				<bean:define id="demoAreaOfCover" name="alDemographicData" property="demoAreaOfCover" type="java.lang.String"/>
				<bean:define id="demoNetwork" name="alDemographicData" property="demoNetwork" type="java.lang.String"/>
				<bean:define id="demoMaximumBenfitLimit" name="alDemographicData" property="demoMaximumBenfitLimit" type="java.lang.String"/>
				<bean:define id="demoOpCopay" name="alDemographicData" property="demoOpCopay" type="java.lang.String"/>
				<bean:define id="demoOPDeductable" name="alDemographicData" property="demoOPDeductable" type="java.lang.String"/>
				<bean:define id="demoAlahli" name="alDemographicData" property="demoAlahli" type="java.lang.String"/>
				<bean:define id="demoMaternityLimit" name="alDemographicData" property="demoMaternityLimit" type="java.lang.String"/>
				<bean:define id="matCopay" name="alDemographicData" property="maternityCopay" type="java.lang.String"/>
				<bean:define id="demoOpticalLimit" name="alDemographicData" property="demoOpticalLimit" type="java.lang.String"/>
				<bean:define id="demoOpticalCopay" name="alDemographicData" property="demoOpticalCopay" type="java.lang.String"/>
				<bean:define id="demoDentalLimit" name="alDemographicData" property="demoDentalLimit" type="java.lang.String"/>
				<bean:define id="demoDentalCopay" name="alDemographicData" property="demoDentalCopay" type="java.lang.String"/>
				<bean:define id="demoNationality" name="alDemographicData" property="demoNationality" type="java.lang.String"/>
			    <%	
			     String demodataTypeCode = demodataType;
			   
			    if(demodataType.equalsIgnoreCase("PROJ_DTA")){
			    	 colordemoOpCopay = demoOpCopay;  colorOPDeductable=demoOPDeductable; colorAlahli=demoAlahli;
			    	  colorMaternityLt=demoMaternityLimit; colorOpticalLt=demoOpticalLimit; colorOpticalCpy=demoOpticalCopay;
			    	  colorDentalLt=demoDentalLimit;  colorDentalCopay=demoDentalCopay;  colorNationality=demoNationality;
			    	  colordurpermonth =demoPolicyDurationPerMonth;colorNoLives=demoNoOfLives;coloraverageAge=demoAverageAge;
			    	  colorAreaofCover = demoAreaOfCover;colorNetwork = demoNetwork;colorMBL = demoMaximumBenfitLimit;
			    	  colorMaternityCopay=matCopay;
			    }
			   
				 if(demodataTypeCode.equalsIgnoreCase("PST_DTA_YR1")){
					 demopastExplable ="Y";
				 }
				%> 
				</logic:iterate>
			<%if(demopastExplable.equalsIgnoreCase("Y")){ %>
			<td width="15%" ID="listsubheader"  CLASS="gridMainHeaderPricing">Policy reference&nbsp;</td>
			<%}else{%>
				<td width="15%" >&nbsp;</td>
			<%}%>
			
				<td width="12%" ID="listsubheader" CLASS="gridsubHeaderPricing">Coverage<br>Start Date&nbsp;</td>
				<td width="12%" ID="listsubheader" CLASS="gridsubHeaderPricing">Coverage<br>End Date&nbsp;</td>
				<td width="12%" ID="listsubheader" CLASS="gridsubHeaderPricing">Duration<br>(<i>mths</i>)&nbsp;</td>
				<td width="10%" ID="listsubheader" CLASS="gridsubHeaderPricing">Lives&nbsp;</td>
				<td width="10%" ID="listsubheader" CLASS="gridHighlightHeaderPricing">Avg. Age&nbsp;</td> 
				<td width="10%" ID="listsubheader" CLASS="gridHighlightHeaderPricing">Area of Cover&nbsp;</td>
				<td width="10%" ID="listsubheader" CLASS="gridHighlightHeaderPricing">Network&nbsp;</td>
				<td width="10%" ID="listsubheader" CLASS="gridHighlightHeaderPricing">MBL&nbsp;</td>
				<td width="10%" ID="listsubheader" CLASS="gridHighlightHeaderPricing">OP Copay/Ded&nbsp;</td>
				<td width="12%" ID="listsubheader" CLASS="gridHighlightHeaderPricing">IP Copay&nbsp;</td>
				<!-- <td width="12%" ID="listsubheader" CLASS="gridHighlightHeaderPricing">Maternity&nbsp;</td> -->
				<td width="10%" ID="listsubheader" CLASS="gridHighlightHeaderPricing">Al Alhi&nbsp;</td>
				<td width="10%" ID="listsubheader" CLASS="gridHighlightHeaderPricing">MAT Limit&nbsp;</td>
				<td width="10%" ID="listsubheader" CLASS="gridHighlightHeaderPricing">MAT Copay&nbsp;</td>
				<!-- <td width="12%" ID="listsubheader" CLASS="gridHighlightHeaderPricing">Optical&nbsp;</td> -->
				<td width="12%" ID="listsubheader" CLASS="gridHighlightHeaderPricing">OPT Limit&nbsp;</td>
				<td width="12%" ID="listsubheader" CLASS="gridHighlightHeaderPricing">OPT Copay&nbsp;</td>
				<!-- <td width="12%" ID="listsubheader" CLASS="gridHighlightHeaderPricing">Dental&nbsp;</td> -->
				<td width="12%" ID="listsubheader" CLASS="gridHighlightHeaderPricing">DENT Limit&nbsp;</td>
				<td width="12%" ID="listsubheader" CLASS="gridHighlightHeaderPricing">DENT Copay&nbsp;</td>
				<td width="12%" ID="listsubheader" CLASS="gridHighlightHeaderPricing">Nationality&nbsp;</td>
				</tr>
				
				<logic:iterate id="alDemographicData" name="frmSwPolicyConfig" indexId="x" property="alDemographicData">
				<bean:define id="demodataType" name="alDemographicData" property="demodataType" type="java.lang.String"/>
			   
			   <%	
			    String textbox = "gridHighlightPricing"; String textboxOPDeduct = "gridHighlightPricing"; String textboxalahli = "gridHighlightPricing";   String textboxmatlimt = "gridHighlightPricing"; String textboxoptlimit = "gridHighlightPricing"; String textboxopticallimit = "gridHighlightPricing";
			    String textboxopticalcopay = "gridHighlightPricing"; String textboxdentallimit = "gridHighlightPricing"; String textboxdentalcopay = "gridHighlightPricing";    String textboxnationality = "gridHighlightPricing";
			    String textboxdurpermonth = "gridHighlightPricing"; String textboxcolorNoLives = "gridHighlightPricing"; String textboxAverageAge = "gridHighlightPricing"; 
			    String textboxAreaofCover = "gridHighlightPricing"; String textboxNetwork = "gridHighlightPricing"; String textboxMBL = "gridHighlightPricing";
			    String textboxMatCopay = "gridHighlightPricing";
			    
			    String demodataTypeCode = demodataType;
				 if(!demodataType.equalsIgnoreCase("USR_IP_YR")){
				
				%> 
				<tr>
				<% if(demodataType.equalsIgnoreCase("PROJ_DTA")){	%>
				
				<td width="15%"  ID="listsubheader" CLASS="gridMainHeaderPricing">Proposed Policy </td>
				<%}else{ %>
				
				<td width="15%" ID="listsubheader" CLASS="gridsubHeaderPricing"> Policy No. -<bean:write property="demoPolicyNo" name="alDemographicData"/> </td>
				<%}%>
				
				<td width="10%" class="gridHighlightPricing" > <bean:write property="demoPolicyEffDate" name="alDemographicData"/> </td>
				<td width="10%" class="gridHighlightPricing"> <bean:write property="demoPolicyExpDate" name="alDemographicData"/> </td>
				
				<bean:define id="demoPolicyDurationPerMonth" name="alDemographicData" property="demoPolicyDurationPerMonth" type="java.lang.String"/>
				<% String subdemoPolicyDurationPerMonth = demoPolicyDurationPerMonth;
				if(!colordurpermonth.equalsIgnoreCase(demoPolicyDurationPerMonth) && !demodataType.equalsIgnoreCase("PROJ_DTA")){textboxdurpermonth = "gridHighlightPricingred";}%>
				<td width="10%" class="<%=textboxdurpermonth%>"> <bean:write property="demoPolicyDurationPerMonth" name="alDemographicData"/> </td>
				
				<bean:define id="demoNoOfLives" name="alDemographicData" property="demoNoOfLives" type="java.lang.String"/>
				<% String subdemoNoOfLives = demoNoOfLives;
				if(!colorNoLives.equalsIgnoreCase(subdemoNoOfLives) && !demodataType.equalsIgnoreCase("PROJ_DTA")){textboxcolorNoLives = "gridHighlightPricingred";}%>
				<td width="10%" class="<%=textboxcolorNoLives%>"> <bean:write property="demoNoOfLives" name="alDemographicData"/> </td>
				
				<bean:define id="demoAverageAge" name="alDemographicData" property="demoAverageAge" type="java.lang.String"/>
				<% String subdemoAverageAge = demoAverageAge;
				if(!coloraverageAge.equalsIgnoreCase(subdemoAverageAge) && !demodataType.equalsIgnoreCase("PROJ_DTA")){textboxAverageAge = "gridHighlightPricingred";}%>
				<td width="10%" class="<%=textboxAverageAge%>"> <bean:write property="demoAverageAge" name="alDemographicData"/> </td>
				
				<bean:define id="demoAreaOfCover" name="alDemographicData" property="demoAreaOfCover" type="java.lang.String"/>
				<%String subdemoAreaOfCover = demoAreaOfCover;	
				if(!colorAreaofCover.equalsIgnoreCase(subdemoAreaOfCover) && !demodataType.equalsIgnoreCase("PROJ_DTA")){textboxAreaofCover = "gridHighlightPricingred";}%>
				<td width="10%" class="<%=textboxAreaofCover%>"> <bean:write property="demoAreaOfCover" name="alDemographicData"/> </td>
				
				
				<bean:define id="demoNetwork" name="alDemographicData" property="demoNetwork" type="java.lang.String"/>
				<%String subdemodemoNetwork = demoNetwork;	
				if(!colorNetwork.equalsIgnoreCase(subdemodemoNetwork) && !demodataType.equalsIgnoreCase("PROJ_DTA")){textboxNetwork = "gridHighlightPricingred";}%>
				<td width="10%" class="<%=textboxNetwork%>"> <bean:write property="demoNetwork" name="alDemographicData"/> </td>
				
				
				<bean:define id="demoMBL" name="alDemographicData" property="demoMaximumBenfitLimit" type="java.lang.String"/>
				<%String subdemoMBL = demoMBL;	
				if(!colorMBL.equalsIgnoreCase(subdemoMBL) && !demodataType.equalsIgnoreCase("PROJ_DTA")){textboxMBL = "gridHighlightPricingred";}%>
				<td width="10%" class="<%=textboxMBL%>"> <bean:write property="demoMaximumBenfitLimit" name="alDemographicData"/> </td>
				
				<bean:define id="demoOpCopay" name="alDemographicData" property="demoOpCopay" type="java.lang.String"/>
				<% String subdemoOpcopy = demoOpCopay;
				if(!colordemoOpCopay.equalsIgnoreCase(subdemoOpcopy) && !demodataType.equalsIgnoreCase("PROJ_DTA")){textbox = "gridHighlightPricingred";}%>
				<td width="10%" class="<%=textbox%>"> <bean:write property="demoOpCopay" name="alDemographicData"/> </td>
				
				<bean:define id="demoOPDeductable" name="alDemographicData" property="demoOPDeductable" type="java.lang.String"/>
				<%String subdemoOPDeductable = demoOPDeductable;	
				if(!colorOPDeductable.equalsIgnoreCase(subdemoOPDeductable) && !demodataType.equalsIgnoreCase("PROJ_DTA")){textboxOPDeduct = "gridHighlightPricingred";}%>
				<td width="10%" class="<%=textboxOPDeduct%>"> <bean:write property="demoOPDeductable" name="alDemographicData"/> </td>
				
				<bean:define id="demoAlahli" name="alDemographicData" property="demoAlahli" type="java.lang.String"/>
				<%String subdemoAlahli = demoAlahli;	
				if(!colorAlahli.trim().equalsIgnoreCase(subdemoAlahli.trim()) && !demodataType.equalsIgnoreCase("PROJ_DTA")){	textboxalahli = "gridHighlightPricingred";}	%>	
	 			<td width="10%" class="<%=textboxalahli%>"> <bean:write property="demoAlahli" name="alDemographicData"/> </td>
				
				<bean:define id="demoMaternityLimit" name="alDemographicData" property="demoMaternityLimit" type="java.lang.String"/>
				<%String subdemoMaternityLimit = demoMaternityLimit;	
				if(!colorMaternityLt.equalsIgnoreCase(subdemoMaternityLimit) && !demodataType.equalsIgnoreCase("PROJ_DTA")){textboxmatlimt = "gridHighlightPricingred";}	%>	
				<td width="10%" class="<%=textboxmatlimt%>"> <bean:write property="demoMaternityLimit" name="alDemographicData"/> </td>
				

				<bean:define id="matCopay" name="alDemographicData" property="maternityCopay" type="java.lang.String"/>
				<%String subMatCopay = matCopay;	
				if(!colorMaternityCopay.equalsIgnoreCase(subMatCopay) && !demodataType.equalsIgnoreCase("PROJ_DTA")){textboxMatCopay = "gridHighlightPricingred";}	%>	
				<td width="10%" class="<%=textboxMatCopay%>"> <bean:write property="maternityCopay" name="alDemographicData"/> </td>
				
				

				<bean:define id="demoOpticalLimit" name="alDemographicData" property="demoOpticalLimit" type="java.lang.String"/>
				<%String subdemoOpticalLimit = demoOpticalLimit;	
				if(!colorOpticalLt.equalsIgnoreCase(subdemoOpticalLimit) && !demodataType.equalsIgnoreCase("PROJ_DTA")){textboxopticallimit = "gridHighlightPricingred";}	%>	
				<td width="10%" class="<%=textboxopticallimit%>"> <bean:write property="demoOpticalLimit" name="alDemographicData"/> </td>
				
				<bean:define id="demoOpticalCopay" name="alDemographicData" property="demoOpticalCopay" type="java.lang.String"/>
				<%String subdemoOpticalCopay = demoOpticalCopay;	
				if(!colorOpticalCpy.equalsIgnoreCase(subdemoOpticalCopay) && !demodataType.equalsIgnoreCase("PROJ_DTA")){textboxopticalcopay = "gridHighlightPricingred";}	%>	
				<td width="10%" class="<%=textboxopticalcopay%>"> <bean:write property="demoOpticalCopay" name="alDemographicData"/> </td>
				
				<bean:define id="demoDentalLimit" name="alDemographicData" property="demoDentalLimit" type="java.lang.String"/>
				<%String subdemoDentalLimit = demoDentalLimit;		
				if(!colorDentalLt.equalsIgnoreCase(subdemoDentalLimit) && !demodataType.equalsIgnoreCase("PROJ_DTA")){textboxdentallimit = "gridHighlightPricingred";}	%>	
				<td width="10%" class="<%=textboxdentallimit%>"> <bean:write property="demoDentalLimit" name="alDemographicData"/> </td>
				
				<bean:define id="demoDentalCopay" name="alDemographicData" property="demoDentalCopay" type="java.lang.String"/>
				<%	String subdemoDentalCopay = demoDentalCopay;	
				if(!colorDentalCopay.equalsIgnoreCase(subdemoDentalCopay) && !demodataType.equalsIgnoreCase("PROJ_DTA")){textboxdentalcopay = "gridHighlightPricingred";}	%>	
				<td width="10%" class="<%=textboxdentalcopay%>"> <bean:write property="demoDentalCopay" name="alDemographicData"/> </td>
				
				<bean:define id="demoNationality" name="alDemographicData" property="demoNationality" type="java.lang.String"/>
				<% String subdemoNationality = demoNationality;	
				if(!colorNationality.equalsIgnoreCase(subdemoNationality) && !demodataType.equalsIgnoreCase("PROJ_DTA")){textboxnationality = "gridHighlightPricingred";}	%>	
				<td width="10%" class="<%=textboxnationality%>"> <bean:write property="demoNationality" name="alDemographicData"/> </td>
				
				<%-- <td width="10%" class="gridHighlightPricing"> <bean:write property="demoMaternityCoverage" name="alDemographicData"/> </td> --%>	
				<%-- <td width="10%" class="gridHighlightPricing"> <bean:write property="demoOpticalCoverage" name="alDemographicData"/> </td> --%>		
				<%-- <td width="10%" class="gridHighlightPricing"> <bean:write property="demoDentalCoverage" name="alDemographicData"/> </td>--%>	
				
<%}else{ 



%>
				<td width="15%"  ID="listsubheader" CLASS="gridMainHeaderPricing">Current Policy(for fresh pricing) &nbsp;</td>
				<td width="10%" class="formLabel"> <html:text indexed="true"  name="alDemographicData"  property="demoPolicyEffDate" styleClass="textBox textBoxSmall"  styleId="<%="democurrentPolicy[1]"%>" onchange="dateValidate(this);" /> </td> 
				<td width="10%" class="formLabel"> <html:text indexed="true"  name="alDemographicData"  property="demoPolicyExpDate" styleClass="textBox textBoxSmall" styleId="<%="democurrentPolicy[2]"%>"  onchange="dateValidate(this);" /> </td> 
				<td width="10%" class="formLabel"> <html:text indexed="true"  name="alDemographicData"  property="demoPolicyDurationPerMonth" styleClass="textBox textBoxSmallest" styleId="<%="democurrentPolicy[3]"%>" onkeyup="isNumeric(this);"/> </td> 
				<td width="10%" class="formLabel"> <html:text indexed="true"  name="alDemographicData"  property="demoNoOfLives" styleClass="textBox textBoxSmallest" styleId="<%="democurrentPolicy[4]"%>" onkeyup="isNumeric(this);"/> </td> 
				<td width="10%" class="formLabel"> <html:text indexed="true"  name="alDemographicData"  property="demoAverageAge" styleClass="textBox textBoxSmallest"  styleId="<%="democurrentPolicy[5]"%>" onkeyup="isNumeric(this);"/> </td> 
				<td width="10%" class="formLabel"> <html:text indexed="true"  name="alDemographicData"  property="demoAreaOfCover" styleClass="textBox textBoxSmallest" styleId="<%="democurrentPolicy[6]"%>" /> </td> 
				<td width="10%" class="formLabel"> <html:text indexed="true"  name="alDemographicData"  property="demoNetwork" styleClass="textBox textBoxSmallest" styleId="<%="democurrentPolicy[7]"%>" /> </td> 
				<td width="10%" class="formLabel"> <html:text indexed="true"  name="alDemographicData"  property="demoMaximumBenfitLimit" styleClass="textBox textBoxSmallest" styleId="<%="democurrentPolicy[8]"%>" onkeyup="isNumeric(this);"/> </td> 
				<td width="10%" class="formLabel"> <html:text indexed="true"  name="alDemographicData"  property="demoOpCopay" styleClass="textBox textBoxSmallest" styleId="<%="democurrentPolicy[9]"%>" /> </td> 
				<td width="10%" class="formLabel"> <html:text indexed="true"  name="alDemographicData"  property="demoOPDeductable" styleClass="textBox textBoxSmallest" styleId="<%="democurrentPolicy[10]"%>" /> </td> 
<%-- 			<td width="10%" class="formLabel"> <html:text indexed="true"  name="alDemographicData"  property="demoMaternityCoverage" styleClass="textBox textBoxSmallest" styleId="<%="democurrentPolicy[11]"%>" /> </td> 
 --%>			<td width="10%" class="formLabel"> <html:text indexed="true"  name="alDemographicData"  property="demoAlahli" styleClass="textBox textBoxSmallest" styleId="<%="democurrentPolicy[11]"%>" />
 				<td width="10%" class="formLabel"> <html:text indexed="true"  name="alDemographicData"  property="demoMaternityLimit" styleClass="textBox textBoxSmallest" styleId="<%="democurrentPolicy[12]"%>" onkeyup="isNumeric(this);"/> </td> 
 				<td width="10%" class="formLabel"> <html:text indexed="true"  name="alDemographicData"  property="maternityCopay" styleClass="textBox textBoxSmallest" styleId="<%="democurrentPolicy[18]"%>" onkeyup="isNumeric(this);"/> </td> 
<%-- 			<td width="10%" class="formLabel"> <html:text indexed="true"  name="alDemographicData"  property="demoOpticalCoverage" styleClass="textBox textBoxSmallest" styleId="<%="democurrentPolicy[13]"%>" /> </td> 
 --%>			<td width="10%" class="formLabel"> <html:text indexed="true"  name="alDemographicData"  property="demoOpticalLimit" styleClass="textBox textBoxSmallest" styleId="<%="democurrentPolicy[13]"%>" onkeyup="isNumeric(this);"/> </td> 
				<td width="10%" class="formLabel"> <html:text indexed="true"  name="alDemographicData"  property="demoOpticalCopay" styleClass="textBox textBoxSmallest" styleId="<%="democurrentPolicy[14]"%>" /> </td> 
<%-- 			<td width="10%" class="formLabel"> <html:text indexed="true"  name="alDemographicData"  property="demoDentalCoverage" styleClass="textBox textBoxSmallest" styleId="<%="democurrentPolicy[16]"%>" /> </td> 
 --%>			<td width="10%" class="formLabel"> <html:text indexed="true"  name="alDemographicData"  property="demoDentalLimit" styleClass="textBox textBoxSmallest" styleId="<%="democurrentPolicy[15]"%>" onkeyup="isNumeric(this);"/> </td> 
				<td width="10%" class="formLabel"> <html:text indexed="true"  name="alDemographicData"  property="demoDentalCopay" styleClass="textBox textBoxSmallest" styleId="<%="democurrentPolicy[16]"%>" /> </td> 
				<td width="10%" class="formLabel"> <html:textarea indexed="true"  name="alDemographicData"  property="demoNationality" styleClass="textBox textAreaPricing" styleId="<%="democurrentPolicy[17]"%>"  /> </td> 

<% }%>
				</tr>
  			    <html:hidden  name="alDemographicData" property="demoNoOfLives"/> 
   			    <html:hidden  name="alDemographicData" property="lngGroupProfileSeqID"/> 
   			    <html:hidden  name="alDemographicData" property="demodataType"/> 
   			    <html:hidden  name="alDemographicData" property="demoPolicyNo"/> 
   			    <html:hidden  name="alDemographicData" property="demoPolicyEffDate"/> 
  			    <html:hidden  name="alDemographicData" property="demoPolicyExpDate"/> 
  			    <html:hidden  name="alDemographicData" property="demoEffectiveYear"/> 
  			    <html:hidden  name="alDemographicData" property="demoSlNo"/> 
  			    <html:hidden  name="alDemographicData" property="demographicSeqId"/> 
  			    
  			   
				</logic:iterate>
			</table>
			 <table align="center" class="buttonsContainer" border="0" cellspacing="0" cellpadding="0"><tr>
	 		 	<td class="formLabel" >P = Pharmacy </td>
  	   			 <td class="formLabel" >C = Consultation </td>
  	    		 <td class="formLabel" >I = Investigation</td>
  	    		 <td class="formLabel" >O = Others</td>
  				</tr>
			</table>
			 <table align="center" class="buttonsContainer" border="0" cellspacing="0" cellpadding="0"><tr>
	 		 	<td class="formLabel" >A&E = American and european </td>
  	   			 <td class="formLabel" >ROW = Rest of the world </td>
  	    		 <td class="formLabel" > NA = Not covered so not applicable</td>
  				</tr>
			</table>
			 <table align="center" class="buttonsContainer" border="0" cellspacing="0" cellpadding="0"><tr>
	 		 	<td class="formLabel" style="color:red;" ><i>Note :- Variations in benefits and key demographics in the past policy years (when compared with the proposed policy benefits) are highlighted in red in the table above. </i></td>
  	   			</tr>
			</table>
			  <table align="center" class="buttonsContainer" border="0" cellspacing="0" cellpadding="0">
				<tr>
				<td width="100%" align="right">
				<button type="button" name="Button" accesskey="s" class="buttonsPricingSmall" onMouseout="this.className='buttonsPricingSmall'" onMouseover="this.className='buttonsPricingSmall buttonsHover'"	onClick="javascript:onSaveDemography();"><u>S</u>ave Data</button>&nbsp;
		    <td></td></tr></table>
			
	 </fieldset> 
	
	
	<!-- demographic data end -->
	
	<%
	
	int iRowCount = 0;
	int totaliterate = 0;
%>
<!-- GROUP LEVEL FEATURES -->

            <fieldSet style="width:100%" ><legend>Risk Premium - Working</legend>
          <br>
            <table align="center" class="gridWithPlanning" border="0" cellspacing="1" cellpadding="0">
			
			<tr>
			<td ></td>
			
			<td width="20%"  colspan="2" ID="listsubheader" CLASS="gridMainHeaderPricing" align="center"> Coverage Details&nbsp;</td>
			<td ID="listsubheader"  colspan="7"  align="center" CLASS="gridMainHeaderPricing">Actual/Projected claims cost per covered member (<i>adjusted for trend</i>)</td>
			<td ID="listsubheader"  colspan="5"  align="center" CLASS="gridMainHeaderPricing">Credibility (%) </td>
</tr>
			<tr>
			
				<logic:iterate id="alResultpastYear" name="frmSwPolicyConfig"  property="alResultpastYear">
				<bean:define id="dataTypelable" name="alResultpastYear" property="dataType" type="java.lang.String"/>
				<bean:define id="proposedinPatientCPM" name="alResultpastYear" property="inPatientCPM" type="java.lang.String"/>
				<bean:define id="proposedoutPatientCPM" name="alResultpastYear" property="outPatientCPM" type="java.lang.String"/>
				<bean:define id="proposedmaternityCPM" name="alResultpastYear" property="maternityCPM" type="java.lang.String"/>
				<bean:define id="proposedopticalCPM" name="alResultpastYear" property="opticalCPM" type="java.lang.String"/>
				<bean:define id="proposedindentalCPM" name="alResultpastYear" property="dentalCPM" type="java.lang.String"/>
			    <%	
			     String dataTypeCodelable = dataTypelable;
			   
				 if(dataTypeCodelable.equalsIgnoreCase("PST_DTA_YR1")){
					 pastExplable ="Y";
				 }
				 if(dataTypeCodelable.equalsIgnoreCase("PROJ_DTA")){
					 INPCPM = proposedinPatientCPM;
					 OUTCPM = proposedoutPatientCPM;
					 MATCPM = proposedmaternityCPM;
					 OPTCPM = proposedopticalCPM;
					 DENTCPM = proposedindentalCPM;
				 }
				%> 
				</logic:iterate>
			<%if(pastExplable.equalsIgnoreCase("Y")){ %>
			<td width="12%" ID="listsubheader"  CLASS="gridMainHeaderPricing">Policy reference&nbsp;</td>
			<%}else{%>
				<td width="12%" >&nbsp;</td>
			<%}%>
			<td width="10%" ID="listsubheader" CLASS="gridsubHeaderPricing">Coverage<br>Start Date&nbsp;</td>
			<td width="10%" ID="listsubheader" CLASS="gridsubHeaderPricing">Coverage<br>End Date&nbsp;</td>
			<!-- 	<td width="10%" ID="listsubheader" CLASS="gridsubHeaderPricing">Policy Duration<br>(<i>in months</i>)&nbsp;</td>
				<td width="10%" ID="listsubheader" CLASS="gridsubHeaderPricing">Number of Lives&nbsp;</td> -->
				<td width="10%" ID="listsubheader" CLASS="gridsubHeaderPricing">IP &nbsp;</td>
				<td width="10%" ID="listsubheader" CLASS="gridsubHeaderPricing">OP &nbsp;</td>
				<td width="10%" ID="listsubheader" CLASS="gridsubHeaderPricing">MAT &nbsp;</td>
				<td width="10%" ID="listsubheader" CLASS="gridsubHeaderPricing">OPT &nbsp;</td>
				<td width="10%" ID="listsubheader" CLASS="gridsubHeaderPricing">DENT &nbsp;</td>
				<td width="12%" ID="listsubheader" CLASS="gridHighlightHeaderPricing">All Excl Maternity&nbsp;</td>
				<td width="12%" ID="listsubheader" CLASS="gridHighlightHeaderPricing">Maternity &nbsp;</td>
				<td width="12%" ID="listsubheader" CLASS="gridsubHeaderPricing">IP &nbsp;</td>
				<td width="12%" ID="listsubheader" CLASS="gridsubHeaderPricing">OP &nbsp;</td>
				<td width="12%" ID="listsubheader" CLASS="gridsubHeaderPricing">MAT &nbsp;</td>
				<td width="12%" ID="listsubheader" CLASS="gridsubHeaderPricing">OPT &nbsp;</td>
				<td width="12%" ID="listsubheader" CLASS="gridsubHeaderPricing">DENT &nbsp;</td>
				</tr>
				<!-- <tr class="gridEvenRow">
				<td ID="listsubheader" rowspan="10"  CLASS="gridHeader">Cost per covered member</td>
				</tr> -->
				
				
				
				<logic:iterate id="alResultpastYear" name="frmSwPolicyConfig" indexId="x" property="alResultpastYear">
				<bean:define id="dataType" name="alResultpastYear" property="dataType" type="java.lang.String"/>
			
			  
			<%	
			
			     String dataTypeCode = dataType;
				 if(!dataTypeCode.equalsIgnoreCase("USR_IP_YR")){
					
				
				%> 
			
					<%-- <% if(x%2==0) { %>
					<tr class="gridOddRow">
					<%} else { %>
		  				<tr class="gridEvenRow">
		  			<%} %>  --%>
			<tr class="gridEvenRow">
				<% if(dataTypeCode.equalsIgnoreCase("PROJ_DTA")){	%>
				
				<td width="12%"  ID="listsubheader" CLASS="gridMainHeaderPricing">Proposed Policy Tool Output/Book Rate</td>
				
				<%}else if(dataTypeCode.equalsIgnoreCase("FNL_PROJ_DTA")){ %>	
					 <tr><td colspan="15"></td></tr> 
					 	<tr><td colspan="15" align="right">	
					 	 <%
           				  if(TTKCommon.isAuthorized(request,"Edit")) {
            			 %>
					 	<button type="button" name="Button" accesskey="c" class="buttonsPricingSmall" onMouseout="this.className='buttonsPricingSmall'" onMouseover="this.className='buttonsPricingSmall buttonsHover'"	onClick="javascript:onSaveCalculate();"><u>C</u>alculate</button>&nbsp;
					 	 <%
				           }
				          %>
					 	</td></tr>
					 <tr><td colspan="13"></td></tr> 
					
				<td width="15%" ID="listsubheader"  align="center" colspan="3" CLASS="gridMainHeaderPricing">Final blended risk premium per covered member &nbsp;</td>
	 		
	 			<%}else{ %>
				
				<td width="12%" ID="listsubheader" CLASS="gridsubHeaderPricing"> Policy No. -<bean:write property="policyNo" name="alResultpastYear"/> </td>
				<%}%>
			 				
			  <%if(dataTypeCode.equalsIgnoreCase("FNL_PROJ_DTA")){ %>	
			   <td width="10%" class="gridPricingOutputdata" > <bean:write property="inPatientCPM" name="alResultpastYear"/> </td>
				<td width="10%" class="gridPricingOutputdata"> <bean:write property="outPatientCPM" name="alResultpastYear"/> </td>
				<td width="10%" class="gridPricingOutputdata"> <bean:write property="maternityCPM" name="alResultpastYear"/> </td>
				<td width="10%" class="gridPricingOutputdata"> <bean:write property="opticalCPM" name="alResultpastYear"/> </td>
				<td width="10%" class="gridPricingOutputdata"> <bean:write property="dentalCPM" name="alResultpastYear"/> </td>
				<td width="10%" class="gridHighlightPricing"> <b><bean:write property="allExlMaternity" name="alResultpastYear"/> </b></td>
				<td width="10%" class="gridHighlightPricing"><b> <bean:write property="maternityCPM" name="alResultpastYear"/></b> </td>
				<td></td>
				<%-- <td width="10%" class="gridsideHeaderPricing"> <bean:write property="finalweightage" name="alResultpastYear"/> </td>	 	 --%>		
	 			<% }
			  else
			  {%>
				<td width="10%" class="formLabel"> <bean:write property="strpolicyEffDate" name="alResultpastYear"/> </td>
				<td width="10%" class="formLabel"> <bean:write property="strpolicyExpDate" name="alResultpastYear"/> </td>
			<%-- 	<td width="10%" class="formLabel"> <bean:write property="policyDurationPerMonth" name="alResultpastYear"/> </td>
				<td width="10%" class="formLabel"> <bean:write property="noOfLives" name="alResultpastYear"/> </td> --%>
				<td width="10%" class="formLabel"> <bean:write property="inPatientCPM" name="alResultpastYear"/> </td>
				<td width="10%" class="formLabel"> <bean:write property="outPatientCPM" name="alResultpastYear"/> </td>
				<td width="10%" class="formLabel"> <bean:write property="maternityCPM" name="alResultpastYear"/> </td>
				<td width="10%" class="formLabel"> <bean:write property="opticalCPM" name="alResultpastYear"/> </td>
				<td width="10%" class="formLabel"> <bean:write property="dentalCPM" name="alResultpastYear"/> </td>
				<td width="10%" class="gridHighlightPricing"> <bean:write property="allExlMaternity" name="alResultpastYear"/> </td>
				<td width="10%" class="gridHighlightPricing"> <bean:write property="maternityCPM" name="alResultpastYear"/> </td>
<%-- 				<td width="10%" class="gridsideHeaderPricing"> <html:text indexed="true"  name="alResultpastYear"  property="finalweightage" styleClass="textBox textBoxSmall"  styleId="<%="finalweightage["+x+"]"%>" onkeyup="isNumeric(this);"/> </td> 
 --%>				
			<%-- 	<td width="10%" class="gridsideHeaderPricing"> <html:text indexed="true"  name="alResultpastYear"  property="inpatientcrediblty" styleClass="textBox textBoxSmall"  styleId="<%="inpatientcrediblty["+x+"]"%>" onkeyup="isNumeric(this);"/> </td> 
				<td width="10%" class="gridsideHeaderPricing"> <html:text indexed="true"  name="alResultpastYear"  property="outpatientcrediblty" styleClass="textBox textBoxSmall"  styleId="<%="outpatientcrediblty["+x+"]"%>" onkeyup="isNumeric(this);"/> </td> 
				<td width="10%" class="gridsideHeaderPricing"> <html:text indexed="true"  name="alResultpastYear"  property="maternitycrediblty" styleClass="textBox textBoxSmall"  styleId="<%="maternitycrediblty["+x+"]"%>" onkeyup="isNumeric(this);"/> </td> 
				<td width="10%" class="gridsideHeaderPricing"> <html:text indexed="true"  name="alResultpastYear"  property="opticalcrediblty" styleClass="textBox textBoxSmall"  styleId="<%="opticalcrediblty["+x+"]"%>" onkeyup="isNumeric(this);"/> </td> 
				<td width="10%" class="gridsideHeaderPricing"> <html:text indexed="true"  name="alResultpastYear"  property="dentalcrediblty" styleClass="textBox textBoxSmall"  styleId="<%="dentalcrediblty["+x+"]"%>" onkeyup="isNumeric(this);"/> </td> 
				 --%>
				 
				<td width="10%" class="gridsideHeaderPricing">
				 <logic:notEqual value="0" name="alResultpastYear" property="inPatientCPM">
					<% 				
					if(Integer.parseInt(INPCPM) != 0 && INPCPM != ""){%>
						<html:text indexed="true"  name="alResultpastYear"  property="inpatientcrediblty" styleClass="textBox textBoxSmallest" readonly="" styleId="<%="inpatientcrediblty["+x+"]"%>" onkeyup="isNumeric(this);"/>
					<%}else{%>
						<html:text indexed="true"  name="alResultpastYear"  property="inpatientcrediblty" styleClass="textBox textBoxSmallestDisabled"  readonly="true" styleId="<%="inpatientcrediblty["+x+"]"%>" onkeyup="isNumeric(this);"/>
					<%}%>
				 </logic:notEqual>  
				 <logic:equal value="0" name="alResultpastYear" property="inPatientCPM">
						<html:text indexed="true"  name="alResultpastYear"  property="inpatientcrediblty" styleClass="textBox textBoxSmallestDisabled"  readonly="true" styleId="<%="inpatientcrediblty["+x+"]"%>" onkeyup="isNumeric(this);"/>
			     </logic:equal>
				</td> 
				
				<td width="10%" class="gridsideHeaderPricing">
				<logic:notEqual value="0" name="alResultpastYear" property="outPatientCPM">
					<% 				
					if(Integer.parseInt(OUTCPM) != 0 && OUTCPM != ""){%>
						<html:text indexed="true"  name="alResultpastYear"  property="outpatientcrediblty" styleClass="textBox textBoxSmallest" readonly="" styleId="<%="outpatientcrediblty["+x+"]"%>" onkeyup="isNumeric(this);"/>
					<%}else{%>
						<html:text indexed="true"  name="alResultpastYear"  property="outpatientcrediblty" styleClass="textBox textBoxSmallestDisabled"  readonly="true" styleId="<%="outpatientcrediblty["+x+"]"%>" onkeyup="isNumeric(this);"/>
					<%}%>
			   </logic:notEqual>
				<logic:equal value="0" name="alResultpastYear" property="outPatientCPM">
					 <html:text indexed="true"  name="alResultpastYear"  property="outpatientcrediblty" styleClass="textBox textBoxSmallestDisabled" readonly="true" styleId="<%="outpatientcrediblty["+x+"]"%>" onkeyup="isNumeric(this);"/> 
				</logic:equal>
				</td>
				
				<td width="10%" class="gridsideHeaderPricing">
				<logic:notEqual value="0" name="alResultpastYear" property="maternityCPM">
					<% 				
					if(Integer.parseInt(MATCPM) != 0 && MATCPM != ""){%>
						<html:text indexed="true"  name="alResultpastYear"  property="maternitycrediblty" styleClass="textBox textBoxSmallest" readonly="" styleId="<%="maternitycrediblty["+x+"]"%>" onkeyup="isNumeric(this);"/>
					<%}else{%>
						<html:text indexed="true"  name="alResultpastYear"  property="maternitycrediblty" styleClass="textBox textBoxSmallestDisabled"  readonly="true" styleId="<%="maternitycrediblty["+x+"]"%>" onkeyup="isNumeric(this);"/>
					<%}%>
				</logic:notEqual>
				<logic:equal value="0" name="alResultpastYear" property="maternityCPM">
				 <html:text indexed="true"  name="alResultpastYear"  property="maternitycrediblty" styleClass="textBox textBoxSmallestDisabled" readonly="true" styleId="<%="maternitycrediblty["+x+"]"%>" onkeyup="isNumeric(this);"/> 
				</logic:equal>
				</td>
				
				<td width="10%" class="gridsideHeaderPricing">
				<logic:notEqual value="0" name="alResultpastYear" property="opticalCPM">
					<% 				
					if(Integer.parseInt(OPTCPM) != 0 && OPTCPM != ""){%>
						<html:text indexed="true"  name="alResultpastYear"  property="opticalcrediblty" styleClass="textBox textBoxSmallest" readonly="" styleId="<%="opticalcrediblty["+x+"]"%>" onkeyup="isNumeric(this);"/>
					<%}else{%>
						<html:text indexed="true"  name="alResultpastYear"  property="opticalcrediblty" styleClass="textBox textBoxSmallestDisabled"  readonly="true" styleId="<%="opticalcrediblty["+x+"]"%>" onkeyup="isNumeric(this);"/>
					<%}%>
				
				</logic:notEqual>
				<logic:equal value="0" name="alResultpastYear" property="opticalCPM">
				<html:text indexed="true"  name="alResultpastYear"  property="opticalcrediblty" styleClass="textBox textBoxSmallestDisabled" readonly="true" styleId="<%="opticalcrediblty["+x+"]"%>" onkeyup="isNumeric(this);"/>  
				</logic:equal>
				</td>
			
				<td width="10%" class="gridsideHeaderPricing">
				<logic:notEqual value="0" name="alResultpastYear" property="dentalCPM">
				<% 				
					if(Integer.parseInt(DENTCPM) != 0 && DENTCPM != ""){%>
						<html:text indexed="true"  name="alResultpastYear"  property="dentalcrediblty" styleClass="textBox textBoxSmallest" readonly="" styleId="<%="dentalcrediblty["+x+"]"%>" onkeyup="isNumeric(this);"/>
					<%}else{%>
						<html:text indexed="true"  name="alResultpastYear"  property="dentalcrediblty" styleClass="textBox textBoxSmallestDisabled"  readonly="true" styleId="<%="dentalcrediblty["+x+"]"%>" onkeyup="isNumeric(this);"/>
					<%}%>
				
				</logic:notEqual>
				<logic:equal value="0" name="alResultpastYear" property="dentalCPM">
				 <html:text indexed="true"  name="alResultpastYear"  property="dentalcrediblty" styleClass="textBox textBoxSmallestDisabled" readonly="true" styleId="<%="dentalcrediblty["+x+"]"%>" onkeyup="isNumeric(this);"/> 
				</logic:equal>
				</td>
				 
				 
				
				 <%} %>
				
				
<%-- 		<td><input type="text"  name="alResultpastYear"   value="<bean:write name="alResultpastYear" property="finalweightage"/>" class="textBox textBoxVerySmall" maxlength="3" onkeyup="isNumeric(this);"></td> 
		 --%>		
				
				</tr>
				<%}else{ %>
				
				<tr class="gridEvenRow">
				
				<td width="12%"  ID="listsubheader" CLASS="gridMainHeaderPricing">Current Policy(for fresh pricing) &nbsp;</td>
		<!-- 		<td width="10%"  class="formLabel">- &nbsp;</td>
				<td width="10%"  class="formLabel">- &nbsp;</td> -->
				<td width="10%" class="formLabel"> <html:text indexed="true"  name="alResultpastYear"  property="strpolicyEffDate" styleClass="textBox textBoxSmall"  onchange="dateValidate(this);" /> </td> 
				<td width="10%" class="formLabel"> <html:text indexed="true"  name="alResultpastYear"  property="strpolicyExpDate" styleClass="textBox textBoxSmall"   onchange="dateValidate(this);" /> </td> 
				
<%-- 				<td width="10%" class="formLabel"> <html:text indexed="true"  name="alResultpastYear"  property="policyDurationPerMonth" styleClass="textBox textBoxSmall"  onkeyup="isNumericOnly(this);"/> </td> 
	        	<td width="10%" class="formLabel"> <html:text indexed="true"  name="alResultpastYear"  property="noOfLives" onchange="gettotallivesType(this)"  styleClass="textBox textBoxSmall"  onkeyup="isNumericOnly(this);"/> </td>  --%>
				<td width="10%" class="formLabel"> <html:text indexed="true"  name="alResultpastYear"  property="inPatientCPM" styleClass="textBox textBoxSmall mandatory" styleId="<%="inPatientCPMvalue"%>"  onkeyup="isNumeric(this);"/> </td> 
				<td width="10%" class="formLabel"> <html:text indexed="true"  name="alResultpastYear"  property="outPatientCPM" styleClass="textBox textBoxSmall" styleId="<%="outPatientCPMvalue"%>" onkeyup="isNumeric(this);"/> </td> 
				<td width="10%" class="formLabel"> <html:text indexed="true"  name="alResultpastYear"  property="maternityCPM" styleClass="textBox textBoxSmall" styleId="<%="maternityCPMvalue"%>" onkeyup="isNumeric(this);"/> </td> 
				<td width="10%" class="formLabel"> <html:text indexed="true"  name="alResultpastYear"  property="opticalCPM" styleClass="textBox textBoxSmall" styleId="<%="opticalCPMvalue"%>" onkeyup="isNumeric(this);"/> </td> 
				<td width="10%" class="formLabel"> <html:text indexed="true"  name="alResultpastYear"  property="dentalCPM" styleClass="textBox textBoxSmall" styleId="<%="dentalCPMvalue"%>" onkeyup="isNumeric(this);"/> </td> 
				<td width="10%" class="gridHighlightPricing" > <html:text indexed="true" readonly="true" name="alResultpastYear"  property="allExlMaternity" styleClass="textBox textBoxSmallDisabled"  onkeyup="isNumeric(this);"/> </td> 
				<td width="10%" class="gridHighlightPricing"  > <html:text indexed="true" readonly="true"  name="alResultpastYear"  property="maternityCPM" styleClass="textBox textBoxSmallDisabled"  onkeyup="isNumeric(this);"/> </td> 
<%-- 				<td width="10%" class="gridsideHeaderPricing"> <html:text indexed="true"  name="alResultpastYear" styleId="<%="finalweightage["+x+"]"%>" property="finalweightage" styleClass="textBox textBoxSmall"  onkeyup="isNumeric(this);"/> </td> 
 --%>			
 
 <% 				
					if(Integer.parseInt(INPCPM) != 0 && INPCPM != ""){%>
					<td width="10%" class="gridsideHeaderPricing"> <html:text indexed="true"  name="alResultpastYear"  property="inpatientcrediblty" styleClass="textBox textBoxSmallest"  styleId="<%="inpatientcrediblty["+x+"]"%>"  onkeyup="isNumeric(this);onbenefitvalueCheck(this,'inpatient');"/> </td> 
					<%}else{%>
					<td width="10%" class="gridsideHeaderPricing"> <html:text indexed="true"  name="alResultpastYear"  property="inpatientcrediblty" styleClass="textBox textBoxSmallestDisabled" readonly="true" styleId="<%="inpatientcrediblty["+x+"]"%>"  onkeyup="isNumeric(this);onbenefitvalueCheck(this,'inpatient');"/> </td> 
					<%}%>	
					
				 <% 	if(Integer.parseInt(OUTCPM) != 0 && OUTCPM != ""){%>
				<td width="10%" class="gridsideHeaderPricing"> <html:text indexed="true"  name="alResultpastYear"  property="outpatientcrediblty" styleClass="textBox textBoxSmallest"  styleId="<%="outpatientcrediblty["+x+"]"%>" onkeyup="isNumeric(this);onbenefitvalueCheck(this,'outpatient');"/> </td> 
					<%}else{%>
				<td width="10%" class="gridsideHeaderPricing"> <html:text indexed="true"  name="alResultpastYear"  property="outpatientcrediblty" styleClass="textBox textBoxSmallestDisabled" readonly="true"  styleId="<%="outpatientcrediblty["+x+"]"%>" onkeyup="isNumeric(this);onbenefitvalueCheck(this,'outpatient');"/> </td> 
					<%}%>
					
					
				 <% 	if(Integer.parseInt(MATCPM) != 0 && MATCPM != ""){%>
				<td width="10%" class="gridsideHeaderPricing"> <html:text indexed="true"  name="alResultpastYear"  property="maternitycrediblty" styleClass="textBox textBoxSmallest"  styleId="<%="maternitycrediblty["+x+"]"%>" onkeyup="isNumeric(this);onbenefitvalueCheck(this,'maternity');"/> </td> 
					<%}else{%>
				<td width="10%" class="gridsideHeaderPricing"> <html:text indexed="true"  name="alResultpastYear"  property="maternitycrediblty" styleClass="textBox textBoxSmallestDisabled" readonly="true"   styleId="<%="maternitycrediblty["+x+"]"%>" onkeyup="isNumeric(this);onbenefitvalueCheck(this,'maternity');"/> </td> 
					<%}%>
					
					
				 <% 	if(Integer.parseInt(OPTCPM) != 0 && OPTCPM != ""){%>
				<td width="10%" class="gridsideHeaderPricing"> <html:text indexed="true"  name="alResultpastYear"  property="opticalcrediblty" styleClass="textBox textBoxSmallest"  styleId="<%="opticalcrediblty["+x+"]"%>" onkeyup="isNumeric(this);onbenefitvalueCheck(this,'optical');"/> </td> 
					<%}else{%>
				<td width="10%" class="gridsideHeaderPricing"> <html:text indexed="true"  name="alResultpastYear"  property="opticalcrediblty" styleClass="textBox textBoxSmallestDisabled" readonly="true"   styleId="<%="opticalcrediblty["+x+"]"%>" onkeyup="isNumeric(this);onbenefitvalueCheck(this,'optical');"/> </td> 
					<%}%>
					
					
				 <% if(Integer.parseInt(DENTCPM) != 0 && DENTCPM != ""){%>
				<td width="10%" class="gridsideHeaderPricing"> <html:text indexed="true"  name="alResultpastYear"  property="dentalcrediblty" styleClass="textBox textBoxSmallest"  styleId="<%="dentalcrediblty["+x+"]"%>" onkeyup="isNumeric(this);onbenefitvalueCheck(this,'dental');"/> </td> 
					<%}else{%>
				<td width="10%" class="gridsideHeaderPricing"> <html:text indexed="true"  name="alResultpastYear"  property="dentalcrediblty" styleClass="textBox textBoxSmallestDisabled" readonly="true"   styleId="<%="dentalcrediblty["+x+"]"%>" onkeyup="isNumeric(this);onbenefitvalueCheck(this,'dental');"/> </td> 
					<%}%>
				
				
				</tr>
				<%
				}%>	
				
  			    <html:hidden  name="alResultpastYear" property="noOfLives"/> 
   			    <html:hidden  name="alResultpastYear" property="lngGroupProfileSeqID"/> 
   			    <html:hidden  name="alResultpastYear" property="cpmSeqID"/> 
   			    <html:hidden  name="alResultpastYear" property="dataType"/> 
   			    <html:hidden  name="alResultpastYear" property="policyNo"/> 
   			    <html:hidden  name="alResultpastYear" property="policyEffDate"/> 
  			    <html:hidden  name="alResultpastYear" property="policyExpDate"/> 
  			    <html:hidden  name="alResultpastYear" property="effectiveYear"/> 
  			    <html:hidden  name="alResultpastYear" property="slNo"/> 
  			    <%totaliterate = x; %>
				</logic:iterate>
				
				
			</table>

	<!-- S T A R T : Buttons -->
	<table align="center" class="buttonsContainer" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%" align="center"><font color="red"><b>Data run date for all ongoing policies : <%-- <%=dataRunDate%> --%><bean:write name="frmSwPolicyConfig" property="addedDateForRiskPremium" /></b></font>
  </td>
		</tr>
	</table>

     <table><tr><td><font size="1" color="#000000"><b>&nbsp;&nbsp;Notes :-</b><br><br>&nbsp;&nbsp;&nbsp;1. Current policy is a provison for fresh pricing, is an input for the user, and should be adjusted for trend and IBNR. These can be left blank with zero weightage if not available.</font>
<br><font size="1" color="#EE0707">&nbsp;&nbsp;&nbsp;2. Projected cost per member for ongoing policies with completed months less than 8 months or with less number of lives is subject to high volatility.</font>
<br><font size="1" color="#EE0707">&nbsp;&nbsp;&nbsp;3. Projected cost per member for ongoing policies with duration not equal to 12 months is not annualized.</font>
<br>&nbsp;&nbsp;&nbsp;<font size="1" color="#000000">4. Maternity cost is applicable only to maternity eligible covered members.</font>
<br>&nbsp;&nbsp;&nbsp;<font size="1" color="#000000">5. The information generated by the pricing tool is an estimate based on certain pre-determined presumptions on historic data. The results should be interpreted by an experienced <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;medical insurance underwriter and/or an actuary with the requisite skills and should always be reviewed for reasonableness.
										  </font>
										  </td></tr></table>
     
      </fieldSet> 

 
 <%--  <fieldSet><legend>Loadings & Management Discount</legend>
  
  <table width ="95%"><tr><td>
  <table align="left" class="gridWithPricing" border="0" cellspacing="1" cellpadding="1">
	<tr>
		<td width="20%" ID="listsubheader" align="center" colspan="2" CLASS="gridHeaderPricing">Loadings & Management Discount</td>
	</tr>
		<logic:iterate id="alLoadingData" name="frmSwPolicyConfig" indexId="q" property="alLoadingData">
		<bean:define id="managedataType" name="alLoadingData" property="load_DeductType" type="java.lang.String"/>
		
		<% if(managedataType.equalsIgnoreCase("Management discount")){
			textbox = "textBoxpricing textBoxVerySmall";
		   }else{
			   textbox = "textBox textBoxVerySmall";
		   }
		%>
		
		<% if(q%2==0) { %>
			<tr class="gridOddRow">
			<%} else { %>
  				<tr class="gridEvenRow">
  			<%} %> 
			
			  	<td width="60%" class="formLabel"><bean:write name="alLoadingData" property="load_DeductType" /></td>
				<td  class="formLabel" >
				<html:text indexed="true"  name="alLoadingData"  property="load_DeductTypePercentage" styleId="load_DeductTypePercentage"    styleClass="<%=textbox%>" onkeyup="isNumeric(this);"/><b>%</b> 																										 
				  <html:hidden  name="alLoadingData" property="load_DeductTypeSeqId"/> 
				  <html:hidden  name="alLoadingData" property="grp_load_SeqId"/> 
			   </td>
			</tr>
			</logic:iterate>
  </table>
  </td>
  </tr>
  <tr>
  <td>
   <table align="left" class="gridWithPricing" border="0" cellspacing="1" cellpadding="0">
	<tr>
	<td class="formLabel" nowrap colspan="4">Comments:<span class="mandatorySymbol">*</span> <br>
			    	<html:textarea  property="loadComments" styleId="loadComments" styleClass="textBox textAreaLong" />
			    	</td>
			    	<td></td>
	
	</tr>
	</table>
	
	</td></tr>
	
	<tr><td>
	<table align="center" class="buttonsContainer" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%" align="center">
		<logic:match name="frmSwPolicyConfig" property="calCPM_FlagYN" value="Y">
	
  			<%
             if(TTKCommon.isAuthorized(request,"Edit")) {
             %>
 		<button type="button" name="Button" accesskey="a" class="buttonsPricingLarge" onMouseout="this.className='buttonsPricingLarge'" onMouseover="this.className='buttonsPricingLarge buttonsHover'"	onClick="javascript:onSaveLoading();">Calculate Gross Premium</button>&nbsp;
			<%
            }
          %>
		</logic:match>
			
<!--  &nbsp;<button type="button" name="Button" accesskey="a" class="buttons" onMouseout="this.className='buttons'"	onMouseover="this.className='buttons buttonsHover'"	onClick="javascript:onViewInputSummary();">View <u>I</u>nput Summary</button>&nbsp;
 -->		
		</td>
		
			
		</tr>
	</table>
	
	</td></tr>
	</table>
	</fieldSet>
	
	<br>
	 <fieldSet><legend>Gross  Premium (<i>final premium per covered member per policy year</i>)</legend>
     <table width ="95%"><tr><td>
     <table align="left" class="gridWithPricing" border="0" cellspacing="1" cellpadding="0">
  <tr>
		<td width="60%" ID="listsubheader"  colspan="" CLASS="gridHeader">Gross Premium &nbsp;</td>
		<td width="40%" ID="listsubheader"  colspan="" CLASS="gridHeader">Currency (QAR) &nbsp;</td>
		
  </tr>
  
   <tr class="gridOddRow">
  	 <td class="formLabel" width="60%">Inpatient  </td>
       <td class="formLabel" >
		<html:text name="frmSwPolicyConfigQuote" property="fininPatientCPM" readonly="true" styleClass="textBox textBoxSmall" />
		<b><bean:write property="fininPatientCPM" name="frmSwPolicyConfig"/></b>
		</td>
	
  </tr>
   	<tr class="gridEvenRow">
  	 <td class="formLabel" width="60%">Outpatient  </td>
       <td class="formLabel">
		<html:text name="frmSwPolicyConfigQuote" property="finoutPatientCPM" readonly="true" styleClass="textBox textBoxSmall" />
		<b><bean:write property="finoutPatientCPM" name="frmSwPolicyConfig"/></b>
		</td>
	
  </tr>
  
  	<tr class="gridOddRow">
  	 <td class="formLabel" width="60%">Maternity  </td>
       <td class="formLabel">
		<b><bean:write property="finmaternityCPM" name="frmSwPolicyConfig"/></b>
		<html:text name="frmSwPolicyConfigQuote" property="finmaternityCPM" readonly="true" styleClass="textBox textBoxSmall" />
		</td>
	
  </tr>
  
  <tr class="gridEvenRow">
  	 <td class="formLabel" width="60%">Optical  </td>
       <td class="formLabel">
		<html:text name="frmSwPolicyConfigQuote" property="finopticalCPM" readonly="true" styleClass="textBox textBoxSmall" />
		<b><bean:write property="finopticalCPM" name="frmSwPolicyConfig"/></b>
		</td>
	
  </tr>
  
   	<tr class="gridOddRow">
  	 <td class="formLabel" width="60%">Dental </td>
       <td class="formLabel">
		<html:text name="frmSwPolicyConfigQuote" property="findentalCPM" readonly="true" styleClass="textBox textBoxSmall" />
		<b><bean:write property="findentalCPM" name="frmSwPolicyConfig"/></b>
		</td>
	
  </tr>
  </table>
</td></tr>
<tr><td>
  <table align="left" class="gridWithPricing" border="0" cellspacing="1" cellpadding="0">
    <tr class="gridEvenRow">
  	 <td   ID="listsubheader"  colspan="" CLASS="gridHeader" width="60%">All excluding maternity  </td>
       <td  ID="listsubheader"  colspan="" CLASS="gridHeader">
		<html:text name="frmSwPolicyConfigQuote" property="finallExlMaternity" readonly="true" styleClass="textBox textBoxSmall" />
		<bean:write property="finallExlMaternity" name="frmSwPolicyConfig"/>
		</td>
	
  </tr>
  
   	<tr class="gridEvenRow">
  	 <td  width="60%" ID="listsubheader"  colspan="" CLASS="gridHeader">Maternity  </td>
       <td  ID="listsubheader"  colspan="" CLASS="gridHeader">
		<bean:write property="finmaternityCPM" name="frmSwPolicyConfig"/>
		<html:text name="frmSwPolicyConfigQuote" property="finmaternityCPM" readonly="true" styleClass="textBox textBoxSmall" />
		</td>
	
  </tr>
    </table>
    </td>
  </tr>
  
  
  <tr><td>

	</td></tr>
	</table>
	
	
	</fieldSet> --%>
	
  <table align="center" class="buttonsContainer" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%" align="center">
	      	<button type="button" name="Button" accesskey="a" class="buttonsPricingLarge" onMouseout="this.className='buttonsPricingLarge'" onMouseover="this.className='buttonsPricingLarge buttonsHover'"	onClick="javascript:onViewGrosspremium();">Proceed >></button>&nbsp;
			
			 <button type="button" name="Button" accesskey="b" class="buttonsPricingSmall" onMouseout="this.className='buttonsPricingSmall'" onMouseover="this.className='buttonsPricingSmall buttonsHover'" onClick="javascript:onClosePlan();"><u>B</u>ack</button>&nbsp;

</td>
		
			
		</tr>
	</table>
	<!-- <fieldSet>
	  <table align="center" class="buttonsContainer" border="0" cellspacing="0" cellpadding="0">
		 <tr class="gridOddRow">
  	    <td class="formLabel">Avg. Age = Avergae age  </td>
    	<td class="formLabel" >MBL = Maximum benefit limit </td>
    		<td class="formLabel" >IP = Inpatient </td>
  	    <td class="formLabel" >OP = Outpatient </td>
	 	</tr><tr>
	 	<td class="formLabel" >MAT = Maternity </td>
  	    <td class="formLabel" >DENT =Dental </td>
  	     <td class="formLabel" > OPT = Optical</td>
  	      <td class="formLabel" > NA = Not applicable</td>
  </tr>
  <tr>
	 	<td class="formLabel" >A&E = American and european </td>
  	    <td class="formLabel" >ROW = Rest of the world </td>
  	     <td class="formLabel" > MENA = Middle east and northern africa</td>
  	      <td class="formLabel" > SEA = South east asia</td>
  </tr>
   <tr>
	 	<td class="formLabel" >SA = Southern asia</td>
  	    <td class="formLabel" >LOC = Local </td>
  
  </tr>
	</table>
	</fieldSet> -->
  
  <!-- S T A R T : Buttons -->
		        
	
	</div>
	<!-- E N D : Buttons -->
	 <html:hidden  name="frmSwPolicyConfig" property="finaldataSeqID"/> 
	 <html:hidden  name="frmSwPolicyConfig" property="calCPM_FlagYN"/> 
	 <html:hidden  name="frmSwPolicyConfig" styleId="groupProfileSeqID" property="lngGroupProfileSeqID"/> 
	  
	 
	<input type="hidden" name="mode"> 

	<html:hidden property="insClm" /> 
    <html:hidden property="insPat" />  
	<html:hidden property="caption" />  
	 <INPUT TYPE="hidden" NAME="totaliterate" id="totaliterate" VALUE="<%=totaliterate%>">
	 
	<INPUT TYPE="hidden" NAME="prpINPCPM" id="prpINPCPM" VALUE="<%=INPCPM%>">
	<INPUT TYPE="hidden" NAME="prpOUTCPM" id="prpOUTCPM" VALUE="<%=OUTCPM%>">
	<INPUT TYPE="hidden" NAME="prpMATCPM" id="prpMATCPM" VALUE="<%=MATCPM%>">
	<INPUT TYPE="hidden" NAME="prpOPTCPM" id="prpOPTCPM" VALUE="<%=OPTCPM%>">
	<INPUT TYPE="hidden" NAME="prpDENTCPM" id="prpDENTCPM" VALUE="<%=DENTCPM%>">
	<INPUT TYPE="hidden" NAME="durationcheck" id="durationcheckId" VALUE="${sessionScope.durationcheck}">
	 
 <INPUT TYPE="hidden" NAME="tab" VALUE="">
</html:form>
<!-- E N D : Content/Form Area -->