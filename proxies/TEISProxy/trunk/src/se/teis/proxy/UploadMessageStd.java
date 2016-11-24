
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
 *         &lt;element name="BixStdEnvelope" type="{http://schemas.datacontract.org/2004/07/TeisWsCommon}StdEnvelope" minOccurs="0"/>
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
    "bixStdEnvelope"
})
@XmlRootElement(name = "UploadMessageStd", namespace = "http://tieto.com/bix/")
public class UploadMessageStd {

    @XmlElement(name = "BixStdEnvelope", namespace = "http://tieto.com/bix/", nillable = true)
    protected StdEnvelope bixStdEnvelope;

    /**
     * Gets the value of the bixStdEnvelope property.
     * 
     * @return
     *     possible object is
     *     {@link StdEnvelope }
     *     
     */
    public StdEnvelope getBixStdEnvelope() {
        return bixStdEnvelope;
    }

    /**
     * Sets the value of the bixStdEnvelope property.
     * 
     * @param value
     *     allowed object is
     *     {@link StdEnvelope }
     *     
     */
    public void setBixStdEnvelope(StdEnvelope value) {
        this.bixStdEnvelope = value;
    }

}
