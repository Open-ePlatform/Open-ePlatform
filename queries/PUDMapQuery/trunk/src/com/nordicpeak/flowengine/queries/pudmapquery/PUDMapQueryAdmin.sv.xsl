<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/basemapquery/BaseMapQueryCommon.sv.xsl"/>
	<xsl:include href="PUDMapQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Markera punkt p� karta</xsl:variable>
	
	<xsl:variable name="i18n.MinimumScale">Minsta skala f�r att markera punkt p� kartan</xsl:variable>
	<xsl:variable name="i18n.minimumScale">minsta skala f�r att markera punkt p� kartan</xsl:variable>
	
	<xsl:variable name="i18n.PUDMapQueryNotFound">Den beg�rda fr�gan hittades inte!</xsl:variable>
	
	<xsl:variable name="i18n.AdvancedSettings">Avancerade inst�llningar</xsl:variable>
	<xsl:variable name="i18n.setAsAttribute">Spara f�ltets v�rde som attribut</xsl:variable>
	<xsl:variable name="i18n.attributeName">Attributnamn</xsl:variable>
	
	<xsl:variable name="i18n.attributeDescription">F�ljande attribut kommer att sparas med det ovan valda prefixet. Anv�ndings exempel om du valt prefixet 'test': $attribute{test.address}</xsl:variable>
	<xsl:variable name="i18n.attributes.name">Attributnamn</xsl:variable>
	<xsl:variable name="i18n.attributes.value">V�rde</xsl:variable>
	
	<xsl:variable name="i18n.attributes.propertyObjectIdentity">Fastighetens objektsidentitet</xsl:variable>
	<xsl:variable name="i18n.attributes.propertyUnitDesignation">Fastighetsbeteckning</xsl:variable>
	<xsl:variable name="i18n.attributes.propertyUnitDesignationNoMunicipality">Fastighetsbeteckning utan kommunnamn</xsl:variable>
	
</xsl:stylesheet>
