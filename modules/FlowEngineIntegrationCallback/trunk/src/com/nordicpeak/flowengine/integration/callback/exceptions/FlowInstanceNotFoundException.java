
package com.nordicpeak.flowengine.integration.callback.exceptions;

import javax.xml.ws.WebFault;

@WebFault(name = "FlowInstanceNotFoundFault", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
public class FlowInstanceNotFoundException extends Exception
{

    private static final long serialVersionUID = 8611074059538340227L;
    /**
     * Java type that goes as soapenv:Fault detail element.
     */
    private FlowInstanceNotFound faultInfo;

    /**
     * @param message
     * @param faultInfo
     */
    public FlowInstanceNotFoundException(String message, FlowInstanceNotFound faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * @param message
     * @param faultInfo
     * @param cause
     */
    public FlowInstanceNotFoundException(String message, FlowInstanceNotFound faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * @return
     *     returns fault bean: com.nordicpeak.flowengine.integration.callback.FlowInstanceNotFound
     */
    public FlowInstanceNotFound getFaultInfo() {
        return faultInfo;
    }

}
