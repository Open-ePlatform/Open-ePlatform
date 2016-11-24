
package com.nordicpeak.flowengine.integration.callback;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="managers" type="{http://www.oeplatform.org/version/1.0/schemas/integration/callback}Principal" maxOccurs="unbounded" minOccurs="0"/>
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
    "managers"
})
@XmlRootElement(name = "setManagers")
public class SetManagers {

    protected Integer flowInstanceID;
    protected ExternalID externalID;
    protected List<Principal> managers;

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
     * Gets the value of the managers property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the managers property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getManagers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Principal }
     * 
     * 
     */
    public List<Principal> getManagers() {
        if (managers == null) {
            managers = new ArrayList<Principal>();
        }
        return this.managers;
    }

}
