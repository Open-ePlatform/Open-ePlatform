<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="ContactDetailQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Kontaktuppgiftsfråga (privatperson)</xsl:variable>
	
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
	<xsl:variable name="i18n.ManagerUpdateAccess">Handläggares ändringar i kontaktuppgiftsfrågan uppdaterar användarens kontaktinformation i mina sidor</xsl:variable>
	
</xsl:stylesheet>
