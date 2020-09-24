<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="ContactDetailQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Kontaktuppgiftsfråga (privatperson)</xsl:variable>
	<xsl:variable name="java.queryDescription">
Frågetypen Kontaktuppgiftsfråga (privatperson) används för att samla in uppgifter om användaren. Under frågans inställningar är det möjligt att välja vilka uppgifter som skall samlas in
och vilka som är valfria respektive obligatoriska. I det fall installationen har koppling till Navet folkbokföring och användaren är inloggad förifylls användarens folkbokföringsadress.
Om användaren har skyddad identitet hämtas inga folkbokföringsuppgifter.
	</xsl:variable>
	
	<xsl:variable name="java.exportFirstName">Förnamn</xsl:variable>
	<xsl:variable name="java.exportLastName">Efternamn</xsl:variable>
	<xsl:variable name="java.exportCareOf">C/O</xsl:variable>
	<xsl:variable name="java.exportAddress">Adress</xsl:variable>
	<xsl:variable name="java.exportZipCode">Postnummer</xsl:variable>
	<xsl:variable name="java.exportPostalAddress">Ort</xsl:variable>
	<xsl:variable name="java.exportPhone">Telefon</xsl:variable>
	<xsl:variable name="java.exportEmail">E-post</xsl:variable>
	<xsl:variable name="java.exportMobilePhone">Mobiltelefon</xsl:variable>
	<xsl:variable name="java.exportCitizenID">Personnummer</xsl:variable>
	
	<xsl:variable name="i18n.maxLength">tillåten längd på textinnehåll</xsl:variable>
	
	<xsl:variable name="i18n.ContactDetailQueryNotFound">Den begärda frågan hittades inte!</xsl:variable>
	
	<xsl:variable name="i18n.HideNotificationChannelSettings">Dölj notifikationsinställningarna</xsl:variable>
	<xsl:variable name="i18n.AllowSMS">Tillåt notifieringar via SMS</xsl:variable>
	
	<xsl:variable name="i18n.ContactChannelSettings">Inställningar</xsl:variable>
	
	<xsl:variable name="i18n.RequireAddress">Kräv postadress</xsl:variable>
	
	<xsl:variable name="i18n.ShowCitizenIDField">Visa fält för personnummer</xsl:variable>
	<xsl:variable name="i18n.RequireEmail">Kräv e-post</xsl:variable>
	<xsl:variable name="i18n.RequirePhone">Kräv telefon</xsl:variable>
	<xsl:variable name="i18n.RequireMobilePhone">Kräv mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.OfficalAddress">Lås postadressfälten till folkbokföringsadressen om denna finns tillgänglig</xsl:variable>
	<xsl:variable name="i18n.DisableProfileUpdate">Dölj kryssruta för uppdatering av kontaktuppgifter</xsl:variable>
	<xsl:variable name="i18n.RequireAtLeastOneContactWay">Kräv antingen e-post eller mobiltelefon</xsl:variable>
	
	<xsl:variable name="i18n.Fields">Fält</xsl:variable>
	<xsl:variable name="i18n.Field.Hidden">Dolt</xsl:variable>
	<xsl:variable name="i18n.Field.Visible">Valfritt</xsl:variable>
	<xsl:variable name="i18n.Field.Required">Obligatoriskt</xsl:variable>
	
	<xsl:variable name="i18n.CitizenID">Personnummer</xsl:variable>
	<xsl:variable name="i18n.FirstAndLastname">Namn</xsl:variable>
	<xsl:variable name="i18n.Address">Postadress</xsl:variable>
	<xsl:variable name="i18n.MobilePhone">Mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.Phone">Telefon</xsl:variable>
	<xsl:variable name="i18n.Email">E-postadress</xsl:variable>
	<xsl:variable name="i18n.CareOf">C/O</xsl:variable>
	
	<xsl:variable name="i18n.ProfileUpdate">Uppdatera kontaktuppgifter under mina uppgifter</xsl:variable>
	<xsl:variable name="i18n.FieldUpdate.Always">Alltid</xsl:variable>
	<xsl:variable name="i18n.FieldUpdate.Ask">Valfritt</xsl:variable>
	<xsl:variable name="i18n.FieldUpdate.Never">Aldrig</xsl:variable>
	<xsl:variable name="i18n.ManagerUpdateAccess">Uppdatera kontaktuppgifter under mina uppgifter när handläggare ändrar i ärenden</xsl:variable>
	
	<xsl:variable name="i18n.AdvancedSettings">Avancerade inställningar</xsl:variable>
	<xsl:variable name="i18n.setAsAttribute">Spara värden som attribut</xsl:variable>
	<xsl:variable name="i18n.attributeName">Attributprefix</xsl:variable>
	<xsl:variable name="i18n.attributeDescription">Följande attribut kommer att sparas (om aktiverade ovan) med det ovan valda prefixet. Användningsexempel om du valt prefixet 'test': $attribute{test.citizenIdentifier}</xsl:variable>
	<xsl:variable name="i18n.GeneralContactAttributeInfo">Notera att attribut utan prefix alltid sparas från den sista ifyllda Kontaktuppgiftsfrågan (privatperson eller organisation) i e-tjänsten. Undantag är om kontaktuppgifter under mina uppgifter uppdateras. Ex $attribute{citizenIdentifier}</xsl:variable>
	<xsl:variable name="i18n.attributes.name">Attributnamn</xsl:variable>
	<xsl:variable name="i18n.attributes.value">Värde</xsl:variable>
	
</xsl:stylesheet>
