<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="OrganizationDetailQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.exportOrganizationName">Organisationsnamn</xsl:variable>
	<xsl:variable name="java.exportOrganizationNumber">Organisationsnummer</xsl:variable>
	<xsl:variable name="java.exportContactFirstName">Kontaktpersonsförnamn</xsl:variable>
	<xsl:variable name="java.exportContactLastName">Kontaktpersonsefternamn</xsl:variable>
	<xsl:variable name="java.exportAddress">Adress</xsl:variable>
	<xsl:variable name="java.exportZipCode">Postnummer</xsl:variable>
	<xsl:variable name="java.exportPostalAddress">Ort</xsl:variable>
	<xsl:variable name="java.exportPhone">Telefon</xsl:variable>
	<xsl:variable name="java.exportEmail">E-post</xsl:variable>
	<xsl:variable name="java.exportMobilePhone">Mobiltelefon</xsl:variable>
	
	<xsl:variable name="java.queryTypeName">Kontaktuppgiftsfråga (organisation)</xsl:variable>
	
	<xsl:variable name="i18n.maxLength">tillåten längd på textinnehåll</xsl:variable>
	
	<xsl:variable name="i18n.OrganizationDetailQueryNotFound">Den begärda frågan hittades inte!</xsl:variable>
	
	<xsl:variable name="i18n.AllowSMS">Tillåt notifieringar via SMS</xsl:variable>
	
	<xsl:variable name="i18n.ContactChannelSettings">Inställningar</xsl:variable>
	
	<xsl:variable name="i18n.RequireAddress">Kräv postadress</xsl:variable>
	
</xsl:stylesheet>