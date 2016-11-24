<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryCommon.sv.xsl"/>
	<xsl:include href="TreeQueryTemplates.xsl"/>
	
	<xsl:variable name="i18n.Error.Required">Den här frågan är obligatorisk!</xsl:variable>
	<xsl:variable name="i18n.Error.UnableToGetTrees">Det går inte att komma åt tjänsten för att hämta träddata, vänligen försök igen senare.</xsl:variable>
	<xsl:variable name="i18n.Error.UnknownValidationError">Ett okänt valideringsfel har uppstått!</xsl:variable>
	<xsl:variable name="i18n.Error.RequiredField">Det här fältet är obligatoriskt!</xsl:variable>
	<xsl:variable name="i18n.Error.InvalidFormat">Du har valt ett ogiltig alternativ, försök igen!</xsl:variable>
	
	<xsl:variable name="i18n.ChosenTree">Valt alternativ</xsl:variable>
	<xsl:variable name="i18n.Filter">Sök</xsl:variable>
	
</xsl:stylesheet>
