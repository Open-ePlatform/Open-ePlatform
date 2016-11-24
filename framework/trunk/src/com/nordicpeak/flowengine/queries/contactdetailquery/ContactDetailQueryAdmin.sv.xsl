<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="ContactDetailQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Kontaktuppgiftsfr�ga (privatperson)</xsl:variable>
	
	<xsl:variable name="java.exportFirstName">F�rnamn</xsl:variable>
	<xsl:variable name="java.exportLastName">Efternamn</xsl:variable>
	<xsl:variable name="java.exportAddress">Adress</xsl:variable>
	<xsl:variable name="java.exportZipCode">Postnummer</xsl:variable>
	<xsl:variable name="java.exportPostalAddress">Ort</xsl:variable>
	<xsl:variable name="java.exportPhone">Telefon</xsl:variable>
	<xsl:variable name="java.exportEmail">E-post</xsl:variable>
	<xsl:variable name="java.exportMobilePhone">Mobiltelefon</xsl:variable>
	<xsl:variable name="java.exportSocialSecurityNumber">Personnummer</xsl:variable>
	
	<xsl:variable name="i18n.maxLength">till�ten l�ngd p� textinneh�ll</xsl:variable>
	
	<xsl:variable name="i18n.ContactDetailQueryNotFound">Den beg�rda fr�gan hittades inte!</xsl:variable>
	
	<xsl:variable name="i18n.AllowSMS">Till�t notifieringar via SMS</xsl:variable>
	
	<xsl:variable name="i18n.ContactChannelSettings">Inst�llningar</xsl:variable>
	
	<xsl:variable name="i18n.RequireAddress">Kr�v postadress</xsl:variable>
	
	<xsl:variable name="i18n.ShowSocialSecurityNumberField">Visa f�lt f�r personnummer</xsl:variable>
	<xsl:variable name="i18n.RequireEmail">Kr�v e-post</xsl:variable>
	<xsl:variable name="i18n.RequirePhone">Kr�v telefon</xsl:variable>
	<xsl:variable name="i18n.RequireMobilePhone">Kr�v mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.OfficalAddress">L�s postadressf�lten till folkbokf�ringsadressen om denna finns tillg�nglig</xsl:variable>
	<xsl:variable name="i18n.DisableProfileUpdate">D�lj kryssruta f�r uppdatering av kontaktuppgifter</xsl:variable>
</xsl:stylesheet>
