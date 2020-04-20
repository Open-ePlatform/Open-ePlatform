<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:import href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>

	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="MultiTreeQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Tr�dfr�ga (flerval)</xsl:variable>
	<xsl:variable name="java.missingTreeProvider">Tidigare vald tr�dleverant�r som saknas</xsl:variable>
	<xsl:variable name="java.selectedAlternativeName">N�got valt</xsl:variable>
	
	<xsl:variable name="i18n.UpdateQuery">�ndra fr�ga</xsl:variable>
	<xsl:variable name="i18n.ProviderIdentifier">Tr�dleverant�r</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara �ndringar</xsl:variable>
	<xsl:variable name="i18n.MultiTreeQueryNotFound">Den beg�rda fr�gan hittades inte</xsl:variable>
	
	<xsl:variable name="i18n.OnlyAllowSelectingLeafs">Till�t endast val av l�v, dvs spetsen p� en gren.</xsl:variable>
	
	<xsl:variable name="i18n.AdvancedSettings">Avancerade inst�llningar</xsl:variable>
	<xsl:variable name="i18n.Query.setAsAttribute">Spara f�ltens v�rde som attribut</xsl:variable>
	<xsl:variable name="i18n.Query.attributeName">Attributprefix</xsl:variable>
	<xsl:variable name="i18n.Query.previewMode">F�rhandsgranskningsl�ge</xsl:variable>
	<xsl:variable name="i18n.Query.previewMode.Tree">Tr�d</xsl:variable>
	<xsl:variable name="i18n.Query.previewMode.List">Lista</xsl:variable>
	
	<xsl:variable name="i18n.attributeDescription">F�ljande attribut kommer att sparas (om aktiverade ovan) kommaseparerade med det ovan valda prefixet. Anv�ndings exempel om du valt prefixet 'test': $attribute{test.ID}</xsl:variable>
	<xsl:variable name="i18n.attributes.name">Attributnamn</xsl:variable>
	<xsl:variable name="i18n.attributes.value">V�rde</xsl:variable>
	<xsl:variable name="i18n.attribute.ID">ID</xsl:variable>
	<xsl:variable name="i18n.attribute.Name">Namn</xsl:variable>
	
</xsl:stylesheet>