
package se.teis.proxy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Applicable for GetProperty and FetchProperties.
 * 				Holds Property information.
 * 			
 * 
 * <p>Java class for Property complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Property">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Address" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BuildingAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Owners" type="{http://schemas.datacontract.org/2004/07/TeisWsCommon}ArrayOfOwner" minOccurs="0"/>
 *         &lt;element name="PropertyId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Property", propOrder = {
    "address",
    "buildingAddress",
    "name",
    "owners",
    "propertyId"
})
public class Property {

    @XmlElement(name = "Address", nillable = true)
    protected String address;
    @XmlElement(name = "BuildingAddress", nillable = true)
    protected String buildingAddress;
    @XmlElement(name = "Name", nillable = true)
    protected String name;
    @XmlElement(name = "Owners", nillable = true)
    protected ArrayOfOwner owners;
    @XmlElement(name = "PropertyId", nillable = true)
    protected String propertyId;

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * Gets the value of the buildingAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuildingAddress() {
        return buildingAddress;
    }

    /**
     * Sets the value of the buildingAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuildingAddress(String value) {
        this.buildingAddress = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the owners property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfOwner }
     *     
     */
    public ArrayOfOwner getOwners() {
        return owners;
    }

    /**
     * Sets the value of the owners property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfOwner }
     *     
     */
    public void setOwners(ArrayOfOwner value) {
        this.owners = value;
    }

    /**
     * Gets the value of the propertyId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPropertyId() {
        return propertyId;
    }

    /**
     * Sets the value of the propertyId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPropertyId(String value) {
        this.propertyId = value;
    }

}
