<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:import href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>

	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="ChildQueryAdminTemplates.xsl"/>
	<xsl:variable name="java.queryDescription">
Frågetypen Val av barn hämtar uppgifter om de barn den inloggade användaren är vårdnadshavare för och kräver att ett barn väljs om frågetypen är satt som obligatorisk. Detta kräver en koppling mot Skatteverket (Navet).
Frågetypen kan även hämta in uppgifter om övriga vårdnadshavare för barnet.
	</xsl:variable>
	<xsl:variable name="java.queryTypeName">Val av barn</xsl:variable>
	<xsl:variable name="java.testChildrenMenuName">Testbarn</xsl:variable>
	
	<xsl:variable name="java.childSelectedAlternativeName">Barn valt</xsl:variable>
	<xsl:variable name="java.singleGuardianAlternativeName">Ensam vårdnad</xsl:variable>
	<xsl:variable name="java.multiGuardianAlternativeName">Gemensam  vårdnad</xsl:variable>
	
	<xsl:variable name="java.exportChildCitizenName">Barnets namn</xsl:variable>
	<xsl:variable name="java.exportChildCitizenFirstName">Barnets förnamn</xsl:variable>
	<xsl:variable name="java.exportChildCitizenLastName">Barnets efternamn</xsl:variable>
	<xsl:variable name="java.exportChildCitizenIdentifier">Barnets personnummer</xsl:variable>
	<xsl:variable name="java.exportChildAdress">Barnets address</xsl:variable>
	<xsl:variable name="java.exportChildPostalAdress">Barnets postort</xsl:variable>
	<xsl:variable name="java.exportChildZipCode">Barnets postnummer</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianName">Andra vårdnadshavarens namn</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianFirstname">Andra vårdnadshavarens förnamn</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianLastname">Andra vårdnadshavarens efternamn</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianEmail">Andra vårdnadshavarens e-post</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianPhone">Andra vårdnadshavarens telefon</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianCitizenIdentifier">Andra vårdnadshavarens personnummer</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianAdress">Andra vårdnadshavarens address</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianPostalAdress">Andra vårdnadshavarens postort</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianZipCode">Andra vårdnadshavarens postnummer</xsl:variable>
	
	<xsl:variable name="i18n.UpdateQuery">Ändra fråga</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara ändringar</xsl:variable>
	<xsl:variable name="i18n.ChildQueryNotFound">Den begärda frågan hittades inte</xsl:variable>
	
	<xsl:variable name="i18n.OtherGuardiansDescription">Beskrivning för andra vårdnadshavare</xsl:variable>
	<xsl:variable name="i18n.UseMultipartSigning">Använd flerpartssignering</xsl:variable>
	<xsl:variable name="i18n.Query.skipMultipartSigningIfSameAddress">Om båda vårdnadshavarna är folkbokförda på samma adress behövs endast sökandes signering</xsl:variable>
	<xsl:variable name="i18n.Query.skipMultipartSigningIfSameAddress.warning">Systemet hanterar ej folkbokföringsadresser som är inskrivna på olika sätt hos Skatteverket och i dessa fall måste båda vårdnadshavarna signera ansökan.</xsl:variable>
	<xsl:variable name="i18n.AlwaysShowOtherGuardians">Visa alltid andra vårdnadshavare</xsl:variable>
	<xsl:variable name="i18n.ShowAddress">Visa folkbokföringsadress för barn</xsl:variable>
	<xsl:variable name="i18n.ShowGuardianAddress">Visa folkbokföringsaddress för vårdnadshavare</xsl:variable>
	<xsl:variable name="i18n.RequiredContactWays">Krävda kontaktvägar för andra vårdnadshavare</xsl:variable>
	<xsl:variable name="i18n.RequiredContactWayVerification">Kräv verifiering av kontaktvägar</xsl:variable>
	<xsl:variable name="i18n.MinChildAge">Lägsta ålder på barn (lämna tomt för ingen gräns)</xsl:variable>
	<xsl:variable name="i18n.MaxChildAge">Högsta ålder på barn (lämna tomt för ingen gräns)</xsl:variable>
	<xsl:variable name="i18n.MinAgeLargerThanMaxAge">Minsta ålder kan inte vara större än högsta ålder</xsl:variable>
	<xsl:variable name="i18n.SetMultipartsAsOwners">Sätt andra vårdnadshavaren som medsökande för ärendet</xsl:variable>
	<xsl:variable name="i18n.CommunicationErrorDescription">Meddelande om det inte går att kontakta navet eller API-källan (kan lämnas tom för att återställa standardtext)</xsl:variable>
	
	<xsl:variable name="i18n.FilterEndpoint.Attributes">Extra fält</xsl:variable>
	<xsl:variable name="i18n.FilterEndpoint.Attribute.DisplayMode">Visningsläge</xsl:variable>
	<xsl:variable name="i18n.FilterEndpoint.Attribute.DisplayMode.Always">Visa alltid</xsl:variable>
	<xsl:variable name="i18n.FilterEndpoint.Attribute.DisplayMode.IfValue">Visa om värde finns</xsl:variable>
	<xsl:variable name="i18n.FilterEndpoint.Attribute.DisplayMode.Never">Visa inte</xsl:variable>
	
	<xsl:variable name="i18n.ContactWays.Either">Antingen e-post eller mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.ContactWays.Email">E-post</xsl:variable>
	<xsl:variable name="i18n.ContactWays.Phone">Mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.ContactWays.Both">Både e-post och mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.HideSSNForOtherGuardians">Dölj personnummer på andra vårdnadshavaren</xsl:variable>
	
	<xsl:variable name="i18n.TestChildren.title">Testbarn</xsl:variable>
	<xsl:variable name="i18n.TestChildren.description.part1">Här kan du aktivera testbarn i val av barnfrågan för denna inloggning. När denna funktionen är aktiverad så hämtas inga barn från Skatteverket, istället visas en fast uppsättning testbarn. Testbarnen har åldern 0-17 år samt kommunkod 4321.</xsl:variable>
	<xsl:variable name="i18n.TestChildren.description.part2">Denna ändring slår endast igenom på nya ärenden. Om du redan har ett ärende öppet så kommer det inte påverkas. Denna inställning nollställs varje gång du loggar in och ut.</xsl:variable>
	<xsl:variable name="i18n.TestChildren.enabled">Aktivera testbarn</xsl:variable>
	
	<xsl:variable name="i18n.FilterEndpoint.title">Välj API-källa för filtrering och extra information</xsl:variable>
	<xsl:variable name="i18n.FilterEndpoint.EmptyOption">Ingen API-källa vald</xsl:variable>
	<xsl:variable name="i18n.EmptyFilterDescription">Meddelande vid avsaknad av data från API-källan (tomt svar, inga barn kvar efter filtrering eller åldersgräns används och inga barn är inom intervallet)</xsl:variable>
	<xsl:variable name="i18n.useFilteredChildrenDescription">Visa meddelande vid bortfiltrering av barn (men åtminstone 1 barn blev kvar efter filtrering)</xsl:variable>
	<xsl:variable name="i18n.FilteredChildrenDescription">Meddelande för bortfiltrerade barn</xsl:variable>
	<xsl:variable name="i18n.FilteredChildrenDescription.Tags.children">Namn på barnen som filterats bort</xsl:variable>
	<xsl:variable name="i18n.Tags">Följande taggar kan användas i texten ovan</xsl:variable>
	<xsl:variable name="i18n.Tags.name">Tagg</xsl:variable>
	<xsl:variable name="i18n.Tags.value">Värde</xsl:variable>

	<xsl:variable name="i18n.AdvancedSettings">Avancerade inställningar</xsl:variable>
	<xsl:variable name="i18n.Query.setAsAttribute">Spara valt barn som attribut</xsl:variable>
	
	<xsl:variable name="i18n.Query.attributeName">Attributprefix</xsl:variable>
	<xsl:variable name="i18n.attributeDescription">Följande attribut kommer att sparas (om aktiverade ovan) med det ovan valda prefixet. Användningsexempel om du valt prefixet 'test': $attribute{test.childCitizenIdentifier}</xsl:variable>
	<xsl:variable name="i18n.attributes.name">Attributnamn</xsl:variable>
	<xsl:variable name="i18n.attributes.value">Värde</xsl:variable>
	
	<xsl:variable name="i18n.Query.setSecondGuardianAsAttribute">Spara andra vårdnadshavaren som attribut</xsl:variable>
	<xsl:variable name="i18n.secondGuardianAttributeDescription">Följande attribut kommer att sparas (om aktiverade ovan) med det ovan valda prefixet. Användningsexempel om du valt prefixet 'test': $attribute{test.guardianCitizenIdentifier}.
	Observera att saknas annan vårdnadshavare kommer attributen inte sättas.</xsl:variable>
	
</xsl:stylesheet>
