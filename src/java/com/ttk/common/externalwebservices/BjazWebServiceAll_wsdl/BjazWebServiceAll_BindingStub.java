/**
 * BjazWebServiceAll_BindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ttk.common.externalwebservices.BjazWebServiceAll_wsdl;
import org.apache.log4j.Logger;



public class BjazWebServiceAll_BindingStub extends org.apache.axis.client.Stub implements com.ttk.common.externalwebservices.BjazWebServiceAll_wsdl.BjazWebServiceAll_PortType {
	private static final Logger log = Logger.getLogger(BjazWebServiceAll_BindingStub.class);
	private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();
  
    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[1];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("bjazSingleWsForAll");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "pUserId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "pPassKey"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "pProcessFlag"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "pTieupName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "pInputDataXmlStr_inout"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "pErrorCode_out"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"), java.math.BigDecimal.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

    }

    public BjazWebServiceAll_BindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public BjazWebServiceAll_BindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public BjazWebServiceAll_BindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public java.util.Map bjazSingleWsForAll(java.lang.String pUserId, java.lang.String pPassKey, java.lang.String pProcessFlag, java.lang.String pTieupName, javax.xml.rpc.holders.StringHolder pInputDataXmlStr_inout, javax.xml.rpc.holders.BigDecimalHolder pErrorCode_out) throws java.rmi.RemoteException {
     
		log.info("Inside BjazWebServiceAll_BindingStub class bjazSingleWsForAll method");

    	if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        
        
        java.util.Map responsefromBajaj;
        responsefromBajaj = new java.util.HashMap();
       org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://com/bajajallianz/BjazWebServiceAll.wsdl/bjazSingleWsForAll");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://com/bajajallianz/BjazWebServiceAll.wsdl", "bjazSingleWsForAll"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {pUserId, pPassKey, pProcessFlag, pTieupName, pInputDataXmlStr_inout.value, pErrorCode_out.value});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                pInputDataXmlStr_inout.value = (java.lang.String) _output.get(new javax.xml.namespace.QName("", "pInputDataXmlStr_inout"));
                responsefromBajaj.put("pInputDataXmlStr_inout",pInputDataXmlStr_inout.value);
            } catch (java.lang.Exception _exception) {
                pInputDataXmlStr_inout.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "pInputDataXmlStr_inout")), java.lang.String.class);
                responsefromBajaj.put("pInputDataXmlStr_inout",pInputDataXmlStr_inout.value);

            }
            try {
                pErrorCode_out.value = (java.math.BigDecimal) _output.get(new javax.xml.namespace.QName("", "pErrorCode_out"));
                responsefromBajaj.put("pErrorCode_out",pErrorCode_out.value);

            } catch (java.lang.Exception _exception) {
                pErrorCode_out.value = (java.math.BigDecimal) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "pErrorCode_out")), java.math.BigDecimal.class);
                responsefromBajaj.put("pErrorCode_out",pErrorCode_out.value);

            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
return responsefromBajaj;
    }

}
