<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryCommon.sv.xsl"/>
	<xsl:include href="TreeQueryTemplates.xsl"/>
	
	<xsl:variable name="i18n.Error.Required">Den h�r fr�gan �r obligatorisk!</xsl:variable>
	<xsl:variable name="i18n.Error.UnableToGetTrees">Det g�r inte att komma �t tj�nsten f�r att h�mta tr�ddata, v�nligen f�rs�k igen senare.</xsl:variable>
	<xsl:variable name="i18n.Error.UnknownValidationError">Ett ok�nt valideringsfel har uppst�tt!</xsl:variable>
	<xsl:variable name="i18n.Error.RequiredField">Det h�r f�ltet �r obligatoriskt!</xsl:variable>
	<xsl:variable name="i18n.Error.InvalidFormat">Du har valt ett ogiltig alternativ, f�rs�k igen!</xsl:variable>
	
	<xsl:variable name="i18n.ChosenTree">Valt alternativ</xsl:variable>
	<xsl:variable name="i18n.Filter">S�k</xsl:variable>
	
</xsl:stylesheet>
