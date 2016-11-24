
package se.teis.proxy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Applicable for operation UploadMessageStd and UploadMessageBin.
 * 				Holds Meta information.
 * 			
 * 
 * <p>Java class for Meta complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Meta">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DateTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EnvelopeType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EnvelopeVersion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MessageAckRequest" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="MessageFileName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MessageId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MessageInfo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MessageTestFlag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="MessageType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Receiver" type="{http://schemas.datacontract.org/2004/07/TeisWsCommon}Receiver"/>
 *         &lt;element name="Sender" type="{http://schemas.datacontract.org/2004/07/TeisWsCommon}Sender"/>
 *         &lt;element name="ServiceType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Meta", propOrder = {
    "dateTime",
    "envelopeType",
    "envelopeVersion",
    "messageAckRequest",
    "messageFileName",
    "messageId",
    "messageInfo",
    "messageTestFlag",
    "messageType",
    "receiver",
    "sender",
    "serviceType"
})
public class Meta {

    @XmlElement(name = "DateTime", required = true, nillable = true)
    protected String dateTime;
    @XmlElement(name = "EnvelopeType", required = true, nillable = true)
    protected String envelopeType;
    @XmlElement(name = "EnvelopeVersion", required = true, nillable = true)
    protected String envelopeVersion;
    @XmlElement(name = "MessageAckRequest")
    protected boolean messageAckRequest;
    @XmlElement(name = "MessageFileName", required = true, nillable = true)
    protected String messageFileName;
    @XmlElement(name = "MessageId", required = true, nillable = true)
    protected String messageId;
    @XmlElement(name = "MessageInfo", required = true, nillable = true)
    protected String messageInfo;
    @XmlElement(name = "MessageTestFlag")
    protected boolean messageTestFlag;
    @XmlElement(name = "MessageType", required = true, nillable = true)
    protected String messageType;
    @XmlElement(name = "Receiver", required = true, nillable = true)
    protected Receiver receiver;
    @XmlElement(name = "Sender", required = true, nillable = true)
    protected Sender sender;
    @XmlElement(name = "ServiceType", required = true, nillable = true)
    protected String serviceType;

    /**
     * Gets the value of the dateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * Sets the value of the dateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateTime(String value) {
        this.dateTime = value;
    }

    /**
     * Gets the value of the envelopeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnvelopeType() {
        return envelopeType;
    }

    /**
     * Sets the value of the envelopeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnvelopeType(String value) {
        this.envelopeType = value;
    }

    /**
     * Gets the value of the envelopeVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnvelopeVersion() {
        return envelopeVersion;
    }

    /**
     * Sets the value of the envelopeVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnvelopeVersion(String value) {
        this.envelopeVersion = value;
    }

    /**
     * Gets the value of the messageAckRequest property.
     * 
     */
    public boolean isMessageAckRequest() {
        return messageAckRequest;
    }

    /**
     * Sets the value of the messageAckRequest property.
     * 
     */
    public void setMessageAckRequest(boolean value) {
        this.messageAckRequest = value;
    }

    /**
     * Gets the value of the messageFileName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageFileName() {
        return messageFileName;
    }

    /**
     * Sets the value of the messageFileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageFileName(String value) {
        this.messageFileName = value;
    }

    /**
     * Gets the value of the messageId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Sets the value of the messageId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageId(String value) {
        this.messageId = value;
    }

    /**
     * Gets the value of the messageInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageInfo() {
        return messageInfo;
    }

    /**
     * Sets the value of the messageInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageInfo(String value) {
        this.messageInfo = value;
    }

    /**
     * Gets the value of the messageTestFlag property.
     * 
     */
    public boolean isMessageTestFlag() {
        return messageTestFlag;
    }

    /**
     * Sets the value of the messageTestFlag property.
     * 
     */
    public void setMessageTestFlag(boolean value) {
        this.messageTestFlag = value;
    }

    /**
     * Gets the value of the messageType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * Sets the value of the messageType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageType(String value) {
        this.messageType = value;
    }

    /**
     * Gets the value of the receiver property.
     * 
     * @return
     *     possible object is
     *     {@link Receiver }
     *     
     */
    public Receiver getReceiver() {
        return receiver;
    }

    /**
     * Sets the value of the receiver property.
     * 
     * @param value
     *     allowed object is
     *     {@link Receiver }
     *     
     */
    public void setReceiver(Receiver value) {
        this.receiver = value;
    }

    /**
     * Gets the value of the sender property.
     * 
     * @return
     *     possible object is
     *     {@link Sender }
     *     
     */
    public Sender getSender() {
        return sender;
    }

    /**
     * Sets the value of the sender property.
     * 
     * @param value
     *     allowed object is
     *     {@link Sender }
     *     
     */
    public void setSender(Sender value) {
        this.sender = value;
    }

    /**
     * Gets the value of the serviceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Sets the value of the serviceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceType(String value) {
        this.serviceType = value;
    }

}
