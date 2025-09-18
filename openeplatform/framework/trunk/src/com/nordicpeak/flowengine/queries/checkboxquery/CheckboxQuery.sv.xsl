<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="classpath://se/unlogic/hierarchy/core/utils/xsl/CommonErrors.sv.xsl"/>
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryCommon.sv.xsl"/>
	<xsl:include href="CheckboxQueryTemplates.xsl"/>
	
	<xsl:variable name="i18n.CheckAllBoxes">Markera alla</xsl:variable>
	
	<xsl:variable name="i18n.TooManyAlternativesSelected.part1">Du har valt f�r m�nga alternativ. Du f�r max v�lja </xsl:variable>
	<xsl:variable name="i18n.TooManyAlternativesSelected.part2"> alternativ och du har valt </xsl:variable>
	<xsl:variable name="i18n.TooManyAlternativesSelected.part3"> alternativ!</xsl:variable>
	
	<xsl:variable name="i18n.TooFewAlternativesSelected.part1">Du har valt f�r f� alternativ. Du m�ste v�lja minst </xsl:variable>
	<xsl:variable name="i18n.TooFewAlternativesSelected.part2"> alternativ och du har endast valt </xsl:variable>
	<xsl:variable name="i18n.TooFewAlternativesSelected.part3"> alternativ!</xsl:variable>
	
	<xsl:variable name="i18n.MinAlternatives.part1">Du m�ste v�lja minst</xsl:variable>
	<xsl:variable name="i18n.MinAlternatives.part2">alternativ.</xsl:variable>
	
	<xsl:variable name="i18n.MaxAlternatives.part1">Du f�r max v�lja</xsl:variable>
	<xsl:variable name="i18n.MaxAlternatives.part2">alternativ.</xsl:variable>
	
	<xsl:variable name="i18n.MinMaxAlternatives.part1">Du m�ste v�lja minst</xsl:variable>
	<xsl:variable name="i18n.MinMaxAlternatives.part2">alternativ och f�r max v�lja</xsl:variable>
	<xsl:variable name="i18n.MinMaxAlternatives.part3">alternativ.</xsl:variable>
	
	<xsl:variable name="i18n.InvalidFormat">Inneh�llet i det h�r f�ltet har ett ogiltigt format!</xsl:variable>
	<xsl:variable name="i18n.RequiredQuery">Den h�r fr�gan �r obligatorisk, du m�ste v�lja minst ett alternativ.</xsl:variable>
	
</xsl:stylesheet>
