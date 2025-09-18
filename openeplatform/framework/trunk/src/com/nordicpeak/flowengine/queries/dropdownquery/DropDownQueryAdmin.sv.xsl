<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:include href="DropDownQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Rullistafr�ga</xsl:variable>
	<xsl:variable name="java.queryDescription">
Fr�getypen Rullistafr�ga anv�nds n�r anv�ndaren skall kunna v�lja mellan olika alternativ. Anv�ndaren kan endast v�lja ett alternativ.
	</xsl:variable>
	
	<xsl:variable name="java.countText">Antal</xsl:variable>
	<xsl:variable name="java.alternativesText">Alternativ</xsl:variable>
	
	<xsl:variable name="i18n.ShortDescription">Kort beskrivning</xsl:variable>
	<xsl:variable name="i18n.shortDescription">kort beskrivning</xsl:variable>
	<xsl:variable name="i18n.DropDownQueryNotFound">Den beg�rda fr�gan hittades inte!</xsl:variable>
	<xsl:variable name="i18n.lockOnOwnershipTransfer">L�s fr�ga vid �verl�telse</xsl:variable>
	<xsl:variable name="i18n.Searchable">G�r f�ltet s�kbart</xsl:variable>
	<xsl:variable name="i18n.FormatValidator">Validator</xsl:variable>
	<xsl:variable name="i18n.InvalidFormatMessage">Valideringsmeddelande</xsl:variable>
	<xsl:variable name="i18n.invalidFormatMessage">valideringsmeddelande</xsl:variable>
	
	<xsl:variable name="i18n.AdvancedSettings">Avancerade inst�llningar</xsl:variable>
	<xsl:variable name="i18n.setAsAttribute">Spara f�ltets v�rde som attribut</xsl:variable>
	<xsl:variable name="i18n.attributeName">Attributnamn</xsl:variable>	
	
</xsl:stylesheet>
