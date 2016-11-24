<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:import href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>

	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="TreeQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Trädfråga</xsl:variable>
	<xsl:variable name="java.missingTreeProvider">Tidigare vald trädleverantör som saknas</xsl:variable>
	
	<xsl:variable name="i18n.UpdateQuery">Ändra fråga</xsl:variable>
	<xsl:variable name="i18n.ProviderIdentifier">Trädleverantör</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara ändringar</xsl:variable>
	<xsl:variable name="i18n.TreeQueryNotFound">Den begärda frågan hittades inte</xsl:variable>
	
	<xsl:variable name="i18n.OnlyAllowSelectingLeafs">Tillåt endast val av löv, dvs spetsen på en gren.</xsl:variable>
	
</xsl:stylesheet>
