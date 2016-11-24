
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
 *         &lt;element name="FetchPropertiesResult" type="{http://schemas.datacontract.org/2004/07/TeisWsCommon}ArrayOfProperty" minOccurs="0"/>
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
    "fetchPropertiesResult"
})
@XmlRootElement(name = "FetchPropertiesResponse", namespace = "http://tieto.com/bix/")
public class FetchPropertiesResponse {

    @XmlElement(name = "FetchPropertiesResult", namespace = "http://tieto.com/bix/", nillable = true)
    protected ArrayOfProperty fetchPropertiesResult;

    /**
     * Gets the value of the fetchPropertiesResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfProperty }
     *     
     */
    public ArrayOfProperty getFetchPropertiesResult() {
        return fetchPropertiesResult;
    }

    /**
     * Sets the value of the fetchPropertiesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfProperty }
     *     
     */
    public void setFetchPropertiesResult(ArrayOfProperty value) {
        this.fetchPropertiesResult = value;
    }

}
