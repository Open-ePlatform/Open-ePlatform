
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
 *         &lt;element name="GetPropertyResult" type="{http://schemas.datacontract.org/2004/07/TeisWsCommon}Property" minOccurs="0"/>
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
    "getPropertyResult"
})
@XmlRootElement(name = "GetPropertyResponse", namespace = "http://tieto.com/bix/")
public class GetPropertyResponse {

    @XmlElement(name = "GetPropertyResult", namespace = "http://tieto.com/bix/", nillable = true)
    protected Property getPropertyResult;

    /**
     * Gets the value of the getPropertyResult property.
     * 
     * @return
     *     possible object is
     *     {@link Property }
     *     
     */
    public Property getGetPropertyResult() {
        return getPropertyResult;
    }

    /**
     * Sets the value of the getPropertyResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Property }
     *     
     */
    public void setGetPropertyResult(Property value) {
        this.getPropertyResult = value;
    }

}
