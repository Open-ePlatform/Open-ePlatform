
package com.nordicpeak.flowengine.integration.callback;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import com.nordicpeak.flowengine.integration.callback.exceptions.AccessDeniedException;
import com.nordicpeak.flowengine.integration.callback.exceptions.FlowInstanceNotFoundException;
import com.nordicpeak.flowengine.integration.callback.exceptions.StatusNotFoundException;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "IntegrationCallback", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface IntegrationCallback {


    /**
     * This method is used update the status of flow instances. The statuses are referred to using their ID or an alias.
     * 
     * @param principal
     * @param statusID
     * @param statusAlias
     * @param externalID
     * @param flowInstanceID
     * @return
     *     returns int
     * @throws StatusNotFoundException
     * @throws AccessDeniedException
     * @throws FlowInstanceNotFoundException
     */
    @WebMethod
    @WebResult(name = "eventID", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
    @RequestWrapper(localName = "setStatus", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback", className = "com.nordicpeak.flowengine.integration.callback.SetStatus")
    @ResponseWrapper(localName = "setStatusResponse", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback", className = "com.nordicpeak.flowengine.integration.callback.SetStatusResponse")
    public int setStatus(
        @WebParam(name = "flowInstanceID", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        Integer flowInstanceID,
        @WebParam(name = "externalID", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        ExternalID externalID,
        @WebParam(name = "statusID", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        Integer statusID,
        @WebParam(name = "statusAlias", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        String statusAlias,
        @WebParam(name = "principal", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        Principal principal)
        throws AccessDeniedException, FlowInstanceNotFoundException, StatusNotFoundException
    ;

    /**
     * This method is used to add events to the history flow instances. The message can be either in plain text or HTML.
     * 
     * @param date
     * @param principal
     * @param externalID
     * @param message
     * @param flowInstanceID
     * @return
     *     returns int
     * @throws AccessDeniedException
     * @throws FlowInstanceNotFoundException
     */
    @WebMethod
    @WebResult(name = "eventID", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
    @RequestWrapper(localName = "addEvent", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback", className = "com.nordicpeak.flowengine.integration.callback.AddEvent")
    @ResponseWrapper(localName = "addEventResponse", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback", className = "com.nordicpeak.flowengine.integration.callback.AddEventResponse")
    public int addEvent(
        @WebParam(name = "flowInstanceID", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        Integer flowInstanceID,
        @WebParam(name = "externalID", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        ExternalID externalID,
        @WebParam(name = "date", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        XMLGregorianCalendar date,
        @WebParam(name = "message", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        String message,
        @WebParam(name = "principal", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        Principal principal)
        throws AccessDeniedException, FlowInstanceNotFoundException
    ;

    /**
     * This method is used to add messages to flow instances. The message can be either in plain text or HTML.
     * 
     * @param principal
     * @param externalID
     * @param message
     * @param flowInstanceID
     * @return
     *     returns int
     * @throws AccessDeniedException
     * @throws FlowInstanceNotFoundException
     */
    @WebMethod
    @WebResult(name = "messageID", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
    @RequestWrapper(localName = "addMessage", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback", className = "com.nordicpeak.flowengine.integration.callback.AddMessage")
    @ResponseWrapper(localName = "addMessageResponse", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback", className = "com.nordicpeak.flowengine.integration.callback.AddMessageResponse")
    public int addMessage(
        @WebParam(name = "flowInstanceID", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        Integer flowInstanceID,
        @WebParam(name = "externalID", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        ExternalID externalID,
        @WebParam(name = "message", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        IntegrationMessage message,
        @WebParam(name = "principal", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        Principal principal)
        throws AccessDeniedException, FlowInstanceNotFoundException
    ;

    /**
     * This method is used to add messages to flow instances. The message can be either in plain text or HTML.
     * 
     * @param principal
     * @param externalID
     * @param message
     * @param flowInstanceID
     * @return
     *     returns int
     * @throws AccessDeniedException
     * @throws FlowInstanceNotFoundException
     */
    @WebMethod
    @WebResult(name = "messageID", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
    @RequestWrapper(localName = "addInternalMessage", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback", className = "com.nordicpeak.flowengine.integration.callback.AddInternalMessage")
    @ResponseWrapper(localName = "addInternalMessageResponse", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback", className = "com.nordicpeak.flowengine.integration.callback.AddInternalMessageResponse")
    public int addInternalMessage(
        @WebParam(name = "flowInstanceID", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        Integer flowInstanceID,
        @WebParam(name = "externalID", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        ExternalID externalID,
        @WebParam(name = "message", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        IntegrationMessage message,
        @WebParam(name = "principal", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        Principal principal)
        throws AccessDeniedException, FlowInstanceNotFoundException
    ;

    /**
     * This method is used by the various integration platforms to confirm whether or not a flow instance could be delivered to and external system. If the flow instance has been delivered and received a new ID in the external system then this new ID should be passed on to this method using the externalID parameter.
     * 
     * @param logMessage
     * @param externalID
     * @param delivered
     * @param flowInstanceID
     * @throws AccessDeniedException
     * @throws FlowInstanceNotFoundException
     */
    @WebMethod
    @RequestWrapper(localName = "confirmDelivery", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback", className = "com.nordicpeak.flowengine.integration.callback.ConfirmDelivery")
    @ResponseWrapper(localName = "confirmDeliveryResponse", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback", className = "com.nordicpeak.flowengine.integration.callback.ConfirmDeliveryResponse")
    public void confirmDelivery(
        @WebParam(name = "flowInstanceID", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        int flowInstanceID,
        @WebParam(name = "externalID", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        ExternalID externalID,
        @WebParam(name = "delivered", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        boolean delivered,
        @WebParam(name = "logMessage", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        String logMessage)
        throws AccessDeniedException, FlowInstanceNotFoundException
    ;

    /**
     * This method is used to delete flow instances.
     * 
     * @param logMessage
     * @param externalID
     * @param flowInstanceID
     * @throws AccessDeniedException
     * @throws FlowInstanceNotFoundException
     */
    @WebMethod
    @RequestWrapper(localName = "deleteInstance", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback", className = "com.nordicpeak.flowengine.integration.callback.DeleteInstance")
    @ResponseWrapper(localName = "deleteInstanceResponse", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback", className = "com.nordicpeak.flowengine.integration.callback.DeleteInstanceResponse")
    public void deleteInstance(
        @WebParam(name = "flowInstanceID", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        Integer flowInstanceID,
        @WebParam(name = "externalID", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        ExternalID externalID,
        @WebParam(name = "logMessage", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        String logMessage)
        throws AccessDeniedException, FlowInstanceNotFoundException
    ;

    /**
     * This method is used to set the managers currently handling the flow instance.
     * 
     * @param externalID
     * @param managerGroups
     * @param flowInstanceID
     * @param managers
     * @return
     *     returns int
     * @throws AccessDeniedException
     * @throws FlowInstanceNotFoundException
     */
    @WebMethod
    @WebResult(name = "setManagersReturn", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
    @RequestWrapper(localName = "setManagers", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback", className = "com.nordicpeak.flowengine.integration.callback.SetManagers")
    @ResponseWrapper(localName = "setManagersResponse", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback", className = "com.nordicpeak.flowengine.integration.callback.SetManagersResponse")
    public int setManagers(
        @WebParam(name = "flowInstanceID", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        Integer flowInstanceID,
        @WebParam(name = "externalID", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        ExternalID externalID,
        @WebParam(name = "managers", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        List<Principal> managers,
        @WebParam(name = "managerGroups", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        List<PrincipalGroup> managerGroups)
        throws AccessDeniedException, FlowInstanceNotFoundException
    ;

    /**
     * This method is used to get the managers currently handling the flow instance.
     * 
     * @param parameters
     * @return
     *     returns com.nordicpeak.flowengine.integration.callback.GetManagersResponse
     * @throws AccessDeniedException
     * @throws FlowInstanceNotFoundException
     */
    @WebMethod
    @WebResult(name = "getManagersResponse", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback", partName = "parameters")
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    public GetManagersResponse getManagers(
        @WebParam(name = "getManagersRequest", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback", partName = "parameters")
        GetManagersRequest parameters)
        throws AccessDeniedException, FlowInstanceNotFoundException
    ;

    /**
     * This method is used to set attributes in flow instances. The attribute name will be automatically prefixed by "callback.". An empty value will remove the attribute.
     * 
     * @param name
     * @param externalID
     * @param value
     * @param flowInstanceID
     * @throws AccessDeniedException
     * @throws FlowInstanceNotFoundException
     */
    @WebMethod
    @RequestWrapper(localName = "setAttribute", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback", className = "com.nordicpeak.flowengine.integration.callback.SetAttribute")
    @ResponseWrapper(localName = "setAttributeResponse", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback", className = "com.nordicpeak.flowengine.integration.callback.SetAttributeResponse")
    public void setAttribute(
        @WebParam(name = "flowInstanceID", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        Integer flowInstanceID,
        @WebParam(name = "externalID", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        ExternalID externalID,
        @WebParam(name = "name", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        String name,
        @WebParam(name = "value", targetNamespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback")
        String value)
        throws AccessDeniedException, FlowInstanceNotFoundException
    ;

}
