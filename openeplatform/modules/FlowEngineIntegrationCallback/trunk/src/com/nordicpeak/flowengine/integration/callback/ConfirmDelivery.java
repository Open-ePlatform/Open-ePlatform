
package com.nordicpeak.flowengine.integration.callback;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="flowInstanceID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="externalID" type="{http://www.oeplatform.org/version/1.0/schemas/integration/callback}ExternalID" minOccurs="0"/>
 *         &lt;element name="delivered" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="logMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "flowInstanceID",
    "externalID",
    "delivered",
    "logMessage"
})
@XmlRootElement(name = "confirmDelivery")
public class ConfirmDelivery {

    protected int flowInstanceID;
    protected ExternalID externalID;
    protected boolean delivered;
    protected String logMessage;

    /**
     * Gets the value of the flowInstanceID property.
     * 
     */
    public int getFlowInstanceID() {
        return flowInstanceID;
    }

    /**
     * Sets the value of the flowInstanceID property.
     * 
     */
    public void setFlowInstanceID(int value) {
        this.flowInstanceID = value;
    }

    /**
     * Gets the value of the externalID property.
     * 
     * @return
     *     possible object is
     *     {@link ExternalID }
     *     
     */
    public ExternalID getExternalID() {
        return externalID;
    }

    /**
     * Sets the value of the externalID property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExternalID }
     *     
     */
    public void setExternalID(ExternalID value) {
        this.externalID = value;
    }

    /**
     * Gets the value of the delivered property.
     * 
     */
    public boolean isDelivered() {
        return delivered;
    }

    /**
     * Sets the value of the delivered property.
     * 
     */
    public void setDelivered(boolean value) {
        this.delivered = value;
    }

    /**
     * Gets the value of the logMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogMessage() {
        return logMessage;
    }

    /**
     * Sets the value of the logMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogMessage(String value) {
        this.logMessage = value;
    }

}
