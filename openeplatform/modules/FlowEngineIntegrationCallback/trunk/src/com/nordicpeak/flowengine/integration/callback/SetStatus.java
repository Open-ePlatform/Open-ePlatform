
package com.nordicpeak.flowengine.integration.callback;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;choice>
 *           &lt;element name="flowInstanceID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *           &lt;element name="externalID" type="{http://www.oeplatform.org/version/1.0/schemas/integration/callback}ExternalID"/>
 *         &lt;/choice>
 *         &lt;choice>
 *           &lt;element name="statusID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *           &lt;element name="statusAlias" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;/choice>
 *         &lt;element name="principal" type="{http://www.oeplatform.org/version/1.0/schemas/integration/callback}Principal" minOccurs="0"/>
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
    "flowInstanceID",
    "externalID",
    "statusID",
    "statusAlias",
    "principal"
})
@XmlRootElement(name = "setStatus")
public class SetStatus {

    protected Integer flowInstanceID;
    protected ExternalID externalID;
    protected Integer statusID;
    protected String statusAlias;
    protected Principal principal;

    /**
     * Gets the value of the flowInstanceID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getFlowInstanceID() {
        return flowInstanceID;
    }

    /**
     * Sets the value of the flowInstanceID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setFlowInstanceID(Integer value) {
        this.flowInstanceID = value;
    }

    /**
     * Gets the value of the externalID property.
     * 
     * @return
     *     possible object is
     *     {@link ExternalID }
     *     
     */
    public ExternalID getExternalID() {
        return externalID;
    }

    /**
     * Sets the value of the externalID property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExternalID }
     *     
     */
    public void setExternalID(ExternalID value) {
        this.externalID = value;
    }

    /**
     * Gets the value of the statusID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getStatusID() {
        return statusID;
    }

    /**
     * Sets the value of the statusID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setStatusID(Integer value) {
        this.statusID = value;
    }

    /**
     * Gets the value of the statusAlias property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusAlias() {
        return statusAlias;
    }

    /**
     * Sets the value of the statusAlias property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusAlias(String value) {
        this.statusAlias = value;
    }

    /**
     * Gets the value of the principal property.
     * 
     * @return
     *     possible object is
     *     {@link Principal }
     *     
     */
    public Principal getPrincipal() {
        return principal;
    }

    /**
     * Sets the value of the principal property.
     * 
     * @param value
     *     allowed object is
     *     {@link Principal }
     *     
     */
    public void setPrincipal(Principal value) {
        this.principal = value;
    }

}
