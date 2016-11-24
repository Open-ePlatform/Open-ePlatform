
package se.teis.proxy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="GetMessageResult" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
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
    "getMessageResult"
})
@XmlRootElement(name = "GetMessageResponse", namespace = "http://tieto.com/bix/")
public class GetMessageResponse {

    @XmlElement(name = "GetMessageResult", namespace = "http://tieto.com/bix/", nillable = true)
    protected byte[] getMessageResult;

    /**
     * Gets the value of the getMessageResult property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getGetMessageResult() {
        return getMessageResult;
    }

    /**
     * Sets the value of the getMessageResult property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setGetMessageResult(byte[] value) {
        this.getMessageResult = value;
    }

}
