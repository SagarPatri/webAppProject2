/**
 * @ (#) CorporateMemberTree.java rd Feb 2006
 * Project      :
 * File         : CorporateMemberTree.java
 * Author       : Krishna K H
 * Company      :
 * Date Created : 3rd Feb 2006
 *
 * @author       : Krishna K H
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.action.tree;


/**
 * This class provides the information of Member Tree
 */
public class CorporateMemberTree extends Tree
{
	/**
	 * This creates the columnproperties objects for each and
	 * every column and adds the column object to the table
	 */
	public void setTreeProperties()
	{
		String strRootImagelist[]={"DependentsIcon","DomiciliaryIcon","PolicyIcon","CardIcon","RatesIcon","AddIcon","CancelledIcon","ChangePlanIcon","DeleteIcon"};	//"ChangePlanIcon" added for koc 1278 and 1270
        String strNodeImagelist[]={"PedIcon","PolicyIcon","CardIcon","EditIcon","CancelledIcon","MedicalCertificateIcon","DeleteIcon","MemberPolicyMapping"};
		String strRootImageTitle[]={"Renew Members","Domiciliary Treatment(OPD) / Hospitalization Limits/ High Deductable","Add MDF","Approve for Card Printing","Premium Details","Add New Dependant","Cancel Enrollment","Change Plan / Hospital Cash Benefit / Canvalescence Cash Benefit / Personal Waiting Period","Delete Alkoot"};	//Personal waiting period added for koc 1278 and 1270
        String strNodeImageTitle[]={"Pre Existing Desease","Medical Declaration Form","Approve for Card Printing","Edit Dependant Details","Cancel Dependant","Download Medical Insurance Certificate","Delete Dependant","Member Policy Mapping"};

        //FAMILY RULES UPDATED AS ADD MDF REF CR NO. 616
		setRowCount(10);
		setCurrentPage(1);
		setPageLinkCount(10);
		//Setting properties for Root
		Node nodeRoot= new Node();
        nodeRoot.setWidth(55);
        nodeRoot.setMethodName("getName");
        nodeRoot.setNodeMethodName("getMemberList");
        nodeRoot.setIsLink(true);
        nodeRoot.setIcons(createIcons(strRootImagelist,strRootImageTitle));
        setRootSetting(nodeRoot);

        //Setting properties for Node
        Node nodeChild = new Node();
        nodeChild.setWidth(55);
        nodeChild.setMethodName("getName");
        nodeChild.setIcons(createIcons(strNodeImagelist,strNodeImageTitle));
        setChildNodeSetting(nodeChild);
}//end of public void setTreeProperties()
}//end of class MemberTree

