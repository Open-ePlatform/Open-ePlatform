<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:include href="DropDownQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Rullistafråga</xsl:variable>
	<xsl:variable name="java.queryDescription">
Frågetypen Rullistafråga används när användaren skall kunna välja mellan olika alternativ. Användaren kan endast välja ett alternativ.
	</xsl:variable>
	
	<xsl:variable name="java.countText">Antal</xsl:variable>
	<xsl:variable name="java.alternativesText">Alternativ</xsl:variable>
	
	<xsl:variable name="i18n.ShortDescription">Kort beskrivning</xsl:variable>
	<xsl:variable name="i18n.shortDescription">kort beskrivning</xsl:variable>
	<xsl:variable name="i18n.DropDownQueryNotFound">Den begärda frågan hittades inte!</xsl:variable>
	<xsl:variable name="i18n.lockOnOwnershipTransfer">Lås fråga vid överlåtelse</xsl:variable>
	<xsl:variable name="i18n.Searchable">Gör fältet sökbart</xsl:variable>
	<xsl:variable name="i18n.FormatValidator">Validator</xsl:variable>
	<xsl:variable name="i18n.InvalidFormatMessage">Valideringsmeddelande</xsl:variable>
	<xsl:variable name="i18n.invalidFormatMessage">valideringsmeddelande</xsl:variable>
	
	<xsl:variable name="i18n.AdvancedSettings">Avancerade inställningar</xsl:variable>
	<xsl:variable name="i18n.setAsAttribute">Spara fältets värde som attribut</xsl:variable>
	<xsl:variable name="i18n.attributeName">Attributnamn</xsl:variable>	
	
</xsl:stylesheet>
