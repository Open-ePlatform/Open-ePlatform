<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="ContactDetailQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Kontaktuppgiftsfråga (privatperson)</xsl:variable>
	
	<xsl:variable name="java.exportFirstName">Förnamn</xsl:variable>
	<xsl:variable name="java.exportLastName">Efternamn</xsl:variable>
	<xsl:variable name="java.exportAddress">Adress</xsl:variable>
	<xsl:variable name="java.exportZipCode">Postnummer</xsl:variable>
	<xsl:variable name="java.exportPostalAddress">Ort</xsl:variable>
	<xsl:variable name="java.exportPhone">Telefon</xsl:variable>
	<xsl:variable name="java.exportEmail">E-post</xsl:variable>
	<xsl:variable name="java.exportMobilePhone">Mobiltelefon</xsl:variable>
	<xsl:variable name="java.exportSocialSecurityNumber">Personnummer</xsl:variable>
	
	<xsl:variable name="i18n.maxLength">tillåten längd på textinnehåll</xsl:variable>
	
	<xsl:variable name="i18n.ContactDetailQueryNotFound">Den begärda frågan hittades inte!</xsl:variable>
	
	<xsl:variable name="i18n.AllowSMS">Tillåt notifieringar via SMS</xsl:variable>
	
	<xsl:variable name="i18n.ContactChannelSettings">Inställningar</xsl:variable>
	
	<xsl:variable name="i18n.RequireAddress">Kräv postadress</xsl:variable>
	
	<xsl:variable name="i18n.ShowSocialSecurityNumberField">Visa fält för personnummer</xsl:variable>
	<xsl:variable name="i18n.RequireEmail">Kräv e-post</xsl:variable>
	<xsl:variable name="i18n.RequirePhone">Kräv telefon</xsl:variable>
	<xsl:variable name="i18n.RequireMobilePhone">Kräv mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.OfficalAddress">Lås postadressfälten till folkbokföringsadressen om denna finns tillgänglig</xsl:variable>
	<xsl:variable name="i18n.DisableProfileUpdate">Dölj kryssruta för uppdatering av kontaktuppgifter</xsl:variable>
</xsl:stylesheet>
