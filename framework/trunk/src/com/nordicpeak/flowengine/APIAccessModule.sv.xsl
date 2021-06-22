<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="classpath://se/unlogic/hierarchy/core/utils/usergrouplist/UserGroupList.sv.xsl"/>
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="APIAccessModuleTemplates.xsl"/>
	
	<!-- Module XSL variables  -->
	<xsl:variable name="java.extensionViewTitle">API �tkomstinst�llningar</xsl:variable>
	<xsl:variable name="java.settingsUpdated">Uppdaterade API �tkomstinst�llningar</xsl:variable>
	<xsl:variable name="java.settingsRemoved">Inaktiverade API �tkomst</xsl:variable>
	
	<!-- Validation -->
	<xsl:variable name="i18n.Validation.requiredField">Du m�ste fylla i f�ltet:</xsl:variable>
	<xsl:variable name="i18n.Validation.invalidFormat">Felaktigt format p� f�ltet:</xsl:variable>
	<xsl:variable name="i18n.Validation.tooShort">F�r kort inneh�ll i f�ltet:</xsl:variable>
	<xsl:variable name="i18n.Validation.tooLong">F�r l�ngt inneh�ll i f�ltet:</xsl:variable>
	<xsl:variable name="i18n.Validation.unknownError">Ok�nt fel p� f�ltet:</xsl:variable>
	<xsl:variable name="i18n.Validation.unknownFault">Ett ok�nt valideringsfel har uppst�tt.</xsl:variable>
	
	<xsl:variable name="i18n.Update">�ndra inst�llningar</xsl:variable>
	<xsl:variable name="i18n.Update.title">�ndra API �tkomstinst�llningar f�r </xsl:variable>
	<xsl:variable name="i18n.Update.description">Har kan du v�lja vilka API anv�ndare som ska f� �tkomst till den h�r e-tj�nsten via API:erna. Om du inte kan hitta den du vill s� beh�ver en administrat�r l�gga till den anv�ndaren i gruppen API anv�ndare.</xsl:variable>
	<xsl:variable name="i18n.Update.submit">Spara inst�llningar</xsl:variable>
	<xsl:variable name="i18n.Back">Bak�t</xsl:variable>
	
	<xsl:variable name="i18n.Settings.allowedUsers">Anv�ndare med API �tkomst</xsl:variable>
	<xsl:variable name="i18n.Settings.allowedUsers.empty">Inga anv�ndare har API �tkomst till den h�r e-tj�nsten.</xsl:variable>
	
</xsl:stylesheet>
