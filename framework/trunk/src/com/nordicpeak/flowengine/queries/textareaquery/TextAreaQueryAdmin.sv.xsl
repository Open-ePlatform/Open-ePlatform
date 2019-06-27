<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="TextAreaQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Textareafråga</xsl:variable>
	<xsl:variable name="java.queryDescription">
Frågetypen Textareafråga används när användaren skall ange information i ett textfält och kunna skriva in flera rader.
	</xsl:variable>
	
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
	<xsl:variable name="i18n.lockOnOwnershipTransfer">Lås fråga vid överlåtelse</xsl:variable>
	<xsl:variable name="i18n.showLetterCount">Visa räknare för antal tecken som använts av maximalt antal tecken</xsl:variable>
	<xsl:variable name="i18n.keepalive">Håll användaren inloggad när användaren skriver i fältet</xsl:variable>
	
	<xsl:variable name="i18n.HideTitle">Dölj rubrik</xsl:variable>
	
</xsl:stylesheet>
