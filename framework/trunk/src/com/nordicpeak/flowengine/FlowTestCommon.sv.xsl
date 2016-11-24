<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="BaseFlowBrowserModule.sv.xsl"/>
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:include href="FlowTestCommonTemplates.xsl"/>
	
	<xsl:variable name="i18n.AuthenticationRequired">Denna e-tjänst kräver inloggning.</xsl:variable>
	<xsl:variable name="i18n.Questions">Frågor om e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.Responsible">Personuppgiftsansvarig</xsl:variable>
	<xsl:variable name="i18n.SendMailTo">Skicka mail till</xsl:variable>
	
	<xsl:variable name="i18n.ChecklistTitle">Följande behövs för e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.StartFlow">Starta e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.StepDescriptionTitle">Du kommer gå igenom följande steg</xsl:variable>
	<xsl:variable name="i18n.preview">Förhandsgranska</xsl:variable>
	
</xsl:stylesheet>