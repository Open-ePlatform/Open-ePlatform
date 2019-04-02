<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="PUDQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Ange adress eller fastighetsbeteckning</xsl:variable>
	<xsl:variable name="java.lmUserSettingName">Användare Lantmäteriet</xsl:variable>
	<xsl:variable name="java.lmUserSettingDescription">Användare som används för anrop mot lantmäteriet via Search LM</xsl:variable>
	<xsl:variable name="java.searchPrefixSettingName">Sökprefix</xsl:variable>
	<xsl:variable name="java.searchPrefixSettingDescription">Sökprefix som används vid anrop mot lantmäteriet via Serch LM</xsl:variable>
	<xsl:variable name="java.isPopulatedAlternativeName">Fastighet eller address vald</xsl:variable>
	<xsl:variable name="java.isNotPopulatedAlternativeName">Ej ifylld</xsl:variable>
	
	<xsl:variable name="i18n.PUDQueryNotFound">Den begärda frågan hittades inte!</xsl:variable>
	<xsl:variable name="i18n.AllowedSearchServices">Söktjänster</xsl:variable>
	<xsl:variable name="i18n.AllowedSearchServicesDescription">Välj vilka söktjänster som skall finnas tillgänglig för den här frågan</xsl:variable>
	<xsl:variable name="i18n.NoSearchService">Du måste välja minst en söktjänst</xsl:variable>
	
	<xsl:variable name="i18n.PUD">Sök via fastighetsbeteckning</xsl:variable>
	<xsl:variable name="i18n.Address">Sök via adress</xsl:variable>
	<xsl:variable name="i18n.useAddressAsResult">Använd adress som resultat</xsl:variable>
	
	<xsl:variable name="i18n.AdvancedSettings">Avancerade inställningar</xsl:variable>
	<xsl:variable name="i18n.setAsAttribute">Spara fältets värde som attribut</xsl:variable>
	<xsl:variable name="i18n.attributeName">Attributnamn</xsl:variable>
	
	<xsl:variable name="i18n.attributeDescription">Följande attribut kommer att sparas med det ovan valda prefixet. Användningsexempel om du valt prefixet 'test': $attribute{test.address}</xsl:variable>
	<xsl:variable name="i18n.attributes.name">Attributnamn</xsl:variable>
	<xsl:variable name="i18n.attributes.value">Värde</xsl:variable>
	
	<xsl:variable name="i18n.attributes.legacy">Fastighetsbeteckning eller adress om valt som resultat ovan</xsl:variable>
	<xsl:variable name="i18n.attributes.address">Adress</xsl:variable>
	<xsl:variable name="i18n.attributes.propertyUnitDesignation">Fastighetsbeteckning</xsl:variable>
	<xsl:variable name="i18n.attributes.propertyUnitDesignationNoMunicipality">Fastighetsbeteckning utan kommunnamn</xsl:variable>
	<xsl:variable name="i18n.attributes.propertyObjectIdentity">Fastighetens objektsidentitet</xsl:variable>
	
</xsl:stylesheet>
