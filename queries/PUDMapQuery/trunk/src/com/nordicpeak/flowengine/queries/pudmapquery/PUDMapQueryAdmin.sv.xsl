<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/basemapquery/BaseMapQueryCommon.sv.xsl"/>
	<xsl:include href="PUDMapQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Markera punkt på karta</xsl:variable>
	
	<xsl:variable name="i18n.MinimumScale">Minsta skala för att markera punkt på kartan</xsl:variable>
	<xsl:variable name="i18n.minimumScale">minsta skala för att markera punkt på kartan</xsl:variable>
	
	<xsl:variable name="i18n.PUDMapQueryNotFound">Den begärda frågan hittades inte!</xsl:variable>
	
	<xsl:variable name="i18n.AdvancedSettings">Avancerade inställningar</xsl:variable>
	<xsl:variable name="i18n.setAsAttribute">Spara fältets värde som attribut</xsl:variable>
	<xsl:variable name="i18n.attributeName">Attributnamn</xsl:variable>
	
	<xsl:variable name="i18n.attributeDescription">Följande attribut kommer att sparas med det ovan valda prefixet. Användings exempel om du valt prefixet 'test': $attribute{test.address}</xsl:variable>
	<xsl:variable name="i18n.attributes.name">Attributnamn</xsl:variable>
	<xsl:variable name="i18n.attributes.value">Värde</xsl:variable>
	
	<xsl:variable name="i18n.attributes.propertyObjectIdentity">Fastighetens objektsidentitet</xsl:variable>
	<xsl:variable name="i18n.attributes.propertyUnitDesignation">Fastighetsbeteckning</xsl:variable>
	<xsl:variable name="i18n.attributes.propertyUnitDesignationNoMunicipality">Fastighetsbeteckning utan kommunnamn</xsl:variable>
	
</xsl:stylesheet>
