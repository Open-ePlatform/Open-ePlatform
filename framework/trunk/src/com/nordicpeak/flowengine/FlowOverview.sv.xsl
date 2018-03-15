<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:include href="FlowOverviewTemplates.xsl"/>
	
	<xsl:variable name="i18n.AuthenticationRequired">Denna e-tjänst kräver inloggning.</xsl:variable>
	
	<xsl:variable name="i18n.ChecklistTitle">Följande behövs för e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.StartFlow">Starta e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.StepDescriptionTitle">Du kommer att gå igenom följande steg</xsl:variable>	
	
	<xsl:variable name="i18n.Questions">Frågor om e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.QuestionsContact">Kontaktperson</xsl:variable>
	<xsl:variable name="i18n.Responsible">Personuppgiftsansvarig</xsl:variable>
	<xsl:variable name="i18n.SendMailTo">Skicka e-post till</xsl:variable>
	<xsl:variable name="i18n.CallNumber">Ring nummer</xsl:variable>
	<xsl:variable name="i18n.MostPopular">Populärast</xsl:variable>
	<xsl:variable name="i18n.Uncategorized">Övriga</xsl:variable>
	<xsl:variable name="i18n.NoFlowsFound">Inga publicerade e-tjänster hittades.</xsl:variable>
	<xsl:variable name="i18n.MostUsedFLows">Mest använda e-tjänsterna</xsl:variable>
	<xsl:variable name="i18n.AllFlows">Alla e-tjänster</xsl:variable>
	<xsl:variable name="i18n.ShowAll">Visa alla</xsl:variable>

</xsl:stylesheet>
