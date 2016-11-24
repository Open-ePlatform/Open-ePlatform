
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
 *         &lt;element name="UploadMessageBinResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "uploadMessageBinResult"
})
@XmlRootElement(name = "UploadMessageBinResponse", namespace = "http://tieto.com/bix/")
public class UploadMessageBinResponse {

    @XmlElement(name = "UploadMessageBinResult", namespace = "http://tieto.com/bix/", nillable = true)
    protected String uploadMessageBinResult;

    /**
     * Gets the value of the uploadMessageBinResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUploadMessageBinResult() {
        return uploadMessageBinResult;
    }

    /**
     * Sets the value of the uploadMessageBinResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUploadMessageBinResult(String value) {
        this.uploadMessageBinResult = value;
    }

}
