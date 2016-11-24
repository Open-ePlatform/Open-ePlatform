
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
 *         &lt;element name="BixBinEnvelope" type="{http://schemas.datacontract.org/2004/07/TeisWsCommon}BinEnvelope" minOccurs="0"/>
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
    "bixBinEnvelope"
})
@XmlRootElement(name = "UploadMessageBin", namespace = "http://tieto.com/bix/")
public class UploadMessageBin {

    @XmlElement(name = "BixBinEnvelope", namespace = "http://tieto.com/bix/", nillable = true)
    protected BinEnvelope bixBinEnvelope;

    /**
     * Gets the value of the bixBinEnvelope property.
     * 
     * @return
     *     possible object is
     *     {@link BinEnvelope }
     *     
     */
    public BinEnvelope getBixBinEnvelope() {
        return bixBinEnvelope;
    }

    /**
     * Sets the value of the bixBinEnvelope property.
     * 
     * @param value
     *     allowed object is
     *     {@link BinEnvelope }
     *     
     */
    public void setBixBinEnvelope(BinEnvelope value) {
        this.bixBinEnvelope = value;
    }

}
