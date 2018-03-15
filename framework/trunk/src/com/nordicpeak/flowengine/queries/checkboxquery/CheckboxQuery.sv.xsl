<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryCommon.sv.xsl"/>
	<xsl:include href="CheckboxQueryTemplates.xsl"/>
	
	<xsl:variable name="i18n.TooManyAlternativesSelected.part1">Du har valt för många alternativ. Du får max välja </xsl:variable>
	<xsl:variable name="i18n.TooManyAlternativesSelected.part2"> alternativ och du har valt </xsl:variable>
	<xsl:variable name="i18n.TooManyAlternativesSelected.part3"> alternativ!</xsl:variable>
	
	<xsl:variable name="i18n.TooFewAlternativesSelected.part1">Du har valt för få alternativ. Du måste välja minst </xsl:variable>
	<xsl:variable name="i18n.TooFewAlternativesSelected.part2"> alternativ och du har endast valt </xsl:variable>
	<xsl:variable name="i18n.TooFewAlternativesSelected.part3"> alternativ!</xsl:variable>
	
	<xsl:variable name="i18n.MinAlternatives.part1">Du måste välja minst</xsl:variable>
	<xsl:variable name="i18n.MinAlternatives.part2">alternativ.</xsl:variable>
	
	<xsl:variable name="i18n.MaxAlternatives.part1">Du får max välja</xsl:variable>
	<xsl:variable name="i18n.MaxAlternatives.part2">alternativ.</xsl:variable>
	
	<xsl:variable name="i18n.MinMaxAlternatives.part1">Du måste välja minst</xsl:variable>
	<xsl:variable name="i18n.MinMaxAlternatives.part2">alternativ och får max välja</xsl:variable>
	<xsl:variable name="i18n.MinMaxAlternatives.part3">alternativ.</xsl:variable>
	
	<xsl:variable name="i18n.RequiredQuery">Den här frågan är obligatorisk!</xsl:variable>
	<xsl:variable name="i18n.UnknownValidationError">Ett okänt valideringsfel har uppstått!</xsl:variable>
	
</xsl:stylesheet>
