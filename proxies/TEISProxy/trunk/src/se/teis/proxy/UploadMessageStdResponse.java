
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
 *         &lt;element name="UploadMessageStdResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "uploadMessageStdResult"
})
@XmlRootElement(name = "UploadMessageStdResponse", namespace = "http://tieto.com/bix/")
public class UploadMessageStdResponse {

    @XmlElement(name = "UploadMessageStdResult", namespace = "http://tieto.com/bix/", nillable = true)
    protected String uploadMessageStdResult;

    /**
     * Gets the value of the uploadMessageStdResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUploadMessageStdResult() {
        return uploadMessageStdResult;
    }

    /**
     * Sets the value of the uploadMessageStdResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUploadMessageStdResult(String value) {
        this.uploadMessageStdResult = value;
    }

}
