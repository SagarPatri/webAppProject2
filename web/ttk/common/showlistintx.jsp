<%
/**
 * @ (#) showlistintx.jsp Aug 13th 2006
 * Project      : TTK HealthCare Services
 * File         : showlistintx.jsp
 * Author       : kishor kumar S H
 * Company      : RCS technologies
 * Date Created : 21st May 2015
 *
 * @author       :kishor kumar S H
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>
<%@ page import="com.ttk.common.WebBoardHelper,com.ttk.common.TTKCommon,com.ttk.common.security.Cache"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<link href="/ttk/styles/Default.css" media="screen" rel="stylesheet"></link>
<script>

function save()
{	
	objform = document.forms[0];
	var checkid="checkid";
	var strCheckedVal="";
	var controlname=  document.forms[0].strControlName.value;
	var showRadio=document.forms[0].showRadio.value;
	var strTypeId	=	document.forms[0].strTypeId.value;
	if(showRadio=='Y')  //If showing radio box
	{
		for(var i=0;i<objform.length;i++)
		{
			var regexp=new RegExp("^(radid){1}[0-9]{1,}$"); 
			if(regexp.test(objform.elements[i].id))
			{
				if(objform.elements[i].checked == true)
				{
					strCheckedVal=objform.elements[i].value;
					break;
				}//end of if(objform.elements[i].checked == true)
			}//end of if(regexp.test(objform.elements[i].id))
		}//end of for(i=0;i<objform.length;i++)
		
		if(strCheckedVal=='')
		{
			alert('Select atleast one record');
			return false;
		}//end of if(strCheckedVal=='')
	}//end of if(showRadio=='Y')
	else
	{
		if("PAYERSCODEGEN"==strTypeId)
		{var chkdYN	=	"";
		for(i=0;i<objform.length;i++)
		{
			var regexp=new RegExp("("+checkid+"){1}[0-9]*");
			if(regexp.test(objform.elements[i].id))
			{
				if(objform.elements[i].checked == true) 
					chkdYN	=	"Y";
				else	
					chkdYN	=	"N";
					strCheckedVal=strCheckedVal+objform.elements[i].value+"|"+chkdYN+"||";
			}//end of if(regexp.test(objform.elements[i].id))
				
		}//end of for(i=0;i<objform.length;i++)
		strCheckedVal	=	"||"+strCheckedVal;
		if(strCheckedVal=="||") // if no check box is selected
			strCheckedVal="";
		else 				 // remove the last '|'
			strCheckedVal=strCheckedVal.substring(0,strCheckedVal.lastIndexOf('||'));
		}else{
			for(i=0;i<objform.length;i++)
			{
				var regexp=new RegExp("("+checkid+"){1}[0-9]*");
				if(regexp.test(objform.elements[i].id))
				{
					if(objform.elements[i].checked == true)
						strCheckedVal=strCheckedVal+objform.elements[i].value+"|";
				}//end of if(regexp.test(objform.elements[i].id))
			}//end of for(i=0;i<objform.length;i++)
			if(strCheckedVal=="|") // if no check box is selected
				strCheckedVal="";
			else 				 // remove the last '|'
				strCheckedVal=strCheckedVal.substring(0,strCheckedVal.lastIndexOf('|'));
		}
	}//end of else
	if (window.opener && !window.opener.closed)
			window.opener.document.getElementById(controlname).value = strCheckedVal;	
	window.opener.document.getElementById(controlname).focus();
	self.close();
}//end of save()

function checkPreSelect(iCount)
{
	var iTotalChecked = 0;
	var showRadio=document.forms[0].showRadio.value;
	var controlId=document.forms[0].strControlName.value;
	var strTypeId	=	document.forms[0].strTypeId.value;
	if(showRadio=='Y')		//for showing radio box
	{
		var selVal= window.opener.document.getElementById(controlId).value;
		for(var i=0;i<iCount;i++)
		{
			objCheck = document.getElementById("radid"+i);
			if(objCheck.value == selVal)
			{
				objCheck.checked= true;
				break;
			}//end of if(objCheck.value == selVal)
		}//end of for(i=0;i<objform.length;i++)
	}//end of if(showRadio=='Y')
	else	//for showing check box
	{
		if(strTypeId=="PAYERSCODEGEN"){
		
		var txtBoxVals	=	document.forms[0].txtBoxVals.value;
		var txtBoxValsArr	=	txtBoxVals.split("\|");
		var l	=	3;
		for(var k=0;k<txtBoxValsArr.length;k++)
		{
			/* if(txtBoxValsArr[k+1]=="Y")
			{ */
				objCheck = document.getElementById("checkid"+k);
				//if(isSelectedForEmpnl(txtBoxValsArr[k+1])==1)
				if(txtBoxValsArr[l]=="Y")
				{
					objCheck.checked = true;
					iTotalChecked++;
				}//end of if(strPreSelected.indexOf(objCheck.value)>=0)
			//}
				l	=	l+3;
		}
			
		/* for(i=0;i<iCount;i++)
		{
			objCheck = document.getElementById("checkid"+i);
			if(isSelected(objCheck.value)==1)
			{
				objCheck.checked = true;
				iTotalChecked++;
			}//end of if(strPreSelected.indexOf(objCheck.value)>=0)
		}//end of for(i=0;i<iCount;i++)
		 */}
		else{
			for(i=0;i<iCount;i++)
			{
				objCheck = document.getElementById("checkid"+i);
				if(isSelected(objCheck.value)==1)
				{
					objCheck.checked = true;
					iTotalChecked++;
				}//end of if(strPreSelected.indexOf(objCheck.value)>=0)
			}//end of for(i=0;i<iCount;i++)}
		}
		if(iTotalChecked==iCount)
			document.getElementById("checkall").checked = true;
	}//end of else
}//end of checkPreSelect(iCount)

function isSelected(strId)
{
	var controlname = document.forms[0].strControlName.value;
	var strPreSelected = window.opener.document.getElementById(controlname).value;
	var temp = new Array();
	temp = strPreSelected.split('|');
	for(var j=0;j<temp.length;j++)
		if(temp[j]==strId)
		  return 1;
	return 0;
}//end of isSelected(strId)

function isSelectedForEmpnl(strId)
{
	var controlname = document.forms[0].strControlName.value;
	var strPreSelected = window.opener.document.getElementById(controlname).value;
	var temp = new Array();
	temp = strPreSelected.split('|');
	//for(j=1;j<temp.length;j=(j+1))
		alert("strId::"+strId+"::index::"+index+"temp[index]::"+temp[index]);
		if(temp[index]==strId)
		  return 1;
	return 0;
}//end of isSelected(strId)

function oncheck(strId)
{
	var i	=	0;
	var bFlag=true;
	if(strId =='checkall')
		bFlag = document.getElementById("checkall").checked;
	var checkid="checkid";
	objform = document.forms[0];
	for(i=0;i<objform.length;i++)
	{
		var regexp=new RegExp("("+checkid+"){1}[0-9]*");
		if(regexp.test(objform.elements[i].id))
		{
			if(strId =='checkall')
				objform.elements[i].checked = bFlag;
			else if(objform.elements[i].checked != true)
			{
				bFlag=false;
				break;
			}//end of else if(objform.elements[i].checked != true)
		}//end of if(regexp.test(objform.elements[i].id))
	}//end of for(i=0;i<objform.length;i++)
 	document.getElementById("checkall").checked = bFlag ;
}//end of oncheck(strId)
</script>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>

<%
		String strTypeId = TTKCommon.checkNull((String)request.getParameter("typeId"));
       Long hospitalgroupseqid = null;

         if(request.getSession().getAttribute("hospitalgroupseqid")!=null && request.getSession().getAttribute("hospitalgroupseqid") !=""){
	       hospitalgroupseqid = Long.valueOf(String.valueOf(request.getSession().getAttribute("hospitalgroupseqid")));
             }
		String strControlName = TTKCommon.checkNull((String)request.getParameter("controlName"));
		String strControlValue = TTKCommon.checkNull((String)request.getParameter("controlVal"));
		String strShowRadio=TTKCommon.checkNull((String)request.getParameter("showRadio"));
		String strCondParam=TTKCommon.checkNull((String)request.getParameter("condParam"));
		
		String strIdTile = "";
		String strDescTitle ="";
		String toothCode	=	"";
		String toothDesc	=	"";
		String temp			=	"";
		int iRowCount = 0;
		if(strTypeId.equals("STA"))
		{
			pageContext.setAttribute("dataList", Cache.getCacheObject("stateCode"));
			strIdTile = "";
			strDescTitle ="State Name";
		}//end of if(strTypeId.equals("STA"))
		else if(strTypeId.equals("CON"))
		{
			pageContext.setAttribute("dataList", Cache.getCacheObject("countryCode"));
			strIdTile = "";
			strDescTitle ="Country Name";
		}//end of else if(strTypeId.equals("CON"))
		else if(strTypeId.equals("REL"))
		{
			pageContext.setAttribute("dataList", Cache.getCacheObject("relationshipCode"));
			strIdTile = "";
			strDescTitle ="Relationship";
		}//end of else if(strTypeId.equals("REL"))
		else if(strTypeId.equals("ACH"))
		{
			pageContext.setAttribute("dataList", Cache.getCacheObject("ruleAccountHead"));
			strIdTile = "";
			strDescTitle ="Account Head";
		}//end of else if(strTypeId.equals("ACH"))
		else if(strTypeId.equals("DAYCARE_GROUP"))
		{
			pageContext.setAttribute("dataList", Cache.getCacheObject("daycareGroup"));
			strIdTile = "";
			strDescTitle ="Ailment Groups";
		}//end of else if(strTypeId.equals("ACH"))
		else if(strTypeId.equals("MICD"))
		{
			pageContext.setAttribute("dataList", Cache.getCacheObject("maternityICDGroup"));
			strIdTile = "";
			strDescTitle ="Maternity ICD Groups";
		}//end of else if(strTypeId.equals("ACH"))
		
		//Changes As  per KOC 1284 Vhange Request
		else if(strTypeId.equals("REGION"))
		{
			pageContext.setAttribute("dataList", Cache.getCacheObject("regioncode"));
			strIdTile = "";
			strDescTitle ="Select Regions";
		}//end of else if(strTypeId.equals("REGION"))   
		//Changes As  per KOC 1284 Vhange Request
		//ADDED FOR KOC-1310
		else if (strTypeId.equals("CANCER_GROUP"))
		{
			pageContext.setAttribute("dataList", Cache.getCacheObject("cancerGroup"));
			strIdTile = "";
			strDescTitle = "Cancer ICD Groups";
		}else if (strTypeId.equals("PAYERSCODE"))
		{
			pageContext.setAttribute("dataList",Cache.getCacheObject("payerCode"));
			strIdTile = "";
			strDescTitle = "PAYERS";
		}else if (strTypeId.equals("PAYERSCODEGEN"))
		{
			pageContext.setAttribute("dataList",Cache.getCacheObject("payerCode"));
			strIdTile = "";
			strDescTitle = "PAYERS";
		}
		else if (strTypeId.equals("PROVIDERSCODEGEN"))
		{
			pageContext.setAttribute("dataList",Cache.getCacheObject("providerCode"));
			strIdTile = "";
			strDescTitle = "PROVIDERS";
		}
		else if (strTypeId.equals("PROVIDERSCODE"))
		{
			pageContext.setAttribute("dataList",Cache.getCacheObject1("providerCodeAdminTariff",strCondParam, request));
			strIdTile = "";
			strDescTitle = "PROVIDERS";
		}else if (strTypeId.equals("NETWORKS"))
		{
			pageContext.setAttribute("dataList",Cache.getCacheObject("networkType"));
			strIdTile = "";
			strDescTitle = "NETWORKS";
		}
		else if (strTypeId.equals("NETWORKSGEN"))
		{
			pageContext.setAttribute("dataList",Cache.getCacheObject1("networkTypeForTariff",strCondParam, request));
			strIdTile = "";
			strDescTitle = "NETWORKS GEN";
		}
		
		else if (strTypeId.equals("CORPORATES"))
		{
			pageContext.setAttribute("dataList",Cache.getCacheObject("corpCode"));
			strIdTile = "";
			strDescTitle = "CORPORATES";
		}else if (strTypeId.equals("HAADGROUP"))
		{
			pageContext.setAttribute("dataList",Cache.getCacheObject("haadGroup"));
			strIdTile = "";
			strDescTitle = "HAAD GROUP";
		}else if (strTypeId.equals("TOOTHNOS"))
		{
			pageContext.setAttribute("dataList",Cache.getCacheObject("toothNos"));
			strIdTile = "";
			strDescTitle = "TOOTH NOs";
		}
		else if (strTypeId.equals("GROUPNETWORKCAT"))
		{
			pageContext.setAttribute("dataList",Cache.getCacheObject("groupnetworkcat"));
			strIdTile = "";
			strDescTitle = "NETWORKS GEN";
		}
		else if (strTypeId.equals("GROUPPROVIDERLIST"))
		{
			pageContext.setAttribute("dataList",Cache.getCacheObject1("groupproviderlist", strControlValue, hospitalgroupseqid));
			strIdTile = "";
			strDescTitle = "PROVIDER LIST";
		}
		//ended
%>
<!-- S T A R T : Content/Form Area -->
<html:form action="/CorporateMemberAction.do" >
<title>List of - <%=strDescTitle%></title>
	<div class="contentArea" id="contentArea">
	<html:errors/>

	<table align="center" class="gridWithCheckBox" border="0" cellspacing="1" cellpadding="0">
	<tr>
	<td width="90%" ID="listsubheader" CLASS="gridHeader"> <%=strDescTitle%> &nbsp;</td>
		<td width="10%" ID="listsubheader" CLASS="gridHeader" nowrap="nowrap">
		<%if(!strShowRadio.equals("Y"))
		{
			if(!strTypeId.equals("TOOTHNOS")){//Not showing select all option for dental only
		%>
			Check All<input name="checkall" id="checkall" type="checkbox" onclick="javascript:oncheck('checkall')">&nbsp;
		<%
			}
		}//end of if(!strshowRadio.equals("Y"))
		%>
        <%=strIdTile%></td>
		
	</tr>
	<logic:notEmpty name="dataList">
		<logic:iterate id="dataList" name="dataList">
		<bean:define id="cacheDescTemp" name="dataList" property="cacheDesc"/>	
		   <%if(iRowCount%2==0) { %>
				<tr class="gridOddRow">
			<%
			  } else { %>
  				<tr class="gridEvenRow">
  			<%
			  } 
			  if(strTypeId.equals("TOOTHNOS")){
			  	temp	=	String.valueOf(cacheDescTemp);
				toothCode	=	temp.substring(0,temp.indexOf(':')-1);
				toothDesc	=	temp.substring(temp.indexOf(':')+1);%>
			  	<td width="90%" class="textLabelBold"><%=toothDesc %></td><!-- For Tooth Nos showing diffetntly in pop up -->
			  <%}else{ %>
			 	<td width="90%" class="textLabelBold"><bean:write name="dataList" property="cacheDesc" /></td>
			 	<%} %>
				<td width="10%" align="center" nowrap="nowrap" class="textLabelBold">
				<%
				if(strShowRadio.equals("Y")){
				%>
				<input type="radio" id="radid<%=iRowCount%>" name="optRadio" value="<bean:write name="dataList" property="cacheId" />">
				<%
				}else{
				%>
				<input type="checkbox" id="checkid<%=iRowCount%>" onclick="javascript:oncheck('');" name="optSelect" value="<bean:write name="dataList" property="cacheId" />">
				<%if(strTypeId.equals("TOOTHNOS")){%>
					<%= toothCode%>
				<%}
				}%>				
				</td>
				
			</tr>
			<%iRowCount++;%>
		</logic:iterate>
	</logic:notEmpty>
	  <tr>
		<td align="center" colspan="2" class="buttonsContainerGrid">
			  <%if(iRowCount!=0) { 
				  if(strTypeId.equals("PAYERSCODEGEN")){
			  %>
				<input type="button" name="Button" value="Associate" class="buttons" onClick="javascript:save();">&nbsp;
				<%}
				  else{ %>
					<input type="button" name="Button" value="Save" class="buttons" onClick="javascript:save();" accesskey="s">&nbsp;
					<%
				  }
			  }%>
			<input type="button" name="Button" value="Close" class="buttons" onClick="javascript:self.close();" accesskey="c">
		</td>
	  </tr>
	</table>
	<!-- E N D : Form Fields -->
<p>&nbsp;</p>
</div>
<input type="hidden" name="strControlName" value="<%=strControlName%>">
<input type="hidden" name="showRadio" value="<%=strShowRadio%>">
<input type="hidden" name="strTypeId" value="<%=strTypeId %>" >
<input type="hidden" name="txtBoxVals" value="<%=request.getParameter("controlVal") %>" >

<script>
	checkPreSelect(<%=iRowCount%>);
</script>
<!-- E N D : Buttons --></div>
<!-- E N D : Content/Form Area -->
<!-- E N D : Main Container Table -->

</html:form>
