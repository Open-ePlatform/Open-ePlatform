<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:include href="DropDownQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Rullistafråga</xsl:variable>
		<xsl:variable name="java.countText">Antal</xsl:variable>
	<xsl:variable name="java.alternativesText">Alternativ</xsl:variable>
	
	<xsl:variable name="i18n.ShortDescription">Kort beskrivning</xsl:variable>
	<xsl:variable name="i18n.shortDescription">kort beskrivning</xsl:variable>
	<xsl:variable name="i18n.DropDownQueryNotFound">Den begärda frågan hittades inte!</xsl:variable>
	
</xsl:stylesheet>
