<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="ManualMultiSignQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Flerpartssigneringsfråga</xsl:variable>
	
	<xsl:variable name="java.exportFirstName">Förnamn</xsl:variable>
	<xsl:variable name="java.exportLastName">Efternamn</xsl:variable>
	<xsl:variable name="java.exportEmail">E-post</xsl:variable>
	<xsl:variable name="java.exportMobilePhone">Mobiltelefon</xsl:variable>
	<xsl:variable name="java.exportSocialSecurityNumber">Personnummer</xsl:variable>
	<xsl:variable name="i18n.hideCitizenIdetifierInPDF">Dölj personnummer i PDF</xsl:variable>
	<xsl:variable name="i18n.AdvancedSettings">Avancerade inställningar</xsl:variable>
	<xsl:variable name="i18n.SetMultipartsAsOwners">Sätt part som sökande för ärendet</xsl:variable>
	
	<xsl:variable name="i18n.ManualMultiSignQueryNotFound">Den begärda frågan hittades inte!</xsl:variable>
</xsl:stylesheet>
