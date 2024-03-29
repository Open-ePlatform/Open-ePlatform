
package com.nordicpeak.flowengine.integration.callback.exceptions;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "FlowInstanceNotFoundFault", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
public class FlowInstanceNotFoundException
    extends Exception
{

    private static final long serialVersionUID = 3726130509723312217L;
	/**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private FlowInstanceNotFound faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public FlowInstanceNotFoundException(String message, FlowInstanceNotFound faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public FlowInstanceNotFoundException(String message, FlowInstanceNotFound faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: com.nordicpeak.flowengine.integration.callback.FlowInstanceNotFound
     */
    public FlowInstanceNotFound getFaultInfo() {
        return faultInfo;
    }

}
