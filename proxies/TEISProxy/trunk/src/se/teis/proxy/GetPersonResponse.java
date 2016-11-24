
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
 *         &lt;element name="GetPersonResult" type="{http://schemas.datacontract.org/2004/07/TeisWsCommon}Person" minOccurs="0"/>
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
    "getPersonResult"
})
@XmlRootElement(name = "GetPersonResponse", namespace = "http://tieto.com/bix/")
public class GetPersonResponse {

    @XmlElement(name = "GetPersonResult", namespace = "http://tieto.com/bix/", nillable = true)
    protected Person getPersonResult;

    /**
     * Gets the value of the getPersonResult property.
     * 
     * @return
     *     possible object is
     *     {@link Person }
     *     
     */
    public Person getGetPersonResult() {
        return getPersonResult;
    }

    /**
     * Sets the value of the getPersonResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Person }
     *     
     */
    public void setGetPersonResult(Person value) {
        this.getPersonResult = value;
    }

}
