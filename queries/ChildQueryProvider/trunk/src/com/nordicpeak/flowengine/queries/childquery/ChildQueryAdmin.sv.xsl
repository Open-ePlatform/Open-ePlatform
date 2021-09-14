<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:import href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>

	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="ChildQueryAdminTemplates.xsl"/>
	<xsl:variable name="java.queryDescription">
Fr�getypen Val av barn h�mtar uppgifter om de barn den inloggade anv�ndaren �r v�rdnadshavare f�r och kr�ver att ett barn v�ljs om fr�getypen �r satt som obligatorisk. Detta kr�ver en koppling mot Skatteverket (Navet).
Fr�getypen kan �ven h�mta in uppgifter om �vriga v�rdnadshavare f�r barnet.
	</xsl:variable>
	<xsl:variable name="java.queryTypeName">Val av barn</xsl:variable>
	<xsl:variable name="java.testChildrenMenuName">Testbarn</xsl:variable>
	
	<xsl:variable name="java.childSelectedAlternativeName">Barn valt</xsl:variable>
	<xsl:variable name="java.singleGuardianAlternativeName">Ensam v�rdnad</xsl:variable>
	<xsl:variable name="java.multiGuardianAlternativeName">Gemensam  v�rdnad</xsl:variable>
	
	<xsl:variable name="java.exportChildCitizenName">Barnets namn</xsl:variable>
	<xsl:variable name="java.exportChildCitizenFirstName">Barnets f�rnamn</xsl:variable>
	<xsl:variable name="java.exportChildCitizenLastName">Barnets efternamn</xsl:variable>
	<xsl:variable name="java.exportChildCitizenIdentifier">Barnets personnummer</xsl:variable>
	<xsl:variable name="java.exportChildAdress">Barnets address</xsl:variable>
	<xsl:variable name="java.exportChildPostalAdress">Barnets postort</xsl:variable>
	<xsl:variable name="java.exportChildZipCode">Barnets postnummer</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianName">Andra v�rdnadshavarens namn</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianFirstname">Andra v�rdnadshavarens f�rnamn</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianLastname">Andra v�rdnadshavarens efternamn</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianEmail">Andra v�rdnadshavarens e-post</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianPhone">Andra v�rdnadshavarens telefon</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianCitizenIdentifier">Andra v�rdnadshavarens personnummer</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianAdress">Andra v�rdnadshavarens address</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianPostalAdress">Andra v�rdnadshavarens postort</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianZipCode">Andra v�rdnadshavarens postnummer</xsl:variable>
	
	<xsl:variable name="i18n.UpdateQuery">�ndra fr�ga</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara �ndringar</xsl:variable>
	<xsl:variable name="i18n.ChildQueryNotFound">Den beg�rda fr�gan hittades inte</xsl:variable>
	
	<xsl:variable name="i18n.OtherGuardiansDescription">Beskrivning f�r andra v�rdnadshavare</xsl:variable>
	<xsl:variable name="i18n.UseMultipartSigning">Anv�nd flerpartssignering</xsl:variable>
	<xsl:variable name="i18n.Query.skipMultipartSigningIfSameAddress">Om b�da v�rdnadshavarna �r folkbokf�rda p� samma adress beh�vs endast s�kandes signering</xsl:variable>
	<xsl:variable name="i18n.Query.skipMultipartSigningIfSameAddress.warning">Systemet hanterar ej folkbokf�ringsadresser som �r inskrivna p� olika s�tt hos Skatteverket och i dessa fall m�ste b�da v�rdnadshavarna signera ans�kan.</xsl:variable>
	<xsl:variable name="i18n.AlwaysShowOtherGuardians">Visa alltid andra v�rdnadshavare</xsl:variable>
	<xsl:variable name="i18n.ShowAddress">Visa folkbokf�ringsadress f�r barn</xsl:variable>
	<xsl:variable name="i18n.ShowGuardianAddress">Visa folkbokf�ringsaddress f�r v�rdnadshavare</xsl:variable>
	<xsl:variable name="i18n.RequiredContactWays">Kr�vda kontaktv�gar f�r andra v�rdnadshavare</xsl:variable>
	<xsl:variable name="i18n.RequiredContactWayVerification">Kr�v verifiering av kontaktv�gar</xsl:variable>
	<xsl:variable name="i18n.MinChildAge">L�gsta �lder p� barn (l�mna tomt f�r ingen gr�ns)</xsl:variable>
	<xsl:variable name="i18n.MaxChildAge">H�gsta �lder p� barn (l�mna tomt f�r ingen gr�ns)</xsl:variable>
	<xsl:variable name="i18n.MinAgeLargerThanMaxAge">Minsta �lder kan inte vara st�rre �n h�gsta �lder</xsl:variable>
	<xsl:variable name="i18n.SetMultipartsAsOwners">S�tt andra v�rdnadshavaren som meds�kande f�r �rendet</xsl:variable>
	<xsl:variable name="i18n.CommunicationErrorDescription">Meddelande om det inte g�r att kontakta navet eller API-k�llan (kan l�mnas tom f�r att �terst�lla standardtext)</xsl:variable>
	
	<xsl:variable name="i18n.FilterEndpoint.Attributes">Extra f�lt</xsl:variable>
	<xsl:variable name="i18n.FilterEndpoint.Attribute.DisplayMode">Visningsl�ge</xsl:variable>
	<xsl:variable name="i18n.FilterEndpoint.Attribute.DisplayMode.Always">Visa alltid</xsl:variable>
	<xsl:variable name="i18n.FilterEndpoint.Attribute.DisplayMode.IfValue">Visa om v�rde finns</xsl:variable>
	<xsl:variable name="i18n.FilterEndpoint.Attribute.DisplayMode.Never">Visa inte</xsl:variable>
	
	<xsl:variable name="i18n.ContactWays.Either">Antingen e-post eller mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.ContactWays.Email">E-post</xsl:variable>
	<xsl:variable name="i18n.ContactWays.Phone">Mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.ContactWays.Both">B�de e-post och mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.HideSSNForOtherGuardians">D�lj personnummer p� andra v�rdnadshavaren</xsl:variable>
	
	<xsl:variable name="i18n.TestChildren.title">Testbarn</xsl:variable>
	<xsl:variable name="i18n.TestChildren.description.part1">H�r kan du aktivera testbarn i val av barnfr�gan f�r denna inloggning. N�r denna funktionen �r aktiverad s� h�mtas inga barn fr�n Skatteverket, ist�llet visas en fast upps�ttning testbarn. Testbarnen har �ldern 0-17 �r samt kommunkod 4321.</xsl:variable>
	<xsl:variable name="i18n.TestChildren.description.part2">Denna �ndring sl�r endast igenom p� nya �renden. Om du redan har ett �rende �ppet s� kommer det inte p�verkas. Denna inst�llning nollst�lls varje g�ng du loggar in och ut.</xsl:variable>
	<xsl:variable name="i18n.TestChildren.enabled">Aktivera testbarn</xsl:variable>
	
	<xsl:variable name="i18n.FilterEndpoint.title">V�lj API-k�lla f�r filtrering och extra information</xsl:variable>
	<xsl:variable name="i18n.FilterEndpoint.EmptyOption">Ingen API-k�lla vald</xsl:variable>
	<xsl:variable name="i18n.EmptyFilterDescription">Meddelande vid avsaknad av data fr�n API-k�llan (tomt svar, inga barn kvar efter filtrering eller �ldersgr�ns anv�nds och inga barn �r inom intervallet)</xsl:variable>
	<xsl:variable name="i18n.useFilteredChildrenDescription">Visa meddelande vid bortfiltrering av barn (men �tminstone 1 barn blev kvar efter filtrering)</xsl:variable>
	<xsl:variable name="i18n.FilteredChildrenDescription">Meddelande f�r bortfiltrerade barn</xsl:variable>
	<xsl:variable name="i18n.FilteredChildrenDescription.Tags.children">Namn p� barnen som filterats bort</xsl:variable>
	<xsl:variable name="i18n.Tags">F�ljande taggar kan anv�ndas i texten ovan</xsl:variable>
	<xsl:variable name="i18n.Tags.name">Tagg</xsl:variable>
	<xsl:variable name="i18n.Tags.value">V�rde</xsl:variable>

	<xsl:variable name="i18n.AdvancedSettings">Avancerade inst�llningar</xsl:variable>
	<xsl:variable name="i18n.Query.setAsAttribute">Spara valt barn som attribut</xsl:variable>
	
	<xsl:variable name="i18n.Query.attributeName">Attributprefix</xsl:variable>
	<xsl:variable name="i18n.attributeDescription">F�ljande attribut kommer att sparas (om aktiverade ovan) med det ovan valda prefixet. Anv�ndningsexempel om du valt prefixet 'test': $attribute{test.childCitizenIdentifier}</xsl:variable>
	<xsl:variable name="i18n.attributes.name">Attributnamn</xsl:variable>
	<xsl:variable name="i18n.attributes.value">V�rde</xsl:variable>
	
	<xsl:variable name="i18n.Query.setSecondGuardianAsAttribute">Spara andra v�rdnadshavaren som attribut</xsl:variable>
	<xsl:variable name="i18n.secondGuardianAttributeDescription">F�ljande attribut kommer att sparas (om aktiverade ovan) med det ovan valda prefixet. Anv�ndningsexempel om du valt prefixet 'test': $attribute{test.guardianCitizenIdentifier}.
	Observera att saknas annan v�rdnadshavare kommer attributen inte s�ttas.</xsl:variable>
	
</xsl:stylesheet>
