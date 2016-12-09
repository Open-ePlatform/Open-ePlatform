<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="TextAreaQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Textareafråga</xsl:variable>
	
	<xsl:variable name="i18n.MaxLength">Tillåten längd på textinnehåll</xsl:variable>
	<xsl:variable name="i18n.maxLength">tillåten längd på textinnehåll</xsl:variable>
	
	<xsl:variable name="i18n.TextAreaQueryNotFound">Den begärda frågan hittades inte!</xsl:variable>
	<xsl:variable name="i18n.MaxLengthToBig">Maxlängden för textinnehållet kan vara högst 65535!</xsl:variable>
	
	<xsl:variable name="i18n.RowCountTooHigh">Max antal rader får högst vara 255!</xsl:variable>
	
	<xsl:variable name="i18n.Rows">Antal rader</xsl:variable>
	
	<xsl:variable name="i18n.AdvancedSettings">Avancerade inställningar</xsl:variable>
	<xsl:variable name="i18n.setAsAttribute">Spara fältets värde som attribut</xsl:variable>
	<xsl:variable name="i18n.attributeName">Attributnamn</xsl:variable>
	
	<xsl:variable name="i18n.hideDescriptionInPDF">Dölj beskrivning i PDF</xsl:variable>
	
</xsl:stylesheet>
