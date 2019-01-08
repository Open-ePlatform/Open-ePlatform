<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryCommon.sv.xsl"/>
	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/basemapquery/BaseMapQueryCommon.sv.xsl"/>
	<xsl:include href="PUDMapQueryTemplates.xsl"/>
	
	<xsl:variable name="i18n.CantContactMapServer">Kartan kunde inte laddas. Ingen kontakt med kartservern, kontakta administratören</xsl:variable>
	
	<xsl:variable name="i18n.PropertyUnitDesignation">Fastighetsbeteckning</xsl:variable>
	<xsl:variable name="i18n.Coordinates">Koordinater</xsl:variable>

	<xsl:variable name="i18n.CoordinatesNotValid">Koordinaterna för punkten ligger inte inom den angivna fastigheten. Försök igen.</xsl:variable>
	<xsl:variable name="i18n.RetrievingPUD">Hämtar fastighet...</xsl:variable>
	<xsl:variable name="i18n.ZoomScaleMessage">Du måste zooma i kartan för att kunna sätta ut punkten med tillräcklig precision.</xsl:variable>
	<xsl:variable name="i18n.ZoomScaleButton">Ta mig till rätt zoomnivå</xsl:variable>
	
	<xsl:variable name="i18n.SearchToolDescription">Sök via fastighet, adress eller ort</xsl:variable>
	
	<xsl:variable name="i18n.UnknownValidationError">Ett okänt valideringsfel har uppstått.</xsl:variable>
	<xsl:variable name="i18n.UnkownErrorMessageTitle">Oväntat fel</xsl:variable>
	<xsl:variable name="i18n.UnkownErrorMessage">Ett oväntat fel inträffade när fastigheten skulle hämtas. Kontakta administratören.</xsl:variable>
	<xsl:variable name="i18n.NoPUDFoundMessageTitle">Kan inte hitta någon fastighet</xsl:variable>
	<xsl:variable name="i18n.NoPUDFoundMessage">Det går inte att hitta någon fastighet på den angivna positionen.</xsl:variable>
	
	<xsl:variable name="i18n.Validation.ErrorLookingUpEstateID">Det gick inte att översätta fastighetsbeteckningen till fastighets ID, vänligen försök igen senare.</xsl:variable>
	
</xsl:stylesheet>
