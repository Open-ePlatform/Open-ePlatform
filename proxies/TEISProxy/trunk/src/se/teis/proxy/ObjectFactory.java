
package se.teis.proxy;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the se.teis.proxy package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AnswerData_QNAME = new QName("http://schemas.datacontract.org/2004/07/TeisWsCommon", "AnswerData");
    private final static QName _AnyURI_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyURI");
    private final static QName _ArrayOfRelation_QNAME = new QName("http://schemas.datacontract.org/2004/07/TeisWsCommon", "ArrayOfRelation");
    private final static QName _ArrayOfAnswerData_QNAME = new QName("http://schemas.datacontract.org/2004/07/TeisWsCommon", "ArrayOfAnswerData");
    private final static QName _BinEnvelope_QNAME = new QName("http://schemas.datacontract.org/2004/07/TeisWsCommon", "BinEnvelope");
    private final static QName _Char_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "char");
    private final static QName _DateTime_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "dateTime");
    private final static QName _Relation_QNAME = new QName("http://schemas.datacontract.org/2004/07/TeisWsCommon", "Relation");
    private final static QName _Owner_QNAME = new QName("http://schemas.datacontract.org/2004/07/TeisWsCommon", "Owner");
    private final static QName _QName_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "QName");
    private final static QName _UnsignedShort_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedShort");
    private final static QName _Float_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "float");
    private final static QName _Long_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "long");
    private final static QName _Short_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "short");
    private final static QName _ArrayOfOwner_QNAME = new QName("http://schemas.datacontract.org/2004/07/TeisWsCommon", "ArrayOfOwner");
    private final static QName _Receiver_QNAME = new QName("http://schemas.datacontract.org/2004/07/TeisWsCommon", "Receiver");
    private final static QName _Base64Binary_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "base64Binary");
    private final static QName _Byte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "byte");
    private final static QName _Status_QNAME = new QName("http://schemas.datacontract.org/2004/07/TeisWsCommon", "Status");
    private final static QName _Person_QNAME = new QName("http://schemas.datacontract.org/2004/07/TeisWsCommon", "Person");
    private final static QName _Boolean_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "boolean");
    private final static QName _Meta_QNAME = new QName("http://schemas.datacontract.org/2004/07/TeisWsCommon", "Meta");
    private final static QName _StdEnvelope_QNAME = new QName("http://schemas.datacontract.org/2004/07/TeisWsCommon", "StdEnvelope");
    private final static QName _UnsignedByte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedByte");
    private final static QName _AnyType_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyType");
    private final static QName _UnsignedInt_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedInt");
    private final static QName _Int_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "int");
    private final static QName _QuestionData_QNAME = new QName("http://schemas.datacontract.org/2004/07/TeisWsCommon", "QuestionData");
    private final static QName _Connection_QNAME = new QName("http://schemas.datacontract.org/2004/07/TeisWsCommon", "Connection");
    private final static QName _Decimal_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "decimal");
    private final static QName _Double_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "double");
    private final static QName _ArrayOfProperty_QNAME = new QName("http://schemas.datacontract.org/2004/07/TeisWsCommon", "ArrayOfProperty");
    private final static QName _Property_QNAME = new QName("http://schemas.datacontract.org/2004/07/TeisWsCommon", "Property");
    private final static QName _Guid_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "guid");
    private final static QName _Duration_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "duration");
    private final static QName _ArrayOfPerson_QNAME = new QName("http://schemas.datacontract.org/2004/07/TeisWsCommon", "ArrayOfPerson");
    private final static QName _String_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "string");
    private final static QName _Sender_QNAME = new QName("http://schemas.datacontract.org/2004/07/TeisWsCommon", "Sender");
    private final static QName _UnsignedLong_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedLong");
    private final static QName _ArrayOfQuestionData_QNAME = new QName("http://schemas.datacontract.org/2004/07/TeisWsCommon", "ArrayOfQuestionData");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: se.teis.proxy
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetMessageResponse }
     * 
     */
    public GetMessageResponse createGetMessageResponse() {
        return new GetMessageResponse();
    }

    /**
     * Create an instance of {@link UploadMessageBin }
     * 
     */
    public UploadMessageBin createUploadMessageBin() {
        return new UploadMessageBin();
    }

    /**
     * Create an instance of {@link BinEnvelope }
     * 
     */
    public BinEnvelope createBinEnvelope() {
        return new BinEnvelope();
    }

    /**
     * Create an instance of {@link GetProperty }
     * 
     */
    public GetProperty createGetProperty() {
        return new GetProperty();
    }

    /**
     * Create an instance of {@link Connection }
     * 
     */
    public Connection createConnection() {
        return new Connection();
    }

    /**
     * Create an instance of {@link FetchProperties }
     * 
     */
    public FetchProperties createFetchProperties() {
        return new FetchProperties();
    }

    /**
     * Create an instance of {@link QuestionAnswerResponse }
     * 
     */
    public QuestionAnswerResponse createQuestionAnswerResponse() {
        return new QuestionAnswerResponse();
    }

    /**
     * Create an instance of {@link ArrayOfAnswerData }
     * 
     */
    public ArrayOfAnswerData createArrayOfAnswerData() {
        return new ArrayOfAnswerData();
    }

    /**
     * Create an instance of {@link UploadMessageStd }
     * 
     */
    public UploadMessageStd createUploadMessageStd() {
        return new UploadMessageStd();
    }

    /**
     * Create an instance of {@link StdEnvelope }
     * 
     */
    public StdEnvelope createStdEnvelope() {
        return new StdEnvelope();
    }

    /**
     * Create an instance of {@link GetStatus }
     * 
     */
    public GetStatus createGetStatus() {
        return new GetStatus();
    }

    /**
     * Create an instance of {@link UploadMessageStdResponse }
     * 
     */
    public UploadMessageStdResponse createUploadMessageStdResponse() {
        return new UploadMessageStdResponse();
    }

    /**
     * Create an instance of {@link QuestionAnswer }
     * 
     */
    public QuestionAnswer createQuestionAnswer() {
        return new QuestionAnswer();
    }

    /**
     * Create an instance of {@link ArrayOfQuestionData }
     * 
     */
    public ArrayOfQuestionData createArrayOfQuestionData() {
        return new ArrayOfQuestionData();
    }

    /**
     * Create an instance of {@link GetMessage }
     * 
     */
    public GetMessage createGetMessage() {
        return new GetMessage();
    }

    /**
     * Create an instance of {@link FetchPersons }
     * 
     */
    public FetchPersons createFetchPersons() {
        return new FetchPersons();
    }

    /**
     * Create an instance of {@link FetchPropertiesResponse }
     * 
     */
    public FetchPropertiesResponse createFetchPropertiesResponse() {
        return new FetchPropertiesResponse();
    }

    /**
     * Create an instance of {@link ArrayOfProperty }
     * 
     */
    public ArrayOfProperty createArrayOfProperty() {
        return new ArrayOfProperty();
    }

    /**
     * Create an instance of {@link UploadMessageBinResponse }
     * 
     */
    public UploadMessageBinResponse createUploadMessageBinResponse() {
        return new UploadMessageBinResponse();
    }

    /**
     * Create an instance of {@link GetStatusResponse }
     * 
     */
    public GetStatusResponse createGetStatusResponse() {
        return new GetStatusResponse();
    }

    /**
     * Create an instance of {@link Status }
     * 
     */
    public Status createStatus() {
        return new Status();
    }

    /**
     * Create an instance of {@link GetPerson }
     * 
     */
    public GetPerson createGetPerson() {
        return new GetPerson();
    }

    /**
     * Create an instance of {@link FetchPersonsResponse }
     * 
     */
    public FetchPersonsResponse createFetchPersonsResponse() {
        return new FetchPersonsResponse();
    }

    /**
     * Create an instance of {@link ArrayOfPerson }
     * 
     */
    public ArrayOfPerson createArrayOfPerson() {
        return new ArrayOfPerson();
    }

    /**
     * Create an instance of {@link GetPersonResponse }
     * 
     */
    public GetPersonResponse createGetPersonResponse() {
        return new GetPersonResponse();
    }

    /**
     * Create an instance of {@link Person }
     * 
     */
    public Person createPerson() {
        return new Person();
    }

    /**
     * Create an instance of {@link GetPropertyResponse }
     * 
     */
    public GetPropertyResponse createGetPropertyResponse() {
        return new GetPropertyResponse();
    }

    /**
     * Create an instance of {@link Property }
     * 
     */
    public Property createProperty() {
        return new Property();
    }

    /**
     * Create an instance of {@link Owner }
     * 
     */
    public Owner createOwner() {
        return new Owner();
    }

    /**
     * Create an instance of {@link Relation }
     * 
     */
    public Relation createRelation() {
        return new Relation();
    }

    /**
     * Create an instance of {@link AnswerData }
     * 
     */
    public AnswerData createAnswerData() {
        return new AnswerData();
    }

    /**
     * Create an instance of {@link ArrayOfRelation }
     * 
     */
    public ArrayOfRelation createArrayOfRelation() {
        return new ArrayOfRelation();
    }

    /**
     * Create an instance of {@link QuestionData }
     * 
     */
    public QuestionData createQuestionData() {
        return new QuestionData();
    }

    /**
     * Create an instance of {@link Sender }
     * 
     */
    public Sender createSender() {
        return new Sender();
    }

    /**
     * Create an instance of {@link ArrayOfOwner }
     * 
     */
    public ArrayOfOwner createArrayOfOwner() {
        return new ArrayOfOwner();
    }

    /**
     * Create an instance of {@link Receiver }
     * 
     */
    public Receiver createReceiver() {
        return new Receiver();
    }

    /**
     * Create an instance of {@link Meta }
     * 
     */
    public Meta createMeta() {
        return new Meta();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AnswerData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/TeisWsCommon", name = "AnswerData")
    public JAXBElement<AnswerData> createAnswerData(AnswerData value) {
        return new JAXBElement<AnswerData>(_AnswerData_QNAME, AnswerData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyURI")
    public JAXBElement<String> createAnyURI(String value) {
        return new JAXBElement<String>(_AnyURI_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRelation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/TeisWsCommon", name = "ArrayOfRelation")
    public JAXBElement<ArrayOfRelation> createArrayOfRelation(ArrayOfRelation value) {
        return new JAXBElement<ArrayOfRelation>(_ArrayOfRelation_QNAME, ArrayOfRelation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAnswerData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/TeisWsCommon", name = "ArrayOfAnswerData")
    public JAXBElement<ArrayOfAnswerData> createArrayOfAnswerData(ArrayOfAnswerData value) {
        return new JAXBElement<ArrayOfAnswerData>(_ArrayOfAnswerData_QNAME, ArrayOfAnswerData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BinEnvelope }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/TeisWsCommon", name = "BinEnvelope")
    public JAXBElement<BinEnvelope> createBinEnvelope(BinEnvelope value) {
        return new JAXBElement<BinEnvelope>(_BinEnvelope_QNAME, BinEnvelope.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "char")
    public JAXBElement<Integer> createChar(Integer value) {
        return new JAXBElement<Integer>(_Char_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "dateTime")
    public JAXBElement<XMLGregorianCalendar> createDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DateTime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Relation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/TeisWsCommon", name = "Relation")
    public JAXBElement<Relation> createRelation(Relation value) {
        return new JAXBElement<Relation>(_Relation_QNAME, Relation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Owner }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/TeisWsCommon", name = "Owner")
    public JAXBElement<Owner> createOwner(Owner value) {
        return new JAXBElement<Owner>(_Owner_QNAME, Owner.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "QName")
    public JAXBElement<QName> createQName(QName value) {
        return new JAXBElement<QName>(_QName_QNAME, QName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedShort")
    public JAXBElement<Integer> createUnsignedShort(Integer value) {
        return new JAXBElement<Integer>(_UnsignedShort_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "float")
    public JAXBElement<Float> createFloat(Float value) {
        return new JAXBElement<Float>(_Float_QNAME, Float.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "long")
    public JAXBElement<Long> createLong(Long value) {
        return new JAXBElement<Long>(_Long_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "short")
    public JAXBElement<Short> createShort(Short value) {
        return new JAXBElement<Short>(_Short_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfOwner }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/TeisWsCommon", name = "ArrayOfOwner")
    public JAXBElement<ArrayOfOwner> createArrayOfOwner(ArrayOfOwner value) {
        return new JAXBElement<ArrayOfOwner>(_ArrayOfOwner_QNAME, ArrayOfOwner.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Receiver }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/TeisWsCommon", name = "Receiver")
    public JAXBElement<Receiver> createReceiver(Receiver value) {
        return new JAXBElement<Receiver>(_Receiver_QNAME, Receiver.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "base64Binary")
    public JAXBElement<byte[]> createBase64Binary(byte[] value) {
        return new JAXBElement<byte[]>(_Base64Binary_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Byte }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "byte")
    public JAXBElement<Byte> createByte(Byte value) {
        return new JAXBElement<Byte>(_Byte_QNAME, Byte.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Status }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/TeisWsCommon", name = "Status")
    public JAXBElement<Status> createStatus(Status value) {
        return new JAXBElement<Status>(_Status_QNAME, Status.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Person }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/TeisWsCommon", name = "Person")
    public JAXBElement<Person> createPerson(Person value) {
        return new JAXBElement<Person>(_Person_QNAME, Person.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "boolean")
    public JAXBElement<Boolean> createBoolean(Boolean value) {
        return new JAXBElement<Boolean>(_Boolean_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Meta }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/TeisWsCommon", name = "Meta")
    public JAXBElement<Meta> createMeta(Meta value) {
        return new JAXBElement<Meta>(_Meta_QNAME, Meta.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StdEnvelope }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/TeisWsCommon", name = "StdEnvelope")
    public JAXBElement<StdEnvelope> createStdEnvelope(StdEnvelope value) {
        return new JAXBElement<StdEnvelope>(_StdEnvelope_QNAME, StdEnvelope.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedByte")
    public JAXBElement<Short> createUnsignedByte(Short value) {
        return new JAXBElement<Short>(_UnsignedByte_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyType")
    public JAXBElement<Object> createAnyType(Object value) {
        return new JAXBElement<Object>(_AnyType_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedInt")
    public JAXBElement<Long> createUnsignedInt(Long value) {
        return new JAXBElement<Long>(_UnsignedInt_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "int")
    public JAXBElement<Integer> createInt(Integer value) {
        return new JAXBElement<Integer>(_Int_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QuestionData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/TeisWsCommon", name = "QuestionData")
    public JAXBElement<QuestionData> createQuestionData(QuestionData value) {
        return new JAXBElement<QuestionData>(_QuestionData_QNAME, QuestionData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Connection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/TeisWsCommon", name = "Connection")
    public JAXBElement<Connection> createConnection(Connection value) {
        return new JAXBElement<Connection>(_Connection_QNAME, Connection.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "decimal")
    public JAXBElement<BigDecimal> createDecimal(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_Decimal_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "double")
    public JAXBElement<Double> createDouble(Double value) {
        return new JAXBElement<Double>(_Double_QNAME, Double.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfProperty }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/TeisWsCommon", name = "ArrayOfProperty")
    public JAXBElement<ArrayOfProperty> createArrayOfProperty(ArrayOfProperty value) {
        return new JAXBElement<ArrayOfProperty>(_ArrayOfProperty_QNAME, ArrayOfProperty.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Property }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/TeisWsCommon", name = "Property")
    public JAXBElement<Property> createProperty(Property value) {
        return new JAXBElement<Property>(_Property_QNAME, Property.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "guid")
    public JAXBElement<String> createGuid(String value) {
        return new JAXBElement<String>(_Guid_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Duration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "duration")
    public JAXBElement<Duration> createDuration(Duration value) {
        return new JAXBElement<Duration>(_Duration_QNAME, Duration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfPerson }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/TeisWsCommon", name = "ArrayOfPerson")
    public JAXBElement<ArrayOfPerson> createArrayOfPerson(ArrayOfPerson value) {
        return new JAXBElement<ArrayOfPerson>(_ArrayOfPerson_QNAME, ArrayOfPerson.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "string")
    public JAXBElement<String> createString(String value) {
        return new JAXBElement<String>(_String_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Sender }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/TeisWsCommon", name = "Sender")
    public JAXBElement<Sender> createSender(Sender value) {
        return new JAXBElement<Sender>(_Sender_QNAME, Sender.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedLong")
    public JAXBElement<BigInteger> createUnsignedLong(BigInteger value) {
        return new JAXBElement<BigInteger>(_UnsignedLong_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfQuestionData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/TeisWsCommon", name = "ArrayOfQuestionData")
    public JAXBElement<ArrayOfQuestionData> createArrayOfQuestionData(ArrayOfQuestionData value) {
        return new JAXBElement<ArrayOfQuestionData>(_ArrayOfQuestionData_QNAME, ArrayOfQuestionData.class, null, value);
    }

}
