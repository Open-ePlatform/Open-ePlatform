
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
 *         &lt;element name="QuestionAnswerResult" type="{http://schemas.datacontract.org/2004/07/TeisWsCommon}ArrayOfAnswerData" minOccurs="0"/>
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
    "questionAnswerResult"
})
@XmlRootElement(name = "QuestionAnswerResponse", namespace = "http://tieto.com/bix/")
public class QuestionAnswerResponse {

    @XmlElement(name = "QuestionAnswerResult", namespace = "http://tieto.com/bix/", nillable = true)
    protected ArrayOfAnswerData questionAnswerResult;

    /**
     * Gets the value of the questionAnswerResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAnswerData }
     *     
     */
    public ArrayOfAnswerData getQuestionAnswerResult() {
        return questionAnswerResult;
    }

    /**
     * Sets the value of the questionAnswerResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAnswerData }
     *     
     */
    public void setQuestionAnswerResult(ArrayOfAnswerData value) {
        this.questionAnswerResult = value;
    }

}
