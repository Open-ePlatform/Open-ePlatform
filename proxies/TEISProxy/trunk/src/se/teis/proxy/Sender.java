
package se.teis.proxy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Holds Meta/Sender information.
 * 
 * <p>Java class for Sender complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Sender">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SenderAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SenderInfo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SenderOperator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SenderSubAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Sender", propOrder = {
    "senderAddress",
    "senderInfo",
    "senderOperator",
    "senderSubAddress"
})
public class Sender {

    @XmlElement(name = "SenderAddress", required = true, nillable = true)
    protected String senderAddress;
    @XmlElement(name = "SenderInfo", required = true, nillable = true)
    protected String senderInfo;
    @XmlElement(name = "SenderOperator", required = true, nillable = true)
    protected String senderOperator;
    @XmlElement(name = "SenderSubAddress", required = true, nillable = true)
    protected String senderSubAddress;

    /**
     * Gets the value of the senderAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderAddress() {
        return senderAddress;
    }

    /**
     * Sets the value of the senderAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderAddress(String value) {
        this.senderAddress = value;
    }

    /**
     * Gets the value of the senderInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderInfo() {
        return senderInfo;
    }

    /**
     * Sets the value of the senderInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderInfo(String value) {
        this.senderInfo = value;
    }

    /**
     * Gets the value of the senderOperator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderOperator() {
        return senderOperator;
    }

    /**
     * Sets the value of the senderOperator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderOperator(String value) {
        this.senderOperator = value;
    }

    /**
     * Gets the value of the senderSubAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderSubAddress() {
        return senderSubAddress;
    }

    /**
     * Sets the value of the senderSubAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderSubAddress(String value) {
        this.senderSubAddress = value;
    }

}
