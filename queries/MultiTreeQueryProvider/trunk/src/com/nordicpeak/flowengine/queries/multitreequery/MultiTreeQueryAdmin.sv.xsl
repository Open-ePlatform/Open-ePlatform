<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:import href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>

	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="MultiTreeQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Trädfråga (flerval)</xsl:variable>
	<xsl:variable name="java.missingTreeProvider">Tidigare vald trädleverantör som saknas</xsl:variable>
	<xsl:variable name="java.selectedAlternativeName">Något valt</xsl:variable>
	
	<xsl:variable name="i18n.UpdateQuery">Ändra fråga</xsl:variable>
	<xsl:variable name="i18n.ProviderIdentifier">Trädleverantör</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara ändringar</xsl:variable>
	<xsl:variable name="i18n.MultiTreeQueryNotFound">Den begärda frågan hittades inte</xsl:variable>
	
	<xsl:variable name="i18n.OnlyAllowSelectingLeafs">Tillåt endast val av löv, dvs spetsen på en gren.</xsl:variable>
	
	<xsl:variable name="i18n.AdvancedSettings">Avancerade inställningar</xsl:variable>
	<xsl:variable name="i18n.Query.setAsAttribute">Spara fältens värde som attribut</xsl:variable>
	<xsl:variable name="i18n.Query.attributeName">Attributprefix</xsl:variable>
	<xsl:variable name="i18n.Query.previewMode">Förhandsgranskningsläge</xsl:variable>
	<xsl:variable name="i18n.Query.previewMode.Tree">Träd</xsl:variable>
	<xsl:variable name="i18n.Query.previewMode.List">Lista</xsl:variable>
	
	<xsl:variable name="i18n.attributeDescription">Följande attribut kommer att sparas (om aktiverade ovan) kommaseparerade med det ovan valda prefixet. Användings exempel om du valt prefixet 'test': $attribute{test.ID}</xsl:variable>
	<xsl:variable name="i18n.attributes.name">Attributnamn</xsl:variable>
	<xsl:variable name="i18n.attributes.value">Värde</xsl:variable>
	<xsl:variable name="i18n.attribute.ID">ID</xsl:variable>
	<xsl:variable name="i18n.attribute.Name">Namn</xsl:variable>
	
</xsl:stylesheet>