<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="CheckboxPaymentQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Betalningsfråga - Kryssrutor</xsl:variable>
	<xsl:variable name="java.countText">Antal</xsl:variable>
	<xsl:variable name="java.alternativesText">Alternativ</xsl:variable>
	
	<xsl:variable name="i18n.CheckboxQueryNotFound">Den begärda frågan hittades inte!</xsl:variable>
	<xsl:variable name="i18n.MinChecked">Minst antal valda alternativ</xsl:variable>
	<xsl:variable name="i18n.MaxChecked">Max antal valda alternativ</xsl:variable>
	<xsl:variable name="i18n.minChecked">minst antal valda alternativ</xsl:variable>
	<xsl:variable name="i18n.maxChecked">max antal valda alternativ</xsl:variable>
	<xsl:variable name="i18n.MinCheckedBiggerThanMaxChecked">Minst antal valda alternativ får inte vara större än max!</xsl:variable>
	<xsl:variable name="i18n.MaxCheckedToBig">Max antal valda alternativ får inte överstiga antalet alternativ!</xsl:variable>
	<xsl:variable name="i18n.MinCheckedToBig">Minst antal valda alternativ får inte överstiga antalet alternativ!</xsl:variable>
	
	<xsl:variable name="i18n.TooFewAlternatives1Min">Du behöver skapa minst 1 alternativ för frågan!</xsl:variable>
	
</xsl:stylesheet>
