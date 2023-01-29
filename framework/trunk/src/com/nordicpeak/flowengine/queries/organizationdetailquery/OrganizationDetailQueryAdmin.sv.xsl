<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="OrganizationDetailQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.exportOrganizationName">Organisationsnamn</xsl:variable>
	<xsl:variable name="java.exportOrganizationNumber">Organisationsnummer</xsl:variable>
	<xsl:variable name="java.exportContactFirstName">Kontaktpersonsförnamn</xsl:variable>
	<xsl:variable name="java.exportContactLastName">Kontaktpersonsefternamn</xsl:variable>
	<xsl:variable name="java.exportContactCitizenIdentifier">Kontaktpersonspersonnummer</xsl:variable>
	<xsl:variable name="java.exportAddress">Adress</xsl:variable>
	<xsl:variable name="java.exportZipCode">Postnummer</xsl:variable>
	<xsl:variable name="java.exportPostalAddress">Ort</xsl:variable>
	<xsl:variable name="java.exportPhone">Telefon</xsl:variable>
	<xsl:variable name="java.exportEmail">E-post</xsl:variable>
	<xsl:variable name="java.exportMobilePhone">Mobiltelefon</xsl:variable>
	
	<xsl:variable name="java.queryTypeName">Kontaktuppgiftsfråga (organisation)</xsl:variable>
	<xsl:variable name="java.queryDescription">
Frågetypen Kontaktuppgiftsfråga (organisation) är en skräddarsydd frågetyp när användaren skall ange sina kontaktuppgifter manuellt samt ange uppgifter om en organisation. 
Användaren har även möjlighet att välja att spara inmatade uppgifter under Mina organisationer på Mina sidor.
	</xsl:variable>

	<xsl:variable name="i18n.Field.Hidden">Dolt</xsl:variable>
	<xsl:variable name="i18n.Field.Visible">Valfritt</xsl:variable>
	<xsl:variable name="i18n.Field.Required">Obligatoriskt</xsl:variable>
	<xsl:variable name="i18n.Fields">Fält</xsl:variable>
	<xsl:variable name="i18n.Address">Postadress</xsl:variable>
	<xsl:variable name="i18n.MobilePhone">Mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.Phone">Telefon</xsl:variable>
	<xsl:variable name="i18n.Email">E-postadress</xsl:variable>
	
	<xsl:variable name="i18n.maxLength">tillåten längd på textinnehåll</xsl:variable>
	
	<xsl:variable name="i18n.OrganizationDetailQueryNotFound">Den begärda frågan hittades inte!</xsl:variable>
	
	<xsl:variable name="i18n.HideNotificationChannelSettings">Dölj notifikationsinställningarna</xsl:variable>
	<xsl:variable name="i18n.AllowSMS">Tillåt notifieringar via SMS</xsl:variable>
	
	<xsl:variable name="i18n.ContactChannelSettings">Inställningar</xsl:variable>
	
	<xsl:variable name="i18n.RequireEmailOrMobile">Kräv antingen e-post eller mobiltelefon</xsl:variable>
	
	<xsl:variable name="i18n.ValidateZipCode">Validera formatet på postnummer (XXX XX eller XXXXX)</xsl:variable>

	<xsl:variable name="i18n.AdvancedSettings">Avancerade inställningar</xsl:variable>
	<xsl:variable name="i18n.setAsAttribute">Spara värden som attribut</xsl:variable>
	<xsl:variable name="i18n.attributeName">Attributprefix</xsl:variable>
	<xsl:variable name="i18n.attributeDescription">Följande attribut kommer att sparas (om aktiverade ovan) med det ovan valda prefixet. Användningsexempel om du valt prefixet 'test': $attribute{test.citizenIdentifier}</xsl:variable>
	<xsl:variable name="i18n.GeneralContactAttributeInfo">Notera att attribut utan prefix alltid sparas från den sista ifyllda Kontaktuppgiftsfrågan (privatperson eller organisation) i e-tjänsten. Undantag är om kontaktuppgifter under mina uppgifter uppdateras. Ex $attribute{citizenIdentifier}</xsl:variable>
	<xsl:variable name="i18n.attributes.name">Attributnamn</xsl:variable>
	<xsl:variable name="i18n.attributes.value">Värde</xsl:variable>
	
</xsl:stylesheet>