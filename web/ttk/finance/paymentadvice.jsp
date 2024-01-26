<%
/**
 * @ (#) chequeseries.jsp 9th June 2006
 * Project      : TTK HealthCare Services
 * File         : paymentadvice.jsp
 * Author       : Arun K.M
 * Company      : Span Systems Corporation
 * Date Created : 27th oct 2006
 *
 * @author       :Arun K.M
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ page import=" com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>

<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/finance/paymentadvice.js"></script>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<SCRIPT LANGUAGE="JavaScript">
	var TC_Disabled = true;
	var claimsAmt = new Array(<bean:write name="frmFloatAccounts" property="strClaimsAmt" filter="false"/>);
	var convertedClaimsAmt=new Array(<bean:write name="frmFloatAccounts" property="strConvertedClaimsAmt" filter="false"/>);
	var convertedUSDAmt=new Array(<bean:write name="frmFloatAccounts" property="strConvertedUSDAmt" filter="false"/>);
	var claimTypeDesc=new Array(<bean:write name="frmFloatAccounts" property="strClaimTypeDesc" filter="false"/>);
	var incurredCurr=new Array(<bean:write name="frmFloatAccounts" property="strIncurredCurr" filter="false"/>);
	var discountedAmount = new Array(<bean:write name="frmFloatAccounts" property="strDiscountedAmount" filter="false"/>);
	var payableAmount = new Array(<bean:write name="frmFloatAccounts" property="strPayableAmount" filter="false"/>);

</SCRIPT>
<%
	pageContext.setAttribute("bankName",Cache.getCacheObject("bankName"));
	pageContext.setAttribute("listofficeInfo",Cache.getCacheObject("officeInfo"));
	//start cr koc 1103 and 1105
	pageContext.setAttribute("paymentMethod", Cache.getCacheObject("paymentMethod1"));
	pageContext.setAttribute("curencyTypeList", Cache.getCacheObject("allCurencyCode"));
	pageContext.setAttribute("purposeOfRemit", Cache.getCacheObject("purposeOfRemit"));
	pageContext.setAttribute("paymentTypefin", Cache.getCacheObject("paymentTypefin"));
	pageContext.setAttribute("banksName", Cache.getCacheObject("banksName"));
	//end cr koc 1103 and 1105
%>

<html:form action="/PaymentAdviceAction.do" method="post" enctype="multipart/form-data">

		<logic:notEmpty name="fileName" scope="request">
			<SCRIPT LANGUAGE="JavaScript">
				var w = screen.availWidth - 10;
				var h = screen.availHeight - 82;
				var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
				window.open("/PaymentAdviceAction.do?mode=doPaymentAdviceXL&displayFile=<bean:write name="fileName"/>&alternateFileName=<bean:write name="alternateFileName"/>",'PaymentAdvice',features);
			</SCRIPT>
		</logic:notEmpty>
	<!-- S T A R T : Page Title -->
	
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
  		<tr>
    		<td width="57%">List of Claims</td>
    		<td align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
 		</tr>
	</table>

	<!-- E N D : Page Title -->
	<div class="contentArea" id="contentArea">
	<!-- S T A R T : Search Box -->

	<html:errors/>
	<logic:notEmpty name="notify" scope="session">
	
		<table align="center" class="errorContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td><img src="/ttk/images/ErrorIcon.gif" alt="Alert" title="Alert" width="16" height="16" align="absmiddle">&nbsp;
	    
	        <%=request.getSession().getAttribute("notify")%>
	        </td>
	      </tr>
   	 </table>
   	 </logic:notEmpty>
   	 <%  session.removeAttribute("notify");%>
   	 
	<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td nowrap>Float Name :<br>
            <html:text property="sFloatName"  styleClass="textBox textBoxMedium" maxlength="10"/>
        </td>
        <td nowrap>Alkoot Branch:<br>
            <html:select property="sTTKBranch" styleClass="selectBox selectBoxMedium" >
				<html:option value="">Any</html:option>
				<html:optionsCollection name="listofficeInfo" label="cacheDesc" value="cacheId"/>
				</html:select>
		</td>
        <td nowrap>Bank Account No.:<br>
          <html:text property="sbankaccountNbr"  styleClass="textBox textBoxMedium" maxlength="10"/>
        </td>
        <!-- start changes for cr koc 1103 and 1105 -->
        <td nowrap> Payment Method:<br>
        	<html:select property="paymethod" styleClass="selectBox selectBoxMedium" onchange="enableField(this)" >
				 <html:optionsCollection name="paymentMethod" label="cacheDesc" value="cacheId"/>
			</html:select>
		</td>
		</tr><tr>
		
		<td nowrap> Payment Type:<br>
        	<html:select property="paymentTypeprop" styleClass="selectBox selectBoxMedium" onchange="enableField(this)" >
				 <html:optionsCollection name="paymentTypefin" label="cacheDesc" value="cacheId"/>
			</html:select>
		</td>
		
		<td nowrap> Purpose Of Remit:<br>
        	<html:select property="purposeOfRemitprop" styleClass="selectBox selectBoxMedium" onchange="enableField(this)" >
				 <html:optionsCollection name="purposeOfRemit" label="cacheDesc" value="cacheId"/>
			</html:select>
		</td> 
		<td nowrap>Incurred Currency Format:<br>
        	<html:select property="incuredCurencyFormat" styleId="incuredCurencyFormat" styleClass="selectBox selectBoxMedium" onchange="enableField(this)" >
		   <html:option value="ANY">Any Currency</html:option>
		   <html:option value="OTQ">Other Than QAR</html:option>
				 <html:optionsCollection name="curencyTypeList" label="cacheDesc" value="cacheId"/>
			</html:select>
		</td> 
		 <td nowrap>Payable Currency :<br>
		 	 			<html:select property="payableCurrency" styleClass="selectBox selectBoxMedium">
		  					<html:option value="USD">USD</html:option>
		 					<html:option value="GBP">GBP</html:option>
		  					<html:option value="EUR">EURO</html:option>
		  				</html:select>
		  	</td>
			<td nowrap>Discount Type :<br>
			<html:select property="discountType" styleClass="selectBox selectBoxMedium" onchange="enableField(this)" >
        			<html:option value="NONE">None</html:option>
           		<%-- 	<html:option value="ANPD">Annual Net Payable Discount</html:option>
           			<html:option value="MNPD">Monthly Net Payable Discount</html:option> --%>
   			</html:select>
		</td>
		<!-- end changes for cr koc 1103 and 1105-->
        <td width="100%" valign="bottom" nowrap>
	        <a href="#" accesskey="s" onClick="javascript:onSearch(this)"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>
        </td>
        </tr>

    </table>
	<!-- E N D : Search Box -->
    <!-- S T A R T : Grid -->
	<ttk:HtmlGrid name="tableData"/>
    <!-- E N D : Grid -->
    <!-- S T A R T : Buttons and Page Counter -->

	<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <logic:match name="frmFloatAccounts" property="sBankDesc" value="CITI">
	      		<td align="right" width="60%">Bank Names:<br>
	            	<html:select property="banksName" styleClass="selectBox selectBoxMedium" onchange="javascript:onBankChanged()">	
						<html:optionsCollection name="banksName" label="cacheDesc" value="cacheId"/>
					</html:select>
					</td>
				</logic:match>
	      <logic:match name="frmFloatAccounts" property="sBankDesc" value="CITI">
		      <td align="center" width="80%">Payment Transfer in :<br>
	            <html:select property="spaymentTransIn" styleClass="selectBox selectBoxMedium" onchange="javascript:onPaymentTypeChanged()">	
					<html:option value="QAR">Transfer in QAR</html:option>
					<html:option value="USD">Transfer in USD</html:option>
				<%-- 	<html:option value="IIC">Transfer in Incurred Currency</html:option> --%>
					<html:option value="GBP">Transfer in GBP</html:option>
					<html:option value="EUR">Transfer in EUR</html:option>
				</html:select>
				</td>
			</logic:match>
	        <td width="60%" align="center">
	         <%
		   		if(TTKCommon.isDataFound(request,"tableData")&& TTKCommon.isAuthorized(request,"Add"))
		    		{
		    	%>
		    	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	
				Report Format :<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;				
				<html:select property="sreportFormat" styleClass="selectBox selectBoxMedium"  styleId="reportFormatId">
					<logic:match name="frmFloatAccounts" property="sBankDesc" value="CITI">		
						<logic:empty name="frmFloatAccounts" property="spaymentTransIn" >
								  <html:option value="Al khaliji Local Format">Al khaliji Local Format</html:option>
						 </logic:empty>
						 	 		 
						 <logic:notEmpty name="frmFloatAccounts" property="spaymentTransIn" > 
						 	<logic:match name="frmFloatAccounts" property="banksName" value="AL KHALIJ COMMERCIAL BANK">
						 			<logic:match name="frmFloatAccounts" property="spaymentTransIn" value="QAR">		
								 			 <html:option value="Al khaliji Local Format">Al khaliji Local Format</html:option> 
							 	 </logic:match>
								 <logic:notMatch name="frmFloatAccounts" property="spaymentTransIn" value="QAR">
								 			 <html:option value="Al khaliji Foreign Format">Al khaliji Foreign Format</html:option>
						         </logic:notMatch>
						    </logic:match>
						     <logic:match name="frmFloatAccounts" property="banksName" value="QATAR NATIONAL BANK">
						     				 <html:option value="QNB Format">QNB Format</html:option>
						     </logic:match>
						     <logic:match name="frmFloatAccounts" property="banksName" value="THE COMMERCIAL BANK">
						     				 <html:option value="Default Format">CBQ Format</html:option>
						     </logic:match>
						</logic:notEmpty>				 
					</logic:match>
<!--  CITI is default BANK setting in DAO , below code for QATAR banks in QATAR-->
					<logic:notMatch name="frmFloatAccounts" property="sBankDesc" value="CITI">
 						<logic:empty name="frmFloatAccounts" property="spaymentTransIn" >
								  <html:option value="Al khaliji Local Format">Al khaliji Local Format</html:option>
						 </logic:empty>
						 	 		 
						 <logic:notEmpty name="frmFloatAccounts" property="spaymentTransIn" > 
						 	<logic:match name="frmFloatAccounts" property="banksName" value="AL KHALIJ COMMERCIAL BANK">
						 			<logic:match name="frmFloatAccounts" property="spaymentTransIn" value="QAR">		
								 			 <html:option value="Al khaliji Local Format">Al khaliji Local Format</html:option> 
							 	 </logic:match>
								 <logic:notMatch name="frmFloatAccounts" property="spaymentTransIn" value="QAR">
								 			 <html:option value="Al khaliji Foreign Format">Al khaliji Foreign Format</html:option>
						         </logic:notMatch>
						    </logic:match>
						     <logic:match name="frmFloatAccounts" property="banksName" value="QATAR NATIONAL BANK">
						     				 <html:option value="QNB Format">QNB Format</html:option>
						     </logic:match>
						     <logic:match name="frmFloatAccounts" property="banksName" value="THE COMMERCIAL BANK">
						     				 <html:option value="Default Format">CBQ Format</html:option>
						     </logic:match>
						</logic:notEmpty>							 
					</logic:notMatch>
					
				</html:select>
				</td>
				  <td width="100%" nowrap>
				<%
		    		}//end of if(TTKCommon.isDataFound(request,"tableData")&& TTKCommon.isAuthorized(request,"Add"))
		    	%>
				
	        <%
		   		if(TTKCommon.isDataFound(request,"tableData")&& TTKCommon.isAuthorized(request,"Add"))
		    		{
		    	%>
		    	&nbsp;
		    	<logic:match name="frmFloatAccounts" property="sBankDesc" value="UTI">		    		
		    		   <button type="button" name="Button2" accesskey="g" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onGenerateUTIXL()"><u>G</u>enerateXL</button>&nbsp;
		    	</logic:match>
				<logic:notMatch name="frmFloatAccounts" property="sBankDesc" value="UTI">		    		
				   		<button type="button" name="Button2" accesskey="g" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onGenerateXL()"><u>G</u>enerateXL</button>&nbsp;
				</logic:notMatch>
		    	<%
		    		}//end of if(TTKCommon.isDataFound(request,"tableData")&& TTKCommon.isAuthorized(request,"Add"))
		    	%>
	        </td>
	      </tr>	  
	      <tr>
	          <td>
	          <ttk:PageLinks name="tableData"/>    
	          </td>
		  </tr>		          	
    </table>
    <table align="center;"   border="0" cellspacing="0" cellpadding="0" style="padding-left:17px;border-left-width:29px;" >
        <tr>
          
           <td width="60%" height="28" colspan="2">File Name :
          <html:file  name="frmFloatAccounts" property="stmFile"/>            
           </td>
        <td height="28" nowrap class="fieldGroupHeader">Available Float Balance :<br>
            <html:text property="avblFloatBalance" styleClass="textBox textBoxMedium textBoxDisabled" disabled="true"/></td>
        <td nowrap class="fieldGroupHeader">Total Amt. Selected :<br>
            <html:text property="totalAmt" styleClass="textBox textBoxMedium textBoxDisabled" disabled="true"/>
        </td>
        <td height="28" nowrap class="fieldGroupHeader">Available Balance :<br>
            <html:text property="availBalance" styleClass="textBox textBoxMedium textBoxDisabled" disabled="true"/></td> 
        </tr>
        
        <tr>
      
        <td width="100%" height="28" nowrap class="fieldGroupHeader"> &nbsp; </td>
         <td height="28" nowrap class="fieldGroupHeader">&nbsp;</td>
           <td height="28" nowrap class="fieldGroupHeader">Total Amount :<br>
             <input type="text" id="totlAmountinConvert"  class="textBox textBoxMedium textBoxDisabled" readonly="readonly"/></td>
       <td height="28" nowrap class="fieldGroupHeader">Converted Total Amount :<br>
            <input type="text" id="convertedTotalAmt"  class="textBox textBoxMedium textBoxDisabled" readonly="readonly"/></td>
           <td height="28" nowrap class="fieldGroupHeader"><!-- <br> -->
           <html:text property="incuredCurencyFormat" styleId="incuredCurencyFormatNew" styleClass="textBox textBoxTooTiny textBoxDisabled" disabled="true"/>
           <!-- </td>
           
           <td height="28" nowrap class="fieldGroupHeader"> Payable Amount in USD : <br> -->
          <logic:empty name="frmFloatAccounts" property="currencyType">						
 			Payable Amount in USD :
		</logic:empty>
        <logic:match name="frmFloatAccounts" property="currencyType" value="USD">						
 			Payable Amount in USD :
		</logic:match>
		<logic:match name="frmFloatAccounts" property="currencyType" value="GBP">						
 			Payable Amount in GBP :
		</logic:match>
		<logic:match name="frmFloatAccounts" property="currencyType" value="EUR">						
			Payable Amount in EURO :
		</logic:match>
            <input type="text" id="convertedTotalUSDAmt"  class="textBox textBoxSmall textBoxDisabled" readonly="readonly"/>
           </td>
      </tr>
      
      <tr>
      <td width="100%" height="28" nowrap class="fieldGroupHeader">&nbsp;</td>
        <td height="28" nowrap class="fieldGroupHeader">&nbsp;</td>
      
        <td height="28" nowrap class="fieldGroupHeader">Starting Cheque No.:<br>
        <logic:match name="frmFloatAccounts" property="paymethod" value="PMM">
            <html:text property="startNo" styleId="startNo" styleClass="textBox textBoxMedium" />
		</logic:match>
        <logic:notMatch name="frmFloatAccounts" property="paymethod" value="PMM">
        	<html:text property="startNo" styleId="startNo" disabled="true" styleClass="textBox textBoxMedium textBoxDisabled" />
        </logic:notMatch>
         </td>
         <td height="28" nowrap class="fieldGroupHeader">No. of Claims:<br>
                <html:text property="noofclaimssettlementnum" styleId="noofclaimssettlementnum" styleClass="textBox textBoxMedium textBoxDisabled" disabled="true" />
          </td>
        </tr>
        
         <tr>
        		<td width="100%" height="28" nowrap class="fieldGroupHeader">&nbsp;</td>
       			 <td height="28" nowrap class="fieldGroupHeader">&nbsp;</td>
        
     		 <td height="28" nowrap class="fieldGroupHeader">Total Fast Track Payment Discount<br>
             		<input type="text" id="totalPaymentDiscount"  class="textBox textBoxMedium textBoxDisabled" readonly="readonly"/>
             </td>
              <td height="28" nowrap class="fieldGroupHeader">Total Amount Payable after Discount<br>
             		<input type="text" id="totalPayableDiscount"  class="textBox textBoxMedium textBoxDisabled" readonly="readonly"/>
             </td> 
             
      </tr>
        
        <tr>
      	<td width="100%" height="28" nowrap class="fieldGroupHeader">
      		<!--<ttk:PageLinks name="tableData"/> 	-->&nbsp;
      		</td>
        <td>&nbsp;</td>
        <td height="28" nowrap class="fieldGroupHeader" colspan="3">Remarks:<br>
        <logic:match name="frmFloatAccounts" property="paymethod" value="EFT">
            <html:textarea property="remarks" styleClass="textBox textAreaMediumht" />
        </logic:match>
        <logic:notMatch name="frmFloatAccounts" property="paymethod" value="EFT">
            <html:textarea property="remarks" styleClass="textBox textAreaMediumht textAreaMediumhtDisabled" disabled="true"/>
        </logic:notMatch></td>
      </tr>
      
        <tr>
          <td width="100%" height="28" nowrap class="fieldGroupHead-r">
          <button type="button" name="uploadButton" accesskey="u" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onUploadBatchdetail()"><u>U</u>pload File</button>
          </td>
             <td> </td>
           <td> </td>   
            <td></td>   
        </tr>
   </table>
   <br>
    
     <table align="center" border="0" cellspacing="0" cellpadding="0" class="buttonsContainerGrid"> <tr> <td> 
       <fieldset style="width:65%;"><legend>Log Details</legend>
          <table align="left"   width="85%"  border="0"  style="padding-left:17px;border-left-width:29px;"  cellspacing="0" cellpadding="0">
        <tr>
          
           <td width="50%" height="28">
         
           Start Date:<br/>
           <html:text property="startDate" styleClass="textBox textDate" maxlength="10"/>
				<A NAME="calStartDate" ID="calStartDate" HREF="#" onClick="javascript:show_calendar('calStartDate','frmFloatAccounts.startDate',document.frmFloatAccounts.startDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"  width="24" height="17" border="0" align="absmiddle"></a>
           </td>
           <td width="50%" height="28">
           End Date:<br/>
           <html:text property="endDate" styleClass="textBox textDate" maxlength="10"/>
	        	<A NAME="calEndDate" ID="calEndDate" HREF="#" onClick="javascript:show_calendar('calEndDate','frmFloatAccounts.endDate',document.frmFloatAccounts.endDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"  width="24" height="17" border="0" align="absmiddle"></a>
         
             <a href="#" accesskey="s" onClick="javascript:onLogSearch()"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>
          
            </td>
              
        </tr>
             </table>
        </fieldset> 
        </td>
        
        <td>
  		<logic:equal value="Y"  property="sussessYN" name="frmFloatAccounts">
	<fieldset style="width: 90%"> <legend>Summary of your latest data uploaded</legend>
	 <table align="center"  border="0" cellspacing="0" cellpadding="0" style="width: 70%"> 
	    
			    <tr> 
			    	<td class="formLabel" width="50%"  align="left"> Total No. of Claims Uploaded </td> 
			    	<td width="50%" align="center"> <bean:write name="frmFloatAccounts" property="totalNoOfRows"/>  </td>
			    </tr><tr>
			    	<td class="formLabel" width="50%" align="left"> Total No. of Claims Success </td> 
			    	<td width="50%" align="center"> <bean:write name="frmFloatAccounts" property="totalNoOfRowsPassed"/>  </td>
			    </tr><tr>
			    	<td class="formLabel" width="50%" align="left"> Total No. of Claims Failed </td> 
			    	<td width="50%" align="center"> <bean:write name="frmFloatAccounts" property="totalNoOfRowsFailed"/>  </td>
			    </tr>
			    <tr><td>&nbsp;</td></tr> 
    <tr>
    <td colspan="2"> 
		<a href="#" onclick="javascript:onDownloadPaymentUploadLog();">Click here</a> to download error log. 
	</td> </tr>
			    
	
	</table>
	</fieldset>
	</logic:equal>
        </td>
        </tr>
        </table>

    <br>
    </div>
    <!-- E N D : Buttons and Page Counter -->
	<INPUT TYPE="hidden" NAME="rownum" VALUE="">
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<html:hidden name="frmFloatAccounts" property="currencyType"/>
<logic:equal value="Y"  property="sussessYN" name="frmFloatAccounts">
	<script>
		selectAll(true,document.forms[1]);
	</script>
</logic:equal>
	</html:form>

	<!-- E N D : Content/Form Area -->