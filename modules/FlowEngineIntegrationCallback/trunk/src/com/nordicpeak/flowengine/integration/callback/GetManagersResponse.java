
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
 *         &lt;element name="managers" type="{http://www.oeplatform.org/version/1.0/schemas/integration/callback}Principal" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="managerGroups" type="{http://www.oeplatform.org/version/1.0/schemas/integration/callback}PrincipalGroup" maxOccurs="unbounded" minOccurs="0"/>
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
    "managers",
    "managerGroups"
})
@XmlRootElement(name = "getManagersResponse")
public class GetManagersResponse {

    protected List<Principal> managers;
    protected List<PrincipalGroup> managerGroups;

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

    /**
     * Gets the value of the managerGroups property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the managerGroups property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getManagerGroups().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PrincipalGroup }
     * 
     * 
     */
    public List<PrincipalGroup> getManagerGroups() {
        if (managerGroups == null) {
            managerGroups = new ArrayList<PrincipalGroup>();
        }
        return this.managerGroups;
    }

}
