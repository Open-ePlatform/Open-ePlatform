
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
 *         &lt;element name="connection" type="{http://schemas.datacontract.org/2004/07/TeisWsCommon}Connection" minOccurs="0"/>
 *         &lt;element name="QuestionType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Questions" type="{http://schemas.datacontract.org/2004/07/TeisWsCommon}ArrayOfQuestionData" minOccurs="0"/>
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
    "connection",
    "questionType",
    "questions"
})
@XmlRootElement(name = "QuestionAnswer", namespace = "http://tieto.com/bix/")
public class QuestionAnswer {

    @XmlElement(namespace = "http://tieto.com/bix/", nillable = true)
    protected Connection connection;
    @XmlElement(name = "QuestionType", namespace = "http://tieto.com/bix/", nillable = true)
    protected String questionType;
    @XmlElement(name = "Questions", namespace = "http://tieto.com/bix/", nillable = true)
    protected ArrayOfQuestionData questions;

    /**
     * Gets the value of the connection property.
     * 
     * @return
     *     possible object is
     *     {@link Connection }
     *     
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Sets the value of the connection property.
     * 
     * @param value
     *     allowed object is
     *     {@link Connection }
     *     
     */
    public void setConnection(Connection value) {
        this.connection = value;
    }

    /**
     * Gets the value of the questionType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuestionType() {
        return questionType;
    }

    /**
     * Sets the value of the questionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuestionType(String value) {
        this.questionType = value;
    }

    /**
     * Gets the value of the questions property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfQuestionData }
     *     
     */
    public ArrayOfQuestionData getQuestions() {
        return questions;
    }

    /**
     * Sets the value of the questions property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfQuestionData }
     *     
     */
    public void setQuestions(ArrayOfQuestionData value) {
        this.questions = value;
    }

}
