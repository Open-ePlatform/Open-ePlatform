
package com.nordicpeak.flowengine.integration.callback;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import com.nordicpeak.flowengine.integration.callback.exceptions.AccessDenied;
import com.nordicpeak.flowengine.integration.callback.exceptions.FlowInstanceNotFound;
import com.nordicpeak.flowengine.integration.callback.exceptions.StatusNotFound;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.nordicpeak.flowengine.integration.callback package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _StatusNotFoundFault_QNAME = new QName("http://www.oeplatform.org/version/1.0/schemas/integration/callback", "StatusNotFoundFault");
    private final static QName _AccessDeniedFault_QNAME = new QName("http://www.oeplatform.org/version/1.0/schemas/integration/callback", "AccessDeniedFault");
    private final static QName _FlowInstanceNotFoundFault_QNAME = new QName("http://www.oeplatform.org/version/1.0/schemas/integration/callback", "FlowInstanceNotFoundFault");
    private final static QName _PrincipalName_QNAME = new QName("http://www.oeplatform.org/version/1.0/schemas/integration/callback", "name");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.nordicpeak.flowengine.integration.callback
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DeleteInstanceResponse }
     * 
     */
    public DeleteInstanceResponse createDeleteInstanceResponse() {
        return new DeleteInstanceResponse();
    }

    /**
     * Create an instance of {@link FlowInstanceNotFound }
     * 
     */
    public FlowInstanceNotFound createFlowInstanceNotFound() {
        return new FlowInstanceNotFound();
    }

    /**
     * Create an instance of {@link SetManagers }
     * 
     */
    public SetManagers createSetManagers() {
        return new SetManagers();
    }

    /**
     * Create an instance of {@link ExternalID }
     * 
     */
    public ExternalID createExternalID() {
        return new ExternalID();
    }

    /**
     * Create an instance of {@link Principal }
     * 
     */
    public Principal createPrincipal() {
        return new Principal();
    }

    /**
     * Create an instance of {@link PrincipalGroup }
     * 
     */
    public PrincipalGroup createPrincipalGroup() {
        return new PrincipalGroup();
    }

    /**
     * Create an instance of {@link SetStatusResponse }
     * 
     */
    public SetStatusResponse createSetStatusResponse() {
        return new SetStatusResponse();
    }

    /**
     * Create an instance of {@link GetManagersRequest }
     * 
     */
    public GetManagersRequest createGetManagersRequest() {
        return new GetManagersRequest();
    }

    /**
     * Create an instance of {@link AddInternalMessage }
     * 
     */
    public AddInternalMessage createAddInternalMessage() {
        return new AddInternalMessage();
    }

    /**
     * Create an instance of {@link IntegrationMessage }
     * 
     */
    public IntegrationMessage createIntegrationMessage() {
        return new IntegrationMessage();
    }

    /**
     * Create an instance of {@link AddMessageResponse }
     * 
     */
    public AddMessageResponse createAddMessageResponse() {
        return new AddMessageResponse();
    }

    /**
     * Create an instance of {@link StatusNotFound }
     * 
     */
    public StatusNotFound createStatusNotFound() {
        return new StatusNotFound();
    }

    /**
     * Create an instance of {@link SetStatus }
     * 
     */
    public SetStatus createSetStatus() {
        return new SetStatus();
    }

    /**
     * Create an instance of {@link AddMessage }
     * 
     */
    public AddMessage createAddMessage() {
        return new AddMessage();
    }

    /**
     * Create an instance of {@link AccessDenied }
     * 
     */
    public AccessDenied createAccessDenied() {
        return new AccessDenied();
    }

    /**
     * Create an instance of {@link AddEvent }
     * 
     */
    public AddEvent createAddEvent() {
        return new AddEvent();
    }

    /**
     * Create an instance of {@link ConfirmDeliveryResponse }
     * 
     */
    public ConfirmDeliveryResponse createConfirmDeliveryResponse() {
        return new ConfirmDeliveryResponse();
    }

    /**
     * Create an instance of {@link SetAttribute }
     * 
     */
    public SetAttribute createSetAttribute() {
        return new SetAttribute();
    }

    /**
     * Create an instance of {@link AddInternalMessageResponse }
     * 
     */
    public AddInternalMessageResponse createAddInternalMessageResponse() {
        return new AddInternalMessageResponse();
    }

    /**
     * Create an instance of {@link SetManagersResponse }
     * 
     */
    public SetManagersResponse createSetManagersResponse() {
        return new SetManagersResponse();
    }

    /**
     * Create an instance of {@link SetAttributeResponse }
     * 
     */
    public SetAttributeResponse createSetAttributeResponse() {
        return new SetAttributeResponse();
    }

    /**
     * Create an instance of {@link AddEventResponse }
     * 
     */
    public AddEventResponse createAddEventResponse() {
        return new AddEventResponse();
    }

    /**
     * Create an instance of {@link DeleteInstance }
     * 
     */
    public DeleteInstance createDeleteInstance() {
        return new DeleteInstance();
    }

    /**
     * Create an instance of {@link ConfirmDelivery }
     * 
     */
    public ConfirmDelivery createConfirmDelivery() {
        return new ConfirmDelivery();
    }

    /**
     * Create an instance of {@link GetManagersResponse }
     * 
     */
    public GetManagersResponse createGetManagersResponse() {
        return new GetManagersResponse();
    }

    /**
     * Create an instance of {@link Attachment }
     * 
     */
    public Attachment createAttachment() {
        return new Attachment();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StatusNotFound }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback", name = "StatusNotFoundFault")
    public JAXBElement<StatusNotFound> createStatusNotFoundFault(StatusNotFound value) {
        return new JAXBElement<StatusNotFound>(_StatusNotFoundFault_QNAME, StatusNotFound.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AccessDenied }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback", name = "AccessDeniedFault")
    public JAXBElement<AccessDenied> createAccessDeniedFault(AccessDenied value) {
        return new JAXBElement<AccessDenied>(_AccessDeniedFault_QNAME, AccessDenied.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FlowInstanceNotFound }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback", name = "FlowInstanceNotFoundFault")
    public JAXBElement<FlowInstanceNotFound> createFlowInstanceNotFoundFault(FlowInstanceNotFound value) {
        return new JAXBElement<FlowInstanceNotFound>(_FlowInstanceNotFoundFault_QNAME, FlowInstanceNotFound.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oeplatform.org/version/1.0/schemas/integration/callback", name = "name", scope = Principal.class)
    public JAXBElement<String> createPrincipalName(String value) {
        return new JAXBElement<String>(_PrincipalName_QNAME, String.class, Principal.class, value);
    }

}
