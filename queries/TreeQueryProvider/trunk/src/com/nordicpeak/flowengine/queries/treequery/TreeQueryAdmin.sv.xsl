<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:import href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>

	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="TreeQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Tr�dfr�ga</xsl:variable>
	<xsl:variable name="java.missingTreeProvider">Tidigare vald tr�dleverant�r som saknas</xsl:variable>
	
	<xsl:variable name="i18n.UpdateQuery">�ndra fr�ga</xsl:variable>
	<xsl:variable name="i18n.ProviderIdentifier">Tr�dleverant�r</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara �ndringar</xsl:variable>
	<xsl:variable name="i18n.TreeQueryNotFound">Den beg�rda fr�gan hittades inte</xsl:variable>
	
	<xsl:variable name="i18n.OnlyAllowSelectingLeafs">Till�t endast val av l�v, dvs spetsen p� en gren.</xsl:variable>
	
</xsl:stylesheet>
