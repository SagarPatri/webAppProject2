<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,com.ttk.common.PreAuthWebBoardHelper,com.ttk.common.ClaimsWebBoardHelper,com.ttk.dto.usermanagement.UserSecurityProfile"%>

<html>
<head>
<script type="text/javascript" 	src="/ttk/scripts/preauth/preauthgeneral-async.js"></script>
<script type="text/javascript" >

		// 1: calling Override SubRemarks Dropdown	
		function onChangeOverrideRemarks()
		{
			var evr = document.getElementById("overrideGenRemarks");
			var overrideGenRemarks = evr.value;
		//	alert(overrideGenRemarks);
			
			 /* popupWindow=window.open("/PreAuthGeneralOverrideAction.do?mode=getOverrideGenSubRemarks"+"&overrideGenRemarks="+overrideGenRemarks,"PREAUTH","toolbar=no,scrollbars=yes,status=no,menubar=0,width=450,height=250");
		     document.onmousedown=focusPopup; 
			 document.onkeyup=focusPopup; 
			 document.onmousemove=focusPopup; */
			 document.forms[0].action="/PreAuthGeneralOverrideAction.do?mode=getOverrideGenSubRemarks"+"&overrideGenRemarks="+overrideGenRemarks;
		     document.forms[0].submit();
		} // end of onChangeOverrideRemarks()
	
		// 2: calling save procedure to submit form..	
		function saveGenOverrideRemarks()
		{
			var evr= document.getElementById("overrideGenRemarks");
			var overrideGenRemarks = evr.value;
			
			// 1: override remarks mandatory
			if(overrideGenRemarks == "")
			{
					alert("Please select Override Main Remarks.");
					return ;
			}
			
			// 2: override Subremarks mandatory
			if(overrideGenRemarks == "RPAC" || overrideGenRemarks == "PREA" )
			{
				var e2 = document.getElementById("overrideGenSubRemarks");
				var overrideGenSubRemarks = e2.value;
				if(overrideGenSubRemarks =="")
				{
					alert("Please select Override Sub Remarks.");
					return
				}
			}
			
			var e3 = document.getElementById("overrideGenOtherRemarks");
			var overrideGenOtherRemarks = e3.value;
			 var space = overrideGenOtherRemarks.charCodeAt(0);
		     if(space==32)
		     {
		            alert("Override Other Remarks should not start with space.");
		            document.getElementById("overrideGenOtherRemarks").value="";
		            document.getElementById("overrideGenOtherRemarks").focus();
		            return;
		     }
		     if(overrideGenOtherRemarks.length!=0 && overrideGenOtherRemarks.length<=20){
				 alert("Override Other Remarks Should Not Less Than 20 Characters");
				 document.getElementById("overrideGenOtherRemarks").focus();
				 return;
		     }	 
			window.opener.document.getElementById("overrideRemarks").value=overrideGenOtherRemarks;	
			
			window.opener.document.forms[1].mode.value="overridPreAuthDetails"; 
			window.opener.document.forms[1].action="/PreAuthGeneralAction.do?overrideGenRemarks="+overrideGenRemarks+"&overrideGenSubRemarks="+overrideGenSubRemarks;
			window.opener.document.forms[1].submit();
			window.close();
		} // end of saveGenOverrideRemarks()
		
		// 3: calling close button 
		function onClose()
		{
			window.close();
		} // end of onClose()
	</script>
</head>
<body>
	<%
		pageContext.setAttribute("overrideList",Cache.getCacheObject("overrideList"));
	%>
	
	<html:form action="/PreAuthGeneralOverrideAction.do">
	<table>
	<tr style="height: 40px;">
		<td>Override Main Remarks:<span class="mandatorySymbol">*</span></td>
		<td>
			<html:select property="overrideGenRemarks" styleId="overrideGenRemarks" onchange="onChangeOverrideRemarks();">
				<html:option value="">Select from list</html:option>
				<html:optionsCollection name="overrideList" label="cacheDesc" value="cacheId" />
			</html:select>
		</td>
	</tr>
	
	<logic:equal name="frmPreAuthGeneral" property="overrideGenRemarks" value="PREA">
		<tr style="height: 40px;">
			<td>Override Sub Remarks:<span class="mandatorySymbol">*</span></td>
			<td>
				<html:select property="overrideGenSubRemarks" styleId="overrideGenSubRemarks" >
					<html:option value="">Select from list</html:option>
					<html:optionsCollection  property="overrideRemarksSub" label="cacheDesc" value="cacheId" />
				</html:select>
			</td>
		</tr>
	</logic:equal>
	<logic:equal name="frmPreAuthGeneral" property="overrideGenRemarks" value="RPAC">
		<tr style="height: 40px;">
			<td>Override Sub Remarks:<span class="mandatorySymbol">*</span></td>
			<td>
				<html:select property="overrideGenSubRemarks" styleId="overrideGenSubRemarks" >
					<html:option value="">Select from list</html:option>
					<html:optionsCollection  property="overrideRemarksSub" label="cacheDesc" value="cacheId" />
				</html:select>
			</td>
		</tr>
	</logic:equal>
	<tr>
		<td>Override Other Remarks :</td>
		<td><html:textarea property="overrideGenOtherRemarks" styleId="overrideGenOtherRemarks" rows="3" cols="30" /></td>
	</tr>	
	
</table>
<table>
<tr>
	<td align="center" style="width: 13%;"><br><input type="button" value="Save" onclick="saveGenOverrideRemarks();">
											&nbsp;&nbsp;<input type="button" value="Close" onclick="onClose();">
	</td>
</tr>
</table>
<input type="hidden" id="mode" name="mode">
<input type="hidden" id="child" name="child">	
</html:form>
</body>
</html>