
package se.teis.proxy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Holds Meta/Receiver information.
 * 
 * <p>Java class for Receiver complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Receiver">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ReceiverAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ReceiverInfo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ReceiverOperator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ReceiverSubAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Receiver", propOrder = {
    "receiverAddress",
    "receiverInfo",
    "receiverOperator",
    "receiverSubAddress"
})
public class Receiver {

    @XmlElement(name = "ReceiverAddress", required = true, nillable = true)
    protected String receiverAddress;
    @XmlElement(name = "ReceiverInfo", required = true, nillable = true)
    protected String receiverInfo;
    @XmlElement(name = "ReceiverOperator", required = true, nillable = true)
    protected String receiverOperator;
    @XmlElement(name = "ReceiverSubAddress", required = true, nillable = true)
    protected String receiverSubAddress;

    /**
     * Gets the value of the receiverAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiverAddress() {
        return receiverAddress;
    }

    /**
     * Sets the value of the receiverAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiverAddress(String value) {
        this.receiverAddress = value;
    }

    /**
     * Gets the value of the receiverInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiverInfo() {
        return receiverInfo;
    }

    /**
     * Sets the value of the receiverInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiverInfo(String value) {
        this.receiverInfo = value;
    }

    /**
     * Gets the value of the receiverOperator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiverOperator() {
        return receiverOperator;
    }

    /**
     * Sets the value of the receiverOperator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiverOperator(String value) {
        this.receiverOperator = value;
    }

    /**
     * Gets the value of the receiverSubAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiverSubAddress() {
        return receiverSubAddress;
    }

    /**
     * Sets the value of the receiverSubAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiverSubAddress(String value) {
        this.receiverSubAddress = value;
    }

}
