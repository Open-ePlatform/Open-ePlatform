<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="OrganizationDetailQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.exportOrganizationName">Organisationsnamn</xsl:variable>
	<xsl:variable name="java.exportOrganizationNumber">Organisationsnummer</xsl:variable>
	<xsl:variable name="java.exportContactFirstName">Kontaktpersonsf�rnamn</xsl:variable>
	<xsl:variable name="java.exportContactLastName">Kontaktpersonsefternamn</xsl:variable>
	<xsl:variable name="java.exportContactCitizenIdentifier">Kontaktpersonspersonnummer</xsl:variable>
	<xsl:variable name="java.exportAddress">Adress</xsl:variable>
	<xsl:variable name="java.exportZipCode">Postnummer</xsl:variable>
	<xsl:variable name="java.exportPostalAddress">Ort</xsl:variable>
	<xsl:variable name="java.exportPhone">Telefon</xsl:variable>
	<xsl:variable name="java.exportEmail">E-post</xsl:variable>
	<xsl:variable name="java.exportMobilePhone">Mobiltelefon</xsl:variable>
	
	<xsl:variable name="java.queryTypeName">Kontaktuppgiftsfr�ga (organisation)</xsl:variable>
	<xsl:variable name="java.queryDescription">
Fr�getypen Kontaktuppgiftsfr�ga (organisation) �r en skr�ddarsydd fr�getyp n�r anv�ndaren skall ange sina kontaktuppgifter manuellt samt ange uppgifter om en organisation. 
Anv�ndaren har �ven m�jlighet att v�lja att spara inmatade uppgifter under Mina organisationer p� Mina sidor.
	</xsl:variable>

	<xsl:variable name="i18n.Field.Hidden">Dolt</xsl:variable>
	<xsl:variable name="i18n.Field.Visible">Valfritt</xsl:variable>
	<xsl:variable name="i18n.Field.Required">Obligatoriskt</xsl:variable>
	<xsl:variable name="i18n.Fields">F�lt</xsl:variable>
	<xsl:variable name="i18n.Address">Postadress</xsl:variable>
	<xsl:variable name="i18n.MobilePhone">Mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.Phone">Telefon</xsl:variable>
	<xsl:variable name="i18n.Email">E-postadress</xsl:variable>
	
	<xsl:variable name="i18n.maxLength">till�ten l�ngd p� textinneh�ll</xsl:variable>
	
	<xsl:variable name="i18n.OrganizationDetailQueryNotFound">Den beg�rda fr�gan hittades inte!</xsl:variable>
	
	<xsl:variable name="i18n.HideNotificationChannelSettings">D�lj notifikationsinst�llningarna</xsl:variable>
	<xsl:variable name="i18n.AllowSMS">Till�t notifieringar via SMS</xsl:variable>
	
	<xsl:variable name="i18n.ContactChannelSettings">Inst�llningar</xsl:variable>
	
	<xsl:variable name="i18n.RequireEmailOrMobile">Kr�v antingen e-post eller mobiltelefon</xsl:variable>
	
	<xsl:variable name="i18n.ValidateZipCode">Validera formatet p� postnummer (XXX XX eller XXXXX)</xsl:variable>

	<xsl:variable name="i18n.AdvancedSettings">Avancerade inst�llningar</xsl:variable>
	<xsl:variable name="i18n.setAsAttribute">Spara v�rden som attribut</xsl:variable>
	<xsl:variable name="i18n.attributeName">Attributprefix</xsl:variable>
	<xsl:variable name="i18n.attributeDescription">F�ljande attribut kommer att sparas (om aktiverade ovan) med det ovan valda prefixet. Anv�ndningsexempel om du valt prefixet 'test': $attribute{test.citizenIdentifier}</xsl:variable>
	<xsl:variable name="i18n.GeneralContactAttributeInfo">Notera att attribut utan prefix alltid sparas fr�n den sista ifyllda Kontaktuppgiftsfr�gan (privatperson eller organisation) i e-tj�nsten. Undantag �r om kontaktuppgifter under mina uppgifter uppdateras. Ex $attribute{citizenIdentifier}</xsl:variable>
	<xsl:variable name="i18n.attributes.name">Attributnamn</xsl:variable>
	<xsl:variable name="i18n.attributes.value">V�rde</xsl:variable>
	
</xsl:stylesheet>