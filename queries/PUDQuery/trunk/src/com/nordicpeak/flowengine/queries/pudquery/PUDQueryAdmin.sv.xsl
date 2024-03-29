<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="PUDQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Ange adress eller fastighetsbeteckning</xsl:variable>
	<xsl:variable name="java.queryDescription">
Fr�getypen Ange adress eller fastighetsbeteckning har koppling till Lantm�teriet och anv�nds f�r att baserat p� adress och/eller fastighetsbeteckning s�ka fram giltig fastighetsbeteckning fr�n Lantm�teriets register.
	</xsl:variable>
	<xsl:variable name="java.lmUserSettingName">Anv�ndare Lantm�teriet</xsl:variable>
	<xsl:variable name="java.lmUserSettingDescription">Anv�ndare som anv�nds f�r anrop mot lantm�teriet via Search LM</xsl:variable>
	<xsl:variable name="java.searchPrefixSettingName">S�kprefix</xsl:variable>
	<xsl:variable name="java.searchPrefixSettingDescription">S�kprefix som anv�nds vid anrop mot lantm�teriet via Serch LM</xsl:variable>
	<xsl:variable name="java.isPopulatedAlternativeName">Fastighet eller address vald</xsl:variable>
	<xsl:variable name="java.isNotPopulatedAlternativeName">Ej ifylld</xsl:variable>
	
	<xsl:variable name="i18n.PUDQueryNotFound">Den beg�rda fr�gan hittades inte!</xsl:variable>
	<xsl:variable name="i18n.AllowedSearchServices">S�ktj�nster</xsl:variable>
	<xsl:variable name="i18n.AllowedSearchServicesDescription">V�lj vilka s�ktj�nster som skall finnas tillg�nglig f�r den h�r fr�gan</xsl:variable>
	<xsl:variable name="i18n.NoSearchService">Du m�ste v�lja minst en s�ktj�nst</xsl:variable>
	
	<xsl:variable name="i18n.PUD">S�k via fastighetsbeteckning</xsl:variable>
	<xsl:variable name="i18n.Address">S�k via adress</xsl:variable>
	<xsl:variable name="i18n.useAddressAsResult">Anv�nd adress som resultat</xsl:variable>
	
	<xsl:variable name="i18n.AdvancedSettings">Avancerade inst�llningar</xsl:variable>
	<xsl:variable name="i18n.setAsAttribute">Spara f�ltets v�rde som attribut</xsl:variable>
	<xsl:variable name="i18n.attributeName">Attributnamn</xsl:variable>
	
	<xsl:variable name="i18n.attributeDescription">F�ljande attribut kommer att sparas med det ovan valda prefixet. Anv�ndningsexempel om du valt prefixet 'test': $attribute{test.address}</xsl:variable>
	<xsl:variable name="i18n.attributes.name">Attributnamn</xsl:variable>
	<xsl:variable name="i18n.attributes.value">V�rde</xsl:variable>
	
	<xsl:variable name="i18n.attributes.legacy">Fastighetsbeteckning eller adress om valt som resultat ovan</xsl:variable>
	<xsl:variable name="i18n.attributes.address">Adress</xsl:variable>
	<xsl:variable name="i18n.attributes.propertyUnitDesignation">Fastighetsbeteckning</xsl:variable>
	<xsl:variable name="i18n.attributes.propertyUnitDesignationNoMunicipality">Fastighetsbeteckning utan kommunnamn</xsl:variable>
	<xsl:variable name="i18n.attributes.propertyObjectIdentity">Fastighetens objektsidentitet</xsl:variable>
	<xsl:variable name="i18n.attributes.propertyUnitNumber">Fastighetsnyckel</xsl:variable>
	
</xsl:stylesheet>
