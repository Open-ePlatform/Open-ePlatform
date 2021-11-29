<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="RadioButtonQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Radioknappfråga</xsl:variable>
	<xsl:variable name="java.queryDescription">
Frågetypen Radioknappfråga används när användaren skall kunna välja mellan olika alternativ. Användaren kan endast välja ett alternativ.
	</xsl:variable>
	<xsl:variable name="java.countText">Antal</xsl:variable>
	<xsl:variable name="java.alternativesText">Alternativ</xsl:variable>
	
	<xsl:variable name="i18n.RadioButtonQueryNotFound">Den begärda frågan hittades inte!</xsl:variable>
	<xsl:variable name="i18n.Searchable">Gör alternativen sökbara</xsl:variable>
	
	<xsl:variable name="i18n.AdvancedSettings">Avancerade inställningar</xsl:variable>
	<xsl:variable name="i18n.setAsAttribute">Spara fältets värde som attribut</xsl:variable>
	<xsl:variable name="i18n.attributeName">Attributnamn</xsl:variable>
	
	<xsl:variable name="i18n.Columns">Kolumner</xsl:variable>
	<xsl:variable name="i18n.Columns.One">En kolumn</xsl:variable>
	<xsl:variable name="i18n.Columns.Two">Två kolumner</xsl:variable>
	<xsl:variable name="i18n.Columns.Three">Tre kolumner</xsl:variable>
	
	<xsl:variable name="i18n.HideTitle">Dölj rubrik</xsl:variable>
	<xsl:variable name="i18n.hideDescriptionInPDF">Dölj beskrivning i PDF</xsl:variable>
	<xsl:variable name="i18n.Query.lockForManagerUpdate">Lås fråga vid ändring av handläggare</xsl:variable>
	<xsl:variable name="i18n.Query.lockForOwnerUpdate">Lås fråga för ärendets ägare vid komplettering</xsl:variable>
	
</xsl:stylesheet>
