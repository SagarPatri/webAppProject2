/** @ (#) addenrollment.js 07th April 2017
 * Project     : Project X
 * File        : providerdetails.js
 * Author      : Nagababu K
 * Company     : RCS Technologies
 * Date Created: 07th April 2017
 *
 * @author 		 : Nagababu K
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */


function onPreauthProccess(){		
	document.forms[1].mode.value ="doPreauthProccess";
	document.forms[1].child.value ="PreauthProccess";
	document.forms[1].action="/PbmPharmacyGeneralAction.do";
	document.forms[1].submit();
}
