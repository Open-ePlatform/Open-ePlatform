<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="RadioButtonQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Radioknappfr�ga</xsl:variable>
	<xsl:variable name="java.countText">Antal</xsl:variable>
	<xsl:variable name="java.alternativesText">Alternativ</xsl:variable>
	
	<xsl:variable name="i18n.RadioButtonQueryNotFound">Den beg�rda fr�gan hittades inte!</xsl:variable>
	
	<xsl:variable name="i18n.AdvancedSettings">Avancerade inst�llningar</xsl:variable>
	<xsl:variable name="i18n.setAsAttribute">Spara f�ltets v�rde som attribut</xsl:variable>
	<xsl:variable name="i18n.attributeName">Attributnamn</xsl:variable>
	
	<xsl:variable name="i18n.Columns">Kolumner</xsl:variable>
	<xsl:variable name="i18n.Columns.One">En kolumn</xsl:variable>
	<xsl:variable name="i18n.Columns.Two">Tv� kolumner</xsl:variable>
	<xsl:variable name="i18n.Columns.Three">Tre kolumner</xsl:variable>
	
</xsl:stylesheet>
