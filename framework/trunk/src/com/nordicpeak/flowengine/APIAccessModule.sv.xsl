<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="classpath://se/unlogic/hierarchy/core/utils/usergrouplist/UserGroupList.sv.xsl"/>
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="APIAccessModuleTemplates.xsl"/>
	
	<!-- Module XSL variables  -->
	<xsl:variable name="java.extensionViewTitle">API åtkomstinställningar</xsl:variable>
	<xsl:variable name="java.settingsUpdated">Uppdaterade API åtkomstinställningar</xsl:variable>
	<xsl:variable name="java.settingsRemoved">Inaktiverade API åtkomst</xsl:variable>
	
	<!-- Validation -->
	<xsl:variable name="i18n.Validation.requiredField">Du måste fylla i fältet:</xsl:variable>
	<xsl:variable name="i18n.Validation.invalidFormat">Felaktigt format på fältet:</xsl:variable>
	<xsl:variable name="i18n.Validation.tooShort">För kort innehåll i fältet:</xsl:variable>
	<xsl:variable name="i18n.Validation.tooLong">För långt innehåll i fältet:</xsl:variable>
	<xsl:variable name="i18n.Validation.unknownError">Okänt fel på fältet:</xsl:variable>
	<xsl:variable name="i18n.Validation.unknownFault">Ett okänt valideringsfel har uppstått.</xsl:variable>
	
	<xsl:variable name="i18n.Update">Ändra inställningar</xsl:variable>
	<xsl:variable name="i18n.Update.title">Ändra API åtkomstinställningar för </xsl:variable>
	<xsl:variable name="i18n.Update.description">Har kan du välja vilka API användare som ska få åtkomst till den här e-tjänsten via API:erna. Om du inte kan hitta den du vill så behöver en administratör lägga till den användaren i gruppen API användare.</xsl:variable>
	<xsl:variable name="i18n.Update.submit">Spara inställningar</xsl:variable>
	<xsl:variable name="i18n.Back">Bakåt</xsl:variable>
	
	<xsl:variable name="i18n.Settings.allowedUsers">Användare med API åtkomst</xsl:variable>
	<xsl:variable name="i18n.Settings.allowedUsers.empty">Inga användare har API åtkomst till den här e-tjänsten.</xsl:variable>
	
</xsl:stylesheet>
