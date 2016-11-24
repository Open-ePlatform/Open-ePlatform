
package se.teis.proxy;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfAnswerData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfAnswerData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AnswerData" type="{http://schemas.datacontract.org/2004/07/TeisWsCommon}AnswerData" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfAnswerData", propOrder = {
    "answerData"
})
public class ArrayOfAnswerData {

    @XmlElement(name = "AnswerData", nillable = true)
    protected List<AnswerData> answerData;

    /**
     * Gets the value of the answerData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the answerData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAnswerData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AnswerData }
     * 
     * 
     */
    public List<AnswerData> getAnswerData() {
        if (answerData == null) {
            answerData = new ArrayList<AnswerData>();
        }
        return this.answerData;
    }

}
