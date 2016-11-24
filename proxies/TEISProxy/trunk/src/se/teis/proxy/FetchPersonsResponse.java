
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
 *         &lt;element name="FetchPersonsResult" type="{http://schemas.datacontract.org/2004/07/TeisWsCommon}ArrayOfPerson" minOccurs="0"/>
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
    "fetchPersonsResult"
})
@XmlRootElement(name = "FetchPersonsResponse", namespace = "http://tieto.com/bix/")
public class FetchPersonsResponse {

    @XmlElement(name = "FetchPersonsResult", namespace = "http://tieto.com/bix/", nillable = true)
    protected ArrayOfPerson fetchPersonsResult;

    /**
     * Gets the value of the fetchPersonsResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfPerson }
     *     
     */
    public ArrayOfPerson getFetchPersonsResult() {
        return fetchPersonsResult;
    }

    /**
     * Sets the value of the fetchPersonsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfPerson }
     *     
     */
    public void setFetchPersonsResult(ArrayOfPerson value) {
        this.fetchPersonsResult = value;
    }

}
